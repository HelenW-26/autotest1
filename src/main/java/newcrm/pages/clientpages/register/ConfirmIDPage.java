package newcrm.pages.clientpages.register;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import newcrm.pages.Page;
import org.testng.Assert;
import utils.LogUtils;

import java.util.List;
import java.time.Duration;

public class ConfirmIDPage extends Page {

	private int fileCannotReadRetryCnt = 0;

	public ConfirmIDPage(WebDriver driver) {
		super(driver);
	}

	protected WebElement getIdentityVerificationContentEle() {
		return assertElementExists(By.xpath("//div[contains(@class,'account_opening_drawer') and not(contains(@style,'display'))]"), "Identity Verification Content");
	}

	protected WebElement getPOAVerificationContentEle() {
		return assertVisibleElementExists(By.xpath("//div[contains(@class,'kyc_drawer') and not(contains(@style,'display'))]//div[@aria-label='Residency Address Verification']"),"Residency Address Verification Content");
	}

	protected By getAlertMsgBy() {
		return By.cssSelector("div.el-message.ht-message--error > p");
	}

	public void uploadID(String id) {
		try {
			WebElement idinput = driver.findElement(By.xpath("(//input[@type='file'])[1]"));
			idinput.sendKeys(id);
			this.waitLoading();
		}
		catch (Exception e)
		{
			LogUtils.info("no need to upload ID");
		}

		LogUtils.info("ConfirmIDPage: upload id: " + id);

	}

	public void uploadIDBack(String id) {
		try {
			WebElement idinput = driver.findElement(By.xpath("(//input[@type='file'])[2]"));
			idinput.sendKeys(id);
			this.waitLoading();
		}
		catch (Exception e)
		{
			LogUtils.info("no need to upload ID");
		}

		LogUtils.info("ConfirmIDPage: upload id: " + id);

	}

	public void uploadPOA(String id) {
		try {
			WebElement idinput = driver.findElement(By.xpath("(//input[@type='file'])[2]"));
			idinput.sendKeys(id);
			waitLoading();
			LogUtils.info("ConfirmIDPage: upload POA: " + id);
		}
		catch (Exception e)
		{
			LogUtils.info("no need to upload POA");
		}
	}
	
	public void filleName(String firstName,String middleName, String lastName) {
		waitLoading();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		this.setInputValue(this.findVisibleElemntByXpath("//input[@data-testid='firstName']"), firstName);
		WebElement fistN = driver.findElement(By.xpath("//input[@data-testid='firstName']"));
		js.executeScript("arguments[0].value='"+firstName+ "';", fistN);

		this.setInputValue(this.findVisibleElemntByXpath("//input[@data-testid='middleName']"), middleName);
		WebElement middleN = driver.findElement(By.xpath("//input[@data-testid='middleName']"));
		js.executeScript("arguments[0].value='"+middleName+ "';", middleN);

		this.setInputValue(this.findVisibleElemntByXpath("//input[@data-testid='lastName']"), lastName);
		WebElement lastN = driver.findElement(By.xpath("//input[@data-testid='lastName']"));
		js.executeScript("arguments[0].value='"+lastName+ "';", lastN);

		LogUtils.info("ConfirmIDPage: set the full name to: " + firstName + " " + middleName + " " + lastName);
	}
	
	public String getCountry() {
		return this.findVisibleElemntByXpath("//div[@data-testid='countryId']/div/input").getAttribute("value");
	}

	protected WebElement getIDTypeInput() {
		return this.findClickableElemntBy(By.xpath("//input[@id='idType']"));
	}
	public String getIDType() {
		return this.findVisibleElemntByXpath("//div[@data-testid='idType']/div/input").getAttribute("value");
	}
	
	public String getIDNumber() {
		return this.findVisibleElemntByXpath("//input[@data-testid='idNumber']").getAttribute("value");
	}

	public void setIDNumber(String idNumber) {
		driver.findElement(By.xpath("//input[@data-testid='idNumber']")).sendKeys(idNumber);
		LogUtils.info("PersonalDetailsPage: set Identification Number to: " + idNumber);
	}

	public void next() {
		WebElement e = this.findClickableElementByXpath("//button[@data-testid='next']");
		triggerElementClickEvent(e);
		LogUtils.info("Click Submit button");
	}

	public String setIdentificationType() {
		WebElement e = this.getIDTypeInput();
		e.click();
		String result = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
		if(result!=null) {
			LogUtils.info("PersonalDetailsPage: set Identification Type to: " + result);
		}else
		{
			LogUtils.info("ERROR: PersonalDetailsPage: set Identification Type failed!" );
			e.click();
			return null;
		}
		return result;
	}

