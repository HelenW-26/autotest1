package newcrm.pages.vtibpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.ibpages.DashBoardPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class VTIBDashBoardPage extends DashBoardPage {

	public VTIBDashBoardPage(WebDriver driver) {
		super(driver);
	}

    @Override
    protected WebElement getAccountDiv() {
        return this.findClickableElementByXpath("//span[@class='account_name']/parent::div");
    }

    @Override
    protected WebElement getCurrentlySelectedAccountEle() {
        return assertElementExists(By.xpath("//span[@id='showMessage']"), "Currently Selected IB Account");
    }

    //获取IB返佣账号的Referral Link
    @Override
    public String getIBReferralLink() {

        WebElement e = checkElementExists(By.xpath("//span[@class='referral_link']"), "IB Referral Link");

        if (e == null){
            GlobalMethods.printDebugInfo("[VT] IB Referral Link not found - Refresh Page");
            driver.navigate().refresh();
            e = assertElementExists(By.xpath("//span[@class='referral_link']"), "IB Referral Link");
        }

        String ibUrl = e.getAttribute("innerText");
        driver.navigate().to(ibUrl);
        String ibLiveRefLink = getCurrentURL();

        int index = ibLiveRefLink.indexOf("?affid=");
            // Extract everything from "?affid=" to the end
            String queryPart = ibLiveRefLink.substring(index + 7);
            GlobalMethods.printDebugInfo("Affliate ID: " + queryPart);

            return queryPart;
    }


}
