package newcrm.cpapi;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.qameta.allure.*;
import newcrm.business.dbbusiness.EmailDB;
import newcrm.global.GlobalProperties;
import newcrm.utils.api.HyTechUtils;
import org.bouncycastle.jce.provider.PEMUtil;
import org.testng.annotations.Test;

import java.util.*;

import static newcrm.utils.api.HyTechUrl.*;
import static org.testng.Assert.*;

public class CPAPIWalletBase extends CPAPIBase{

    public CPAPIWalletBase(String url, String cplogin, String password) {
        super(url, cplogin, password);
    }
    public void setAPIHeader(String branchVersion){
        header.put("Branchversion",branchVersion);
    }
    @Test(dataProvider = "testData")
    public void open_account_validate() {
        header.put("Content-Type", "application/json");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(openaccountvalidate);
        printAPICPInfo(this.url+openaccountvalidate,"",result.toString());
        assertEquals(result.get("code"), 0, openaccountvalidate + " failed!! \n" + result);

   }

    @Test(dataProvider = "testData",description = "钱包帐户基本信息")
    public void base_info() {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(walletbaseinfo);
        printAPICPInfo(this.url+walletbaseinfo, "", result.toString());
        assertEquals(result.get("code"), 0, walletbaseinfo + " failed!! \n" + result);
    }

    @Test(dataProvider = "testData",description = "级别信息")
    public void level_info()
    {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(walletlevelinfo);
        printAPICPInfo(this.url+walletlevelinfo, "", result.toString());
        assertEquals(result.get("code"), 0, walletlevelinfo + " failed!! \n" + result);
    }

    @Test(dataProvider = "testData",description = "钱包帐户跟单详细")
    public void flow_detail()
    {
        header.put("Content-Type", "application/x-www-form-urlencoded");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(walletflowdetail);
        printAPICPInfo(this.url+walletflowdetail, "", result.toString());
        assertEquals(result.get("code"), 0, walletflowdetail + " failed!! \n" + result);
    }

    @Test(dataProvider = "testData",description = "资金流水列表页")
    public void flow_list()
    {
        cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject();
        JSONArray array1 = new JSONArray();
        JSONArray array2 = new JSONArray();
        jsonObject.putOnce("endTime",null);
        jsonObject.putOnce("startTime",null);
        jsonObject.putOnce("pageSize","10");
        jsonObject.putOnce("pageNum","1");
        jsonObject.putOnce("tradeTypeList","-1");
        jsonObject.putOnce("currencyList",array1);
        //jsonObject.putOnce("tradeTypeList",array2);

        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(walletFlowList,jsonObject.toString());
        printAPICPInfo(this.url+walletFlowList, jsonObject.toString(), result.toString());
        assertEquals(result.get("code"), 0, walletFlowList + " failed!! \n" + result);
    }

    @Test(dataProvider = "testData",description = "钱包帐户选择列表")
    public void select_list()
    {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(walletselectlist);
        printAPICPInfo(this.url+walletselectlist, "", result.toString());
        assertEquals(result.get("code"), 0, walletselectlist + " failed!! \n" + result);
    }

    @Test(dataProvider = "testData",description = "钱包出金详细")
    public void order_detail()
    {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(walletWithdrawOrderDetail);
        printAPICPInfo(this.url+walletWithdrawOrderDetail, "", result.toString());
        assertEquals(result.get("code"), 0, walletWithdrawOrderDetail + " failed!! \n" + result);
    }

    @Test(dataProvider = "testData",description = "钱包权限")
    public void user_permission()
    {
        header.put("Content-Type", "application/json");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(walletuserpermission);
        printAPICPInfo(this.url+walletuserpermission, "", result.toString());
        assertEquals(result.get("code"), 0, walletuserpermission + " failed!! \n" + result);
    }

