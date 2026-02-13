package newcrm.utils.callback;

import java.text.DecimalFormat;
import java.util.HashMap;

import org.apache.http.util.EntityUtils;

import vantagecrm.RestAPI;

public abstract class  CallbackBase {

	protected String url;
	protected String orderNum;
	protected String currency;
	protected String amount;
	
	//card
	protected String first6num;
	protected String last4num;
	protected String year;
	protected String month;
	
	protected String type;
	
	protected String fullPath;
	protected String body;
	protected HashMap<String,String> formBody;
	protected HashMap<String, String> headerMap;
	
	
	//brand
	protected String brand;
	protected String regulator;
	
	protected String key;
	
	/**
	 *  初始化基本信息
	 * @param url open api地址
	 * @param brand 品牌
	 * @param regulator 监管
	 */
	public void setEnv(String url,String brand,String regulator) {
		if(!url.contains("https")) {
			this.url ="https://"+ url;
		}else {
			this.url = url;
		}
		if(url.lastIndexOf("/")!=url.length()-1) {
			this.url = url + "/";
		}
		this.brand = brand;
		this.regulator = regulator;
	}
	
	public void setPaymentInfo(String orderNum, String currency, Double amount,String first6num,String last4num,String year,String month) {
		body = null;
		formBody = null;
		this.orderNum = orderNum;
		this.currency = currency;
		//this.amount = (new BigDecimal(amount).setScale(2,ROUND_DOWN)).toString();
		DecimalFormat dc = new DecimalFormat("0.00");
		this.amount = dc.format(amount);//金额保留两位小数
		this.last4num = last4num;
		this.first6num = first6num;
		this.year = year;
		this.month = month;
		setFullpath();
		setKey();
		generateRequest();
	}
	public void setPaymentInfo(String orderNum, String currency, String amount,String first6num,String last4num,String year,String month) {
		body = null;
		formBody = null;
		this.orderNum = orderNum;
		this.currency = currency;
		//this.amount = (new BigDecimal(amount).setScale(2,ROUND_DOWN)).toString();
		/*DecimalFormat dc = new DecimalFormat("0.00");
		this.amount = dc.format(amount);//金额保留两位小数*/
		this.amount = amount;
		this.last4num = last4num;
		this.first6num = first6num;
		this.year = year;
		this.month = month;
		setFullpath();
		setKey();
		generateRequest();
	}
	
	//统一的发送接口
	public String sendCallback() {
		if(body==null&&formBody==null&&!"get".equalsIgnoreCase(type)) {
			return null;
		}
		try {
			System.out.println("URL: " + fullPath);
			//System.out.println("Body: " + body);
			String result =null;
			if("get".equalsIgnoreCase(type)) {
				result = EntityUtils.toString(RestAPI.commonGetAPI(fullPath, headerMap).getEntity(),"UTF-8") ;
				//result = RestAPI.commonGetAPI(fullPath, headerMap).toString();
			}else {
				result = body != null?RestAPI.commonPostAPI(fullPath,headerMap,body):RestAPI.commonPostAPI(fullPath,headerMap,formBody);
				//result = RestAPI.commonPostAPI(fullPath,headerMap,body);
			}
			System.out.println("Response: " +result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//子类需要实现的方法
	/**
	 * 按照不同入金渠道要求，实现对应的callback body
	 */
	protected abstract void generateRequest();
	/**
	 * 设置对应入金渠道callback的路径
	 */
	protected abstract void setFullpath();
	/**
	 * 设置对应入金渠道的key
	 */
	protected abstract void setKey();
	
	/**
	 * 设置http header中content type为json
	 */
	protected void jsonBodyHeader() {
		headerMap = new HashMap<>();
		headerMap.put("Content-Type", "application/json");
		headerMap.put("Accept", "*/*");
	}
}
