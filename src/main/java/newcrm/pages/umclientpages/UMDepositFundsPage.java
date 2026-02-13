package newcrm.pages.umclientpages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalProperties.DEPOSITMETHOD;
import newcrm.pages.clientpages.DepositFundsPage;

public class UMDepositFundsPage extends DepositFundsPage {
	public UMDepositFundsPage(WebDriver driver) {
		super(driver);
	}
	
	/*@Override
	protected List<WebElement> getDepositMethodsElements(){
		List<WebElement> deposit_methods = driver.findElements(By.xpath("//ul[@class='link_box']/div/li"));
		
		return deposit_methods;
	}*/


}
