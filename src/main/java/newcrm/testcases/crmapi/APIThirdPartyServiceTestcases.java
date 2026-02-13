package newcrm.testcases.crmapi;

import newcrm.cpapi.APIThirdPartyService;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.BRAND;
import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;

public class APIThirdPartyServiceTestcases {

    protected String initbrand;
    protected String initserver;
    protected Object data[][];


    // 信息中心的登录 > 发送短信 > 搜索OTP发送记录
    @Test
    public void apiMsgCenterSendAndVerifyPhoneOTP(ENV env, BRAND brand) throws Exception {
        APIThirdPartyService APIThirdPartyService  = new APIThirdPartyService(env, brand);

        // 先登录信息中心获取token
        APIThirdPartyService.apiMsgCenterGetLoginToken();

        // 注意: 手机号码前缀一定是4个零 = "0000"，否则发送短信将有额外收费
        String phoneNum = "0000" + GlobalMethods.getRandomNumberString(7);

        // 触发API 发送PhoneOTP
        APIThirdPartyService.apiSendPhoneOTP(phoneNum);

        // 使用法国, 获取countryId
        String countryId = APIThirdPartyService.apiGetCountryID("33");

        Thread.sleep(5000);

        // 使用手机号&countryId搜索OTP记录
        String otp = APIThirdPartyService.apiSearchSentOTPRecord(phoneNum, countryId);
        assertTrue(otp != null,"3rdPartyMessageCenter - Search Phone OTP record failed");
        GlobalMethods.printDebugInfo("3rdPartyMessageCenter - Search Phone OTP record successful.\n");
    }

}
