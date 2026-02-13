package newcrm.business.businessbase.promotions;

import org.openqa.selenium.WebDriver;

import newcrm.pages.clientpages.promotions.DepositBonusPage;

public class DepositBonus {

	protected DepositBonusPage dpb;
	
	public DepositBonus(WebDriver driver) {
		dpb = new DepositBonusPage(driver);
	}
	
	public String optInDPB() {
		dpb.tickTandC(true);
		dpb.clickOpt();
		return dpb.getMessage();
	}
}
