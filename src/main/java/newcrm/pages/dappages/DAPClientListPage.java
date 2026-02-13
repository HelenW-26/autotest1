package newcrm.pages.dappages;

import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import utils.LogUtils;

import java.util.List;

public class DAPClientListPage extends Page {

    public DAPClientListPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getClientValueEle() {
        return assertElementExists(By.xpath("(//span[@class='statistics__item-value-text'])[1]"), "Client Value");
    }

    protected WebElement getFTDValueEle() {
        return assertElementExists(By.xpath("(//span[@class='statistics__item-value-text'])[2]"), "FTD Value");
    }

    protected WebElement getQFTDValueEle() {
        return assertElementExists(By.xpath("(//span[@class='statistics__item-value-text'])[3]"), "QFTD Value");
    }

    protected WebElement getProjectedCommissionDollarEle() {
        return assertElementExists(By.xpath("(//span[@class='statistics__item-value-text'])[4]"), "Projected Commission - Dollar Value");
    }

    protected WebElement getProjectedCommissionCurrencyEle() {
        return assertElementExists(By.xpath("//span[@class='statistics__item-value-currency']"), "Projected Commission - Currency");
    }

    protected WebElement getStartDateInputEle() {
        return assertElementExists(By.xpath("(//div[@class='date-range-picker'] //input)[1]"), "Start Date Input");
    }

    protected WebElement getEndDateInputEle() {
        return assertElementExists(By.xpath("(//div[@class='date-range-picker'] //input)[2]"), "End Date Input");
    }

    protected WebElement getPreviousYearArrowEle() {
        return assertElementExists(By.xpath("//button[@aria-label='Previous Year']"), "Previous Year Arrow");
    }

    protected WebElement getNextYearArrowEle() {
        return assertElementExists(By.xpath("//button[@aria-label='Next Year']"), "Next Year Arrow");
    }

    protected WebElement getDateFilterFirstAvailableDateEle() {
        return assertElementExists(By.xpath("(//td[contains(@class,'available')])[1]"), "Date Filter - First Available Date");
    }

    protected WebElement getDateFilterLastAvailableDateEle() {
        return assertElementExists(By.xpath("(//td[contains(@class,'available')])[last()]"), "Date Filter - First Available Date");
    }

    protected WebElement getApplyFilterEle() {
        return assertElementExists(By.xpath("//button[contains(@class,'apply-button')]"), "Apply Filter");
    }

    protected WebElement getQueryDateTypeDropdownEle() {
        return assertElementExists(By.xpath("//div[@data-testid='query-date']"), "Query Date Dropdown Trigger (Date type to filter)");
    }

    protected List<WebElement> getQueryDateTypeDropdownOptionListEle() {
        return assertElementsExists(By.xpath("//div[not(contains(@style,'display: none'))]/div[@class='ht-select-dropdown'] //li/span"), "Query Date Dropdown Option List");
    }

    protected WebElement getStatusDropdownEle() {
        return assertElementExists(By.xpath("//div[@data-testid='account-status']"), "Status Dropdown Trigger");
    }

    protected List<WebElement> getStatusDropdownOptionListEle() {
        return assertElementsExists(By.xpath("//div[not(contains(@style,'display: none'))]/div/div/div/ul/li[@class='ht-select-dropdown__item']/span"), "Status Dropdown Option List");
    }

    protected List<WebElement> getClientUserIDListEle() {
        return assertElementsExists(By.xpath("//table[@class='ht-table__body']/tbody/tr/td[1]/div/span"), "Client User ID List");
    }

    protected List<WebElement> getClientNameListEle() {
        return assertElementsExists(By.xpath("//table[@class='ht-table__body']/tbody/tr/td[2]/div/span"), "Client Name List");
    }

    protected List<WebElement> getClientRegistrationDateListEle() {
        return assertElementsExists(By.xpath("//table[@class='ht-table__body']/tbody/tr/td[3]/div/span"), "Client Registration Date List");
    }

    protected List<WebElement> getClientStatusListEle() {
        return assertElementsExists(By.xpath("//table[@class='ht-table__body']/tbody/tr/td[4]/div/span"), "Client Status List");
    }

    protected List<WebElement> getClientCountryListEle() {
        return assertElementsExists(By.xpath("//table[@class='ht-table__body']/tbody/tr/td[5]/div/span"), "Client Status List");
    }

    protected List<WebElement> getClientAFPListEle() {
        return assertElementsExists(By.xpath("//table[@class='ht-table__body']/tbody/tr/td[6]/div/span"), "Client AFP List");
    }

    protected List<WebElement> getClientFTDListEle() {
        return assertElementsExists(By.xpath("//table[@class='ht-table__body']/tbody/tr/td[7]/div/span"), "Client FTD List");
    }





