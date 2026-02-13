package newcrm.testcases.cptestcases.copyTrading;

import newcrm.business.businessbase.copyTrading.CPCopyTrading;
import newcrm.business.businessbase.CPLiveAccounts;
import newcrm.business.businessbase.CPMenu;
import newcrm.factor.Factor;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.LiveAccountsPage;
import newcrm.testcases.BaseTestCaseNew;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import utils.CustomAssert;
import utils.LogUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CopyTradingTestCases extends BaseTestCaseNew {
    CPMenu menu;
    CPCopyTrading cpCopyTrading;
    CPLiveAccounts liveAccounts;
    LiveAccountsPage.Account acc = null;
    private Factor myfactor;
   @BeforeMethod(alwaysRun = true)
    public void initMethod(){
        if (myfactor == null){
            myfactor = getFactorNew();
        }
    }
    protected void initCopyTradingTest()
    {
        menu = myfactor.newInstance(CPMenu.class);
        cpCopyTrading = myfactor.newInstance(CPCopyTrading.class);
        liveAccounts = myfactor.newInstance(CPLiveAccounts.class);
        menu.goToMenu(GlobalProperties.CPMenuName.CPPORTAL);
        menu.changeLanguage("English");
        menu.goToMenu(GlobalProperties.CPMenuName.HOME);
    }
    public void testCopyTrading(String copierAccount,String strategyProvAccount)
    {
        stopCopyAndSubmitCopy(copierAccount,strategyProvAccount,false);
        //check copier account info in signal provider page
        menu.goToMenu(GlobalProperties.CPMenuName.SIGNALPROVIDER);
        cpCopyTrading.checkCopierAccountInSP(copierAccount);
        cpCopyTrading.getCopierAccTitleInfoInSP();

        //stop copy
        menu.goToMenu(GlobalProperties.CPMenuName.COPIER);
        Assert.assertTrue(cpCopyTrading.stopCopy(strategyProvAccount),"Stop copy failed");

    }

    public void testCopyTradingEUR_FixedLots(String copierAccount,String strategyProvAccount){
        String copyMode = "Fixed Lots";
        testCopyMode(copierAccount,strategyProvAccount,copyMode);
    }

    public void testCopyTradingEUR_FixedMultiples(String copierAccount,String strategyProvAccount){
        String copyMode = "Fixed Multiples";
        testCopyMode(copierAccount,strategyProvAccount,copyMode);
    }

    public void testCopyTradingEUR_EquivalentMargin(String copierAccount,String strategyProvAccount)
    {
        String copyMode = "Equivalent Used Margin";
        testCopyMode(copierAccount,strategyProvAccount,copyMode);
    }

    private void testCopyMode(String copierAccount,String strategyProvAccount,String copyMode)
    {
        //跟单账户无信用金，跟单-停止跟单
        stopCopyAndSubmitCopy(copierAccount, strategyProvAccount, false, copyMode);
        cpCopyTrading.stopCopy(strategyProvAccount);

        //跟单历史订单展示
        cpCopyTrading.clickPositionsInHome();
        cpCopyTrading.clickHistoryInHome();
        cpCopyTrading.clickPositionsInHome();

        cpCopyTrading.clickHistoryInHome();
        int count = 0;

        //retry history tab loaded 5 times
        int retryTime = 5;
        boolean historyLoaded = cpCopyTrading.checkStrategyAccHistoryInfo(strategyProvAccount);
        while(!historyLoaded && count < retryTime)
        {
            cpCopyTrading.clickPositionsInHome();
            cpCopyTrading.clickHistoryInHome();
            cpCopyTrading.clickPositionsInHome();
            cpCopyTrading.clickHistoryInHome();
            historyLoaded = cpCopyTrading.checkStrategyAccHistoryInfo(strategyProvAccount);
            count++;
            GlobalMethods.printDebugInfo("retry history tab loaded "+ count + " times");
        }
        Assert.assertTrue(cpCopyTrading.checkStrategyAccHistoryInfo(strategyProvAccount),"check history in copier home page failed");
    }

    //discover page检查
    public void testDiscoverCopyTrading(String signalProviderAccount,String signalProvider)
    {
        initCopyTradingTest();

        //check discover page title and three contents(Highest Annual return, Low risk and stable return, High Win Rate)
        menu.goToMenu(GlobalProperties.CPMenuName.DISCOVER);
        Assert.assertTrue(cpCopyTrading.checkDiscoverPageTitle(),"Discover page title is not correct");
        cpCopyTrading.checkDiscoverPageContent();

        /*String firstStrategyName = null;
        String secStrategyName = null;
        //recommendation: click first strategy in highest annual return tab, check strategy name in details page
        if(cpCopyTrading.isFirstStrategyExists()) {
            LogUtils.info("Check first strategy in highest annual return tab");
            String strategyDetails = cpCopyTrading.getFirstStrategyNameInHighestReturnTab();
            GlobalMethods.printDebugInfo("strategyDetails:" + strategyDetails);

            String[] firstParts = strategyDetails.split("\\n");
            firstStrategyName = firstParts[0].matches("\\d+") ? firstParts[1] : firstParts[0];
            GlobalMethods.printDebugInfo("First strategy name: " + firstStrategyName);
        }
        else
        {
            LogUtils.info("Not found first strategy in highest annual return tab");
        }

        if(cpCopyTrading.isSecondStrategyExists()) {
            String secStrategyDetails = cpCopyTrading.getSecStrategyNameInHighestReturnTab();
            GlobalMethods.printDebugInfo("SecStrategyDetails:" + secStrategyDetails);
            String[] secParts = secStrategyDetails.split("\\n");
            secStrategyName = secParts[0].matches("\\d+") ? secParts[1] : secParts[0];
            GlobalMethods.printDebugInfo("Second strategy name: " + secStrategyName);
        }
        else {
            LogUtils.info("Not found second strategy in highest annual return tab");
        }

        cpCopyTrading.clickFirstImgInHighestReturnTab();

        if(!cpCopyTrading.checkDiscoverDetailsPageTitle()){
            cpCopyTrading.clickSecImgInHighestReturnTab();
            cpCopyTrading.checkDiscoverDetailsPageTitle();
            //check strategy name is same with discover and details page
            String strategyNameInDetails = cpCopyTrading.getStrategyDetailsDisplay();
            GlobalMethods.printDebugInfo("Strategy name in discover detail page: " + strategyNameInDetails);
            CustomAssert.assertTrue(StringUtils.containsIgnoreCase(strategyNameInDetails, secStrategyName), "Strategy name in discover details page should be same with discovery page");
        }
        else{
            //check strategy name is same with discover and details page
            String strategyNameInDetails = cpCopyTrading.getStrategyDetailsDisplay();
            GlobalMethods.printDebugInfo("Strategy name in discover detail page: " + strategyNameInDetails);
            CustomAssert.assertTrue(StringUtils.containsIgnoreCase(strategyNameInDetails, firstStrategyName), "Strategy name in discover details page should be same with discovery page");
        }

        //go back from discover detail to discover page
        cpCopyTrading.backToDiscoverPage();

        //click copy of the first tab in the highest annual return tab: 第一个可能有问题，不能copy,就找第二个
        if(cpCopyTrading.clickFirstCopyBtnAndCheckWarningMsg())
        {
            LogUtils.info("Click first copy button");
            if(cpCopyTrading.checkCopyDialogDisplay()) {
                String strategyNameInCopyDialog = cpCopyTrading.getStrategyNameInCopyDialog().trim();
                //check strategy name in copy dialog
                Assert.assertEquals(strategyNameInCopyDialog, firstStrategyName.trim(), "Strategy name " + strategyNameInCopyDialog + "in copy dialog is not same with firstStrategyName:" + firstStrategyName);
                cpCopyTrading.closeCopyBtn();
            }
        }
        else
        {
            LogUtils.info("Click sec copy button");
            if(cpCopyTrading.clickSecCopyBtn())
            {
                if(cpCopyTrading.checkCopyDialogDisplay()) {
                    String strategyNameInCopyDialog = cpCopyTrading.getStrategyNameInCopyDialog().trim();
                    //check strategy name in copy dialog
                    Assert.assertEquals(strategyNameInCopyDialog, secStrategyName.trim(), "Strategy name " + strategyNameInCopyDialog + "in copy dialog is not same with firstStrategyName:" + secStrategyName);

                    cpCopyTrading.closeCopyBtn();
                }
            }
        }*/
        //rank list
        cpCopyTrading.clickRankList();

        //check rank list card info
        Assert.assertTrue(cpCopyTrading.compareListNameInRankList(),"Rank list name is not correct");
        Assert.assertTrue(cpCopyTrading.compareSizeOfTop3InRankList(),"check Top 3 rank list size correct");

        if(cpCopyTrading.getSizeInRankList()>= 17)
        {
            Assert.assertTrue(cpCopyTrading.comapreSizeOfTopRatingInRankList(), "check Top rating rank list size");
        }
        //click view (the fourth in rank list), check go to the detail page correct
        String fourthStrategyName = cpCopyTrading.getFourthStrategyNameInRankList().replaceAll("\\s+", "");
        GlobalMethods.printDebugInfo("FourthStrategyName in discover page:" + fourthStrategyName);
        cpCopyTrading.clickFourthViewInRankList();

        String strategyInfoInDetailPage = cpCopyTrading.getStrategyDetailsDisplay().replaceAll("\\s+", "");
        GlobalMethods.printDebugInfo("Strategy name in Detail Page:" + strategyInfoInDetailPage);

        Assert.assertTrue(StringUtils.containsIgnoreCase(strategyInfoInDetailPage, fourthStrategyName), "Fourth strategy name not found in detail page");

        //back to discover page
        cpCopyTrading.backToDiscoverPage();

        //check view more
        int MIN_RATING_SIZE_BEFORE = 17;
        if(cpCopyTrading.getSizeInRankList()>= MIN_RATING_SIZE_BEFORE) {
            cpCopyTrading.clickViewMoreBtn();
            GlobalMethods.printDebugInfo("Check view more button");
            int sizeAfterViewM = cpCopyTrading.sizeOfTopRatingInRankListAfterViewM();

            //only compare if there is more than 37 data after click view more
            Assert.assertTrue(cpCopyTrading.compareSizeOfTopRatingInRankListAfterViewM(), "Top rating rank list size");
            GlobalMethods.printDebugInfo("more strategy display correctly");
        }
        cpCopyTrading.searchStrategy(signalProviderAccount);

        //fuzzy search in strategy info Top strategies
        Assert.assertTrue(cpCopyTrading.checkTopStrategyAndProvider(),"Strategies and Signal Provider tab name in search page are not correct");
        Assert.assertTrue(cpCopyTrading.checkSearchResult(signalProviderAccount),"Search result is not correct");
        cpCopyTrading.clickSearchResultAndCopy(signalProviderAccount);

        //check discover detail page title, and strategy name in detail page display correct
        cpCopyTrading.checkDiscoverDetailsPageTitle();
        strategyInfoInDetailPage = cpCopyTrading.getStrategyDetailsDisplay().replaceAll("\\s+", "");
        GlobalMethods.printDebugInfo("strategy name in Detail Page:" + strategyInfoInDetailPage);

        Assert.assertTrue(StringUtils.containsIgnoreCase(strategyInfoInDetailPage, signalProviderAccount), "searched strategy name not found in detail page");

        cpCopyTrading.backToDiscoverPage();

        //click strategy info Top strategies, check go to detail page correct
        cpCopyTrading.clickSearch();

        //fuzzy search in strategy info signal provider
        cpCopyTrading.clickSignalProviderTab();
        cpCopyTrading.searchStrategyInSearchPage(signalProvider);

        //check search in signal provider
        Assert.assertTrue(cpCopyTrading.checkSearchResultInSignalProvider(signalProvider),"Search result is not correct");
        cpCopyTrading.clickSearchResultInSignalPrv();
        String signalProviderInfoInSignalPrvPage = cpCopyTrading.getsignalProviderInfoInSignalPrvPage().replaceAll("\\s+", "");
        Assert.assertTrue(StringUtils.containsIgnoreCase(signalProviderInfoInSignalPrvPage, signalProvider.replaceAll("\\s+", "")), "searched signal providers name not found in detail page");

    }
    //discover detail page检查
    public void testDiscoverDetailCopyTrading(String signalProviderAccount,String signalProvider)
    {
        initCopyTradingTest();

        //check discover page title and three contents(Highest Annual return, Low risk and stable return, High Win Rate)
        menu.goToMenu(GlobalProperties.CPMenuName.DISCOVER);
        Assert.assertTrue(cpCopyTrading.checkDiscoverPageTitle(),"Discover page title is not correct");

        //rank list
        cpCopyTrading.clickRankList();

        if(cpCopyTrading.getSizeInRankList()>= 4)
        {
            //click view (the fourth in rank list), check go to the detail page correct
            String fourthStrategyName = cpCopyTrading.getFourthStrategyNameInRankList().replaceAll("\\s+", "");
            GlobalMethods.printDebugInfo("FourthStrategyName in discover page:" + fourthStrategyName);
            cpCopyTrading.clickFourthViewInRankList();
        }
        else
        {
            String strategyDetails = cpCopyTrading.getFirstStrategyNameInHighestReturnTab();
            GlobalMethods.printDebugInfo("strategyDetails:" + strategyDetails);
        }

        Assert.assertTrue(cpCopyTrading.checkDiscoverDetailsPageTitle(),"Discover detail title is not correct");
        Assert.assertTrue(cpCopyTrading.checkDiscoverDetailTab(),"Discover detail tab info is not correct");

        //切换Overview选项卡
        cpCopyTrading.clickCopierReviewTab();
        cpCopyTrading.clickCopierReviewTab();

    }



    //Copier页面跟单position列表信息检查
    public void testCopierPositionCopyTrading(String copierAccount,String strategyProvAccount) {
        submitCopy(copierAccount,strategyProvAccount,true,true);

        //检查position，跟单订单详情页:点击detai，跳转跟单订单详情
        cpCopyTrading.clickDetail();
        Assert.assertTrue(cpCopyTrading.checkCopierPositionDetailPageTitle(),"check Copier position detail page");

        //订单详情中postion列表正常
        cpCopyTrading.clickPositionInDetail();
        boolean isPositionSizeGreaterThanZero =cpCopyTrading.checkPositionSizeInDetail();

        LogUtils.info("isPositionSizeGreaterThanZero:" + isPositionSizeGreaterThanZero);
        boolean isPendingOrderSizeGreaterThanZero = false;

        //如果有pending order检查pending order列表
        if(cpCopyTrading.checkPendingOrderDisplay()) {
            cpCopyTrading.clickPendingOrderInDetail();
            isPendingOrderSizeGreaterThanZero = cpCopyTrading.checkPendingSizeInDetail();
            LogUtils.info("isPendingOrderSizeGreaterThanZero:" + isPendingOrderSizeGreaterThanZero);
        }
        Assert.assertTrue(isPositionSizeGreaterThanZero || isPendingOrderSizeGreaterThanZero, "Position size or Pending order in copier detail page should be greater than 0");

        //订单详情中history列表正常
        cpCopyTrading.clickHistoryInDetail();
        Assert.assertTrue(cpCopyTrading.checkHistorySizeInDetail(),"History size in copier detail page should be greater than 0");
    }

    //Copier页面positions跟单订单详情页detail以及详情页manage菜单检查
    public void testCopierDetail(String copierAccount, String strategyProvAccount) {
        //copier trading - copier菜单触发,选择账号信息,positions列表信息展示，跟单信息卡片列表信息展示正常
        initCopyTradingTest();

        menu.goToMenu(GlobalProperties.CPMenuName.COPIER);
        cpCopyTrading.selectCopierAccountInCopier(copierAccount);

        //check position, submit copy request if no position
        if(!cpCopyTrading.checkPositionInCopier())
        {
            submitCopyRequest(menu,cpCopyTrading,copierAccount,strategyProvAccount,true,false);
        }

        //订单详情页点击manage，展示下拉功能菜单
        cpCopyTrading.clickDetail();

        Map<String,String> positionInfo = cpCopyTrading.getPositionInfoInCoiperDetail();
        double balanceBeforeAdd = Double.parseDouble(positionInfo.get("balance"));
        double equityBeforeAdd = Double.parseDouble(positionInfo.get("equity"));
        cpCopyTrading.clickManageInDetail();
        Assert.assertTrue(cpCopyTrading.checkManageMenuInDetail(),"check Manage menu in copier detail page");

        //Map<String,String> positionInfo = cpCopyTrading.getPositionInfoInCopier();
        //订单详情页点击manage，唤起追加资金页加载正常，页面信息展示正常

        cpCopyTrading.clickAddFundsInDetail();

        Assert.assertTrue(cpCopyTrading.checkUpdateTitle(),"check update dialog title");
        Assert.assertTrue(cpCopyTrading.checkUpdateDialogContent_addFund(),"check content info in add fund update dialog");
        Assert.assertTrue(cpCopyTrading.checkUpdateDialogBaseInfo_addFund(),"check base info in add fund update dialog");
        double moneyAllocated = Double.parseDouble(cpCopyTrading.getMoneyAllocatedInUpdateDialog());
        GlobalMethods.printDebugInfo("Money allocated: " + moneyAllocated);
        cpCopyTrading.updateFundUpdateDialog();

        //check money added
        positionInfo = cpCopyTrading.getPositionInfoInCoiperDetail();
        double balanceAfterAdd = Double.parseDouble(positionInfo.get("balance"));
        double equityAfterAdd = Double.parseDouble(positionInfo.get("equity"));

        double balanceExpected = moneyAllocated + balanceBeforeAdd;
        double equityExpected = moneyAllocated + equityBeforeAdd;
        double delta = 1e-9;
        GlobalMethods.printDebugInfo("Balance after add: " + balanceAfterAdd + " balanceExpected: " + balanceExpected
                + " equity after add:" + equityAfterAdd + " equityExpected: " + equityExpected) ;

        //check balance and equity added, 检查添加资金和equity
        Assert.assertEquals(balanceAfterAdd, balanceExpected, delta,
                String.format("Add fund for position, balance check. actual=%.17f, expected=%.17f, diff=%g",
                        balanceAfterAdd, balanceExpected, (balanceAfterAdd - balanceExpected)));

        Assert.assertEquals(equityAfterAdd, equityExpected, delta,
                String.format("Add fund for position, equity check. actual=%.17f, expected=%.17f, diff=%g",
                        equityAfterAdd, equityExpected, (equityAfterAdd - equityExpected)));

        //订单详情页点击manage，唤起减少资金页加载正常，页面信息展示正常
        positionInfo = cpCopyTrading.getPositionInfoInCoiperDetail();
        double balanceBeforeRemove = Double.parseDouble(positionInfo.get("balance"));
        double equityBeforeRemove = Double.parseDouble(positionInfo.get("equity"));
        GlobalMethods.printDebugInfo("Balance before remove: " + balanceBeforeRemove);
        cpCopyTrading.clickManageInDetail();
        cpCopyTrading.clickRemoveFundsInDetail();

        Assert.assertTrue(cpCopyTrading.checkUpdateTitle(),"update dialog title is not correct");
        Assert.assertTrue(cpCopyTrading.checkUpdateDialogContent_removeFund(),"check content info in remove fund update dialog");
        Assert.assertTrue(cpCopyTrading.checkUpdateDialogBaseInfo_removeFund(),"check base info in remove fund update dialog");

        double moneyRemoved = moneyAllocated;
        cpCopyTrading.setMoneyRemovedInUpdateDialog(moneyRemoved);
        GlobalMethods.printDebugInfo("Set money removed: " + moneyRemoved);
        cpCopyTrading.removeFundUpdateDialog();

        //check money removed
        positionInfo = cpCopyTrading.getPositionInfoInCoiperDetail();
        double balanceAfterRemove = Double.parseDouble(positionInfo.get("balance"));
        double equityAfterRemove = Double.parseDouble(positionInfo.get("equity"));
        balanceExpected = balanceBeforeRemove - moneyRemoved;
        equityExpected = equityBeforeRemove - moneyRemoved;
        GlobalMethods.printDebugInfo("Balance after remove: " + balanceAfterRemove + " balanceExpected: " + balanceExpected);

        //check balance and equity removed
        Assert.assertEquals(balanceAfterRemove, balanceExpected, delta,
                String.format("Remove fund for position. actual=%.17f, expected=%.17f, diff=%g",
                        balanceAfterRemove, balanceExpected, (balanceAfterRemove - balanceExpected)));

        Assert.assertEquals(equityAfterRemove, equityExpected, delta,
                String.format("Add fund for position, equity check. actual=%.17f, expected=%.17f, diff=%g",
                        equityAfterRemove, equityExpected, (equityAfterRemove - equityExpected)));

        //订单详情页点击manage，暂停跟单唤起二次确认框，确认框功能正常
        cpCopyTrading.clickManageInDetail();
        cpCopyTrading.checkManageMenuInDetail();
        if(cpCopyTrading.hasPauseCopy()) {
            Assert.assertFalse(cpCopyTrading.checkPauseCopyTextExist(),"check no paused copying display before click pause copying");
            cpCopyTrading.clickPauseCopyingInDetail();

            //click cancel,check pause copy dialog close
            cpCopyTrading.clickCancelInPauseCopyDialog();
            Assert.assertTrue(cpCopyTrading.checkPauseCopyDlgCancel(),"check Pause copy dialog cancel button");

            //click confirm, check menu change to resume copying
            cpCopyTrading.clickManageInDetail();
            cpCopyTrading.clickPauseCopyingInDetail();
            cpCopyTrading.clickConfirmInPauseCopyDialog();
            Assert.assertTrue(cpCopyTrading.checkPausedCopyTextInDetail(),"check 'Paused Copying' text after click pause copying");
        }

        //click resume copying, check menu change to pause copying
        cpCopyTrading.clickManageInDetail();

        if(!cpCopyTrading.hasPauseCopy()) {
            Assert.assertTrue(cpCopyTrading.checkResumeCopying(), "check 'resume copying' in copier detail page manage menu ");
            cpCopyTrading.clickResumeCopy();
            cpCopyTrading.clickCancelInResumeCopyDialog();
            Assert.assertTrue(cpCopyTrading.checkPauseCopyDlgCancel(), "check Resume copy dialog cancel button");

            //click confirm, check menu change to resume copying
            cpCopyTrading.clickManageInDetail();
            cpCopyTrading.clickResumeCopy();
            cpCopyTrading.clickConfirmInResumeCopyDialog();
        }
        //唤起more setting页加载正常，页面信息展示正常
        cpCopyTrading.clickManageInDetail();
        cpCopyTrading.clickMoreSettingInDetail();

        Assert.assertTrue(cpCopyTrading.checkUpdateTitle(),"check update dialog title");
        //* Assert.assertTrue(cpCopyTrading.checkUpdateDialogContent_moreSetting(),"check content info in more setting update dialog");*//*
        Assert.assertTrue(cpCopyTrading.checkUpdateDialogBaseInfo_moreSetting(),"check base info in more setting update dialog");
        cpCopyTrading.closeUpdateDialog();

        //订单详情页点击manage，停止跟单唤起二次确认框，确认框功能正常
        cpCopyTrading.clickManageInDetail();
        cpCopyTrading.clickStopCopyInDetail();

        //click cancel in stop copy, check stop copy dialog close
        cpCopyTrading.clickCancelInStopCopyDialog();
        Assert.assertTrue(cpCopyTrading.checkStopCopyDlgCancel(),"check Stop Copy dialog: 'Cancel' button");

        //click confirm, check position closed
       /* cpCopyTrading.clickManageInDetail();
        cpCopyTrading.stopCopy();
        Assert.assertTrue(cpCopyTrading.checkPositionSize(0),"Position size in copier detail page should be 0");*/
    }


    //Copier页面订单卡片manage检查
    public void testCopierManageMenu(String copierAccount,String strategyProvAccount) {
        //copier trading - copier菜单触发,选择账号信息,positions列表信息展示，跟单信息卡片列表信息展示正常
        initCopyTradingTest();

        menu.goToMenu(GlobalProperties.CPMenuName.COPIER);
        cpCopyTrading.selectCopierAccountInCopier(copierAccount);

        if(!cpCopyTrading.checkPositionInCopier())
        {
            submitCopyRequest(menu,cpCopyTrading,copierAccount,strategyProvAccount,true,false);
        }

        //订单卡片点击manage，展示下拉功能菜单
        double balanceBeforeAdd = Double.parseDouble(cpCopyTrading.getPositionInfoInCopier().get("balance"));
        double equityBeforeAdd = Double.parseDouble(cpCopyTrading.getPositionInfoInCopier().get("equity"));

        GlobalMethods.printDebugInfo("Balance before add: " + balanceBeforeAdd);
        cpCopyTrading.clickManageInDetail();
        Assert.assertTrue(cpCopyTrading.checkManageMenuInDetail(),"check Manage menu in copier detail page");

        //订单卡片点击manage，唤起追加资金页加载正常，页面信息展示正常
        cpCopyTrading.clickAddFundsInDetail();

        Assert.assertTrue(cpCopyTrading.checkUpdateTitle(),"check update dialog title");
        Assert.assertTrue(cpCopyTrading.checkUpdateDialogContent_addFund(),"check content info in add fund update dialog");
        Assert.assertTrue(cpCopyTrading.checkUpdateDialogBaseInfo_addFund(),"check base info in add fund update dialog");
        double moneyAllocated = Double.parseDouble(cpCopyTrading.getMoneyAllocatedInUpdateDialog());
        GlobalMethods.printDebugInfo("Money allocated: " + moneyAllocated);
        cpCopyTrading.updateFundUpdateDialog();

        //check money added
        double balanceAfterAdd = Double.parseDouble(cpCopyTrading.getPositionInfoInCopier().get("balance"));
        double equityAfterAdd = Double.parseDouble(cpCopyTrading.getPositionInfoInCopier().get("equity"));
        double balanceExpected = moneyAllocated + balanceBeforeAdd;
        double equityExpected = moneyAllocated + equityBeforeAdd;

        double delta = 1e-9;
        GlobalMethods.printDebugInfo("Balance after add: " + balanceAfterAdd + " balanceExpected: " + balanceExpected);

        Assert.assertEquals(balanceAfterAdd, balanceExpected, delta,
                String.format("Add fund for position, balance add. actual=%.17f, expected=%.17f, diff=%g",
                        balanceAfterAdd, balanceExpected, (balanceAfterAdd - balanceExpected)));

        Assert.assertEquals(equityAfterAdd, equityExpected, delta,
                String.format("Add fund for position, equity add. actual=%.17f, expected=%.17f, diff=%g",
                        balanceAfterAdd, balanceExpected, (balanceAfterAdd - balanceExpected)));

        //订单卡片点击manage，唤起减少资金页加载正常，页面信息展示正常
        double balanceBeforeRemove = Double.parseDouble(cpCopyTrading.getPositionInfoInCopier().get("balance"));
        double equityBeforeRemove = Double.parseDouble(cpCopyTrading.getPositionInfoInCopier().get("equity"));

        GlobalMethods.printDebugInfo("Balance before remove: " + balanceBeforeRemove);
        cpCopyTrading.clickManageInDetail();
        cpCopyTrading.clickRemoveFundsInDetail();

        Assert.assertTrue(cpCopyTrading.checkUpdateTitle(),"update dialog title is not correct");
        Assert.assertTrue(cpCopyTrading.checkUpdateDialogContent_removeFund(),"check content info in remove fund update dialog");
        Assert.assertTrue(cpCopyTrading.checkUpdateDialogBaseInfo_removeFund(),"check base info in remove fund update dialog");

        double moneyRemoved = 0.01;
        cpCopyTrading.setMoneyRemovedInUpdateDialog(moneyRemoved);
        GlobalMethods.printDebugInfo("Set money removed: " + moneyRemoved);
        cpCopyTrading.removeFundUpdateDialog();

        //check money removed
        double balanceAfterRemove = Double.parseDouble(cpCopyTrading.getPositionInfoInCopier().get("balance"));
        double equityAfterRemove = Double.parseDouble(cpCopyTrading.getPositionInfoInCopier().get("equity"));
        balanceExpected = balanceBeforeRemove - moneyRemoved;
        equityExpected = equityBeforeRemove - moneyRemoved;
        GlobalMethods.printDebugInfo("Balance after remove: " + balanceAfterRemove + " balanceExpected: " + balanceExpected);

        Assert.assertEquals(balanceAfterRemove, balanceExpected, delta,
                String.format("Remove fund for position, balance removed. actual=%.17f, expected=%.17f, diff=%g",
                        balanceAfterRemove, balanceExpected, (balanceAfterRemove - balanceExpected)));

        Assert.assertEquals(equityAfterRemove, equityExpected, delta,
                String.format("Remove fund for position, equity removed. actual=%.17f, expected=%.17f, diff=%g",
                        equityAfterRemove, equityExpected, (equityAfterRemove - equityExpected)));
        //cpCopyTrading.closeUpdateDialog();

        //订单卡片点击manage，暂停跟单唤起二次确认框，确认框功能正常
        cpCopyTrading.clickManageInDetail();
        cpCopyTrading.checkManageMenuInDetail();
        if(cpCopyTrading.hasPauseCopy())
        {
            Assert.assertFalse(cpCopyTrading.checkPauseCopyTextExist(),"check no 'pause copying' text display before pause copying");
            cpCopyTrading.clickPauseCopyingInDetail();

            //click cancel,check pause copy dialog close
            cpCopyTrading.clickCancelInPauseCopyDialog();
            CustomAssert.assertTrue(cpCopyTrading.checkPauseCopyDlgCancel(),"check Pause copy dialog cancel button");

            //click confirm, check menu change to resume copying
            cpCopyTrading.clickManageInDetail();
            cpCopyTrading.clickPauseCopyingInDetail();
            cpCopyTrading.clickConfirmInPauseCopyDialog();
            Assert.assertTrue(cpCopyTrading.checkPausedCopyText(),"check 'Paused Copying' text after click pause copying");
        }

        //订单卡片点击manage，停止跟单唤起二次确认框，确认框功能正常
        cpCopyTrading.clickManageInDetail();

        if(!cpCopyTrading.hasPauseCopy()) {
            Assert.assertTrue(cpCopyTrading.checkResumeCopying(), "check 'resume copying' in copier detail page manage menu ");
            cpCopyTrading.clickResumeCopy();
            cpCopyTrading.clickCancelInResumeCopyDialog();
            Assert.assertTrue(cpCopyTrading.checkPauseCopyDlgCancel(), "check Resume copy dialog cancel button");

            //click confirm, check menu change to resume copying
            cpCopyTrading.clickManageInDetail();
            cpCopyTrading.clickResumeCopy();
            cpCopyTrading.clickConfirmInResumeCopyDialog();
        }
        //订单卡片唤起more setting页加载正常，页面信息展示正常, 检查三种copymode是否正常update
        List<String> copyMode =Arrays.asList("Equivalent Used Margin", "Fixed Lots", "Fixed Multiples");
        for(String mode:copyMode) {
            cpCopyTrading.clickManageInDetail();
            cpCopyTrading.clickMoreSettingInDetail();
            cpCopyTrading.changeCopyMode(mode);
            if(Objects.equals(mode, copyMode.get(0)))
            {
                Assert.assertTrue(cpCopyTrading.checkUpdateTitle(), "check update dialog title");
                Assert.assertTrue(cpCopyTrading.checkUpdateDialogContent_moreSetting(), "check content info in more setting update dialog");
                Assert.assertTrue(cpCopyTrading.checkUpdateDialogBaseInfo_moreSetting(), "check base info in more setting update dialog");
            }
            Assert.assertTrue(cpCopyTrading.checkCopyMode(mode));
        }


       // cpCopyTrading.closeUpdateDialog();

        //订单卡片点击mamage，停止跟单唤起二次确认框，确认框功能正常
        cpCopyTrading.clickManageInDetail();
        cpCopyTrading.clickStopCopyInDetail();

        //click cancel in stop copy, check stop copy dialog close
        cpCopyTrading.clickCancelInStopCopyDialog();
        Assert.assertTrue(cpCopyTrading.checkStopCopyDlgCancel(),"check Stop Copy dialog: 'Cancel' button");

        /*//click confirm in stop copy, check position closed
        cpCopyTrading.clickManageInDetail();
        cpCopyTrading.stopCopy();
        Assert.assertTrue(cpCopyTrading.checkPositionSize(0),"Position size in copier detail page should be 0");*/
    }
    //Copier页面订单卡片history检查
    public void testCopierHistory(String copierAccount,String strategyProvAccount) {
        //copier trading - copier菜单触发,选择账号信息,positions列表信息展示，跟单信息卡片列表信息展示正常
        initCopyTradingTest();

        menu.goToMenu(GlobalProperties.CPMenuName.COPIER);
        cpCopyTrading.selectCopierAccountInCopier(copierAccount);

        if(!cpCopyTrading.checkPositionInCopier())
        {
            submitCopyRequest(menu,cpCopyTrading,copierAccount,strategyProvAccount,true,false);
        }

        Map<String,String> positionInfo = cpCopyTrading.getPositionInfoInCopier();

        //click confirm in stop copy, check position closed
        cpCopyTrading.clickManageInDetail();
        cpCopyTrading.stopCopy();
        Assert.assertTrue(cpCopyTrading.checkPositionSize(0),"Position size in copier detail page should be 0");

        //copier页面history，订单信息卡片列表
        cpCopyTrading.clickHistoryInHome();

        cpCopyTrading.comparePositionAndHistory(positionInfo);
        //CustomAssert.assertTrue(cpCopyTrading.checkStrategyAccHistoryInfo(positionInfo),"check history in copier home page");

        //History订单详情，点击订单卡片detail，跳转到订单详情页
        cpCopyTrading.clickDetailsBtnInHistory();
        Assert.assertTrue(cpCopyTrading.checkCopierHistoryDetailPageTitle(),"check copier history detail page title");
        Assert.assertTrue(cpCopyTrading.checkHistoryCardInHistory(),"check history size in copier history detail page");
    }

    public void testSignalProvider(String strategyAccount)
    {
        initCopyTradingTest();
        //check copier account info in signal provider page
        menu.goToMenu(GlobalProperties.CPMenuName.SIGNALPROVIDER);

        //选中public strategies account，点击下拉列表，检查copier账号信息
        cpCopyTrading.checkCopierAccountInSP(strategyAccount);
        cpCopyTrading.getCopierAccTitleInfoInSP();

        List<String> strategyList = Arrays.asList("Public", "Delisted","Draft");

        cpCopyTrading.chooseStrategy(strategyList.get(0));
        String strategyMode = strategyList.get(0);

        int strategyCount = cpCopyTrading.checkStrategyCount();

        if(strategyCount==0)
        {
            cpCopyTrading.chooseStrategy(strategyList.get(1));
            strategyMode = strategyList.get(1);
            strategyCount = cpCopyTrading.checkStrategyCount();
            if(strategyCount==0)
            {
                Assert.fail("Not found suitable signal provider, please check");
            }
        }

        for(String strategyName : cpCopyTrading.getStrategyNameList()) {
            if(strategyName.contains(strategyAccount)) {
                if(Objects.equals(strategyMode, strategyList.get(0))) {
                    //cpCopyTrading.checkStrategyID(strategyAccount);
                    //deListed this account
                    cpCopyTrading.clickMoreBtn(strategyAccount);
                    cpCopyTrading.delistStrategy();

                    //检查strategies -> deListed 列表展示正常
                    cpCopyTrading.checkStrategyMode("Delisted");
                    cpCopyTrading.checkStrategyID(strategyAccount);

                    //public this account
                    cpCopyTrading.clickMoreBtn(strategyAccount);
                    cpCopyTrading.publicStrategy();
                    cpCopyTrading.checkPublishMsg();
                    cpCopyTrading.clickOkInPublishMsg();
                    cpCopyTrading.checkStrategyID(strategyAccount);
                } else {
                    //public this account
                    cpCopyTrading.clickMoreBtn(strategyAccount);
                    cpCopyTrading.publicStrategy();
                    cpCopyTrading.checkPublishMsg();
                    cpCopyTrading.clickOkInPublishMsg();

                    cpCopyTrading.chooseStrategy("Public");
                    cpCopyTrading.checkStrategyID(strategyAccount);

                    cpCopyTrading.checkStrategyMode("Delisted");
                    cpCopyTrading.checkStrategyID(strategyAccount);
                }
                break;
            }
        }

        //check Profit Sharing Statement
        cpCopyTrading.clickMoreBtn(strategyAccount);
        cpCopyTrading.clickProfitSharing();
        Assert.assertTrue(cpCopyTrading.checkProfitSharingStatementSummary(strategyAccount),"check profit sharing statement summary");
        cpCopyTrading.backToSignalProviderPage();

        //check strategy homepage
        cpCopyTrading.clickMoreBtn(strategyAccount);
        cpCopyTrading.clickStrategyHomePage();
        Assert.assertTrue(cpCopyTrading.checkStrategyHomePage(strategyAccount),"check strategy Home detail page");

        //点击position，检查有position显示
        menu.goToMenu(GlobalProperties.CPMenuName.SIGNALPROVIDER);
        cpCopyTrading.checkCopierAccountInSP(strategyAccount);
        cpCopyTrading.clickPositionTab();
        Assert.assertTrue(cpCopyTrading.checkPositionSize_SignalProvider(),"check position size in signal provider page");

        //点击history，检查如期组件默认显示日期周期正确
        cpCopyTrading.clickHistoryTab_SignalProvider();
        Assert.assertTrue(cpCopyTrading.checkPositionHistoryDate(),"check date and history position in strategy history signal provider page");
    }

    public void  testSubSignalProvider(String strategyAccount) {

        String strategyName = "TestS"+ LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        GlobalMethods.printDebugInfo("Strategy name: " + strategyName);
        String updatedStrategyName;

        initCopyTradingTest();

        //check copier account info in signal provider page
        menu.goToMenu(GlobalProperties.CPMenuName.SIGNALPROVIDER);
        cpCopyTrading.checkCopierAccountInSP(strategyAccount);

        cpCopyTrading.chooseStrategy("Public");

        //delist public strategy if there is any
        while (!cpCopyTrading.getStrategyNameList().isEmpty()) {
            cpCopyTrading.clickMoreBtn();
            cpCopyTrading.delistStrategy();
            cpCopyTrading.chooseStrategy("Public");
        }

        //增加副策略
        cpCopyTrading.clickCreateStgyBtn();
        cpCopyTrading.createNewStrategy(strategyName);

        Assert.assertTrue(
                cpCopyTrading.getStrategyNameList().stream().anyMatch(s -> s.contains(strategyName)),
                "Not found new strategy: " + strategyName + " in returned list: ");

        Assert.assertTrue(cpCopyTrading.checkOfferCount(),"check offer count in signal provider page");

        //修改策略信息,发布成功之后策略信息展示正确
        updatedStrategyName = "UptT"+ LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        cpCopyTrading.clickEditStgyBtn();
        cpCopyTrading.editNewStrategy(updatedStrategyName);

        Assert.assertTrue(
                cpCopyTrading.getStrategyNameList().stream().anyMatch(s -> s.contains(updatedStrategyName)),
                "Not found updated strategy: " + updatedStrategyName + " in returned list");

        cpCopyTrading.clickMoreBtn();
        cpCopyTrading.delistStrategy();
    }
    protected void stopCopyAndSubmitCopy(String copierAccount,String strategyProvAccount,boolean needCopyDetail) {

        stopCopyAndSubmitCopy(copierAccount,strategyProvAccount, needCopyDetail,null);
    }
    protected void stopCopyAndSubmitCopy(String copierAccount,String strategyProvAccount,boolean needCopyDetail,String copyMode) {
        initCopyTradingTest();

        //stop copy if there is already a copy request
        menu.goToMenu(GlobalProperties.CPMenuName.COPIER);
        Assert.assertTrue(cpCopyTrading.selectCopierAccountInCopier(copierAccount),"Not found the copier account: " + copierAccount);
        cpCopyTrading.checkAndStopCopy();
        if(copyMode!=null) {
            submitCopyRequest(menu, cpCopyTrading, copierAccount, strategyProvAccount, needCopyDetail, false, copyMode);
        }
        else
            submitCopyRequest(menu, cpCopyTrading, copierAccount, strategyProvAccount, needCopyDetail, false);
    }

    protected void stopCopyAndSubmitCopy(String copierAccount,String strategyProvAccount,boolean needCopyDetail, boolean isCopyOpenTrade) {
        initCopyTradingTest();
        //stop copy if there is already a copy request
        menu.goToMenu(GlobalProperties.CPMenuName.COPIER);
        cpCopyTrading.selectCopierAccountInCopier(copierAccount);
        cpCopyTrading.checkAndStopCopy();
        submitCopyRequest(menu,cpCopyTrading,copierAccount,strategyProvAccount,needCopyDetail,isCopyOpenTrade);
    }
    protected void submitCopy(String copierAccount,String strategyProvAccount,boolean needCopyDetail, boolean isCopyOpenTrade) {
        initCopyTradingTest();
        //don't copy if there is already a copy request
        menu.goToMenu(GlobalProperties.CPMenuName.COPIER);
        cpCopyTrading.selectCopierAccountInCopier(copierAccount);
        if(!cpCopyTrading.ifHasPosition()){
            submitCopyRequest(menu,cpCopyTrading,copierAccount,strategyProvAccount,needCopyDetail,isCopyOpenTrade);
        }
    }

    public void testSignalProviderCopierReview() {
        initCopyTradingTest();
        //check copier account info in signal provider page
        menu.goToMenu(GlobalProperties.CPMenuName.SIGNALPROVIDER);
        Assert.assertTrue(cpCopyTrading.clickCopierReviewTab(),"Open Copier Review tab");
        List<String> copierMode = Arrays.asList("Approved","Pending","Rejected");
        for(String mode:copierMode) {
            Assert.assertTrue(cpCopyTrading.checkCopierReviewMode(mode), "check different mode for copier review." + " mode:" + mode);
        }
    }

    public void testSignalProviderAgentLink(String agentNickName) {
        initCopyTradingTest();
        //check copier account info in signal provider page
        menu.goToMenu(GlobalProperties.CPMenuName.SIGNALPROVIDER);
        Assert.assertTrue(cpCopyTrading.clickAgentLinkTab(),"Open Agent Link tab");
        List<String> linkStatus = Arrays.asList("Pending","Approved");
        for(String status:linkStatus) {
            Assert.assertTrue(cpCopyTrading.checkAgentLinkStatus(status,agentNickName), "check different status for agent link");
        }
    }

    protected void submitCopyRequest(CPMenu menu, CPCopyTrading cpCopyTrading, String copierAccount, String strategyProvAccount, boolean needCopyDetail, boolean isCopyOpenTrade) {
        submitCopyRequest(menu, cpCopyTrading, copierAccount, strategyProvAccount, needCopyDetail, isCopyOpenTrade, null);
    }

    protected void submitCopyRequest(CPMenu menu, CPCopyTrading cpCopyTrading, String copierAccount, String strategyProvAccount, boolean needCopyDetail, boolean isCopyOpenTrade, String copyMode) {
        //提交跟随请求
        menu.goToMenu(GlobalProperties.CPMenuName.DISCOVER);
        cpCopyTrading.selectCopierInDiscovery(copierAccount);
        cpCopyTrading.searchStrategy(strategyProvAccount);

        //根据 copyMode 是否为空，调用对应的方法
        boolean isSubmitSuccess;
        if (copyMode == null) {
            isSubmitSuccess = cpCopyTrading.submitCopyRequest(menu, strategyProvAccount, needCopyDetail, isCopyOpenTrade);
        } else {
            isSubmitSuccess = cpCopyTrading.submitCopyRequest(menu, strategyProvAccount, needCopyDetail, isCopyOpenTrade, copyMode);
        }
        Assert.assertTrue(isSubmitSuccess, "Submit copy request failed");

        //切换到 Copier 页面并验证账号信息
        menu.goToMenu(GlobalProperties.CPMenuName.COPIER);
        Assert.assertTrue(cpCopyTrading.selectCopierAccountInCopier(copierAccount), "Check copier account info in copier page");

        //检查持仓信息 (仅当不跟随开仓单时校验)
        cpCopyTrading.getCopierAccTitleInfo();
        if (!isCopyOpenTrade) {
            Assert.assertTrue(cpCopyTrading.checkCopyAccPositionInfo(strategyProvAccount),
                    "Check the position information of copier and signal provider account in copier page");
        }
    }

}
