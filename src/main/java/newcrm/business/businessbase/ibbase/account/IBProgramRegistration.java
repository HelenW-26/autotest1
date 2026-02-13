package newcrm.business.businessbase.ibbase.account;

import newcrm.global.GlobalProperties;
import newcrm.pages.ibpages.*;
import org.openqa.selenium.WebDriver;


public class IBProgramRegistration {

    public GlobalProperties.BRAND dbBrand;

    protected static WebDriver driver;
    protected IBProgramRegistrationPage ibProgramRegistrationPage;


    public IBProgramRegistration(WebDriver driver) {
        this.ibProgramRegistrationPage = new IBProgramRegistrationPage(driver);
    }

    public IBProgramRegistration(IBProgramRegistrationPage ibProgramRegistrationPage) {
        this.ibProgramRegistrationPage = ibProgramRegistrationPage;
    }

    public void registerNewIBThroughIBProgram(String cpURL, String country, String email, String firstName, String lastName, String phone, String pwd) throws InterruptedException {
        ibProgramRegistrationPage.registerNewIBThroughIBProgram(cpURL, country, email, firstName, lastName, phone, pwd);

    }


}