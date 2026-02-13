package newcrm.utils.app;

import com.alibaba.fastjson.JSONObject;


public class AddAdditonalAccount extends APIBase{
    /**
     * 这个构造函数没有传递APP相关的秘钥，后续发现有太多的差异考虑添加一个初始化秘钥的构造函数
     *
     * @param host      app service address
     * @param uri       request uri
     * @param regulator regulator
     * @param brand     brand
     */
    public AddAdditonalAccount(String host, String uri, String regulator, String brand) {

        super(host, "/account/apply_namesake_account", regulator,brand);
    }

    public String sendAddAddtionalAccount(String mt4_account_type, String uid,String mamNumber, String currency,String agentmt4account, String tradingPlatform) {

        JSONObject obj = new JSONObject();
        obj.put("mt4_account_type", mt4_account_type);
        obj.put("userId", uid);
        obj.put("mam_number", mamNumber);
        obj.put("currency", currency);
        obj.put("agentmt4account", agentmt4account);
        obj.put("tradingPlatform", tradingPlatform);

        return this.send(obj);
    }
    public static void main(String[] args){
        String host= "app-everton.crm-alpha.com";

       // String host= "app-new.vtmarkets.com.cn";
        AddAdditonalAccount addAdditonalAccount =new AddAdditonalAccount(host,"/account/apply_namesake_account","SVG","VT");

        String result = addAdditonalAccount.sendAddAddtionalAccount("1","10012552","","USD","810281153","MT4");
       // String result = addAdditonalAccount.sendAddAddtionalAccount("1","361701","","USD","8482321","MT4");
        System.out.println(result);

    }
}
