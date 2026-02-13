package newcrm.pages.vjpclientpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.withdraw.LocalBankWithdrawPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class VJPJapanBankWithdrawPage extends LocalBankWithdrawPage {

    WebDriverWait wait03 = new WebDriverWait(driver, Duration.ofSeconds(20));

    public VJPJapanBankWithdrawPage(WebDriver driver)
    {
        super(driver);
    }

    @Override
    public void setBankName() {
        WebElement bankName = this.findVisibleElemntByXpath("//div[@data-testid='bankName' or @data-testid='bank_code'] | //input[@data-testid='bank_name']");
        String placeholder = bankName.getAttribute("class");
        //if bank branch is input field
        if(placeholder!=null && placeholder.trim().toLowerCase().contains("el-input".toLowerCase()))
        {
            String disabled = bankName.getAttribute("disabled");
            if(disabled!=null && disabled.trim().equalsIgnoreCase("disabled")) {
                GlobalMethods.printDebugInfo("The Bank Name element is disabled");
                return;
            }
            this.moveElementToVisible(bankName);
            String tagName = bankName.getTagName();
            if (tagName!=null && tagName.trim().equalsIgnoreCase("div")){
                bankName = this.findVisibleElemntByXpath("//div/input[@data-testid='bankName']");
            }
            bankName.sendKeys("test bank name");
            GlobalMethods.printDebugInfo("Set Bank Name to: test bank name");
            return;
        }
        //else it wil be dropdown
        this.moveElementToVisible(bankName);
        bankName.click();
        String item_xpath = "//div[@class='el-select-dropdown el-popper ht-select-dropdown' and not(contains(@style,'display'))]//li";
        wait03.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(item_xpath)));
        List<WebElement> ops = driver.findElements(By.xpath(item_xpath));
        String name = this.selectRandomValueFromDropDownList(ops);
        GlobalMethods.printDebugInfo("Set Bank Name to: " + name);
    }

    @Override
    public void setBankBranch(String branch) {
        WebElement bankBranch = this.findVisibleElemntByXpath("//input[@data-testid='bankBranch'] | //div[@data-testid='selectedBankBranch']");
        String placeholder = bankBranch.getAttribute("class");
        //if bank branch is input field
        if(placeholder!=null && placeholder.trim().equalsIgnoreCase("el-input__inner"))
        {
            String disabled = bankBranch.getAttribute("disabled");
            if(disabled!=null && disabled.trim().equalsIgnoreCase("disabled")) {
                GlobalMethods.printDebugInfo("The Bank Branch element is disabled");
                return;
            }
            this.moveElementToVisible(bankBranch);
            bankBranch.sendKeys(branch);
            GlobalMethods.printDebugInfo("Set Bank Branch to: " + branch);
            return;
        }
        //else it wil be dropdown
        this.moveElementToVisible(bankBranch);
        bankBranch.click();
        List<WebElement> ops = driver.findElements(By.xpath("//div[@class='el-select-dropdown el-popper ht-select-dropdown' and not(contains(@style,'display'))]//li"));
        String name = this.selectRandomValueFromDropDownList(ops);
        GlobalMethods.printDebugInfo("Set Bank Branch to: " + name);
    }

    @Override
    public void setAccountType() {
        WebElement dp_accountType = this.findVisibleElemntByXpath("//div[@data-testid='accountType' or @data-testid='attach_account_type'] | //input[@data-testid='accountType']");
        String placeholder = dp_accountType.getAttribute("class");
        //if account type is input field
        if(placeholder!=null && placeholder.trim().toLowerCase().contains("el-input".toLowerCase()))
        {
            String disabled = dp_accountType.getAttribute("disabled");
            if(disabled!=null && disabled.trim().equalsIgnoreCase("disabled")) {
                GlobalMethods.printDebugInfo("The Account Type element is disabled");
                return;
            }
            this.moveElementToVisible(dp_accountType);
            String tagName = dp_accountType.getTagName();
            if (tagName!=null && tagName.trim().equalsIgnoreCase("div")){
                dp_accountType = this.findVisibleElemntByXpath("//div/input[@data-testid='accountType']");
            }
            dp_accountType.sendKeys("accountType");
            GlobalMethods.printDebugInfo("Set Account Type to: accountType");
            return;
        }
        this.moveElementToVisible(dp_accountType);
        dp_accountType.click();
        List<WebElement> ops = driver.findElements(By.xpath("//div[@class='el-select-dropdown el-popper ht-select-dropdown' and not(contains(@style,'display'))]//li"));
        String name = this.selectRandomValueFromDropDownList(ops);
        GlobalMethods.printDebugInfo("Set account type to: " + name);
    }

}
