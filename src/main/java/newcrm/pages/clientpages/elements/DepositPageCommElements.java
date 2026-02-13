package newcrm.pages.clientpages.elements;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

public class DepositPageCommElements {

	@CacheLookup
	@FindAll({
			@FindBy(xpath="//div[@data-testid='accountNumber']/div/input | //div[@data-testid='formAccount']//img")
		}
	)
	public WebElement e_account;
	
	@CacheLookup
	@FindAll({
			@FindBy(xpath="//button[@data-testid='submit']"),
			@FindBy(xpath="//span[contains(text(),'Submit')]"),
			@FindBy(xpath="//button[@data-testid='continue']")
		}
	)
	public WebElement e_submit;
	
	@CacheLookup
	@FindAll({
		@FindBy(xpath="//input[@data-testid='numericInput']")
		}
	)
	public WebElement e_amount;
	
	@CacheLookup
	@FindAll({
		@FindBy(xpath="//input[@data-testid='applicationNotes']"),
		@FindBy(xpath="//input[@data-testid='imptNotes']"),
		@FindBy(xpath="//input[@data-testid='notes']")
		}
	)
	public WebElement e_notes;

}
