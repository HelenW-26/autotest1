package newcrm.pages.clientpages.elements;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

public class TransferPageElements {

	@CacheLookup
	@FindAll({
		@FindBy(xpath="//div[@data-testid='fromAccount']/div/input"),//PUG
		@FindBy(id="fromAccount")//VT
		}
	)
	public WebElement e_acc_from;
	
	@CacheLookup
	@FindAll({
		@FindBy(xpath="//div[@data-testid='toAccount']/div/input"),//PUG
		@FindBy(id="toAccount")//VT
		}
	)
	public WebElement e_acc_to;
	
	@CacheLookup
	@FindAll({
		@FindBy(xpath="//input[@data-testid='numericInput']")//VT
		}
	)
	public WebElement e_amount;
	
	@CacheLookup
	@FindAll({
		@FindBy(xpath="//button[@data-testid='submit']")//VT
		}
	)
	public WebElement e_submit;
}
