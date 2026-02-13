package newcrm.pages.pugclientpages.register;

import newcrm.global.GlobalMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.pages.clientpages.register.FinishPage;

public class PUGFinishPage extends FinishPage {

	public PUGFinishPage(WebDriver driver) {
		super(driver);
	}

	public void backHome() {
		WebElement a = this.findClickableElementByXpath("//a[@class='el-button btn_red']");
		this.moveElementToVisible(a);
		a.click();
	}

}