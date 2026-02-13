package newcrm.business.businessbase;

import newcrm.global.GlobalProperties.PORTAL_LANGUAGE;
import org.openqa.selenium.WebDriver;

import newcrm.pages.clientpages.LoginPage;
import newcrm.utils.totp.EmailTOTP;
import org.testng.Assert;
import tools.ScreenshotHelper;
import utils.LogUtils;

import java.util.Map;

public class CPLogin {
	
	protected LoginPage login;

	public CPLogin(LoginPage login) {
		this.login = login;
	}
	
	
	public CPLogin(WebDriver driver,String url) 
	{
		this.login = new LoginPage(driver,url);
	}

	public void inputUserName(String username) {

		login.setUserName(username);
		
	}

	public String checkExistsLoginAlertMsg() {
		return login.checkExistsLoginAlertMsg();
	}

	public String getLoginAlertMsg() {
		// Capture ss
		ScreenshotHelper.takeFullPageScreenshot(login.getDriver(), "screenshots", "Login");
		return login.getLoginAlertMsg();
	}

	public Map.Entry<Boolean, String> checkLoginSuccess() {
		return login.checkLoginSuccess();
	}

	public void inputPassWord(String password) {
		login.setPassWord(password);
	}
	
	public void submitOld() {
		login.submitOld();
	}
	
	/***
	 * 
	 * @param username user's email address
	 * @param password passowrd
	 * @return return true if succeed， otherwise false
	 */
	public boolean loginOld(String username, String password) {
		login.setUserName(username);
		login.setPassWord(password);

		return login.submitOld();
	}

	public void login(String username, String password) {
//		login.changeLanguage(PORTAL_LANGUAGE.ENGLISH);
		login.waitLoading();
		login.setUserName(username);
		login.setPassWord(password);
		login.submit();
	}

	public void phoneLogin(String username, String password) {
//		login.changeLanguage(PORTAL_LANGUAGE.ENGLISH);
		login.waitLoading();
		login.clickPhonePanel();
		login.setLoginPhoneNo(username);
		login.setLoginPhonePassword(password);
		login.submitPhoneLogin();
	}

	public void loginIB(String username, String password) {
//		login.changeLanguage(PORTAL_LANGUAGE.ENGLISH);
		login.waitLoading();
		login.setUserName(username);
		login.setPassWord(password);
		login.submitIB();
	}
	
	public boolean logout() {
		return login.logout();
	}
	
	public void goToCpHome() {
		login.gotoCpIndex();
	}
	
	public boolean isLogin() {
		return login.isLogin();
	}

	public void checkRedirectToEmailTOTPPage () {
		login.checkRedirectToEmailTOTPPage();
	}

	public void emailTOTPAuth(String url) throws Exception {
		EmailTOTP totpGenerator = new EmailTOTP(url);

		login.waitLoadingEmailTOTPContent();

		for (int attempt = 1; attempt <= 3; attempt++) {

			LogUtils.info("Generate TOTP on attempt " + attempt);

			// Generate email TOTP
			String code = totpGenerator.generateTOTP();

			login.setEmailTOTP(code);

			// Submit TOTP
			login.submitEmailTOTP();

			// Check exists totp validation message - TOTP might be already invalid or expired
			String invalidMsg = login.checkExistsLoginAuthInvalidMsg();

			if (invalidMsg != null && !invalidMsg.isEmpty()) {
				if (invalidMsg.toLowerCase().contains("Verification expired".toLowerCase()) || invalidMsg.toLowerCase().contains("Invalid verification code".toLowerCase())) {
					LogUtils.info("Generated TOTP expired or invalid. Regenerate TOTP again...");
					continue;
				} else {
					Assert.fail("Email Login TOTP Authentication failed. Error Msg: " + invalidMsg);
				}
			}

			break;
		}

		// Check authentication success
		login.checkEmailAuthSuccess();
	}

}
