package newcrm.business.vtbusiness.register;

import newcrm.business.businessbase.register.CPRegisterDemo;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.ACCOUNTTYPE;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.pages.vtclientpages.register.VTRegisterDemoEntryPage;
import newcrm.pages.vtclientpages.register.VTRegisterHomePage;
import org.openqa.selenium.WebDriver;
import tools.ScreenshotHelper;

public class VTCPRegisterDemo extends CPRegisterDemo {

    public VTCPRegisterDemo(WebDriver driver, String registerURL) {
        super(driver, registerURL);
    }

    @Override
    protected void setUpHomepage() {
        homepage = new VTRegisterHomePage(driver,registerURL);
    }

    @Override
    protected void setUpEntrypage() {
        entrypage  = new VTRegisterDemoEntryPage(driver);
    }

    @Override
    public void fillDemoEntryPage(String email, String firstName, String lastName, String country, String phone, String pwd, String branchVersion) {
        setDemoUserInfo(email, firstName, lastName, country, phone, branchVersion);
        homepage.clickDemoContinueBtn();
    }

    @Override
    public void setDemoUserInfo(String email, String firstName, String lastName, String country, String phone, String branchVersion) {
        entrypage.setFirstName(firstName);
        entrypage.setLastName(lastName);
        entrypage.setCountry(country);
        entrypage.setPhone(phone);
        entrypage.setEmail(email);

        userdetails.put("First Name", firstName);
        userdetails.put("Last Name",lastName);
        userdetails.put("Country", country);
        userdetails.put("Phone Number", phone);
        userdetails.put("Email", email);
    }

    @Override
    public void checkDemoAccountDetails() {
        ScreenshotHelper.takeFullPageScreenshot(entrypage.getDriver(), "screenshots", "DemoAccountDtl");
        entrypage.waitLoadingDemoAccConfigResponseContent();
        getDemoAccNo();
        getDemoAccServer();
    }

    @Override
    public void fillDemoAccountPage(PLATFORM platform, ACCOUNTTYPE accountType, CURRENCY currency, String leverage, String balance, String branchVersion) {
        entrypage.setPlatForm(platform);
        entrypage.setAccountType(platform, accountType);
        entrypage.setCurrency(currency);
        entrypage.setLeverage(leverage);
        entrypage.setAccountBalance(balance);
        entrypage.setBranchVersion(branchVersion);

        userdetails.put("Demo Account Trading Platform", platform.getPlatformDesc());
        userdetails.put("Demo Account Type", accountType.getLiveAccountName());
        userdetails.put("Demo Account Currency", currency.getCurrencyDesc());
        userdetails.put("Demo Account Leverage", leverage);
        userdetails.put("Demo Account Balance", balance);
        userdetails.put("Branch Version", branchVersion);
    }

}
