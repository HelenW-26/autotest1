package newcrm.pages.auclientpages.Register;

import newcrm.pages.clientpages.register.RegisterHomePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

public class AURegisterHomePage extends RegisterHomePage {

    public AURegisterHomePage(WebDriver driver,String url)
    {
        super(driver,url);
        driver.get(url);
        this.waitLoading();
    }

    @Override
    public void registerLiveAccount() {
        WebElement pwd = driver.findElement(By.xpath("//input[@name='post_password']"));
        pwd.sendKeys("147258");

        WebElement enter = driver.findElement(By.xpath("//input[@name='Submit']"));
        enter.click();

      /* WebElement a_live = this.getLiveReister();
        this.moveElementToVisible(a_live);
        this.clickElement(a_live);
        if(driver.getWindowHandles().size()>1) {
            driver.switchTo().window(driver.getWindowHandles().toArray()[1].toString());
        }
        this.checkUrlContains("/forex-trading-account/");*/
    }
}
