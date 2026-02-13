package newcrm.business.businessbase.webTrading;

import newcrm.business.businessbase.CPMenu;
import newcrm.pages.clientpages.webTrade.WebTradePage;
import org.openqa.selenium.WebDriver;

public class CPWebTrading {

    protected WebTradePage webTradePage;

    public CPWebTrading(WebDriver driver)
    {
        webTradePage = new WebTradePage(driver);
    }
    public boolean isTradePage()
    {
        return webTradePage.getCurrentURL().contains("web-trade/trade");
    }
    public String getCurrentTab()
    {
        return webTradePage.getCurrentTab();
    }

    public String getBuyPrice()
    {
        return webTradePage.getBuyPrice();
    }
    public String getSellPrice()
    {
        return webTradePage.getSellPrice();
    }
    public String getStopPrice()
    {
        return webTradePage.getStopPrice();
    }

    public String getLimitPrice()
    {
        return webTradePage.getLimitPrice();
    }
    public void selectAccount(String originalTab, String account, CPMenu menu)
    {
        webTradePage.selectAccount(originalTab,account,menu);
    }

    public void selectSymbol(String symbol)
    {
        webTradePage.selectSymbol(symbol);
    }

    public boolean placeOrderAndVerifyNotification(String orderType, String volume,String symbol)
    {
        return webTradePage.placeOrderAndVerifyNotification(orderType, volume,symbol);
    }
    public boolean placePendingOrderAndVerifyNotification(String orderType, String volume, String symbol)
    {
        return webTradePage.placePendingOrderAndVerifyNotification(orderType, volume,symbol);
    }
    public boolean placeInvalidStopLimit(String orderType, String volume)
    {
        return webTradePage.placeInvalidPrice(orderType, volume);
    }

    public boolean placeInvalidStop(String orderType, String volume)
    {
        return webTradePage.placeInvalidPrice(orderType, volume);
    }

    public boolean placeInvalidLimit(String orderType, String volume)
    {
        return webTradePage.placeInvalidPrice(orderType, volume);
    }

    public void getStopLevel()
    {
        webTradePage.getStopsLevel();
    }

    public void getDigitsValue()
    {
        webTradePage.getDigitsValue();
    }
    public boolean closeOrderAndVerifyNotification()
    {
        return webTradePage.closeOrderAndVerifyNotification();
    }

    public boolean verifyPositionAndHisTabAfterClose(){
        return webTradePage.verifyPositionTabAndHisAfterClose();
    }

    public boolean verifyOrderAfterReverse(){
        return webTradePage.verifyOrderAfterReverse();
    }

    public boolean verifyPositionHisAfterCloseBy(){
        return webTradePage.verifyPositionHisAfterCloseBy();
    }

    public boolean closeAllOrderAndVerifyNotification()
    {
        return webTradePage.closeAllOrderAndVerifyNotification();
    }

    public boolean checkPositionClosed()
    {
        return webTradePage.getPositionInfo().isEmpty();
    }


    public boolean cancelPendingOrdersAndVerifyNotification()
    {
        return webTradePage.cancelPendingOrderAndVerifyNotification();
    }

    public boolean updateTakeProfitAndStopLoss(String orderType){
        return webTradePage.updateTakeProfitAndStopLoss(orderType);
    }
    public boolean updatePendingPositionPrice(){
        return webTradePage.updatePendingPositionPrice();
    }
    public void getPositionInfo()
    {
        webTradePage.getPositionInfo();
    }
    public void getPositionHistoryInfo(){
        webTradePage.getPositionHistoryInfo(webTradePage);
    }

    public boolean checkPositionHisAfterCloseAll(){
        return webTradePage.checkPositionHisAfterCloseAll();
    }

    public boolean checkLatestPosition()
    {
        return webTradePage.checkLatestPosition();
    }
    public void getPendingOrderInfo()
    {
        webTradePage.getPendingOrderInfo();
    }

    public boolean checkLatestPendingOrder(String orderType)
    {
        return webTradePage.checkLatestPendingOrder(orderType);
    }
    public boolean checkCancelPendingOrder(){
        return webTradePage.checkCancelPendingOrder();
    }

    public boolean reverseOrderAndVerifyNotification(){
        return webTradePage.reverseOrderAndVerifyNotification();
    }

    public boolean closeByOrderAndVerifyNotification(){
        return webTradePage.closeByOrderAndVerifyNotification();
    }
    public void refresh(){
        webTradePage.refresh();
    }

    public boolean checkChartInfo(){
        return webTradePage.checkChartInfo();
    }

}
