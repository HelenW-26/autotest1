package newcrm.business.starbusiness.register;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.starclientpages.PRODSTARPersonalDetailsPage;
import newcrm.pages.starclientpages.PRODSTARRegisterEntryPage;
import newcrm.pages.starclientpages.ProdSTARConfirmIDPage;
import org.openqa.selenium.WebDriver;
import utils.LogUtils;

public class STARPRODRegister extends STARCPRegister {

    public STARPRODRegister(WebDriver driver,String url)
    {
        super(driver,url);
    }
    @Override
    protected void setUpEntrypage() {
        entrypage  = new PRODSTARRegisterEntryPage(driver);
    }

    protected void setUpPDpage() {
        this.pdpage = new PRODSTARPersonalDetailsPage(driver);
    }

    @Override
    protected void setUpIDpage() {
        this.idpage = new ProdSTARConfirmIDPage(driver);
    }

    @Override
    public void setTradeUrl(String url) {
        LogUtils.info("Prod enviroment do not need set trade url");
    }

    @Override
    public boolean setRegulatorAndCountry(String country,String regulator) {
        if(!entrypage.setCountry(country)) {
            return false;
        }
        LogUtils.info("Prod enviroment do not need set regulator");
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
        checkAgreeCom();
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
    public boolean goToPersonalDetailPage() {
        entrypage.next();

        LogUtils.info("CPRegister: go to personal details page.");
        return true;
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
        acpage.tickBox();
        return true;
    }

    @Override
    public boolean goToFinishPage() {
        return true;
    }

    @Override
    public void entrySubmit(String traderURL) {}
}
