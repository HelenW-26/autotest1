package newcrm.pages.clientpages.register;



import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.USERTYPE;
import newcrm.pages.Page;
import newcrm.pages.clientpages.elements.register.HomePageElements;
import utils.LogUtils;
import vantagecrm.Utils;

public class RegisterHomePage extends Page {
	
	public RegisterHomePage(WebDriver driver,String url) {
		super(driver);
		driver.get(url);
		this.waitLoading();
	}
	
	protected WebElement getDemoRegister() {
		//return this.findClickableElementByXpath("(//a[@class='btn_demo'])[1]");
		HomePageElements els = PageFactory.initElements(driver, HomePageElements.class);
		//return this.findClickableElementByXpath("(//a[contains(text(),'Demo Account') or contains(text(),'DEMO ACCOUNT')])[1]");
		return els.demoButton;
		
	}
	protected WebElement getLiveReister() {
		
		//return this.findClickableElementByXpath("(//a[@class='btn_live'])[1]");
		//return this.findClickableElementByXpath("(//a[contains(text(),'Live Account') or contains(text(),'LIVE ACCOUNT')])[1]");
		HomePageElements els = PageFactory.initElements(driver, HomePageElements.class);
		return els.liveButton;
	}

	protected WebElement getProtectedPwdInput() {
		return assertElementExists(By.xpath("//input[@name='post_password']"), "Page Protected Password");
	}

	protected WebElement getProtectedPwdSubmitBtn() {
		return assertElementExists(By.xpath("//input[@name='Submit']"), "Page Protected Submit button");
	}

	protected WebElement getDemoDomainUrlInput() { return null; }
	protected WebElement getDemoDomainUrlSubmitBtn() { return null; }
	protected WebElement getDemoContinueBtn() { return null; }

	public void registerDemoAccount() {
		WebElement a_demo = getDemoRegister();
		this.moveElementToVisible(a_demo);
		a_demo.click();
		this.checkUrlContains("/forex-trading-account/");
	}
	
	public void registerLiveAccount() {
		WebElement a_live = this.getLiveReister();
		this.moveElementToVisible(a_live);
		this.clickElement(a_live);
		if(driver.getWindowHandles().size()>1) {
			driver.switchTo().window(driver.getWindowHandles().toArray()[1].toString());
		}
		this.checkUrlContains("/forex-trading-account/");
	}
	
	public static void main(String[] args) {
		WebDriver driver = null;
		driver = Utils.funcSetupDriver(driver, "chrome", "false");
		RegisterHomePage page = new RegisterHomePage(driver, GlobalProperties.ALPHAURL);
		page.registerLiveAccount();
		RegisterEntryPage entry = new RegisterEntryPage(driver);
		//entry.setIBcode("12345678");
		entry.setActionUrl("https://secure-staging-au-ex.crm-alpha.com");
		String firstName = GlobalMethods.getRandomString(10);
		entry.setFirstName(firstName);
		String lastName = "CRMTest";
		entry.setLastName("CRMTest");
		entry.setCountry("Spain");
		entry.setPhone(GlobalMethods.getRandomNumberString(10));
		entry.setEmail("autotest"+GlobalMethods.getRandomString(5)+"@mailinator.com");
		entry.setRegulator("vfsc2");
		entry.setUserType(USERTYPE.Individual);
		entry.submit();
		//check db
		PersonalDetailsPage details = new PersonalDetailsPage(driver);
		System.out.println(details.getPageTitle());
		System.out.println(details.setPersonTitle());
		System.out.println(details.getFirstName());
		details.setMiddleName("Automation Test");
		System.out.println(details.getLastName());
		System.out.println(details.getEmail());
		System.out.println(details.getPhone());
		System.out.println(details.setNationality());
		System.out.println(details.setBirthDay());
		System.out.println(details.setBirthPlace());
		System.out.println(details.setIdentificationType());
		details.setIDNumber("TESTID"+GlobalMethods.getRandomNumberString(10));
		//details.setRefer("automation");
		details.submit();
		
		//check db
		
		ResidentialAddressPage addresspage = new ResidentialAddressPage(driver);
		addresspage.setAddress("123", GlobalMethods.getRandomString(20));
		addresspage.setState(GlobalMethods.getRandomString(5)+" state");
		addresspage.setSuburb("test suburb");
		addresspage.setPostcode("2222");
		addresspage.next();
		
		//check db
		
		FinancialDetailsPage fpage = new FinancialDetailsPage(driver);
		System.out.println(fpage.setEmploymentStatus());
		System.out.println(fpage.setEstimatedAnnualIncome());
		System.out.println(fpage.setEstimatedSavingsAndInvestments());
		System.out.println(fpage.setEstimatedIntendedDeposit());
		System.out.println(fpage.setSourceOfFunds());
		System.out.println(fpage.setNumberOfTradesPerWeek());
		System.out.println(fpage.setTradingAmountPerWeek());
		fpage.next();
		//check db
		
		AccountConfigurationPage acpage = new AccountConfigurationPage(driver);
		acpage.setPlatForm(PLATFORM.MT4);
		System.out.println(acpage.setAccountType());
		System.out.println(acpage.setCurrency());
		acpage.tickBox();
		acpage.next();
		
		//check db

		String fileFront = "\\src\\main\\resources\\vantagecrm\\doc\\ID_Card.png"; //Driver_License2.jpg//Passport.png;Prod_ID.jpeg
		String fileBack = "\\src\\main\\resources\\vantagecrm\\doc\\ID_Card_Back.png";
		String filePOA = "\\src\\main\\resources\\vantagecrm\\doc\\ID_Card.png";
		ConfirmIDPage idpage = new ConfirmIDPage(driver);
		idpage.uploadID(Utils.workingDir + fileFront);
		idpage.uploadID(Utils.workingDir + fileBack);
		idpage.filleName(firstName, "Automation Test", lastName);
		idpage.uploadPOA(Utils.workingDir + filePOA);
		idpage.uploadPOA(Utils.workingDir + filePOA);
		idpage.next();
		
		FinishPage finishpage = new FinishPage(driver);
		finishpage.getResponse();
		finishpage.backHome();
		
		//check data CPRegister.java from line 907
	}

	public void setDemoRegistrationDomainUrl(String url) {
		setInputValue_withoutMoveElement(getDemoDomainUrlInput(), url);
		LogUtils.info("Set Domain Url to: " + url);

		getDemoDomainUrlSubmitBtn().click();
		LogUtils.info("Click Domain Url Submit button");
	}

	public void clickDemoContinueBtn() {
		getDemoContinueBtn().click();
		LogUtils.info("Click Continue button");
	}

}
