package newcrm.pages.vtadminpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.adminpages.AdminAccountAuditPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


import java.util.ArrayList;
import java.util.List;

public class VTAdminAccountAuditPage extends AdminAccountAuditPage {

    protected List<String> serversId;

    public VTAdminAccountAuditPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public String selectAccountTypeTrading(){
        List<WebElement> accountTypeDropdownOptions = new ArrayList<>(getSelectAccountTypeDropdownListEle());

        accountTypeDropdownOptions.removeIf(option -> {
            String text = option.getAttribute("innerText");
            return text != null && (
                    text.toLowerCase().contains("hedge") ||
                            text.toLowerCase().contains("mts") ||
                            text.toLowerCase().contains("institution type") ||
                            text.toLowerCase().contains("cent")
            );
        });
        triggerElementClickEvent(getAccountTypeDropdownEle());
        String accountType = selectRandomDropDownOption_ElementClickEvent(accountTypeDropdownOptions);
        GlobalMethods.printDebugInfo("Account Type: "+accountType);

        return accountType;
    }

}
