package newcrm.testcases.cptestcases;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import newcrm.adminapi.AdminAPIPayment;
import newcrm.cpapi.CPAPIWithdraw;
import newcrm.cpapi.PCSAPIWithdraw;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.LogUtils;

import java.math.BigDecimal;
import java.util.Map;

import static newcrm.cpapi.CustomPaymentPayload.submitWDPayloadBuilder.*;
import static org.testng.Assert.assertTrue;
import static utils.CustomAssert.assertEquals;

public class ApiAuditWithdrawTestCases{

    protected Object[][] data;

    protected PCSAPIWithdraw pcswdapi;
    protected CPAPIWithdraw cpapi;
    protected AdminAPIPayment adminPaymentAPI;
    BigDecimal ccAmount = null;

    @Test(description = "同进同出：CC+LBT+other")
    public void apiInOutWithdrawal() throws Exception{

        int stateCount = 3;

//        pcswdapi.cancelSubmittedWithdrawals();

        JSONObject accInfo = adjustAccount();

        String account = accInfo.getString("account");
        String currency = accInfo.getString("currency");
        String userId = accInfo.getString("userId");

        BigDecimal amount = BigDecimal.valueOf(50);


        JSONArray ccArr = this.buildCC(accInfo);


//        boolean lbtSwitch = pcswdapi.apiLBTSwitch();


        // build bank body
        Map<String, String> bankDetails = pcswdapi.apiWDPCSChannelInfo(accInfo, "F00000", 5, 6);

        boolean lbtSwitch = Boolean.parseBoolean(bankDetails.get("isLbtLimited"));
        if(lbtSwitch){
            amount = BigDecimal.valueOf(55);
            stateCount = 2;
        }

        String bankBody = bankTranWDRequest(accInfo, bankDetails, amount);
        JSONArray bankArr = JSONArray.parseArray(bankBody);


        JSONArray body = new JSONArray();
        body.addAll(ccArr);
        body.addAll(bankArr);

        //build usdt body
        Map<String, String> cryptoDetail = null;
        if (!lbtSwitch){
            cryptoDetail = pcswdapi.apiWDPCSChannelInfo(accInfo, "F00000", 4, 87);
            String cryptoBody = cryptoWDRequest(accInfo,cryptoDetail, amount);
            JSONArray cryptoArr = JSONArray.parseArray(cryptoBody);
            body.addAll(cryptoArr);
        }

        pcswdapi.apiWDSubmitWithdraw(new Gson().toJson(body));

        String myrRate = pcswdapi.apiWDExchgRate(currency, "MYR");

         // 5  Accept
        Thread.sleep(30*1000);

        JSONObject jsWDAudit = adminPaymentAPI.apiWDAuditSearch(userId, account, "5");

        Assert.assertEquals((int) jsWDAudit.getInteger("total"), stateCount, "wrong count");
        JSONArray list = jsWDAudit.getJSONArray("rows");

        for (Object item: list){
            String id = ((JSONObject) item).getString("id");
            String channelDisplayName = ((JSONObject) item).getString("channelDisplayName");
            String currency1 = ((JSONObject) item).getString("currency");
            String rate = ((JSONObject) item).getString("rate");
            String withdrawType = ((JSONObject) item).getString("withdrawType");   //wdid
            String orderCurrency = ((JSONObject) item).getString("orderCurrency");   // detail wdActcurrencyCode
            BigDecimal actualAmount = ((JSONObject) item).getBigDecimal("actualAmount");
            if(!channelDisplayName.equalsIgnoreCase("Credit Card")) {
                assertEquals(actualAmount.compareTo(amount), 0, "wrong withdrawal amount");
            }else {
                assertEquals(actualAmount.compareTo(ccAmount), 0, "wrong withdrawal amount");
            }
            if(channelDisplayName.toLowerCase().contains("Bank Transfer")){
                assertEquals(Double.parseDouble(rate) , Double.parseDouble(myrRate),"wrong exchange rate");
            }

            assertTrue(currency1.equalsIgnoreCase(currency),"wrong currency");
            if(!channelDisplayName.equalsIgnoreCase("Credit Card")){
                assertTrue(orderCurrency.equalsIgnoreCase(bankDetails.get("wdActcurrencyCode")) || orderCurrency.equalsIgnoreCase(cryptoDetail.get("wdActcurrencyCode")), "wrong actual currency");
            }
//            if(!lbtSwitch){
//                assertTrue(orderCurrency.equalsIgnoreCase(bankDetails.get("wdActcurrencyCode")) || orderCurrency.equalsIgnoreCase(cryptoDetail.get("wdActcurrencyCode")), "wrong actual currency");
//            }
        }
        pcswdapi.printUserFundsInfo(GlobalMethods.getBrand(), accInfo.getString("account"), accInfo.getString("currency"), "Withdraw", "Credit Card/LBT/crypto" ,"");
    }

