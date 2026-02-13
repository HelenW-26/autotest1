package newcrm.pages.vtclientpages;

import newcrm.pages.clientpages.deposit.LocalBankTransferDepositPage;
import newcrm.global.GlobalMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class VTLocalBankTransferDepositPage extends LocalBankTransferDepositPage {

    public VTLocalBankTransferDepositPage(WebDriver driver)
    {
        super(driver);
    }

    @Override
    public void setLocalDepositorNew () {
        WebElement localdepositor = this.findVisibleElemntByXpath("//div[@class='vt-select']");
        this.moveElementToVisible(localdepositor);
        localdepositor.click();
        List<WebElement> ops = driver.findElements(By.xpath("//div[contains(@class,'ant-select-item-option-content')]"));
        String localdep = this.selectRandomValueFromDropDownList(ops);
        GlobalMethods.printDebugInfo("Set Local Depositor to: " + localdep);
    }

    public void setTaxID (String taxid) {
        WebElement input_taxid = this.findVisibleElemntByXpath("//input[@id='form_item_personal_id']");
        this.moveElementToVisible(input_taxid);
        input_taxid.sendKeys(taxid);
        GlobalMethods.printDebugInfo("Set Personal Tax ID to: " + taxid);
    }

}
