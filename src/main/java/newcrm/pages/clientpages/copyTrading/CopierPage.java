package newcrm.pages.clientpages.copyTrading;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.Getter;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.Page;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utils.LogUtils;

public class CopierPage extends Page {
    protected CopyTradingAccount copierAccount = new CopyTradingAccount();
    protected CopyTradingAccount strategyProvAccount = new CopyTradingAccount();
    @Getter
    protected List<CopyTradingAccount> strategyProvAccountList = new ArrayList<>();

    @Data
    public class CopyTradingAccount {
        private String accnum;
        private String type;
        private GlobalProperties.CURRENCY currency;
        private String equity;
        private String pnL;
        private String balance;
        private String credit;
    }

    //copier home page
    protected WebElement getPositionDivEle() {
        return assertVisibleElementExists(By.xpath("//div[@class='title-info']//input"), "Position div in copier home page");
    }
    protected List<WebElement> getCopierListEle() {
        return assertVisibleElementsExists(By.xpath("//ul[@class='el-scrollbar__view el-select-dropdown__list']/li"), "Copier List");
    }
    protected List<WebElement> getCopierTitleInfoEle() {
        return assertVisibleElementsExists(By.xpath("//div[@class='data-box']/div[@class='common-data-bar']/div"), "Copier Title info");
    }
    protected By positionList = By.xpath("//div[@class='position']//div[contains(@class,'el-table__body')]");
    protected WebElement getFirstPositionEle() {
        return assertVisibleElementExists(By.xpath("(//div[@class='position']//div[contains(@class,'el-table__body')])[1]"), "First position info");
    }
   protected WebElement getManageBtnEle() {
       return assertVisibleElementExists(By.xpath("//button[./span[contains(text(),'Manage')]]"), "Manage button");
   }
    protected WebElement getStopCopyBtnEle() {
        return assertVisibleElementExists(By.xpath("//ul[contains(@id,'dropdown-menu')]/li[contains(text(),'Stop Copy')]"), "Stop Copy button");
    }
    protected WebElement getConfirmBtnEle() {
        return assertVisibleElementExists(By.xpath("//button[./span[contains(text(),'Confirm')]][not(ancestor::*[contains(@style,'display: none')])]"), "Confirm button");
    }
    protected WebElement getCloseTextDivEle() {
        return findVisibleElemntByXpath("//div[@class='close_text']");
    }
    protected List<WebElement> getCloseTextListEle() {
        return findVisibleElemntsBy(By.xpath("//div[@class='close_text']"));
    }
    protected WebElement getOkBtnEle() {
      //  return assertVisibleElementExists(By.xpath("(//button[./span[contains(text(),'Ok')]])[1]"), "Ok button");
        return findVisibleElemntBy(By.xpath("//button[./span[contains(text(),'Ok')]][not(ancestor::*[contains(@style,'display: none')])]"));
    }
   protected WebElement getOpenCopyAccountBtnEle() {
       return assertVisibleElementExists(By.xpath("//button[@data-testid='button'][.//span[contains(text(),'Open copy account')]]"), "Open Copy Account button");
   }
    protected WebElement getHistoryListEle() {
        return assertVisibleElementExists(By.xpath("//div[@class='history_container']//div[contains(@class,'el-table__body-wrapper')]//tr[1]"), "History List");
    }
    protected WebElement getHistoryTabEle() {
        return assertVisibleElementExists(By.xpath("//div[@id='tab-history']"), "History Tab");
    }
    protected WebElement getDetailsInHistoryEle() {
        return assertVisibleElementExists(By.xpath("(//span[normalize-space(.) = 'Details'])[1]"), "Details in History");
    }
   protected List<WebElement> gethistoryCardEle() {
       return assertVisibleElementsExists(By.xpath("//div[@class='partition_card']"), "History card");
   }
   protected WebElement getPositionsTabEle() {
       return assertVisibleElementExists(By.xpath("//div[@id='tab-position']"), "Positions Tab");
   }
    protected WebElement getDetailsBtnEle() {
        return assertVisibleElementExists(By.xpath("//button[./span[contains(text(),'Details')]]"), "Details button");
    }
    protected By pausedText = By.xpath("//*[@class='status pause']");
    protected WebElement getPausedTextInDetailEle() {
        return assertVisibleElementExists(By.xpath("//div[@class='secondary_info']"), "Paused text in detail");
    }
    //copier detail page
    protected WebElement getPositionsTabInDetailEle() {
        return findVisibleElemntBy(By.xpath("//div[@id='tab-positionDetail']"));
    }
    protected List<WebElement> checkPendingorderInDetailEle() {
        return driver.findElements(By.xpath("//div[@id='tab-pendingOpenDetail']"));
    }
    protected WebElement getPendingorderInDetailEle() {
        return findVisibleElemntBy(By.xpath("//div[@id='tab-pendingOpenDetail']"));
    }
    protected WebElement getHistoryTabInDetailEle() {
        return assertVisibleElementExists(By.xpath("//div[@id='tab-historyDetail']"), "history tab in detail");
    }
    protected List<WebElement> getPositionListInDetailEle() {
        return driver.findElements(By.xpath("//div[@class='list_wrapper']/div"));
    }
   protected List<WebElement> getHistoryListInDetailEle() {
       return assertVisibleElementsExists(By.xpath("//div[@class='list_wrapper']/div"), "history List in detail");
   }
    protected WebElement getManageBtnInDetailEle() {
        return assertVisibleElementExists(By.xpath("//button[@data-testid='button']/span[normalize-space(.) = 'Manage']"), "manage button in detail");
    }
    protected WebElement getManageMenuInDetailEle() {
        return assertVisibleElementExists(By.xpath("//ul[contains(@class,'el-dropdown-menu el-popper')][not(contains(@style,'display: none'))]"), "manage menu in detail");
    }
    protected By manageMenuInDetail = By.xpath("//ul[contains(@class,'el-dropdown-menu el-popper')][not(contains(@style,'display: none'))]");

