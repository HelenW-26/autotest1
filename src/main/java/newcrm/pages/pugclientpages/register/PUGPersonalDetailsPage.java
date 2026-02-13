package newcrm.pages.pugclientpages.register;

import newcrm.global.GlobalMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.pages.clientpages.register.PersonalDetailsPage;
import utils.LogUtils;

public class PUGPersonalDetailsPage extends PersonalDetailsPage {

	public PUGPersonalDetailsPage(WebDriver driver) {
		super(driver);
	}

	@Override
	protected WebElement getMobileInput() {
		return this.findClickableElementByXpath("//div[@data-testid='phoneCode']/input");
	}

	@Override
	protected WebElement getNationalityInput(){
		return this.findClickableElemntBy(By.xpath("//div[@data-testid='nationalityId']"));
	}

	@Override
	public String setPhone(String phone) {
		WebElement e = this.getMobileInput();
		this.moveElementToVisible(e);
		e.click();
		e.sendKeys(phone);
		LogUtils.info("PersonalDetailsPage: set mobile to: " + phone);
		return phone;
	}

}
