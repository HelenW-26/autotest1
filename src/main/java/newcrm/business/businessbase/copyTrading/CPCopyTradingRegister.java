package newcrm.business.businessbase.copyTrading;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import newcrm.business.dbbusiness.EmailDB;
import newcrm.business.dbbusiness.UsersDB;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.register.*;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import vantagecrm.Utils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class CPCopyTradingRegister {
    protected WebDriver driver;
    protected String registerURL;

    public EmailDB emailDB;
    public String TestEnv;
    public String Regulator;
    public GlobalProperties.ENV dbenv;
    public GlobalProperties.BRAND dbBrand;
    public GlobalProperties.REGULATOR dbRegulator;

    public HashMap<String,String> userdetails;

    protected RegisterGoldHomePage homepage;
    protected RegisterGoldEntryPage entrypage;
    protected RegisterGoldPersonalDetailsPage pdpage;
    protected RegisterGoldAccountConfigurationPage acpage;
    protected RegisterGoldPOIPage idpage;
    public CPCopyTradingRegister(WebDriver driver, String registerURL) {
        this.driver = driver;
        this.registerURL = registerURL;
        userdetails = new HashMap<>();

        //override these methods if you need
        this.setUpHomepage();
        this.setUpEntrypage();
        this.setUpPDpage();
        this.setUpACpage();
        this.setUpIDpage();
    }

    protected void setUpHomepage() {
        homepage = new RegisterGoldHomePage(driver,registerURL);
    }

    protected void setUpEntrypage() {
        entrypage  = new RegisterGoldEntryPage(driver);
    }

    protected void setUpPDpage() {
        pdpage = new RegisterGoldPersonalDetailsPage(driver);
    }

    protected void setUpACpage() {
        acpage = new RegisterGoldAccountConfigurationPage(driver);
    }

    protected void setUpIDpage() {
        idpage = new RegisterGoldPOIPage(driver);
    }


    public void setIBcode(String code, boolean bIsEnvProd) {
        entrypage.setIBcode(code, bIsEnvProd);
        userdetails.put("IBCode", code);
    }

    public void setTradeUrl(String url) {
        entrypage.setActionUrl(url);
        userdetails.put("Action Url", url);
    }

    public void setRAFCode(String rafCode) {
        entrypage.setRAFCode(rafCode);
        userdetails.put("RAF Code", rafCode);
    }

    public boolean setRegulatorAndCountry(String country,String regulator) {
        if(!entrypage.setCountry(country)) {
            return false;
        }

        if(!entrypage.setRegulator(regulator)) {
            return false;
        }

        userdetails.put("Country", country);
        userdetails.put("Regulator", regulator);

        return true;
    }

    public boolean setRegulatorAndCountry(String country, String regulator, String registerInterface) {

        if(!entrypage.setCountry(country)) {
            return false;
        }

        if(!entrypage.setRegulator(regulator)) {
            return false;
        }

        if(!entrypage.setRegisterInterface(registerInterface)) {
            return false;
        }

        userdetails.put("Country", country);
        userdetails.put("Regulator", regulator);
        userdetails.put("registerInterface", registerInterface);

        return true;
    }

    public boolean setCountry(String country) {

        if(!entrypage.setCountry(country)) {
            return false;
        }
        userdetails.put("Country", country);

        return true;
    }

    public void setUserInfo(String firstName, String lastName, String phonenum,String email,String pwd, String brand) {
        entrypage.setFirstName(firstName);
        //entrypage.setLastName(lastName);
        entrypage.setPhone(phonenum);
        entrypage.setEmail(email);
        entrypage.setPassword(pwd);
        entrypage.setBrand(brand);

        userdetails.put("First Name", firstName);
        //userdetails.put("Last Name",lastName);
        userdetails.put("Phone Number", phonenum);
        userdetails.put("Email", email);
        userdetails.put("Pwd", pwd);
        userdetails.put("Brand", brand);
    }

    public void setUserInfo(String firstName, String country, String email,String pwd) {
        entrypage.setFirstName(firstName);
        entrypage.setPassword(pwd);
        entrypage.setEmail(email);
        setCountry(country);

        userdetails.put("First Name", firstName);
        userdetails.put("Password",pwd);
        userdetails.put("Country", country);
        userdetails.put("Email", email);
    }

    public void setWid(String wid) {
        entrypage.setWid(wid);
    }

    public void setEmail(String email) {
        entrypage.setEmail(email);
        userdetails.put("Email", email);
    }

    public void setPwd(String pwd) {
        entrypage.setPassword(pwd);
        userdetails.put("Pwd", pwd);
    }

    public void checkUNonUS(){
        entrypage.checkNonUSResident();
    }

    public boolean checkSumsubExists() {
        return entrypage.checkSumsubExists();
    }

    public void entrySubmit(String traderURL) {
        entrypage.submit();
    }

    public void verifyPersonalDetails(String phone, String email, String code,GlobalProperties.ENV env) throws Exception {
        verifyEmail(email, code);
        pdpage.nextStep();
        verifyMobile(phone, code);
        pdpage.submit();
    }

    public void verifyPersonalDetails_email(String email, String code) {
        verifyEmail(email, code);
        pdpage.nextStep();
        idpage.clickPOICloseBtn();
        idpage.clickPOIExitBtn();
    }

    public void verifyPersonalDetails_withLinkPhone(String phone, String email, String code,GlobalProperties.ENV env) throws Exception {
        verifyEmail(email, code);
        pdpage.nextStep();
        fillPhonePage(phone);
        verifyMobile_withoutClickOTPButton(phone, code);
        pdpage.submit();
    }

    public void fillPersonalDetails(String firstName, String lastName,String phone) {
        pdpage.waitLoadingKycPDContent();
        userdetails.put("gender", pdpage.setGender("Female"));
        userdetails.put("Date Of Birth", pdpage.setBirthDay());
        userdetails.put("firstName", pdpage.setfirstName(firstName));
        userdetails.put("lastName",pdpage.setLastName(lastName));
    }

    public boolean fillAccountPage(GlobalProperties.PLATFORM platform, String accPageType) {
        if (accPageType.toLowerCase().contains("Open Account".toLowerCase())) {
            if(!acpage.setPlatForm(platform)) {
                return false;
            }
        } else {
            userdetails.put("Platform", platform.toString());
            GlobalMethods.printDebugInfo("AccountConfigurationPage: set Platform to: " + platform);
        }
        userdetails.put("Account Type", acpage.setAccountType());
        userdetails.put("Currency", acpage.setCurrency());
        acpage.selectTickBox();

        return true;
    }

    public void closeSetUpAccount(){
        acpage.closeSetUpAccount();
    }
    public boolean fillAccountPage(GlobalProperties.PLATFORM platform, GlobalProperties.ACCOUNTTYPE accountType, GlobalProperties.CURRENCY currency, String accPageType) {
        acpage.setPlatForm(platform);
        userdetails.put("Platform", platform.toString());
        GlobalMethods.printDebugInfo("AccountConfigurationPage: set Platform to: " + platform);

        userdetails.put("Account Type", acpage.setAccountType(accountType));
        userdetails.put("Currency", acpage.setCurrency(currency));
        acpage.selectTickBox();

        return true;
    }

    public void fillIDPage() {
        Path parent = Paths.get("src", "main", "resources", "vantagecrm", "doc");
        String fileFront = Paths.get(parent.toString(), "Update_ID_Card.jpg").toString();
        String fileBack = Paths.get(parent.toString(), "Update_ID_Card_Back.jpg").toString();
        String filePOA = Paths.get(parent.toString(), "Update_ID_Card.jpg").toString();


        String num = GlobalMethods.getRandomNumberString(10);
        String idnum = "TESTID"+GlobalMethods.getRandomNumberString(10);
//        String idnum = GlobalMethods.getRandomNumberString(5);
//        String city = "TESTID"+GlobalMethods.getRandomString(5);
//        String postcode = GlobalMethods.getRandomNumberString(5);
//        String address = GlobalMethods.getRandomString(5);

        idpage.waitLoadingIdentityVerificationContent();
        idpage.nextStep();
        userdetails.put("ID Type", idpage.setIDType());
        userdetails.put("ID Number", idpage.setIDNumber(idnum));
        idpage.nextStep();
        idpage.uploadIDFront(Paths.get(Utils.workingDir, fileFront).toString());

        idpage.uploadIDBack(Paths.get(Utils.workingDir, fileBack).toString());
        idpage.nextStep();
        idpage.clickPOICloseBtn();
    }

    public void fillIDPage_withSumsub(String country) {
        Path parent = Paths.get("src", "main", "resources", "vantagecrm", "doc");
        String fileFront = Paths.get(parent.toString(), "Update_ID_Card.jpg").toString();
        String fileBack = Paths.get(parent.toString(), "Update_ID_Card_Back.jpg").toString();

        idpage.waitLoadingIdentityVerificationContent();
        // Wait for Sumsub content to load
        idpage.waitLoadingSumSubContent();
        // Check whether page has access to Sumsub
        if (!idpage.checkHasSumSubAccess()) {
            return;
        }
        // Sumsub - Start Verification Step
        idpage.startSumSubVerification();
        // Sumsub - Personal Information Step
        idpage.fillSumSubPersonalDetails(country);
        // Sumsub - Select Document Type Step
        idpage.setSumSubIdentificationType(country);
        // Sumsub - Upload Document Step
        idpage.uploadSumSubDocType(Paths.get(Utils.workingDir, fileFront).toString(), Paths.get(Utils.workingDir, fileBack).toString());
    }

    public void fillPhonePage(String phone) {
        pdpage.waitLoadingKycPDContent();
        userdetails.put("Phone Number", pdpage.setPhone(phone));
        pdpage.clickSendOTPBtn();
    }

    public void verifyEmail(String email, String code) {
        pdpage.waitLoader();
        pdpage.clickEmailCodeBtn();
        code = getCode(emailDB, email);
        userdetails.put("Email OTP", pdpage.sendEmailCode(code));
    }

    public void verifyMobile(String phone, String code) {
        pdpage.waitLoader();
        pdpage.clickPhoneCodeBtn();
        userdetails.put("Phone OTP", pdpage.sendPhoneCode(code));
    }

    public void verifyMobile_withoutClickOTPButton(String phone, String code) {
        pdpage.waitLoader();
        userdetails.put("Phone OTP", pdpage.sendPhoneCode(code));
    }

    public boolean goToPersonalDetailPage() {
        entrySubmit("");

        /*MenuPage menu = new MenuPage(this.driver);
        menu.refresh();
        menu.changeLanguage("English");*/

        GlobalMethods.printDebugInfo("CPRegisterGold: go to Personal Details page.");
        return true;
    }

    public boolean goToPersonalDetailsSummaryPage() {
        pdpage.submit();
        GlobalMethods.printDebugInfo("CPRegisterGold: go to Personal Details Summary page.");
        return true;
    }

    public boolean goToSetupAccountPage() {
        acpage.clickSetupAccountBtn();
        GlobalMethods.printDebugInfo("CPRegisterGold: go to Setup Account Configuration page.");
        return true;
    }

    public boolean goToOpenAccountPage() {
        acpage.clickOpenAccountBtn();
        GlobalMethods.printDebugInfo("CPRegisterGold: go to Open Account Configuration page.");
        return true;
    }

    public boolean goToIDPage() {
        pdpage.submit();
        GlobalMethods.printDebugInfo("CPRegister: go to ID Verification page.");
        return true;
    }

    public boolean getAccountOpenTitle()
    {
        return StringUtils.containsIgnoreCase(acpage.getAccountOpenTitle(),"Your Request For A Trading Account Is Under Review");
    }

    public boolean getAccountOpenCompleteContext()
    {
        return StringUtils.containsIgnoreCase(acpage.getAccountOpenCompleteContext(),"Your information has been successfully submitted and is currently under review");
    }

    public void closeKYCDialog () {
        idpage.waitLoader();
        idpage.clickPOICloseBtn();
        idpage.clickPOIExitBtn();
    }

    public void submitKYC_Lvl1() {
        pdpage.submit();
        idpage.clickPOICloseBtn();
    }

    public boolean goToFinishPage() {
        return true;
    }

    public String getAccountPageType() {
        waitLoading();
        String accPageType = acpage.getSetupAccountBtn();
        GlobalMethods.printDebugInfo("CPRegisterGold: " + accPageType);
        return accPageType;
    }

    public void printUserRegisterInfo() {
        System.out.println("*********************Register Summary**********************");
        for(Map.Entry<String, String> entry: userdetails.entrySet()) {
            System.out.printf("%-30s : %s\n",entry.getKey(),entry.getValue());
        }
        System.out.println("***********************************************************");
    }

    public void setEmailCode(String email)
    {
        try {
            pdpage.clickEmailCodeBtn();
            String code = getCode(emailDB, email);
            pdpage.sendEmailCode(code);
        }
        catch(Exception e)
        {
            GlobalMethods.printDebugInfo("no need to enter OTP");
        }
    }

    public void setPhoneCode(String phone)
    {
        try {
            pdpage.clickPhoneCodeBtn();
            String code = getCode(emailDB, phone);
            pdpage.sendPhoneCode(code);
        }
        catch(Exception e)
        {
            GlobalMethods.printDebugInfo("no need to enter OTP");
        }
    }

    public String getCode(EmailDB instance, String email)
    {
        JSONObject obj = null;

        Map<String, GlobalProperties.REGULATOR> regulatorMap = Map.of(
                "vfsc", GlobalProperties.REGULATOR.VFSC,
                "vfsc2", GlobalProperties.REGULATOR.VFSC2,
                "svg", GlobalProperties.REGULATOR.SVG,
                "fsa", GlobalProperties.REGULATOR.FSA
        );

        GlobalProperties.REGULATOR regulator = regulatorMap.getOrDefault(Regulator.toLowerCase(), null);

        try {
            if (regulator != null) {
                obj = instance.getCodeRecord(dbenv, dbBrand, regulator, email);
            } else {
                obj = instance.getCodeRecord(dbenv, dbBrand, dbRegulator, email);
            }
        } catch (Exception e) {
            GlobalMethods.printDebugInfo("An error occurred when retrieve data from db. Error Msg: " + e.getMessage());
        }

        GlobalMethods.printDebugInfo(obj.getJSONObject("vars").getString("CODE")+ ", \n"+ obj.toJSONString());
        String code = obj.getJSONObject("vars").getString("CODE");

        return code;
    }

    public void checkUserInfo(String email, GlobalProperties.ENV env, GlobalProperties.BRAND brand, GlobalProperties.REGULATOR regulator) {
        String wcStatus [] = {"Processing","Completed","Rejected","Pending","Re_Audit","RE_Register"};//"RE_Register=9"
        String idStatus [] = {"Submitted","Pending","Approved","Rejected"};
        String poaStatus [] = {"Submitted","Pending","Approved","Rejected"};
        UsersDB db = new UsersDB();

        JSONArray jsArray = db.getUserRegistrationInfo(email, env, brand, regulator);
        assertNotNull(jsArray);
        assertTrue(jsArray.size() >0,"Do not find the user by email: " + email);

        JSONObject obj = jsArray.getJSONObject(0);
        userdetails.put("User ID", obj.getString("userId"));
        Integer pos = obj.getInteger("wcStatus");
        if(pos==9) {
            pos = 5;
        }
        userdetails.put("WorldCheck",wcStatus[pos]);
        String status = obj.getString("idStatus");
        if(status==null || status.equalsIgnoreCase("null") || status.equalsIgnoreCase("")) {
            userdetails.put("ID Status", "Have not submitted");
        }else {
            userdetails.put("ID Status", idStatus[obj.getIntValue("idStatus")]);
        }
        status = obj.getString("addrStatus");
        if(status==null || status.equalsIgnoreCase("null") || status.equalsIgnoreCase("")) {
            userdetails.put("POA Status", "Have not submitted");
        }else {
            userdetails.put("POA Status", poaStatus[obj.getIntValue("addrStatus")]);
        }

        String vars = obj.getString("vars");
        if(vars!=null && !vars.trim().equalsIgnoreCase("")) {
            JSONObject var = JSON.parseObject(vars);
            userdetails.put("Password",var.getString("PASSWORD"));
        }
    }

    public void waitLoading() {
        homepage.waitLoading();
    }
}