    @Test(dataProvider = "testData",description = "钱包入金币种")
    public void getCurrencyChains()
    {
        header.put("Content-Type", "application/json");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(getCurrencyChains);
        printAPICPInfo(this.url+getCurrencyChains, "", result.toString());
        assertEquals(result.get("code"), 0, getCurrencyChains + " failed!! \n" + result);
    }

    @Test(dataProvider = "testData",description = "钱包入金币种H5")
    public void H5getCurrencyChains()
    {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(mobilegetCurrencyChains);
        printAPICPInfo(this.url+mobilegetCurrencyChains, "", result.toString());
        assertEquals(result.get("code"), 0, mobilegetCurrencyChains + " failed!! \n" + result);
    }

    @Test(dataProvider = "testData",description = "钱包入金地址")
    public void getDepositAddress(Map<String,Object> testData)
    {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(getDepositpcAddress);
        printAPICPInfo(this.url + getDepositpcAddress, "", result.toString());
        assertEquals(result.get("code"), 0, getDepositpcAddress + " failed!! \n" + result);
    }

    @Test(dataProvider = "testData",description = "钱包入金币种H5")
    public void mobilegetDepositAddress(Map<String,Object> testData)
    {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(getDepositAddress);
        printAPICPInfo(this.url + getDepositAddress, "", result.toString());
        assertEquals(result.get("code"), 0, getDepositAddress + " failed!! \n" + result);
    }

    @Test(dataProvider = "testData",description = "钱包入金记录详细")
    public void depositRecordDetail(Map<String,Object> testData)
    {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(Depositrecorddetail + testData.get("depositRecordID"));
        printAPICPInfo(this.url + Depositrecorddetail + testData.get("depositRecordID"), "", result.toString());
        assertEquals(result.get("code"), 0, Depositrecorddetail + testData.get("depositRecordID") + " failed!! \n" + result);
    }

    @Test(dataProvider = "testData",description = "钱包入金记录列表")
    public void depositRecordList(Map<String,Object> testData) {
        JSONObject result = null;
        cn.hutool.json.JSONObject jsonObject = null;
        try {
            jsonObject = new cn.hutool.json.JSONObject();
            jsonObject.putOnce("pageSize", "3");
            jsonObject.putOnce("pageNum", "1");

            HyTechUtils.genXSourceHeader(header);
            result = sendCPAPIrequest(DepositRecordList, jsonObject.toString());
            testData.put("depositRecordID", result.getJSONObject("data").getJSONArray("data").getJSONObject(0).getString("id"));

        } catch (Exception e) {
            fail(DepositRecordList + " failed!\n" + e.getMessage());
        }
        printAPICPInfo(this.url + DepositRecordList, jsonObject.toString(), result.toString());
        assertEquals(result.get("code"), 0, DepositRecordList + " failed!! \n" + result);
    }

    @Test(dataProvider = "testData",description = "钱包入金权限")
    public  void checkDepositPermissions()
    {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(checkDepositwebPermissions);
        printAPICPInfo(this.url + checkDepositwebPermissions, "", result.toString());
        assertEquals(result.get("code"), 0, checkDepositwebPermissions + " failed!! \n" + result);
    }

    @Test(dataProvider = "testData",description = "钱包入金权限H5")
    public  void mobilecheckDepositPermissions()
    {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(checkDepositPermissions);
        printAPICPInfo(this.url + checkDepositPermissions, "", result.toString());
        assertEquals(result.get("code"), 0, checkDepositPermissions + " failed!! \n" + result);
    }

    @Test(dataProvider = "testData",description = "钱包主页")
    @Step("wallethome")
    public void wallethome(){
        cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject();
        jsonObject.putOnce("fiatCurrency","USD");
        jsonObject.putOnce("cryptoCurrency","BTC");

        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(wallethome, jsonObject.toString());
        printAPICPInfo(this.url+wallethome,jsonObject.toString(),result.toString());
        assertEquals(result.get("code"), 0, wallethome + " failed!! \n" + result);
    }

