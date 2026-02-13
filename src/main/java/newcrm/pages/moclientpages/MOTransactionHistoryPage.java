package newcrm.pages.moclientpages;

import newcrm.pages.clientpages.TransactionHistoryPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class MOTransactionHistoryPage extends TransactionHistoryPage {

    public MOTransactionHistoryPage(WebDriver driver)
    {
        super(driver);
    }

    @Override
    protected List<WebElement> getFirst25DpElements() {
        WebElement d_tab = assertClickableElementExists(By.xpath("//div[@id='tab-deposit']"), "Deposit Tab");
        triggerElementClickEvent_withoutMoveElement(d_tab);
        return assertElementsExists(By.xpath("//div[@id='transactionHistory']/div[@class='content_box']//table/tbody/tr"), "Deposit History Content");
    }

    @Override
    public List<History> getFirst25Withdraw() {
//        driver.navigate().refresh();
//        driver.navigate().refresh();
        this.waitLoadingForCustomise(120);
        List<WebElement> elements = getFirst25WithdrawElements();
        return getHistory(elements);
    }
    public List<History> getFirst25Deposit() {
        //driver.navigate().refresh();
        //driver.navigate().refresh();
        this.waitLoadingForCustomise(120);
        List<WebElement> elements = getFirst25DpElements();
        return getHistory(elements);
    }
    @Override
    protected List<WebElement> getFirst25WithdrawElements() {
        waitLoading();
        WebElement w_tab = assertClickableElementExists(By.xpath("//div[@id='tab-withdraw']"), "Withdrawal Tab");
        triggerElementClickEvent_withoutMoveElement(w_tab);
        return assertElementsExists(By.xpath("//div[@id='transactionHistory']/div[@class='content_box']//table/tbody/tr"), "Withdrawal History Content");
    }

    @Override
    protected List<WebElement> getFirst25TransferElements(){
        WebElement w_tab = assertClickableElementExists(By.xpath("//div[@id='tab-transfer']"), "Transfers Tab");
        triggerElementClickEvent_withoutMoveElement(w_tab);
        return assertElementsExists(By.xpath("//div[@id='transactionHistory']/div[@class='content_box']//table/tbody/tr"), "Transfers History Content");
    }

    @Override
    protected List<WebElement> getFirstRowTransferElements(){
        WebElement w_tab = this.findClickableElemntByTestId("transfer");
        this.moveElementToVisible(w_tab);
        w_tab.click();
        this.findVisibleElemntByXpath(
                "//div[@id='transactionHistory']/div[2]/div/ul[2]/li[contains(@class,'active')]//table/tbody/tr");
        return driver.findElements(
                By.xpath("//div[@id='transactionHistory']/div[2]/div/ul[2]/li[contains(@class,'active')]//table/tbody/tr[1]"));
    }

    @Override
    public List<TransferHistory> getFirstRowTransfer() {
        driver.navigate().refresh();
        driver.navigate().refresh();
        waitLoadingForCustomise(120);
        List<WebElement> elements = this.getFirstRowTransferElements();
        List<TransferHistory> result = new ArrayList<>();
        for (WebElement element : elements) {
            String info = element.getText();
            String[] values = info.split("\n");

            String date = values[0].trim();
            int pos = date.indexOf(" ");
            if(pos > 0) {
                date = date.substring(0,pos);
            }

            String t_account = values[1].trim();
            String fr_account = t_account.substring(0, t_account.indexOf("(")).trim();
            String currency = t_account.substring(t_account.indexOf("(") + 1, t_account.lastIndexOf(")")).trim();

            t_account = values[2].trim();
            String to_account = t_account.substring(0, t_account.indexOf("(")).trim();

            String method = "";
            String t_amount = values[3].trim();

            String amount = getAmount(t_amount);

            String status = values[4].trim();

            result.add(new TransferHistory(date, fr_account, currency, method, amount, status,to_account));
        }
        return result;
    }
}
