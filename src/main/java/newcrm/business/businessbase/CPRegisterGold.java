package newcrm.business.businessbase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import newcrm.business.dbbusiness.EmailDB;
import newcrm.business.dbbusiness.UsersDB;
import newcrm.cpapi.APIThirdPartyService;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.ACCOUNTTYPE;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.REGULATOR;
import newcrm.pages.clientpages.MenuPage;
import newcrm.pages.clientpages.register.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import tools.ScreenshotHelper;
import utils.LogUtils;
import vantagecrm.Utils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class CPRegisterGold {

    protected WebDriver driver;
    protected String registerURL;

    public EmailDB emailDB;
    public String TestEnv;
    public String Regulator;
    public ENV dbenv;
    public BRAND dbBrand;
    public REGULATOR dbRegulator;

    public HashMap<String,String> userdetails;

    protected RegisterGoldHomePage homepage;
    protected RegisterGoldEntryPage entrypage;
    protected RegisterGoldPersonalDetailsPage pdpage;
    protected RegisterGoldResidentialAddressPage addresspage;
    protected RegisterGoldAccountConfigurationPage acpage;
    protected RegisterGoldPOIPage idpage;
    protected RegisterGoldFinancialDetailsPage fdpage;

    public CPRegisterGold(WebDriver driver, String registerURL) {
        this.driver = driver;
        this.registerURL = registerURL;
        userdetails = new HashMap<>();

        //override these methods if you need
        this.setUpHomepage();
        this.setUpEntrypage();
        this.setUpPDpage();
        this.setUpACpage();
        this.setUpIDpage();
        this.setUpFDpage();
        this.setUpAddresspage();
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

    protected void setUpFDpage() {
        fdpage = new RegisterGoldFinancialDetailsPage(driver);
    }

    protected void setUpAddresspage() {
        addresspage = new RegisterGoldResidentialAddressPage(driver);
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

    public void setCampaignCode(String campaignCode) {
        entrypage.setCampaignCode(campaignCode);
        userdetails.put("Campaign Code",campaignCode);
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

    public void setUserInfo_CRMNEW_TEMP(String firstName, String country, String email,String pwd) {
        driver.findElement(By.xpath("//input[@id='country']")).click();
        driver.findElement(By.xpath("//div[@class='country-code'] //div[@class='results']/div[@class='results-option']/span[text()='"+country+"']")).click();
        setEmail(email);
        setPwd(pwd);
        driver.findElement(By.xpath("//label[@for='notUs']")).click();
        driver.findElement(By.xpath("//label[@for='agreeTerms']")).click();
    }

    public void setReferralCode(String refCode){
        driver.findElement(By.xpath("//input[@id='referredBy']")).sendKeys(refCode);
    }

    public void setWid(String wid) {
        entrypage.setWid(wid);
    }

    public void setBranchVersion(String branchVersion) {
        entrypage.setBranchVersion(branchVersion);
    }

    public void setEmail(String email) {
        entrypage.setEmail(email);
        userdetails.put("Email", email);
    }

    public void setPwd(String pwd) {
        entrypage.setPassword(pwd);
        userdetails.put("Pwd", pwd);
    }

    public void setFirstName(String firstName) {
        entrypage.setFirstName(firstName);
        userdetails.put("First Name", firstName);
    }

    public void setASICFirstName(String firstName) {
        entrypage.setASICFirstName(firstName);
        userdetails.put("First Name", firstName);
    }

    public void setASICLastName(String firstName) {
        entrypage.setASICLastName(firstName);
        userdetails.put("Last Name", firstName);
    }

    public void setMobile(String phonenum) {
        entrypage.setMobile(phonenum);
        userdetails.put("Phone Number", phonenum);
    }

    public void checkUNonUS(){
        entrypage.checkNonUSResident();
    }

    public void checkASICUNonUS(){
        entrypage.checkASICNonUSResident();
    }

    public boolean checkSumsubExists() {
        return entrypage.checkSumsubExists();
    }

    public void entrySubmit(String traderURL) {
        String oldUrl = entrypage.getCurrentURL();
        entrypage.submit();
        entrypage.checkNavigateSuccess(traderURL, oldUrl, RegisterEntryPage.REG_SRC.LIVE_REG);
    }

    public void entrySubmit_WithoutCheckURL() {
        entrypage.submit_WithoutCheckURL();
    }

    public void entrySubmit_Temp() {
        entrypage.submit_Next();
    }

    public void verifyPersonalDetails(String phone, String email, ENV env) throws Exception {
        pdpage.closeToolSkipButton();
        verifyEmail(email);
        pdpage.nextStep();
        LogUtils.info("Submit Email OTP");
        checkExistsVerifyPhoneNoDialog(phone, env, true);
    }

    public void verifyPersonalDetails_email(String email) {
        pdpage.closeToolSkipButton();
        verifyEmail(email);
        pdpage.nextStep();
        closeProfileVerificationDialog();
    }

    public void verifyPersonalDetails_withLinkPhone(String phone, String email, ENV env) throws Exception {
        pdpage.closeToolSkipButton();
        verifyEmail(email);
        pdpage.nextStep();
        LogUtils.info("Submit Email OTP");
        fillPhonePage(phone);
        checkExistsVerifyPhoneNoDialog(phone, env, false);
    }

    public void fillPersonalDetails(String firstName, String lastName,String phone) {
        pdpage.waitLoadingKycPDContent();
        userdetails.put("gender", pdpage.setGender("Female"));
        userdetails.put("Date Of Birth", pdpage.setBirthDay());
        userdetails.put("firstName", pdpage.setfirstName(firstName));
        userdetails.put("lastName",pdpage.setLastName(lastName));
    }

    public boolean fillAccountPage(PLATFORM platform, String accPageType) {
        waitLoading();
        if (accPageType.toLowerCase().contains("Open Account".toLowerCase())) {
            if(!acpage.setPlatForm(platform)) {
                return false;
            }
        } else {
            userdetails.put("Platform", platform.toString());
            LogUtils.info("AccountConfigurationPage: set Platform to: " + platform);
        }
        userdetails.put("Account Type", acpage.setAccountType());
        userdetails.put("Currency", acpage.setCurrency());
        acpage.selectTickBox();

        return true;
    }

    public boolean fillAccountPage(PLATFORM platform, ACCOUNTTYPE accountType, CURRENCY currency, String accPageType) {
        waitLoading();
        if (accPageType.toLowerCase().contains("Open Account".toLowerCase())) {
            acpage.waitLoadingOpenAccountContent();
            if(!acpage.setPlatForm(platform)) {
                return false;
            }
        } else {
            acpage.waitLoadingSetupAccountContent();
            userdetails.put("Platform", platform.toString());
            LogUtils.info("AccountConfigurationPage: set Platform to: " + platform);
        }

        userdetails.put("Account Type", acpage.setAccountType(accountType));
        userdetails.put("Currency", acpage.setCurrency(currency));
        acpage.selectTickBox();

        return true;
    }

    public void fillIDPage() {
        Path parent = Paths.get("src", "main", "resources", "vantagecrm", "doc");
        String fileFront = Paths.get(parent.toString(), "Update_ID_Card.jpg").toString();
        String fileBack = Paths.get(parent.toString(), "Update_ID_Card_Back.jpg").toString();
        String idnum = "TESTID"+GlobalMethods.getRandomNumberString(10);

        idpage.nextStep();
        userdetails.put("ID Type", idpage.setIDType());
        userdetails.put("ID Number", idpage.setIDNumber(idnum));
        idpage.nextStep();
        idpage.uploadIDFront(Paths.get(Utils.workingDir, fileFront).toString());

        idpage.uploadIDBack(Paths.get(Utils.workingDir, fileBack).toString());
        idpage.nextStep();
        // Check for error message
        idpage.checkExistsAlertMsg("Submit POI");
    }

    public void fillIDPage_withSumsub(String country) {
        Path parent = Paths.get("src", "main", "resources", "vantagecrm", "doc");
        String fileFront = Paths.get(parent.toString(), "ID_Card_Gen.jpg").toString();
        String fileBack = Paths.get(parent.toString(), "ID_Card_Back_Gen.jpg").toString();

        // Wait for Sumsub content to load
        idpage.waitLoadingSumSubContent();
        // Check whether page has access to Sumsub
        if (!idpage.checkHasSumSubAccess()) {
            return;
        }
        // Sumsub - Change Language
        idpage.sumSubChangeLanguage();
        // Sumsub - Start Verification Step
        idpage.startSumSubVerification();
        // Sumsub - Personal Information Step
        idpage.fillSumSubPersonalDetails(country);
        // Sumsub - Select Document Type Step
        idpage.setSumSubIdentificationType(country);
        // Sumsub - Upload Document Step
        idpage.uploadSumSubDocType(Paths.get(Utils.workingDir, fileFront).toString(), Paths.get(Utils.workingDir, fileBack).toString());
        // Switch back to default content
        driver.switchTo().defaultContent();
//        idpage.nextStep();
    }

    public void fillAdvanceIDPage_withSumsub(String country) {
        Path parent = Paths.get("src", "main", "resources", "vantagecrm", "doc");
        String fileFront = Paths.get(parent.toString(), "ID_Card_Gen.jpg").toString();
        String fileBack = Paths.get(parent.toString(), "ID_Card_Back_Gen.jpg").toString();

        // Wait for Sumsub content to load
        idpage.waitLoadingSumSubContent();
        // Check whether page has access to Sumsub
        if (!idpage.checkHasSumSubAccess()) {
            return;
        }
        // Sumsub - Change Language
        idpage.sumSubChangeLanguage();
        // Sumsub - Personal Information Step
        idpage.fillSumSubPersonalDetails(country);
        // Sumsub - Select Document Type Step
        idpage.setSumSubIdentificationType(country);
        // Sumsub - Upload Document Step
        idpage.uploadSumSubDocType(Paths.get(Utils.workingDir, fileFront).toString(), Paths.get(Utils.workingDir, fileBack).toString());
        // Switch back to default content
        driver.switchTo().defaultContent();
//        idpage.nextStep();
    }

    public void fillAdvanceFinancialPage_withSumsub(String country) {
        // Wait for Sumsub content to load
        idpage.waitLoadingSumSubContent();
        // Check whether page has access to Sumsub
        if (!idpage.checkHasSumSubAccess()) {
            return;
        }
        // Sumsub - Change Language
        idpage.sumSubChangeLanguage();
        // Sumsub - Start Verification Step
        idpage.startSumSubVerification();
        // Sumsub - Financial Information Step
        userdetails = addresspage.fillSumSubFinancial(country, userdetails);
        // Switch back to default content
        driver.switchTo().defaultContent();
    }

    public void fillPhonePage(String phone) {
        waitLoading();
        pdpage.waitLoadingKycPDContent();
        userdetails.put("Phone Number", pdpage.setPhone(phone));
        pdpage.clickSendOTPBtn();
    }

    public void fillAddressDetails() {
        Path parent = Paths.get("src", "main", "resources", "vantagecrm", "doc");
        String filePOA = Paths.get(parent.toString(), "Update_ID_Card.jpg").toString();

        String streetNum = GlobalMethods.getRandomNumberString(3);
        String streetName = GlobalMethods.getRandomString(15);
        addresspage.setAddress(streetNum, streetName);
        userdetails.put("Address", streetNum + " " + streetName);

        String suburb = GlobalMethods.getRandomString(6) + " test suburb";
        addresspage.setSuburb(suburb);
        userdetails.put("suburb", suburb);

        idpage.nextStep();
        idpage.nextStep();

        // Submit POA
        idpage.uploadIDFront(Paths.get(Utils.workingDir, filePOA).toString());
        addresspage.submit();
    }

    public void fillAddressDetails_withSumsub(String country) {
        Path parent = Paths.get("src", "main", "resources", "vantagecrm", "doc");
        String fileFront = Paths.get(parent.toString(), "Update_ID_Card_Back.jpg").toString();

        // Wait for Sumsub content to load
        idpage.waitLoadingSumSubContent();
        // Check whether page has access to Sumsub
        if (!idpage.checkHasSumSubAccess()) {
            return;
        }
        // Subsum - Change Language
        idpage.sumSubChangeLanguage();
        // Sumsub - Start Verification Step
        idpage.startSumSubVerification();
        // Sumsub - Personal Information Step
        idpage.fillSumSubPersonalDetails(country);
        // Sumsub - Upload Document Step
        idpage.uploadSumSubPOA(Paths.get(Utils.workingDir, fileFront).toString());
        // Switch back to default content
        driver.switchTo().defaultContent();
    }

    public void fillAdvanceAddressDetails_withSumsub() {
        Path parent = Paths.get("src", "main", "resources", "vantagecrm", "doc");
        String fileFront = Paths.get(parent.toString(), "Update_ID_Card_Back.jpg").toString();

        // Wait for Sumsub content to load
        idpage.waitLoadingSumSubContent();
        // Check whether page has access to Sumsub
        if (!idpage.checkHasSumSubAccess()) {
            return;
        }
        // Subsum - Change Language
        idpage.sumSubChangeLanguage();
        // Sumsub - Upload Document Step
        idpage.uploadSumSubPOA(Paths.get(Utils.workingDir, fileFront).toString());
        // Switch back to default content
        driver.switchTo().defaultContent();
    }

    public void fillAdvanceFinancialPage() {
        // Wait dialog content loaded
        fdpage.waitLoadingAdvanceVerificationContent();
        // Check Financial and trading details Questionnaire Content
        fdpage.verifyQuestionnaireContent();

        // Fill up questionnaire
        userdetails.put("Employment Status", fdpage.setQuizAnswer(1, "Employment Status"));
        userdetails.put("Annual Income", fdpage.setQuizAnswer(1, "Estimated Annual Income"));
        userdetails.put("Savings & Investments", fdpage.setQuizAnswer(1, "Savings & Investments"));
        userdetails.put("Intended Deposit", fdpage.setQuizAnswer(1, "Intended Deposit"));
        userdetails.put("Source of Funds", fdpage.setQuizAnswer(1, "Source of Funds"));
        userdetails.put("Place of Birth", fdpage.setQuizAnswer(1, "Place of Birth"));

        // Submit
        pdpage.submit();
    }

    public void fillAdvanceIDPage() {
        Path parent = Paths.get("src", "main", "resources", "vantagecrm", "doc");
        String fileFront = Paths.get(parent.toString(), "Update_ID_Card.jpg").toString();
        String fileBack = Paths.get(parent.toString(), "Update_ID_Card_Back.jpg").toString();
        String idnum = "TESTID"+GlobalMethods.getRandomNumberString(10);

        idpage.nextStep();
        userdetails.put("ID Number", idpage.setIDNumber(idnum));
        idpage.nextStep();
        idpage.uploadIDFront(Paths.get(Utils.workingDir, fileFront).toString());
        idpage.uploadIDBack(Paths.get(Utils.workingDir, fileBack).toString());
        idpage.nextStep();
        // Check for error message
        idpage.checkExistsAlertMsg("Submit POI");
    }

    public void fillAdvanceAddressDetails() {
        Path parent = Paths.get("src", "main", "resources", "vantagecrm", "doc");
        String filePOA = Paths.get(parent.toString(), "Update_ID_Card.jpg").toString();

        String streetNum = GlobalMethods.getRandomNumberString(3);
        String streetName = GlobalMethods.getRandomString(15);
        addresspage.setAddress(streetNum, streetName);
        userdetails.put("Address", streetNum + " " + streetName);

        String suburb = GlobalMethods.getRandomString(6) + " test suburb";
        addresspage.setSuburb(suburb);
        userdetails.put("suburb", suburb);

        idpage.nextStep();
        idpage.nextStep();

        // Submit POA
        idpage.uploadIDFront(Paths.get(Utils.workingDir, filePOA).toString());
        pdpage.submit();
    }

    public void verifyEmail(String email) {
        pdpage.waitLoader();
        pdpage.waitLoadingKycPDContent();
        pdpage.clickEmailCodeBtn();

        // Check for error
        String alertMsg = pdpage.checkExistsAlertMsg();
        if (alertMsg != null) {
            Assert.fail("An error occurred during Email OTP request. Error Msg: " + alertMsg);
        }

        String code = getEmailOTP(email);
        userdetails.put("Email OTP", pdpage.sendEmailCode(code));
    }

    public String getEmailOTP(String email) {
        String code = "";

        try {
            code = getCode(emailDB, email);
        } catch (Exception ex) {
            Assert.fail("An error occurred when retrieving email OTP from db. Error Msg: " + ex.getMessage());
        }

        if (code == null) {
            Assert.fail("No email OTP record found in database");
        } else if (code.isEmpty()) {
            Assert.fail("Email OTP record exists in database but contains no value");
        }

        return code;
    }

    public String getPhoneOTP(String phone, ENV env) throws Exception {
        // CP端手机验证页面，获取手机号国家前缀
        String countryCode = pdpage.getPhoneDisplayCountryCode();
        // Get otp from message center
        String otp = getPhoneOTPFromGEP(phone, countryCode, env);

        return otp;
    }

    public boolean goToPersonalDetailPage() {
        MenuPage menu = new MenuPage(this.driver);
        menu.refresh();
//        menu.changeLanguage("English");

        LogUtils.info("CPRegisterGold: go to Personal Details page.");
        return true;
    }

    public boolean goToPersonalDetailsSummaryPage() {
        pdpage.submit();
        LogUtils.info("CPRegisterGold: go to Personal Details Summary page.");
        return true;
    }

    public boolean goToSetupAccountPage() {
        acpage.clickSetupAccountBtn();
        LogUtils.info("CPRegisterGold: go to Setup Account Configuration page.");
        return true;
    }

    public boolean goToOpenAccountPage() {
        acpage.clickOpenAccountBtn();
        LogUtils.info("CPRegisterGold: go to Open Account Configuration page.");
        return true;
    }

    public boolean goToIDPage() {
        pdpage.submit();
        idpage.clickPOICloseBtn();
        idpage.clickIDVerifyBtn();
        LogUtils.info("CPRegister: go to ID Verification page.");
        return true;
    }

    public void closeProfileVerificationDialog() {
        idpage.waitLoader();
        idpage.clickPOICloseBtn();
        idpage.clickPOIExitBtn();
    }

    public void closeProfileVerificationDialog_withoutExit() {
        idpage.waitLoader();
        idpage.clickPOICloseBtn();
    }

    public void closeAccountConfigDialog(String accPageType) {
        if (accPageType.toLowerCase().contains("Open Account".toLowerCase())) {
            acpage.waitLoadingOpenAccountContent();
        } else {
            acpage.waitLoadingSetupAccountContent();
        }

        idpage.clickPOICloseBtn();
    }

    public void waitLoadingIdentityVerificationContent() {
        idpage.waitLoadingIdentityVerificationContent();
    }

    public void waitLoadingPOAVerificationContent() {
        idpage.waitLoadingPOAVerificationContent();
    }

    public void waitLoadingAdvanceVerificationContent() {
        fdpage.waitLoadingAdvanceVerificationContent();
    }

    public void submitKYC_Lvl1() {
        pdpage.submit();
        idpage.clickPOICloseBtn();
    }

    public boolean goToFinishPage() {
        return true;
    }

    public String getAccountPageType() {
        pdpage.waitLoadingKycPDSummaryContent();
        String accPageType = acpage.getSetupAccountBtn();
        LogUtils.info("CPRegisterGold: " + accPageType);
        return accPageType;
    }

    public void printUserRegisterInfo() {
        System.out.println("*********************Register Summary**********************");
        for(Map.Entry<String, String> entry: userdetails.entrySet()) {
            System.out.printf("%-30s : %s\n",entry.getKey(),entry.getValue());
        }
        System.out.println("***********************************************************");
    }

    public String getCode(EmailDB instance, String email)
    {
        JSONObject obj = null;
        String code = null;

        Map<String, REGULATOR> regulatorMap = Map.of(
                "vfsc", REGULATOR.VFSC,
                "vfsc2", REGULATOR.VFSC2,
                "svg", REGULATOR.SVG,
                "fsa", REGULATOR.FSA
        );

        REGULATOR regulator = regulatorMap.getOrDefault(Regulator.toLowerCase(), null);

        try {
            if (regulator != null) {
                obj = instance.getCodeRecord(dbenv, dbBrand, regulator, email);
            } else {
                obj = instance.getCodeRecord(dbenv, dbBrand, dbRegulator, email);
            }
        } catch (Exception e) {
            LogUtils.info("An error occurred when retrieve data from db. Error Msg: " + e.getMessage());
        }

        if (obj != null) {
            LogUtils.info(obj.getJSONObject("vars").getString("CODE")+ ", \n"+ obj.toJSONString());
            code = obj.getJSONObject("vars").getString("CODE");
        }

        return code;
    }

    public void checkUserInfo(String email, ENV env, BRAND brand, REGULATOR regulator) {
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
            if (var == null) {
                Assert.fail("No welcome email content found for email " + email);
            }
            userdetails.put("Password",var.getString("PASSWORD"));
        }
    }

    public void waitLoading() {
        homepage.waitLoading();
    }

    public void checkExistsVerifyPhoneNoDialog(String phone, ENV env, boolean isSendRequestOTP) throws Exception {
        // Capture ss
        ScreenshotHelper.takeFullPageScreenshot(pdpage.getDriver(), "screenshots", "checkExistsVerifyPhoneNoDialog");

        pdpage.waitLoadingKycPDContent();

        // Phone OTP verification is now based on country setup in OWS > KYC Mgmt > lvl 1 OTP Setting
        boolean isExistsPhoneOTPDialog = pdpage.checkExistsVerifyPhoneNoDialog(phone);

        if (isExistsPhoneOTPDialog) {

            // In flows that require a phone number, after submitting it, an OTP is automatically sent upon redirecting to the next page.
            // Otherwise, it requires manually clicking the "Send OTP" button
            if (isSendRequestOTP) {
                pdpage.clickPhoneCodeBtn();
            }

            // Check for error
            String alertMsg = pdpage.checkExistsAlertMsg();
            if (alertMsg != null) {
                Assert.fail("An error occurred during Phone OTP request. Error Msg: " + alertMsg);
            }

            Thread.sleep(5000);
            LogUtils.info("Wait 5 seconds before retrieving the phone OTP..");

            // Search for OTP record
            String phoneOTP = getPhoneOTP(phone, env);

            // Set phone OTP
            userdetails.put("Phone OTP", pdpage.sendPhoneCode(phoneOTP));
            pdpage.submit();
            LogUtils.info("Submit Phone OTP");

            // Check for error after submit otp
            alertMsg = pdpage.checkExistsAlertMsg();
            if (alertMsg != null) {
                Assert.fail("An error occurred during phone verification submission. Error Msg: " + alertMsg);
            }
        }
    }

    public void sendPhoneCode(String OTP){
        pdpage.clickPhoneCodeBtn();
        pdpage.sendPhoneCode(OTP);
        pdpage.submit();
    }

    public void setAndSendPhoneCode(String phone, String OTP){
        pdpage.setPhone(phone);
        pdpage.clickSendOTPBtn();
        pdpage.sendPhoneCode(OTP);
        pdpage.submit();
    }

    public void sendEmailCode(String OTP){
        pdpage.clickEmailCodeBtn();
        pdpage.sendEmailCode(OTP);
        pdpage.nextStep();
    }

    public String getPhoneOTPFromGEP(String phone, String countryCode, ENV env) throws Exception {

        LogUtils.info("**Going to get phone OTP from 3rd Party Message Center**");

        // 登录信息中心，获取手机OTP
        String code = "";

        try {
            APIThirdPartyService ApiThirdPartyService = new APIThirdPartyService(env, dbBrand);
            ApiThirdPartyService.apiMsgCenterGetLoginToken();
            String countryId = ApiThirdPartyService.apiGetCountryID(countryCode);
            code = ApiThirdPartyService.apiSearchSentOTPRecord(phone, countryId);
        } catch (Exception ex) {
            Assert.fail("An error occurred when retrieving phone OTP from 3rd Party Message Center. Error Msg: " + ex.getMessage());
        }

        if (code == null) {
            Assert.fail("No phone OTP record found in 3rd Party Message Center");
        } else if (code.isEmpty()) {
            Assert.fail("Phone OTP record exists in 3rd Party Message Center but contains no value");
        }

        return code;
    }

    public void registerLiveAccount_Temp(String url, REGULATOR regulator) {
        // AU alpha temp registration
        String oldUrl = homepage.getCurrentURL();
        homepage.registerDemoAccount();
        homepage.waitLoadingPageContent(oldUrl);
        homepage.setDemoRegistrationDomainUrl(url);
        homepage.setSrcBranchVersion();
    }


    public void registerLiveAccountASIC_Temp(String url, REGULATOR regulator) {
        // AU alpha temp registration
        homepage.registerDemoAccount();
        homepage.setASICRegistrationDomainUrl(url);
        homepage.setASICSrcBranchVersion();
    }

    public void inputPagePassword(){
        homepage.registerDemoAccount();
    }

    public void setDemoRegistrationDomainUrl(String url, String oldUrl){
        homepage.waitLoadingPageContent(oldUrl);
        homepage.setDemoRegistrationDomainUrl(url);
        homepage.setSrcBranchVersion();
    }

    public void refresh() {
        idpage.refresh();
    }

}
