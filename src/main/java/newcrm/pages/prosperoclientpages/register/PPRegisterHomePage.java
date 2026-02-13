package newcrm.pages.prosperoclientpages.register;

import org.openqa.selenium.WebDriver;

import newcrm.pages.clientpages.register.RegisterHomePage;

public class PPRegisterHomePage extends RegisterHomePage {
	public PPRegisterHomePage(WebDriver driver,String url) {
		super(driver,url);
	}
	
	@Override
	public void registerDemoAccount() {
		this.driver.get("https://new.vantagefx.com/forex-trading/open-demo-account-prospero/");
		this.waitLoading();
	}
	
	@Override
	public void registerLiveAccount() {
		this.driver.get("https://new.vantagefx.com/forex-trading/forex-trading-account-prospero/");
		this.waitLoading();
	}
}
