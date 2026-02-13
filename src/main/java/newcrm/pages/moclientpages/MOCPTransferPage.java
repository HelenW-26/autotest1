package newcrm.pages.moclientpages;

import newcrm.pages.clientpages.TransferPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MOCPTransferPage extends TransferPage {

    public MOCPTransferPage(WebDriver driver)
    {
        super(driver);
    }

    @Override
    public void submit() {
        WebElement submit = driver.findElement(By.xpath("//button[@data-testid='submit']"));
        submit.click();
        this.waitLoading();
        this.moveContainerToTop();
        this.findVisibleElemntByXpath("//div[contains(@class, 'result_info')]");
    }

}
