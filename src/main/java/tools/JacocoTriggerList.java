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

public class JacocoTriggerList {

    private static Config appConfig;
    private static final HttpClient client = createInsecureHttpClient();
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String RESULT_DIR = "JacocoResults";

    // 全局超时设置
    private static final int GLOBAL_TIMEOUT_MINUTES = 20;

    public static void main(String[] args) {
        try {
            String configPath = (args.length > 0) ? args[0] : "jacoco_config.xml";
            System.out.println(">>> Loading configuration: " + new File(configPath).getAbsolutePath());

            appConfig = loadConfigFromXml(configPath);
            overrideConfigWithSystemProperties();

            System.out.println(">>> Configuration loaded!");
            System.out.println("    Jenkins URL: " + appConfig.jenkinsUrl);
            System.out.println("    Brand Tag: " + appConfig.brand);

            File resultDir = new File(RESULT_DIR);
            if (!resultDir.exists()) resultDir.mkdirs();
            // [Step 0] 获取全局版本号 (Global Version)
            System.out.println("\n>>> [Step 0] Fetching Global Version from Git Job...");
            String globalVersion = fetchLatestGlobalVersion();

            // Step 1: 扫描 (30天/1000条 + Common参数支持)
            System.out.println("\n>>> [Step 1] Scanning deployment history (List-Based)...");
            List<ServiceInfo> targetServices = step1_ScanServicesBasedOnList();

            if (targetServices.isEmpty()) {
                System.out.println("No services found. Exiting.");
                return;
            }

            // Step 2: 触发 & 轮询 & 报告
            System.out.println("\n>>> [Step 2] Triggering Jacoco & Reporting...");
            // 将 globalVersion 传递给 Step 2
            step2_RunJacocoAudit(targetServices, globalVersion);

            System.out.println(">>> All steps completed. Exiting.");
            System.exit(0);

        } catch (Exception e) {
            System.err.println("\n Critical Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    // --- [新增] 获取全局版本号的方法 ---
    private static String fetchLatestGlobalVersion() throws Exception {
        if (appConfig.gitJobPath == null || appConfig.gitJobPath.isEmpty()) {
            System.out.println("No <gitJobPath> configured. Using 'Unknown' as global version.");
            return "Unknown";
        }
        // 获取 Git Job 的最近 50 次构建
        String apiUrl = String.format("%s/%s/api/json?tree=builds%%5Bnumber,url,timestamp,result,actions%%5Bparameters%%5Bname,value%%5D%%5D%%5D%%7B0,50%%7D",
                appConfig.jenkinsUrl, appConfig.gitJobPath);
        System.out.println("    ... Querying: " + apiUrl);
        String jsonResponse = sendGetRequest(apiUrl);
        JSONObject root = new JSONObject(jsonResponse);
        JSONArray builds = root.optJSONArray("builds");
        if (builds == null) return "Unknown";
        // 寻找最近一次 SUCCESS 的构建
        for (int i = 0; i < builds.length(); i++) {
            JSONObject build = builds.getJSONObject(i);
            if ("SUCCESS".equals(build.optString("result"))) {
                Map<String, String> params = extractParameters(build);
                String version = params.get(appConfig.gitJobParamName);
                if (version != null && !version.isEmpty()) {
                    System.out.println("    ✅ Found Global Version: " + version);
                    System.out.println("       -> Source Build: #" + build.optInt("number"));
                    System.out.println("       -> Build URL:    " + build.optString("url"));
                    return version;
                }
            }
        }
        System.out.println("    ⚠️ No SUCCESS build found with param '" + appConfig.gitJobParamName + "'");
        return "Unknown";
    }

    // --- Step 1: 扫描模块 ---

    private static List<ServiceInfo> step1_ScanServicesBasedOnList() throws Exception {
        Set<String> targetServices = loadServiceList(appConfig.serviceListPath);

        // 时间窗口 30 天
        long timeWindow = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000);

        // API 拉取数量 5000 条
        String treeParamRaw = "allBuilds[number,url,timestamp,result,actions[parameters[name,value]]]{0,5000}";
        String treeParamEncoded = treeParamRaw
                .replace("[", "%5B")
                .replace("]", "%5D")
                .replace("{", "%7B")
                .replace("}", "%7D");
        String apiUrl = String.format("%s/%s/api/json?tree=%s",
                appConfig.jenkinsUrl, appConfig.deployJobPath, treeParamEncoded);

        System.out.println("Fetching recent 5000 builds from Jenkins...");
        String jsonResponse = sendGetRequest(apiUrl);
        JSONObject root = new JSONObject(jsonResponse);
        JSONArray builds = root.optJSONArray("allBuilds");
        if (builds == null) builds = new JSONArray();

        Map<String, ServiceInfo> latestServicesMap = new HashMap<>();
        System.out.println("Analyzing " + builds.length() + " records (Window: 30 days)...");

        // 7品牌映射
        String jenkinsParamName = appConfig.brand;
        if (appConfig.brand != null) {
            switch (appConfig.brand.toLowerCase()) {
                case "pu": jenkinsParamName = "pug"; break;
                case "mo": jenkinsParamName = "moneta"; break;
                default: jenkinsParamName = appConfig.brand; break;
            }
        }
        System.out.println(">>> [Config] Target Brand: " + appConfig.brand + " | Looking for Jenkins Param: " + jenkinsParamName + " (+ common)");

        for (int i = 0; i < builds.length(); i++) {
            JSONObject build = builds.getJSONObject(i);
            long ts = build.getLong("timestamp");
            String result = build.optString("result");

            if (!"SUCCESS".equals(result)) continue;
            if (ts < timeWindow) continue;

            Map<String, String> params = extractParameters(build);

            // 合并 Brand 参数和 Common 参数
            Set<String> deployedInThisBuild = new HashSet<>();

            // 1. 获取 Brand
            if (jenkinsParamName != null && !jenkinsParamName.isEmpty()) {
                String brandSvcs = params.get(jenkinsParamName);
                if (brandSvcs != null && !brandSvcs.isEmpty()) {
                    Collections.addAll(deployedInThisBuild, brandSvcs.split(","));
                }
            }

            // 2. 获取 Common 公共服务
            String commonSvcs = params.get("common");
            if (commonSvcs != null && !commonSvcs.isEmpty()) {
                Collections.addAll(deployedInThisBuild, commonSvcs.split(","));
            }

            // 3. 兜底
            if (deployedInThisBuild.isEmpty()) {
                String fallback = params.get("serviceName");
                if (fallback == null) fallback = params.get("module_name");
                if (fallback != null && !fallback.isEmpty()) {
                    Collections.addAll(deployedInThisBuild, fallback.split(","));
                }
            }

            if (deployedInThisBuild.isEmpty()) continue;

            String gitVersion = params.getOrDefault("git_version", "Unknown");
            String brand = appConfig.brand; // 强制统一使用当前配置的 brand
            String buildTimeStr = formatTimestamp(ts);
            String buildUrl = build.getString("url");

            for (String svc : deployedInThisBuild) {
                svc = svc.trim();
                if (svc.isEmpty()) continue;
                String svcKey = svc.toLowerCase();

                if (targetServices.contains(svcKey)) {
                    if (!latestServicesMap.containsKey(svcKey)) {
                        latestServicesMap.put(svcKey, new ServiceInfo(svc, gitVersion, brand, buildUrl, buildTimeStr));
                    }
                }
            }
        }

        // 补全 N/A
        for (String target : targetServices) {
            if (!latestServicesMap.containsKey(target)) {
                // 即使未部署，Brand 也要设为当前配置的品牌，防止发给 Hytest 时报错
                latestServicesMap.put(target, new ServiceInfo(target, "N/A", appConfig.brand, "N/A", "N/A"));
            }
        }

        List<ServiceInfo> resultList = new ArrayList<>(latestServicesMap.values());
        resultList.sort(Comparator.comparing(s -> s.serviceName));

        String allFileName = new File(RESULT_DIR, String.format("jenkins_deploy_%s_all.csv", appConfig.brand)).getPath();
        saveToCsv(allFileName, resultList);
        System.out.println("Saved snapshot: " + allFileName + " (Total: " + resultList.size() + ")");

        return resultList;
    }

    // --- Step 2: 触发 & 轮询 & 报告 ---

    private static void step2_RunJacocoAudit(List<ServiceInfo> services, String globalVersion) throws Exception {
        System.out.println("Loading mapping for tagging: " + appConfig.mappingFilePath);
        Map<String, MappingData> mapping = loadMapping(appConfig.mappingFilePath);

        // 打印 globalVersion，但后续不传给 DTO
        System.out.println(">>> [INFO] Current Global Version context: " + globalVersion);

        List<JacocoTask> tasks = new ArrayList<>();
        for (ServiceInfo svc : services) {
            tasks.add(new JacocoTask(svc));
        }

        System.out.println("\n====== Starting Batch Trigger (" + tasks.size() + " tasks) ======");
        triggerBatch(tasks);
        processTasksAndWait(tasks);

        List<JacocoTask> failedTasks = tasks.stream()
                .filter(t -> needRetry(t))
                .collect(Collectors.toList());

        if (!failedTasks.isEmpty()) {
            System.out.println("\n====== ⚠️ Found " + failedTasks.size() + " failed tasks. Retrying... ======");
            for (JacocoTask t : failedTasks) t.resetForRetry();
            triggerBatch(failedTasks);
            processTasksAndWait(failedTasks);
        } else {
            System.out.println("\n====== All tasks passed or skipped. No retry needed. ======");
        }

        fetchMetricsForSuccessTasks(tasks);

        tasks.sort((t1, t2) -> {
            String team1 = mapping.getOrDefault(t1.serviceInfo.serviceName.toLowerCase(), new MappingData("Unknown", "")).team;
            String team2 = mapping.getOrDefault(t2.serviceInfo.serviceName.toLowerCase(), new MappingData("Unknown", "")).team;
            int w1 = appConfig.teamSortWeights.getOrDefault(team1, 999);
            int w2 = appConfig.teamSortWeights.getOrDefault(team2, 999);
            return Integer.compare(w1, w2);
        });

        String outputCsvName = new File(RESULT_DIR, "jacoco_merged_report.csv").getPath();
        writeFinalCsv(outputCsvName, tasks, mapping);
        System.out.println("✅ Report generated: " + outputCsvName);

        HytestSendBody body = new HytestSendBody();
        body.setBrand(appConfig.brand);
        body.jacocoList = tasks.stream().map(t -> buildJacocoReportData(t, mapping)).collect(Collectors.toList());

        try {
            HttpResponse<String> resp = sendPostRequestToHytest(body);
            System.out.println("Hytest Response: " + resp.statusCode() + " | " + resp.body());
        } catch (Exception e) {
            System.err.println("Failed to send to Hytest: " + e.getMessage());
        }
    }

    private static boolean needRetry(JacocoTask t) {
        if ("NO_RECENT_DEPLOY".equals(t.status)) return false;
        if ("SUCCESS".equals(t.status)) return false;
        return true;
    }

    private static void triggerBatch(List<JacocoTask> batchTasks) throws Exception {
        String triggerBaseUrl = String.format("%s/%s/buildWithParameters", appConfig.jenkinsUrl, appConfig.jacocoJobPath);

        for (int i = 0; i < batchTasks.size(); i++) {
            JacocoTask task = batchTasks.get(i);

            // 拦截 N/A 版本，防止发送请求导致 500 错误
            if ("N/A".equals(task.serviceInfo.gitVersion) || "NO_RECENT_DEPLOY".equals(task.status)) {
                System.out.println(String.format("[%d/%d] Skipping: %s (No recent deploy)", (i + 1), batchTasks.size(), task.serviceInfo.serviceName));
                task.status = "NO_RECENT_DEPLOY";
                continue;
            }

            System.out.print(String.format("[%d/%d] Triggering: %s (%s)... ", (i + 1), batchTasks.size(), task.serviceInfo.serviceName, task.serviceInfo.gitVersion));
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
                            task.status = "PENDING";
                            System.out.println("[SUCCESS]");
                            break;
                        }
                    }
                    System.out.print("(HTTP " + resp.statusCode() + ") ");
                } catch (Exception e) { System.out.print("(Err) "); }
                if (attempt < 3) TimeUnit.SECONDS.sleep(1);
            }
            if (!triggered) {
                System.out.println("[FAILED]");
                task.status = "TRIGGER_FAILED";
            }
            if (i < batchTasks.size() - 1) TimeUnit.SECONDS.sleep(1);
        }
    }

    private static void processTasksAndWait(List<JacocoTask> batchTasks) throws Exception {
        List<JacocoTask> activeTasks = batchTasks.stream()
                .filter(t -> t.queueUrl != null && !"TRIGGER_FAILED".equals(t.status) && !"NO_RECENT_DEPLOY".equals(t.status))
                .collect(Collectors.toList());

        if (activeTasks.isEmpty()) return;

        System.out.println(String.format("\nWaiting for %d tasks to finish (Timeout: %d mins)...", activeTasks.size(), GLOBAL_TIMEOUT_MINUTES));

        long startTime = System.currentTimeMillis();
        long timeoutMillis = GLOBAL_TIMEOUT_MINUTES * 60 * 1000L;
        boolean allFinished = false;

        while (!allFinished) {
            if (System.currentTimeMillis() - startTime > timeoutMillis) {
                System.out.println("\n⚠️ Global timeout reached! Stop polling.");
                break;
            }

            allFinished = true;
            int pendingCount = 0;
            int runningCount = 0;

            for (JacocoTask task : activeTasks) {
                if (isFinalState(task.status)) continue;

                allFinished = false;

                if (task.buildUrl == null) {
                    try {
                        JSONObject q = new JSONObject(sendGetRequest(task.queueUrl));
                        if (q.has("executable")) {
                            task.buildUrl = q.getJSONObject("executable").getString("url");
                        }
                        pendingCount++;
                    } catch (Exception e) {}
                } else {
                    try {
                        JSONObject b = new JSONObject(sendGetRequest(task.buildUrl + "api/json"));
                        boolean building = b.optBoolean("building", false);
                        if (!building) {
                            task.status = b.optString("result", "UNKNOWN");
                            task.finishTime = formatTimestamp(b.optLong("timestamp"));
                            System.out.println("   -> Finished: " + task.serviceInfo.serviceName + " [" + task.status + "]");
                        } else {
                            runningCount++;
                        }
                    } catch (Exception e) {}
                }
            }

            if (!allFinished) {
                System.out.print(String.format("\rPolling... (Queue: %d, Running: %d)", pendingCount, runningCount));
                TimeUnit.SECONDS.sleep(10);
            }
        }
        System.out.println("\nAll active tasks processed (or timed out).");
    }

    private static boolean isFinalState(String status) {
        return "SUCCESS".equals(status) || "FAILURE".equals(status) || "ABORTED".equals(status) || "UNSTABLE".equals(status);
    }

    private static void fetchMetricsForSuccessTasks(List<JacocoTask> tasks) {
        System.out.println("\nFetching HTML reports for SUCCESS tasks...");
        for (JacocoTask task : tasks) {
            if ("SUCCESS".equals(task.status) && task.buildUrl != null) {
                task.metrics = fetchJacocoMetrics(task.serviceInfo.serviceName, task.buildUrl);
            }
        }
    }

    // --- 辅助方法 ---
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

    private static Map<String, MappingData> loadMapping(String file) {
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
                    if (headerFound) firstLine = false;
                    continue;
                }
                int maxIdx = Math.max(moduleIdx, Math.max(teamIdx, domainIdx));
                if (parts.length <= maxIdx) continue;
                map.put(parts[moduleIdx].trim().toLowerCase(), new MappingData(parts[teamIdx].trim(), parts[domainIdx].trim()));
            }
        } catch (IOException e) { throw new RuntimeException(e); }
        return map;
    }

    private static Config loadConfigFromXml(String xmlPath) throws Exception {
        File xmlFile = new File(xmlPath);
        if (!xmlFile.exists()) throw new RuntimeException("Config file not found: " + xmlFile.getAbsolutePath());

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

        config.brand = getTagValue(root, "brand");
        config.mappingFilePath = getTagValue(root, "mappingFilePath");
        config.serviceListPath = getTagValue(root, "serviceListPath");
        if (config.serviceListPath.isEmpty()) throw new RuntimeException("Missing <serviceListPath>...");

        // [新增] 读取 Git Job 配置
        config.gitJobPath = getTagValue(root, "gitJobPath");
        config.gitJobParamName = getTagValue(root, "gitJobParamName");

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
        if (System.getProperty("brand") != null) appConfig.brand = System.getProperty("brand");
    }

    private static String getTagValue(Element element, String tagName) {
        if (element.getElementsByTagName(tagName).getLength() > 0) {
            return element.getElementsByTagName(tagName).item(0).getTextContent().trim();
        }
        return "";
    }

    private static Set<String> loadServiceList(String file) {
        if (!new File(file).exists()) throw new RuntimeException("Service List file not found: " + new File(file).getAbsolutePath());
        Set<String> list = new HashSet<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(file), StandardCharsets.UTF_8)) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine && line.startsWith("\ufeff")) line = line.substring(1);
                firstLine = false;
                String svc = line.trim();
                if (!svc.isEmpty() && !svc.equalsIgnoreCase("ServiceName")) {
                    list.add(svc.toLowerCase());
                }
            }
        } catch (IOException e) { throw new RuntimeException(e); }
        System.out.println("Loaded service list: " + list.size() + " items.");
        return list;
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
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("AccessKey","Kd7E8ldo1cyOvey9")
                .header("signature","lrf8ibDWf6AZblcr")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBodyJsonStr))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
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

    private static String removePercent(String s) {
        if (s == null || s.isEmpty()){ return "N/A"; }
        return s.replaceAll("%", "").replaceAll(",","");
    }

    static class Config {
        String jenkinsUrl, deployJobPath, jacocoJobPath, username, token;
        String brand, mappingFilePath, serviceListPath;
        // [新增] Git Job 配置
        String gitJobPath;
        String gitJobParamName;
        Map<String, Integer> teamSortWeights;
    }
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
            this.metrics = new ArrayList<>(Collections.nCopies(13, "N/A"));
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