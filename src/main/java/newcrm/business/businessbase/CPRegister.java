package newcrm.business.businessbase;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import newcrm.global.GlobalProperties;
import newcrm.pages.ptclientpages.payment.PTPaymentPage;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import newcrm.business.dbbusiness.UsersDB;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.ACCOUNTTYPE;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.REGULATOR;
import newcrm.global.GlobalProperties.USERTYPE;
import newcrm.pages.clientpages.MenuPage;
import newcrm.pages.clientpages.register.AccountConfigurationPage;
import newcrm.pages.clientpages.register.ConfirmIDPage;
import newcrm.pages.clientpages.register.FinancialDetailsPage;
import newcrm.pages.clientpages.register.FinishPage;
import newcrm.pages.clientpages.register.PersonalDetailsPage;
import newcrm.pages.clientpages.register.RegisterEntryPage;
import newcrm.pages.clientpages.register.RegisterHomePage;
import newcrm.pages.clientpages.register.ResidentialAddressPage;
import newcrm.pages.clientpages.register.QuizPage;
import org.testng.Assert;
import utils.LogUtils;
import vantagecrm.DBUtils;
import vantagecrm.Utils;


public class CPRegister {
	protected WebDriver driver;
	protected String registerURL;
	public HashMap<String,String> userdetails;
	public boolean bIDSumsub = false;

	protected RegisterHomePage homepage;
	protected RegisterEntryPage entrypage;
	protected PersonalDetailsPage pdpage;//step one
	protected ResidentialAddressPage addresspage; //step two
	protected FinancialDetailsPage fdpage;//step three
	protected AccountConfigurationPage acpage;//step four
	protected ConfirmIDPage idpage;//step five,ASIC need greed id;
	protected QuizPage quizpage;
	protected FinishPage finishpage;
	protected PTPaymentPage paymentPage;

	public CPRegister(WebDriver driver, String registerURL) {
		this.driver = driver;
		this.registerURL = registerURL;
		userdetails = new HashMap<>();
		//override these methods if you need
		this.setUpHomepage();
		this.setUpEntrypage();
		this.setUpPDpage();
		this.setUpAddresspage();
		this.setUpFDpage();
		this.setUpACpage();
		this.setUpIDpage();
		this.setUpQuizPage();
		this.setUpFinishPage();
		this.setUpPTPaymentPage();
	}

	protected void setUpHomepage() {
		homepage = new RegisterHomePage(driver,registerURL);
	}

	protected void setUpEntrypage() {
		entrypage  = new RegisterEntryPage(driver);
	}

	protected void setUpPDpage() {
		pdpage = new PersonalDetailsPage(driver);
	}

	protected void setUpAddresspage() {
		addresspage = new ResidentialAddressPage(driver);
	}

	protected void setUpFDpage() {
		fdpage = new FinancialDetailsPage(driver);
	}

	protected void setUpACpage() {
		acpage = new AccountConfigurationPage(driver);
	}

	protected void setUpIDpage() {
		idpage = new ConfirmIDPage(driver);
	}

	protected void setUpQuizPage() {
		quizpage = new QuizPage(driver);
	}

	protected void setUpFinishPage() {
		finishpage = new FinishPage(driver);
	}

	protected void setUpPTPaymentPage() {
		paymentPage = new PTPaymentPage(driver);
	}

	//public methods for testcase

	public HashMap<String,String> getUserDetails(){
		return userdetails;
	}
	public void openLiveAccount() {
		homepage.registerLiveAccount();
	}
	public void openDemoAccount() {
		homepage.registerDemoAccount();
	}

	public void setIBcode(String code, boolean bIsEnvProd) {
		entrypage.setIBcode(code, bIsEnvProd);
		userdetails.put("IBCode",code);
	}
	public void setTradeUrl(String url) {
		entrypage.setActionUrl(url);
		userdetails.put("Action Url", url);
	}
	public void setRAFCode(String rafCode) {
		entrypage.setRAFCode(rafCode);
		userdetails.put("RAF Code",rafCode);
	}

    public void setCampaignCode(String campaignCode) {
        entrypage.setCampaignCode(campaignCode);
        userdetails.put("Campaign Code",campaignCode);
    }

