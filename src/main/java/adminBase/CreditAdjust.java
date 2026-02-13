package adminBase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/*
 * Yanni on 21/10/2020: Credit Adjustment dialog
 */
public class CreditAdjust {

	private WebDriver driver;
	private Select creditType;
	private WebElement creditAmount;
	private WebElement creditValidty;
	private Select creditInNotes;
	private Select creditOutNotes;
	private WebElement confirmButton;
	private WebElement cancelButton;
	private List<WebElement> creditHistory;

	public CreditAdjust(WebDriver tDriver) {
		driver = tDriver;
	}

	// index =0: depost; index = 1, withdraw
	public Select getCreditType() {

		try {
			creditType = new Select(driver.findElement(By.id("type")));

		} catch (Exception e) {
			System.out.println("Get Credit Type error.");
		}

		return creditType;
	}

	// Input credits
	public WebElement getCreditAmount() {

		try {

			creditAmount = driver.findElement(By.id("amount"));

		} catch (Exception e) {
			System.out.println("Get Credit Amount error.");
		}

		return creditAmount;
	}

	// Select Validity
	public WebElement getValidityDate() throws InterruptedException {

		try {
			creditValidty = driver.findElement(By.id("interviewDate1"));

		} catch (ElementNotInteractableException e) {
			System.out.println("No validity date on MT5 account");
		} catch (Exception e) {
			System.out.println("Get Credit Validity error.");
		}
		Thread.sleep(500);

		return creditValidty;
	}

	
	  //Get CreditIn Notes element
	  public Select getCreditInNotes () throws InterruptedException
	  {
	  
	  try {
	  
		  creditInNotes = new Select(driver.findElement(By.id("creditInRemark")));	  
	  
	  }catch(Exception e)
	  {
		  System.out.println("Select Notes error.");
	  }
	  
	  return creditInNotes;
	  }
	 
	  //Get CreditOut Notes element creditOutRemark
	  public Select getCreditOutNotes () throws InterruptedException
	  {
	  
	  try {
	  
	 	creditOutNotes = new Select(driver.findElement(By.id("creditOutRemark")));
	  
	  }catch(Exception e)
	  {
		  System.out.println("Select Notes error.");	
	  }
	  
	  return creditOutNotes;
	  }

	public List<WebElement> getCreditHistory() {
		try {
			creditHistory = driver.findElements(By.cssSelector("table#recordTable>tbody>tr"));
		} catch (Exception e) {
			System.out.println("Getting Credit Hisotry Error");
		}

		return creditHistory;

	}

	public WebElement getConfirmBtn() {
		try {
			confirmButton = driver
					.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(1)"));
		} catch (Exception e) {
			System.out.println("Getting Confirm button Error");
		}

		return confirmButton;
	}

	public WebElement getCancelBtn() {
		try {
			cancelButton = driver
					.findElement(By.cssSelector("div.bootstrap-dialog-footer-buttons button:nth-of-type(2)"));
		} catch (Exception e) {
			System.out.println("Getting Cancel button Error");
		}

		return confirmButton;
	}

	// Select Validity
	public Boolean inputValidityDate() throws InterruptedException {
		Boolean result = false;
		// Get current date and input date
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String dateFormatted = dateFormat.format(new Date());
		System.out.println("Credit Validity Date : " + dateFormatted);

		try {
			this.getValidityDate().sendKeys(dateFormatted);
			result = true;
		} catch (ElementNotInteractableException e) {
			System.out.println("No validity date on MT5 account");
		} catch (Exception e) {
			System.out.println("Input Credit Validity error.");
		}
		Thread.sleep(500);

		return result;
	}
}
