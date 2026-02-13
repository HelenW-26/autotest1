package newcrm.business.aubusiness.register;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPRegister;
import newcrm.global.GlobalMethods;
import newcrm.pages.auclientpages.Register.ProdVFSCRegisterHomePage;
import utils.LogUtils;

public class ProdVFSC2CPRegister extends CPRegister {

	public ProdVFSC2CPRegister(WebDriver driver, String registerURL) {
		super(driver,registerURL);
		this.homepage = new ProdVFSCRegisterHomePage(driver,registerURL);
	}
	
	@Override
	public void setTradeUrl(String url) {
		LogUtils.info("Prod enviroment do not need set trade url");
	}
	
	@Override
	public boolean setRegulatorAndCountry(String country,String regulator) {
		/*if(country.trim().equalsIgnoreCase("Malaysia")||country.trim().equalsIgnoreCase("Thailand")||country.trim().equalsIgnoreCase("Australia"))
		{
			country = "France";
		}*/
		if(!entrypage.setCountry(country)) {
			return false;
		}
		LogUtils.info("Prod enviroment do not need set regulator");
		userdetails.put("Country", country);
		userdetails.put("Regulator", regulator);
		return true;
	}
}
