package newcrm.pages.vtclientpages;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.ACC_STATUS;
import newcrm.pages.clientpages.LiveAccountsPage;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VTLiveAccountsPage extends LiveAccountsPage {

    public VTLiveAccountsPage(WebDriver driver)
    {
        super(driver);
    }

    @Override
    protected WebElement getAccountChgPwdSubmitBtnEle() {
        return assertElementExists(By.cssSelector("div.el-dialog__wrapper:not([style*='display: none']) button[data-testid='changePw']"), "Change Password button");
    }

    @Override
    public void closeDialog() {
        WebElement e = getDialogCloseBtnEle();
        if (e == null) {
            Assert.fail("Dialog Close button");
        }
        triggerClickEvent_withoutMoveElement(e);
        GlobalMethods.printDebugInfo("Close Dialog");
    }

    @Override
    protected WebElement getProfileAvatarEle() {
        return assertElementExists(By.cssSelector("div[data-testid='dropdown'] > div > img"), "Profile Avatar");
    }

    @Override
    protected WebElement getProfileUserIdLabelEle() {
        return assertElementExists(By.cssSelector("ul.login_dropdown_box div.dropdown_username.cursor-pointer"), "User ID");
    }

    @Override
    protected WebElement getLeverageConfirmationDialogEle() {
        return checkElementExists(By.cssSelector("div.change_leverage_dialog div.common_dialog_wrapper:nth-of-type(2) > div.el-dialog__wrapper:not([style*='display: none'])"), "Leverage Confirmation Dialog");
    }

    @Override
    protected WebElement getLeverageConfirmationBtnEle() {
        return assertElementExists(By.cssSelector("div.change_leverage_dialog div.common_dialog_wrapper:nth-of-type(2) button[data-testid='confirm']"), "Confirm button");
    }

    @Override
    protected WebElement getDialogCloseBtnEle() {
        return checkElementExists(By.cssSelector("div.el-dialog__wrapper:not([style*='display: none']) img.closeImg"));
    }

    @Override
    protected WebElement getChgAccountPwdDialogCloseBtnEle() {
        return assertElementExists(By.cssSelector("div.el-dialog__wrapper:not([style*='display: none']) img.closeImg"), "Change Password Dialog Close button");
    }

    @Override
    protected WebElement getDemoChangeLeverageBtnEle(PLATFORM platform) {
        return assertElementExists(By.cssSelector("div.el-popover:not([style*='display: none']) button span.more_btn_text"), "Change Leverage button", e -> e.getText().trim().toLowerCase().contains("change leverage".toLowerCase()));
    }

    @Override
    public String getProfileUserId() {
        WebElement e = getProfileUserIdLabelEle();
        String userId = e.getText().trim();
        GlobalMethods.printDebugInfo("Profile User ID: " + userId);

        return userId;
    }

    @Override
    public void clickProfileAvatar() {
        waitLoading();
        WebElement e = getProfileAvatarEle();
        WebElement profileDdlEle = assertElementExists(By.cssSelector("ul.login_dropdown_box"), "Profile Avatar Dropdown List");

        String style = profileDdlEle.getAttribute("style");
        if (style != null && style.contains("display: none")) {
            e.click();
            waitLoading();
            GlobalMethods.printDebugInfo("Open Profile Avatar");
        }
    }

    @Override
    public void closeProfileAvatar() {
        WebElement e = getProfileAvatarEle();
        WebElement profileDdlEle = assertElementExists(By.cssSelector("ul.login_dropdown_box"), "Profile Avatar Dropdown List");

        String style = profileDdlEle.getAttribute("style");
        if (style == null || !style.contains("display: none")) {
            e.click();
            GlobalMethods.printDebugInfo("Close Profile Avatar");
        }
    }

    @Override
    protected WebElement getPlatformListEle(String platform) {
        return assertElementExists(By.cssSelector("div.tab_filter_item"), "Platform List", e -> e.getText().trim().equalsIgnoreCase(platform));
    }

    protected WebElement getAccountTabEle() {
        return assertElementExists(By.cssSelector("div.switch_btn"), "Trading Account Type");
    }

    @Override
    protected WebElement getAccountListNoDataEle() {
        return checkElementExists(By.cssSelector("div.no_data_box"), "No Data Content");
    }

    @Override
    protected WebElement getDemoAccountListNoDataEle() {
        return getAccountListNoDataEle();
    }

    @Override
    protected List<WebElement> getAccountListEle() {
        return assertElementsExists(By.cssSelector("div.account_card_box > div > div, div.table_list_box table.el-table__body tr"), "Account List");
    }

    @Override
    protected List<WebElement> getDemoAccountListEle() {
        return assertElementsExists(By.xpath("//div[@id='homeDemo']//div[@class='account_card_box']/div/div"), "Demo Account List");
    }

    @Override
    protected void getAccountListContentEle() {
        WebElement contentEle = assertVisibleElementExists(By.cssSelector("div.account_card_box, div.table_list_box, div.no_data_box"), "Account Listing Content");

        try {
            waitvisible.until(driver -> !contentEle.getText().trim().isEmpty());
        } catch (Exception e) {
            Assert.fail("Account Listing Content not found");
        }
    }

    protected WebElement getAccountSelectorEle() {
        return assertElementExists(By.cssSelector("div#AccountSelector"), "Account Display Mode icon");
    }

    @Override
    protected WebElement getAccountSettingIconEle(int pos) {
        return assertClickableElementExists(By.xpath("(//div[@id='home']//div[@class='setting_box'])" + "[" + pos + "]"), "Account Setting icon");
    }

    @Override
    protected WebElement getDemoAccountSettingIconEle(int pos) {
        return assertClickableElementExists(By.xpath("(//div[@id='homeDemo']//div[@class='setting_box'])" + "[" + pos + "]"), "Account Setting icon");
    }

    @Override
    protected WebElement getEditBalanceEle(int pos) {
        return assertClickableElementExists(By.xpath("//div[contains(@class,'el-popover') and not(contains(@style, 'display: none'))]//span[contains(text(), 'Reset balance')]"), "Reset Balance");
    }

    @Override
    protected WebElement getAccStatusListEle(ACC_STATUS accStatus) {
        return assertElementExists(By.cssSelector("div.el-select-dropdown:not([style*='display: none']) ul li span.el-checkbox__label"), "Status List", e -> e.getText().trim().equalsIgnoreCase(accStatus.getStatusDesc()));
    }

    @Override
    protected WebElement getGridModeEle() {
        return assertElementExists(By.cssSelector("div#AccountSelector[viewType='card'] div.type_switch> img"), "Grid Mode icon");
    }

    @Override
    protected WebElement getListModeEle() {
        return assertElementExists(By.cssSelector("div#AccountSelector[viewType='list'] div.type_switch> img"), "List Mode icon");
    }

    @Override
    protected WebElement getChgLeverageResponseContentEle() {
        return assertVisibleElementExists(By.xpath("//div[@class='dialog_body']"), "Update Leverage Response Content");
    }

    @Override
    protected WebElement getChgAccountPwdResponseContentEle() {
        return assertVisibleElementExists(By.cssSelector("div.home_common_dialog div[class='el-dialog__body']"), "Change Password Response Content");
    }

    @Override
    public void selectDemoAcc(String desc) {
        WebElement e = checkElementExists(By.xpath("//div[@id='homeDemo']"), "Demo Account Tab");

        if (e == null) {
            // Switch to demo account tab if current tab is not demo account
            e = getAccountTabEle();
            triggerElementClickEvent_withoutMoveElement(e);
            GlobalMethods.printDebugInfo("Filter " + desc);
        } else {
            GlobalMethods.printDebugInfo("Already at Demo Account Tab");
        }
    }

    @Override
    public void selectLiveAcc(String desc) {
        WebElement e = checkElementExists(By.cssSelector("div.card_view"), "Live Account Tab");

        if (e == null) {
            // Switch to live account tab if current tab is not live account
            e = getAccountTabEle();
            triggerElementClickEvent_withoutMoveElement(e);
            GlobalMethods.printDebugInfo("Filter " + desc);
        } else {
            GlobalMethods.printDebugInfo("Already at Live Account Tab");
        }
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
                String accTypeDesc = "", platformDesc = "", currencyDesc = "", statusDesc = "";
                GlobalProperties.ACCOUNTTYPE type = null;
                GlobalProperties.CURRENCY currency = null;
                GlobalProperties.PLATFORM platform = null;
                GlobalProperties.ACC_STATUS status = null;

                if(values[1].trim().equals("-") || values[3].trim().equals("--")) {

                    platformDesc = values[0].trim();
                    accNum = values[1].trim();
                    statusDesc = values[2].trim();

                    equity = values[3].trim();
                    if (!equity.equalsIgnoreCase("--")) {
                        Optional<List<String>> strCurr = GlobalMethods.findValueByRegex(equity, "([\\d,]+\\.\\d+)\\s*([A-Z]+)");
                        if (strCurr.isPresent()) {
                            equity = strCurr.get().get(0);
                            currencyDesc = strCurr.get().get(1);
                        }
                    }

                    credit = GlobalMethods.findValueByRegex(values[4], "Credits\\s*:\\s*(.+)").map(groups -> groups.get(0)).orElse("");
                    balance = GlobalMethods.findValueByRegex(values[5], "Balance\\s*:\\s*(.+)").map(groups -> groups.get(0)).orElse("");
                    leverage = GlobalMethods.findValueByRegex(values[6], "Leverage\\s*:\\s*(.+)").map(groups -> groups.get(0)).orElse("");
                    accTypeDesc = GlobalMethods.findValueByRegex(values[7], "Type\\s*:\\s*(.+)").map(groups -> groups.get(0)).orElse("");
                    server = GlobalMethods.findValueByRegex(values[8], "Server\\s*:\\s*(.+)").map(groups -> groups.get(0)).orElse("");

                    // Find platform
                    platform = GlobalProperties.PLATFORM.getRecByPlatformDesc(platformDesc);
                    if (platform == null) {
                        System.out.println("Platform not found, Acc Number: " + accNum + ", Platform: " + platformDesc);
                    }

                    // Find currency
                    if (!currencyDesc.isEmpty()) {
                        currency = GlobalProperties.CURRENCY.getRecByCurrencyDesc(currencyDesc);
                        if (currency == null) {
                            System.out.println("Currency not found, Acc Number: " + accNum + ", Currency: " + currencyDesc);
                        }
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

                } else {

                    platformDesc = values[0].trim();
                    accNum = values[1].trim();

                    Optional<List<String>> strCurr = GlobalMethods.findValueByRegex(values[2], "([\\d,]+\\.\\d+)\\s*([A-Z]+)");
                    if (strCurr.isPresent()) {
                        equity = strCurr.get().get(0);
                        currencyDesc = strCurr.get().get(1);
                    }

                    credit = GlobalMethods.findValueByRegex(values[3], "Credits\\s*:\\s*([0-9,]+\\.\\d+)").map(groups -> groups.get(0)).orElse("");
                    balance = GlobalMethods.findValueByRegex(values[4], "Balance\\s*:\\s*([0-9,]+\\.\\d+)").map(groups -> groups.get(0)).orElse("");
                    leverage = GlobalMethods.findValueByRegex(values[5], "Leverage\\s*:\\s*(.+)").map(groups -> groups.get(0)).orElse("");
                    accTypeDesc = GlobalMethods.findValueByRegex(values[6], "Type\\s*:\\s*(.+)").map(groups -> groups.get(0)).orElse("");
                    server = GlobalMethods.findValueByRegex(values[7], "Server\\s*:\\s*(.+)").map(groups -> groups.get(0)).orElse("");

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

                    status = GlobalProperties.ACC_STATUS.ACTIVE;
                }

                LiveAccountsPage.Account acc = new LiveAccountsPage.Account(accNum, server, type, currency, leverage, status, equity, balance, credit, platform, "");
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
                String accTypeDesc = "", platformDesc = "", currencyDesc = "", statusDesc = "";
                GlobalProperties.ACCOUNTTYPE type = null;
                GlobalProperties.CURRENCY currency = null;
                GlobalProperties.PLATFORM platform = null;
                GlobalProperties.ACC_STATUS status = null;

                accNum = values[0].trim();
                server = values[1].trim();
                platformDesc = values[2].trim();
                accTypeDesc = values[3].trim();
                currencyDesc = values[4].trim();
                equity = values[5].trim();
                credit = values[6].trim();
                balance = values[7].trim();
                leverage = values[8].trim();
                statusDesc = values[9].trim();

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

                LiveAccountsPage.Account acc = new LiveAccountsPage.Account(accNum, server, type, currency, leverage, status, equity, balance, credit, platform, "");
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
            }else {
                String accNum = "", server = "", equity = "", leverage = "";
                String accTypeDesc = "", platformDesc = "", currencyDesc = "", statusDesc = "";
                GlobalProperties.ACCOUNTTYPE type = null;
                GlobalProperties.CURRENCY currency = null;
                GlobalProperties.PLATFORM platform = null;
                GlobalProperties.ACC_STATUS status = null;

                if(values[1].trim().equals("-") || values[3].trim().equals("--")) {

                    platformDesc = values[0].trim();
                    accNum = values[1].trim();
                    statusDesc = values[2].trim();

                    equity = values[3].trim();
                    if (!equity.equalsIgnoreCase("--")) {
                        Optional<List<String>> strCurr = GlobalMethods.findValueByRegex(equity, "([\\d,]+\\.\\d+)\\s*([A-Z]+)");
                        if (strCurr.isPresent()) {
                            equity = strCurr.get().get(0);
                            currencyDesc = strCurr.get().get(1);
                        }
                    }

                    leverage = GlobalMethods.findValueByRegex(values[4], "Leverage\\s*:\\s*(.+)").map(groups -> groups.get(0)).orElse("");
                    accTypeDesc = GlobalMethods.findValueByRegex(values[5], "Type\\s*:\\s*(.+)").map(groups -> groups.get(0)).orElse("");
                    server = GlobalMethods.findValueByRegex(values[6], "Server\\s*:\\s*(.+)").map(groups -> groups.get(0)).orElse("");

                    // Find platform
                    platform = GlobalProperties.PLATFORM.getRecByPlatformDesc(platformDesc);
                    if (platform == null) {
                        System.out.println("Platform not found, Acc Number: " + accNum + ", Platform: " + platformDesc);
                    }

                    // Find currency
                    if (!currencyDesc.isEmpty()) {
                        currency = GlobalProperties.CURRENCY.getRecByCurrencyDesc(currencyDesc);
                        if (currency == null) {
                            System.out.println("Currency not found, Acc Number: " + accNum + ", Currency: " + currencyDesc);
                        }
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

                } else {

                    platformDesc = values[0].trim();
                    accNum = values[1].trim();

                    Optional<List<String>> strCurr = GlobalMethods.findValueByRegex(values[2], "([\\d,]+\\.\\d+)\\s*([A-Z]+)");
                    if (strCurr.isPresent()) {
                        equity = strCurr.get().get(0);
                        currencyDesc = strCurr.get().get(1);
                    }

                    leverage = GlobalMethods.findValueByRegex(values[3], "Leverage\\s*:\\s*(.+)").map(groups -> groups.get(0)).orElse("");
                    accTypeDesc = GlobalMethods.findValueByRegex(values[4], "Type\\s*:\\s*(.+)").map(groups -> groups.get(0)).orElse("");
                    server = GlobalMethods.findValueByRegex(values[5], "Server\\s*:\\s*(.+)").map(groups -> groups.get(0)).orElse("");

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

                    status = GlobalProperties.ACC_STATUS.ACTIVE;
                }

                LiveAccountsPage.Account acc = new LiveAccountsPage.Account(accNum, server, type, currency, leverage, status, equity, "", "", platform, "");
                result.add(acc);
            }
        }

        return result;
    }

    @Override
    // 获取账号区域显示的信息
    public String getAccountInfo() {
        String accountInfo = findVisibleElemntBy(By.id("accounts")).getText();
        System.out.println(accountInfo);
        return accountInfo;
    }

    @Override
    public void selectPlatform(PLATFORM platform) {
        WebElement e = getPlatformListEle(platform.toString());
        triggerElementClickEvent_withoutMoveElement(e);

        GlobalMethods.printDebugInfo("Filter account list by " + platform .getPlatformDesc()+ " platform");
    }

    @Override
    public void selectAccStatus(ACC_STATUS accStatus) {
        WebElement accStatusEle = getAccStatusEle();
        triggerElementClickEvent_withoutMoveElement(accStatusEle);
        GlobalMethods.printDebugInfo("Open Status List");

        WebElement listItemEle = getAccStatusListEle(accStatus);
        WebElement parentEle = listItemEle.findElement(By.xpath("./parent::label"));
        String class_value = parentEle.getAttribute("class");
        if(!class_value.contains("is-checked")) {
            triggerClickEvent_withoutMoveElement(listItemEle);
            GlobalMethods.printDebugInfo("Filter account list by " + accStatus.getStatusDesc() + " status");
        }
        triggerElementClickEvent_withoutMoveElement(accStatusEle);
        GlobalMethods.printDebugInfo("Close Status List");
    }

    @Override
    public void selectDemoAccStatus(ACC_STATUS accStatus) {}

    @Override
    public void setViewContentGridMode() {
        WebElement e = getAccountSelectorEle();
        String viewType = e.getAttribute("viewtype");

        // Set to grid mode if current data is not display in grid mode
        if (!"card".equalsIgnoreCase(viewType)) {
            WebElement listMode = getListModeEle();
            listMode.click();
            GlobalMethods.printDebugInfo("View account list in grid mode.");
        }
    }

    @Override
    public void setViewContentListMode() {
        WebElement e = getAccountSelectorEle();
        String viewType = e.getAttribute("viewtype");

        // Set to list mode if current data is not display in list mode
        if (!"list".equalsIgnoreCase(viewType)) {
            WebElement gridMode = getGridModeEle();
            gridMode.click();
            GlobalMethods.printDebugInfo("View account list in list mode.");
        }
    }
    @Override
    public List<Account> getFirstPageMTSAccountsWithBalance(GlobalProperties.PLATFORM platform){
        waitLoading();
        waitLoadingInCopyTrading();
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
        waitLoading();
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
                    accnum = values[1].trim();
                    // server = values[9].trim();
                    equity = values[2].trim().split("\\s")[0];
                    credit = values[3].trim().split("\\s")[1];
                    balance = values[4].split(":")[1].trim();
                    leve = values[5].split(": ")[1];
                    acctype = values[6];
                    server = values[7].split(":")[1].trim();

                   /* if(StringUtils.containsIgnoreCase(server,"MTS")){
                        balance = values[10].trim();
                        leve = values[5];
                        acctype = values[1];
                        server = values[6];
                    }*/

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
                        GlobalMethods.printDebugInfo("Didn't find type 2: " + values[6].trim());
                    }
                   /* //find currency
                    for(GlobalProperties.CURRENCY c: GlobalProperties.CURRENCY.values()) {
                        if(c.toString().equalsIgnoreCase(values[2].trim())) {
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
                    }*/

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
