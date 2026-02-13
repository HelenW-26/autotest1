package newcrm.cpapi;

import newcrm.utils.api.HyTechUrl;
import newcrm.utils.api.HyTechUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import newcrm.business.dbbusiness.EmailDB;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.REGULATOR;
import org.testng.SkipException;
import utils.CustomAssert;
import utils.LogUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

import static org.testng.Assert.*;

public class CPAPIWithdraw extends CPAPIPaymentBase{
    protected static ENV dbenv = ENV.ALPHA;
    protected static BRAND dbBrand;
    protected static REGULATOR dbRegulator;
    public CPAPIWithdraw cpapi;

    public CPAPIWithdraw(String url, String cplogin, String password) {
        super(url, cplogin, password);

        //Cancel all submitted withdrawals
        if (!this.url.contains("ib")) {
            cancelSubmittedWithdrawals();
            GlobalMethods.printDebugInfo("Cancel all submitted withdrawals success");
            LogUtils.info("Cancel all submitted withdrawals success");
        } else {
            try {
                apiCancelWithdrawTransactionIB();
                GlobalMethods.printDebugInfo("Cancel all ib submitted withdrawals success");
                LogUtils.info("Cancel all ib submitted withdrawals success");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    /* Cancel submitted withdrawals on the first page
     * (pageSize is 25) in transaction history.
     * */
    public int cancelSubmittedWithdrawals(){
        String fullPath = this.url +  HyTechUrl.WD_TRANS_HISTORY;

        header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        HyTechUtils.genXSourceHeader(header);

        String body = "pageNo=1&pageSize=50";

        // 添加统计变量
        int cancelledCount = 0;
        List<String> cancelledIds = new ArrayList<>();

        try {
            HttpResponse response = httpClient.getPostResponse(fullPath, header, body);
            String responseBody = EntityUtils.toString(response.getEntity(),"UTF-8");
            LogUtils.info("Query withdraw history response:\n " + responseBody);
            JSONObject message = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
            Integer rescode = message.getInteger("code");
            LogUtils.info("rescode: " + rescode);

            if(rescode.equals(0)) {
                JSONArray history =  message.getJSONObject("data").getJSONArray("withdrawHistory");
                for (Object item : history) {
                    String id = ((JSONObject) item).getString("id");
                    String status = ((JSONObject) item).getString("status");
                    String method = ((JSONObject) item).getString("method");
                    Boolean processedByOp = ((JSONObject) item).getBoolean("processedByOp");
                    String processedNotes = ((JSONObject) item).getString("processedNotes");
                    //status 5 = accepted status 1 = submitted  status =3 is unionpay submitted
                    if (((("5".equals(status) || "1".equals(status)) && processedByOp == null && (processedNotes == null || !processedNotes.contains("Cancelled"))) || "3".equals(status)))//Submitted and not be claimed
                    {
                        //Send cancel withdrawal API by id
                        apiWDsendWithdrawCancel(id,method);
                        // 统计取消的条数和ID
                        cancelledCount++;
                        cancelledIds.add(id);
                    }
                }

                // 记录统计信息
                LogUtils.info("Total cancelled withdrawals: " + cancelledCount);
                LogUtils.info("Cancelled withdrawal IDs: " + String.join(", ", cancelledIds));

            }else {
                System.out.println("!!!Query withdraw history failed.");
            }
        }catch(Exception e) {
//			System.out.println("ERROR!!!Query withdraw history failed.");
            e.printStackTrace();
        }

        return cancelledCount;
    }



    /* Cancel withdrawal by or_id.
     * @parameter or_id - withdrawal order id
     * */
    private void apiWDsendWithdrawCancel(String or_id,String payment_type) {
        String path = HyTechUrl.WD_CANCEL_URL+ or_id+ "&paymentWithdrawType=" +payment_type;

        HyTechUtils.genXSourceHeader(header);

        try {
            JSONObject result = sendCPAPIrequest(path, "");
            Integer rescode = result.getInteger("code");
            JSONObject data = result.getJSONObject("data");
            System.out.println("Going to cancel withdrawal: "+data.getString("id"));

            GlobalMethods.printDebugInfo("rescode.equals(0): " + rescode.equals(0) + " or_id: " + or_id + " data.getString(\"status\"): " + data.getString("status"));
            //Status 11 = accepted to  cancelled and  Status 2/4 = submited to cancelled
            if(!Objects.equals(data.getString("status"), "7")){
                CustomAssert.assertTrue(rescode.equals(0) && data.getString("id").equals(or_id) &&
                        (data.getString("status").equals("11")||data.getString("status").equals("2")||data.getString("status").equals("4")), "Withdraw cancellation failed!\n" + result);
                CustomAssert.assertAll();
            }

        } catch (Exception e) {
            System.out.println("Withdraw cancellation failed! ID: "+or_id);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void apiCancelWithdrawTransactionIB() throws Exception {
        //Get all IB rebate accounts
        JSONObject rebateData = null;
        try {
            rebateData = apiQueryRebateList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        JSONArray rebateAccountList = rebateData.getJSONObject("data").getJSONArray("list");
        //Get rebate withdraw history by each IB rebate accounts, proceed cancellation
        for (int i = 0; i < rebateAccountList.size(); i++) {
            JSONObject accInfo = rebateAccountList.getJSONObject(i);
            String accountNum = accInfo.getString("login");
            apiRebateHistoryAndCancel(accountNum);
        }
    }

    //Retrieve withdrawal history record for specified account and cancel record with Submit status
    public void apiRebateHistoryAndCancel(String rebateAcc) throws Exception {
        String fullPath = this.url + HyTechUrl.IBWD_REBATE_HISTORY;
        header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        HyTechUtils.genXSourceHeader(header);

        Map<String,String> body = new HashMap();
        body.put("pageNo","1");
        body.put("limit","25");
        body.put("qUserId","0");
        body.put("qAccount",rebateAcc);
        body.put("qStartTime","2025-04-01 00:00:00");
        body.put("qEndTime","2099-04-18 23:59:59");

        try {
            HttpResponse response = httpClient.getPostResponse(fullPath, header, body);
            JSONObject message = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
            Integer rescode = message.getInteger("code");

            if(rescode.equals(0)) {
                JSONArray history =  message.getJSONArray("data");
                for (Object item : history) {
                    String id = ((JSONObject) item).getString("id");
                    String status = ((JSONObject) item).getString("status");
                    String method = ((JSONObject) item).getString("destination");
                    Boolean processedByOp = ((JSONObject) item).getBoolean("processedByOp");
                    //status 5 = accepted status 1 = submitted  status =3 is unionpay submitted
                    if (((status.equals("5") || status.equals("1")) && processedByOp==null)|| status.equals("3"))//Submitted and not be claimed
                    {
                        //Send cancel withdrawal API by id
                        apiWDsendWithdrawCancelIB(id,method);
                    }
                }
            }else {
                System.out.println("!!!Query IB rebate withdraw history failed.");
            }
        }catch(Exception e) {
            System.out.println("!!!Query IB rebate withdraw history failed.");
            //e.printStackTrace();
        }
    }

    //Cancel the specified withdrawal record
    private void apiWDsendWithdrawCancelIB(String or_id,String payment_type) {
        String path = HyTechUrl.WD_IB_CANCEL_URL+or_id+ "&paymentWithdrawType=" +payment_type;

        try {
            HyTechUtils.genXSourceHeader(header);
            JSONObject result = sendCPAPIrequest(path, "");
            Integer rescode = result.getInteger("code");
            JSONObject data = result.getJSONObject("data");
            System.out.println("Cancel rebate withdrawal id: "+data.getString("id"));

            //Status 11 = accepted to  cancelled and  Status 2/4 = submited to cancelled
            assertTrue(rescode.equals(0) && data.getString("id").equals(or_id) &&
                    (data.getString("status").equals("11")||data.getString("status").equals("2")||data.getString("status").equals("4")), "Withdraw cancellation failed!\n" + result);

        } catch (Exception e) {
            System.out.println("Withdraw cancellation failed! withdrawal id: "+or_id);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    // Return the first non-archived trading account that has enough balance for withdrawal.
    public JSONObject queryMetaTraderAccountForWithdraw() {
        String fullPath = this.url + "web-api/cp/api/home/query-metaTrader-account-details";
        header.put("Content-Type", "application/json");
        HyTechUtils.genXSourceHeader(header);
//		String body = "{\"hiddenMtAccountIdList\":[],\"unHide\":false,\"platform\":null}";
        String body = "{\"platform\":null}";

        LogUtils.info("queryMetaTraderAccountForWithdraw: " + fullPath+ "\n"+ body);
        JSONObject accInfo = new JSONObject();
        try {
            JSONArray data = null;
            String lastBody = null;

            for (int attempt = 1; attempt <= 3; attempt++) {
                HttpResponse response = httpClient.getPostResponse(fullPath, header, body);
                lastBody = EntityUtils.toString(response.getEntity(), java.nio.charset.StandardCharsets.UTF_8);


                data = JSONArray.parseArray(lastBody);
                LogUtils.info("query-metaTrader-account-details response: " + data);
                if (data != null && !data.isEmpty()) {
                    break; // success
                }
                LogUtils.info("queryMetaTraderAccountForWithdraw (attempt " + attempt + "/" + 3 + "): " + lastBody);


                if (attempt < 3) {
                    LogUtils.info("API returned empty array, retrying in 5s...");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }

            assertFalse(data == null || data.isEmpty(),
                    "API response is empty after " + 3 + " attempts.\nLast body:\n" + lastBody);

            JSONArray activeAcctList = new JSONArray();
            for (Object item : data) {
                String isArchive = ((JSONObject) item).getString("isArchive") == null ? "" : ((JSONObject) item).getString("isArchive"); //to handle null value
                String currency = ((JSONObject) item).getString("currency");
                double equity = ((JSONObject) item).getDoubleValue("equity");
                double credit = ((JSONObject) item).getDoubleValue("credit");
                //	double balance = equity - credit;
                double balance = ((JSONObject) item).getDoubleValue("balance");
                accInfo.put("userId", ((JSONObject) item).getString("user_id"));

                if (isArchive.equals("0") && !currency.equals("USC") && balance >= 200.0) {
                    //Get non-USC account
                    accInfo.put("account", ((JSONObject) item).getString("mt4_account"));
                    accInfo.put("currency", ((JSONObject) item).getString("currency"));
                    accInfo.put("balance", balance);
                    accInfo.put("credit", credit);
                    return accInfo;
                }else if (isArchive.equals("0") && currency.equals("USC") && balance >= 20000.0) {
                    //USC account
                    accInfo.put("account", ((JSONObject) item).getString("mt4_account"));
                    accInfo.put("currency", ((JSONObject) item).getString("currency"));
                    accInfo.put("balance", balance);
//                    accInfo.put("userId", ((JSONObject) item).getString("user_id"));
                    return accInfo;
                }else if (isArchive.equals("0")) {
                    //add unarchived account without enough balance to a list
                    activeAcctList.add(item);
                }
            }
            System.out.println("\nNo suitable MT4/5 account!! All accounts balance less than (USC)20000 / (Non-USC)200");

            //if none of the account with enough balance, then random choose one account for next step (cash adjustment)
            if (accInfo.isEmpty())
            {
                Random random = new Random();
                if (activeAcctList.isEmpty()) {
                    System.out.println("No active account found!");
                    return null;
                }
                int index = random.nextInt(activeAcctList.size());
                accInfo.put("account", ((JSONObject) activeAcctList.get(index)).getString("mt4_account"));
                accInfo.put("currency", ((JSONObject) activeAcctList.get(index)).getString("currency"));
                accInfo.put("userId", ((JSONObject) activeAcctList.get(index)).getString("user_id"));
                accInfo.put("balance", ((JSONObject) activeAcctList.get(index)).getDouble("equity") - ((JSONObject) activeAcctList.get(index)).getInteger("credit"));
                System.out.println("Random select an account: "+accInfo+"\n");
                return accInfo;
            }
        }catch(Exception e) {
            e.printStackTrace();
            System.out.println("Query mt4_account failed!");
        }
        return null;
    }


    public JSONObject apiWithdrawalData(){
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIrequest(HyTechUrl.WD_DATA_CP,"");
        Integer resCode = result.getInteger("code");
        String resData = result.getString("data");
        assertTrue(resCode.equals(0) && (resData != null && !resData.isEmpty()),"Get Client Withdrawal account & channels failed!!");
        return result;
    }

    /*
     * This method is used for checking credit deduction when withdrawing with different amount.
     * There are three request body with different amount variable.
     * The reference wiki is https://suntontech.atlassian.net/wiki/spaces/~624e9395258562006fa84cb1/pages/1458044929/User+Manual+Credit+Deduction+Rules
     * */
    public void apiWDDeductCredit(JSONObject accInfo) throws Exception {
        String fullPath = this.url + "web-api/cp/api/withdrawal/getDeductCredit";
        header.put("Content-Type", "application/json");
        HyTechUtils.genXSourceHeader(header);
        Double accountbalance = accInfo.getDouble(("balance"));
        System.out.println("account balance: " + accountbalance);
        Double userCredit = accInfo.getDouble(("credit"));
        System.out.println("User credit: " + userCredit);

        //reserve credit < Account Credit, credit deducted will be as followed: User credit - (account balance - withdrawal amount) * X%
        // (X% = depends on the user’s country (may refer to the appendix below))
        Integer withdrawAmountBodya = (int)(accountbalance*0.9831);
        System.out.println("Withdraw Amount: " + withdrawAmountBodya);
        Double deductCredit = Math.round((userCredit - (accountbalance - withdrawAmountBodya)*0.2) * 100.0) / 100.0;
        System.out.println("deductCredit: " + deductCredit);

        String body = "{\"sourceAccount\":" + accInfo.getString("account") + ",\"withdrawAmount\":" + withdrawAmountBodya + "}";

        try {
            HttpResponse response = httpClient.getPostResponse(fullPath, header, body);
            JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
            printAPICPInfo(fullPath, String.valueOf(body), String.valueOf(result));
            Integer rescode = result.getInteger("code");
            String data = result.getString("data");

            //assertTrue(rescode.equals(0) && data.contains("{\"transferCredit\":0,\"sourceAccount\":"+ accInfo.getString("account") + ",\"usdtERCAddress\":[],\"deductCredit\":"), "deduct failed!! \n" + result);
            assertTrue(rescode.equals(0) && data.contains("\"sourceAccount\":"+ accInfo.getString("account") + ",\"usdtERCAddress\":[],\"deductCredit\":"), "deduct failed!! \n" + result);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //reserve credit >= Account Credit, no credit will be deducted/removed
        Integer withdrawAmountBodyb = (int)(accountbalance/2);
        System.out.println("Withdraw Amount: " + withdrawAmountBodyb);


        String bodyb = "{\"sourceAccount\":" + accInfo.getString("account") + ",\"withdrawAmount\":" + withdrawAmountBodyb + "}";

        try {
            HttpResponse response = httpClient.getPostResponse(fullPath, header, bodyb);
            JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
            Integer rescode = result.getInteger("code");
            String data = result.getString("data");
            String deductCreditb = result.getJSONObject("data").getString("deductCredit");
            System.out.println("deductCredit: " + deductCreditb);

            //assertTrue(rescode.equals(0) && data.contains("{\"transferCredit\":0,\"sourceAccount\":"+ accInfo.getString("account") + ",\"usdtERCAddress\":[],\"deductCredit\":"), "deduct failed!! \n" + result);
            assertTrue(rescode.equals(0) && data.contains("\"sourceAccount\":"+ accInfo.getString("account") + ",\"usdtERCAddress\":[],\"deductCredit\":"), "deduct failed!! \n" + result);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    //Trigger to send email verification code
    //If api/mfa/sendEmailVerificationCode returns 500 error then wait for 60s try again
    private String sendEmailVerificationCode() {
        String path = "web-api/api/mfa/sendEmailVerificationCode";
        String body = "{\"namespace\":\"CP_WITHDRAW\"}";
        HyTechUtils.genXSourceHeader(header);

        try {
            JSONObject result = sendCPAPIrequest(path, body);
            Integer rescode = result.getInteger("code");
            if (rescode.equals(500)) {
                System.out.println("Sleep for 60 seconds and resend OTP again...");
                Thread.sleep(60000);
                result = sendCPAPIrequest(path, body);
                rescode = result.getInteger("code");
            }
            String txId = result.getJSONObject("data").getString("txId");
            assertTrue(rescode.equals(0) ,"CP API - Send OTP code failed!! \n"+result);
            return txId;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    //Retrieve unionpay card info
    public Map<String, String> getUnionpayInfo() {
        HyTechUtils.genXSourceHeader(header);
        String unionURL =  HyTechUrl.WD_GET_UNIONPAY_CARD;
        JSONObject unionInfo = sendCPAPIGETrequest(unionURL);
        JSONArray jsonArray = unionInfo.getJSONArray("data");
        assertFalse(jsonArray.isEmpty(),"API response - data object is empty!!\n" + unionInfo);

        Map<String, String> unionDetails = new HashMap<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            if (obj.getIntValue("is_del") == 0) {
                unionDetails.put("unionBankName", obj.getString("bank_name"));
                unionDetails.put("unionCardName", obj.getString("card_holder_name"));
                unionDetails.put("unionCardNum", obj.getString("card_number"));
                unionDetails.put("unionBranch", obj.getString("branch_name"));
                unionDetails.put("unionType", obj.getString("payment_type"));
                unionDetails.put("unionName", obj.getString("payment_type_display"));

                return unionDetails;
            }
        }
        System.out.println("*******Failed! Alert! No unionpay card available. Kindly check manually through CP.");
        return null;
    }

    //Retrieve withdrawal history record from transaction history
    public void apiWDTransHist() throws Exception {
        String fullPath = this.url + HyTechUrl.WD_TRANS_HISTORY;
        header.put("Content-Type","application/x-www-form-urlencoded");
        HyTechUtils.genXSourceHeader(header);
        JSONObject body = new JSONObject();
        body.put("pageNo","1");
        body.put("page25","25");

        HttpResponse response = httpClient.getPostResponse(fullPath, header, body);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
        printAPICPInfo(fullPath, String.valueOf(body), String.valueOf(result));
        Integer resCode = result.getInteger("code");
        String resData = result.getString("data");
        JSONArray resWdHistory = result.getJSONObject("data").getJSONArray("withdrawHistory");
        assertTrue(resCode.equals(0) && resData != null,"Get Client Withdrawal Transaction History failed!!");
        assertFalse(resWdHistory.isEmpty(),"API response - withdrawHistory object is empty");
    }


    //Retrieve blacklist status of the user/account for withdrawal
    public void apiWDBlacklist(){
        String fullPath = this.url + HyTechUrl.WD_BLACKLIST_STATUS;
        HyTechUtils.genXSourceHeader(header);
        JSONObject result = sendCPAPIGETrequest(HyTechUrl.WD_BLACKLIST_STATUS);
        printAPICPInfo(fullPath, "", String.valueOf(result));
        Integer resCode = result.getInteger("code");
        String resData = result.getString("data");
        assertTrue(resCode.equals(0) && resData.equalsIgnoreCase("true"), "Get Client Withdraw Blacklist status failed!!");
    }

    //Retrieve MAM fee settlement for the user
    public void apiWDValidateMamFeeSett(JSONObject accInfo) throws Exception {
        JSONArray obj = accInfo.getJSONObject("data").getJSONArray("logins");
        Integer accNumber = obj.getJSONObject(0).getInteger("login");

        header.put("Content-Type","application/json");
        HyTechUtils.genXSourceHeader(header);
        String queryParam = "account=" + accNumber;
        String fullPath = this.url + HyTechUrl.WD_VALIDATE_MAMFEESETTLEMENT + "?" + queryParam;

        HttpResponse response = httpClient.getPostResponse(fullPath, header, "");
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
        printAPICPInfo(fullPath, "", String.valueOf(result));
        Integer resCode = result.getInteger("code");
        String resData = result.getString(("msg"));
        assertTrue(resCode.equals(0) && resData.equalsIgnoreCase("Success"), "Get Client MAM Fee Settlement failed!!");
    }

    //Retrieve the user's LBT withdrawal balance
    public BigDecimal apiWDLBTData(String acctCurrency) {
        header.put("Content-Type", "application/json");
        HyTechUtils.genXSourceHeader(header);
        String queryParam = "country=" + "MY";
        String fullPath = this.url + HyTechUrl.WD_LBT_DATA + "?" + queryParam;

        JSONObject result = null;
        try {
            HttpResponse response = httpClient.getGetResponse(fullPath, header);
            result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
            printAPICPInfo(fullPath, "", String.valueOf(result));
            Integer resCode = result.getInteger("code");
            String resData = result.getString("data");
            assertTrue(resCode.equals(0) && resData != null, "Get LBT Withdrawal Info failed!!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject lbtBalance = result.getJSONObject("data").getJSONObject("lbtBalance");
        if (lbtBalance == null || lbtBalance.getBigDecimal(acctCurrency) == null) {
            return BigDecimal.ZERO;
        }
        return lbtBalance.getBigDecimal(acctCurrency);
    }

    //Retrieve the user's credit card withdrawal balance
    public JSONObject apiWDCCBalanceInfo(String acctCurrency, BigDecimal wdAmt) {
        HyTechUtils.genXSourceHeader(header);
        String ccInfoURL = HyTechUrl.WD_CC_BALANCES + acctCurrency;
        JSONArray jsonArray = sendCPGETrequestArray(ccInfoURL);
        printAPICPInfo(ccInfoURL, "", String.valueOf(jsonArray));
        assertFalse(jsonArray.isEmpty(), "Get Credit Card balance failed!!\n"+jsonArray);

        JSONArray activeAcctList = new JSONArray();

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject cc = jsonArray.getJSONObject(i);
            Boolean ccIsExpired = cc.getBoolean("isExpired");
            BigDecimal ccBalance = cc.getBigDecimal("balance");
            Integer ccIsDeleted = cc.getJSONObject("creditCard").getInteger("is_del");

            if (!ccIsExpired && ccIsDeleted == 0) {
                activeAcctList.add(cc);
                if (ccBalance.compareTo(wdAmt) > 0) {
                    return cc;
                }
            }
        }

        //if none of the cc with enough balance, then random return one active account
        if (!activeAcctList.isEmpty())
        {
            Random random = new Random();
            int index = random.nextInt(activeAcctList.size());
            System.out.println("Random select an account: "+activeAcctList.getJSONObject(index)+"\n");
            return activeAcctList.getJSONObject(index);
        }
        return null;
    }


    public JSONArray apiWDCCBalanceInfo(String acctCurrency) {
        HyTechUtils.genXSourceHeader(header);
        String ccInfoURL = HyTechUrl.WD_CC_BALANCES + acctCurrency;
        JSONArray jsonArray = sendCPGETrequestArray(ccInfoURL);
        printAPICPInfo(ccInfoURL, "", String.valueOf(jsonArray));

        return jsonArray;
    }

    //Retrieve channel info for specified withdrawal method
    public Map<String, String> apiWDChannelInfo(String cpsCode, Integer wdTypeID) {
        HyTechUtils.genXSourceHeader(header);
        String channelURL = "web-api/cp/api/withdrawal/getCPSWithdrawalData_cp?country=&currency=USD";
        JSONObject channelResult = sendNonJsonGETrequest(channelURL);
        JSONArray jsonArray = channelResult.getJSONObject("data").getJSONObject("withdrawalType").getJSONArray("channels");
        Map<String, String> channelDetails = new HashMap<>();

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            if (obj.getString("payment_method").equals(cpsCode) && obj.getInteger("withdrawTypeId").equals(wdTypeID)) {
                channelDetails.put("wdCpsCode", obj.getString("payment_method"));
                channelDetails.put("wdtype", obj.getString("withdrawTypeId"));
                channelDetails.put("wdchannelTitle", obj.getString("name"));
                channelDetails.put("wdcurrency", obj.getString("currency_number"));
                channelDetails.put("wdcurrencyCode", obj.getString("currency_code"));
                channelDetails.put("wdActcurrency", obj.getString("actual_currency_number"));
                channelDetails.put("wdActcurrencyCode", obj.getString("actual_currency_code"));
                channelDetails.put("channelMerchantId", obj.getString("channel_merchant_id"));

                //Withdrawal method - F00000
                if ("F00000".equals(cpsCode)) {
                    //if bank transfer
                    if (channelDetails.get("wdchannelTitle").contains("Bank Transfer")) {
                        JSONObject bankDetails = JSON.parseObject(obj.getString("attach_variable")).getJSONObject("2");

                        if (bankDetails.containsKey("field_content") && bankDetails.getJSONArray("field_content").size() > 0) {
                            channelDetails.put("bankCode", bankDetails.getJSONArray("field_content")
                                    .getJSONObject(0)
                                    .getString("bank_code")
                            );
                        }else
                        {
                            channelDetails.put("bankCode", "Bank Name"); // Default if "field_content" is missing or empty
                        }
                    }
                }
                return channelDetails;
            }
        }

        throw new SkipException("No matching cpsMethodCode found for "+ cpsCode +". Kindly check manually through CP.");
//		return null;
    }

    //Retrieve withdrawal exchange rate
    public void apiWDExchgRate() throws Exception {
        header.put("Content-Type","application/json");
        HyTechUtils.genXSourceHeader(header);
        String queryParam = "fromCurrency=USD&toCurrency=GBP";
        String fullPath = this.url + HyTechUrl.WD_EXCHG_RATE + "?" + queryParam;

        HttpResponse response = httpClient.getGetResponse(fullPath,header);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        printAPICPInfo(fullPath, "", String.valueOf(result));
        Integer resCode = result.getInteger("code");
        String resData = result.getString("data");
        assertTrue(resCode.equals(0) && resData != null, "Get Withdrawal exchange rate failed!!");
    }

    public String apiWDExchgRate(String fromCurrency,String toCurrency) throws Exception {
        header.put("Content-Type","application/json");
        HyTechUtils.genXSourceHeader(header);
        String queryParam = "fromCurrency="+fromCurrency+"&toCurrency="+toCurrency;
        String fullPath = this.url + HyTechUrl.WD_EXCHG_RATE + "?" + queryParam;

        HttpResponse response = httpClient.getGetResponse(fullPath,header);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        printAPICPInfo(fullPath, "", String.valueOf(result));
        Integer resCode = result.getInteger("code");
        String resData = result.getString("data");
        assertTrue(resCode.equals(0) && resData != null, "Get Withdrawal exchange rate failed!!");
        return resData;
    }
    public boolean apiLBTSwitch() throws Exception {
        header.put("Content-Type","application/json");
        HyTechUtils.genXSourceHeader(header);
        String fullPath = this.url + HyTechUrl.WD_LBT_SWITCH;

        HttpResponse response = httpClient.getGetResponse(fullPath,header);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        printAPICPInfo(fullPath, "", String.valueOf(result));
        Integer resCode = result.getInteger("code");
        Boolean resData = result.getBoolean("data");
        assertTrue(resCode.equals(0) && resData != null, "Get LBT switch status failed!!");
        return resData;
    }

    //Retrieve withdrawal mix and max amount info
    public void apiWDLimitInfo(JSONObject accInfo) throws Exception {
        JSONArray obj = accInfo.getJSONObject("data").getJSONArray("logins");
        Integer accNumber = obj.getJSONObject(0).getInteger("login");
        Integer userID = obj.getJSONObject(0).getInteger("userId");

        header.put("Content-Type","application/json");
        HyTechUtils.genXSourceHeader(header);
        String queryParam = "userId=" + userID + "&account=" + accNumber;
        String fullPath = this.url + HyTechUrl.WD_LIMITINFO + "?" + queryParam;

        HttpResponse response = httpClient.getGetResponse(fullPath,header);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        printAPICPInfo(fullPath, "", String.valueOf(result));
        Integer resCode = result.getInteger("code");
        String resData = result.getString("data");
        assertTrue(resCode.equals(1) && resData != null, "Get Withdrawal Limit Info failed!!");
    }

    //Retrieve list of non-credit card withdrawal method
    public void apiWDNonCCType(JSONObject accInfo) throws Exception {
        JSONArray allAcc = accInfo.getJSONObject("data").getJSONArray("logins");
        String accNumber = allAcc.getJSONObject(0).getString("login");

        String fullPath = this.url + HyTechUrl.WD_NONCCTYPE;
        header.put("Content-Type", "application/json");
        HyTechUtils.genXSourceHeader(header);
        String body = "{\"accountNumber\":"+ accNumber + "}";

        HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
        String responseBody = EntityUtils.toString(response.getEntity());
        JSONArray result = JSONArray.parseArray(responseBody);
        printAPICPInfo(fullPath, body, String.valueOf(result));
        assertTrue(result != null, "Get Withdrawal Non-CC type failed!!");
    }

    //Risk audit approval/reject
    public void apiRiskAuditApproval(String link, String flag){
        try {
            LogUtils.info(String.format("Risk Audit Approval/Reject\n%s,\n%s",link, flag));
            HttpResponse response = httpClient.getGetResponse(link, null);
            String result = EntityUtils.toString(response.getEntity(),"UTF-8");
            switch (flag) {
                case "APPROVE_URL":
                    assertTrue(result.contains("Operation successful - the withdrawal application has been progressed to the next step."), "Openapi response message wrong. Kindly check.\n" + result);
                    break;
                case "REJECT_URL":
                    //Do nothing
                    GlobalMethods.printDebugInfo(" Risk Audit Reject.");
                    break;
                default:
                    GlobalMethods.printDebugInfo("Invalid input for risk audit link flag!");
            }
            this.printAPICPInfo(link, "", result );
        }catch(Exception e) {
            if(flag.equalsIgnoreCase("APPROVE_URL")){
                fail( "apiRiskAuditApproval API sending failure!\n"+e.getMessage());
            }
            GlobalMethods.printDebugInfo("apiRiskAuditApproval API sending failure!" + link);
        }
    }

    //Submit withdraw for CP
    public void apiWDSubmitWithdraw(String wdbody) {
        String wdpath = HyTechUrl.WD_APPLY;

        try {
            apiAntiReuseToken();
            HyTechUtils.genXSourceHeader(header);
            JSONObject result = sendCPAPIrequest(wdpath, wdbody);
            assertTrue(result != null, "CP API - Withdraw failed!!");
//			printAPICPInfo(wdpath, wdbody, String.valueOf(result));
            printAPICPInfoPro(wdpath, header, wdbody,  result);

            Integer rescode = result.getInteger("code");
            String msg = result.getString("msg");
            String data = result.getString("data");
            String errmsg = result.getString("errmsg");
            // 优化后的代码 - 更清晰的表达
            boolean isMethodUnavailable = Stream.of(data, msg, errmsg)
                    .filter(Objects::nonNull)
                    .anyMatch(str -> str.equalsIgnoreCase("This withdraw method is not available"));

            if (rescode == 583 && isMethodUnavailable) {
                throw new SkipException(data != null ? data : (msg != null ? msg : errmsg));
            }
            else {
                assertTrue(rescode.equals(0) && msg.equals("Withdraw successfully") ,"CP API - Withdraw failed!! \n"+result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Submit withdraw for IB
    public void apiWDSubmitWithdrawIB(String wdbody) throws Exception {
//        String fullPath = this.url + "hgw/rw/payment-api/api/withdrawal/applyWithdrawal";
        String fullPath = this.url + HyTechUrl.IBWD_APPLY;

        header.put("Content-Type", "application/x-www-form-urlencoded");
        HyTechUtils.genXSourceHeader(header);
        apiAntiReuseToken();
        String trimmedBody = wdbody.substring(1, wdbody.length() - 1);
        String body = convertToFormUrlEncoded(trimmedBody);

        HttpResponse response = httpClient.getPostResponse(fullPath, header, body);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        Integer rescode = result.getInteger("code");
        String data = result.getString("data");
        assertTrue(rescode.equals(0) && data.equals("Withdraw successfully"), "IB API - Withdraw failed!!");
    }


    /* Get IB account data for Withdrawal */
    public JSONObject apiWithdrawalDataIB() throws Exception {
        String fullPath = this.url + HyTechUrl.IBWD_DATA;
        HashMap<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/x-www-form-urlencoded");
        HyTechUtils.genXSourceHeader(header);
        JSONObject body = new JSONObject();
        body.put("qUserId", "0");

        HttpResponse response = httpClient.getPostResponse(fullPath, header, body);
        JSONObject responseBody = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        JSONArray logins = responseBody.getJSONObject("data").getJSONArray("logins");
        assertFalse(logins.isEmpty(),"API response - logins object is empty\n"+ responseBody);

        JSONObject accInfo = new JSONObject();
        for (int i = 0; i < logins.size(); i++) {
            JSONObject loginObj = logins.getJSONObject(i);
            String login = loginObj.getString("login");
            String currency = loginObj.getString("currency");
            double balance = loginObj.getDouble("balance");
            String userID = loginObj.getString("userId");

            if (!currency.equals("USC") && balance > 40.0) {
                accInfo.put("account", login);
                accInfo.put("currency", currency);
                accInfo.put("balance", balance);
                accInfo.put("userId", userID);
                return accInfo;
            } else if (currency.equals("USC") && balance > 4000.0) {
                accInfo.put("account", login);
                accInfo.put("currency", currency);
                accInfo.put("balance", balance);
                accInfo.put("userId", userID);
                System.out.println("USC account: " + login + ",currency: " + currency + ", balance: "+ balance);
                return accInfo;
            }
        }
        System.out.println("\nNo suitable IB account!! All accounts balance less than (USC)4000 / (Non-USC)40");

        if (accInfo.isEmpty())
        {
            Random random = new Random();
            int index = random.nextInt(logins.size());
            accInfo.put("account", ((JSONObject) logins.get(index)).getString("login"));
            accInfo.put("currency", ((JSONObject) logins.get(index)).getString("currency"));
            accInfo.put("balance", ((JSONObject) logins.get(index)).getDouble("balance"));
            accInfo.put("userId", ((JSONObject) logins.get(index)).getDouble("userId"));
            System.out.println("Random select an IB Account: "+accInfo+"\n");
            return accInfo;
        }
        return null;
    }

    /* Retrieve blacklist status of the IB user for withdrawal */
    public void apiWDBlacklistIB() throws Exception {
        String fullPath = this.url + HyTechUrl.IBWD_BLACKLIST_STATUS;
        HashMap<String, String> header = new HashMap<>();
        header.put("Content-Type","application/json");
        HyTechUtils.genXSourceHeader(header);

        HttpResponse response = httpClient.getGetResponse(fullPath, header);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        printAPICPInfo(fullPath, "", String.valueOf(result));
        Integer resCode = result.getInteger("code");
        String resData = result.getString("data");
        assertTrue(resCode.equals(0) && resData.equalsIgnoreCase("true"), "Get IB Withdraw Blacklist status failed!!");
    }

    /*Get Withdraw Channel Info*/
    public String apiChannelInfo(JSONObject accInfo) throws Exception {
        String fullPath = this.url + HyTechUrl.WD_PCS_SORTINFO;
        HashMap<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        HyTechUtils.genXSourceHeader(header);
        JSONObject body = new JSONObject();
        body.put("applyAmount", "40");
        body.put("device", "WEB");
        body.put("mt4Account", accInfo.getString("account"));

        HttpResponse response = httpClient.getPostResponse(fullPath, header, body);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        printAPICPInfo(fullPath, String.valueOf(body), String.valueOf(result));

        Integer resCode = result.getInteger("code");
        assertTrue(resCode.equals(0), "Get channel info failed!!");
        //Get withdrawal type
        JSONArray dataArr = result.getJSONArray("data");
        assertFalse(dataArr.isEmpty(),"API response - data object is empty!!");
        for (int i = 0; i < dataArr.size(); i++) {
            JSONObject data = dataArr.getJSONObject(i);
            if (data != null && !data.isEmpty()) {
                String key = data.keySet().iterator().next();
                JSONArray arr = data.getJSONArray(key);
                if (!arr.isEmpty()) {
                    JSONObject firstItem = arr.getJSONObject(0);
                    Integer wdType = firstItem.getInteger("withdraw_type");
                    return wdType.toString();
                }
            }
        }
        return null;
    }


    public void apiPaymentInfoIB(JSONObject ibInfo) throws Exception {
        String withdrawType = apiChannelInfo(ibInfo);

        String fullPath = this.url + HyTechUrl.IB_PAYMENT_INFO;
        HashMap<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        HyTechUtils.genXSourceHeader(header);
        JSONObject body = new JSONObject();
        body.put("currency", ibInfo.get("currency"));
        body.put("mt4Account", ibInfo.get("account"));
        body.put("withdrawType", withdrawType);
        body.put("withdrawalAmount", "40");

        HttpResponse response = httpClient.getPostResponse(fullPath, header, body);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        printAPICPInfo(fullPath, String.valueOf(body), String.valueOf(result));
        Integer resCalAmount = result.getInteger("calculated_amount");
        Integer resPerceFee = result.getInteger("percentage_fee");
        assertTrue(resCalAmount.equals(0) & resPerceFee.equals(0), "Get IB payment info failed");
    }

    public void apiPaymentListIB(JSONObject ibInfo) throws Exception {
        String withdrawType = apiChannelInfo(ibInfo);

        String fullPath = this.url + HyTechUrl.IB_PAYMENT_LIST;
        HashMap<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        HyTechUtils.genXSourceHeader(header);
        JSONObject body = new JSONObject();
        body.put("paymentType", withdrawType);

        HttpResponse response = httpClient.getPostResponse(fullPath, header, body);
        String responseStr = EntityUtils.toString(response.getEntity(), "UTF-8");
        JSONArray result = JSON.parseArray(responseStr);
        printAPICPInfo(fullPath, String.valueOf(body), String.valueOf(result));
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(statusCode,200 ,"Get IB payment list failed!!");
    }
}
