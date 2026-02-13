package newcrm.pages.umclientpages.copytrading;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.copyTrading.SignalProviderPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class UMSignalProviderPage extends SignalProviderPage {
    public UMSignalProviderPage(WebDriver driver) {
        super(driver);
    }
    protected List<WebElement> gettableBodyRows_strategyEle(){
        return driver.findElements(By.xpath("//div[@id='pane-strategies']//div[@class='list_pc_item']"));
    }
    protected WebElement moreBtn(String strategyAccount){
        return assertElementExists(By.xpath("//div[contains(@class,'more-btn-wrapper')]"),"More button");
    }
    protected List<WebElement> gettableBodyRows_positionEle(){
        return driver.findElements(By.xpath("//div[@id='pane-position']//div[@class='list-pc-wrap']"));
    }

    public void clickMoreBtn(String strategyAccount){
        waitLoadingInCopyTrading();
        moreBtn(strategyAccount).click();
        GlobalMethods.printDebugInfo("Click more button at strategy list");
    }

    public int getStrategyCount() {
        waitLoadingInCopyTrading();
        List<WebElement> rows = gettableBodyRows_strategyEle();
        GlobalMethods.printDebugInfo("strategy rows size:" + rows.size());
        return rows.size();
    }
    public List<String> getStrategyNameList() {
        List<WebElement> rows = gettableBodyRows_strategyEle();
        List<String> texts = new ArrayList<>();

        if (rows == null || rows.isEmpty()) {
            GlobalMethods.printDebugInfo("No strategy in signal provider page");
            return texts;
        }

        for (WebElement row : rows) {
            if (row == null) continue;
            String t = row.getText();
            if (t != null) {
                t = t.trim();
                if (!t.isEmpty()) {
                    texts.add(t);
                }
            }
        }

        return texts;
    }
}
