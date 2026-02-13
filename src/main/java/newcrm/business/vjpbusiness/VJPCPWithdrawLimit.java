package newcrm.business.vjpbusiness;

import newcrm.business.businessbase.CPWithdrawLimit;
import newcrm.global.GlobalMethods;
import newcrm.pages.vjpclientpages.VJPWithdrawPage;
import newcrm.utils.totp.EmailTOTP;
import org.openqa.selenium.WebDriver;
import utils.LogUtils;

public class VJPCPWithdrawLimit extends CPWithdrawLimit {

    public VJPCPWithdrawLimit(WebDriver driver){
        super(driver);
        this.wp = new VJPWithdrawPage(driver);
    }

    @Override
    public boolean authApp(String pwd ){
        try {
            gotoSecurityMageTab();
            wp.click2faEnableBtn();
            wp.clickSendCode();
            wp.inputAuthCode("999999");

            wp.waitLoading();


            String otpauthUri = wp.getQRcodefromCanvas();
            enableApp(otpauthUri);

            wp.click2faModifyBtn();
            clickPwdModifyConfirmBtn();

            inputCode(otpauthUri);

            otpauthUri = wp.getQRcodefromCanvas();

            enableApp(otpauthUri);
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
            wp.click2faEnableBtn();
            wp.clickSendCode();
            wp.inputAuthCode("999999");

            wp.waitLoading();
            otpauthUri = wp.getQRcodefromCanvas();
            enableApp(otpauthUri);

            return otpauthUri;
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("Modify 2fa app failed");
            return otpauthUri;
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
        inputPass("code",totpCode);

        confirmBtn(2);
    }
}
