package newcrm.pages.pugclientpages.account;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.LiveAccountsPage;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PULiveAccountsPage extends LiveAccountsPage {

    public PULiveAccountsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected List<WebElement> getDemoAccountListEle() {
        return assertElementsExists(By.xpath("//div[@id='HomeDemo']//div[@class='account_card_box']/div/div"), "Demo Account List");
    }

    @Override
    protected WebElement getEditBalanceEle(int pos) {
        return assertClickableElementExists(By.xpath("(//*[@data-testid='resetBal'])" + "[" + pos + "]"), "Edit Balance icon");
    }

    @Override
    protected List<LiveAccountsPage.Account> getAccounts(List<WebElement> trs) {
        ArrayList<Account> result = new ArrayList<>();

        for (int i = 0; i < trs.size(); i++) {
            WebElement tr = trs.get(i);
            String info = tr.getText();
//            GlobalMethods.printDebugInfo("Info:\n" + info);

//            if (i != trs.size() - 1) {
//                GlobalMethods.printDebugInfo("********************************************");
//            }

            String values[] = info.split("\n");

            if(info.toLowerCase().contains("Documentary account".toLowerCase()) ||
                    info.toLowerCase().contains("Set up your account".toLowerCase()) ||
                    info.toLowerCase().contains("Copy. Profit. Repeat.".toLowerCase()) ||
                    values.length < 2) {
                continue;
            } else {
                String accNum = "", server = "", equity = "", balance = "", credit = "", leverage = "";
                String accTypeDesc = "", platformDesc = "", currencyDesc = "", statusDesc = "", nickname = "";
                int nicknameCnt = 0, serverCnt = 0;
                GlobalProperties.ACCOUNTTYPE type = null;
                GlobalProperties.CURRENCY currency = null;
                GlobalProperties.PLATFORM platform = null;
                GlobalProperties.ACC_STATUS status = null;

                if(values[2].trim().equals("--.--")||values[3].trim().equals("--.--")) {//for new application
                    //find account type
                    GlobalMethods.printDebugInfo("finding type from: " + values[0].trim());
                    for(GlobalProperties.ACCOUNTTYPE t: GlobalProperties.ACCOUNTTYPE.values()) {
                        if(t.getLiveAccountName().equalsIgnoreCase(values[6].trim())) {
                            type=t;
                            GlobalMethods.printDebugInfo("found type " +t);
                            break;
                        }
                    }
                    if (type == null) {
                        GlobalMethods.printDebugInfo("Didn't find type 1: " + values[0].trim());
                    }
                    //find currency
                    for(GlobalProperties.CURRENCY c: GlobalProperties.CURRENCY.values()) {
                        if(c.toString().equalsIgnoreCase(values[3].trim())) {
                            currency = c;
                            break;
                        }
                    }
                    if (currency == null) {
                        GlobalMethods.printDebugInfo("Didn't find currency: " + values[2].trim());
                    }
                    //find status
                    for(GlobalProperties.ACC_STATUS s: GlobalProperties.ACC_STATUS.values()) {
                        if(s.toString().equalsIgnoreCase(values[0].trim())) {
                            status = s;
                            break;
                        }
                    }
                } else {
                    statusDesc = values[0].trim();
                    platformDesc = values[1].trim();
                    accNum = values[2].trim();

//                    if (GlobalMethods.checkDuplicateByRegex(info, accNum)) nicknameCnt = 1;
                    String nicknamePosVal = GlobalMethods.findValueByRegex(values[3], "([0-9,]+\\.\\d+)").map(groups -> groups.get(0)).orElse("");
                    if (!nicknamePosVal.isEmpty()) {
                        nicknameCnt = -1;
                    } else {
                        nickname = values[3];
                    }

                    equity = values[4 + nicknameCnt].trim();
                    currencyDesc = values[5 + nicknameCnt].trim();
                    leverage = values[6 + nicknameCnt];
                    accTypeDesc = values[7 + nicknameCnt];

                    if (Objects.equals(values[8 + nicknameCnt], "Credits")) {
                        serverCnt = -1;
                    } else {
                        server = values[8 + nicknameCnt];
                    }
                    credit = values[10 + nicknameCnt + serverCnt].trim();
                    balance = values[12 + nicknameCnt + serverCnt].trim();

                    // Find platform
                    platform = GlobalProperties.PLATFORM.getRecByPlatformDesc(platformDesc);
                    if (platform == null) {
                        System.out.println("Platform not found, Acc Number: " + accNum + ", Platform: " + platformDesc);
                    }

                    // Find currency
                    currency = GlobalProperties.CURRENCY.getRecByCurrencyDesc(currencyDesc);
                    if (currency == null) {
                        System.out.println("Currency not found, Acc Number: " + accNum + ", Platform: " + currencyDesc);
                    }

                    // Find account type
                    type = GlobalProperties.ACCOUNTTYPE.getRecByAccTypeDesc(accTypeDesc, platform);
                    if (type == null) {
                        System.out.println("Account Type not found, Acc Number: " + accNum + ", Acc Type: " + accTypeDesc);
                    }

                    // Find status
                    status = GlobalProperties.ACC_STATUS.getRecByStatusDesc(statusDesc);
                    if (status == null) {
                        System.out.println("Status not found, Acc Number: " + accNum + ", Status: " + statusDesc);
                    }
                }

                LiveAccountsPage.Account acc = new LiveAccountsPage.Account(accNum, server, type, currency, leverage, status, equity, balance, credit, platform, nickname);
                result.add(acc);
            }
        }

        return result;
    }

    @Override
    protected List<LiveAccountsPage.Account> getAccounts_ListMode(List<WebElement> trs) {
        ArrayList<Account> result = new ArrayList<>();

        for (int i = 0; i < trs.size(); i++) {
            WebElement tr = trs.get(i);
            String info = tr.getText();
//            GlobalMethods.printDebugInfo("Info:\n" + info);

//            if (i != trs.size() - 1) {
//                GlobalMethods.printDebugInfo("********************************************");
//            }

            String values[] = info.split("\n");

            if(info.toLowerCase().contains("Documentary account".toLowerCase()) ||
                    info.toLowerCase().contains("Set up your account".toLowerCase()) ||
                    info.toLowerCase().contains("Copy. Profit. Repeat.".toLowerCase()) ||
                    values.length < 2) {
                continue;
            } else {
                String accNum = "", server = "", equity = "", balance = "", credit = "", leverage = "";
                String accTypeDesc = "", platformDesc = "", currencyDesc = "", statusDesc = "", nickname = "";
                int nicknameCnt = 0;
                GlobalProperties.ACCOUNTTYPE type = null;
                GlobalProperties.CURRENCY currency = null;
                GlobalProperties.PLATFORM platform = null;
                GlobalProperties.ACC_STATUS status = null;

                platformDesc = values[0].trim();
                accNum = values[1].trim();

//                if (GlobalMethods.checkDuplicateByRegex(info, accNum)) nicknameCnt = 1;
                String nicknamePosVal = GlobalMethods.findValueByRegex(values[2], "([0-9,]+\\.\\d+)").map(groups -> groups.get(0)).orElse("");
                if (!nicknamePosVal.isEmpty()) {
                    nicknameCnt = -1;
                } else {
                    nickname = values[2];
                }

                statusDesc = values[3 + nicknameCnt].trim();
                equity = values[4 + nicknameCnt].trim();
                currencyDesc = values[5 + nicknameCnt].trim();
                credit = values[7 + nicknameCnt].trim();
                balance = values[9 + nicknameCnt].trim();
                leverage = values[10 + nicknameCnt];
                accTypeDesc = values[11 + nicknameCnt];
                server = Objects.equals(values[12 + nicknameCnt], "Deposit") ? "" : values[12 + nicknameCnt];

                // Find platform
                platform = GlobalProperties.PLATFORM.getRecByPlatformDesc(platformDesc);
                if (platform == null) {
                    System.out.println("Platform not found, Acc Number: " + accNum + ", Platform: " + platformDesc);
                }

                // Find currency
                currency = GlobalProperties.CURRENCY.getRecByCurrencyDesc(currencyDesc);
                if (currency == null) {
                    System.out.println("Currency not found, Acc Number: " + accNum + ", Platform: " + currencyDesc);
                }

                // Find account type
                type = GlobalProperties.ACCOUNTTYPE.getRecByAccTypeDesc(accTypeDesc, platform);
                if (type == null) {
                    System.out.println("Account Type not found, Acc Number: " + accNum + ", Acc Type: " + accTypeDesc);
                }

                // Find status
                status = GlobalProperties.ACC_STATUS.getRecByStatusDesc(statusDesc);
                if (status == null) {
                    System.out.println("Status not found, Acc Number: " + accNum + ", Status: " + statusDesc);
                }

                LiveAccountsPage.Account acc = new LiveAccountsPage.Account(accNum, server, type, currency, leverage, status, equity, balance, credit, platform, nickname);
                result.add(acc);
            }
        }

        return result;
    }

    @Override
    protected List<LiveAccountsPage.Account> getDemoAccounts(List<WebElement> trs) {
        ArrayList<Account> result = new ArrayList<>();

        for (int i = 0; i < trs.size(); i++) {
            WebElement tr = trs.get(i);
            String info = tr.getText();
//            GlobalMethods.printDebugInfo("Info:\n" + info);

//            if (i != trs.size() - 1) {
//                GlobalMethods.printDebugInfo("********************************************");
//            }

            String values[] = info.split("\n");

            if(values.length < 2)
            {
                continue;
            }
            if(values[0].trim().equalsIgnoreCase("Documentary account"))
            {
                continue;
            } else {
                String accNum = "", server = "", equity = "", leverage = "";
                String accTypeDesc = "", platformDesc = "", currencyDesc = "", statusDesc = "";
                int leverageCnt = 0;
                GlobalProperties.ACCOUNTTYPE type = null;
                GlobalProperties.CURRENCY currency = null;
                GlobalProperties.PLATFORM platform = null;
                GlobalProperties.ACC_STATUS status = null;

                if(values[2].trim().equals("--.--")||values[3].trim().equals("--.--")) {//for new application
                    //find account type
                    GlobalMethods.printDebugInfo("finding type from: " + values[0].trim());
                    for(GlobalProperties.ACCOUNTTYPE t: GlobalProperties.ACCOUNTTYPE.values()) {
                        if(t.getLiveAccountName().equalsIgnoreCase(values[6].trim())) {
                            type=t;
                            GlobalMethods.printDebugInfo("found type " +t);
                            break;
                        }
                    }
                    if (type == null) {
                        GlobalMethods.printDebugInfo("Didn't find type 1: " + values[0].trim());
                    }
                    //find currency
                    for(GlobalProperties.CURRENCY c: GlobalProperties.CURRENCY.values()) {
                        if(c.toString().equalsIgnoreCase(values[3].trim())) {
                            currency = c;
                            break;
                        }
                    }
                    if (currency == null) {
                        GlobalMethods.printDebugInfo("Didn't find currency: " + values[2].trim());
                    }
                    //find status
                    for(GlobalProperties.ACC_STATUS s: GlobalProperties.ACC_STATUS.values()) {
                        if(s.toString().equalsIgnoreCase(values[0].trim())) {
                            status = s;
                            break;
                        }
                    }
                } else {
                    statusDesc = values[0].trim();
                    platformDesc = values[1].trim();
                    accNum = values[2].trim();
                    equity = values[3].trim();
                    currencyDesc = values[4].trim();

                    leverage = GlobalMethods.findValueByRegex(values[5], "\\d+\\s*:\\s*1").map(groups -> groups.get(0)).orElse("");
                    if (leverage.isEmpty()) leverageCnt = -1;

                    accTypeDesc = values[6 + leverageCnt];
                    if (!Objects.equals(values[7 + leverageCnt], "Trade")) {
                        server = values[7 + leverageCnt];
                    }

                    // Find platform
                    platform = GlobalProperties.PLATFORM.getRecByPlatformDesc(platformDesc);
                    if (platform == null) {
                        System.out.println("Platform not found, Acc Number: " + accNum + ", Platform: " + platformDesc);
                    }

                    // Find currency
                    currency = GlobalProperties.CURRENCY.getRecByCurrencyDesc(currencyDesc);
                    if (currency == null) {
                        System.out.println("Currency not found, Acc Number: " + accNum + ", Platform: " + currencyDesc);
                    }

                    // Find account type
                    type = GlobalProperties.ACCOUNTTYPE.getRecByAccTypeDesc(accTypeDesc, platform);
                    if (type == null) {
                        System.out.println("Account Type not found, Acc Number: " + accNum + ", Acc Type: " + accTypeDesc);
                    }

                    // Find status
                    status = GlobalProperties.ACC_STATUS.getRecByStatusDesc(statusDesc);
                    if (status == null) {
                        System.out.println("Status not found, Acc Number: " + accNum + ", Status: " + statusDesc);
                    }
                }

                LiveAccountsPage.Account acc = new LiveAccountsPage.Account(accNum, server, type, currency, leverage, status, equity, "", "", platform, "");
                result.add(acc);
            }
        }

        return result;
    }

    @Override
    public List<Account> getFirstPageMTSAccountsWithBalance(GlobalProperties.PLATFORM platform){
        waitLoading();
        String xpath = "//div[@class='account_card_box']/div/div[not(.//div[contains(@class, 'copyTrading_box')])]";

        List<WebElement> divs = null;
        try {
            divs = driver.findElements(By.xpath(xpath));
        }catch(Exception e) {

            GlobalMethods.printDebugInfo("LiveAccountsPage: * ERROR: Some Error appeared at live accounts page!");
        }

        if(divs == null || divs.size() == 0)
        {
            GlobalMethods.printDebugInfo("LiveAccountsPage: Have not found any " + platform.toString() +"  account info");
        }

        return getMTSAccountsWithbalance(divs);
    }
    @Override
    protected List<LiveAccountsPage.Account> getMTSAccountsWithbalance(List<WebElement> trs){
        ArrayList<Account> result = new ArrayList<>();
        for(WebElement tr:trs) {
            String info = tr.getText();
            GlobalMethods.printDebugInfo("info:" + info);
            String values[] = info.split("\n");
            int leh = values.length;

            for(int i = 0; i<leh;i++)
            {
                String v= values[i];
                System.out.println(v);
            }
            if(values.length < 2)
            {
                continue;
            }
            if(values[0].trim().equalsIgnoreCase("Documentary account"))
            {
                continue;
            }
            else {
                String accnum = "";
                String server = "";
                String equity = "";
                String balance = "";
                String credit = "";
                String leve="";
                String acctype="";
                GlobalProperties.ACCOUNTTYPE type = null;
                GlobalProperties.CURRENCY currency = null;
                String leverage = "";
                GlobalProperties.ACC_STATUS status = null;
                GlobalProperties.PLATFORM platform = null;
                System.out.println("values[2].trim()" + values[2].trim());

                if(values[2].trim().equals("--.--")||values[3].trim().equals("--.--")) {//for new application
                    //find account type
                    GlobalMethods.printDebugInfo("finding type from: " + values[0].trim());
                    for(GlobalProperties.ACCOUNTTYPE t: GlobalProperties.ACCOUNTTYPE.values()) {
                        if(t.getLiveAccountName().equalsIgnoreCase(values[6].trim())) {
                            type=t;
                            GlobalMethods.printDebugInfo("found type " +t);
                            break;
                        }
                    }
                    if (type == null) {
                        GlobalMethods.printDebugInfo("Didn't find type 1: " + values[0].trim());
                    }
                    //find currency
                    for(GlobalProperties.CURRENCY c: GlobalProperties.CURRENCY.values()) {
                        if(c.toString().equalsIgnoreCase(values[3].trim())) {
                            currency = c;
                            break;
                        }
                    }
                    if (currency == null) {
                        GlobalMethods.printDebugInfo("Didn't find currency: " + values[2].trim());
                    }
                    //find status
                    for(GlobalProperties.ACC_STATUS s: GlobalProperties.ACC_STATUS.values()) {
                        if(s.toString().equalsIgnoreCase(values[0].trim())) {
                            status = s;
                            break;
                        }
                    }
                }else {
                    accnum = values[2].trim();
                    // server = values[9].trim();
                    equity = values[3].trim();
                    credit = values[8].trim();
                    balance = values[11].trim();
                    leve = values[5];
                    acctype = values[6];
                    server = values[7];

                    if(StringUtils.containsIgnoreCase(balance,"Balance") || StringUtils.containsIgnoreCase(balance,"Deposit")){
                        balance = values[10].trim();
                        leve = values[5];
                        acctype = values[1];
                        server = values[6];
                    }

                    GlobalMethods.printDebugInfo("leverage: " + leve);
                    GlobalMethods.printDebugInfo("acctype: " + acctype);
                    GlobalMethods.printDebugInfo("server: " + server);
                    //find account type
                    for(GlobalProperties.ACCOUNTTYPE t: GlobalProperties.ACCOUNTTYPE.values()) {
                        if(t.getLiveAccountName().equalsIgnoreCase(acctype)) {
                            type=t;
                            break;
                        }
                    }
                    if (type == null) {
                        GlobalMethods.printDebugInfo("Didn't find type 2: " + values[9].trim());
                    }
                    //find currency
                    for(GlobalProperties.CURRENCY c: GlobalProperties.CURRENCY.values()) {
                        if(c.toString().equalsIgnoreCase(values[5].trim())) {
                            currency = c;
                            break;
                        }
                    }
                    //find status
                    for(GlobalProperties.ACC_STATUS s: GlobalProperties.ACC_STATUS.values()) {
                        if(s.toString().equalsIgnoreCase(values[0].trim())) {
                            status = s;
                            break;
                        }
                    }

                    // leverage = values[7];
                    leverage = leve.trim();
                }

                LiveAccountsPage.Account acc = new LiveAccountsPage.Account(accnum,server,type,currency,leverage,status,equity,balance,credit, platform,"");
                result.add(acc);
            }
        }

        return result;
    }

}
