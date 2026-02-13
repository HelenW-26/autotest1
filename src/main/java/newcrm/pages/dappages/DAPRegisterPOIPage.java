package newcrm.pages.dappages;

import newcrm.pages.Page;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utils.LogUtils;

import java.time.Duration;
import java.util.List;

public class DAPRegisterPOIPage extends Page {

    public DAPRegisterPOIPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getAlertMsgEle() {
        return this.checkElementExists(By.cssSelector("div.el-message.ht-message--error > p"));
    }

    protected By getAlertMsgBy() {
        return By.cssSelector("div.el-message.ht-message--error > p");
    }

    protected WebElement getIDVerifyButton() {
        return assertElementExists(By.xpath("//button[@data-testid='button' and contains(@class, 'register_guide_btn')]"), "ID Verify home button");
    }

    protected WebElement getNextButton() {
        return assertClickableElementExists(By.xpath("//button[@data-testid='next']"), "Next button");
    }

    protected WebElement getContinueToPOAButton() {
        return assertClickableElementExists(By.xpath("//p[@class='result-btns-extra-btn']"), "Continue to POA button");
    }

    protected WebElement getIDNumInput() {
        return this.findClickableElemntBy(By.xpath("//input[@data-testid='idNumber']"));
    }

    protected WebElement getIDTypeEle() {
        return assertElementExists(By.xpath("(//div[@data-testid='idType']/label)[1]"), "ID Type");
    }

    protected WebElement getPOICloseButton() {
        return assertElementExists(By.xpath("//div[contains(@class,'kyc-drawer') and not(contains(@style,'display'))]//button[@class='ht-drawer__close-btn']"), "Close button");
    }

    protected WebElement getPOIConfirmButton() {
        return assertElementExists(By.xpath("//div[@class='result-btns']/button"), "Confirm button");
    }

    protected WebElement getPOIExitButton() {
        return assertElementExists(By.xpath("//button[./span[normalize-space() = 'Exit Anyway']]"), "Exit button");
    }

    public String setIDType() {
        WebElement e = this.getIDTypeEle();
        triggerClickEvent(e);

        WebElement labelDiv = e.findElement(By.xpath(".//div[@class='proof-type__item-content']"));
        String idType = labelDiv.getText();
        LogUtils.info("POI Page: set Identification Type to: " + idType);

        return idType;
    }

    public String setIDNumber(String idnum) {
        this.setInputValue(this.getIDNumInput(), idnum);
        LogUtils.info("POI Page: set Identification Number to: " + idnum);

        return idnum;
    }

    public void clickIDVerifyBtn() {
        triggerClickEvent(this.getIDVerifyButton());
        LogUtils.info("Click ID Verify home button");
    }

    public void waitLoadingIdentityVerificationContent() {
        waitLoader();
        assertVisibleElementExists(By.xpath("//div[contains(@class,'is-drawer') and not(contains(@style,'display'))]//div[@aria-label='Identity Verification' or @aria-label='Verify Your Identity']"),"Identity Verification Content");
        waitButtonLoader();
        waitLoader();
    }

    public void waitLoadingPOAVerificationContent() {
        waitLoader();
        assertVisibleElementExists(By.xpath("//div[contains(@class,'kyc_drawer') and not(contains(@style,'display'))]//div[@aria-label='Residency Address Verification']"),"Residency Address Verification Content");
        waitButtonLoader();
        waitLoader();
    }

    public void clickPOIExitBtn() {
        triggerClickEvent(this.getPOIExitButton());
        LogUtils.info("Click Exit button");
    }

    public void verifyPOIConfirmBtn() {
        Assert.assertTrue(getPOIConfirmButton().isDisplayed());
        LogUtils.info("Click Confirm button");
    }

    public void clickPOICloseBtn() {
        triggerClickEvent(this.getPOICloseButton());
        LogUtils.info("Close dialog");
    }

