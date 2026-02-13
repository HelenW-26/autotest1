package newcrm.utils.api;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YamlDataProviderUtils {

    public static List<Map<String, String>> getYamlList(String filePath) {
        List<Map<String, String>> list = new ArrayList();
        Map<String, Map<String, String>> map = readYamlUtil(filePath);
        for (Map.Entry<String, Map<String, String>> me : map.entrySet()) {
            Map<String, String> numNameMapValue = me.getValue();
            Map<String, String> tmp = new HashMap<>();
            for (Map.Entry<String, String> nameMapEntry : numNameMapValue.entrySet()) {
                String numKey = nameMapEntry.getKey();
                String nameValue = nameMapEntry.getValue();
                tmp.put(numKey, nameValue);
            }
            list.add(tmp);
        }
        return list;
    }

    public static Map<String, Map<String, String>> readYamlUtil(String filePath) {
        Map<String, Map<String, String>> map = null;
        try {
            Yaml yaml = new Yaml();
            String resourcePath = YamlDataProviderUtils.class.getClassLoader().getResource("").getPath();
            if(resourcePath.contains("%20")){
                resourcePath = resourcePath.replaceAll("%20"," ");
            }
            String path = resourcePath.substring(0,resourcePath.indexOf("target"))+"src/main/resources/";
            //获取yaml文件中的配置数据，然后转换为Map
            map = (Map<String, Map<String, String>>) yaml.load(new FileInputStream(path+filePath));
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static Object[][] getTestData(String filePath){
        List<Map<String, String>> yamlList = YamlDataProviderUtils.getYamlList(filePath);
        Object[][] data = new Object[yamlList.size()][];
        for (int i = 0; i < yamlList.size(); i++) {
            data[i] = new Object[]{yamlList.get(i)};
        }
        return data;
    }

    }
