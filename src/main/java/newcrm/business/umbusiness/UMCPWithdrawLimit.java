package newcrm.business.umbusiness;

import newcrm.business.businessbase.CPWithdrawLimit;
import newcrm.global.GlobalMethods;
import newcrm.pages.umclientpages.UMWithdrawPage;
import newcrm.utils.totp.EmailTOTP;
import org.openqa.selenium.WebDriver;
import utils.LogUtils;

public class UMCPWithdrawLimit extends CPWithdrawLimit {

    public UMCPWithdrawLimit(WebDriver driver){
        super(driver);
        this.wp = new UMWithdrawPage(driver);
    }


    @Override
    public boolean authApp(String pwd ){
        try {
            gotoSecurityMageTab();
            wp.click2faEnableBtn();

//            wp.clickSendCode();
//            inputPass("code","999999");
//            confirmBtn(2);
//            wp.waitLoading();
            String otpauthUri = wp.getQRcodefromCanvas();
            enableApp(otpauthUri);

            wp.click2faModifyBtn();
            wp.clickPwdModifyConfirmBtn();

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

        EmailTOTP emailTOTP = new EmailTOTP(authUri);
        // 生成TOTP验证码
        String totpCode = emailTOTP.generateTOTP();

        LogUtils.info("get totp code: " + totpCode);
        wp.inputAuthCode(totpCode);

        wp.clickSendCode();

        wp.inputAuthCode("999999");
    }

    @Override
    public String getAuthUri(String pwd ){
        String otpauthUri = null;
        try {
            wp.click2faEnableBtn();
            otpauthUri = wp.getQRcodefromCanvas();
            enableApp(otpauthUri);

            return otpauthUri;
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("Modify 2fa app failed");
            return otpauthUri;
        }

    }
}
