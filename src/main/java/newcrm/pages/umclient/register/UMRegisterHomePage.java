package newcrm.pages.umclient.register;

import newcrm.global.GlobalMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import newcrm.pages.clientpages.register.RegisterHomePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import utils.LogUtils;

import java.util.List;

public class UMRegisterHomePage extends RegisterHomePage {

	public UMRegisterHomePage(WebDriver driver,String url) {
		super(driver,url);
	}

	@Override
	protected WebElement getDemoDomainUrlInput() {
		return assertElementExists(By.id("form_action"), "Domain Url");
	}

	@Override
	protected WebElement getDemoDomainUrlSubmitBtn() {
		return assertElementExists(By.id("change_action"), "Domain Url Submit button");
	}

	@Override
	protected WebElement getDemoContinueBtn() {
		return assertElementExists(By.id("sub-next"), "Continue button");
	}

	@Override
	public void registerDemoAccount() {
		WebElement pwd = getProtectedPwdInput();
		pwd.sendKeys("147258");

		getProtectedPwdSubmitBtn().click();

		LogUtils.info("Click Submit button");
	}

	@Override
	public void registerLiveAccount() {
		driver.get("https://new.vantagefx.com/open-a-live-forex-trading-account-um");
		this.waitLoading();
	}

	@Override
	public void clickDemoContinueBtn() {
		getDemoContinueBtn().click();

		fastwait.until((ExpectedCondition<Boolean>) d -> {
			List<WebElement> loaders = d.findElements(By.xpath("//div[@class='loading']"));
			return loaders.isEmpty();
		});

		LogUtils.info("Click Continue button");
	}

}
