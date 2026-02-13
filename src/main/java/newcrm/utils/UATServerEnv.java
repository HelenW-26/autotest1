package newcrm.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import newcrm.global.GlobalProperties;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static org.testng.Assert.assertNotNull;

/**
 * 该类主要用于处理alpha测试环境，读取server的配置JSON，脚本里可根据测试环境名字获得各个url
 * @author FengLiu
 *
 */
public class UATServerEnv {
	private static String servers = "";
	private static void getString() {
		String path = System.getProperty("user.dir");
		path = path + "//src//main//resources//newcrm//data//servers.json";
		String result = "";
		
		try {
			File js = new File(path);
			InputStream ins = new FileInputStream(js);
			InputStreamReader rd = new InputStreamReader(ins,"utf-8");
			Scanner sc = new Scanner(rd);
			while(sc.hasNextLine()) {
				result = result+sc.nextLine();
			}
			sc.close();
			rd.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		servers = result;//将文件所有内容读取为一个字符串
	}
	
	/**
	 * 
	 * @param name alpha环境的名字
	 * @return 返回对应环境的JSON object
	 */
	private static JSONObject getSreverByName(String name) {
		if(servers.equals("")) {
			getString();
		}
		JSONArray objs = (JSONArray) JSON.parse(servers);
		for(Object obj : objs) {
			String serverName = ((JSONObject)obj).getString("name").trim();
			if(name.equalsIgnoreCase(serverName)){
				return (JSONObject) obj;
			}
		}
		return null;
	}
	
	public static String getCpUrl(String server) {
		JSONObject obj = getSreverByName(server);
		assertNotNull(obj,"DO Not Find the env -- " + server);
		
		return "https://" + obj.getString("cp_url");
	}
	
	public static String getAdminUrl(String server) {
		JSONObject obj = getSreverByName(server);
		assertNotNull(obj,"DO Not Find the env -- " + server);
		
		return "https://" + obj.getString("admin_url");
	}
	
	public static String getIBUrl(String server) {
		JSONObject obj = getSreverByName(server);
		assertNotNull(obj,"DO Not Find the env -- " + server);
		
		return "https://" + obj.getString("IB_url");
	}

    public static String getDAPUrl(String server) {
        JSONObject obj = getSreverByName(server);
        assertNotNull(obj,"DO Not Find the env -- " + server);

        return "https://" + obj.getString("DAP_url");
    }

    public static String getOWSUrl(String server) {
        JSONObject obj = getSreverByName(server);
        assertNotNull(obj,"DO Not Find the env -- " + server);

        return "https://" + obj.getString("ows_url");
    }
	
	public static String getOpenApiUrl(String server) {
		JSONObject obj = getSreverByName(server);
		assertNotNull(obj,"DO Not Find the env -- " + server);
		
		return "https://" + obj.getString("openAPI_url");
	}
	
	public static String getAPPServiceUrl(String server) {
		JSONObject obj = getSreverByName(server);
		assertNotNull(obj,"DO Not Find the env -- " + server);
		
		return "https://" + obj.getString("app_service");
	}
	
	//Get data source ID from data source name
	public static String getDataSourceID(String dataSourceName,String brand) {
		String dataSourceID = "";
		switch(dataSourceName.toLowerCase())
		{		
			case "au":
				dataSourceID = "5";
			  break;
			case "au2":
				dataSourceID = "11";
			  break;
			case "au3":
				dataSourceID = "14";
			  break;
			case "au4":
				dataSourceID = "200";
			  break;
			case "uk":
				dataSourceID = "2";
			  break;
			case "uk2":
				dataSourceID = "12";
			  break;
			case "uk3":
				if (brand.equalsIgnoreCase("vfx")) {
					dataSourceID = "15";
				}else if (brand.equalsIgnoreCase("mo")) {
					dataSourceID = "900";
				}
			  break;
			case "uk4":
				dataSourceID = "201";
			  break;
			case "uk5":
				dataSourceID = "220";
			  break;
			case "mt5":
				if (brand.equalsIgnoreCase("vfx")) {
					dataSourceID = "8";
				}else if (brand.equalsIgnoreCase("vt")) {
					dataSourceID = "19";
				}else if (brand.equalsIgnoreCase("pug")) {
					dataSourceID = "20";
				}else if (brand.equalsIgnoreCase("mo")) {
					dataSourceID = "907";
				}
			  break;
			case "vp":
				dataSourceID = "17";
			  break;
			case "vt":
				dataSourceID = "10";
			  break;
			case "vt2":
				dataSourceID = "18";
			  break;
			case "vt3":
				dataSourceID = "58";
			  break;
			case "vt4":
				dataSourceID = "68";
			  break;
			case "vt5":
				dataSourceID = "78";
			  break;
			case "pug":
				dataSourceID = "7";
			  break;
			case "pug2":
				dataSourceID = "21";
			case "pug3":
				dataSourceID = "59";
			  break;
			case "um":
				dataSourceID = "600";
			  break;
			default:
				System.out.println("\n! ! ! DataSourceName not Found!");
		}
		
		return dataSourceID;
	}

	//Get davinci URL based on brand
	public static String getDavinciURL(String brand) {
		String davinciURL = "";
		switch(brand.toLowerCase())
		{
			case "au":
			case "vfx":
				davinciURL = "https://davinci-au.crm-alpha.com";
				break;
			case "vt":
				davinciURL = "https://davinci-vt.crm-alpha.com";
				break;
			case "pug":
			case "pu":
				davinciURL = "https://davinci-pug.crm-alpha.com";
				break;
			case "star":
				davinciURL = "https://davinci-star.crm-alpha.com";
				break;
			case "vjp":
				davinciURL = "https://davinci-vjp.crm-alpha.com";
				break;
			case "um":
				davinciURL = "https://davinci-um.crm-alpha.com";
				break;
			case "mo":
				davinciURL = "https://davinci-mo.crm-alpha.com";
				break;
			default:
				System.out.println("\n! ! ! No davinci URL =( check code code");
		}

		return davinciURL;
	}
	
	public static void main(String args[]) {
		
		System.out.println(getCpUrl("earth"));
	}

	/**
	 *
	 * @param brand
	 * @return MT4 Test Server Id
	 */
	public static List<String> getMT4TestServerIdByBrand(GlobalProperties.BRAND brand) {
		switch(brand){
			case VFX:
				return Arrays.asList("5");
			case VT:
				return Arrays.asList("400");
			case PUG:
				return Arrays.asList("400");
			case STAR:
				return Arrays.asList("1050");
			case VJP:
				return Arrays.asList("400");
			case UM:
				return Arrays.asList("400");
			case MO:
				return Arrays.asList("400");
			default:
				return Collections.emptyList();
		}
	}

	/**
	 *
	 * @param brand
	 * @return MT5 Test Server Id
	 */
	public static List<String> getMT5TestServerIdByBrand(GlobalProperties.BRAND brand) {
		switch(brand){
			case VFX:
				return Arrays.asList("8");
			case VT:
				return Arrays.asList("19");
			case PUG:
				return Arrays.asList("20");
			case STAR:
				return Arrays.asList("830");
			case VJP:
				return Arrays.asList("520");
			case UM:
				return Arrays.asList("620");
			case MO:
				return Arrays.asList("911");
			default:
				return Collections.emptyList();
		}
	}
	/**
	 *
	 * @param brand
	 * @return MTS Test Server Id
	 */
	public static List<String> getMTSTestServerIdByBrand(GlobalProperties.BRAND brand) {
		switch(brand){
			case VFX:
				return Arrays.asList("998");
			case PUG:
				return Arrays.asList("999","3030");
			default:
				return Collections.emptyList();
		}
	}
}
