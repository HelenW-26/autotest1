package newcrm.business.vjpbusiness.register;

import newcrm.business.businessbase.register.CPRegisterDemo;
import newcrm.pages.vjpclientpages.VJPRegisterHomePage;
import newcrm.pages.vjpclientpages.register.VJPRegisterDemoEntryPage;
import org.openqa.selenium.WebDriver;

public class VJPCPRegisterDemo extends CPRegisterDemo {

    public VJPCPRegisterDemo(WebDriver driver, String registerURL) {
        super(driver, registerURL);
    }

    @Override
    protected void setUpHomepage() {
        homepage = new VJPRegisterHomePage(driver,registerURL);
    }

    @Override
    protected void setUpEntrypage() {
        entrypage  = new VJPRegisterDemoEntryPage(driver);
    }

    @Override
    public void fillDemoEntryPage(String email, String firstName, String lastName, String country, String phone, String pwd, String branchVersion) {
        setEmail(email);
        homepage.clickDemoContinueBtn();

//        setPwd(pwd);
//        entrypage.agreeTnC();
//        entrypage.agreePolicy();
//        entrypage.agreeReceiveDeals();
//        setDemoUserInfo(email, firstName, lastName, country, phone);
    }

}
