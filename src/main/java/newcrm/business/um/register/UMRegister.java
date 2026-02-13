package newcrm.business.um.register;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.umclient.register.*;
import newcrm.pages.umclientpages.UMMenuPage;
import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPRegister;
import newcrm.pages.auclientpages.Register.CIMAFinancialDetailsPage;
import vantagecrm.Utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class UMRegister extends CPRegister {

	public UMRegister(WebDriver driver,String url) {
		super(driver,url);
	}

	@Override
	protected void setUpHomepage() {
		homepage = new UMRegisterHomePage(driver,registerURL);
	}

	@Override
	protected void setUpPDpage() {
		pdpage = new UMPersonalDetailsPage(driver);
	}

	@Override
	protected void setUpFDpage() {
		this.fdpage = new CIMAFinancialDetailsPage(driver);
	}

	@Override
	protected void setUpACpage() {
		this.acpage = new UMAccountConfigurationPage(driver);
	}
	
	@Override
	public void fillFinacialPage() {
		((CIMAFinancialDetailsPage)this.fdpage).answerAllQuestionsWithoutReturn();
	}

	@Override
	protected void setUpFinishPage() {
		this.finishpage = new UMFinishPage(driver);
	}

	@Override
	protected void setUpIDpage() {
		idpage = new UMConfirmIDPage(driver);
	}

	@Override
	protected void setUpAddresspage() {
		addresspage = new UMResidentialAddressPage(driver);
	}

	@Override
	public void setUserInfo(String firstName, String lastName, String phonenum,String email,String pwd, String brand) {
		entrypage.setFirstName(firstName);
		entrypage.setEmail(email);
		entrypage.setPassword(pwd);
		entrypage.setBrand(brand);

		userdetails.put("First Name", firstName);
		userdetails.put("Email", email);
		userdetails.put("pwd", pwd);
		userdetails.put("Brand", brand);
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
		acpage.tickBox();
		return true;
	}

	@Override
	public boolean goToPersonalDetailPage() {
		//check
		UMMenuPage menu = new UMMenuPage(this.driver);
	//	menu.refresh();
		menu.closeCouponGuideDialog();
//		menu.changeLanguage("English");

		GlobalMethods.printDebugInfo("CPRegister: go to personal details page.");
		return true;
	}

	@Override
	public void fillPersonalDetails(String idNum,String firstName, String lastName,String phone ) {
//		pdpage.createButton();
		userdetails.put("firstName", pdpage.setfirstName(firstName));
		userdetails.put("lastName",pdpage.setLastName(lastName));
		userdetails.put("phone",pdpage.setPhone(phone));
		userdetails.put("gender", pdpage.setGender("Female"));
		userdetails.put("Date Of Birth", pdpage.setBirthDay());
	}

	@Override
	public boolean goToIDPage() {
		acpage.next();
		GlobalMethods.printDebugInfo("CPRegister: go to ACCOUNT OPENING page.");
		acpage.next();
		GlobalMethods.printDebugInfo("CPRegister: go to CONFIRM YOUR ID page.");
		return true;
	}

	@Override
	public void fillIDPage() {
		Path parent = Paths.get("src", "main", "resources", "vantagecrm", "doc");
		String fileFront = Paths.get(parent.toString(), "Update_ID_Card.jpg").toString();
		String fileBack = Paths.get(parent.toString(), "Update_ID_Card_Back.jpg").toString();

		idpage.setIdentificationType();
		String iDnumber = GlobalMethods.getRandomNumberString(5);
		idpage.setIDNumber(iDnumber);
		userdetails.put("id Number", iDnumber);

		idpage.uploadID(Paths.get(Utils.workingDir, fileFront).toString());
		idpage.uploadIDBack(Paths.get(Utils.workingDir, fileBack).toString());

		// Submit POI
		idpage.next();
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
		idpage.next();
	}

	@Override
	public void setUserInfo(String fistName,String country,String email,String pwd) {
		//entrypage.setFirstName(fistName);
		setCountry(country);
		setEmail(email);
		setPwd(pwd);
		checkAgreeCom();
	}

}