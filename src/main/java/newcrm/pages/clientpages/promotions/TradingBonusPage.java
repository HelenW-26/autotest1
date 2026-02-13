package newcrm.pages.clientpages.promotions;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;

public class TradingBonusPage extends Page {

	public TradingBonusPage(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	public void clickActive() {
		WebElement active = this.findClickableElementByXpath("//button[@data-testid='opt']");
		this.moveElementToVisible(active);
		active.click();
		this.waitLoading();
	}
	
	protected WebElement getAccountInput() {
		String xpath = "//div[@class='el-select']";
		
		return this.findClickableElementByXpath(xpath);
	}
	
	public String selectAccount() {
		WebElement input = this.getAccountInput();
		this.moveElementToVisible(input);
		input.click();
		
		List<WebElement> accounts = this.getAllOpendElements();
		if(accounts == null || accounts.size() == 0) {
			input.click();
			return null;
		}
		return this.selectRandomValueFromDropDownList(accounts);
	}
	
	protected String getMessage() {
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
	
	public boolean submit() {
		WebElement submit = this.findClickableElementByCss("button.el-button.btn_red.el-button--default");
		this.moveElementToVisible(submit);
		submit.click();
		this.waitLoading();
		String message = getMessage();
		GlobalMethods.printDebugInfo("Get message from trading bonus page: " + message);
		if(message==null || !message.contains("successfully activated")) {
			return false;
		}
		
		return true;
	}
}
