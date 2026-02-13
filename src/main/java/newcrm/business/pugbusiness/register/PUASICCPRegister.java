package newcrm.business.pugbusiness.register;

import newcrm.business.businessbase.CPRegister;
import newcrm.global.GlobalMethods;
import newcrm.pages.pugclientpages.PUMenuPage;
import newcrm.pages.auclientpages.Register.*;
import newcrm.pages.pugclientpages.register.*;
import org.openqa.selenium.WebDriver;
import utils.LogUtils;
import vantagecrm.Utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PUASICCPRegister extends CPRegister {

    protected PUASICFinancialDetailsPage asic_fdpage;

    public PUASICCPRegister(WebDriver driver, String url) {
        super(driver, url);
        asic_fdpage = new PUASICFinancialDetailsPage(driver);
    }

    @Override
    protected void setUpACpage() {
        acpage = new PUGAccountConfigurationPage(driver);
    }

    @Override
    protected void setUpPDpage() {
        this.pdpage = new PUASICPersonalDetailsPage(driver);
    }

    @Override
    protected void setUpAddresspage() {
        addresspage = new PUASICResidentialAddressPage(driver);
    }

    @Override
    protected void setUpQuizPage() {
        quizpage = new PUASICQuizPage(driver);
    }

    @Override
    protected void setUpFDpage() {
        fdpage = new PUASICFinancialDetailsPage(driver);
    }

    @Override
    protected void setUpIDpage() {
        idpage = new ASICConfirmIDPage(driver);
    }

    @Override
    protected void setUpFinishPage() {
        finishpage = new PUGFinishPage(driver);
    }

    @Override
    public boolean goToPersonalDetailPage() {
        PUMenuPage menu = new PUMenuPage(this.driver);
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
        entrypage.setPhone(phonenum);
        entrypage.setPassword(pwd);
        entrypage.setBrand(brand);

        userdetails.put("Email", email);
        userdetails.put("Phone Number", phonenum);
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
        userdetails.put("Date Of Birth", pdpage.setBirthDay());
    }

    @Override
    public void fillFinacialPage() {
        // Check Questionnaire Content
        asic_fdpage.verifyQuestionnaireContent();
        userdetails.put("Income Source and Stability", asic_fdpage.setIncomeSourceStability());
        userdetails.put("Financial Circumstances", asic_fdpage.setFinancialCircumstances());
        userdetails.put("Annual Income", asic_fdpage.setEstimatedAnnualIncome());
        userdetails.put("Savings & Investments", asic_fdpage.setEstimatedSavingsAndInvestments());
        userdetails.put("Loss Impact Assessment", asic_fdpage.setLossImpactAssessment());
        userdetails.put("Investment Purpose", asic_fdpage.setInvestmentPurpose());
        userdetails.put("Investment Objective", asic_fdpage.setInvestmentObjective());
        userdetails.put("Risk & Return Expectations", asic_fdpage.setRiskReturnExpectations());
        userdetails.put("Investment Timeframe", asic_fdpage.setInvestmentTimeframe());
        userdetails.put("Risk Tolerance", asic_fdpage.setRiskTolerance());
        asic_fdpage.setAgreeTickbox();
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
        userdetails.put("OTC Derivative Contracts", quizpage.setOTCDerivativeContracts());
        quizpage.next();
        userdetails.put("Leverage Benefits", quizpage.setLeverageBenefit());
        quizpage.next();
        userdetails.put("Leverage Risk", quizpage.setLeverageRisk());
        quizpage.next();
        userdetails.put("Short Position", quizpage.setShortPosition());
        quizpage.next();
        userdetails.put("Long Position", quizpage.setLongPosition());
        quizpage.next();
        userdetails.put("Order Type", quizpage.setOrderType());
        quizpage.next();
        userdetails.put("Market Order", quizpage.setMarketOrder());
        quizpage.next();
        userdetails.put("Stop Loss Purpose", quizpage.setStopLossPurpose());
        quizpage.next();
        userdetails.put("Trade Loss Balance", quizpage.setTradeLossBalance());
        quizpage.submit();
    }

}
