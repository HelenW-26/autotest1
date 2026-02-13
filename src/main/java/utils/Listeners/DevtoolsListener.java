package utils.Listeners;

import newcrm.global.GlobalMethods;
import newcrm.testcases.BaseTestCase;
import newcrm.testcases.BaseTestCaseNew;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v114.emulation.Emulation;
import org.openqa.selenium.devtools.v114.network.Network;
import org.openqa.selenium.devtools.v114.network.model.RequestId;
import org.openqa.selenium.devtools.v114.network.model.Response;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class DevtoolsListener {
    private static final Logger logger = LogManager.getLogger(DevtoolsListener.class);
    public static DevTools devTools;
    private static boolean isFirstTime = true;

//    public static final AtomicReference<String> fileDownloadName = new AtomicReference<>("");
//    public static final AtomicReference<String> fileDownloadUrl = new AtomicReference<>("");

    public static void setUpDevTools(WebDriver driver) {
        setUpDevToolsWeb(driver,true);
    }

    public static void setUpDevToolsWeb(WebDriver driver,boolean isWeb) {

        //Check if devTools is already set
        if(devTools != null)
        {
            return;
        }
        devTools = ((ChromeDriver) driver).getDevTools();
        devTools.createSessionIfThereIsNotOne();
//        resetDownloadInfo();

        if(!isWeb)
        {// Set device metrics
            devTools.send(Emulation.setDeviceMetricsOverride(
                    375, // width
                    812, // height
                    100, // device scale factor
                    true, // mobile
                    Optional.empty(), // screen width
                    Optional.empty(), // screen height
                    Optional.empty(), // position x
                    Optional.empty(), // position y
                    Optional.empty(), // dont set
                    Optional.empty(), // dont set
                    Optional.empty(), // dont set
                    Optional.empty(), // dont set
                    Optional.empty() // dont set
                    // dont set
            ));
        }
        if(isFirstTime)
        {
            isFirstTime = false;
            String msg = "\n**********************************************************<br>"
                    + "********** AUTOMATION TEST EXECUTION START **********<br>"
                    + " <strong>TimeZone:</strong> " + TimeZone.getDefault().getDisplayName() + " <strong>StartTime</strong>: " + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))+ "<br>"
                    + "------------------------------------------------------------------------";
            logger.error(msg);
        }
        // Enable Network domain
        devTools.send(Network.enable(java.util.Optional.empty(), java.util.Optional.empty(), java.util.Optional.empty()));

        Map<String, Map<String, Object>> requestHeadersMap = new ConcurrentHashMap<>();
        //listener for request
        devTools.addListener(Network.requestWillBeSent(), event -> {
            String id = event.getRequestId().toString();
            Map<String, Object> headers = event.getRequest().getHeaders();

            requestHeadersMap.merge(id, headers, (oldHeaders, newHeaders) -> {
                oldHeaders.putAll(newHeaders);
                return oldHeaders;
            });
            //requestHeadersMap.forEach((key, value) -> logger.error("test1 reqHeaders: " + key + " = " + value));
        });
        // listener for requestWillBeSentExtraInfo
