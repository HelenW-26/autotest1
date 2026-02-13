package newcrm.pages.clientpages.deposit;

import newcrm.global.GlobalMethods;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class JapanBankTransferPage extends LocalBankTransferDepositPage{
	
    public JapanBankTransferPage(WebDriver driver) {
        super(driver);
    }
    
    public void setBankTransferEmail(String email) 
    {
    	try 
    	{
        	WebElement emailfield = driver.findElement(By.xpath("//input[@data-testid='email']"));
        	this.setInputValue(emailfield, email);
    		GlobalMethods.printDebugInfo("Set Bank Transfer Email to: " + email);        	
    	}
    	catch (Exception e)
    	{
			GlobalMethods.printDebugInfo("No email field for this Japan Bank Transfer channel");
		}
    }

	@Override
	public void setJCBEmail (String email) {
		WebElement jcbemail = this.findVisibleElemntByXpath("//input[@id='form_item_email']");
		this.moveElementToVisible(jcbemail);
		jcbemail.sendKeys(email);
		GlobalMethods.printDebugInfo("Set JCB Email to: " + email);
	}

}
