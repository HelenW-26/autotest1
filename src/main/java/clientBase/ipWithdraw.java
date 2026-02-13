package clientBase;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import vantagecrm.Utils;

public class ipWithdraw {

	private WebDriver driver;
	private String Brand;
	public String wdMethod;

	public ipWithdraw(WebDriver driver, String Brand) {
		this.driver = driver;
		this.Brand = Brand;
		// this.wdMethod = wdMethod;
	}

	public WebElement getWithdrawLink() {
		WebElement tempEle = driver
				.findElement(By.xpath("//a[contains(translate(text(),'ithdraw','ITHDRAW'),'WITHDRAW')]"));
		return tempEle;
	}

	// the Rebate account list
	public WebElement getRebateAccountList() {
		WebElement tempEle=null;
		
		  if (Brand.equalsIgnoreCase("vt")) {
			  tempEle = driver.findElement(By.xpath("//input[@id='qAccount']"));
		  }else {
		 
			tempEle = driver.findElement(By.xpath("//div[@class='el-form-item is-success is-required']//input[@placeholder='Select']"));
		}
		  
		return tempEle;
	}

	// After user clicks the rebate account list, a new DIV will be added directly under the Body
	public List<WebElement> getMenuGroup() {
		List<WebElement> menuGroup = driver.findElements(By.cssSelector("body>div.el-select-dropdown.el-popper"));
		return menuGroup;
	}

	// the input amount
	public WebElement getWithdrawAmount() {
		WebElement tempEle = driver.findElement(By.xpath("//input[@class='el-input__inner' and @type='tel']"));
		return tempEle;
	}

	// the submit withdraw button
	public WebElement getSubmitBtn() {
		WebElement tempEle = driver
				.findElement(By.xpath("//span[contains(translate(text(),'Submit','SUBMIT'),'SUBMIT')]"));
		return tempEle;
	}

	// the popup confirm withdraw button
	public WebElement getConfirmBtn() {
		WebElement tempEle = driver
				.findElement(By.xpath("//span[contains(translate(text(),'Confirm','CONFIRM'),'CONFIRM')]"));
		return tempEle;
	}
	
	// the dashboard link
	public WebElement getDashboardEle() {
		WebElement tempEle = driver.findElement(By.xpath("//span[contains(translate(text(),'ashbord','ASHBORD'),'DASHBOARD')]"));
		return tempEle;
	}
	
	//VT loading image
	public WebElement getVTLoadingEle() {
		WebElement tempEle = driver.findElement(By.xpath("//i[@class='client-portal-loading']"));
		return tempEle;
	}
	
	// After selecting Bank Transfer as the withdraw method, customers are then prompted to select Bank Account. This function returns to the element
	public WebElement getBankAccountSelelctEle() {
		WebElement tmpEle = null;

		switch (Brand.toLowerCase()) {
		case "vt":
			tmpEle = driver.findElement(By.xpath("//div[@data-testid='selectedCard']/div/input"));
			break;

		default:
			tmpEle = driver.findElement(By.xpath("//div[@id='transferCountry']/form/div/ul/li/div/div/div/div/input"));

			break;

		}

		return tmpEle;
	}

	// Return the element of specified Country/Region
	public WebElement getBankCountryEle(String countryName) {
		WebElement tmpEle;
		String methodxPathLocator = "//span[contains(text(),'method')]";

		switch (Brand.toLowerCase()) {
		case "vt":
			driver.findElement(By.xpath("//div[@data-testid='country_region']/div/input")).click();
			break;
		/*
		 * case "fsa":
		 * case "svg":
		 * driver.findElement(By.xpath("//div[@id='transferCountry']/form/div/ul/li[2]/div/div/div/div/div/div/input")).click();
		 * break;
		 */

		default:
			driver.findElement(By.xpath("//div[@id='transferCountry']/form/div/ul/li[2]/div/div/div/div/div/div/input"))
					.click();

			break;
		}

		methodxPathLocator = methodxPathLocator.replace("method", countryName);

		tmpEle = driver.findElement(By.xpath(methodxPathLocator));

		return tmpEle;
	}

