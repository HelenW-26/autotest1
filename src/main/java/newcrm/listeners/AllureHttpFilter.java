package newcrm.listeners;

import io.qameta.allure.Allure;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.apache.http.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AllureHttpFilter implements Filter {
    // allure报告过滤器，拦截所有请求和响应，用于生成Allure报告
    public static class RequestInterceptor implements HttpRequestInterceptor {
        @Override
        public void process(HttpRequest request, HttpContext context) throws HttpException {
            try {
                // 获取完整URL
                HttpHost target = (HttpHost) context.getAttribute("http.target_host");
                String fullUrl = (target != null? target.toURI() : "") + request.getRequestLine().getUri();

                // 存储基本信息，供响应拦截器使用
                context.setAttribute("full_url", fullUrl);
                context.setAttribute("request_method", request.getRequestLine().getMethod());

                // 处理请求头
                Map<String, String> headersMap = new HashMap<>();
                for (Header header : request.getAllHeaders()) {
                    headersMap.put(header.getName(), header.getValue());
                }
                context.setAttribute("headers", new JSONObject(headersMap).toString(2));

                // 处理请求体（如果存在）
                if (request instanceof HttpEntityEnclosingRequest) {
                    HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
                    if (entity != null) {
                        String requestBody = EntityUtils.toString(entity);
                        // 恢复实体以便后续处理
                        ((HttpEntityEnclosingRequest) request).setEntity(
                                new StringEntity(requestBody, StandardCharsets.UTF_8));
                        context.setAttribute("request_body", requestBody);
                    }
                }
            } catch (Exception e) {
                logErrorToAllure(e);
                throw new HttpException("处理Allure报告的请求时出错", e);
            }
        }
    }

    public static class ResponseInterceptor implements HttpResponseInterceptor {
        @Override
        public void process(HttpResponse response, HttpContext context) throws HttpException {
            try {
                // 获取存储的请求信息
                String fullUrl = (String) context.getAttribute("full_url");
                String requestMethod = (String) context.getAttribute("request_method");
                String headersJson = (String) context.getAttribute("headers");
                String requestBody = (String) context.getAttribute("request_body");

                // 处理响应
                String responseBody = EntityUtils.toString(response.getEntity());
                // 恢复实体以便后续处理
                response.setEntity(new StringEntity(responseBody, StandardCharsets.UTF_8));

                // 准备调试信息
                String debugInfo = "调用时间: " + new Date() + "\n" +
                        "请求方法: " + requestMethod + "\n" +
                        "URL: " + fullUrl + "\n" +
                        "状态码: " + response.getStatusLine().getStatusCode();

                // 创建Allure步骤，包含单独的附件
                Allure.step(fullUrl + " API调用 - ", () -> {
                    // 请求头
                    Allure.addAttachment("请求头", "application/json", headersJson);

                    // 请求体
                    Allure.addAttachment("请求体", "text/plain",
                            requestBody != null? requestBody : "");

                    // 响应内容
                    Allure.addAttachment("响应内容", "application/json", responseBody);

                    // 调试信息
                    Allure.addAttachment("调试信息", "text/plain", debugInfo);
                });
            } catch (Exception e) {
                logErrorToAllure(e);
                throw new HttpException("处理Allure报告的响应时出错", e);
            }
        }
    }

    private static void logErrorToAllure(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        Allure.step("Allure报告错误", () -> {
            Allure.addAttachment("错误详情", "text/plain",
                    "生成Allure报告时发生错误:\n" + sw.toString());
        });
    }

    @Override
    public io.restassured.response.Response filter(FilterableRequestSpecification requestSpec,
                                                   FilterableResponseSpecification responseSpec,
                                                   FilterContext filterContext) {

        return filterContext.next(requestSpec, responseSpec);
    }
}