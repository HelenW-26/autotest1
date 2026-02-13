package newcrm.pages.clientpages.register;

import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

import java.util.HashMap;

public class RegisterGoldResidentialAddressPage extends Page {

    public RegisterGoldResidentialAddressPage(WebDriver driver)
    {
        super(driver);
    }

    protected WebElement getSuburbInput() {
        return assertElementExists(By.xpath("//input[@data-testid='cityOrSuburb']"), "City / Suburb");
    }

    protected WebElement getAddressInput() {
        return this.findVisibleElemntByXpath("//input[@data-testid='address']");
    }

    protected WebElement getSubmitButton() {
        return assertClickableElementExists(By.xpath("//button[@data-testid='submit']"), "Submit button");
    }

    public void setSuburb(String suburb) {
        this.setInputValue(getSuburbInput(), suburb);
        LogUtils.info("ResidentialAddressPage: Set suburb to :" + suburb);
    }

    public void setAddress(String streetnum, String streetname) {
        this.setInputValue(getAddressInput(),streetnum + " " + streetname);
        LogUtils.info("ResidentialAddressPage: Set Address to :" + streetnum + " " + streetname);
    }

    public void submit() {
        waitLoading();
        WebElement e = this.getSubmitButton();
        triggerClickEvent(e);
        waitButtonLoader();
        waitLoader();
    }

    // region [ SumSub POA ]

    public HashMap<String,String> fillSumSubFinancial(String country, HashMap<String,String> userdetails) {
        WebElement personalInfoStep = assertVisibleElementExists(By.xpath("//div/main[@data-step='questionnaire']"), "Sumsub Financial information Content");
        if (personalInfoStep != null) {
            LogUtils.info("Sumsub - Financial Information Step");

            userdetails.put("Employment Status", setQuizAnswer_Sumsub(1, "Employment Status", 1));
            userdetails.put("Annual Income", setQuizAnswer_Sumsub(1, "Estimated Annual Income", 2));
            userdetails.put("Savings & Investments", setQuizAnswer_Sumsub(1, "Savings & Investments", 3));
            userdetails.put("Intended Deposit", setQuizAnswer_Sumsub(1, "Intended Deposit", 4));
            userdetails.put("Source of Funds", setQuizAnswer_Sumsub(1, "Source of Funds", 5));
            userdetails.put("Place of Birth", setQuizDdl_Sumsub(country, "Place of Birth"));

            WebElement personalInfoBtn = assertElementExists(By.xpath("//button[@aria-label='Continue']"), "Sumsub Financial Info Continue button");
            personalInfoBtn.click();
            LogUtils.info("Click Sumsub Financial Info Continue button");
        }

        return userdetails;
    }

    public String setQuizAnswer_Sumsub(int answernum, String desc, int iQuestion) {
        String result = this.getQuizAnswerOptions_Sumsub(answernum, iQuestion);
        LogUtils.info("Sumsub Financial Info: set " + desc + " to: " + result);
        return result;
    }

    public String setQuizDdl_Sumsub(String country, String desc) {
        String result = this.getQuizAnswerDdl_Sumsub(country);
        LogUtils.info("Sumsub Financial Info: set " + desc + " to: " + result);
        return result;
    }

    protected String getQuizAnswerOptions_Sumsub(int answernum, int iQuestion){
        WebElement grpEle = assertVisibleElementExists(By.xpath("(//fieldset[@role='radiogroup'])[" + iQuestion + "]"), "Sumsub Financial Quiz");

        WebElement e = assertVisibleElementExists(By.xpath("(//fieldset[@role='radiogroup'])[" + iQuestion + "]//label[contains(@class, 'RadioCheckContainer')][" + answernum + "]"), "Sumsub Financial Quiz Answer");
        String result = e.getAttribute("innerText");

        triggerClickEvent(e);

        return result;
    }

    protected String getQuizAnswerDdl_Sumsub(String country){
        WebElement ddlEle = assertVisibleElementExists(By.xpath("//span[@class='sdk-select']/button"), "Sumsub Financial Place of Birth Quiz");
        triggerClickEvent(ddlEle);
        LogUtils.info("Click Sumsub Financial Place of Birth List");
        WebElement selectedCountry = assertElementExists(By.xpath("//div[@id='listbox']//button//span[normalize-space() = '" + country + "']"), "Sumsub Financial Place of Birth Country");
        triggerClickEvent(selectedCountry);
        LogUtils.info("Select Sumsub Financial Place of Birth country: " + country);

        return country;
    }

    // endregion

}