	public void proceedToIDVerfication() {}

	public void nextSec() {}
	
	public String getPageTitle() {
		return this.findVisibleElemntByXpath("//*[self::h2 or self::h3]").getText();
	}

	public void completeIDBtn() {
		WebElement e = this.findClickableElementByXpath("(//button[@data-testid='button'])[1]");
		this.moveElementToVisible(e);
		e.click();
		this.waitLoading();
	}

	public String setCity(String city)
	{
		driver.findElement(By.xpath("//input[@data-testid='suburb']")).sendKeys(city);
		return "test";
	}

	public String setPostcode(String postcode)
	{
		driver.findElement(By.xpath("//input[@data-testid='postcode']")).sendKeys(postcode);
		return "test";
	}

	public String setStreetNum(String num)
	{
		driver.findElement(By.xpath("//input[@data-testid='streetNumber']")).sendKeys(num);
		return String.valueOf(num);
	}

	public void comfirmBtn(){
		WebElement confirmBtn= driver.findElement(By.xpath("//button[@data-testid='confirm']"));
		confirmBtn.click();
		waitLoading();
	}
	public void uploadBtn() {
		driver.findElement(By.xpath("//button[@class='el-button v_upload el-button--primary']")).click();
		LogUtils.info("Click upload button");
	}

	public void setAddress(String address) {
		driver.findElement(By.xpath("//input[@data-testid='address']")).sendKeys(address);
		LogUtils.info("PersonalDetailsPage: set adress to: " + address);
	}

	public void waitLoadingIdentityVerificationContent() {
		waitLoader();
		getIdentityVerificationContentEle();
		waitButtonLoader();
		waitLoader();
	}

	public void waitLoadingPOAVerificationContent() {
		waitLoader();
		getPOAVerificationContentEle();
		waitButtonLoader();
		waitLoader();
	}

	public void checkExistsAlertMsg(String lblDesc) {
		// Check for alert error message
		String alertMsg = checkExistsAlertMsg(this::getAlertMsgBy, lblDesc);

		if (alertMsg != null) {
			Assert.fail("An error occurred during " + lblDesc + ". Error Msg: " + alertMsg);
		}
	}

	// region [ SumSub ID Verification ]

	protected WebElement getSumSubIDTypeCountry(String country) {
		return assertElementExists(By.xpath("//div[@id='listbox']//button//span[normalize-space() = '" + country + "']"), "Country: " + country);
	}

	protected WebElement getSumSubLanguageIconEle() {
		return assertVisibleElementExists(By.cssSelector("span.sdk-select button[aria-label='Change language']"), "Sumsub Language Icon");
	}

	protected WebElement getSumSubLanguageListEle(String id) {
		return assertElementExists(By.cssSelector("#" + id), "Sumsub Language List");
	}

	protected WebElement getSumSubLanguageListItemEle(String language) {
		return assertElementExists(By.xpath("//button[@aria-selected='false']//span[span[@lang='en']]/following-sibling::span"), "SumSub " + language + " Language");
	}

	public WebElement waitLoadingSumSubIframe() {
		return assertVisibleElementExists(By.xpath("//div[@id='sumsub-websdk-container']/iframe"), "Sumsub Content");
	}

	public void waitLoadingSumSubContainer() {
		assertVisibleElementExists(By.xpath("//div[@id='sdk-base-container']"),"Sumsub Content");
	}

	public void waitLoadingSumSubContent() {
		WebElement sumSub = waitLoadingSumSubIframe();
		// Switch to sumsub iframe
		driver.switchTo().frame(sumSub);
		waitLoadingSumSubContainer();
	}

	public String getSumSubContentTitle() {
		// Get sumsub content title
		WebElement sumSubPageTitle = assertVisibleElementExists(By.xpath("//div[@id='sdk-base-container']/main//h1"), "Sumsub Content Title");
		String h1Content = sumSubPageTitle.getText();
		LogUtils.info("Sumsub Content Title: " + h1Content);

		return h1Content;
	}

	public boolean checkHasSumSubAccess() {
		// Get sumsub content title
		String h1Content = getSumSubContentTitle();

		// Check for Access Denied Content
		if (h1Content.equalsIgnoreCase("Access denied")) {
			LogUtils.info("Sumsub (Access Denied) page found.");

			WebElement continueBtn = assertElementExists(By.xpath("//div[@id='sdk-base-container']//button[1]"), "Sumsub Try again button");
			continueBtn.click();
			waitLoadingSumSubContainer();
			LogUtils.info("Click Sumsub Try again button");
			return true;
		}

		//  Check for Unknown url Content
		if (h1Content.equalsIgnoreCase("Unknown url")) {
			Assert.fail("Unable to proceed Sumsub Verification. Error Msg: Sumsub (Unknown url)");
			return false;
		}

		return true;
	}

