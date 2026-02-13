package newcrm.pages.pugclientpages.register;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import newcrm.pages.clientpages.register.RegisterHomePage;

public class ProdPUGRegisterHomePage extends RegisterHomePage {

	public ProdPUGRegisterHomePage(WebDriver driver,String url) {
		super(driver,url);
	}
	
//	@Override
//	protected WebElement getLiveReister() {
//		return this.findClickableElementByCss("a[class='blue_button join_now']");
//	}
//	@Override
//	public void registerLiveAccount() {
//		WebElement PUG_live = this.getLiveReister();
//		this.moveElementToVisible(PUG_live);
//		Actions act = new Actions(driver);
//		act.doubleClick(PUG_live).perform();
//		
//		if(driver.getWindowHandles().size()>1) {
//			driver.switchTo().window(driver.getWindowHandles().toArray()[1].toString());
//		}
//		this.checkUrlContains("/forex-trading-account/");
//	}




}
