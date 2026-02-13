package newcrm.pages.moclientpages;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.LiveAccountsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class MOLiveAccountsPage extends LiveAccountsPage{

    public MOLiveAccountsPage(WebDriver driver)
    {
        super(driver);
    }

    @Override
    protected List<WebElement> getAccountListEle() {
        return assertVisibleElementsExists(By.cssSelector("div#LivePage div.account_card_box > div > div:not(:has(div.copyTrading_box)), div#LivePage div.account_list_box table.el-table__body tr"), "Live Account List");
    }

    @Override
    protected List<WebElement> getDemoAccountListEle() {
        return assertVisibleElementsExists(By.xpath("//div[@id='HomeDemo']//div[@class='account_card_box']/div/div"), "Demo Account List");
    }

    @Override
    protected List<LiveAccountsPage.Account> getAccounts(List<WebElement> trs) {
        ArrayList<LiveAccountsPage.Account> result = new ArrayList<>();

        for (int i = 0; i < trs.size(); i++) {
            WebElement tr = trs.get(i);
            String info = tr.getText();
//            GlobalMethods.printDebugInfo("Info:\n" + info);
//
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
                String accTypeDesc = "", platformDesc = "", currencyDesc = "", statusDesc = "";
                GlobalProperties.ACCOUNTTYPE type = null;
                GlobalProperties.CURRENCY currency = null;
                GlobalProperties.PLATFORM platform = null;
                GlobalProperties.ACC_STATUS status = null;

                if(values[1].trim().equals("-")) {//for new application
                    //find account type
                    GlobalMethods.printDebugInfo("finding type from: " + values[2].trim());
                    for(GlobalProperties.ACCOUNTTYPE t: GlobalProperties.ACCOUNTTYPE.values()) {
                        if(t.getLiveAccountName().equalsIgnoreCase(values[2].trim())) {
                            type=t;
                            GlobalMethods.printDebugInfo("found type " +t);
                            break;
                        }
                    }
                    if (type == null) {
                        GlobalMethods.printDebugInfo("Didn't find type 1: " + values[2].trim());
                    }
                    //find currency
                    for(GlobalProperties.CURRENCY c: GlobalProperties.CURRENCY.values()) {
                        if(c.toString().equalsIgnoreCase(values[3].trim())) {
                            currency = c;
                            break;
                        }
                    }
                    if (currency == null) {
                        GlobalMethods.printDebugInfo("Didn't find currency: " + values[3].trim());
                    }
                    //find status
                    for(GlobalProperties.ACC_STATUS s: GlobalProperties.ACC_STATUS.values()) {
                        if(s.toString().equalsIgnoreCase(values[6].trim())) {
                            status = s;
                            break;
                        }
                    }
                } else {
                    statusDesc = values[0].trim();
                    platformDesc = values[1].trim();
                    accNum = values[2].trim();
                    equity = values[4].trim();
                    currencyDesc = values[5].trim();
                    credit = GlobalMethods.findValueByRegex(values[6], "Credits\\s*:\\s*([0-9,]+\\.\\d+)").map(groups -> groups.get(0)).orElse("");
                    balance = GlobalMethods.findValueByRegex(values[7], "Balance\\s*:\\s*([0-9,]+\\.\\d+)").map(groups -> groups.get(0)).orElse("");

                    // Find platform
                    platform = GlobalProperties.PLATFORM.getRecByPlatformDesc(platformDesc);
                    if (platform == null) {
                        System.out.println("Platform not found, Acc Number: " + accNum + ", Platform: " + platformDesc);
                    }

                    // Format: Leverage | Account Type | Server
                    String item = values[3];

                    if (item != null && !item.isEmpty()) {
                        // Find leverage
                        leverage = GlobalMethods.findValueByRegex(item, "\\d+\\s*:\\s*1").map(groups -> groups.get(0)).orElse("");
                        if (!leverage.isEmpty()) item = item.replace(leverage,"");

                        // Find account type
                        type = GlobalProperties.ACCOUNTTYPE.getRecByAccTypeDesc(item, platform);
                        if (type == null) {
                            System.out.println("Account Type not found, Acc Number: " + accNum + ", Acc Type: " + accTypeDesc);
                        } else {
                            // Find server
                            server = GlobalMethods.replaceAllIgnoreCase(item, type.getLiveAccountName(), "").trim();
                        }
                    } else {
                        System.out.println("Account Type not found, Acc Number: " + accNum + ", Acc Type: " + item);
                    }

                    // Find currency
                    currency = GlobalProperties.CURRENCY.getRecByCurrencyDesc(currencyDesc);
                    if (currency == null) {
                        System.out.println("Currency not found, Acc Number: " + accNum + ", Platform: " + currencyDesc);
                    }

                    // Find status
                    status = GlobalProperties.ACC_STATUS.getRecByStatusDesc(statusDesc);
                    if (status == null) {
                        System.out.println("Status not found, Acc Number: " + accNum + ", Status: " + statusDesc);
                    }
                }

                LiveAccountsPage.Account acc = new LiveAccountsPage.Account(accNum, server, type, currency, leverage, status, equity, balance, credit, platform, "");
                result.add(acc);
            }
        }

        return result;
    }

    @Override
    protected List<LiveAccountsPage.Account> getAccounts_ListMode(List<WebElement> trs) {
        ArrayList<LiveAccountsPage.Account> result = new ArrayList<>();

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
                String accTypeDesc = "", platformDesc = "", currencyDesc = "", statusDesc = "";
                GlobalProperties.ACCOUNTTYPE type = null;
                GlobalProperties.CURRENCY currency = null;
                GlobalProperties.PLATFORM platform = null;
                GlobalProperties.ACC_STATUS status = null;

                statusDesc = values[5].trim();

                platformDesc = values[0].trim();
                accNum = values[0].trim();

                // Find platform
                platform = GlobalProperties.PLATFORM.getRecByPlatformDesc(platformDesc);
                if (platform == null) {
                    System.out.println("Platform not found, Acc Number: " + accNum + ", Platform: " + platformDesc);
                } else {
                    Pattern pattern = Pattern.compile(platform.toString(), Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(values[0].trim());

                    if (matcher.find()) {
                        platformDesc = matcher.group();
                    }

                    accNum = values[0].trim().replace(platformDesc,"");
                }

                equity = values[1].trim();
                if (!equity.equalsIgnoreCase("-")) {
                    Optional<List<String>> strCurr = GlobalMethods.findValueByRegex(equity, "([\\d,]+\\.\\d+)\\s*([A-Z]+)");
                    if (strCurr.isPresent()) {
                        equity = strCurr.get().get(0);
                        currencyDesc = strCurr.get().get(1);
                    }
                }

                credit = values[2].trim();
                balance = values[3].trim();

                // Format: Leverage | Account Type | Server
                String item = values[4];

                if (item != null && !item.isEmpty()) {
                    // Find leverage
                    leverage = GlobalMethods.findValueByRegex(item, "\\d+\\s*:\\s*1").map(groups -> groups.get(0)).orElse("");
                    if (!leverage.isEmpty()) item = item.replace(leverage,"");

                    // Find account type
                    type = GlobalProperties.ACCOUNTTYPE.getRecByAccTypeDesc(item, platform);
                    if (type == null) {
                        System.out.println("Account Type not found, Acc Number: " + accNum + ", Acc Type: " + accTypeDesc);
                    } else {
                        // Find server
                        server = GlobalMethods.replaceAllIgnoreCase(item, type.getLiveAccountName(), "").trim();
                    }
                } else {
                    System.out.println("Account Type not found, Acc Number: " + accNum + ", Acc Type: " + item);
                }

                // Find currency
                currency = GlobalProperties.CURRENCY.getRecByCurrencyDesc(currencyDesc);
                if (currency == null) {
                    System.out.println("Currency not found, Acc Number: " + accNum + ", Platform: " + currencyDesc);
                }

                // Find status
                status = GlobalProperties.ACC_STATUS.getRecByStatusDesc(statusDesc);
                if (status == null) {
                    System.out.println("Status not found, Acc Number: " + accNum + ", Status: " + statusDesc);
                }

                LiveAccountsPage.Account acc = new LiveAccountsPage.Account(accNum, server, type, currency, leverage, status, equity, balance, credit, platform, "");
                result.add(acc);
            }
        }

        return result;
    }

    @Override
    protected List<LiveAccountsPage.Account> getDemoAccounts(List<WebElement> trs){
        ArrayList<LiveAccountsPage.Account> result = new ArrayList<>();

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
            if(values[0].trim().equalsIgnoreCase("Documentary account") || values[0].trim().equalsIgnoreCase("Copy. Profit. Repeat."))
            {
                continue;
            } else {
                String accNum = "", server = "", equity = "", leverage = "";
                String accTypeDesc = "", platformDesc = "", currencyDesc = "", statusDesc = "";
                GlobalProperties.ACCOUNTTYPE type = null;
                GlobalProperties.CURRENCY currency = null;
                GlobalProperties.PLATFORM platform = null;
                GlobalProperties.ACC_STATUS status = null;

                if(values[1].trim().equals("-")) {//for new application
                    //find account type
                    GlobalMethods.printDebugInfo("finding type from: " + values[2].trim());
                    for(GlobalProperties.ACCOUNTTYPE t: GlobalProperties.ACCOUNTTYPE.values()) {
                        if(t.getLiveAccountName().equalsIgnoreCase(values[2].trim())) {
                            type=t;
                            GlobalMethods.printDebugInfo("found type " +t);
                            break;
                        }
                    }
                    if (type == null) {
                        GlobalMethods.printDebugInfo("Didn't find type 1: " + values[2].trim());
                    }
                    //find currency
                    for(GlobalProperties.CURRENCY c: GlobalProperties.CURRENCY.values()) {
                        if(c.toString().equalsIgnoreCase(values[3].trim())) {
                            currency = c;
                            break;
                        }
                    }
                    if (currency == null) {
                        GlobalMethods.printDebugInfo("Didn't find currency: " + values[3].trim());
                    }
                    //find status
                    for(GlobalProperties.ACC_STATUS s: GlobalProperties.ACC_STATUS.values()) {
                        if(s.toString().equalsIgnoreCase(values[6].trim())) {
                            status = s;
                            break;
                        }
                    }
                } else {
                    statusDesc = values[0].trim();
                    platformDesc = values[1].trim();
                    accNum = values[2].trim();
                    equity = values[4].trim();
                    currencyDesc = values[5].trim();

                    // Find platform
                    platform = GlobalProperties.PLATFORM.getRecByPlatformDesc(platformDesc);
                    if (platform == null) {
                        System.out.println("Platform not found, Acc Number: " + accNum + ", Platform: " + platformDesc);
                    }

                    // Format: Leverage | Account Type | Server
                    String item = values[3];

                    if (item != null && !item.isEmpty()) {
                        // Find leverage
                        leverage = GlobalMethods.findValueByRegex(item, "\\d+\\s*:\\s*1").map(groups -> groups.get(0)).orElse("");
                        if (!leverage.isEmpty()) item = item.replace(leverage,"");

                        // Find account type
                        type = GlobalProperties.ACCOUNTTYPE.getRecByAccTypeDesc(item, platform);
                        if (type == null) {
                            System.out.println("Account Type not found, Acc Number: " + accNum + ", Acc Type: " + accTypeDesc);
                        } else {
                            // Find server
                            server = GlobalMethods.replaceAllIgnoreCase(item, type.getLiveAccountName(), "").trim();
                        }
                    } else {
                        System.out.println("Account Type not found, Acc Number: " + accNum + ", Acc Type: " + item);
                    }

                    // Find currency
                    currency = GlobalProperties.CURRENCY.getRecByCurrencyDesc(currencyDesc);
                    if (currency == null) {
                        System.out.println("Currency not found, Acc Number: " + accNum + ", Platform: " + currencyDesc);
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

}
