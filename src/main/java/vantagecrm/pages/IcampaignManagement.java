package vantagecrm.pages;

import java.util.List;

import org.openqa.selenium.WebElement;

public interface IcampaignManagement {

	public WebElement getSearchButton();
	public WebElement getCampaignType();
	public WebElement getPromotionName();
	public WebElement getSearchBar();
	public WebElement getExportButton();
	
	public List<WebElement> getUsers();
	public List<WebElement> getAccounts();
	
	public Boolean setSearchOption(String option);
	public Boolean clearSearchOption();
	
	public Boolean clearKeyword();
	public Boolean setKeyword();
}
