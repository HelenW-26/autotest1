package newcrm.pages.auclientpages;

import com.alibaba.fastjson.JSONObject;
import newcrm.business.dbbusiness.EmailDB;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.LoginPage;
import org.openqa.selenium.WebDriver;

import java.util.Map;

import utils.LogUtils;

public class AUPRODLoginPage extends LoginPage {
    public AUPRODLoginPage(WebDriver driver, String url) {
        super(driver, url);
    }

//    @Override
//    public boolean submit(String username, GlobalProperties.ENV dbenv, GlobalProperties.BRAND dbBrand, GlobalProperties.REGULATOR dbRegulator) {
//
//        try {
//            this.getSubmit().click();
//            this.waitLoading();
//
//            boolean bIsReqOTP = driver.getCurrentUrl().contains("emailOtpValidate");
//
//            if (bIsReqOTP && checkOTPDialog()) {
//                clickEmailCodeBtn();
//                EmailDB emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);
//                code = getCode(emailDB, username, dbenv, dbBrand, dbRegulator);
//                sendEmailCode(code);
//                clickEmailCodeSubmitBtn();
//            } else {
//                LogUtils.info("No need login otp");
//            }
//
//            this.waitvisible.until(ExpectedConditions.urlContains("vantagemarkets"));
//            this.waitLoading();
//            //driver.navigate().refresh();
//
//        } catch(Exception e){
//            System.out.println("Login Failed: "+ e.getMessage());
//            return false;
//        }
//
//        return true;
//    }

//    @Override
//    public boolean submitIB() {
//
//        try {
//            this.waitLoading();
//            this.getSubmit().click();
//            LogUtils.info("Click IB Email Login button");
//            this.waitvisible.until(ExpectedConditions.urlContains("vantagemarkets"));
//            this.waitLoading();
//            //driver.navigate().refresh();
//        }catch(Exception e){
//            System.out.println("Login Failed: "+ e.getMessage());
//            return false;
//        }
//        return true;
//    }

    public String getCode(EmailDB instance, String email, GlobalProperties.ENV dbenv, GlobalProperties.BRAND dbBrand, GlobalProperties.REGULATOR dbRegulator)
    {
        JSONObject obj = null;

        Map<String, GlobalProperties.REGULATOR> regulatorMap = Map.of(
                "vfsc", GlobalProperties.REGULATOR.VFSC,
                "vfsc2", GlobalProperties.REGULATOR.VFSC2,
                "svg", GlobalProperties.REGULATOR.SVG,
                "fsa", GlobalProperties.REGULATOR.FSA
        );

        GlobalProperties.REGULATOR regulator = regulatorMap.getOrDefault(dbRegulator.toString().toLowerCase(), null);

        try {
            if (regulator != null) {
                obj = instance.getCodeRecord(dbenv, dbBrand, regulator, email);
            } else {
                obj = instance.getCodeRecord(dbenv, dbBrand, dbRegulator, email);
            }
        } catch (Exception e) {
            LogUtils.info("An error occurred when retrieve data from db. Error Msg: " + e.getMessage());
        }

        LogUtils.info(obj.getJSONObject("vars").getString("CODE")+ ", \n"+ obj.toJSONString());
        String code = obj.getJSONObject("vars").getString("CODE");

        return code;
    }

}
