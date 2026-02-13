package newcrm.business.vjpbusiness;

import newcrm.business.businessbase.CPRegister;
import newcrm.global.GlobalMethods;
import newcrm.pages.vjpclientpages.VJPFinishPage;
import newcrm.pages.vjpclientpages.VJPRegisterEntryPage;
import newcrm.pages.vjpclientpages.VJPRegisterHomePage;
import newcrm.pages.vjpclientpages.VJPAccountConfigurationPage;
import newcrm.pages.vjpclientpages.VJPConfirmIDPage;
import newcrm.pages.vjpclientpages.VJPPersonalDetailsPage;
import org.openqa.selenium.WebDriver;
import utils.LogUtils;
import vantagecrm.Utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class VJPRegister extends CPRegister {
    String firstName = "autotest" + GlobalMethods.getRandomString(10);
    String lastName = "TestCRM";
    String phone = GlobalMethods.getRandomNumberString(10);

    public VJPRegister(WebDriver driver, String url) {
        super(driver, url);
    }

    @Override
    protected void setUpACpage() {
        acpage = new VJPAccountConfigurationPage(driver);
    }

    @Override
    protected void setUpPDpage() {
        this.pdpage = new VJPPersonalDetailsPage(driver);
    }

    @Override
    protected void setUpFinishPage() {
        finishpage = new VJPFinishPage(driver);
    }

    @Override
    protected void setUpHomepage() {
        homepage = new VJPRegisterHomePage(driver, registerURL);
    }

    @Override
    protected void setUpEntrypage() {
        entrypage = new VJPRegisterEntryPage(driver);
    }

    @Override
    protected void setUpIDpage() {
        idpage = new VJPConfirmIDPage(driver);
    }

    @Override
    public boolean goToPersonalDetailPage() {
        //check
       /* MenuPage menu = new MenuPage(this.driver);
        // menu.refresh();
        pdpage.closeImg();
        menu.changeLanguage("English");*/

        LogUtils.info("CPRegister: go to personal details page.");
        return true;
    }

    @Override
    public void fillPersonalDetails(String idNum) {
        userdetails.put("gender", pdpage.setGender("Female"));
        userdetails.put("Date Of Birth", pdpage.setBirthDay());
        userdetails.put("firstName", pdpage.setfirstName(firstName));
        userdetails.put("lastName", pdpage.setLastName(lastName));
        userdetails.put("phone", pdpage.setPhone(phone));

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
        String filePOA = Paths.get(parent.toString(), "Update_ID_Card.jpg").toString();


        String num = GlobalMethods.getRandomNumberString(10);
        String idnum = "TESTID" + GlobalMethods.getRandomNumberString(10);
        String city = "TESTID" + GlobalMethods.getRandomString(5);
        String postcode = GlobalMethods.getRandomNumberString(5);
        String address = GlobalMethods.getRandomString(5);

        idpage.setIdentificationType();
        idpage.setIDNumber(idnum);

        idpage.uploadID(Paths.get(Utils.workingDir, fileFront).toString());
        idpage.uploadIDBack(Paths.get(Utils.workingDir, fileBack).toString());

        // Submit POI
        idpage.uploadBtn();
        // Check for error message
        idpage.checkExistsAlertMsg("Submit POI");
    }

    @Override
    public boolean setRegulatorAndCountry(String country, String regulator) {

        if (!entrypage.setCountry(country)) {
            return false;
        }
        userdetails.put("Country", country);
        return true;
    }

    @Override
    public void setUserInfo(String firstName, String lastName, String phonenum, String email, String pwd, String brand) {
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
    public void fillPersonalDetails(String idNum, String firstName, String lastName, String phone) {
        pdpage.closeToolSkipButton();
        userdetails.put("gender", pdpage.setGender("Female"));
        userdetails.put("Date Of Birth", pdpage.setBirthDay());
        userdetails.put("firstName", pdpage.setfirstName(firstName));
        userdetails.put("lastName", pdpage.setLastName(lastName));
        userdetails.put("phone", pdpage.setPhone(phone));
    }
}
