package newcrm.pages.auclientpages.Register;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.register.RegisterEntryPage;
import utils.LogUtils;

public class ProdASICRegisterEntryPage extends RegisterEntryPage {
	public ProdASICRegisterEntryPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
	public boolean setCountry(String country) {
		/*if("Australia".equals(country)) {
			driver.get(GlobalProperties.PRODASICURL);
			return true;
		}*/
		Select select = new Select(this.findClickableElemntBy(By.id("country")));
		select.selectByIndex(0);
		LogUtils.info("ProdASICRegisterEntryPage: set country to: " + country);
		return true;
	}
	
	@Override
	protected WebElement getSubmitButton() {
		return this.findClickableElementByXpath("//button[@id='button']/..");
	}
	
	@Override
	public List<String> getCountries(){
		ArrayList<String> result = new ArrayList<>();
		Select select = new Select(this.findClickableElemntBy(By.id("country")));
		List<WebElement> countries = select.getOptions();
		for(WebElement e:countries) {
			String country = e.getAttribute("value");
			if(country != null && country.trim().length()>0) {
				result.add(country.trim());
			}
		}
		return result;
	}
	
	@Override
	public void submit() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.submit();
	}
}