    @Test(dataProvider = "testData",description = "钱包闪兑列表")
    public void get_conver_list(String fromCoin){
        cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject();
        jsonObject.putOnce("fromCoin",fromCoin);

        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(convertgetconverlist, jsonObject.toString());
        printAPICPInfo(this.url+convertgetconverlist,jsonObject.toString(),result.toString());
        assertEquals(result.get("code"), 0, convertgetconverlist + " failed!! \n" + result);
    }

    @Test(dataProvider = "testData",description = "钱包闪兑数量")
    public void get_quantity_and_exchange(String fromCoin, String toCoin, String amount){
        cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject();
        //兌出幣種
        jsonObject.putOnce("fromCoin",fromCoin);
        //兌入幣種
        jsonObject.putOnce("toCoin",toCoin);
        //請求報價幣種數量
        jsonObject.putOnce("fromCoinAmount",amount);

        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(convertquantityexchange, jsonObject.toString());
        printAPICPInfo(this.url+convertquantityexchange,jsonObject.toString(),result.toString());
        assertEquals(result.get("code"), 0, convertquantityexchange + " failed!! \n" + result);
    }

    @Test(dataProvider = "testData",description = "钱包闪兑提交")
    public void conver_submit(Map<String,Object> testData, String fromCoin, String toCoin, String amount) {
        cn.hutool.json.JSONObject jsonObject = null;
        JSONObject result = null;
        try {
            jsonObject = new cn.hutool.json.JSONObject();
            //兌出幣種
            jsonObject.putOnce("fromCoin", fromCoin);
            //兌入幣種
            jsonObject.putOnce("toCoin", toCoin);
            //請求報價幣種數量
            jsonObject.putOnce("requestAmount", amount);

            HyTechUtils.genXSourceHeader(header);
            result = sendCPAPIrequest(convertsubmit, jsonObject.toString());
            testData.put("tradeorder", result.getJSONObject("data").getString("tradeOrderNo"));
        } catch (Exception e) {
            fail(convertsubmit + " failed!\n" + e.getMessage());
        }
        printAPICPInfo(this.url + convertsubmit, jsonObject.toString(), result.toString());
        assertEquals(result.get("code"), 0, convertsubmit + " failed!! \n" + result);

    }

    @Test(dataProvider = "testData",description = "钱包闪兑刷新订单")
    public void refresh_order(Map<String,Object> testData){
        cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject();
        //订单号
        jsonObject.putOnce("tradeOrderNo",testData.get("tradeorder"));

        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(convertrefreshorder, jsonObject.toString());
        printAPICPInfo(this.url + convertrefreshorder, jsonObject.toString(), result.toString());
        assertEquals(result.get("code"), 0, convertrefreshorder + " failed!! \n" + result);
    }

    @Test(dataProvider = "testData",description = "钱包闪兑订单确认")
    public void confirm_order(Map<String,Object> testData){
        cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject();
        //订单号
        jsonObject.putOnce("tradeOrderNo",testData.get("tradeorder"));

        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(convertconfirmorder, jsonObject.toString());
        printAPICPInfo(this.url + convertconfirmorder, jsonObject.toString(), result.toString());
        assertEquals(result.get("code"), 0, convertconfirmorder + " failed!! \n" + result);
    }

    @Test(dataProvider = "testData",description = "钱包闪兑记录列表")
    public void record_list(){
        cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject();
        //页码数
        jsonObject.putOnce("pageSize","10");
        jsonObject.putOnce("tradeType","1");
        jsonObject.putOnce("pageNum","1");

        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(convertrecordlist, jsonObject.toString());
        printAPICPInfo(this.url + convertrecordlist, jsonObject.toString(), result.toString());
        assertEquals(result.get("code"), 0, convertrecordlist + " failed!! \n" + result);
    }

