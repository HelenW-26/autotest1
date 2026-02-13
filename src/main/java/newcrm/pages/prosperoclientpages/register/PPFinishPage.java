package newcrm.pages.prosperoclientpages.register;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.FinishPage;

public class PPFinishPage extends FinishPage {
	public PPFinishPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
	public String getResponse() {
		WebElement e = this.findVisibleElemntBy(By.cssSelector("div.content"));
		String result = e.getText();
		GlobalMethods.printDebugInfo("FinishPage: response message: " + result);
		return result;
	}

}
