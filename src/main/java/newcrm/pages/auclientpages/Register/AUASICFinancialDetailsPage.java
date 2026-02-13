package newcrm.pages.auclientpages.Register;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.FinancialDetailsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

import java.util.List;

public class AUASICFinancialDetailsPage extends FinancialDetailsPage {

    public AUASICFinancialDetailsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public String setSourceOfFunds() {
        return setQuizAnswer(1, "Source of Funds");
    }

    public String setRelySocialSecurityPayment() {
        return setQuizAnswer(2, "Rely Social Security Payments");
    }

    public String setFaceFinancialHardship() {
        return setQuizAnswer(2, "Face Financial Hardship");
    }

    public String setFutureEvent() {
        return setQuizAnswer(7, "Future Events");
    }

    public String setBankruptcyStatus() {
        return setQuizAnswer(2, "Bankruptcy Status");
    }

    @Override
    public String setEstimatedAnnualIncome() {
        return setQuizAnswer(3, "Annual Income");
    }

    @Override
    public String setEstimatedSavingsAndInvestments() {
        return setQuizAnswer(3, "Savings & Investments");
    }

    @Override
    public String setEstimatedIntendedDeposit() {
        return setQuizAnswer(3, "Intended Deposit");
    }

    public String setYearlyRiskAmount() {
        return setQuizAnswer(1, "Yearly Risk Amount");
    }

    public String setInvestmentPurpose() {
        return setQuizAnswer(5, "Investment Purpose");
    }

    public String setRiskAppetite() {
        return setQuizAnswer(1, "Risk Appetite");
    }

    public String setInvestmentTimeframe() {
        return setQuizAnswer(1, "Trades Timeframes");
    }

    public String setRiskTolerance() {
        return setQuizAnswer(1, "Risk Tolerance");
    }

    public String setLosingTradeOpinion() {
        return setQuizAnswer(2, "Losing Trade Opinion");
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
