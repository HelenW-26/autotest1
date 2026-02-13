package newcrm.business.dbbusiness;

import com.alibaba.fastjson.JSONArray;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.REGULATOR;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.utils.db.DbUtils;
import vantagecrm.DecryptUtils;

public class SMSDB {

    private DbUtils db;
    private final REGULATOR regulator;

    public SMSDB(ENV env, BRAND brand, REGULATOR regulator) {
        db = new DbUtils(env,brand,regulator);
        this.regulator = regulator;
    }

    public REGULATOR getRegulator() {
        return regulator;
    }

    //Get OTP code from tb_sms_history in DB
    public String getOTP(GlobalProperties.ENV env, GlobalProperties.BRAND brand, GlobalProperties.REGULATOR regulator, String phoneNo) {

        String encodedPhoneNo = DecryptUtils.encode(phoneNo, brand);

        String sql = "select * from tb_sms_history where mobile_number = '" + encodedPhoneNo + "' order by gateway_sent_time desc limit 1;";
        GlobalMethods.printDebugInfo("sql: " + sql);

        db= new DbUtils(env, brand, regulator);
        JSONArray array = db.queryGlobalDB(sql);

        if(!array.isEmpty()) {
            GlobalMethods.printDebugInfo("Record found in db. Data: " + array.getJSONObject(0).getString("body") + ", \n"+ array.toJSONString());
            return String.valueOf(array.getJSONObject(0).getString("body")).replaceAll("[^0-9]","");
        }
        return null;
    }

}