    protected WebElement getAddFundsInDetailEle() {
        return assertVisibleElementExists(By.xpath("//ul[contains(@id,'dropdown-menu')]/li[translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz') = 'add funds']"), "add funds menu in detail");
    }
    protected WebElement getRemoveFundsInDetailEle() {
        return assertVisibleElementExists(By.xpath("//ul[contains(@id,'dropdown-menu')]/li[translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz') = 'remove funds']"), "remove funds menu in detail");
    }
    protected WebElement getPauseCopyInDetailEle() {
        return assertVisibleElementExists(By.xpath("//ul[contains(@id,'dropdown-menu')]/li[translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz') = 'pause copying']"), "pause copying menu in detail");
    }
   protected WebElement getResumeCopyingInDetailEle() {
       return assertVisibleElementExists(By.xpath("//ul[contains(@id,'dropdown-menu')]/li[translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz') = 'resume copying']"), "resume copying menu in detail");
   }
    protected WebElement getStopCopyInDetailEle() {
        return assertVisibleElementExists(By.xpath("//ul[contains(@id,'dropdown-menu')]/li[translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz') = 'stop copy']"), "stop copy menu in detail");
    }
    protected WebElement getMoreSettingInDetailEle() {
        return assertVisibleElementExists(By.xpath("//ul[contains(@id,'dropdown-menu')]/li[translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz') = 'more settings']"), "more setting menu in detail");
    }
    protected WebElement getCopyInfoInDetailEle() {
        return assertVisibleElementExists(By.xpath("//div[@class='data-info-box']"), "more setting menu in detail");
    }
    //update dialog
    protected WebElement getUpdateBtnEle() {
        return assertVisibleElementExists(By.xpath("//button/span[normalize-space(.)='Update']"), "update button");
    }
    protected WebElement getUpdateDialogTitleEle() {
        return assertVisibleElementExists(By.xpath("//div[@aria-label='Update']//span[@title='Update']"), "update dialog title");
    }
    protected WebElement getStrategyInfoInUpdateDialogEle() {
        return assertVisibleElementExists(By.xpath("//section[@class='el-drawer__body']//div[@class='info_warp info_small']"), "strategy Info In Update Dialog");
    }
    protected WebElement getBaseInfoInUpdateDialogEle() {
        return assertVisibleElementExists(By.xpath("//div[@class='base_info']"), "base Info in Update Dialog");
    }
    protected WebElement getContentInUpdateDialogEle() {
        return assertVisibleElementExists(By.xpath("//div[@class='operate_content']/form"), "content in Update Dialog");
    }