	// Get Bank Name element
	public WebElement getBankNameEle() {
		WebElement tmpEle = null;
		switch (Brand.toLowerCase()) {
		case "vt":
			tmpEle = driver.findElement(By.id("bankName"));
			break;

		default:
			tmpEle = driver
					.findElement(By.xpath("//div[@id='transferCountry']/form/form/div/div/ul/li/div/div/div/input"));
		}

		return tmpEle;
	}

	// Get Bank Address element
	public WebElement getBankAddrEle() {
		WebElement tmpEle = null;
		switch (Brand.toLowerCase()) {
		case "vt":
			tmpEle = driver.findElement(By.id("bankAddress"));
			break;

		default:
			tmpEle = driver
					.findElement(By.xpath("//div[@id='transferCountry']/form/form/div/div/ul/li[2]/div/div/div/input"));
		}

		return tmpEle;
	}

	// Get Bank Beneficiary element
	public WebElement getBankBenefiEle() {
		WebElement tmpEle = null;
		switch (Brand.toLowerCase()) {
		case "vt":
			tmpEle = driver.findElement(By.id("beneficiaryName"));
			break;

		default:
			tmpEle = driver.findElement(
					By.xpath("//div[@id='transferCountry']/form/form/div/div[2]/ul/li[2]/div/div/div/input"));
		}

		return tmpEle;
	}

	// Get Bank Account Number element
	public WebElement getBankAcctNoEle() {
		WebElement tmpEle = null;
		switch (Brand.toLowerCase()) {
		case "vt":
			tmpEle = driver.findElement(By.id("accountNumber"));
			break;

		default:
			tmpEle = driver
					.findElement(By.xpath("//div[@id='transferCountry']/form/form/div/div[2]/ul/li/div/div/div/input"));
		}

		return tmpEle;
	}
	// Get Bank Account Number element
	public WebElement getAcctHolderAddrEle() {
		WebElement tmpEle = null;
		switch (Brand.toLowerCase()) {
		case "vt":
			tmpEle = driver.findElement(By.id("holderAddress"));
			break;

		default:
			tmpEle = driver
					.findElement(By.xpath("//div[@id='transferCountry']/form/form/div/div[3]/ul/li/div/div/div/input"));
		}

		return tmpEle;
	}

	// Get Swift Code element
	public WebElement getSwiftCodeEle() {
		WebElement tmpEle = null;
		switch (Brand.toLowerCase()) {
		case "vt":
			tmpEle = driver.findElement(By.id("swift"));
			break;

		default:
			tmpEle = driver.findElement(
					By.xpath("//div[@id='transferCountry']/form/form/div/div[3]/ul/li[2]/div/div/div/input"));
		}

		return tmpEle;
	}

	// Get ABA/Sort Code element
	public WebElement getABACodeEle() {
		WebElement tmpEle = null;

		switch (Brand.toLowerCase()) {
		case "vt":
			tmpEle = driver.findElement(By.xpath("//div[@id='transferCountry']/div/form/ul[4]/li/div/div/div/input"));
			break;

		default:
			tmpEle = driver
					.findElement(By.xpath("//div[@id='transferCountry']/form/form/div/div[4]/ul/li/div/div/div/input"));
		}

		return tmpEle;
	}

	// get Remeber My account checkbox
	public WebElement getRememberAccountCkb() {
		WebElement tmpEle = null;
		tmpEle = driver.findElement(By.xpath("//span[@class='el-checkbox__inner']"));

		return tmpEle;
	}

	// GetSkrill/Neteller Email element
	public WebElement getSkrillNetellerEmailEle() {
		WebElement tmpEle = null;

		switch (Brand.toLowerCase()) {

		case "vt":
			tmpEle = driver.findElement(By.id("skrillEmail"));
			break;

		default:
			tmpEle = driver.findElement(By.xpath("//div[@class='el-form-item is-required']//input"));
		}

		return tmpEle;
	}