	public void setPartnerNum(String partnerNum) {
		entrypage.setPartnerNum(partnerNum);
		userdetails.put("Partner Number",partnerNum);
	}

	public void setPwd(String pwd) {
		entrypage.setPassword(pwd);
		userdetails.put("pwd", pwd);

	}

	//some countries must be belongs to the specific regulator. override method if rules have been changed
	public boolean setRegulatorAndCountry(String country,String regulator) {
		/*if(country.trim().equalsIgnoreCase("Malaysia")||country.trim().equalsIgnoreCase("Thailand")||country.trim().equalsIgnoreCase("Australia"))
		{
			country = "France";
		}*/
		if(!entrypage.setCountry(country)) {
			return false;
		}

		if(!entrypage.setRegulator(regulator)) {
			return false;
		}
		userdetails.put("Country", country);
		userdetails.put("Regulator", regulator);
		return true;
	}

	public boolean setRegulatorAndCountry(String country,String regulator,String registerInterface) {
		/*if(country.trim().equalsIgnoreCase("Malaysia")||country.trim().equalsIgnoreCase("Thailand")||country.trim().equalsIgnoreCase("Australia"))
		{
			country = "France";
		}*/
		if(!entrypage.setCountry(country)) {
			return false;
		}

		if(!entrypage.setRegulator(regulator)) {
			return false;
		}

		if(!entrypage.setRegisterInterface(registerInterface)) {
			return false;
		}
		userdetails.put("Country", country);
		userdetails.put("Regulator", regulator);
		userdetails.put("registerInterface", registerInterface);
		return true;
	}

	public boolean setCountry(String country) {

		if(!entrypage.setCountry(country)) {
			return false;
		}
		userdetails.put("Country", country);

		return true;
	}
	public boolean setUserType(USERTYPE type) {
		if(!entrypage.setUserType(type)) {
			return false;
		}
		userdetails.put("User Type:", type.toString());
		return true;
	}

	public void setUserInfo(String firstName, String country, String email,String pwd) {
		entrypage.setFirstName(firstName);
		entrypage.setPassword(pwd);
		entrypage.setEmail(email);
		setCountry(country);

		userdetails.put("First Name", firstName);
		userdetails.put("Password",pwd);
		userdetails.put("Country", country);
		userdetails.put("Email", email);
	}

	public void setPTUserInfo(String firstName, String lastName, String phone,String email) {
		entrypage.setFirstName(firstName);
		entrypage.setLastName(lastName);
		entrypage.setPhone(phone);
		entrypage.setEmail(email);

		userdetails.put("First Name", firstName);
		userdetails.put("lastName",lastName);
		userdetails.put("phone", phone);
		userdetails.put("Email", email);
	}

	public void setUserInfo(String firstName, String lastName, String phonenum,String email,String pwd, String brand) {
		entrypage.setFirstName(firstName);
		//entrypage.setLastName(lastName);
		entrypage.setPhone(phonenum);
		entrypage.setEmail(email);
		entrypage.setPassword(pwd);
		entrypage.setBrand(brand);

		userdetails.put("First Name", firstName);
		//userdetails.put("Last Name",lastName);
		userdetails.put("Phone Number", phonenum);
		userdetails.put("Email", email);
		userdetails.put("pwd", pwd);
		userdetails.put("Brand", brand);
	}

	public void setWid(String wid) {
		entrypage.setWid(wid);
	}

	public void setBranchVersion(String branchVersion) {
		entrypage.setBranchVersion(branchVersion);
	}

	public void setEmail(String email) {
		entrypage.setEmail(email);
		userdetails.put("Email", email);
		//entrypage.next();
	}

	public void clickCodeBtn(){
		entrypage.clickCodeBtn();
	}

	public void sendCode(String code){
		entrypage.sendCode(code);
	}

	public void checkUNonUS(){
		entrypage.checkNonUSResident();
	}

	public void checkAgreeCom(){
		entrypage.checkAgreeCommunication();
	}

	public void entrySubmit(String traderURL) {
		String oldUrl = entrypage.getCurrentURL();
		entrypage.submit();
		entrypage.checkNavigateSuccess(traderURL, oldUrl, RegisterEntryPage.REG_SRC.LIVE_REG);
	}

