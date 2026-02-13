package newcrm.business.starbusiness.register;

import newcrm.business.businessbase.register.CPRegisterDemo;
import newcrm.pages.starclientpages.STARRegisterHomePage;
import newcrm.pages.starclientpages.register.*;
import org.openqa.selenium.WebDriver;

public class STARCPRegisterDemo extends CPRegisterDemo {

    public STARCPRegisterDemo(WebDriver driver, String registerURL) {
        super(driver, registerURL);
    }

    @Override
    protected void setUpHomepage() {
        homepage = new STARRegisterHomePage(driver,registerURL);
    }

    @Override
    protected void setUpEntrypage() {
        entrypage  = new STARRegisterDemoEntryPage(driver);
    }

    @Override
    public void openDemoAccount(String url) {
        entrypage.setAllowCookie();
        homepage.registerDemoAccount();
        homepage.setDemoRegistrationDomainUrl(url);
    }

}
