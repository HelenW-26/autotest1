package newcrm.pages.starclientpages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.DEPOSITMETHOD;
import newcrm.pages.ibpages.IBEmailWithdrawPage;

public class STARIBWithdrawPage extends IBEmailWithdrawPage {

	public STARIBWithdrawPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
	public void setMethod(DEPOSITMETHOD method) {
		WebElement dropdown = this.findClickableElemntByTestId("withdrawalType");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click()",dropdown);
		
		WebElement e_method = this.findClickableElemntByTestId(method.getIBWithdrawTypeDataTestId());
		this.moveElementToVisible(e_method);
		
		GlobalMethods.printDebugInfo("RebateWithdrawBasePage: Select IB withdraw method : " + method.getWithdrawName());
		e_method.click();
		this.waitLoading();
	}
}
