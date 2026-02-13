package newcrm.utils.http;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import newcrm.utils.api.HyTechUtils;
import utils.LogUtils;

import java.util.Map;

/**
 *
 */
@Slf4j
public class HytechHttp {
    public static JSONObject postRawText(Map<String,String> param, String url){
        String v2 = param.get("v2");
        String jsId = param.get("jsId");
        param.remove("v2");
        param.remove("jsId");
//        log.info(HyTechUtils.mapToString(param));
        String res = HttpRequest.post(url)
                .header("current-regulator", v2)
                .header("cookie",jsId)
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .body(HyTechUtils.mapToString(param))
                .timeout(20000)
                .execute().body();
        JSONObject jsonObject = new JSONObject(res);
        LogUtils.info(url+jsonObject.toString());
        return  jsonObject;
    }

    public static JSONObject postRawJson(Map<String,String> param, String url){
        String v2 = param.get("v2");
        String jsId = param.get("jsId");
        param.remove("v2");
        param.remove("jsId");

        String res = HttpRequest.post(url)
                .header("Current-Regulator", v2)
                .header("cookie",jsId)
                .header("content-type", "application/json;charset=UTF-8")
                .body(param.get("json"))
                .timeout(20000)
                .execute().body();
        JSONObject jsonObject = new JSONObject(res);
        LogUtils.info(url+jsonObject.toString());
        return  jsonObject;
    }

    public static String adminSendPost(String url,Map<String,Object> testData,String request){
        if(isJson(request)){
            return HttpRequest.post(url)
                    .header("Current-Regulator", testData.get("regulator").toString())
                    .header("Cookie", testData.get("jsId").toString())
                    .header("Content-Type", "application/json;charset=UTF-8")
                    .body(request)
                    .timeout(20000)
                    .execute().body();
        }else{
            return HttpRequest.post(url)
                    .header("Current-Regulator", testData.get("regulator").toString())
                    .header("cookie", testData.get("jsId").toString())
                    .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                    .body(request)
                    .timeout(20000)
                    .execute().body();
        }

    }

    public  static String httpReqWithToken(String url,String params,String token){
        if(isJson(params)){
            return HttpRequest.post(url)
                    .header("token",token)
                    .header("content-type", "application/json;charset=UTF-8")
                    .body(params)
//                    .timeout(30000)
                    .execute().body();
        }else {
            return HttpRequest.post(url)
                    .header("token", token)
                    .header("content-type", "application/x-www-form-urlencoded")
                    .body(params)
//                    .timeout(30000)
                    .execute().body();
        }
    }
;

    public static boolean isJson(String content) {
        try {
            com.alibaba.fastjson.JSONObject jsonStr = com.alibaba.fastjson.JSONObject.parseObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public static void main(String[] args) {
        String str = "{\"name\":\"zhans\"}";
        System.out.println(isJson(str));;
    }

}
