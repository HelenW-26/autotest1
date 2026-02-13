package newcrm.cpapi;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.REGULATOR;
import newcrm.utils.api.*;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static newcrm.adminapi.AdminAPIWallet.printSuccessAPIInfo;
import static newcrm.utils.api.HyTechUrl.*;
import static org.testng.Assert.*;

@Slf4j
public class CPAPIPaymentBase extends CPAPIBase{


    public String filePath = "testData/InitDepositInterfData.yaml";

    public CPAPIPaymentBase(String url, String cplogin, String password) {
        super(url, cplogin, password);
    }

    public CPAPIPaymentBase(String url, String cplogin, String password, BRAND brand, REGULATOR regulator) {
        super(url, cplogin, password, brand, regulator); // delegate to CPAPIBase's 5-arg constructor
    }

    @DataProvider(name = "testData")
    public Object[][] getTestData()
    {
        return YamlDataProviderUtils.getTestData(filePath);
    }


    @Test(dataProvider = "testData")
    public static void init_deposit(Map<String,Object> testData)
    {

        Map<String,String> map  = new HashMap<>();
        map.put("mt4Account",testData.get("mt4Account").toString());
        map.put("operateAmount","100");
        map.put("feBrand","vantage");
        map.put("language","en_US");
        map.put("redemptionCode",null);
        map.put("voucherApplied",null);

        String response = HttpRequest.post(testData.get("url").toString() + "/web-api/cp/api/deposit/init_deposit")
                .header("content-type","application/json;charset=UTF-8")
                .header("token",testData.get("token").toString())
                .body(map.toString())
                .execute().body();
        com.alibaba.fastjson.JSONObject result  = com.alibaba.fastjson.JSONObject.parseObject(response);
        testData.put("token",result.getString("data"));

        Map<String, String> headers = new HashMap<>();
        headers.put("content-type","application/json;charset=UTF-8");
        headers.put("token",testData.get("token").toString());
        String fullurl = testData.get("url").toString() + "/web-api/cp/api/deposit/init_deposit";
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", headers, map.toString(), result);
        //assertTrue(result.get("code").equals("200"), "/cp/api/deposit/init_deposit"+" failed!! \n" + result);

        //System.out.println(testData.put("token",result.getString("data")));
    }

