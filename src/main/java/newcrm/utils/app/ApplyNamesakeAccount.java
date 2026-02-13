package newcrm.utils.app;
import com.alibaba.fastjson.JSONObject;
public class ApplyNamesakeAccount extends APIBase{
    public ApplyNamesakeAccount(String host,String regulator,String brand) {
        super(host, "/account/apply_namesake_account", regulator,brand);
        // TODO Auto-generated constructor stub
    }
    /**
     *
     * @param userId 用户id
     * @param mt4_account_type mt4账号类型
     * @param currency 币种
     * @param mam_number 账户类型
     * @param tradingPlatform 交易平台
     * @return
     */
    public String sendNameSakeAccount(String userId,String mt4_account_type,String currency,String mam_number,String tradingPlatform){
        JSONObject obj = new JSONObject();
        obj.put("userId",userId);
        obj.put("mt4_account_type",mt4_account_type);
        obj.put("currency",currency);
        obj.put("mam_number",mam_number);
        obj.put("tradingPlatform",tradingPlatform);
        return this.send(obj);
    }
    
    /**
    *
    * @param userId 用户id
    * @param account_type 账号类型
    * @param currency 币种
    * @param tradingPlatform 交易平台
    * @return
    */
    public String openDemoAccount(String userId,String account_type,String currency,String tradingPlatform){
    	this.uri = "/createDemoAccount";
        JSONObject obj = new JSONObject();
        obj.put("userId",userId);
        obj.put("accountType",account_type);
        obj.put("currency",currency);
        obj.put("tradingPlatform",tradingPlatform);
        return this.send(obj);
    }

    public static void main(String[] args){
        String host ="app-earth-ex.crm-alpha.com";
        ApplyNamesakeAccount applyNamesakeAccount = new ApplyNamesakeAccount(host,"VFSC2","VFX");
        String result =applyNamesakeAccount.sendNameSakeAccount("10004205","1","USD","","MT4");
        System.out.println(result);
    }
}
