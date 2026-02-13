package newcrm.pages.clientpages;



import java.util.ArrayList;
import java.util.List;

import newcrm.global.GlobalProperties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


import newcrm.global.GlobalProperties.DEPOSITMETHOD;
import newcrm.pages.Page;

public class DepositFundsPage extends Page{
	public DepositFundsPage(WebDriver driver) {
		super(driver);
	}
	
	protected List<WebElement> getDepositMethodsElements(){
		List<WebElement> deposit_methods = driver.findElements(By.xpath("//div[@class='deposit_channel']/div"));
		
		return deposit_methods;
	}
	
	/***
	 * 
	 * @return get all methods tag <li>
	 */
	public List<String> getAvailableDepositMethodsName(){
		List<WebElement> deposit_methods = getDepositMethodsElements();
		List<String> result = new ArrayList<>();
		if(deposit_methods != null && deposit_methods.size()>0) {
			for(WebElement element:deposit_methods) {
				String deposit_name = element.getAttribute("data-testid");
				if(deposit_name!=null && deposit_name.trim() != "") {
					result.add(deposit_name.trim().toLowerCase());
				}
			}
		}
		return result;
	}
	
	/***
	 * 
	 * @param method enumtype, deposit method
	 * @return 
	 */
	public boolean navigateTo(DEPOSITMETHOD method) {
		if(!GlobalProperties.isWeb)
		{
			waitLoading();
		}
		List<WebElement> deposit_methods = getDepositMethodsElements();
		if(deposit_methods != null && deposit_methods.size()>0) {
			for(WebElement element:deposit_methods) {
				String deposit_name = element.getAttribute("data-testid");
				if(deposit_name==null) {
					continue;
				}
				
				if(method.getCPTestId().toLowerCase().equals(deposit_name.trim().toLowerCase())) {
					element.click();
					this.waitLoading();
					return true;
				}
			}
		}
		
		return false;
	}
}
