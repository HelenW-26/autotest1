package newcrm.pages.auclientpages.Register;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.ResidentialAddressPage;
import utils.LogUtils;

public class ASICResidentialAddressPage extends ResidentialAddressPage {

	public ASICResidentialAddressPage(WebDriver driver) {
		super(driver);
	}
	@Override
	public void setState(String state) {
		WebElement e = this.findClickableElemntBy(By.id("state"));
		this.moveElementToVisible(e);
		e.click();
		List<WebElement> ops = driver.findElements(By.xpath("//div[@class='el-select-dropdown el-popper' and not(contains(@style,'display'))]//li"));
		state = this.selectRandomValueFromDropDownList(ops);
		LogUtils.info("ResidentialAddressPage: Set State to :" + state);
	}
}
