package newcrm.business.businessbase;


import org.openqa.selenium.WebDriver;

import newcrm.global.GlobalProperties.CPMenuName;
import newcrm.pages.clientpages.MenuPage;

public class CPMenu {

    protected MenuPage menu;
    public String walletCryptoDesc;

    public CPMenu(MenuPage menu) {
        this.menu = menu;
    }

    public CPMenu(WebDriver driver) {
        this.menu = new MenuPage(driver);
    }

    public void goToMenu(CPMenuName menuName) {

        switch (menuName) {
            case HOME:
                menu.clickHome();
                break;
            case LIVEACCOUNTS:
                menu.clickLiveAccount();
                break;
            case DEMOACCOUNTS:
                menu.clickDemoAccount();
                break;
            case DEPOSITFUNDS:
                menu.clickDepositFunds();
                break;
            case WITHDRAWFUNDS:
                menu.clickWithdrawFunds();
                break;
            case TRANSFERACCOUNTS:
                menu.clickTransferAcc();
                break;
            case PAYMENTDETAILS:
                menu.clickPaymentDetails();
                break;
            case TRANSACTIONHISTORY:
                menu.clickTransactionHis();
                break;
            case DOWNLOADS:
                menu.clickDownload();
                break;
            case SUPPORT:
                menu.clickSupport();
                break;
            case CHANGEPWD:
                menu.clickChangePwd();
                break;
            case CHARTSBYTRADINGVIEW:
                menu.clickChartsByTV();
                break;
            case CONTACTSUS:
                menu.clickContactUs();
                break;
            case MARKETWIDGETS:
                menu.clickMarketBuzz();
                break;
            case MYPROFILES:
                menu.clickMyProfile();
                break;
            case PREMIUMTRADERTOOLS:
                menu.clickTraderTools();
                break;
            case PREMIUMTRADERTUTORIALS:
                menu.clickTutorials();
                break;
            case PROFILES:
                menu.clickProfile();
                break;
            case TWOFA:
                menu.click2FA();
                break;
            case CPPORTAL:
                menu.switchToCP();
                break;
            case ADDACCOUNT:
                menu.clickOpenAdditionAccount();
                break;
            case ADDDEMOACCOUNT:
                menu.clickOpenAdditionDemoAccount();
                break;
            case WALLET_HOME:
                menu.clickWallet();
                break;
            case WALLET_CARD:
                menu.clickWalletCard();
                break;
            case CloseVCard:
                menu.CloseVCard();
                break;
            case CLOSE_VCARD_TIPS:
                menu.CloseVCardTips();
                break;
            case WALLET_EARN:
                menu.walletEarn();
                break;
            case WALLET_DEPOSIT:
                menu.clickWalletDeposit(walletCryptoDesc);
                break;
            case WALLET_WITHDRAW:
                menu.clickWalletWithdraw(walletCryptoDesc);
                break;
            case WALLET_TRANSFER:
                menu.clickWalletTransfer(walletCryptoDesc);
                break;
            case WALLET_CONVERT:
                menu.clickWalletConvert(walletCryptoDesc);
                break;
            case WALLET_TRANSACTION_HISTORY:
//                menu.clickWalletTransactionHistory();
                break;
            case COPYTRADING:
                menu.clickCopyTrading();
                break;
            case DISCOVER:
                menu.clickDiscover();
                break;
            case COPIER:
                menu.clickCopier();
                break;
            case SIGNALPROVIDER:
                menu.clickSignalProvider();
                break;
            case TRADE:
                menu.clickTrade();
                break;
            //ib
            case IBPORTAL:
                menu.switchToIB();
                break;
            case IBDASHBOARD:
                menu.ibDashBoard();
                break;
            case IBTRANSACTIONHISTORY:
                menu.ibTransactionHistory();
                break;
            case DEPOSITBONUS:
                menu.depositBonus();
                break;
            case TRADINGBONUS:
                menu.tradingBonus();
                break;
            case IBREBATEREPORT:
                menu.ibRebateReport();
                break;
            case IBACCOUNTREPORT:
                menu.ibAccountReport();
                break;
            case IBCLIENTREPORT:
                menu.ibClientReport();
                break;
            case IBREFERRALLINKSMENU:
                menu.ibReferralLinksMenu();
                break;
            case IBCAMPAIGNLINKSMENU:
                menu.ibCampaignLinksMenu();
                break;
            case IBPROFILEMENU:
                menu.ibProfileMenu();
                break;
            case UPGRADETOIB:
                menu.upgradeToIB();
                break;
            case AGREEIBAGREEMENT:
                menu.agreeIBAgreement();
                break;
            //dap
            case DAPPORTAL:
                menu.switchToDAP();
                break;
            //mobile
            case ADDACCOUNTMOBILE:
                menu.clickOpenAdditionAccountMobile();
                break;
            case DEPOSITFUNDSMOBILE:
                menu.clickDepositFundsMobile();
                break;
            case TRANSACTIONHISTORYMOBILE:
                menu.clickTransactionHisMobile();
                break;
            case SECURITYMANAGEMENT:
                menu.clickSecurityManagement();
        }

    }

    public void changeLanguage(String language) {
        if (language == null || language.trim().length() == 0) {
            return;
        }

        menu.changeLanguage(language);
    }

    public String getActiveLanguage(){
        return menu.getActiveLanguage();
    }

    public void refresh() {
        menu.refresh();
    }

    //for sreenshot
    public void waitLoading() {
        menu.waitLoading();
    }

    public void closeIBNotificationDialog() {
        this.waitLoading();
        menu.closeIBNotificationDialog();
    }

    public void closeSkipDialogPopup(){
        menu.closeSkipDialog();
    }

    public void closeCouponGuideDialog() {
        menu.closeCouponGuideDialog();
    }

    public void closeBannerDialogB4Login() {
        menu.closeBannerDialogB4Login();
    }

    public String getUserID() {
        return menu.getUserID();
    }

}