    public void verifyClientList(){
        String clientValue = getClientValueEle().getText();
        String ftdValue = getFTDValueEle().getText();
        String qftdValue = getQFTDValueEle().getText();
        String projectedCommissionDollar = getProjectedCommissionDollarEle().getText();
        String projectedCommissionCurrency = getProjectedCommissionCurrencyEle().getText();

        Assert.assertTrue(!clientValue.equals("0"));
        Assert.assertTrue(!ftdValue.equals("0"));
        Assert.assertTrue(!qftdValue.equals("0"));
        Assert.assertTrue(!projectedCommissionDollar.equals("0.00"));
        Assert.assertTrue(projectedCommissionCurrency.equals("USD"));

        //Filter by Registration Date (should show all clients)
        getStartDateInputEle().click();
        triggerElementClickEvent(getPreviousYearArrowEle());
        triggerElementClickEvent(getPreviousYearArrowEle());
        triggerElementClickEvent(getDateFilterFirstAvailableDateEle());
        triggerElementClickEvent(getNextYearArrowEle());
        triggerElementClickEvent(getNextYearArrowEle());
        triggerElementClickEvent(getDateFilterLastAvailableDateEle());
        triggerElementClickEvent(getApplyFilterEle());

        List<String> clientUserIDList = getClientUserIDListEle().stream().map(WebElement::getText).toList();
        List<String> clientNameList = getClientNameListEle().stream().map(WebElement::getText).toList();
        List<String> clientRegistrationDateList = getClientRegistrationDateListEle().stream().map(WebElement::getText).toList();
        List<String> clientStatusList = getClientStatusListEle().stream().map(WebElement::getText).toList();
        List<String> clientCountryList = getClientCountryListEle().stream().map(WebElement::getText).toList();
        List<String> clientAFPList = getClientAFPListEle().stream().map(WebElement::getText).toList();
        List<String> clientFTDList = getClientFTDListEle().stream().map(WebElement::getText).toList();

        Assert.assertTrue(clientStatusList.contains("Registrations"), "None of the clients has 'Registrations' status");
        Assert.assertTrue(clientStatusList.contains("Live"), "None of the clients has 'Live' status");
        Assert.assertTrue(clientStatusList.contains("Qualify"), "None of the clients has 'Qualify' status");

        LogUtils.info("List of Clients:");
        LogUtils.info("Client User ID, Client Name, Registration Date, Status, Country, AFP, FTD");

        for(int i=0;i<clientUserIDList.size();i++){
            LogUtils.info(clientUserIDList.get(i)+", "+clientNameList.get(i)+", "+clientRegistrationDateList.get(i)+", "+clientStatusList.get(i)+", "+clientCountryList.get(i)+", "+clientAFPList.get(i)+", "+clientFTDList.get(i));
        }

//        triggerElementClickEvent(getQueryDateTypeDropdownEle());
//        selectRandomDropDownOption_ElementClickEvent(getQueryDateTypeDropdownOptionListEle());
        triggerElementClickEvent(getStatusDropdownEle());
        String statusFilter = selectRandomDropDownOption_ElementClickEvent(getStatusDropdownOptionListEle());
        triggerElementClickEvent(getApplyFilterEle());

        List<String> filteredClientUserIDList = getClientUserIDListEle().stream().map(WebElement::getText).toList();
        List<String> filteredClientNameList = getClientNameListEle().stream().map(WebElement::getText).toList();
        List<String> filteredClientRegistrationDateList = getClientRegistrationDateListEle().stream().map(WebElement::getText).toList();
        List<String> filteredClientStatusList = getClientStatusListEle().stream().map(WebElement::getText).toList();
        List<String> filteredClientCountryList = getClientCountryListEle().stream().map(WebElement::getText).toList();
        List<String> filteredClientAFPList = getClientAFPListEle().stream().map(WebElement::getText).toList();
        List<String> filteredClientFTDList = getClientFTDListEle().stream().map(WebElement::getText).toList();

        Assert.assertTrue(clientUserIDList.contains(filteredClientUserIDList.get(0)));

        LogUtils.info("Filter Test - List of Clients with status = " +statusFilter+":");
        LogUtils.info("Client User ID, Client Name, Registration Date, Status, Country, AFP, FTD");

        for(int i=0;i<filteredClientUserIDList.size();i++){
            LogUtils.info(filteredClientUserIDList.get(i)+", "+filteredClientNameList.get(i)+", "+filteredClientRegistrationDateList.get(i)+", "+filteredClientStatusList.get(i)+", "+filteredClientCountryList.get(i)+", "+filteredClientAFPList.get(i)+", "+filteredClientFTDList.get(i));
        }
    }


}
