package newcrm.pages.dappages;

import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import tools.ScreenshotHelper;
import utils.LogUtils;

import java.util.List;

public class DAPRegisterPersonalDetailsPage extends Page {

    public DAPRegisterPersonalDetailsPage(WebDriver driver) {
        super(driver);
    }

    protected By getAlertMsgBy() {
        return By.cssSelector("div.el-message.ht-message--error > p");
    }

    protected WebElement getDayInput() {
        return this.findClickableElementByXpath("//div[@data-testid='dob'] //input");
    }

    protected WebElement getMonthInput() {
        return this.findClickableElementByXpath("//div[@data-testid='month'] //input");
    }

    protected WebElement getYearInput() {
        return this.findClickableElementByXpath("//div[@data-testid='year'] //input");
    }

    protected WebElement getGenderInput() {
        return this.findClickableElementByXpath("//div[@data-testid='gender'] //input");
    }

    protected WebElement getEmailCodeInput() {
        return driver.findElement(By.xpath("//div[@class='code-display']/input"));
    }

    protected WebElement getPhoneCodeInput() {
        return driver.findElement(By.xpath("//div[@class='code-display']/input"));
    }

    protected WebElement getFirstNameInput() {
        return driver.findElement(By.xpath("//input[@data-testid='firstName']"));
    }

    protected WebElement getLastNameInput() {
        return driver.findElement(By.xpath("//input[@data-testid='lastName']"));
    }

    protected WebElement getMobileInput() {
        return driver.findElement(By.xpath("//input[@data-testid='phoneNumber']"));
    }

    protected WebElement getSendCodeButton() {
        return driver.findElement(By.xpath("//button[@data-testid='sendOtp'] | //button[@data-testid='resendOtp']"));
    }

    protected WebElement getSendOTPButton() {
        return driver.findElement(By.xpath("//button[@data-testid='getVerificationCode']"));
    }

    protected WebElement getNextButton() {
        return driver.findElement(By.xpath("//button[@data-testid='next']"));
    }

    protected WebElement getSubmitButton() {
        return driver.findElement(By.xpath("//button[@data-testid='submit']"));
    }

    protected WebElement getPhoneDisplayEle() {
        return assertElementExists(By.xpath("//div//span[@class='opt_value_text']"), "Phone Country Code Display");
    }

    protected WebElement getPhoneOTPDialog(String phone) {
        return checkElementExists(By.xpath("//span[contains(text(), '" + phone + "') or contains(text(), '" + phone.substring(phone.length() - 3) + "')]"),"Verify Phone OTP Dialog");
    }

    protected List<WebElement> getAllOpendElements() {
        return driver.findElements(By.xpath("//div[contains(@class,'ht-popper') and not(contains(@style, 'display: none'))] //li"));
    }

    public String setGender(String gender) {
        WebElement e = this.getGenderInput();
        this.moveElementToVisible(e);
        e.click();
        String result = this.selectRandomValueFromDropDownList(getAllOpendElements());
        LogUtils.info("PersonalDetailsPage: set Gender to: " + result);

        return result;
    }

    public String setBirthDay() {
        WebElement e = this.getYearInput();
        e.click();
        waitLoading();
        e = this.getMonthInput();
        e.click();
        String year = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
        if(year!=null) {
            LogUtils.info("PersonalDetailsPage: set Year to: " + year);
        }else
        {
            LogUtils.info("ERROR: PersonalDetailsPage: set Year failed!" );
            e.click();
            return null;
        }
        String month = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
        if(month!=null) {
            LogUtils.info("PersonalDetailsPage: set Month to: " + month);
        }else
        {
            LogUtils.info("ERROR: PersonalDetailsPage: set Month failed!" );
            e.click();
            return null;
        }
        waitLoading();
        e = this.getDayInput();
        e.click();
        String day = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
        if(day!=null) {
            LogUtils.info("PersonalDetailsPage: set Day to: " + day);
        }else
        {
            LogUtils.info("ERROR: PersonalDetailsPage: set Day failed!" );
            e.click();
            return null;
        }


        return day+"-"+month+"-"+year;
    }

    public String setfirstName(String firstName) {
        this.setInputValue(this.getFirstNameInput(), firstName);
        LogUtils.info("PersonalDetailsPage: set firstName to: " + firstName);

        return firstName;
    }

