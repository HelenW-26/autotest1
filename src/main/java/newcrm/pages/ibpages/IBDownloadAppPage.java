package newcrm.pages.ibpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

public class IBDownloadAppPage extends Page {

    public IBDownloadAppPage(WebDriver driver) {
        super(driver);
    }

    protected String ibRefDownloadAppLink(){
        return "https://apps.apple.com/au/app/vantage-all-in-one-trading-app/id1457929724";
    }

    protected WebElement getCountryCodeDropdownEle(){
        return assertElementExists(By.xpath("//div[@class='area-code']/i"), "Country Code - Dropdown");
    }

    protected List<WebElement> getPopularCountryCodeListEle(){
        return assertElementsExists(By.xpath("//div[@class='van-index-bar']/div[2]/div[2]/div/span[@class='country-num' and not(text()='+61')]"), "Country Code List - Popular");
    }

    protected WebElement getPhoneNumberInputEle(){
        return assertElementExists(By.xpath("//input[@placeholder='Phone number']"), "Phone Number Input");
    }

    protected WebElement getAgreeTermsEle(){
        return assertElementExists(By.xpath("//span[@class='agree']"), "Agree Terms Radio Button");
    }

    protected WebElement getSendSMSOTPEle(){
        return assertElementExists(By.xpath("//div[@class='SMS']"), "Send SMS OTP Button");
    }

    protected List<WebElement> getSMSOTPInputsEle(){
        return assertElementsExists(By.xpath("//div[@class='code-container']/div/input"), "SMS OTP Inputs");
    }

    protected WebElement getSetPasswordInputEle(){
        return assertElementExists(By.xpath("//input[@id='password']"), "Set Password Input");
    }

    protected WebElement getSubmitPasswordEle(){
        return assertElementExists(By.xpath("//button[contains(@class,'btn-submit')]"), "Submit Password Button");
    }

    protected WebElement getDownloadAppButtonEle(){
        return assertElementExists(By.xpath("//div[@class='modal-button']/button"), "Download App Button");
    }

    public String downloadAppSetCountryCode(){
        triggerElementClickEvent_withoutMoveElement(getCountryCodeDropdownEle());
        String countryCode = selectRandomDropDownOption_ElementClickEvent(getPopularCountryCodeListEle());

        return countryCode;
    }

    public String downloadAppSetPhoneNo(){

        String phone = "0000"+GlobalMethods.getRandomNumberString(11);
        setInputValue(getPhoneNumberInputEle(),phone);

        triggerElementClickEvent_withoutMoveElement(getAgreeTermsEle());
        triggerElementClickEvent_withoutMoveElement(getSendSMSOTPEle());

        return phone;
    }


    public boolean downloadAppInputOTP(String otp){

        fastwait.until(ExpectedConditions.visibilityOfAllElements(getSMSOTPInputsEle()));
        for (int i = 0; i < getSMSOTPInputsEle().size(); i++) {
            WebElement input = getSMSOTPInputsEle().get(i);
            input.sendKeys(Character.toString(otp.charAt(i)));
        }
        fastwait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='loading']")));

        if(!driver.findElements(By.xpath("//div[@class='van-toast__text']")).isEmpty()){
            return true;
        }

        String pwd = GlobalMethods.generatePassword();
        fastwait.until(ExpectedConditions.visibilityOf(getSetPasswordInputEle()));
        setInputValue(getSetPasswordInputEle(),pwd);
        triggerElementClickEvent_withoutMoveElement(getSubmitPasswordEle());

        fastwait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='loading']")));
        if(!driver.findElements(By.xpath("//div[@class='van-toast__text']")).isEmpty()){
            return true;
        }

        fastwait.until(ExpectedConditions.visibilityOf(getDownloadAppButtonEle()));
        triggerElementClickEvent_withoutMoveElement(getDownloadAppButtonEle());

        String downloadAppHandle = driver.getWindowHandle();

        //Switch To last tab
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(tabs.size()-1));
        String appStoreURL = getCurrentURL();

        if(!appStoreURL.contains(ibRefDownloadAppLink())){
            Assert.fail("Directed to incorrect URL after Download App flow - Expected: apps.apple.com/au/app/vantage-all-in-one-trading-app/id1457929724 ; Actual: " + appStoreURL);
        }
        driver.close();
        driver.switchTo().window(downloadAppHandle);

        return false;
    }
}
