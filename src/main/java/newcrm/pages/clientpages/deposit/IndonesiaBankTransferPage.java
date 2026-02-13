package newcrm.pages.clientpages.deposit;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.DepositBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Random;

public class IndonesiaBankTransferPage extends LocalBankTransferDepositPage {
    public IndonesiaBankTransferPage(WebDriver driver){
        super(driver);
    }
    //old method use , not using now
    public void selectBankTrasfer(String channel){

        driver.findElement(By.xpath("//span[contains(text(),'"+channel+"')]")).click();
        waitLoading();
    }
    //old method use , not using now
    public void setBank()
    {
        driver.findElement(By.xpath("(//input[@placeholder='Select'])[2]")).click();

        List<WebElement> allbank = driver.findElements(By.xpath("(//ul[@class='el-scrollbar__view el-select-dropdown__list'])[2]/li"));

        Random rd = new Random();
        int i = rd.nextInt(4) + 1;
        allbank.get(i).click();
    }
    
    public boolean checkRandomBankName() 
    {
    	try 
    	{
        	driver.findElement(By.xpath("//div[@data-testid='bank_code']  | //input[@data-testid='bank_name' or @data-testid='bankcode']"));
    		return true;    	
    	}
    	catch (Exception e)
    	{
			GlobalMethods.printDebugInfo("No bank name field for this Indonesia Bank Transfer channel");
			return false;
		}
    }
    
    public void setRandomBankName() 
    {
    	if(checkRandomBankName()) 
    	{
    		setBankName();
    	}
    }
}