	// Get Neteller Notes element
	public WebElement getNotesEle() {
		WebElement tmpEle = null;
		tmpEle = driver.findElement(By.xpath("//div[@class='el-form-item']//div[@class='el-input']//input"));
		return tmpEle;
	}

	// Get Crypto Email element
	public WebElement getCryptoAddressEle() {
		WebElement tmpEle = null;

		switch (Brand.toLowerCase()) {
		case "vt":
			tmpEle = driver.findElement(By.id("cryptoWalletAddress"));
			break;

		default:
			tmpEle = driver.findElement(By.xpath("//div[@class='el-form-item is-required']//input"));
		}

		return tmpEle;
	}

	// Get Bitwallet email element
	public WebElement getBitwalletEmailEle() {
		WebElement tmpEle = null;

		switch (Brand.toLowerCase()) {
		case "vt":
			System.out.println("VT doesn't support Bitwallet");
			tmpEle = null;
			break;

		default:
			tmpEle = driver.findElement(By.xpath("//div[@class='el-form-item is-required']//input"));
		}

		return tmpEle;
	}

	// Get Sticpay email element
	public WebElement getSticpayEmailEle() {
		WebElement tmpEle = null;

		switch (Brand.toLowerCase()) {
		case "vt":
			System.out.println("VT doesn't support Sticpay");
			tmpEle = null;
			break;

		default:
			tmpEle = driver.findElement(By.xpath("//div[@class='el-form-item is-required']//input"));
		}

		return tmpEle;
	}

	// General Get Email element
	public WebElement getAllEmailEle() {
		WebElement tmpEle = null;
		String splitWdMethod;

		if (wdMethod.contains(":")) {
			splitWdMethod = wdMethod.split(":")[1];
		} else {
			splitWdMethod = wdMethod;
		}

		switch (splitWdMethod.toLowerCase()) {
		case "skrill":
		case "neteller":
			tmpEle = this.getSkrillNetellerEmailEle();
			break;
		case "cryptocurrency-bitcoin":
		case "cryptocurrency-usdt":
			tmpEle = this.getCryptoAddressEle();
			break;
		case "bitwallet":
			tmpEle = this.getBitwalletEmailEle();
			break;
		case "sticpay":
			tmpEle = this.getSticpayEmailEle();
			break;

		default:  // Use BTC withdraw
			tmpEle = this.getCryptoAddressEle();
		}

		return tmpEle;
	}

	// Fasa Acccount Name
	public WebElement getFasaAcctNameEle() {
		WebElement tmpEle = null;

		switch (Brand.toLowerCase()) {
		case "vt":
			tmpEle = driver.findElement(By.id("bankName"));
			break;

		case "fsa":
		case "svg":
			System.out.println("PUG IP doesn't support FasaPay Withdraw");
			tmpEle = null;
			break;

		default:
			tmpEle = driver
					.findElement(By.xpath("//div[@class='el-form-item is-required']//div[@class='el-input']//input"));
		}

		return tmpEle;

	}

	// Fasa Acccount No
	public WebElement getFasaAcctNoEle() {
		WebElement tmpEle = null;

		switch (Brand.toLowerCase()) {
		case "vt":
			tmpEle = driver.findElement(By.id("accountNumber"));
			break;

		case "fsa":
		case "svg":
			System.out.println("PUG IP doesn't support FasaPay Withdraw");
			tmpEle = null;
			break;

		default:
			tmpEle = driver.findElement(By.xpath("//form[2]/div/ul/li[2]/div/div/div/input"));
		}

		return tmpEle;

	}

	// Get Bank Province List
	public List<WebElement> getBankProvinceCityList() {
		List<WebElement> tmpList;
		tmpList = Utils.funcGetListItem(driver, "div.el-select-dropdown.el-popper");
		return tmpList;
	}

}
