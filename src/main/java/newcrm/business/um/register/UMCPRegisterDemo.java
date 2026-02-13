package newcrm.business.um.register;

import newcrm.business.businessbase.register.CPRegisterDemo;
import newcrm.pages.umclient.register.*;
import org.openqa.selenium.WebDriver;

public class UMCPRegisterDemo extends CPRegisterDemo {

    public UMCPRegisterDemo(WebDriver driver, String registerURL) {
        super(driver, registerURL);
    }

    @Override
    protected void setUpHomepage() {
        homepage = new UMRegisterHomePage(driver,registerURL);
    }

    @Override
    protected void setUpEntrypage() {
        entrypage  = new UMRegisterDemoEntryPage(driver);
    }

}
