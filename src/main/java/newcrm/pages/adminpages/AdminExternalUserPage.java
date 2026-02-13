package newcrm.pages.adminpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import newcrm.testcases.BaseTestCase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AdminExternalUserPage extends Page {

    public AdminExternalUserPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getAddButtonEle() {
        return assertElementExists(By.xpath("//button[@id='button1']"), "Add Button");
    }

    protected WebElement getEmailSearchInputEle() {
        return assertElementExists(By.xpath("//th[@data-field='email'] //input"), "Email Search Input");
    }

    protected WebElement getSearchButtonEle() {
        return assertElementExists(By.xpath("//button[@id='query']"), "Search Button");
    }

    protected List<WebElement> getSelectGenderDropdownListEle() {
        return assertElementsExists(By.xpath("//select[@name='gender']/option"), "Add new user - Gender Dropdown List",e -> !e.getText().toLowerCase().contains("please select"));
    }

    protected List<WebElement> getSelectClientTypeDropdownListEle() {
        return assertElementsExists(By.xpath("//select[@name='ib_Account_type']/option"), "Add new user - Client Type Dropdown List",e -> !e.getText().toLowerCase().contains("please select"));
    }

    protected WebElement getFirstNameInputEle() {
        return assertElementExists(By.xpath("//input[@name='acc_first_name']"), "Add new user - First Name");
    }

    protected List<WebElement> getSelectAccountTypeDropdownListEle() {
        return assertElementsExists(By.xpath("//select[@name='ib_accountType']/option"), "Add new user - Account Type Dropdown List",e -> !e.getText().toLowerCase().contains("please select"));
    }

    protected WebElement getBirthdayDropdownEle() {
        return assertElementExists(By.xpath("//span[@class='add-on']"), "Add new user - Birthday Dropdown");
    }

    protected WebElement getBirthdaySwitchEle() {
        return assertElementExists(By.xpath("(//div[@style='display: block;'] //th[@class='switch'])[last()]"), "Add new user - Birthday Dropdown");
    }

    protected List<WebElement> getBirthdayYearsEle() {
        return assertElementsExists(By.xpath("//div[@class='datepicker-years' and not(@style='display: none;')] //span[contains(@class, 'year') and not(contains(@class, 'disabled'))]"), "Add new user - Birthday Year");
    }

    protected WebElement getLastNameInputEle() {
        return assertElementExists(By.xpath("//input[@name='acc_last_name']"), "Add new user - Last Name");
    }

    protected List<WebElement> getSelectIdentificationTypeDropdownListEle() {
        return assertElementsExists(By.xpath("//select[@name='acc_identification_type']/option"), "Add new user - Identification Type Dropdown List",e -> !e.getText().toLowerCase().contains("please select"));
    }

    protected WebElement getIBLevel1DropdownOptionEle() {
        return assertElementExists(By.xpath("//select[@name='ib_Role']/option[not(text()='Please select')][1]"), "Add new user - IB Level 1");
    }

    protected WebElement getAccountOwnerDropdownEle() {
        return assertElementExists(By.xpath("//div[@id='accountOwner_chosen']"), "Add new user - Account Owner Dropdown");
    }

    protected WebElement getAccountOwnerSearchEle() {
        return assertElementExists(By.xpath("//div[@class='chosen-search']/input"), "Add new user - Account Owner Search");
    }

    protected WebElement getAccountOwnerFirstSearchResultEle() {
        return assertElementExists(By.xpath("//ul[@class='chosen-results']/li[1]"), "Add new user - Account Owner First Search Result");
    }

    protected WebElement getIdentificationNumberInputEle() {
        return assertElementExists(By.xpath("//input[@name='acc_identification_number']"), "Add new user - Identification Number");
    }

    protected WebElement getEmailInputEle() {
        return assertElementExists(By.xpath("//input[@name='ib_email']"), "Add new user - Email Address");
    }

    protected WebElement getCPPasswordEle() {
        return assertElementExists(By.xpath("//input[@name='ib_password']"), "Add new user - CP Password");
    }

    protected List<WebElement> getSelectMobileCountryCodeDropdownListEle() {
        return assertElementsExists(By.xpath("//select[@name='mobile_code']/option"), "Add new user - Mobile Country Code Dropdown List",e -> !e.getText().toLowerCase().contains("select"));
    }

    protected WebElement getMobileNumberInputEle() {
        return assertElementExists(By.xpath("//input[@name='ib_Mobile']"), "Add new user - Mobile Number");
    }

    protected List<WebElement> getSelectCountryDropdownListEle() {
        return assertElementsExists(By.xpath("//select[@name='ib_Country']/option"), "Add new user - Country Dropdown List",e -> !e.getText().toLowerCase().contains("select"));
    }

    protected List<WebElement> getSelectNationalityDropdownListEle() {
        return assertElementsExists(By.xpath("//select[@name='ib_Nationality']/option"), "Add new user - Nationality Dropdown List",e -> !e.getText().toLowerCase().contains("please select"));
    }

    protected WebElement getStreetNumberNameInputEle() {
        return assertElementExists(By.xpath("//input[@name='ib_street_adress']"), "Add new user - Street Number & Name");
    }

    protected WebElement getCityInputEle() {
        return assertElementExists(By.xpath("//input[@name='ib_Suburb']"), "Add new user - City");
    }

    protected WebElement getProvinceInputEle() {
        return assertElementExists(By.xpath("//input[@name='inputState']"), "Add new user - Province/State");
    }

    protected WebElement getPostcodeInputEle() {
        return assertElementExists(By.xpath("//input[@name='ib_Postcode']"), "Add new user - Postcode");
    }

    protected WebElement getIBTypeIBEle() {
        return assertElementExists(By.xpath("//select[@name='ib_type']/option[not(text()='Please select')][1]"), "Add new user - IB Type = IB");
    }

    protected WebElement getConfirmButtonEle() {
        return assertElementExists(By.xpath("//div[@class='modal-footer'] //button[@class='btn btn-default'][1]"), "Add new user - Confirm");
    }

    protected WebElement getAddNewIBFlowButtonEle() {
        return assertElementExists(By.xpath("//tr[1]//a[@class='addAccountOnNewIbFlow']"), "New IB Flow: Add Account Button");
    }

    protected WebElement getOperationAddIBButtonEle() {
        return assertElementExists(By.xpath("//tr[1]//a[@class='addRe']"), "Operation: ADD (IB) Button");
    }

    protected WebElement getEnterSearchContentOKButtonEle() {
        return assertElementExists(By.xpath("//button[@id='alertOk']"), "Enter Search Content - OK Button");
    }

    public void clickAddButton(){
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@id='button1']")));

        triggerElementClickEvent_withoutMoveElement(this.getAddButtonEle());
        GlobalMethods.printDebugInfo("Clicked on Add Button");
    }

    public void selectGender() {
        String gender = selectRandomDropDownOption_ElementClickEvent(getSelectGenderDropdownListEle());
        GlobalMethods.printDebugInfo("Gender: "+gender);
    }

    public void selectClientType(){
        String clientType = selectRandomDropDownOption_ElementClickEvent(getSelectClientTypeDropdownListEle());
        GlobalMethods.printDebugInfo("Client Type: "+clientType);
    }

    public void inputFirstName(){
        String firstName = "autotest" + GlobalMethods.getRandomString(10);
        this.setInputValue(this.getFirstNameInputEle(), firstName);
        GlobalMethods.printDebugInfo("First Name: "+firstName);
    }

    public void inputLastName(){
        String lastName = "TestCRM";
        this.setInputValue(this.getLastNameInputEle(), lastName);
        GlobalMethods.printDebugInfo("Last Name: "+lastName);
    }

    public void selectAccountType(){
        List<WebElement> accountTypeDropdownOptions = new ArrayList<>(getSelectAccountTypeDropdownListEle());

        accountTypeDropdownOptions.removeIf(option -> {
            String text = option.getAttribute("innerText");
            return text != null && (
                    text.toLowerCase().contains("hedge") ||
                            text.toLowerCase().contains("mts") ||
                            text.toLowerCase().contains("institution type")
            );
        });

        String accountType = selectRandomDropDownOption_ElementClickEvent(accountTypeDropdownOptions);
        GlobalMethods.printDebugInfo("Account Type: "+accountType);
    }

    public void selectBirthday(){
        moveElementToVisible(getBirthdayDropdownEle());
        triggerElementClickEvent_withoutMoveElement(this.getBirthdayDropdownEle());
        triggerElementClickEvent_withoutMoveElement(this.getBirthdaySwitchEle());
        triggerElementClickEvent_withoutMoveElement(this.getBirthdaySwitchEle());
        selectRandomDropDownOption_ElementClickEvent(getBirthdayYearsEle());
        WebElement birthDay = assertElementExists(By.xpath("//div[@id='datetimepicker1']/input"),"Birthday Input Value");
        GlobalMethods.printDebugInfo("Birthday: " + birthDay.getAttribute("value"));
        //click another field to close date selector
        triggerElementClickEvent_withoutMoveElement(this.getLastNameInputEle());
    }

    public void selectIdentificationType(){
        String identificationType = selectRandomDropDownOption_ElementClickEvent(getSelectIdentificationTypeDropdownListEle());
        GlobalMethods.printDebugInfo("Identification Type: "+identificationType);
    }

    public void selectIBLevel1(){
        triggerElementClickEvent_withoutMoveElement(this.getIBLevel1DropdownOptionEle());
        GlobalMethods.printDebugInfo("IB Level 1");
    }

    public void selectAccountOwner(String adminName){
        triggerElementClickEvent_withoutMoveElement(this.getAccountOwnerDropdownEle());
        this.setInputValue(this.getAccountOwnerSearchEle(),adminName);
        triggerElementClickEvent_withoutMoveElement(this.getAccountOwnerFirstSearchResultEle());
        GlobalMethods.printDebugInfo("Account Owner: " + adminName);
    }

    public void inputIdentificationNumber(){
        String idnum = "TESTID"+GlobalMethods.getRandomNumberString(10);
        this.setInputValue(this.getIdentificationNumberInputEle(), idnum);
        GlobalMethods.printDebugInfo("Identification Number: "+idnum);
    }

    public String inputEmail(){
        String email = ("autotest"+GlobalMethods.getRandomString(8)+"@testcrmautomation.com").toLowerCase();
        this.setInputValue(this.getEmailInputEle(), email);
        GlobalMethods.printDebugInfo("Email Address: "+email);

        return email;
    }

    public void inputCPPassword() {
        String pwd = GlobalMethods.generatePassword();
        this.setInputValue(this.getCPPasswordEle(), pwd);
        GlobalMethods.printDebugInfo("CP Password: "+pwd);
    }

    public void selectMobileCountryCode(){
        String mobileCountryCode = selectRandomDropDownOption_ElementClickEvent(getSelectMobileCountryCodeDropdownListEle());
        GlobalMethods.printDebugInfo("Mobile Country Code: "+mobileCountryCode);
    }

    public void inputMobileNumber(){
        String phone = GlobalMethods.getRandomNumberString(10);
        this.setInputValue(this.getMobileNumberInputEle(), phone);
        GlobalMethods.printDebugInfo("Mobile Number: "+phone);
    }

    public void selectCountryOfResidence(){
        String country = selectRandomDropDownOption_ElementClickEvent(getSelectCountryDropdownListEle());
        GlobalMethods.printDebugInfo("Country of Residence: "+country);
        //click another field for 'irregular' error message to display (if any)
        triggerElementClickEvent_withoutMoveElement(this.getMobileNumberInputEle());
        //re-select if the country is 'irregular'
        while(!driver.findElements(By.xpath("//select[@class='form-control text-danger']")).isEmpty()){
            country = selectRandomDropDownOption_ElementClickEvent(getSelectCountryDropdownListEle());
            GlobalMethods.printDebugInfo("Re-select Country of Residence: "+country);
            triggerElementClickEvent_withoutMoveElement(this.getMobileNumberInputEle());
        }
    }

    public void selectNationality() {
        String nationality = selectRandomDropDownOption_ElementClickEvent(getSelectNationalityDropdownListEle());
        GlobalMethods.printDebugInfo("Nationality: " + nationality);
    }

    public void inputStreetNumberName(){
        String street = GlobalMethods.getRandomNumberString(3)+" Test Street";
        this.setInputValue(this.getStreetNumberNameInputEle(), street);
        GlobalMethods.printDebugInfo("Street Number & Name: "+street);
    }

    public void inputCity(){
        String city = GlobalMethods.getRandomString(3)+ " TestCity";
        this.setInputValue(this.getCityInputEle(), city);
        GlobalMethods.printDebugInfo("City: "+city);
    }

    public void inputProvinceState(){
        String province = GlobalMethods.getRandomString(3)+ " TestProvince";
        this.setInputValue(this.getProvinceInputEle(),province);
        GlobalMethods.printDebugInfo("Province/State: "+province);
    }

    public void inputPostcode(){
        String postcode = GlobalMethods.getRandomNumberString(6);
        this.setInputValue(this.getPostcodeInputEle(), postcode);
        GlobalMethods.printDebugInfo("Postcode: "+postcode);
    }

    public String inputNewExternalUserDetails(String adminName){
        clickAddButton();
        selectGender();
        selectClientType();
        inputFirstName();
        inputLastName();
        selectAccountType();
        selectBirthday();
        selectIdentificationType();
        selectIBLevel1();
        selectAccountOwner(adminName);
        inputIdentificationNumber();
        String email = inputEmail();
        inputCPPassword();
        selectMobileCountryCode();
        inputMobileNumber();
        selectCountryOfResidence();
        selectNationality();
        inputStreetNumberName();
        inputCity();
        inputProvinceState();
        inputPostcode();

        return email;
    }

    public void selectIBType(){
        triggerElementClickEvent_withoutMoveElement(getIBTypeIBEle());
        GlobalMethods.printDebugInfo("IB Type: IB");
    }

    public void clickConfirmButton(){
        triggerElementClickEvent_withoutMoveElement(getConfirmButtonEle());
        GlobalMethods.printDebugInfo("Clicked Confirm Button");

        By loadingImage = By.id("AjaxLoading");
        fastwait.until(ExpectedConditions.invisibilityOfElementLocated(loadingImage));
    }

    public void clickEnterSearchContent_OKButton(){
        try {
            triggerElementClickEvent_withoutMoveElement(getEnterSearchContentOKButtonEle());
        } catch (AssertionError e) {
            GlobalMethods.printDebugInfo("Search Content Popup not found, skip clicking OK Button.");
        }
    }

    public void searchExternalUserByEmail(String email){
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//th[@data-field='email'] //input")));
        fastwait.until(ExpectedConditions.elementToBeClickable(By.xpath("//th[@data-field='email'] //input")));
        fastwait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[@data-field='email'] //input")));
        this.setInputValue(this.getEmailSearchInputEle(),email);
        triggerElementClickEvent_withoutMoveElement(getSearchButtonEle());
    }

    //Use New IB Flow if available, otherwise use Operation Flow
    public void addAdditionalIB(){
        try {
            addAdditionalIB_NewIBFlow();
        } catch(AssertionError e) {
            addAdditionalIB_OperationFlow();
        }
    }

    public void addAdditionalIB_NewIBFlow(){
        triggerElementClickEvent_withoutMoveElement(getAddNewIBFlowButtonEle());
        triggerElementClickEvent_withoutMoveElement(getConfirmButtonEle());
        GlobalMethods.printDebugInfo("Additional IB Account Added Through New IB Flow (Add Account button)");
    }

    public void addAdditionalIB_OperationFlow(){
        triggerElementClickEvent_withoutMoveElement(getOperationAddIBButtonEle());
        triggerElementClickEvent_withoutMoveElement(getConfirmButtonEle());
        GlobalMethods.printDebugInfo("Additional IB Account Added Through Operation Flow (ADD button), New IB Flow (Add Account button) NOT available");
    }

}
