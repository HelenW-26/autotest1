package newcrm.business.pugbusiness.promotions;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.promotions.DepositBonus;
import newcrm.pages.pugclientpages.promotions.PUGDepositBonusPage;

public class PUGDepositBonus extends DepositBonus {

	protected PUGDepositBonusPage pugDpb;
	public PUGDepositBonus(WebDriver driver) {
		super(driver);
		pugDpb = new PUGDepositBonusPage(driver);
	}
	
	/**
	 * PUG has a different way to opt-in the promotion
	 */
	@Override
	public String optInDPB() {
		pugDpb.clickOpt();
		pugDpb.tickTandC(true);
		pugDpb.clickAgreeAction();
		return pugDpb.getMessage();
	}
}
