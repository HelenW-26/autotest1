package newcrm.business.businessbase.register;

import newcrm.business.businessbase.CPRegisterGold;
import newcrm.pages.clientpages.register.RegisterDemoGoldenFlowEntryPage;
import newcrm.pages.clientpages.register.RegisterEntryPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import tools.ScreenshotHelper;
import utils.LogUtils;

import java.util.Objects;

public class CPRegisterDemoGoldenFlow extends CPRegisterGold
{
    public CPRegisterDemoGoldenFlow(WebDriver driver, String registerURL) {
        super(driver, registerURL);
    }

    protected RegisterDemoGoldenFlowEntryPage entrypage;

    @Override
    protected void setUpEntrypage() {
        entrypage  = new RegisterDemoGoldenFlowEntryPage(driver);
    }

    public void openDemoAccount(String url, String email, String firstName, String country, String phone, String pwd, String branchVersion) {
        String oldUrl = homepage.getCurrentURL();
        homepage.registerDemoAccount();
        homepage.waitLoadingPageContent(oldUrl);
        homepage.setDemoRegistrationDomainUrl(url);
        entrypage.setEmail(email);
        userdetails.put("Email", email);
        homepage.clickDemoContinueBtn();
        setDemoUserInfo(firstName, country, phone, pwd, branchVersion);
    }

    public void setDemoUserInfo(String firstName, String country, String phone, String pwd, String branchVersion) {
        entrypage.setFirstName(firstName);
        entrypage.setCountry(country);
        entrypage.setPhone(phone);
        entrypage.setPassword(pwd);
        entrypage.setBranchVersion(branchVersion);

        userdetails.put("First Name", firstName);
        userdetails.put("Country", country);
        userdetails.put("Phone Number", phone);
        userdetails.put("Pwd", pwd);
        userdetails.put("BranchVersion", branchVersion);
    }

    public void proceedToOpenLiveAccount() {
        entrypage.clickOpenLiveAccBtn();
        entrypage.clickOpenAccountVerifyBtn();
    }

    public void checkDemoAccountDetails() {
        ScreenshotHelper.takeFullPageScreenshot(entrypage.getDriver(), "screenshots", "DemoAccountDtl");

        LogUtils.info("**Check Demo Account Details**");
        entrypage.waitLoadingDemoAccConfigResponseContent();

        String demoAccNo = entrypage.getDemoAccNo().getText();
        LogUtils.info("Demo Account: " + demoAccNo);

        if ("".equals(Objects.toString(demoAccNo, ""))) {
            Assert.fail("Demo Account Number is empty");
        }

        String demoAccServer = entrypage.getDemoAccServer().getText();
        LogUtils.info("Demo Account Server: " + demoAccServer);

        if ("".equals(Objects.toString(demoAccServer, ""))) {
            Assert.fail("Demo Account Server is empty");
        }

        String demoAccExpiryDate = entrypage.getDemoAccExpiryDate().getText();
        LogUtils.info("Demo Account Expiry Date: " + demoAccExpiryDate);

        if ("".equals(Objects.toString(demoAccExpiryDate, ""))) {
            Assert.fail("Demo Account Expiry Date is empty");
        }
    }

    @Override
    public void entrySubmit(String traderURL) {
        String oldUrl = entrypage.getCurrentURL();
        entrypage.submit();
        entrypage.checkNavigateSuccess(traderURL, oldUrl, RegisterEntryPage.REG_SRC.DEMO_REG);
    }

}
