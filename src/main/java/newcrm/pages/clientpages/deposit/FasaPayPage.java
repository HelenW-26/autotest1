package newcrm.pages.clientpages.deposit;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.pages.clientpages.DepositBasePage;




public class FasaPayPage extends DepositBasePage {
	
	public FasaPayPage(WebDriver driver) {
		super(driver);
	}
	
	
	public String getDepoitAmountFromFasaPay() {
		//WebElement info = this.findVisibleElemntBy(By.cssSelector("//tbody/tr[1]/td[2]"));
		WebElement info = this.findVisibleElemntBy(By.cssSelector("tbody tr:nth-child(1) td:nth-child(2)"));
		if(info!=null) {
			return info.getText().trim();
		}
		return null;
	}

}