	public void sumSubChangeLanguage() {
		LogUtils.info("Sumsub Change Language");
		WebElement languageIconEle = getSumSubLanguageIconEle();

		if (!"en".equalsIgnoreCase(languageIconEle.getText().trim())) {
			languageIconEle.click();
			LogUtils.info("Click Sumsub Language Icon");

			String lstId = languageIconEle.getAttribute("aria-controls");

			// Get Language List by id
			WebElement languageList = getSumSubLanguageListEle(lstId);
			// Set language
			WebElement languageItem = getSumSubLanguageListItemEle("English");
			languageItem.click();
			waitSumSubLanguageIconLoader();
			LogUtils.info("Change Sumsub language to: English");
		}
	}

	public void startSumSubVerification() {
		// Check sumsub initial step
		String h1Content = getSumSubContentTitle();

		if (StringUtils.containsIgnoreCase(h1Content, "Data and Privacy")) {
			sumSubDataPrivacyStep();
		} else if (StringUtils.containsIgnoreCase(h1Content, "Confirm your country of residence")) {
			sumSubCountryOfResidenceStep();
		} else if (StringUtils.containsIgnoreCase(h1Content, "Let's get you verified")) {
			WebElement sumsubStepContent = assertElementExists(By.cssSelector("div#sdk-base-container main.SdkContent"), "Sumsub Step Content");
			String dataStep = sumsubStepContent.getAttribute("data-step");
			LogUtils.info("Data Step: " + dataStep);

			if ("welcome".equalsIgnoreCase(dataStep)) {
				sumSubWelcomeStep();
				waitLoadingSumSubContainer();

				h1Content = getSumSubContentTitle();
				if (StringUtils.containsIgnoreCase(h1Content, "Data and Privacy")) {
					sumSubDataPrivacyStep();
				} else if (StringUtils.containsIgnoreCase(h1Content, "Confirm your country of residence")) {
					sumSubCountryOfResidenceStep();
				}
			} else {
				sumSubWelcomeAgreementStep();
			}
		} else {
			sumSubWelcomeAgreementStep();
		}
	}

	public void sumSubWelcomeAgreementStep() {
		WebElement agreementStep = assertVisibleElementExists(By.xpath("//div/main[@data-step='welcome_with_agreement']"), "Sumsub Start Verification Content");
		if (agreementStep != null) {
			LogUtils.info("Sumsub - Start Verification Step");
			WebElement agreementBtn = assertElementExists(By.xpath("//button[@aria-label='Start verification']"), "Sumsub Start Verification button");
			agreementBtn.click();
			LogUtils.info("Click Sumsub Start Verification button");

			// Agree and continue
			WebElement continueBtn = assertVisibleElementExists(By.xpath("//button[@aria-label='Agree and continue']"), "Sumsub Agreement Content");
			if (continueBtn != null) {
				LogUtils.info("Sumsub - Start Verification Step - Agreement");

				// USA resident options
				WebElement usaBtn = checkElementExists(By.xpath("//input[@value='no']/ancestor::label[contains(@class, 'RadioCheckContainer')]"), "USA Resident option");
				if (usaBtn != null) {
					clickElement(usaBtn);
					LogUtils.info("Select USA Resident option: " + usaBtn.getText());
				}

				clickElement(continueBtn);
				LogUtils.info("Click Sumsub Start Verification Agreement button");
			}
		}
	}

	public void sumSubDataPrivacyStep() {
		// Data and Privacy
		WebElement agreementStep = assertVisibleElementExists(By.xpath("//div/main[@data-step='agreement']"), "Sumsub Data and Privacy Content");
		if (agreementStep != null) {
			LogUtils.info("Sumsub - Data and Privacy Step");

			// Agreement Checkbox
			WebElement agreementChkBox = assertElementExists(By.xpath("//span[@class='sdk-checkbox default']"), "Sumsub Data and Privacy agreement checkbox");
			agreementChkBox.click();
			LogUtils.info("Tick Sumsub Data and Privacy agreement checkbox");

			// Continue button
			WebElement continueBtn = assertElementExists(By.xpath("//div[@id='sdk-base-container']//button[1]"), "Sumsub Continue button");
			continueBtn.click();
			LogUtils.info("Click Sumsub Continue button");
		}
	}