    protected  WebElement getUpdateMsgEle(){
        return assertVisibleElementExists(By.xpath("//div[@class='ht-dialog__title-content']"), "content in Update confirm Dialog");
    }
    protected WebElement getCloseBtnInUpdateEle() {
        return assertVisibleElementExists(By.xpath("//button[@aria-label='close Update']//i[@class='el-dialog__close el-icon el-icon-close']"), "close button in Update");
    }
    protected WebElement getMoneyAllocatedEle() {
        return assertVisibleElementExists(By.xpath("//div[contains(normalize-space(.),'Money Allocated')]//div[@class='form-item-investment-container']//input"), "money allocated");
    }
    protected WebElement getMoneyRemovedEle() {
        return assertVisibleElementExists(By.xpath("//div[normalize-space(.)='Money Removed'] /following::div[contains(@class,'el-form-item')][1]//input"), "money removed");
    }
    protected WebElement getStopLossEle() {
        return assertVisibleElementExists(By.xpath("//div[contains(normalize-space(.),'Stop Loss')]/ancestor::div[contains(@class,'label_row')][1]/following-sibling::*[1]//input[not(@type='hidden')][1]"), "stop loss");
    }
    //confirm pause copy dialog
    protected WebElement getCancelBtnInPauseCopyDialogEle() {
        return assertVisibleElementExists(By.xpath("(//div[contains(normalize-space(.),'Confirm pause copy')]//span[normalize-space(.)='Cancel'])[not(ancestor::*[contains(@style,'display: none')])]"), "cancel button in pause copy dialog");
    }
    protected WebElement getConfirmBtnInPauseCopyDialogEle() {
        return assertVisibleElementExists(By.xpath("//div[contains(normalize-space(.),'Confirm pause copy')]//span[normalize-space(.)='Confirm'][not(ancestor::*[contains(@style,'display: none')])]"), "confirm button in pause copy dialog");
    }
    protected By pauseCopyDialog = By.xpath("//div[@class='copy-trading-route-root']//div[@class='el-dialog'][not(ancestor::*[contains(@style,'display: none')])]");
    protected WebElement getCancelBtnInResumeCopyDialogEle() {
        return assertVisibleElementExists(By.xpath("(//div[contains(normalize-space(.),'Confirm resume copy')]//span[normalize-space(.)='Cancel'])[not(ancestor::*[contains(@style,'display: none')])]"), "cancel button in resume copy dialog");
    }
    protected WebElement getConfirmBtnInResumeCopyDialogEle() {
        return assertVisibleElementExists(By.xpath("//div[contains(normalize-space(.),'Confirm resume copy')]//span[normalize-space(.)='Confirm'][not(ancestor::*[contains(@style,'display: none')])]"), "confirm button in resume copy dialog");
    }
    protected WebElement getOkBtnInResumeCopyDialogEle() {
        return assertVisibleElementExists(By.xpath("//button[./span[contains(text(),'Ok')]][not(ancestor::*[contains(@style,'display: none')])]"), "ok button in resume copy dialog");
    }
    protected WebElement getCancelBtnInStopCopyDialogEle() {
        return assertVisibleElementExists(By.xpath("(//div[contains(normalize-space(.),'Confirm Stop Copy')]//span[normalize-space(.)='Cancel'])[not(ancestor::*[contains(@style,'display: none')])]"), "cancel button in resume copy dialog");
    }
    protected WebElement getCopyModeArrowEle() {
        return assertVisibleElementExists(By.xpath("//div[@class='el-form-item__content']//i[@class='el-select__caret el-input__icon el-icon-arrow-up']"),
                "Arrow beside copy mode");
    }
    protected WebElement getCodeModeInCopyDialogInputEle() {
        return assertVisibleElementExists(By.xpath("(//div[contains(normalize-space(.),'Copy Mode')]/ancestor::div[contains(@class,'el-form-item')])[1]"
                        + "//input[contains(@class,'el-input__inner') and not(@type='hidden')][1]"),
                "Copy Mode input in Copy dialog");
    }

    public CopierPage(WebDriver driver)
    {
        super(driver);
    }

    public void clickCancelInPauseCopyDialog()
    {
        getCancelBtnInPauseCopyDialogEle().click();
        GlobalMethods.printDebugInfo("Click Cancel in pause copy dialog");
    }

    public String getPausedCopyText(){
        waitLoading();
        waitLoadingInCopierHome();
        return findVisibleElemntBy(pausedText).getText();
    }

    public String getPausedCopyTextInDetail(){
        waitLoadingInCopierHome();
        return getPausedTextInDetailEle().getText();
    }
    public int isPausedCopyTextExist(){
        waitLoadingInCopierHome();
        List<WebElement> pausedTextCheck = driver.findElements(pausedText);
        return pausedTextCheck.size();
    }
    public int getNumberofHistory(){
       List<WebElement> historyList = gethistoryCardEle();
       GlobalMethods.printDebugInfo("Get number of history card in copier detail page: " + historyList.size());
       return historyList.size();
    }
    public void clickCancelInStopCopyDialog()
    {
        getCancelBtnInStopCopyDialogEle().click();
        GlobalMethods.printDebugInfo("Click Cancel in stop copy dialog");
    }

    public void clickDetailsInHistory(){
        getDetailsInHistoryEle().click();
        waitLoadingInCopierHome();
        GlobalMethods.printDebugInfo("Click Details in History");
    }
    public void clickCancelInResumeCopyDialog()
    {
        getCancelBtnInResumeCopyDialogEle().click();
        GlobalMethods.printDebugInfo("Click Cancel in Resume copy dialog");
    }

    public boolean checkPauseCopyDlgCancel()
    {
        waitLoading();
        return waitDialogClosed(pauseCopyDialog);
    }

    public boolean checkStopCopyDlgCancel()
    {
        waitLoading();
        return waitDialogClosed(pauseCopyDialog);
    }

    public void confirmPauseCopy()
    {
        getConfirmBtnInPauseCopyDialogEle().click();
        waitLoading();
        waitLoadingInCopyTrading();
        findVisibleElemntByXpath("//button[./span[contains(text(),'Ok')]][not(ancestor::*[contains(@style,'display: none')])]");
        getOkBtnEle().click();
        GlobalMethods.printDebugInfo("Click Confirm in Pause copy dialog");
    }

    public void confirmResumeCopy()
    {
        waitLoadingInCopyTrading();
        getConfirmBtnInResumeCopyDialogEle().click();
        getOkBtnInResumeCopyDialogEle().click();
        GlobalMethods.printDebugInfo("Click Confirm in Pause copy dialog");
    }

