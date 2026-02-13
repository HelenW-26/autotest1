package newcrm.business.mobusiness;

import newcrm.business.businessbase.CPMenu;
import newcrm.business.businessbase.copyTrading.CPCopyTrading;
import newcrm.pages.moclientpages.copytrading.MOCopierPage;
import newcrm.pages.moclientpages.copytrading.MODiscoverPage;
import newcrm.pages.moclientpages.copytrading.MOSignalProviderPage;
import org.openqa.selenium.WebDriver;

public class MOCPCopyTrading extends CPCopyTrading {
    protected MODiscoverPage discoveryPage;
    protected MOCopierPage copierPage;
    protected MOSignalProviderPage signalProviderPage;

    public MOCPCopyTrading(WebDriver driver) {
        super(driver);
        this.copierPage = new MOCopierPage(driver);
        this.discoveryPage = new MODiscoverPage(driver);
        this.signalProviderPage = new MOSignalProviderPage(driver);
    }
    public boolean submitCopyRequest(CPMenu menu, String copier, boolean needCopyDetail, boolean isCopyOpenTrade, String copyMode) {
        return discoveryPage.submitCopyRequest(menu, copier, needCopyDetail, true, copyMode);
    }
    public void getCopierAccTitleInfo() {
        copierPage.getCopierAccTitleInfo();
    }

    public boolean checkCopyAccPositionInfo(String copyAcc) {
        // position info exists on the Copier page
        boolean hasPositionInfo = copierPage.checkStrategyAccPositionInfo(copyAcc);

        // account name exists on the Discovery page
        String expectedAccName = discoveryPage.getStrategyAccName().strip();   // Trim whitespace

        // Split by any whitespace and re-join with an underscore
        String expectedAcc = String.join("_", expectedAccName.trim().split("\\s+"));

        boolean providerAccountMatches = copierPage
                .getStrategyProvAccountList()   // List<?> of provider accounts
                .stream()
                .anyMatch(acc -> String.join("_", acc.getAccnum().trim().split("\\s+"))
                        .equalsIgnoreCase(expectedAcc));

        // Return true only if both conditions are met
        return hasPositionInfo && providerAccountMatches;
    }
}
