package newcrm.business.aubusiness.register;

import newcrm.business.businessbase.CPRegister;
import newcrm.global.GlobalMethods;
import newcrm.pages.auclientpages.AUMenuPage;
import newcrm.pages.auclientpages.Register.*;
import org.openqa.selenium.WebDriver;
import utils.LogUtils;
import vantagecrm.Utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AuASICCPRegister extends CPRegister {

    protected AUASICFinancialDetailsPage asic_fdpage;

    public AuASICCPRegister(WebDriver driver, String url) {
        super(driver, url);
        asic_fdpage = new AUASICFinancialDetailsPage(driver);
    }

    @Override
    protected void setUpACpage() {
        acpage = new AUAccountConfigurationPage(driver);
    }

    @Override
    protected void setUpPDpage() {
        this.pdpage = new AUASICPersonalDetailsPage(driver);
    }

    @Override
    protected void setUpAddresspage() {
        addresspage = new AUASICResidentialAddressPage(driver);
    }

    @Override
    protected void setUpFDpage() {
        fdpage = new AUASICFinancialDetailsPage(driver);
    }

    @Override
    protected void setUpIDpage() {
        idpage = new ASICConfirmIDPage(driver);
    }

    @Override
    protected void setUpFinishPage() {
        finishpage = new AUFinishPage(driver);
    }

    @Override
    protected void setUpHomepage() {
        homepage = new AURegisterHomePage(driver,registerURL);
    }

    @Override
    public boolean goToPersonalDetailPage() {
        AUMenuPage menu = new AUMenuPage(this.driver);
        pdpage.closeImg();
//        menu.changeLanguage("English");

        LogUtils.info("ASICCPRegister: go to personal details page.");
        return true;
    }

    @Override
    public boolean goToAddressPage() {
        pdpage.submit();

        LogUtils.info("ASICCPRegister: go to MAIN RESIDENTIAL ADDRESS page.");
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

    @Override
    public boolean goToFinancialPage() {
        addresspage.next();
        addresspage.waitLoading();

        LogUtils.info("ASICCPRegister: go to EMPLOYMENT AND FINANCIAL DETAILS page.");
        return true;
    }

    @Override
    public boolean goToIDPage() {
        acpage.next();

        LogUtils.info("ASICCPRegister: go to CONFIRM YOUR ID page.");
        return true;
    }

    @Override
    public boolean goToQuizPage() {
        acpage.next();
        LogUtils.info("ASICCPRegister: go to Suitability Quiz page.");
        quizpage.waitLoadingSuitabilityQuizContent();
        quizpage.startQuiz();
        quizpage.waitLoadingSuitabilityQuestionnaireContent();

        LogUtils.info("ASICCPRegister: go to Suitability Questionnaire page.");
        return true;
    }

    @Override
    public boolean goToFinishPage() {
        return true;
    }

    @Override
    public void fillIDPage() {
        Path parent = Paths.get("src", "main", "resources", "vantagecrm", "doc");
        String fileFront = Paths.get(parent.toString(), "Update_ID_Card.jpg").toString();
        String fileBack = Paths.get(parent.toString(), "Update_ID_Card_Back.jpg").toString();
        String filePOA = Paths.get(parent.toString(), "Update_ID_Card.jpg").toString();

        idpage.uploadID(Paths.get(Utils.workingDir, fileFront).toString());
        idpage.uploadID(Paths.get(Utils.workingDir, fileBack).toString());
        idpage.uploadPOA(Paths.get(Utils.workingDir, filePOA).toString());
    }

    @Override
    public void fillPersonalDetails(String idNum,String firstName, String lastName,String phone) {
        pdpage.closeToolSkipButton();
        userdetails.put("firstName", pdpage.setfirstName(firstName));
        userdetails.put("middleName", pdpage.setMiddleName("Automation Test"));
        userdetails.put("lastName",pdpage.setLastName(lastName));
        userdetails.put("Nationality", pdpage.setNationality());
        userdetails.put("phone",pdpage.setPhone(phone));
        userdetails.put("Date Of Birth", pdpage.setBirthDay());
    }

    @Override
    public void fillFinacialPage() {
        // Check Questionnaire Content
        asic_fdpage.verifyQuestionnaireContent();
        userdetails.put("Source of Funds", asic_fdpage.setSourceOfFunds());
        userdetails.put("Rely Social Security Payments", asic_fdpage.setRelySocialSecurityPayment());
//        userdetails.put("Face Financial Hardship", asic_fdpage.setFaceFinancialHardship());
        userdetails.put("Future Events", asic_fdpage.setFutureEvent());
        userdetails.put("Bankruptcy Status", asic_fdpage.setBankruptcyStatus());
        userdetails.put("Annual Income", asic_fdpage.setEstimatedAnnualIncome());
        userdetails.put("Savings & Investments", asic_fdpage.setEstimatedSavingsAndInvestments());
//        userdetails.put("Intended Deposit", asic_fdpage.setEstimatedIntendedDeposit());
        userdetails.put("Yearly Risk Amount", asic_fdpage.setYearlyRiskAmount());
        userdetails.put("Investment Purpose", asic_fdpage.setInvestmentPurpose());
        userdetails.put("Risk Appetite", asic_fdpage.setRiskAppetite());
        userdetails.put("Trades Timeframes", asic_fdpage.setInvestmentTimeframe());
        userdetails.put("Risk Tolerance", asic_fdpage.setRiskTolerance());
        userdetails.put("Losing Trade Opinion", asic_fdpage.setLosingTradeOpinion());
    }

    @Override
    public void fillAddressDetails() {
        String streetNum = GlobalMethods.getRandomNumberString(3);
        String streetName = GlobalMethods.getRandomString(15);
        addresspage.setAddress(streetNum, streetName);
        userdetails.put("Address", streetNum + " " + streetName);
        String state = GlobalMethods.getRandomString(6) + " test state";
        String suburb = GlobalMethods.getRandomString(6) + " test suburb";

        // 测试环境Green ID 没有接，只能走手动 https://suntontech.atlassian.net/wiki/spaces/VFX/pages/2065662523/Q+A
        String postcode = "yyyy";//GlobalMethods.getRandomNumberString(4);
        addresspage.setState(state);
        addresspage.setSuburb(suburb);
        addresspage.setPostcode(postcode);
        userdetails.put("state", state);
        userdetails.put("suburb", suburb);
        userdetails.put("postcode", postcode);
    }

    @Override
    public void fillQuizPage() {
        userdetails.put("OTC Derivative products", quizpage.setOTCDerivativeProduct());
        quizpage.next();
        userdetails.put("Online Trading Platform Allows", quizpage.setOnlineTradingPlatformAllows());
        quizpage.next();
        userdetails.put("Stop Loss Order Purpose", quizpage.setStopLossOrderPurpose());
        quizpage.next();
        userdetails.put("Share CFD Statement", quizpage.setShareCFDStatement());
        quizpage.next();
        userdetails.put("Risk Warning", quizpage.setRiskWarning());
        quizpage.next();
        userdetails.put("Overall Exposure", quizpage.setOverallExposure());
        quizpage.next();
        userdetails.put("Spread Meaning", quizpage.setSpreadMeaning());
        quizpage.next();
        userdetails.put("True Statement", quizpage.setTrueStatement());
        quizpage.next();
        userdetails.put("Trading Account Responsibility", quizpage.setTradingAccResponsibility());
        quizpage.next();
        userdetails.put("Market Response", quizpage.setMarketResponse());
        quizpage.submit();
    }

}
