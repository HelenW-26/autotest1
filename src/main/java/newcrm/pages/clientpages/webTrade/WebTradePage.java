package newcrm.pages.clientpages.webTrade;

import com.google.gson.Gson;
import lombok.Data;
import newcrm.business.businessbase.CPMenu;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.Page;
import newcrm.testcases.cptestcases.webTrading.WebTradingTestCases;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import utils.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static newcrm.testcases.cptestcases.webTrading.WebTradingTestCases.symbol;
import static newcrm.testcases.cptestcases.webTrading.WebTradingTestCases.volume;


public class WebTradePage extends Page {
    public WebTradePage(WebDriver driver){
        super(driver);
    }

    @Data
    public class WebTradeAccount {
        private String accnum;
        private String orderType;
        private String symbol;
        private String digit;
        private String stopLevel;
        private String entryPrice;
        private String equity;
        private String balance;
        private String credit;
        private String volume;
        private String closeVolume;
        private String openTime;
        private String limitPrice;
        private String stopPrice;
        private String takeProfitUpdate;
        private String stopLossUpdate;
        private String orderId;

    }



    WebTradeAccount webTradeAccount = new WebTradeAccount();
    String notification = null;
    List<WebTradeAccount> positionResult = null;
    List<WebTradeAccount> positionHistoryResult = null;
    List<String> list = null;
    Actions actions = new Actions(driver);
    String reverseOrderId = null;
    //element in account select panel
//    protected By accountSelectDropdown = By.xpath("//div[@class='menu-right']//div[@class='el-select__suffix']");
    protected By accountSelectDropdown = By.xpath("//div[@data-testid='selectAccount']//div[contains(@class,'el-select__wrapper')]");

    //elements in symbol search panel
    protected By searchInput = By.xpath("//div[@data-testid='input-search']//input[@class='el-input__inner']");

    // elements in order panel
    protected By buyBtn = By.xpath("//div[@data-testid='buy-btn']/div/div");
    protected By buyPrice = By.xpath("//div[@data-testid='buy-btn']//span[contains(@class,'order-panel__price-value')]");
    protected By sellBtn = By.xpath("//div[@data-testid='sell-btn']/div/div");
    protected By sellPrice = By.xpath("//div[@data-testid='sell-btn']//span[contains(@class,'order-panel__price-value')]");
    protected By priceInput = By.xpath("//div[@data-testid='limitPrice']//input");
    protected By volumeInput = By.xpath("//div[@data-testid='volume-input']//input[@class='el-input__inner']");
    protected By submitBtn = By.xpath("//button[@data-testid='submit-btn']");
    protected By limitTab = By.xpath("//div[@id='tab-limit']");
    protected By stopTab = By.xpath("//div[@id='tab-dropdown']");
//    protected By stopTabDropDown = By.xpath("//div[@id='tab-dropdown']//div");
    protected By stopTabDropDown = By.xpath("//div[@id='tab-dropdown']//*[local-name()='svg']");
    protected By stop = By.xpath("//ul[@class='el-dropdown-menu']/li[normalize-space(.) = 'Stop']");
    protected By stopLimit = By.xpath("//li[@data-testid='stopLimitMenu']");
//    protected By stopPriceInput = By.xpath("//div[@class='el-form-item__content']//div[contains(@tooltiptext,'Stop Price')]//input");
    protected By stopPriceInput = By.xpath("(//div[@class='el-form-item__content']//label[text()='Stop Price']/following::input[@class='el-input__inner'])[1]");
    protected By stopLimitPriceInput = By.xpath("(//div[@class='el-form-item__content']//label[text()='Limit Price']/following::input[@class='el-input__inner'])[1]");
    protected By limitPriceInput = By.xpath("//div[@class='el-form-item__content']//div[@data-testid='limitPrice']//input");
//    protected By orderSucessMsg = By.xpath("//p[@class='el-message__content']");
    protected By orderNotification = By.xpath("//div[@class='el-notification__content']/p");
    protected By orderNotification1 = By.xpath("(//div[@class='el-notification__content']/p)[2]");

    protected By stopLimit_stopPriceErrorMsg = By.xpath("//div[@class='ht-form-error']//div[contains(text(),'Min value')]");
    protected By stopLimit_limitPriceErrorMsg = By.xpath("//div[@class='ht-form-error']//div[contains(text(),'Max value')]");
    protected By updatePostionErrorMsg = By.xpath("//div[@class='ht-form-error']");

    //elements in position panel
    protected By postionTr = By.xpath("//div[@data-testid='position-list']//table[@class='el-table__body']//tr");
    protected By closeBtn = By.xpath("//div[@data-testid='position-list']//table[@class='el-table__body']//tr/td//button[.//span[normalize-space(.) = 'Close']]");
    protected By closeByBtn = By.xpath("//div[@data-testid='position-list']//table[@class='el-table__body']//tr/td//button[.//span[normalize-space(.) = 'Close By']]");
    protected By reverseBtn = By.xpath("//div[@data-testid='position-list']//table[@class='el-table__body']//tr/td//button[.//span[normalize-space(.) = 'Reverse']]");
    protected By closeAllBtn = By.xpath("//div[@data-testid='position-list']//table[@class='el-table__header']//th[last()]//span[normalize-space(.) = 'Close All']");

    protected By pendingOrderTab = By.xpath("//div[contains(@class,'el-tabs__item is-top')][contains(text(),'Pending Orders')]");
    protected By positionHistory = By.xpath("//div[@class='el-tabs__item is-top'][contains(text(),'Position History')]");
    protected By positionHistoryTr = By.xpath("//div[@id='pane-3']//table[@class='el-table__body']//tr");
    protected By pendingOrderTr = By.xpath("//div[@id='pane-2']//table[@class='el-table__body']//tr");
    protected By cancelPendingOrderBtn = By.xpath("//button/span[normalize-space(.) = 'Cancel']");
    protected By confirmCancel = By.xpath("//button/span[normalize-space(.) = 'Confirm']");
    protected By updatePostionCell = By.xpath("(//*[@data-testid='tpslUpdate'])[1]");
    protected By updatePendingPriceCell = By.xpath("(//*[@data-testid='pending-order-price-edit'])[1]");


    protected By firstRow_Position = By.xpath("//table[@class='el-table__body']//tr[1]");
    protected By takeProfitPriceXPath = By.xpath("(//form[.//label[contains(text(),'Price')]][preceding-sibling::div[./div[contains(text(),'Take Profit')]]]//input)[1]");
    protected By stopLossPriceXPath = By.xpath("(//form[.//label[contains(text(),'Price')]][preceding-sibling::div[./div[contains(text(),'Stop Loss')]]]//input)[1]");
    protected By pendingPriceXPath = By.xpath("//div[contains(@class,'is-active') and @data-testid='pending-order-limit-price']//input");
    protected By confirmPrice = By.xpath("//button/span[normalize-space()='Confirm']");

    //elements in kPanel
    //div[@id='pane-chart']//span[@class='item']
    protected By chartTab= By.xpath("//div[@id='tab-chart']");
    protected By infoTab= By.xpath("//div[@id='tab-info']");
    protected By digit = By.xpath("//span[preceding-sibling::span[text()='Digits']]"); //父产品小数位
    protected By stopLevel = By.xpath("//span[preceding-sibling::span[text()='Stops level']]"); //止损水平
    protected By orderConfirm = By.xpath("//div[@aria-label='Order Confirmation']//button/span[normalize-space()='Confirm']");

    public void selectAccount(String originalTab, String account, CPMenu menu){

        this.selectAccount(originalTab,account);
//        // if web trade page is not load, close and reopen
        if(!driver.findElements(By.xpath("//div[@class='error-state']")).isEmpty()){
            LogUtils.info("reopen web trade page ....");
            switchToOriginalTab(originalTab);
            refresh();
            menu.goToMenu(GlobalProperties.CPMenuName.TRADE);
            this.selectAccount(originalTab,account);
        }
    }

    private void selectAccount(String originalTab,String account){
        switchTab(originalTab);
        for (int i = 0;i < 3;i++){
            try{
                waitLoader();
                WebElement ele = findClickableElemntBy(accountSelectDropdown);
                ele.click();
                findVisibleElemntByXpath("//ul[contains(@class,'el-select-dropdown')]/li[.//span[contains(text(),'"+ account+"')]]").click();
                LogUtils.info("Select account: " + account);
                break;
            } catch (Exception e) {
                LogUtils.info("refresh web trade page ......");
                waitOrder();
                refresh();
            }
        }
    }

    public void selectSymbol(String symbol) {
        final int MAX_RETRY = 3;

        for (int i = 1; i <= MAX_RETRY; i++) {
            try {
                waitLoader();

                WebElement searchInputEle = findVisibleElemntBy(searchInput);

                if (!searchInputEle.isEnabled()) {
                    throw new IllegalStateException("Search input is disabled");
                }

                searchInputEle.clear();
                searchInputEle.sendKeys(symbol);

                webTradeAccount.setSymbol(symbol);

                By symbolLocator = By.xpath(
                        "//div[contains(@class,'market-item__symbol')]//span" +
                                "[contains(@class,'el-tooltip__trigger') and normalize-space(.)='" + symbol + "']"
                );

                WebElement symbolSpan = assertClickableElementExists(
                        symbolLocator,
                        "Can't find symbol: " + symbol
                );

                triggerClickEvent_withoutMoveElement(symbolSpan);

                GlobalMethods.printDebugInfo("Select symbol success: " + symbol);
                return; // 成功直接返回

            } catch (Exception e) {
               LogUtils.info("select symbol failed, try again");
                refresh();
                waitLoader();
            }
        }
    }

