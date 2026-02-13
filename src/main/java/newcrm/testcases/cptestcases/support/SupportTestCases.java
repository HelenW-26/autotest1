package newcrm.testcases.cptestcases.support;

import newcrm.business.businessbase.CPLogin;
import newcrm.business.businessbase.CPMenu;
import newcrm.business.businessbase.support.CPSupport;
import newcrm.factor.Factor;
import newcrm.global.GlobalProperties.CPMenuName;
import newcrm.testcases.BaseTestCaseNew;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

public class SupportTestCases extends BaseTestCaseNew {

    protected Object data[][];
    private CPMenu menu;
    private CPLogin login;
    private Factor myfactor;
    private WebDriver driver;

    @BeforeMethod(groups= {"CP_Support_Ticket"})
    protected void initMethod(Method method) {

        if (driver ==null){
            driver = getDriverNew();
        }
        if (myfactor == null){
            myfactor = getFactorNew();
        }
        if (login == null){
            login = getLogin();
        }

        login.goToCpHome();
        menu = myfactor.newInstance(CPMenu.class);
        menu.goToMenu(CPMenuName.CPPORTAL);
        menu.changeLanguage("English");
        menu.goToMenu(CPMenuName.HOME);
    }

    public void createSupportTicket() {
        CPSupport cpSupport = myfactor.newInstance(CPSupport.class);

        menu.goToMenu(CPMenuName.SUPPORT);

        cpSupport.waitLoadingSupportTicketContent();
        cpSupport.createSupportTicket();

        System.out.println("***Test Create Support Ticket succeed!!********");
    }

}
