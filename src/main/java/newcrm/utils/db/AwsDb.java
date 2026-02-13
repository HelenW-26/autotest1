package newcrm.utils.db;

import java.util.HashMap;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;

import newcrm.utils.http.CRMHttpClient;

/**
 * 提供访问AWS DB的基本方法
 * @author FengLiu
 *
 */
public class AwsDb {

	//需要修改为可用的用户名和密码
	private final String url = "https://archery-aws.crm-prod.com";
	
	private final String user = "shan.liu@vantagemarkets.com";
	
	private final String password = "park-Mary-hyundai-65";
	
	private HashMap<String,String> header;
	
	private CRMHttpClient httpClient;
	
	
	public AwsDb() {
		header = new HashMap<>();
		header.put("accept", "application/json, text/javascript, */*; q=0.01");
		header.put("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
		//header.put("origin", url);
		//header.put("referer", url);
		header.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36");
		header.put("Host", "archery-aws.crm-prod.com");
		header.put("accept-language", "en-GB,en-US;q=0.9,en;q=0.8");
		header.put("Connection", "keep-alive");
		httpClient = new CRMHttpClient();
		
		//拿到x-csrftoken
		try {
			HttpResponse response = httpClient.getGetResponse(url+"/login/", null);
			String strCookie = response.getLastHeader("Set-Cookie").getValue();
			String args[] = strCookie.split(";");
        	String values[] = args[0].split("=");
        	header.put("x-csrftoken",values[1]);
        	header.put("Cookie",args[0]);
			
		}catch(Exception e) {
			System.out.println(e.getStackTrace());
		}
		
		//拿到session id 并更新x-csftoken
		HashMap<String,String> body = new HashMap<>();
		body.put("username", user);
		body.put("password", password);
		
		try {
			HttpResponse response = httpClient.getPostResponse(url+"/authenticate/", header, body);
			//System.out.println(EntityUtils.toString(response.getEntity(),"UTF-8"));
			String newCookie = "";
			for(Header h: response.getHeaders("Set-Cookie")) {
				
				String args[] = h.getValue().split(";");
				if(args[0].contains("csrftoken")) {
					header.put("x-csrftoken", args[0].split("=")[1]);
					BasicClientCookie cookie = new BasicClientCookie("csrftoken",args[0].split("=")[1]);
					cookie.setDomain("archery-aws.crm-prod.com");
					cookie.setPath("/");
					cookie.setAttribute("sameSite", "Lax");
					httpClient.setCookie(cookie);
					
				}else {
					BasicClientCookie cookie = new BasicClientCookie(args[0],args[1]);
					cookie.setDomain("archery-aws.crm-prod.com");
					cookie.setPath("/");
					cookie.setAttribute("sameSite", "Lax");
					httpClient.setCookie(cookie);
				}
				
				if("".equals(newCookie)) {
					newCookie = args[0];
				}else {
					newCookie = newCookie + ";"+args[0];
				}
			}
			
			header.put("Cookie", newCookie);
			
			//response = httpClient.getGetResponse(url+"/index/", header);
			//response = httpClient.getGetResponse(url+"/sqlquery/", header);
			//System.out.println(EntityUtils.toString(response.getEntity(),"UTF-8"));
			
		}catch(Exception e) {
			System.out.println("ERROR: " + e.getMessage());
			System.out.println(e.getStackTrace());
		}
	}
	
	/**
	 * 处理返回结果，格式化为JSONArray，与DbUtils中的返回格式统一
	 * @param obj 查询结果的JSON
	 * @return 格式化后的result
	 */
	private JSONArray formatResult(JSONObject obj) {
		if(obj == null) {
			return null;
		}
		JSONArray result = new JSONArray();
		
		
		JSONArray names = obj.getJSONObject("data").getJSONArray("column_list");
		
		//System.out.println(names);
		
		JSONArray rows = obj.getJSONObject("data").getJSONArray("rows");
		//System.out.println(rows);
		for(int i=0; i< rows.size();i++) {
			JSONArray row = rows.getJSONArray(i);
			JSONObject objRow = new JSONObject();
			for(int j = 0; j< row.size();j++) {
				String value = row.getString(j);
				if(value == null || value.equalsIgnoreCase("null")) {
					objRow.put(names.getString(j), "");
				}else if(value !=null && JSONValidator.from(value).validate()) {
					objRow.put(names.getString(j), JSON.parse(value));
				}else {
					objRow.put(names.getString(j), value);
				}
				
			}
			result.add(objRow);
		}
		return result;
	}
	
	/**
	 * 通过指定不同的instance，db name可以实现查询不同db的功能
	 * @param instance_name aws上的instance名
	 * @param db_name db 名字
	 * @param sql_content sql语句
	 * @return 格式化后的result
	 */
	public JSONArray query(String instance_name,String db_name,String sql_content) {
		String fullPath = url + "/query/";
		String body = "instance_name="+instance_name+"&db_name="+db_name+"&schema_name=&tb_name=&sql_content="+sql_content+"&limit_num=100";
		//System.out.println(body);
		try {
			HttpResponse response = httpClient.getPostResponse(fullPath, header, body);
			String result = EntityUtils.toString(response.getEntity(),"UTF-8");
			JSONObject obj = JSON.parseObject(result);
			return formatResult(obj);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
		
	}
	
	public static void main(String args[]) {
		
		AwsDb db = new AwsDb();
		/*String sql = "select U.id as userId, M.status as wcStatus, I.status as idStatus,AP.status as addrStatus,E.vars as vars from tb_user U left join tb_account_mt4 M on U.id = M.user_id left join tb_id_proof I on U.id = I.user_id left join tb_address_proof AP on U.id = AP.user_id left join tb_mail_send_log E on U.email = E.to_mail and E.template_invoke_name = \"welcome_mail\"\r\n"
				+ " where U.email = \"6IuYg/7RuT858QgHNjoUCQX3pVVX/4BW\" and U.is_del!=1";*/
		
		//String sql = "select * from tb_user order by id desc limit 2";
		String sql = "select vars from tb_mail_send_log where template_invoke_name like \'verification_code\' and vars like '%Test CRM%' order by create_time desc limit 1;";
		JSONArray result = db.query("MO+LIVE+%E6%A5%AD%E5%8B%99%E5%BA%AB", "mo_vfsc_business", sql);
		if(result !=null)
		for(Object obj:result) {
			System.out.println("Find result: ");
			System.out.println(JSON.parseObject(obj.toString()).toJSONString());
		}
		
		System.out.println("finish");
	}
	
}
