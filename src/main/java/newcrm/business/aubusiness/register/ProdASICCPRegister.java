package newcrm.business.aubusiness.register;

import java.util.List;

import org.openqa.selenium.WebDriver;
import newcrm.global.GlobalMethods;
import newcrm.pages.auclientpages.Register.ProdASICRegisterEntryPage;
import newcrm.pages.auclientpages.Register.ProdVFSCRegisterHomePage;
import utils.LogUtils;

public class ProdASICCPRegister extends ASICCPRegister {

	public ProdASICCPRegister(WebDriver driver, String registerURL) {
		super(driver,registerURL);
		this.homepage = new ProdVFSCRegisterHomePage(driver,registerURL);
	}
	
	@Override
	protected void setUpEntrypage() {
		this.entrypage = new ProdASICRegisterEntryPage(driver);
	}
	
	@Override
	public void setTradeUrl(String url) {
		LogUtils.info("Prod enviroment do not need set trade url");
	}
	
	@Override
	public List<String> getAllCountriesOnEntryPage(){
		return this.entrypage.getCountries();
	}
	
	@Override
	public boolean setRegulatorAndCountry(String country,String regulator) {
		userdetails.put("Country", country);
		this.entrypage.setCountry(country);
		return true;
	}
	
}