    public void clickResumeCopy(){
        waitLoading();
        getResumeCopyingInDetailEle().click();
        waitLoading();
    }
    private boolean waitDialogClosed(By dialogLocator) {
        return waitvisible.until(ExpectedConditions.invisibilityOfElementLocated(dialogLocator));
    }

    public String getContentInUpdateDialog()
    {
        waitLoading();
        return getContentInUpdateDialogEle().getText();
    }

    public String getMoneyAllocatedInUpdateDialog()
    {
        waitLoading();
        WebElement moneyA = getMoneyAllocatedEle();
        moneyA.clear();
        moneyA.sendKeys("0.01");
        return (String) js.executeScript("return arguments[0].value;", moneyA);
    }


    public void setMoneyRemovedInUpdateDialog(double moneyRmv)
    {
        waitLoadingInCopierHome();
        getMoneyRemovedEle().click();
        getMoneyRemovedEle().clear();
        getMoneyRemovedEle().sendKeys(String.valueOf(moneyRmv));
    }

    public String getStopLossInUpdateDialog()
    {
        waitLoading();
        return (String) js.executeScript("return arguments[0].value;", getStopLossEle());
    }

    public String getBaseInfoInUpdateDialog()
    {
        waitLoading();
        return getBaseInfoInUpdateDialogEle().getText();
    }

    public String getStrategyInfoInUpdateDialog()
    {
        waitLoading();
        String strategyInfo = getStrategyInfoInUpdateDialogEle().getText();
        GlobalMethods.printDebugInfo("Strategy info in update dialog: " + strategyInfo);
        return strategyInfo;
    }
    public String getUpdateDialogTitle()
    {
        waitLoading();
        String updateDialogTitle = getUpdateDialogTitleEle().getText();
        GlobalMethods.printDebugInfo("Update dialog title: " + updateDialogTitle);
        return updateDialogTitle;
    }
    public void clickAddFundsInDetail(){
        waitLoading();
        getAddFundsInDetailEle().click();
        GlobalMethods.printDebugInfo("click 'Add funds' in Manage");
    }

    public void clickRemoveFundsInDetail(){
        waitLoading();
        getRemoveFundsInDetailEle().click();
        GlobalMethods.printDebugInfo("click 'Remove funds' in Manage");
    }

    public void clickPauseCopyingInDetail(){
        waitLoading();
        getPauseCopyInDetailEle().click();
        GlobalMethods.printDebugInfo("click 'Pause copying' in Manage");
    }

    public void clickStopCopyInDetail(){
        waitLoading();
        getStopCopyInDetailEle().click();
        GlobalMethods.printDebugInfo("click 'Stop copy' in Manage");
    }

    public void clickMoreSettingInDetail(){
        waitLoading();
        getMoreSettingInDetailEle().click();
        GlobalMethods.printDebugInfo("click 'More setting' in Manage");
    }

    public String manageMenuInDetail()
    {
        waitLoading();
        return getManageMenuInDetailEle().getText();
    }

    public int subMenuInManage(){
        List<WebElement> subMenu = driver.findElements(manageMenuInDetail);
        return subMenu.size();
    }
    public void clickManageInDetail(){
        waitLoading();
        getManageBtnInDetailEle().click();
    }
    public void clickPositionsInDetail(){
        waitLoading();
        getPositionsTabInDetailEle().click();
    }
    public void clickPendingOrderInDetail(){
        waitLoading();
        getPendingorderInDetailEle().click();
    }
    public boolean checkPendingOrderDisplay(){
        waitLoading();
        return checkPendingorderInDetailEle().size()>0;
    }

    public void clickHistoryInDetail(){
        waitLoading();
        getHistoryTabInDetailEle().click();
    }

    public int getPositionListSizeInDetail()
    {
        waitLoading();
        List<WebElement> positionList = getPositionListInDetailEle();
        return positionList.size();
    }
    public int getPendingPositionListSizeInDetail()
    {
        waitLoading();
        List<WebElement> pendingpositionList = getPositionListInDetailEle();
        return pendingpositionList.size();
    }

    public int getHistoryListSizeInDetail()
    {
        waitLoading();
        List<WebElement> historyList = getHistoryListInDetailEle();
        return historyList.size();
    }
    public void clickPositionsInHome(){
        waitLoading();
        //wait for the status syn
        waitForCopyTrading(6000);
        waitLoadingInCopyTrading();
        getPositionsTabEle().click();
        waitLoadingInCopyTrading();
        waitForCopyTrading(2000);
        waitLoadingInCopyTrading();
    }
    public void clickHistoryInHome(){
        waitLoading();
       getHistoryTabEle().click();
       waitForCopyTrading(2000);
        waitLoadingInCopierHome();
        refresh();
    }
    public void clickDetailBtn(){
        waitLoading();
        getDetailsBtnEle().click();
        waitLoading();
        waitLoadingInCopyTrading();
    }

    public void closeUpdateDialog(){
        getCloseBtnInUpdateEle().click();
    }

    public void updateAddFund(){
        getUpdateBtnEle().click();
        waitLoadingInCopierHome();
        waitLoading();
        getOkBtnInResumeCopyDialogEle().click();
        waitLoadingInCopierHome();
    }

