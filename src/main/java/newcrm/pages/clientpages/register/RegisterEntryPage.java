package newcrm.pages.clientpages.register;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import newcrm.business.dbbusiness.EmailDB;
import newcrm.global.GlobalProperties;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.TimeoutException;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.USERTYPE;
import newcrm.pages.Page;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import tools.ScreenshotHelper;
import utils.LogUtils;

public class RegisterEntryPage extends Page {

	private String tradeUrl = "";
	public RegisterEntryPage(WebDriver driver) {
		super(driver);
	}
	
	//The following methods aim to find elements
	protected WebElement getActionInput() {
		return this.findVisibleElemntByXpath("//input[@class='change_live']");
	}
	
	protected WebElement getActionButton() {
		return this.findClickableElemntBy(By.id("change_button"));
	}
	
	protected WebElement getFirstNameInput() {
		return this.findVisibleElemntBy(By.id("firstName"));
	}

	protected WebElement getASICFirstNameInput() {
		return this.findVisibleElemntBy(By.id("firstname"));
	}

	protected WebElement getLastNameInput() {
		return this.findVisibleElemntBy(By.id("lastName"));
	}

	protected WebElement getASICLastNameInput() {
		return this.findVisibleElemntBy(By.id("lastname"));
	}

	protected WebElement getCountryInput() {
		return this.findClickableElemntBy(By.id("country"));
	}
	
	protected List<WebElement> getCountryElements(){
		String xpath = "//div[@class='country_code']/div/div[@class='results_option']";
		this.findVisibleElemntByXpath(xpath);
		return driver.findElements(By.xpath(xpath));
	}
	
	protected WebElement getPhoneInput() {
		return this.findVisibleElemntBy(By.id("phone"));
	}

	protected WebElement getMobileInput() {
		return assertVisibleElementExists(By.id("mobile"), "Phone");
	}

	protected WebElement getEmailInput() {
		return this.findVisibleElemntBy(By.id("email"));
	}

	protected WebElement getBrandInput() {
		return this.findVisibleElemntBy(By.id("brand"));
	}

	protected Select getRegulatorSelect() {
		WebElement regulators = this.findClickableElemntBy(By.id("regulator"));
		Select select = new Select(regulators);
		return select;
	}

	protected Select getRegisterSelect() {
		WebElement registerInterface = this.findClickableElemntBy(By.id("register-interface"));
		Select select = new Select(registerInterface);
		return select;
	}


	protected Select getUserTypeSelect() {
		WebElement type = this.findVisibleElemntBy(By.id("accountType"));
		return new Select(type);
	}
	
	/*protected WebElement getSubmitButton() {
		return this.findClickableElemntBy(By.id("button"));
	}*/

	protected WebElement getSubmitButton() {
		return this.findClickableElemntBy(By.id("sub-open"));
	}

	protected WebElement getSendCodeBtnEle() {
		return assertElementExists(By.xpath("//button[@id='getVerifyCode']"), "Send Code button");
	}

	//The following method aim to return the data on page

	public List<String> getCountries(){
		ArrayList<String> result = new ArrayList<>();
		this.getCountryInput().click();
		//this.clickElement(this.getCountryInput());
		List<WebElement> countries = this.getCountryElements();
		for(WebElement e:countries) {
			String country = e.getAttribute("data-conutry");
			if(country != null && country.trim().length()>0) {
				result.add(country.trim());
			}
		}
		this.clickElement(this.getCountryInput());
		return result;
	}
	
	public List<String> getRegulators(){
		ArrayList<String> result = new ArrayList<>();
		Select r = this.getRegulatorSelect();
		if(r == null) {
			return result;
		}
		List<WebElement> wrs = r.getOptions();
		for(WebElement e : wrs) {
			String regulator = e.getAttribute("value");
			if(regulator!=null && regulator.trim().length()>0) {
				result.add(regulator.trim());
			}
		}
		return result;
	}
	
