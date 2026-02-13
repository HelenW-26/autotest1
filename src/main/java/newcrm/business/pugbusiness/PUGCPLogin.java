package newcrm.business.pugbusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPLogin;
import newcrm.pages.pugclientpages.PUGLoginPage;

public class PUGCPLogin extends CPLogin {

	public PUGCPLogin(WebDriver driver,String url) {
		super(new PUGLoginPage(driver,url));
	}
}