    public void updateRemoveFund(){
        getUpdateBtnEle().click();
        waitLoadingInCopierHome();
        getConfirmBtnEle().click();
        waitLoadingInCopierHome();
        getOkBtnInResumeCopyDialogEle().click();
        waitLoadingInCopierHome();
    }
    public String getPageUrl()
    {
        waitLoading();
        return getURL();
    }
    //check and select copier account
    public boolean selectCopierAccInCopier(String accNum)
    {
        waitLoading();
        waitLoadingInCopyTrading();
        WebElement positionEle = getPositionDivEle();
        js.executeScript("arguments[0].click();",positionEle);
        waitLoadingInCopierHome();
        List<WebElement> copierAccountList = getCopierListEle();

        for(WebElement e:copierAccountList)
        {
            String accInfo = e.getText();
            System.out.println("copier accInfo:" + accInfo);
            String[] accInfoArr = accInfo.split("-");

            String accNumInList = accInfoArr[0];
            String accCurrency = accInfoArr[1];
            String accTypeInfo = accInfoArr[2].replaceAll("\\s+","");

            GlobalMethods.printDebugInfo(accNumInList+ " " + accCurrency + " " + accTypeInfo + " " + accNum.equalsIgnoreCase(accNumInList) + " " +
                    StringUtils.containsIgnoreCase(accTypeInfo,"CopyTrading") );

            if(accNum.equalsIgnoreCase(accNumInList)
                    && StringUtils.containsIgnoreCase(accTypeInfo,"CopyTrading"))
            {
                GlobalMethods.printDebugInfo("Select copier account: " + accNumInList);
                copierAccount.setAccnum(accNumInList);
                copierAccount.setCurrency(GlobalProperties.CURRENCY.valueOf(accCurrency));
                copierAccount.setType(accTypeInfo);
                e.click();
                waitLoadingInCopyTrading();
                waitLoading();
                return true;
            }
        }
        return false;
    }

    public void getCopierAccTitleInfo() {
        List<WebElement> titleInfoList = getCopierTitleInfoEle();

        for (WebElement element : titleInfoList) {
            String titleInfo = element.getText();

            if (titleInfo == null || !titleInfo.contains("\n")) continue;

            String[] lines = titleInfo.split("\\n");
            if (lines.length < 2) continue;

            String value = lines[0].trim();
            String label = lines[1].toLowerCase().trim();

            if (label.contains("equity")) {
                copierAccount.setEquity(value);
            } else if (label.contains("pnl")) {
                copierAccount.setPnL(value);
            } else if (label.contains("balance")) {
                copierAccount.setBalance(value);
            } else if (label.contains("credit")) {
                copierAccount.setCredit(value);
            }
        }
        printCopyTradingAccount(copierAccount);
    }

    public CopyTradingAccount getPositionInfoInCoiper(){
        CopyTradingAccount strategyProvAccount = new CopyTradingAccount();
        //String positionInfo = driver.findElement(this.firstPosition).getText();
        String positionInfo = getFirstPositionEle().getText();
        System.out.println("signal provider position info: " + positionInfo);
        String accNum = null;
        String[] info = positionInfo.split("\\n");

        if (info.length >= 7) {
            accNum = info[0];
            strategyProvAccount.setAccnum(accNum);
            String pnlStr = info[2].replaceAll("[^\\d.-]", "");
            String balanceStr = info[3].split("\\s")[0].replaceAll("[^\\d.-]", "");
            String equityStr = info[4].split("\\s")[0].replaceAll("[^\\d.-]", "");

            if (isNumeric(pnlStr) && isNumeric(balanceStr) && isNumeric(equityStr)) {
                double pnl = Double.parseDouble(pnlStr);
                double balance = Double.parseDouble(balanceStr);
                double equity = Double.parseDouble(equityStr);

                strategyProvAccount.setPnL(String.format("%.2f", pnl));
                strategyProvAccount.setBalance(String.format("%.2f", balance));
                strategyProvAccount.setEquity(String.format("%.2f", equity));
            }
        }else{
            GlobalMethods.printDebugInfo("The positions info is insufficient: " + accNum);
        }
            return strategyProvAccount;
        }

