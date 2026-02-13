package newcrm.pages.auclientpages.Register;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import newcrm.pages.clientpages.elements.register.HomePageElements;
import newcrm.pages.clientpages.register.RegisterHomePage;

public class ProdVFSCRegisterHomePage extends RegisterHomePage {

	public ProdVFSCRegisterHomePage(WebDriver driver,String url) {
		super(driver,url);
	}
	
	protected WebElement getDemoRegister() {
		//return this.findClickableElementByXpath("(//a[contains(text(),'Demo Account')])[1]");
		HomePageElements els = PageFactory.initElements(driver, HomePageElements.class);
		return els.demoButton;
	}
	protected WebElement getLiveReister() {
		//return this.findClickableElementByXpath("(//a[contains(text(),'Live Account')])[1]");
		HomePageElements els = PageFactory.initElements(driver, HomePageElements.class);
		return els.liveButton;
	}
	
	@Override
	public void registerDemoAccount() {
		WebElement a_demo = getDemoRegister();
		this.moveElementToVisible(a_demo);
		a_demo.click();
		this.checkUrlContains("/forex-trading-account/");
	}
	
	@Override
	public void registerLiveAccount() {
		WebElement a_live = this.getLiveReister();
		this.moveElementToVisible(a_live);
		//a_live.click();
		js.executeScript("arguments[0].click()",a_live);
		this.checkUrlContains("/open-live-account/");
	}
}
