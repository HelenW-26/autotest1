package newcrm.business.businessbase.copyTrading;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import newcrm.business.businessbase.CPMenu;
import newcrm.business.dbbusiness.UsersDB;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.LiveAccountsPage;
import newcrm.pages.clientpages.copyTrading.CopierPage;
import newcrm.pages.clientpages.copyTrading.DiscoverPage;
import newcrm.pages.clientpages.copyTrading.SignalProviderPage;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import utils.LogUtils;

import java.util.*;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class CPCopyTrading {

    protected DiscoverPage discoveryPage;
    protected CopierPage copierPage;
    protected SignalProviderPage signalProviderPage;
    public HashMap<String, String> userdetails;

    public CPCopyTrading(WebDriver driver) {
        this.discoveryPage = new DiscoverPage(driver);
        this.copierPage = new CopierPage(driver);
        this.signalProviderPage = new SignalProviderPage(driver);
    }
    public String getCopyAmount() {
        return discoveryPage.getCopyAmount();
    }

    public void clickCancelInPauseCopyDialog(){
        copierPage.clickCancelInPauseCopyDialog();
    }

    public void clickCancelInStopCopyDialog(){
        copierPage.clickCancelInStopCopyDialog();
    }

    public void clickCancelInResumeCopyDialog(){
        copierPage.clickCancelInResumeCopyDialog();
    }

    public void clickConfirmInPauseCopyDialog(){
        copierPage.confirmPauseCopy();
    }

    public boolean checkCopyDialogDisplay(){
        return discoveryPage.checkCopyDialogDisplay();
    }

    public boolean checkWarningMsgNotExist(){
        return discoveryPage.warnMsgNotExist();
    }

    public boolean clickFirstCopyBtnAndCheckWarningMsg(){
        return discoveryPage.clickFirstCopyBtnChekcMsg();
    }
    public boolean checkWarningMsg(){
        Optional<String> warningOpt = discoveryPage.getWarningMessageText();
        String msg = null;
        if (warningOpt.isPresent()) {
           msg = warningOpt.get();
        }
        return StringUtils.containsIgnoreCase(msg, "This strategy has been delisted by the signal provider");
    }
    public void clickConfirmInResumeCopyDialog(){
        copierPage.confirmResumeCopy();
    }
    public boolean checkPauseCopyDlgCancel(){
        return copierPage.checkPauseCopyDlgCancel();
    }

    public boolean checkStopCopyDlgCancel(){
        return copierPage.checkStopCopyDlgCancel();
    }

    public void clickResumeCopy() {
        copierPage.clickResumeCopy();
    }
    public void selectCopierInDiscovery(String copier) {
        discoveryPage.selectCopierAccount(copier);
    }
    public void searchStrategy(String copier) {
        discoveryPage.searchProviderInDiscoverPage(copier);
    }

    public void clickHistoryInHome() {
        copierPage.clickHistoryInHome();
        GlobalMethods.printDebugInfo("click history tab copier home page");
    }

    public void clickOverviewInDiscoverDetail() {
        discoveryPage.clickOverviewTab();
        GlobalMethods.printDebugInfo("click overview tab in discover detail page");
    }
    public void clickOrderInDiscoverDetail() {
        discoveryPage.clickOrderTab();
        GlobalMethods.printDebugInfo("click order tab in discover detail page");
    }
    public void clickCopiersInDiscoverDetail() {
        discoveryPage.clickCopiersTab();
        GlobalMethods.printDebugInfo("click watchers tab in discover detail page");
    }
    public void clickWatchersInDiscoverDetail() {
        discoveryPage.clickWatchersTab();
        GlobalMethods.printDebugInfo("click watchers tab in discover detail page");
    }
    public void clickDetailsBtnInHistory(){
        copierPage.clickDetailsInHistory();
    }

    public void clickPositionsInHome() {
        copierPage.clickPositionsInHome();
        GlobalMethods.printDebugInfo("click positions tab copier home page");
    }

    public boolean checkStrategyAccHistoryInfo(String strategyAcc) {
        return copierPage.checkStrategyAccHistoryInfo(strategyAcc);
    }
    public void searchStrategyInSearchPage(String copier) {
        discoveryPage.searchProviderInSearchPage(copier);
    }

  /*  public boolean submitCopyRequest(CPMenu menu, String copier,boolean needCopyDetail,boolean isCopyOpenTrade) {
        return discoveryPage.submitCopyRequest(menu, copier,needCopyDetail,true);
    }
    public boolean submitCopyRequest(CPMenu menu, String copier,boolean needCopyDetail,boolean isCopyOpenTrade,String copyMode) {
        return discoveryPage.submitCopyRequest(menu, copier,needCopyDetail,true, copyMode);
    }*/

    public boolean submitCopyRequest(CPMenu menu, String copier, boolean needCopyDetail, boolean isCopyOpenTrade) {
        return submitCopyRequest(menu, copier, needCopyDetail, true, null);
    }

    public boolean submitCopyRequest(CPMenu menu, String copier, boolean needCopyDetail, boolean isCopyOpenTrade, String copyMode) {
        return discoveryPage.submitCopyRequest(menu, copier, needCopyDetail, true, copyMode);
    }

    public boolean selectCopierAccountInCopier(String accNum) {
        return copierPage.selectCopierAccInCopier(accNum);
    }

    public void getCopierAccTitleInfo() {
        copierPage.getCopierAccTitleInfo();
    }

    public boolean checkCopyAccPositionInfo(String copyAcc) {
        // position info exists on the Copier page
        boolean hasPositionInfo = copierPage.checkStrategyAccPositionInfo(copyAcc);

        // account name exists on the Discovery page
        String expectedAccName = discoveryPage.getStrategyAccName().strip();   // Trim whitespace

        // Split by any whitespace and re-join with an underscore
        String expectedAcc = String.join("_", expectedAccName.trim().split("\\s+"));

        boolean providerAccountMatches = copierPage
                .getStrategyProvAccountList()   // List<?> of provider accounts
                .stream()
                .anyMatch(acc -> String.join("_", acc.getAccnum().trim().split("\\s+"))
                        .equalsIgnoreCase(expectedAcc));

        // Return true only if both conditions are met
        return hasPositionInfo && providerAccountMatches;
    }

    public Map<String, String> getPositionInfoInCopier() {
        HashMap<String, String> copierAccInfo = new HashMap<>();
        CopierPage.CopyTradingAccount position = copierPage.getPositionInfoInCoiper();
        copierAccInfo.put("balance", position.getBalance());
        copierAccInfo.put("equity", position.getEquity());

        return copierAccInfo;
    }

    public Map<String, String> getPositionInfoInCoiperDetail() {
        HashMap<String, String> copierAccInfo = new HashMap<>();
        CopierPage.CopyTradingAccount position = copierPage.getPositionInfoInCoiperDetail();
        copierAccInfo.put("balance", position.getBalance());
        copierAccInfo.put("equity", position.getEquity());

        return copierAccInfo;
    }

    //find signal copier account in signal provider page
    public boolean checkCopierAccountInSP(String accNum) {
        return signalProviderPage.checkCopierAccount(accNum);
    }

    //get signal copier account info in signal provider page
    public void getCopierAccTitleInfoInSP() {
        signalProviderPage.getCopierAccTitleInfo();
    }

    public boolean clickCopierReviewTab(){
        return signalProviderPage.getCopierReview();
    }

    public boolean clickAgentLinkTab(){
        return signalProviderPage.getAgentLink();
    }

    public boolean checkCopierReviewMode(String mode){
        return signalProviderPage.checkReviewMode(mode);
    }

    public boolean checkAgentLinkStatus(String status,String agentName){
        return signalProviderPage.checkAgentStatus(status,agentName);
    }

    public boolean stopCopy(String providerAccount) {
       return copierPage.stopCopy(providerAccount);
    }

    public boolean compareCopierInfo(LiveAccountsPage.Account liveAccount) {
        return signalProviderPage.compareCopierAccountInfo(liveAccount);
    }

    public void checkAndStopCopy() {
        copierPage.checkAndStopCopy();
    }
    //don't submit copy if has position
    public boolean ifHasPosition() {
        return copierPage.ifHasPosition();
    }

    public void stopCopy() {
        copierPage.stopCopy();
    }

    public boolean checkPositionSize(int size)
    {
        return copierPage.positionSizeCheck() == size;
    }

    public boolean checkPositionInCopier() {
        return copierPage.checkPositionInCopier();
    }

    public void openCopyAccount() {
        copierPage.openCopyAccount();
    }

    public void checkUserInfo(String email, GlobalProperties.ENV env, GlobalProperties.BRAND brand, GlobalProperties.REGULATOR regulator) {
        userdetails = new HashMap<>();
        String wcStatus[] = {"Processing", "Completed", "Rejected", "Pending", "Re_Audit", "RE_Register"};//"RE_Register=9"
        String idStatus[] = {"Submitted", "Pending", "Approved", "Rejected"};
        String poaStatus[] = {"Submitted", "Pending", "Approved", "Rejected"};
        UsersDB db = new UsersDB();

        JSONArray jsArray = db.getUserRegistrationInfo(email, env, brand, regulator);
        assertNotNull(jsArray);
        assertTrue(jsArray.size() > 0, "Do not find the user by email: " + email);

        JSONObject obj = jsArray.getJSONObject(0);
        userdetails.put("User ID", obj.getString("userId"));
        Integer pos = obj.getInteger("wcStatus");
        if (pos == 9) {
            pos = 5;
        }
        userdetails.put("WorldCheck", wcStatus[pos]);
        String status = obj.getString("idStatus");
        if (status == null || status.equalsIgnoreCase("null") || status.equalsIgnoreCase("")) {
            userdetails.put("ID Status", "Have not submitted");
        } else {
            userdetails.put("ID Status", idStatus[obj.getIntValue("idStatus")]);
        }
        status = obj.getString("addrStatus");
        if (status == null || status.equalsIgnoreCase("null") || status.equalsIgnoreCase("")) {
            userdetails.put("POA Status", "Have not submitted");
        } else {
            userdetails.put("POA Status", poaStatus[obj.getIntValue("addrStatus")]);
        }

        String vars = obj.getString("vars");
        if (vars != null && !vars.trim().equalsIgnoreCase("")) {
            JSONObject var = JSON.parseObject(vars);
            if (var == null) {
                Assert.fail("No welcome email content found for email " + email);
            }
            userdetails.put("Password", var.getString("PASSWORD"));
        }
    }

    public boolean checkDiscoverPageTitle() {
        GlobalMethods.printDebugInfo("Checking discover page title: copyTrading/discover/home");
        return StringUtils.containsIgnoreCase(discoveryPage.getPageUrl(), "copyTrading/discover/home");
    }

    public boolean checkDiscoverDetailsPageTitle() {
        GlobalMethods.printDebugInfo("Checking discover details page title: copyTrading/discover/discoverDetail");
        return StringUtils.containsIgnoreCase(discoveryPage.getPageUrl(), "copyTrading/discover/discoverDetail");
    }

    public boolean checkDiscoverDetailTab(){
        return discoveryPage.checkDiscoverDetail();
    }
    public boolean checkCopierPositionDetailPageTitle() {
        GlobalMethods.printDebugInfo("Checking copier position detail page title: copyTrading/copier/positionDetail");
        return StringUtils.containsIgnoreCase(copierPage.getPageUrl(), "copyTrading/copier/positionDetail");
    }

    public boolean checkCopierHistoryDetailPageTitle() {
        GlobalMethods.printDebugInfo("Checking copier position detail page title: copyTrading/copier/historyDetail");
        return StringUtils.containsIgnoreCase(copierPage.getPageUrl(), "copyTrading/copier/historyDetail");
    }

    public boolean checkHistoryCardInHistory(){
        return copierPage.getNumberofHistory()>0;
    }
    public void clickDetail(){
        copierPage.clickDetailBtn();
    }

    public void clickPositionInDetail(){
        copierPage.clickPositionsInDetail();
    }

    public void clickPendingOrderInDetail(){
        copierPage.clickPendingOrderInDetail();
    }

    public boolean checkPendingOrderDisplay(){
        return copierPage.checkPendingOrderDisplay();
    }
    public void clickHistoryInDetail(){
        copierPage.clickHistoryInDetail();
    }

    public boolean checkPositionSizeInDetail(){
       return copierPage.getPositionListSizeInDetail()>0;
    }
    public boolean checkPendingSizeInDetail(){
        return copierPage.getPendingPositionListSizeInDetail()>0;
    }

    public boolean checkHistorySizeInDetail(){
       return copierPage.getHistoryListSizeInDetail()>0;
    }

    public void clickManageInDetail(){
        if(!checkManageSubMenu()) {
            copierPage.clickManageInDetail();
        }
    }

    public boolean checkManageMenuInDetail(){
        String menuList  = copierPage.manageMenuInDetail();
        GlobalMethods.printDebugInfo("Check Manage menu list: " + menuList);
        return StringUtils.containsIgnoreCase(menuList, "add funds") &&
                StringUtils.containsIgnoreCase(menuList, "remove funds") &&
                (StringUtils.containsIgnoreCase(menuList, "pause copying") ||
                        StringUtils.containsIgnoreCase(menuList, "resume copying"))&&
                (StringUtils.containsIgnoreCase(menuList, "stop copy")
                        || StringUtils.containsIgnoreCase(menuList, "resume copy")) &&
                StringUtils.containsIgnoreCase(menuList, "more settings");
    }

    // true means has submenu, false means no submenu
    public boolean checkManageSubMenu()
    {
        return copierPage.subMenuInManage() != 0 ;
    }

    public boolean hasPauseCopy(){
        String menuList  = copierPage.manageMenuInDetail();
        return StringUtils.containsIgnoreCase(menuList,"pause copying");
    }

    public boolean checkResumeCopying(){
        String menuList  = copierPage.manageMenuInDetail();
        return StringUtils.containsIgnoreCase(menuList,"resume copying");
    }
    public void clickAddFundsInDetail(){
        copierPage.clickAddFundsInDetail();
    }
    public boolean checkUpdateTitle(){
        String title = copierPage.getUpdateDialogTitle();
        return title.equalsIgnoreCase("Update");
    }

    public String getMoneyAllocatedInUpdateDialog(){
        return copierPage.getMoneyAllocatedInUpdateDialog();
    }

    public void setMoneyRemovedInUpdateDialog(double moneyRemoved){
         copierPage.setMoneyRemovedInUpdateDialog(moneyRemoved);
    }
    public boolean checkUpdateDialogContent_addFund() {
        GlobalMethods.printDebugInfo("Start to check the content of update dialog - add fund");

        String content = copierPage.getContentInUpdateDialog();
        String stopLoss = copierPage.getStopLossInUpdateDialog();

        GlobalMethods.printDebugInfo("Update dialog - add fund content:\n" + content);

        if (StringUtils.isBlank(content)) {
            GlobalMethods.printDebugInfo("Update dialog - add fund content is blank");
            return false;
        }

        String[] info = content.split("\\R");

        if (info.length < 10) {
            GlobalMethods.printDebugInfo("The update dialog - add fund content info is insufficient, length=" + info.length);
            return false;
        }

        /*String availableInvestment = StringUtils.trimToEmpty(info[2]);
        String usedBalance         = StringUtils.trimToEmpty(info[10]);
        String availableBalance    = StringUtils.trimToEmpty(info[12]);*/

        HashMap<String, String> copyDetails = discoveryPage.getStrategyDetail();

        boolean contentCheck = true;

       /* contentCheck &= StringUtils.containsIgnoreCase(copyDetails.get("UsedBalance"),  usedBalance);
        contentCheck &= StringUtils.containsIgnoreCase(copyDetails.get("StopLoss"),  stopLoss);*/

        // 这些只是检查文本是否包含
        /*contentCheck &= StringUtils.containsIgnoreCase(content, "Money Allocated");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Available Investment");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Used Credit");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Available Credit");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Used Balance");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Available Balance");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Stop Loss");*/
        contentCheck &= StringUtils.containsIgnoreCase(content, "Money Allocated");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Investment Balance");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Max Available Balance");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Total Investment");
       // contentCheck &= StringUtils.containsIgnoreCase(content, "Used Balance");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Max Available Investment");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Stop Loss");

        if(contentCheck)
        {
            GlobalMethods.printDebugInfo("Check update dialog content - add fund passed");
        }
        return contentCheck;
    }

    public boolean checkUpdateDialogContent_moreSetting() {
        GlobalMethods.printDebugInfo("Start to check the content of update dialog - more setting");

        String content = copierPage.getContentInUpdateDialog();
        String stopLoss = copierPage.getStopLossInUpdateDialog();

        GlobalMethods.printDebugInfo("Update dialog- more setting content:\n" + content);

        if (StringUtils.isBlank(content)) {
            GlobalMethods.printDebugInfo("Update dialog- more setting Content is blank");
            return false;
        }

        String[] info = content.split("\\R");

        if (info.length < 11) {
            GlobalMethods.printDebugInfo("Update dialog- more setting content info is insufficient, length=" + info.length);
            return false;
        }

        /*String availableInvestment = StringUtils.trimToEmpty(info[2]);
        String usedBalance         = StringUtils.trimToEmpty(info[10]);
        String availableBalance    = StringUtils.trimToEmpty(info[12]);*/

        HashMap<String, String> copyDetails = discoveryPage.getStrategyDetail();

        boolean contentCheck = true;

       /* contentCheck &= StringUtils.containsIgnoreCase(copyDetails.get("UsedBalance"),  usedBalance);
        contentCheck &= StringUtils.containsIgnoreCase(copyDetails.get("StopLoss"),  stopLoss);*/

        // 这些只是检查文本是否包含
        contentCheck &= StringUtils.containsIgnoreCase(content, "Copy Mode");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Used Margin Multiples");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Stop Loss");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Estimated Loss");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Take Profit");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Lot Round-Up");

        if(contentCheck)
        {
            GlobalMethods.printDebugInfo("Check update dialog content - more setting passed");
        }
        return contentCheck;
    }

    public void changeCopyMode(String mode){
         copierPage.switchCopyMode(mode);
    }
    public boolean checkCopyMode(String mode){
        LogUtils.info("Checking Copy Mode: " + mode);
        return copierPage.checkCopyMode(mode);
    }
    public boolean checkUpdateDialogContent_removeFund(){
        String content = copierPage.getContentInUpdateDialog();
        GlobalMethods.printDebugInfo("Update dialog content: " + content);

        boolean contentCheck = true;

        contentCheck &= StringUtils.containsIgnoreCase(content, "Money Removed");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Removed Credit");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Max Removable Investment");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Removable Credit");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Removed Balance");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Removable Balance");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Stop Loss");
        if(contentCheck)
        {
            GlobalMethods.printDebugInfo("Check update dialog content info - remove fund passed");
        }

        return contentCheck;
    }

    public boolean checkUpdateDialogBaseInfo_addFund(){
        String baseInfo = copierPage.getBaseInfoInUpdateDialog();
        GlobalMethods.printDebugInfo("Add fund - Update dialog base info: " + baseInfo);

        boolean contentCheck = true;

        contentCheck &= StringUtils.containsIgnoreCase(baseInfo, "Return");
        contentCheck &= StringUtils.containsIgnoreCase(baseInfo, "Settlement");
        contentCheck &= StringUtils.containsIgnoreCase(baseInfo, "Profit Sharing");

        if(contentCheck)
        {
            GlobalMethods.printDebugInfo("Check update dialog base info - add fund passed");
        }
        return contentCheck;
    }

    public boolean checkUpdateDialogBaseInfo_moreSetting(){
        String baseInfo = copierPage.getBaseInfoInUpdateDialog();
        GlobalMethods.printDebugInfo("More setting - Update dialog base info: " + baseInfo);

        boolean contentCheck = true;

        contentCheck &= StringUtils.containsIgnoreCase(baseInfo, "Return");
        contentCheck &= StringUtils.containsIgnoreCase(baseInfo, "Settlement");
        contentCheck &= StringUtils.containsIgnoreCase(baseInfo, "Profit Sharing");

        if(contentCheck)
        {
            GlobalMethods.printDebugInfo("Check update dialog base info - more setting passed");
        }
        return contentCheck;
    }

    public void clickRemoveFundsInDetail(){
        copierPage.clickRemoveFundsInDetail();
    }

    public boolean checkUpdateDialogBaseInfo_removeFund(){
        String baseInfo = copierPage.getBaseInfoInUpdateDialog();
        GlobalMethods.printDebugInfo("Remove fund - Update dialog base info: " + baseInfo);

        boolean contentCheck = true;

        contentCheck = StringUtils.containsIgnoreCase(baseInfo, "Return");
        contentCheck &= StringUtils.containsIgnoreCase(baseInfo, "Settlement");
        contentCheck &= StringUtils.containsIgnoreCase(baseInfo, "Profit Sharing");

        if(contentCheck)
        {
            GlobalMethods.printDebugInfo("Check update dialog base info - remove fund passed");
        }
        return contentCheck;
    }

    public void clickPauseCopyingInDetail(){
        copierPage.clickPauseCopyingInDetail();
    }

    public void clickStopCopyInDetail(){
        copierPage.clickStopCopyInDetail();
    }

    public void clickMoreSettingInDetail(){
        copierPage.clickMoreSettingInDetail();
    }
    public String getFirstStrategyNameInHighestReturnTab() {
        return discoveryPage.getFirstStrategyNameInHighestReturnTab();
    }
    public String getSecStrategyNameInHighestReturnTab() {
        return discoveryPage.getSecStrategyNameInHighestReturnTab();
    }

    public boolean isFirstStrategyExists(){
        return discoveryPage.isFirstCardInHighestReturnTabExist();
    }

    public boolean isSecondStrategyExists(){
        return discoveryPage.isSecondCardInHighestReturnTabExist();
    }
    /*public void clickFirstCopyBtn() {
         discoveryPage.clickFirstCopyBtn();
    }*/

    public boolean clickSecCopyBtn(){
        return discoveryPage.clickSecCopyBtn();
    }

    public void clickFirstImgInHighestReturnTab() {
        discoveryPage.clickFirstImgInHighestReturnTab();
    }

    public void clickSecImgInHighestReturnTab() {
        discoveryPage.clickSecImgInHighestReturnTab();
    }

    public void clickFourthViewInRankList() {
        discoveryPage.clickFourthViewBtnInRankList();
    }

    public String getFourthStrategyNameInRankList() {
        return discoveryPage.getFourthStrategyNameInRankList();
    }

    public boolean checkDiscoverPageContent() {
        boolean recommendationTab = discoveryPage.getRecommendationTab().equalsIgnoreCase("Recommendation");
        boolean rankListTab = discoveryPage.getRankListTab().equalsIgnoreCase("Rank List");
        boolean recommendContent = discoveryPage.verifyRecommendationContent();

        GlobalMethods.printDebugInfo("check discover page content: recommendationTab:" + recommendationTab + " rankListTab:" + rankListTab + " recommendContent:" + recommendContent);
        return recommendationTab && rankListTab && recommendContent;
    }

    public String getStrategyDetailsDisplay() {
        return discoveryPage.getStrategyDetailsDisplay();
    }

    public String getsignalProviderInfoInSignalPrvPage() {
        return discoveryPage.getsignalProviderInfoInSignalPrvPage();
    }

    /*public void clickCopyBtn() {
        discoveryPage.clickCopyBtn();
    }*/

    public String getStrategyNameInCopyDialog() {
        return discoveryPage.getStrategyNameInCopyDialog();
    }

   /* public String getStrategyIDInCopyDialog() {
        return discoveryPage.getStrategyIDInCopyDialog();
    }*/

    public void closeCopyBtn() {
        discoveryPage.closeCopyBtn();
    }

    public void clickRankList() {
        discoveryPage.clickRankListTab();
    }

    public boolean compareListNameInRankList() {
        String listName = getTabListInRankList();
        GlobalMethods.printDebugInfo("getTabInRankList:" + listName);
        String s = listName.replaceAll("\\s+","");
        return StringUtils.containsIgnoreCase(s,"ratingreturncopierwinrateriskband")
                || StringUtils.containsIgnoreCase(s,"ratingreturncopierswinrateriskband");
    }

    public String getTabListInRankList() {
        return discoveryPage.getTabListInRankList();
    }

    public boolean compareSizeOfTop3InRankList() {
        boolean compareTp3 = discoveryPage.getSizeOfTop3InRankList()==3;

        return compareTp3;

    }

    public boolean comapreSizeOfTopRatingInRankList() {
        return discoveryPage.getSizeOfTopRatingInRankList()>=17;
    }

    public int getSizeInRankList(){
        return discoveryPage.getSizeOfTopRatingInRankList();
    }

    public void clickProfitSharing(){
        signalProviderPage.clickProfitSharingStatement();
    }

    public void clickStrategyHomePage(){
        signalProviderPage.clickStrategyHome();
    }

    public void backToSignalProviderPage(){
        signalProviderPage.backToSignalProviderPage();
    }
    public boolean checkProfitSharingStatementSummary(String strategyAccount) {
        return signalProviderPage.checkProfitSharingStatementSummary(strategyAccount);
    }
    public boolean checkStrategyHomePage(String strategyAccount) {
        return signalProviderPage.checkStrategyHomePage(strategyAccount);
    }
    public boolean compareSizeOfTopRatingInRankListAfterViewM() {
        int MIN_RATING_SIZE = 37;
        int MAX_RATING_SIZE = 40;
        int size = discoveryPage.getSizeOfTopRatingInRankList();
        return size>=MIN_RATING_SIZE && size<=MAX_RATING_SIZE;
    }
    public int sizeOfTopRatingInRankListAfterViewM() {
        return discoveryPage.getSizeOfTopRatingInRankList();
    }

    public void clickViewMoreBtn() {
        discoveryPage.clickViewMoreBtn();
    }

    public boolean checkTopStrategyAndProvider() {
        String tabName = "Strategies Signal Provider".replaceAll("\\s+", "");
        return StringUtils.containsIgnoreCase(discoveryPage.getTopStrategyAndProvider().replaceAll("\\s+", ""), tabName);
    }

    public boolean checkSearchResult(String signalProviderAccount)
    {
        String searchResult = discoveryPage.getSearchResult().replaceAll("\\s+","");
       return StringUtils.contains(searchResult,signalProviderAccount.replaceAll("\\s+",""));
    }

    public boolean checkSearchResultInSignalProvider(String signalProviderAccount)
    {
        String searchResult = discoveryPage.getSearchResultInSignalProvider().replaceAll("\\s+","");
        return StringUtils.contains(searchResult,signalProviderAccount.replaceAll("\\s+",""));
    }


    public void clickSearchResultAndCopy(String copier)
    {
        discoveryPage.clickSearchResultAndCopy(copier);
    }

    public void clickSearch()
    {
        discoveryPage.clickSearch();
    }

    public void backToDiscoverPage()
    {
        discoveryPage.backToDiscoverPage();
    }

    public void clickSignalProviderTab()
    {
    	discoveryPage.clickSignalProviderTab();
    }

    public void clickSearchResultInSignalPrv() {
        discoveryPage.clickSearchResultInSignalPrv();
    }

    public void closeUpdateDialog()
    {
    	copierPage.closeUpdateDialog();
        GlobalMethods.printDebugInfo("Close update dialog");
    }

    public void updateFundUpdateDialog()
    {
        copierPage.updateAddFund();
        GlobalMethods.printDebugInfo("Update fund in update dialog");
    }

    public void removeFundUpdateDialog()
    {
        copierPage.updateRemoveFund();
        GlobalMethods.printDebugInfo("Remove fund in update dialog");
    }

    public void comparePositionAndHistory(Map<String,String> position)
    {
        copierPage.comparePositionAndHistory(position);
        GlobalMethods.printDebugInfo("Compare position and history in copier page");
    }

    public boolean checkPausedCopyText(){
        String text = copierPage.getPausedCopyText().trim();
        GlobalMethods.printDebugInfo("Check paused copy text:" + text);
        return text.equalsIgnoreCase("Paused Copying");
    }

    public boolean checkPausedCopyTextInDetail(){
        String text = copierPage.getPausedCopyTextInDetail().trim();
        GlobalMethods.printDebugInfo("Check paused copy text:" + text);
        return StringUtils.containsIgnoreCase(text,"Paused Copying");
    }

    public boolean checkPauseCopyTextExist(){
        return copierPage.isPausedCopyTextExist()>0;
    }

    public void chooseStrategy(String strategyName)
    {
        //signalProviderPage.clickSignalProviderCenterTab();
        signalProviderPage.clickStrategiesTab();
        signalProviderPage.selectStrategy(strategyName);
    }

    public boolean checkStrategyID(String strategyID)
    {
        GlobalMethods.printDebugInfo("Check strategy ID:" + signalProviderPage.getStrategyName());
        return StringUtils.containsIgnoreCase(signalProviderPage.getStrategyName(),strategyID);
    }

    public List<String> getStrategyNameList()
    {
        return signalProviderPage.getStrategyNameList();
    }

    public boolean checkOfferCount(){
        return signalProviderPage.checkOfferCount();
    }

    public int checkStrategyCount(){
        return signalProviderPage.getStrategyCount();
    }

    public void clickPositionTab()
    {
        signalProviderPage.clickPositionsTab();
    }

    public boolean checkPositionSize_SignalProvider()
    {
        int positionCount = signalProviderPage.getPositionCount();
        GlobalMethods.printDebugInfo("Check position size:" + positionCount);
        return positionCount>=0;
    }

    public void clickHistoryTab_SignalProvider()
    {
        signalProviderPage.clickHistoryTab();
    }

    public boolean checkHistorySize_SignalProvider()
    {
        GlobalMethods.printDebugInfo("Check history size:" + signalProviderPage.getHistoryCount());
        return signalProviderPage.getHistoryCount()>0;
    }

    public boolean checkPositionHistoryDate()
    {
        GlobalMethods.printDebugInfo("Check position history date");
    	return signalProviderPage.checkPositionHistoryDate();
    }

    public void clickMoreBtn(String strategyAccount)
    {
        signalProviderPage.clickMoreBtn(strategyAccount);
    }

    public void clickMoreBtn( )
    {
        signalProviderPage.clickMoreBtn();
    }

    public void delistStrategy()
    {
        signalProviderPage.deListStrategy();
    }

    public void publicStrategy()
    {
        signalProviderPage.publicStrategy();
    }

    public boolean checkPublishMsg()
    {
        return signalProviderPage.getPublishMsg().equalsIgnoreCase("Publish Successful");
    }

    public void clickOkInPublishMsg()
    {
        signalProviderPage.clickOkInPublishMsg();
    }

    public boolean checkStrategyMode(String expectedMode)
    {
        GlobalMethods.printDebugInfo("Check strategy mode:" + signalProviderPage.getStrategyMode() + " expectedMode: " + expectedMode );

        return signalProviderPage.getStrategyMode().equalsIgnoreCase(expectedMode);
    }

    public void clickCreateStgyBtn(){
        signalProviderPage.clickCreateStgyBtn();
    }

    public void createNewStrategy(String nickName)
    {
        signalProviderPage.createNewStrategy(nickName);
    }
    public void clickEditStgyBtn(){
        signalProviderPage.clickEditStgyBtn();
    }
    public void editNewStrategy(String editNickName)
    {
        signalProviderPage.editNewStrategy(editNickName);
    }

}