    public CopyTradingAccount getPositionInfoInCoiperDetail(){
        waitLoading();
        waitLoadingInCopierHome();
        CopyTradingAccount strategyProvAccount = new CopyTradingAccount();
        String positionInfo = getCopyInfoInDetailEle().getText();
        System.out.println("signal provider position info in copier position detail: " + positionInfo);
        String accNum = null;
        String[] info = positionInfo.split("\\n");

        if (info.length >= 7) {
            String pnlStr = info[2].replaceAll("[^\\d.-]", "");
            String balanceStr = info[4].split("\\s")[0].replaceAll("[^\\d.-]", "");
            String equityStr = info[0].split("\\s")[0].replaceAll("[^\\d.-]", "");

            if (isNumeric(pnlStr) && isNumeric(balanceStr) && isNumeric(equityStr)) {
                double pnl = Double.parseDouble(pnlStr);
                double balance = Double.parseDouble(balanceStr);
                double equity = Double.parseDouble(equityStr);

                strategyProvAccount.setPnL(String.format("%.2f", pnl));
                strategyProvAccount.setBalance(String.format("%.2f", balance));
                strategyProvAccount.setEquity(String.format("%.2f", equity));
            }
        }else{
            GlobalMethods.printDebugInfo("The positions info is insufficient: " + accNum);
        }
        return strategyProvAccount;
    }
    public boolean checkStrategyAccPositionInfo(String strategyAcc) {
        //List<WebElement> postionList = driver.findElements(CopierPage.postionList);
        List<WebElement> postionList = driver.findElements(positionList);

        boolean isNumberCheck = false;

        double totalPnL = 0.0;
        double totalBalance = 0.0;
        double totalEquity = 0.0;
        String accNum = null;

        //add each line of position together to get total
        for (WebElement element : postionList) {
            CopyTradingAccount strategyProvAccount = new CopyTradingAccount();
            String positionInfo = element.getText();
            System.out.println("signal provider position info: " + positionInfo);

                String[] info = positionInfo.split("\\n");

                if (info.length >= 7) {
                    accNum = info[0];
                    strategyProvAccount.setAccnum(accNum);
                    String pnlStr = info[2].replaceAll("[^\\d.-]", "");
                    String balanceStr = info[3].split("\\s")[0].replaceAll("[^\\d.-]", "");
                    String equityStr = info[4].split("\\s")[0].replaceAll("[^\\d.-]", "");

                    if (isNumeric(pnlStr) && isNumeric(balanceStr) && isNumeric(equityStr)) {
                        double pnl = Double.parseDouble(pnlStr);
                        double balance = Double.parseDouble(balanceStr);
                        double equity = Double.parseDouble(equityStr);

                        strategyProvAccount.setPnL(String.format("%.2f", pnl));
                        strategyProvAccount.setBalance(String.format("%.2f", balance));
                        strategyProvAccount.setEquity(String.format("%.2f", equity));

                        strategyProvAccountList.add(strategyProvAccount);
                        totalPnL += pnl;
                        totalBalance += balance;
                        totalEquity += equity;

                        isNumberCheck = true;
                    }
                    GlobalMethods.printDebugInfo("The positions info for strategy account: " + accNum);
                    printCopyTradingAccount(strategyProvAccount);
                }
            }

        strategyProvAccount.setPnL(String.format("%.2f", totalPnL));
        strategyProvAccount.setBalance(String.format("%.2f", totalBalance));
        strategyProvAccount.setEquity(String.format("%.2f", totalEquity));

        GlobalMethods.printDebugInfo("The positions info for strategy account total: ");
        printCopyTradingAccount(strategyProvAccount);

        GlobalMethods.printDebugInfo("isNumberCheck: " + isNumberCheck +
                " equityCheck: " + strategyProvAccount.getEquity().equalsIgnoreCase(copierAccount.getEquity()) +
                " balanceCheck: " + strategyProvAccount.getBalance().equalsIgnoreCase(copierAccount.getBalance()) );

        //compare the total position info in position tab with copier account
        return isNumberCheck &&
                strategyProvAccount.getEquity().equalsIgnoreCase(copierAccount.getEquity()) &&
                strategyProvAccount.getBalance().equalsIgnoreCase(copierAccount.getBalance());
    }


