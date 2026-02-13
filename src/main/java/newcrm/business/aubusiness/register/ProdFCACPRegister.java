package newcrm.business.aubusiness.register;

import org.openqa.selenium.WebDriver;

import newcrm.global.GlobalMethods;
import newcrm.pages.auclientpages.Register.ProdFCARegisterEntryPage;
import utils.LogUtils;

public class ProdFCACPRegister extends FCACPRegister {

	public ProdFCACPRegister(WebDriver driver, String registerURL) {
		super(driver,registerURL);
		this.entrypage = new ProdFCARegisterEntryPage(driver);
	}
	
	@Override
	public void setTradeUrl(String url) {
		LogUtils.info("Prod enviroment do not need set trade url");
	}
	
	@Override
	public boolean setRegulatorAndCountry(String country,String regulator) {
		if(!entrypage.setCountry(country)) {
			return false;
		}
		LogUtils.info("Prod enviroment do not need set regulator");
		userdetails.put("Country", country);
		userdetails.put("Regulator", regulator);
		return true;
	}
	
	
}
