package newcrm.pages.owspages;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import tools.ScreenshotHelper;
import utils.LogUtils;

import java.util.List;

public class OWSKYCRecordsPage extends Page {

    public OWSKYCRecordsPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getUserIdInputEle() {
        return assertElementExists(By.xpath("//input[@id='business_search_userId']"), "User ID Input");
    }

    protected WebElement getClientNameInputEle() {
        return assertElementExists(By.xpath("//input[@id='business_search_clientName']"), "Client Name Input");
    }

    protected WebElement getEmailInputEle() {
        return assertElementExists(By.xpath("//input[@id='business_search_email']"), "Email Input");
    }

    protected WebElement getExpandSearchEle(){
        return assertElementExists(By.xpath("//div[@class='businessTable_search'] //button[contains(@class,'ant-btn-icon-end')]"),"Expand Search Criteria Arrow");
    }

    protected WebElement getSearchButtonEle() {
        return assertElementExists(By.xpath("//div[@class='ant-form-item-control-input'] //button[contains(@class,'ant-btn-color-primary')]"), "Search Button");
    }

    protected WebElement getTypeFilterEle() {
        return assertElementExists(By.xpath("//span[text()='Type']/parent::div/following-sibling::div/button"), "Type Filter");
    }

    protected WebElement getPOATypeFilterEle() {
        return assertElementExists(By.xpath("//span[text()='POA']"), "Type Filter - POA");
    }

    protected WebElement getPOITypeFilterEle() {
        return assertElementExists(By.xpath("//span[text()='POI']"), "Type Filter - POI");
    }

    protected WebElement getAccountAuditTypeFilterEle() {
        return assertElementExists(By.xpath("//span[text()='Account Audit']"), "Type Filter - Account Audit");
    }

    protected WebElement getFirstRowStatusEle() {
        return assertElementExists(By.xpath("//tbody/tr[contains(@class,'table-row')][1]/td[@class='ant-table-cell'][2]"), "First Row - Status");
    }

    protected WebElement getFirstRowRegulatorEle() {
        return assertElementExists(By.xpath("//tbody/tr[contains(@class,'table-row')][1]/td[@class='ant-table-cell ant-table-cell-ellipsis'][3]"), "First Row - Regulator");
    }

    protected WebElement getFirstRowAuditButtonEle() {
        return assertElementExists(By.xpath("//tbody/tr[contains(@class,'table-row')][1] //*[@*[local-name()='href']='#icon-Audit1']/parent::*"), "First Row - Audit Button");
    }

    // region [ POI Panel Elements ]
    protected WebElement getPOIFirstNameInputEle() {
        return assertElementExists(By.xpath("(//div[@class='ant-drawer-body'] //label[@for='firstNameEn'])/parent::*/parent::* //input"), "POI Panel - First Name Input");
    }

    protected WebElement getPOILastNameInputEle() {
        return assertElementExists(By.xpath("(//div[@class='ant-drawer-body'] //label[@for='lastNameEn'])/parent::*/parent::* //input"), "POI Panel - Last Name Input");
    }

    protected WebElement getNationalityValueEle() {
        return assertElementExists(By.xpath("(//div[@class='ant-drawer-body'] //label[@for='nationalityId'])/parent::*/parent::* //span[@class='ant-select-selection-item']"), "POI Panel - Nationality Value");
    }

    protected WebElement getNationalityInputEle() {
        return assertElementExists(By.xpath("(//div[@class='ant-drawer-body'] //label[@for='nationalityId'])/parent::*/parent::* //input"), "POI Panel - Nationality Input");
    }

    protected WebElement getIdentityTypeInputEle() {
        return assertElementExists(By.xpath("(//div[@class='ant-drawer-body'] //label[@for='userIdType'])/parent::*/parent::* //input"), "POI Panel - Identity Type Input");
    }

    protected WebElement getAMLCheckTrueRadioButtonEle() {
        return assertElementExists(By.xpath("//div[@class='ant-drawer-body'] //input[@value='true']"), "POI Panel - AML Status: True Radio Button");
    }

    protected WebElement getAMLCheckTrueValueEle() {
        return assertElementExists(By.xpath("//div[@class='ant-drawer-body']//input[@value='true']/parent::*"), "POI Panel - AML Status: True Radio Value");
    }

