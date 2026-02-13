package newcrm.testcases.crmapi;

import com.alibaba.fastjson.JSONObject;
import newcrm.cpapi.CPAPIPaymentOthers;
import javax.validation.constraints.Email;

public class CPAPIPaymentOthersTestCases {

    protected String initbrand;
    protected String initserver;
    protected Object data[][];
    protected Object ibData[][];
    protected Object email[][];
    protected CPAPIPaymentOthers cpAPIPaymentOthers;
    protected CPAPIPaymentOthers ibAPIPaymentOthers;
    protected Email emailDB;


    /* Client Funds Transfer API */
    public void apiTransfer() throws Exception {
        cpAPIPaymentOthers.apiFundsTransfer(1.0);
    }

    /* Client Funds Transfer Others API */
    public void apiTransferOthers() throws Exception {
        JSONObject accInfo = cpAPIPaymentOthers.apiFTAccInfo();
        cpAPIPaymentOthers.apiFTPermission(accInfo);
        cpAPIPaymentOthers.apiFTTransHist();
        cpAPIPaymentOthers.apiFTCurrList();
        cpAPIPaymentOthers.apiFTCurrRate();
        cpAPIPaymentOthers.apiFTDeductCred();
        cpAPIPaymentOthers.apiExpTransHist();
        cpAPIPaymentOthers.apiFTBlacklist();
    }


    /* IB Funds Transfer To Own Account API */
    public void apiTransferOwnIB() throws Exception {
        JSONObject ibAcc = ibAPIPaymentOthers.apiGetRebateAndTradingAccIB();
        ibAPIPaymentOthers.apiTransferToOwnIB(ibAcc);
    }

    /* IB Funds Transfer To Sub-IB Account API */
    public void apiTransferSubIBClientIB() throws Exception {
        String toAcc = ((String) ibData[0][6]);
        JSONObject fromAcc = ibAPIPaymentOthers.apiGetRebateAccIB();
        String ibEmail = (String) ibData[0][1];

        ibAPIPaymentOthers.apiGetSubIBInfo(fromAcc, toAcc);
        ibAPIPaymentOthers.apiTransferToSubIBClientIB(fromAcc, toAcc,ibEmail);
    }

    /* IB Funds Transfer Others API */
    public void apiTransferOthersIB() throws Exception {
        ibAPIPaymentOthers.apiQueryBalanceIB();
        ibAPIPaymentOthers.apiEligibleTransferIB();
        ibAPIPaymentOthers.apiGetRebateAndTradingAccIB();
        ibAPIPaymentOthers.apiDownloadTransTemplate();
    }
}
