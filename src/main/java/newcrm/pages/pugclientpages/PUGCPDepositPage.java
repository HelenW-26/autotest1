package newcrm.pages.pugclientpages;

import newcrm.pages.clientpages.deposit.InterBankTransPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import tools.ScreenshotHelper;
import utils.LogUtils;

import java.time.Duration;

public class PUGCPDepositPage extends InterBankTransPage {

	public PUGCPDepositPage(WebDriver driver) {
		super(driver);
	}

	public void fillCCForm(String cardNumber, String expiryDate, String cvv) {
        try {
            LogUtils.info("==========PUG Fill Credit Card Form===================");
            WebElement state=findVisibleElemntByXpath("//*[@id=\"mat-input-2\"]");
            state.click();
            WebElement country=findVisibleElemntByXpath("//*[@id=\"mat-option-0\"]");
            country.click();
            this.setInputValue(this.findVisibleElemntByXpath("//*[@id=\"mat-input-0\"]"), cardNumber);
            this.setInputValue(this.findVisibleElemntByXpath("//*[@id=\"mat-input-1\"]"), expiryDate);
            this.setInputValue(this.findVisibleElemntByXpath("//*[@id=\"mat-input-3\"]"), cvv);
            WebElement submit = this.findClickableElementByXpath("//button[contains(@class, 'btn-submit') and @type='submit']");
            submit.click();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"pspIframe\"]")));
            WebElement iframe = this.findVisibleElemntByXpath("//*[@id=\"pspIframe\"]");
            if (iframe.isDisplayed()) {
                ScreenshotHelper.takeScreenshot(driver, iframe, "screenshots", "PUG_CC_Deposit_Iframe");

                this.goToIframe("pspIframe");
                setVerificationCode();
                driver.switchTo().defaultContent();

            }
            WebElement depositSuccess = null;
            try {
                ScreenshotHelper.takeScreenshot(driver, depositSuccess, "screenshots", "PUG_CC_Deposit_Iframe_before");

//                depositSuccess = this.findVisibleElemntByXpath("//bp-card-success-page//div[contains(., 'Your deposit was successful')]");
                if (isCCDepositSuccess()) {
                    LogUtils.info("✅ depositSuccess 在页面上是可见的。入金成功");
                    ScreenshotHelper.takeScreenshot(driver, depositSuccess, "screenshots", "PUG_CC_Deposit_Iframe");

//                    this.goBack();
//                    LogUtils.info("✅ 已返回到首页");
//                    LogUtils.info(driver.getCurrentUrl());
                }
            } catch (Exception e) {
               e.printStackTrace();
                if (isCCDepositSuccess()) {
                    LogUtils.info("✅ depositSuccess 在页面上是可见的。入金成功");
                    ScreenshotHelper.takeScreenshot(driver, depositSuccess, "screenshots", "PUG_CC_Deposit_Iframe");

                    this.goBack();
                }
            }



        } catch (Exception e) {
           e.printStackTrace();
        }

        LogUtils.info("==========PUG Fill Credit Card Form Finish!===================");



	}

    public void setVerificationCode() {
        try {
            LogUtils.info("==========PUG Fill Credit Card Form===================");

            WebElement code = this.findClickableElemntByTestId("3ds-code-input");
            boolean isCodeDisplayed = code.isDisplayed();
            if (isCodeDisplayed){
                LogUtils.info("==========PUG Fill Credit Card Form Code Verification===================");
                this.setInputValue(this.findClickableElemntByTestId("3ds-code-input"), "123456");
                WebElement submit = this.findClickableElementByXpath("//button[@data-testid='place_order_button']");
                ScreenshotHelper.takeScreenshot(driver, code, "screenshots", "PUG_CC_Deposit_Code");
                submit.click();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean isCCDepositSuccess() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(90));

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"iFrameResizer0\"]")));
            WebElement iframe = this.findVisibleElemntByXpath("//*[@id=\"iFrameResizer0\"]");
            if (iframe.isDisplayed()) {
                ScreenshotHelper.takeScreenshot(driver, iframe, "screenshots", "PUG_CC_Deposit_Success_Iframe");
                this.goToIframe("iFrameResizer0");

            }

            WebElement depositSuccess = this.findVisibleElemntByXpath("//bp-card-success-page//div[contains(., 'Your deposit was successful')]");

            boolean isIframeDisplayed = depositSuccess.isDisplayed();

            if (isIframeDisplayed) {
                LogUtils.info("depositSuccess 在页面上是可见的。");
                return true;
            } else {
                LogUtils.info("depositSuccess 在页面上不可见（可能被CSS隐藏）。");
            }

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.info("在页面上未找到指定的depositSuccess元素，它可能不存在或尚未加载。");
        }
        return false;
    }


}