    protected WebElement getAMLFailedReasonInputEle() {
        return assertElementExists(By.xpath("//label[@for='amlReason']/parent::*/parent::*//input"), "POI Panel - AML Failed Reason Input");
    }

    protected List<WebElement> getAMLFailedReasonOptionsListEle() {
        return assertElementsExists(By.xpath("//div[not(contains(@class,'hidden'))]/div/div/div/div/div/div/div[@class='ant-select-item-option-content']"), "POI Panel - AML Failed Reason Options List");
    }

    // endregion [ POI Panel Elements ]

    // region [ POA Panel Elements ]
    protected WebElement getPOACityInputEle() {
        return assertElementExists(By.xpath("(//div[@class='ant-drawer-body'] //label[@for='citySuburb'])/parent::*/parent::* //input"), "POA Panel - City Input");
    }

    protected WebElement getPOAAddressInputEle() {
        return assertElementExists(By.xpath("(//div[@class='ant-drawer-body'] //label[@for='address'])/parent::*/parent::* //input"), "POA Panel - Address Input");
    }


    // endregion [ POA Panel Elements ]

    // region [ Account Group Panel Elements ]
    protected WebElement getAccountTypeInputEle() {
        return assertElementExists(By.xpath("//label[@for='accountType']/parent::*/parent::* //input"), "Account Audit Panel - Leverage Input");
    }

    protected List<WebElement> getAccountTypeOptionsListEle() {
        return assertElementsExists(By.xpath("//div[not(contains(@class,'hidden'))]/div/div/div/div/div/div/div[@class='ant-select-item-option-content']"), "Account Audit Panel - Account Group Setting Options List");
    }

    protected WebElement getAccountTypeValueEle() {
        return assertElementExists(By.xpath("//label[@for='accountType']/parent::*/parent::* //span[@class='ant-select-selection-wrap']/span[not(contains(@class,'ant-select-selection-search'))]"), "Account Audit Panel - Leverage Current Value");
    }

    protected WebElement getServerInputEle() {
        return assertElementExists(By.xpath("//label[@for='server']/parent::*/parent::*/div[2] //input"), "Account Audit Panel - Server Input");
    }

    protected List<WebElement> getServerOptionsListEle() {
        return assertElementsExists(By.xpath("//div[not(contains(@class,'hidden'))]/div/div/div/div/div/div/div[@class='ant-select-item-option-content']"), "Account Audit Panel - Server Options List");
    }

    protected WebElement getAccountGroupSettingInputEle() {
        return assertElementExists(By.xpath("//label[@for='accountGroup']/parent::*/parent::* //input"), "Account Audit Panel - Account Group Setting Input");
    }

    protected List<WebElement> getAccountGroupSettingOptionsListEle() {
        return assertElementsExists(By.xpath("//div[not(contains(@class,'hidden'))]/div/div/div/div/div/div/div[@class='ant-select-item-option-content']"), "Account Audit Panel - Account Group Setting Options List");
    }

    protected WebElement getLeverageInputEle() {
        return assertElementExists(By.xpath("//label[@for='leverage']/parent::*/parent::* //input"), "Account Audit Panel - Leverage Input");
    }

    protected WebElement getLeverageValueEle() {
        return assertElementExists(By.xpath("//label[@for='leverage']/parent::*/parent::* //span[@class='ant-select-selection-wrap']/span[not(contains(@class,'ant-select-selection-search'))]"), "Account Audit Panel - Leverage Current Value");
    }

    protected List<WebElement> getLeverageOptionsListEle() {
        return assertElementsExists(By.xpath("//div[not(contains(@class,'hidden'))]/div/div/div/div/div/div/div[@class='ant-select-item-option-content']"), "Account Audit Panel - Leverage Options List");
    }

    // endregion [ Account Group Panel Elements ]

    // region [ POI/POA/Account Audit Panel Shared Elements ]

    //Complete button - Can be used for both POI, POA & Account Audit panels
    protected WebElement getCompleteButtonEle() {
        return assertElementExists(By.xpath("//div[@class='ant-drawer-body'] //div[@class='ant-drawer-footer'] //button[contains(@class,'ant-btn-variant-solid')]"), "POI/POA/Account Audit Panel - Complete Button");
    }

