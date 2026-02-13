package newcrm.business.mobusiness;

import newcrm.business.businessbase.CPWithdrawLimit;
import newcrm.global.GlobalMethods;
import newcrm.pages.moclientpages.MOWithdrawPage;
import newcrm.utils.totp.EmailTOTP;
import org.openqa.selenium.WebDriver;
import utils.LogUtils;

public class MOCPWithdrawLimit extends CPWithdrawLimit {

    public MOCPWithdrawLimit(WebDriver driver) {
        super(driver);
        this.wp = new MOWithdrawPage(driver);
    }


    @Override
    public void inputCode(String authUri) throws Exception{

        EmailTOTP emailTOTP = new EmailTOTP(authUri);
        // 生成TOTP验证码
        String totpCode = emailTOTP.generateTOTP();

        LogUtils.info("get totp code: " + totpCode);
        wp.inputAuthCode(totpCode);

        wp.clickSendCode();

        wp.inputAuthCode("999999");
    }
}