    @Test(dataProvider = "testData",description = "钱包导出PDF")
    public void export_pdf(){
        cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject();
        //页码数
        jsonObject.putOnce("pageSize","10");
        jsonObject.putOnce("tradeType","1");
        jsonObject.putOnce("pageNum","1");

        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(export_pdf, jsonObject.toString());
        printAPICPInfo(this.url + export_pdf, jsonObject.toString(), result.toString());
        assertEquals(result.get("code"), 0, export_pdf + " failed!! \n" + result);
    }

    @Test(dataProvider = "testData", description = "钱包出金黑名单")
    public void queryBlacklist() {
        header.put("Content-Type", "application/json");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(queryBlacklist);
        printAPICPInfo(this.url + queryBlacklist, "", result.toString());
        assertEquals(result.get("code"), 0, queryBlacklist + " failed!! \n" + result);
    }


    @Test(dataProvider = "testData",description = "钱包出金列表")
    public void order_list(Map<String,Object> testData){
        cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject();
        jsonObject.putOnce("pageSize",testData.get("3"));
        jsonObject.putOnce("pageNumber",testData.get("1"));

        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(withdraworder_list, jsonObject.toString());
        JSONObject data= result.getJSONObject("data");
        JSONObject datalist= (JSONObject) data.getJSONArray("data").get(0);
        String strwithdrawAmt=datalist.get("withdrawAmt").toString();
        testData.put("withdrawAmt",strwithdrawAmt);
        testData.put("currency",datalist.get("currency").toString());
        testData.put("status",datalist.get("status"));
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        printAPICPInfo(this.url+withdraworder_list, jsonObject.toString(), result.toString());
        assertEquals(result.get("code"), 0, withdraworder_list + " failed!! \n" + result);
    }


    @Test(dataProvider = "testData", description = "钱包crypto转换")
    public void fetch_crypto_fiat_exchange(Map<String, Object> testData) {
        cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject();
        jsonObject.putOnce("fiatCurrency", "USD");
        jsonObject.putOnce("cryptoCurrency", testData.get("currency").toString());
        JSONObject result = sendCPAPIrequest(withdrawfetch_crypto, jsonObject.toString());

        HyTechUtils.genXSourceHeader(header);
        printAPICPInfo(this.url + withdrawfetch_crypto, jsonObject.toString(), result.toString());
        assertEquals(result.get("code"), 0, withdrawfetch_crypto + " failed!! \n" + result);
    }

    @Test(dataProvider = "testData",description = "钱包出金汇率计算")
    public void calc_fee(Map<String,Object> testData,String withdrawAmt, String currency, String chainName){
        cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject();
        jsonObject.putOnce("withdrawAmt",withdrawAmt);
        jsonObject.putOnce("currency",currency);
        jsonObject.putOnce("chainName",chainName);

        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(withdrawcalc_fee, jsonObject.toString());
        printAPICPInfo(this.url + withdrawcalc_fee, jsonObject.toString(), result.toString());
        assertEquals(result.get("code"), 0, withdrawcalc_fee + " failed!! \n" + result);
    }

    @Test(dataProvider = "testData",description = "钱包出金币种列表")
    public void withdraw_currency_list(Map<String,Object> testData){
        String currencyAccountNo = "",balance="";
        cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject();
        jsonObject.putOnce("fiatCurrency", "USD");

        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(withdrawcurrency_list, jsonObject.toString());
        printAPICPInfo(this.url+withdrawcurrency_list, jsonObject.toString(), result.toString());
        assertEquals(result.get("code"), 0, withdrawcurrency_list + " failed!! \n" + result);

        JSONArray dataArray = result.getJSONArray("data");
        for (int i = 0; i < dataArray.size(); i++) {
            com.alibaba.fastjson.JSONObject item = dataArray.getJSONObject(i);
            if ("USDT".equals(item.getString("currency"))) {
                currencyAccountNo = item.getString("currencyAccountNo");
                balance = item.getString("balance");
                break;
            }
        }
        testData.put("currencyAccountNo", currencyAccountNo);

        double str = Double.parseDouble(balance);
        System.out.println(str);
        //Wallet deposit via openapi
        if (str < 500) {
            System.out.println("+++++++++++++++++++++++++++++");
            genSyncDepositRecords(testData);
        }
    }

