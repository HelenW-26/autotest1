package newcrm.utils.app;

import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import newcrm.utils.encryption.EncryptUtil;
import newcrm.utils.http.CRMHttpClient;
import vantagecrm.RestAPI;

public class APIBase {

	protected  String appkey = "app20171012ok123";
	protected  String md5key = "APP20171012MD5123";
	protected  String regulator=null;
	protected  String appID = "2a8e158055a04b3a8f7172c3e59500f7";
	
	protected String pug_key = "VTAPP2XS0AE1QTK8";
	protected String pug_md5 = "VTAPP201802MD5EDCRTYHBVCD";
	
	protected String uri=null;
	protected String host = null;
	
	protected String brand;
	//http
	protected HashMap<String,String> httpHeader = new HashMap<>();
	/**
	 * 这个构造函数没有传递APP相关的秘钥，后续发现有太多的差异考虑添加一个初始化秘钥的构造函数
	 * @param host app service address
	 * @param uri request uri
	 * @param regulator regulator
	 * @param brand brand
	 */
	public APIBase(String host,String uri,String regulator,String brand) {
		if(host.indexOf("/")==host.length()-1) {
			host = host.substring(0,host.length()-1);
		}
		if(!host.contains("http")) {
			this.host = "https://"+ host;
		}else {
			this.host = host;
		}
		this.uri = uri;
		this.regulator = regulator;
		this.brand = brand;
		httpHeader.put("Content-Type", "application/json");
		httpHeader.put("Accept", "*/*");
		httpHeader.put("AppId", appID);
	}
	
	/**
	 * 将request的data加密
	 * @param obj 需要加密的字符串
	 * @return 返回加密后的字符串
	 */
	private String getEncStr(String obj) {
		String key = null;
		if("pug".equalsIgnoreCase(brand)) {
			key = this.pug_key;
		}else {
			key = this.appkey;
		}
		String result = EncryptUtil.AES_Encrypt(key,obj);
		
		return result;
	}
	
	/**
	 * 计算request所需的sign签名
	 * @param data 加密的data数据
	 * @return 返回计算的sign
	 */
	private String getSign(String data) {
		if(uri==null) {
			return null;
		}
		String key = null;
		if("pug".equalsIgnoreCase(brand)) {
			key = this.pug_md5;
		}else {
			key = this.md5key;
		}
		String sign = uri + data+"null"+key;
		sign = EncryptUtil.MD5(sign);
		return sign.toUpperCase();
	}
	
	/**
	 * 将obj以post方式发送
	 * @param obj 请求数据
	 * @return response中的body值
	 */
	protected String send(Object obj) {
		String value = "";
		if(obj.getClass() == JSONObject.class) {
			value = ((JSONObject)obj).toJSONString();
		}else {
			value = String.valueOf(obj);
		}
		
		String enc = getEncStr(value);
		String sign = getSign(enc);
		String data = "{\"data\":\""+enc+"\",\"sign\":\""+sign+"\",\"regulator\":\""+this.regulator+"\"}";

		String fullpath = host + uri;
		String result = null;
		System.out.println("Send: " + data + "\nto\n" + fullpath);
		try {
			result = RestAPI.commonPostAPI(fullpath,httpHeader,((JSONObject)JSON.parse(data)).toJSONString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 将obj以get方式发送
	 * @param obj 需要发送的数据
	 * @return response中的body值
	 */
	protected String sendGet(Object obj) {
		String value = "";
		CRMHttpClient httpClient = new CRMHttpClient();
		
		if(obj.getClass() == JSONObject.class) {
			value = ((JSONObject)obj).toJSONString();
		}else {
			value = String.valueOf(obj);
		}
		
		String enc = getEncStr(value);
		String sign = getSign(enc);
		String data = "{\"data\":\""+enc+"\",\"sign\":\""+sign+"\",\"regulator\":\""+this.regulator+"\"}";

		String fullpath = host + uri;
		HttpResponse response = null;
		String result  = null;
		//System.out.println("Send: " + data + "\nto\n" + fullpath);
		try {
			response = httpClient.getGetResponse(fullpath, httpHeader, data);
			result = EntityUtils.toString(response.getEntity(),"UTF-8");
			//result = RestAPI.commonGetAPI(fullpath,httpHeader,((JSONObject)JSON.parse(data)).toJSONString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 取得response中的data字符串，并解密返回
	 * @param reponse APP API返回的字符串
	 * @return 
	 */
	public String getResponse(String reponse) {
		JSONObject obj = JSON.parseObject(reponse);
		/*if(!"1000".equals(obj.get("code"))){
			return obj.getString("msg");
		}
		*/
		String data = obj.getString("data");
		String decStr="";
		String key = null;
		if("pug".equalsIgnoreCase(brand)) {
			key = this.pug_key;
		}else {
			key = this.appkey;
		}
		try {
			if(data !=null && !"".equals(data)) {
				decStr = EncryptUtil.AES_Decrypt(key, data);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return decStr;
	}

	public static void main(String args[]) {
		String key = "VTAPP2XS0AE1QTK8";
		String host = "app-aquarius-ex.crm-alpha.com";
		String uri = "/verification/process";
		String geturi = "/campaign/campaign-code";
		APIBase base = new APIBase(host,geturi,"VFSC2","VFX");
		JSONObject obj = new JSONObject();
		
		obj.put("userId", "10004580");
		obj.put("campaignId", "9");
		
		String response = base.sendGet(obj);
		
		System.out.println(base.getResponse(response));
		
		/*
		obj.put("userId", Integer.valueOf(10003765));
		obj.put("step", "4");
		obj.put("skipNextStep", false);
		obj.put("hideAccountType", false);
		obj.put("accountCreated", false);		
		obj.put("accountReadOnly", false);

		obj.put("tradingPlatform", "MT4");
		obj.put("accountType", "standardSTP");
		obj.put("currency", "USD");
		JSONObject answers = JSONObject.parseObject("{\"step\":3,\"employmentFinanceAnswers\":[{\"questionId\":\"1\",\"answers\":[2]},{\"questionId\":\"2\",\"answers\":[10]},{\"questionId\":\"3\",\"answers\":[18]},{\"questionId\":\"4\",\"answers\":[25]},{\"questionId\":\"5\",\"answers\":[36]}],\"tradingAnswers\":[{\"questionId\":\"6\",\"answers\":[40]},{\"questionId\":\"7\",\"answers\":[44]}]}");
		answers.put("userId", 10003765);
		
		String result = base.send(obj);
		System.out.println(result);
		System.out.println(base.getResponse(result));
		*/
		
		/*
		try {
			System.out.println(EncryptUtil.AES_Decrypt("app20171012ok123","5rrUmOc2lZ1YaQl540x7cw=="));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/


	}
}