//        devTools.addListener(Network.requestWillBeSentExtraInfo(), event -> {
//            String id = event.getRequestId().toString();
//            Map<String, Object> headers = event.getHeaders(); // ExtraInfo 的 headers
//
//            requestHeadersMap.merge(id, headers, (oldHeaders, newHeaders) -> {
//                oldHeaders.putAll(newHeaders);
//                return oldHeaders;
//            });
//        });

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            //listener for response
            devTools.addListener(Network.responseReceived(), responseReceived -> {
                RequestId requestId = responseReceived.getRequestId();
                Response response = responseReceived.getResponse();
                String url = response.getUrl();

                //确保两个base都能正确初始化CPurl
                String CPURL = BaseTestCase.getCPURL();
                if (CPURL == null || CPURL.isBlank()){
                    CPURL = BaseTestCaseNew.getCPURL();
                }
                // Check if the response URL matches what you're interested in
                if ((url.contains("api") && url.contains(CPURL)) || url.contains("app")) {
                    try {
                        Network.GetResponseBodyResponse responseBody = devTools.send(Network.getResponseBody(requestId));
                        String resBody = responseBody.getBody().replace("<", "&lt").replace(">", "&gt");

                        Map<String, Object> reqHeaders = requestHeadersMap.get(requestId.toString());
                        if (reqHeaders == null) {
                            logger.warn("Headers map is null for Request ID: " + requestId);
                            // 可选：打印 requestHeadersMap.keySet()
                        }

                        String branchVersion = "N/A";
                        if (reqHeaders != null) {
                            Object bv = reqHeaders.get("branchversion");
                            if (bv == null) {
                                bv = reqHeaders.get("branchVersion");
                            }
                            if (bv == null) {
                                for (Map.Entry<String, Object> e : reqHeaders.entrySet()) {
                                    if ("branchversion".equalsIgnoreCase(e.getKey())) {
                                        bv = e.getValue();
                                        break;
                                    }
                                }
                            }
                            if (bv != null) {
                                branchVersion = bv.toString();
                            }
                        }
                        if (responseBody.getBody() != null) {
                            logger.info("Response Status: " + response.getStatus() + " <br> Branchversion: " + branchVersion + "<br> URL:" +  url + " <br> Crm-Event-Id:" + response.getHeaders().get("eventid") + "<br>     Response Body: " + resBody);
                        } else {
                            logger.info("No response body found for request ID: " + requestId);
                        }
                        //catch error like : {"code":500,"errmsg":"Server failed","data":null,"totalRecords":null,"msg":null,"extendString":null,"extendInteger":null}
                       // if (responseBody.getBody().matches(".*\"code\":(5[0-9]{2}|-32000).*") ) {
                        if (responseBody.getBody().matches(".*\"code\":(5[0-9]{2}|4[0-9]{2}|-32000).*") ) {

                            String alert = "The following api contains error code: " + "<br> Response Status: " + response.getStatus() + " <br>Crm-Event-Id:" + response.getHeaders().get("eventid")+ " <br> Branchversion: " + branchVersion+ " <br> URL: " + url
                                     + "<br> Response Body: " + responseBody.getBody() +" <br>";
                            logger.error(alert);
                            GlobalMethods.printDebugInfo(alert);
                            String screenshotBase64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
                            logger.error(" <br><img src=\"data:image/png;base64," + screenshotBase64 + "\" width=\"" + 1000 + "\" height=\"" + 600 + "\"/>");
                        }

                    } catch (org.openqa.selenium.devtools.DevToolsException e) {
                        logger.info("Error getting response body: " + e.getMessage() + response.getStatus() + "<br> URL: " + url + "<br>    RequestId: " + requestId);
                    } catch (Exception e) {
                        logger.info("final exception: " + e);
                    }
                }

                // Capture file download info
//                try {
//                    Map<String, Object> headers = response.getHeaders();
//                    String headersStr = headers.toString().toLowerCase();
//
//                    if (headersStr.contains("content-disposition")) {
//                        Object cdHeader = headers.get("content-disposition");
//                        if (cdHeader != null) {
//                            String disposition = cdHeader.toString();
//
//                            if (disposition.contains("filename=")) {
//                                // Extract filename safely
//                                String name = disposition.substring(disposition.indexOf("filename=") + 9)
//                                        .replace("\"", "")
//                                        .replace(";", "")
//                                        .trim();
//
//                                // Set atomic references
//                                fileDownloadName.set(name);
//                                fileDownloadUrl.set(url);
//
//                                logger.info("File download detected, Name: " + name + ", URL: " + url);
//                            } else {
//                                logger.info("Content-Disposition found, but no filename= part.");
//                            }
//                        } else {
//                            logger.info("Content-Disposition header present but value is null.");
//                        }
//                    }
//                } catch (Exception e) {
//                    logger.info("Exception while parsing Content-Disposition header: " + e);
//                }

            });
        });
        // Wait for the asynchronous handling to complete
        future.join();
        // Add a delay to ensure the request is complete
      /* try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

//    public static void resetDownloadInfo() {
//        fileDownloadName.set("");
//        fileDownloadUrl.set("");
//        GlobalMethods.printDebugInfo("Clear download info...");
//    }

//    public static boolean hasDownloadFile() {
//        return DevtoolsListener.fileDownloadName.get() != null && !DevtoolsListener.fileDownloadName.get().isEmpty();
//    }

}
