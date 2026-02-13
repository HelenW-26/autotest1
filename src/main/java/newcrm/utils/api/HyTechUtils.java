package newcrm.utils.api;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import junit.framework.Assert;
import lombok.extern.slf4j.Slf4j;
import newcrm.testcases.others.XSourceCoderTest;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class HyTechUtils {

    //map转成形如： a=11&b=xxx
    public static String mapToString(Map<String,String> map){
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append('&'); // 添加'&'分隔符
            }
            sb.append(entry.getKey()).append('=').append(entry.getValue());
        }
        return sb.toString();
    }

    public static String mapToForm(Map<String,Object> map){
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append('&'); // 添加'&'分隔符
            }
            sb.append(entry.getKey()).append('=').append(entry.getValue());
        }
        return sb.toString();
    }

    public static String getCurrentDate(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(date);
        System.out.println(currentDate);
        return  currentDate;
    }

    //从chrome复制的参数，转换成map，不用手动一个个复制
    public static void convertMap(String string){
        StringBuilder sb = new StringBuilder();
        sb.append("Map<String, String> map = new HashMap<>();\n");
        String[] split = string.split("\n");
        for (int i = 0; i < split.length; i++) {
            String[] split1 = split[i].split(":");
            String key = split1[0].trim();
            String val = split1[1].trim();
            sb.append("map.put(\""+key+"\",\""+val+"\");\n");
        }
        System.out.println(sb.toString());
    }

    public static String getEmail(){
        String preFix = "auto";
        String min = System.currentTimeMillis()+"";
        String sufFix = "@qa.com";
        return preFix+min+sufFix;
    }

    public static String getRandomMobile(){
        return System.currentTimeMillis()+"";
    }


    static  String string = "id: 101562\n" +
            "user_Id: 10060228\n" +
            "audit_type: 1\n" +
            "status: 2\n" +
            "suburb: 1\n" +
            "street: 1\n" +
            "state: \n" +
            "postcode: \n" +
            "replacementFiles: \n" +
            "processed_notes: \n" +
            "pending_reason: \n" +
            "custom_pending_reason: \n" +
            "reject_reason: \n" +
            "custom_reject_reason: ";

    public static List<JSONObject> getJsonObjectByFile(Class<?> currentClass){
        URL url = currentClass.getResource("");
        URI uri = null;
        try {
            uri = url.toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        Path path = Paths.get(uri);
        String pathName = path.toString();
        //C:\yangyang\javacode20240409\codes\QaAuto\target\classes\com\hytech\auto\caseapi\au
        //C:\yangyang\javacode20240409\codes\QaAuto\src\main\java\com\hytech\auto\caseapi\au\Case1.txt
        pathName = pathName.replaceAll("\\\\","/");
        pathName = pathName.replaceAll("/target","/src/main/java");
        pathName = pathName.replaceAll("/classes","");


        List<JSONObject> jsonObjectList = new ArrayList<>();
        String filename = pathName+"/Case1.txt"; // txt文件路径
        String line;
        StringBuilder sb = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            while ((line = reader.readLine()) != null) {

                if(line.endsWith("}end")){
                    sb.append("}");
                    jsonObjectList.add(JSONUtil.parseObj(sb));
                    sb.setLength(0);
                }else {
                    sb.append(line);
                }
            }
            jsonObjectList.add(JSONUtil.parseObj(sb));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObjectList;
    }
    public static Map<String,Object> initYamlFile(Class<?> currentClass){
        Map<String,Object> res = new HashMap<>();

        URL url = currentClass.getResource("");
        URI uri = null;
        try {
            uri = url.toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        Path path = Paths.get(uri);
        String pathName = path.toString();
        //C:\yangyang\javacode20240409\codes\QaAuto\target\classes\com\hytech\auto\caseapi\au
        //C:\yangyang\javacode20240409\codes\QaAuto\src\main\java\com\hytech\auto\caseapi\au\Case1.txt
        pathName = pathName.replaceAll("\\\\","/");
        pathName = pathName.replaceAll("/target","/src/main/java");
        pathName = pathName.replaceAll("/classes","");


        List<JSONObject> jsonObjectList = new ArrayList<>();
        String filename = pathName+"/"+currentClass.getSimpleName()+".yaml"; // txt文件路径

        Yaml yaml = new Yaml();
        try {
            Map<String, Object> data = (Map<String, Object>) yaml.load(new FileInputStream(filename));
            if (data != null) {
                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    Map<String, Object> data1 = (Map)entry.getValue();
                    JSONObject param = new JSONObject(data1.get("param"));
                    Object flag = data1.get("code");

                    res.put(entry.getKey()+"param",param);
                    res.put(entry.getKey()+"code",flag);
//                    System.out.println(entry.getKey() + " : " + entry.getValue());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return res;
    }


    public static void totalResult(Class className,Map<String,Boolean> result){
        int success = 0;
        int fail = 0;
        //第一种方式
        for(Map.Entry entry : result.entrySet()){
            String mapKey = (String) entry.getKey();
            Boolean mapValue = (Boolean)entry.getValue();
            System.out.println("【"+className.getSimpleName()+"】："+mapKey+"，执行结果："+mapValue);
            if(mapValue){
                success = success+1;
            }else {
                fail = fail+1;
            }
        }
        System.out.println("【"+className.getSimpleName()+"】合计："+result.size()+"条，成功"+success+"条，失败："+fail+"条");

    }

    public static void main(String[] args) {
//        convertMap(string);
        System.out.println(System.currentTimeMillis());

    }

    // Real-time generated x-source
    public static HashMap<String,String> genXSourceHeader(HashMap<String,String> header) {

        String brandGM = GlobalMethods.getRegisterBrand(GlobalMethods.getBrand().toUpperCase());
        String brand, env;
        XSourceCoderTest.EnvPlatform envPlatform;

        if (brandGM == null || brandGM.trim().isEmpty()) {
            brand = GlobalMethods.getRegisterBrand(GlobalProperties.brand.toUpperCase());
        } else {
            brand = brandGM;
        }

        if (brand == null || brand.trim().isEmpty()) {
            Assert.fail("Brand not found");
        }

        env = GlobalProperties.env.trim();

        if ("prod".equalsIgnoreCase(env)) {
            envPlatform = XSourceCoderTest.EnvPlatform.PROD_PC;
        } else {
            envPlatform = XSourceCoderTest.EnvPlatform.ALPHA_PC;
        }

        header.put("X-Source", new XSourceCoderTest().xSourceCoder.create(XSourceCoderTest.JWTPLATFORM.PC.getValue(), envPlatform, brand));

        return header;
    }

    // Non-real time generated x-source. Can be used in alpha environment.
    public static String getAlphaXSource(String Brand)
    {
        String xSource = "";

        switch(Brand)
        {
            case "vfx":
                xSource = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwYyIsImN0IjoxNzQ1NTcwNjA1MTE0LCJiIjoiQVUiLCJzY29wZSI6ImRlYnVnIn0.ifeBZo9cqbwWFDqNt8VjL-DYTIv6Df65kydj2evHdKw";
                break;
            case "pug":
                xSource = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwYyIsImN0IjoxNzQ1NTcwNjA1MTc1LCJiIjoiUFVHIiwic2NvcGUiOiJkZWJ1ZyJ9.8ApRoSX5ltvuIe3UundvxJqLNRPRSqtmjbN41wvfKe4";
                break;
            case "mo":
                xSource = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwYyIsImN0IjoxNzQ1NTcwNjA1MTc2LCJiIjoiTU9ORVRBIiwic2NvcGUiOiJkZWJ1ZyJ9.HzaEAvrsApYzJ7Mzcc0bjSbZxlB3FCb95ROltZS3LZU";
                break;
            case "um":
                xSource = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwYyIsImN0IjoxNzQ1NTcwNjA1MTc2LCJiIjoiVU0iLCJzY29wZSI6ImRlYnVnIn0.G7o7pu_cIN1YZOWqsEEYtTfdEgb1PYCDhPgzIG6qXG8";
                break;
            case "vjp":
                xSource = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwYyIsImN0IjoxNzQ1NTcwNjA1MTc3LCJiIjoiVkpQIiwic2NvcGUiOiJkZWJ1ZyJ9.aSgV7X4yKQ3oVu5Fr-vmmmy4PMc16mlcK5UfcyS0vLo";
                break;
            case "star":
                xSource = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwYyIsImN0IjoxNzQ1NTcwNjA1MTc3LCJiIjoiU1RBUiIsInNjb3BlIjoiZGVidWcifQ.XLlJr0rr7MjVr9UQck2t6GmjnUw94-FzchVkKacwkn4";
                break;
            case "vt":
                xSource = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJwYyIsImN0IjoxNzQ1NTcwNjA1MTc1LCJiIjoiVlQiLCJzY29wZSI6ImRlYnVnIn0.CMy-eJJKzZ6kisDsBABr6gVgN3WJw9H77r-CcihtZ0c";
                break;
            default:
                xSource = "";
        }

        GlobalMethods.printDebugInfo("x-source: " + xSource);

        return xSource;
    }

}