    @Test(dataProvider = "testData")
    public static void cpscheckoutcashier(Map<String,Object> testData){
        String str=testData.get("token").toString();
        String str1=str.substring(0,str.indexOf("="));
        String str2=str.substring(str1.length()+1,str.length());
        String str3=str2.substring(0,str2.indexOf("&"));
//        System.out.println("选择渠道");
//        System.out.println(testData.get("channel_id").toString());
        try {
            Map<String,String> map  = new HashMap<>();
            map.put("content-type","application/json;charset=UTF-8");
            map.put("Authorization","Bearer "+str3);
            map.put("Referer",testData.get("token").toString());
            cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject();

            jsonObject.putOnce("channel_id", testData.get("channel_id"));///api/cashier/checkout
            HttpClientResult httpClientResult = HttpClientUtils.doPatchJson(HyTechUrl.pcsPaymentUrl+ cashier_checkout, map, jsonObject);
            System.out.println(httpClientResult.getJsonObject().toString());

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test(dataProvider = "testData")
    public static void depositcps(Map<String,Object> testData){
        System.out.println("提交");
        String str=testData.get("token").toString();
        String str1=str.substring(0,str.indexOf("="));
        String str2=str.substring(str1.length()+1,str.length());
        String str3=str2.substring(0,str2.indexOf("&"));

        cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject();

        jsonObject.putOnce("payment_channel",testData.get("payment_channel"));

        //System.out.println(jsonObject.toString());
        String response = HttpRequest.post(HyTechUrl.pcsPaymentUrl+HyTechUrl.deposit_cps)
                .header("content-type","application/json;charset=UTF-8")
                .header("Authorization","Bearer "+str3)
                .header("Referer",testData.get("token").toString())
                .body(jsonObject.toString())
                .execute().body();
        System.out.println("query_server 查询返回参数是：" + response);
        com.alibaba.fastjson.JSONObject result  = com.alibaba.fastjson.JSONObject.parseObject(response);
        Reporter.log("Query_server Back Paramar：" +result);
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type","application/json;charset=UTF-8");
        headers.put("Authorization","Bearer "+str3);
        headers.put("token",testData.get("token").toString());
        String fullurl = HyTechUrl.pcsPaymentUrl+HyTechUrl.deposit_cps;
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", headers, jsonObject.toString(), result);
        //assertTrue("", HyTechUrl.pcsPaymentUrl+HyTechUrl.deposit_cps+" failed!! \n" + result);
        //System.out.println(result.toString());
    }

    @Test(dataProvider = "testData")
    public static void cryptocheckoutcashier(Map<String,Object> testData){

        String str=testData.get("token").toString();
        String str1=str.substring(0,str.indexOf("="));
        String str2=str.substring(str1.length()+1,str.length());
        String str3=str2.substring(0,str2.indexOf("&"));
        //System.out.println("----------Select Channer---------");

        try {
            Map<String,String> map  = new HashMap<>();
            map.put("content-type","application/json;charset=UTF-8");
            map.put("Authorization","Bearer "+str3);
            map.put("Referer",testData.get("token").toString());
            cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject();
            jsonObject.putOnce("channel_id",testData.get("channel_id"));
            //System.out.println(cp_domain+"api/cashier/checkout");
            HttpClientResult httpClientResult = HttpClientUtils.doPatchJson(testData.get("url").toString()+cashier_checkout, map, jsonObject);

            JSONObject result  = JSON.parseObject(httpClientResult.getJsonObject().toString());
            String fullurl = testData.get("url").toString()+cashier_checkout;
            printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", map, jsonObject.toString(), result);
            //assertTrue(result.get("code").equals("200"), "/cp/api/deposit/init_deposit"+" failed!! \n" + result);
            //System.out.println(httpClientResult.toString());

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Test(dataProvider = "testData")
    public static void checkoutcrypto(Map<String,Object> testData){
        String str=testData.get("token").toString();
        String str1=str.substring(0,str.indexOf("="));
        String str2=str.substring(str1.length()+1,str.length());
        String str3=str2.substring(0,str2.indexOf("&"));
        //System.out.println("Commit Crypto Channel Deposit");

        try {
            Map<String,String> map  = new HashMap<>();
            map.put("content-type","application/json;charset=UTF-8");
            map.put("Authorization","Bearer "+str3);
            map.put("Referer",testData.get("token").toString());
            cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject();
            jsonObject.putOnce("channel_id",testData.get("channel_id"));
            HttpClientResult httpClientResult = HttpClientUtils.doPatchJson(HyTechUrl.pcsPaymentUrl+HyTechUrl.cashier_crypto, map, jsonObject);
            System.out.println("Crypto Deposit Success...............");
            System.out.println(httpClientResult.getJsonObject().toString());
           // System.out.println(httpClientResult.getJsonObject().get("crypto_info").toString());
           // Reporter.log("Query_server Back Paramar：" +httpClientResult.getJsonObject().get("crypto_info").toString());
            System.out.println("Crypto Deposit test....");

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test(dataProvider = "testData")
    public static void cryptodepositcps(Map<String,Object> testData){
        System.out.println("---Commit Deposit---");
        String str=testData.get("token").toString();
        String str1=str.substring(0,str.indexOf("="));
        String str2=str.substring(str1.length()+1,str.length());
        String str3=str2.substring(0,str2.indexOf("&"));

        String response = HttpRequest.post(HyTechUrl.pcsPaymentUrl+deposit_crypto)
                .header("content-type","application/json;charset=UTF-8")
                .header("Authorization","Bearer "+str3)
                .header("Referer",testData.get("token").toString())
                .body("{}")
                .execute().body();

        System.out.println("Query_server Back Paramar：" + response);
        Reporter.log("Query_server Back Paramar：" +response);
        System.out.println(response);
    }

    @Test(dataProvider = "testData")
    public static void channel_forwarderdeposit(Map<String,Object> testData){
        String str=testData.get("token").toString();
        String str1=str.substring(0,str.indexOf("="));
        String str2=str.substring(str1.length()+1,str.length());
        String str3=str2.substring(0,str2.indexOf("&"));

        cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject();
        jsonObject.putOnce("applicationNotes","");
        jsonObject.putOnce("payment_channel",testData.get("payment_channel"));
        jsonObject.putOnce("endpoint","/bridgepay");

        System.out.println(jsonObject.toString());
        String response = HttpRequest.post(HyTechUrl.pcsPaymentUrl+ channel_forwarder)
                .header("content-type","application/json;charset=UTF-8")
                .header("Authorization","Bearer "+str3)
                .header("Referer",testData.get("token").toString())
                .body(jsonObject.toString())
                .execute().body();
        System.out.println("query_server 查询返回参数是：" + response);
        com.alibaba.fastjson.JSONObject result  = com.alibaba.fastjson.JSONObject.parseObject(response);

        String fullurl = HyTechUrl.pcsPaymentUrl+ channel_forwarder;
        Map<String, String> headers  = new HashMap<>();
        headers.put("content-type","application/json;charset=UTF-8");
        headers.put("Authorization","Bearer "+str3);
        headers.put("token",testData.get("token").toString());
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", headers, jsonObject.toString(), result);
        //assertTrue(result.get("code").equals("200"), HyTechUrl.pcsPaymentUrl+ channel_forwarder+" failed!! \n" + result);

        System.out.println(HyTechUrl.pcsPaymentUrl+channel_forwarder);
        Reporter.log("Query_server Back Paramar：" +result);

    }

    @Test(dataProvider = "testData")
    public static void newzealanddepositcps(Map<String,Object> testData)
    {
        String str=testData.get("token").toString();
        String str1=str.substring(0,str.indexOf("="));
        String str2=str.substring(str1.length()+1,str.length());
        String str3=str2.substring(0,str2.indexOf("&"));
        String strBody = "{\"file_path\":\"\",\"ibt_bank\":\"VFX Equals\",\"fileList\":[\"https://nv-hytech-crm-au-s3-file-store.s3.amazonaws.com/other/c6e4732812c042e1bfeeb38e91c50c48.jpeg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20250110T020847Z&X-Amz-SignedHeaders=host&X-Amz-Expires=600&X-Amz-Credential=AKIA2P6JJWBAK4DSMHID%2F20250110%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Signature=31aded4a74ea70d4ee68cc0d9f77422f1966dbf1a0009a72278a96fcf6c74cf0\"],\"attachVariables\":\"{\\\"file_path\\\":[\\\"/other/c6e4732812c042e1bfeeb38e91c50c48.jpeg\\\"],\\\"ibt_bank\\\":\\\"VFX Equals\\\"}\",\"applicationNotes\":\"\",\"payment_channel\":"+testData.get("payment_channel")+"}";
        String response = HttpRequest.post(HyTechUrl.pcsPaymentUrl+"/api/cashier/v1/deposit/cps")
                .header("content-type","application/json;charset=UTF-8")
                .header("Authorization","Bearer "+str3)
                .header("Referer",testData.get("token").toString())
                .body(strBody)
                .execute().body();
        System.out.println("query_server query paramar back：" + response);
        com.alibaba.fastjson.JSONObject result  = com.alibaba.fastjson.JSONObject.parseObject(response);
        String fullurl = HyTechUrl.pcsPaymentUrl+"/api/cashier/v1/deposit/cps";
        Map<String, String> headers  = new HashMap<>();
        headers.put("content-type","application/json;charset=UTF-8");
        headers.put("Authorization","Bearer "+str3);
        headers.put("token",testData.get("token").toString());
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", headers, strBody, result);
        //assertTrue(result.get("code").equals("200"), HyTechUrl.pcsPaymentUrl+ "/api/cashier/v1/deposit/cps"+" failed!! \n" + result);
        // System.out.println(result.toString());
        //System.out.println(result.get("data").toString());
        Reporter.log("Query_server Back Paramar：" +result.toString());
    }

    @Test(dataProvider = "testData")
    public static void depositIBT(Map<String,Object> testData){
//        testData.put("email", "autotestetdfyzri@testcrmautomation.com");
//        loginWithEmail2(testData);
//        IBTinit_deposit(testData);
        System.out.println("----Commit----");
        String str=testData.get("token").toString();
        String str1=str.substring(0,str.indexOf("="));
        String str2=str.substring(str1.length()+1,str.length());
        String str3=str2.substring(0,str2.indexOf("&"));
        System.out.println(str3);
        cn.hutool.json.JSONObject jsonObject = new cn.hutool.json.JSONObject();
        ArrayList list=new ArrayList();
        list.add("https://crm-au-alpha.s3.ap-southeast-1.amazonaws.com/other/0898d17a6d4049a78e99ed7854af986b.jpeg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20241210T101407Z&X-Amz-SignedHeaders=host&X-Amz-Expires=599&X-Amz-Credential=AKIA6LZROUZKAQU5T4EI%2F20241210%2Fap-southeast-1%2Fs3%2Faws4_request&X-Amz-Signature=bef9560466f1b29ee456fdf7a3b9a60443e2650c2f63ee5af21065050d897bc4");
        jsonObject.putOnce("fileList",list);
        jsonObject.putOnce("applicationNotes","");
        jsonObject.putOnce("payment_channel",testData.get("payment_channel"));
        jsonObject.putOnce("endpoint","/broker_to_broker_payment");
        String response = HttpRequest.post(HyTechUrl.pcsPaymentUrl+ channel_forwarder)
                .header("content-type","application/json;charset=UTF-8")
                .header("Authorization","Bearer "+str3)
                .header("Referer",testData.get("token").toString())
                .body(jsonObject.toString())
                .execute().body();
        System.out.println("query_server query paramar back：" + response);
        com.alibaba.fastjson.JSONObject result  = com.alibaba.fastjson.JSONObject.parseObject(response);
        String fullurl = HyTechUrl.pcsPaymentUrl+ channel_forwarder;
        Map<String, String> headers  = new HashMap<>();
        headers.put("content-type","application/json;charset=UTF-8");
        headers.put("Authorization","Bearer "+str3);
        headers.put("token",testData.get("token").toString());
        printSuccessAPIInfo(testData.get("brand").toString(), fullurl, "POST", headers, jsonObject.toString(), result);
        //to-do 需要添加断言
        //assertTrue(result.get("code").equals("0"), 0+" failed!! \n" + result);
        //System.out.println(result.get("data").toString());
        Reporter.log("Query_server Back Paramar：" +result.get("data").toString());
    }

    public JSONObject apiQueryRebateList() throws Exception {
        header.put("Content-Type","application/json");
        HyTechUtils.genXSourceHeader(header);

        String fullPath = this.url + HyTechUrl.IB_REBATELIST;
        HttpResponse response = httpClient.getGetResponse(fullPath, header);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
        printAPICPInfo(fullPath, "", String.valueOf(result));

        JSONArray rebAcctList = result.getJSONObject("data").getJSONArray("list");
        assertFalse(result.isEmpty(), "Get IB Account List failed!!");
        assertFalse(rebAcctList.isEmpty(),"API response - list object is empty");
        return result;
    }


    public void apiAntiReuseToken() {
        try {
            HyTechUtils.genXSourceHeader(header);
            JSONObject result = sendCPAPIGETrequest(HyTechUrl.ANTI_REUSE);
            Integer resCode = result.getInteger("code");
            String antiReuseToken = result.getString(("data"));
            assertTrue(resCode.equals(0) && antiReuseToken.contains("portal.token"), "Get anti-reuse token failed!!\n" + result);
            header.put("token", antiReuseToken);
        } catch (Exception e) {
            System.err.println("An error occurred while getting anti-reuse token: " + e.getMessage());
            e.printStackTrace();
        }
    }



}
