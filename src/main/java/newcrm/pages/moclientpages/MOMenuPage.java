package newcrm.pages.moclientpages;

import newcrm.global.GlobalMethods;
import org.openqa.selenium.*;

import newcrm.pages.clientpages.MenuPage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.LogUtils;

import java.time.Duration;

public class MOMenuPage extends MenuPage {

	public MOMenuPage(WebDriver driver) {
		super(driver);
	}

	// region [ Client Portal ]

	// region [ General ]

	@Override
	public void switchToCP() {
		LogUtils.info("Switch to CP portal");
		closeNotificationDialog();
		closeCouponGuideDialog();
		close2FADialog();
		closeIndonesiaDialog();
        closeCouponPop();
		closeBannerDialog();

		String title = driver.getTitle();
		if(!title.toLowerCase().contains("ib")) {
			LogUtils.info("At CP portal. Do not need switch");
			return;
		}

		WebElement cp = this.findClickableElemntByTestId("redirectToCp");
		triggerElementClickEvent(cp);
	}

	// endregion

	// region [ CP Menu ]

	@Override
	protected WebElement getDemoAccountConfigTabMenuEle() {
		return assertElementExists(By.xpath("#openAccount div.ht-switcher__item"), "Demo Account Tab", e -> "Demo Account".equalsIgnoreCase(e.getText().trim()));

	}

	@Override
	public void clickTransferAcc() {
		clickFund();
		closeBannerDialog();
		WebElement e = assertClickableElementExists(By.xpath("//*[@data-testid='menu.transferBetweenAccs']"), "Transfer Funds menu");
		triggerElementClickEvent_withoutMoveElement(e);
		LogUtils.info("Go to Transfer Funds page");
	}

	@Override
	public void clickWithdrawFunds() {
		clickFund();
		closeBannerDialog();
		WebElement e = assertClickableElementExists(By.xpath("//*[@data-testid='menu.withdrawFunds']"), "Withdraw Funds menu");
		triggerElementClickEvent_withoutMoveElement(e);
		LogUtils.info("Go to Withdraw Funds page");
	}

	@Override
	public void clickDepositFunds() {
		clickFund();
		closeBannerDialog();
		WebElement e = assertClickableElementExists(By.xpath("//*[@data-testid='menu.depositFund']"), "Deposit Funds menu");
		triggerElementClickEvent_withoutMoveElement(e);
		LogUtils.info("Go to Deposit Funds page");
		closeBannerDialog();
	}

	@Override
	public void clickLiveAccount() {
		waitLoading();
		close2FADialog();
		closeNotificationDialog();
		clickAccount();
	}

	@Override
	public void clickTransactionHis() {
		waitLoading();
		moveContainerToTop();
		clickFund();
		closeBannerDialog();
		waitLoading();
		assertVisibleElementExists(By.xpath("//*[@data-testid='menu.transactionHis']"), "Transaction History menu").click();
		waitLoadingForCustomise(120);
		LogUtils.info("Go to Transaction History page");
	}

	@Override
	public void clickFund() {
		waitLoading();
		closeBannerDialog();

		WebElement funds = assertClickableElementExists(By.xpath("//li[@data-testid='menu.funds']"), "Funds menu");

		String cls = funds.getAttribute("class");
		if(cls != null && cls.trim().toLowerCase().contains("is-active") && driver.findElements(By.xpath("//div[@id='depositPCS'] | //div[@id='withdraw'] | //div[@id='transactionHistory'] | //div[@id='transferFunds']")).isEmpty()) {
			clickProfile();
		}

		String expand = funds.getAttribute("aria-expanded");
		//expanded,return
		if(expand !=null && expand.toLowerCase().trim().equals("true")) {
			return;
		}

		close2FADialog();
		closeIndonesiaDialog();
		triggerElementClickEvent_withoutMoveElement(funds);
		LogUtils.info("Go to Funds page");
	}

