package newcrm.pages.pugclientpages.register;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.FinancialDetailsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class PUASICFinancialDetailsPage extends FinancialDetailsPage {

    public PUASICFinancialDetailsPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getAgreeTickbox() {
        return assertClickableElementExists(By.xpath("//div[@data-testid='checkbox']//section[contains(@class, 'ht-protocol__checkbox')]"), "Agreement tick box");
    }

    public void setAgreeTickbox() {
        WebElement e_tick = this.getAgreeTickbox();
        String class_value = e_tick.getAttribute("class");
        if(class_value.contains("active")) {
            LogUtils.info("FinancialDetailsPage: Already ticked agreement");
            return;
        }
        triggerClickEvent(e_tick);
        LogUtils.info("FinancialDetailsPage: tick agreement");
    }

    public String setIncomeSourceStability() {
        return setQuizAnswer(1, "Income Source and Stability");
    }

    public String setFinancialCircumstances() {
        return setQuizAnswer(5, "Financial Circumstances");
    }

    @Override
    public String setEstimatedAnnualIncome() {
        return setQuizAnswer(3, "Annual Income");
    }

    @Override
    public String setEstimatedSavingsAndInvestments() {
        return setQuizAnswer(3, "Savings & Investments");
    }

    public String setLossImpactAssessment() {
        return setQuizAnswer(1, "Loss Impact Assessment");
    }

    public String setInvestmentPurpose() {
        return setQuizAnswer(1, "Investment Purpose");
    }

    public String setInvestmentObjective() {
        return setQuizAnswer(1, "Investment Objective");
    }

    public String setRiskReturnExpectations() {
        return setQuizAnswer(1, "Risk & Return Expectations");
    }

    public String setInvestmentTimeframe() {
        return setQuizAnswer(1, "Investment Timeframe");
    }

    public String setRiskTolerance() {
        return setQuizAnswer(1, "Risk Tolerance");
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
