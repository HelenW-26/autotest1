package newcrm.pages.clientpages.deposit;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.By;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;


public class BridgePayDepositPage extends CreditCardDepositPage {
	public BridgePayDepositPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public boolean checkIframeExists() {
		this.waitLoading();
		try {
			waitvisible.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//iframe[@id='iFrameResizer0']")));
		}catch(Exception e) {
			System.out.println("!!! Coundn't find BridgePay iFrame!");
			return false;
		}
		GlobalMethods.printDebugInfo("BridgePay iFrame is visible.");
		return true;
	}
	
	/*//check bridgepay amount -- to do
	 * public boolean checkIframeAmount(String amount) { String iframeAmount=""; try
	 * { WebElement iframeMsg = driver.findElement(By.
	 * xpath("//*[contains(@class, 'bp-checkout-iframe bp-cashier-iframe')]"));
	 * driver.switchTo().frame(iframeMsg);
	 * 
	 * WebElement body = driver.findElement(By.cssSelector("body"));
	 * System.out.println(body.getText()); }catch(Exception e) {
	 * System.out.println("555555 error="+e); throw e; } return true; }
	 */
	
//	public double getAmountFromBridgePay() {
//		
//		
//		WebElement amount=null;
//		
//		try {
//			amount = driver.findElement(By.cssSelector("span.accented.ng-star-inserted"));
//		}catch(Exception e) {
//			System.out.println("555555 error="+e);
//			throw e;
//		}
//		
//		
//		if (amount == null) {
//
//			return -1.0;
//		}
//
//		String amountWithCurrency = amount.getText().trim();
//		Pattern compile = Pattern.compile("[1-9]\\d*.[0-9]\\d*.[0-9]\\d*");
//		Matcher matcher = compile.matcher(amountWithCurrency);
//		matcher.find();
//		double amount1 = Double.parseDouble(matcher.group());
//		System.out.println("amount in Australia Dollars in Third URL is" + " A$: " + amount1);
//		return amount1;
//	}

	/*public void moveNoteBox() {

		WebElement notes = this.findClickableElemntByTestId("applicationNotes");

		super.moveElementToVisible(notes);
	}

	public void setNotes(String notes) {
		WebElement note_element = this.findClickableElemntByTestId("applicationNotes");
		note_element.clear();
		note_element.sendKeys(notes);
		GlobalMethods.printDebugInfo("Set Important notes to: " + notes);
	}*/
}
