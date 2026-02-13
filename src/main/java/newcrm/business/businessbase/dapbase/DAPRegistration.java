package newcrm.business.businessbase.dapbase;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.MenuPage;
import newcrm.pages.dappages.DAPRegisterPOIPage;
import newcrm.pages.dappages.DAPRegisterPersonalDetailsPage;
import newcrm.pages.dappages.DAPRegisterResidentialAddressPage;
import newcrm.pages.dappages.DAPRegistrationPage;
import newcrm.pages.ibpages.IBReportPage;
import org.openqa.selenium.WebDriver;
import utils.LogUtils;
import vantagecrm.Utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DAPRegistration {

    protected WebDriver driver;
    protected DAPRegistrationPage dapRegistrationPage;
    protected DAPRegisterPersonalDetailsPage dapRegisterPersonalDetailsPage;
    protected DAPRegisterPOIPage dapRegisterPOIPage;
    protected DAPRegisterResidentialAddressPage dapRegisterResidentialAddressPage;

    public DAPRegistration(WebDriver driver) {
        this.driver = driver;

        this.dapRegistrationPage = new DAPRegistrationPage(driver);
        this.dapRegisterPersonalDetailsPage = new DAPRegisterPersonalDetailsPage(driver);
        this.dapRegisterPOIPage = new DAPRegisterPOIPage(driver);
        this.dapRegisterResidentialAddressPage = new DAPRegisterResidentialAddressPage(driver);
    }

    public DAPRegistration(DAPRegistrationPage dapRegistrationPage) {
        this.dapRegistrationPage = dapRegistrationPage;
    }

    public DAPRegistration(DAPRegisterPersonalDetailsPage dapRegisterPersonalDetailsPage) {
        this.dapRegisterPersonalDetailsPage = dapRegisterPersonalDetailsPage;
    }

    public DAPRegistration(DAPRegisterPOIPage dapRegisterPOIPage) {
        this.dapRegisterPOIPage = dapRegisterPOIPage;
    }

    public DAPRegistration(DAPRegisterResidentialAddressPage dapRegisterResidentialAddressPage) {
        this.dapRegisterResidentialAddressPage = dapRegisterResidentialAddressPage;
    }

    public void registerNewDAP(String cpURL, String firstName, String lastName, String email, String phone, String pwd, String country) throws InterruptedException {
        dapRegistrationPage.registerNewDAP(cpURL, firstName, lastName, email, phone, pwd, country);
    }

    public void sendEmailCode(String OTP) throws InterruptedException {
        dapRegisterPersonalDetailsPage.clickEmailCodeBtn();
        Thread.sleep(2000);
        dapRegisterPersonalDetailsPage.sendEmailCode(OTP);
        dapRegisterPersonalDetailsPage.nextStep();
    }

    public void sendPhoneCode(String OTP) throws InterruptedException {
        dapRegisterPersonalDetailsPage.clickPhoneCodeBtn();
        Thread.sleep(2000);
        dapRegisterPersonalDetailsPage.sendPhoneCode(OTP);
        dapRegisterPersonalDetailsPage.submit();
    }
    public void fillPersonalDetails(String firstName, String lastName,String phone) {
        dapRegisterPersonalDetailsPage.waitLoadingKycPDContent();
        dapRegisterPersonalDetailsPage.setGender("Female");
        dapRegisterPersonalDetailsPage.setBirthDay();
        dapRegisterPersonalDetailsPage.setfirstName(firstName);
        dapRegisterPersonalDetailsPage.setLastName(lastName);
    }

    public boolean goToPersonalDetailsSummaryPage() {
        dapRegisterPersonalDetailsPage.submit();
        LogUtils.info("Go to Personal Details Summary page.");
        return true;
    }

    public boolean goToPersonalDetailPage() {
        MenuPage menu = new MenuPage(this.driver);
        menu.refresh();

        LogUtils.info("Go to Personal Details page.");
        return true;
    }

    public void waitLoading() {
        dapRegisterPersonalDetailsPage.waitLoading();
    }

    public void waitLoadingIdentityVerificationContent() {
        dapRegisterPOIPage.waitLoadingIdentityVerificationContent();
    }

    public void fillIDPage() {
        Path parent = Paths.get("src", "main", "resources", "vantagecrm", "doc");
        String fileFront = Paths.get(parent.toString(), "Update_ID_Card.jpg").toString();
        String fileBack = Paths.get(parent.toString(), "Update_ID_Card_Back.jpg").toString();
        String idnum = "TESTID"+ GlobalMethods.getRandomNumberString(10);

        dapRegisterPOIPage.nextStep();
        dapRegisterPOIPage.setIDType();
        dapRegisterPOIPage.setIDNumber(idnum);
        dapRegisterPOIPage.nextStep();
        dapRegisterPOIPage.uploadIDFront(Paths.get(Utils.workingDir, fileFront).toString());

        dapRegisterPOIPage.uploadIDBack(Paths.get(Utils.workingDir, fileBack).toString());
        dapRegisterPOIPage.nextStep();
        // Check for error message
        dapRegisterPOIPage.checkExistsAlertMsg("Submit POI");
    }

    public void closeProfileVerificationDialog_withoutExit() {
        dapRegisterPOIPage.waitLoader();
        dapRegisterPOIPage.verifyPOIConfirmBtn();
        dapRegisterPOIPage.clickPOICloseBtn();
    }

    public void fillIDPage_withSumsub(String country) {
        Path parent = Paths.get("src", "main", "resources", "vantagecrm", "doc");
        String fileFront = Paths.get(parent.toString(), "ID_Card_Gen.jpg").toString();
        String fileBack = Paths.get(parent.toString(), "ID_Card_Back_Gen.jpg").toString();

        // Wait for Sumsub content to load
        dapRegisterPOIPage.waitLoadingSumSubContent();
        // Check whether page has access to Sumsub
        if (!dapRegisterPOIPage.checkHasSumSubAccess()) {
            return;
        }
        // Subsum - Change Language
        dapRegisterPOIPage.sumSubChangeLanguage();
        // Sumsub - Start Verification Step
        dapRegisterPOIPage.startSumSubVerification();
        // Sumsub - Personal Information Step
        dapRegisterPOIPage.fillSumSubPersonalDetails();
        // Sumsub - Select Document Type Step
        dapRegisterPOIPage.setSumSubIdentificationType(country);
        // Sumsub - Upload Document Step
        dapRegisterPOIPage.uploadSumSubDocType(Paths.get(Utils.workingDir, fileFront).toString(), Paths.get(Utils.workingDir, fileBack).toString());
        // Switch back to default content
        driver.switchTo().defaultContent();
//        idpage.nextStep();
    }

    public void fillAddressDetails_withSumsub() {
        Path parent = Paths.get("src", "main", "resources", "vantagecrm", "doc");
        String fileFront = Paths.get(parent.toString(), "ID_Card_Gen.jpg").toString();

        dapRegisterPOIPage.waitLoadingPOAVerificationContent();
        // Wait for Sumsub content to load
        dapRegisterPOIPage.waitLoadingSumSubContent();
        // Check whether page has access to Sumsub
        if (!dapRegisterPOIPage.checkHasSumSubAccess()) {
            return;
        }
        // Subsum - Change Language
        dapRegisterPOIPage.sumSubChangeLanguage();
        // Sumsub - Start Verification Step
        dapRegisterPOIPage.startSumSubVerification();
        // Sumsub - Personal Information Step
        dapRegisterPOIPage.fillSumSubPersonalDetails();
        // Sumsub - Upload Document Step
        dapRegisterPOIPage.uploadSumSubPOA(Paths.get(Utils.workingDir, fileFront).toString());
        // Switch back to default content
        driver.switchTo().defaultContent();
    }

    public void closeProfileVerificationDialog() {
        dapRegisterPOIPage.waitLoader();
        dapRegisterPOIPage.clickPOICloseBtn();
        dapRegisterPOIPage.clickPOIExitBtn();
    }

    public void fillAddressDetails() {
        dapRegisterPOIPage.continueToPOA();

        Path parent = Paths.get("src", "main", "resources", "vantagecrm", "doc");
        String filePOA = Paths.get(parent.toString(), "Update_ID_Card.jpg").toString();

        String streetNum = GlobalMethods.getRandomNumberString(3);
        String streetName = GlobalMethods.getRandomString(15);
        dapRegisterResidentialAddressPage.setAddress(streetNum, streetName);
//        userdetails.put("Address", streetNum + " " + streetName);

        String suburb = GlobalMethods.getRandomString(6) + " test suburb";
        dapRegisterResidentialAddressPage.setSuburb(suburb);
//        userdetails.put("suburb", suburb);

        dapRegisterPOIPage.nextStep();
        dapRegisterPOIPage.nextStep();

        // Submit POA
        dapRegisterPOIPage.uploadIDFront(Paths.get(Utils.workingDir, filePOA).toString());
        dapRegisterResidentialAddressPage.submit();
    }

}
