package newcrm.cpapi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.BRAND;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import utils.LogUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

import static org.testng.Assert.assertTrue;

public class APIThirdPartyService extends CPAPIBase{

    private String resToken = "";
    private final String brandId;
    private final String brandName;
    private final String hostName;
    private final String realUserName;
    private final String authUsername;
    private final String authPassword;
    private final String encodedPassword;
    private final String apiLoginTokenUrl;
    private final String apiSendOtpUrl;
    private String apiGetCountryIdUrl = "";
    private String apiSearchOtpUrl = "";

    public APIThirdPartyService(ENV env, BRAND brand) {
        super();

        this.hostName = switch (env) {
            case ALPHA -> "share-msg-frontend.app-alpha.com";
            case UAT -> "share-srv-msg.app.bit-beta.com";
            case PROD -> "share-srv-msg.app-prod.com";
        };

        this.realUserName = switch (env) {
            case ALPHA -> "helenwu";
            case UAT -> "autotes01";
            case PROD -> "crmqaautomation";
        };

        this.encodedPassword = switch (env) {
            case ALPHA -> "Q8Sa/ZGJDPU=";
            // Plain text - Aa123456
            case UAT -> "M5eYntXYGOI=";
            // Plain text - &zl5m6TpG$q
            case PROD -> "VIzFmYvaeaSR2/o=";
        };

        this.authUsername = switch (env) {
            case ALPHA, UAT -> "test";
            case PROD -> "automated_test";
        };

        this.authPassword = switch (env) {
            case ALPHA, UAT -> "test";
            case PROD -> "hxjhsxdhgqidghshgg";
        };

        this.apiLoginTokenUrl = "https://" + hostName + "/api/auth/oauth2/token?username=" + realUserName + "&grant_type=password&scope=server";
        this.apiSendOtpUrl = "https://" + hostName + "/api/msg/sms/send-otp/v1";

        String[] brandInfo = getBrandInfo(env, brand);
        this.brandId = brandInfo[0];
        this.brandName = brandInfo[1];
    }

    private void setApiGetCountryIdUrl(String countryCode) {
        this.apiGetCountryIdUrl = "https://" + hostName + "/api/msgAdmin/country/page?areaCode=" + countryCode + "&current=1&size=10";
    }

    private void setApiSearchOtpUrl(String dateStart, String dateEnd, String countryId, String phoneNum) {
        this.apiSearchOtpUrl = "https://" + hostName + "/api/msgAdmin/statistics/otpRecord/page?productId="+ brandId +"&startTime=" + dateStart + "&endTime=" + dateEnd + "&scenarioId=&countryId="+ countryId +"&subscriberNumber=" + phoneNum + "&current=1&size=10";
    }


    //三方信息中心，取登录Token
    public void apiMsgCenterGetLoginToken() throws Exception {
        // Basic认证用户
        String auth = Base64.getEncoder().encodeToString((authUsername + ":" + authPassword).getBytes(StandardCharsets.UTF_8));

        HashMap<String, String> header = new HashMap<>();
        header.put("authority", hostName);
        header.put("Authorization", "Basic " + auth);
        header.put("Content-Type", "application/x-www-form-urlencoded");
        HashMap<String,String> body = new HashMap<>();
        body.put("password", encodedPassword);

        HttpResponse response = httpClient.getPostResponse(apiLoginTokenUrl, header,body);
        String result = EntityUtils.toString(response.getEntity(),"UTF-8");
        printAPICPInfo(apiLoginTokenUrl, "", result);
        JSONObject jsonResponse = JSONObject.parseObject(result);
        String resCode = jsonResponse.getString("code");
        String msg = jsonResponse.getString("msg");

        if ("1".equals(resCode) && msg.contains("Incorrect password")) {
            Assert.fail("Invalid login credentials. Error Msg: " + msg);
        }

        resToken = jsonResponse.getJSONObject("data").getString("access_token");
        assertTrue(resCode.equals("200") && resToken != null, "3rdPartyMessageCenter - Failed to Get Login Token!");
        LogUtils.info("3rdPartyMessageCenter - Get Login Token successful.");
    }

    //三方信息中心，触发API发送手机OTP, 默认使用 France国家
    public void apiSendPhoneOTP(String phoneNum) throws Exception {
        HashMap<String,String> header = new HashMap();
        header.put("Authorization","Bearer " + resToken);
        header.put("token", resToken);
        header.put("Content-Type", "application/json");
        header.put("Host", hostName);
        header.put("User-Agent", "Mozilla/5.0");

        JSONObject targetObj = new JSONObject();
        targetObj.put("countryCode","FR");
        targetObj.put("areaCode",33);
        targetObj.put("subscriberNumber",phoneNum);
        targetObj.put("ip","165.31.103.120");
        targetObj.put("deviceId","30007359-AB21-4F1D-8C3A-7D912A5E1BCD");
        targetObj.put("emulator",false);
        targetObj.put("uid","a12345");

        JSONObject body = new JSONObject();
        body.put("productCode", brandName);
        body.put("scenarioCode","sms_001");
        body.put("language","en_US");
        body.put("target", targetObj);
        body.put("captcha","");
        body.put("codeLength","6");

        HttpResponse response = httpClient.getPostResponse(apiSendOtpUrl, header,body);
        String result = EntityUtils.toString(response.getEntity(),"UTF-8");
        printAPICPInfo(apiSendOtpUrl, String.valueOf(body), String.valueOf(result));
        JSONObject jsonResponse = JSONObject.parseObject(result);
        String resCode = jsonResponse.getString("code");
        String resMsg = jsonResponse.getString("msg");
        assertTrue(resCode.equals("0") && resMsg == null, "3rdPartyMessageCenter - Sent Phone OTP failed!");
        LogUtils.info("3rdPartyMessageCenter - Phone Number: " + phoneNum + " sent Phone OTP successful.");
    }

