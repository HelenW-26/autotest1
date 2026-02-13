package newcrm.business.adminbusiness;

import newcrm.pages.adminpages.AdminLoginPage;
import newcrm.pages.clientpages.LoginPage;
import org.openqa.selenium.WebDriver;

public class AdminLogin {
    protected AdminLoginPage login;
    public AdminLogin(AdminLoginPage login) {
        this.login = login;
    }

    public AdminLogin(WebDriver driver, String url)
    {
        this.login = new AdminLoginPage(driver,url);
    }

    public boolean login(String username, String password) {
        login.setUserName(username);
        login.setPassWord(password);

        return login.submit();
    }
}
