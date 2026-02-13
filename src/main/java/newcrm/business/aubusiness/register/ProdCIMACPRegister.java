package newcrm.business.aubusiness.register;

import org.openqa.selenium.WebDriver;

import newcrm.global.GlobalMethods;
import newcrm.pages.auclientpages.Register.ProdVFSCRegisterHomePage;
import utils.LogUtils;

public class ProdCIMACPRegister extends CIMACPRegister {

	public ProdCIMACPRegister(WebDriver driver, String registerURL) {
		super(driver,registerURL);
		this.homepage = new ProdVFSCRegisterHomePage(driver,registerURL);
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