	public List<USERTYPE> getUserType(){
		ArrayList<USERTYPE> result = new ArrayList<>();
		Select r = this.getUserTypeSelect();
		if(r == null) {
			return result;
		}
		List<WebElement> wrs = r.getOptions();
		for(WebElement e : wrs) {
			String usertype = e.getAttribute("value");
			if(usertype!=null && usertype.trim().length()>0) {
				for(USERTYPE type : USERTYPE.values())
				{
					if(type.toString().equalsIgnoreCase(usertype.trim())) {
						result.add(type);
						break;
					}
				}
			}
		}
		return result;
	}
	
	
	//following methods aim to put values to the page
	public boolean setCountry(String country) {
		this.getCountryInput().click();
		List<WebElement> countries = this.getCountryElements();
		for(WebElement e:countries) {
			String value = e.getAttribute("data-conutry");
			if(value != null && value.trim().length()>0) {
				if(country.equalsIgnoreCase(value.trim())) {
					this.moveElementToVisible(e);
					e.click();
					LogUtils.info("RegisterEntryPage: set country to: " + value.trim());
					return true;
				}
			}
		}
		LogUtils.info("RegisterEntryPage: Do not find country : " + country +".");
		this.getCountryInput().click();
		return false;
	}

	public void setPassword(String pwd)
	{
		WebElement password = driver.findElement(By.xpath("//input[@id='password']"));
		password.sendKeys(pwd);

		LogUtils.info("RegisterEntryPage: set Pwd to: " + pwd);
	}

	public boolean setRegulator(String regulator) {
		Select select = this.getRegulatorSelect();
		if(select == null) {
			LogUtils.info("WARNING ** RegisterEntryPage: Do not find regulator element");
			return false;
		}
		select.selectByValue(regulator.toUpperCase().trim());
		LogUtils.info("RegisterEntryPage: set Regulator to: " + regulator.trim());
		return true;
	}

	public boolean setRegisterInterface(String registerInterface) {
		String value;
		Select select = this.getRegisterSelect();
		if(select == null) {
			LogUtils.info("WARNING ** RegisterEntryPage: Do not find regulator element");
			return false;
		}

		if (registerInterface.equalsIgnoreCase("V2"))
		{
			value = "/web-api/api/registrationV2/register";
		}
		else
		{
//			value = "/web-api/api/registration/register";
			value = "/hgw/user-api/bsn/registration/register";
		}
		select.selectByValue(value.trim());
		LogUtils.info("RegisterEntryPage: set registerInterface to: " + value.trim());
		return true;
	}

	
	public boolean setUserType(USERTYPE type) {
		Select select = this.getUserTypeSelect();
		if(select == null) {
			LogUtils.info("WARNING ** RegisterEntryPage: Do not find user type element");
			return false;
		}
		select.selectByValue(type.toString());
		LogUtils.info("RegisterEntryPage: set user type to: " + type.toString());
		return true;
	}
	
	//fill input fields
	public void setFirstName(String name) {
		this.setInputValue(this.getFirstNameInput(), name);
		LogUtils.info("RegisterEntryPage: set First Name to: " + name);
	}

	public void setASICFirstName(String name) {
		this.setInputValue(this.getASICFirstNameInput(), name);
		LogUtils.info("RegisterEntryPage: set First Name to: " + name);
	}

	public void setWid(String wid) {
		driver.findElement(By.xpath("//input[@id='wid']")).sendKeys(wid.toUpperCase());
		LogUtils.info("RegisterEntryPage: set Wid to: " + wid.toUpperCase());
	}

	public void setBranchVersion(String branchVersion) {
		branchVersion = branchVersion == null ? "" : branchVersion;
		assertElementExists(By.xpath("//input[@id='branchversion']"), "Branch Version").sendKeys(branchVersion);
		LogUtils.info("RegisterEntryPage: set Branch Version to: " + branchVersion);
	}
	
	public void setLastName(String name) {
		this.setInputValue(this.getLastNameInput(), name);
		LogUtils.info("RegisterEntryPage: set Last Name to: " + name);
	}

	public void setASICLastName(String name) {
		this.setInputValue(this.getASICLastNameInput(), name);
		LogUtils.info("RegisterEntryPage: set Last Name to: " + name);
	}

	public void setPhone(String phone) {
		this.setInputValue(this.getPhoneInput(), phone);
		LogUtils.info("RegisterEntryPage: set phone to: " + phone);
	}

	public void setMobile(String phone) {
		this.setInputValue(this.getMobileInput(), phone);
		LogUtils.info("RegisterEntryPage: set phone to: " + phone);
	}

