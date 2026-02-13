package newcrm.business.businessbase.promotions;

import org.openqa.selenium.WebDriver;

import newcrm.pages.clientpages.promotions.TradingBonusPage;

public class TradingBonus {

	protected TradingBonusPage tbp;
	
	public TradingBonus(WebDriver driver) {
		tbp = new TradingBonusPage(driver);
	}
	
	public String activeTradingBonus() {
		tbp.clickActive();
		String account = tbp.selectAccount();
		if(account==null) {
			return null;
		}else {
			boolean result = tbp.submit();
			if(result) {
				return account;
			}else {
				return null;
			}
		}
	}
}