    @Test(dataProvider = "testData",description = "钱包出金验证")
    public void order_verification(Map<String,Object> testData){
        JSONObject result = null;
        cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject();
        jsonObject.putOnce("currencyAccountNo", testData.get("currencyAccountNo"));
        jsonObject.putOnce("withdrawAmt", testData.get("withdrawAmt").toString());
        jsonObject.putOnce("currency", testData.get("currency").toString());
        jsonObject.putOnce("chainName", "TRC20");
        jsonObject.putOnce("realFee", "0");
        jsonObject.putOnce("estimateFee", "1.6000");
        jsonObject.putOnce("address", "TQETUR38cL8nY1gQcr43sWgFFwTKSwQjLw");
        jsonObject.putOnce("receiveAmt", testData.get("withdrawAmt").toString());
        jsonObject.putOnce("withdrawType", "127");
        jsonObject.putOnce("sceneType", "WEB");

        try {
            HyTechUtils.genXSourceHeader(header);
            result = sendCPAPIrequest(order_verification, jsonObject.toString());
            testData.put("sign", result.getJSONObject("data").getString("sign"));
        } catch (Exception e) {
            fail(order_verification +" failed!\n"+e.getMessage());
        }
        printAPICPInfo(this.url + order_verification, jsonObject.toString(), result.toString());
        assertEquals(result.get("code"), 0, order_verification + " failed!! \n" + result);
    }

    @Test(dataProvider = "testData",description = "钱包出金验证信息")
    public void getMultiAuthMethod()
    {
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(getMultiAuthMethod);
        printAPICPInfo(this.url + getMultiAuthMethod, "", result.toString());
        assertEquals(result.get("code"), 0, getMultiAuthMethod + " failed!! \n" + result);
    }

    @Test(dataProvider = "testData",description = "钱包出金确认")
    public void withdraw_order_confirm(Map<String,Object> testData){
        cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject();
        jsonObject.putOnce("withdrawAmt",testData.get("withdrawAmt").toString());
        jsonObject.putOnce("currency",testData.get("currency").toString());
        jsonObject.putOnce("chainName","TRC20");
        jsonObject.putOnce("currencyAccountNo",testData.get("currencyAccountNo"));
        jsonObject.putOnce("realFee","0");
        jsonObject.putOnce("estimateFee","1.6000");
        jsonObject.putOnce("address","TQETUR38cL8nY1gQcr43sWgFFwTKSwQjLw");
        jsonObject.putOnce("receiveAmt",testData.get("withdrawAmt").toString());
        jsonObject.putOnce("withdrawType","127");
        jsonObject.putOnce("sceneType","WEB");
        jsonObject.putOnce("sign",testData.get("sign").toString());

        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(withdraworder_confirm, jsonObject.toString());
        printAPICPInfo(this.url + withdraworder_confirm, jsonObject.toString(), result.toString());
        assertTrue(result.getJSONObject("data").containsKey("orderNo"), withdraworder_confirm +" failed to get orderNo!! \n" + result);
        assertEquals(result.get("code"), 0, withdraworder_confirm + " failed!! \n" + result);
    }

//    public String setCode(String email){
//        EmailDB emailDb = new EmailDB(GlobalProperties.ENV.getENV("alpha"), brand,regulator);
//        JSONObject obj = emailDb.getCodeRecord(GlobalProperties.ENV.getENV("alpha"), brand, regulator, email);
//        String code = obj.getJSONObject("vars").getString("CODE");
//        return code;
//    }

