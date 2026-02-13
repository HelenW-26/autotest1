package newcrm.utils.app;
import com.alibaba.fastjson.JSONObject;
public class ChangeDepositStatus extends APIBase {
    /**
     *
     * @param host app server url from yannibot
     * @param regulator regulator
     */
    public ChangeDepositStatus(String host,String regulator,String brand) {
        super(host, "/deposit/state_change", regulator,brand);
        // TODO Auto-generated constructor stub
    }

    /**
     *
     * @param amountUsd 申请入金金额（不分币种）100 2000
     * @param rate 入金汇率 1.7 0.01
     * @param depositAmount 实际支付金额（支付币种金额）170 20
     * @param status 状态
     * @param mt4Account mt4账号
     * @param uid 用户id
     * @param sourceServerId MTS
     * @param orderNumber 订单号
     * @param payerType 入金渠道
     * @param depositMode 信用卡/银联入金
     * @param cardBeginSixDigits 信用卡前六位
     * @param cardLastFourDigits 信用卡后四位
     * @param cardHolderName 信用卡持卡人姓名
     * @param expireMonth 信用卡有效期月份
     * @param expireYear 信用卡有效期年份

     * @return
     */
    public String sendChangeStatus(String amountUsd,String rate,String depositAmount,String status,String mt4Account,String uid,
                                   String sourceServerId, String orderNumber,String payerType,String depositMode,
                                   String cardBeginSixDigits, String cardLastFourDigits, String cardHolderName,
                                   String expireMonth,String expireYear
    ) {
        JSONObject obj = new JSONObject();
        obj.put("amountUsd", amountUsd);
        obj.put("rate", rate);
        obj.put("depositAmount", depositAmount);
        obj.put("status",status);
        obj.put("mt4Account", mt4Account);
        obj.put("userId", uid);
        obj.put("sourceServerId",sourceServerId);
        obj.put("orderNumber",orderNumber);
        obj.put("payerType",payerType);
        obj.put("depositMode",depositMode);
        obj.put("cardBeginSixDigits",cardBeginSixDigits);
        obj.put("cardLastFourDigits",cardLastFourDigits);
        obj.put("cardHolderName",cardHolderName);
        obj.put("expireMonth",expireMonth);
        obj.put("expireYear",expireYear);


        return this.send(obj);
    }
    public static void main(String[] args){
        String host= "app-earth-ex.crm-alpha.com";

        ChangeDepositStatus changeDepositStatus = new ChangeDepositStatus(host,"VFSC2","VFX");

        String result=changeDepositStatus.sendChangeStatus("132.00","1.0","132.00","1","810260354","10009096",
                                                           "MT4","VTSG81026035420220905002515","124","1",
                                                           "545433","6666", "Postman testCRM",
                                                           "06","2028");

        System.out.println(result);
    }
}

