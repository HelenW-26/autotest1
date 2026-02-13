package newcrm.pages.newbrandclientpages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalProperties.DEPOSITMETHOD;
import newcrm.pages.clientpages.DepositFundsPage;

public class NewBrandDepositFundsPage extends DepositFundsPage{

	public NewBrandDepositFundsPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
	protected List<WebElement> getDepositMethodsElements(){
		List<WebElement> deposit_methods = driver.findElements(By.xpath("//div[@class='row_card el-row el-row--flex']/div"));
		
		return deposit_methods;
	}
	
}