	public void setEmail(String email) {
		this.setInputValue(this.getEmailInput(), email);
		LogUtils.info("RegisterEntryPage: set email to: " + email);
	}

	public void setBrand(String brand) {
		this.setInputValue(this.getBrandInput(), brand);
		LogUtils.info("RegisterEntryPage: set Brand to: " + brand);
	}

	public void setActionUrl(String url) {
		WebElement e = this.getActionInput();
		this.tradeUrl = url.trim();
		if(e == null) {
			LogUtils.info("WARNING ** RegisterEntryPage: Do not find action input element");
			return;
		}
		this.setInputValue(this.getActionInput(), url);
		WebElement b = this.getActionButton();
		this.moveElementToVisible(b);
		b.click();
		LogUtils.info("RegisterEntryPage: set action url to: " + url);
	}
	
	public void submit() {
		LogUtils.info("Start to click submit button in register entry page");
		WebElement button = this.getSubmitButton();
		button.click();
		LogUtils.info("Already click submit in register entry page ");
		//this.clickElement(this.getSubmitButton());
		this.waitLoadingForCustomise(180);
//		this.checkUrlContains(this.tradeUrl);
		this.waitLoading();
		ScreenshotHelper.takeFullPageScreenshot(getDriver(), "screenshots", "RegSubmit");
	}

    public void submit_WithoutCheckURL() {
        LogUtils.info("Start to click submit button in register entry page");
        WebElement button = this.getSubmitButton();
        button.click();
        LogUtils.info("Already click submit in register entry page ");
        //this.clickElement(this.getSubmitButton());
        this.waitLoadingForCustomise(180);
        this.waitLoading();
    }

	public void submit_Next() {
		WebElement button = assertElementExists(By.id("next"), "Create Account button");
		button.click();
		LogUtils.info("Click Create Account button");
		this.waitLoading();
	}

	public void next() {
		WebElement button = driver.findElement(By.xpath("//input[@id='sub-next-one']"));
		button.click();
		this.waitLoading();
	}
	
	public void submit(String url) {
		WebElement button = this.getSubmitButton();
		button.click();
		//this.clickElement(this.getSubmitButton());
		this.checkUrlContains(url);
		this.waitLoading();
	}

	public void clickCodeBtn() {
		waitLoading();
		WebElement codeBtn = getSendCodeBtnEle();
		this.moveElementToVisible(codeBtn);
		codeBtn.click();
		LogUtils.info("Click Send Code button");
		waitLoading();
	}

	public void sendCode(String code)
	{
		WebElement emailCode = driver.findElement(By.xpath("//input[@id='captcha']"));
		emailCode.sendKeys(code);
		waitLoading();
	}

	/**
	 *  this function must be invoke at first if you need set IB code
	 * @param code ib code
	 */
	public void setIBcode(String code, boolean bIsEnvProd) {
		if(code==null || code.trim().length()<1) {
			return;
		}
		if(driver.getCurrentUrl().contains(code)) {
			return;
		}
		if (!code.equals("")) {
			String IBurl = driver.getCurrentUrl() +"?affid="+code.trim();
			driver.navigate().to(IBurl);

            if (bIsEnvProd) {
                this.checkUrlContains(code);
            } else {
                this.checkUrlContains_customWait(code);
            }

            LogUtils.info("RegisterEntryPage: set register url to: " + driver.getCurrentUrl());
		}
	}
	
	public void setRAFCode(String code) {
		if(code==null || code.trim().length()<1) {
			return;
		}
		if(driver.getCurrentUrl().contains(code)) {
			return;
		}
		if (!code.equals("")) {
			String IBurl = driver.getCurrentUrl() +"?c="+code.trim();
			driver.navigate().to(IBurl);
			this.checkUrlContains(code);
			LogUtils.info("RegisterEntryPage: set register url to: " + driver.getCurrentUrl());
		}
	}

    public void setCampaignCode(String code) {
        if(code==null || code.trim().length()<1) {
            return;
        }
        if(driver.getCurrentUrl().contains(code)) {
            return;
        }
        if (!code.equals("")) {
            String IBurl = driver.getCurrentUrl() +"?cs="+code.trim();
            driver.navigate().to(IBurl);
            this.checkUrlContains(code);
            GlobalMethods.printDebugInfo("RegisterEntryPage: set register url to: " + driver.getCurrentUrl());
        }
    }

