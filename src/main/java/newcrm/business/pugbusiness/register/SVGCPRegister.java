package newcrm.business.pugbusiness.register;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.MenuPage;
import newcrm.pages.pugclientpages.PUMenuPage;
import newcrm.pages.pugclientpages.register.*;
import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPRegister;
import utils.LogUtils;
import vantagecrm.Utils;

import java.nio.file.Path;
import java.nio.file.Paths;


public class SVGCPRegister extends CPRegister {
	String firstName = "autotest" + GlobalMethods.getRandomString(10);
	String lastName = "TestCRM";
	String phone = GlobalMethods.getRandomNumberString(10);

	public SVGCPRegister(WebDriver driver,String url) {
		super(driver,url);
	}

	@Override
	protected void setUpHomepage() {
		homepage = new PURegisterHomePage(driver,registerURL);
	}

	@Override
	protected void setUpPDpage() {
		this.pdpage = new PUGPersonalDetailsPage(driver);
	}

	@Override
	protected void setUpAddresspage() {
		addresspage = new PUGResidentialAddressPage(driver);
	}
	
	@Override
	protected void setUpFDpage() {
		fdpage = new PUGFinancialDetailsPage(driver);
	}

	@Override
	protected void setUpACpage() {
		acpage = new PUGAccountConfigurationPage(driver);
	}

	@Override
	protected void setUpIDpage() {
		idpage = new PUGConfirmIDPage(driver);
	}
	
	@Override
	protected void setUpFinishPage() {
		finishpage = new PUGFinishPage(driver);
	}

	@Override
	protected void setUpEntrypage() {
		entrypage = new PUGRegisterEntryPage(driver);
	}

	@Override
	public boolean goToPersonalDetailPage() {
		//entrySubmit();
		//check
		MenuPage menu = new PUMenuPage(this.driver);
		//menu.refresh();
//		menu.changeLanguage("English");

		/*String firstName = userdetails.get("First Name");
		if(!firstName.equalsIgnoreCase(pdpage.getFirstName().trim())) {
			LogUtils.info("CPRegister: First Name brought in from Home page is wrong. It is " + pdpage.getFirstName() + ". It should be " + firstName);
			return false;
		}*/
		/*String lastName = userdetails.get("Last Name");
		if(!lastName.equalsIgnoreCase(pdpage.getLastName().trim())) {
			LogUtils.info("CPRegister: Last Name brought in from Home page is wrong. It is " + pdpage.getLastName() + ". It should be " + lastName);
			return false;
		}
		String email = userdetails.get("Email");
		if(!email.equalsIgnoreCase(pdpage.getEmail().trim())) {
			LogUtils.info("CPRegister: Email brought in from Home page is wrong. It is " + pdpage.getEmail() + ". It should be " + email);
			return false;
		}
		String phonenum = userdetails.get("Phone Number");
		if(!phonenum.equalsIgnoreCase(pdpage.getPhone().trim())) {
			LogUtils.info("CPRegister: Phone Number brought in from Home page is wrong. It is " + pdpage.getPhone() + ". It should be " + phonenum);
			return false;
		}*/
		LogUtils.info("CPRegister: go to personal details page.");
		return true;
	}

	@Override
	public void setWid(String wid) {
		entrypage.setWid(wid);
	}

	/*@Override
	public void fillPersonalDetails(String idNum) {
		pdpage.createButton();
		userdetails.put("firstName", pdpage.setfirstName(firstName));
		userdetails.put("lastName",pdpage.setLastName(lastName));
		userdetails.put("phone",pdpage.setPhone(phone));
		pdpage.setMiddleName("Automation Test");
		userdetails.put("Middle Name", "Automation Test");
		userdetails.put("gender", pdpage.setGender("Female"));
		userdetails.put("Date Of Birth", pdpage.setBirthDay());
	}*/
	@Override
	public void fillPersonalDetails(String idNum,String firstName, String lastName,String phone) {
		pdpage.closeToolSkipButton();
		userdetails.put("gender", pdpage.setGender("Female"));
		userdetails.put("Date Of Birth", pdpage.setBirthDay());
		userdetails.put("firstName", pdpage.setfirstName(firstName));
		userdetails.put("lastName",pdpage.setLastName(lastName));
		userdetails.put("phone",pdpage.setPhone(phone));
	}

