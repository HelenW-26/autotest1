package newcrm.testcases.cptestcases.account;

import newcrm.business.businessbase.CPLogin;
import newcrm.business.businessbase.CPMenu;
import newcrm.business.businessbase.account.CPForgotPwd;
import newcrm.business.dbbusiness.EmailDB;
import newcrm.business.dbbusiness.SMSDB;
import newcrm.cpapi.APIThirdPartyService;
import newcrm.factor.Factor;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.testcases.BaseTestCaseNew;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import utils.LogUtils;

import java.util.Arrays;
import java.util.List;

public class AccountForgotPwdTestCases extends BaseTestCaseNew {

    protected Object data[][];
    private Factor myfactor;
    protected List<String> forgotPwdExcludeBrands = Arrays.asList(BRAND.STAR.toString());
    private String countryCode, phoneNo;
    private CPForgotPwd forgotPwd;
    private CPLogin login;
    private static String TraderName;
    private static String TraderPass;

    @Override
    protected void login() {}

    @BeforeMethod(groups= {"CP_Email_Forgot_Pwd", "CP_Phone_Forgot_Pwd"})
    protected void initMethod() {
        LogUtils.info("**Proceed Before Method Action**");

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

    public void emailForgotPwd() throws Exception {
        forgotPwd = myfactor.newInstance(CPForgotPwd.class);

        // Forgot Password submission
        emailForgotPwdProcess();

        // Reset Password via reset password link
        emailResetPwdProcess();

        System.out.println("***Test Email Forgot Password succeed!!********");
    }

    protected void emailForgotPwdProcess() throws Exception {
        forgotPwd.emailForgotPwd(TraderName, TraderURL);
    }

    protected void emailResetPwdProcess() throws Exception {

        String resetPwdLink = getEmailResetPwdLink();

        // Redirect to reset password link
        forgotPwd.goEmailResetPwdPage(resetPwdLink.trim(), TraderURL);

        // Check current selected Language
        CPMenu menu = myfactor.newInstance(CPMenu.class);
        menu.changeLanguage("English");

        // Reset Password
        forgotPwd.emailResetPwd(TraderPass);
    }

    protected String getEmailResetPwdLink() throws Exception {
        String resetPwdLink = null;

        // Get reset password link from db email content
        try {
            LogUtils.info("Going to get reset password link from database");
            EmailDB emailDB = new EmailDB(dbenv, dbBrand, dbRegulator);
            resetPwdLink = emailDB.getResetPwdLink(dbenv, dbBrand, dbRegulator, TraderName, "CHANGE_LINK");

            if (resetPwdLink == null) {
                Assert.fail("No email reset password link found in db. Kindly check.");
            } else if (resetPwdLink.isEmpty()) {
                Assert.fail("Email Reset Password link value is empty");
            }

        } catch (Exception ex) {
            Assert.fail("No email reset password link found in db. Kindly check.");
        }

        return resetPwdLink;
    }

    public void phoneForgotPwd() throws Exception {
        forgotPwd = myfactor.newInstance(CPForgotPwd.class);

        try{
            // Forgot Password submission
            phoneForgotPwdProcess();

            // Close dialog
            if (dbBrand.toString().equalsIgnoreCase(BRAND.VT.toString())) {
                CPMenu menu = myfactor.newInstance(CPMenu.class);
                menu.closeBannerDialogB4Login();
            }

            phoneResetPwdProcess();

        } finally {
            clearLoginSession();
        }

        System.out.println("***Test Phone Number Forgot Password succeed!!********");
    }

    protected void phoneForgotPwdProcess() throws Exception {
        // Get phone number and country code
        String[] val = forgotPwd.getPhoneNoFormat((String)data[0][9]);
        countryCode = val[0].trim();
        phoneNo = val[1].trim();

        // Request for phone OTP
        forgotPwd.requestPhoneOTP(countryCode, phoneNo, TraderURL);

        Thread.sleep(5000);
        LogUtils.info("Wait 5 seconds before retrieving the phone OTP..");

        // Get phone OTP
        String code = getPhoneOTP();

        // Forgot Password Submission
        forgotPwd.phoneForgotPwd(code.trim());
    }

    protected void phoneResetPwdProcess() throws Exception {
        // Reset Password
        forgotPwd.phoneResetPwd(TraderPass);
    }

    protected String getPhoneOTP() throws Exception {
        String code = null;

        // Get otp code from db sms history
        try {
            LogUtils.info("Going to get phone OTP from database");
            // Get otp code from db sms history
            SMSDB smsDB = new SMSDB(dbenv, dbBrand, dbRegulator);
            code = smsDB.getOTP(dbenv, dbBrand, dbRegulator, phoneNo);

        } catch (Exception ex) {
            Assert.fail("An error occurred when retrieving phone OTP from db. Error Msg: " + ex.getMessage());
        }

        // Applicable for AU only.
        if (dbBrand.toString().equalsIgnoreCase(BRAND.VFX.toString()) && code == null) {
            // Get otp code from 3rd Party Message Center
            try {
                LogUtils.info("Going to get phone OTP from 3rd Party Message Center");

                // FYI
                // Apollo Settings: gold.account.opening.process.enable (regulator-function), gold.test.userIdListWhite (biz-data)
                // When regulator is set to true, get OTP from third party.
                // When regulator is set to false, get OTP from third party if user id is in gold user id whitelist, else get from db sms history table.
                //登录信息中心，获取手机OTP
                APIThirdPartyService ApiThirdPartyService = new APIThirdPartyService(dbenv, dbBrand);
                ApiThirdPartyService.apiMsgCenterGetLoginToken();
                String countryId = ApiThirdPartyService.apiGetCountryID(countryCode);
                code = ApiThirdPartyService.apiSearchSentOTPRecord(phoneNo, countryId);

            } catch (Exception ex) {
                ex.printStackTrace();
                Assert.fail("Failed to get phone OTP from 3rd Party Message Center. Kindly check Message Center manually.");
            }
        }

        if (code == null) {
            Assert.fail("No phone OTP record found in 3rd Party Message Center");
        } else if (code.isEmpty()) {
            Assert.fail("Phone OTP record exists in 3rd Party Message Center but contains no value");
        }

        return code;
    }

}
