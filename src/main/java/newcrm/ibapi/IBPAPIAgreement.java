
package newcrm.ibapi;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import newcrm.cpapi.CPAPIBase;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.utils.api.HyTechUtils;
import newcrm.utils.encryption.EncryptUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.StringEntity;
import utils.LogUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

import static org.testng.Assert.assertTrue;

public class IBPAPIAgreement extends CPAPIBase {

    protected String resToken = "";
//    protected static String xtoken = "";

    public IBPAPIAgreement(String traderName, String traderPass, String ibUrl) {
        super(ibUrl, traderName, traderPass, null, null);
    }


    public Integer apiSignedIBRebateAgreement(GlobalProperties.ENV env, String brand, String ibAcc) throws Exception {
        String fullPath = "";
        if(brand.equalsIgnoreCase("vfx") || brand.equalsIgnoreCase("au")){
            fullPath = this.url + "web-api/r/api/rebate/agreement/account/" + ibAcc;
        }else{
            fullPath = this.url + "web-api/api/rebate/agreement/account/" + ibAcc;
        }


        try {


            cn.hutool.http.HttpResponse response = HttpRequest.get(fullPath)
                    .addHeaders(header)
                    .timeout(20000)
                    .execute();

            String responseBody = response.body();

            JSONObject result = JSONObject.parseObject(responseBody);
            JSONObject dataObj = result.getJSONObject("data");
            Integer status = dataObj.getInteger("status");
            String mt4Account = dataObj.getString("mt4Account");
            String fileName = dataObj.getString("fileName");

            assertTrue(mt4Account.equals(ibAcc), "API Response (mt4Account): " + mt4Account + "Expected (mt4Account): " + ibAcc);
            LogUtils.info("Rebate Agreement Template: " + fileName);

            GlobalMethods.printDebugInfo("GET Rebate Agreement URL: " + fullPath);
            printAPICPInfo(fullPath, null, responseBody);

            LogUtils.info("Rebate Agreement Response: " + responseBody);
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }


    private String[] getBrandInfo(String brand) {
        return switch (brand.toUpperCase()) {
            case "VFX", "AU" -> new String[]{"1", "vau"};
            case "VT" -> new String[]{"1730409740838420482", "vt"};
            case "PUG" -> new String[]{"1730409697846804481", "pu"};
            case "STAR" -> new String[]{"1730411148362956802", "sa"};
            case "MO" -> new String[]{"1730410950668632066", "mo"};
            case "UM" -> new String[]{"1730409998469349377", "um"};
            case "VJP" -> new String[]{"1790633099798024194", "vjp"};
            default -> throw new IllegalArgumentException("Unknown brand: " + brand);
        };
    }

}
