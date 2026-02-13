package newcrm.pages.starclientpages;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.ACC_STATUS;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import newcrm.pages.clientpages.LiveAccountsPage;

import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class STARLiveAccountsPage extends LiveAccountsPage {
	int size = 0;

	public STARLiveAccountsPage(WebDriver driver) {
		super(driver);
	}

    @Override
    protected WebElement getLeverageConfirmationDialogEle() {
        return checkElementExists(By.cssSelector("div#HomeDemo div.change_leverage_dialog div.el-dialog__wrapper:nth-of-type(2):not([style*='display: none'])"), "Leverage Confirmation Dialog");
    }

    @Override
    protected WebElement getLeverageConfirmationBtnEle() {
        return assertElementExists(By.cssSelector("div#HomeDemo div.change_leverage_dialog div.el-dialog__wrapper:nth-of-type(2) button[data-testid='confirm']"), "Confirm button");
    }

    @Override
    protected WebElement getAssetsCurrencyImgEle() {
        return assertElementExists(By.xpath("//div[@class='total_trading']//div[contains(@class, 'amount')]//img"), "Assets Currency Img");
    }

    @Override
    protected WebElement getAssetsAmountEle() {
        return assertElementExists(By.xpath("//div[@class='total_trading']//div[contains(@class, 'amount')]//div[@class='wrap']//span"), "Assets Amount");
    }

    @Override
    protected WebElement getAssetsCurrencyEle() {
        return assertElementExists(By.xpath("//div[contains(@class,'el-select-dropdown el-popper') and not(contains(@style, 'display: none'))]//li[contains(@class, 'selected')]"), "Assets Currency");
    }

    protected WebElement getAssetsCurrencyDropdownEle() {
        return assertElementExists(By.xpath("//div[@class='total_trading']//div[contains(@class, 'amount')]//i"), "Assets Currency List");
    }

    @Override
    protected WebElement getTradingAccountNoEle(WebElement ele) {
        return assertElementExists(By.cssSelector("div:nth-of-type(1) > span:nth-of-type(2)"), "Trading Account Number", ele);
    }

    @Override
    protected List<WebElement> getDemoAccountListEle() {
        return assertElementsExists(By.xpath("//div[@id='HomeDemo']//div[@class='account_card_box']/div/div"), "Demo Account List");
    }


    @Override
    protected List<LiveAccountsPage.Account> getAccounts(List<WebElement> trs) {
        ArrayList<Account> result = new ArrayList<>();

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
                String accTypeDesc = "", platformDesc = "", currencyDesc = "", statusDesc = "", item = "";
                int serverCnt = 0;
                GlobalProperties.ACCOUNTTYPE type = null;
                GlobalProperties.CURRENCY currency = null;
                GlobalProperties.PLATFORM platform = null;
                GlobalProperties.ACC_STATUS status = null;

                if(values[2].trim().equals("--.--")||values[3].trim().equals("--.--")) {//for new application
                    //find account type

                    statusDesc = values[0].trim();
                    platformDesc = values[1].trim();
                    accNum = values[2].trim();
                    equity = values[3].trim();

                    if (!values[3].trim().equals("--.--")) {
                        currencyDesc = values[4].trim();
                        item = values[5];
                    } else {
                        item = values[4];
                    }

                    // Find platform
                    platform = GlobalProperties.PLATFORM.getRecByPlatformDesc(platformDesc);
                    if (platform == null) {
                        System.out.println("Platform not found, Acc Number: " + accNum + ", Platform: " + platformDesc);
                    }

                    // Format: Leverage | Account Type | Server
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

                }else {
                    statusDesc = values[0].trim();
                    platformDesc = values[1].trim();
                    accNum = values[2].trim();
                    leverage = values[3];
                    accTypeDesc = values[4];

                    String serverPosVal = GlobalMethods.findValueByRegex(values[5], "([0-9,]+\\.\\d+)").map(groups -> groups.get(0)).orElse("");
                    if (!serverPosVal.isEmpty()) {
                        serverCnt = -1;
                    } else {
                        server = values[5];
                    }
                    equity = values[6 + serverCnt].trim();
                    currencyDesc = values[7 + serverCnt].trim();
                    credit = values[10 + serverCnt].trim();
                    balance = values[12 + serverCnt].trim();

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

                LiveAccountsPage.Account acc = new LiveAccountsPage.Account(accNum, server, type, currency, leverage, status, equity, balance, credit, platform, "");
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
            }else {
                String accNum = "", server = "", equity = "", leverage = "";
                String accTypeDesc = "", platformDesc = "", currencyDesc = "", statusDesc = "", item = "";
                GlobalProperties.ACCOUNTTYPE type = null;
                GlobalProperties.CURRENCY currency = null;
                GlobalProperties.PLATFORM platform = null;
                GlobalProperties.ACC_STATUS status = null;

                if(values[2].trim().equals("--.--") || values[3].trim().equals("--.--")) {//for new application

                    statusDesc = values[0].trim();
                    platformDesc = values[1].trim();
                    accNum = values[2].trim();
                    equity = values[3].trim();

                    if (!values[3].trim().equals("--.--")) {
                        currencyDesc = values[4].trim();
                        item = values[5];
                    } else {
                        item = values[4];
                    }

                    // Find platform
                    platform = GlobalProperties.PLATFORM.getRecByPlatformDesc(platformDesc);
                    if (platform == null) {
                        System.out.println("Platform not found, Acc Number: " + accNum + ", Platform: " + platformDesc);
                    }

                    // Format: Leverage | Account Type | Server
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
                    equity = values[3].trim();
                    currencyDesc = values[4].trim();

                    // Find platform
                    platform = GlobalProperties.PLATFORM.getRecByPlatformDesc(platformDesc);
                    if (platform == null) {
                        System.out.println("Platform not found, Acc Number: " + accNum + ", Platform: " + platformDesc);
                    }

                    // Format: Leverage | Account Type | Server
                    item = values[5];

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

    // 获取账号区域显示的信息
    @Override
    public String getAccountInfo() {
        String accountInfo = findVisibleElemntBy(By.xpath("//*[@id=\"LivePage\"]//div[contains(@class,'account_card_box')]")).getText();
        System.out.println(accountInfo);
        return accountInfo;
    }

    @Override
    public void clickAssetsCurrencyDropdown() {
        getAssetsCurrencyDropdownEle().click();
    }

}
