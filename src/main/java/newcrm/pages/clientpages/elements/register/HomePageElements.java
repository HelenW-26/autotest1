package newcrm.pages.clientpages.elements.register;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

public class HomePageElements {

	@CacheLookup
	@FindAll({
			@FindBy(xpath = "(//a[contains(text(),'TRIAL DEMO')])[1]"),// kcm prod
			@FindBy(xpath = "(//a[contains(text(),'Demo Account') or contains(text(),'DEMO ACCOUNT')])[1]"),//alpha
			@FindBy(xpath = "(//a[contains(text(),'Request a demo')])[1]")//vt prod
	}
	)
	public WebElement demoButton;

	@CacheLookup
	@FindAll({
			@FindBy(xpath = "(//a[contains(text(),'OPEN ACCOUNT')])[1]"),//kcm prod
			@FindBy(xpath = "(//a[contains(text(),'Live Account') or contains(text(),'LIVE ACCOUNT')])[1]"),//alpha
			@FindBy(xpath = "(//a[@class='blue_button header_open_a_live_button'])[1]"),//vt prod
			@FindBy(xpath = "(//a[@class='blue_button join_now'])"),//PUG prod
			@FindBy(xpath = "(//a[contains(text(),'TRADE NOW') or contains(text(),'取引を始める')])[1]"),//vfx prod
			@FindBy(xpath = "(//span[contains(text(),'TRADE NOW')])[1]")//vfx prod
		}
	)
	public WebElement liveButton;
}
