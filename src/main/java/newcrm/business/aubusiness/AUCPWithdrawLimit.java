package newcrm.business.aubusiness;

import newcrm.business.businessbase.CPWithdrawLimit;
import newcrm.global.GlobalMethods;
import newcrm.pages.auclientpages.AuWithdrawPage;
import newcrm.utils.totp.EmailTOTP;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import utils.LogUtils;

public class AUCPWithdrawLimit extends CPWithdrawLimit {

    String code = "999999";

    private static String optUrl ;

    public AUCPWithdrawLimit(WebDriver driver){
        super(driver);
        this.wp = new AuWithdrawPage(driver);
    }

    @Override
    public boolean modifyPwd(String oldPwd,String newPwd){
        try {
            gotoSecurityMageTab();


            clickPwdModifyBtn();

            wp.inputOtp(code);

            wp.waitLoading();

            inputPass("currentPass",oldPwd);

            inputPass("pass",newPwd);

            inputPass("checkPass",newPwd);
            confirmBtn(1);
            wp.waitLoading();
            closePwdUpdateDialog();

            GlobalMethods.printDebugInfo("modify pwd success !!! new pwd:  " + newPwd);

            return true;
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("Modify password failed");
            return false;
        }

    }

    @Override
    public boolean modifyEmail( String pwd,String newEmail){
        try {
            gotoSecurityMageTab();

            String otpauthUri = null;

            boolean flag = wp.click2faEnableBtn();
            if(flag){
                inputPass("password",pwd );
                wp.clickPwdModifyConfirmBtn();
                otpauthUri = wp.getQRcodefromCanvas();
                enableAuthapp(otpauthUri);
                LogUtils.info("enable auth app success");
            }

            wp.clickEmailModifyBtn();


            EmailTOTP emailTOTP = new EmailTOTP(otpauthUri);
            // 生成TOTP验证码
            String totpCode = emailTOTP.generateTOTP();
            LogUtils.info("code is: " + totpCode);
            inputPass("code",totpCode);
            wp.waitLoading();
            confirmBtn(2);
            wp.inputOtp(code);

            inputPass("email",newEmail);
            inputPass("password",pwd);
            wp.clickPwdModifyConfirmBtn();
            LogUtils.info("update email success!");
            wp.waitLoading();

            wp.inputOtp(code);

            closePwdUpdateDialog();
            return true;
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("Modify email failed");
            return false;
        }

    }

    @Override
    public boolean modifyPhone( String pwd,String phone){
        try {
            gotoSecurityMageTab();
            wp.clickPhoneModifyBtn();

            wp.inputOtp(code);


            wp.typePhone(phone);
            inputPass("password",pwd);
            wp.clickPwdModifyConfirmBtn();
            LogUtils.info("modify phone success!");
            wp.waitLoading();

            wp.inputOtp("123456");

            closePwdUpdateDialog();

            return true;
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("Modify email failed");
            return false;
        }

    }

    @Override
    public boolean authApp(String pwd ){
        try {
            gotoSecurityMageTab();
            String otpauthUri = null;

            boolean flag = wp.click2faEnableBtn();
            EmailTOTP emailTOTP = null;
            if(flag){
                inputPass("password",pwd );
                wp.clickPwdModifyConfirmBtn();
                otpauthUri = wp.getQRcodefromCanvas();
                enableAuthapp(otpauthUri);
            }

            wp.click2faModifyBtn();
            if(StringUtils.isBlank(otpauthUri)){
                otpauthUri =  wp.readFile();
            }
            emailTOTP = new EmailTOTP(otpauthUri);
            inputPass("code",emailTOTP.generateTOTP());
            confirmBtn(2);

            wp.inputOtp(code);

            optUrl = wp.getQRcodefromCanvas();
            enableAuthapp(optUrl);
            return true;
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("Modify 2fa app failed");
            return false;
        }

    }

    @Override
    public String getAuthUri(String pwd ){
        String otpauthUri = null;
        try {
            wp.waitLoading();
            wp.click2faEnableBtn();
            wp.waitLoading();
            inputPass("password",pwd );
            wp.clickPwdModifyConfirmBtn();
            otpauthUri = wp.getQRcodefromCanvas();
            enableAuthapp(otpauthUri);

            return otpauthUri;
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("Modify 2fa app failed");
            return otpauthUri;
        }

    }

    private void enableAuthapp(String otpauthUri) throws Exception{


        EmailTOTP emailTOTP = new EmailTOTP(otpauthUri);
        // 生成TOTP验证码
        String totpCode = emailTOTP.generateTOTP();

        inputPass("verificationCode",totpCode);

        wp.clickConfirmDisclaimer();
        wp.clickSubmitAuth();
        closePwdUpdateDialog();

        LogUtils.info("new auth url is : " + otpauthUri);

    }
}
