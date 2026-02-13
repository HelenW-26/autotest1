package newcrm.business.newbrandbusiness;

import org.openqa.selenium.WebDriver;
import newcrm.business.businessbase.CPDepositFunds;
import newcrm.pages.newbrandclientpages.NewBrandDepositFundsPage;

public class NewBrandCPDepositFunds extends CPDepositFunds{
	
	public NewBrandCPDepositFunds(WebDriver driver) {
		super(new NewBrandDepositFundsPage(driver));

	}
	
}
