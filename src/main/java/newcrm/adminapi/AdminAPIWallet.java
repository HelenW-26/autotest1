package newcrm.adminapi;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import newcrm.utils.api.DateUtils;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static newcrm.utils.api.HyTechUrl.*;
import static org.testng.Assert.assertTrue;

@Slf4j
public class AdminAPIWallet {

    public static void setBranchVersionHeader(String BranchVersion){
        Map<String, String> header = new HashMap<>();
        header.put("Branchversion",BranchVersion);
    }
    public static Map<String, String> setHeaderJSON(Map<String, Object> testData){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        headers.put("Cookie",testData.get("jsId").toString());
        headers.put("Accept","application/json, text/plain, */*");
        return headers;
    }

    public static Map<String, String> setHeader(Map<String, Object> testData){
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie",testData.get("jsId").toString());
        headers.put("Accept","application/json, text/plain, */*");
        return headers;
    }

    public static Map<String, String> setHeaderPDF(Map<String, Object> testData){
        Map<String, String> headers = new HashMap<>();
        headers.put("cookie",testData.get("jsId").toString());
        headers.put("Accept","application/pdf");
        return headers;
    }
    public static void frozen_detail_list(Map<String,Object> testData) {
        JSONObject request = new JSONObject();
        request.put("accountNo",testData.get("accountNo").toString());
        request.put("pageSize",10);
        request.put("pageNumber",1);

        String response = HttpRequest.post(testData.get("url").toString() + frozenDetailList)
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .body(request.toString())
                .execute().body();

        JSONObject result = JSONObject.parseObject(response);
        String fullurl = testData.get("url").toString() + frozenDetailList;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", setHeaderJSON(testData), request.toString(),result);
        assertTrue(result.get("code").equals("200"), frozenDetailList+" failed!! \n" + result);

    }

