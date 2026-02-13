package newcrm.pages.umclientpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.withdraw.LocalBankWithdrawPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.LogUtils;

import java.time.Duration;
import java.util.List;

public class UMLocalBankWithdrawPage extends LocalBankWithdrawPage {
    public UMLocalBankWithdrawPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void setBankBranch(String branch) {
        LogUtils.info("Adding new bank branch to:"+ branch);
        WebElement bankBranch = this.findVisibleElemntByXpath("//input[@data-testid='bankBranch']");
        String placeholder = bankBranch.getAttribute("class");
        LogUtils.info("==============:"+placeholder);
        //if bank branch is input field
        if(placeholder!=null && placeholder.trim().equalsIgnoreCase("el-input__inner"))
        {	LogUtils.info("The Bank Branch element is input");
            String disabled = bankBranch.getAttribute("disabled");
            if(disabled!=null && disabled.trim().equalsIgnoreCase("disabled")) {
                GlobalMethods.printDebugInfo("The Bank Branch element is disabled");
                return;
            }
            this.moveElementToVisible(bankBranch);
            bankBranch.sendKeys(branch);
            LogUtils.info("Set Bank Branch to: " + branch);
            GlobalMethods.printDebugInfo("Set Bank Branch to: " + branch);
            return;
        }
        //else it wil be dropdown
        LogUtils.info("The Bank Branch element is dropdown");
        this.moveElementToVisible(bankBranch);
        bankBranch.click();
        List<WebElement> ops = driver.findElements(By.xpath("//div[@class='el-select-dropdown el-popper' and not(contains(@style,'display'))]//li"));
        String name = this.selectRandomValueFromDropDownList(ops);
        GlobalMethods.printDebugInfo("Set Bank Branch to: " + name);
    }

}
