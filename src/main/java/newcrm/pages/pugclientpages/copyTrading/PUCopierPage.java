package newcrm.pages.pugclientpages.copyTrading;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.copyTrading.CopierPage;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;

public class PUCopierPage extends CopierPage {
    public PUCopierPage(WebDriver driver) {
        super(driver);
    }

    private By postionList = By.xpath("//div[@class='position']//div[contains(@class,'el-table__body')]");
    protected WebElement getHistoryListEle() {
        return assertVisibleElementExists(By.xpath("//div[@class='history_container']//div[contains(@class,'el-table__body-wrapper')]//tr[1]"), "History List");
    }
    protected WebElement getFirstPositionEle() {
        return assertVisibleElementExists(By.xpath("(//div[@class='position']//div[contains(@class,'el-table__body')])[1]"), "First position info");
    }
    protected WebElement getMoneyAllocatedEle() {
        return assertVisibleElementExists(By.xpath("//div[contains(normalize-space(.),'Money Allocated')]/ancestor::div[contains(@class,'label_row')][1]/following-sibling::*[1]//input[not(@type='hidden')][1]"), "money allocated");
        //return assertVisibleElementExists(By.xpath("//div[contains(normalize-space(.),'Money Allocated')]//div[@class='form-item-investment-container']//input"), "money allocated");
    }
    protected WebElement getMoneyRemovedEle() {
        return assertVisibleElementExists(By.xpath("//div[contains(normalize-space(.),'Money Removed')]/ancestor::div[contains(@class,'label_row')][1]/following-sibling::*[1]//input[not(@type='hidden')][1]"), "money removed");
       // return assertVisibleElementExists(By.xpath("//div[normalize-space(.)='Money Removed'] /following::div[contains(@class,'el-form-item')][1]//input"), "money removed");
    }
    public void checkAndStopCopy() {
        waitLoadingInCopierHome();
        waitForCopyTrading(5000);
        List<WebElement> positionList = driver.findElements(postionList);

        if (!positionList.isEmpty()) {
            waitLoading();
            WebElement manageButton = getManageBtnEle();
            manageButton.click();

            WebElement stopButton = getStopCopyBtnEle();
            stopButton.click();

            WebElement confirmBtn = getConfirmBtnEle();
            confirmBtn.click();

            WebElement closeText = getCloseTextDivEle();
            GlobalMethods.printDebugInfo("closeText: " + closeText.getText());

            WebElement okBtn = getOkBtnEle();
            okBtn.click();
            GlobalMethods.printDebugInfo("Stop Copy successfully");
        }
        waitLoadingInCopierHome();
        //waitForElementsEmpty(postionList);
    }
    //return false if there is no position in copier position
    @Override
    public boolean checkPositionInCopier() {
        waitLoading();
        List<WebElement> positionList = driver.findElements(postionList);
        GlobalMethods.printDebugInfo("positionList size: " +positionList.size());
        return !positionList.isEmpty();
    }
    @Override
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
    @Override
    public boolean checkStrategyAccPositionInfo(String strategyAcc) {
        List<WebElement> postionL = driver.findElements(postionList);

        boolean isNumberCheck = false;

        double totalPnL = 0.0;
        double totalBalance = 0.0;
        double totalEquity = 0.0;
        String accNum = null;

        //add each line of position together to get total
        for (WebElement element : postionL) {
            CopyTradingAccount strategyProvAccount = new CopyTradingAccount();
            String positionInfo = element.getText();
            System.out.println("signal provider position info: " + positionInfo);

            String[] info = positionInfo.split("\\n");

            if (info.length >= 7) {
                accNum = info[0];
                strategyProvAccount.setAccnum(accNum);
                String pnlStr = info[2].replaceAll("[^\\d.-]", "");
                String balanceStr = info[4].split("\\s")[0].replaceAll("[^\\d.-]", "");
                String equityStr = info[6].split("\\s")[0].replaceAll("[^\\d.-]", "");

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
                GlobalMethods.printDebugInfo("The positions info for: " + accNum);
                printCopyTradingAccount(strategyProvAccount);
            }
        }

        strategyProvAccount.setPnL(String.format("%.2f", totalPnL));
        strategyProvAccount.setBalance(String.format("%.2f", totalBalance));
        strategyProvAccount.setEquity(String.format("%.2f", totalEquity));

        GlobalMethods.printDebugInfo("The positions info for total: ");
        printCopyTradingAccount(strategyProvAccount);

        GlobalMethods.printDebugInfo("isNumberCheck: " + isNumberCheck +
                " equityCheck: " + strategyProvAccount.getEquity().equalsIgnoreCase(copierAccount.getEquity()) +
                " balanceCheck: " + strategyProvAccount.getBalance().equalsIgnoreCase(copierAccount.getBalance()) );

        //compare the total position info in position tab with copier account
        return isNumberCheck &&
                strategyProvAccount.getEquity().equalsIgnoreCase(copierAccount.getEquity()) &&
                strategyProvAccount.getBalance().equalsIgnoreCase(copierAccount.getBalance());
    }

