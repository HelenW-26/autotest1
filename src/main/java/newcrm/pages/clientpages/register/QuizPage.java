package newcrm.pages.clientpages.register;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import utils.LogUtils;

import java.util.List;

public class QuizPage extends Page {

    public QuizPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getStartQuizBtn() {
        return assertClickableElementExists(By.xpath("//div[@class='account_opening_complete']//button[@data-testid='button']"), "Start Quiz button");
    }

    protected WebElement getNextBtn() {
        return assertClickableElementExists(By.xpath("//button[@data-testid='button' and normalize-space() = 'Next']"), "Next button");
    }

    protected WebElement getSubmitBtn() {
        return assertClickableElementExists(By.xpath("//button[@data-testid='button' and normalize-space() = 'Submit']"), "Submit button");
    }

    public void startQuiz() {
        triggerElementClickEvent(this.getStartQuizBtn());
        LogUtils.info("QuizPage: Start quiz");
    }

    public void next() {
        triggerElementClickEvent(this.getNextBtn());
    }

    public void submit() {
        triggerElementClickEvent(this.getSubmitBtn());
        LogUtils.info("Submit Quiz successful");
    }

    public String setOTCDerivativeProduct() {
        return setQuizAnswer(4, "OTC Derivative products");
    }

    public String setOnlineTradingPlatformAllows() {
        return setQuizAnswer(4, "Online Trading Platform Allows");
    }

    public String setStopLossOrderPurpose() {
        return setQuizAnswer(3, "Stop Loss Order Purpose");
    }

    public String setShareCFDStatement() {
        return setQuizAnswer(3, "Share CFD Statement");
    }

    public String setRiskWarning() {
        return setQuizAnswer(1, "Risk Warning");
    }

    public String setOverallExposure() {
        return setQuizAnswer(2, "Overall Exposure");
    }

    public String setSpreadMeaning() {
        return setQuizAnswer(3, "Spread Meaning");
    }

    public String setTrueStatement() {
        return setQuizAnswer(1, "True Statement");
    }

    public String setTradingAccResponsibility() {
        return setQuizAnswer(2, "Trading Account Responsibility");
    }

    public String setMarketResponse() {
        return setQuizAnswer(2, "Market Response");
    }

    public String setHedgingStrategyPurpose() { return ""; }

    public String setMarginPurpose() { return ""; }

    public String setLeverageBenefit() { return ""; }

    public String setShortPosition() { return ""; }

    public String setOngoingCost() { return ""; }

    public String setOrderType() { return ""; }

    public String setNegativeBeta() { return ""; }

    public String setStopLossPurpose() { return ""; }

    public String setTradeLossBalance() { return ""; }

    public String setOTCDerivativeContracts() { return ""; }

    public String setLeverageRisk() { return ""; }

    public String setLongPosition() { return ""; }

    public String setMarketOrder() { return ""; }

    public String setQuizAnswer(int answernum, String desc) {
        String result = this.getQuizAnswerOptions(answernum);
        LogUtils.info("QuizPage: set " + desc + " to: " + result);
        return result;
    }

    protected String getQuizAnswerOptions(int answernum){
        // Check empty on quiz title
        WebElement quizTitleEle = assertVisibleElementExists(By.xpath("//p[@class='question-content']"), "Quiz Title");
        if (quizTitleEle.getText().trim().isEmpty()) {
            Assert.fail("Quiz Title is empty");
        }

        // Check empty on quiz options
        List<WebElement> quizOptionEles = assertVisibleElementsExists(By.xpath("//div[@class='question-answer']/button[@data-testid='button']"), "Quiz Options");
        for (int j = 0; j < quizOptionEles.size(); j++) {
            if (quizOptionEles.get(0).getText().trim().isEmpty()) {
                Assert.fail(String.format("Quiz Option %s is empty", j + 1));
            }
        }

        // Click on specific quiz option
        WebElement e = assertElementExists(By.xpath("//div[@class='question-answer']/button[@data-testid='button'][" +answernum + "]"), "Quiz Options");
        triggerClickEvent(e);

        return e.getAttribute("innerText");
    }

    public void waitLoadingSuitabilityQuizContent() {
        assertVisibleElementExists(By.xpath("//div[contains(@class,'account_opening_drawer')]//p[normalize-space() = 'Suitability Quiz']"),"Suitability Quiz Content");
    }

    public void waitLoadingSuitabilityQuestionnaireContent() {
        assertVisibleElementExists(By.xpath("//div[contains(@class,'questionCenter')]//h4[normalize-space(text()) != '']"),"Suitability Questionnaire Content");
        this.waitLoading();
    }

}
