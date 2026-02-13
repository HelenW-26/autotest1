package adminBase;

import java.util.List;
import java.util.Random;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

public class WholesaleAudit {
	
	WebDriver driver;
	String userEmail;
	Select t;

	/**
	 * Wholesale Audit main page
	 */
	@FindBy(id="searchType") public WebElement searchType;
	
	@FindBy(xpath="//div[@class='button_select btn-group open']/ul/li") public List<WebElement> searchOptions;
	
	@FindBy(id="userQuery") public WebElement userQuery;
	
	@FindBy(id="query") public WebElement searchButton;
	
	
	
	
	
	//Search result table rows
	@FindBy(xpath="//table[@id='table']/tbody/tr") public List<WebElement> trs;
	
	@FindBy(xpath="//table[@id='table']/tbody/tr[1]/td[8]") public WebElement status;
	
	/**
	 * Wealthy Audit page
	 */
	@FindBy(id="wealthType") public WebElement wealthType;
	
	
	@FindBy(xpath ="//input[@id='issue_date']") public WebElement issueDateBox;
	
	@FindBy(xpath ="//input[@id='expire_date']") public WebElement expireDateBox;
	
	@FindBy(xpath ="//textarea[@id='processed_notes']") public WebElement processedNotesBox;
	
	@FindBy(xpath ="//div[@id='datetimepicker1']/span/i") public WebElement issueDatePick;
	
	@FindBy(xpath="//div[@class='datepicker-days']/table/thead/tr[1]/th[2]") public  List<WebElement> currentMonth;
	
	@FindBy(xpath="//div[@class='datepicker-days']/table/tbody/tr/td") public List<WebElement> days;
	
	
	@FindBy(xpath="//div[@class='datepicker-days']/table/thead/tr[1]/th[1]") public  List<WebElement> preBtn;
	
	@FindBy(xpath="//button[@class='btn btn-warning']") public WebElement pendingBtn;
	@FindBy(xpath="//button[@class='btn btn-danger']") public WebElement rejectBtn;
	@FindBy(xpath="//button[@class='btn btn-success']") public WebElement completeBtn;
	
	
	/**
	 * Constructor
	 */
	
	public WholesaleAudit(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}	
	
	/**
	 * Select type to search
	 */
	public void selectSearchType(String selectedType) {
		searchType.click();
		//select the email to search
		for (WebElement option : searchOptions ) {
			if(option.getText().trim().equals(selectedType)) {
				option.click();
			}
		}
	}
	
	/**
	 * Enter Client Email to search
	 */
	public void setEmail(String clientEmail) {
		userQuery.sendKeys(clientEmail);
	}
	
	/**
	 * Click search button
	 */
	public void clickSearchButton() {
		searchButton.click();
	}
	
	
	/**
	 * search audit task by client's email and click on audit
	 */
	public void operationAudit(String clientEmail) throws Exception {
		// Enter client's email to search
		selectSearchType ("Email");
		setEmail (clientEmail);
		clickSearchButton();
		
		Thread.sleep(5000);
		System.out.println(trs.size());
		//if the search result shows no records:
		if(trs.size()==1 && trs.get(0).getAttribute("class").equals("no-records-found"))
		{
			System.out.println("No Submitted wholesale Records.");
		}
		else
		{
			for(int i=0;i<trs.size();i++)
			{
				String wholesaleOperation = trs.get(i).findElement(By.cssSelector("td:last-of-type a")).getText();
				
				System.out.println(wholesaleOperation);
				
				if (wholesaleOperation.equalsIgnoreCase("Audit"))
						{
						//	userEmail =trs.get(i).findElement(By.cssSelector("td:nth-of-type(3)")).getText();
							
							trs.get(i).findElement(By.cssSelector("td:last-of-type a")).click();
						}
			}
		}
	}
	
	/**
	 * Check the status update in wholesale audit main page
	 */
	public void validateStatus(String expectedStatus)throws Exception{
		// Enter client's email to search
		
		Assert.assertTrue(status.getText().equalsIgnoreCase(expectedStatus));
				
				Thread.sleep(5000);
	}
	
	public void wealthyAudit() throws Exception 
	{
		// Select wealthy test type randomly
		Random r = new Random();
		t=new Select(wealthType);
		t.selectByIndex(r.nextInt(t.getOptions().size() - 1) + 1);
		
		//input certificate issue date and expire date
		String issueDate = "2021/01/1";
		
		String expireDate = "2021/12/1";
		
		((JavascriptExecutor)driver).executeScript ("document.getElementById('issue_date').removeAttribute('readonly',0);");
		
		issueDateBox.clear();
		issueDateBox.sendKeys(issueDate);
		
		Thread.sleep(5000);
		((JavascriptExecutor)driver).executeScript ("document.getElementById('expire_date').removeAttribute('readonly',0);");
		
		expireDateBox.clear();
		
		expireDateBox.sendKeys(expireDate);
		
		processedNotesBox.clear();
		
		processedNotesBox.sendKeys("Automation testing");

		/*issueDatePick.click();
				
		String currMonth = currentMonth.get(0).getText();
		
		System.out.println(currMonth);
		while(!currMonth.contains(issueMonth)) {
			preBtn.get(0).click();
			Thread.sleep(1000);
			currMonth = currentMonth.get(0).getText();
			
		}
		
		Thread.sleep(5000);
		
		for (WebElement day : days)
		{
			String x = day.getText().trim();
			if(x.equals(issueDate)) {
				day.click();
			}
		}
		*/
		Thread.sleep(5000);
		
		
	}
	
	public void sophisticatedAudit() throws Exception
	{
		processedNotesBox.clear();
		processedNotesBox.sendKeys("Automation testing");
	}
	
	public void wealthyAuditPending() throws Exception
	{
		
		wealthyAudit();
		pendingBtn.click();
	}
	
	public void wealthyAuditReject() throws Exception
	{
		
		wealthyAudit();
		rejectBtn.click();
	}
	
	public void wealthyAuditComplete() throws Exception
	{
		
		wealthyAudit();
		completeBtn.click();
	}
	
	public void sophisticatedAuditPending() throws Exception
	{
		
		sophisticatedAudit();
		pendingBtn.click();
	}
	
	public void sophisticatedAuditReject() throws Exception
	{
		
		sophisticatedAudit();
		rejectBtn.click();
	}
	
	public void sophisticatedAuditComplete() throws Exception
	{
		
		sophisticatedAudit();
		completeBtn.click();
	}

}
