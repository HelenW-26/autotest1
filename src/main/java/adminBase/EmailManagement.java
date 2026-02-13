package adminBase;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class EmailManagement {
	
	WebDriver driver;
	/**
	 * All elements
	 */
	@FindBy(id="query") public WebElement searchBtn;
	@FindBy(xpath="//button[@name='clearSearch']") public WebElement clearBtn;
	
	@FindBy(xpath="//table[@id='table']/thead/tr[1]/th[2]/div[2]/div/input") public WebElement recipientsSearchBox;
	
	@FindBy(xpath="//table[@id='table']/thead/tr[1]/th[3]/div[2]/div/input") public WebElement accountSearchBox;
	
	@FindBy(xpath="//table[@id='table']/thead/tr[1]/th[4]/div[2]/div/input") public WebElement subjectSearchBox;
	
	@FindBy(xpath="//table[@id='table']/thead/tr[1]/th[5]/div[2]/div/input") public WebElement callTemplateSearchBox;
	
	@FindBy(xpath="//table[@id='table']/thead/tr[1]/th[6]/div[2]/div") public WebElement sendStatusSelection;
	
	@FindBy(xpath="//div[@class='ms-drop bottom']/ul/li") public List<WebElement> statusOptions;
	
	@FindBy(xpath="//table[@id='table']/thead/tr[1]/th[7]/div[2]/div/input") public WebElement resendCountSearchBox;
	
	//Search result table rows
	@FindBy(xpath="//table[@id='table']/tbody/tr") public List<WebElement> rows;
	
	//Resend email popup window elements
	@FindBy(id="toMail") public WebElement toMailBox;
	
	@FindBy(xpath="//div[3]/div/div/button") public WebElement sendBtn;
	
	@FindBy(xpath="//button[text()='Cancel']") public WebElement cancelBtn;
	
	/**
	 * Constructor
	 */
	
	public EmailManagement(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	/**
	 * Click on search button
	 */
	public void clickSearchButton() {
		searchBtn.click();
	}
	
	/**
	 * Enter Email recipients to search
	 */
	public void setRecipients(String recipientsSearchStr) {
		recipientsSearchBox.sendKeys(recipientsSearchStr);
	}
	
	/**
	 * Enter account to search
	 */
	public void setAccount(String accountSearchStr) {
		accountSearchBox.sendKeys(accountSearchStr);
	}
	
	/**
	 * Enter Email Subject to search
	 */
	public void setEmailSubject(String subjectSearchStr) {
		subjectSearchBox.sendKeys(subjectSearchStr);
	}
	
	/**
	 * Enter Email Call Template to search
	 */
	public void setEmailCallTemplate(String callTemplateSearchStr) {
		subjectSearchBox.sendKeys(callTemplateSearchStr);
	}
	
	/**
	 * Select send status to search
	 */
	public void selectSendStatus(String selectedStatus) {
		sendStatusSelection.click();
		//select the status to search
		for (WebElement option : statusOptions ) {
			if(option.getText().trim().equals(selectedStatus)) {
				option.click();
			}
		}
	}
	
	/**
	 * Resend emails to your provided email
	 * @throws Exception
	 */
	public void resendEmail(String recipientEmail) throws Exception
	{
		
		/* This method is also used to iterate the web table row by row, but it can not find DOM after a row is updated and the page is refreshed
		 * 
		 * for (WebElement row : rows) {
		 * 
		 * 
		 * row.findElement(By.xpath("td[8]/a[@class='send']/i")).click(); String
		 * parentWindow = driver.getWindowHandle();
		 * 
		 * Set <String> windows = driver.getWindowHandles();
		 * 
		 * for (String window:windows) { if (!window.equals(parentWindow)) {
		 * driver.switchTo().window(window); } }
		 * 
		 * Thread.sleep(2000);
		 * 
		 * toMailBox.clear(); toMailBox.sendKeys(recipientEmail); Thread.sleep(2000);
		 * sendBtn.click(); Thread.sleep(2000);
		 * 
		 * }
		 */
		 
		System.out.println("Total No of rows are:"+rows.size());
		
		for (int i=1; i<=rows.size();i++) {
			try {
		driver.findElement(By.xpath("//table[@id='table']/tbody/tr["+(i)+"]/td[8]/a[@class='send']/i")).click();
		
		//Switch to popup window, and resend the email to your personal email box
		  String parentWindow = driver.getWindowHandle();
		  
		  Set <String> windows = driver.getWindowHandles();
		  
		  for (String window:windows) 
		  { 
			  if (!window.equals(parentWindow)) {
		  driver.switchTo().window(window); 
			  } 
		  }
		  
		  Thread.sleep(2000);
		  
		  toMailBox.clear(); toMailBox.sendKeys(recipientEmail); 
		  Thread.sleep(2000);
		  sendBtn.click(); 
		  Thread.sleep(2000);
			}catch(Exception e)
			{
				System.out.println("No matching records found");
			}
		
		}
	
	}
}
				
