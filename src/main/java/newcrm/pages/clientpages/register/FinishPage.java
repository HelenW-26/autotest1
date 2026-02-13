package newcrm.pages.clientpages.register;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import utils.LogUtils;

public class FinishPage extends Page {

	public FinishPage(WebDriver driver) {
		super(driver);
	}
	
	public void backHome() {
		WebElement a = this.findClickableElementByXpath("//a[@data-testid='bkToHm']");
		this.moveElementToVisible(a);
		a.click();
	}

	public String getResponse() {
		WebElement e = assertVisibleElementExists(By.xpath("//div/*[@class='account_opening_complete_title']"), "Identity verification Review");
		String result = e.getText();
		LogUtils.info("FinishPage: response message:\n" + result);
		return result;
	}

}
