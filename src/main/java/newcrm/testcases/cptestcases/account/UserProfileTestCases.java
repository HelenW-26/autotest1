package newcrm.testcases.cptestcases.account;

import newcrm.business.businessbase.CPLogin;
import newcrm.business.businessbase.CPMenu;
import newcrm.business.businessbase.profile.CPUserProfile;
import newcrm.factor.Factor;
import newcrm.global.GlobalProperties.CPMenuName;
import newcrm.testcases.BaseTestCaseNew;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

public class UserProfileTestCases extends BaseTestCaseNew {

    public Object data[][];
    private CPMenu menu;

    private Factor myfactor;
    private CPLogin login;
    private WebDriver driver;

    @BeforeMethod(groups = {"CP_Profile_Info_Masking_Check"})
    protected void initMethod(Method method) {

        myfactor = getFactorNew();
        login = getLogin();
        driver = getDriverNew();

//		checkValidLoginSession();
        login.goToCpHome();
        menu = myfactor.newInstance(CPMenu.class);
        menu.goToMenu(CPMenuName.CPPORTAL);
        menu.changeLanguage("English");
        menu.goToMenu(CPMenuName.HOME);
    }

    public void userProfileContactInfoMaskingCheck() throws Exception {
        CPUserProfile userProfile = myfactor.newInstance(CPUserProfile.class);

        menu.goToMenu(CPMenuName.PROFILES);
        menu.goToMenu(CPMenuName.SECURITYMANAGEMENT);

        userProfile.checkProfileBoxInfoMasking();
        userProfile.checkProfileMainInfoMasking();

        System.out.println("***Test User Profile Contact Info Masking Check succeed!!********");
    }

}
