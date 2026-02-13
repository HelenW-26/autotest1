package newcrm.business.aubusiness.register;

import newcrm.business.businessbase.CPRegisterGold;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.auclientpages.AUMenuPage;
import newcrm.pages.auclientpages.Register.AUProdVFSCRegisterGoldEntryPage;
import newcrm.pages.auclientpages.Register.AURegisterGoldHomePage;
import newcrm.pages.auclientpages.Register.AURegisterGoldPersonalDetailsPage;
import org.openqa.selenium.WebDriver;
import utils.LogUtils;
import vantagecrm.Utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AUProdVFSCCPRegisterGold extends CPRegisterGold {

    public AUProdVFSCCPRegisterGold(WebDriver driver, String registerURL) {
        super(driver, registerURL);
    }

    @Override
    protected void setUpHomepage() {
        homepage = new AURegisterGoldHomePage(driver,registerURL);
    }

    @Override
    protected void setUpEntrypage() {
        entrypage  = new AUProdVFSCRegisterGoldEntryPage(driver);
    }

    @Override
    protected void setUpPDpage() {
        this.pdpage = new AURegisterGoldPersonalDetailsPage(driver);
    }

    @Override
    public boolean setRegulatorAndCountry(String country,String regulator) {
        if(!entrypage.setCountry(country)) {
            return false;
        }
        LogUtils.info("Prod environment do not need set regulator");

        userdetails.put("Country", country);
        userdetails.put("Regulator", regulator);

        return true;
    }

    @Override
    public void setUserInfo(String fistName,String country,String email,String pwd) {
        setCountry(country);
        setEmail(email);
        setPwd(pwd);
        checkUNonUS();
    }

    @Override
    public void setTradeUrl(String url) {
        LogUtils.info("Prod environment do not need set to trader url");
    }

    @Override
    public boolean goToPersonalDetailPage() {
        AUMenuPage menu = new AUMenuPage(this.driver);
        pdpage.closeToolSkipButton();
        pdpage.closeImg();
//        menu.changeLanguage("English");

        LogUtils.info("CPRegisterGold: go to personal details page.");
        return true;
    }

    @Override
    public void fillPersonalDetails(String firstName, String lastName,String phone) {
        pdpage.waitLoadingKycPDContent();
        userdetails.put("gender", pdpage.setGender("Female"));
        userdetails.put("Date Of Birth", pdpage.setBirthDay());
        userdetails.put("firstName", pdpage.setfirstName(firstName));
        userdetails.put("lastName",pdpage.setLastName(lastName));
    }

}