    public void uploadIDFront(String id) {

        for (int attempt = 0; attempt < 2; attempt++) {
            try {
                WebElement idinput = driver.findElement(By.xpath("(//input[@type='file'])[1]"));
                idinput.sendKeys(id);
                this.waitLoading();

                // Wait until an image appears
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("(//input[@type='file'])[1]/ancestor::div[1]/preceding-sibling::li[@class='ht-upload-list__item is-success']")));

                LogUtils.info("Upload succeeded.");
                break;

            } catch (TimeoutException e) {
                if (attempt > 0) {
                    LogUtils.info("Upload ID Front Side failed.");
                    Assert.fail("File upload failed.");
                    break;
                };
                LogUtils.info("Upload failed. Retrying...");
                // Small wait before retry
                try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            }
        }

        LogUtils.info("POI Page: upload id: " + id);
    }

    public void uploadIDBack(String id) {
        for (int attempt = 0; attempt < 2; attempt++) {
            try {
                WebElement idinput = driver.findElement(By.xpath("(//input[@type='file'])[2]"));
                idinput.sendKeys(id);
                this.waitLoading();

                // Wait until an image appears
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("(//input[@type='file'])[2]/ancestor::div[1]/preceding-sibling::li[@class='ht-upload-list__item is-success']")));

                LogUtils.info("Upload succeeded.");
                break;

            } catch (TimeoutException e) {
                if (attempt > 0) {
                    LogUtils.info("Upload ID Back Side failed.");
                    Assert.fail("File upload failed.");
                    break;
                };
                LogUtils.info("Upload failed. Retrying...");
                // Small wait before retry
                try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            }
        }

        LogUtils.info("POI Page: upload id: " + id);

    }

    public void continueToPOA(){
        triggerClickEvent(getContinueToPOAButton());
    }

    public void nextStep() {
        triggerClickEvent(this.getNextButton());
        LogUtils.info("Click Next button");
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

    protected WebElement getSumSubLanguageIconEle() {
        return assertVisibleElementExists(By.cssSelector("span.sdk-select button[aria-label='Change language']"), "Sumsub Language Icon");
    }

    protected WebElement getSumSubLanguageListEle(String id) {
        return assertElementExists(By.cssSelector("#" + id), "Sumsub Language List");
    }

    protected WebElement getSumSubLanguageListItemEle(String language) {
        return assertElementExists(By.cssSelector("button[aria-selected='false'] span[lang='en']"), "SumSub " + language + " Language");
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
        LogUtils.info("Subsub Content Title: " + h1Content);

        return h1Content;
    }

    public boolean checkHasSumSubAccess() {
        // Get sumsub content title
        String h1Content = getSumSubContentTitle();

        // Check for Access Denied Content
        if (h1Content.equalsIgnoreCase("Access denied")) {
            LogUtils.info("Unable to proceed Sumsub (Access Denied)");
            return false;
        }

        //  Check for Unknown url Content
        if (h1Content.equalsIgnoreCase("Unknown url")) {
            LogUtils.info("Unable to proceed Sumsub (Unknown url)");
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

            if ("welcome".equalsIgnoreCase(dataStep)) {
                sumSubWelcomeStep();
                waitLoadingSumSubContainer();

                h1Content = getSumSubContentTitle();
                if (StringUtils.containsIgnoreCase(h1Content, "Data and Privacy")) {
                    sumSubDataPrivacyStep();
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
            WebElement agreementChkBox = assertElementExists(By.xpath("//span[@class='sdk-checkbox default']"), "Sumsub Continue button");
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
        }
    }

    public void fillSumSubPersonalDetails() {
        WebElement personalInfoStep = assertVisibleElementExists(By.xpath("//div/main[@data-step='applicant-data']"), "Sumsub Personal information Content");
        if (personalInfoStep != null) {
            LogUtils.info("Sumsub - Personal Information Step");
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
                WebElement countryDdl = driver.findElement(By.xpath("//span[@class='sdk-select']/button"));
                countryDdl.click();
                LogUtils.info("Click Sumsub Country List");
                WebElement selectedCountry = driver.findElement(By.xpath("//div[@id='listbox']/button//span[normalize-space() = '" + country + "']"));
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

            By fileInputLocator = By.xpath("//input[@type='file']");
            By fileUploadErrLocator = By.xpath("//div[@data-type='error' and contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'), 'cannot be read')]");
            By imgNotClearLocator = By.xpath("//div/div[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'image is not clear')]");

            List<WebElement> fileInputList = assertElementsExists(fileInputLocator, "File Upload");

            String sIDDesc = "";
            int maxRetries = 5;

            for (int i = 0; i < fileInputList.size(); i++) {
                sIDDesc = (i == 0 ? "Front" : "Back");

                for (int attempt = 1; attempt <= maxRetries; attempt++) {
                    // Upload ID Front Side
                    WebElement fileInputFront = assertElementExists(fileInputLocator, "File Upload (ID " + sIDDesc + " Side)");
                    fileInputFront.sendKeys((i == 0 ? fileFront : fileBack));
                    waitLoading();
                    LogUtils.info("Uploading Sumsub ID " + sIDDesc + " Side");

                    // Check for image not clear popup
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
                    WebElement imgNotClearEle;
                    try {
                        imgNotClearEle = wait.until(ExpectedConditions.presenceOfElementLocated(imgNotClearLocator));
                    } catch (TimeoutException e) {
                        imgNotClearEle = null;
                    }

                    // Check for upload file error
                    WebElement fileUploadErr = checkElementExists(fileUploadErrLocator);

                    if (imgNotClearEle == null && fileUploadErr == null) {
                        LogUtils.info("Upload Sumsub ID " + sIDDesc + " Side successful on attempt " + attempt);
                        break;
                    } else {

                        String errMsg = "";
                        if (imgNotClearEle != null) {
                            LogUtils.info("Image uploaded not clear detected on attempt " + attempt);
                            errMsg = imgNotClearEle.getText();
                        }
                        if (fileUploadErr != null) {
                            LogUtils.info("File upload error on attempt " + attempt);
                            errMsg = fileUploadErr.getText();
                        }

                        if (attempt == maxRetries) {
                            LogUtils.info("Reached maximum retries (" + maxRetries + ") without success");
                            Assert.fail("Reached maximum retries while uploading Sumsub ID " + sIDDesc + " Side. Error Msg: " + errMsg);
                        } else {
                            if (imgNotClearEle != null) {
                                WebElement reuploadBtn = assertElementExists(By.xpath("//button[@aria-label='Upload again']"), "Upload again button");
                                reuploadBtn.click();
                                LogUtils.info("Click Upload again button");
                            }
                        }
                    }
                }
            }

            // Submit Sumsub file upload
            submitSumSubDoc();
        }
    }

    public void uploadSumSubPOA(String file) {
        WebElement uploadDocStep = assertVisibleElementExists(By.xpath("//div/main[@data-step='poa' and @data-substep='select-doc-type']"), "Sumsub Upload POA Document Content");

        if (uploadDocStep != null) {
            LogUtils.info("Sumsub - Upload POA Document Step");

            By fileInputLocator = By.xpath("//input[@type='file']");
            By fileUploadErrLocator = By.xpath("//div[@data-type='error' and contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'), 'cannot be read')]");
            By imgNotClearLocator = By.xpath("//div/div[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'image is not clear')]");

            List<WebElement> fileInputList = assertElementsExists(fileInputLocator, "POA File Upload");

            int maxRetries = 5;

            for (int i = 0; i < fileInputList.size(); i++) {

                for (int attempt = 1; attempt <= maxRetries; attempt++) {
                    // Upload ID Front Side
                    WebElement fileInputFront = assertElementExists(fileInputLocator, "File Upload POA");
                    fileInputFront.sendKeys(file);
                    waitLoading();
                    LogUtils.info("Uploading Sumsub POA");

                    // Check for image not clear popup
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
                    WebElement imgNotClearEle;
                    try {
                        imgNotClearEle = wait.until(ExpectedConditions.presenceOfElementLocated(imgNotClearLocator));
                    } catch (TimeoutException e) {
                        imgNotClearEle = null;
                    }

                    // Check for upload file error
                    WebElement fileUploadErr = checkElementExists(fileUploadErrLocator);

                    if (imgNotClearEle == null && fileUploadErr == null) {
                        LogUtils.info("Upload Sumsub POA successful on attempt " + attempt);
                        break;
                    } else {

                        String errMsg = "";
                        if (imgNotClearEle != null) {
                            LogUtils.info("Image uploaded not clear detected on attempt " + attempt);
                            errMsg = imgNotClearEle.getText();
                        }
                        if (fileUploadErr != null) {
                            LogUtils.info("File upload error on attempt " + attempt);
                            errMsg = fileUploadErr.getText();
                        }

                        if (attempt == maxRetries) {
                            LogUtils.info("Reached maximum retries (" + maxRetries + ") without success");
                            Assert.fail("Reached maximum retries while uploading Sumsub POA. Error Msg: " + errMsg);
                        } else {
                            if (imgNotClearEle != null) {
                                WebElement reuploadBtn = assertElementExists(By.xpath("//button[@aria-label='Upload again']"), "Upload again button");
                                reuploadBtn.click();
                                LogUtils.info("Click Upload again button");
                            }
                        }
                    }
                }
            }

            // Submit Sumsub file upload
            submitSumSubDoc();
        }
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