    //三方信息中心 > 国家管理，根据国家搜索CountryId
    public String apiGetCountryID(String countryCode) throws Exception {
        setApiGetCountryIdUrl(countryCode);

        HashMap<String, String> header = new HashMap();
        header.put("authorization", "Bearer " + resToken);
        header.put("accept", "application/json, text/plain, */*");
        header.put("authority", hostName);

        HttpResponse response = httpClient.getGetResponse(apiGetCountryIdUrl, header);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        printAPICPInfo(apiGetCountryIdUrl, "", String.valueOf(result));
        Integer resCode = result.getInteger("code");
        assertTrue(resCode.equals(200), "response code is not 200 !,response is:"+result);

        String resCountryId = result.getJSONObject("data").getJSONArray("records").getJSONObject(0).getString("id");
        String resCountryCode = result.getJSONObject("data").getJSONArray("records").getJSONObject(0).getString("code");
        assertTrue(resCountryId != null, "3rdPartyMessageCenter - Failed to Get CountryId!!");
        LogUtils.info("3rdPartyMessageCenter - Get CountryId: " + resCountryId + ", CountryCode: " + resCountryCode);

        return resCountryId;
    }

    //三方信息中心 > 统计 > 验证码管理, 搜索 OTP发送记录&获取OTP
    public String apiSearchSentOTPRecord(String phoneNum, String countryId) throws Exception {

        String dateStart = URLEncoder.encode(GlobalMethods.dateDayStart(1), StandardCharsets.UTF_8).replace("+", "%20");
        String dateEnd = URLEncoder.encode(GlobalMethods.dateDayEnd(1), StandardCharsets.UTF_8).replace("+", "%20");
        setApiSearchOtpUrl(dateStart, dateEnd, countryId, phoneNum);

        HashMap<String, String> header = new HashMap();
        header.put("accept", "application/json, text/plain, */*");
        header.put("authority", "share-msg-frontend.app-alpha.com");
        header.put("authorization", "Bearer " + resToken);

        HttpResponse response = httpClient.getGetResponse(apiSearchOtpUrl, header);
        JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        printAPICPInfo(apiSearchOtpUrl, "", String.valueOf(result));

        Integer resCode = result.getInteger("code");
        assertTrue(resCode.equals(200), "3rdPartyMessageCenter - Failed to Get OTP record!"); //预期只获取一组OTP记录
        JSONArray resData = result.getJSONObject("data").getJSONArray("records");

        if(CollectionUtils.isEmpty(resData)) {
            LogUtils.info("3rdPartyMessageCenter - Phone Number: " + phoneNum + ", no Phone OTP record found.");
            return null;
        }

        JSONObject firstRowData = resData.getJSONObject(0);

        try {
            String otp = firstRowData.getString("otp");
            assertTrue(otp != null, "3rdPartyMessageCenter - Failed in PhoneOTP empty value!!");
            LogUtils.info("3rdPartyMessageCenter - Phone Number: " + phoneNum + ", OTP: " + otp);
            return otp;
        } catch (Exception e) {
            LogUtils.info("3rdPartyMessageCenter - Failed to Get OTP record");
        }
        return null;
    }

    /**
     * 返回alpha 三方信息中心 > "品牌管理"页面，获取 productId.
     *
     * @param env       Testing Environment
     * @param brand     Brand
     * @return brand's productID, brand name
     */
    private String[] getBrandInfo(ENV env, BRAND brand) {
        return switch (env) {
            case ALPHA -> switch (brand) {
                case VFX -> new String[]{"1","vau"};
                case VT -> new String[]{"1730409740838420482","vt"};
                case PUG -> new String[]{"1730409697846804481","pu"};
                case STAR -> new String[]{"1730411148362956802","sa"};
                case MO -> new String[]{"1730410950668632066","mo"};
                case UM -> new String[]{"1730409998469349377","um"};
                case VJP -> new String[]{"1790633099798024194","vjp"};
                default -> throw new IllegalArgumentException("Unknown brand: " + brand);
            };
            case UAT, PROD -> switch (brand) {
                case VFX -> new String[]{"1","vau"};
                case VT -> new String[]{"3","vt"};
                case PUG -> new String[]{"2","pu"};
                case STAR -> new String[]{"6","sa"};
                case MO -> new String[]{"5","mo"};
                case UM -> new String[]{"4","um"};
                case VJP -> new String[]{"8","vjp"};
                default -> throw new IllegalArgumentException("Unknown brand: " + brand);
            };
        };
    }

}
