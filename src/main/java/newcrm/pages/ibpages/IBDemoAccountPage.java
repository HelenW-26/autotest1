package newcrm.pages.ibpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

public class IBDemoAccountPage extends Page {

    public IBDemoAccountPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getIBDemoPagePasswordInputEle(){
        return assertElementExists(By.xpath("//input[@name='post_password']"), "IB Demo Page Password Input");
    }

    protected WebElement getIBDemoPagePasswordSubmitEle(){
        return assertElementExists(By.xpath("//input[@class='ppw-pwd-submit-btn']"), "IB Demo Page Password Submit");
    }

    protected WebElement getCrmURLInputEle(){
        return assertElementExists(By.xpath("//input[@id='CrmUrl']"), "IB Demo Page CRM URL Input");
    }

    protected WebElement getCrmURLClickEle(){
        return assertElementExists(By.xpath("//button[@id='changeCrmUrl']"), "IB Demo Page CRM URL Click");
    }

    protected WebElement getEmailInputEle(){
        return assertElementExists(By.xpath("//input[@id='email']"), "IB Demo Page Email Input");
    }

    protected WebElement getContinueButtonEle(){
        return assertElementExists(By.xpath("//p[@id='next']"), "IB Demo Page Continue Button");
    }

    protected WebElement getNameInputEle(){
        return assertElementExists(By.xpath("//input[@id='name']"), "IB Demo Page Name Input");
    }

    protected WebElement getCountryDropdownEle(){
        return assertElementExists(By.xpath("//input[@id='country']"), "IB Demo Page Country Dropdown");
    }

    protected List<WebElement> getCountryDropdownListEle(){
        return assertElementsExists(By.xpath("//div[@class='country-code'] //div[@class='results']/div[@class='results-option']"), "IB Demo Page Country Dropdown List");
    }

    protected WebElement getCountryDropdownOptionEle(String country){
        return assertElementExists(By.xpath("//div[@class='country-code'] //div[@class='results']/div[@class='results-option']/span[text()='"+country+"']"), "IB Demo Page Country Dropdown Option");
    }

    protected WebElement getCountryCodeDropdownEle(){
        return assertElementExists(By.xpath("//input[@id='phoneCode']"), "IB Demo Page Country Code Dropdown");
    }

    protected List<WebElement> getCountryCodeDropdownListEle(){
        return assertElementsExists(By.xpath("//div[@class='phoneCode-results']/div[@class='phoneCode-results-option']"), "IB Demo Page Country Code Dropdown List",e -> !e.getText().toLowerCase().contains("australia") && !e.getText().toLowerCase().contains("united kingdom"));
    }

    protected WebElement getPhoneInputEle(){
        return assertElementExists(By.xpath("//input[@id='phone']"), "IB Demo Page Phone Number Input");
    }

    protected WebElement getPasswordInputEle(){
        return assertElementExists(By.xpath("//input[@id='password']"), "IB Demo Page Password Input");
    }

    protected WebElement getCloseVideoPlayerEle(){
        return assertElementExists(By.xpath("//p[@class='delvideos']"), "IB Demo Page Close Video Player");
    }

    protected WebElement getBranchVersionInputEle(){
        return assertElementExists(By.xpath("//input[@id='form_branchversion']"), "IB Demo Page Branch Version Input");
    }

    protected WebElement getCreateAccountButtonEle(){
        return assertElementExists(By.xpath("//button[@id='sub-open']"), "IB Demo Page Create Account Button");
    }

    protected WebElement getBritainPopUpButtonEle(){
        return assertElementExists(By.xpath("//div[@style='display: block;'] //a[@class='orange_arrow_button page_next_step']"), "Britain - Important Information Popup Confirm Intention Button");
    }


    public String registerIBDemoAccount(String traderURL, String country) throws InterruptedException {
        try{
            Thread.sleep(1000);
            setInputValue(getIBDemoPagePasswordInputEle(),"147258");
            triggerClickEvent(getIBDemoPagePasswordSubmitEle());
        } catch (AssertionError e) {
            GlobalMethods.printDebugInfo("Page Protected Password input not found, skip entering password.");
        }

        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@id='nowAdd']")));
//        fastwait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//span[@id='nowAdd']"),"https"));
        setInputValue(getCrmURLInputEle(),traderURL);
        triggerClickEvent_withoutMoveElement(getCrmURLClickEle());

        String email = ("autotest"+GlobalMethods.getRandomString(8)+"@testcrm.com").toLowerCase();
        String name = "autotest" + GlobalMethods.getRandomString(10);
        String phone = "0000"+GlobalMethods.getRandomNumberString(10);
        String pwd = GlobalMethods.generatePassword();

        setInputValue(getEmailInputEle(),email);
        triggerElementClickEvent_withoutMoveElement(getContinueButtonEle());

        triggerElementClickEvent_withoutMoveElement(getCloseVideoPlayerEle());

        setInputValue(getNameInputEle(),name);
        triggerElementClickEvent_withoutMoveElement(getCountryDropdownEle());
        triggerElementClickEvent_withoutMoveElement(getCountryDropdownOptionEle(country));

        try{
            //For Britain - Important Information Popup
            triggerElementClickEvent_withoutMoveElement(getBritainPopUpButtonEle());
        } catch (AssertionError e) {

        }

//        String demoCountry = selectRandomDropDownOption_ElementClickEvent(getCountryDropdownListEle());
//        GlobalMethods.printDebugInfo("IB Demo Account Registration - Selected Country: "+demoCountry);

        //If Country Code not auto-filled
        if(driver.findElements(By.xpath("//div[@class='form-element phoneCode hasInner']")).isEmpty()){
            triggerElementClickEvent_withoutMoveElement(getCountryCodeDropdownEle());
            selectRandomDropDownOption_ElementClickEvent(getCountryCodeDropdownListEle());
        }

        setInputValue(getPhoneInputEle(),phone);
        setInputValue(getPasswordInputEle(),pwd);

        triggerClickEvent_withoutMoveElement(getCreateAccountButtonEle());
        Thread.sleep(5000);
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='demo' or @class='register_guide_title_text']")));

        return name;
    }



}
