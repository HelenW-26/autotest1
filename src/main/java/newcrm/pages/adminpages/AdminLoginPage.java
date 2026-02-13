package newcrm.pages.adminpages;

import newcrm.pages.Page;
import org.openqa.selenium.WebDriver;

public class AdminLoginPage extends Page {
    protected String admin_url;
    public AdminLoginPage(WebDriver driver,String url) {
        super(driver);
        admin_url = url;
        this.driver = driver;
        driver.get(url);
    }

    /***
     *
     * @param name username or email
     */
    public void setUserName(String name) {
        this.findVisibleElemntByXpath("//input[@name='userName_login']").sendKeys(name);
    }
    /***
     *
     * @param password user password
     */
    public void setPassWord(String password) {
        this.findVisibleElemntByXpath("//input[@name='password_login']").sendKeys(password);
    }

    public boolean submit() {

        try {
            this.findVisibleElemntByXpath("//button[@id='btnLogin']").click();
            //this.waitvisible.until(ExpectedConditions.urlContains("home"));
            this.waitLoading();
        }catch(Exception e){
            System.out.println("Login Failed: "+ e.getMessage());
            return false;
        }
        return true;
    }
}
