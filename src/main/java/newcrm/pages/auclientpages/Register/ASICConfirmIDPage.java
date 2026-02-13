package newcrm.pages.auclientpages.Register;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.ConfirmIDPage;
import utils.LogUtils;

public class ASICConfirmIDPage extends ConfirmIDPage {

	public ASICConfirmIDPage(WebDriver driver) {
		super(driver);
	}
	
	public void goToUpload() {
		try {
		WebElement e = this.findClickableElementByXpath("//div[@class='verify_note']/p/a");
		this.moveElementToVisible(e);
		e.click();
		}catch(Exception e) {
			LogUtils.info("ConfirmIDPage: this environment does not configure green id！");
		}
		this.waitLoading();
	}
}
