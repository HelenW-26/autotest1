package newcrm.pages.adminpages;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.Page;
import newcrm.utils.AlphaServerEnv;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.util.List;

public class AdminAdditionalAccountAuditPage extends AdminAccountAuditPage {

    protected List<String> serversId;

    public AdminAdditionalAccountAuditPage(WebDriver driver) {
        super(driver);
    }


    public void auditAdditionalAccountIB(String email, GlobalProperties.BRAND brand){
        //select search by Email
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@id='searchType']")));
        triggerElementClickEvent_withoutMoveElement(getSearchOptionsDropdownEle());
        triggerElementClickEvent_withoutMoveElement(getEmailSearchOptionEle());

        //input email and search
        this.setInputValue(getSearchInputEle(), email);
        triggerElementClickEvent_withoutMoveElement(getSearchButtonEle());
        fastwait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'loading table') and not(contains(@style, 'none'))]")));

        if (!getFirstResultUserIDEle().getText().isEmpty()) {
            GlobalMethods.printDebugInfo("Additional Account Audit Search: Able to find user " + email + " with User ID: " + getFirstResultUserIDEle().getText());
        }else{
            Assert.fail("Additional Account Audit Search: UNABLE to find user with email: " + email);
        }

        triggerElementClickEvent_withoutMoveElement(getRebateAccountOperationButtonEle());

        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//select[@id='mt4_account_type']")));
        String accountType = selectAccountTypeIB();

        //select server based on account type and brand
        if (accountType.toLowerCase().contains("mt5")){
            serversId = AlphaServerEnv.getMT5TestServerIdByBrand(brand);
        }else{
            serversId = AlphaServerEnv.getMT4TestServerIdByBrand(brand);
        }
        String serverID = serversId.get(0);
        WebElement d = null;
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//select[@id='ib_server']/option")));
        for (WebElement element : getIBServerDropdownListEle()) {
            if (element.getAttribute("value").equals(serverID)) {
                d = element;
            }
        }
        triggerElementClickEvent_withoutMoveElement(d);
        GlobalMethods.printDebugInfo("IB Server: " + d.getText());

        fastwait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='ib_mt4_set_chosen']/a/span[contains(text(), 'Loading')]")));

//        //select random Rebate Group
//        triggerElementClickEvent_withoutMoveElement(getGroupDropdownEle());
//        String rebateGroup = selectRandomDropDownOption_ElementClickEvent(getRebateGroupDropdownListEle());
//        GlobalMethods.printDebugInfo("Rebate Group: " + rebateGroup);

        //select random Rebate Group
        triggerElementClickEvent_withoutMoveElement(getGroupDropdownEle());
        String rebateGroup = null;
        try{
            rebateGroup = selectRandomDropDownOption_ElementClickEvent(getRebateGroupDropdownListEle());
        } catch (ElementNotInteractableException ex) {
            triggerElementClickEvent_withoutMoveElement(getGroupDropdownEle());
            rebateGroup = selectRandomDropDownOption_ElementClickEvent(getRebateGroupDropdownListEle());
        }
        GlobalMethods.printDebugInfo("Rebate Group: " + rebateGroup);

        String currency = rebateGroup.substring(rebateGroup.length() - 3);

        //select random IB Group
        fastwait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='ib_mt4_set_chosen']/a/span[contains(text(), 'Loading')]")));
        triggerElementClickEvent(getIBGroupDropdownEle());

        //click IB Group dropdown again if the list still not displayed
        while(driver.findElements(By.xpath("(//div[@class='list row' and contains(@style, 'display: block')]/ul//input[@type='checkbox' and not(contains(@class, 'selectAll'))])[1]")).isEmpty()){
            triggerElementClickEvent(getIBGroupDropdownEle());
        }
        //click IB Group dropdown again if the list still not displayed
        if(!getFirstIBGroupDropdownListEle().getText().contains(currency)){
            triggerElementClickEvent(getIBGroupDropdownEle());
        }

        if(!getFirstIBGroupDropdownListEle().getText().contains(currency)){
            triggerElementClickEvent(getIBGroupDropdownEle());
        }
        //Click first option
        triggerElementClickEvent_withoutMoveElement(getFirstIBGroupDropdownListEle());

        //click confirm and verify status changed to Completed
        triggerElementClickEvent(getConfirmButtonEle());
        By loadingImage = By.id("AjaxLoading");
        fastwait.until(ExpectedConditions.invisibilityOfElementLocated(loadingImage));
        fastwait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'loading table') and not(contains(@style, 'none'))]")));
        assertElementExists(By.xpath("//tbody/tr[1]/td"), "Additional Account Audit Status: Completed", e -> e.getText().toLowerCase().contains("completed"));
        GlobalMethods.printDebugInfo("Additional Account Audit Status: Completed");
    }

}