    // endregion [ POI/POA/Account Audit Panel Shared Elements ]



    //Pass one of the three parameters will be sufficient
    public void searchKYCRecords(String userId,String clientName, String email){
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='business_search_userId']")));
        setInputValue(getUserIdInputEle(),userId);
        setInputValue(getClientNameInputEle(),clientName);
        if(driver.findElements(By.xpath("//input[@id='business_search_email']")).isEmpty()){
            triggerElementClickEvent_withoutMoveElement(getExpandSearchEle());
        }
        setInputValue(getEmailInputEle(),email);
        triggerElementClickEvent_withoutMoveElement(getSearchButtonEle());
    }

    public boolean auditTradingAccount(){
        triggerElementClickEvent_withoutMoveElement(getTypeFilterEle());
        triggerElementClickEvent_withoutMoveElement(getAccountAuditTypeFilterEle());
        triggerElementClickEvent_withoutMoveElement(getSearchButtonEle());

        if(driver.findElements(By.xpath("//tbody/tr[contains(@class,'table-row')][1]")).isEmpty()){
            ScreenshotHelper.takeFullPageScreenshot(getDriver(), "screenshots", "auditTradingAccount");
            Assert.fail("Pending Audit Trading Account record not found, unable to proceed with Trading Account audit.");
        }

        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tbody/tr[contains(@class,'table-row')][1]")));
        if(getFirstRowStatusEle().getText().equalsIgnoreCase("completed")){
            LogUtils.info("Trading Account has already been audited, no audit is needed.");
            ScreenshotHelper.takeFullPageScreenshot(getDriver(), "screenshots", "auditPOI");
            return true;
        }

        triggerElementClickEvent_withoutMoveElement(getFirstRowAuditButtonEle());

        try{
        fastwait.until(ExpectedConditions.elementToBeClickable(getServerInputEle()));
        triggerElementClickEvent(getServerInputEle());
        } catch (ElementClickInterceptedException e) {
            Assert.fail("Timeout waiting for Server Input");
        }

        //Select MT5 server
        WebElement mt5Element = null;
        for (WebElement element : getServerOptionsListEle()) {
            if ("MT5".equals(element.getText().trim())) {
                mt5Element = element;
                break;
            }
        }
        triggerElementClickEvent(mt5Element);

        fastwait.until(ExpectedConditions.elementToBeClickable(getAccountGroupSettingInputEle()));
        triggerElementClickEvent(getAccountGroupSettingInputEle());
        triggerElementClickEvent(getAccountGroupSettingOptionsListEle().get(0));

        fastwait.until(ExpectedConditions.elementToBeClickable(getLeverageInputEle()));

        String currentLeverage = getLeverageValueEle().getText().trim();

        if(currentLeverage.equalsIgnoreCase("") || currentLeverage==null){
            triggerElementClickEvent(getLeverageInputEle());
            selectRandomDropDownOption_ElementClickEvent(getLeverageOptionsListEle());
//            triggerElementClickEvent(getLeverageOptionsListEle().get(0));
        }

        //If Account Type got unselected
        String currentAccountType = getAccountTypeValueEle().getText().trim();

        if(currentAccountType.equalsIgnoreCase("") || currentAccountType==null || currentAccountType.equalsIgnoreCase("please select")){
            triggerElementClickEvent(getAccountTypeInputEle());
            selectRandomDropDownOption_ElementClickEvent(getAccountTypeOptionsListEle());
//            triggerElementClickEvent(getLeverageOptionsListEle().get(0));
        }

        triggerClickEvent_withoutMoveElement(getCompleteButtonEle());
        ScreenshotHelper.takeFullPageScreenshot(getDriver(), "screenshots", "auditTradingAccount");
        fastwait.until(ExpectedConditions.elementToBeClickable(getSearchButtonEle()));

        LogUtils.info("Successfully Audited Trading Account");
        return true;
    }

    public boolean auditPOI() {
        triggerElementClickEvent_withoutMoveElement(getTypeFilterEle());
        triggerElementClickEvent_withoutMoveElement(getPOITypeFilterEle());
        triggerElementClickEvent_withoutMoveElement(getSearchButtonEle());

        if(driver.findElements(By.xpath("//tbody/tr[contains(@class,'table-row')][1]")).isEmpty()){
            ScreenshotHelper.takeFullPageScreenshot(getDriver(), "screenshots", "auditPOI");
            Assert.fail("No POI record is found, unable to proceed with POI audit.");
        }

        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tbody/tr[contains(@class,'table-row')][1]")));
        if(getFirstRowStatusEle().getText().equalsIgnoreCase("completed")){
            LogUtils.info("POI has already been audited, no audit is needed.");
            ScreenshotHelper.takeFullPageScreenshot(getDriver(), "screenshots", "auditPOI");
            return true;
        }

        triggerElementClickEvent_withoutMoveElement(getFirstRowAuditButtonEle());

        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//div[@class='ant-drawer-body'] //label[@for='firstNameEn'])/parent::*/parent::* //input")));

        if (!driver.findElements(By.xpath("//div[@class='ant-drawer-body']//input[@value='true']/parent::*")).isEmpty()){
            if (!getAMLCheckTrueValueEle().getAttribute("class").contains("disabled")) {
                triggerElementClickEvent_withoutMoveElement(getAMLCheckTrueRadioButtonEle());
                triggerElementClickEvent_withoutMoveElement(getAMLFailedReasonInputEle());
                selectRandomDropDownOption_ElementClickEvent(getAMLFailedReasonOptionsListEle());
            }
        }

        triggerClickEvent_withoutMoveElement(getCompleteButtonEle());
        ScreenshotHelper.takeFullPageScreenshot(getDriver(), "screenshots", "auditPOI");
        fastwait.until(ExpectedConditions.elementToBeClickable(getSearchButtonEle()));
        LogUtils.info("Successfully Audited POI");
        return true;
    }

    public void auditPOA() {
        triggerElementClickEvent_withoutMoveElement(getTypeFilterEle());
        triggerElementClickEvent_withoutMoveElement(getPOATypeFilterEle());
        triggerElementClickEvent_withoutMoveElement(getSearchButtonEle());

        if(driver.findElements(By.xpath("//tbody/tr[contains(@class,'table-row')][1]")).isEmpty()){
            ScreenshotHelper.takeFullPageScreenshot(getDriver(), "screenshots", "auditPOA");
            Assert.fail("No POA record is found, unable to proceed with POA audit.");
        }

        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tbody/tr[contains(@class,'table-row')][1]")));
//        js.executeScript("arguments[0].scrollLeft = arguments[0].scrollWidth;", driver.findElement(By.xpath("//div[@class='ant-table-content']")));
//        if(getFirstRowStatusEle().getText().equalsIgnoreCase("completed")){
//            LogUtils.info("POA has already been audited, no audit is needed.");
//            ScreenshotHelper.takeFullPageScreenshot(getDriver(), "screenshots", "auditPOI");
//            return;
//        }

        triggerElementClickEvent_withoutMoveElement(getFirstRowAuditButtonEle());

        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//div[@class='ant-drawer-body'] //label[@for='citySuburb'])/parent::*/parent::* //input")));
        String currentCity = getPOACityInputEle().getAttribute("value").trim();
        if(currentCity.equalsIgnoreCase("") || currentCity==null){
            String city = "autotestcity" + GlobalMethods.getRandomString(10);
            setInputValue(getPOACityInputEle(), city);
        }

        String currentAddress = getPOAAddressInputEle().getAttribute("value").trim();
        if(currentAddress.equalsIgnoreCase("") || currentCity==null){
            String address = "autotestaddress" + GlobalMethods.getRandomString(10);
            setInputValue(getPOAAddressInputEle(), address);
        }

        triggerClickEvent_withoutMoveElement(getCompleteButtonEle());
        ScreenshotHelper.takeFullPageScreenshot(getDriver(), "screenshots", "auditPOA");
        fastwait.until(ExpectedConditions.elementToBeClickable(getSearchButtonEle()));
        LogUtils.info("Successfully Audited POA");
    }


    public String getRegulator(){
        return getFirstRowRegulatorEle().getText();
    }









}
