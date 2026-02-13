package newcrm.pages.vjpclientpages;

import newcrm.pages.clientpages.DepositBasePage;
import newcrm.pages.clientpages.deposit.CryptoDepositPage;
import newcrm.pages.clientpages.deposit.InterBankTransPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import tools.ScreenshotHelper;
import utils.LogUtils;

import java.time.Duration;

public class VJPCPDepositPage extends InterBankTransPage {

	public VJPCPDepositPage(WebDriver driver) {
		super(driver);
	}

	public void fillCCForm(String cardNumber, String expiryDate, String cvv) {
        try {
            LogUtils.info("==========VJP Fill Credit Card Form===================");
            WebElement state=findVisibleElemntByXpath("//*[@id=\"mat-input-2\"]");
            state.click();
            WebElement country=findVisibleElemntByXpath("//*[@id=\"mat-option-0\"]");
            country.click();
            this.setInputValue(this.findVisibleElemntByXpath("//*[@id=\"mat-input-0\"]"), cardNumber);
            this.setInputValue(this.findVisibleElemntByXpath("//*[@id=\"mat-input-1\"]"), expiryDate);
            this.setInputValue(this.findVisibleElemntByXpath("//*[@id=\"mat-input-3\"]"), cvv);
            WebElement submit = this.findClickableElementByXpath("//button[contains(@class, 'btn-submit') and @type='submit']");
            submit.click();
            // 等待submit按钮文字变成continue表示提交成功
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(90));
            WebElement iframe =wait.until(ExpectedConditions.visibilityOfElementLocated(

                    By.xpath("//*[@id=\"pspIframe\"]")
));
            ScreenshotHelper.takeScreenshot(driver, iframe, "screenshots", "VJP_CC_Deposit_Iframe");
            this.goToIframe("pspIframe");
            WebElement password=this.findVisibleElemntByXpath("//*[@id=\"testAcsForm\"]//input[contains(@name, 'password')]");
            this.setInputValue(password, "test");
            WebElement confirm = this.findClickableElementByXpath("//*[@id=\"testAcsForm\"]/button");
            confirm.click();
            if(isCCDepositSuccess()){
                LogUtils.info("depositSuccess 在页面上是可见的。入金成功");
                ScreenshotHelper.takeScreenshot(driver, null,"screenshots", "VJP_CC_Deposit_Success");
                this.goBack();
                return;
            };
        } catch (Exception e) {
           e.printStackTrace();
            if(isCCDepositSuccess()){
                LogUtils.info("depositSuccess 在页面上是可见的。入金成功");
                ScreenshotHelper.takeScreenshot(driver, null,"screenshots", "VJP_CC_Deposit_Success");
                String url =driver.getCurrentUrl() ;
                if(url.contains("secure-app08.crm-alpha.com")){
                    LogUtils.info("当前已退出登陆进行后退");
                    this.goBack();

                }
                return;
            };
        }

        LogUtils.info("==========VJP Fill Credit Card Form Finish!===================");



	}

	public boolean isCCDepositSuccess() {
		try {
            ScreenshotHelper.takeScreenshot(driver, null,"screenshots", "VJP_CC_Deposit_Success");
            this.goToIframe("iFrameResizer0");
            WebElement iframeElement =this.findVisibleElemntByXpath("//*[contains(@class, 'slogan') and contains(., 'Your deposit was successful')]");

			// 判断iframe是否展示
			boolean isIframeDisplayed = iframeElement.isDisplayed();

			if (isIframeDisplayed) {
				LogUtils.info("iframe 在页面上是可见的。");
				return true;
			} else {
				LogUtils.info("iframe 在页面上不可见（可能被CSS隐藏）。");
			}

		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.info("在页面上未找到指定的iframe元素，它可能不存在或尚未加载。");
		}
		return false;
	}


}
