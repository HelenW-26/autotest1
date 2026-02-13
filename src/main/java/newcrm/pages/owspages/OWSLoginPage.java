package newcrm.pages.owspages;

import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.LogUtils;

import java.time.Duration;
import java.util.AbstractMap;
import java.util.Map;


public class OWSLoginPage extends Page {
	
	protected String ows_url;
//	protected String code = "999999";

    protected WebElement getLoginButtonEle() {
        return assertElementExists(By.xpath("//button[@type='submit']"), "Login Button");
    }

    protected By getAlertMsgBy() {
        return By.xpath("//div[@class='ant-message-custom-content ant-message-error']");
    }

	public OWSLoginPage(WebDriver driver, String url) {
		super(driver);
		ows_url = url;
		this.driver = driver;
		driver.get(url);
	}

    /***
     *
     * @param name username or email
     */
    public void owsLogin(String name,String password) {
        //check if already logged in
        if(driver.findElements(By.xpath("//div[contains(@aria-controls,'task-popup')]")).isEmpty()){
            try{
                setUserName(name);
                setPassWord(password);
                triggerElementClickEvent_withoutMoveElement(getLoginButtonEle());
                LogUtils.info("Clicked OWS Login Button");
            }catch(AssertionError e){
                LogUtils.info("OWS: Already Logged In");
            }
        }
    }


//	/***
//	 *
//	 * @param name username or email
//	 */
	public void setUserName(String name) {
		WebElement ele = assertVisibleElementExists(By.xpath("//input[@id='loginFrom_username']"), "Login Email");
		ele.sendKeys(name);
		LogUtils.info("Set Login Email");
	}
//	/***
//	 *
//	 * @param password user password
//	 */
	public void setPassWord(String password) {
		WebElement ele = assertVisibleElementExists(By.xpath("//input[@id='loginFrom_password']"), "Login Password");
		ele.sendKeys(password);
		LogUtils.info("Set Login Password");
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
                if (!checkNavigateUrl(ows_url)) {
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
