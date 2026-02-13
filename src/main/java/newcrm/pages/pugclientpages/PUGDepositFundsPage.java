package newcrm.pages.pugclientpages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalProperties.DEPOSITMETHOD;
import newcrm.pages.clientpages.DepositFundsPage;

public class PUGDepositFundsPage extends DepositFundsPage {
	public PUGDepositFundsPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
	protected List<WebElement> getDepositMethodsElements(){
		List<WebElement> deposit_methods = driver.findElements(By.xpath("//ul[@class='link_box']/li"));
		
		return deposit_methods;
	}

	@Override
	public boolean navigateTo(DEPOSITMETHOD method) {
		List<WebElement> deposit_methods = getDepositMethodsElements();
		int size = deposit_methods.size();
		if(size>0) {
			for(int i = 0; i <size; i++) {
				WebElement element = deposit_methods.get(i);
				String deposit_name = element.getAttribute("data-testid");
				if(method.getCPTestId().toLowerCase().contains(deposit_name.trim().toLowerCase())) {
					WebElement div = driver.findElement(By.xpath("//*[@data-testid='"+deposit_name+"']/../.."));
					String id = div.getAttribute("id");
					id = id.replace("pane", "tab");
					this.findClickableElemntBy(By.id(id)).click();
					this.findClickableElemntByTestId(deposit_name).click();
					this.waitLoading();
					return true;
				}
			}
		}
		return false;
	}
}
