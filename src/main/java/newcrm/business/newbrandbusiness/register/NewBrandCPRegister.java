package newcrm.business.newbrandbusiness.register;

import org.openqa.selenium.WebDriver;

import newcrm.business.businessbase.CPRegister;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.pages.newbrandclientpages.register.NewBrandAccountConfigurationPage;
import newcrm.pages.newbrandclientpages.register.NewBrandFinancialDetailsPage;
import newcrm.pages.newbrandclientpages.register.NewBrandPersonalDetailsPage;

public class NewBrandCPRegister extends CPRegister {
	protected NewBrandPersonalDetailsPage npdpage;//step two
	protected NewBrandFinancialDetailsPage nfdpage;//step three
	protected NewBrandAccountConfigurationPage nacpage;//step four
	
	public NewBrandCPRegister(WebDriver driver, String url) {
		super(driver,url);
		npdpage = new NewBrandPersonalDetailsPage(driver);
		nfdpage = new NewBrandFinancialDetailsPage(driver);
		nacpage = new NewBrandAccountConfigurationPage(driver);
	}
	
	@Override
	protected void setUpPDpage() {
		pdpage = new NewBrandPersonalDetailsPage(driver);
	}
	
	@Override
	protected void setUpFDpage() {
		fdpage = new NewBrandFinancialDetailsPage(driver);
	}
	
	@Override
	public void fillFinacialPage() {
		userdetails.put("Employment Status", nfdpage.setEmploymentStatus());
		userdetails.put("Estimated Annual Income", nfdpage.setEstimatedAnnualIncome());
		userdetails.put("Estimated Savings and Investments", nfdpage.setEstimatedSavingsAndInvestments());
		userdetails.put("Estimated Intended Deposit", nfdpage.setEstimatedIntendedDeposit());
		userdetails.put("Source of Funds", nfdpage.setSourceOfFunds());
		userdetails.put("Industry", nfdpage.setIndustry());
		userdetails.put("OTC Transaction", nfdpage.setOTCTransaction());
		userdetails.put("Financial Org", nfdpage.setFinancialOrg());
		userdetails.put("Investment Experience", nfdpage.setInvestmentExperience());
		userdetails.put("Number Of Trades Per Week", nfdpage.setNumberOfTradesPerWeek());
		userdetails.put("Trading Amount Per Week", nfdpage.setTradingAmountPerWeek());
	}
	
	@Override
	public boolean fillAccountPage(PLATFORM platform) {
		userdetails.put("Platform", nacpage.setPlatForm());
		userdetails.put("Account Type", nacpage.setAccountType());
		userdetails.put("Currency", nacpage.setCurrency());
		nacpage.tickBox();
		return true;
	}
}
