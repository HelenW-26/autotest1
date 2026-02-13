package newcrm.pages.pugclientpages.ib;

import newcrm.global.GlobalMethods;
import newcrm.pages.ibpages.IBInternationalBankTransferWithdrawPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PUGIBInternationalBankTransferWithdrawPage extends IBInternationalBankTransferWithdrawPage {

	public PUGIBInternationalBankTransferWithdrawPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
	protected WebElement getRegionInput() {
		WebElement div_region;
		try {
			div_region= driver.findElement(By.xpath("//div[@id='transferCountry']/form/div//li[2]//input"));
			div_region.isDisplayed();
			GlobalMethods.printDebugInfo("Find country region field");
			this.moveElementToVisible(div_region);
			return div_region;
		}
		catch (Exception e)
		{
			GlobalMethods.printDebugInfo("Country region field not found");
			return null;
		}
	}
}
