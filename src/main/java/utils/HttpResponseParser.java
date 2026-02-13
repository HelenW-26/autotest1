package utils;

import org.apache.http.HttpResponse;

import java.util.*;
import java.util.regex.*;

public class HttpResponseParser {
    /**
     * 从HTTP响应头中获取指定键的值
     * @param response HttpResponse对象
     * @param headerName 要获取的响应头名称
     * @return 响应头的值，如果不存在则返回null
     */
    public static String getResponseHeaderValue(HttpResponse response, String headerName) {
        try {
            org.apache.http.Header[] headers = response.getHeaders(headerName);
            if (headers != null && headers.length > 0) {
                return headers[0].getValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析HTTP响应头字符串为键值对
     * @param headerString 原始HTTP响应头字符串
     * @return 包含所有键值对的Map
     */
    public static Map<String, List<String>> parseHttpResponseHeaders(String headerString) {
        Map<String, List<String>> headers = new LinkedHashMap<>();

        // 移除方括号并按逗号分割（注意处理引号内的逗号）
        String cleanHeaders = headerString.replaceAll("^\\[|\\]$", "");

        // 使用正则表达式匹配键值对
        Pattern pattern = Pattern.compile("([^:]+):\\s*(.*?)(?=,\\s*[A-Za-z-]+:|$)");
        Matcher matcher = pattern.matcher(cleanHeaders);

        while (matcher.find()) {
            String key = matcher.group(1).trim();
            String value = matcher.group(2).trim();

            // 处理重复的header键
            headers.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
        }

        return headers;
    }

    /**
     * 更精确的解析方法
     * @param headerString 原始HTTP响应头字符串
     * @return 包含所有键值对的Map
     */
    public static Map<String, List<String>> parseHeadersAccurately(String headerString) {
        Map<String, List<String>> headers = new LinkedHashMap<>();

        // 清理输入字符串
        String input = headerString.trim();
        if (input.startsWith("[") && input.endsWith("]")) {
            input = input.substring(1, input.length() - 1);
        }

        // 分割键值对
        String[] pairs = input.split(",(?=[A-Za-z-]+:)");

        for (String pair : pairs) {
            int colonIndex = pair.indexOf(":");
            if (colonIndex > 0) {
                String key = pair.substring(0, colonIndex).trim();
                String value = pair.substring(colonIndex + 1).trim();

                // 添加到map中，支持多值header
                headers.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
            }
        }

        return headers;
    }

    public static void main(String[] args) {
        String httpResponse = "[Date: Wed, 29 Oct 2025 06:59:06 GMT, Content-Type: application/json;charset=UTF-8, " +
                "Transfer-Encoding: chunked, Connection: keep-alive, CF-RAY: 9960c9bedde2ddc5-HKG, " +
                "vary: Origin, vary: Access-Control-Request-Method, vary: Access-Control-Request-Headers, " +
                "vary: Accept-Encoding, x-oneagent-js-injection: true, access-control-allow-credentials: true, " +
                "x-tool-session-internal-id: 960db40f-660e-44c3-b7d1-6bb79828967e, " +
                "x-tool-session-id: 57bc661b4f6857aecf0545269c7f7b0f0029a8ad4dba13777848285557a9216d, " +
                "Host: sso.bit-eks.crm-alpha.com, Cache-Control: no-cache, must-revalidate, proxy-revalidate, max-age=0, " +
                "Cache-Control: no-cache, must-revalidate, proxy-revalidate, max-age=0, " +
                "eventid: cf26aadc9e8f25440ebb83480a560fb28bc, " +
                "request-id: c67ddb49c4cbac174cb7e293c8c74b55, " +
                "x-real-ip: 16.163.116.20, " +
                "x-forwarded-for: 16.163.116.20, 172.71.215.128, 16.163.116.20, " +
                "server-timing: dtSInfo;desc=\"0\", server-timing: dtRpid;desc=\"-567026016\", " +
                "cf-cache-status: DYNAMIC, Server: cloudflare]";

        Map<String, List<String>> parsedHeaders = parseHeadersAccurately(httpResponse);

        // 打印结果
        for (Map.Entry<String, List<String>> entry : parsedHeaders.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();

            for (String value : values) {
                System.out.println(key + ": " + value);
            }
        }
    }
}
