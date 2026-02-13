package newcrm.cpapi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import junit.framework.Assert;
import newcrm.business.dbbusiness.EmailDB;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.REGULATOR;
import newcrm.utils.api.HyTechUrl;
import newcrm.utils.app.Transfer;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

import javax.validation.constraints.AssertTrue;
import java.util.*;

import static org.testng.Assert.assertTrue;

public class CPAPIPaymentOthers extends CPAPIPaymentBase {
    public EmailDB emailDB;

    public CPAPIPaymentOthers(String url, String cplogin, String password) {
        super(url, cplogin, password);
    }

    public CPAPIPaymentOthers(String url, String cplogin, String password, BRAND brand, REGULATOR regulator) {
        super(url, cplogin, password, brand, regulator);
    }


    //Get client's all trading accounts info
    public JSONObject apiFTAccInfo() {
        try {
            String fullPath = this.url + HyTechUrl.FT_GET_DATA;
            Map<String,String> header = new HashMap<>();
            header.put("Content-Type","application/json");
            HttpResponse response = httpClient.getPostResponse(fullPath, header, "");
            JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
            Integer resCode = result.getInteger("code");
            printAPICPInfo(fullPath, "", String.valueOf(result));
            assertTrue(resCode.equals(0), "Get Available Transfer Accounts failed." + result);
            return result;
        } catch (Exception e) {
            System.err.println("Error fetching account info: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    //Get two accounts(same currency and with available balance) for Fund Transfer
    public String[] getAccForTransfer() {
        JSONObject allAccInfo = apiFTAccInfo();
        JSONArray allTradingAcc = allAccInfo.getJSONObject("data").getJSONArray("logins");
        if (allTradingAcc == null || allTradingAcc.size() < 2) {
            GlobalMethods.printDebugInfo("No available account found.");
            return new String[0];
        }

        //Get UserID
        JSONArray allAcc = allAccInfo.getJSONObject("data").getJSONArray("logins");
        JSONObject firstAccInfo = allAcc.getJSONObject(0);
        String getUserId = firstAccInfo.getString("userId");
        GlobalMethods.printDebugInfo("UserID: " + getUserId);
        return checkSameCurrencyAndAvailBalanceAcc(allTradingAcc);
}


    //Check account currency and balance
    public String[] checkSameCurrencyAndAvailBalanceAcc(JSONArray allTradingAcc) {
        for (int i = 0; i < allTradingAcc.size(); i++) {
            JSONObject account1 = allTradingAcc.getJSONObject(i);

            for (int j = i + 1; j < allTradingAcc.size(); j++) {
                JSONObject account2 = allTradingAcc.getJSONObject(j);

                if (account1.getString("currency").equals(account2.getString("currency"))) {
                    String[] accWithBalances = checkAccWithBalances(account1, account2);
                    if (accWithBalances.length == 0) {
                        break;
                    }
                    return accWithBalances;
                }
            }
        }
        GlobalMethods.printDebugInfo("No account found with same currency/available balance.");
        return new String[0];
    }

    //Check account balance
    private String[] checkAccWithBalances(JSONObject acc1, JSONObject acc2) {
        String[] availAcc = new String[2];
        Double acc1balance = acc1.getDouble("balance");
        Double acc2balance = acc2.getDouble("balance");

        //both accounts have zero balance
        if (acc1balance <= 0 && acc2balance <= 0) {
            return new String[0];
        }

        //Higher balance account assign to availAcc[0]
        if (acc1balance > acc2balance) {
            availAcc[0] = acc1.getString("login");
            availAcc[1] = acc2.getString("login");
        } else {
            availAcc[0] = acc2.getString("login");
            availAcc[1] = acc1.getString("login");
        }
        return availAcc;
    }

    //Perform Funds Transfer between account
    public void apiFundsTransfer(double transferAmount) throws Exception {
        //Get accounts to perform funds transfer.
        String[] availAcc = getAccForTransfer();
        if (availAcc.length == 0) {
            Assert.fail("Perform Transfer between accounts failed.");
        }
        //Assign ToAccount and FromAccount
        String accHigherBalance = availAcc[0];
        String accLowerBalance = availAcc[1];

        String fullPath = this.url + HyTechUrl.FT_APPLY;
        header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        apiAntiReuseToken();

        Map<String, String> body = new HashMap<>();
        body.put("amount", String.valueOf(transferAmount));
        body.put("fromAccount", accHigherBalance);
        body.put("isConfirmPayMamFee", "false");
        body.put("toAccount", accLowerBalance);

        HttpResponse response = httpClient.getPostResponse(fullPath, header, body);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        printAPICPInfo(fullPath, String.valueOf(body), String.valueOf(result));
        Integer resCode = result.getInteger("code");
        String resMessage = result.getString("data");
        assertTrue(resCode.equals(0) && resMessage.equals("Transfer successful"), "Perform Transfer not successful");
        printFTInfo(accHigherBalance, accLowerBalance, transferAmount);
    }

    public void printFTInfo(String fromAccount, String toAccount, double transferAmount) {
        System.out.println("***********Transfer Between Account successfully***********");
        System.out.printf("%-30s : %s\n", "From Account", fromAccount);
        System.out.printf("%-30s : %s\n", "To Account", toAccount);
        System.out.printf("%-30s : %s\n", "Amount Transferred", transferAmount);
        System.out.println("***********************************************************");
    }


    public void apiExpTransHist() throws Exception {
        String fullPath = this.url + HyTechUrl.EXP_TRANS_HISTORY;
        header.put("Content-Type","application/json");
        JSONObject body = new JSONObject();
        body.put("accountNumber", queryMetaTraderAccountDetails().getString("account"));
        body.put("endDate","2099-04-02");
        body.put("startDate","2025-04-02");

        HttpResponse response = httpClient.getPostResponse(fullPath, header, String.valueOf(body));
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
        printAPICPInfo(fullPath, String.valueOf(body), String.valueOf(result));
        Integer resCode = result.getInteger("code");
        String resData = result.getString("data");
        assertTrue(resCode.equals(0) && resData != null,"Export Transaction History report failed!!");
    }

    public void apiFTPermission(JSONObject accInfo) throws Exception {
        JSONArray jsonArray = accInfo.getJSONObject("data").getJSONArray("logins");
        String account = jsonArray.getJSONObject(0).getString("login");

        String fullPath = this.url + HyTechUrl.FT_PERMISSION;
        header.put("Content-Type","application/x-www-form-urlencoded");
        Map<String,String> body = new HashMap();
        body.put("account", account);

        HttpResponse response = httpClient.getPostResponse(fullPath, header, body);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
        printAPICPInfo(fullPath, String.valueOf(body), String.valueOf(result));
        Integer resCode = result.getInteger("code");
        String resData = result.getString("data");
        assertTrue(resCode.equals(0) && resData.equals("PERMITTED"),"Get Status of Transfer Permission failed!!");
    }

    public void apiFTTransHist() throws Exception {
        header.put("Content-Type","application/json");
        String fullPath = this.url + HyTechUrl.FT_HISTORY;

        HttpResponse response = httpClient.getGetResponse(fullPath, header);
        String responseBody = EntityUtils.toString(response.getEntity());
        JSONArray result = JSONArray.parseArray(responseBody);
        printAPICPInfo(fullPath, "", String.valueOf(result));
        assertTrue(result != null && !result.isEmpty());
    }

    public void apiFTBlacklist() throws Exception {
        header.put("Content-Type","application/json");
        String fullPath = this.url + HyTechUrl.FT_BLACKLIST;

        HttpResponse response = httpClient.getGetResponse(fullPath, header);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
        printAPICPInfo(fullPath, "", String.valueOf(result));
        Integer resCode = result.getInteger("code");
        String resData = result.getString("data");
        assertTrue(resCode.equals(0) && resData != null,"Get funds transfer blacklist failed!!");
    }

    public void apiFTDeductCred() throws Exception {
        String[] availAcc = getAccForTransfer();
        if (availAcc.length == 0) {
            Assert.fail("No available accounts found.");
        }
        //Assign ToAccount and FromAccount
        String accHigherBalance = availAcc[0];
        String accLowerBalance = availAcc[1];

        String fullPath = this.url + HyTechUrl.FT_DEDUCTCRED;
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");

        Map<String, String> body = new HashMap<>();
        body.put("sourceAccount", accHigherBalance);
        body.put("toAccount", accLowerBalance);
        body.put("transferCreditType", "1");
        body.put("withdrawAmount", "1");

        HttpResponse response = httpClient.getPostResponse(fullPath, header, String.valueOf(body));
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        printAPICPInfo(fullPath, String.valueOf(body), String.valueOf(result));
        Integer resCode = result.getInteger("code");
        assertTrue(resCode.equals(0), "Get Funds Transfer - credit deduction amount failed");
    }

    public void apiFTCurrRate() throws Exception {
        header.put("Content-Type","application/json");
        String queryParam = "formCurrency=USD&&toCurrency=USD";
        String fullPath = this.url + HyTechUrl.FT_CURRENCYRATE + "?" + queryParam;

        HttpResponse response = httpClient.getGetResponse(fullPath, header);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
        printAPICPInfo(fullPath, "", String.valueOf(result));
        Integer resCode = result.getInteger("code");
        Integer resData = result.getInteger("data");
        assertTrue(resCode.equals(0) && resData.equals(1), "Get funds transfer currency rate failed!!");

    }

    public void apiFTCurrList() throws Exception {
        header.put("Content-Type","application/json");
        String queryParam = "formCurrency=USD";
        String fullPath = this.url + HyTechUrl.FT_CURRENCYLIST + "?" + queryParam;

        HttpResponse response = httpClient.getGetResponse(fullPath, header);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
        printAPICPInfo(fullPath, "", String.valueOf(result));
        Integer resCode = result.getInteger("code");
        String resData = result.getString("data");
        assertTrue(resCode.equals(0) && resData != null, "Get funds transfer currency list failed!!");
    }

    /* Get first IB Account Available Balance */
    public void apiQueryBalanceIB() throws Exception {
        JSONObject ibAccList = apiQueryRebateList();
        JSONArray ibAccInfo = ibAccList.getJSONObject("data").getJSONArray("list");
        JSONObject firstAcc = ibAccInfo.getJSONObject(0);
        String ibAccNum = firstAcc.getString("login");

        String fullPath = this.url + HyTechUrl.IB_AVAIBALANCE;
        header.put("Content-Type", "application/x-www-form-urlencoded");
        String body = "qUserId=0&qAccount=" + ibAccNum;

        HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
        printAPICPInfo(fullPath, body, String.valueOf(result));
        Integer resCode = result.getInteger("code");
        String resData = result.getString("data");
        assertTrue(resCode.equals(0) && !resData.isEmpty(), "Get IB Account Available Balance failed!!");
    }

    public JSONObject apiEligibleTransferIB() throws Exception {
        header.put("Content-Type","application/json");
        String fullPath = this.url + HyTechUrl.IBFT_ELIGTRANSFER;

        HttpResponse response = httpClient.getGetResponse(fullPath, header);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
        printAPICPInfo(fullPath, "", String.valueOf(result));
        Integer resCode = result.getInteger("code");
        String resData = result.getString("data");
        assertTrue(resCode.equals(0) && resData.equals("true"), "Get IB Account Available Balance failed!!");
        return result;
    }

    //Download the template for ib transfer upload purpose
    public void apiDownloadTransTemplate() throws Exception {
        header.put("Content-Type","application/json");
        String fullPath = this.url + HyTechUrl.IBFT_DOWNLOAD_TEMPLATE;

        HttpResponse response = httpClient.getGetResponse(fullPath, header);
        String result = EntityUtils.toString(response.getEntity(),"UTF-8");
        printAPICPInfo(fullPath, "", result);
        int resCode = response.getStatusLine().getStatusCode();
        assertTrue(resCode == 200 && result.contains("To Account,Amount,\"*Please ensure the format"), "IB Download Transfer Template failed!!");
    }

    //Get list of rebate and trading account for the IB
    public JSONObject apiGetRebateAndTradingAccIB() throws Exception {
        String fullPath = this.url + HyTechUrl.IBFT_REBATETRAD_ACC;
        header.put("Content-Type","application/x-www-form-urlencoded");
        String body = "qUserId=0";

        HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
        printAPICPInfo(fullPath, "", String.valueOf(result));
        Integer resCode = result.getInteger("code");
        JSONArray resAccList = result.getJSONObject("data").getJSONArray("accountList");
        JSONArray resRebates = result.getJSONObject("data").getJSONArray("rebates");
        assertTrue(resCode.equals(0) && (resAccList != null && !resAccList.isEmpty()) && (resRebates != null && !resRebates.isEmpty()),
                "Get IB Own Rebate & Trading Accounts failed!!");
        return result;
    }

    //Get IB rebate account with sufficient balance
    public JSONObject apiGetRebateAccIB() throws Exception {
        header.put("Content-Type","application/x-www-form-urlencoded");
        String fullPath = this.url + HyTechUrl.IBFT_REBATE_ACC;

        HttpResponse response = httpClient.getPostResponse(fullPath, header,"");
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
        printAPICPInfo(fullPath, "", String.valueOf(result));
        Integer resCode = result.getInteger("code");
        JSONArray resRebateAccList = result.getJSONObject("data").getJSONArray("rebates");
        assertTrue(resCode.equals(0) && (resRebateAccList != null && !resRebateAccList.isEmpty()), "Get IB Rebate Accounts failed!!");

        for (int i = 0; i < resRebateAccList.size(); i++) {
            JSONObject loginObj = resRebateAccList.getJSONObject(i);
            String login = loginObj.getString("login");
            String currency = loginObj.getString("currency");
            double balance = loginObj.getDouble("balance");
            String userID = loginObj.getString("userId");

            if (currency.equals("USD") && balance > 1.0) {
                JSONObject accInfo = new JSONObject();
                accInfo.put("account", login);
                accInfo.put("currency", currency);
                accInfo.put("balance", balance);
                accInfo.put("userId", userID);
                return accInfo;
            }
        }
        GlobalMethods.printDebugInfo("Not able to find a suitable IB rebate account!");
        return null;
    }


    public void apiGetSubIBInfo(JSONObject ibRebateAcc, String toAccNum) throws Exception {
        String fromAccNum = ibRebateAcc.getString("account");
        String fromAccCurr = ibRebateAcc.getString("currency");

        header.put("Content-Type","application/json");
        String fullPath = this.url + HyTechUrl.IBFT_SUBIBINFO;
        JSONObject body = new JSONObject();
        body.put("fromAccCurrency",fromAccCurr);
        body.put("fromAccount",fromAccNum);
        body.put("toAccount",toAccNum);

        HttpResponse response = httpClient.getPostResponse(fullPath, header,body);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
        printAPICPInfo(fullPath, body.toString(), String.valueOf(result));
        Integer resCode = result.getInteger("code");
        String resData = result.getString("data");
        assertTrue(resCode.equals(0) && resData != null, "Get IB's Sub-IB Info failed!!");
    }

    //Internal transfer between IB
    public void apiTransferToOwnIB(JSONObject ibRebateAndTradAcc) throws Exception {
        JSONArray rebateAccList = ibRebateAndTradAcc.getJSONObject("data").getJSONArray("rebates");
        JSONArray tradAccList = ibRebateAndTradAcc.getJSONObject("data").getJSONArray("accountList");

        //Get Rebate Account with balance as FromAcc
        String fromAcc = null;
        for (int i = 0; i < rebateAccList.size(); i++) {
            JSONObject rebateAcc = rebateAccList.getJSONObject(i);
            double balance = rebateAcc.getDouble("balance");
            if (balance > 1) {
                fromAcc = rebateAcc.getString("login");
                break;
            }
        }

        //Get Trading Account as ToAccount
        String toAcc = null;
        if (tradAccList != null) {
            JSONObject tradAccount = tradAccList.getJSONObject(0);
            toAcc = tradAccount.getString("login");
        }

        String fullPath = this.url + HyTechUrl.IBFT_TRANSFER_IB;
        apiAntiReuseToken();
        header.put("Content-Type","application/x-www-form-urlencoded");
        Map<String, String> body = new HashMap<>();
        body.put("qUserId", "0");
        body.put("fromAccount", fromAcc);
        body.put("toAccount", toAcc);
        body.put("amount", "1.0");
        body.put("transferType", "4");

        HttpResponse response = httpClient.getPostResponse(fullPath, header, body);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        printAPICPInfo(fullPath, body.toString(), String.valueOf(result));
        Integer resCode = result.getInteger("code");
        String resData = result.getString("data");
        assertTrue(resCode.equals(0) && resData.equals("您的转账已经在处理中"), "IB transfer to own trading/rebate account failed!!");
        GlobalMethods.printDebugInfo("IB transfer to Own Trading/Rebate Account successful!");
    }

    public void apiTransferToSubIBClientIB(JSONObject ibRebateAcc, String toAccNum,String ibEmail) throws Exception {
        if(brand.toString().equals("MO")){
            GlobalMethods.printDebugInfo("--------------Message: Skipped Brand MO as function "+ Thread.currentThread().getStackTrace()[2].getMethodName() +" is unavailable--------------");
            return;
        }
        String fromAcc = ibRebateAcc.getString("account");

        //create toAccounts array
        JSONArray toAccounts = new JSONArray();
        JSONObject toAccObj = new JSONObject();
        toAccObj.put("toAccount", toAccNum);
        toAccObj.put("amount",1);
        toAccObj.put("transferType",6);
        //add account info into toAccounts array
        toAccounts.add(toAccObj);

        String fullPath  = this.url + HyTechUrl.IBFT_TRANSFER_SUBIB;
        header.put("Content-Type","application/json");
        JSONObject body = new JSONObject();
        body.put("fromAccount",fromAcc);
        body.put("remark","");
        body.put("toAccounts",toAccounts);
        body.put("txId",apiTxId());
        body.put("code",setCode(ibEmail));

        HttpResponse response = httpClient.getPostResponse(fullPath, header, body);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        printAPICPInfo(fullPath, body.toString(), String.valueOf(result));
        Integer resCode = result.getInteger("code");
        JSONArray resAcc = result.getJSONObject("data").getJSONArray("validAccounts");
        assertTrue(resCode.equals(0) && (resAcc != null && !resAcc.isEmpty()), "IB transfer to Sub-IB failed!!");
        GlobalMethods.printDebugInfo("IB transfer to SubIB/Client successful!");
    }

    //Get txID for withdrawal
    public String apiTxId() throws Exception {
        String fullPath  = this.url + HyTechUrl.VERIFICATION_CODE;
        header.put("Content-Type","application/json");
        apiAntiReuseToken();
        JSONObject body = new JSONObject();
        body.put("namespace","IB_WITHDRAW");

        HttpResponse response = httpClient.getPostResponse(fullPath, header, body);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        printAPICPInfo(fullPath, body.toString(), String.valueOf(result));
        Integer resCode = result.getInteger("code");
        String resTxId = result.getJSONObject("data").getString("txId");
        assertTrue(resCode.equals(0) && resTxId != null, "Get TxId before IB transfer failed!!");
        return resTxId;
    }

    //Get 2fa code from DB for withdrawal
    public String setCode(String email){
        EmailDB emailDb = new EmailDB(GlobalProperties.ENV.getENV("alpha"), brand,regulator);
        JSONObject obj = emailDb.getCodeRecord(GlobalProperties.ENV.getENV("alpha"), brand, regulator, email);
            String code = obj.getJSONObject("vars").getString("CODE");
            return code;
    }
}