    public void entrySubmit_WithoutCheckURL() {
        entrypage.submit_WithoutCheckURL();
    }

	public boolean goToPersonalDetailPage() {
		//check
		MenuPage menu = new MenuPage(this.driver);
		menu.refresh();
		menu.changeLanguage("English");
		String pagetitle = pdpage.getPageTitle();
		if(!pagetitle.trim().equalsIgnoreCase("PERSONAL DETAILS")) {
			LogUtils.info("CPRegister: Page title is wrong. It is " + pdpage.getPageTitle() + ". It should be " + "PERSONAL DETAILS" );
			return false;
		}
		String firstName = userdetails.get("First Name");
		if(!firstName.equalsIgnoreCase(pdpage.getFirstName().trim())) {
			LogUtils.info("CPRegister: First Name brought in from Home page is wrong. It is " + pdpage.getFirstName() + ". It should be " + firstName);
			return false;
		}
		String lastName = userdetails.get("Last Name");
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
		}
		LogUtils.info("CPRegister: go to personal details page.");
		return true;
	}

	public void fillPersonalDetails(String idNum) {
		userdetails.put("Person Title", pdpage.setPersonTitle());
		pdpage.setMiddleName("Automation Test");
		userdetails.put("Middle Name", "Automation Test");
		userdetails.put("Nationality", pdpage.setNationality());
		userdetails.put("Date Of Birth", pdpage.setBirthDay());
		userdetails.put("Place Of Birth", pdpage.setBirthPlace());
		userdetails.put("Identification Type", pdpage.setIdentificationType());
		pdpage.setIDNumber(idNum);
		userdetails.put("ID Number", idNum);
		//pdpage.setRefer("Automation Test");
		//userdetails.put("referred by", "Automation Test");
	}

	public void fillPersonalDetails(String idNum,String firstName, String lastName,String phone) {
		userdetails.put("First Name", pdpage.setfirstName(firstName));
		userdetails.put("Last Name", pdpage.setLastName(lastName));
		userdetails.put("Phone Number", pdpage.setPhone(phone));
		userdetails.put("Person Title", pdpage.setPersonTitle());
		pdpage.setMiddleName("Automation Test");
		userdetails.put("Middle Name", "Automation Test");
		userdetails.put("Nationality", pdpage.setNationality());
		userdetails.put("Date Of Birth", pdpage.setBirthDay());
		userdetails.put("Place Of Birth", pdpage.setBirthPlace());
		userdetails.put("Identification Type", pdpage.setIdentificationType());
		pdpage.setIDNumber(idNum);
		userdetails.put("ID Number", idNum);
		//pdpage.setRefer("Automation Test");
		//userdetails.put("referred by", "Automation Test");
	}


	public boolean goToAddressPage() {
		pdpage.submit();
		String pageTitle = addresspage.getPageTitle();
		if(!pageTitle.trim().equalsIgnoreCase("MAIN RESIDENTIAL ADDRESS")) {
			LogUtils.info("CPRegister: Page title is wrong. It is " + addresspage.getPageTitle() + ". It should be " + "MAIN RESIDENTIAL ADDRESS" );
			return false;
		}
		String country = userdetails.get("Country");
		String t_country = addresspage.getCountry().trim();
		//special for Hong Kong
		if(t_country.equalsIgnoreCase("Hong Kong")) {
			t_country = "HongKong";
		}
		if(!country.trim().equalsIgnoreCase(t_country)) {
			LogUtils.info("CPRegister: Country brought in from Home page is wrong. It is " + t_country + ". It should be " + country);
			return false;
		}
		LogUtils.info("CPRegister: go to MAIN RESIDENTIAL ADDRESS page.");
		return true;
	}

    public boolean goToAdditionalDetailsPage(){
        return true;
    }

	public void fillAddressDetails() {
		String streetNum = GlobalMethods.getRandomNumberString(3);
		String streetName = GlobalMethods.getRandomString(15);
		addresspage.setAddress(streetNum, streetName);
		userdetails.put("Street Number", streetNum);
		userdetails.put("Street Name", streetName);

		String state = GlobalMethods.getRandomString(6) + " test state";
		String suburb = GlobalMethods.getRandomString(6) + " test suburb";
		String postcode = GlobalMethods.getRandomNumberString(4);
		addresspage.setState(state);
		addresspage.setSuburb(suburb);
		addresspage.setPostcode(postcode);
		userdetails.put("state", state);
		userdetails.put("suburb", suburb);
		userdetails.put("postcode", postcode);
	}

	public void fillAddressDetails_withSumsub(String country) {
		Path parent = Paths.get("src", "main", "resources", "vantagecrm", "doc");
		String fileFront = Paths.get(parent.toString(), "ID_Card_Gen.jpg").toString();

//		idpage.waitLoadingPOAVerificationContent();
		// Wait for Sumsub content to load
		idpage.waitLoadingSumSubContent();
		// Check whether page has access to Sumsub
		if (!idpage.checkHasSumSubAccess()) {
			return;
		}
		// Subsum - Change Language
		idpage.sumSubChangeLanguage();
		// Sumsub - Start Verification Step
		idpage.startSumSubVerification();
		// Sumsub - Personal Information Step
		idpage.fillSumSubPersonalDetails(country);
		// Sumsub - Upload Document Step
		idpage.uploadSumSubPOA(Paths.get(Utils.workingDir, fileFront).toString());
		// Switch back to default content
		driver.switchTo().defaultContent();
	}

	public boolean goToFinancialPage() {
		addresspage.next();
		addresspage.waitLoading();
		String pagetitle = fdpage.getPageTitle();
		if(!pagetitle.trim().equalsIgnoreCase("EMPLOYMENT AND FINANCIAL DETAILS")) {
			LogUtils.info("CPRegister: Page title is wrong. It is " + pagetitle + ". It should be " + "EMPLOYMENT AND FINANCIAL DETAILS" );
			return false;
		}

		LogUtils.info("CPRegister: go to EMPLOYMENT AND FINANCIAL DETAILS page.");
		return true;
	}

	public void fillFinacialPage() {
		userdetails.put("Employment Status", fdpage.setEmploymentStatus());
		userdetails.put("Estimated Annual Income", fdpage.setEstimatedAnnualIncome());
		userdetails.put("Estimated Savings and Investments", fdpage.setEstimatedSavingsAndInvestments());
		userdetails.put("Estimated Intended Deposit", fdpage.setEstimatedIntendedDeposit());
		userdetails.put("Source of Funds", fdpage.setSourceOfFunds());
		userdetails.put("Number Of Trades Per Week", fdpage.setNumberOfTradesPerWeek());
		userdetails.put("Trading Amount Per Week", fdpage.setTradingAmountPerWeek());
	}

	public boolean goToAccountPage() {
		addresspage.next();
		String pagetitle = acpage.getPageTitle();
		if(!pagetitle.trim().equalsIgnoreCase("ACCOUNT CONFIGURATION")) {
			LogUtils.info("CPRegister: Page title is wrong. It is " + pagetitle + ". It should be " + "ACCOUNT CONFIGURATION" );
			return false;
		}

		LogUtils.info("CPRegister: go to ACCOUNT CONFIGURATION page.");
		return true;
	}


	public boolean goToAccountPageNew() {
		pdpage.submit();
		LogUtils.info("CPRegister: go to ACCOUNT CONFIGURATION page.");
		return true;
	}

	public boolean goToAccountPageForPT() {
		entrypage.submit();
		LogUtils.info("CPRegister: go to ACCOUNT CONFIGURATION page.");
		return true;
	}

	public boolean goToPaymentPageForPT() {
		acpage.next();
		LogUtils.info("CPRegister: go to ACCOUNT CONFIGURATION page.");
		return true;
	}

	public void choosePaymentMethod(String pMethod)
	{
		paymentPage.selectPaymentMethod(pMethod);
	}

	public boolean fillAccountPage(PLATFORM platform) {
			if (!acpage.setPlatForm(platform)) {
				return false;
			}
			userdetails.put("Platform", platform.toString());
			userdetails.put("Account Type", acpage.setAccountType());
			userdetails.put("Currency", acpage.setCurrency());
			acpage.tickBox();
			return true;

	}

	public boolean fillAccountPage(PLATFORM platform,ACCOUNTTYPE accountType,CURRENCY currency) {
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

	public boolean fillAccountPage(PLATFORM platform,ACCOUNTTYPE accountType,CURRENCY currency,String balance) {
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

	public boolean goToIDPage() {
		acpage.next();
		String pagetitle = idpage.getPageTitle();
		if(!pagetitle.trim().equalsIgnoreCase("CONFIRM YOUR ID")) {
			LogUtils.info("CPRegister: Page title is wrong. It is " + pagetitle + ". It should be " + "CONFIRM YOUR ID" );
			return false;
		}

		LogUtils.info("CPRegister: go to CONFIRM YOUR ID page.");
		return true;
	}

	public boolean goToQuizPage() {
		return true;
	}

	public void fillIDPage() {
		//String fileFront = "\\src\\main\\resources\\vantagecrm\\doc\\Update_ID_Card.jpg"; //Driver_License2.jpg//Passport.png;Prod_ID.jpeg
		//String fileBack = "\\src\\main\\resources\\vantagecrm\\doc\\Update_ID_Card_Back.jpg";
		//String filePOA = "\\src\\main\\resources\\vantagecrm\\doc\\Update_ID_Card.jpg";
		Path parent = Paths.get("src", "main", "resources", "vantagecrm", "doc");
		String fileFront = Paths.get(parent.toString(), "Update_ID_Card.jpg").toString();
		String fileBack = Paths.get(parent.toString(), "Update_ID_Card_Back.jpg").toString();
		String filePOA = Paths.get(parent.toString(), "Update_ID_Card.jpg").toString();
		//idpage.uploadID(Utils.workingDir + fileFront);
		//idpage.uploadID(Utils.workingDir + fileBack);
		idpage.uploadID(Paths.get(Utils.workingDir, fileFront).toString());
		idpage.uploadID(Paths.get(Utils.workingDir, fileBack).toString());
		idpage.filleName(userdetails.get("First Name"), "Automation Test", userdetails.get("Last Name"));
		idpage.uploadPOA(Paths.get(Utils.workingDir, filePOA).toString());
		idpage.uploadPOA(Paths.get(Utils.workingDir, filePOA).toString());
	}

	public void fillIDPage_withSumsub(String country) {
		Path parent = Paths.get("src", "main", "resources", "vantagecrm", "doc");
		String fileFront = Paths.get(parent.toString(), "ID_Card_Gen.jpg").toString();
		String fileBack = Paths.get(parent.toString(), "ID_Card_Back_Gen.jpg").toString();

		idpage.waitLoadingIdentityVerificationContent();
		// Wait for Sumsub content to load
		idpage.waitLoadingSumSubContent();
		// Check whether page has access to Sumsub
		if (!idpage.checkHasSumSubAccess()) {
			return;
		}
		// Subsum - Change Language
		idpage.sumSubChangeLanguage();
		// Sumsub - Start Verification Step
		idpage.startSumSubVerification();
		// Sumsub - Personal Information Step
		idpage.fillSumSubPersonalDetails(country);
		// Sumsub - Select Document Type Step
		idpage.setSumSubIdentificationType(country);
		// Sumsub - Upload Document Step
		idpage.uploadSumSubDocType(Paths.get(Utils.workingDir, fileFront).toString(), Paths.get(Utils.workingDir, fileBack).toString());
		// Switch back to default content
		driver.switchTo().defaultContent();
	}

	public void fillQuizPage() {}

	public boolean goToFinishPage() {
		if (!bIDSumsub) {
			String result = finishpage.getResponse();
			if(!(StringUtils.containsIgnoreCase(result, "Verification Review"))) {
//				LogUtils.info("CPRegister: finish page get message: " + result);
				return false;
			}
		}

		LogUtils.info("CPRegister: go to finish page.");
		return true;
	}

	public boolean backtoHome() {
		finishpage.backHome();
		return true;
	}

	public List<String> getAllCountriesOnEntryPage(){
		return entrypage.getCountries();
	}

	public void printUserRegisterInfo() {
		System.out.println("*********************Register Summary**********************");
		for(Entry<String, String> entry: userdetails.entrySet()) {
			System.out.printf("%-30s : %s\n",entry.getKey(),entry.getValue());
		}
		System.out.println("***********************************************************");
	}

	public boolean checkInfoAfterFinish(String firstName,String regulator, String testEnv, String brand)  {
		if(brand.equalsIgnoreCase("Prospero") || brand.equalsIgnoreCase("um")) {
			LogUtils.info("CPRegister: Prospero and um do not have DB check");
			return true;
		}
		String userID = "";
		String v_Brand = "";
		vantagecrm.CPRegister pre_cp = new vantagecrm.CPRegister();//use some verify function from previous function
		//The following code use the previous methods, need change Regulator to brand;

		v_Brand = GlobalMethods.getPreVBrand(regulator, brand);
		try {
			userID = DBUtils.checkDBStatus(firstName, testEnv, v_Brand);
			userdetails.put("User ID", userID);
			userdetails.put("ID Status",pre_cp.funcGetIDRecord(userID, testEnv, v_Brand));

			userdetails.put("WorldCheck",pre_cp.funcGetWorldCheck(userID, testEnv, v_Brand));

			userdetails.put("POA Status",pre_cp.funcGetPOARecord(userID, testEnv, v_Brand));
			DBUtils.readEmailvUserName(firstName, v_Brand, testEnv, 1);
			userdetails.put("Password",pre_cp.funcCheckEmailtoGetPassword(v_Brand, testEnv, 1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;

	}

	public void checkUserInfo(String email,ENV env,BRAND brand,REGULATOR regulator) {
		String wcStatus [] = {"Processing","Completed","Rejected","Pending","Re_Audit","RE_Register"};//"RE_Register=9"
		String idStatus [] = {"Submitted","Pending","Approved","Rejected"};
		String poaStatus [] = {"Submitted","Pending","Approved","Rejected"};
		UsersDB db = new UsersDB();

		JSONArray jsArray = db.getUserRegistrationInfo(email, env, brand, regulator);
		assertNotNull(jsArray);
		assertTrue(jsArray.size() >0,"Do not find the user by email: " + email);

		JSONObject obj = jsArray.getJSONObject(0);
		userdetails.put("User ID", obj.getString("userId"));
		Integer pos = obj.getInteger("wcStatus");
		if(pos==9) {
			pos = 5;
		}
		userdetails.put("WorldCheck",wcStatus[pos]);
		String status = obj.getString("idStatus");
		if(status==null || status.equalsIgnoreCase("null") || status.equalsIgnoreCase("")) {
			userdetails.put("ID Status", "Have not submitted");
		}else {
			userdetails.put("ID Status", idStatus[obj.getIntValue("idStatus")]);
		}
		status = obj.getString("addrStatus");
		if(status==null || status.equalsIgnoreCase("null") || status.equalsIgnoreCase("")) {
			userdetails.put("POA Status", "Have not submitted");
		}else {
			userdetails.put("POA Status", poaStatus[obj.getIntValue("addrStatus")]);
		}

		String vars = obj.getString("vars");
		if(vars!=null && !vars.trim().equalsIgnoreCase("")) {
			JSONObject var = JSON.parseObject(vars);
			if (var == null) {
				Assert.fail("No welcome email content found for email " + email);
			}
			userdetails.put("Password",var.getString("PASSWORD"));
		}
	}

	public static void main(String args[]) throws Exception {
		String email = "autotestfeasuark@testcrmautomation.com";
		ENV env = ENV.ALPHA;
		BRAND brand = BRAND.VT;
		REGULATOR regulator = REGULATOR.SVG;
		GlobalProperties.brand = brand.toString();
		UsersDB db = new UsersDB();
		JSONArray jsArray = db.getUserRegistrationInfo(email, env, brand, regulator);
		System.out.println(jsArray);
		assertNotNull(jsArray);
		assertTrue(jsArray.size() >0,"Did not find the user by email: " + email);
	}

	public void waitLoading() {
		homepage.waitLoading();
	}

	public boolean checkSumsubExists() {
		waitLoading();
		return entrypage.checkSumsubExists();
	}

	public void closeOpenAccountDialog() {
		pdpage.closeOpenAccountDialog();
	}

}
