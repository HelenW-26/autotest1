package newcrm.pages.vjpclientpages.copytrading;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.copyTrading.CopierPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class VJPCopierPage extends CopierPage {
    public VJPCopierPage(WebDriver driver) {
        super(driver);
    }
    public By positionList = By.xpath("//div[@class='position']//div[contains(@class,'el-table__body')]");
    protected WebElement getMoneyAllocatedEle() {
        return assertVisibleElementExists(By.xpath("//div[contains(normalize-space(.),'Money Allocated')]/ancestor::div[contains(@class,'label_row')][1]/following-sibling::*[1]//input[not(@type='hidden')][1]"), "money allocated");
        //return assertVisibleElementExists(By.xpath("//div[contains(normalize-space(.),'Money Allocated')]//div[@class='form-item-investment-container']//input"), "money allocated");
    }
    protected WebElement getMoneyRemovedEle() {
        return assertVisibleElementExists(By.xpath("//div[contains(normalize-space(.),'Money Removed')]/ancestor::div[contains(@class,'label_row')][1]/following-sibling::*[1]//input[not(@type='hidden')][1]"), "money removed");
        // return assertVisibleElementExists(By.xpath("//div[normalize-space(.)='Money Removed'] /following::div[contains(@class,'el-form-item')][1]//input"), "money removed");
    }

    //return false if there is no position in copier position
    @Override
    public boolean checkPositionInCopier() {
        waitLoading();
        List<WebElement> positionL = driver.findElements(positionList);
        GlobalMethods.printDebugInfo("positionList size: " +positionL.size());
        return !positionL.isEmpty();
    }

    public void checkAndStopCopy() {
        waitLoadingInCopierHome();
        waitForCopyTrading(5000);
        List<WebElement> positionL = driver.findElements(positionList);

        if (!positionL.isEmpty()) {
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
        waitLoadingInCopyTrading();
       // waitForElementsEmpty(positionL);
    }
    @Override
    public boolean checkStrategyAccPositionInfo(String strategyAcc) {
        //List<WebElement> postionList = driver.findElements(CopierPage.postionList);
        List<WebElement> postionL
                = driver.findElements(positionList);

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
    public String getMoneyAllocatedInUpdateDialog()
    {
        waitLoading();
        WebElement moneyA = getMoneyAllocatedEle();
        /*moneyA.clear();
        moneyA.sendKeys("0.01");*/
        return (String) js.executeScript("return arguments[0].value;", moneyA);
    }


}
