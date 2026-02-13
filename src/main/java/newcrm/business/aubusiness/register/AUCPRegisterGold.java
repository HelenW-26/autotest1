package newcrm.business.aubusiness.register;

import newcrm.business.businessbase.CPRegisterGold;
import newcrm.cpapi.APIThirdPartyService;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.auclientpages.AUMenuPage;
import newcrm.pages.auclientpages.Register.AURegisterGoldHomePage;
import newcrm.pages.auclientpages.Register.AURegisterGoldPersonalDetailsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

import java.util.Objects;

public class AUCPRegisterGold extends CPRegisterGold {

    public AUCPRegisterGold(WebDriver driver, String registerURL) {
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
    public boolean goToPersonalDetailPage() {
        AUMenuPage menu = new AUMenuPage(this.driver);
        pdpage.closeToolSkipButton();
        pdpage.closeImg();
//        menu.changeLanguage("English");

        LogUtils.info("CPRegisterGold: go to personal details page.");
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
//    public void fillIDPage_withSumsub() {
//        idpage.closePOIDialog();
//        idpage.clickPOIExitBtn();
//        LogUtils.info("Skip Sumsub Verification");
//    }

}
