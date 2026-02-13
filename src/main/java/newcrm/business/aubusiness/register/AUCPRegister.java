package newcrm.business.aubusiness.register;

import newcrm.business.businessbase.CPRegister;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.auclientpages.Register.*;
import newcrm.pages.auclientpages.AUMenuPage;
import newcrm.pages.clientpages.MenuPage;
import newcrm.pages.clientpages.register.ConfirmIDPage;
import newcrm.pages.pugclientpages.register.*;
import org.openqa.selenium.WebDriver;
import utils.LogUtils;
import vantagecrm.Utils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class AUCPRegister extends CPRegister {
    String firstName = "autotest" + GlobalMethods.getRandomString(10);
    String lastName = "TestCRM";
    String phone = GlobalMethods.getRandomNumberString(10);

    public AUCPRegister(WebDriver driver, String registerURL) {
        super(driver, registerURL);
    }

    @Override
    protected void setUpACpage() {
        acpage = new AUAccountConfigurationPage(driver);
    }

    @Override
    protected void setUpPDpage() {
        this.pdpage = new AUPersonalDetailsPage(driver);
    }

    @Override
    protected void setUpIDpage() {
        idpage = new AUConfirmIDPage(driver);
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
    protected void setUpEntrypage() {
        entrypage = new AURegisterEntryPage(driver);
    }

    @Override
    public boolean goToPersonalDetailPage() {
        //check
        AUMenuPage menu = new AUMenuPage(this.driver);
       // menu.refresh();
        pdpage.closeImg();
//        menu.changeLanguage("English");

        LogUtils.info("CPRegister: go to personal details page.");
        return true;
    }

    @Override
    public void fillPersonalDetails(String idNum) {
        userdetails.put("gender", pdpage.setGender("Female"));
        userdetails.put("Date Of Birth", pdpage.setBirthDay());
        userdetails.put("firstName", pdpage.setfirstName(firstName));
        userdetails.put("lastName",pdpage.setLastName(lastName));
        userdetails.put("phone",pdpage.setPhone(phone));

    }

    @Override
    public boolean goToIDPage() {
        acpage.next();
        LogUtils.info("CPRegister: go to ACCOUNT OPENING page.");
        acpage.next();
        LogUtils.info("CPRegister: go to CONFIRM YOUR ID page.");
        return true;
    }

    @Override
    public void fillIDPage() {
        Path parent = Paths.get("src", "main", "resources", "vantagecrm", "doc");
        String fileFront = Paths.get(parent.toString(), "Update_ID_Card.jpg").toString();
        String fileBack = Paths.get(parent.toString(), "Update_ID_Card_Back.jpg").toString();

        String num = GlobalMethods.getRandomNumberString(10);
        String idnum = "TESTID"+GlobalMethods.getRandomNumberString(10);
        String city = "TESTID"+GlobalMethods.getRandomString(5);
        String postcode = GlobalMethods.getRandomNumberString(5);
        String address = GlobalMethods.getRandomString(5);

        idpage.setIdentificationType();
        idpage.setIDNumber(idnum);
        userdetails.put("id Number", idnum);

        idpage.uploadID(Paths.get(Utils.workingDir, fileFront).toString());
        idpage.uploadIDBack(Paths.get(Utils.workingDir, fileBack).toString());

        // Submit POI
        idpage.uploadBtn();
        // Check for error message
        idpage.checkExistsAlertMsg("Submit POI");
    }

    @Override
    public boolean setRegulatorAndCountry(String country,String regulator) {

        if(!entrypage.setCountry(country)) {
            return false;
        }
        userdetails.put("Country", country);
        return true;
    }

    @Override
    public void setUserInfo(String firstName, String lastName, String phonenum,String email,String pwd, String brand) {
        //entrypage.setFirstName(firstName);
        //entrypage.setLastName(lastName);
        //entrypage.setPhone(phonenum);
        entrypage.setEmail(email);
        entrypage.setPassword(pwd);
        entrypage.setBrand(brand);

        //userdetails.put("First Name", firstName);
        //userdetails.put("Last Name",lastName);
        //userdetails.put("Phone Number", phonenum);
        userdetails.put("Email", email);
        userdetails.put("pwd", pwd);
        userdetails.put("Brand", brand);
    }

    @Override
    public void fillPersonalDetails(String idNum,String firstName, String lastName,String phone) {
        pdpage.closeToolSkipButton();
        userdetails.put("gender", pdpage.setGender("Female"));
        userdetails.put("Date Of Birth", pdpage.setBirthDay());
        userdetails.put("firstName", pdpage.setfirstName(firstName));
        userdetails.put("lastName",pdpage.setLastName(lastName));
        userdetails.put("phone",pdpage.setPhone(phone));
    }

    @Override
    public boolean fillAccountPage(GlobalProperties.PLATFORM platform, GlobalProperties.ACCOUNTTYPE accountType, GlobalProperties.CURRENCY currency) {
        waitLoading();
        if(!acpage.setPlatForm(platform)) {
            return false;
        }
        userdetails.put("Platform", platform.toString());
        userdetails.put("Account Type", acpage.setAccountType(accountType));
        userdetails.put("Currency", acpage.setCurrency(currency));
        return true;
    }

}