    public String getBuyPrice()
    {
        String buyP = findVisibleElemntBy(buyPrice).getText().replace(",", "");
        webTradeAccount.setEntryPrice(buyP);
        GlobalMethods.printDebugInfo("Buy price: " + buyP);
        return  buyP;
    }
    public String getStopPrice()
    {
        return webTradeAccount.getStopPrice();
    }
    public String getLimitPrice()
    {
        return  webTradeAccount.getLimitPrice();
    }

    public String getSellPrice()
    {
        String sellP = findVisibleElemntBy(sellPrice).getText().replace(",", "");
        webTradeAccount.setEntryPrice(sellP);
        GlobalMethods.printDebugInfo("Sell price: " + sellP);
        return  sellP;
    }
    public String getDigitsValue() {
        String digitsText = findVisibleElemntBy(digit).getText(); // e.g., "1", "2", "5"
        GlobalMethods.printDebugInfo("Digits: " + digitsText);

        int digits = Integer.parseInt(digitsText);

        double digutValue = Math.pow(10, -digits);
        String value = String.format("%." + digits + "f", digutValue);

        webTradeAccount.setDigit(value);
        GlobalMethods.printDebugInfo("Digit(负产品小数次方): " + value);
        return value; // e.g., "0.1", "0.01", "0.00001"
    }

    public String getStopsLevel()
    {
        moveElementToVisible(driver.findElement(infoTab));
        findVisibleElemntBy(infoTab).click();
        GlobalMethods.printDebugInfo("Click kPanel - info tab");
        String stopL = findVisibleElemntBy(stopLevel).getText();
        webTradeAccount.setStopLevel(stopL);
        GlobalMethods.printDebugInfo("StopLevel: " + stopL);
        return stopL;
    }


    public String getOrderMsg (){
        try {
            WebElement ele = findVisibleElemntBy(orderNotification);

            return ele.getText();
        } catch (Exception e) {
            LogUtils.info("get notification failed");
            e.printStackTrace();
        }
        return null;
    }
    public boolean placeOrderAndVerifyNotification(String orderType,String volume, String symbol) {

        if (orderType.equalsIgnoreCase("buy")) {
            clickElement(driver.findElement(buyBtn));
            getBuyPrice();
        } else {
            clickElement(driver.findElement(sellBtn));
            getSellPrice();
        }

        setVolume(volume);
        submitOrder();
        GlobalMethods.printDebugInfo("Place order: " + orderType + " " + volume + " " + symbol);

        boolean isExist = !driver.findElements(orderConfirm).isEmpty();

        if(isExist){
            WebElement element = driver.findElement(orderConfirm);
            element.click();
        }

        webTradeAccount.setOrderType(orderType);

        waitLoaderForTrader();

        String orderSuccessMsg = null;
        int retry = 2;
        for(int i =0;i < retry;i++){
            orderSuccessMsg = getOrderMsg();
            if(!StringUtils.isBlank(orderSuccessMsg)){
                break;
            }
            LogUtils.info("create order again......");
            placeOrderAndVerifyNotification(orderType,volume,symbol);
        }

        if(StringUtils.isBlank(orderSuccessMsg)) {
            return true;
        }
        waitLoaderForTrader();
        GlobalMethods.printDebugInfo("Order success message: " + orderSuccessMsg);
        notification = null;
        waitWBNotificationDisplay();
        waitOrder();

        if(StringUtils.isBlank(notification)){
            return true;
        }
        // Check notification content
        if(orderSuccessMsg.contains(symbol)){
            notification = orderSuccessMsg;
        }
        String[] details = notification.split("[\\s,]");
        if (details.length < 7) return false;

        String p_orderType = details[0];
        String p_symbol = details[3];
        String p_volume = details[1];
        String p_price = details[6];

        //存入price和下单有时间差，允许价格波动0.001
        boolean isPriceMatch = false;
        try {
            double pPriceDouble = Double.parseDouble(p_price.replace(",", "").replaceAll("\\.$", "").trim());
            double priceDouble = Double.parseDouble(webTradeAccount.getEntryPrice().replace(",", "").trim());
            double diff = Math.abs(pPriceDouble - priceDouble);
            isPriceMatch = diff <= 0.001;
        } catch (NumberFormatException e) {
            GlobalMethods.printDebugInfo("Can't parse price");
        }

        GlobalMethods.printDebugInfo("page_Order type: " + p_orderType + " page_Symbol: " + p_symbol   + "  page_Volume: " + p_volume + " page_Price: " + p_price);
        GlobalMethods.printDebugInfo("Order type:: " + orderType + " Symbol: " + symbol+ "  Order volume: " + volume +  " Price:" + webTradeAccount.getEntryPrice());

        GlobalMethods.printDebugInfo("Order type match: " + orderType.equalsIgnoreCase(p_orderType) + " symbol: " + symbol.equalsIgnoreCase(p_symbol) + " volume: " + StringUtils.contains(p_volume, volume) + " price:" + StringUtils.contains(p_price, webTradeAccount.getEntryPrice() + " isPriceMatch:" + isPriceMatch));
        return orderType.equalsIgnoreCase(p_orderType)
                && symbol.equalsIgnoreCase(p_symbol)
                && StringUtils.contains(p_volume, volume)
                && (StringUtils.contains(p_price, webTradeAccount.getEntryPrice()) || isPriceMatch);
    }


    public void waitWBNotificationDisplay() {

        try{
            List<WebElement> elements = driver.findElements(orderNotification);
            if(elements.size()>=2){
                notification = elements.get(1).getText();
                if(StringUtils.isNotBlank(notification)){
                    LogUtils.info("Order notification:" + notification);
                    return;
                }

            }

            for (int i = 0;i<6;i++) {
                waitLoaderForTrader();
                By path = orderNotification1;
                WebElement visibleEle = findVisibleElemntBy(path);
                if (visibleEle.isDisplayed()) {
                    GlobalMethods.printDebugInfo("Find Order notification Element");
                    notification = visibleEle.getText();
                    GlobalMethods.printDebugInfo("Order notification:" + notification);
                    break;
                }
                waitOrder(100);
            }
        } catch (Exception e) {
            LogUtils.info("get notification failed");
            e.printStackTrace();
        }


//        try {
//            waitvisible.until(driver -> {
//                try {
//                    waitLoaderForTrader();
//                    By path = orderNotification1;
//                    findVisibleElemntBy(path).isDisplayed();
//                    GlobalMethods.printDebugInfo("Find Order notification Element");
//                    notification = findVisibleElemntBy(path).getText();
//                    GlobalMethods.printDebugInfo("Order notification:" + notification);
//                    return true;
//                } catch (Exception ex) {
//                    return false;
//                }
//            });
//        } catch (Exception ex) {
//            Assert.fail("No orderNotification available or timeout waiting for orderNotification");
//        }
    }

