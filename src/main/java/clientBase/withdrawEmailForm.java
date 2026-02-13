package clientBase;


/*Yanni on 19/03/2021 , Funds->Withdraw Funds, Withdraw Page - Skrill part
 * Skrill Email
 * Notes
 * 
*/

import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import vantagecrm.Utils;


public class withdrawEmailForm extends withdrawGeneral {
	
	private WebDriver driver;
	private String Brand;
	private String wdMethod;
	
	public withdrawEmailForm(WebDriver driver, String Brand, String wdMethod)
	{
		super(driver,Brand);
		this.driver = driver;
		this.Brand = Brand;
		this.wdMethod = wdMethod;
		
	}
	
	
	//Get Skrill Email element
	public WebElement getSkrillEmailEle()
	{
		WebElement tmpEle=null;
		
		 switch(Brand.toLowerCase())
		 {
			 case "vt":
				 tmpEle = driver.findElement(By.id("skrillEmial_skrill"));
				 break;
			 case "fsa":
			 case "svg":
				 System.out.println("PUG doesn't support Neteller withdraw");
				 tmpEle = null; //PUG doesn't have skrill withdraw
				 break;
				 
				 default:
					 tmpEle = driver.findElement(By.xpath("//div[@class='el-form-item is-required']//input"));
			 }		 	
		
		return tmpEle;				
	 }
	
	//Get Skrill Notes element
	public WebElement getSkrillNotesEle()
	{
		WebElement tmpEle=null;
		
		 switch(Brand.toLowerCase())
		 {
			 case "vt":
				 tmpEle = driver.findElement(By.id("notes_skrill"));
				 break;
			 case "fsa":
			 case "svg":
				 tmpEle = null; //PUG doesn't have skrill withdraw
				 break;
				 
				 default:
					 tmpEle = driver.findElement(By.xpath("//div[@class='el-form-item']//div[@class='el-input']//input"));
		 }		 	
		
		return tmpEle;				
	 }
	
	
	//Get Neteller Email element
	public WebElement getNetellerEmailEle()
	{
		WebElement tmpEle=null;
		
		 switch(Brand.toLowerCase())
		 {
			 case "vt":
				 tmpEle = driver.findElement(By.id("netellerEmial_neteller"));
				 break;
			 case "fsa":
			 case "svg":
				 System.out.println("PUG doesn't support Neteller withdraw");
				 tmpEle = null; //PUG doesn't have skrill withdraw
				 break;
				 
				 default:
					 tmpEle = driver.findElement(By.xpath("//div[@class='el-form-item is-required']//input"));
			 }		 	
		
		return tmpEle;				
	 }
	
	//Get Neteller Notes element
	public WebElement getNetellerNotesEle()
	{
		WebElement tmpEle=null;
		
		 switch(Brand.toLowerCase())
		 {
			 case "vt":
				 tmpEle = driver.findElement(By.id("notes_neteller"));
				 break;
			 case "fsa":
			 case "svg":
				 tmpEle = null; //PUG doesn't have skrill withdraw
				 break;
				 
				 default:
					 tmpEle = driver.findElement(By.xpath("//div[@class='el-form-item']//div[@class='el-input']//input"));
		 }		 	
		
		return tmpEle;				
	 }
	
	//

	//Get Crypto BTC Email element
	public WebElement getBTCEmailEle()
	{
		WebElement tmpEle=null;
		
		 switch(Brand.toLowerCase())
		 {
			 case "vt":
				 tmpEle = driver.findElement(By.id("address"));
				 break;
			 case "fsa":
			 case "svg":
				 tmpEle = driver.findElements(By.xpath("//div[@id='bitcoinWithdraw']//input")).get(0);
				 break;
				 
				 default:
					 tmpEle = driver.findElement(By.xpath("//div[@class='el-form-item is-required']//input"));
			 }		 	
		
		return tmpEle;				
	 }
	
