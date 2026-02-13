package newcrm.business.starbusiness;

import newcrm.business.businessbase.CPWithdrawLimit;
import newcrm.pages.starclientpages.STARWithdrawPage;
import newcrm.utils.totp.EmailTOTP;
import org.openqa.selenium.WebDriver;
import utils.LogUtils;

public class STARCPWithdrawLimit extends CPWithdrawLimit {

    public STARCPWithdrawLimit(WebDriver driver){
        super(driver);
        this.wp = new STARWithdrawPage(driver);
    }


    @Override
    public void inputCode(String authUri) throws Exception{

        wp.clickSendCode();

        wp.inputAuthCode("999999");

        EmailTOTP emailTOTP = new EmailTOTP(authUri);
        // 生成TOTP验证码
        String totpCode = emailTOTP.generateTOTP();

        LogUtils.info("get totp code: " + totpCode);

        wp.inputAuthCode(totpCode);

    }
}
