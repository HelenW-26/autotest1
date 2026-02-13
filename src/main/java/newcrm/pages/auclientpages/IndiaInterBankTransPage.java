package newcrm.pages.auclientpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.deposit.InterBankTransPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class IndiaInterBankTransPage extends InterBankTransPage {
	public IndiaInterBankTransPage(WebDriver driver) {
		super(driver);
	}

	public void checkTnc() {
		try{
			WebElement checkbox = driver.findElement(By.xpath("//div[@class='form_deposit india_form_deposit']//img[@class='me-2']"));
			js.executeScript("arguments[0].click()", checkbox);
			GlobalMethods.printDebugInfo("Ticked the T&C checkbox");
		}
		catch (Exception e)
		{
			GlobalMethods.printDebugInfo("No tickbox require to tick");
		}
	}

	public String getConvertUSDamt() {
        String convtUsdAmt = null;
        try {
            WebElement usdAmt = driver.findElement(By.xpath("//ul[@class='clearfix']/li[@class='fr']//input"));
            convtUsdAmt = usdAmt.getAttribute("value");
            GlobalMethods.printDebugInfo("Converted USD Amount = " + convtUsdAmt);
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("Cannot find converted USD amount");
			convtUsdAmt = null;
        }
        return convtUsdAmt;
    }
}
