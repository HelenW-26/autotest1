package newcrm.business.pugbusiness.register;

import newcrm.business.businessbase.register.CPRegisterDemo;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.ACCOUNTTYPE;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.pages.pugclientpages.register.*;
import org.openqa.selenium.WebDriver;

public class PUCPRegisterDemo extends CPRegisterDemo {

    public PUCPRegisterDemo(WebDriver driver, String registerURL) {
        super(driver, registerURL);
    }

    @Override
    protected void setUpHomepage() {
        homepage = new PURegisterHomePage(driver,registerURL);
    }

    @Override
    protected void setUpEntrypage() {
        entrypage  = new PURegisterDemoEntryPage(driver);
    }

    @Override
    public void openDemoAccount(String url) {
        entrypage.waitLoadingCookiePreferencesContent();
        entrypage.setAllowCookie();
        homepage.registerDemoAccount();
        homepage.setDemoRegistrationDomainUrl(url);
    }

    @Override
    public void fillDemoEntryPage(String email, String firstName, String lastName, String country, String phone, String pwd, String branchVersion) {
        setDemoUserInfo(email, firstName, lastName, country, phone, branchVersion);
    }

    @Override
    public void setDemoUserInfo(String email, String firstName, String lastName, String country, String phone, String branchVersion) {
        entrypage.setCountry(country);
        entrypage.setFirstName(firstName);
        entrypage.setLastName(lastName);
        entrypage.setEmail(email);
        entrypage.setPhone(phone);
        entrypage.setBranchVersion(branchVersion);

        userdetails.put("Country", country);
        userdetails.put("First Name", firstName);
        userdetails.put("Last Name",lastName);
        userdetails.put("Email", email);
        userdetails.put("Phone Number", phone);
        userdetails.put("Branch Version", branchVersion);
    }

    @Override
    public void fillDemoAccountPage(PLATFORM platform, ACCOUNTTYPE accountType, CURRENCY currency, String leverage, String balance, String branchVersion) {
        entrypage.clickExpandAccountConfig();

        entrypage.setPlatForm(platform);
        entrypage.setAccountType(platform, accountType);
        entrypage.setCurrency(currency);
        entrypage.setLeverage(leverage);
        entrypage.setAccountBalance(balance);

        userdetails.put("Demo Account Trading Platform", platform.getPlatformDesc());
        userdetails.put("Demo Account Type", accountType.getLiveAccountName());
        userdetails.put("Demo Account Currency", currency.getCurrencyDesc());
        userdetails.put("Demo Account Leverage", leverage);
        userdetails.put("Demo Account Balance", balance);

        homepage.clickDemoContinueBtn();
    }

}
