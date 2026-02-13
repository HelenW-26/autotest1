package newcrm.business.aubusiness;

import newcrm.business.businessbase.copyTrading.CPCopyTradingRegister;
import newcrm.cpapi.APIThirdPartyService;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.pages.auclientpages.AUMenuPage;
import newcrm.pages.auclientpages.Register.AURegisterGoldHomePage;
import newcrm.pages.auclientpages.Register.AURegisterGoldPersonalDetailsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Objects;

public class AUCPCopyTradingRegister extends CPCopyTradingRegister {
    public AUCPCopyTradingRegister(WebDriver driver, String registerURL) {
        super(driver, registerURL);
    }
    @Override
    protected void setUpHomepage() {
        homepage = new AURegisterGoldHomePage(driver,registerURL);
    }

    @Override
    protected void setUpPDpage() {
        this.pdpage = new AURegisterGoldPersonalDetailsPage(driver);
    }

    @Override
    public void verifyPersonalDetails(String phone, String email, String code,ENV env) throws Exception {
        pdpage.closeToolSkipButton();
        verifyEmail(email, code);
        pdpage.nextStep();
        String phoneOTP = getPhoneOTP(dbBrand,phone,email,env);
        if (Objects.isNull(phoneOTP)) {
            verifyMobile(phone, code);
        } else {
            verifyMobile(phone, phoneOTP);
        }
        pdpage.submit();
    }

    @Override
    public void verifyPersonalDetails_withLinkPhone(String phone, String email, String code, ENV env) throws Exception {
        pdpage.closeToolSkipButton();
        verifyEmail(email, code);
        pdpage.nextStep();
        fillPhonePage(phone);

        // Phone OTP verification is now based on country setup in OWS > KYC Mgmt > lvl 1 OTP Setting
        boolean isExistsPhoneOTPDialog = pdpage.checkExistsVerifyPhoneNoDialog(phone);

        if (isExistsPhoneOTPDialog) {
            String phoneOTP = getPhoneOTP(dbBrand,phone,email,env);
            if (Objects.isNull(phoneOTP)) {
                verifyMobile_withoutClickOTPButton(phone, code);
            } else {
                verifyMobile_withoutClickOTPButton(phone, phoneOTP);
            }
            pdpage.submit();
        }
    }

    @Override
    public boolean goToPersonalDetailPage() {
        entrySubmit("");
        //check
        AUMenuPage menu = new AUMenuPage(this.driver);
        pdpage.closeToolSkipButton();
        pdpage.closeImg();
        menu.changeLanguage("English");

        GlobalMethods.printDebugInfo("CPRegisterGold: go to personal details page.");
        return true;
    }

    @Override
    public void setUserInfo(String firstName, String lastName, String phonenum,String email,String pwd, String brand) {
        entrypage.setEmail(email);
        entrypage.setPassword(pwd);
        entrypage.setBrand(brand);

        userdetails.put("Email", email);
        userdetails.put("pwd", pwd);
        userdetails.put("Brand", brand);
    }

//    @Override
//    public void fillIDPage_withSumsub(String country) {
//        idpage.closePOIDialog();
//        idpage.clickPOIExitBtn();
//        GlobalMethods.printDebugInfo("Skip Sumsub Verification");
//    }

    public String getPhoneOTP(BRAND brand, String phone, String email, ENV env) throws Exception {
        //CP端手机验证页面，获取手机号国家前缀
        WebElement countryCodes = driver.findElement(By.xpath("//div//span[@class='opt_value_text']"));
        String codeText = countryCodes.getText();
        String countryCode = codeText.replace("+", "");

        //登录信息中心，获取手机OTP
        APIThirdPartyService ApiThirdPartyService = new APIThirdPartyService(env, brand);
        ApiThirdPartyService.apiMsgCenterGetLoginToken();
        String countryId = ApiThirdPartyService.apiGetCountryID(countryCode);
        String code = ApiThirdPartyService.apiSearchSentOTPRecord(phone, countryId);
        return code;
    }
}
