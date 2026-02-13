package newcrm.business.pugbusiness;

import newcrm.business.businessbase.CPMenu;
import newcrm.business.businessbase.copyTrading.CPCopyTrading;
import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.LiveAccountsPage;
import newcrm.pages.pugclientpages.copyTrading.PUCopierPage;
import newcrm.pages.pugclientpages.copyTrading.PUDiscoverPage;
import newcrm.pages.pugclientpages.copyTrading.PUSignalProviderPage;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PUCPCopyTrading extends CPCopyTrading {
    protected PUDiscoverPage discoveryPage;
    protected PUCopierPage copierPage;
    protected PUSignalProviderPage signalProviderPage;

    public PUCPCopyTrading(WebDriver driver) {
        super(driver);
        this.discoveryPage = new PUDiscoverPage(driver);
        this.copierPage = new PUCopierPage(driver);
        this.signalProviderPage = new PUSignalProviderPage(driver);
    }
    public void selectCopierInDiscovery(String copier) {
        discoveryPage.selectCopierAccount(copier);
    }
    public void searchStrategy(String copier) {

        discoveryPage.searchProviderInDetailPage(copier);
    }
    public boolean checkCopierAccountInSP(String accNum) {
        return signalProviderPage.checkCopierAccount(accNum);
    }
    public boolean checkStrategyAccHistoryInfo(String strategyAcc) {
        return copierPage.checkStrategyAccHistoryInfo(strategyAcc);
    }
    public boolean compareCopierInfo(LiveAccountsPage.Account liveAccount) {
        return signalProviderPage.compareCopierAccountInfo(liveAccount);
    }
    //get signal copier account info in signal provider page
    public void getCopierAccTitleInfoInSP() {
        signalProviderPage.getCopierAccTitleInfo();
    }
    public void checkAndStopCopy() {
        copierPage.checkAndStopCopy();
    }
    public void getCopierAccTitleInfo() {
        copierPage.getCopierAccTitleInfo();
    }
    public boolean checkCopyAccPositionInfo(String copyAcc) {
        // compare copier and strategy account position
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
    @Override
    public String getStrategyDetailsDisplay() {
        return discoveryPage.getStrategyDetailsDisplay();
    }
    @Override
    public void backToDiscoverPage()
    {
        discoveryPage.backToDiscoverPage();
    }
    @Override
    public boolean compareListNameInRankList() {
        //No sub-list underRating list for pu
        return true;
        /*String listName = getTabListInRankList();
        GlobalMethods.printDebugInfo("getTabInRankList:" + listName);
        return StringUtils.containsIgnoreCase(listName.replaceAll("\\s+", ""), "RatingReturnCopiersWinRateRiskband");*/
    }
    public boolean compareSizeOfTop3InRankList() {
        return discoveryPage.getSizeOfTop3InRankList()==3;
    }

    public boolean comapreSizeOfTopRatingInRankList() {
        return discoveryPage.getSizeOfTopRatingInRankList()>=17 || discoveryPage.getSizeOfTopRatingInRankList()<=18;
    }

    public boolean compareSizeOfTopRatingInRankListAfterViewM() {
        return discoveryPage.getSizeOfTopRatingInRankList()>=37 || discoveryPage.getSizeOfTopRatingInRankList()<=38;
    }

    public boolean checkPositionInCopier() {
        return copierPage.checkPositionInCopier();
    }
    public String getBalanceInCopierPosition(){
        return copierPage.getPositionInfoInCoiper().getBalance();
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

        if (info.length < 8) {
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
       // contentCheck &= StringUtils.containsIgnoreCase(content, "Used Margin Multiples");
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

    public Map<String, String> getPositionInfoInCopier() {
        HashMap<String, String> copierAccInfo = new HashMap<>();
        copierAccInfo.put("balance", copierPage.getPositionInfoInCoiper().getBalance());
        copierAccInfo.put("equity", copierPage.getPositionInfoInCoiper().getEquity());

        return copierAccInfo;
    }

    public void comparePositionAndHistory(Map<String,String> position)
    {
        copierPage.comparePositionAndHistory(position);
        GlobalMethods.printDebugInfo("Compare position and history in copier page");
    }

    public int checkStrategyCount(){
        return signalProviderPage.getStrategyCount();
    }
    public boolean checkStrategyID(String strategyID)
    {
        String strategyName = signalProviderPage.getStrategyName();
        GlobalMethods.printDebugInfo("Check strategy ID:" + strategyName);
        return StringUtils.containsIgnoreCase(strategyName,strategyID);
    }

    public boolean checkHistorySize_SignalProvider()
    {
        int strategyCount = signalProviderPage.getHistoryCount();
        GlobalMethods.printDebugInfo("Check history size:" + strategyCount);
        return strategyCount>0;
    }
    public void clickMoreBtn(String strategyAccount)
    {
        signalProviderPage.clickMoreBtn(strategyAccount);
    }
    public boolean checkPositionSize_SignalProvider()
    {
        GlobalMethods.printDebugInfo("Check position size:" + signalProviderPage.getPositionCount());
        return signalProviderPage.getPositionCount()>=0;
    }
    public boolean checkPositionHistoryDate()
    {
        GlobalMethods.printDebugInfo("Check position history date");
        return signalProviderPage.checkPositionHistoryDate();
    }
    public List<String> getStrategyNameList()
    {
        return signalProviderPage.getStrategyNameList();
    }

    public void clickMoreBtn( )
    {
        signalProviderPage.clickMoreBtn();
    }
    public void chooseStrategy(String strategyName)
    {
        signalProviderPage.clickSignalProviderCenterTab();
        signalProviderPage.clickStrategiesTab();
        signalProviderPage.selectStrategy(strategyName);
    }
    public String getMoneyAllocatedInUpdateDialog(){
        return copierPage.getMoneyAllocatedInUpdateDialog();
    }
    public boolean ifHasPosition() {
        return copierPage.ifHasPosition();
    }
    public boolean submitCopyRequest(CPMenu menu, String copier, boolean needCopyDetail, boolean isCopyOpenTrade) {
        return discoveryPage.submitCopyRequest(menu, copier,needCopyDetail,true);
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
        contentCheck &= StringUtils.containsIgnoreCase(content, "Money Allocated");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Available Investment");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Used Credit");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Available Credit");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Used Balance");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Available Balance");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Stop Loss");
        /*contentCheck &= StringUtils.containsIgnoreCase(content, "Money Allocated");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Investment Balance");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Max Available Balance");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Total Investment");
        // contentCheck &= StringUtils.containsIgnoreCase(content, "Used Balance");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Max Available Investment");
        contentCheck &= StringUtils.containsIgnoreCase(content, "Stop Loss");*/

        if(contentCheck)
        {
            GlobalMethods.printDebugInfo("Check update dialog content - add fund passed");
        }
        return contentCheck;
    }

    public boolean checkStrategyHomePage(String strategyAccount) {
        return signalProviderPage.checkStrategyHomePage(strategyAccount);
    }
}
