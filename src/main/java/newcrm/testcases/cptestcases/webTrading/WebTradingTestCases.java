package newcrm.testcases.cptestcases.webTrading;

import newcrm.business.businessbase.CPMenu;
import newcrm.business.businessbase.webTrading.CPWebTrading;
import newcrm.factor.Factor;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.testcases.BaseTestCaseNew;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

public class WebTradingTestCases extends BaseTestCaseNew {
    public static String symbol = "AUDUSD";
    public static String volume = "0.01";
    CPWebTrading cpWebTrading = null;
    private Factor myfactor;
    @BeforeMethod(alwaysRun = true)
    public void initMethod(){
        if (myfactor == null){
            myfactor = getFactorNew();
        }
    }

    public void testOpenUpdateCloseBuyPosition(String webTradingAccount) {
        testOpenUpdateClosePosition(webTradingAccount, "Buy");
    }

    public void testPositionCloseAll(String webTradingAccount) {
        testPositionCloseAll(webTradingAccount, "Buy");
    }

    public void testOpenUpdateCloseSellPosition(String webTradingAccount) {
        testOpenUpdateClosePosition(webTradingAccount, "Sell");
    }

    public void testPendingPosition_BuyStopLimit(String webTradingAccount) {
        testPendingPositionCommon(webTradingAccount, "Buy Stop Limit");
    }

    public void testPendingPosition_SellStopLimit(String webTradingAccount) {
        testPendingPositionCommon(webTradingAccount, "Sell Stop Limit");
    }

    public void testPendingPosition_BuyStop(String webTradingAccount) {
        testPendingPositionCommon(webTradingAccount, "Buy Stop");
    }

    public void testPendingPosition_SellStop(String webTradingAccount) {
        testPendingPositionCommon(webTradingAccount, "Sell Stop");
    }

    public void testPendingPosition_BuyLimit(String webTradingAccount) {
        testPendingPositionCommon(webTradingAccount, "Buy Limit");
    }

    public void testPendingPosition_SellLimit(String webTradingAccount) {
        testPendingPositionCommon(webTradingAccount, "Sell Limit");
    }

    protected void testPendingPositionCommon(String webTradingAccount, String orderType) {

        CPMenu menu = myfactor.newInstance(CPMenu.class);
        cpWebTrading = myfactor.newInstance(CPWebTrading.class);

        // Go to trade page
        if (!cpWebTrading.isTradePage()) {
            cpWebTrading.refresh();
            String currentTab = cpWebTrading.getCurrentTab();
            menu.goToMenu(GlobalProperties.CPMenuName.TRADE);
            cpWebTrading.selectAccount(currentTab, webTradingAccount,menu);
        }
        cpWebTrading.refresh();
        // Select symbol and fetch config
        cpWebTrading.selectSymbol(symbol);
        cpWebTrading.getStopLevel();
        cpWebTrading.getDigitsValue();

        // Determine which invalid method to use
        boolean invalidResult;
        if (orderType.contains("Limit") && orderType.contains("Stop")) {
            invalidResult = cpWebTrading.placeInvalidStopLimit(orderType, volume);
        } else if (orderType.contains("Limit")) {
            invalidResult = cpWebTrading.placeInvalidLimit(orderType, volume);
        } else {
            invalidResult = cpWebTrading.placeInvalidStop(orderType, volume);
        }

        Assert.assertTrue(invalidResult, "Invalid price check failed for: " + orderType);

        // Place valid pending order and verify notification
        Assert.assertTrue(
                cpWebTrading.placePendingOrderAndVerifyNotification(orderType, volume, symbol),
                "Order notification details check failed for: " + orderType
        );

        // Check pending order
        cpWebTrading.getPendingOrderInfo();
        Assert.assertTrue(cpWebTrading.checkLatestPendingOrder(orderType), "Check pending info failed for: " + orderType);


        if(orderType.equalsIgnoreCase("Buy Limit")){
            Assert.assertTrue(cpWebTrading.updatePendingPositionPrice(),"update limit price for buy limit pending order failed");
        }

        // Cancel order and verify
        Assert.assertTrue(cpWebTrading.cancelPendingOrdersAndVerifyNotification(), "Cancel order verification failed");
        Assert.assertTrue(cpWebTrading.checkCancelPendingOrder(), "verify pending list failed after Cancel order");
    }

