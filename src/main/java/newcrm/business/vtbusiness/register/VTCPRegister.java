package newcrm.business.vtbusiness.register;

import newcrm.global.GlobalProperties;
import newcrm.pages.vtclientpages.register.*;
import newcrm.pages.vtclientpages.VTMenuPage;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPRegister;
import newcrm.global.GlobalMethods;
import utils.LogUtils;
import vantagecrm.Utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class VTCPRegister extends CPRegister {
	protected VTFinancialDetailsPage vt_fdpage;
	//String fileFront = "\\src\\main\\resources\\vantagecrm\\doc\\ID_Card_VT.jpg"; //Driver_License2.jpg//Passport.png;Prod_ID.jpeg
	//String fileBack = "\\src\\main\\resources\\vantagecrm\\doc\\ID_Card_Back_VT.jpg";
	Path parent = Paths.get("src", "main", "resources", "vantagecrm", "doc");
	String fileFront = Paths.get(parent.toString(), "ID_Card_Gen.jpg").toString();
	String fileBack = Paths.get(parent.toString(), "ID_Card_Back_Gen.jpg").toString();

	public VTCPRegister(WebDriver driver, String url) {
		super(driver,url);
		vt_fdpage = new VTFinancialDetailsPage(driver);
	}

	@Override
	protected void setUpHomepage() {
		homepage = new VTRegisterHomePage(driver,registerURL);
	}

	@Override
	protected void setUpPDpage() {
		this.pdpage = new VTPersonalDetailsPage(driver);
	}

	@Override
	protected void setUpAddresspage() {
		addresspage = new VTResidentialAddressPage(driver);
	}

	@Override
	protected void setUpFDpage() {
		fdpage = new VTFinancialDetailsPage(driver);
	}

	@Override
	public void fillFinacialPage() {
		vt_fdpage.answerAllQuestionsWithoutReturn();
	}

	@Override
	protected void setUpACpage() {
		acpage = new VTAccountConfigurationPage(driver);
	}

	@Override
	protected void setUpIDpage() {
		idpage = new VTConfirmIDPage(driver);
	}

	@Override
	protected void setUpFinishPage() {
		finishpage = new VTFinishPage(driver);
	}

	@Override
	public void setUserInfo(String fistName,String country,String email,String pwd) {
		setCountry(country);
		setEmail(email);
	}
	@Override
	public boolean fillAccountPage(GlobalProperties.PLATFORM platform) {
		if (!acpage.setPlatForm(platform)) {
			return false;
		}
		userdetails.put("Platform", platform.toString());
		userdetails.put("Account Type", acpage.setAccountType());
		userdetails.put("Currency", acpage.setCurrency());
		acpage.tickBox();
		return true;

	}
	@Override
	public boolean goToPersonalDetailPage() {
		//check
		VTMenuPage menu = new VTMenuPage(this.driver);
//		menu.refresh();
		menu.closeCouponGuideDialog();
//		menu.changeLanguage("English");

		/*String firstName = userdetails.get("First Name");
		if(!firstName.equalsIgnoreCase(pdpage.getFirstName().trim())) {
			LogUtils.info("CPRegister: First Name brought in from Home page is wrong. It is " + pdpage.getFirstName() + ". It should be " + firstName);
			return false;
		}
		String lastName = userdetails.get("Last Name");
		if(!lastName.equalsIgnoreCase(pdpage.getLastName().trim())) {
			LogUtils.info("CPRegister: Last Name brought in from Home page is wrong. It is " + pdpage.getLastName() + ". It should be " + lastName);
			return false;
		}

		String phonenum = userdetails.get("Phone Number");
		String p2= pdpage.getPhone().trim().replaceAll("\\s+","");
		if(!phonenum.equalsIgnoreCase(pdpage.getPhone().trim().replaceAll("\\s+",""))) {
			LogUtils.info("CPRegister: Phone Number brought in from Home page is wrong. It is " + pdpage.getPhone().trim() + ". It should be " + phonenum);
			return false;
		}*/
		LogUtils.info("CPRegister: go to personal details page.");
		return true;
	}

	@Override
	public void fillPersonalDetails(String idNum,String firstName, String lastName,String phone) {
		try
		{
			if(driver.findElement(By.xpath("(//img[@class='closeImg'])[2]")).isDisplayed())
			{
				driver.findElement(By.xpath("(//img[@class='closeImg'])[2]")).click();
			}
		}
		catch(Exception e)
		{
			LogUtils.info("No popup window display");
		}
		userdetails.put("First Name", pdpage.setfirstName(firstName));
		userdetails.put("Last Name", pdpage.setLastName(lastName));
		userdetails.put("Phone Number", pdpage.setPhone(phone));
		userdetails.put("Date Of Birth", pdpage.setBirthDay());
		userdetails.put("gender", pdpage.setGender("Female"));
	}

	@Override
	public void setUserInfo(String firstName, String lastName, String phonenum,String email,String pwd, String brand) {
		//entrypage.setFirstName(firstName);
		//entrypage.setLastName(lastName);
		//entrypage.setPhone(phonenum);
		entrypage.setEmail(email);
		entrypage.setPassword(pwd);
		entrypage.setBrand(brand);

		//userdetails.put("First Name", firstName);
		//userdetails.put("Last Name",lastName);
		//userdetails.put("Phone Number", phonenum);
		userdetails.put("Email", email);
		userdetails.put("pwd", pwd);
		userdetails.put("Brand", brand);
	}
	@Override
	public void fillIDPage() {
		Path parent = Paths.get("src", "main", "resources", "vantagecrm", "doc");
		String fileFront = Paths.get(parent.toString(), "Update_ID_Card.jpg").toString();
		String fileBack = Paths.get(parent.toString(), "Update_ID_Card_Back.jpg").toString();

		String idnum = "TESTID"+GlobalMethods.getRandomNumberString(10);

		idpage.setIdentificationType();
		idpage.setIDNumber(idnum);
		userdetails.put("id Number", idnum);
		idpage.next();

		idpage.uploadID(Paths.get(Utils.workingDir, fileFront).toString());
		idpage.uploadIDBack(Paths.get(Utils.workingDir, fileBack).toString());
		idpage.nextSec();
	}

	@Override
	public void setEmail(String email) {
		entrypage.setEmail(email);
		userdetails.put("Email", email);
		entrypage.waitLoading();

		entrypage.next();
		entrypage.waitLoading();
	}
	@Override
	public boolean goToAddressPage() {
		pdpage.submit();
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

	@Override
	public boolean goToAccountPage() {
		pdpage.submit();
		LogUtils.info("CPRegister: go to ACCOUNT CONFIGURATION page.");
		return true;
	}

	@Override
	public boolean goToIDPage() {
		acpage.next();
		LogUtils.info("CPRegister: go to ACCOUNT OPENING page.");
		idpage.proceedToIDVerfication();
		LogUtils.info("CPRegister: go to CONFIRM YOUR ID page.");
		return true;
	}

	@Override
	public boolean goToFinishPage() {
		if (!bIDSumsub) {
			String result = finishpage.getResponse();
			if(!(StringUtils.containsIgnoreCase(result, "under review"))) {
//				LogUtils.info("CPRegister: finish page get message: " + result);
				return false;
			}
		}

		LogUtils.info("CPRegister: go to finish page.");
		return true;
	}

}
