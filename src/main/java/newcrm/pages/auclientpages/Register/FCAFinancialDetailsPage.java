package newcrm.pages.auclientpages.Register;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.FinancialDetailsPage;
import org.openqa.selenium.WebElement;

import java.util.List;

public class FCAFinancialDetailsPage extends FinancialDetailsPage {

	public FCAFinancialDetailsPage(WebDriver driver) {
		super(driver);
	}

    @Override
    protected List<WebElement> getQuestionOptions(int questionnum){
        String xpath = "(//div[@class='questionnaire-question']/*)[" +questionnum + "]/div/label";
        this.findVisibleElemntByXpath(xpath);
        return driver.findElements(By.xpath(xpath));
    }

	public String setOccupation() {
		String result = this.selectRandomValueFromDropDownList(this.getQuestionOptions(1));
		GlobalMethods.printDebugInfo("FCAFinancialDetailsPage: set Occupation to: " + result);
		return result;
	}
	
	public String setEmploymentSector() {
		String result = this.selectRandomValueFromDropDownList(this.getQuestionOptions(1));
		GlobalMethods.printDebugInfo("FCAFinancialDetailsPage: set Employment Sector to: " + result);
		return result;
	}
	
	@Override
	public String setEstimatedAnnualIncome() {
		String result = this.selectRandomValueFromDropDownList(this.getQuestionOptions(1));
		GlobalMethods.printDebugInfo("FCAFinancialDetailsPage: set Estimated Annual Income to: " + result);
		return result;
	}
	
	public String setPortfolioSize() {
		String result = this.selectRandomValueFromDropDownList(this.getQuestionOptions(1));
		GlobalMethods.printDebugInfo("FCAFinancialDetailsPage: set Portfolio Size to: " + result);
		return result;
	}
	
	public String setIntendToFundAccount() {
		String result = this.selectRandomValueFromDropDownList(this.getQuestionOptions(1));
		GlobalMethods.printDebugInfo("FCAFinancialDetailsPage: set How Do You Intend to Fund Your Account to: " + result);
		return result;
	}
	
	public String setTypeOfTrade() {
		String result = this.selectRandomValueFromDropDownList(this.getQuestionOptions(1));
		GlobalMethods.printDebugInfo("FCAFinancialDetailsPage: set What Type of Securities Do You Intend to Trade? to: " + result);
		return result;
	}
	
	public String setExpectedInitialDeposit() {
		String result = this.selectRandomValueFromDropDownList(this.getQuestionOptions(1));
		GlobalMethods.printDebugInfo("FCAFinancialDetailsPage: set Expected Initial Deposit into Investment Account to: " + result);
		return result;
	}
	
	public String setDailyTradeValue() {
		String result = this.selectRandomValueFromDropDownList(this.getQuestionOptions(1));
		GlobalMethods.printDebugInfo("FCAFinancialDetailsPage: set Expected Average Daily Trade Value to: " + result);
		return result;
	}
	
	public String setShares() {
		String result = this.selectRandomValueFromDropDownList(this.getQuestionOptions(1));
		GlobalMethods.printDebugInfo("FCAFinancialDetailsPage: set Shares to: " + result);
		return result;
	}
	
	public String setSpotFX() {
		String result = this.selectRandomValueFromDropDownList(this.getQuestionOptions(1));
		GlobalMethods.printDebugInfo("FCAFinancialDetailsPage: set Spot FX to: " + result);
		return result;
	}
	
	public String setEquityDerivatives() {
		String result = this.selectRandomValueFromDropDownList(this.getQuestionOptions(1));
		GlobalMethods.printDebugInfo("FCAFinancialDetailsPage: set Equity derivatives to: " + result);
		return result;
	}
	
	public String setFXCFDs() {
		String result = this.selectRandomValueFromDropDownList(this.getQuestionOptions(1));
		GlobalMethods.printDebugInfo("FCAFinancialDetailsPage: set FX CFDs to: " + result);
		return result;
	}
	
	public String setKnowledge() {
		String result = this.selectRandomValueFromDropDownList(this.getQuestionOptions(1));
		GlobalMethods.printDebugInfo("FCAFinancialDetailsPage: set Trade knowledge to: " + result);
		return result;
	}
	
	public String setEducationOntrade() {
		String result = this.selectRandomValueFromDropDownList(this.getQuestionOptions(1));
		GlobalMethods.printDebugInfo("FCAFinancialDetailsPage: set Have you received education or on-the-job training on the instruments which you are looking to trade to: " + result);
		return result;
	}
}
