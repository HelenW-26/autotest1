package newcrm.pages.prosperoclientpages.register;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PPProdFinishPage extends PPFinishPage {

	public PPProdFinishPage(WebDriver driver) {
		super(driver);
	}
	
	public void backHome() {
		WebElement a = this.findClickableElementByXpath("//*[@data-testid='confirm']");
		this.moveElementToVisible(a);
		a.click();
	}
}
