package newcrm.business.pugbusiness.register;

import newcrm.pages.pugclientpages.register.PRODPUGConfirmIDPage;
import org.openqa.selenium.WebDriver;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.USERTYPE;
import newcrm.pages.pugclientpages.register.ProdPUGRegisterEntryPage;
import newcrm.pages.pugclientpages.register.ProdPUGRegisterHomePage;
import utils.LogUtils;

public class ProdSVGCPRegister extends SVGCPRegister {

	public ProdSVGCPRegister(WebDriver driver, String registerURL) {
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
	protected void setUpIDpage() {
		idpage = new PRODPUGConfirmIDPage(driver);
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

	@Override
	public void setUserInfo(String firstName, String country, String email,String pwd) {
		setCountry(country);
		entrypage.setEmail(email);
		entrypage.setPassword(pwd);
		entrypage.setFirstName(firstName);

		userdetails.put("Country", country);
		userdetails.put("Email", email);
		userdetails.put("Password",pwd);
		userdetails.put("First Name", firstName);
	}

	@Override
	public boolean goToFinishPage() {
		return true;
	}

}