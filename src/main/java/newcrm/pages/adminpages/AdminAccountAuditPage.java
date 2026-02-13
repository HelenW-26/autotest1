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
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class AdminAccountAuditPage extends Page {

    protected List<String> serversId;

    public AdminAccountAuditPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getSearchOptionsDropdownEle() {
        return assertElementExists(By.xpath("//button[@id='searchType']"), "Account Audit - Search Options Dropdown");
    }

    protected WebElement getIBRoleDropdownEle(){
        return assertElementExists(By.xpath("//select[@id='ib_level']"),"IB Role Dropdown");
    }

    protected WebElement getAccountTypeDropdownEle(){
        return assertElementExists(By.xpath("//div[@id='account_type_chosen']"),"Account Type Dropdown");
    }

    protected List<WebElement> getSelectAccountTypeDropdownListEle() {
        return assertElementsExists(By.xpath("//select[contains(@id,'account_type')]/option[not(text()='Please Select')]"), "Account Audit - Account Type List");
    }

    protected WebElement getEmailSearchOptionEle() {
        return assertElementExists(By.xpath("(//button[@id='searchType']/following-sibling::*)/li/a[@data-id='3']"), "Account Audit - Search Option = Email");
    }

    protected WebElement getSearchInputEle() {
        return assertElementExists(By.xpath("//input[@id='userQuery']"), "Account Audit - Search Input");
    }

    protected WebElement getSearchButtonEle() {
        return assertElementExists(By.xpath("//button[@id='query']"), "Account Audit - Search Button");
    }

    protected WebElement getFirstResultUserIDEle() {
        return assertElementExists(By.xpath("//div[@class='fixed-table-body'] //tbody/tr[1]/td[1]"), "Account Audit - First Search Result User ID");
    }

    protected WebElement getRebateAccountOperationButtonEle() {
        return assertElementExists(By.xpath("//td[text()='Rebate Account']/following-sibling::td/a[@class='update']"), "Account Audit - Operation Button");
    }

    protected WebElement getTradingAccountOperationButtonEle() {
        return assertElementExists(By.xpath("//td[text()='Trading Account']/following-sibling::td/a[@class='update']"), "Account Audit - Operation Button");
    }

    protected WebElement getIncorrectCurrencyGroupMessageEle() {
        return assertElementExists(By.xpath("//div[@class='messenger-message-inner']"), "Incorrect Currency Group Message",e -> !e.getText().toLowerCase().contains("currencies"));
    }

    protected WebElement getSelectedAccountTypeEle() {
        return assertElementExists(By.xpath("//select[contains(@id,'account_type')]/option[@selected]"), "Account Audit - Selected Account Type");
    }

    protected List<WebElement> getIBServerDropdownListEle() {
        return assertElementsExists(By.xpath("//select[@id='ib_server']/option"), "Account Audit - IB Server Dropdown List",e -> !e.getText().toLowerCase().contains("please select"));
    }

    //Rebate Group for IB Account | Account Group for Trading Account
    protected WebElement getGroupDropdownEle() {
        return assertElementExists(By.xpath("//div[@id='ib_mt4_set_chosen']"), "Account Audit - Rebate Groups Dropdown Activation");
    }

    //Rebate Group for IB Account
    protected List<WebElement> getRebateGroupDropdownListEle() {
//        return assertElementsExists(By.xpath("//div[@id='ib_mt4_set_chosen']//ul/li[not(contains(@class, 'result-selected'))]"), "Rebate Account Audit - Rebate Groups Dropdown List");
        return assertElementsExists(By.xpath("//div[@id='ib_mt4_set_chosen']//ul/li[not(text()='Please Select')]"), "Rebate Account Audit - Rebate Groups Dropdown List");
    }

    //Account Group for Trading Account
    protected List<WebElement> getAccountGroupDropdownListEle() {
        return assertElementsExists(By.xpath("//div[@id='ib_mt4_set_chosen']//ul/li[not(text()='Please Select')]"), "Trading Account Audit - Account Groups Dropdown List");
//        return assertElementsExists(By.xpath("//div[@id='ib_mt4_set_chosen']//ul/li[starts-with(text(), 'TEST_') and substring(text(), string-length(text()) - 3) = '_USD']"), "Trading Account Audit - Account Groups Dropdown List (Options that start with 'TEST_' & end with '_USD'");
    }

    protected WebElement getIBGroupDropdownEle() {
        return assertElementExists(By.xpath("//input[@id='ib_IB_Grounp']"), "Account Audit - IB Groups Dropdown Activation");
    }

    protected List<WebElement> getIBGroupDropdownListEle() {
        return assertElementsExists(By.xpath("//div[@class='list row' and contains(@style, 'display: block')]/ul//input[@type='checkbox' and not(contains(@class, 'selectAll'))]"), "Account Audit - IB Groups Dropdown List");
    }

    protected WebElement getFirstIBGroupDropdownListEle() {
        return assertElementExists(By.xpath("(//div[@class='list row' and contains(@style, 'display: block')]/ul//input[@type='checkbox' and not(contains(@class, 'selectAll'))])[1]"), "Account Audit - First Option - IB Groups Dropdown List");
    }

    protected WebElement getLeverageDropdownEle() {
        return assertElementExists(By.xpath("//select[contains(@id,'ib_Leverage')]"), "Account Audit - Leverage Dropdown Activation");
    }

    protected List<WebElement> getLeverageDropdownListEle() {
        return assertElementsExists(By.xpath("//select[contains(@id,'ib_Leverage')]/option[not(text()='Please Select')]"), "Account Audit - Leverage List");
    }

    protected WebElement getConfirmButtonEle() {
        return assertElementExists(By.xpath("//div[@class='modal-footer'] //button[@class='btn btn-default'][1]"), "Account Audit - Confirm");
    }

    public void searchByEmail(String email){
        //select search by Email
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@id='searchType']")));
        triggerElementClickEvent_withoutMoveElement(getSearchOptionsDropdownEle());
        triggerElementClickEvent_withoutMoveElement(getEmailSearchOptionEle());

        //input email and search
        this.setInputValue(getSearchInputEle(), email);
        triggerElementClickEvent_withoutMoveElement(getSearchButtonEle());
        fastwait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'loading table') and not(contains(@style, 'none'))]")));

        if (!getFirstResultUserIDEle().getText().isEmpty()) {
            GlobalMethods.printDebugInfo("Account Audit Search: Able to find user " + email + " with User ID: " + getFirstResultUserIDEle().getText());
        }else{
            Assert.fail("Account Audit Search: UNABLE to find user with email: " + email);
        }
    }

    public void waitForAccountAuditPageToLoad(){
        fastwait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='IB Campaign Source' or @class='fixed-table-container']")));
    }

    public void auditIBAccount(GlobalProperties.BRAND brand){
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[text()='Rebate Account']")));

        if(!driver.findElements(By.xpath("//td[text()='Rebate Account']/following-sibling::td[text()='Completed']")).isEmpty()){
            return;
        }
        triggerElementClickEvent_withoutMoveElement(getRebateAccountOperationButtonEle());

        //Select IB Role (level) until no error message prompt
        Select ibRole =  new Select(getIBRoleDropdownEle());
        int optionCount = ibRole.getOptions().size();
        for (int i = 1; i < optionCount; i++) {
            ibRole.selectByIndex(i);
            if(driver.findElements(By.xpath("//li[@class='messenger-message-slot messenger-shown messenger-first messenger-last']")).isEmpty()){
                break;
            }
            fastwait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//li[@class='messenger-message-slot messenger-shown messenger-first messenger-last']")));

        }

        String accountType = selectAccountTypeIB();

        //select server based on account type and brand
//        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//select[@id='mt4_account_type']/option[@selected='selected']")));
//        if (accountType.toLowerCase().contains("mt5") || accountType.toLowerCase().contains("hedge")){
            serversId = AlphaServerEnv.getMT5TestServerIdByBrand(brand);
//        }else{
//            serversId = AlphaServerEnv.getMT4TestServerIdByBrand(brand);
//        }
        String serverID = serversId.get(0);
        WebElement e = null;
        for (WebElement element : getIBServerDropdownListEle()) {
            if (element.getAttribute("value").equals(serverID)) {
                e = element;
            }
        }
        triggerElementClickEvent_withoutMoveElement(e);
        GlobalMethods.printDebugInfo("IB Server: " + e.getText());

        fastwait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='ib_mt4_set_chosen']/a/span[contains(text(), 'Loading')]")));

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

