package newcrm.business.ptbusiness.register;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import newcrm.business.businessbase.CPRegister;
import newcrm.business.dbbusiness.EmailDB;
import newcrm.business.dbbusiness.PTDB;
import newcrm.business.dbbusiness.UsersDB;
import newcrm.global.GlobalProperties;
import newcrm.pages.ptclientpages.register.PTAccountConfigurationPage;
import newcrm.pages.ptclientpages.register.PTRegisterEntryPage;
import org.openqa.selenium.WebDriver;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class PTCPRegister extends CPRegister {

    public PTCPRegister(WebDriver driver, String url) {
        super(driver,url);
    }
    protected void setUpEntrypage() {
        entrypage  = new PTRegisterEntryPage(driver);
    }

    protected void setUpACpage() {
        acpage = new PTAccountConfigurationPage(driver);
    }

    @Override
    public boolean fillAccountPage(GlobalProperties.PLATFORM platform, GlobalProperties.ACCOUNTTYPE accountType, GlobalProperties.CURRENCY currency, String balance) {
        userdetails.put("Account Type", acpage.setAccountType(accountType));
        userdetails.put("Currency", acpage.setCurrency(currency));
        userdetails.put("Balance", acpage.setBalance(balance));
        userdetails.put("registrationFee", acpage.getPTRefistrationFee());
        acpage.tickBox();
        return true;
    }

    //get userID, password and sessionNumber
    public void checkUserInfo(String email, GlobalProperties.ENV env, GlobalProperties.BRAND brand, GlobalProperties.REGULATOR regulator) {
        UsersDB db = new UsersDB();
        PTDB ptdb = new PTDB();
        EmailDB emailDB = new EmailDB(env, brand, regulator);

        JSONArray jsArray = db.getPTUserRegistrationInfo(email, env, brand, regulator);
        assertNotNull(jsArray);
        assertTrue(jsArray.size() >0,"Do not find the user by email: " + email);

        JSONObject obj = jsArray.getJSONObject(0);
        userdetails.put("UserID", obj.getString("id"));

        JSONArray jsArrayPwd = emailDB.getVarFromEmailDB(env, brand, regulator,email);
        JSONObject objPwd = jsArrayPwd.getJSONObject(0);

        String vars = objPwd.getString("vars");
        if(vars!=null)
        {
            JSONObject varsJson = JSON.parseObject(vars);
            userdetails.put("Password", varsJson.getString("PASSWORD"));
        }

        JSONArray jsArraySession = ptdb.getSessionFromDB(env, brand, regulator,userdetails.get("UserID"));
        JSONObject jsSessionNum = jsArraySession.getJSONObject(0);

        String sessionNum = jsSessionNum.getString("session_number");
        userdetails.put("sessionNum",sessionNum);


    }
}
