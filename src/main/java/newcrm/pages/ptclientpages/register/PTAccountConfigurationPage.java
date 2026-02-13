package newcrm.pages.ptclientpages.register;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.register.AccountConfigurationPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;

public class PTAccountConfigurationPage extends AccountConfigurationPage {

    public PTAccountConfigurationPage(WebDriver driver)
    {
        super(driver);
    }

    @Override
    public String setAccountType(GlobalProperties.ACCOUNTTYPE accountType) {
        driver.navigate().refresh();
        waitLoading();
        driver.findElement(By.xpath("//div[@data-testid='type']")).click();

        HashMap<String,String> datatestid = new HashMap<>();
        datatestid.put("Basic","select_0");
        datatestid.put("Plus","select_1");

        driver.findElement(By.xpath("(//ul[@class='el-scrollbar__view el-select-dropdown__list'])//li[@data-testid='"+datatestid.get(accountType.toString())+"']")).click();
        GlobalMethods.printDebugInfo("set account type to: " + accountType.toString());
        return accountType.toString();
    }

    @Override
    public String setCurrency(GlobalProperties.CURRENCY currency) {
        driver.findElement(By.xpath("//div[@data-testid='currency']")).click();
        driver.findElement(By.xpath("(//ul[@class='el-scrollbar__view el-select-dropdown__list'])//li[@data-testid='select_"+currency.toString()+"']")).click();
        GlobalMethods.printDebugInfo("set currency to: " + currency.toString());
        return currency.toString();
    }

    @Override
    public void tickBox() {
        WebElement tick = this.findClickableElementByXpath("//label[@data-testid='checkStatus']/span/span");
        this.moveElementToVisible(tick);
        tick.click();
    }

    @Override
    public void next() {
        this.waitLoading();
        WebElement e = this.findClickableElementByXpath("//button[@data-testid='confirm']");
        this.moveElementToVisible(e);
        e.click();
        this.waitLoading();
    }
    @Override
    public String getPTRefistrationFee() {
        WebElement e = this.findClickableElementByXpath("//ul[@class='fee']/li//div[@class='value']/span[2]");
        String amount = e.getText();
        this.waitLoading();
        return amount;
    }


}
