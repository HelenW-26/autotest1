package newcrm.pages.vtclientpages;

import newcrm.pages.clientpages.TransferPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class VTCPTransferPage extends TransferPage {

    public VTCPTransferPage(WebDriver driver)
    {
        super(driver);
    }

    @Override
    public void submit() {
        WebElement submit = driver.findElement(By.xpath("//button[@data-testid='submit']"));
        submit.click();
        this.waitLoading();
        this.findVisibleElemntByXpath("//div[@class='success_content']");
    }

}