    public String setLastName(String lastName) {
        this.setInputValue(this.getLastNameInput(), lastName);
        LogUtils.info("PersonalDetailsPage: set lastName to: " + lastName);

        return lastName;
    }

    public String setPhone(String phone) {
        this.setInputValue(this.getMobileInput(), phone);
        LogUtils.info("PersonalDetailsPage: set phone to: " + phone);
        return phone;
    }

    public String sendEmailCode(String code) {
        setInputValue(getEmailCodeInput(),code);
        LogUtils.info("PersonalDetailsPage: set Email OTP to: " + code);

        return code;
    }

    public String sendPhoneCode(String code) {
        setInputValue(getPhoneCodeInput(),code);
        LogUtils.info("PersonalDetailsPage: set Phone OTP to: " + code);

        return code;
    }

    public void clickEmailCodeBtn() {
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@data-testid='sendOtp'] | //button[@data-testid='resendOtp']")));
        WebElement e = this.getSendCodeButton();
        this.moveElementToVisible(e);
        e.click();
        waitLoading();
        waitButtonLoader();
        waitLoader();

        LogUtils.info("PersonalDetailsPage: click send Email OTP");
    }

    public void clickPhoneCodeBtn() {
        WebElement e = this.getSendCodeButton();
        this.moveElementToVisible(e);
        e.click();
        waitLoading();
        waitButtonLoader();
        waitLoader();

        LogUtils.info("PersonalDetailsPage: click send Phone OTP");
    }

    public void clickSendOTPBtn() {
        WebElement e = this.getSendOTPButton();
        this.moveElementToVisible(e);
        e.click();

        // Check for error
        String alertMsg = checkExistsAlertMsg();
        if (alertMsg != null) {
            Assert.fail("An error occurred during Phone OTP request. Error Msg: " + alertMsg);
        }

        this.waitLoading();
        waitButtonLoader();
        waitLoader();

        LogUtils.info("PersonalDetailsPage: click send Phone OTP");
    }

    public void nextStep() {
        WebElement e = this.getNextButton();
        this.moveElementToVisible(e);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click()",e);
//        ScreenshotHelper.takeFullPageScreenshot(getDriverNew(), "screenshots", "registerNextStep");

        // Check for error
        String alertMsg = checkExistsAlertMsg();
        if (alertMsg != null) {
            Assert.fail("An error occurred during email verification submission. Error Msg: " + alertMsg);
        }

        this.waitLoading();
        waitButtonLoader();
        waitLoader();
    }

    public void submit() {
        WebElement e = this.getSubmitButton();
        this.moveElementToVisible(e);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click()",e);
        LogUtils.info("Click Submit button");
        this.waitLoading();
        waitButtonLoader();
        waitLoader();
    }

    public void closeImg()
    {
        driver.findElement(By.xpath("//img[@data-testid='closeImg']")).click();
    }

    public void closeToolSkipButton()
    {
        try {
            driver.findElement(By.xpath("//a[@class='introjs-skipbutton']")).click();
            waitLoading();
        }
        catch (Exception e)
        {
            LogUtils.info("no tools skip popup");
        }
    }

    public boolean checkExistsVerifyPhoneNoDialog(String phone) {
        return getPhoneOTPDialog(phone) != null;
    }

    public void waitLoadingKycPDContent() {
        assertVisibleElementExists(By.xpath("//div[contains(@class,'is-drawer') and not(contains(@style,'display'))]//div[@aria-label='Personal Details Verification']"),"Personal Details Verification Dialog");
        assertVisibleElementExists(By.xpath("//div[contains(@class, 'kyc-container')]"),"Personal Details Verification Content");
    }

    public void waitLoadingKycPDSummaryContent() {
        assertVisibleElementExists(By.xpath("//div[contains(@class,'kyc_drawer') and not(contains(@style,'display'))]"),"Personal Details Summary Dialog");
        assertVisibleElementExists(By.xpath("//div[contains(@class, 'kyc_container')]"),"Personal Details Summary Content");
    }

    public String checkExistsAlertMsg() {
        return checkExistsAlertMsg(this::getAlertMsgBy, "Registration");
    }

    public String getPhoneDisplayCountryCode() {
        WebElement e = getPhoneDisplayEle();
        String codeText = e.getText();
        String countryCode = codeText.replace("+", "");
        LogUtils.info("Phone Country Code Display: " + codeText);

        return countryCode;
    }

}
