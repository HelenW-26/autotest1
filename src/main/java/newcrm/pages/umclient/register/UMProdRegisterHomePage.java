package newcrm.pages.umclient.register;

import newcrm.global.GlobalMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.pages.clientpages.register.RegisterHomePage;

public class UMProdRegisterHomePage extends RegisterHomePage {

	public UMProdRegisterHomePage(WebDriver driver,String url) {
		super(driver,url);
	}
	
	@Override
	public void registerLiveAccount() {
		waitLoading();
		driver.findElement(By.xpath("//a[@class='act_button opacity_button']")).click();
		waitLoading();

	}

	
}
