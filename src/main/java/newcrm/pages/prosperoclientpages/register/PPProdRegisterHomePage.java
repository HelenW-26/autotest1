package newcrm.pages.prosperoclientpages.register;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import newcrm.pages.clientpages.register.RegisterHomePage;

public class PPProdRegisterHomePage extends RegisterHomePage {

	public PPProdRegisterHomePage(WebDriver driver,String url) {
		super(driver,url);
	}
	
	@Override
	public void registerLiveAccount() {
		driver.findElement(By.linkText("Open Account")).click();
		this.waitLoading();
	}
}
