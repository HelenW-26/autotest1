package newcrm.pages.pugclientpages.promotions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.pages.clientpages.promotions.DepositBonusPage;

public class PUGDepositBonusPage extends DepositBonusPage {

	public PUGDepositBonusPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
	public String getMessage() {
		WebElement e = this.findVisibleElemntByXpath("//div[@class='dialog_body']/p");
		if(e!=null) {
			String message = e.getText();
			WebElement close  = this.findClickableElementByXpath("//em[@class='el-icon-close close_image']");
			this.moveElementToVisible(close);
			close.click();
			return message;
		}
		return "";
	}
	
	public void clickAgreeAction() {
		WebElement button = this.findClickableElemntByTestId("agreeAction");
		this.moveElementToVisible(button);
		button.click();
	}
}
