package newcrm.business.businessbase;

import com.alibaba.fastjson.JSONObject;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.WithdrawPage;
import newcrm.utils.totp.EmailTOTP;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.LogUtils;
import vantagecrm.DBUtils;

import java.util.Map;

public class CPWithdrawLimit {


    private static final Logger log = LoggerFactory.getLogger(CPWithdrawLimit.class);
    protected WithdrawPage wp;

    public CPWithdrawLimit(WebDriver driver) {
        this.wp = new WithdrawPage(driver);
    }


    public boolean modifyPwd(String oldPwd,String newPwd){
        try {
            gotoSecurityMageTab();

            clickPwdModifyBtn();

            clickPwdModifyConfirmBtn();

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

    public boolean modifyEmail( String pwd,String newEmail){
        try {
            gotoSecurityMageTab();
            wp.clickEmailModifyBtn();

            clickPwdModifyConfirmBtn();
            inputPass("currentPass",pwd);
            inputPass("email",newEmail);
            wp.clickSendCode();

            inputPass("code","999999");
            confirmBtn(2);
            wp.waitLoading();
            closePwdUpdateDialog();
            return true;
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("Modify email failed");
            return false;
        }

    }

    public boolean modifyPhone( String pwd,String phone){
        try {
            gotoSecurityMageTab();
            wp.clickPhoneModifyBtn();

            clickPwdModifyConfirmBtn();

            return true;
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("Modify email failed");
            return false;
        }

    }


    public String getAuthUri(String pwd ){
        String otpauthUri = null;
        try {
            wp.click2faEnableBtn();

            wp.waitLoading();
            otpauthUri = wp.getQRcodefromCanvas();
            enableApp(otpauthUri);

            return otpauthUri;
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("Modify 2fa app failed");
            return otpauthUri;
        }

    }


    public boolean authApp(String pwd ){
        try {
            gotoSecurityMageTab();
            wp.click2faEnableBtn();

            wp.waitLoading();
            String otpauthUri = wp.getQRcodefromCanvas();
            enableApp(otpauthUri);

            wp.click2faModifyBtn();
            clickPwdModifyConfirmBtn();

            otpauthUri = wp.getQRcodefromCanvas();

            enableApp(otpauthUri);
            return true;
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("Modify 2fa app failed");
            return false;
        }

    }

    public void inputCode(String authUri) throws Exception{
        EmailTOTP emailTOTP = new EmailTOTP(authUri);
        // 生成TOTP验证码
        String totpCode = emailTOTP.generateTOTP();

        LogUtils.info("get totp code: " + totpCode);
        inputPass("code",totpCode);

        confirmBtn(2);
    }



    public void enableApp(String otpauthUri) throws Exception{
        if (otpauthUri.isEmpty()){
            LogUtils.info("get opt image failed");
            return;
        }

        EmailTOTP emailTOTP = new EmailTOTP(otpauthUri);
        // 生成TOTP验证码
        String totpCode = emailTOTP.generateTOTP();

        wp.waitLoading();
        LogUtils.info("Authenticator url is: " + otpauthUri);
        inputPass("verificationCode",totpCode);

        wp.clickConfirmDisclaimer();
        wp.clickSubmitAuth();

        closePwdUpdateDialog();
    }

    public String getEmailVerifyCode(String brand,String regulator,String email,String env){
        try {
            String vars = DBUtils.funcReadOTPInBusinessDB(env, brand, regulator, email);

            JSONObject js = JSONObject.parseObject(vars);
            JSONObject obj = js.getJSONObject("vars");

            return obj.getString("CODE");

        } catch (Exception e) {
            GlobalMethods.printDebugInfo("get email verify code failed from DB");
        }
        return null;
    }

    public String getLimitMsg(){
        return wp.getLimitMsg();
    }

    public void clickPwdModifyConfirmBtn(){
        wp.clickPwdModifyConfirmBtn();
    }

    public void clickVerify2faConfirmBtn(){
        wp.clickVerify2faConfirmBtn();
    }

    public String getVerifyMsgEnable2FA(){
        return wp.getVerifyMsgEnable2FA();
    }

    public void clickUnderstanBtn(){

        wp.clickIUnderstandBtn();
    }

    public void gotoSecurityMageTab(){
        wp.goToSecurityMageTab();
    }

    public void clickPwdModifyBtn(){
        wp.clickPwdModifyBtn();
    }

    public void confirmBtn(int index){
        wp.clickConfirmBtn(index);
    }

    public void closePwdUpdateDialog(){
        wp.closePwdUpdateDialog();

    }
    public void inputPass(String pwdId,String pwd){
        wp.inputPass(pwdId,pwd);
    }
    public void inputModifyPass(String pwdId,String pwd){
        wp.inputModifyPass(pwdId,pwd);
    }

    public void clickCreditOkBtn(){
        wp.creditOK();
    }

    public void submitWithdraw(){
        wp.submitWithoutCheck();
    }

    public Map<String,Object> getCreditMsg(){
        return wp.getCreditMsg();
    }

    public void refresh(){
        wp.refresh();
    }
}
