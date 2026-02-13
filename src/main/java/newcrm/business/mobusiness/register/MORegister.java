package newcrm.business.mobusiness.register;

import newcrm.pages.moclientpages.*;
import org.openqa.selenium.WebDriver;

import newcrm.global.GlobalMethods;

public class MORegister extends MOAlphaRegister {

	String firstName = "autotest" + GlobalMethods.getRandomString(10);
	String lastName = "TestCRM";
	String phone = GlobalMethods.getRandomNumberString(10);

	public MORegister(WebDriver driver, String url) {
		super(driver,url);
	}
	
	@Override
	public void openLiveAccount() {
		GlobalMethods.printDebugInfo("MO Register: Do not need click Live Button"); 
	}
	
	@Override
	public void setTradeUrl(String url) {
		GlobalMethods.printDebugInfo("MORegister PROD: Do not need set action url"); 
	}

	@Override
	protected void setUpEntrypage() {
		entrypage = new ProdMORegisterEntryPage(driver);
	}

	@Override
	protected void setUpPDpage() {
		this.pdpage = new MOPersonalDetailsPage(driver);
	}

	@Override
	protected void setUpAddresspage() {
		addresspage = new ProdMOResidentialAddressPage(driver);
	}

	@Override
	protected void setUpIDpage() {
		idpage = new ProdMOConfirmIDPage(driver);
	}

	@Override
	protected void setUpFinishPage() {
		finishpage = new MOFinishPage(driver);
	}

	@Override
	public boolean setRegulatorAndCountry(String country,String regulator) {
		if(!entrypage.setCountry(country)) {
			return false;
		}
		GlobalMethods.printDebugInfo("Prod enviroment do not need set regulator");
		userdetails.put("Country", country);
		userdetails.put("Regulator", regulator);
		return true;
	}

	@Override
	public void setUserInfo(String fistName,String country,String email,String pwd) {
		setCountry(country);
		entrypage.setFirstName(firstName);
		entrypage.setLastName(lastName);
		setEmail(email);
		setPwd(pwd);
		checkUNonUS();
	}

	@Override
	public void fillPersonalDetails(String idNum,String firstName, String lastName,String phone) {
		userdetails.put("gender", pdpage.setGender("Female"));
		userdetails.put("Date Of Birth", pdpage.setBirthDay());
//		userdetails.put("firstName", pdpage.setfirstName(firstName));
//		userdetails.put("lastName",pdpage.setLastName(lastName));
		userdetails.put("phone",pdpage.setPhone(phone));
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
	public boolean goToFinishPage() {
		return true;
	}

}
