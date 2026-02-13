package newcrm.pages.ibpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;

import newcrm.global.GlobalMethods;

public class IBEmailWithdrawPage extends RebateWithdrawBasePage {

	public IBEmailWithdrawPage(WebDriver driver) {
		super(driver);
	}
	
	protected WebElement getAccountInput() {
		return this.findVisibleElemntByTestId("skrillEmail");
	}
	
	public void setWithdrawAccount(String account) {
		WebElement e = this.getAccountInput();
		setInputValue_withoutMoveElement(e, account);
		GlobalMethods.printDebugInfo("IBEmailWithdrawPage: Set withdraw account to " + account);
	}
	
	public void setAccountName(String accountName) {
		WebElement input = assertElementExists(By.xpath("//input[@data-testid='accountName']"), "Account Name");
		setInputValue(input, accountName);
		GlobalMethods.printDebugInfo("Set Account Name to " + accountName);
	}	
	
	public void setAccountNumber(String accountNumber) {
		WebElement input = assertElementExists(By.xpath("//input[@data-testid='accountNumber']"), "Account Number");
		setInputValue(input, accountNumber);
		GlobalMethods.printDebugInfo("Set Account Number to " + accountNumber);
	}
}
