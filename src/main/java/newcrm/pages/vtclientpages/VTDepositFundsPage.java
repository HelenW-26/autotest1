package newcrm.pages.vtclientpages;

import java.util.List;

import newcrm.global.GlobalProperties;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.pages.clientpages.DepositFundsPage;

public class VTDepositFundsPage extends DepositFundsPage {

	public VTDepositFundsPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
	protected List<WebElement> getDepositMethodsElements(){
		//List<WebElement> deposit_methods = driver.findElements(By.xpath("//div[@class='row_card el-row']/div"));
		List<WebElement> deposit_methods = driver.findElements(By.xpath("//div[@id='depositFunds']//div"));
		return deposit_methods;
	}

	public boolean navigateTo(GlobalProperties.DEPOSITMETHOD method) {
		List<WebElement> deposit_methods = getDepositMethodsElements();
		if(deposit_methods != null && deposit_methods.size()>0) {
			for(WebElement element:deposit_methods) {
				String deposit_name = element.getAttribute("data-testid");
				if(deposit_name==null) {
					continue;
				}

				if(method.getCPTestId().toLowerCase().equals(deposit_name.trim().toLowerCase())) {
					//element.click();
					js.executeScript("arguments[0].click()",element);
					this.waitLoading();
					return true;
				}
			}
		}

		return false;
	}

}
