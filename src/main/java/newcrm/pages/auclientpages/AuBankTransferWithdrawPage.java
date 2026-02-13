package newcrm.pages.auclientpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.withdraw.LocalBankWithdrawPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class AuBankTransferWithdrawPage extends LocalBankWithdrawPage {

    WebDriverWait wait03 = new WebDriverWait(driver, Duration.ofSeconds(20));

    public AuBankTransferWithdrawPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected List<WebElement> getAllCardsElementNew(){
        //observed the "Add New Account" selection will be selected automatically sometimes
        String xpath = "//div[contains(@class,'el-select-dropdown el-popper') and not(contains(@style,'display'))]//li";
        String bankaccountxpath = "//div[@data-testid='selectedCardID']";
        try {
            wait03.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
            List<WebElement> result = driver.findElements(By.xpath(xpath));
            return result;
        }catch(Exception e) {
            GlobalMethods.printDebugInfo("BankTransferWithdrawPage: Dropdown close jor");
            wait03.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(bankaccountxpath)));
            WebElement element = driver.findElement(By.xpath(bankaccountxpath));
            js.executeScript("arguments[0].click()",element);
            List<WebElement> result = driver.findElements(By.xpath(xpath));
            return result;
        }
    }
}