    @Test(dataProvider = "testData",description = "钱包账号充值mockup")
    public void genSyncDepositRecords(Map<String,Object> testData){
        JSONArray array = new JSONArray();
        cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject();
        jsonObject.putOnce("amount","500");
        jsonObject.putOnce("batchReleaseLimit","-1");
        jsonObject.putOnce("blockHash","0x51a58cedd266c8a358c3fa5eb75de8c8a49d22c379c63f29a0046247fc7e7f28");
        jsonObject.putOnce("chain","ETH");
        jsonObject.putOnce("coin","USDT");
        jsonObject.putOnce("confirmations","64");
        jsonObject.putOnce("depositFee","");
        jsonObject.putOnce("depositType","0");
        jsonObject.putOnce("status",3);
        jsonObject.putOnce("subMemberId","104531853");
        jsonObject.putOnce("successAt","1736328989000");
        jsonObject.putOnce("tag","");
        jsonObject.putOnce("toAddress","0x110e82b377de919296c40f51b1a8f88623538f86");
        jsonObject.putOnce("txID","0x03a42599ce2e734315b89312ae603919bcea9374085250d730d61eb0cb1a3c03061615test0407001");
        jsonObject.putOnce("txIndex","0");
        array.add(jsonObject);

        header.put("content-type","application/json;charset=UTF-8");
        header.put("Accept", "application/json, text/plain, */*");
         header.put("wallet-verify-key","KFCCrazyThursday");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result =  sendCPAPIrequest(genSyncDepositRecords +"?userId="+testData.get("userId"), array.toString());
        printAPICPInfo(this.url + genSyncDepositRecords +"?userId="+testData.get("userId"), array.toString(), result.toString());
        assertEquals(result.get("code"), 0, genSyncDepositRecords + " failed!! \n" + result);
    }

    @Test(dataProvider = "testData",description = "钱包转帐列表")
    public void wallet_transfer_list(){
        cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject();
        jsonObject.putOnce("pageNum","5");
        jsonObject.putOnce("pageSize","1");
        jsonObject.putOnce("startTime","");
        jsonObject.putOnce("endTime","");
        jsonObject.putOnce("status","0");
        jsonObject.putOnce("tradeOrderNo","");

        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(transfer_list, jsonObject.toString());
        printAPICPInfo(this.url + transfer_list, jsonObject.toString(), result.toString());
        assertEquals(result.get("code"), 0, transfer_list + " failed!! \n" + result);
    }

    @Test(dataProvider = "testData", description = "钱包防重验证token")
    public void anti_reuse() {
        header.put("Content-Type", "application/json");
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(anti_reuse);
        printAPICPInfo(this.url + anti_reuse, "", result.toString());
        assertEquals(result.get("code"), 0, anti_reuse + " failed!! \n" + result);
    }

    @Test(dataProvider = "testData",description = "钱包转帐")
    public void transfer_getTransferData_cp(Map<String,Object> testData){
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(getTransferData_cp,"");
        printAPICPInfo(this.url + getTransferData_cp, "", result.toString());
        testData.put("result",result); //to store client's transfer data
        assertEquals(result.get("code"), 0, getTransferData_cp + " failed!! \n" + result);
    }


    @Test(dataProvider = "testData",description = "钱包转帐")
    public void transfer_getWalletTransferLimit_cp(Map<String,Object> testData){
        LinkedHashMap<String, Object> fromCurrencyParams = new LinkedHashMap<>();
        fromCurrencyParams.put("fromCurrency", "USD");
        fromCurrencyParams.put("toCurrency", "ETH");
        fromCurrencyParams.put("isFromWalletAccount", "0");
        String req = HyTechUtils.mapToForm(fromCurrencyParams);

        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(getWalletTransferLimit_cp+req);
        printAPICPInfo(this.url + getWalletTransferLimit_cp, "", result.toString());
        assertEquals(result.get("code"), 0, getWalletTransferLimit_cp + " failed!! \n" + result);
    }

