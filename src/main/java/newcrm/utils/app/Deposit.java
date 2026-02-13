package newcrm.utils.app;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.REGULATOR;
import newcrm.utils.db.DbUtils;

import java.lang.reflect.Array;
import java.math.BigDecimal;
public class Deposit extends APIBase {
	protected DbUtils db = null;
    /**
     *
     * @param host app server url from yannibot
     * @param regulator regulator
     */
    public Deposit(String host,String regulator,String brand) {
        super(host, "/deposit/advance_order", regulator,brand);
        // TODO Auto-generated constructor stub
    }

    /**
     *
     * @param amountUsd 申请入金金额（不分币种）
     * @param rate 汇率
     * @param depositAmount 入金数额
     * @param mt4Account mt4账号
     * @param uid 用户id
     * @param payerType 入金渠道
       @param //fileUrlList 附件路径集合
     * @param depositMode 信用卡/银联入金
     * @param cardBeginSixDigits 信用卡前六位
     * @param cardLastFourDigits 信用卡后四位
     * @param cardHolderName 信用卡持卡人姓名
     * @param expireMonth 信用卡有效期月份
     * @param expireYear 信用卡有效期年份
     * @param threeDomainSecure  是否3D认证 0 无3D   1 有3D
     * @param extend1  比特币：quote deposit rate  泰达币：原定入金 - 钞
     * @param extend2  比特币：quote deposit rate  泰达币：原定入金 -数字币
     * @param extend3  比特币： pow                泰达币：公司承担额
     * @return
     */
    public String sendDeposit(String amountUsd, BigDecimal rate, BigDecimal depositAmount, String mt4Account, 
    						  String uid, String platform, String payerType,
                              String cardBeginSixDigits, String cardLastFourDigits, String cardHolderName, String expireMonth, String expireYear,
                              String threeDomainSecure, String extend1, String extend2, String extend3) {

        JSONObject obj = new JSONObject();
        obj.put("amountUsd", amountUsd);
        obj.put("clientIp", "52.220.91.99");
        obj.put("rate", rate);
        obj.put("depositAmount", depositAmount);
        obj.put("mt4Account", mt4Account);
        obj.put("mtsAccount", "");
        obj.put("sourceServerId", platform);
        obj.put("userId", uid);
        obj.put("payerType",payerType);
        //obj.put("fileUrlList",fileUrlList);
        obj.put("cardBeginSixDigits",cardBeginSixDigits);
        obj.put("cardLastFourDigits",cardLastFourDigits);
        obj.put("cardHolderName",cardHolderName);
        obj.put("expireMonth",expireMonth);
        obj.put("expireYear",expireYear);
        obj.put("threeDomainSecure",threeDomainSecure);
        obj.put("extend1",extend1);
        obj.put("extend2",extend2);
        obj.put("extend3",extend3);

        return this.send(obj);
    }

    /**
     *
     * @param amountUsd 申请入金金额（不分币种）
     * @param rate 汇率
     * @param depositAmount 入金数额
     * @param mt4Account mt4账号
     * @param uid 用户id
     * @param payerType 入金渠道
     @param //fileUrlList 附件路径集合
      * @param depositMode 信用卡/银联入金
     * @param cardBeginSixDigits 信用卡前六位
     * @param cardLastFourDigits 信用卡后四位
     * @param cardHolderName 信用卡持卡人姓名
     * @param expireMonth 信用卡有效期月份
     * @param expireYear 信用卡有效期年份
     * @param threeDomainSecure  是否3D认证 0 无3D   1 有3D
     * @param extend1  比特币：quote deposit rate  泰达币：原定入金 - 钞
     * @param extend2  比特币：quote deposit rate  泰达币：原定入金 -数字币
     * @param extend3  比特币： pow                泰达币：公司承担额
     * @return
     */
    public String sendCPSDeposit(String amountUsd, BigDecimal rate, BigDecimal depositAmount, String mt4Account,
                              String uid, String platform, String payerType,
                              String cardBeginSixDigits, String cardLastFourDigits, String cardHolderName, String expireMonth, String expireYear,
                              String threeDomainSecure, String extend1, String extend2, String extend3) {

        JSONObject obj = new JSONObject();

        obj.put("userId", uid);
        obj.put("amountUsd", amountUsd);
        obj.put("depositAmount", depositAmount);
        obj.put("rate", rate);
        obj.put("extend1",extend1);
        obj.put("extend2",extend2);
        obj.put("mt4Account", mt4Account);
        obj.put("payerType",payerType);
        //obj.put("fileUrlList",fileUrlList);
        obj.put("cardBeginSixDigits",cardBeginSixDigits);
        obj.put("cardLastFourDigits",cardLastFourDigits);
        obj.put("cardHolderName",cardHolderName);
        obj.put("expireMonth",expireMonth);
        obj.put("expireYear",expireYear);
        obj.put("threeDomainSecure",threeDomainSecure);
        obj.put("mtsAccount", "");
        obj.put("attachVariables", "{}");
        obj.put("sourceServerId", platform);
        return this.send(obj);
    }
    
    public String updateStatus(String amountUsd, BigDecimal rate, BigDecimal depositAmount, 
    		String mt4Account, String uid, String payerType, String platform, String orderNumber,String status,String cardBeginSixDigits, String cardLastFourDigits,String cardHolderName) {
		this.uri = "/deposit/state_change";
		JSONObject obj = new JSONObject();
        obj.put("userId", uid);
        obj.put("clientIp", "52.220.91.99");
        obj.put("rate", rate);
        obj.put("mt4Account", mt4Account);
        obj.put("mtsAccount", "");
        obj.put("sourceServerId", platform);
		obj.put("amountUsd", amountUsd);
        obj.put("depositAmount", depositAmount);
        obj.put("payerType",payerType);
        obj.put("orderNumber",orderNumber);
        obj.put("status",status);
        obj.put("comment","");
        obj.put("netellerToken","");
        obj.put("nabType",0);
        obj.put("cardBeginSixDigits",cardBeginSixDigits);
        obj.put("cardLastFourDigits",cardLastFourDigits);
        obj.put("cardHolderName",cardHolderName);
        
		return this.send(obj);
    }

	public JSONObject getDepositRecord(ENV env, BRAND brand, REGULATOR regulator, String account, Integer type, Integer channel) {

		String sql = "select * from tb_payment_deposit where mt4_account=" + account + " and payment_type=" + type + " and payment_channel=" + channel + " order by id desc limit 1";
		if (db == null) {
			db = new DbUtils(env, brand, regulator);
		}
		JSONArray array = db.queryRegulatorDB(sql);
		if (array.size() > 0) {
			return array.getJSONObject(0);
		}
		return null;
	}
    
    public static void main(String[] args){
        String host= "app-earth-ex.crm-alpha.com";
        Deposit deposit =new Deposit(host,"VFSC2","VFX");

        String result = deposit.sendDeposit("132.00",BigDecimal.valueOf(1),BigDecimal.valueOf(132.00),"810260354", "10009096","MT4","124",
                                            "545433","6666", "Postman testCRM",
                                            "06","2028","1","","","");

        System.out.println(result);

    }
}


