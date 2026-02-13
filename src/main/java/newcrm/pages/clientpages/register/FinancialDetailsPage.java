package newcrm.pages.clientpages.register;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import org.testng.Assert;
import utils.LogUtils;

/**
 * this class base on vfsc2
 * @author FengLiu 22-10-2021
 *
 */
public class FinancialDetailsPage extends Page {

	public FinancialDetailsPage(WebDriver driver) {
		super(driver);
	}
	
	protected List<WebElement> getQuestionOptions(int questionnum){
		String xpath = "(//div[@class='question'])[" +questionnum + "]/div/label";
		this.findVisibleElemntByXpath(xpath);
		return driver.findElements(By.xpath(xpath));
	}
	
	public String setEmploymentStatus() {
		String result = this.selectRandomValueFromDropDownList(this.getQuestionOptions(1));
		LogUtils.info("FinancialDetailsPage: set Emplyment Status to: " + result);
		return result;
	}
	
	public String setEstimatedAnnualIncome() {
		String result = this.selectRandomValueFromDropDownList(this.getQuestionOptions(2));
		LogUtils.info("FinancialDetailsPage: set Estimated Annual Income to: " + result);
		return result;
	}
	
	public String setEstimatedSavingsAndInvestments() {
		String result = this.selectRandomValueFromDropDownList(this.getQuestionOptions(3));
		LogUtils.info("FinancialDetailsPage: set Estimated Savings and Investments to: " + result);
		return result;
	}
	
	public String setEstimatedIntendedDeposit() {
		String result = this.selectRandomValueFromDropDownList(this.getQuestionOptions(4));
		LogUtils.info("FinancialDetailsPage: set Estimated Intended Deposit to: " + result);
		return result;
	}
	public String setSourceOfFunds() {
		String result = this.selectRandomValueFromDropDownList(this.getQuestionOptions(5));
		LogUtils.info("FinancialDetailsPage: set Source of Funds to: " + result);
		return result;
	}
	
	public String setNumberOfTradesPerWeek() {
		String result = this.selectRandomValueFromDropDownList(this.getQuestionOptions(6));
		LogUtils.info("FinancialDetailsPage: set Number Of Trades Per Week to: " + result);
		return result;
	}
	
	public String setTradingAmountPerWeek() {
		String result = this.selectRandomValueFromDropDownList(this.getQuestionOptions(7));
		LogUtils.info("FinancialDetailsPage: set Trading Amount Per Week to: " + result);
		return result;
	}
	
	public void next() {
		WebElement e = this.findClickableElementByXpath("//button[@data-testid='next']");
		this.moveElementToVisible(e);
		e.click();
		this.waitLoading();
	}
	
	public String getPageTitle() {
		return this.findVisibleElemntByXpath("//h3").getText();
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

}