    public boolean placeInvalidPrice(String orderType, String volume) {
        boolean isBuy = orderType.toLowerCase().contains("buy");
        boolean isSell = orderType.toLowerCase().contains("sell");
        boolean isStop = orderType.toLowerCase().contains("stop");
        boolean isLimit = orderType.toLowerCase().contains("limit");
        boolean isStopLimit = orderType.toLowerCase().contains("stop limit");

        // 点击 Buy/Sell 按钮
        if (isBuy) {
            clickElement(driver.findElement(buyBtn));
        } else if (isSell) {
            clickElement(driver.findElement(sellBtn));
        } else {
            GlobalMethods.printDebugInfo("Unsupported order type: " + orderType);
            return false;
        }

        waitLoaderForTrader();

        // 选择 Tab
        if (isStopLimit) {
            clickElement(driver.findElement(stopTab));
            hoverToElement(driver.findElement(stopTabDropDown));
            findClickableElemntBy(stopLimit).click();
        } else if (isStop) {
            clickElement(driver.findElement(stopTab));
        } else if (isLimit) {
            clickElement(driver.findElement(limitTab));
        } else {
            GlobalMethods.printDebugInfo("Unsupported order type tab: " + orderType);
            return false;
        }

        waitLoaderForTrader();
        GlobalMethods.printDebugInfo("Selected order type: " + orderType);

        // 设置 Volume
        setVolume(volume);

        // 设置错误价格
        if (isStopLimit) {
            if (isBuy) {
                setInvalidStopPrice_ForBuyLimitStop();
                setInvalidLimitPrice_ForBuyLimitStop();
            } else {
                setInvalidStopPrice_ForSellLimitStop();
                setInvalidLimitPrice_ForSellLimitStop();
            }
        } else if (isStop) {
            if (isBuy) {
                setInvalidStopPrice_ForBuyLimitStop();
            } else {
                setInvalidStopPrice_ForSellLimitStop();
            }
        } else if (isLimit) {
            if (isBuy) {
                setInvalidLimitPrice_ForBuyLimit();
            } else {
                setInvalidLimitPrice_ForSellLimit();
            }
        }

        // 提交订单
        submitOrder();

        // 错误提示信息
        String stopPriceErrorMsg = "";
        String limitPriceErrorMsg = "";

        if ( (isBuy && isStop) || isStopLimit || (isSell && isLimit) ) {
            stopPriceErrorMsg = getTextSafe(stopLimit_stopPriceErrorMsg);
        }
        if ( (isBuy && isLimit) || isStopLimit || (isSell && isStop) ) {
            limitPriceErrorMsg = getTextSafe(stopLimit_limitPriceErrorMsg);
        }

        // 检查 Pending Order tab 中是否有记录
        findClickableElemntBy(pendingOrderTab).click();
        positionResult = new ArrayList<>();
        List<WebElement> trs = driver.findElements(pendingOrderTr);

        GlobalMethods.printDebugInfo("StopPriceErr: " + stopPriceErrorMsg + ", LimitPriceErr: " + limitPriceErrorMsg + ", Pending rows: " + trs.size());

        // 判断错误提示和 pending order 状态
        if (isStopLimit) {
            return StringUtils.containsIgnoreCase(stopPriceErrorMsg, "Min value")
                    || StringUtils.containsIgnoreCase(limitPriceErrorMsg, "Max value")
                    && trs.isEmpty();
        }

        if (isStop) {
            return isBuy
                    ? StringUtils.containsIgnoreCase(stopPriceErrorMsg, "Min value")
                    : StringUtils.containsIgnoreCase(limitPriceErrorMsg, "Max value");
        }

        if (isLimit) {
            return isBuy
                    ? StringUtils.containsIgnoreCase(limitPriceErrorMsg, "Max value")
                    : StringUtils.containsIgnoreCase(stopPriceErrorMsg, "Min value");
        }

        return false;
    }

