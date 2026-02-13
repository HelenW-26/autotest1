package newcrm.pages.starclientpages;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.TransactionHistoryPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class STARTransactionHistoryPage extends TransactionHistoryPage {

    public STARTransactionHistoryPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected List<WebElement> getFirst25DpElements() {
        WebElement d_tab = this.findClickableElementByXpath("//div[@id='tab-deposit']");
        this.moveElementToVisible(d_tab);
        d_tab.click();
        try {
            return driver.findElements(By.xpath("//div[@id='transactionHistory']//table/tbody/tr"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected List<WebElement> getFirst25WithdrawElements() {
        WebElement w_tab = this.findClickableElementByXpath("//div[@id='tab-withdraw']");
        this.moveElementToVisible(w_tab);
        w_tab.click();
        String xpath = "//div[@id='transactionHistory']//table/tbody/tr";
        this.findVisibleElemntByXpath(xpath);
        return driver.findElements(By.xpath(xpath));
    }

    @Override
    protected List<WebElement> getFirst25TransferElements(){
        WebElement w_tab = this.findClickableElemntByTestId("transfer");
        this.moveElementToVisible(w_tab);
        w_tab.click();
        this.findVisibleElemntByXpath(
                "//div[@class='el-table__body-wrapper is-scrolling-none']/table/tbody");
        return driver.findElements(
                By.xpath("//div[@class='el-table__body-wrapper is-scrolling-none']/table/tbody/tr"));
    }

    @Override
    protected List<WebElement> getFirstRowTransferElements(){
        WebElement w_tab = this.findClickableElemntByTestId("transfer");
        this.moveElementToVisible(w_tab);
        w_tab.click();
        this.findVisibleElemntByXpath(
                "//div[@class='el-table__body-wrapper is-scrolling-none']/table/tbody");
        return driver.findElements(
                By.xpath("//div[@class='el-table__body-wrapper is-scrolling-none']/table/tbody/tr[1]"));
    }

    @Override
    public void cancelWithdraw()
    {
        if(!GlobalProperties.isWeb) {
            return;
        }

        //Cancel withdraw in CP
        try {
            WebElement cancelBtn = driver.findElement(By.xpath("(//button[./span[contains(text(),'CANCEL')]])[1]"));
            cancelBtn.click();
            waitLoading();
            WebElement yesBtn = driver.findElement(By.xpath("//button[./span[normalize-space() = 'Yes']]"));
            yesBtn.click();
            waitLoading();
            GlobalMethods.printDebugInfo("Cancelled withdraw in CP");
        }
        catch (Exception e)
        {
            GlobalMethods.printDebugInfo("No withdrawal Cancel button found");
        }
    }

}