	public void sumSubCountryOfResidenceStep() {
		// Confirm your country of residence
		WebElement agreementStep = assertVisibleElementExists(By.xpath("//div/main[@data-step='agreement']"), "Sumsub Confirm your country of residence Content");
		if (agreementStep != null) {
			LogUtils.info("Sumsub - Confirm your country of residence Step");

			// Country of residence options
			WebElement residenceEle = checkElementExists(By.xpath("//input[@value='0']/ancestor::label[contains(@class, 'RadioCheckContainer')]"), "Country of Residence option");
			if (residenceEle != null) {
				clickElement(residenceEle);
				LogUtils.info("Select Country of Residence option: " + residenceEle.getText());
			}

			// Continue button
			WebElement continueBtn = assertElementExists(By.xpath("//div[@id='sdk-base-container']//button[1]"), "Sumsub Continue button");
			continueBtn.click();
			LogUtils.info("Click Sumsub Continue button");
		}
	}

	public void sumSubWelcomeStep() {
		// Confirm your country of residence
		WebElement agreementStep = assertVisibleElementExists(By.xpath("//div/main[@data-step='welcome']"), "Sumsub Let's get you verified Content");
		if (agreementStep != null) {
			LogUtils.info("Sumsub - Let's get you verified Step");

			// Continue button
			WebElement continueBtn = assertElementExists(By.xpath("//div[@id='sdk-base-container']//button[1]"), "Sumsub Continue button");
			continueBtn.click();
			LogUtils.info("Click Sumsub Continue button");
			waitSumSubBtnLoader();
		}
	}

	public void fillSumSubPersonalDetails(String country) {
		WebElement personalInfoStep = assertVisibleElementExists(By.xpath("//div/main[@data-step='applicant-data']"), "Sumsub Personal information Content");
		if (personalInfoStep != null) {
			LogUtils.info("Sumsub - Personal Information Step");

			// Set Country if default no country selected
			WebElement defaultCountry = assertElementExists(By.xpath("//span[@class='sdk-select']/button//div[@class='SdkSelectLabelText']"), "Sumsub Default Selected Country");
			if (defaultCountry.getText().equalsIgnoreCase("Country")) {
				LogUtils.info("Sumsub no default country selected. Select country now...");
				WebElement countryDdl = driver.findElement(By.xpath("//span[@class='sdk-select']/button"));
				countryDdl.click();
				LogUtils.info("Click Sumsub Country List");
				WebElement selectedCountry = driver.findElement(By.xpath("//div[@id='listbox']//button//span[normalize-space() = '" + country + "']"));
				this.moveElementToVisible(selectedCountry);
				selectedCountry.click();
				LogUtils.info("Select Sumsub country: " + country);
			}

			WebElement personalInfoBtn = assertElementExists(By.xpath("//button[@aria-label='Continue']"), "Sumsub Personal Info Continue button");
			personalInfoBtn.click();
			LogUtils.info("Click Sumsub Personal Info Continue button");
		}
	}

	public void setSumSubIdentificationType(String country) {
		WebElement docTypeStep = assertVisibleElementExists(By.xpath("//div/main[@data-step='document' and @data-substep='docdef']"), "Sumsub Select Document Type Content");
		if (docTypeStep != null) {
			LogUtils.info("Sumsub - Select Document Type Step");

			// Set Country if default no country selected
			WebElement defaultCountry = assertElementExists(By.xpath("//span[@class='sdk-select']/button//div[@class='SdkSelectLabelText']"), "Sumsub Default Selected Country");
			if (defaultCountry.getText().equalsIgnoreCase("Select country")) {
				LogUtils.info("Sumsub no default country selected. Select country now...");
				WebElement countryDdl = assertElementExists(By.xpath("//span[@class='sdk-select']/button"), "Sumsub Country List");
				countryDdl.click();
				LogUtils.info("Click Sumsub Country List");
				WebElement selectedCountry = this.getSumSubIDTypeCountry(country);
				this.moveElementToVisible(selectedCountry);
				selectedCountry.click();
				LogUtils.info("Select Sumsub country: " + country);
			}

			// Select ID Card Type option
			WebElement idTypeBtn = assertElementExists(By.xpath("//input[@value='ID_CARD']/following-sibling::span[@class='sdk-radio default']"), "Sumsub ID Card Document Type");
			idTypeBtn.click();
			LogUtils.info("Click Sumsub ID Card option");

			// Continue button
			WebElement continueBtn = checkElementExists(By.xpath("//button[@aria-label='Continue']"));
			if (continueBtn != null) {
				continueBtn.click();
				LogUtils.info("Click Sumsub Continue button");
			}
		}
	}

