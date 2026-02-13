package newcrm.business.um.register;

import newcrm.global.GlobalProperties;
import newcrm.pages.umclient.ProdUMConfirmIDPage;
import newcrm.pages.umclient.register.ProdUMRegisterEntryPage;
import org.openqa.selenium.WebDriver;

import newcrm.global.GlobalMethods;
import newcrm.pages.umclient.register.UMProdRegisterHomePage;
import utils.LogUtils;

public class UMProdRegister extends UMRegister {

	public UMProdRegister(WebDriver driver,String url) {
		super(driver,url);
	}
	
	@Override
	protected void setUpHomepage() {
		this.homepage = new UMProdRegisterHomePage(driver,this.registerURL);
	}
	
	@Override
	protected void setUpEntrypage() {
		entrypage  = new ProdUMRegisterEntryPage(driver);
	}

	@Override
	protected void setUpIDpage() {	idpage = new ProdUMConfirmIDPage(driver);	}

	@Override
	public boolean setUserType(GlobalProperties.USERTYPE type) {
		if(!entrypage.setUserType(type)) {
			return false;
		}
		userdetails.put("User Type:", type.toString());

		return true;
	}

	@Override
	public void setTradeUrl(String url) {
		LogUtils.info("UMProdRegister: prod enviroment do not need action url");
	}

	@Override
	public boolean fillAccountPage(GlobalProperties.PLATFORM platform) {
		if (!acpage.setPlatForm(platform)) {
			return false;
		}
		userdetails.put("Platform", platform.toString());
		userdetails.put("Account Type", acpage.setAccountType());
		userdetails.put("Currency", acpage.setCurrency());
		//acpage.tickBox();
		return true;

	}

	@Override
	public boolean setRegulatorAndCountry(String country,String regulator) {
		
		if(!entrypage.setCountry(country)) {
			return false;
		}
		LogUtils.info("UMProdRegister: prod enviroment do not need set regulator");
		userdetails.put("Country", country);
		return true;
	}

	@Override
	public boolean goToFinishPage() {
		return true;
	}

}
