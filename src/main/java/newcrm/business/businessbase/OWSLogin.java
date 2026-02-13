package newcrm.business.businessbase;

import newcrm.pages.clientpages.LoginPage;
import newcrm.pages.owspages.OWSLoginPage;
import newcrm.utils.totp.EmailTOTP;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.Map;

public class OWSLogin {

	protected OWSLoginPage login;

	public OWSLogin(WebDriver driver, String url)
	{
		this.login = new OWSLoginPage(driver,url);
	}

    public OWSLogin(OWSLoginPage login) {
        this.login = login;
    }

    public Map.Entry<Boolean, String> checkLoginSuccess() {
        return login.checkLoginSuccess();
    }

    public void login(String username, String password) {
        login.owsLogin(username, password);

        String loginErrMsg = login.checkExistsLoginAlertMsg();

        if (loginErrMsg != null) {
            Assert.fail("An error occurred during login. Error Msg: " + loginErrMsg);
        }

        // Check login success
        Map.Entry<Boolean, String> checkLoginSuccessResp = login.checkLoginSuccess();
        if (!checkLoginSuccessResp.getKey()) {
            Assert.fail("Login failed. Error Msg: " + checkLoginSuccessResp.getValue());
        }
    }

}
