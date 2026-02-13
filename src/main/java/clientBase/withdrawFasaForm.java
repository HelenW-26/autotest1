package clientBase;


/*Yanni on 19/03/2021 , Funds->Withdraw Funds, Withdraw Page - FasaPay
 * 
*/

import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import vantagecrm.Utils;


public class withdrawFasaForm extends withdrawGeneral {
	
	private WebDriver driver;
	private String Brand;
		
	public withdrawFasaForm(WebDriver driver, String Brand)
	{
		super(driver,Brand);
		this.driver = driver;
		this.Brand = Brand;
		
	}
	
	//Fasa Acccount Name
	public WebElement getFasaAcctNameEle()
	{
		WebElement tmpEle = null;
		
		switch(Brand.toLowerCase())
		{
			case "vt":
				tmpEle = driver.findElement(By.id("fasapayAccountName_fasapay"));
				break;
			
			case "fsa":
			case "svg":
				tmpEle = driver.findElements(By.xpath("//div[@id='fasapayForm']//input")).get(0);
				break;
				
				default:
					tmpEle = driver.findElement(By.xpath("//li[@class='fl']//div[@class='el-form-item is-required']//input"));
		}
		
		return tmpEle;

	}
	
	//Fasa Acccount No
	public WebElement getFasaAcctNoEle()
	{
		WebElement tmpEle = null;
		
		switch(Brand.toLowerCase())
		{
			case "vt":
				tmpEle = driver.findElement(By.id("fasapayAccountNumber_fasapay"));
				break;
			
			case "fsa":
			case "svg":
				tmpEle = driver.findElements(By.xpath("//div[@id='fasapayForm']//input")).get(1);
				break;
				
				default:
					tmpEle = driver.findElement(By.xpath("//li[@class='fr']//div[@class='el-form-item is-required']//input"));
		}
		
		return tmpEle;

	}
	
	//Fasa Withdraw Notes
	public WebElement getFasaNotesEle()
	{
		WebElement tmpEle = null;
		
		switch(Brand.toLowerCase())
		{
			case "vt":
				tmpEle = driver.findElement(By.id("notes_fasapay"));
				break;
			
			case "fsa":
			case "svg":
				tmpEle = driver.findElements(By.xpath("//div[@id='fasapayForm']//input")).get(2);
				break;
				
				default:
					tmpEle = driver.findElement(By.xpath("//div[@class='el-form-item']//div[@class='el-input']//input"));
		}
		
		return tmpEle;

	}    

}