    public boolean comparePositionAndHistory(Map<String,String> position){

        String balanceInPosition = position.get("balance");
        String equityInPosition = position.get("equity");

        String balanceInHistory = null;
        String equityInHistory = null;

        GlobalMethods.printDebugInfo("balanceInPosition: " + balanceInPosition + " equityInPosition: " + equityInPosition);

        //等待关单同步到history
        waitForCopyTrading(3000);

        String positionInfo = getHistoryListEle().getText();
        GlobalMethods.printDebugInfo("signal provider position info: " + positionInfo);

        if (StringUtils.isBlank(positionInfo)) {
            GlobalMethods.printDebugInfo("positionInfo is null/blank");
            return false;
        }

        String[] info = positionInfo.split("\\n");

        if (info.length < 7) {
            GlobalMethods.printDebugInfo("positionInfo has insufficient fields: " + info.length);
            return false;
        }

        if (info.length >= 7) {

            balanceInHistory = info[4].split("\\s")[0].replaceAll("[^\\d.-]", "");
            equityInHistory = info[5].split("\\s")[0].replaceAll("[^\\d.-]", "");

            GlobalMethods.printDebugInfo("balanceInHistory: " + balanceInHistory + " equityInHistory: " + equityInHistory);
        }

        return StringUtils.containsIgnoreCase(balanceInPosition, balanceInHistory) &&
                StringUtils.containsIgnoreCase(equityInPosition, equityInHistory);

    }
    public boolean checkStrategyAccHistoryInfo(String strategyAcc) {
        boolean isNumberCheck = false;
        String accNum = null;

        //get the latest record in history
        CopyTradingAccount strategyProvHistoryAccount = new CopyTradingAccount();
        String positionInfo = getHistoryListEle().getText();
        System.out.println("signal provider position info: " + positionInfo);

        if (StringUtils.isBlank(positionInfo)) {
            GlobalMethods.printDebugInfo("positionInfo is null/blank");
            return false;
        }

        String[] info = positionInfo.split("\\n");

        if (info.length < 7) {
            GlobalMethods.printDebugInfo("positionInfo has insufficient fields: " + info.length);
            return false;
        }

        if (info.length >= 7) {
            accNum = info[2];
            strategyProvHistoryAccount.setAccnum(accNum);
            String pnlStr = info[3].replaceAll("[^\\d.-]", "");
            String balanceStr = info[4].split("\\s")[0].replaceAll("[^\\d.-]", "");
            String equityStr = info[5].split("\\s")[0].replaceAll("[^\\d.-]", "");

            if (isNumeric(pnlStr) && isNumeric(balanceStr) && isNumeric(equityStr)) {
                double pnl = Double.parseDouble(pnlStr);
                double balance = Double.parseDouble(balanceStr);
                double equity = Double.parseDouble(equityStr);

                strategyProvHistoryAccount.setPnL(String.format("%.2f", pnl));
                strategyProvHistoryAccount.setBalance(String.format("%.2f", balance));
                strategyProvHistoryAccount.setEquity(String.format("%.2f", equity));
                isNumberCheck = true;
            }

            GlobalMethods.printDebugInfo("The positions info for strategyProvAccount History: " + accNum);
            printCopyTradingAccount(strategyProvHistoryAccount);
        }

        GlobalMethods.printDebugInfo("strategyProvHistoryAccount.getEquity:" + strategyProvHistoryAccount.getEquity() + " strategyProvAccount.getEquity():" + strategyProvAccount.getEquity()
                + " strategyProvHistoryAccount.getBalance():" + strategyProvHistoryAccount.getBalance() + "strategyProvAccount.getBalance(): " + strategyProvAccount.getBalance());

        GlobalMethods.printDebugInfo("isNumberCheck: " + isNumberCheck +
                " equityCheck: " + strategyProvHistoryAccount.getEquity().equalsIgnoreCase(strategyProvAccount.getEquity()) +
                " balanceCheck: " + strategyProvHistoryAccount.getBalance().equalsIgnoreCase(strategyProvAccount.getBalance()) );

        //compare the total position info in position tab with copier account
        return isNumberCheck &&
                strategyProvHistoryAccount.getEquity().equalsIgnoreCase(strategyProvAccount.getEquity()) &&
                strategyProvHistoryAccount.getBalance().equalsIgnoreCase(strategyProvAccount.getBalance());
    }

    public boolean ifHasPosition() {
        waitLoading();
        waitLoadingInCopierHome();
        List<WebElement> positionL = driver.findElements(this.positionList);

        return !positionL.isEmpty();

    }
    public void checkAndStopCopy() {
        waitLoading();
        waitLoadingInCopierHome();
        List<WebElement> positionL = driver.findElements(this.positionList);

        if(!positionL.isEmpty())
        {
            GlobalMethods.printDebugInfo("positionList is not empty, start to stop copy");
            waitLoading();
            WebElement manageButton = getManageBtnEle();
            manageButton.click();
            stopCopy();
            //wait for stop copy
            waitForCopyTrading(3000);
            getPositionsTabEle().click();
            waitLoadingInCopyTrading();

            List<WebElement> positionLC = driver.findElements(this.positionList);
            GlobalMethods.printDebugInfo("positionList size: " + positionLC.size());
            int count =0;
            while(!positionLC.isEmpty() && count < 6)
            {
                positionLC = driver.findElements(this.positionList);
                GlobalMethods.printDebugInfo("positionList is not empty, try again. " + " positionList size: " + positionLC);
                getPositionsTabEle().click();
                waitForCopyTrading(10000);
                waitLoading();
                count++;
            }
            Assert.assertTrue(positionLC.isEmpty(), "position has stopped");
        }

    }
    public void waitForElementsEmpty(List<WebElement> positionL){
        new WebDriverWait(driver, Duration.ofMinutes(1))
                .pollingEvery(Duration.ofSeconds(20))
                .until(drv -> positionL.isEmpty());
    }

    public void waitForElementsEmpty(By positionL){
        new WebDriverWait(driver, Duration.ofMinutes(1))
                .pollingEvery(Duration.ofSeconds(20))
                .until(drv -> driver.findElements(positionL).isEmpty());
    }

    public int positionSizeCheck() {
        waitLoadingInCopierHome();
        List<WebElement> positionList = driver.findElements(this.positionList);
        return positionList.size();
    }

