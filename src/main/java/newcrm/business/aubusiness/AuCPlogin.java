package newcrm.business.aubusiness;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPLogin;
import newcrm.pages.auclientpages.AULoginPage;

public class AuCPlogin extends CPLogin{

	public AuCPlogin(WebDriver driver, String url) {
		super(new AULoginPage(driver,url));	
	}
}
