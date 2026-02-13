package newcrm.pages.dappages;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.util.List;
import java.util.stream.Collectors;

public class DAPRegistrationPage extends Page {

    public DAPRegistrationPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getPageProtectPasswordEle() {
        return assertElementExists(By.xpath("//input[@name='post_password']"), "Page Protect Password Input Box");
    }

    protected WebElement getSubmitPageProtectPasswordButtonEle() {
        return assertElementExists(By.xpath("//input[@type='submit']"), "Submit Page Protect Password Button");
    }

    protected WebElement getChangeActionInputEle() {
        return assertElementExists(By.xpath("//div[@class='change-url']/input[@class='domain-input']"), "Change Action Input Box");
    }

    protected WebElement getSubmitChangeActionEle() {
        return assertElementExists(By.xpath("//div[@class='change-url']/span[@class='button']"), "Submit Change Action Button");
    }

    protected WebElement getEmailInputEle() {
        return assertElementExists(By.xpath("//div[@class='ginput_container']/input[@type='email']"), "Email Input Box");
    }

    protected WebElement getUserNameInputEle() {
        return assertElementExists(By.xpath("//div[contains(@class,'form-username')] //input[@type='text']"), "User Name Input Box");
    }

    protected WebElement getFirstNameInputEle() {
        return assertElementExists(By.xpath("//div[contains(@class,'form-firstname')] //input[@type='text']"), "First Name Input Box");
    }

    protected WebElement getLastNameInputEle() {
        return assertElementExists(By.xpath("//div[contains(@class,'form-lastname')] //input[@type='text']"), "Last Name Input Box");
    }

    protected WebElement getPasswordInputEle() {
        return assertElementExists(By.xpath("//input[@type='password']"), "Password Input Box");
    }

    protected WebElement getCountryOfResidenceDropdownTriggerEle() {
        return assertElementExists(By.xpath("//div[contains(@class,'form-country')]/div[@class='ginput_container']/input[@type='text']"), "Country Of Residence Dropdown Trigger");
    }

    protected WebElement getCountryOfResidenceSearchInputEle() {
        return assertElementExists(By.xpath("//div[@class='country-code' and not(contains(@style,'display: none'))]/input[@class='country-search']"), "Country Of Residence Search Input Box");
    }

    protected List<WebElement> getCountryOfResidenceDropdownValuesListEle() {
        return assertElementsExists(By.xpath("//div[@class='country-code' and contains(@style,'display: block')] //div[@class='results-option'and not(contains(@style,'display: none'))]"), "Country Of Residence Dropdown Values List");
    }

    protected WebElement getPhoneNumberInputEle() {
        return assertElementExists(By.xpath("//div[contains(@class,'form-phone gfield')]/div[@class='ginput_container']/input"), "Phone Number Input Box");
    }

    protected WebElement getRegisterNowButtonEle() {
        return assertElementExists(By.xpath("//input[@type='submit']"), "Register Now Button");
    }




    public void registerNewDAP(String cpURL, String firstName, String lastName, String email, String phone, String pwd, String country) throws InterruptedException {
        driver.get(GlobalProperties.TESTENV_CPA_REG_URL);

        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='submit']")));
        setInputValue(getPageProtectPasswordEle(),"147258");
        triggerClickEvent(getSubmitPageProtectPasswordButtonEle());
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='change-url']/input[@class='domain-input']")));

        Thread.sleep(2000);
        fastwait.until(ExpectedConditions.elementToBeClickable(getSubmitChangeActionEle()));
        triggerClickEvent(getSubmitChangeActionEle());
        fastwait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[@class='now-action']"),"/"));
        setInputValue(getChangeActionInputEle(),cpURL);
        triggerElementClickEvent(getSubmitChangeActionEle());

        setInputValue(getEmailInputEle(),email);
        setInputValue(getUserNameInputEle(),firstName.toLowerCase());
        setInputValue(getFirstNameInputEle(),firstName);
        setInputValue(getLastNameInputEle(),lastName);
        setInputValue(getPasswordInputEle(),pwd);

        triggerElementClickEvent(getCountryOfResidenceDropdownTriggerEle());
        setInputValue(getCountryOfResidenceSearchInputEle(), country);
        triggerElementClickEvent(getCountryOfResidenceDropdownValuesListEle().get(0));

        setInputValue(getPhoneNumberInputEle(),phone);

        triggerElementClickEvent(getRegisterNowButtonEle());
    }


}