	public void uploadSumSubDocType(String fileFront, String fileBack) {
		WebElement uploadDocStep = assertVisibleElementExists(By.xpath("//div/main[@data-step='document' and @data-substep='both-upload']"), "Sumsub Upload Document Content");

		if (uploadDocStep != null) {
			LogUtils.info("Sumsub - Upload Document Step");
			fileCannotReadRetryCnt = 0;
			fileUploadProcess(fileFront, fileBack, true);

			// Submit file upload
			submitSumSubDoc();
		}
	}

	public void uploadSumSubPOA(String file) {
		WebElement uploadDocStep = assertVisibleElementExists(By.xpath("//div/main[@data-step='poa' and @data-substep='select-doc-type']"), "Sumsub Upload POA Document Content");

		if (uploadDocStep != null) {
			LogUtils.info("Sumsub - Upload POA Document Step");
			fileCannotReadRetryCnt = 0;
			fileUploadProcess(file, "", false);

			// Submit file upload
			submitSumSubDoc();
		}
	}

	public boolean fileUploadProcess(String fileFront, String fileBack, boolean bIsPOI) {

		By fileInputLocator = By.xpath("//input[@type='file']");
		By fileCannotReadLocator = By.xpath("//div[@data-type='error' and contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'), 'cannot be read')]");
		By fileNotClearLocator = By.xpath("//div/div[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'image is not clear')]");

		// Get no. of file upload available
		List<WebElement> fileInputList = assertElementsExists(fileInputLocator, "File Upload");
		int maxRetries = 5;

		for (int i = 0; i < fileInputList.size(); i++) {
			String sDesc = bIsPOI ? "POI " + (i == 0 ? "Front" : "Back") + " Side" : "POA";

			for (int attempt = 1; attempt <= maxRetries; attempt++) {
				String errMsg = "";
				boolean bIsUploadFail = false;

				// Get first available file upload
				WebElement fileInput = assertElementsExists(fileInputLocator, sDesc + " File Upload").get(0);
				// Upload image
				fileInput.sendKeys((i == 0 ? fileFront : fileBack));
				LogUtils.info("Uploading Sumsub " + sDesc);
				waitSumSubFileUploadLoader();

				// Check for file upload error message
				// Check for image not clear popup message
				WebElement imgNotClearEle = checkElementExists(fileNotClearLocator);

				if (imgNotClearEle != null) {
					bIsUploadFail = true;
					errMsg = imgNotClearEle.getText();
					LogUtils.info(String.format("File uploaded not clear detected on attempt %s. Error Msg: %s", attempt, errMsg));

					// Upload again button
					WebElement reuploadBtn = assertElementExists(By.xpath("//button[@aria-label='Upload again']"), "Upload again button");
					reuploadBtn.click();
					LogUtils.info("Click Upload again button");
				}

				// Check for file cannot be read error message
				WebElement fileUploadErr = checkElementExists(fileCannotReadLocator);

				if (fileUploadErr != null) {
					bIsUploadFail = true;
					errMsg = fileUploadErr.getText();
					LogUtils.info(String.format("File upload error on attempt %s. Error Msg: %s", attempt, errMsg));

					// When new file uploaded cannot be read, previous file uploaded will be cleared. Need to re-upload first file again.
					if (fileCannotReadRetryCnt < maxRetries) {
						fileCannotReadRetryCnt += 1;
						LogUtils.info("All files cleared when uploaded file cannot be read. Retry file upload process...");
						if (fileUploadProcess(fileFront, fileBack, bIsPOI)) {
							return true;
						}
					}
				}

				boolean bIsAllFileUploaded = checkElementExists(fileInputLocator, "File Upload") == null;

				// Check if reach max upload retry attempt
				if (bIsUploadFail && attempt == maxRetries) {
					LogUtils.info("Reached maximum retries (" + maxRetries + ") without success");
					Assert.fail("Reached maximum retries while uploading Sumsub " + sDesc + ". Error Msg: " + errMsg);
				}

				// Check if all files uploaded successfully
				if (!bIsUploadFail) {
					if (!bIsAllFileUploaded) {
						break;
					}

					LogUtils.info(String.format("All files (%s) upload success", fileInputList.size()));
					return true;
				}
			}
		}

		return false;
	}

	public void submitSumSubDoc() {
		// Continue button
		WebElement continueBtn = checkElementExists(By.xpath("//button[@aria-label='Continue']"));
		if (continueBtn != null) {
			continueBtn.click();
			LogUtils.info("Submit Sumsub Document");
			waitLoading();
			waitButtonLoader();
			waitLoader();
		}
	}

	// endregion

}