	//Get Crypto BTC Notes element
	public WebElement getBTCNotesEle()
	{
		WebElement tmpEle=null;
		
		 switch(Brand.toLowerCase())
		 {
			 case "vt":
				 tmpEle = driver.findElement(By.id("notes"));
				 break;
			 case "fsa":
			 case "svg":
				 tmpEle = driver.findElements(By.xpath("//div[@id='bitcoinWithdraw']//input")).get(1);;
				 break;
				 
				 default:
					 tmpEle = driver.findElement(By.xpath("//div[@class='el-form-item']//div[@class='el-input']//input"));
		 }
	
		 return tmpEle;				
	}	

	
	//Get Crypto USDT Notes element
	public WebElement getUSDTNotesEle()
	{
		WebElement tmpEle=null;
		
		 switch(Brand.toLowerCase())
		 {
			 case "vt":
				 tmpEle = driver.findElement(By.id("notes"));
				 break;
			 case "fsa":
			 case "svg":
				 tmpEle = driver.findElements(By.xpath("//div[@id='usdtWithdraw']//input")).get(1);;
				 break;
				 
				 default:
					 tmpEle = driver.findElement(By.xpath("//div[@class='el-form-item']//div[@class='el-input']//input"));
		 }
		 
		 return tmpEle;
	}
		 
		//Get Crypto USDT Email element
		public WebElement getUSDTEmailEle()
		{
			WebElement tmpEle=null;
			
			 switch(Brand.toLowerCase())
			 {
				 case "vt":
					 tmpEle = driver.findElement(By.id("address"));
					 break;
				 case "fsa":
				 case "svg":
					 tmpEle = driver.findElements(By.xpath("//div[@id='usdtWithdraw']//input")).get(0);
					 break;
					 
					 default:
						 tmpEle = driver.findElement(By.xpath("//div[@class='el-form-item is-required']//input"));
				 }		 	
			
			return tmpEle;				
		 }
		
		//Get Bitwallet email element
		public WebElement getBitwalletEmailEle()
		{
			WebElement tmpEle=null;
			
			 switch(Brand.toLowerCase())
			 {
				 case "vt":
					 System.out.println("VT doesn't support Bitwallet");
					 tmpEle = null;
					 break;
				 case "fsa":
				 case "svg":
					 tmpEle = driver.findElements(By.xpath("//div[@id='bitwalletForm']//input")).get(0);
					 break;
					 
					 default:
						 tmpEle = driver.findElement(By.xpath("//div[@class='el-form-item is-required']//input"));
				 }		 	
			
			return tmpEle;				
		 }
		
		//Get Bitwallet Notes element
		public WebElement getBitwalletNotesEle()
		{
			WebElement tmpEle=null;
			
			 switch(Brand.toLowerCase())
			 {
				 case "vt":
					 System.out.println("VT doesn't support Bitwallet");
					 tmpEle = null;
					 break;
				 case "fsa":
				 case "svg":
					 tmpEle = driver.findElements(By.xpath("//div[@id='bitwalletForm']//input")).get(1);;
					 break;
					 
					 default:
						 tmpEle = driver.findElement(By.xpath("//div[@class='el-form-item']//div[@class='el-input']//input"));
			 }
		
			 return tmpEle;				
		}
		
		//Get Sticpay email element
		public WebElement getSticpayEmailEle()
		{
			WebElement tmpEle=null;
			
			 switch(Brand.toLowerCase())
			 {
				 case "vt":
					 System.out.println("VT doesn't support Sticpay");
					 tmpEle = null;
					 break;
				 case "fsa":
				 case "svg":
					 tmpEle = driver.findElements(By.xpath("//div[@id='sticpayForm']//input")).get(0);
					 break;
					 
					 default:
						 tmpEle = driver.findElement(By.xpath("//div[@class='el-form-item is-required']//input"));
				 }		 	
			
			return tmpEle;				
		 }
		
