package tools;

import com.alibaba.fastjson.JSONObject;
import org.testng.annotations.Test;
import utils.LogUtils;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class RiskAuditRequest {

    private static JSONObject executeHttpRequest(String urlStr, String jsonInputString, String brand) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if (brand.equalsIgnoreCase("vfx")) {
            brand = "AU";
        }
        if (brand.equalsIgnoreCase("mo")){
            brand = "MONETA";
        }
        // 设置请求方法和头部信息
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("tag", brand);
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        // 打印所有请求头信息
        LogUtils.info("Request Headers:");
        Map<String, List<String>> requestProperties = connection.getRequestProperties();
        for (Map.Entry<String, List<String>> entry : requestProperties.entrySet()) {
            String key = entry.getKey();
            if (key != null) {
                for (String value : entry.getValue()) {
                    LogUtils.info(key + ": " + value);
                }
            }
        }

        LogUtils.info(jsonInputString);
        // 发送请求体
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // 获取响应码
        int responseCode = connection.getResponseCode();
        LogUtils.info("Response Code: " + responseCode);

        // 读取并打印响应内容
        StringBuilder response = null;
        try (Scanner scanner = new Scanner(connection.getInputStream(), "utf-8")) {
            response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            LogUtils.info("Response Body: " + response.toString());
        } catch (Exception e) {
            // 如果响应码表示错误，可能需要从错误流读取
            try (Scanner scanner = new Scanner(connection.getErrorStream(), "utf-8")) {
                StringBuilder errorResponse = new StringBuilder();
                while (scanner.hasNext()) {
                    errorResponse.append(scanner.nextLine());
                }
                LogUtils.info("Error Response Body: " + errorResponse.toString());
                return JSONObject.parseObject(errorResponse.toString());
            }
        }
        return JSONObject.parseObject(response.toString());
    }
    /** 大额出金risk audit审核
     * 发送Risk Audit请求
     *
     * @param callBackData 回调数据
     * @return 响应内容
     * @throws Exception
     */
    private static String buildStandardRiskAuditJson(RiskAuditCallBack callBackData) {
        String messageId = UUID.randomUUID().toString();
        String timestamp = Instant.now().toString();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("messageId", messageId);
        jsonObject.put("timestamp", timestamp);
        jsonObject.put("transferId", callBackData.getTransferId());
        jsonObject.put("brand", callBackData.getBrand());
        jsonObject.put("regulator", callBackData.getRegulator());
        jsonObject.put("internalReason", "");
        jsonObject.put("status", "Approve");
        jsonObject.put("orderNumber", callBackData.getOrderNum());
        jsonObject.put("checkName", "Big_Amount");

        return jsonObject.toJSONString();
    }

    /** AU 2.0出金SRC risk audit审核
     * 发送Risk Audit请求
     *
     * @param callBackData 回调数据
     * @return 响应内容
     * @throws Exception
     */
    private static String buildSRCRiskAuditJson(RiskAuditCallBack callBackData) {
        String requestId = UUID.randomUUID().toString();
        String brand = callBackData.getBrand();
        String regulator = callBackData.getRegulator();
        String srcAppOrderNo = "APP_ORDER_00000001000001493021";
        String srcAppId =brand + "BRC041001PWM000000001";
        String businessOrderId =callBackData.getOrderNum();
        String userId = callBackData.getUserId();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("schemaVersion", "2.0");
        jsonObject.put("requestId", requestId);
        jsonObject.put("brand", brand);
        jsonObject.put("regulator",regulator);
        jsonObject.put("internalReason", "");
        jsonObject.put("status", "Approve");
        jsonObject.put("businessOrderId", businessOrderId);
        jsonObject.put("srcAppId", srcAppId);
        jsonObject.put("srcAppId", srcAppId);
        jsonObject.put("srcAppOrderNo", srcAppOrderNo);
        jsonObject.put("userId", userId);
        jsonObject.put("rejectionReasonCode", "");
        jsonObject.put("rejectionReason", "");

        return jsonObject.toJSONString();
    }


    public static JSONObject sendRiskAuditRequest(RiskAuditCallBack callBackData, String brand) throws Exception {
        String jsonInputString = buildStandardRiskAuditJson(callBackData);
        return executeHttpRequest(callBackData.getUrl(), jsonInputString, brand);
    }

    public static JSONObject sendSRCRiskAuditRequest(RiskAuditCallBack callBackData, String brand) throws Exception {
        String jsonInputString = buildSRCRiskAuditJson(callBackData);
        return executeHttpRequest(callBackData.getUrl(), jsonInputString, brand);
    }
    /**
     * 测试V2大额出金risk audit审核
     */
    @Test
    public void testV2RiskAudit() {
        RiskAuditCallBack callBackData = new RiskAuditCallBack(
                "Alpha",
                "core-vt",
                "VT",
                "SVG",
                "1414501",
                "VTSG784049120260204130343"
        );
        try {
            sendRiskAuditRequest(callBackData, "VT");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 测试AU 3.0出金SRC risk audit审核
     */
    @Test
    public void testSRCRiskAudit() {
        try {
            RiskAuditCallBack callBackDataAU = new RiskAuditCallBack(
                    "Alpha",
                    "VJP",
                    "SVG",
                    "2670986",
                    "VJP1477002320260204122033"
            );
            sendSRCRiskAuditRequest(callBackDataAU, "VJP");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
