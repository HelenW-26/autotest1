package newcrm.pages.starclientpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.FinancialDetailsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class STARASICFinancialDetailsPage extends FinancialDetailsPage {

    public STARASICFinancialDetailsPage(WebDriver driver) {
        super(driver);
    }

    public String setFinancialProductsExp() {
        return setQuizAnswer(2, "Experience with Financial Products");
    }

    public String setIncomeSourceStability() {
        return setQuizAnswer(2, "Income Source and Stability");
    }

    public String setMonthlyIncome() {
        return setQuizAnswer(2, "Monthly Income");
    }

    public String setEssentialMonthlyExpenses() {
        return setQuizAnswer(2, "Essential Monthly Expenses");
    }

    public String setInvestmentTimeframe() {
        return setQuizAnswer(1, "Investment Timeframe");
    }

    public String setInvestmentPurpose() {
        return setQuizAnswer(2, "Investment Purpose");
    }

    public String setRiskReturnExpectations() {
        return setQuizAnswer(1, "Risk & Return Expectations");
    }

    public String setInvestmentAmountContext() {
        return setQuizAnswer(1, "Investment Amount Context");
    }

    public String setLossImpactAssessment() {
        return setQuizAnswer(2, "Loss Impact Assessment");
    }

    public String setSourceOfInvestmentFunds() {
        return setQuizAnswer(3, "Source of Investment Funds");
    }

    public String setFinancialCircumstances() {
        return setQuizAnswer(6, "Financial Circumstances");
    }

    public String setPersonalCircumstances() {
        return setQuizAnswer(5, "Personal Circumstances");
    }

    public String setQuizAnswer(int answernum, String desc) {
        String result = this.getQuizAnswerOptions(answernum);
        LogUtils.info("FinancialDetailsPage: set " + desc + " to: " + result);
        return result;
    }

    protected String getQuizAnswerOptions(int answernum){
        assertVisibleElementExists(By.xpath("//div[@role='radiogroup']"), "Quiz Answer");

        WebElement e = assertVisibleElementExists(By.xpath("(//div[@role='radiogroup'])[1]//div[@class='questionnaire-question-item'][" +answernum + "]/label/span[2]"), "Quiz Question");
        String result = e.getAttribute("innerText");
        triggerClickEvent(e);

        return result;
    }

}
