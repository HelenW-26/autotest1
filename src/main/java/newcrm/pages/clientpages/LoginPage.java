package newcrm.pages.clientpages;

import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.PORTAL_LANGUAGE;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import newcrm.pages.Page;
import newcrm.pages.clientpages.elements.LogoutElements;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import tools.ScreenshotHelper;
import utils.LogUtils;

import java.time.Duration;
import java.util.AbstractMap;
import java.util.Map;

/***
 * Login Page: control elements on login page
 * @author QA team
 *
 */

public class LoginPage extends Page {
	
	protected String trade_url;
	protected String code = "999999";

	public LoginPage(WebDriver driver,String url) {
		super(driver);
		trade_url = url;
		this.driver = driver;
		driver.get(url);
		
	}

	/***
	 * 
	 * @param name username or email
	 */
	public void setUserName(String name) {
		assertVisibleElementExists(By.xpath("//input[@data-testid='userName_login']"), "Login Email").sendKeys(name);
		LogUtils.info("Set Login Email");
	}
	/***
	 * 
	 * @param password user password
	 */
	public void setPassWord(String password) {
		assertVisibleElementExists(By.xpath("//input[@data-testid='password_login']"), "Login Password").sendKeys(password);
		LogUtils.info("Set Login Password");
	}

	protected WebElement getLanguageIconEle() {
		return assertElementExists(By.cssSelector("[data-testid='dropdownFlag'] svg.ht-icon-languages"), "Language Icon");
	}

	protected WebElement getLanguageListEle(String ddlId) {
		return assertElementExists(By.cssSelector("#" + ddlId), "Language List");
	}

	protected WebElement getActiveLanguageEle(WebElement parentEle) {
		return assertElementExists(By.cssSelector("div.language-item__active"), "Active Language", parentEle);
	}

	protected WebElement getLanguageItemEle(WebElement parentEle, PORTAL_LANGUAGE language) {
		return assertElementExists(By.cssSelector("[data-testid='" + language.getTestId() + "']"), language.getDesc() + " Language", parentEle);
	}

	protected WebElement getPhonePanelEle() {
		return assertElementExists(By.xpath("//div[@id='tab-Phone']"), "Phone Number Tab");
	}

	protected WebElement getLoginPhoneCountryCodeEle() {
		return assertElementExists(By.xpath("//div[@id='pane-Phone']//span[@class='el-popover__reference-wrapper']//input"), "Phone Country Code");
	}

	protected WebElement getLoginPhoneNoEle() {
		return assertElementExists(By.xpath("//input[@data-testid='mobile_login']"), "Phone Number");
	}

	protected WebElement getCountryCodeSearchEle() {
		return assertElementExists(By.xpath("//input[@data-testid='search']"), "Phone Country Code Search Box");
	}

	protected WebElement getEmailTOTPContentEle() {
		return assertVisibleElementExists(By.cssSelector("div.main_verification"), "Email Security Authentication Content");
	}

	protected WebElement getEmailTOTPEle() {
		return assertElementExists(By.xpath("//div[@class='main_verification']//input"), "Email Authentication Verification Code");
	}

	protected WebElement getPhoneLoginPwdEle() {
		return assertElementExists(By.xpath("//div[@id='pane-Phone']//input[@data-testid='password_login']"), "Login Password");
	}

	protected WebElement getEmailTOTPSubmitBtnEle() {
		return assertElementExists(By.xpath("//button[@data-testid='changePw']"), "Email TOTP Submit button");
	}

	protected WebElement getEmailTOTPLabelEle() {
		return assertElementExists(By.cssSelector("label[for='code']"), "Verification Code Label");
	}

	public void clickPhonePanel() {
		WebElement phonePanel = getPhonePanelEle();
		String ariaSelected = phonePanel.getAttribute("aria-selected");

		if (ariaSelected == null) {
			triggerElementClickEvent_withoutMoveElement(phonePanel);
			LogUtils.info("Click Phone Number Tab");
		}
	}

	public void setLoginPhoneNo(String phoneNo) {
		String values[] = phoneNo.split(" ");
		String strCountryCode = values[0] == null ? "" : values[0].trim();
		String strPhoneNo    = values[1] == null ? "" : values[1].trim();

		// Click on country code dropdown
		WebElement countryCode = getLoginPhoneCountryCodeEle();
		countryCode.click();
		waitLoading();
		LogUtils.info("Click Country Code");
		assertVisibleElementExists(By.xpath("//div[contains(@class,'el-popper') and not(contains(@style, 'display: none'))]"), "Phone Country Code List");

		// Filter country code via search box
		WebElement countryCodeSearch = getCountryCodeSearchEle();
		countryCodeSearch.click();
		LogUtils.info("Click Country Code List search box");
		countryCodeSearch.sendKeys(strCountryCode);
		LogUtils.info("Search for Country Code: " + strCountryCode);

		// Select country code
		WebElement countryCodeVal = assertElementExists(By.xpath("//span[@class='view-value' and text()='+" + strCountryCode + "']"), strCountryCode + " Country Code");
		countryCodeVal.click();
		LogUtils.info("Select Country Code");

		// Set phone number
		WebElement phoneNumber = getLoginPhoneNoEle();
		phoneNumber.sendKeys(strPhoneNo);
		LogUtils.info("Set Login Phone Number");
	}

