package tools;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JacocoTrigger {

    private static Config appConfig;
    private static final HttpClient client = createInsecureHttpClient();
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // RESULT_DIR 不需要为兼容linux修改，因为是相对路径
    private static final String RESULT_DIR = "JacocoResults";

    public static void main(String[] args) {
        try {
            String configPath = (args.length > 0) ? args[0] : "jacoco_config.xml";
            // 为兼容linux路径修改，打印时统一显示路径
            System.out.println(">>> Loading configuration file: " + new File(configPath).getAbsolutePath());

            appConfig = loadConfigFromXml(configPath);
            overrideConfigWithSystemProperties();

            System.out.println(">>> Configuration loaded successfully!");
            System.out.println("    Target Version: " + appConfig.targetVersion);
            System.out.println("    Brand: " + appConfig.brand);
            System.out.println("    Time Range: " + appConfig.startTime + " ~ " + appConfig.endTime);

            File resultDir = new File(RESULT_DIR);
            if (!resultDir.exists()) {
                resultDir.mkdirs();
                System.out.println(">>> Output directory created: " + resultDir.getAbsolutePath());
            }

            System.out.println("\n>>> [Step 1] Fetching deployment list...");
            List<ServiceInfo> jacocoServices = step1_GetAndSaveServices();

            if (jacocoServices.isEmpty()) {
                System.out.println("No matching services found. Program exiting.");
                return;
            }

            System.out.println("\n>>> [Step 2] Triggering Jacoco tasks and generating report...");
            step2_RunJacocoAudit(jacocoServices);

            System.out.println(">>> All steps completed. Exiting system.");
            System.exit(0);

        } catch (Exception e) {
            System.err.println("\n Critical Error, program terminated: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static Config loadConfigFromXml(String xmlPath) throws Exception {
        // 为兼容linux路径修改，使用 File 对象读取，自动处理 separators
        File xmlFile = new File(xmlPath);
        if (!xmlFile.exists()) throw new RuntimeException("Configuration file not found: " + xmlFile.getAbsolutePath());

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        Config config = new Config();
        Element root = doc.getDocumentElement();

        config.jenkinsUrl = getTagValue(root, "jenkinsUrl");
        config.deployJobPath = getTagValue(root, "deployJobPath");
        config.jacocoJobPath = getTagValue(root, "jacocoJobPath");
        config.username = getTagValue(root, "username");
        config.token = getTagValue(root, "token");
        config.startTime = getTagValue(root, "startTime");
        config.endTime = getTagValue(root, "endTime");
        config.targetVersion = getTagValue(root, "targetVersion");
        config.brand = getTagValue(root, "brand");
        config.filterKeywords = getTagValue(root, "filterKeywords");
        config.mappingFilePath = getTagValue(root, "mappingFilePath");

        String teamOrderStr = getTagValue(root, "teamOrder");
        config.teamSortWeights = new HashMap<>();
        if (teamOrderStr != null && !teamOrderStr.isEmpty()) {
            String[] teams = teamOrderStr.split(",");
            for (int i = 0; i < teams.length; i++) {
                config.teamSortWeights.put(teams[i].trim(), i);
            }
        }
        return config;
    }

    private static void overrideConfigWithSystemProperties() {
        if (System.getProperty("startTime") != null) appConfig.startTime = System.getProperty("startTime");
        if (System.getProperty("endTime") != null) appConfig.endTime = System.getProperty("endTime");
        if (System.getProperty("targetVersion") != null) appConfig.targetVersion = System.getProperty("targetVersion");
        if (System.getProperty("brand") != null) appConfig.brand = System.getProperty("brand");
    }

    private static String getTagValue(Element element, String tagName) {
        if (element.getElementsByTagName(tagName).getLength() > 0) {
            return element.getElementsByTagName(tagName).item(0).getTextContent().trim();
        }
        return "";
    }

    static class Config {
        String jenkinsUrl, deployJobPath, jacocoJobPath, username, token;
        String startTime, endTime, targetVersion, brand, mappingFilePath, filterKeywords;
        Map<String, Integer> teamSortWeights;
    }

    private static List<ServiceInfo> step1_GetAndSaveServices() throws Exception {
        long startTs = parseToTimestamp(appConfig.startTime);
        long endTs = parseToTimestamp(appConfig.endTime);
        List<String> excludeList = Arrays.stream(appConfig.filterKeywords.split(","))
                .map(String::trim).map(String::toLowerCase).collect(Collectors.toList());

        String apiUrl = String.format("%s/%s/api/json?tree=builds%%5Bnumber,url,timestamp,result,actions%%5Bparameters%%5Bname,value%%5D%%5D%%5D%%7B0,150%%7D",
                appConfig.jenkinsUrl, appConfig.deployJobPath);

        // Debug Json URL
//        System.out.println("DEBUG Request URL: " + apiUrl); // 1. 打印请求的完整 URL
//        String jsonResponse = sendGetRequest(apiUrl);
//        System.out.println("DEBUG Response: " + jsonResponse); // 2. 打印 Jenkins 返回的实际内容

        String jsonResponse = sendGetRequest(apiUrl);
        JSONObject root = new JSONObject(jsonResponse);
        JSONArray builds = root.optJSONArray("builds");
        if (builds == null) return new ArrayList<>();

        Map<String, ServiceInfo> latestServicesMap = new HashMap<>();
        System.out.println("Analyzing " + builds.length() + " build records...");

        for (int i = 0; i < builds.length(); i++) {
            JSONObject build = builds.getJSONObject(i);
            long ts = build.getLong("timestamp");
            String result = build.optString("result");

            if (ts < startTs || ts > endTs || !"SUCCESS".equals(result)) continue;

            Map<String, String> params = extractParameters(build);
            if (!appConfig.targetVersion.equals(params.get("git_version"))) continue;
            String servicesRaw = params.get(appConfig.brand);
            if (servicesRaw == null || servicesRaw.isEmpty()) continue;

            String[] serviceList = servicesRaw.split(",");
            String buildTimeStr = formatTimestamp(ts);

            for (String svc : serviceList) {
                svc = svc.trim();
                if (svc.isEmpty()) continue;
                if (!latestServicesMap.containsKey(svc)) {
                    latestServicesMap.put(svc, new ServiceInfo(svc, appConfig.targetVersion, appConfig.brand, build.getString("url"), buildTimeStr));
                }
            }
        }

        List<ServiceInfo> allList = new ArrayList<>(latestServicesMap.values());
        allList.sort(Comparator.comparing(s -> s.serviceName));

        // 为兼容linux路径修改，使用 File 构造函数来生成路径
        String allFileName = new File(RESULT_DIR, String.format("jenkins_deploy_%s_all.csv", appConfig.brand)).getPath();
        saveToCsv(allFileName, allList);
        System.out.println("Saved full list: " + allFileName + " (Total: " + allList.size() + ")");

        List<ServiceInfo> jacocoList = allList.stream()
                .filter(info -> excludeList.stream().noneMatch(k -> info.serviceName.toLowerCase().contains(k)))
                .collect(Collectors.toList());

        // 为兼容linux路径修改
        String forJacocoFileName = new File(RESULT_DIR, String.format("jenkins_deploy_%s_forJacoco.csv", appConfig.brand)).getPath();
        saveToCsv(forJacocoFileName, jacocoList);
        System.out.println("Saved filtered list: " + forJacocoFileName + " (Total: " + jacocoList.size() + ")");

        return jacocoList;
    }

    private static void step2_RunJacocoAudit(List<ServiceInfo> services) throws Exception {
        System.out.println("Loading mapping file: " + appConfig.mappingFilePath);
        Map<String, MappingData> mapping = loadMapping(appConfig.mappingFilePath);

        List<JacocoTask> tasks = new ArrayList<>();
        for (ServiceInfo svc : services) {
            tasks.add(new JacocoTask(svc));
        }

        System.out.println("\n====== Starting Batch 1 Trigger (" + tasks.size() + " tasks) ======");
        triggerBatch(tasks);
        processTasksAndWait(tasks);

        List<JacocoTask> failedTasks = tasks.stream()
                .filter(t -> "FAILURE".equals(t.status) || "TRIGGER_FAILED".equals(t.status))
                .collect(Collectors.toList());

        if (!failedTasks.isEmpty()) {
            System.out.println("\n======  Found " + failedTasks.size() + " failed tasks. Preparing to Retry ======");
            for (JacocoTask t : failedTasks) {
                t.resetForRetry();
            }
            triggerBatch(failedTasks);
            processTasksAndWait(failedTasks);
        } else {
            System.out.println("\n====== All tasks passed in Batch 1. No retry needed. ======");
        }

        fetchMetricsForSuccessTasks(tasks);

        System.out.println("Sorting by configured Team order...");
        tasks.sort((t1, t2) -> {
            String team1 = mapping.getOrDefault(t1.serviceInfo.serviceName.toLowerCase(), new MappingData("Unknown", "")).team;
            String team2 = mapping.getOrDefault(t2.serviceInfo.serviceName.toLowerCase(), new MappingData("Unknown", "")).team;
            int w1 = appConfig.teamSortWeights.getOrDefault(team1, 999);
            int w2 = appConfig.teamSortWeights.getOrDefault(team2, 999);
            return Integer.compare(w1, w2);
        });

        // 为兼容linux路径修改，输出路径兼容
        String outputCsvName = new File(RESULT_DIR, "jacoco_merged_report.csv").getPath();
        writeFinalCsv(outputCsvName, tasks, mapping);
        System.out.println("✅ Report generated: " + outputCsvName);
        HytestSendBody body = new HytestSendBody();
        body.setBrand(appConfig.brand);
        body.jacocoList = tasks.stream()
                .map(t -> buildJacocoReportData(t, mapping))
                .collect(Collectors.toList());
        HttpResponse<String> resp = sendPostRequestToHytest(body);
        System.out.println("Response status: " + resp.statusCode());
        System.out.println("Response headers: " + resp.headers().map());
        System.out.println("Response body: '" + resp.body() + "'");
    }

    private static void triggerBatch(List<JacocoTask> batchTasks) throws Exception {
        // 为兼容linux路径修改， URL 的路径必须是 "/"，千万不要变成 "\"，因为 HTTP 协议规定是 "/"
        // 这里不做 File.separator 修改，因为这是 URL
        String triggerBaseUrl = String.format("%s/%s/buildWithParameters", appConfig.jenkinsUrl, appConfig.jacocoJobPath);

        for (int i = 0; i < batchTasks.size(); i++) {
            JacocoTask task = batchTasks.get(i);
            System.out.print(String.format("[%d/%d] Triggering: %s ... ", (i + 1), batchTasks.size(), task.serviceInfo.serviceName));

            Map<String, String> formData = new HashMap<>();
            formData.put("module", task.serviceInfo.serviceName);
            formData.put("branch", task.serviceInfo.gitVersion);
            formData.put("brand", task.serviceInfo.brand);
            formData.put("eks", "uat-nv");

            boolean triggered = false;
            for (int attempt = 1; attempt <= 3; attempt++) {
                try {
                    HttpResponse<String> resp = sendPostRequest(triggerBaseUrl, formData);
                    if (resp.statusCode() == 201) {
                        Optional<String> loc = resp.headers().firstValue("Location");
                        if (loc.isPresent()) {
                            task.queueUrl = loc.get() + "api/json";
                            triggered = true;
                            System.out.println("[SUCCESS]");
                            break;
                        }
                    } else { System.out.print("(HTTP " + resp.statusCode() + ") "); }
                } catch (Exception e) { System.out.print("(Error: " + e.getMessage() + ") "); }
                if (attempt < 3) TimeUnit.SECONDS.sleep(1);
            }
            if (!triggered) {
                System.out.println("[FAILED]");
                task.status = "TRIGGER_FAILED";
            }

            if (i < batchTasks.size() - 1) {
                TimeUnit.SECONDS.sleep(2);
            }
        }
    }

    private static void processTasksAndWait(List<JacocoTask> batchTasks) throws Exception {
        System.out.println("\nWaiting for queue to assign build numbers...");
        boolean anyPending = true;
        List<JacocoTask> activeTasks = batchTasks.stream()
                .filter(t -> t.queueUrl != null && !"TRIGGER_FAILED".equals(t.status))
                .collect(Collectors.toList());

        while (anyPending) {
            anyPending = false;
            for (JacocoTask task : activeTasks) {
                if (task.buildUrl == null && task.queueUrl != null) {
                    anyPending = true;
                    try {
                        String qJson = sendGetRequest(task.queueUrl);
                        JSONObject qObj = new JSONObject(qJson);
                        if (qObj.has("executable")) {
                            task.buildUrl = qObj.getJSONObject("executable").getString("url");
                        }
                    } catch (Exception ignored) {}
                }
            }
            if (anyPending) TimeUnit.SECONDS.sleep(2);
        }

        System.out.println("\nTasks running. Polling every 10 seconds...");
        boolean anyRunning = true;
        while (anyRunning) {
            anyRunning = false;
            for (JacocoTask task : activeTasks) {
                if ("PENDING".equals(task.status) && task.buildUrl != null) {
                    try {
                        String bJson = sendGetRequest(task.buildUrl + "api/json");
                        JSONObject bObj = new JSONObject(bJson);
                        boolean building = bObj.optBoolean("building", false);
                        if (!building) {
                            task.status = bObj.optString("result", "UNKNOWN");
                            task.finishTime = formatTimestamp(bObj.optLong("timestamp"));
                            System.out.println("   " + task.serviceInfo.serviceName + " -> " + task.status);
                        } else {
                            anyRunning = true;
                        }
                    } catch (Exception e) {
                        anyRunning = true;
                    }
                }
            }
            if (anyRunning) TimeUnit.SECONDS.sleep(10);
        }
    }

    private static void fetchMetricsForSuccessTasks(List<JacocoTask> tasks) {
        System.out.println("\nFetching HTML reports...");
        for (JacocoTask task : tasks) {
            if ("SUCCESS".equals(task.status) && task.buildUrl != null) {
                task.metrics = fetchJacocoMetrics(task.serviceInfo.serviceName, task.buildUrl);
            }
        }
    }

    private static Map<String, MappingData> loadMapping(String file) {
        // [兼容性修改] 使用 File.exists() 检查路径，它自动兼容 Win/Linux
        if (!new File(file).exists()) throw new RuntimeException("Mapping file not found: " + new File(file).getAbsolutePath());

        Map<String, MappingData> map = new HashMap<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(file), StandardCharsets.UTF_8)) {
            String line;
            boolean firstLine = true;
            int moduleIdx = 0, domainIdx = 1, teamIdx = 3;

            while ((line = br.readLine()) != null) {
                if (firstLine && line.startsWith("\ufeff")) line = line.substring(1);
                String[] parts = line.split(",");
                if (firstLine) {
                    boolean headerFound = false;
                    for (int i = 0; i < parts.length; i++) {
                        String header = parts[i].trim().toLowerCase();
                        if (header.contains("module") || header.contains("service")) { moduleIdx = i; headerFound = true; }
                        else if (header.equals("team")) { teamIdx = i; headerFound = true; }
                        else if (header.contains("domain")) { domainIdx = i; headerFound = true; }
                    }
                    if (headerFound) {
                        System.out.println("   -> [Smart Detect] Header parsed: Service[" + moduleIdx + "], Domain[" + domainIdx + "], Team[" + teamIdx + "]");
                        firstLine = false;
                        continue;
                    } else {
                        System.out.println("   -> [Info] No standard header detected. Using default index: module(0), domain(1), team(3)");
                    }
                    firstLine = false;
                }
                int maxIdx = Math.max(moduleIdx, Math.max(teamIdx, domainIdx));
                if (parts.length <= maxIdx) continue;
                map.put(parts[moduleIdx].trim().toLowerCase(), new MappingData(parts[teamIdx].trim(), parts[domainIdx].trim()));
            }
        } catch (IOException e) { throw new RuntimeException(e); }
        return map;
    }

    private static HttpClient createInsecureHttpClient() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {}
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {}
                public java.security.cert.X509Certificate[] getAcceptedIssuers() { return new java.security.cert.X509Certificate[0]; }
            }}, new SecureRandom());
            return HttpClient.newBuilder().sslContext(sslContext).build();
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    private static String sendGetRequest(String url) throws Exception {
        String auth = Base64.getEncoder().encodeToString((appConfig.username + ":" + appConfig.token).getBytes());
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Authorization", "Basic " + auth).GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    private static HttpResponse<String> sendPostRequest(String url, Map<String, String> formData) throws Exception {
        String auth = Base64.getEncoder().encodeToString((appConfig.username + ":" + appConfig.token).getBytes());
        String form = formData.entrySet().stream().map(e -> e.getKey() + "=" + java.net.URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8)).collect(Collectors.joining("&"));
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Authorization", "Basic " + auth).header("Content-Type", "application/x-www-form-urlencoded").POST(HttpRequest.BodyPublishers.ofString(form)).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<String> sendPostRequestToHytest(HytestSendBody requestBody) throws IOException, InterruptedException {
        String url = "https://hytest.crm-alpha.com/all/report/jacoco/add";
        String requestBodyJsonStr = JSON.toJSONString(requestBody);
        System.out.println("request body :" + requestBodyJsonStr);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("AccessKey","Kd7E8ldo1cyOvey9")
                .header("signature","lrf8ibDWf6AZblcr")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBodyJsonStr))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static Map<String, String> extractParameters(JSONObject build) {
        Map<String, String> params = new HashMap<>();
        JSONArray actions = build.optJSONArray("actions");
        if (actions == null) return params;
        for (int i = 0; i < actions.length(); i++) {
            JSONObject action = actions.getJSONObject(i);
            JSONArray parameters = action.optJSONArray("parameters");
            if (parameters != null) {
                for (int j = 0; j < parameters.length(); j++) {
                    JSONObject p = parameters.getJSONObject(j);
                    params.put(p.optString("name"), String.valueOf(p.opt("value")));
                }
            }
        }
        return params;
    }

    private static void saveToCsv(String filename, List<ServiceInfo> data) throws IOException {
        try (FileWriter writer = new FileWriter(filename, StandardCharsets.UTF_8)) {
            writer.write("\ufeff");
            writer.write("service_name,git_version,brand,url,build_time\n");
            for (ServiceInfo info : data) {
                writer.write(String.format("%s,%s,%s,%s,%s\n", info.serviceName, info.gitVersion, info.brand, info.url, info.buildTime));
            }
        }
    }

    private static void writeFinalCsv(String filename, List<JacocoTask> tasks, Map<String, MappingData> mapping) throws IOException {
        try (FileWriter writer = new FileWriter(filename, StandardCharsets.UTF_8)) {
            writer.write("\ufeff");
            writer.write("Service Name,team,domain,brand,git_version,UAT Depoly URL,UAT Deploy Build Time," +
                    "Jacoco Jenkins Status,Jacoco Build URL,Jacoco Build Time," +
                    "Element,Missed Instructions,Instructions Cov%,Missed Branches,Branches Cov%," +
                    "Missed Cxty,Total Cxty,Missed Lines,Total Lines,Missed Methods,Total Methods," +
                    "Missed Classes,Total Classes\n");
            for (JacocoTask t : tasks) {
                ServiceInfo si = t.serviceInfo;
                MappingData mapData = mapping.getOrDefault(si.serviceName.toLowerCase(), new MappingData("Unknown", "Unknown"));
                StringBuilder sb = new StringBuilder();
                sb.append(escapeCsv(si.serviceName)).append(",");
                sb.append(escapeCsv(mapData.team)).append(",");
                sb.append(escapeCsv(mapData.domain)).append(",");
                sb.append(escapeCsv(si.brand)).append(",");
                sb.append(escapeCsv(si.gitVersion)).append(",");
                sb.append(escapeCsv(si.url)).append(",");
                sb.append(escapeCsv(si.buildTime)).append(",");
                sb.append(escapeCsv(t.status)).append(",");

                String reportLink = (t.buildUrl == null) ? "N/A" : t.buildUrl + "artifact/report/html/index.html";
                sb.append(escapeCsv(reportLink)).append(",");

                sb.append(escapeCsv(t.finishTime)).append(",");
                for (int i = 0; i < t.metrics.size(); i++) {
                    sb.append(escapeCsv(t.metrics.get(i)));
                    if (i < t.metrics.size() - 1) sb.append(",");
                }
                sb.append("\n");
                writer.write(sb.toString());
            }
        }
    }

    private static String escapeCsv(String val) { if (val == null) return ""; if (val.contains(",")) return "\"" + val + "\""; return val; }
    private static long parseToTimestamp(String dateStr) { return LocalDateTime.parse(dateStr, DATE_FMT).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(); }
    private static String formatTimestamp(long ts) { return Instant.ofEpochMilli(ts).atZone(ZoneId.systemDefault()).format(DATE_FMT); }

    private static List<String> fetchJacocoMetrics(String svcName, String buildUrl) {
        List<String> metrics = new ArrayList<>(Collections.nCopies(13, "N/A"));
        // URL 里的路径不需要改
        String reportUrl = buildUrl.replaceAll("/$", "") + "/artifact/report/html/index.html";
        try {
            String html = sendGetRequest(reportUrl);
            Pattern tfootPattern = Pattern.compile("<tfoot>(.*?)</tfoot>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher tfootMatcher = tfootPattern.matcher(html);
            String rowContent = null;
            if (tfootMatcher.find()) { rowContent = tfootMatcher.group(1); }
            else if (html.contains("Total")) {
                int totalIndex = html.indexOf("Total");
                int trStart = html.lastIndexOf("<tr", totalIndex);
                int trEnd = html.indexOf("</tr>", totalIndex);
                if (trStart != -1 && trEnd != -1) rowContent = html.substring(trStart, trEnd);
            }
            if (rowContent != null) {
                Pattern cellPattern = Pattern.compile("<t[dh][^>]*>(.*?)</t[dh]>", Pattern.CASE_INSENSITIVE);
                Matcher cellMatcher = cellPattern.matcher(rowContent);
                int count = 0;
                while (cellMatcher.find() && count < 13) {
                    metrics.set(count++, cellMatcher.group(1).replaceAll("<[^>]+>", "").trim());
                }
            }
        } catch (Exception e) { System.err.println(" Failed to fetch metrics: " + svcName + " - " + e.getMessage()); }
        return metrics;
    }
    private static JacocoReportData buildJacocoReportData(JacocoTask t, Map<String, MappingData> mapping) {
        ServiceInfo si = t.serviceInfo;
        MappingData mapData = mapping.getOrDefault(si.serviceName.toLowerCase(), new MappingData("Unknown", "Unknown"));
        String reportLink = (t.buildUrl == null) ? "N/A" : t.buildUrl + "artifact/report/html/index.html";
        JacocoReportData reportData = new JacocoReportData();
        reportData.setServiceName(si.serviceName);
        reportData.setTeam(mapData.team);
        reportData.setDomain(mapData.domain);
        reportData.setBrand(si.brand);
        reportData.setGitVersion(si.gitVersion);
        reportData.setUatDeployUrl(si.url);
        reportData.setUatDeployBuildTime(si.buildTime);
        reportData.setJacocoJenkinsStatus(t.status);
        reportData.setJacocoBuildUrl(reportLink);
        reportData.setJacocoBuildTime(t.finishTime);
        reportData.setElement(t.metrics.size() > 0 ? t.metrics.get(0).replaceAll(",", "") : "N/A");
        reportData.setMissedInstructions(t.metrics.size() > 1 ? t.metrics.get(1).replaceAll(",", "") : "N/A");
        reportData.setInstructionsCovPercent(t.metrics.size() > 2 ? removePercent(t.metrics.get(2)) : "N/A");
        reportData.setMissedBranches(t.metrics.size() > 3 ? t.metrics.get(3).replaceAll(",", "") : "N/A");
        reportData.setBranchesCovPercent(t.metrics.size() > 4 ? removePercent(t.metrics.get(4)) : "N/A");
        reportData.setMissedCxty(t.metrics.size() > 5 ? t.metrics.get(5).replaceAll(",", "") : "N/A");
        reportData.setTotalCxty(t.metrics.size() > 6 ? t.metrics.get(6).replaceAll(",", "") : "N/A");
        reportData.setMissedLines(t.metrics.size() > 7 ? t.metrics.get(7).replaceAll(",", "") : "N/A");
        reportData.setTotalLines(t.metrics.size() > 8 ? t.metrics.get(8).replaceAll(",", "") : "N/A");
        reportData.setMissedMethods(t.metrics.size() > 9 ? t.metrics.get(9).replaceAll(",", "") : "N/A");
        reportData.setTotalMethods(t.metrics.size() > 10 ? t.metrics.get(10).replaceAll(",", "") : "N/A");
        reportData.setMissedClasses(t.metrics.size() > 11 ? t.metrics.get(11).replaceAll(",", "") : "N/A");
        reportData.setTotalClasses(t.metrics.size() > 12 ? t.metrics.get(12).replaceAll(",", "") : "N/A");

        return reportData;
    }
    private static String removePercent(String s) {
        if (s == null || s.isEmpty()){
            return "N/A";
        }
        return s.replaceAll("%", "").replaceAll(",",""); }

    static class MappingData { String team, domain; public MappingData(String t, String d) { this.team = t; this.domain = d; } }
    static class ServiceInfo { String serviceName, gitVersion, brand, url, buildTime; public ServiceInfo(String s, String g, String b, String u, String bt) { this.serviceName = s; this.gitVersion = g; this.brand = b; this.url = u; this.buildTime = bt; } }
    static class JacocoTask {
        ServiceInfo serviceInfo;
        String queueUrl;
        String buildUrl;
        String status = "PENDING";
        String finishTime = "N/A";
        List<String> metrics = new ArrayList<>(Collections.nCopies(13, "N/A"));

        public JacocoTask(ServiceInfo info) { this.serviceInfo = info; }

        public void resetForRetry() {
            this.status = "PENDING";
            this.queueUrl = null;
            this.buildUrl = null;
            this.finishTime = "N/A";
        }
    }
    @Data
    static class HytestSendBody{
        List<JacocoReportData> jacocoList;
        String brand;
    }
    @Data
    static class JacocoReportData {
        String serviceName;
        String team;
        String domain;
        String brand;
        String gitVersion;
        String uatDeployUrl;
        String uatDeployBuildTime;
        String jacocoJenkinsStatus;
        String jacocoBuildUrl;
        String jacocoBuildTime;
        String element;
        String missedInstructions;
        String instructionsCovPercent;
        String missedBranches;
        String branchesCovPercent;
        String missedCxty;
        String totalCxty;
        String missedLines;
        String totalLines;
        String missedMethods;
        String totalMethods;
        String missedClasses;
        String totalClasses;
    }
}