    /**
     * Bybit账户管理-币种参数
     * @return
     */
    public static void queryAccountCurrencyList(Map<String,Object> testData){
        String response =HttpRequest.get(testData.get("url") + adminqueryAccountCurrencyList)
                .header("cookie", testData.get("jsId").toString())
                .timeout(20000)
                .execute().body();
        JSONObject result = JSONObject.parseObject(response);

        String fullurl = testData.get("url").toString() + adminqueryAccountCurrencyList;
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie",testData.get("jsId").toString());
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "GET", headers, "",result);
        assertTrue(result.get("code").equals("200"),  adminqueryAccountCurrencyList+" failed!! \n" + result);
    }

    public static JSONObject walletaccountList(Map<String,Object> testData){
        String requestBody = "{\"pageSize\":20,\"pageNumber\":1,\"accountTypeList\":[],\"orders\":[{\"field\":\"userId\",\"direction\":\"DESC\"},{\"field\":\"accountTypeCode\",\"direction\":\"ASC\"},{\"field\":\"createTime\",\"direction\":\"DESC\"}],\"userName\":\"autotest\"}";
        String response = HttpRequest.post(testData.get("url").toString() +walletaccountlist)
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .body(requestBody)
                .execute().body();

        JSONObject result = JSONObject.parseObject(response);
        JSONObject re= (JSONObject) result.getJSONArray("data").get(0);
        testData.put("accountId", re.get("accountId"));
        testData.put("accountNo", re.get("accountNo"));
        testData.put("userId", re.get("userId"));
        String fullurl = testData.get("url").toString() + walletaccountlist;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", setHeaderJSON(testData), requestBody,result);
        assertTrue(result.get("code").equals("200"), walletaccountlist+" failed!! \n" + result);

        return result;
    }

    public static JSONObject currency_account_info(Map<String,Object> testData){
        JSONObject request = new JSONObject();
        request.put("accountId",testData.get("accountId").toString());
        request.put("accountNo",testData.get("accountNo").toString());
        String response = HttpRequest.post(testData.get("url").toString() +admincurrencyaccountinfo)
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .body(request.toString())
                .execute().body();

        JSONObject result = JSONObject.parseObject(response);
        String fullurl = testData.get("url").toString() + admincurrencyaccountinfo;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", setHeaderJSON(testData), "{accountId: 1028}",result);
        assertTrue(result.get("code").equals("200"), admincurrencyaccountinfo+" failed!! \n" + result);

        return result;
    }

    public static JSONObject walletAccountPermissions(Map<String,Object> testData){

        String response = HttpRequest.get(testData.get("url").toString() +adminaccountpermissionsUAT+"?userId="+testData.get("userId").toString())
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .execute().body();

        JSONObject result = JSONObject.parseObject(response);
        String fullurl = testData.get("url").toString() + adminaccountpermissionsUAT;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "GET", setHeaderJSON(testData), "",result);
        assertTrue(result.get("code").equals("200"), adminaccountpermissionsUAT+" failed!! \n" + result);
        return result;
    }

    public static JSONObject walletDepositPermissions(Map<String,Object> testData){

        String response = HttpRequest.get(testData.get("url").toString() +adminqueryPermission)
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .execute().body();

        JSONObject result = JSONObject.parseObject(response);
        String fullurl = testData.get("url").toString() + adminqueryPermission;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "GET", setHeaderJSON(testData), "",result);
        return result;
    }

      public static JSONObject walletAccounSetpermissions(Map<String,Object> testData){
        String response = HttpRequest.post(testData.get("url").toString() +adminsetPermission)
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .body("{\"accountNo\":"+testData.get("accountNo").toString()+",\"permissionList\":[{\"permissionCode\":\"wallet.deposit\",\"status\":1,\"permissionName\":\"Deposit\"},{\"permissionCode\":\"wallet.withdraw\",\"status\":0,\"permissionName\":\"Withdrawal\"},{\"permissionCode\":\"wallet.transfer\",\"status\":1,\"permissionName\":\"Transfer\"},{\"permissionCode\":\"wallet.convert\",\"status\":1,\"permissionName\":\"Convert\"}]}")
                .execute().body();

        JSONObject result = JSONObject.parseObject(response);
        String fullurl = testData.get("url").toString() + adminsetPermission;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", setHeaderJSON(testData), "{\"accountId\":"+testData.get("accountId").toString()+",\"permissionList\":[{\"permissionCode\":\"wallet.deposit\",\"status\":1,\"permissionName\":\"Deposit\"},{\"permissionCode\":\"wallet.withdraw\",\"status\":0,\"permissionName\":\"Withdrawal\"},{\"permissionCode\":\"wallet.transfer\",\"status\":1,\"permissionName\":\"Transfer\"},{\"permissionCode\":\"wallet.convert\",\"status\":1,\"permissionName\":\"Convert\"}]}",result);
        assertTrue(result.get("code").equals("200"), adminsetPermission+" failed!! \n" + result);

        return result;
    }

     public static JSONObject walletAccountFundFlowlist(Map<String,Object> testData){
        String body = "{\"userId\":\""+testData.get("accountId").toString()+"\",\"pageSize\":20,\"pageNumber\":1,\"accountNo\":\""+testData.get("accountNo").toString()+"\",\"accountType\":\"1\",\"operateStartTime\":\"2025-02-26\",\"operateEndTime\":\"2025-03-27\",\"orders\":[{\"field\":\"operateTime\",\"direction\":\"DESC\"}]}";
                String response = HttpRequest.post(testData.get("url").toString() +adminaccountfundflowlist)
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .body("{\"userId\":\""+testData.get("userId").toString()+"\",\"pageSize\":20,\"pageNumber\":1,\"accountNo\":\""+testData.get("accountNo").toString()+"\",\"accountType\":\"1\",\"operateStartTime\":\"2025-02-26\",\"operateEndTime\":\"2025-03-27\",\"orders\":[{\"field\":\"operateTime\",\"direction\":\"DESC\"}]}")
                .execute().body();

        JSONObject result = JSONObject.parseObject(response);
        String fullurl = testData.get("url").toString() + adminaccountfundflowlist;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", setHeaderJSON(testData), "{\"pageSize\":20,\"pageNumber\":1,\"accountNo\":"+testData.get("accountNo").toString()+",\"operateStartTime\":\"2025-02-26\",\"operateEndTime\":\"2025-03-27\",\"orders\":[{\"field\":\"operateTime\",\"direction\":\"DESC\"}]}",result);
        assertTrue(result.get("code").equals("200"), adminaccountfundflowlist+" failed!! \n" + result);
        return result;
    }

    public static JSONObject walletAccountFreeze(Map<String,Object> testData){
        String response = HttpRequest.post(testData.get("url").toString() +adminaccountfreeze)
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .body("{\"accountNo\":"+testData.get("accountNo").toString()+"}")
                .execute().body();

        JSONObject result = JSONObject.parseObject(response);
        String fullurl = testData.get("url").toString() + adminaccountfreeze;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", setHeaderJSON(testData), "{\"accountNo\":"+testData.get("accountNo").toString()+"}",result);
        assertTrue(result.get("code").equals("200"), adminaccountfreeze+" failed!! \n" + result);

        return result;
    }


    public static JSONObject walletAccountUnFreeze(Map<String,Object> testData){
        String response = HttpRequest.post(testData.get("url").toString() +adminaccountunfreeze)
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .body("{\"accountNo\":"+testData.get("accountNo").toString()+"}")
                .execute().body();

        JSONObject result = JSONObject.parseObject(response);
        String fullurl = testData.get("url").toString() + adminaccountunfreeze;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", setHeaderJSON(testData), "{\"accountNo\":"+testData.get("accountNo").toString()+"}",result);
        assertTrue(result.get("code").equals("200"), adminaccountunfreeze+" failed!! \n" + result);
        return result;
    }




    public static void platformFundqueryAccountCurrencyList(Map<String,Object> testData){
        String response =HttpRequest.get(testData.get("url") + adminqueryAccountCurrencyList)
                .header("cookie", testData.get("jsId").toString())
                .timeout(20000)
                .execute().body();
        JSONObject result = JSONObject.parseObject(response);
        String fullurl = testData.get("url").toString() + adminqueryAccountCurrencyList;
        Map<String, String> headers = new HashMap<>();
        headers.put("cookie",testData.get("jsId").toString());
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "GET", headers, "", result);
        assertTrue(result.get("code").equals("200"), adminqueryAccountCurrencyList+" failed!! \n" + result);

    }



    public static void walletIngressWithdrawDetail(Map<String,Object> testData){
        String response =HttpRequest.get(testData.get("url") + adminIngressWithdrawDetail +"?withdrawOrderNo="+testData.get("withdrawOrderNo").toString())
                .header("Cookie", testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .timeout(20000)
                .execute().body();
        JSONObject result = JSONObject.parseObject(response);
        String fullurl = testData.get("url").toString() + adminIngressWithdrawDetail;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "GET", setHeader(testData), "", result);
        assertTrue(result.get("code").equals("200"), adminIngressWithdrawDetail +" failed!! \n" + result);
    }

    public static void walletIngressWithdrawList(Map<String,Object> testData){
        JSONObject body = new JSONObject();
        JSONArray emptyArray = new JSONArray();
        body.put("orders",emptyArray);
        body.put("startTime","");
        body.put("endTime","");
        body.put("pageSize",10);
        body.put("pageNumber",1);

        String response =HttpRequest.post(testData.get("url") + adminIngressWithdrawList)
                .header("Cookie", testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .timeout(20000)
                .body(body.toString())
                .execute().body();
        JSONObject result = JSONObject.parseObject(response);
        JSONObject re= (JSONObject) result.getJSONArray("data").get(0);
        testData.put("withdrawOrderNo", re.get("withdrawOrderNo"));
        assertTrue(result.containsKey("total"), adminIngressWithdrawList +" not contains the key of total!! \n" + result);
        String fullurl = testData.get("url").toString() + adminIngressWithdrawList;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", setHeader(testData), body.toString(), result);
        assertTrue(result.get("code").equals("200"), adminIngressWithdrawList +" response code incorrect!! \n" + result);
    }

    public static void walletIngressWithdrawExport(Map<String,Object> testData){
        JSONObject body = new JSONObject();
        JSONArray emptyArray = new JSONArray();
        body.put("orders",emptyArray);
        body.put("startTime","");
        body.put("endTime","");
        HttpResponse response = HttpRequest.post(testData.get("url") + adminIngressWithdrawExport)
                .header("Cookie", testData.get("jsId").toString())
                .header("Accept","application/pdf")
                .body(body.toString())
                .execute();
        JSONObject result = JSONObject.parseObject(response.body());
        String fullurl = testData.get("url").toString() + adminIngressWithdrawExport;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", setHeaderPDF(testData), body.toString(), result);
        assertTrue(response.getStatus() == 200, adminIngressWithdrawExport +" response code incorrect!! \n" + response);
    }

    public static void walletOrderDetail(Map<String,Object> testData){

        String response =HttpRequest.get(testData.get("url") + adminWithdrawOrderDetail+"?id="+testData.get("id").toString())
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .timeout(20000)
                .execute().body();
        JSONObject result = JSONObject.parseObject(response);

        String fullurl = testData.get("url").toString() + adminWithdrawOrderDetail;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "GET", setHeaderJSON(testData), "", result);
        assertTrue(result.get("code").equals("200"), adminWithdrawOrderDetail +" failed!! \n" + result);
    }



    public static void walletDepositCurrentList(Map<String,Object> testData){
        String response =HttpRequest.get(testData.get("url") + admindepositcurencylist)
                .header("cookie", testData.get("jsId").toString())
                .timeout(20000)
                .execute().body();
        JSONObject result = JSONObject.parseObject(response);
        String fullurl = testData.get("url").toString() + admindepositcurencylist;
        Map<String, String> headers = new HashMap<>();
        headers.put("cookie",testData.get("jsId").toString());
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "GET", headers, "", result);
        assertTrue(result.get("code").equals("200"), admindepositcurencylist + " failed!! \n" + result);

    }

    public static JSONObject walletOrderList(Map<String,Object> testData){
        String strBody = "{\"pageSize\":20,\"pageNumber\":1,\"orders\":[{\"field\":\"updateTime\",\"direction\":\"DESC\"}],\"createStartTime\":\"\",\"createEndTime\":\"\"}";
        String response = HttpRequest.post(testData.get("url").toString() +admindepositwalletorderlist)
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .body(strBody)
                .execute().body();

        JSONObject result = JSONObject.parseObject(response);
        JSONObject re= (JSONObject) result.getJSONArray("data").get(0);
        testData.put("id", re.get("id"));
        String fullurl = testData.get("url").toString() + admindepositwalletorderlist;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", setHeaderJSON(testData), strBody, result);
        assertTrue(result.get("code").equals("200"), admindepositwalletorderlist+" failed!! \n" + result);

        return result;
    }


    public static JSONObject walletWithdrawOrderList(Map<String,Object> testData){
        String strBody ="{\"dateType\":\"create\",\"orders\":[{\"field\":\"createTime\",\"direction\":\"DESC\"}],\"startTime\":\"2025-02-27 00:00:00\",\"endTime\":\"2025-03-28 23:59:59\",\"pageSize\":20,\"pageNumber\":1}";
                String response = HttpRequest.post(testData.get("url").toString() +adminwalletorderlist)
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .body(strBody)
                .execute().body();

        JSONObject result = JSONObject.parseObject(response);
        String fullurl = testData.get("url").toString() + adminwalletorderlist;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", setHeaderJSON(testData), strBody, result);
        assertTrue(result.get("code").equals("200"), adminwalletorderlist+" failed!! \n" + result);
        return result;
    }


    public static void walletWithdrawOrderDetail(Map<String,Object> testData){

        String response =HttpRequest.get(testData.get("url") + adminwalletorderlistwalletWithdrawOrderNo + testData.get("wdOrder"))
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .timeout(20000)
                .execute().body();
        System.out.println(response);
        JSONObject result = JSONObject.parseObject(response);
        String fullurl = testData.get("url").toString() + adminwalletorderlistwalletWithdrawOrderNo;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "GET", setHeaderJSON(testData), "", result);
        assertTrue(result.get("code").equals("200"), adminwalletorderlistwalletWithdrawOrderNo+" failed!! \n" + result);
    }


    public static void walletWithdrawCurrencyList(Map<String,Object> testData){

        String response =HttpRequest.get(testData.get("url") + walletWithdrawCurrencyList + testData.get("wdOrder"))
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .timeout(20000)
                .execute().body();
        System.out.println(response);
        JSONObject result = JSONObject.parseObject(response);
        String fullurl = testData.get("url").toString() + walletWithdrawCurrencyList;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "GET", setHeaderJSON(testData), "", result);
        //assertTrue(result.get("code").equals("200"), walletWithdrawCurrencyList+" failed!! \n" + result);
    }

    public static void walletWithdrawOrderListDownload(Map<String,Object> testData){
        String strBody = "{\"dateType\":\"create\",\"orders\":[{\"field\":\"createTime\",\"direction\":\"DESC\"}],\"startTime\":\"2025-03-12 00:00:00\",\"endTime\":\"2025-04-10 23:59:59\"}";
        HttpResponse response = HttpRequest.post(testData.get("url") + walletWithdraworderListDownload)
                .header("Cookie", testData.get("jsId").toString())
                .header("Accept","application/pdf")
                .body(strBody)
                .execute();
        // 下载文件，只打印响应码和提示语
        JSONObject result = new JSONObject();
        result.put("statusCode", response.getStatus());
        result.put("success", response.isOk());
        String fullurl = testData.get("url").toString() + walletWithdraworderListDownload;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", setHeaderPDF(testData), strBody, result);
        assertTrue(response.getStatus() == 200, walletWithdraworderListDownload +" response code incorrect!! \n" + response);
    }

    public static JSONObject walletTransferOrderList(Map<String,Object> testData){
        String strBody = "{\"dateType\":\"create\",\"transferDirectionList\":[],\"currencyList\":[],\"fromType\":[],\"statusList\":[],\"pageSize\":20,\"pageNumber\":1,\"orders\":[{\"field\":\"createTime\",\"direction\":\"DESC\"}],\"createStartTime\":\"\",\"createEndTime\":\"\"}";
        String response = HttpRequest.post(testData.get("url").toString() +admintransferorderlist)
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .body(strBody)
                .execute().body();

        JSONObject result = JSONObject.parseObject(response);
        JSONObject re= (JSONObject) result.getJSONArray("data").get(0);
        testData.put("id", re.get("id"));
        String fullurl = testData.get("url").toString() + admintransferorderlist;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", setHeaderJSON(testData), strBody, result);
        assertTrue(result.get("code").equals("200"), admintransferorderlist+" failed!! \n" + result);
        return result;
    }

    public static void walletTransferOrderDetail(Map<String,Object> testData){

        String response =HttpRequest.get(testData.get("url") + admintransferorderdetailid+"?id="+testData.get("id").toString())
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .timeout(20000)
                .execute().body();
        System.out.println(response);
        JSONObject result = JSONObject.parseObject(response);
        String fullurl = testData.get("url").toString() + admintransferorderdetailid;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "GET", setHeaderJSON(testData), "", result);
        assertTrue(result.get("code").equals("200"), admintransferorderdetailid+" failed!! \n" + result);
    }

    public static JSONObject walletConvertOrderList(Map<String,Object> testData){
        String strBody = "{\"dateType\":\"create\",\"createStartTime\":\"2025-03-02\",\"createEndTime\":\"2025-03-31\",\"pageSize\":20,\"pageNumber\":1,\"orders\":[{\"field\":\"createTime\",\"direction\":\"DESC\"}]}";
        String response = HttpRequest.post(testData.get("url").toString() +adminconvertorderlist)
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .body(strBody)
                .execute().body();

        JSONObject result = JSONObject.parseObject(response);
        String fullurl = testData.get("url").toString() + adminconvertorderlist;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", setHeaderJSON(testData), strBody, result);
        assertTrue(result.get("code").equals("200"), adminconvertorderlist+" failed!! \n" + result);
        return result;
    }


    public static void walletConvertOrderListDownload(Map<String,Object> testData){
        String strBody = "{\"dateType\":\"create\",\"orders\":[{\"field\":\"createTime\",\"direction\":\"DESC\"}],\"createStartTime\":\"2025-03-09\",\"createEndTime\":\"2025-04-07\"}";
        HttpResponse response = HttpRequest.post(testData.get("url") + walletConvertDownload)
                .header("Cookie", testData.get("jsId").toString())
                .header("Accept","application/pdf")
                .body(strBody)
                .execute();
        // 下载文件，只打印响应码和提示语
        JSONObject result = new JSONObject();
        result.put("statusCode", response.getStatus());
        result.put("success", response.isOk());
        String fullurl = testData.get("url").toString() + walletConvertDownload;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", setHeaderPDF(testData), strBody, result);
        assertTrue(response.getStatus() == 200, walletConvertDownload +" response code incorrect!! \n" + response);
    }


    public static JSONObject walletBybitOrderList(Map<String,Object> testData){
        String strBody = "{\"dateType\":\"create\",\"createStartTime\":\"\",\"createEndTime\":\"\",\"pageSize\":20,\"pageNumber\":1,\"orders\":[{\"field\":\"createTime\",\"direction\":\"DESC\"}]}";
        String response = HttpRequest.post(testData.get("url").toString() +platformAccountqueryAccountList)
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .body(strBody)
                .execute().body();

        JSONObject result = JSONObject.parseObject(response);
        String fullurl = testData.get("url").toString() + platformAccountqueryAccountList;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "GET", setHeaderJSON(testData), strBody, result);
        assertTrue(result.get("code").equals("200"), platformAccountqueryAccountList+" failed!! \n" + result);
        return result;
    }

    public static void walletGetPlatformAccount(Map<String,Object> testData){

        String response =HttpRequest.get(testData.get("url") + queryAccountCurrencyList)
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .timeout(20000)
                .execute().body();
        System.out.println(response);
        JSONObject result = JSONObject.parseObject(response);
        String fullurl = testData.get("url").toString() + queryAccountCurrencyList;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "GET", setHeaderJSON(testData), "", result);
        assertTrue(result.get("code").equals("200"), queryAccountCurrencyList+" failed!! \n" + result);
    }

    public static void walletiIngressWithdrawChainList(Map<String,Object> testData){

        String response =HttpRequest.get(testData.get("url") + walletIngressChainList)
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .timeout(20000)
                .execute().body();
        System.out.println(response);
        JSONObject result = JSONObject.parseObject(response);
        String fullurl = testData.get("url").toString() + walletIngressChainList;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "GET", setHeaderJSON(testData), "", result);
        assertTrue(result.get("code").equals("200"), walletIngressChainList+" failed!! \n" + result);
    }

    public static JSONObject walletBybitgetFundList(Map<String,Object> testData){
        String endTime = DateUtils.getDateTime_HHMMSS();
        String strBody = "{\"startTime\":\"2025-03-03 00:00:00\",\"endTime\":\""+endTime+"\",\"orders\":[{\"field\":\"id\",\"direction\":\"DESC\"}],\"pageNumber\":1,\"pageSize\":20}";
        String response = HttpRequest.post(testData.get("url").toString() +platformFund)
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .body(strBody)
                .execute().body();

        JSONObject result = JSONObject.parseObject(response);
        JSONObject re= (JSONObject) result.getJSONArray("data").get(0);
        testData.put("accountId", re.get("accountId"));
        String fullurl = testData.get("url").toString() + platformFund;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", setHeaderJSON(testData), strBody, result);
        assertTrue(result.get("code").equals("200"), platformFund+" failed!! \n" + result);
        return result;
    }


    public static JSONObject wallettPlatFormAccountQueryAccountDetailList(Map<String,Object> testData){
        String strBody = "{\"accountId\":"+testData.get("accountId").toString()+",\"pageNumber\":1,\"pageSize\":20,\"orders\":[{\"field\":\"id\",\"direction\":\"DESC\"}]}";
        String response = HttpRequest.post(testData.get("url").toString() +walletPlatFormAccountQueryAccountDetailURL)
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .body(strBody)
                .execute().body();

        JSONObject result = JSONObject.parseObject(response);
        String fullurl = testData.get("url").toString() + walletPlatFormAccountQueryAccountDetailURL;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", setHeaderJSON(testData), strBody, result);
        assertTrue(result.get("code").equals("200"), walletPlatFormAccountQueryAccountDetailURL+" failed!! \n" + result);
        return result;
    }


    public static JSONObject walletHedgeConvertOrderList(Map<String,Object> testData){
        String strBody  = "{\"dateType\":\"create\",\"orders\":[{\"field\":\"createTime\",\"direction\":\"DESC\"}],\"createStartTime\":\"2025-03-03\",\"createEndTime\":\"2025-04-01\",\"pageSize\":20,\"pageNumber\":1}";
        String response = HttpRequest.post(testData.get("url").toString() + adminHedgeConvertOrderList)
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .body(strBody)
                .execute().body();

        JSONObject result = JSONObject.parseObject(response);
        String fullurl = testData.get("url").toString() + adminHedgeConvertOrderList;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", setHeaderJSON(testData), strBody, result);
        assertTrue(result.get("code").equals("200"), adminHedgeConvertOrderList +" failed!! \n" + result);
        return result;
    }

    public static void walletHedgeConvertOrderListDownload(Map<String,Object> testData){
        String strBody  = "{\"dateType\":\"create\",\"orders\":[{\"field\":\"createTime\",\"direction\":\"DESC\"}],\"createStartTime\":\"2025-03-09\",\"createEndTime\":\"2025-04-07\"}";
        HttpResponse response = HttpRequest.post(testData.get("url") + walletHedgeDownload)
                .header("Cookie", testData.get("jsId").toString())
                .header("Accept","application/pdf")
                .body(strBody)
                .execute();
        // 下载文件，只打印响应码和提示语
        JSONObject result = new JSONObject();
        result.put("statusCode", response.getStatus());
        result.put("success", response.isOk());
        String fullurl = testData.get("url").toString() + walletHedgeDownload;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", setHeaderPDF(testData), strBody, result);
        assertTrue(response.getStatus() == 200, walletHedgeDownload +" response code incorrect!! \n" + response);
    }

    public static void walletTransferOrderListDownload(Map<String,Object> testData){
        String strBody  = "{\"transferDirectionList\":[],\"currencyList\":[],\"fromType\":[],\"statusList\":[],\"pageSize\":20,\"pageNumber\":1,\"orders\":[{\"field\":\"createTime\",\"direction\":\"DESC\"}],\"createStartTime\":\"2025-03-19\",\"createEndTime\":\"2025-04-17\"}";
        HttpResponse response = HttpRequest.post(testData.get("url") + TransferOrderListDownloadURL)
                .header("Cookie", testData.get("jsId").toString())
                .header("Accept","application/pdf")
                .body(strBody)
                .execute();
        // 下载文件，只打印响应码和提示语
        JSONObject result = new JSONObject();
        result.put("statusCode", response.getStatus());
        result.put("success", response.isOk());
        String fullurl = testData.get("url").toString() + TransferOrderListDownloadURL;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", setHeaderPDF(testData), strBody, result);
        assertTrue(response.getStatus() == 200, TransferOrderListDownloadURL +" response code incorrect!! \n" + response);
    }

    public static void walletAccountFundFlowDownload(Map<String,Object> testData){
        String strBody = "pageSize=20&pageNumber=1&accountNo=AUV21110126329&operateStartTime=2025-03-19&operateEndTime=2025-04-17&userId=10126329&accountId=1134&field=operateTime&direction=DESC";
        HttpResponse response = HttpRequest.get(testData.get("url") + walletAccountFundFlowDownloadUrl)
                .header("Cookie", testData.get("jsId").toString())
                .header("Accept","application/pdf")
                .form(strBody)
                .execute();
        // 下载文件，只打印响应码和提示语
        JSONObject result = new JSONObject();
        result.put("statusCode", response.getStatus());
        result.put("success", response.isOk());
        String fullurl = testData.get("url").toString() + walletAccountFundFlowDownloadUrl;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", setHeaderPDF(testData), strBody, result);
        assertTrue(response.getStatus() == 200, walletAccountFundFlowDownloadUrl +" response code incorrect!! \n" + response);
    }

    public static void walletDepositOrderListDownload(Map<String,Object> testData){
        String strBody  = "{\"dateType\":\"create\",\"orders\":[{\"field\":\"createTime\",\"direction\":\"DESC\"}],\"createStartTime\":\"2025-03-09\",\"createEndTime\":\"2025-04-07\"}";
        HttpResponse response = HttpRequest.post(testData.get("url") + walletDepositOrderListDownloadUrl)
                .header("Cookie", testData.get("jsId").toString())
                .header("Accept","application/pdf")
                .body(strBody)
                .execute();
        // 下载文件，只打印响应码和提示语
        JSONObject result = new JSONObject();
        result.put("statusCode", response.getStatus());
        result.put("success", response.isOk());
        String fullurl = testData.get("url").toString() + walletDepositOrderListDownloadUrl;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", setHeaderPDF(testData), strBody, result);
        assertTrue(response.getStatus() == 200, walletDepositOrderListDownloadUrl +" response code incorrect!! \n" + response);
    }

    public static void walletAccountDownload(Map<String,Object> testData){
        String strBody  = "{\"dateType\":\"create\",\"orders\":[{\"field\":\"createTime\",\"direction\":\"DESC\"}],\"createStartTime\":\"2025-03-09\",\"createEndTime\":\"2025-04-07\"}";
        HttpResponse response = HttpRequest.post(testData.get("url") + walletAccountDownload)
                .header("Cookie", testData.get("jsId").toString())
                .header("Accept","application/pdf")
                .body(strBody)
                .execute();
        // 下载文件，只打印响应码和提示语
        JSONObject result = new JSONObject();
        result.put("statusCode", response.getStatus());
        result.put("success", response.isOk());
        String fullurl = testData.get("url").toString() + walletAccountDownload;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", setHeaderPDF(testData), strBody, result);
        assertTrue(response.getStatus() == 200, walletAccountDownload +" response code incorrect!! \n" + response);
    }

    //wrong API path ???
    public static void walletBybitTransactionRecords(Map<String,Object> testData){

        String response =HttpRequest.get(testData.get("url") + adminqueryAccountCurrencyList)
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .timeout(20000)
                .execute().body();
        System.out.println(response);
        JSONObject result = JSONObject.parseObject(response);
        String fullurl = testData.get("url").toString() + adminqueryAccountCurrencyList;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "GET", setHeaderJSON(testData), "", result);
        assertTrue(result.get("code").equals("200"), adminqueryAccountCurrencyList+" failed!! \n" + result);
    }

    public static JSONObject walletChannelOrderList(Map<String,Object> testData){
        String strBody = "{\"depositStatusList\":null,\"depositBusinessTypeList\":null,\"isCollectList\":null,\"pageSize\":20,\"pageNumber\":1,\"orders\":[{\"field\":\"updateTime\",\"direction\":\"DESC\"}],\"createStartTime\":\"\",\"createEndTime\":\"\"}";
        String response = HttpRequest.post(testData.get("url").toString() + walletChannelList)
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .body(strBody)
                .execute().body();

        JSONObject result = JSONObject.parseObject(response);
        JSONObject re= (JSONObject) result.getJSONArray("data").get(0);
        testData.put("id", re.get("id"));
        String fullurl = testData.get("url").toString() + walletChannelList;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", setHeaderJSON(testData), strBody, result);
        assertTrue(result.get("code").equals("200"), walletChannelList +" failed!! \n" + result);
        return result;
    }

    public static void walletDepositChannelOrderDetail(Map<String,Object> testData){

        String response =HttpRequest.get(testData.get("url") + walletChannelDetail+"?id="+testData.get("id").toString())
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .timeout(20000)
                .execute().body();
        JSONObject result = JSONObject.parseObject(response);
        String fullurl = testData.get("url").toString() + walletChannelDetail;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "GET", setHeaderJSON(testData), "", result);
        assertTrue(result.get("code").equals("200"), walletChannelDetail+" failed!! \n" + result);

    }

    public static void walletDepositChainList(Map<String,Object> testData){

        String response =HttpRequest.get(testData.get("url") + walletChaninList)
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .timeout(20000)
                .execute().body();
        System.out.println(response);
        JSONObject result = JSONObject.parseObject(response);
        String fullurl = testData.get("url").toString() + walletChaninList;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "GET", setHeaderJSON(testData), "", result);
        assertTrue(result.get("code").equals("200"), walletChaninList+" failed!! \n" + result);

    }

    public static JSONObject walletChannelTransferRecordList(Map<String,Object> testData){
        String strBody  = "{\"pageSize\":20,\"pageNumber\":1,\"statusList\":[],\"fromMemberTypeList\":null,\"toMemberTypeList\":null,\"orders\":[{\"field\":\"operateTime\",\"direction\":\"DESC\"}],\"createStartTime\":\"\",\"createEndTime\":\"\"}";
        String response = HttpRequest.post(testData.get("url").toString() + walletChannelTransferRecordlist)
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .body(strBody)
                .execute().body();

        JSONObject result = JSONObject.parseObject(response);
        JSONObject re= (JSONObject) result.getJSONArray("data").get(0);
        testData.put("id", re.get("id"));
        String fullurl = testData.get("url").toString() + walletChannelTransferRecordlist;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", setHeaderJSON(testData), strBody, result);
        assertTrue(result.get("code").equals("200"), walletChannelTransferRecordlist +" failed!! \n" + result);
        return result;
    }


    public static JSONObject walletDepositWalletRewardOrderList(Map<String,Object> testData){
        String response = HttpRequest.get(testData.get("url").toString() + walletDepositWalletRewardOrderListUrl + "?id=" + testData.get("id").toString())
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .execute().body();

        JSONObject result = JSONObject.parseObject(response);
        testData.put("rewardid",result.getJSONObject("data").getString("id"));
        String fullurl = testData.get("url").toString() + walletDepositWalletRewardOrderListUrl;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "GET", setHeaderJSON(testData), "", result);
        assertTrue(result.get("code").equals("200"), walletDepositWalletRewardOrderListUrl +" failed!! \n" + result);
        return result;
    }

    public static void walletDepositRewardOrderDetail(Map<String,Object> testData){

        String response =HttpRequest.get(testData.get("url") + walletDepositRewardOrderDetail+"?id="+testData.get("rewardid").toString())
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .timeout(20000)
                .execute().body();
        JSONObject result = JSONObject.parseObject(response);
        String fullurl = testData.get("url").toString() + walletDepositRewardOrderDetail;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "GET", setHeaderJSON(testData), "", result);
        assertTrue(result.get("code").equals("200"), walletDepositRewardOrderDetail+" failed!! \n" + result);
    }


    public static void walletDepositChannelTransferRecordDetail(Map<String,Object> testData){

        String response =HttpRequest.get(testData.get("url") + walletChannelTransferRecordDetail+"?id="+testData.get("id").toString())
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .timeout(20000)
                .execute().body();
        JSONObject result = JSONObject.parseObject(response);
        String fullurl = testData.get("url").toString() + walletChannelTransferRecordDetail;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "GET", setHeaderJSON(testData), "", result);
        assertTrue(result.get("code").equals("200"), walletChannelTransferRecordDetail+" failed!! \n" + result);
    }

    public static JSONObject walletPlatformAssetQueryToday(Map<String,Object> testData){

        String response = HttpRequest.post(testData.get("url").toString() + walletQueryTodayAsset)
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .body("{}")
                .execute().body();

        JSONObject result = JSONObject.parseObject(response);
        String fullurl = testData.get("url").toString() + walletQueryTodayAsset;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", setHeaderJSON(testData), "{}", result);
        assertTrue(result.get("code").equals("200"), walletQueryTodayAsset +" failed!! \n" + result);
        return result;
    }

    public static JSONObject walletPlatformAssetQueryYesterday(Map<String,Object> testData){

        String response = HttpRequest.post(testData.get("url").toString() + walletQueryYesterAsset)
                .header("Content-Type","application/json")
                .header("Cookie",testData.get("jsId").toString())
                .header("Accept","application/json, text/plain, */*")
                .body("{}")
                .execute().body();

        JSONObject result = JSONObject.parseObject(response);
        String fullurl = testData.get("url").toString() + walletQueryYesterAsset;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", setHeaderJSON(testData), "{}", result);
        assertTrue(result.get("code").equals("200"), walletQueryYesterAsset +" failed!! \n" + result);
        return result;
    }

    /**
     * Print API info at the end of each tests
     */
    public static void printSuccessAPIInfo(String Brand, String fullUrl,String method, Map<String, String> headers, String requestBody ,com.alibaba.fastjson.JSONObject result) {

        // 准备调试信息
        String debugInfo = "调用时间: " + new Date() + "\n" +
                "请求方法: "+ method + "\n" +
                "URL: " + fullUrl + "\n";

        // 创建Allure步骤，包含单独的附件
        Allure.step(fullUrl + " API调用 - ", () -> {
            // 请求头
            Allure.addAttachment("请求头", "application/json", headers.toString());

            // 请求体
            Allure.addAttachment("请求体", "text/plain",
                    requestBody != null? requestBody : "");

            // 响应内容
            Allure.addAttachment("响应内容", "application/json", result.toString());

            // 调试信息
            Allure.addAttachment("调试信息", "text/plain", debugInfo);
        });
        // 5. 控制台输出
        System.out.println("*********** API调用成功 ***********");
        System.out.printf("%-15s: %s\n", "Brand", Brand);
        System.out.printf("%-15s: %s\n", "URL", fullUrl);
        System.out.printf("%-15s: %s\n", "请求头", headers);
        System.out.printf("%-15s: %s\n", "请求体", requestBody);
        System.out.printf("%-15s: %s\n", "响应", result);
        System.out.println("**********************************");
    }
}

