package newcrm.pages.clientpages.deposit;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.DepositBasePage;

public class SkrillPayPage extends DepositBasePage {

	public SkrillPayPage(WebDriver driver) {
		super(driver);
	}	
	
	/**
	 * Set depoist email
	 * @param email
	 */
	public void setEmail(String email) {
		WebElement email_element = this.findClickableElemntByTestId("email");
		email_element.clear();
		email_element.sendKeys(email);
		GlobalMethods.printDebugInfo("Set Email to: " + email);
	}
		
	public String getDepoitAmountFromSkrill() {
		WebElement info = this.findVisibleElemntBy(By.cssSelector("div.amount.ng-binding.ng-isolate-scope"));
		if(info!=null) {
			return info.getText().trim();
		}
		return null;
	}
	
	public void acceptCookieOnThird() {
		try {
			driver.findElement(By.id("onetrust-accept-btn-handler")).click();
		}catch(Exception e) {
			
		}
	}
	
	@Override
	public void goBack() {
		//JavascriptExecutor js_executor = (JavascriptExecutor) driver; 
		acceptCookieOnThird();
		driver.navigate().back();
		driver.navigate().back();
		//js_executor.executeScript("window.history.go(-1)");
		
	}
}
