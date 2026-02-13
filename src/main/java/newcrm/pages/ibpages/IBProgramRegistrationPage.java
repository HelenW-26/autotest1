package newcrm.pages.ibpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import newcrm.testcases.BaseTestCase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.Random;

public class IBProgramRegistrationPage extends Page {

    public IBProgramRegistrationPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getPageProtectPasswordEle() {
        return assertElementExists(By.xpath("//input[@type='password']"), "Page Protect Password Input Box");
    }

    protected WebElement getSubmitPageProtectPasswordButtonEle() {
        return assertElementExists(By.xpath("//input[@type='submit']"), "Submit Page Protect Password Button");
    }

    protected WebElement getIBTabEle() {
        return assertElementExists(By.xpath("//div[@class='form-title']/div[3]"), "IB Tab");
    }

    protected WebElement getChangeActionInputEle() {
        return assertElementExists(By.xpath("//div[@class='change-url']/input"), "Change Action Input Box");
    }

    protected WebElement getSubmitChangeActionEle() {
        return assertElementExists(By.xpath("//div[@class='change-url']/span[@class='button']"), "Submit Change Action Button");
    }

    protected WebElement getFirstNameInputEle() {
        return assertElementExists(By.xpath("//div[contains(@class,'form-firstname')]/div[@class='ginput_container']/input"), "First Name Input Box");
    }

    protected WebElement getLastNameInputEle() {
        return assertElementExists(By.xpath("//div[contains(@class,'form-lastname')]/div[@class='ginput_container']/input"), "Last Name Input Box");
    }

    protected WebElement getCountryOfResidenceDropdownEle() {
        return assertElementExists(By.xpath("//div[contains(@class,'form-country')]/div[@class='ginput_container']"), "Country of Residence Dropdown");
    }

    protected WebElement getCountryOfResidenceInputEle() {
        return assertElementExists(By.xpath("//div[@class='country-code' and not(contains(@style, 'display: none'))]/input[@class='country-search']"), "Country of Residence Input Box");
    }

    protected List<WebElement> getCountryOfResidenceDropdownOptionEle() {
        return assertElementsExists(By.xpath("//div[@class='country-code' and not(contains(@style, 'display: none'))]/div[@class='results']/div[@class='results-option' and not(contains(@style, 'display: none'))]"), "Country of Residence Dropdown Options List");
    }

    protected WebElement getPhoneInputEle() {
        return assertElementExists(By.xpath("//div[contains(@class,'form-phone ')]/div[@class='ginput_container']/input"), "Phone Number Input Box");
    }

    protected WebElement getEmailInputEle() {
        return assertElementExists(By.xpath("//div[contains(@class,'form-email')]/div[@class='ginput_container']/input"), "Email Input Box");
    }

    protected WebElement getPasswordInputEle() {
        return assertElementExists(By.xpath("//div[contains(@class,'form-ibPassword')] //input[@type='password']"), "Password Input Box");
    }

    protected WebElement getNotUSResidentEle() {
        return assertElementExists(By.xpath("//label[@for='choice_ib_1']"), "Not US Resident Radio Button");
    }

    protected WebElement getPrivacyPolicyEle() {
        return assertElementExists(By.xpath("//label[@for='choice_ib_2']"), "Privacy Policy Radio Button");
    }

    protected WebElement getRegisterNowButtonEle() {
        return assertElementExists(By.xpath("//input[@type='submit']"), "Register Now Button");
    }

    public void registerNewIBThroughIBProgram(String cpURL, String country, String email, String firstName, String lastName, String phone, String pwd) throws InterruptedException {
        setInputValue(getPageProtectPasswordEle(),"147258");
        triggerClickEvent_withoutMoveElement(getSubmitPageProtectPasswordButtonEle());
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='form-title']/div[3]")));
        triggerClickEvent_withoutMoveElement(getIBTabEle());
        Thread.sleep(1000);
        fastwait.until(ExpectedConditions.elementToBeClickable(getChangeActionInputEle()));
        setInputValue(getChangeActionInputEle(), cpURL);
        triggerElementClickEvent_withoutMoveElement(getSubmitChangeActionEle());

        setInputValue(getFirstNameInputEle(),firstName);
        setInputValue(getLastNameInputEle(),lastName);
        triggerElementClickEvent_withoutMoveElement(getCountryOfResidenceDropdownEle());

//        Random random = new Random();
//        int index = random.nextInt(getCountryOfResidenceDropdownOptionEle().size());
//        WebElement countryEle = getCountryOfResidenceDropdownOptionEle().get(index);
//        String country = countryEle.getText();

        setInputValue(getCountryOfResidenceInputEle(),country);
        triggerElementClickEvent_withoutMoveElement(getCountryOfResidenceDropdownOptionEle().get(0));
        setInputValue(getPhoneInputEle(),phone);
        setInputValue(getEmailInputEle(),email);
        setInputValue(getPasswordInputEle(),pwd);
        triggerElementClickEvent_withoutMoveElement(getNotUSResidentEle());
        triggerElementClickEvent_withoutMoveElement(getPrivacyPolicyEle());
        triggerElementClickEvent_withoutMoveElement(getRegisterNowButtonEle());
    }




}