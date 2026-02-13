package newcrm.business.aubusiness.register;

import newcrm.global.GlobalProperties;
import newcrm.pages.auclientpages.Register.*;
import newcrm.pages.auclientpages.AUMenuPage;
import org.openqa.selenium.WebDriver;

import newcrm.global.GlobalMethods;
import utils.LogUtils;

public class ProdVFSCCPRegister extends ProdVFSC2CPRegister {
	String firstName = "autotest" + GlobalMethods.getRandomString(10);
	String lastName = "TestCRM";
	String phone = GlobalMethods.getRandomNumberString(10);
	public ProdVFSCCPRegister(WebDriver driver, String registerURL) {
		super(driver,registerURL);
		this.homepage = new ProdVFSCRegisterHomePage(driver,registerURL);
	}

	@Override
	protected void setUpIDpage() {
		idpage = new PRODAUConfirmIDPage(driver);
	}

	@Override
	protected void setUpPDpage() {
		this.pdpage = new AUPersonalDetailsPage(driver);
	}

	@Override
	protected void setUpFinishPage() {
		finishpage = new AUFinishPage(driver);
	}

	@Override
	protected void setUpHomepage() {
		homepage = new AURegisterHomePage(driver,registerURL);
	}

	@Override
	protected void setUpAddresspage() {
		addresspage = new PRODVFSCResidentAdressPage(driver);
	}

	@Override
	protected void setUpACpage() {
		acpage = new AUAccountConfigurationPage(driver);
	}

	@Override
	protected void setUpEntrypage() {
		entrypage  = new PRODVFXRegisterEntryPage(driver);
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

	@Override
	public void setUserInfo(String fistName,String country,String email,String pwd) {
		setCountry(country);
		setEmail(email);
		setPwd(pwd);
		checkUNonUS();
	}

	@Override
	public void fillPersonalDetails(String idNum) {
		userdetails.put("gender", pdpage.setGender("Female"));
		userdetails.put("Date Of Birth", pdpage.setBirthDay());
		userdetails.put("firstName", pdpage.setfirstName(firstName));
		userdetails.put("lastName",pdpage.setLastName(lastName));
		userdetails.put("phone",pdpage.setPhone(phone));
	}

	@Override
	public void fillPersonalDetails(String idNum,String firstName, String lastName,String phone) {
		userdetails.put("gender", pdpage.setGender("Female"));
		userdetails.put("Date Of Birth", pdpage.setBirthDay());
		userdetails.put("firstName", pdpage.setfirstName(firstName));
		userdetails.put("lastName",pdpage.setLastName(lastName));
		userdetails.put("phone",pdpage.setPhone(phone));
	}

	@Override
	public boolean goToIDPage() {
		acpage.next();
		LogUtils.info("CPRegister: go to ACCOUNT OPENING page.");
		acpage.next();
		LogUtils.info("CPRegister: go to CONFIRM YOUR ID page.");
		return true;
	}

	@Override
	public boolean goToFinishPage() {
		return true;
	}

	@Override
	public void setEmail(String email) {
		entrypage.setEmail(email);
		userdetails.put("Email", email);
		//entrypage.next();
	}

	@Override
	public boolean goToPersonalDetailPage() {
		//check
		AUMenuPage menu = new AUMenuPage(this.driver);
		// menu.refresh();
		pdpage.closeImg();
//		menu.changeLanguage("English");

		LogUtils.info("CPRegister: go to personal details page.");
		return true;
	}

	@Override
	public boolean fillAccountPage(GlobalProperties.PLATFORM platform, GlobalProperties.ACCOUNTTYPE accountType, GlobalProperties.CURRENCY currency) {
		waitLoading();
		if(!acpage.setPlatForm(platform)) {
			return false;
		}
		userdetails.put("Platform", platform.toString());
		userdetails.put("Account Type", acpage.setAccountType(accountType));
		userdetails.put("Currency", acpage.setCurrency(currency));
		return true;
	}
}