		//Get Sticpay Notes element
		public WebElement getSticpayNotesEle()
		{
			WebElement tmpEle=null;
			
			 switch(Brand.toLowerCase())
			 {
				 case "vt":
					 System.out.println("VT doesn't support Sticpay");
					 tmpEle = null;
					 break;
				 case "fsa":
				 case "svg":
					 tmpEle = driver.findElements(By.xpath("//div[@id='sticpayForm']//input")).get(1);;
					 break;
					 
					 default:
						 tmpEle = driver.findElement(By.xpath("//div[@class='el-form-item']//div[@class='el-input']//input"));
			 }
		
			 return tmpEle;				
		}	
		
		//Get Astropay email element
		public WebElement getAstropayEmailEle()
		{
			WebElement tmpEle=null;
			
			 switch(Brand.toLowerCase())
			 {
	
				 case "fsa":
				 case "svg":
					 tmpEle = driver.findElements(By.xpath("//div[@id='astropayForm']//input")).get(0);
					 break;
					 
					 default:
						 System.out.println(Brand + " doesn't support Astropay");
						 tmpEle = null;
				 }		 	
			
			return tmpEle;				
		 }
		
		//Get Astropay Notes element
		public WebElement getAstropayNotesEle()
		{
			WebElement tmpEle=null;
			
			 switch(Brand.toLowerCase())
			 {
	
				 case "fsa":
				 case "svg":
					 tmpEle = driver.findElements(By.xpath("//div[@id='astropayForm']//input")).get(1);;
					 break;
					 
				 default:
					 System.out.println(Brand + " doesn't support Astropay");
					 tmpEle = null;
			 }
		
			 return tmpEle;				
		}	
		
		
	//General Get Email element
	public WebElement getAllEmailEle()
	{
		WebElement tmpEle=null;
		String splitWdMethod;
		
		if(wdMethod.contains(":"))
		{
			splitWdMethod = wdMethod.split(":")[1];
		}else
		{
			splitWdMethod = wdMethod;
		}
		
		 switch(splitWdMethod.toLowerCase())
		 {
			 case "skirll":
				 tmpEle = this.getSkrillEmailEle();
				 break;
			 case "neteller":
				 tmpEle = this.getNetellerEmailEle();
				 break;
			 case "cryptocurrency-bitcoin":	 				
				 tmpEle = this.getBTCEmailEle();
				 break;
			 case "cryptocurrency-usdt":			 	
			 		tmpEle = this.getUSDTEmailEle();
			 		break;
			 case "bitwallet":	
				 	tmpEle = this.getBitwalletEmailEle();
				 	break;
			 case "sticpay":
				 	tmpEle = this.getSticpayEmailEle();
				 	break;
			 case "astropay":
				 	tmpEle = this.getAstropayEmailEle();
				 	break;
				 	
			 	default:  //Use BTC withdraw
					 tmpEle = this.getBTCEmailEle();
		}		 	
		
		return tmpEle;				
	 }
	
	//General Notes element
	public WebElement getAllNotesEle()
	{
		WebElement tmpEle=null;
		String splitWdMethod;
		
		if(wdMethod.contains(":"))
		{
			splitWdMethod = wdMethod.split(":")[1];
		}else
		{
			splitWdMethod = wdMethod;
		}
		
		 switch(splitWdMethod.toLowerCase())
		 {
			 case "skirll":
				 tmpEle = this.getSkrillNotesEle();
				 break;
			 case "neteller":
				 tmpEle = this.getNetellerNotesEle();
				 break;
			 case "cryptocurrency-bitcoin":	 				
				 tmpEle = this.getBTCNotesEle();
				 break;
			 case "cryptocurrency-usdt":			 	
			 		tmpEle = this.getUSDTNotesEle();
			 		break;
			 case "bitwallet":	
				 tmpEle = this.getBitwalletNotesEle();
				 break;
			 case "sticpay":
				 tmpEle = this.getSticpayNotesEle();
				 break;
			 case "astropay":
				 tmpEle = this.getAstropayNotesEle();
				 break;
			 default:
				 tmpEle = this.getBTCNotesEle();
		}	
		
		return tmpEle;				
	 }	
	
}
