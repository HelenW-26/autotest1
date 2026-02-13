package newcrm.business.vtbusiness;

import newcrm.business.businessbase.CPWithdrawLimit;
import newcrm.global.GlobalMethods;
import newcrm.pages.vtclientpages.VTWithdrawPage;
import newcrm.utils.totp.EmailTOTP;
import org.openqa.selenium.WebDriver;
import utils.LogUtils;

public class VTCPWithdrawLimit extends CPWithdrawLimit {

    public VTCPWithdrawLimit(WebDriver driver){
        super(driver);
        this.wp = new VTWithdrawPage(driver);
    }


    @Override
    public boolean modifyPwd(String oldPwd,String newPwd){
        try {

            clickPwdModifyBtn();

            wp.clickPwdModifyConfirmBtn();

            wp.waitLoading();

            inputModifyPass("currentPass",oldPwd);

            inputModifyPass("pass",newPwd);

            inputModifyPass("checkPass",newPwd);
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
    public boolean authApp(String pwd){
        try {

            wp.click2faEnableBtn();

            String otpauthUri = wp.getQRcodefromCanvas();

            enableApp(otpauthUri);
            wp.waitLoading();

            wp.click2faModifyBtn();
            wp.clickEditAuthConfirm();

            EmailTOTP emailTOTP = new EmailTOTP(otpauthUri);
            // 生成TOTP验证码
            String totpCode = emailTOTP.generateTOTP();
            wp.inputVerifyCode(totpCode);

            wp.inputOtp("999999");

            otpauthUri = wp.getQRcodefromCanvas();
            enableApp(otpauthUri);

            return true;
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("Modify 2fa app failed");
            return false;
        }

    }

    @Override
    public void inputCode(String authUri) throws Exception{

        wp.clickSendCode();

        wp.inputAuthCode("999999");

        EmailTOTP emailTOTP = new EmailTOTP(authUri);
        // 生成TOTP验证码
        String totpCode = emailTOTP.generateTOTP();

        LogUtils.info("get totp code: " + totpCode);
//        inputPass("code",totpCode);
        wp.inputAuthCode(totpCode);

    }


//    @Override
//    public String getAuthUri(String pwd ){
//        String otpauthUri = null;
//        try {
//            wp.click2faEnableBtn();
//
//            otpauthUri = wp.getQRcodefromCanvas();
//
//            enableApp(otpauthUri);
//
//            return otpauthUri;
//        } catch (Exception e) {
//            GlobalMethods.printDebugInfo("Modify 2fa app failed");
//            return otpauthUri;
//        }
//
//    }



}
