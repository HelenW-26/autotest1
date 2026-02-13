package newcrm.pages.moclientpages;


import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.withdraw.CryptoWithdrawPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MOCryptoWithdrawPage extends CryptoWithdrawPage {

	public MOCryptoWithdrawPage(WebDriver driver) {
		super(driver);
	}

	@Override
	public void setUsdtChain(String usdtchain) {
		WebElement chaindropdown = this.findVisibleElemntByXpath("//div[@data-testid='chain']");
		this.moveElementToVisible(chaindropdown);
		chaindropdown.click();

		if(GlobalProperties.brand.equalsIgnoreCase("pug")) {chaindropdown.click();}

		WebElement chainvalue = this.findVisibleElemntByXpath("(//li/span[contains(text(),'"+usdtchain+"')])[2]");
		this.moveElementToVisible(chainvalue);
		chainvalue.click();
	}
}