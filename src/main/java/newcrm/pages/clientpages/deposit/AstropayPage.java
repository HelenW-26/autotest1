package newcrm.pages.clientpages.deposit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


/**
 * This class comes from SkrillpayPage, both of them have the same web elements
 * @author CRM QA Team
 *
 */

public class AstropayPage extends SkrillPayPage {

	public AstropayPage(WebDriver driver) {
		super(driver);
	}
	
	public String getDepoitAmountFromAstropay() {
		//WebElement info = this.findVisibleElemntBy(By.xpath("(//span[contains(text(),'Complete')])[1]"));
		WebElement info = this.findVisibleElemntBy(By.xpath("//div[contains(text(),'Continue')]"));
		if(info!=null) {
			
			return info.getText();//it's not the amount
		}
		return null;
	}
	
	
	/***
	 * Return to CP by clicking return link on Astropay 3rd party page
	 */
	public void returnToCPFrom3rdParty() {
		//WebElement returnLink = this.findClickableElemntBy(By.cssSelector("button.MuiButtonBase-root.MuiButton-root.MuiButton-text.jss25 > span.MuiButton-label"));
		//WebElement returnLink = this.findClickableElementByXpath("//span[contains(text(),'Return to') or contains(text(),'返回')]");
		//this.moveElementToVisible(returnLink);
		//returnLink.click();
		driver.navigate().back();
		this.waitLoading(); 
	}
}
