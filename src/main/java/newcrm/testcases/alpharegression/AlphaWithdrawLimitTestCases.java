package newcrm.testcases.alpharegression;

import com.alibaba.fastjson.JSONObject;
import newcrm.adminapi.AdminAPIPayment;
import newcrm.cpapi.CPAPIAccount;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.TestDataProvider;
import newcrm.testcases.cptestcases.WithdrawLimitTestCases;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import utils.LogUtils;
import vantagecrm.DBUtils;

import java.util.HashMap;
import java.util.Map;

import static newcrm.global.GlobalProperties.*;
import static newcrm.global.GlobalProperties.brand;
import static newcrm.utils.testCaseDescUtils.*;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

public class AlphaWithdrawLimitTestCases  extends WithdrawLimitTestCases {

    Object data[][] = null;
    @Override
    public void beforMethod(@Optional("alpha")String TestEnv, @Optional("False") String headless, String Brand, String Regulator,
                            @Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
                            @Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug, @Optional("")String server,
                            ITestContext context) {
        launchBrowser( TestEnv,  headless,  Brand,  Regulator, TraderURL, TraderName, TraderPass, AdminURL,  AdminName, AdminPass,  Debug, context);
    }

    @BeforeClass(alwaysRun = true)
    @Parameters(value= {"Brand","Server"})
    public void initiEnv(String brand,String server, ITestContext context) throws Exception{
        brand = GlobalMethods.setEnvValues(brand);
        data = TestDataProvider.getAlphaWithdrawLimitUsersData(brand, server);
        assertNotNull(data);

        adminPaymentAPI = new AdminAPIPayment((String) data[0][4], GlobalProperties.REGULATOR.valueOf((String)data[0][0]),(String)data[0][7],(String)data[0][8],GlobalProperties.BRAND.valueOf(brand.toUpperCase()), GlobalProperties.ENV.valueOf("ALPHA"));

        ruleArray = adminPaymentAPI.apiQuerySecurityRule();

        String ruleId = getRuleId(pwdCode);
        adminPaymentAPI.apiSwitchWithdrawLimits(ruleId, WithdrawLimitTestCases.limitSwitchOff);

        ruleId = getRuleId(loginCode);
        adminPaymentAPI.apiSwitchWithdrawLimits(ruleId, WithdrawLimitTestCases.limitSwitchOff);

        ruleId = getRuleId(authCode);
        adminPaymentAPI.apiSwitchWithdrawLimits(ruleId, WithdrawLimitTestCases.limitSwitchOff);

        cpapiAccount = new CPAPIAccount((String) data[0][3], (String) data[0][1], (String) data[0][2]);

        testData = new HashMap<>();
        testData.put("url",(String) data[0][3]);
        cpapiAccount.homeQueryMetaTraderAccountOverview(testData);


        launchBrowser("alpha","true",brand,(String)data[0][0],(String)data[0][3],(String)data[0][1],(String)data[0][2],"","","","True",context);
    }

    @Test(priority = 2, description = "Mandatory enable 2FA when withdrawal")
    @Parameters(value= {"Brand"})
    public void judgeMsgEnable2fa(){
        try {

            adminPaymentAPI.disable2FA(userId());
            DBUtils.updateSafeValidate("ALPHA", GlobalMethods.getBrand(), (String)data[0][0],userId());
            GlobalProperties.DEPOSITMETHOD method = null;

            String accountNumber= null;

            if("AU".equalsIgnoreCase(brand) || "VFX".equalsIgnoreCase(brand)){
                method = DEPOSITMETHOD.CRYPTOUSDCSQL;
                accountNumber = "TQETUR38cL8nY1gQcr43sWgFFwTKSwQjLw";
            }else if("VT".equalsIgnoreCase(brand)){
                method = GlobalProperties.DEPOSITMETHOD.ETH_New;
                accountNumber = "0xf12f064b0e122899340268e3fd7af23559962285";
            } else if("UM".equalsIgnoreCase(brand)){
                method = GlobalProperties.DEPOSITMETHOD.CRYPTOTRCNew;
                accountNumber = "TQETUR38cL8nY1gQcr43sWgFFwTKSwQjLw";
            }else {
                method = GlobalProperties.DEPOSITMETHOD.ETH_New;
                accountNumber = "0x5b2a0c329e75be7f0f97c9ff4184d8dd843b15ba";
            }
            this.judgeMsgEnable2fa(data,method,accountNumber);
        } catch (Exception e) {
            fail();
            LogUtils.info("Account detected with remote login activity; enable 2fa failed. ");
        }



    }

    @Test(priority = 2, description = WITHDRAW_SECURITY_LIMIT_RESET_PWD)
    @Parameters(value= {"Brand"})
    public void limitWithResetPwd(){
        String ruleId = getRuleId(pwdCode);
        adminPaymentAPI.apiSwitchWithdrawLimits(ruleId, WithdrawLimitTestCases.limitSwitchOn);

        GlobalProperties.DEPOSITMETHOD method = null;
        if("UM".equalsIgnoreCase(brand)){
            method = GlobalProperties.DEPOSITMETHOD.NETELLER;
        }else if("PUG".equalsIgnoreCase(brand)){
            method = GlobalProperties.DEPOSITMETHOD.CRYPTOBTC;
        }else if("MO".equalsIgnoreCase(brand)){
            method = GlobalProperties.DEPOSITMETHOD.MALAYINSTANT;
        }
        else {
            method = GlobalProperties.DEPOSITMETHOD.ETH_New;
        }


        limitWithResetPwd(data,method,ruleId,"ALPHA");
    }