	public void setLoginPhonePassword(String password) {
		WebElement pwd = getPhoneLoginPwdEle();
		pwd.sendKeys(password);
		LogUtils.info("Set Login Password");
	}

	public void setEmailTOTP(String totp) {
		WebElement totpEle = this.getEmailTOTPEle();
		triggerElementClickEvent_withoutMoveElement(totpEle);
		setInputValue_withoutMoveElement(totpEle, totp);
		LogUtils.info("Set Email TOTP: " + totp);

		// Click outside totp field for totp validation
		WebElement totpLabelEle = getEmailTOTPLabelEle();
		triggerElementClickEvent_withoutMoveElement(totpLabelEle);
	}

	protected WebElement getSubmit() {
		return assertClickableElementByTestIdExists("login", "Email Login button");
	}

	protected WebElement getPhoneSubmitBtnEle() {
		return assertClickableElementExists(By.xpath("//div[@id='pane-Phone']//button"), "Phone Number Login button");
	}

	protected By getAlertMsgBy() {
		return By.cssSelector("div.el-message.ht-message--error > p");
	}

	protected By getLoginAuthMsgBy() {
		return By.xpath("//div[@class='main_verification']//p[contains(@class, 'red_text')]");
	}

	protected WebElement getAlertMsgEle() {
		return assertElementExists(getAlertMsgBy(), "Error Alert Message Content");
	}

	/***
	 * submit
	 */
	public boolean submitOld() {
		
		try {
			this.waitLoading();
			this.getSubmit().click();
			//this.waitvisible.until(ExpectedConditions.urlContains("home"));
			this.waitLoading();
		}catch(Exception e){
			System.out.println("Login Failed: "+ e.getMessage());
			return false;
		}
		return true;	
	}

	public void submit() {
		waitLoading();

		try {
			WebElement btn = this.getSubmit();
			triggerElementClickEvent_withoutMoveElement(btn);
			LogUtils.info("Click Email Login button");
		} catch (Exception e) {
			WebElement btn = findClickableElementByXpath("//*[@data-testid='login']");
			js.executeScript("arguments[0].click();", btn);
			LogUtils.info("Click Email Login button again");
		}
	}

	public void submitPhoneLogin() {
		waitLoading();
		WebElement btn = this.getPhoneSubmitBtnEle();
		triggerElementClickEvent_withoutMoveElement(btn);
		LogUtils.info("Click Phone Number Login button");
	}

	public void submitIB() {
		waitLoading();
		WebElement btn = this.getSubmit();
		triggerElementClickEvent_withoutMoveElement(btn);
		LogUtils.info("Click IB Email Login button");
	}

	public boolean logout() {
		if(!isLogin()) {
			return true;
		}
		try {
			LogoutElements els = PageFactory.initElements(driver, LogoutElements.class);
			els.dropDown.click();
			this.waitLoading();
			els.logOut.click();
			waitvisible.until(ExpectedConditions.urlContains("login"));
		}catch(Exception e) {
			System.out.println("Logout Failed: " + e.getMessage());
			return false;
		}
		return true;
	}
	
	public void gotoCpIndex() {
		try {

			if (!checkNavigateUrl(trade_url)) {
				driver.get(trade_url);
				this.waitLoading();
				LogUtils.info("Redirect to home page");
			} else {
				driver.switchTo().defaultContent();
			}

		} catch (Exception e) {
			driver.get(trade_url);
			this.waitLoading();
			LogUtils.info("Redirect to home page");
		}
	}
	
	public boolean isLogin() {
		String url = driver.getCurrentUrl().toLowerCase();
		return !url.contains("login");
	}

	public boolean checkOTPDialog() {
		return false;
	}

	public void clickEmailCodeBtn() {}

	public String sendEmailCode(String code) {
		return "";
	}

	public void clickEmailCodeSubmitBtn() {}

	public String checkExistsLoginAlertMsg() {
		return checkExistsAlertMsg(this::getAlertMsgBy, "Login");
	}

