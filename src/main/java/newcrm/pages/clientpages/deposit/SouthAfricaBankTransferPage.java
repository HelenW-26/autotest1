package newcrm.pages.clientpages.deposit;

import newcrm.global.GlobalMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SouthAfricaBankTransferPage extends LocalBankTransferDepositPage{
    public SouthAfricaBankTransferPage(WebDriver driver)
    {
        super(driver);
    }
    //Personal ID provided by Eirc Chiu
    public void setPersonalID()
    {
        try
        {
            WebElement personalID = findVisibleElemntBy(By.xpath("//input[contains(@id,'personal_id')]"));
            this.setInputValue(personalID, "9911190233089");
            GlobalMethods.printDebugInfo("Set Personal ID to: " + personalID);
        }
        catch (Exception e)
        {
            GlobalMethods.printDebugInfo("No Personal ID for South Africa Bank Transfer channel");
        }
    }

}
