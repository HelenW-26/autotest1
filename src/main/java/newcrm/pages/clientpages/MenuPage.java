package newcrm.pages.clientpages;

import newcrm.global.GlobalProperties;
import org.junit.rules.ExpectedException;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import newcrm.pages.clientpages.elements.MenuElements;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import utils.LogUtils;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class MenuPage extends Page{

	public MenuPage(WebDriver driver) {
		super(driver);
	}

	protected WebElement getAccountMenuEle() {
		return assertClickableElementExists(By.xpath("//li[@data-testid='menu.accManagement']"), "Account menu");
	}

	protected WebElement getDemoAccountConfigTabMenuEle() {
		return assertElementExists(By.xpath("(//*[contains(@class,'account_opening_tabs')])[last()]//li"), "Demo Account Tab", e -> "Demo Account".equalsIgnoreCase(e.getText().trim()));
	}

	protected WebElement getOpenAdditionalAccBtnEle() {
		return assertClickableElementExists(By.xpath("//*[contains(@class,'create_btn')]"), "Open Account button");
	}

	protected WebElement getOpenAdditionalDemoAccBtnEle() {
		return getOpenAdditionalAccBtnEle();
	}

    protected WebElement getUpgradeIBEle() {
        return assertElementExists(By.xpath("//button[contains(@class,'invite_entry')]"), "Upgrade IB");
    }

    protected WebElement getIBAgreementAgreeButton() {
        return assertElementExists(By.xpath("//div[@class='el-dialog__wrapper ht-dialog overflow-limit' and not(contains(@style,'display: none'))] //div[@class='el-dialog__footer']/div/button"), "Agree IB Agreement Button");
    }

    protected WebElement getGetStartedNowButtonEle() {
        return assertElementExists(By.xpath("//div[@class='footer_content'] //button[@data-testid='button']"), "Get Started Now button");
    }

    protected WebElement getIBReportMenuEle() {
        return assertElementExists(By.xpath("//span[contains(@data-testid, 'rebatereport')]"), "IB Report Menu");
    }

    protected WebElement getRebateReportTabEle() {
        return assertElementExists(By.xpath("//div[contains(@id, 'rebatereport')]/div"), "Rebate Report Tab");
    }

	protected WebElement getLanguageIconEle() {
		return assertElementExists(By.cssSelector("[data-testid='dropdownFlag'] .el-dropdown-selfdefine"), "Language Icon");
	}

	protected WebElement getLanguageListEle(String ddlId) {
		return assertElementExists(By.cssSelector("#" + ddlId), "Language List");
	}

	protected WebElement getActiveLanguageEle(WebElement parentEle) {
		return assertElementExists(By.cssSelector("li.active"), "Active Language", parentEle);
	}

	protected WebElement getLanguageItemEle(WebElement parentEle, String language) {
		return assertElementExists(By.cssSelector("li > span"), language + " Language", e -> e.getText().trim().toLowerCase().contains(language.trim().toLowerCase()), parentEle);
	}

	protected WebElement getMobileMenuIconEle() {
		return assertElementExists(By.cssSelector("div.mobile-menu"), "Mobile Menu Icon");
	}

	protected WebElement getMobileMenuListEle(WebElement parentEle) {
		return assertElementExists(By.cssSelector(".ht-drawer"), "Mobile Menu List", parentEle);
	}

	protected WebElement getMobileLanguageIconEle(WebElement parentEle) {
		return assertElementExists(By.cssSelector(".language-container > [role='tab']"), "Mobile Language Icon", parentEle);
	}

	protected WebElement getMobileLanguageListEle(String ddlId) {
		return assertElementExists(By.cssSelector("#" + ddlId), "Mobile Language List");
	}

	protected WebElement getMobileActiveLanguageEle(WebElement parentEle) {
		return assertElementExists(By.cssSelector("div.active"), "Mobile Active Language", parentEle);
	}

	protected WebElement getMobileLanguageItemEle(WebElement parentEle, String language) {
		return assertElementExists(By.cssSelector("div > span"), language + " Language", e -> e.getText().trim().toLowerCase().contains(language.trim().toLowerCase()), parentEle);
	}

    protected WebElement getMenuMoreIconEle() {
        return assertElementExists(By.xpath("//li[@class='el-submenu' and @id='overflowItems']"), "Menu Ellipsis Icon");
    }

	protected String getProfileMenu() {
		return "menu.profile";
	}

	protected String getMyProfileMenu() {
		return "menu.myProfile";
	}

	protected String getDownloadMenu() {
		return "menu.download";
	}

	protected String getSupportMenu() {
		return "menu.support";
	}

    protected String getReferralLinksMenu() {
        return "/referralLinks";
    }

    protected String getCampaignLinksMenu() {
        return "/campaignLinks";
    }

	protected WebElement getMenuBarEle(String path, String desc) {
		return checkElementExists(By.cssSelector("ul[role='menubar'] li[data-testid='" + path + "']"), desc);
	}

	protected WebElement getMenuHorizontalEle(String path, String desc) {
		return checkElementExists(By.cssSelector("ul[role='menu'] li[data-testid='" + path + "']"), desc);
	}

	protected WebElement getCloseCouponDialogEle() {
		return checkElementExists(By.cssSelector("#coupon-tip-button"));
	}

	protected WebElement getProfileSecurityMgmtMenuEle() {
		return assertClickableElementExists(By.cssSelector("[id='tab-/profile/securityManagement']"), "Security Management Tab");
	}

	public void clickHome() {
		WebElement e = assertClickableElementExists(By.xpath("//li[@data-testid='menu.home']"), "Home menu");
		e.click();
		waitLoading();
	}

	public void clickAccount() {
		waitLoading();
		triggerElementClickEvent_withoutMoveElement(getAccountMenuEle());
		LogUtils.info("Go to Account page");
		waitLoading();
	}

	public void clickLiveAccount() {
		clickAccount();
	}

	public void clickDemoAccount() {
		clickLiveAccount();
		// filter demo account
	}

	public void clickOpenAdditionAccount() {
		clickLiveAccount();
		triggerElementClickEvent_withoutMoveElement(getOpenAdditionalAccBtnEle());
		LogUtils.info("Go to Open Additional Account page");
	}

	public void clickOpenAdditionDemoAccount() {
		clickDemoAccount();
		triggerElementClickEvent_withoutMoveElement(getOpenAdditionalDemoAccBtnEle());
		LogUtils.info("Go to Open Additional Account page");
		triggerElementClickEvent_withoutMoveElement(getDemoAccountConfigTabMenuEle());
		LogUtils.info("Click Demo Account tab");
	}

	public void clickFund() {
		WebElement funds = this.findClickableElementByXpath("//li[@data-testid='menu.funds']");

		String expand = funds.getAttribute("aria-expanded");
		//expanded,return
		if(expand !=null && expand.toLowerCase().trim().equals("true")) {
			return;
		}
		super.findClickableElementByXpath("//li[@data-testid='menu.funds']").click();
		waitLoading();
	}

	public void clickDepositFunds() {
		clickFund();
		super.findClickableElementByXpath("//li[@data-testid='menu.depositFund']").click();
		waitLoading();
	}

	public void clickWithdrawFunds() {
		clickFund();
		super.findClickableElementByXpath("//li[@data-testid='menu.withdrawFunds']").click();
		waitLoading();
	}

	public void clickTransferAcc() {
		clickFund();
		super.findClickableElementByXpath("//li[@data-testid='menu.transferBetweenAccs']").click();
		waitLoading();
	}

	public void clickWallet() {
		WebElement e = assertClickableElementExists(By.xpath("//li[@data-testid='V-Wallet' or @data-testid='menu.wallet']"), "Wallet menu");
		triggerClickEvent(e);
		LogUtils.info("Go to Wallet page");
        closeEarnTitle();
	}
    public void closeEarnTitle(){
        waitLoading();
        WebElement walletEarnSkipEle = checkElementExists(By.xpath("//*[@class='skip-btn']"));
        if (walletEarnSkipEle != null && walletEarnSkipEle.isDisplayed()){
            triggerClickEvent(walletEarnSkipEle);
            LogUtils.info("close earn title success");
        }
    }

	public void CloseVCard() {
		waitLoading();
		WebElement e = checkElementExists(By.xpath("//img[@class='close-icon']"), "OpenCard-Close Button");
		System.out.println(e);
		if (e !=null) {
			if (e.isDisplayed()) {
				e.click();
			}
		}
	}
    public void CloseVCardTips(){
        waitLoading();
        WebElement next1 = checkElementExists(By.xpath("//button[text()='Next']"), "OpenCard-Next 1 Button");
        if (next1 !=null&&next1.isDisplayed()) {
            next1.click();
            waitLoading();
            WebElement next2 = findClickableElementByXpath("//button[text()='Next']");
            next2.click();
            waitLoading();
            WebElement done = findClickableElementByXpath("//button[text()='Done']");
            if (done !=null&&done.isDisplayed()){
                done.click();
                LogUtils.info("card tips close done");
            }
        }


    }
    public void walletEarn(){
        WebElement earn = assertElementExists(By.xpath("//li[@data-testid='Earn' or @data-testid='wallet.earn.home.earn']"),"Wallet Earn");
        triggerClickEvent(earn);
        LogUtils.info("Go to Wallet Earn page");
    }

	public void clickWalletDeposit(String walletCryptoDesc) {
		clickWallet();

		// Get crypto wallet row index
		int rowIdx = getCryptoActionBtn(walletCryptoDesc);

		// Click deposit button based on the specific crypto wallet
		clickCryptoActionBtn("Deposit", rowIdx);
	}

	public void clickWalletWithdraw(String walletCryptoDesc) {
		clickWallet();

		// Get crypto wallet row index
		int rowIdx = getCryptoActionBtn(walletCryptoDesc);

		// Click withdraw button based on the specific crypto wallet
		clickCryptoActionBtn("Withdraw", rowIdx);
	}

	public void clickWalletTransfer(String walletCryptoDesc) {
		clickWallet();

		// Get crypto wallet row index
		int rowIdx = getCryptoActionBtn(walletCryptoDesc);

		// Click transfer button based on the specific crypto wallet
		clickCryptoActionBtn("Transfer", rowIdx);
	}

	public void clickWalletConvert(String walletCryptoDesc) {
		clickWallet();

		// Get crypto wallet row index
		int rowIdx = getCryptoActionBtn(walletCryptoDesc);

		// Click convert button based on the specific crypto wallet
		clickCryptoActionBtn("Convert", rowIdx);
	}
    public void clickWalletCard(){
        waitLoading();
        WebElement e = assertElementExists(By.xpath("//li[@data-testid='V-Card']"), "Card button");
        triggerClickEvent(e);
        LogUtils.info("Go to Wallet Card page");
    }
	public int getCryptoActionBtn(String walletCryptoDesc) {
		assertVisibleElementExists(By.xpath("//div[@class='wallet_table']"), "Crypto Wallet List");

		// Get crypto wallet row index
		List<WebElement> rows = assertElementsExists(By.xpath("//div[@class='wallet_table']//div[contains(@class,'el-table__body-wrapper')]//table//tr"), "Crypto Wallet List");
		List<String> names = rows.stream()
				.map(row -> row.findElement(By.xpath(".//td/div")).getText().trim())
				.toList();
        int index = -1;
        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).contains(walletCryptoDesc)) {
                index = i + 1;
                break;
            }
        }
        return index;
	}

	public void clickCryptoActionBtn(String cryptoBtnDesc, int rowIdx) {
        Boolean isUat = goToWalletDetails(cryptoBtnDesc, rowIdx);
        if (!isUat){
            LogUtils.info("element is:"+"//div[@class='wallet_table']//div[contains(@class,'el-table__fixed-body-wrapper')]//table//tr[" + rowIdx + "]//td/div/span[normalize-space()='" + cryptoBtnDesc + "']");
            WebElement e = assertElementExists(By.xpath("//div[@class='wallet_table']//div[contains(@class,'el-table__fixed-body-wrapper')]//table//tr[" + rowIdx + "]//td/div/span[normalize-space()='" + cryptoBtnDesc + "']"), cryptoBtnDesc + " button");
            triggerClickEvent(e);
        }
	}

    public Boolean goToWalletDetails(String cryptoBtnDesc, int rowIdx){
        boolean flag = false;
        //钱包理财功能，需先进入详情，目前按钮文字有BUG，暂时先这么处理
        WebElement homeDetails = checkElementExists(By.xpath("//div[@class='wallet_table']//div[contains(@class,'el-table__fixed-body-wrapper')]//table//tr["+rowIdx+"]//span[@class='action'  and not(normalize-space(.)='Deposit' or normalize-space(.)='Withdraw' or normalize-space(.)='Transfer' or normalize-space(.)='Convert')]"));
        if (homeDetails != null && homeDetails.isDisplayed()){
            flag = true;
            triggerClickEvent(homeDetails);
        }
        //详情页点击对应出入金
        WebElement cryptoDetails = checkElementExists(By.xpath("//*[@class='el-drawer__body']//div[@class='currency-detail-footer']//span[normalize-space()='" + cryptoBtnDesc + "']"));
        if (cryptoDetails != null && cryptoDetails.isDisplayed()){
            triggerClickEvent(cryptoDetails);
            return flag;
        }else {
            flag = false;
        }
        return flag;
    }

	public void clickPaymentDetails() {
		clickFund();
		WebElement e = assertClickableElementExists(By.xpath("//li[@data-testid='menu.payDetails']"), "Payment Details menu");
		triggerElementClickEvent_withoutMoveElement(e);
	}

	public void clickTransactionHis() {
		clickFund();
		waitLoading();
		super.findVisibleElemntByXpath("//li[@data-testid='menu.transactionHis']").click();
		waitLoading();
	}

	public void clickProfile() {
		clickCPWebMenu(List.of(Map.entry(getProfileMenu(), "Profile menu")));
		LogUtils.info("Go to Profile page");
	}

    public void clickSecurityManagement() {
		WebElement webElement = getProfileSecurityMgmtMenuEle();
		triggerClickEvent_withoutMoveElement(webElement);
		LogUtils.info("Click Security Management Tab");
	}

	public void clickMyProfile() {
		super.findClickableElementByXpath("//*").click();
	}

	public void clickDownload() {
		clickCPWebMenu(List.of(Map.entry(getDownloadMenu(), "Download menu")));
		LogUtils.info("Go to Download page");
	}

	public void clickSupport() {
		clickCPWebMenu(List.of(Map.entry(getSupportMenu(), "Support menu")));
		LogUtils.info("Go to Support page");
	}

	public void clickCPWebMenu(List<Map.Entry<String, String>> entries) {
		if (entries == null || entries.isEmpty()) return;

		// Helper lambda to find menu element either in bar or horizontal list
		BiFunction<String, String, WebElement> findMenu = (path, desc) -> {
			WebElement menuEle = getMenuBarEle(path, desc);
			if (menuEle == null || isEleHidden(menuEle)) {
				triggerElementClickEvent_withoutMoveElement(getMenuMoreIconEle());
				LogUtils.info("Click Menu Ellipsis Icon");
				menuEle = getMenuHorizontalEle(path, desc);
			}
			return menuEle;
		};

		// Loop through all menu levels (main + submenus)
		for (int i = 0; i < entries.size(); i++) {
			Map.Entry<String, String> entry = entries.get(i);
			WebElement menuEle = (i == 0)
					? findMenu.apply(entry.getKey(), entry.getValue())
					: getMenuHorizontalEle(entry.getKey(), entry.getValue());

			if (menuEle == null) {
				Assert.fail(entry.getValue() + " not found");
			}

			// Skip clicking if expandable and already expanded
			if (isEleHasPopup(menuEle) && isElePopupExpanded(menuEle)) {
				continue;
			}

			triggerElementClickEvent(menuEle);
		}
	}

	public void clickChangePwd() {
		super.findClickableElementByXpath("//*").click();
	}

	public void click2FA() {
		super.findClickableElementByXpath("//li[@data-testid='menu.twoFactorAuthentication']").click();
	}
	public void clickChartsByTV() {
		super.findClickableElementByXpath("//*").click();
	}
	public void clickTraderTools() {
		super.findClickableElementByXpath("//*").click();
	}
	public void clickMarketBuzz() {
		super.findClickableElementByXpath("//*").click();
	}
	public void clickEcoCalendar() {
		super.findClickableElementByXpath("//*").click();
	}
	public void clickFeaturedIdea() {
		super.findClickableElementByXpath("//*").click();
	}
	public void clickAnalystViews() {
		super.findClickableElementByXpath("//*").click();
	}
	public void clickWidgets() {
		super.findClickableElementByXpath("//*").click();
	}
	public void clickTechnicalAnalysis() {
		super.findClickableElementByXpath("//*").click();
	}
	public void clickTutorials() {
		super.findClickableElementByXpath("//*").click();
	}

	public void clickContactUs() {
		super.findClickableElementByXpath("//*").click();
	}

	public void switchToCP() {
		String title = driver.getTitle();
		if(!title.toLowerCase().contains("ib")) {
			LogUtils.info("At CP portal. Do not need switch");
			return;
		}
		WebElement cp = this.findClickableElemntByTestId("redirectToCp");
		this.moveElementToVisible(cp);
		cp.click();
		this.waitLoading();
	}

	public void switchToIB() {
		String title = driver.getTitle();
		if(title.toLowerCase().contains("ib")) {
			LogUtils.info("At IB portal. Do not need switch");
			return;
		}
		WebElement ib = this.findClickableElemntByTestId("swith_to");
		this.moveElementToVisible(ib);
		ib.click();
		this.waitLoading();
	}

    public void switchToDAP() {
        String title = driver.getTitle();
        if(title.toLowerCase().contains("partner")) {
            LogUtils.info("At DAP portal. Do not need switch");
            return;
        }
        WebElement profilePanel = this.findClickableElementByXpath("//span[@class='ht-popover profile-panel-popover']");
        this.moveElementToVisible(profilePanel);
        profilePanel.click();

        WebElement dap = this.findClickableElementByXpath("//div[@class='ht-multiple-switcher portal-switch adaptive']/section/div[2]");
        this.moveElementToVisible(dap);
        dap.click();
        this.waitLoading();
    }

    public String getUserID() {
        WebElement profilePanel = this.findClickableElementByXpath("//span[@class='ht-popover profile-panel-popover']");
        this.moveElementToVisible(profilePanel);
        profilePanel.click();

        WebElement uidEle = this.findVisibleElemntByXpath("//span[@id='navBarHeaderUid']");
        return uidEle.getText();
    }

	public WebElement clickMobileMenu() {
		// Get mobile menu
		WebElement mobileMenu = getMobileMenuIconEle();

		// Get mobile menu list
		WebElement mobileMenuList = getMobileMenuListEle(mobileMenu);
		String style = mobileMenuList.getAttribute("style");
		if(style.contains("display")){
			triggerElementClickEvent_withoutMoveElement(mobileMenu);
		}

		return mobileMenu;
	}

	public void changeLanguage(String language) {
		waitLoading();
		if(GlobalProperties.isWeb) {
			setWebLanguage(language);
		} else {
			setMobileLanguage(language);
		}
	}

	public void setWebLanguage(String language) {
		// Get Language Icon
		WebElement languageIcon = getLanguageIconEle();
		String ddlId = languageIcon.getAttribute("aria-controls");

		// Get Language List by id
		WebElement languageList = getLanguageListEle(ddlId);
		// Check if language list is opened
		String style = languageList.getAttribute("style");
		if(style.contains("display")){
			triggerElementClickEvent_withoutMoveElement(languageIcon);
		}

		// Check active language same as expected language
		WebElement activeLanguage = getActiveLanguageEle(languageList);
		if(activeLanguage.getText().trim().toLowerCase().contains(language.trim().toLowerCase())) {
			LogUtils.info(language + " language is in use. No need to switch.");
			return;
		}

		// Set language
		WebElement languageItem = getLanguageItemEle(languageList, language);
		triggerElementClickEvent(languageItem);
		LogUtils.info("Change to language: " + language);
	}

    public String getActiveLanguage(){
        WebElement languageIcon = getLanguageIconEle();
        String ddlId = languageIcon.getAttribute("aria-controls");

        // Get Language List by id
        WebElement languageList = getLanguageListEle(ddlId);
        // Check if language list is opened
        String style = languageList.getAttribute("style");
        if(style.contains("display")){
            triggerElementClickEvent_withoutMoveElement(languageIcon);
        }

        // Check active language same as expected language
        WebElement activeLanguage = getActiveLanguageEle(languageList);
        return activeLanguage.getText().trim().toLowerCase();
    }

	public void setMobileLanguage(String language) {
		WebElement mobileMenu = clickMobileMenu();

		// Get Language Icon
		WebElement languageIcon = getMobileLanguageIconEle(mobileMenu);
		String ddlId = languageIcon.getAttribute("aria-controls");

		// Get Language List by id
		WebElement languageList = getMobileLanguageListEle(ddlId);
		// Check if language list is opened
		String hdn = languageList.getAttribute("aria-hidden");
		if("true".equalsIgnoreCase(hdn)){
			triggerElementClickEvent_withoutMoveElement(languageIcon);
		}

		// Check active language same as expected language
		WebElement activeLanguage = getMobileActiveLanguageEle(languageList);
		if(activeLanguage.getText().trim().toLowerCase().contains(language.trim().toLowerCase())) {
			LogUtils.info(language + " language is in use. No need to switch.");
			return;
		}

		// Set language
		WebElement languageItem = getMobileLanguageItemEle(languageList, language);
		triggerElementClickEvent(languageItem);
		LogUtils.info("Change to language: " + language);
	}

	//IB menu

	public void ibDashBoard() {
		WebElement e = this.findClickableElemntByTestId("click_/home");
		this.moveElementToVisible(e);
		e.click();
		this.waitLoading();
	}

	public void ibTransactionHistory() {
		this.waitLoading();
		WebElement e = assertElementExists(By.xpath("//*[@data-testid='click_/RebatePaymentHistory' or @data-testid='click_/rebatePaymentHistory']"), "IB Transaction History menu");
		triggerElementClickEvent(e);
		LogUtils.info("Go to IB Transaction History page");
	}

    public void ibRebateReport() {
        triggerElementClickEvent(this.getIBReportMenuEle());
        this.waitLoading();
        closeIBReportPopUp();
        triggerElementClickEvent(this.getRebateReportTabEle());
        LogUtils.info("Go to IB Report - Rebate Report menu page");
    }

    public void ibAccountReport() {
        WebElement e = assertElementExists(By.xpath("//span[@data-testid='click_/ibaccounts']"), "IB Account Report menu");
        triggerElementClickEvent(e);
        this.waitLoading();
        LogUtils.info("Go to IB Account Report menu page");
        closeIBReportPopUp();
    }

    public void ibClientReport() {
        WebElement e = assertElementExists(By.xpath("//span[@data-testid='click_/clientLeads']"), "IB Client Report menu");
        triggerElementClickEvent(e);
        this.waitLoading();
        LogUtils.info("Go to IB Client Report Page");
        closeIBReportPopUp();
    }

    public void ibReferralLinksMenu() {
//        WebElement e = assertElementExists(By.xpath("//span[@data-testid='click_/referralLinks']"), "IB Referral Links menu");
//        triggerElementClickEvent(e);
//        this.waitLoading();
        clickCPWebMenu(List.of(Map.entry(getReferralLinksMenu(), "Referral Links menu")));
        LogUtils.info("Go to IB Referral Links menu page");
        closeIBReportPopUp();
    }

    public void ibCampaignLinksMenu() {
//        WebElement e = assertElementExists(By.xpath("//span[@data-testid='click_/campaignLinks']"), "IB Campaign Links menu");
//        triggerElementClickEvent(e);
//        this.waitLoading();
        clickCPWebMenu(List.of(Map.entry(getCampaignLinksMenu(), "Campaign Links menu")));
        LogUtils.info("Go to IB Campaign Links menu page");
        closeIBReportPopUp();
    }

    public void ibProfileMenu() {
        WebElement e = assertElementExists(By.xpath("//span[@data-testid='click_/settings']"), "IB Profile menu");
        triggerElementClickEvent(e);
        this.waitLoading();
        LogUtils.info("Go to IB Profile menu page");
    }

    public void closeIBReportPopUp(){
        try {
            WebElement popUp = driver.findElement(By.xpath("//button[@class='driver-close-btn driver-close-only-btn']"));
            triggerElementClickEvent(popUp);
            LogUtils.info("IB Report Pop-Up Closed");
        }
        catch(Exception e) {
            LogUtils.info("IB Report Pop-Up Not Displayed");
        }

    }

    public void upgradeToIB(){
        triggerClickEvent_withoutMoveElement(getUpgradeIBEle());
        triggerClickEvent_withoutMoveElement(getGetStartedNowButtonEle());
        assertElementExists(By.xpath("//div[@class='rebate-account']"),"Rebate Account List");
    }

    public void agreeIBAgreement(){
        driver.switchTo().defaultContent();
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='el-dialog__wrapper ht-dialog overflow-limit' and not(contains(@style,'display: none'))] //div[@class='el-dialog__footer']/div/button")));
        triggerClickEvent_withoutMoveElement(getIBAgreementAgreeButton());
        LogUtils.info("Clicked Agree for IB Agreement");
    }

    //promotions
	public void clickPromotion() {
		refresh();
		MenuElements els = PageFactory.initElements(driver, MenuElements.class);
		String expand = els.promotion.getAttribute("aria-expanded");
		//expanded,return
		if(expand !=null && expand.toLowerCase().trim().equals("true")) {
			return;
		}
		this.moveElementToVisible(els.promotion);
		els.promotion.click();
	}

	public void depositBonus() {
		clickPromotion();
		MenuElements els = PageFactory.initElements(driver, MenuElements.class);
		this.moveElementToVisible(els.dpb);
		els.dpb.click();
		this.waitLoading();
	}

	public void tradingBonus() {
		clickPromotion();
		WebElement ele = this.findClickableElemntByTestId("menu.tradingBonus");
		this.moveElementToVisible(ele);
		ele.click();
		this.waitLoading();
	}

	public void clickOpenAdditionAccountMobile() {
		this.findClickableElementByXpath("(//div[@class='tab-bar-item'])[1]").click();
		this.findClickableElementByXpath("//div[@class='create_btn']").click();
		waitLoading();
	}

	public void clickDepositFundsMobile() {
		this.findClickableElementByXpath("(//div[@class='tab-bar-item'])[2]").click();
		this.findClickableElementByXpath("//*[@data-testid='menu.depositFund']").click();
		waitLoading();
	}

	public void clickWithdrawFundsMobile() {
		this.findClickableElementByXpath("(//div[@class='tab-bar-item'])[2]").click();
		this.findClickableElementByXpath("//li[@data-testid='menu.withdrawFunds']").click();
		waitLoading();
	}

	public void clickTransactionHisMobile() {
		this.goBack();
		this.findClickableElementByXpath("(//div[@class='tab-bar-item'])[2]").click();
		this.findVisibleElemntByXpath("//li[@data-testid='menu.transactionHis']").click();
	}
	protected WebElement getTradeEle() {
		return assertClickableElementExists(By.xpath("//li[@data-testid='trading.order.trade']"), "Trade menu");
	}
	public void clickTrade() {

        try{
            driver.findElement(By.xpath("//div[@role='dialog']//div[@class='ht-dialog__close']")).click();
        }
        catch(Exception e){
            LogUtils.info("No ad dialog display");
        }

        this.waitLoading();
		WebElement e = this.getTradeEle();
		triggerElementClickEvent_withoutMoveElement(e);
		LogUtils.info("Go to Trade page");
	}

	public void closeIBNotificationDialog() {}

    public void closeSkipDialog() {}

    // region [ Copy Trading ]

	protected WebElement getCopyTradingEle() {
		return assertClickableElementExists(By.xpath("//li[@id='copyTrading.copy_trading' and not (contains(@style,'visibility: hidden'))]"), "Copy Trading menu");
	}

	protected WebElement getCopyTradingDiscoverEle() {
		return findVisibleElemntBy(By.xpath("//li[@data-testid='copyTrading.discover' and not(ancestor-or-self::*[contains(@style,'display:none') or contains(@style,'display: none')])]"));
	}

	protected WebElement getCopyTradingCopierEle() {
		return assertClickableElementExists(By.xpath("//li[@data-testid='copyTrading.Copier' and not(ancestor-or-self::*[contains(@style,'display:none') or contains(@style,'display: none')])]"), "Copy Trading - Copier menu");
	}

	protected WebElement getCopyTradingSignalProviderEle() {
		return assertClickableElementExists(By.xpath("//li[@data-testid='copyTrading.signal_provider' and not(ancestor-or-self::*[contains(@style,'display:none') or contains(@style,'display: none')])]"), "Copy Trading - Signal Provider menu");
	}

	public void clickCopyTrading() {
		waitLoading();
		WebElement e = this.getCopyTradingEle();
		triggerElementClickEvent_withoutMoveElement(e);
		LogUtils.info("Open Copy Trading menu");
	}

	public void clickDiscover() {
		clickCopyTrading();
		WebElement e = this.getCopyTradingDiscoverEle();
		triggerElementClickEvent_withoutMoveElement(e);
		waitLoading();
		waitLoading();
		waitLoadingInCopyTrading();
		LogUtils.info("Go to Copy Trading - Discover page");
	}
	public void clickCopier() {
		int maxRetry = 3;
		int count = 0;
		List<WebElement> titleList;

		// Retry and check if the page refreshes successfully
		while (count <= maxRetry) {
			waitLoading();
			clickCopyTrading();
			WebElement e = this.getCopyTradingCopierEle();
			triggerElementClickEvent_withoutMoveElement(e);

			waitLoading();
			waitLoadingInCopyTrading();

			// Locate the title element to verify if the page has switched
			titleList = driver.findElements(By.cssSelector("div.title"));

			if (!titleList.isEmpty()) {
				LogUtils.info("Page refreshed successfully, target copier page reached.");
				break;
			}

			count++;
			LogUtils.info("Page not refreshed, retrying... Attempt: " + count);
		}

		WebElement title = driver.findElement(By.cssSelector("div.title"));
		Assert.assertTrue(title.getText().toLowerCase().contains("copier"), "Copier page is not opened");
		LogUtils.info("Successfully navigated to Copy Trading - Copier page");
	}

	public void clickSignalProvider() {
		clickCopyTrading();
		WebElement e = this.getCopyTradingSignalProviderEle();
		triggerElementClickEvent_withoutMoveElement(e);
		waitLoading();
		waitLoading();
		waitLoadingInCopyTrading();
		WebElement title = driver.findElement(By.cssSelector("div.title"));
		Assert.assertTrue(title.getText().toLowerCase().contains("signal provider"), "Signal Provider page is not opened");
		LogUtils.info("Go to Copy Trading - Signal Provider page");
	}

    public void closeAccQuizDialogPopup() {
        try {
            WebElement img = driver.findElement(By.xpath("//div[contains(@class,'el-drawer__wrapper ht-drawer') and not(contains(@style, 'display: none'))]//button[@aria-label='close drawer']"));
            js.executeScript("arguments[0].click()", img);
        }
        catch(Exception e) {
            LogUtils.info("No quiz pop up");
        }
    }

	// endregion

	public void closeCouponGuideDialog() {}

	public void closeBannerDialogB4Login() {
		WebElement popup = checkElementExists(By.xpath("//div[@class='el-dialog__wrapper' and not(contains(@style,'display'))]//img[@class='closeImg']"), "Banner Dialog");
		if (popup != null) {
			clickElement(popup);
			LogUtils.info("Close Banner Dialog");
		}
	}
}