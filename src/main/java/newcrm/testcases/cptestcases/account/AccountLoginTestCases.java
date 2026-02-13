package newcrm.testcases.cptestcases.account;

import newcrm.adminapi.AdminAPIPayment;
import newcrm.business.businessbase.CPLogin;
import newcrm.factor.Factor;
import newcrm.testcases.BaseTestCaseNew;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import utils.LogUtils;

import java.util.Map;

public class AccountLoginTestCases extends BaseTestCaseNew {

    protected Object data[][];
    protected AdminAPIPayment adminPaymentAPI;
    private Factor myfactor;
    private CPLogin login;
    private static String TraderName;
    private static String TraderPass;

    @Override
    protected void login() {}

    @BeforeMethod(groups= {"CP_Email_Login", "CP_Email_TOTP_Login", "CP_Phone_Login"})
    protected void initMethod() {
        LogUtils.info("**Proceed Before Method Action**");

        // Make sure there is no active login session
        clearLoginSession();
        if (TraderName==null || TraderPass==null){
            BaseTestCaseNew.UserConfig user = getConfigNew();
            TraderName = user.TraderName;
            TraderPass = user.TraderPass;
        }
        if (myfactor == null){
            myfactor = getFactorNew();
        }
        login = myfactor.newInstance(CPLogin.class, TraderURL);
    }

    public void accountPhoneLoginFail() throws Exception {
        //        // Make sure multi-auth login 2fa is turned off.
//        if(!dbenv.equals(GlobalProperties.ENV.PROD)) {
//            adminPaymentAPI.apiDisableLogin2FA();
//        }

        // Login
        login.phoneLogin((String)data[0][9], "123Te!st");

        // Check for incorrect password message
        String alertMsg = login.getLoginAlertMsg();
        if (alertMsg != null && !alertMsg.equalsIgnoreCase("Incorrect username or password")) {
            Assert.fail("An error occurred during login using phone number. Error Msg: " + alertMsg);
        }

        System.out.println("***Test Phone Number Login Fail succeed!!********");
    }

    public void accountPhoneLoginSuccess() throws Exception {
        //        // Make sure multi-auth login 2fa is turned off.
//        if(!dbenv.equals(GlobalProperties.ENV.PROD)) {
//            adminPaymentAPI.apiDisableLogin2FA();
//        }

        phoneLoginProcess();

        System.out.println("***Test Phone Number Login Success succeed!!********");
    }

    public void accountEmailLoginFail() throws Exception {
//        // Make sure multi-auth login 2fa is turned off.
//        if(!dbenv.equals(GlobalProperties.ENV.PROD)) {
//            adminPaymentAPI.apiDisableLogin2FA();
//        }

        // Login
        login.login((String)data[0][1], "123Te!st");

        // Check for incorrect password message
        String alertMsg = login.getLoginAlertMsg();
        if (alertMsg != null && !alertMsg.equalsIgnoreCase("Incorrect username or password")) {
            Assert.fail("An error occurred during login using email. Error Msg: " + alertMsg);
        }

        System.out.println("***Test Email Login Fail succeed!!********");
    }

    public void accountEmailLoginTOTP() throws Exception {

        // No need 2FA to be turned on in admin for TOTP works
        // Email Login Process
        emailLoginProcess();

        // Email TOTP 2FA Process
        emailTOTP2FAProcess();

//        try {
//            // Make sure multi-auth login 2fa is turned on.
//            if(!dbenv.equals(GlobalProperties.ENV.PROD)) {
//                boolean bIsExists2FA = adminPaymentAPI.apiEnableLogin2FA();
//
//                if (!bIsExists2FA) {
//                    Assert.fail("Login 2FA not found. Please manual check in Admin Portal");
//                }
//            }
//
//            // Email Login Process
//            emailLoginProcess();
//
//            // Email TOTP 2FA Process
//            emailTOTP2FAProcess();
//
//        } finally {
//            // Make sure multi-auth login 2fa is turned off.
//            if(!dbenv.equals(GlobalProperties.ENV.PROD)) {
//                adminPaymentAPI.apiDisableLogin2FA();
//            }
//        }

        System.out.println("***Test Email TOTP Login succeed!!********");
    }

    private void phoneLoginProcess() throws Exception {
        login.phoneLogin((String)data[0][9], TraderPass);

        // Check for login error message
        String loginErrMsg = login.checkExistsLoginAlertMsg();

        if (loginErrMsg != null) {
            Assert.fail("An error occurred during login using phone number. Error Msg: " + loginErrMsg);
        }

        // Check login success
        Map.Entry<Boolean, String> checkLoginSuccessResp = login.checkLoginSuccess();
        if (!checkLoginSuccessResp.getKey()) {
            Assert.fail("Login failed using phone number. Error Msg: " + checkLoginSuccessResp.getValue());
        }
    }

    private void emailLoginProcess() throws Exception {
        login.login((String)data[0][1], TraderPass);

        // Check for login error message
        String loginErrMsg = login.checkExistsLoginAlertMsg();

        if (loginErrMsg != null) {
            Assert.fail("An error occurred during login using email. Error Msg: " + loginErrMsg);
        }

        // Check redirection to TOTP authentication page
        login.checkRedirectToEmailTOTPPage();
    }

    private void emailTOTP2FAProcess() throws Exception {
        // Email Login TOTP Authentication
        login.emailTOTPAuth((String)data[0][10]);
    }

}
