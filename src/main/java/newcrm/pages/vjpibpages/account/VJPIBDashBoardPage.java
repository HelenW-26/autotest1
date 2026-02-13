package newcrm.pages.vjpibpages.account;

import newcrm.pages.ibpages.DashBoardPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class VJPIBDashBoardPage extends DashBoardPage {

	public VJPIBDashBoardPage(WebDriver driver) {
		super(driver);
	}

    @Override
    public List<WebElement> getRecentlyOpenedTradingAccountsDiv() {
        return assertElementsExists(By.xpath("//tbody/tr/td[2]/div"), "Recently Opened Accounts Div");
    }

    //查询返佣账号下所有的交易账号
    @Override
    public List<String> getAllRecentlyOpenedTradingAccounts(){
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
