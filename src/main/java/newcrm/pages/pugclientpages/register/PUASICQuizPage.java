package newcrm.pages.pugclientpages.register;

import newcrm.pages.clientpages.register.QuizPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;

public class PUASICQuizPage extends QuizPage {

    public PUASICQuizPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public String setOTCDerivativeContracts() {
        return setQuizAnswer(3, "OTC Derivative Contracts");
    }

    @Override
    public String setLeverageBenefit() {
        return setQuizAnswer(4, "Leverage Benefits");
    }

    @Override
    public String setLeverageRisk() {
        return setQuizAnswer(3, "Leverage Risk");
    }

    @Override
    public String setShortPosition() {
        return setQuizAnswer(4, "Short Position");
    }

    @Override
    public String setLongPosition() {
        return setQuizAnswer(1, "Long Position");
    }

    @Override
    public String setOrderType() {
        return setQuizAnswer(2, "Order Type");
    }

    @Override
    public String setMarketOrder() {
        return setQuizAnswer(1, "Market Order");
    }

    @Override
    public String setStopLossPurpose() {
        return setQuizAnswer(4, "Stop Loss Purpose");
    }

    @Override
    public String setTradeLossBalance() {
        return setQuizAnswer(1, "Trade Loss Balance");
    }

    @Override
    protected String getQuizAnswerOptions(int answernum){
        // Check empty on quiz title
        WebElement quizTitleEle = assertVisibleElementExists(By.xpath("//p[@class='question-content']"), "Quiz Title");
        if (quizTitleEle.getText().trim().isEmpty()) {
            Assert.fail("Quiz Title is empty");
        }

        // Check empty on quiz options
        List<WebElement> quizOptionEles = assertVisibleElementsExists(By.xpath("//div[@class='question-answer']/button"), "Quiz Options");
        for (int j = 0; j < quizOptionEles.size(); j++) {
            if (quizOptionEles.get(0).getText().trim().isEmpty()) {
                Assert.fail(String.format("Quiz Option %s is empty", j + 1));
            }
        }

        // Click on specific quiz option
        WebElement e = assertElementExists(By.xpath("//div[@class='question-answer']/button[" +answernum + "]"), "Quiz Options");
        triggerClickEvent(e);

        return e.getAttribute("innerText");
    }

}
