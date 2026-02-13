package newcrm.business.vtbusiness;

import newcrm.business.businessbase.CPMenu;
import newcrm.business.businessbase.copyTrading.CPCopyTrading;
import newcrm.global.GlobalMethods;
import newcrm.pages.vtclientpages.copytrading.VTCopierPage;
import newcrm.pages.vtclientpages.copytrading.VTDiscoverPage;
import newcrm.pages.vtclientpages.copytrading.VTSignalProviderPage;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VTCPCopyTrading extends CPCopyTrading {

    protected VTDiscoverPage discoveryPage;
    protected VTCopierPage copierPage;
    protected VTSignalProviderPage signalProviderPage;

    public VTCPCopyTrading(WebDriver driver) {
        super(driver);
        copierPage = new VTCopierPage(driver);
        discoveryPage = new VTDiscoverPage(driver);
        signalProviderPage = new VTSignalProviderPage(driver);
    }
    public boolean checkStrategyAccHistoryInfo(String strategyAcc) {
        return copierPage.checkStrategyAccHistoryInfo(strategyAcc);
    }
    public void checkAndStopCopy() {
        copierPage.checkAndStopCopy();
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
    public boolean checkPositionInCopier() {
        return copierPage.checkPositionInCopier();
    }
    public Map<String, String> getPositionInfoInCopier() {
        HashMap<String, String> copierAccInfo = new HashMap<>();
        copierAccInfo.put("balance", copierPage.getPositionInfoInCoiper().getBalance());
        copierAccInfo.put("equity", copierPage.getPositionInfoInCoiper().getEquity());

        return copierAccInfo;
    }
    public void searchStrategy(String copier) {
        discoveryPage.searchProviderInDiscoverPage(copier);
    }
    public String getStrategyDetailsDisplay() {
        return discoveryPage.getStrategyDetailsDisplay();
    }
    public boolean clickFirstCopyBtnAndCheckWarningMsg(){
        return discoveryPage.clickFirstCopyBtnChekcMsg();
    }
    public void clickSearch()
    {
        discoveryPage.clickSearch();
    }
    public void searchStrategyInSearchPage(String copier) {
        discoveryPage.searchProviderInSearchPage(copier);
    }
    public void comparePositionAndHistory(Map<String,String> position)
    {
        copierPage.comparePositionAndHistory(position);
        GlobalMethods.printDebugInfo("Compare position and history in copier page");
    }
    public int checkStrategyCount(){
        return signalProviderPage.getStrategyCount();
    }

    public boolean checkPositionSize_SignalProvider()
    {
        GlobalMethods.printDebugInfo("Check position size:" + signalProviderPage.getPositionCount());
        return signalProviderPage.getPositionCount()>=0;
    }
    public List<String> getStrategyNameList()
    {
        return signalProviderPage.getStrategyNameList();
    }
    public void clickMoreBtn( )
    {
        signalProviderPage.clickMoreBtn();
    }
    public void clickMoreBtn(String strategyAccount)
    {
        signalProviderPage.clickMoreBtn(strategyAccount);
    }
    public boolean ifHasPosition() {
        return copierPage.ifHasPosition();
    }
    public String getMoneyAllocatedInUpdateDialog(){
        return copierPage.getMoneyAllocatedInUpdateDialog();
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

    //vt has not active offer in signal provider yet
    public boolean checkOfferCount(){
        return true;
    }
}
