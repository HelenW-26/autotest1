package newcrm.pages.pugclientpages;

import newcrm.pages.clientpages.deposit.SKoreaBankTransferPage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;

public class PUSKBankTransferPage extends SKoreaBankTransferPage {
    public PUSKBankTransferPage(WebDriver driver) {
        super(driver);
    }
    @Override
    public HashMap<String,String> getAllAvailableAccounts(){
        waitLoading();
        HashMap<String,String> result = new HashMap<>();
        //getAccountDropDown().click();
        JavascriptExecutor javascript = (JavascriptExecutor) driver;
        javascript.executeScript("arguments[0].click()", getAccountDropDown());
        waitLoading();
        List<WebElement> all_accounts = this.getAllOpendElements();
        result = this.getAccounts(all_accounts);

        getAccountDropDown().click();
        waitLoading();
        return result;
    }
}
