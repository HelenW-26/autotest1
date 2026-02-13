package newcrm.pages.clientpages.elements;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

public class AdvertiseElements {
	@CacheLookup
	@FindAll({
		@FindBy(xpath="//div[@class='el-dialog__wrapper' and not(contains(@style,'display'))]//img[@data-testid='closeImg']"),//AU
		@FindBy(xpath="//div[@class='el-dialog__wrapper' and not(contains(@style,'display'))]//img[@class='closeButton']")//VT
		}
	)
	public WebElement adv;
}