    public void setPartnerNum(String code) {
		if(code==null || code.trim().length()<1) {
			return;
		}
		if(driver.getCurrentUrl().contains(code)) {
			return;
		}
		if (!code.equals("")) {
			String IBurl = driver.getCurrentUrl() +"?partnerNumber="+code.trim();
			driver.navigate().to(IBurl);
			this.checkUrlContains(code);
			LogUtils.info("RegisterEntryPage: set register url to: " + driver.getCurrentUrl());
		}
	}

	public void checkNonUSResident()
	{

			WebElement checkbox = driver.findElement(By.xpath("//input[@type='checkbox']"));
			//checkbox.click();
			js.executeScript("arguments[0].click()", checkbox);
	}

	public void checkASICNonUSResident()
	{

		WebElement checkbox = driver.findElement(By.xpath("//input[@type='checkbox']"));
		//checkbox.click();
		js.executeScript("arguments[0].click()", checkbox);
	}

	public void checkAgreeCommunication()
	{

		WebElement checkbox = driver.findElement(By.xpath("//input[@id='agreeCommunication']"));
		//checkbox.click();
		js.executeScript("arguments[0].click()", checkbox);
	}

	public boolean checkSumsubExists()
	{
		waitLoader();
		List<WebElement> elements = driver.findElements(By.xpath("//div[@id='sumsub-websdk-container']"));
		return !elements.isEmpty() && elements.get(0).isDisplayed();
	}


	protected WebElement getExpandAccountConfigEle() { return null; }

	public void clickExpandAccountConfig() {}

	public enum REG_SRC {
		LIVE_REG, DEMO_REG
	}

	public void checkNavigateSuccess(String traderURL, String oldUrl, REG_SRC regSrc) {

		String regSrcDesc = (regSrc == REG_SRC.DEMO_REG) ? "Demo" : "Live";
		boolean isNavigateFail;
		boolean urlChanged;

		try {
			LogUtils.info("Previous URL: " + oldUrl);

//			if(!oldUrl.contains(traderURL))
//			{
//				//jump to third party page sometimes very slow
//				Thread.sleep(10000);
//			}

			try {
				// Check if still remains at live registration page after submission
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
				urlChanged = wait.until((ExpectedCondition<Boolean>) wd ->
						!wd.getCurrentUrl().equals(oldUrl)
				);

			} catch (TimeoutException e) {
				urlChanged = false;
			}

			LogUtils.info("Current URL: " + getCurrentURL());

			// Verify navigated destination when url changed
			if (urlChanged) {
				try {
					// Check if still remains at URL redirection
					WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
					wait.until((ExpectedCondition<Boolean>) wd ->
							!wd.getCurrentUrl().contains("/?a=")
					);

					LogUtils.info("Current URL: " + getCurrentURL());

				} catch (TimeoutException e) {
					Assert.fail(String.format("Successfully navigated away from the %s Registration Page but timeout during URL redirection (%s)", regSrcDesc, getCurrentURL()));
				}

				// Check if redirect to error page
				if (getCurrentURL().contains("error")) {
					Assert.fail(String.format("An error occurred during %s Registration submission redirection. URL: %s", regSrcDesc, getCurrentURL()));
				}

				if (regSrc == REG_SRC.DEMO_REG) {
					// Check if navigates to specific url and demo page url after registration submission
					isNavigateFail = !checkNavigateUrl(traderURL) || !getCurrentURL().contains("/demo");
				} else {
					// Check if navigates to specific url and register page url after registration submission
					isNavigateFail = !checkNavigateUrl(traderURL) || (!getCurrentURL().contains("/register")  && !getCurrentURL().contains("/kyc"));
				}

				if (isNavigateFail) {
					Assert.fail(String.format("Successfully navigated away from the %s Registration Page but destination URL (%s) is incorrect", regSrcDesc, getCurrentURL()));
				}

				LogUtils.info(String.format("Successfully navigated away from the %s Registration Page", regSrcDesc));

			} else {
				Assert.fail(String.format("%s Registration page still present after timeout", regSrcDesc));
			}

		} catch (Exception e) {
			Assert.fail(String.format("An error occurred during %s Registration submission. Error Msg: %s", regSrcDesc, e.getMessage()));
		}
	}

}
	

