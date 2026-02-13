package newcrm.pages.moclientpages.copytrading;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.copyTrading.CopierPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class MOCopierPage extends CopierPage {
    public MOCopierPage(WebDriver driver) {
        super(driver);
    }
    protected List<WebElement> getCopierTitleInfoEle() {
        return assertVisibleElementsExists(By.xpath("//div[@class='data-box']/div[@class='common-data-bar']"), "Copier Title info");
    }

    public void getCopierAccTitleInfo() {
        List<WebElement> titleInfoList = getCopierTitleInfoEle();

        for (WebElement element : titleInfoList) {
            String titleInfo = element.getText();

            if (titleInfo == null || !titleInfo.contains("\n")) continue;

            String[] lines = titleInfo.split("\\n");
            if (lines.length < 2) continue;

            copierAccount.setEquity(lines[0]);
            copierAccount.setPnL(lines[2]);
            copierAccount.setBalance(lines[4]);
            copierAccount.setCredit(lines[6]);
        }
        printCopyTradingAccount(copierAccount);
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
}