    @Test(dataProvider = "testData",description = "钱包转账账号的兑换率")
    public void transfer_getWalletExchangeRate_cp(Map<String,Object> testData){
        //Retrieve Transfer data's result body
        Map<String, Object> resultMap = (Map<String, Object>) testData.get("result");
        Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");

        //Get USDT Wallet account number & currency (with sufficient wallet balance)
        String walletCurrencyAccountNum = "";
        String walletCurrency = "";
        Integer userID = null;
        List<Map<String, Object>> wallets = (List<Map<String, Object>>) dataMap.get("cryptoWallets");
        for (int i = 0; i < wallets.size(); i++) {
            String wCurrency = (String) wallets.get(i).get("currency");
            if(wCurrency.equals("USDT")){
                walletCurrencyAccountNum = (String) wallets.get(i).get("walletCurrencyAccountNo");
                walletCurrency = (String) wallets.get(i).get("currency");
                userID = (Integer) wallets.get(i).get("userId");
            }
        }

        //Get first Trading account number & currency
        List<Map<String, Object>> logins = (List<Map<String, Object>>) dataMap.get("logins");
        Map<String, Object> firstTradAcc = logins.get(0);
        Object tradAccountNum = firstTradAcc.get("login");
        String tradAccCurrency = (String) firstTradAcc.get("currency");

        cn.hutool.json.JSONObject jbody = new cn.hutool.json.JSONObject();
        jbody.putOnce("fromAccount", walletCurrencyAccountNum);
        jbody.putOnce("fromAmount", "1"); //USDT minimum amount is 1
        jbody.putOnce("fromCurrency",walletCurrency);
        jbody.putOnce("isFromWalletAccount", "1");
        jbody.putOnce("toAccount", String.valueOf(tradAccountNum));
        jbody.putOnce("toCurrency", tradAccCurrency);
        jbody.putOnce("userId", String.valueOf(userID));

        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(getWalletExchangeRate_cp, jbody.toString());
        printAPICPInfo(this.url + getWalletExchangeRate_cp, jbody.toString(), result.toString());
        assertEquals(result.get("code"), 0, getWalletExchangeRate_cp + " failed!! \n" + result);
    }


    public void transfer_getAvailableWalletAndLogins(Map<String,Object> testData){
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(getPaymentTransferData_cp,"");
        printAPICPInfo(this.url + getPaymentTransferData_cp, "", result.toString());
        assertEquals(result.get("code"), 0, getPaymentTransferData_cp + " failed!! \n" + result);
        JSONObject data = result.getJSONObject("data");

        JSONArray wallets = data.getJSONArray("cryptoWallets");
        if(wallets == null || wallets.isEmpty()){
            return;
        }

        for (Object obj : wallets) {

            JSONObject js = (JSONObject) obj;

            Double balance = js.getDouble("availableBalance");

            if(balance > 0){
                testData.put("cryptoWallet",js);
                break;
            }

        }

        JSONArray loginArray = data.getJSONArray("logins");

        for (Object obj : loginArray) {

            JSONObject js = (JSONObject) obj;
            boolean isCredit = js.getBooleanValue("isCredit");

            if(isCredit){
                testData.put("login",js);
                break;
            }

        }
    }

    public JSONObject getTransferRate(String fromAccount,String fromAmount, String fromCurrency,String isFromWalletAccount,String toAccount,String toCurrency,String userId){


        JSONObject jbody = new JSONObject();
        jbody.put("fromAccount", fromAccount);
        jbody.put("fromAmount", fromAmount); //USDT minimum amount is 1
        jbody.put("fromCurrency",fromCurrency);
        jbody.put("isFromWalletAccount", isFromWalletAccount);
        jbody.put("toAccount", toAccount);
        jbody.put("toCurrency", toCurrency);
        jbody.put("userId", userId);

        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(walletTransferRateURL, jbody.toString());
        printAPICPInfo(this.url + walletTransferRateURL, jbody.toString(), result.toString());
        assertEquals(result.get("code"), 0, walletTransferRateURL + " failed!! \n" + result);
        return result;
    }

}