	@Override
	public void clickHome() {
        try {
            waitLoading();
            waitLoading();
            moveContainerToTop();
            closeCouponPop();
            close2FADialog();
            closeBannerDialog();
            WebElement e = assertClickableElementExists(By.xpath("//li[@data-testid='menu.home']"), "Home menu");
            triggerElementClickEvent_withoutMoveElement(e);
            LogUtils.info("Go to Home page");
            closeBannerDialog();
        } catch (Exception ex) {
           ex.printStackTrace();
        }
    }

	// endregion

	// region [ CP Dialog ]

	public void closeNotificationDialog()
	{
		WebElement popup = checkElementExists(By.cssSelector("div[data-testid='notificationDialog']:not([style*='display: none']) svg.ht-icon-close"));
		if (popup != null) {
			popup.click();
			LogUtils.info("Close Notification Dialog");
		}
	}

	public void closeBannerDialog()
	{
		WebElement popup = checkElementExists(By.xpath("//img[@data-testid='closeImg'] | (//img[@data-testid='closeImg'])[1]"));
		if (popup != null) {
			clickElement(popup);
			LogUtils.info("Close Banner Dialog");
		}
	}

    public void closeCouponPop(){
        try
        {
            WebElement popup = driver.findElement(By.xpath("*//el-button[@id='coupon-tip-button']"));
            js.executeScript("arguments[0].click()", popup);
        }
        catch (Exception e)
        {
            LogUtils.info("No coupon dialog");
        }
    }

    public void close2FADialog()
	{
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement popup;
        try {
			popup = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//el-button[@id='experience-button']")));
			if (popup != null) {
				clickElement(popup);
				LogUtils.info("Close 2FA Dialog");
			}
        } catch (Exception e) {
           e.printStackTrace();
        }

	}

	public void closeIndonesiaDialog()
	{
		WebElement popup = checkElementExists(By.xpath("//div[@id='LivePage']/div/div[@class='el-dialog__wrapper']//button[@class='el-dialog__headerbtn']"));
		if (popup != null) {
			clickElement(popup);
			LogUtils.info("Close Indonesia Guide Dialog");
		}
	}

	@Override
	public void closeCouponGuideDialog() {
		WebElement popup = getCloseCouponDialogEle();
		if (popup != null) {
			triggerClickEvent_withoutMoveElement(popup);
			LogUtils.info("Close Coupon Guide Dialog");
		}
	}

	// endregion

	// endregion

	// region [ IB Portal ]

	// region [ IB Menu ]

	@Override
	public void ibDashBoard() {
		waitLoading();
		moveContainerToTop();
		closeSkipDialog();
		closeBannerDialog();
		closeIBNotificationDialog();

		WebElement e = assertElementExists(By.xpath("//span[@data-testid='click_/home']"), "IB Dashboard menu");
		triggerElementClickEvent_withoutMoveElement(e);
		LogUtils.info("Go to IB Dashboard page");
	}

	@Override
	public void ibTransactionHistory() {
		this.waitLoading();
		WebElement e = assertElementExists(By.xpath("//*[@data-testid='click_/RebatePaymentHistory' or @data-testid='click_/rebatePaymentHistory']"), "IB Transaction History menu");
		triggerElementClickEvent_withoutMoveElement(e);
		LogUtils.info("Go to IB Transaction History page");
	}

	// endregion

	// region [ IB Dialog ]

	public void closeSkipDialog() {
		WebElement popup = checkElementExists(By.xpath("//*[contains(text(), 'Skip')]|//*[contains(text(), '跳过')]|//*[contains(@class, 'driver-close-btn')]"));
		if (popup != null) {
			clickElement(popup);
			waitLoading();
			LogUtils.info("Close Skip Dialog");
		}
	}

	@Override
	public void closeIBNotificationDialog()
	{
        try {
            WebElement popup = checkElementExists(By.xpath("//div[contains(@class,'el-dialog__wrapper ht-dialog') and not(contains(@style,'display: none'))]"));
            if (popup != null) {
                WebElement closeIcon = checkElementExists(By.cssSelector("svg.ht-icon-close"));
                closeIcon.click();
                LogUtils.info("Close IB Notification Dialog");
            }
        } catch (Exception e) {
          e.printStackTrace();
		  this.refresh();
		  waitLoading();

		}
    }

	// endregion

	// endregion

}
