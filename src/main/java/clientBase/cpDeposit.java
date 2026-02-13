package clientBase;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class cpDeposit {
	
	private WebDriver driver;
	private String Brand;
	
	public cpDeposit(WebDriver driver, String Brand)
	{
		this.driver = driver;
		this.Brand = Brand;
		
	}
	
	//Credit Card Deposit: element of Credit Card
	public WebElement getCC()
	{
		WebElement tmpEle=null;
		
		switch(Brand)
		{
			case "vt":
				tmpEle = driver.findElement(By.xpath("//span[contains(text(),'Credit/Debit card')]"));
				break;

				
			default:
				tmpEle = driver.findElement(By.xpath("//div[contains(text(),'Credit/Debit Card')]"));
				
		}
		
		return tmpEle;		
		
	}
	
	
	//Credit Card Deposit: Account Number
	public WebElement getCCAccountNumber()
	{
		WebElement tmpEle=null;
		
		switch(Brand)
		{
			case "vt":
			case "fsa":
			case "svg":
				tmpEle = driver.findElement(By.id("accountNumber"));
				break;
				
			default:
				tmpEle = driver.findElement(By.cssSelector("div.el-form-item.el-form-item--feedback.is-required:nth-child(1) div.el-select div> input"));
				
		}
		
		return tmpEle;		
		
	}
	
	/* Credit Card Deposit: Amount
	 * Note: This is a generic amount element. Different from amount with (USD) label.
	 */
	public WebElement getAmount()
	{
		WebElement tmpEle=null;
		
		switch(Brand)
		{

			default:
				tmpEle = driver.findElement(By.xpath("//div[label = 'Amount']//input"));
				
		}
		
		return tmpEle;		
		
	}
	
	/* Credit Card Deposit: Card List 
	 * Note: This is CC card list elements and returns a list.
	 */
	public List<WebElement> getCardList()
	{
		List<WebElement> tmpEle=null;
		
		switch(Brand)
		{
			case "vt":
				tmpEle = driver.findElements(By.id("card"));
				break;
			case "fsa":
			case "svg":
				tmpEle = driver.findElements(By.xpath("//div[label = 'Fund Now by Credit / Debit Card']//input"));
				break;
				
			default:
				tmpEle = driver.findElements(By.xpath("//div[@class='el-form-item el-form-item--feedback is-required']//input[@placeholder='Select']"));
				
		}
		
		return tmpEle;		
		
	}

	/* Credit Card Deposit: Card List Element
	 * Note: This is CC card list element and returns the webElement.
	 */
	public WebElement getCardListElement()
	{
		WebElement tmpEle=null;
		
		switch(Brand)
		{
			case "vt":
				tmpEle = driver.findElement(By.id("card"));
				break;
			case "fsa":
			case "svg":
				tmpEle = driver.findElement(By.xpath("//div[label = 'Fund Now by Credit / Debit Card']//input"));
				break;
				
			default:
				tmpEle = driver.findElement(By.xpath("//div[@class='el-form-item el-form-item--feedback is-required']//input[@placeholder='Select']"));
				
		}
		
		return tmpEle;		
		
	}
	
	/* Credit Card Deposit: Card Number
	 */
	public WebElement getCCInputNumber()
	{
		WebElement tmpEle=null;
		
		switch(Brand)
		{
			case "vt":
				tmpEle = driver.findElement(By.id("creditCard"));
				break;
				
			case "fsa":
			case "svg":
				tmpEle = driver.findElement(By.xpath("//div[label = 'Credit Card Number']//input"));
				break;
				
			default:
				tmpEle = driver.findElement(By.cssSelector("form.el-form.el-form--label-top div.fl>div:nth-of-type(4) input"));
				
		}
		
		return tmpEle;		
		
	}
	
	
	/* Credit Card Deposit: Card Holder Name
	 */
	public WebElement getCCCardName()
	{
		WebElement tmpEle=null;
		
		switch(Brand)
		{
			case "vt":
				tmpEle = driver.findElement(By.id("nameCard"));
				break;
				
			case "fsa":
			case "svg":
				tmpEle = driver.findElement(By.xpath("//p[@class='required_label']/following-sibling::div//input"));
				break;
				
			default:
				tmpEle = driver.findElement(By.cssSelector("div.fl div:nth-of-type(5) input.el-input__inner"));
				
		}
		
		return tmpEle;		
		
	}
	
	
	/* Credit Card Deposit: Card Number middle 6
	 */
	public WebElement getCCMiddle6Num()
	{
		WebElement tmpEle=null;
		
		switch(Brand)
		{
			case "vt":
				tmpEle = driver.findElement(By.cssSelector("div.ccMultipleColumn div.el-form-item:nth-child(2)  div > input"));
				break;
				
			case "fsa":
			case "svg":
				tmpEle = driver.findElement(By.xpath("//div[@class='ccNumber clearfix']//div//div//input"));
				break;
				
			default:
				tmpEle = driver.findElement(By.xpath(
						"//div[@class='el-form-item creadit_input creadit_input_margin el-form-item--feedback is-required']//input"));
				
		}
		
		return tmpEle;		
		
	}
	
	
	/* Credit Card Deposit: Card Number CVV
	 */
	public WebElement getCVV()
	{
		WebElement tmpEle=null;
		
		switch(Brand)
		{
			case "vt":
				tmpEle = driver.findElement(By.id("securityCode"));
				break;
				
			case "fsa":
			case "svg":
				tmpEle = driver.findElement(By.xpath("//label[@for='securityCode']/following-sibling::div//input"));
				break;
				
			default:
				tmpEle = driver.findElement(By.xpath(
						"//div[@class='el-form-item security el-form-item--feedback is-required']//input"));
				
		}
		
		return tmpEle;		
		
	}
	
	/* Credit Card Deposit: Notes
	 */
	public WebElement getCCNotes()
	{
		WebElement tmpEle=null;
		
		switch(Brand)
		{
			case "vt":
				tmpEle = driver.findElement(By.id("notes"));
				break;
				
			default:
				tmpEle = driver.findElement(By.xpath(
						"//div[label = 'Important notes']//input"));
				
		}
		
		return tmpEle;		
		
	}
	
	//International bank transfer Deposit: element of International bank transfer
		public WebElement getI18N()
		{
			WebElement tmpEle=null;
			
			switch(Brand)
			{
				case "vt":
					tmpEle = driver.findElement(By.xpath("//span[contains(text(),'International bank transfer')]"));
					break;
					
				default:
					tmpEle = driver.findElement(By.xpath("//div[contains(text(),'International Bank Transfer')]"));
					
			}
			
			return tmpEle;		
			
		}
	
		/* Get Back To Home button's element after submission.
		 */
		public WebElement getB2H()
		{
			WebElement tmpEle=null;
			
			switch(Brand)
			{
				case "vt":
				case "fsa":
				case "svg":
					tmpEle = driver.findElement(By.xpath("//a[contains(text(),'Back To Home Page')]"));
					break;
					
				default:
					tmpEle = driver.findElement(By.xpath("//a[@class='el-button btn_blue']"));
					
			}
			
			return tmpEle;		
			
		}
	
		
		//Fasapay Deposit: element of Fasapay Deposit
		public WebElement getFasapay()
		{
			WebElement tmpEle=null;
			
			switch(Brand)
			{
			
				case "vt":
					tmpEle = driver.findElement(By.xpath("//span[contains(translate(., 'Fasapay deposit', 'Fasapay Deposit'),'Fasapay Deposit')]"));
					break;
				default:
					tmpEle = driver.findElement(By.xpath("//div[contains(text(),'Fasapay Deposit')]"));
					
					
			}
			
			return tmpEle;		
			
		}
		
		//Neteller Deposit: element of Neteller Deposit
		public WebElement getNeteller()
		{
			WebElement tmpEle=null;
			
			switch(Brand)
			{

				case "vt":
					tmpEle = driver.findElement(By.xpath("//span[contains(text(),'Neteller deposit')]"));
					break;
				default:
					tmpEle = driver.findElement(By.xpath("//div[contains(text(),'Neteller')]"));
					
					
			}
			
			return tmpEle;		
			
		}

		//Skrill Deposit: element of Skrill Deposit
		public WebElement getSkrill()
		{
			WebElement tmpEle=null;
			
			switch(Brand)
			{

				case "vt":
					tmpEle = driver.findElement(By.xpath("//span[contains(text(),'Skrill')]"));
					break;
				default:
					tmpEle = driver.findElement(By.xpath("//div[contains(text(),'Skrill')]"));
					
					
			}
			
			return tmpEle;		
			
		}
		

		//Mobile Deposit: element of Mobile Pay
		public WebElement getMobilepay()
		{
			WebElement tmpEle=null;
			
			switch(Brand)
			{
				case "vt":
					tmpEle = driver.findElement(By.xpath("//span[contains(text(),'Mobile Pay')]"));
					break;
					
				default:
					tmpEle = driver.findElement(By.xpath("//div[contains(text(),'Mobile Pay')]"));
					
			}
			
			return tmpEle;		
			
		}

		
		
		/* Mobile/Thailand.. Deposit: Amount(USD)
		 * Note: Amount with (USD) label.
		 */
		public WebElement getUSDAmount()
		{
			WebElement tmpEle=null;
			
			switch(Brand)
			{
				case "vt":
				case "fsa":
				case "svg":
					tmpEle = driver.findElement(By.xpath("//div[label = 'Amount(USD)']//input"));
					break;
					
				default:
					tmpEle = driver.findElement(By.cssSelector("div.form_list ul.clearfix:nth-child(1) div > input"));
					
			}
			
			return tmpEle;		
			
		}
	
		

		/* Mobile/Thailand.. Deposit: Exchange Rate
		 */
		public WebElement getExRate()
		{
			WebElement tmpEle=null;
			
			switch(Brand)
			{
				case "vt":
				case "fsa":
				case "svg":
					tmpEle = driver.findElement(By.xpath("//li//p[contains(text(),'Exchange')]//span"));
					break;
					
				default:
					tmpEle = driver.findElement(By.cssSelector("div.form_box ul.clearfix:nth-of-type(2) li.fr.data p:nth-of-type(1) span:nth-of-type(2)"));
					
			}
			
			return tmpEle;		
			
		}
		
		/* Mobile Deposit: Exchange Amount for CNY
		 */
		public WebElement getExAmount()
		{
			WebElement tmpEle=null;
			
			switch(Brand)
			{
				case "vt":
				case "fsa":
				case "svg":
					tmpEle = driver.findElement(By.xpath("//li//p[2]//span"));
					break;
					
				default:
					tmpEle = driver.findElement(By.cssSelector("div.form_box ul.clearfix:nth-of-type(2) li.fr.data p:nth-of-type(2) span:nth-of-type(2)"));
					
			}
			
			return tmpEle;		
			
		}

		//Unionpay Reansfer: element of UnionPay P2P Transfer
		public WebElement getUP2P()
		{
			WebElement tmpEle=null;
			
			switch(Brand)
			{
				case "fsa":
				case "svg":
					tmpEle = driver.findElement(By.xpath("//div[contains(text(),'China Union Pay Transfer')]"));
					break;
					
				case "vt":
					tmpEle = driver.findElement(By.xpath("//span[contains(text(),'China UnionPay transfer')]"));
					break;	
					
				default:
					tmpEle = driver.findElement(By.xpath("//div[@class='bottom'][contains(text(),'China Union Pay Transfer')]"));
					
			}
			
			return tmpEle;		
			
		}
		
		/* Other Deposit: Important Notes
		 */
		public WebElement getImportantNotes()
		{
			WebElement tmpEle=null;
			
			switch(Brand)
			{
		
				default:
					tmpEle = driver.findElement(By.xpath(
							"//div[label = 'Important notes']//input"));
					
			}
			
			return tmpEle;		
			
		}
		
		/* UnionPay/P2P Deposit: UnionPay Card Holder Name
		 */
		public WebElement getUP2PCardHolderName()
		{
			WebElement tmpEle=null;
			
			switch(Brand)
			{
				case "vt":
					tmpEle = driver.findElement(By.xpath("//div[label = 'UnionPay card holders name:']//input"));
					break;
					
				default:
					tmpEle = driver.findElement(By.xpath("//div[label = 'UnionPay Card Holders Name:']//input"));
					
			}
			
			return tmpEle;		
			
		}	
		
		//Canada E-transfer Deposit
		public WebElement getInterac()
		{
			WebElement tmpEle=null;
			
			switch(Brand)
			{
				case "vt":
					tmpEle = driver.findElement(By.xpath("//span[contains(text(),'Interac E-Transfer')]"));
					break;
					
				default:
					tmpEle = driver.findElement(By.xpath("//div[contains(text(),'Interac e-Transfer')]"));
					
			}
			
			return tmpEle;		
			
		}
		
		
		//Get Thailand deposit
		public WebElement getThaiDeposit()
		{
			WebElement tmpEle=null;
			
			switch(Brand)
			{
				case "vt":
					tmpEle = driver.findElement(By.xpath("//span[contains(text(),'Thailand instant bank wire transfer')]"));
					break;
					
				default:
					tmpEle = driver.findElement(By.xpath("//div[contains(text(),'Thailand Instant Bank Wire Transfer')]"));
					
			}
			
			return tmpEle;		
			
		}
		
		//Get Malaysia Instant Bank Transfer
		public WebElement getMalayDeposit()
		{
			WebElement tmpEle=null;
			
			switch(Brand)
			{

				default:
					tmpEle = driver.findElement(By.xpath("//div[contains(text(),'Malaysia Instant Bank Transfer')]"));
					
			}
			
			return tmpEle;		
			
		}
		
		
		//Get Vietnam Instant Bank Transfer
		public WebElement getVietDeposit()
		{
			WebElement tmpEle=null;
			
			switch(Brand)
			{
				case "vt":
					tmpEle = driver.findElement(By.xpath("//span[contains(text(),'Vietnam instant bank wire transfer')]"));
					break;
			
				default:
					tmpEle = driver.findElement(By.xpath("//div[contains(text(),'Vietnam Instant Bank Wire Transfer')]"));
					
			}
			
			return tmpEle;		
			
		}
}
