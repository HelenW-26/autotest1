package newcrm.business.pugbusiness.register;

import org.openqa.selenium.WebDriver;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.USERTYPE;
import newcrm.pages.pugclientpages.register.ProdPUGRegisterEntryPage;
import newcrm.pages.pugclientpages.register.ProdPUGRegisterHomePage;
import newcrm.pages.vtclientpages.register.ProdVTRegisterEntryPage;
import newcrm.pages.vtclientpages.register.ProdVTRegisterHomePage;
import utils.LogUtils;

public class ProdFSACPRegister extends FSACPRegister{

	public ProdFSACPRegister(WebDriver driver, String registerURL) {
		super(driver,registerURL);
	}
	
	@Override
	public void setTradeUrl(String url) {
		LogUtils.info("PUG Prod enviroment do not need set trade url");
	}

	@Override
	public boolean setUserType(USERTYPE type) {
		LogUtils.info("PUG Prod enviroment do not need set user type");
		return true;
	}

	
	@Override
	protected void setUpHomepage() {
		homepage = new ProdPUGRegisterHomePage(driver,registerURL);
	}
	
	@Override
	protected void setUpEntrypage() {
		entrypage = new ProdPUGRegisterEntryPage(driver);
	
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