    protected void testOpenUpdateClosePosition(String webTradingAccount, String orderType) {
        //Open a position and verify that the position was successfully created
        Assert.assertTrue(openAndCheckPosition(webTradingAccount, orderType),
                orderType.toLowerCase() + " order and check position failed");

        //Update take profit and stop loss values, then verify that the update was successful
        Assert.assertTrue(cpWebTrading.updateTakeProfitAndStopLoss(orderType),
                "update take profit and stop loss for " + orderType.toLowerCase() + " position failed");

        //Close the position and verify that the close notification is displayed correctly
        Assert.assertTrue(cpWebTrading.closeOrderAndVerifyNotification(),
                "close and check close notification failed");

        cpWebTrading.getPositionHistoryInfo();

        Assert.assertTrue(cpWebTrading.verifyPositionAndHisTabAfterClose(),"verify position tab and history failed");


    }

    protected void testPositionCloseAll(String webTradingAccount, String orderType) {
        //Open a position and verify that the position was successfully created
        Assert.assertTrue(openAndCheckPosition(webTradingAccount, orderType),
                orderType.toLowerCase() + " order and check position failed");

        Assert.assertTrue(openAndCheckPosition(webTradingAccount, "Sell"),
                orderType.toLowerCase() + " order and check position failed");


        //Close the position and verify that the close notification is displayed correctly
        Assert.assertTrue(cpWebTrading.closeAllOrderAndVerifyNotification(),
                "close all and check close all position notification failed");

//        // check position tab and history tab
        cpWebTrading.getPositionHistoryInfo();

        Assert.assertTrue(cpWebTrading.checkPositionHisAfterCloseAll(),"positon is not empty after click close all button");

        Assert.assertTrue(cpWebTrading.checkPositionClosed(),"positon is not empty after click close all button");

    }


    //反向开仓
    public void testPositions_reverse(String webTradingAccount) {
        //Open a position and verify that the position was successfully created
        Assert.assertTrue(openAndCheckPosition(webTradingAccount, "Buy"),
                 "Buy  order and check position failed");

        Assert.assertTrue(cpWebTrading.reverseOrderAndVerifyNotification(),
                 "reverse order failed");

        //Check position, buy order is reverse to sell order
        cpWebTrading.getPositionInfo();

        Assert.assertTrue(cpWebTrading.checkLatestPosition(),
                "reverse to sell order failed");

        cpWebTrading.getPositionHistoryInfo();
//
        Assert.assertTrue(cpWebTrading.verifyOrderAfterReverse(),"verify position history order failed after reverse order");

    }


    // 互抵平仓
    public void testPositions_closeBy(String webTradingAccount) {
        //Open a position and verify that the position was successfully created
        Assert.assertTrue(openAndCheckPosition(webTradingAccount, "Buy"),
                "Buy  order and check position failed");

        Assert.assertTrue(openAndCheckPosition(webTradingAccount, "Sell"),
                "Buy order and check position failed");

        cpWebTrading.getPositionInfo();
        Assert.assertTrue(cpWebTrading.closeByOrderAndVerifyNotification(),
                "reverse order failed");

        cpWebTrading.getPositionHistoryInfo();

        Assert.assertTrue(cpWebTrading.verifyPositionHisAfterCloseBy(),"verify position tab and history failed after close by position");
    }

    public void test_chartInfoInterval(String webTradingAccount){
        CPMenu menu = myfactor.newInstance(CPMenu.class);
        cpWebTrading = myfactor.newInstance(CPWebTrading.class);

        //Go to trade tab
        if (!cpWebTrading.isTradePage()) {
            cpWebTrading.refresh();
            String currentTab = cpWebTrading.getCurrentTab();
            menu.goToMenu(GlobalProperties.CPMenuName.TRADE);
            cpWebTrading.selectAccount(currentTab, webTradingAccount,menu);

        }
        cpWebTrading.refresh();
        //Select symbol
        cpWebTrading.selectSymbol(symbol);

        Assert.assertTrue(cpWebTrading.checkChartInfo(),"verify chart info failed");
    }

    protected boolean openAndCheckPosition(String webTradingAccount, String orderType) {

        CPMenu menu = myfactor.newInstance(CPMenu.class);
        cpWebTrading = myfactor.newInstance(CPWebTrading.class);

        //Go to trade tab
        if (!cpWebTrading.isTradePage()) {
            cpWebTrading.refresh();
            String currentTab = cpWebTrading.getCurrentTab();
            menu.goToMenu(GlobalProperties.CPMenuName.TRADE);
            cpWebTrading.selectAccount(currentTab, webTradingAccount,menu);

        }
        cpWebTrading.refresh();
        //Select symbol
        cpWebTrading.selectSymbol(symbol);

        //Check open oder notification
        if (!cpWebTrading.placeOrderAndVerifyNotification(orderType, volume, symbol)) {
            GlobalMethods.printDebugInfo("place order and verify notification failed");
            return false;
        }

        //Check position
        cpWebTrading.getPositionInfo();
        return cpWebTrading.checkLatestPosition();
    }
}