    @Test(priority = 2, description = WITHDRAW_SECURITY_LIMIT_UNBIND_2FA)
    @Parameters(value= {"Brand"})
    public void limitWithUnbind2fa(String brand){
        String ruleId = getRuleId(authCode);
        adminPaymentAPI.apiSwitchWithdrawLimits(ruleId, WithdrawLimitTestCases.limitSwitchOn);

        GlobalProperties.DEPOSITMETHOD method = null;
        if("AU".equalsIgnoreCase(brand) || "VFX".equalsIgnoreCase(brand)){
            method = DEPOSITMETHOD.CRYPTOTRCNew;
        }else if("vjp".equalsIgnoreCase(brand) ) {
            method = DEPOSITMETHOD.CRYPTOBTCNEW;
        }else if("UM".equalsIgnoreCase(brand) ) {
            method = DEPOSITMETHOD.NETELLER;
        }else{
            method = DEPOSITMETHOD.ETH_New;
        }
        limitWithUnbind2fa(data,method, ruleId);

    }

    @Test(priority = 2, description = WITHDRAW_SECURITY_LIMIT_MODIFY_2FA)
    @Parameters(value= {"Brand"})
    public void limitWithModify2fa(String brand){
        String ruleId = getRuleId(authCode);
        adminPaymentAPI.apiSwitchWithdrawLimits(ruleId, WithdrawLimitTestCases.limitSwitchOn);
        GlobalProperties.DEPOSITMETHOD method = null;
        if("AU".equalsIgnoreCase(brand) || "VFX".equalsIgnoreCase(brand)){
            method = DEPOSITMETHOD.CRYPTOTRCNew;
        }else  if("vjp".equalsIgnoreCase(brand) ) {
            method = DEPOSITMETHOD.CRYPTOBTCNEW;
        }else if("UM".equalsIgnoreCase(brand) ) {
            method = DEPOSITMETHOD.NETELLER;
        }else{
            method = DEPOSITMETHOD.ETH_New;
        }
        try {
            DBUtils.updateSafeValidate("ALPHA", GlobalMethods.getBrand(), (String)data[0][0],userId());
            adminPaymentAPI.disable2FA(userId());
            limitWithModify2fa(data,method,ruleId);
        } catch (Exception e) {
            fail();
            LogUtils.info("Account detected with remote login activity; enable 2fa failed. ");
        }


    }

    @Test(priority = 2, description = WITHDRAW_SECURITY_LIMIT_MODIFY_PHONE)
    @Parameters(value= {"Brand"})
    public void limitWithModifyPhone(String brand){
        String ruleId = getRuleId(loginCode);
        adminPaymentAPI.apiSwitchWithdrawLimits(ruleId, WithdrawLimitTestCases.limitSwitchOn);

        GlobalProperties.DEPOSITMETHOD method = null;
        if("AU".equalsIgnoreCase(brand) || "VFX".equalsIgnoreCase(brand)){
            method = GlobalProperties.DEPOSITMETHOD.CRYPTOTRCNew;
            limitWithModifyPhone(data,method,ruleId);
        }else{
//            method = DEPOSITMETHOD.MALAYINSTANT;
            throw new SkipException("Skipping this test intentionally.");
        }


    }

    @Test(priority = 2, description = WITHDRAW_SECURITY_LIMIT_MODIFY_PWD)
    @Parameters(value= {"Brand"})
    public void limitWithModifyPwd(String brand){
        String ruleId = getRuleId(pwdCode);
        adminPaymentAPI.apiSwitchWithdrawLimits(ruleId, WithdrawLimitTestCases.limitSwitchOn);

        GlobalProperties.DEPOSITMETHOD method = null;
        if("UM".equalsIgnoreCase(brand)){
            method = GlobalProperties.DEPOSITMETHOD.NETELLER;
        }else if("PUG".equalsIgnoreCase(brand)){
            method = GlobalProperties.DEPOSITMETHOD.CRYPTOBTC;
        }else if("MO".equalsIgnoreCase(brand)){
            method = GlobalProperties.DEPOSITMETHOD.MALAYINSTANT;
        }
        else {
            method = GlobalProperties.DEPOSITMETHOD.ETH_New;
        }

        limitWithModifyPwd(data,method,ruleId);
    }

    @Test(priority = 2, description = WITHDRAW_SECURITY_LIMIT_MODIFY_EMAIL)
    @Parameters(value= {"Brand"})
    public void limitWithModifyEmail(String brand){
        String ruleId = getRuleId(loginCode);
        adminPaymentAPI.apiSwitchWithdrawLimits(ruleId, WithdrawLimitTestCases.limitSwitchOn);

        GlobalProperties.DEPOSITMETHOD method = null;
        if("UM".equalsIgnoreCase(brand)){
            method = GlobalProperties.DEPOSITMETHOD.NETELLER;
        }else if("PUG".equalsIgnoreCase(brand) || "MO".equalsIgnoreCase(brand)){
            method = GlobalProperties.DEPOSITMETHOD.CRYPTOBTC;
        }else{
            method = GlobalProperties.DEPOSITMETHOD.ETH_New;
            adminPaymentAPI.disable2FA(userId());
        }
        limitWithModifyEmail(data,brand,method,ruleId,"ALPHA");
    }

    @Test(priority = 2, description = WITHDRAW_CREDIT_DIALOG)
    @Parameters(value= {"Brand"})
    public void withdrawCredit(){

        super.withdrawCredit(data);
    }
}
