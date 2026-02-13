package newcrm.business.businessbase.register;

import newcrm.business.businessbase.CPRegister;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.PLATFORM;
import newcrm.global.GlobalProperties.ACCOUNTTYPE;
import newcrm.global.GlobalProperties.CURRENCY;
import newcrm.pages.clientpages.register.RegisterDemoEntryPage;
import newcrm.pages.clientpages.register.RegisterEntryPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import tools.ScreenshotHelper;
import utils.LogUtils;

import java.util.Objects;

public class CPRegisterDemo extends CPRegister {

    public CPRegisterDemo(WebDriver driver, String registerURL) {
        super(driver, registerURL);
    }

    protected RegisterDemoEntryPage entrypage;

    @Override
    protected void setUpEntrypage() {
        entrypage  = new RegisterDemoEntryPage(driver);
    }

    public void getDemoAccNo() {
        String demoAccNo = entrypage.getDemoAccNo().getText();
        LogUtils.info("Demo Account: " + demoAccNo);

        if ("".equals(Objects.toString(demoAccNo, ""))) {
            Assert.fail("Demo Account Number is empty");
        }
    }

    public void getDemoAccServer() {
        String demoAccServer = entrypage.getDemoAccServer().getText();
        LogUtils.info("Demo Account Server: " + demoAccServer);

        if ("".equals(Objects.toString(demoAccServer, ""))) {
            Assert.fail("Demo Account Server is empty");
        }
    }

    public void getDemoAccExpiryDate() {
        String demoAccExpiryDate = entrypage.getDemoAccExpiryDate().getText();
        LogUtils.info("Demo Account Expiry Date: " + demoAccExpiryDate);

        if ("".equals(Objects.toString(demoAccExpiryDate, ""))) {
            Assert.fail("Demo Account Expiry Date is empty");
        }
    }

    @Override
    public void setEmail(String email) {
        entrypage.setEmail(email);
        userdetails.put("Email", email);
    }

    public void setDemoUserInfo(String email, String firstName, String lastName, String country, String phone, String branchVersion) {
        entrypage.setFirstName(firstName);
        entrypage.setCountry(country);
        entrypage.setPhone(phone);
        entrypage.setBranchVersion(branchVersion);

        userdetails.put("First Name", firstName);
        userdetails.put("Country", country);
        userdetails.put("Phone Number", phone);
        userdetails.put("Branch Version", branchVersion);
    }

    public void openDemoAccount(String url) {
        homepage.registerDemoAccount();
        homepage.setDemoRegistrationDomainUrl(url);
    }

    public void fillDemoEntryPage(String email, String firstName, String lastName, String country, String phone, String pwd, String branchVersion) {
        setEmail(email);
        homepage.clickDemoContinueBtn();
        setDemoUserInfo(email, firstName, lastName, country, phone, branchVersion);
    }

    public void proceedToOpenLiveAccount() {
        entrypage.clickOpenLiveAccBtn();
        entrypage.clickOpenAccountVerifyBtn();
    }

    public void checkDemoAccountDetails() {
        ScreenshotHelper.takeFullPageScreenshot(entrypage.getDriver(), "screenshots", "DemoAccountDtl");
        entrypage.waitLoadingDemoAccConfigResponseContent();
        getDemoAccNo();
        getDemoAccServer();
        getDemoAccExpiryDate();
    }

    @Override
    public void entrySubmit(String traderURL) {
        String oldUrl = entrypage.getCurrentURL();
        entrypage.submit();
        entrypage.checkNavigateSuccess(traderURL, oldUrl, RegisterEntryPage.REG_SRC.DEMO_REG);
    }

    public void fillDemoAccountPage(PLATFORM platform, ACCOUNTTYPE accountType, CURRENCY currency, String leverage, String balance, String branchVersion) {}

}
