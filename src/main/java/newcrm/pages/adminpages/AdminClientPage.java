package newcrm.pages.adminpages;

import newcrm.global.GlobalMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import vantagecrm.Utils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AdminClientPage extends AdminPage {

    public AdminClientPage(WebDriver driver) {
        super(driver);
    }

    protected List<WebElement> getSelectIdentificationTypeDropdownListEle() {
        return assertElementsExists(By.xpath("//select[@name='acc_id_type']/option"), "Add Client - Identification Type Dropdown List",e -> !e.getText().toLowerCase().contains("please select"));
    }

    protected WebElement getIdentificationNumberInputEle() {
        return assertElementExists(By.xpath("//input[@name='acc_id_num']"), "Add Client - Identification Number");
    }

    protected WebElement getContactEmailSearchInputEle() {
        return assertElementExists(By.xpath("//th[@data-field='email_sub'] //input"), "Contact Email Search Input");
    }

    protected WebElement getAffIDSearchInputEle() {
        return assertElementExists(By.xpath("//th[@data-field='affId'] //input"), "AFFID Search Input");
    }

    protected WebElement getUpgradeNewIBFlowButtonEle() {
        return assertElementExists(By.xpath("//tr[1]//a[@class='upgradeIbOnNewIbFlow']"), "New IB Flow: Upgrade IB");
    }

    protected WebElement getOperationUpgradeIBButtonEle() {
        return assertElementExists(By.xpath("//tr[1]//a[@title='Upgrade to IB']"), "Operation: (Upgrade to) IB");
    }

    protected WebElement getOKButtonEle() {
        return assertElementExists(By.xpath("//div[@class='bootstrap-dialog-footer'] //button[@class='btn btn-primary'][1]"), "Upgrade To IB - OK");
    }

    protected WebElement getUploadIdentityProofInputEle() {
        return assertElementExists(By.xpath("//input[@type='file' and @data-name='passport']"), "Identity Proof Upload Input");
    }

    protected WebElement getUploadIdentityProofButtonEle() {
        return assertElementExists(By.xpath("//input[@type='file' and @data-name='passport']/parent::*/preceding-sibling::a"), "Identity Proof 'Upload' Button");
    }

    protected WebElement getUploadAddressProofInputEle() {
        return assertElementExists(By.xpath("//input[@type='file' and @data-name='bankStatement']"), "Address Proof Upload Input");
    }

    protected WebElement getUploadAddressProofButtonEle() {
        return assertElementExists(By.xpath("//input[@type='file' and @data-name='bankStatement']/parent::*/preceding-sibling::a"), "Address Proof 'Upload' Button");
    }

    protected WebElement getSubmitButtonEle() {
        return assertElementExists(By.xpath("//button[@class='btn btn-primary pull-left popModalSub']"), "Submit Button");
    }

    protected WebElement getFirstRowClientNameEle() {
        return assertElementExists(By.xpath("//tbody/tr[1] //a[contains(@onclick, 'editCustomerInfo')]"), "First Result Client Name");
    }

    protected WebElement getFirstRowEmailUnhideEle() {
        return assertElementExists(By.xpath("//tbody/tr[1] //a[@class='checkEmail']"), "First Result Email: Click To Show");
    }

    protected WebElement getFirstRowEmailEle() {
        return assertElementExists(By.xpath("//tbody/tr[1]//a[@class='checkEmail']/parent::div/following-sibling::div[1]/a[1]"), "First Result Email");
    }

    public void upgradeToIB_NewIBFlow(){
        triggerElementClickEvent_withoutMoveElement(getUpgradeNewIBFlowButtonEle());
        triggerElementClickEvent_withoutMoveElement(getOKButtonEle());
        By loadingImage = By.id("AjaxLoading");
        fastwait.until(ExpectedConditions.invisibilityOfElementLocated(loadingImage));
        fastwait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'loading table') and not(contains(@style, 'none'))]")));
        GlobalMethods.printDebugInfo("Upgraded to IB through New IB Flow (Upgrade IB button)");
    }

    public void upgradeToIB_OperationFlow(){
        triggerElementClickEvent_withoutMoveElement(getOperationUpgradeIBButtonEle());
        triggerElementClickEvent_withoutMoveElement(getOKButtonEle());
        By loadingImage = By.id("AjaxLoading");
        fastwait.until(ExpectedConditions.invisibilityOfElementLocated(loadingImage));
        fastwait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'loading table') and not(contains(@style, 'none'))]")));
        GlobalMethods.printDebugInfo("Upgraded to IB through Operation Flow (Operation: IB button)");
    }

    public void searchByEmail(String email){
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//th[@data-field='email_sub'] //input")));
        this.setInputValue(this.getContactEmailSearchInputEle(), email);
        clickSearchBtn();
        fastwait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='fixed-table-loading table table-bordered table-hover']")));
    }

    public void searchByAffID(String ibAccountNumber){
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@id='addCustomer']")));
        this.setInputValue(this.getAffIDSearchInputEle(), ibAccountNumber);
        clickSearchBtn();
    }

    public void selectIdentificationType(){
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//select[@name='acc_id_type']/option")));
        String identificationType = selectRandomDropDownOption_ElementClickEvent(getSelectIdentificationTypeDropdownListEle());
        GlobalMethods.printDebugInfo("Identification Type: "+identificationType);
    }

    public void inputIdentificationNumber(){
        String idnum = "TESTID"+GlobalMethods.getRandomNumberString(10);
        this.setInputValue(this.getIdentificationNumberInputEle(), idnum);
        GlobalMethods.printDebugInfo("Identification Number: "+idnum);
    }

    public void clickFirstClientName(){
        triggerElementClickEvent_withoutMoveElement(getFirstRowClientNameEle());
    }

    public String searchFirstClientNameInList(List<String> ibAccountNumberList){
        //Go through all IB Account Number in List, and click the first Client it finds
        String email = null;

        for (String ibAccountNumber : ibAccountNumberList){
            searchByAffID(ibAccountNumber);
            fastwait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'loading table') and not(contains(@style, 'none'))]")));
            if(!driver.findElements(By.xpath("//tbody/tr[1] //a[contains(@onclick, 'editCustomerInfo')]")).isEmpty()) {
                triggerClickEvent_withoutMoveElement(getFirstRowEmailUnhideEle());
                email = getFirstRowEmailEle().getText();

                GlobalMethods.printDebugInfo("Email of Client: " + email);

                break;
            }
        }

        if(email==null){
            Assert.fail("No client found under IB accounts: " + ibAccountNumberList.toString() + ", unable to proceed with client upgrade to IB flow");
        }

        return email;
    }

    public Boolean checkFirstClientIBUpgradeEnabled_NewIBFlow(){
        Boolean ibEnabled = false;
         if(!driver.findElements(By.xpath("//tr[1]//a[@class='upgradeIbOnNewIbFlow']")).isEmpty()) {
             ibEnabled = true;
         }
        return ibEnabled;
    }

    public Boolean checkFirstClientIBUpgradeEnabled_OperationFlow(){
        Boolean ibEnabled = false;
        if(!driver.findElements(By.xpath("//tr[1]//a[@title='Upgrade to IB']")).isEmpty()) {
            ibEnabled = true;
        }
        return ibEnabled;
    }

    public Boolean checkFirstClientTradingAccountExist(){
        Boolean tradeAccountExist = false;
        if(!driver.findElements(By.xpath("//tr[1] //a[contains(@onclick, 'accountTradeHistory')]")).isEmpty()) {
            tradeAccountExist = true;
        }
        return tradeAccountExist;
    }

    public void inputIdentificationInfo(){
        selectIdentificationType();
        inputIdentificationNumber();
    }

    public void uploadPOIPOADoc(){
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='email']")));

        Path parent = Paths.get("src", "main", "resources", "vantagecrm", "doc");
        String fileID = Paths.get(parent.toString(), "ID_Card_Gen.jpg").toString();
        String fileAddress = Paths.get(parent.toString(), "ID_Card_Back_Gen.jpg").toString();

        if(driver.findElements(By.xpath("//div[contains(@class,'pass')] //i[@class='input-del input_del_btn glyphicon glyphicon-trash']")).isEmpty()) {

        getUploadIdentityProofInputEle().sendKeys(Paths.get(Utils.workingDir, fileID).toString());
        triggerElementClickEvent(getUploadIdentityProofButtonEle());
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'pass')] //i[@class='input-del input_del_btn glyphicon glyphicon-trash']")));
        }

        if(driver.findElements(By.xpath("//div[contains(@class,'bank')] //i[@class='input-del input_del_btn glyphicon glyphicon-trash']")).isEmpty()) {

            getUploadAddressProofInputEle().sendKeys(Paths.get(Utils.workingDir, fileAddress).toString());
            triggerElementClickEvent(getUploadAddressProofButtonEle());
            fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'bank')] //i[@class='input-del input_del_btn glyphicon glyphicon-trash']")));
        }
        triggerElementClickEvent_withoutMoveElement(getSubmitButtonEle());
    }

    public void waitLoadingPanelContent(String desc) {
        try {
            fastwait.until(driver -> {
                WebElement parent = assertElementExists(By.cssSelector("div.url-content"), desc + " Panel Content");
                List<WebElement> children = assertElementsExists(By.xpath("./*"), desc + " Panel Content", parent);
                return !children.isEmpty();
            });
        }
        catch(Exception e)
        {
            Assert.fail("Failed to load " + desc + " panel content");
        }
    }

}
