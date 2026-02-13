package newcrm.pages.auclientpages.Register;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.RegisterGoldPersonalDetailsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AURegisterGoldPersonalDetailsPage extends RegisterGoldPersonalDetailsPage {

    public AURegisterGoldPersonalDetailsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void closeImg()
    {
        try {
            driver.findElement(By.xpath("//div[@data-testid='notificationDialog']/div/div/button")).click();
        }catch (Exception e)
        {
            GlobalMethods.printDebugInfo("no notification dialog display");
        }

        try {
            driver.findElement(By.xpath("(//img[@data-testid='closeImg'])[1]")).click();
        }catch (Exception e)
        {
            GlobalMethods.printDebugInfo("no img display");
        }

        try {
            driver.findElement(By.xpath("(//img[@data-testid='closeImg'])[2]")).click();
        }catch (Exception e)
        {
            GlobalMethods.printDebugInfo("no img display");
        }
    }

}
