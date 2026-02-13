package newcrm.pages.auclientpages.promotions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.pages.clientpages.promotions.DepositBonusPage;

public class AuDepositBonusPage extends DepositBonusPage {

	public AuDepositBonusPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
	public String getMessage() {
		this.waitLoading();
		WebElement p = this.findVisibleElemntByXpath("//p[@class='mb-3']");
		String message = p.getText();
		if(message !=null && !message.equals("")) {
			return message;
		}
		return "";
	}
}
