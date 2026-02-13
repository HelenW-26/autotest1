package newcrm.pages.staribpages;

import newcrm.pages.ibpages.DashBoardPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class STARIBDashBoardPage extends DashBoardPage {

	public STARIBDashBoardPage(WebDriver driver) {
		super(driver);
	}

    @Override
    //STAR多一个步骤，需要先按Recently Opened Accounts查询返佣账号下所有的交易账号
    public List<String> getAllRecentlyOpenedTradingAccounts(){
        WebElement r = assertElementExists(By.xpath("//div[text()='Recently Opened Accounts']"), "Click Recently Opened Accounts Tab (STAR)");
//        moveElementToVisible(r);
        triggerElementClickEvent_withoutMoveElement(r);

        ArrayList<String> accs = new ArrayList<>();
        moveElementToVisible(this.getRecentlyOpenedTradingAccountsDiv().get(0));
        List<WebElement> div = this.getRecentlyOpenedTradingAccountsDiv();
        for(WebElement e: div) {
            String acc = e.getAttribute("innerText");
            if(acc!=null && acc.trim().length()>0) {
                accs.add(acc);
            }
        }
        return accs;
    }

}
