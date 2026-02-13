package newcrm.pages.umclientpages;

import newcrm.pages.clientpages.TransferPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class UMCPTransferPage extends TransferPage {

    public UMCPTransferPage(WebDriver driver)
    {
        super(driver);
    }

    @Override
    public void submit() {
        WebElement submit = driver.findElement(By.xpath("//button[@data-testid='submit']"));
        submit.click();
        this.waitLoading();
        this.findVisibleElemntByXpath("//div[contains(@class, 'result_info')]");
    }

    @Override
    public void clickUseCreditBtn(){
        WebElement confirmBtn = findClickableElementByXpath("(//div[@class='el-dialog__footer']//button[.//span[normalize-space(text())='TRANSFER']])[2]");
        confirmBtn.click();
    }

}
