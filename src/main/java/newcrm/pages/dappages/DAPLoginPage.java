package newcrm.pages.dappages;

import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.LogUtils;

import java.time.Duration;
import java.util.AbstractMap;
import java.util.Map;


public class DAPLoginPage extends Page {

	protected String dap_url;
//	protected String code = "999999";

    protected WebElement getLoginButtonEle() {
        return assertElementExists(By.xpath("//div[@role='tabpanel' and not(contains(@style,'display: none'))] //div[@class='login-form__button-group']/button"), "Login Button");
    }

    protected By getAlertMsgBy() {
        return By.xpath("//div[@class='ant-message-custom-content ant-message-error']");
    }

	public DAPLoginPage(WebDriver driver, String url) {
		super(driver);
        dap_url = url;
		this.driver = driver;
		driver.get(url);
	}

    /***
     *
     * @param name username or email
     */
    public void dapLogin(String name,String password) {
        //check if already logged in
//        if(driver.findElements(By.xpath("//div[contains(@aria-controls,'task-popup')]")).isEmpty()){
            try{
                setUserName(name);
                setPassWord(password);
                triggerElementClickEvent_withoutMoveElement(getLoginButtonEle());
                LogUtils.info("Clicked DAP Login Button");
            }catch(Exception e){
                LogUtils.info("DAP: Already Logged In");
            }
//        }
    }

    /***
     *
     * @param phone user phone number
     */
    public void dapLogin_Phone(String country, String phone,String password) {
        //check if already logged in
                setCountryCode(country);
                setPhoneNumber(phone);
                setPassWord(password);
                triggerElementClickEvent_withoutMoveElement(getLoginButtonEle());
                LogUtils.info("Clicked DAP Phone Login Button");
    }


//	/***
//	 *
//	 * @param name username or email
//	 */
	public void setUserName(String name) {
		WebElement ele = assertVisibleElementExists(By.xpath("//input[@data-testid='userName_login']"), "Login Email");
		ele.sendKeys(name);
		LogUtils.info("Set Login Email");
	}
//	/***
//	 *
//	 * @param phone user phone number
//	 */
    public void setPhoneNumber(String phone) {
        WebElement ele = assertVisibleElementExists(By.xpath("//input[@type='tel']"), "Login Phone Number");
        ele.sendKeys(phone);
        LogUtils.info("Set Login Phone Number");
    }
//	/***
//	 *
//	 * @param password user password
//	 */
	public void setPassWord(String password) {
		WebElement ele = assertVisibleElementExists(By.xpath("//div[@role='tabpanel' and not(contains(@style,'display: none'))] //input[@data-testid='password_login']"), "Login Password");
		ele.sendKeys(password);
		LogUtils.info("Set Login Password");
	}

    public void setCountryCode(String country) {
        WebElement phoneTabEle = assertVisibleElementExists(By.xpath("//div[@id='tab-phone']"), "Phone Tab");
        phoneTabEle.click();

        WebElement ele = assertVisibleElementExists(By.xpath("//div[@class='ht-select__selection'] //input"), "Country Code Dropdown Input");
        ele.click();
//        setInputValue(ele, country);

        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(),'"+country+"')]")));
        triggerClickEvent(driver.findElement(By.xpath("//span[contains(text(),'"+country+"')]")));
//        WebElement countryCodeEle = assertVisibleElementExists(By.xpath("//span[contains(text(),'"+country+"')]"), country+" Country Code Option");
//        countryCodeEle.click();

        LogUtils.info("Set Login Country Code");
    }

    public String checkExistsLoginAlertMsg() {
        return checkExistsAlertMsg(this::getAlertMsgBy, "Login");
    }

    public Map.Entry<Boolean, String> checkLoginSuccess() {

        try {
            // Check if still remains at login page after login submission
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(90));
            boolean urlChanged = wait.until((ExpectedCondition<Boolean>) wd ->
                    !wd.getCurrentUrl().contains("login")
            );

            if (urlChanged) {
                if (!checkNavigateUrl(dap_url)) {
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

}
