package newcrm.pages.clientpages.register;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import utils.LogUtils;

import java.util.List;

public class RegisterGoldFinancialDetailsPage extends Page {

    public RegisterGoldFinancialDetailsPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getAdvanceVerificationContentEle() {
        return assertVisibleElementExists(By.cssSelector("div.kyc_drawer:not([style*='display: none']) div[aria-label='Bank Transfer Verification']"),"Bank Transfer Verification Content");
    }

    protected List<WebElement> getQuestionListEle() {
        return assertElementsExists(By.cssSelector("div.questionnaire-item"),"Question List");
    }

    protected WebElement getQuestionDescEle(WebElement parentEle) {
        return assertElementExists(By.cssSelector("p.questionnaire-item-word"),"Question Description", parentEle);
    }

    protected List<WebElement> getQuestionOptionListEle(WebElement parentEle) {
        return assertElementsExists(By.cssSelector("div.questionnaire-question div[data-testid='question'] div.questionnaire-question-item"),"Question Options", parentEle);
    }

    public void verifyQuestionnaireContent() {
        List<WebElement> questionListEle = getQuestionListEle();

        for (int i = 0; i < questionListEle.size(); i++) {
            WebElement questionEle = questionListEle.get(i);

            // Check question description content
            WebElement questionDescEle = getQuestionDescEle(questionEle);
            String questionDesc = questionDescEle.getText();

            LogUtils.info(String.format("FinancialDetailsPage: Question %s: %s", i + 1, questionDesc));

            if (questionDesc.trim().isEmpty()) {
                Assert.fail(String.format("Question %s Description is empty", i + 1));
            }

            // Check question options available content
            List<WebElement> questionOptionListEle = getQuestionOptionListEle(questionEle);

            for (int j = 0; j < questionOptionListEle.size(); j++) {
                WebElement questionOptionEle = questionOptionListEle.get(j);
                String questionOption = questionOptionEle.getText();

                LogUtils.info(String.format("FinancialDetailsPage: Question Option %s: %s", j + 1, questionOption));

                if (questionOption.trim().isEmpty()) {
                    Assert.fail(String.format("Question Option %s is empty", j + 1));
                }
            }
        }
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

    public void waitLoadingAdvanceVerificationContent() {
        waitLoader();
        getAdvanceVerificationContentEle();
        waitButtonLoader();
        waitLoader();
    }

}
