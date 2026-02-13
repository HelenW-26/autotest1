package newcrm.business.mobusiness.register;

import newcrm.global.GlobalMethods;
import newcrm.pages.moclientpages.*;
import newcrm.pages.moclientpages.register.*;
import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPRegister;
import utils.LogUtils;
import vantagecrm.Utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MOAlphaRegister extends CPRegister {

	public MOAlphaRegister(WebDriver driver, String url) {
		super(driver,url);
	}

	@Override
	protected void setUpHomepage() {
		homepage = new MORegisterHomePage(driver,registerURL);
	}

	@Override
	protected void setUpPDpage() {
		this.pdpage = new MOPersonalDetailsPage(driver);
	}

	@Override
	protected void setUpAddresspage() {
		addresspage = new MOResidentialAddressPage(driver);
	}

	@Override
	protected void setUpIDpage() {
		idpage = new MOConfirmIDPage(driver);
	}

	@Override
	protected void setUpFinishPage() {
		finishpage = new MOFinishPage(driver);
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
		
		if(!entrypage.setRegulator("VFSC")) {
			return false;
		}
		userdetails.put("Country", country);
		userdetails.put("Regulator", regulator);
		return true;
	}

	@Override
	public void setUserInfo(String firstName, String lastName, String phonenum,String email,String pwd, String brand) {
		entrypage.setEmail(email);
		entrypage.setPassword(pwd);
		entrypage.setBrand(brand);

		userdetails.put("Email", email);
		userdetails.put("pwd", pwd);
		userdetails.put("Brand", brand);
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
	public void setUserInfo(String firstName, String lastName, String phonenum,String email) {
		entrypage.setFirstName(firstName);
		entrypage.setLastName(lastName);
		entrypage.setPhone(phonenum);
		entrypage.setEmail(email);


		userdetails.put("First Name", firstName);
		userdetails.put("Last Name",lastName);
		userdetails.put("Phone Number", phonenum);
		userdetails.put("Email", email);
	}

	@Override
	protected void setUpACpage() {
		acpage = new MOAccountConfigurationPage(driver);
	}

	@Override
	public boolean goToPersonalDetailPage() {
		//check
		MOMenuPage menu = new MOMenuPage(this.driver);
//		if(!pdpage.getURL().contains("register")) {
//			menu.refresh();
//		}
		menu.closeCouponGuideDialog();
		menu.closeNotificationDialog();
//		menu.changeLanguage("English");

//		pdpage.proceedToIDVerfication();

		LogUtils.info("CPRegister: go to personal details page.");
		return true;
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
	public void fillAddressDetails() {
		Path parent = Paths.get("src", "main", "resources", "vantagecrm", "doc");
		String filePOA = Paths.get(parent.toString(), "Update_ID_Card.jpg").toString();

		String streetNum = GlobalMethods.getRandomNumberString(3);
		String streetName = GlobalMethods.getRandomString(15);
		addresspage.setAddress(streetNum, streetName);
		userdetails.put("Address", streetNum + " " + streetName);

		String suburb = GlobalMethods.getRandomString(6) + " test suburb";
		addresspage.setSuburb(suburb);
		userdetails.put("suburb", suburb);

		// Submit POA
		idpage.uploadID(Paths.get(Utils.workingDir, filePOA).toString());
		idpage.uploadBtn();
	}

	@Override
	public void setEmail(String email) {
		entrypage.setEmail(email);
		userdetails.put("Email", email);
		//entrypage.next();
	}

}
