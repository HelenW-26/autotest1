package newcrm.business.aubusiness.promotions;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.promotions.DepositBonus;
import newcrm.pages.auclientpages.promotions.AuDepositBonusPage;

public class AUDepositBonus extends DepositBonus {

	public AUDepositBonus(WebDriver driver) {
		super(driver);
		this.dpb = new AuDepositBonusPage(driver);
	}
}