//        //click IB Group dropdown again if the list still not displayed
//        if(!getFirstIBGroupDropdownListEle().getText().contains(currency)){
//            triggerElementClickEvent(getIBGroupDropdownEle());
//        }
        //click IB Group dropdown again if the list still not displayed
        while(driver.findElements(By.xpath("(//div[@class='list row' and contains(@style, 'display: block')]/ul//input[@type='checkbox' and not(contains(@class, 'selectAll'))])[1]")).isEmpty()){
            triggerElementClickEvent(getIBGroupDropdownEle());
        }
        if(!getFirstIBGroupDropdownListEle().getText().contains(currency)){
            triggerElementClickEvent(getIBGroupDropdownEle());
        }
        //Click first option
        triggerElementClickEvent_withoutMoveElement(getFirstIBGroupDropdownListEle());
//        selectRandomDropDownOption_ElementClickEvent(getIBGroupDropdownListEle());

        //click confirm and verify status changed to Completed
        triggerElementClickEvent(getConfirmButtonEle());

        //If IB Group currency not match, re-select IB Group
//        if(getIncorrectCurrencyGroupMessageEle().isDisplayed()){
//            triggerElementClickEvent(getIBGroupDropdownEle());
//            triggerElementClickEvent_withoutMoveElement(getIBGroupDropdownListEle().get(0));
//
//            triggerElementClickEvent(getConfirmButtonEle());
//        }

        By loadingImage = By.id("AjaxLoading");
        fastwait.until(ExpectedConditions.invisibilityOfElementLocated(loadingImage));
        fastwait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'loading table') and not(contains(@style, 'none'))]")));
        assertElementExists(By.xpath("//tbody/tr[1]/td[text()='Completed']"), "Main Account Audit Status: Completed");
        GlobalMethods.printDebugInfo("Main Account Audit Status: Completed");

    }

    public void auditTradingAccount(GlobalProperties.BRAND brand){

        //If Trading Account has already been Audited
        if (checkElementExists(By.xpath("//tbody/tr[1]/td[text()='Completed']"))!=null){
            return;
        }

        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[text()='Trading Account']/following-sibling::td/a[@class='update']")));
        triggerElementClickEvent_withoutMoveElement(getTradingAccountOperationButtonEle());

        String accountType = selectAccountTypeTrading();

        //select server based on account type and brand
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//select[contains(@id,'account_type')]/option[not(text()='Please Select')]")));
        if (accountType.toLowerCase().contains("mt5") || accountType.toLowerCase().contains("hedge")){
            serversId = AlphaServerEnv.getMT5TestServerIdByBrand(brand);
        }else{
            serversId = AlphaServerEnv.getMT4TestServerIdByBrand(brand);
        }
        String serverID = serversId.get(0);
        WebElement e = null;
        for (WebElement element : getIBServerDropdownListEle()) {
            if (element.getAttribute("value").equals(serverID)) {
                e = element;
            }
        }
        triggerElementClickEvent_withoutMoveElement(e);
        GlobalMethods.printDebugInfo("IB Server: " + e.getText());

        fastwait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='ib_mt4_set_chosen']/a/span[contains(text(), 'Loading')]")));

        //select random Account Group
        triggerElementClickEvent_withoutMoveElement(getGroupDropdownEle());
        String accountGroup = selectRandomDropDownOption_ElementClickEvent(getAccountGroupDropdownListEle());
        GlobalMethods.printDebugInfo("Account Group: " + accountGroup);

        //select random Leverage
        triggerElementClickEvent_withoutMoveElement(getLeverageDropdownEle());
        String leverage = selectRandomDropDownOption_ElementClickEvent(getLeverageDropdownListEle());
        LogUtils.info("Leverage: " + leverage);

        //click confirm and verify status changed to Completed
        triggerElementClickEvent(getConfirmButtonEle());
        By loadingImage = By.id("AjaxLoading");
        fastwait.until(ExpectedConditions.invisibilityOfElementLocated(loadingImage));
        fastwait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'loading table') and not(contains(@style, 'none'))]")));
        assertElementExists(By.xpath("//tbody/tr[1]/td[text()='Completed']"), "Main Account Audit Status: Completed");
        GlobalMethods.printDebugInfo("Main Account Audit Status: Completed");
    }

    public String selectAccountTypeIB(){
        List<WebElement> accountTypeDropdownOptions = new ArrayList<>(getSelectAccountTypeDropdownListEle());

        accountTypeDropdownOptions.removeIf(option -> {
            String text = option.getAttribute("innerText");
            return text != null &&
//                    (text.toLowerCase().contains("hedge") ||
//                            text.toLowerCase().contains("mts") ||
//                            text.toLowerCase().contains("institution type") ||
//                            text.toLowerCase().contains("cent") ||
//                            text.toLowerCase().contains("vip") ||
//                            text.toLowerCase().contains("swap")
//            )&&
                    !(text.toLowerCase().contains("mt5"));
//            !(text.toLowerCase().contains("Hedge STP"));


        });

        String accountType = selectRandomDropDownOption_ElementClickEvent(accountTypeDropdownOptions);
        GlobalMethods.printDebugInfo("Account Type: "+accountType);

        return accountType;
    }

    public String selectAccountTypeTrading(){
        List<WebElement> accountTypeDropdownOptions = new ArrayList<>(getSelectAccountTypeDropdownListEle());

        accountTypeDropdownOptions.removeIf(option -> {
            String text = option.getAttribute("innerText");
            return text != null &&
//                    (text.toLowerCase().contains("hedge") ||
//                            text.toLowerCase().contains("mts") ||
//                            text.toLowerCase().contains("institution type") ||
//                            text.toLowerCase().contains("cent") ||
//                            text.toLowerCase().contains("vip") ||
//                            text.toLowerCase().contains("swap")
//            )&&
                    !(text.toLowerCase().equalsIgnoreCase("hedge stp"));
//            !(text.toLowerCase().contains("Hedge STP"));

        });

        String accountType = selectRandomDropDownOption_ElementClickEvent(accountTypeDropdownOptions);
        GlobalMethods.printDebugInfo("Account Type: "+accountType);

        return accountType;
    }

}