    private String getTextSafe(By by) {
        try {
            return findVisibleElemntBy(by).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean placePendingOrderAndVerifyNotification(String orderType, String volume, String symbol) {

        waitOrder();
        // Select order type
        if (orderType.equalsIgnoreCase("Buy Stop Limit")) {
            selectOrder("buy", true);
            setStopPriceForBuyLimitStop();
            setLimitPriceForBuyLimitStop();
        } else if (orderType.equalsIgnoreCase("Sell Stop Limit")) {
            selectOrder("sell", true);
            setStopPriceForSellLimitStop();
            setLimitPriceForSellLimitStop();
        } else if (orderType.equalsIgnoreCase("Buy Stop")) {
            selectOrder("buy stop", false);
            setStopPriceForBuyLimitStop();
        } else if (orderType.equalsIgnoreCase("Sell Stop")) {
            selectOrder("sell stop", false);
            setStopPriceForSellLimitStop();
        }else if (orderType.equalsIgnoreCase("Buy Limit")) {
            selectOrder("buy limit", false);
            setLimitPriceForBuyLimit();
        } else if (orderType.equalsIgnoreCase("Sell Limit")) {
            selectOrder("sell limit", false);
            setLimitPriceForSellLimit();
        } else {
            throw new IllegalArgumentException("Unsupported orderType: " + orderType);
        }

        setVolume(volume);
        submitOrder();

        boolean isExist = !driver.findElements(orderConfirm).isEmpty();

        if(isExist){
            WebElement element = driver.findElement(orderConfirm);
            element.click();
        }

        webTradeAccount.setOrderType(orderType);

        waitLoaderForTrader();

        String orderSuccessMsg = null;
        int retry = 2;
        for(int i =0;i<retry;i++){
            orderSuccessMsg = getOrderMsg();
            if(!StringUtils.isBlank(orderSuccessMsg)){
                break;
            }
            LogUtils.info("create order again......");
            placePendingOrderAndVerifyNotification(orderType,volume,symbol);
            waitOrder(1000);
        }

        if(StringUtils.isBlank(orderSuccessMsg)){
            return true;

        }

        GlobalMethods.printDebugInfo("Order submit message is: " + orderSuccessMsg);

        waitLoaderForTrader();
        notification = null;
        waitWBNotificationDisplay();
        GlobalMethods.printDebugInfo("Order notification message is: " + notification);
        if(StringUtils.isBlank(notification)){
            return true;
        }

        if(orderSuccessMsg.contains(symbol)){
            notification = orderSuccessMsg;
        }
        return validateNotification(orderType, symbol, volume);
    }

    private void selectOrder(String orderType, boolean isStopLimit) {
        if (StringUtils.containsIgnoreCase(orderType, "buy")) {
            clickElement(driver.findElement(buyBtn));
        } else {
            clickElement(driver.findElement(sellBtn));
        }
        if (StringUtils.containsIgnoreCase(orderType, "limit")) {
            clickElement(driver.findElement(limitTab));
        } else {
            clickElement(driver.findElement(stopTab));
        }

        if (isStopLimit) {
            hoverToElement(driver.findElement(stopTabDropDown));
            clickElement(driver.findElement(stopLimit));
            GlobalMethods.printDebugInfo("Select " + orderType + " Stop Limit");
        } else {
            GlobalMethods.printDebugInfo("Select " + orderType + " Stop");
        }
    }

    //check notification
    private boolean validateNotification(String orderType, String symbol, String volume) {
        orderType = orderType.replaceAll("\\s+", "").trim();
        String[] details = notification.split("[\\s,]");
        if (details.length < 7) return false;

        for (String detail : details) {
            System.out.println("Order notification content: " + detail);
        }

        String p_orderType;
        String p_symbol;
        String p_volume;
        String p_stopPrice = "";
        String p_limitPrice = "";

        if (orderType.toLowerCase().contains("stoplimit")) {
            p_orderType = (details[0] + details[1] + details[2]).replaceAll("\\s+", "").trim();
            p_symbol = details[5];
            p_volume = details[3];
            String[] prices = details[8].split("/");
            p_stopPrice = prices[0].trim();
            p_limitPrice = prices[1].trim().replaceAll("\\.$", "");
//            p_stopPrice = prices[1].trim().replaceAll("\\.$", "");
//            p_limitPrice = prices[0].trim();
        } else if (orderType.toLowerCase().contains("stop")){
            p_orderType = (details[0] + details[1]).replaceAll("\\s+", "").trim();
            p_symbol = details[4];
            p_volume = details[2];
            p_stopPrice = details[7].trim().replaceAll("\\.$", "");
        }else{
            p_orderType = (details[0] + details[1]).replaceAll("\\s+", "").trim();
            p_symbol = details[4];
            p_volume = details[2];
            p_limitPrice = details[7].trim().replaceAll("\\.$", "");
        }

        GlobalMethods.printDebugInfo("Parsed: orderType=" + p_orderType + ", symbol=" + p_symbol + ", volume=" + p_volume +
                ", stopPrice=" + p_stopPrice + ", limitPrice=" + p_limitPrice);
        GlobalMethods.printDebugInfo("Expected: orderType=" + orderType + ", symbol=" + symbol + ", volume=" + volume +
                ", stopPrice=" + webTradeAccount.getStopPrice() + ", limitPrice=" + webTradeAccount.getLimitPrice());

        boolean result = StringUtils.containsIgnoreCase(p_orderType, orderType)
                && symbol.equalsIgnoreCase(p_symbol)
                && volume.equalsIgnoreCase(p_volume);

        if (orderType.toLowerCase().contains("limit")) {
            result = result && p_limitPrice.equalsIgnoreCase(webTradeAccount.getLimitPrice());
        }
        else{
            result = result && p_stopPrice.equalsIgnoreCase(webTradeAccount.getStopPrice());
        }

        return result;
    }

    public void setVolume(String volume)
    {
        WebElement volumeEle = findClickableElemntBy(volumeInput);
        moveElementToVisible(volumeEle);
        volumeEle.clear();
        volumeEle.sendKeys(volume);
        webTradeAccount.setVolume(volume);
    }

    public boolean submitOrder(){
        WebElement element = findClickableElemntBy(submitBtn);
        element.click();
        LogUtils.info("commit order。。。。。。");
        return true;
    }

    public List<WebTradeAccount> getPositionInfo(){
        this.waitOrder();
        int retry = 3;
        for (int i = 0;i<retry;i++){
            positionResult = this.<WebTradeAccount>listWebElement(postionTr,row -> {
                String values[] = row.getText().split("\n");
                if (values.length < 11) return null;
                WebTradeAccount wracc  = new WebTradeAccount();
                wracc.setOrderType(values[0].trim());
                wracc.setSymbol(values[1].trim());
                wracc.setVolume(values[2].trim());
                wracc.setEntryPrice(values[3].trim());
                wracc.setTakeProfitUpdate(values[8].trim().split(" /")[0]);
                wracc.setStopLossUpdate(values[10].trim());
                wracc.setOrderId(values[7].trim());

                return wracc;
            });
            waitOrder();

            if (!positionResult.isEmpty()){
                break;
            }
        }

        return  positionResult;
    }


    public void getPositionHistoryInfo(WebTradePage webTradePage) {
        this.waitOrder();
        webTradePage.findClickableElemntBy(webTradePage.positionHistory).click();

        waitListOrderLoader();

        positionHistoryResult = this.getPositionHistoryInfo();

        int retry = 3;
        for (int i = 0;i<retry;i++){
            if (!positionHistoryResult.isEmpty()){
                break;
            }
            waitOrder();
            getPositionHistoryInfo();
        }

//        return  positionHistoryResult;
    }

    private List<WebTradeAccount> getPositionHistoryInfo(){
        positionHistoryResult = this.listWebElement(positionHistoryTr,row -> {
            String values[] = row.getText().split("\n");
            if (values.length < 11) return null;
            WebTradeAccount wracc  = new WebTradeAccount();
            wracc.setOrderType(values[0].trim());
            wracc.setSymbol(values[1].trim());

            String[] volumes = values[2].split("/");
            wracc.setVolume(volumes[1].trim());
            wracc.setCloseVolume(volumes[0].trim());
            wracc.setEntryPrice(values[3].trim());
            wracc.setOrderId(values[10].trim());

            return wracc;
        });
        return positionHistoryResult;
    }

    public boolean checkPositionHisAfterCloseAll(){

        Set<String> orderIds =  this.positionHistoryResult.stream().map(WebTradeAccount::getOrderId).collect(Collectors.toSet());

        LogUtils.info("position history list:" + new Gson().toJson(orderIds));
        LogUtils.info("position  list:" + new Gson().toJson(this.list));
        Set<String> returnList = this.list.stream().filter(orderId -> !orderIds.contains(orderId)).collect(Collectors.toSet());

        LogUtils.info("remove order:" + new Gson().toJson(returnList));

        return returnList.isEmpty();
    }

    public boolean checkLatestPosition() {
        waitOrder();

        boolean orderTypeCheck = StringUtils.containsIgnoreCase(webTradeAccount.orderType,positionResult.get(0).orderType);
        boolean symbolCheck =StringUtils.containsIgnoreCase(webTradeAccount.symbol,positionResult.get(0).symbol);
        boolean volumeCheck =StringUtils.containsIgnoreCase(webTradeAccount.volume,positionResult.get(0).volume);

        //存入price和下单有时间差，允许价格波动0.001
        double p_Price = Double.parseDouble(positionResult.get(0).getEntryPrice());
        double expectedPrice = Double.parseDouble(webTradeAccount.getEntryPrice());
        double diff = Math.abs(p_Price - expectedPrice);
        boolean isPriceMatch = diff <= 0.001;

        boolean entryPrice = (StringUtils.containsIgnoreCase(webTradeAccount.entryPrice,positionResult.get(0).entryPrice)||isPriceMatch);

        GlobalMethods.printDebugInfo("webTradeAccount:" + webTradeAccount.orderType + "  " + webTradeAccount.symbol + "  " + webTradeAccount.volume + "  " + webTradeAccount.entryPrice + " " + webTradeAccount.takeProfitUpdate + " " + webTradeAccount.stopLossUpdate);
        GlobalMethods.printDebugInfo("positionResult.get(0)" + positionResult.get(0).orderType + " " + positionResult.get(0).symbol + " " + positionResult.get(0).volume + " " + positionResult.get(0).entryPrice + " " + positionResult.get(0).takeProfitUpdate +" " + positionResult.get(0).stopLossUpdate);
        GlobalMethods.printDebugInfo("orderTypeCheck:" + orderTypeCheck + "\n" + "symbolCheck:" + symbolCheck + "\n" + "volumeCheck:" + volumeCheck + "\n" + "entryPrice:" + entryPrice);

        return orderTypeCheck && symbolCheck && volumeCheck && entryPrice;
    }
    public boolean checkLatestPosition_Updated() {
        boolean positionCheck = this.checkLatestPosition();

        String takeProfit = positionResult.get(0).takeProfitUpdate;
        String stopLoss = positionResult.get(0).stopLossUpdate;

        if(takeProfit.equalsIgnoreCase("--") || stopLoss.equalsIgnoreCase("--")){
            getPositionInfo();
        }

        boolean takeProfitCheck = StringUtils.containsIgnoreCase(webTradeAccount.takeProfitUpdate,takeProfit);
        boolean stopLossCheck = StringUtils.containsIgnoreCase(webTradeAccount.stopLossUpdate,stopLoss);

        return positionCheck && takeProfitCheck && stopLossCheck;
    }

    public boolean checkLatestPendingOrder(String orderType) {
        boolean orderTypeCheck = StringUtils.containsIgnoreCase(webTradeAccount.orderType,positionResult.get(0).orderType);
        boolean symbolCheck =StringUtils.containsIgnoreCase(webTradeAccount.symbol,positionResult.get(0).symbol);
        boolean volumeCheck =StringUtils.containsIgnoreCase(webTradeAccount.volume,positionResult.get(0).volume);

        //stop单只检查stop price，limit单只检查limit，stop limit检查 stop price and limit price
        boolean hasStop = StringUtils.containsIgnoreCase(orderType, "stop");
        boolean hasLimit = StringUtils.containsIgnoreCase(orderType, "limit");

        boolean stopPriceCheck = true;  // 默认通过
        boolean limitPriceCheck = true; // 默认通过

        if (hasStop) {
            stopPriceCheck = StringUtils.equalsIgnoreCase(
                    webTradeAccount.getStopPrice(),
                    positionResult.get(0).getStopPrice()
            );
        }

        if (hasLimit) {
            limitPriceCheck = StringUtils.equalsIgnoreCase(
                    webTradeAccount.getLimitPrice(),
                    positionResult.get(0).getLimitPrice()
            );
        }

        GlobalMethods.printDebugInfo(
                "orderTypeCheck: " + orderTypeCheck + "\n" +
                        "symbolCheck: " + symbolCheck + "\n" +
                        "volumeCheck: " + volumeCheck + "\n" +
                        (hasStop ? "stopPriceCheck: " + stopPriceCheck + "\n" : "") +
                        (hasLimit ? "limitPriceCheck: " + limitPriceCheck + "\n" : "")
        );

        // 只返回必须要 check 的内容
        boolean finalResult = orderTypeCheck && symbolCheck && volumeCheck;

        if (hasStop) {
            finalResult = finalResult && stopPriceCheck;
        }

        if (hasLimit) {
            finalResult = finalResult && limitPriceCheck;
        }

        return finalResult;
    }

    public List<WebTradeAccount> getPendingOrderInfo() {
        waitOrder();
        findClickableElemntBy(pendingOrderTab).click();
        positionResult = new ArrayList<>();
        waitOrder(3000);

        int retry = 3;
        for (int i = 0;i<retry;i++){
            List<WebElement> trs = driver.findElements(pendingOrderTr);

            for(WebElement tr : trs)
            {
                String info = tr.getText();
                GlobalMethods.printDebugInfo("info:" + info);
                String values[] = info.split("\n");
                if (values.length < 2)
                    continue;
                String time = values[0].trim();
                //            String orderType = values[4].trim() + " " + values[3].trim();
                String orderType = values[3].trim() + " " + values[2].trim();
                String symbol = values[1].trim();
                String volume = values[8].trim().equalsIgnoreCase(WebTradingTestCases.volume) ? values[8].trim() : values[9].trim();

                String orderId = values[12].trim().equals("--")?values[13].trim():values[12].trim();

                //get the stop price
                String stopPrice = values[5].trim();

                Pattern pattern = Pattern.compile("([0-9]+\\.[0-9]+)");
                Matcher matcher = pattern.matcher(stopPrice);

                if (matcher.find()) {
                    stopPrice = matcher.group(1);
                }

                String limitPrice = stopPrice;
                if(values[2].trim().equalsIgnoreCase("Stop Limit") ){
                    limitPrice = values[6].trim();
                }

                WebTradeAccount wracc  = new WebTradeAccount();
                wracc.setOpenTime( time);
                wracc.setOrderType(orderType);
                wracc.setSymbol(symbol);
                wracc.setVolume(volume);
                wracc.setStopPrice(stopPrice);
                wracc.setLimitPrice(limitPrice);
                wracc.setOrderId(orderId);
                positionResult.add(wracc);
                printWebTradAccount(wracc);
            }

            if(!positionResult.isEmpty()){
                waitOrder();
                break;
            }
        }

        return positionResult;

    }
    public boolean closeOrderAndVerifyNotification() {
        waitOrder();
        findVisibleElemntBy(closeBtn).click();
        waitLoaderForTrader();

        By xpath = By.xpath("//div[@aria-label='Confirm']//button/span[normalize-space()='Confirm']");
        boolean isExist = !driver.findElements(xpath).isEmpty();

        if(isExist){
            findClickableElemntBy(xpath).click();
        }

        return verifyNotification("Closing submitted", "Position closed") || verifyNotification( "Position closed","Closing submitted");
    }

    private void moveToEle(By path){
        waitOrder();

        WebElement firstPosition = findVisibleElemntBy(firstRow_Position);
        actions.moveToElement(firstPosition).doubleClick().perform();

        for (int i = 0; i < 10; i++) {
            actions.sendKeys(Keys.ARROW_RIGHT).pause(200).perform();
        }

        WebElement editIcon = findVisibleElemntBy(path);
        actions.moveToElement(editIcon).click().perform();

        waitLoaderForTrader();
    }

    public boolean reverseOrderAndVerifyNotification() {

        String sellPrice = getSellPrice();
        String buyPrice = getBuyPrice();

        moveToEle(reverseBtn);

        reverseOrderId = findClickableElemntBy(By.xpath("//div[@class='reverse-content']//div[@class='reverse-content--info-top-order']")).getText();

        List<WebElement> values = driver.findElements(By.xpath("//div[@class='reverse-content']//div[@class='reverse-content--info-bottom-item-value']"));

        boolean buyPriceFlag = Math.abs(Double.parseDouble(values.get(0).getText().trim()) - Double.parseDouble(buyPrice)) <= 0.01;
        boolean buyVolumeFlag = StringUtils.equalsIgnoreCase(values.get(1).getText().trim(),volume);
        boolean sellPriceFlag = Math.abs(Double.parseDouble(values.get(2).getText().trim()) - Double.parseDouble(buyPrice)) <= 0.01;
        boolean sellVolumeFlag = StringUtils.equalsIgnoreCase(values.get(3).getText().trim(),volume);

        if(!buyPriceFlag || !buyVolumeFlag || !sellPriceFlag || !sellVolumeFlag){
            return false;
        }

        List<WebElement> symbols = driver.findElements(By.xpath("//div[@class='reverse-content']//div[@class='symbol-name']"));

        for (WebElement ele : symbols){
            if(!ele.getText().equalsIgnoreCase(symbol)){
                return false;
            }
        }

        String buy = findVisibleElemntByXpath("//div[@class='reverse-content']//div[@class='symbol-icon buy']").getText();
        String sell = findVisibleElemntByXpath("//div[@class='reverse-content']//div[@class='symbol-icon sell']").getText();

        LogUtils.info("reverse values: "+ new Gson().toJson(values) + "order type: " + buy + " , " + sell);

        if(!"B".equalsIgnoreCase(buy) || !"S".equalsIgnoreCase(sell) ){
            return false;
        }

        webTradeAccount.setOrderType("S");
        webTradeAccount.setEntryPrice(sellPrice);

        By xpath = By.xpath("//div[@aria-label='Reverse']//button/span[normalize-space()='Confirm']");

        findClickableElemntBy(xpath).click();

        waitLoaderForTrader();

        String orderMsg = getOrderMsg();

        if(StringUtils.isBlank(orderMsg)){
            return true;

        }
        notification = null;

        waitWBNotificationDisplay();

        if (StringUtils.isBlank(notification)){
            return true;
        }

        String[] details = notification.split("[\\s,]");
        if (details.length < 7) return false;

        String p_orderType = details[0];
        String p_symbol = details[3];
        String p_volume = details[1];
        String p_price = details[6];

        //存入price和下单有时间差，允许价格波动0.001
        boolean isPriceMatch = false;
        try {
            double pPriceDouble = Double.parseDouble(p_price.replace(",", "").replaceAll("\\.$", "").trim());
            double priceDouble = Double.parseDouble(sellPrice.trim());
            double diff = Math.abs(pPriceDouble - priceDouble);
            isPriceMatch = diff <= 0.001;
        } catch (NumberFormatException e) {
            GlobalMethods.printDebugInfo("Can't parse price");
        }

        GlobalMethods.printDebugInfo("page_Order type: " + p_orderType + " page_Symbol: " + p_symbol   + "  page_Volume: " + p_volume + " page_Price: " + p_price);
        GlobalMethods.printDebugInfo("Order type:: " + "sell" + " Symbol: " + symbol+ "  Order volume: " + volume +  " Price:" + sellPrice);



        return "sell".equalsIgnoreCase(p_orderType)
                && symbol.equalsIgnoreCase(p_symbol)
                && StringUtils.contains(p_volume, volume)
                && (StringUtils.contains(p_price, sell) || isPriceMatch);
    }


    public boolean closeByOrderAndVerifyNotification(){

        getPositionInfo();

        String sellPrice = getSellPrice();
        String buyPrice = getBuyPrice();

        moveToEle(closeByBtn);

        WebElement row = driver.findElement(By.xpath("//div[@class='close-by-content-bottom']//tr[@class='el-table__row']"));

        List<WebElement> cells = row.findElements(By.xpath("//div[@class='close-by-content-bottom']//tr[@class='el-table__row']//div[@class='cell']"));

        String[] bottomValues = cells.get(0).getText().split("\\r?\\n");;
        String bottomVol = cells.get(1).getText();
        String bottomEntryPrice = cells.get(2).getText();

        String topSymbol = findVisibleElemntByXpath("//div[@class='close-by-content-top']//div[@class='symbol-name']").getText();
        List<WebElement> topMsg = driver.findElements(By.xpath("//div[@class='close-by-content-top']//div[@class='close-by-content-top-info-item-value']"));
        String topVol = topMsg.get(0).getText();
        String topEntryPrice = topMsg.get(1).getText();

        String topType = findVisibleElemntByXpath("//div[@class='close-by-content-top']//div[contains(@class,'symbol-icon')]").getText();

        if (!bottomValues[1].equalsIgnoreCase(topSymbol) || !bottomVol.equalsIgnoreCase(topVol) || bottomValues[0].equalsIgnoreCase(topType)){
            return false;
        }
        boolean buyPriceFlag = false;
        boolean sellPriceFlag = false;
        if (topType.equalsIgnoreCase("s")){
            buyPriceFlag = Math.abs(Double.parseDouble(bottomEntryPrice.trim()) - Double.parseDouble(buyPrice)) <= 0.01;
            sellPriceFlag = Math.abs(Double.parseDouble(topEntryPrice.trim()) - Double.parseDouble(sellPrice)) <= 0.01;
        }else{
            sellPriceFlag = Math.abs(Double.parseDouble(bottomEntryPrice.trim()) - Double.parseDouble(sellPrice)) <= 0.01;
            buyPriceFlag = Math.abs(Double.parseDouble(topEntryPrice.trim()) - Double.parseDouble(buyPrice)) <= 0.01;
        }

        if(!sellPriceFlag  || !buyPriceFlag){
            return false;
        }

        LogUtils.info("closeBy confirm page data: " + Arrays.toString(bottomValues) +" , "+ bottomVol + " , "+ topSymbol + " , " + topVol  + " , " + topType);

        findClickableElemntBy(confirmCancel).click();

        waitLoaderForTrader();
        return verifyNotification("Close by request submitted","Position closed");
    }

    public boolean verifyPositionHisAfterCloseBy(){

        Set<String> orderIds = positionResult.stream()
                .map(WebTradeAccount::getOrderId)
                .collect(Collectors.toSet());

        List<WebTradeAccount> sameOrders = positionHistoryResult.stream()
                .filter(a -> orderIds.contains(a.getOrderId()))
                .collect(Collectors.toList());

        LogUtils.info("close by order is : " + sameOrders.stream().toList());

        if(sameOrders.size() < 2){
            return false;
        }

        boolean closeVolumeCheck = StringUtils.equalsIgnoreCase(sameOrders.get(0).closeVolume, sameOrders.get(1).closeVolume);
        boolean volumeCheck1 = StringUtils.equalsIgnoreCase(sameOrders.get(1).volume, volume);
        boolean volumeCheck2 = StringUtils.equalsIgnoreCase(sameOrders.get(0).volume, volume);

        getPositionInfo();

        List<WebTradeAccount> temp = positionResult.stream()
                .filter(a -> orderIds.contains(a.getOrderId()))
                .collect(Collectors.toList());

        return closeVolumeCheck && volumeCheck1 && volumeCheck2 && temp.isEmpty();

    }

    public boolean checkChartInfo(){

        waitOrder();

        String price = driver.findElement(By.xpath("//div[@class='symbol-price']//span[@class='price']")).getText();

        int count = 0;
        for (int i = 0;i<10;i++){
            waitOrder(2000);
            String price1 = driver.findElement(By.xpath("//div[@class='symbol-price']//span[@class='price']")).getText();
            if(!price1.equalsIgnoreCase(price)){
                count ++;
            }
            LogUtils.info("current price:"+ price  + "," + price1);
            price = price1;
        }

        if(count == 0){
            return false;
        }

        List<WebElement> intervalEles = driver.findElements(By.xpath("//div[@class='chart-info']//div[@class='change-interval']//span"));

        for (WebElement interval : intervalEles){
            interval.click();
            LogUtils.info("click time: " + interval.getText());
            waitOrder(3000);
            String clickTime = interval.getText().split("m")[0];

            WebElement iframe = driver.findElement(By.xpath("//iframe[contains(@id,'tradingview')]"));

            driver.switchTo().frame(iframe);
            String getTime = driver.findElement(By.xpath("(//div[@class='chart-gui-wrapper']//button)[3]")).getText();
            String getSymbol = driver.findElement(By.xpath("(//div[@class='chart-gui-wrapper']//button)[1]")).getText();

            LogUtils.info("display symbol is: " + getSymbol + " , and time is: "+ getTime);

            if(!clickTime.equalsIgnoreCase(getTime) || !getSymbol.equalsIgnoreCase(symbol)){
                return false;
            }
            List<WebElement> canvasList = driver.findElements(By.tagName("canvas"));
            if (canvasList.isEmpty()) {
                return false;
            }
            driver.switchTo().defaultContent();
        }

        return true;
    }

    public boolean closeAllOrderAndVerifyNotification() {

        list = this.getPositionInfo().stream().map(WebTradeAccount::getOrderId).collect(Collectors.toList());

        waitOrder();
        hoverToElement(driver.findElement(closeAllBtn));
        findClickableElementByXpath("//ul[contains(@class,'el-dropdown-menu')]//li[normalize-space(.)='Close All']").click();


        boolean isExist = !driver.findElements(confirmCancel).isEmpty();

        if(isExist){
            findClickableElemntBy(confirmCancel).click();
        }

        waitLoaderForTrader();

        return verifyNotification("Batch close request submitted","Position closed") || verifyNotification("Position closed","Batch close request submitted");
    }

    public boolean verifyPositionTabAndHisAfterClose(){

        String orderId = positionResult.get(0).orderId;

        boolean orderTypeCheck = StringUtils.containsIgnoreCase(positionResult.get(0).orderType,positionHistoryResult.get(0).orderType);
        boolean symbolCheck = StringUtils.containsIgnoreCase(positionResult.get(0).symbol,positionHistoryResult.get(0).symbol);
        boolean volumeCheck = StringUtils.containsIgnoreCase(positionResult.get(0).volume,positionHistoryResult.get(0).volume);
        boolean orderCheck = positionHistoryResult.stream().map(WebTradeAccount::getOrderId).filter(orderId::equals).findFirst().isPresent();

        GlobalMethods.printDebugInfo("positionResult.get(0)" + positionResult.get(0).orderType + " " + positionResult.get(0).symbol + " " + positionResult.get(0).volume + " " + positionResult.get(0).entryPrice +" "+orderId);
        GlobalMethods.printDebugInfo("positionHistoryResult.get(0)" + positionHistoryResult.get(0).orderType + " " + positionHistoryResult.get(0).symbol + " " + positionHistoryResult.get(0).volume + " " + positionHistoryResult.get(0).entryPrice +" "+positionHistoryResult.get(0).orderId);
        GlobalMethods.printDebugInfo("orderTypeCheck:" + orderTypeCheck + "\n" + "symbolCheck:" + symbolCheck + "\n" + "volumeCheck:" + volumeCheck + "\norderCheck:" + orderCheck);

        findClickableElementByXpath("//div[@class='el-tabs__item is-top'][contains(text(),'Positions')]").click();

        List<WebTradeAccount> positionInfo = this.getPositionInfo();
        boolean checkPoistion = positionInfo.stream().map(WebTradeAccount::getOrderId).filter(orderId::equals).findFirst().isPresent();
        GlobalMethods.printDebugInfo("positionCheck:" + checkPoistion);

        return orderTypeCheck && symbolCheck && volumeCheck && orderCheck && !checkPoistion;
    }

    public boolean verifyOrderAfterReverse(){
        return positionHistoryResult.stream().map(WebTradeAccount::getOrderId).filter(reverseOrderId::equals).findFirst().isPresent();
    }


    public boolean cancelPendingOrderAndVerifyNotification() {


        waitOrder();
        clickElement(driver.findElement(cancelPendingOrderBtn));

        waitLoaderForTrader();

        findVisibleElemntBy(confirmCancel).click();

        String closeSuccessMsg = getOrderMsg();
        if(StringUtils.isBlank(closeSuccessMsg)){
            return true;
        }

        GlobalMethods.printDebugInfo("cancelSuccessMsg: " + closeSuccessMsg);

        notification = "Order canceled";

        if(StringUtils.isBlank(notification)){
            return "Cancellation submitted".equals(closeSuccessMsg);
        }

        return "Cancellation submitted".equals(closeSuccessMsg)
                && "Order canceled".equals(notification);
    }

    public boolean checkCancelPendingOrder(){
        String orderId = this.positionResult.get(0).orderId;

        List<WebTradeAccount> pendingOrderInfoList = this.getPendingOrderInfo();

        boolean checkPoistion = pendingOrderInfoList.stream().map(WebTradeAccount::getOrderId).filter(orderId::equals).findFirst().isPresent();
        return !checkPoistion;
    }

    //根据 挂单价格>=Buy价 + 止损水平*10的负产品小数次方 算出一个合理的挂单价
    public void setStopPriceForBuyLimitStop() {

        int stopLevel = Integer.parseInt(webTradeAccount.getStopLevel());
        double digitValue = Double.parseDouble(webTradeAccount.getDigit());
        double basePrice = Double.parseDouble(getBuyPrice());

        double stopPrice = basePrice + stopLevel * digitValue;

        //确保大于上限
        stopPrice += 1;
        String formattedStopPrice = String.format("%.5f", stopPrice);
        GlobalMethods.printDebugInfo("stop Price:" + formattedStopPrice);

        webTradeAccount.setStopPrice(formattedStopPrice);

        this.inputPrice(stopPriceInput,formattedStopPrice);
    }

    //根据 挂单价格<=Sell价 - 止损水平*10的负产品小数次方 算出一个合理的挂单价
    public void setStopPriceForSellLimitStop() {

        String formattedStopPrice = this.calculatePriceOfMinus(getSellPrice());
        GlobalMethods.printDebugInfo("stop Price for sell limit stop:" + formattedStopPrice);

        webTradeAccount.setStopPrice(formattedStopPrice);

        this.inputPrice(stopPriceInput,formattedStopPrice);
    }

    private String calculatePriceOfMinus(String price){
        int stopLevel = Integer.parseInt(webTradeAccount.getStopLevel());
        double digitValue = Double.parseDouble(webTradeAccount.getDigit());
        double basePrice = Double.parseDouble(price);

        double limitPrice = basePrice - stopLevel * digitValue;
        limitPrice -= 0.1;
        return String.format("%.5f", limitPrice);

    }


    //根据 限价价格<= 挂单价格 - 止损水平*10的负产品小数次方 算出一个合理的限价价格
    public void setLimitPriceForBuyLimitStop() {

        waitLoaderForTrader();

        String formattedLimitPrice = this.calculatePriceOfMinus(webTradeAccount.getEntryPrice());
        webTradeAccount.setLimitPrice(formattedLimitPrice);

        GlobalMethods.printDebugInfo("limit Price:" + formattedLimitPrice);

        this.inputPrice(stopLimitPriceInput,formattedLimitPrice);

    }

    //根据 限价价格<= Buy价 - 止损水平*10的负产品小数次方 算出一个合理的限价价格
    public void setLimitPriceForBuyLimit() {

        waitLoaderForTrader();

        String formattedLimitPrice = this.calculatePriceOfMinus(getBuyPrice());
        webTradeAccount.setLimitPrice(formattedLimitPrice);

        GlobalMethods.printDebugInfo("limit Price:" + formattedLimitPrice);

        this.inputPrice(limitPriceInput,formattedLimitPrice);

    }
    //根据 限价价格>= 挂单价格 - 止损水平*10的负产品小数次方 算出一个合理的限价价格
    public void setLimitPriceForSellLimitStop() {

        waitLoaderForTrader();
        int stopLevel = Integer.parseInt(webTradeAccount.getStopLevel());
        double digitValue = Double.parseDouble(webTradeAccount.getDigit());
        double basePrice = Double.parseDouble(webTradeAccount.getEntryPrice());

        // 计算限价价格的上限值
        double limitPrice = basePrice - stopLevel * digitValue;

        // 确保小于上限
        limitPrice +=1;
        String formattedLimitPrice = String.format("%.5f", limitPrice);
        webTradeAccount.setLimitPrice(formattedLimitPrice);

        GlobalMethods.printDebugInfo("limit Price for sell stop limit:" + formattedLimitPrice);

        this.inputPrice(stopLimitPriceInput,formattedLimitPrice);
    }
    //根据 限价价格>= Sell价格 + 止损水平*10的负产品小数次方 算出一个合理的限价价格
    public void setLimitPriceForSellLimit() {

        waitLoaderForTrader();
        int stopLevel = Integer.parseInt(webTradeAccount.getStopLevel());
        double digitValue = Double.parseDouble(webTradeAccount.getDigit());
        double basePrice = Double.parseDouble(getSellPrice());

        // 计算限价价格的上限值
        double limitPrice = basePrice - stopLevel * digitValue;

        // 确保小于上限
        limitPrice +=1;
        String formattedLimitPrice = String.format("%.5f", limitPrice);
        webTradeAccount.setLimitPrice(formattedLimitPrice);

        GlobalMethods.printDebugInfo("limit Price for sell stop limit:" + formattedLimitPrice);

        this.inputPrice(limitPriceInput,formattedLimitPrice);
    }

    //根据 挂单价格<=Buy价 + 止损水平*10的负产品小数次方 算出一个不合理的挂单价
    public void setInvalidStopPrice_ForBuyLimitStop() {

        String formattedStopPrice = this.calculatePriceOfMinus(getBuyPrice());
        GlobalMethods.printDebugInfo("invalid stop price for buy stop limit:" + formattedStopPrice);

        this.inputPrice(stopPriceInput,formattedStopPrice);

    }
    //根据 挂单价格>=Buy价 + 止损水平*10的负产品小数次方 算出一个不合理的挂单价
    public void setInvalidLimitPrice_ForBuyLimit() {

        int stopLevel = Integer.parseInt(webTradeAccount.getStopLevel());
        double digitValue = Double.parseDouble(webTradeAccount.getDigit());
        double basePrice = Double.parseDouble(getBuyPrice());

        double stopPrice = basePrice + stopLevel * digitValue;

        stopPrice += 1;
        String formattedStopPrice = String.format("%.5f", stopPrice);
        GlobalMethods.printDebugInfo("invalid limit price for buy limit:" + formattedStopPrice);

        this.inputPrice(limitPriceInput,formattedStopPrice);
    }

    //根据 挂单价格<=Sell价 - 止损水平*10的负产品小数次方 算出一个不合理的挂单价
    public void setInvalidLimitPrice_ForSellLimit() {

        String formattedStopPrice = this.calculatePriceOfMinus(getSellPrice());
        GlobalMethods.printDebugInfo("invalid limit price for sell limit:" + formattedStopPrice);

        this.inputPrice(limitPriceInput,formattedStopPrice);
    }

    //根据 挂单价格>=Sell价 + 止损水平*10的负产品小数次方 算出一个不合理的挂单价
    public void setInvalidStopPrice_ForSellLimitStop() {

        int stopLevel = Integer.parseInt(webTradeAccount.getStopLevel());
        double digitValue = Double.parseDouble(webTradeAccount.getDigit());
        double basePrice = Double.parseDouble(getSellPrice());

        double stopPrice = basePrice + stopLevel * digitValue;

        stopPrice += 1;
        String formattedStopPrice = String.format("%.5f", stopPrice);
        GlobalMethods.printDebugInfo("invalid stop price for sell stop limit:" + formattedStopPrice);

        this.inputPrice(stopPriceInput,formattedStopPrice);
    }
    //根据 限价价格>= 挂单价格 - 止损水平*10的负产品小数次方 算出一个不合理的限价价格
    public void setInvalidLimitPrice_ForBuyLimitStop() {

        int stopLevel = Integer.parseInt(webTradeAccount.getStopLevel());
        double digitValue = Double.parseDouble(webTradeAccount.getDigit());
        double basePrice = Double.parseDouble(webTradeAccount.getEntryPrice());

        // 计算限价价格的上限值
        double limitPrice = basePrice - stopLevel * digitValue;

        // 确保小于上限
        limitPrice += 0.1;
        String formattedLimitPrice = String.format("%.5f", limitPrice);
        webTradeAccount.setLimitPrice(formattedLimitPrice);

        GlobalMethods.printDebugInfo("set invalid limit Price for buy stop limit:" + limitPrice);

        this.inputPrice(stopLimitPriceInput,formattedLimitPrice);
    }
    //根据 限价价格<= 挂单价格 - 止损水平*10的负产品小数次方 算出一个不合理的限价价格
    public void setInvalidLimitPrice_ForSellLimitStop() {

        String formattedLimitPrice = this.calculatePriceOfMinus(webTradeAccount.getEntryPrice());
        webTradeAccount.setLimitPrice(formattedLimitPrice);

        GlobalMethods.printDebugInfo("set invalid limit Price for sell stop limit:" + formattedLimitPrice);

        this.inputPrice(stopLimitPriceInput,formattedLimitPrice);
    }

    /*修改buy持仓单，止盈(take profit) > Sell价 + 止损水平*10的负产品小数次方 算出一个合理的止盈价
    修改buy持仓单，止盈(take profit) < Sell价 + 止损水平*10的负产品小数次方 算出一个异常的止盈价*/
    public void setTakeProfitForBuyPosition(String p_sellPrice,String p_StopLevel, String p_digit,boolean isValid) {

        int stopLevel = Integer.parseInt(p_StopLevel);
        double digitValue = Double.parseDouble(p_digit);
        double sellPrice = Double.parseDouble(p_sellPrice);

        double takeProfitPrice = sellPrice + stopLevel * digitValue;

        String formattedTakeProfitPrice = null;

        if(isValid) {
            //确保大于上限
            takeProfitPrice += 1;
            formattedTakeProfitPrice = String.format("%.5f", takeProfitPrice);
            GlobalMethods.printDebugInfo("Buy order take profit price updated:" + formattedTakeProfitPrice);
        }
        else{
            takeProfitPrice -= 0.1;
            formattedTakeProfitPrice = String.format("%.5f", takeProfitPrice);
        }
        webTradeAccount.setTakeProfitUpdate(formattedTakeProfitPrice);

        this.inputPrice(takeProfitPriceXPath,formattedTakeProfitPrice);
    }

    /*修改buy持仓单，止损(stop loss) < Sell价 + 止损水平*10的负产品小数次方 算出一个合理的止损价
    修改buy持仓单，止损(stop loss) > Sell价 + 止损水平*10的负产品小数次方 算出一个异常的止损价*/
    public void setStopLossForBuyPosition(String p_sellPrice,String p_StopLevel, String p_digit, boolean isValid) {

        int stopLevel = Integer.parseInt(p_StopLevel);
        double digitValue = Double.parseDouble(p_digit);
        double sellPrice = Double.parseDouble(p_sellPrice);

        double stopLossPrice = sellPrice - stopLevel * digitValue;
        String formattedStopLossPrice = null;

        if(isValid) {
            //确保小于上限
            stopLossPrice -= 0.1;
            formattedStopLossPrice = String.format("%.5f", stopLossPrice);
            GlobalMethods.printDebugInfo("Buy order stop loss price updated:" + formattedStopLossPrice);
        }
        else
        {
            stopLossPrice += 1;
            formattedStopLossPrice = String.format("%.5f", stopLossPrice);
        }
        webTradeAccount.setStopLossUpdate(formattedStopLossPrice);

        this.inputPrice(stopLossPriceXPath,formattedStopLossPrice);
    }

    /*修改sell持仓单，止盈(take profit) < buy价 - 止损水平*10的负产品小数次方 算出一个合理的止盈价
    修改sell持仓单，止盈(take profit) > buy价 - 止损水平*10的负产品小数次方 算出一个合理的止盈价*/
    public void setTakeProfitForSellPosition(String p_buyPrice,String p_StopLevel, String p_digit,boolean isValid) {

        int stopLevel = Integer.parseInt(p_StopLevel);
        double digitValue = Double.parseDouble(p_digit);
        double buyPrice = Double.parseDouble(p_buyPrice);

        double takeProfitPrice = buyPrice - stopLevel * digitValue;
        String formattedTakeProfitPrice = null;

        if(isValid) {
            //确保小于上限
            takeProfitPrice -= 0.1;
            formattedTakeProfitPrice = String.format("%.5f", takeProfitPrice);
            GlobalMethods.printDebugInfo("Sell order take profit price updated:" + formattedTakeProfitPrice);
        }
        else{
            takeProfitPrice += 1;
            formattedTakeProfitPrice = String.format("%.5f", takeProfitPrice);
        }
        webTradeAccount.setTakeProfitUpdate(formattedTakeProfitPrice);

        this.inputPrice(takeProfitPriceXPath,formattedTakeProfitPrice);
    }

    /*修改sell持仓单，止损(stop loss) > buy价 + 止损水平*10的负产品小数次方 算出一个合理的止损价
    修改sell持仓单，止损(stop loss) < buy价 + 止损水平*10的负产品小数次方 算出一个合理的止损价*/
    public void setStopLossForSellPosition(String p_buyPrice,String p_StopLevel, String p_digit,boolean isValid) {

        int stopLevel = Integer.parseInt(p_StopLevel);
        double digitValue = Double.parseDouble(p_digit);
        double buyPrice = Double.parseDouble(p_buyPrice);

        double stopLossPrice = buyPrice + stopLevel * digitValue;

        String formattedStopLossPrice = null;

        if(isValid){
            //确保小于上限
            stopLossPrice += 1;
            formattedStopLossPrice = String.format("%.5f", stopLossPrice);
            GlobalMethods.printDebugInfo("Sell order stop loss price updated:" + formattedStopLossPrice);
        }else{
            stopLossPrice -= 0.1;
            formattedStopLossPrice = String.format("%.5f", stopLossPrice);
        }
        webTradeAccount.setStopLossUpdate(formattedStopLossPrice);

        this.inputPrice(stopLossPriceXPath,formattedStopLossPrice);
    }

    /**
     *  normal: 挂单价格 <= Buy价 - 止损水平 * 10的负产品小数次方
     *  invalid: 挂单价格 >= Buy价 - 止损水平 * 10的负产品小数次方
     */
    public void setLimitForPendingPosition(String p_buyPrice,String p_StopLevel, String p_digit, boolean isValid){
        int stopLevel = Integer.parseInt(p_StopLevel);
        double digitValue = Double.parseDouble(p_digit);
        double buyPrice = Double.parseDouble(p_buyPrice);

        double price = buyPrice + stopLevel * digitValue;
        String formattedStopLossPrice = null;

        if(isValid){
            price -= 0.1;
            formattedStopLossPrice = String.format("%.5f", price);
            GlobalMethods.printDebugInfo("Sell order stop loss price updated:" + formattedStopLossPrice);
        }else{
            price += 1;
            formattedStopLossPrice = String.format("%.5f", price);
        }
        webTradeAccount.setStopPrice(formattedStopLossPrice);

        this.inputPrice(pendingPriceXPath,formattedStopLossPrice);
    }

    private void inputPrice(By byPath, String price){

        WebElement priceEle = findClickableElemntBy(byPath);
        moveElementToVisible(priceEle);
        priceEle.clear();
        priceEle.sendKeys(price);
    }

    public boolean verifyNotification(String crmMessage, String wbMessage){
        try{
            waitLoaderForTrader();
            String closeSuccessMsg = getOrderMsg();
            if(StringUtils.isBlank(closeSuccessMsg)){
                return true;
            }

            GlobalMethods.printDebugInfo("crmMessage: " + closeSuccessMsg);

            waitLoaderForTrader();

            notification = null;
            waitWBNotificationDisplay();
            GlobalMethods.printDebugInfo("wbMessage: " + notification);

            waitOrder();
            if(StringUtils.isBlank(notification)){
                return crmMessage.equals(closeSuccessMsg);
            }
            return crmMessage.equals(closeSuccessMsg)
                    && wbMessage.equals(notification);
        } catch (Exception e) {
            LogUtils.info("get notify failed.");
        }
        return true;
    }

    public boolean updatePendingPositionPrice(){



        String stopL = getStopsLevel();
        String buyP = getBuyPrice();
        String digit = getDigitsValue();

        WebElement editIcon = findVisibleElemntBy(updatePendingPriceCell);
        actions.moveToElement(editIcon).click().perform();

        setLimitForPendingPosition(buyP,stopL,digit,false);

        waitLoaderForTrader();
        List<WebElement> trs = driver.findElements(updatePostionErrorMsg);
        GlobalMethods.printDebugInfo("updatePostionErrorMsg: " + trs.size());


        String modifylimitPriceErrorMsg = getTextSafe(stopLimit_limitPriceErrorMsg);


        GlobalMethods.printDebugInfo(" modifyLimitPriceErr: " + modifylimitPriceErrorMsg + ", Pending rows: " + trs.size());

        // 判断错误提示
        if ( trs.size()!=1 || !StringUtils.containsIgnoreCase(modifylimitPriceErrorMsg, "Max value")) {
            GlobalMethods.printDebugInfo("modify invalid limit price failed");
            return false;
        }

        setLimitForPendingPosition(buyP,stopL,digit,true);
        findVisibleElemntByXpath("//div[@aria-hidden='false']//button/span[normalize-space(.) = 'Confirm']").click();
        GlobalMethods.printDebugInfo("modify limit price success");


        waitOrder(3000);
        this.getPendingOrderInfo();
        return this.checkLatestPendingOrder("Buy Stop");

    }

    public boolean updateTakeProfitAndStopLoss(String orderType){
        String stopL = getStopsLevel();
        String digit = getDigitsValue();
        String sellP = getSellPrice();
        String buyP = getBuyPrice();
//        if(orderType.equalsIgnoreCase("Buy")) {
//            buyP = getBuyPrice();
//        }else {
//            sellP = getSellPrice();
//        }

        //double click first row, move tp/sl to view
        WebElement firstPosition = findVisibleElemntBy(firstRow_Position);
        actions.moveToElement(firstPosition).doubleClick().perform();

        for (int i = 0; i < 10; i++) {
            actions.sendKeys(Keys.ARROW_RIGHT).pause(200).perform();
        }

        //click tp/sl update
        WebElement editIcon = findVisibleElemntBy(updatePostionCell);
        actions.moveToElement(editIcon).click().perform();


        //modify position order - Exception
        if(orderType.equalsIgnoreCase("Buy")) {
            webTradeAccount.setEntryPrice(buyP);
            setTakeProfitForBuyPosition(sellP, stopL, digit,false);
            setStopLossForBuyPosition(sellP, stopL, digit,false);
            GlobalMethods.printDebugInfo("modify wrong tp/sl");
        }
        else {
            webTradeAccount.setEntryPrice(sellP);
            setTakeProfitForSellPosition(buyP, stopL, digit,false);
            setStopLossForSellPosition(buyP, stopL, digit,false);
            GlobalMethods.printDebugInfo("modify wrong tp/sl");
        }

        clickElement(driver.findElement(confirmPrice));

        waitLoaderForTrader();
        List<WebElement> trs = driver.findElements(updatePostionErrorMsg);
        GlobalMethods.printDebugInfo("updatePostionErrorMsg: " + trs.size());

        String verifyMaxTest = trs.get(0).getText();
        String verifyMinTest = trs.get(1).getText();

        //check Take profit, Stop loss both have error message
        if ( trs.size()!=2) {
            GlobalMethods.printDebugInfo("updatePostionErrorMsg failed");
            return false;
        }
        if(orderType.equalsIgnoreCase("Buy") && ( !StringUtils.containsIgnoreCase(verifyMinTest, "Max value")
                || !StringUtils.containsIgnoreCase(verifyMaxTest, "Min value"))) {
            return  false;
        }

        if(orderType.equalsIgnoreCase("Sell") && ( !StringUtils.containsIgnoreCase(verifyMinTest, "Min value")
                || !StringUtils.containsIgnoreCase(verifyMaxTest, "Max value"))) {
            return  false;
        }


        //modify take profit and stop loss for pending order
        if(orderType.equalsIgnoreCase("Buy")) {
            setTakeProfitForBuyPosition(sellP, stopL, digit,true);
            setStopLossForBuyPosition(sellP, stopL, digit,true);
        }else
        {
            setTakeProfitForSellPosition(buyP, stopL, digit,true);
            setStopLossForSellPosition(buyP, stopL, digit,true);
        }

        clickElement(driver.findElement(confirmPrice));

        waitOrder();
        getPositionInfo();
        return checkLatestPosition_Updated();

    }
    public void printWebTradAccount(WebTradeAccount acc) {
        System.out.println(" * Account Number: " + acc.accnum);
        System.out.println(" * OrderType: " + acc.orderType);
        System.out.println(" * StopPrice: " + acc.stopPrice);
        System.out.println(" * LimitPrice: " + acc.limitPrice);
        System.out.println(" * Symbol: " + acc.symbol);
        System.out.println(" * Digit: " + acc.digit);
        System.out.println(" * StopLevel: " + acc.stopLevel);
        System.out.println(" * EntryPrice: " + acc.entryPrice);
        System.out.println(" * Equity: " + acc.equity);
        System.out.println(" * Balance: " + acc.balance);
        System.out.println(" * Credit: " + acc.credit);
        System.out.println(" * Volume: " + acc.volume);
        System.out.println(" * OpenTime: " + acc.openTime);
        System.out.println(" * Volume: " + acc.volume);

    }

    public void waitLoader() {
        // Wait for deposit method content to load
        fastwait.until((ExpectedCondition<Boolean>) d -> {
            try{
                d.findElement(By.xpath("//div[@class='ht-loader']"));
            }catch(Exception e) {
                return true;
            }
            return false;
        });
        fastwait.until((ExpectedCondition<Boolean>) d -> {
            try{//loader
                d.findElement(By.xpath("//div[@class='price-wrapper']//div[@class='loader']"));
            }catch(Exception e) {
                return true;
            }
            return false;
        });
        fastwait.until((ExpectedCondition<Boolean>) d -> {
            try{
                d.findElement(By.xpath("//div[@class='chart-info']//div[@class=loader]"));
            }catch(Exception e) {
                return true;
            }
            return false;
        });
        LogUtils.info("Finish webTrade page loading");
    }

    public void waitListOrderLoader() {
        // Wait for deposit method content to load
        fastwait.until((ExpectedCondition<Boolean>) d -> {
            try{
                d.findElement(By.xpath("//div[@class='el-table-body-loading']"));
            }catch(Exception e) {
                return true;
            }
            return false;
        });
        LogUtils.info("Finish order list page loading");
    }


    //place order/receive notification need to wait for a few seconds
    private void waitOrder(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void waitOrder(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private <T> List<T> listWebElement(By rowLocator, Function<WebElement,T> mapper){
        List<T>  result = new ArrayList<>();
        List<WebElement> rows = driver.findElements(rowLocator);

        if (rows == null || rows.isEmpty()){
            GlobalMethods.printDebugInfo("No rows found.");
            return result;
        }

        for (WebElement row :rows){
            if (row == null){
                continue;
            }

            String info = row.getText();

            if (info == null || info.trim().isEmpty()){
                continue;
            }

            T obj = mapper.apply(row);
            if (obj != null){
                result.add(obj);
            }
        }

        return result;
    }
}