    public void stopCopy(){
        waitLoading();
        waitLoadingInCopyTrading();

        try{
            LogUtils.info("Starting to check if Close Text is displayed...");
            int  closeTextSize = getCloseTextListEle().size();
            LogUtils.info("closeTextSize is: " + closeTextSize);

            if(closeTextSize>0)
            {
                LogUtils.info("Close text is displayed");
                getOkBtnEle().click();
            }
        }
        catch (Exception e)
        {
            LogUtils.info("The Copied position is being closed message not displayed." +  e.getMessage());
        }

        WebElement stopButton = getStopCopyBtnEle();
        js.executeScript("arguments[0].click();", stopButton);
        //stopButton.click();

        WebElement confirmBtn = getConfirmBtnEle();
        confirmBtn.click();

        WebElement closeText = getCloseTextDivEle();
        GlobalMethods.printDebugInfo("closeText: " + closeText.getText());

        WebElement okBtn = getOkBtnEle();
        okBtn.click();
        waitLoading();
        waitLoadingInCopierHome();
        //wait for the position list empty
        waitForCopyTrading(3000);

        List<WebElement> positionLC = driver.findElements(this.positionList);
        if(!positionLC.isEmpty())
        {
            getPositionsTabEle().click();
            waitForCopyTrading(3000);
        }
        Assert.assertTrue(positionLC.isEmpty(), "position has stopped");
        GlobalMethods.printDebugInfo("Stop Copy successfully");
    }

    //return false if there is no position in copier position
    public boolean checkPositionInCopier() {
        waitLoading();
        waitLoadingInCopyTrading();
        List<WebElement> positionList = driver.findElements(this.positionList);
        GlobalMethods.printDebugInfo("positionList size: " +positionList.size());
        return !positionList.isEmpty();
    }

    public static boolean isNumeric(String str) {
        if (str == null || str.trim().isEmpty())
            return false;
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    public boolean stopCopy(String signalProvAccount) {
        try {
            waitLoadingInCopierHome();
            waitForCopyTrading(3000);

            WebElement manageButton = getManageBtnEle();
            manageButton.click();

            waitLoadingInCopierHome();
            WebElement stopButton = getStopCopyBtnEle();
            stopButton.click();

            waitLoadingInCopierHome();
            WebElement confirmBtn = getConfirmBtnEle();
            confirmBtn.click();

            waitLoadingInCopierHome();
            WebElement closeText = getCloseTextDivEle();
            String msg = closeText.getText();
            GlobalMethods.printDebugInfo("closeText: " + msg);

            WebElement okBtn = getOkBtnEle();
            okBtn.click();


            boolean success = msg != null && msg.contains("The copied position is being closed, please check later");

            if (success) {
                GlobalMethods.printDebugInfo("Stop Copy successfully");
            } else {
                GlobalMethods.printDebugInfo("Stop Copy failed");
            }
            return success;

        } catch (Exception e) {
            GlobalMethods.printDebugInfo("Stop Copy failed, exception: " + e.getMessage());
            return false;
        }
    }


    public void openCopyAccount(){
        WebElement openCopyAccountButton = getOpenCopyAccountBtnEle();
        openCopyAccountButton.click();
    }
    public void printCopyTradingAccount(CopyTradingAccount acc) {
        System.out.println(" * Account Number: " + acc.accnum);
        System.out.println(" * Type: " + acc.type);
        System.out.println(" * Currency: " + acc.currency);
        System.out.println(" * equity: " + acc.equity);
        System.out.println(" * PNL: " + acc.pnL);
        System.out.println(" * Balance: " + acc.balance);
        System.out.println(" * Credit: " + acc.credit);
    }
    //copy trading has to take between app and web, sometimes have to wait
    public void waitForCopyTrading(int milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void waitLoadingInCopierHome(){

        try {
            fastwait.until((ExpectedCondition<Boolean>) d -> {
                List<WebElement> loaders = d.findElements(By.xpath("//div[@class='el-skeleton__item el-skeleton__text']"));
                return loaders.isEmpty();
            });

            fastwait.until((ExpectedCondition<Boolean>) d -> {
                try{
                    List<WebElement> loaders = d.findElements(By.xpath("//div[@class=loader]"));
                    return loaders.isEmpty();
                }catch(Exception e) {
                    return true;
                }
            });
        } catch (Exception e) {
        }
    }

    public void switchCopyMode(String cm) {
        if (cm == null || cm.isEmpty()) {
            LogUtils.info("Copy Mode string is empty, skipping switch.");
        }

        try {
            LogUtils.info("Switching to Copy Mode: " + cm);

            getCopyModeArrowEle().click();

            WebElement copyModeEle = findVisibleElemntByXpath("//span[contains(text(),'" + cm + "')]");

            js.executeScript("arguments[0].click()", copyModeEle);
            waitLoadingInCopyTrading();

        } catch (Exception e) {
            LogUtils.info("Exception occurred while switching Copy Mode to [" + cm + "]: " + e.getMessage());
        }
    }
    public boolean checkCopyMode(String cm) {
            getUpdateBtnEle().click();

            waitvisible.until(d -> getUpdateMsgEle().isDisplayed());
            String updateMsg = getUpdateMsgEle().getText();
            LogUtils.info("Update Message: " + updateMsg);

            if (StringUtils.containsIgnoreCase(updateMsg,"Update Successful")) {
                getOkBtnEle().click();
                LogUtils.info("Switching to Copy Mode: " + cm + " successfully");
                return true;
            } else {
                LogUtils.info("Update failed with message: " + updateMsg);
                return false;
            }
        }
}
