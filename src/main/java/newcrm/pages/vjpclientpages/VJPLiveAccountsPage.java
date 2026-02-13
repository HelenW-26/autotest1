package newcrm.pages.vjpclientpages;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import newcrm.pages.clientpages.LiveAccountsPage;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class VJPLiveAccountsPage extends LiveAccountsPage {
    int size = 0;

    public VJPLiveAccountsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected List<WebElement> getAccountListEle() {
        return assertElementsExists(By.cssSelector("div#LivePage div.account_card_box > div > div:not(:has(div.copyTrading_box)), div#LivePage div.account_list_box div.el-card > div.el-card__body"), "Live Account List");
    }

    @Override
    protected List<WebElement> getDemoAccountListEle() {
        return assertElementsExists(By.xpath("//div[@id='HomeDemo']//div[@class='account_card_box']/div/div"), "Demo Account List");
    }

    @Override
    protected List<Account> getAccounts(List<WebElement> trs) {
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

                if(values[1].trim().equals("-") || values[4].trim().equals("--")) {

                    statusDesc = values[0].trim();
                    platformDesc = values[1].trim();
                    accNum = values[2].trim();

//                    if (GlobalMethods.checkDuplicateByRegex(info, accNum)) nicknameCnt = 1;
                    nickname = GlobalMethods.findValueByRegex(values[3], ".*\\(.*\\).*").map(groups -> groups.get(0)).orElse("");
                    if (nickname.isEmpty()) nicknameCnt = -1;

                    // Find platform
                    platform = GlobalProperties.PLATFORM.getRecByPlatformDesc(platformDesc);
                    if (platform == null) {
                        System.out.println("Platform not found, Acc Number: " + accNum + ", Platform: " + platformDesc);
                    }

                    // Format: Leverage | Account Type | Server
                    String item = values[4 + nicknameCnt];

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

                    equity = values[5 + nicknameCnt].trim();
                    if (!equity.equalsIgnoreCase("--")) {
                        Optional<List<String>> strCurr = GlobalMethods.findValueByRegex(equity, "([\\d,]+\\.\\d+)\\s*([A-Z]+)");
                        if (strCurr.isPresent()) {
                            equity = strCurr.get().get(0);
                            currencyDesc = strCurr.get().get(1);
                        }
                    }

                    credit = GlobalMethods.findValueByRegex(values[6 + nicknameCnt], "Credits\\s*:\\s*(.+)").map(groups -> groups.get(0)).orElse("");
                    balance = GlobalMethods.findValueByRegex(values[7 + nicknameCnt], "Balance\\s*:\\s*(.+)").map(groups -> groups.get(0)).orElse("");

                    // Find currency
                    if (!currencyDesc.isEmpty()) {
                        currency = GlobalProperties.CURRENCY.getRecByCurrencyDesc(currencyDesc);
                        if (currency == null) {
                            System.out.println("Currency not found, Acc Number: " + accNum + ", Currency: " + currencyDesc);
                        }
                    }

                    // Find status
                    status = GlobalProperties.ACC_STATUS.getRecByStatusDesc(statusDesc);
                    if (status == null) {
                        System.out.println("Status not found, Acc Number: " + accNum + ", Status: " + statusDesc);
                    }

                } else {
                    statusDesc = values[0].trim();
                    platformDesc = values[1].trim();
                    accNum = values[2].trim();

//                    if (GlobalMethods.checkDuplicateByRegex(info, accNum)) nicknameCnt = 1;
                    String nicknamePosVal = GlobalMethods.findValueByRegex(values[3], ".*\\(.*\\).*").map(groups -> groups.get(0)).orElse("");

                    if (nicknamePosVal.isEmpty()) {
                        nicknameCnt = -1;
                    } else {
                        nickname = values[3];
                    }

                    equity = values[5 + nicknameCnt].trim();
                    currencyDesc = values[6 + nicknameCnt].trim();
                    credit = GlobalMethods.findValueByRegex(values[7 + nicknameCnt], "Credits\\s*:\\s*([0-9,]+\\.\\d+)").map(groups -> groups.get(0)).orElse("");
                    balance = GlobalMethods.findValueByRegex(values[8 + nicknameCnt], "Balance\\s*:\\s*([0-9,]+\\.\\d+)").map(groups -> groups.get(0)).orElse("");

                    // Find platform
                    platform = GlobalProperties.PLATFORM.getRecByPlatformDesc(platformDesc);
                    if (platform == null) {
                        System.out.println("Platform not found, Acc Number: " + accNum + ", Platform: " + platformDesc);
                    }

                    // Format: Leverage | Account Type | Server
                    String item = values[4 + nicknameCnt];

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

                LiveAccountsPage.Account acc = new LiveAccountsPage.Account(accNum, server, type, currency, leverage, status, equity, balance, credit, platform, nickname);
                result.add(acc);
            }
        }

        return result;
    }

    @Override
    protected List<Account> getAccounts_ListMode(List<WebElement> trs) {
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

                statusDesc = values[0].trim();
                platformDesc = values[1].trim();
                accNum = values[2].trim();

//                if (GlobalMethods.checkDuplicateByRegex(info, accNum)) nicknameCnt = 1;
                String nicknamePosVal = GlobalMethods.findValueByRegex(values[3], ".*\\(.*\\).*").map(groups -> groups.get(0)).orElse("");

                if (nicknamePosVal.isEmpty()) {
                    nicknameCnt = -1;
                } else {
                    nickname = values[3];
                }

                equity = values[4 + nicknameCnt].trim();
                currencyDesc = values[5 + nicknameCnt].trim();
                credit = GlobalMethods.findValueByRegex(values[6 + nicknameCnt], "Credits\\s*:\\s*([0-9,]+\\.\\d+)").map(groups -> groups.get(0)).orElse("");
                balance = GlobalMethods.findValueByRegex(values[7 + nicknameCnt], "Balance\\s*:\\s*([0-9,]+\\.\\d+)").map(groups -> groups.get(0)).orElse("");

                // Find platform
                platform = GlobalProperties.PLATFORM.getRecByPlatformDesc(platformDesc);
                if (platform == null) {
                    System.out.println("Platform not found, Acc Number: " + accNum + ", Platform: " + platformDesc);
                }

                // Format: Leverage | Account Type | Server
                String item = values[8 + nicknameCnt];

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

                LiveAccountsPage.Account acc = new LiveAccountsPage.Account(accNum, server, type, currency, leverage, status, equity, balance, credit, platform, nickname);
                result.add(acc);
            }
        }

        return result;
    }

    @Override
    protected List<Account> getDemoAccounts(List<WebElement> trs) {
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
                GlobalProperties.ACCOUNTTYPE type = null;
                GlobalProperties.CURRENCY currency = null;
                GlobalProperties.PLATFORM platform = null;
                GlobalProperties.ACC_STATUS status = null;

                if(values[1].trim().equals("-") || values[4].trim().equals("--")) {

                    statusDesc = values[0].trim();
                    platformDesc = values[1].trim();
                    accNum = values[2].trim();

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

                    equity = values[4].trim();
                    if (!equity.equalsIgnoreCase("--")) {
                        Optional<List<String>> strCurr = GlobalMethods.findValueByRegex(equity, "([\\d,]+\\.\\d+)\\s*([A-Z]+)");
                        if (strCurr.isPresent()) {
                            equity = strCurr.get().get(0);
                            currencyDesc = strCurr.get().get(1);
                        }
                    }

                    // Find currency
                    if (!currencyDesc.isEmpty()) {
                        currency = GlobalProperties.CURRENCY.getRecByCurrencyDesc(currencyDesc);
                        if (currency == null) {
                            System.out.println("Currency not found, Acc Number: " + accNum + ", Currency: " + currencyDesc);
                        }
                    }

                    // Find status
                    status = GlobalProperties.ACC_STATUS.getRecByStatusDesc(statusDesc);
                    if (status == null) {
                        System.out.println("Status not found, Acc Number: " + accNum + ", Status: " + statusDesc);
                    }

                } else {
                    statusDesc = values[0].trim();
                    platformDesc = values[1].trim();
                    accNum = values[2].trim();

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

                    equity = values[4].trim();
                    if (!"--".equals(equity)) currencyDesc = values[5].trim();

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
