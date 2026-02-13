package newcrm.pages.vtclientpages.register;

import java.util.List;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.pages.clientpages.register.AccountConfigurationPage;
import utils.LogUtils;

public class VTAccountConfigurationPage extends AccountConfigurationPage {

	public VTAccountConfigurationPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
	public String getPageTitle() {
		return this.findVisibleElemntByXpath("//div[@class='main']/strong").getText();
	}

	@Override
	protected List<WebElement> getAccountTypeEles(){
		return driver.findElements(By.xpath("//div[@class='row accountType']/div[2]/div/div"));
	}
	
	@Override
	protected List<WebElement> getCurrencyEles(){
		return driver.findElements(By.xpath("//div[@class='row currency']/div[2]/div/div"));
	}

	@Override
	protected WebElement getAgreementEle() {
		return assertClickableElementExists(By.xpath("//span[@class='el-checkbox__input']"), "Agreement tick box");
	}

	@Override
	protected WebElement getPlatformItemEle(GlobalProperties.PLATFORM platform) {
		//return assertClickableElementExists(By.xpath("//label[@data-testid='" + platform.getPlatformDesc().toLowerCase() + "']"), platform.getPlatformDesc() + " Trading Platform ");
		return assertClickableElementExists(
				By.xpath("//label[translate(@data-testid,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='"
						+ platform.getPlatformDesc().toLowerCase() + "']"),
				platform.getPlatformDesc() + " Trading Platform ");

	}

	@Override
	protected WebElement getPlatformContinueBtn(GlobalProperties.PLATFORM platform) {
		String path = "(//*[contains(@class,'selectMt4TipDialog')])[last()]//button/span";
		assertClickableElementExists(By.xpath(path), platform.getPlatformDesc() + " Continue button");

		return assertElementExists(
				By.xpath(path), platform.getPlatformDesc() + " Continue button",
				e -> {
					String txt = e.getText().trim().toLowerCase();
					return txt.contains(platform.getPlatformDesc().toLowerCase());}
		);
	}

	@Override
	public boolean setPlatForm(GlobalProperties.PLATFORM platform) {
		triggerElementClickEvent_withoutMoveElement(getPlatformItemEle(platform));
		LogUtils.info("Click " + platform.getPlatformDesc() + " Trading Platform");

		if (platform == GlobalProperties.PLATFORM.MT4) {
			WebElement e = getPlatformContinueBtn(platform);
			if (e != null) {
				triggerElementClickEvent_withoutMoveElement(e);
				LogUtils.info("Click Continue button");
			}
		}

		LogUtils.info("AccountConfigurationPage: set Platform to " + platform.getPlatformDesc());
		return true;
	}

	@Override
	public String setAccountType() {

		driver.findElement(By.xpath("//label[@data-testid='standardSTP']")).click();
		String result= "standardSTP";
		LogUtils.info("AccountType:" + result);
		LogUtils.info("AccountConfigurationPage: set account type to: " + result);
		return result;
	}

	@Override
	public String setCurrency() {

		driver.findElement(By.xpath("//label[@data-testid='USD']")).click();
		String result= "USD";
		LogUtils.info("AccountType:" + result);
		LogUtils.info("AccountConfigurationPage: set account type to: " + result);
		return result;
	}

	@Override
	public void next() {
		this.waitLoading();
		WebElement e = assertElementExists(By.xpath("(//button[@data-testid='next'])[2]"), "Submit button");
		triggerClickEvent_withoutMoveElement(e);
	}
}
