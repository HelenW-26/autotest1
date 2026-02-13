package newcrm.pages.adminpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AdminRebateAccountPage extends Page {

    public AdminRebateAccountPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getContactEmailSearchInputEle() {
        return assertElementExists(By.xpath("//th[@data-field='email_sub'] //input"), "Contact Email Search Input");
    }

    protected WebElement getSearchButtonEle() {
        return assertElementExists(By.xpath("//button[@id='query']"), "Search Button");
    }

    protected WebElement getCommissionRulesButtonEle() {
        return assertElementExists(By.xpath("//div[@class='fixed-table-body']/table/tbody/tr[1] //a[@class='commission']"), "First Row - Commission Rules");
    }

    protected List<WebElement> getCommissionRulesIBGroupsEle() {
        return assertElementsExists(By.xpath("//table[@class='table table-bordered']/tbody/tr[@onclick]"), "Commission Rules - IB Groups");
    }

    protected WebElement getProductTypeSelectionEle() {
        return assertElementExists(By.xpath("//div[@class='btn-wrapper flex'] //button[@class='ms-choice']"), "Commission Rules - Product Type Selection Dropdown");
    }

    protected WebElement getProductTypeSelectAllEle() {
        return assertElementExists(By.xpath("//div[@class='btn-wrapper flex'] //div[@class='ms-drop bottom'] //li[@class='ms-select-all']/label/input"), "Commission Rules - Product Type - Select All");
    }

    protected List<WebElement> getProductTypeListEle() {
        return assertElementsExists(By.xpath("//div[@class='btn-wrapper flex'] //div[@class='ms-drop bottom'] //li[not(@class='ms-select-all')]/label/span"), "Commission Rules - Product Type - List");
    }

    protected List<WebElement> getCommissionAmountInputEle() {
        return assertElementsExists(By.xpath("//table[@id='commissionTable']/tbody/tr/td[@style='display: table-cell;']/input"), "Commission Rules - Inputs");
    }

    protected WebElement getSelectedProductTypeCheckboxEle() {
        return assertElementExists(By.xpath("//table[@id='commissionTable'] //th[@style='display: table-cell;']/input[@type='checkbox']"), "Commission Rules - Selected Product Type - Checkbox");
    }

    protected WebElement getSubmitCommissionRulesButtonEle() {
        return assertElementExists(By.xpath("//button[@id='CheckSubmit4']"), "Commission Rules - Submit");
    }

    protected WebElement getConfirmCommissionRulesButtonEle() {
        return assertElementExists(By.xpath("//div[@class='panel panel-default'] //button[@class='btn btn-primary']"), "Commission Rules - Confirm Submit");
    }

    protected WebElement getCloseCommissionRulesPanelButtonEle() {
        return assertElementExists(By.xpath("//i[@class='glyphicon glyphicon-remove-circle icon-remove-circle']"), "Commission Rules - Close Panel");
    }

    protected WebElement getFirstRowEmailUnhideEle() {
        return assertElementExists(By.xpath("//tbody/tr[1] //a[@class='checkEmail']"), "First Result Email: Click To Show");
    }

    protected WebElement getFirstRowEmailEle() {
        return assertElementExists(By.xpath("//tbody/tr[1]//a[@class='checkEmail']/parent::div/following-sibling::div[1]/a[1]"), "First Result Email");
    }

    public void searchByEmail(String email) throws InterruptedException {
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//th[@data-field='email_sub'] //input")));
        fastwait.until(ExpectedConditions.elementToBeClickable(By.xpath("//th[@data-field='email_sub'] //input")));
        fastwait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[@data-field='email_sub'] //input")));
        Thread.sleep(500);
        this.setInputValue(this.getContactEmailSearchInputEle(), email);
        Thread.sleep(500);
        triggerElementClickEvent_withoutMoveElement(getSearchButtonEle());
        fastwait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'loading table') and not(contains(@style, 'none'))]")));
        Thread.sleep(500);

        triggerClickEvent_withoutMoveElement(getFirstRowEmailUnhideEle());
        String resultEmail = getFirstRowEmailEle().getText();

        //If search criteria not passed first time, search again
        if(!email.equalsIgnoreCase(resultEmail)){
            this.setInputValue(this.getContactEmailSearchInputEle(), email);
            Thread.sleep(500);
            triggerElementClickEvent_withoutMoveElement(getSearchButtonEle());
            fastwait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'loading table') and not(contains(@style, 'none'))]")));
            Thread.sleep(500);
        }
    }

    protected List<WebElement> getRebateAccountEle() {
        return assertElementsExists(By.xpath("//div[@class='fixed-table-body']/table/tbody //a[contains(@onclick, 'accountRebate')]"), "Rebate Account List");
    }

    public List<String> rebateAccountList() throws InterruptedException {
        Thread.sleep(1500);
        List<String> rebateAcc = new ArrayList<>();
        for (WebElement element : getRebateAccountEle()) {
            rebateAcc.add(element.getText().trim());
        }
        return rebateAcc;
    }

    public void configureIBCommissionRules(){
        triggerClickEvent(getCommissionRulesButtonEle());
        String ibGroup = selectRandomDropDownOption_ElementClickEvent(getCommissionRulesIBGroupsEle());
        ibGroup = ibGroup.replace("●","").trim();

        GlobalMethods.printDebugInfo("IB Group Selected: " + ibGroup);

        By loadingImage = By.id("AjaxLoading");
        fastwait.until(ExpectedConditions.invisibilityOfElementLocated(loadingImage));

        triggerClickEvent(getProductTypeSelectionEle());
        triggerClickEvent_withoutMoveElement(getProductTypeSelectAllEle());
        triggerClickEvent_withoutMoveElement(getProductTypeSelectAllEle());
        String productType = selectRandomDropDownOption_ElementClickEvent(getProductTypeListEle());
        triggerClickEvent(getProductTypeSelectionEle());

        GlobalMethods.printDebugInfo("Product Type Selected: " + productType);

        //Distribute 100 across all levels (Total should be = 100)

        List<Integer> amountToInputList = new ArrayList<>(getCommissionAmountInputEle().size());
        int total = 100;
        int size = getCommissionAmountInputEle().size();
        Random random = new Random();

        // Generate random amounts ensuring the sum is 100
        for (int i = 0; i < size; i++) {
            int remainingSlots = size - i;
            int maxForThisSlot = total - (remainingSlots - 1);
            int amountToInput = (remainingSlots == 1) ? total : random.nextInt(maxForThisSlot) + 1;
            total -= amountToInput;
            // Clear and input the value
            getCommissionAmountInputEle().get(i).clear();
            getCommissionAmountInputEle().get(i).sendKeys(String.valueOf(amountToInput));
            amountToInputList.add(amountToInput);
        }

        triggerElementClickEvent_withoutMoveElement(getSelectedProductTypeCheckboxEle());
        triggerElementClickEvent_withoutMoveElement(getSubmitCommissionRulesButtonEle());
        triggerElementClickEvent_withoutMoveElement(getConfirmCommissionRulesButtonEle());
        fastwait.until(ExpectedConditions.invisibilityOfElementLocated(loadingImage));

        triggerElementClickEvent_withoutMoveElement(getCloseCommissionRulesPanelButtonEle());
        fastwait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'loading table') and not(contains(@style, 'none'))]")));

        //Verify Configured Commission Rules were saved
        triggerClickEvent(getCommissionRulesButtonEle());
        WebElement selectedIBGroupEle = assertElementExists(By.xpath("//table[@class='table table-bordered']/tbody/tr[starts-with(@onclick, \"onClickBiGroup('" + ibGroup + "\")]"),"IB Group Selected");
        triggerElementClickEvent_withoutMoveElement(selectedIBGroupEle);
        fastwait.until(ExpectedConditions.invisibilityOfElementLocated(loadingImage));

        triggerClickEvent(getProductTypeSelectionEle());
        triggerClickEvent_withoutMoveElement(getProductTypeSelectAllEle());
        triggerClickEvent_withoutMoveElement(getProductTypeSelectAllEle());

        //CFD's Asia will cause problem with xpath, so replace with "Asia", still sufficient
        if(productType.contains("Asia")){
            productType = "Asia";
        }

        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='btn-wrapper flex'] //div[@class='ms-drop bottom'] //li[not(@class='ms-select-all')]/label/span[contains(text(),'" + productType + "')]")));
        WebElement selectedProdTypeEle = assertElementExists(By.xpath("//div[@class='btn-wrapper flex'] //div[@class='ms-drop bottom'] //li[not(@class='ms-select-all')]/label/span[contains(text(),'" + productType + "')]"), "Product Type Selected");
        triggerElementClickEvent(selectedProdTypeEle);
        triggerClickEvent(getProductTypeSelectionEle());

        for (int i = 0; i < getCommissionAmountInputEle().size(); i++) {
            getCommissionAmountInputEle().get(i).getAttribute("value").equals(amountToInputList.get(i));
        }
        GlobalMethods.printDebugInfo("Commission Rules Saved as below:");
        amountToInputList.forEach(System.out::println);
    }

}
