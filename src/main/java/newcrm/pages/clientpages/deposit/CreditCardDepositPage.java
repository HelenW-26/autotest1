package newcrm.pages.clientpages.deposit;

import java.time.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.DepositBasePage;
import org.openqa.selenium.support.ui.WebDriverWait;
import tools.ScreenshotHelper;
import utils.Listeners.CustomListener;
import utils.LogUtils;


public class CreditCardDepositPage extends DepositBasePage {

	public CreditCardDepositPage(WebDriver driver) {
		super(driver);
	}
	
	public boolean checkIframeExists() {
		this.waitLoading();
		try {
			waitvisible.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//invalidiframe")));
		}catch(Exception e) {
			System.out.println("!!! Coundn't find 3rd Party iFrame!");
			return false;
		}
		GlobalMethods.printDebugInfo("3rd Party iFrame is visible.");
		return true;
	}

	public boolean checkIframeVisible(String barnd,String frameName) {
//		String iframeXpath = "//*[@id='"+frameName+"']";
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
		try {
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(iframeXpath)));
			// 等待iframe元素出现并可交互
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(frameName)));

			// 等待iframe可见且可切换
			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id(frameName)));

			// 额外等待iframe内部文档加载完成
			wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
			ScreenshotHelper.addTextWatermark(driver, "3rd Party iFrame");
			ScreenshotHelper.takeScreenshot(driver, null,"screenshots",barnd+"_CC_IFRAME" );


			LogUtils.info("3rd Party CC iFrame Page is visible.");
		} catch (Exception e) {
			LogUtils.error("!!! Coundn't find 3rd Party %s iFrame!".formatted(frameName), e);
			e.printStackTrace();
			if (e.getMessage().contains("target frame detached")) {
				// 如果出现frame detached错误，稍等后重试一次
				try {
					Thread.sleep(2000);
					LogUtils.info("3rd Party CC iFrame Page is visible.");
					wait.until(ExpectedConditions.presenceOfElementLocated(By.id(frameName)));
					return true;
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					return false;
				}
			} else {
				throw e; // 重新抛出其他异常
			}
        }finally {
			driver.switchTo().defaultContent();
		}
        return true;
    }
}