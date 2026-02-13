package newcrm.pages.moclientpages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.pages.clientpages.register.AccountConfigurationPage;

public class MOAccountConfigurationPage extends AccountConfigurationPage {
	
	public MOAccountConfigurationPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
	protected List<WebElement> getCurrencyEles(){
		return driver.findElements(By.xpath("//ul[@class='accountConfiguration currency_container']//li"));
	}

	@Override
	protected WebElement getAgreementEle() {
		return assertClickableElementExists(By.xpath("//*[@data-testid='checkbox']"), "Agreement tick box");
	}

	@Override
	public void next() {
		this.waitLoading();
		WebElement e = assertClickableElementExists(By.xpath("//button[@data-testid='next']"), "Submit button");
		triggerElementClickEvent_withoutMoveElement(e);
	}

}