    @Test(description = "VJP 同进同出：CC+other")
    public void apiVJPInOutWithdrawal() throws Exception{

        JSONObject accInfo = adjustAccount();

        String account = accInfo.getString("account");
        String currency = accInfo.getString("currency");
        String userId = accInfo.getString("userId");

        BigDecimal amount = BigDecimal.valueOf(50);


        JSONArray ccArr = this.buildCC(accInfo);
        Map<String, String> cryptoDetail = pcswdapi.apiWDPCSChannelInfo(accInfo, "F00000", 4, 87);
        String cryptoBody = cryptoWDRequest(accInfo,cryptoDetail, amount);
        JSONArray cryptoArr = JSONArray.parseArray(cryptoBody);

        JSONArray body = new JSONArray();
        body.addAll(ccArr);
        body.addAll(cryptoArr);

        pcswdapi.apiWDSubmitWithdraw(new Gson().toJson(body));

        // 7  complete, VT 自动出金
        JSONObject jsWDComplete = adminPaymentAPI.apiWDAuditSearch(userId, account, "7");
        JSONObject jsWDAccept = adminPaymentAPI.apiWDAuditSearch(userId, account, "5");

        JSONObject ccDetail = (JSONObject)jsWDComplete.getJSONArray("rows").get(0);

        checkVJP(ccDetail,ccAmount,currency,null);
        
        JSONObject usdtDetail = (JSONObject)jsWDAccept.getJSONArray("rows").get(0);

        checkVJP(usdtDetail,amount,currency,cryptoDetail.get("wdActcurrencyCode"));
        pcswdapi.printUserFundsInfo(GlobalMethods.getBrand(), account, currency, "Withdraw", "Credit Card/crypto" ,"");
    }
    
    private void checkVJP(JSONObject detail,BigDecimal amount,String currency,String actCode){
        String id = detail.getString("id");
        String channelDisplayName = detail.getString("channelDisplayName");
        String currency1 = detail.getString("currency");
        String withdrawType = detail.getString("withdrawType");   //wdid
        String orderCurrency = detail.getString("orderCurrency");   // detail wdActcurrencyCode
        BigDecimal actualAmount = detail.getBigDecimal("actualAmount");
//        assertEquals(actualAmount.compareTo(amount) , 0,"wrong withdrawal amount");
        if(!channelDisplayName.equalsIgnoreCase("Credit Card")) {
            assertEquals(actualAmount.compareTo(amount), 0, "wrong withdrawal amount");
        }else {
            assertEquals(actualAmount.compareTo(amount), 0, "wrong withdrawal amount");
        }
        assertTrue(currency1.equalsIgnoreCase(currency),"wrong currency");
        if(!channelDisplayName.equalsIgnoreCase("Credit Card")){
            assertTrue( orderCurrency.equalsIgnoreCase(actCode), "wrong actual currency");
        }
    }

    private JSONArray buildCC(JSONObject accInfo){
        String currency = accInfo.getString("currency");
        JSONArray js = pcswdapi.apiWDCCBalanceInfo(currency);

        if(js == null || js.isEmpty()){
            JSONObject ccOrderInfo = adminPaymentAPI.apiCCTransctionQueryCC(accInfo.getString("userId"));
            adminPaymentAPI.apiCCTranscInsertAdjustment(ccOrderInfo, "50");
            ccAmount = BigDecimal.valueOf(50);
        }else {
            ccAmount = js.getJSONObject(0).getBigDecimal("balance");
        }

        JSONObject ccDetails = pcswdapi.apiWDCCBalanceInfo(currency, ccAmount);
//        BigDecimal creditBalance = ccDetails.getBigDecimal("balance");

        // if CC balance is 0, adjust it
//        if (creditBalance.compareTo(BigDecimal.valueOf(0)) ==0){
//            JSONObject ccOrderInfo = adminPaymentAPI.apiCCTransctionQueryCC(accInfo.getString("userId"));
//            adminPaymentAPI.apiCCTranscInsertAdjustment(ccOrderInfo, "50");
//            ccDetails = pcswdapi.apiWDCCBalanceInfo(currency, amount);
//        }

        String CCbody = creditCardWDRequest(accInfo, ccDetails, ccAmount);

        return JSONArray.parseArray(CCbody);
    }

    private JSONObject adjustAccount(){
        JSONObject accInfo = pcswdapi.queryMetaTraderAccountForWithdraw();
        LogUtils.info("accInfo: " + accInfo);

        double acctBalance = Double.parseDouble(accInfo.getString("balance"));

        if(acctBalance < 500){
            adminPaymentAPI.cashAdjustment( accInfo.getString("account"),"500","1", accInfo.getString("currency"));
        }

        return accInfo;

    }




//    protected void initBase() {
//        menu = myfactor.newInstance(CPMenu.class);
//        login.goToCpHome();
//        menu.goToMenu(GlobalProperties.CPMenuName.CPPORTAL);
//        menu.changeLanguage("English");
//        menu.goToMenu(GlobalProperties.CPMenuName.HOME);
//    }
//
//    public void test(){
//
//        menu.goToMenu(GlobalProperties.CPMenuName.WITHDRAWFUNDS);
//
//        CPWithdrawLimit cpWithdrawLimit = myfactor.newInstance(CPWithdrawLimit.class);
//        CPSkrillWithdraw instance = myfactor.newInstance(CPSkrillWithdraw.class);
//
//        // Get valid account
//        WithdrawBasePage.Account accSelected = instance.getValidAccount();
//        assertNotNull(accSelected, "No available account found");
//
//        String account = accSelected.getAccNumber();
//        double balance = Double.parseDouble(accSelected.getBalance());
//
//        // get balance and check if balance less than 100, then make cash adjustment
//        //checkBalanceAndCashAdjustment(account, acctCurrency, balance);
//        Assert.assertTrue((int)balance > 200, "Balance of account "+ account +" is less than 200!");
//        //withdrawal amount
//        double amount = 150;
//
//        instance.setAccountAndAmountNew(account, amount);
//
//        instance.clickContinue();
//        cpWithdrawLimit.clickCreditOkBtn();
//
//
//    }
}
