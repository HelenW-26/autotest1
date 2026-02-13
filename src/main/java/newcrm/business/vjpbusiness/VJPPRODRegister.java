package newcrm.business.vjpbusiness;

import newcrm.global.GlobalMethods;
import newcrm.pages.vjpclientpages.*;
import org.openqa.selenium.WebDriver;

public class VJPPRODRegister extends VJPRegister {

    public VJPPRODRegister(WebDriver driver,String url)
    {
        super(driver,url);
        this.homepage = new ProdVJPRegisterHomePage(driver,registerURL);
    }

    @Override
    protected void setUpPDpage() {
        this.pdpage = new PRODVJPPersonalDetailsPage(driver);
    }

    @Override
    protected void setUpEntrypage() {
        entrypage  = new ProdVJPRegisterEntryPage(driver);
    }

    @Override
    protected void setUpIDpage() {
        idpage = new ProdVJPConfirmIDPage(driver);
    }

    @Override
    public void setTradeUrl(String url) {
        GlobalMethods.printDebugInfo("Prod enviroment do not need set trade url");
    }

    @Override
    public boolean setRegulatorAndCountry(String country,String regulator) {
        if(!entrypage.setCountry(country)) {
            return false;
        }
        GlobalMethods.printDebugInfo("Prod enviroment do not need set regulator");
        userdetails.put("Country", country);
        userdetails.put("Regulator", regulator);
        return true;
    }

    @Override
    public void setUserInfo(String fistName,String country,String email,String pwd) {
        setCountry(country);
        //setEmail(email);
        setPwd(pwd);
        checkUNonUS();
    }

    @Override
    public void fillPersonalDetails(String idNum,String firstName, String lastName,String phone) {
        userdetails.put("gender", pdpage.setGender("Female"));
        userdetails.put("Date Of Birth", pdpage.setBirthDay());
        userdetails.put("firstName", pdpage.setfirstName(firstName));
        userdetails.put("lastName",pdpage.setLastName(lastName));
        userdetails.put("phone",pdpage.setPhone(phone));
    }

    @Override
    public boolean goToFinishPage() {
        return true;
    }


    @Override
    public void setEmail(String email) {
        entrypage.setEmail(email);
        userdetails.put("Email", email);
        entrypage.submit();
    }

    @Override
    public boolean goToPersonalDetailPage() {
        entrypage.next();
        GlobalMethods.printDebugInfo("CPRegister: go to personal details page.");

        VJPMenuPage menu = new VJPMenuPage(this.driver);
        PRODVJPPersonalDetailsPage prodPdPage = new PRODVJPPersonalDetailsPage(driver);

        pdpage.closeOpenAccountDialog();
        menu.changeLanguage("English");
        menu.clickProfile();
        prodPdPage.clickOpenAccountVerifyBtn();

        return true;
    }

    @Override
    public void entrySubmit(String traderURL) {}
}