    @Override
    public boolean checkStrategyAccHistoryInfo(String strategyAcc) {
        boolean isNumberCheck = false;
        String accNum = null;

        waitLoadingInCopierHome();
        //get the latest record in history
        CopyTradingAccount strategyProvHistoryAccount = new CopyTradingAccount();
        String positionInfo = getHistoryListEle().getText();
        System.out.println("signal provider position info: " + positionInfo);

        if (StringUtils.isBlank(positionInfo)) {
            GlobalMethods.printDebugInfo("positionInfo is null/blank");
            return false;
        }

        String[] info = positionInfo.split("\\n");

        if (info.length < 9) {
            GlobalMethods.printDebugInfo("positionInfo has insufficient fields: " + info.length);
            return false;
        }

        if (info.length >= 9) {
            accNum = info[2];
            strategyProvHistoryAccount.setAccnum(accNum);
            String pnlStr = info[3].replaceAll("[^\\d.-]", "");
            String balanceStr = info[5].split("\\s")[0].replaceAll("[^\\d.-]", "");
            String equityStr = info[7].split("\\s")[0].replaceAll("[^\\d.-]", "");

            if (isNumeric(pnlStr) && isNumeric(balanceStr) && isNumeric(equityStr)) {
                double pnl = Double.parseDouble(pnlStr);
                double balance = Double.parseDouble(balanceStr);
                double equity = Double.parseDouble(equityStr);

                strategyProvHistoryAccount.setPnL(String.format("%.2f", pnl));
                strategyProvHistoryAccount.setBalance(String.format("%.2f", balance));
                strategyProvHistoryAccount.setEquity(String.format("%.2f", equity));
                isNumberCheck = true;
            }

            GlobalMethods.printDebugInfo("The positions info for: " + accNum);
            printCopyTradingAccount(strategyProvHistoryAccount);
        }

        System.out.println("strategyProvHistoryAccount equity: " + strategyProvHistoryAccount.getEquity() + " copierAccount equity: "+ copierAccount.getEquity() + "strategyProvHistoryAccount balance:" + strategyProvHistoryAccount.getBalance()
                + " copierAccount balance: " + copierAccount.getBalance());

        GlobalMethods.printDebugInfo("isNumberCheck: " + isNumberCheck +
                " equityCheck: " + StringUtils.containsIgnoreCase(copierAccount.getEquity(),strategyProvHistoryAccount.getEquity()) +
                " balanceCheck: " + strategyProvHistoryAccount.getBalance().equalsIgnoreCase(copierAccount.getBalance()) );

        //compare the total position info in position tab with copier account
        return isNumberCheck &&
                strategyProvHistoryAccount.getEquity().equalsIgnoreCase(copierAccount.getEquity()) &&
                strategyProvHistoryAccount.getBalance().equalsIgnoreCase(copierAccount.getBalance());
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
            balanceInHistory = info[5].split("\\s")[0].replaceAll("[^\\d.-]", "");
            equityInHistory = info[7].split("\\s")[0].replaceAll("[^\\d.-]", "");

            GlobalMethods.printDebugInfo("balanceInHistory: " + balanceInHistory + " equityInHistory: " + equityInHistory);
        }

        return StringUtils.containsIgnoreCase(balanceInPosition, balanceInHistory) &&
                StringUtils.containsIgnoreCase(equityInPosition, equityInHistory);

    }

    @Override
    public CopyTradingAccount getPositionInfoInCoiper(){
        CopyTradingAccount strategyProvAccount = new CopyTradingAccount();
        waitLoadingInCopierHome();
        String positionInfo = getFirstPositionEle().getText();
        System.out.println("signal provider position info: " + positionInfo);
        String accNum = null;
        String[] info = positionInfo.split("\\n");

        if (info.length >= 13) {
            String strategyID = info[1];
            accNum = strategyID.split("\\s")[1];
            strategyProvAccount.setAccnum(accNum);
            String pnlStr = info[2].replaceAll("[^\\d.-]", "");
            String balanceStr = info[4].split("\\s")[0].replaceAll("[^\\d.-]", "");
            String equityStr = info[6].split("\\s")[0].replaceAll("[^\\d.-]", "");

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
    public boolean ifHasPosition() {
        waitLoading();
        waitLoadingInCopierHome();
        List<WebElement> positionL = driver.findElements(this.positionList);

        return !positionL.isEmpty();

    }
    public String getMoneyAllocatedInUpdateDialog()
    {
        waitLoading();
        WebElement moneyA = getMoneyAllocatedEle();
        /*moneyA.clear();
        moneyA.sendKeys("0.01");*/
        return (String) js.executeScript("return arguments[0].value;", moneyA);
    }
}
