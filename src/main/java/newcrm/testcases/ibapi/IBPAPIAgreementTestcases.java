package newcrm.testcases.ibapi;

import com.alibaba.fastjson.JSONObject;
import newcrm.cpapi.APIThirdPartyService;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.ibapi.IBPAPIAgreement;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

import static org.testng.Assert.assertTrue;

public class IBPAPIAgreementTestcases {

    protected String initbrand;
    protected String initserver;
    protected Object data[][];
    protected IBPAPIAgreement IBPAPIAgreement;


    @Test
    public void apiSignedIBRebateAgreement(GlobalProperties.ENV env, String brand) throws Exception {
        IBPAPIAgreement  = new IBPAPIAgreement((String)data[0][1],(String)data[0][2],(String)data[0][4]);
        Integer status = IBPAPIAgreement.apiSignedIBRebateAgreement(env, brand,(String)data[0][3]);
        //Status: 1 = Agreement Not Yet Signed, 2 = Agreement Signed
        assertTrue(status.equals(1), "IB Rebate Agreement Status: " + status + ", Agreement already signed!");
    }

}
