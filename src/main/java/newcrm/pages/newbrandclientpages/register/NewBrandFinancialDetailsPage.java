package newcrm.pages.newbrandclientpages.register;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.FinancialDetailsPage;
import vantagecrm.Utils;

public class NewBrandFinancialDetailsPage extends FinancialDetailsPage {

	public NewBrandFinancialDetailsPage(WebDriver driver) {
		super(driver);
	}

	
	@Override
	public String getPageTitle() {
		return this.findVisibleElemntByXpath("//div[@class='title'][1]//strong").getText();
	}

	@Override
	public String setEmploymentStatus() {
		WebElement e = this.findClickableElemntBy(By.xpath("//div[@data-testid='question_traderreg2employmentstatus']"));
		e.click();
		String result = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
		if(result!=null) {
			GlobalMethods.printDebugInfo("FinancialDetailsPage: set Employment Status to: " + result);
		}else
		{
			GlobalMethods.printDebugInfo("ERROR: FinancialDetailsPage: set Employment Status failed!" );
			e.click();
			return null;
		}
		return result;
	}
	
	@Override
	public String setEstimatedAnnualIncome() {
		WebElement e = this.findClickableElemntBy(By.xpath("//div[@data-testid='question_traderreg2annualincome']"));
		e.click();
		String result = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
		if(result!=null) {
			GlobalMethods.printDebugInfo("FinancialDetailsPage: set Estimated Annual Income to: " + result);
		}else
		{
			GlobalMethods.printDebugInfo("ERROR: FinancialDetailsPage: set Estimated Annual Income failed!" );
			e.click();
			return null;
		}
		return result;
	}
	
	@Override
	public String setEstimatedSavingsAndInvestments() {
		WebElement e = this.findClickableElemntBy(By.xpath("//div[@data-testid='question_traderreg2savingsandinvestments']"));
		e.click();
		String result = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
		if(result!=null) {
			GlobalMethods.printDebugInfo("FinancialDetailsPage: set Estimated Savings And Investments to: " + result);
		}else
		{
			GlobalMethods.printDebugInfo("ERROR: FinancialDetailsPage: set Estimated Savings And Investments failed!" );
			e.click();
			return null;
		}
		return result;
	}
	
	@Override
	public String setEstimatedIntendedDeposit() {
		WebElement e = this.findClickableElemntBy(By.xpath("//div[@data-testid='question_traderreg2depositamount']"));
		e.click();
		String result = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
		if(result!=null) {
			GlobalMethods.printDebugInfo("FinancialDetailsPage: set Estimated Intended Deposit to: " + result);
		}else
		{
			GlobalMethods.printDebugInfo("ERROR: FinancialDetailsPage: set Estimated Intended Deposit failed!" );
			e.click();
			return null;
		}
		return result;
	}
	
	@Override
	public String setSourceOfFunds() {
		WebElement e = this.findClickableElemntBy(By.xpath("//div[@data-testid='question_traderreg2sourceoffunds']"));
		e.click();
		String result = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
		if(result!=null) {
			GlobalMethods.printDebugInfo("FinancialDetailsPage: set Source Of Funds to: " + result);
		}else
		{
			GlobalMethods.printDebugInfo("ERROR: FinancialDetailsPage: set Source Of Funds failed!" );
			e.click();
			return null;
		}
		return result;
	}

	public String setIndustry() {
		WebElement e = this.findClickableElemntBy(By.xpath("//div[@data-testid='question_traderreg2industry']"));
		e.click();
		String result = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
		if(result!=null) {
			GlobalMethods.printDebugInfo("FinancialDetailsPage: set Industry to: " + result);
		}else
		{
			GlobalMethods.printDebugInfo("ERROR: FinancialDetailsPage: set Industry failed!" );
			e.click();
			return null;
		}
		return result;
	}
	
	public String setOTCTransaction() {
		WebElement e = this.findClickableElemntBy(By.xpath("//div[@data-testid='question_traderreg2otctransaction']"));
		e.click();
		String result = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
		if(result!=null) {
			GlobalMethods.printDebugInfo("FinancialDetailsPage: set OTC Transaction to: " + result);
		}else
		{
			GlobalMethods.printDebugInfo("ERROR: FinancialDetailsPage: set OTC Transaction failed!" );
			e.click();
			return null;
		}
		return result;
	}
	
	public String setFinancialOrg() {
		WebElement e = this.findClickableElemntBy(By.xpath("//div[@data-testid='question_traderreg2financialorg']"));
		e.click();
		String result = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
		if(result!=null) {
			GlobalMethods.printDebugInfo("FinancialDetailsPage: set Financial Org to: " + result);
		}else
		{
			GlobalMethods.printDebugInfo("ERROR: FinancialDetailsPage: set Financial Org failed!" );
			e.click();
			return null;
		}
		return result;
	}
	
	public String setInvestmentExperience() {
		WebElement e = this.findClickableElemntBy(By.xpath("//div[@data-testid='question_traderreg2investmentex']"));
		e.click();
		String result = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
		if(result!=null) {
			GlobalMethods.printDebugInfo("FinancialDetailsPage: set Investment Experience to: " + result);
		}else
		{
			GlobalMethods.printDebugInfo("ERROR: FinancialDetailsPage: set Investment Experience failed!" );
			e.click();
			return null;
		}
		return result;
	}
	
	@Override
	public String setNumberOfTradesPerWeek() {
		WebElement e = this.findClickableElementByXpath("//main[1]/div[1]/div[1]/div[1]/div[2]/div[1]/form[1]/div[4]/div[1]/div[2]");
		moveElementToVisible(e);
		e.click();
		String result = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
		if(result!=null) {
			GlobalMethods.printDebugInfo("FinancialDetailsPage: set Number Of Trades to: " + result);
		}else
		{
			GlobalMethods.printDebugInfo("ERROR: FinancialDetailsPage: set Number Of Trades failed!" );
			e.click();
			return null;
		}
		return result;
	}
	
	@Override
	public String setTradingAmountPerWeek() {
		WebElement e = this.findClickableElemntBy(By.xpath("//main[1]/div[1]/div[1]/div[1]/div[2]/div[1]/form[1]/div[4]/div[2]/div[2]/div[1]/div[1]"));
		e.click();
		String result = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
		if(result!=null) {
			GlobalMethods.printDebugInfo("FinancialDetailsPage: set Trading Amount to: " + result);
		}else
		{
			GlobalMethods.printDebugInfo("ERROR: FinancialDetailsPage: set Trading Amount failed!" );
			e.click();
			return null;
		}
		return result;
	}
	
}
