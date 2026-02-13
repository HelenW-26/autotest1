package clientBase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ipNavigationBar {
	WebDriver driver;
	String Brand;
	public ipNavigationBar(WebDriver driver, String Brand){
		this.driver = driver;
		this.Brand = Brand;
	}
	
	public WebElement getDashboard() {
		WebElement dashboard = driver.findElement(By.xpath("//span[contains(translate(text(),'shboard','SHBOARD'),'DASHBOARD')]"));
		return dashboard;
	}
	
	public WebElement getLanguageList() {
		WebElement languageLIst = null;
		switch (Brand.toLowerCase()) {
		case "fsa":
		case "svg":
			languageLIst = driver.findElement(By.xpath("//li[contains(@class,'fr')]/div/div/img"));
			break;
		default:
				languageLIst = driver.findElement(By.xpath("//li[contains(@class,'fr')]/div/img"));
			
		}
		
		return languageLIst;
	}
	
	public String getPageTitle() {
		String pageTitle = "";
		switch (Brand.toLowerCase()) {
		case "fsa":
		case "svg":
			pageTitle = "IB Portal";
			break;
		default:
			pageTitle = "Secure IB Portal";
			
		}
		
		return pageTitle;
	}

}
