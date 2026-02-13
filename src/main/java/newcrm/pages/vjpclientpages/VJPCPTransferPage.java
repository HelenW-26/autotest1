package newcrm.pages.vjpclientpages;

import newcrm.pages.clientpages.TransferPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class VJPCPTransferPage extends TransferPage {

    public VJPCPTransferPage(WebDriver driver)
    {
        super(driver);
    }

    public void submit() {
        WebElement submit = driver.findElement(By.xpath("//button[@data-testid='submit']"));
        submit.click();
        this.waitLoading();
        this.findVisibleElemntByXpath("//div[contains(@class, 'result_info')]");
    }
}