	/*@Override
	public void fillPersonalDetails(String idNum,String firstName, String lastName,String phone ) {
		pdpage.createButton();
		userdetails.put("firstName", pdpage.setfirstName(firstName));
		userdetails.put("lastName",pdpage.setLastName(lastName));
		userdetails.put("phone",pdpage.setPhone(phone));
		pdpage.setMiddleName("Automation Test");
		userdetails.put("Middle Name", "Automation Test");
		userdetails.put("gender", pdpage.setGender("Female"));
		userdetails.put("Date Of Birth", pdpage.setBirthDay());
	}*/

	public void fillPersonalDetailsForPT(String idNum,String firstName, String lastName,String phone ) {
		pdpage.createButton();
		driver.navigate().refresh();
		userdetails.put("firstName", pdpage.setfirstName(firstName));
		userdetails.put("lastName",pdpage.setLastName(lastName));
		userdetails.put("phone",pdpage.setPhone(phone));
		pdpage.setMiddleName("Automation Test");
		userdetails.put("Middle Name", "Automation Test");
		userdetails.put("gender", pdpage.setGender("Female"));
		userdetails.put("Date Of Birth", pdpage.setBirthDay());
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
	public void fillIDPage() {
		Path parent = Paths.get("src", "main", "resources", "vantagecrm", "doc");
		String fileFront = Paths.get(parent.toString(), "Update_ID_Card.jpg").toString();
		String fileBack = Paths.get(parent.toString(), "Update_ID_Card_Back.jpg").toString();

		String num = GlobalMethods.getRandomNumberString(10);
		String idnum = "TESTID"+GlobalMethods.getRandomNumberString(10);
		String city = "TESTID"+GlobalMethods.getRandomString(5);
		String postcode = GlobalMethods.getRandomNumberString(5);
		String address = GlobalMethods.getRandomString(5);

		idpage.waitLoadingIdentityVerificationContent();
		idpage.setIdentificationType();
		idpage.setIDNumber(idnum);
		userdetails.put("id Number", idnum);

		idpage.uploadID(Paths.get(Utils.workingDir, fileFront).toString());
		idpage.uploadIDBack(Paths.get(Utils.workingDir, fileBack).toString());

		// Submit POI
		idpage.uploadBtn();
		// Check for error message
		idpage.checkExistsAlertMsg("Submit POI");
	}

	@Override
	public void setEmail(String email) {
		entrypage.setEmail(email);
		userdetails.put("Email", email);
	}

	@Override
	public boolean setRegulatorAndCountry(String country,String regulator) {
		if(!entrypage.setCountry(country)) {
			return false;
		}
		userdetails.put("Country", country);
		return true;
	}


	@Override
	public void setUserInfo(String firstName, String lastName, String phonenum,String email,String pwd, String brand) {
		entrypage.setFirstName(firstName);
		//entrypage.setLastName(lastName);
		//entrypage.setPhone(phonenum);
		entrypage.setEmail(email);
		entrypage.setPassword(pwd);
		entrypage.setBrand(brand);

		userdetails.put("First Name", firstName);
		//userdetails.put("Last Name",lastName);
		//userdetails.put("Phone Number", phonenum);
		userdetails.put("Email", email);
		userdetails.put("pwd", pwd);
		userdetails.put("Brand", brand);
	}
	public boolean fillAccountPage(GlobalProperties.PLATFORM platform, GlobalProperties.ACCOUNTTYPE accountType, GlobalProperties.CURRENCY currency) {
		waitLoading();
		if(!acpage.setPlatForm(platform)) {
			return false;
		}
		userdetails.put("Platform", platform.toString());
		userdetails.put("Account Type", acpage.setAccountType(accountType));
		userdetails.put("Currency", acpage.setCurrency(currency));
		if(platform.toString().equalsIgnoreCase(GlobalProperties.PLATFORM.MTS.toString()))
		{
			acpage.tickTickBoxforCopyTradind();
		}
		else
		{
			acpage.tickBox();
		}

		return true;
	}
}