	public String getLoginAlertMsg() {
		return getAlertMsgEle().getText();
	}

	public Map.Entry<Boolean, String> checkLoginSuccess() {

		try {
			// Check if still remains at login page after login submission
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
			boolean urlChanged = wait.until((ExpectedCondition<Boolean>) wd ->
					!wd.getCurrentUrl().contains("login")
			);

			LogUtils.info("Current URL: " + getCurrentURL());

			if (urlChanged) {
				// Check if navigates to trader url and default page after login success
				if (!checkNavigateUrl(trade_url) || (!getCurrentURL().contains("/home") && !getCurrentURL().contains("/securityManagement"))) {
					return new AbstractMap.SimpleEntry<>(false, "Successfully navigated away from the Login Page but destination URL (" + getCurrentURL() + ") is incorrect");
				}

				LogUtils.info("Login success. Successfully navigated away from the Login Page");

			} else {
				return new AbstractMap.SimpleEntry<>(false, "Login page still present after timeout.");
			}

		} catch(Exception e){
			return new AbstractMap.SimpleEntry<>(false, e.getMessage());
		}

		return new AbstractMap.SimpleEntry<>(true, "");
	}

	public void checkRedirectToEmailTOTPPage() {
		try {
			// Check if still remains at login page after login submission
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
			boolean urlChanged = wait.until((ExpectedCondition<Boolean>) wd ->
					!wd.getCurrentUrl().contains("login")
			);

			String currUrl = getCurrentURL();
			LogUtils.info("Current URL: " + currUrl);

			if (urlChanged) {
				// Check if navigates to trader url and email TOTP page after click login
				String verifyAuthenticationPageName = "verifyAuthentication";

				if (!checkNavigateUrl(trade_url) || !currUrl.contains("/" + verifyAuthenticationPageName)) {
					Assert.fail(String.format("Did not navigate to Email Login TOTP Authentication page. Current Url: %s, Expected Page: %s", currUrl, verifyAuthenticationPageName));
				}

				LogUtils.info("Successfully navigated away from the Login Page");

			} else {
				Assert.fail("Login page still present after timeout.");
			}

		} catch (Exception e) {
			Assert.fail("Email Login TOTP Authentication failed", e);
		}
	}

	public void submitEmailTOTP() {
		WebElement btn = this.getEmailTOTPSubmitBtnEle();
		triggerElementClickEvent_withoutMoveElement(btn);
		LogUtils.info("Click Email TOTP Submit button");
	}

	public void checkEmailAuthSuccess() {

		// Check for totp validation message
		String invalidMsg = checkExistsLoginAuthInvalidMsg();

		if (invalidMsg != null && !invalidMsg.isEmpty()) {
			Assert.fail("Email Login TOTP Authentication failed. Error Msg: " + invalidMsg);
		}

		// Check for alert message
		String alertMsg = checkExistsAlertMsg();

		if (alertMsg != null) {
			Assert.fail("Email Login TOTP Authentication failed. Error Msg: " + alertMsg);
		}

		try {
			// Check if still remains at authentication page after submit otp success
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
			boolean urlChanged = wait.until((ExpectedCondition<Boolean>) wd ->
					!wd.getCurrentUrl().contains("verifyAuthentication")
			);

			String currUrl = getCurrentURL();
			LogUtils.info("Current URL: " + currUrl);

			if (urlChanged) {
				// Check if navigates to trader url and default page after login success
				if (!checkNavigateUrl(trade_url) || (!getCurrentURL().contains("/home") && !getCurrentURL().contains("/securityManagement"))) {
					Assert.fail("Successfully navigated away from the Email Login TOTP Authentication page but destination URL (" + getCurrentURL() + ") is incorrect");
				}

				LogUtils.info("Successfully navigated away from the Email Login TOTP Authentication page.");

			} else {
				Assert.fail("Email Login TOTP Authentication page still present after timeout.");
			}

		} catch(Exception e){
			Assert.fail("Email Login TOTP Authentication failed", e);
		}
	}

	public String checkExistsLoginAuthInvalidMsg() {
		return checkExistsAlertMsg(this::getLoginAuthMsgBy, "Email Login TOTP Verification");
	}

	public String checkExistsAlertMsg() {
		return checkExistsAlertMsg(this::getAlertMsgBy, "Email Login TOTP Authentication");
	}

	public void changeLanguage(PORTAL_LANGUAGE language) {
		waitLoading();
		if(GlobalProperties.isWeb) {
			setWebLanguage(language);
		}
	}

	public void setWebLanguage(PORTAL_LANGUAGE language) {}

	public void waitLoadingEmailTOTPContent() {
		getEmailTOTPContentEle();
	}

}
