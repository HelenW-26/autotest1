package newcrm.business.businessbase;

import newcrm.pages.dappages.DAPLoginPage;
import newcrm.pages.owspages.OWSLoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.Map;

public class DAPLogin {

	protected DAPLoginPage login;

	public DAPLogin(WebDriver driver, String url)
	{
		this.login = new DAPLoginPage(driver,url);
	}

    public DAPLogin(DAPLoginPage login) {
        this.login = login;
    }

    public Map.Entry<Boolean, String> checkLoginSuccess() {
        return login.checkLoginSuccess();
    }

    public void login(String username, String password) {
        login.dapLogin(username, password);

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

    public void login_Phone(String country, String phone, String password) {
        login.dapLogin_Phone(country, phone, password);

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

    public String checkExistsLoginAlertMsg() {
        return login.checkExistsLoginAlertMsg();
    }

}
