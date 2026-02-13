package newcrm.pages.vtclientpages.register;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import newcrm.pages.clientpages.elements.register.HomePageElements;
import newcrm.pages.clientpages.register.RegisterHomePage;

public class ProdVTRegisterHomePage extends RegisterHomePage {

	public ProdVTRegisterHomePage(WebDriver driver,String url) {
		super(driver,url);
	}
	
	@Override
	protected WebElement getLiveReister() {
		//return this.findClickableElementByXpath("(//a[@class='blue_button header_open_a_live_button'])[1]");
		HomePageElements els = PageFactory.initElements(driver, HomePageElements.class);
		return els.liveButton;
	}
	@Override
	public void registerLiveAccount() {
		//WebElement a_live = this.getLiveReister();
		WebElement a_live = driver.findElement(By.xpath("//a[@class='blue_button header_trade_button']"));
		this.moveElementToVisible(a_live);
		this.clickElement(a_live);
		if(driver.getWindowHandles().size()>1) {
			driver.switchTo().window(driver.getWindowHandles().toArray()[1].toString());
		}
		this.checkUrlContains("/trade-now/");
	}
}
