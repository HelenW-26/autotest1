package newcrm.pages.adminpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PTManagementPage extends Page {

    public PTManagementPage(WebDriver driver)
    {
        super(driver);
    }

    public void gotoPTRiskAudit()
    {
        waitLoading();
        WebElement riskAudit = driver.findElement(By.xpath("//div[@id='rc-tabs-0-tab-tab-risk-audit']"));
        riskAudit.click();
        waitLoading();
    }

    public void gotoPTPayout()
    {
        waitLoading();
        WebElement payout = driver.findElement(By.xpath("//div[@id='rc-tabs-0-tab-tab-payout-refund']"));
        payout.click();
        waitLoading();
    }

    public void approveRiskaudit(String sessionNo)
    {
        //click client search drop down
        waitLoading();
        WebElement clientSearch = driver.findElement(By.xpath("(//div[@id='rc-tabs-0-panel-tab-risk-audit']//div[@id='clientSearch'])[1]//div[@class='ant-select ant-select-outlined ant-select-in-form-item dropdown-select css-1k979oh ant-select-single ant-select-show-arrow']"));
        clientSearch.click();

        //click session number in drop down list
        WebElement sessionNoDiv = driver.findElement(By.xpath("//div[@title='Session No']/div"));
        sessionNoDiv.click();

        //input session number
        waitLoading();
        WebElement searchItem = driver.findElement(By.xpath("(//input[@id='userQuery'])[2]"));
        searchItem.sendKeys(sessionNo);
        waitLoading();

        WebElement searchBtn = driver.findElement(By.xpath("(//div[@class='client-search-action']/button[./span[contains(text(),'Search')]])[2]"));
        searchBtn.click();
        waitLoading();

        //click approve risk audit
        WebElement approveBtn = driver.findElement(By.xpath("//div[@class='risk-audit-table-actions']/button[1]"));
        approveBtn.click();

        //submit approval
        WebElement submitBtn = findClickableElementByXpath("//div[@class='pt-audit-actions']/button[2]/span[contains(text(),'Submit')]");
        submitBtn.click();

        GlobalMethods.printDebugInfo("risk audit approved");
    }

    public void approveRiskProfitSplit(String sessionNo)
    {
        WebElement profitSplit = findVisibleElemntByXpath("//div[@data-node-key='tab-profit-split']/div");
        profitSplit.click();

        WebElement clientSearch = findClickableElementByXpath("//form[@id='pt-search-bar']/div[@class='ant-form-item pt-searchbar-item css-18iikkb'][7]//div[@id='pt-search-bar_clientSearch']//div[@class='ant-select ant-select-in-form-item dropdown-select css-18iikkb ant-select-single ant-select-show-arrow']");
        clientSearch.click();

        WebElement sessionNoDiv = driver.findElement(By.xpath("//div[@title='Session No']/div"));
        sessionNoDiv.click();

        waitLoading();
        WebElement searchItem = driver.findElement(By.xpath("(//input[@id='pt-search-bar_userQuery'])[3]"));
        searchItem.sendKeys(sessionNo);

        WebElement searchBtn = driver.findElement(By.xpath("(//div[@class='client-search-action']/button[1]//span[contains(text(),'Search')])[3]"));
        searchBtn.click();
        waitLoading();

        //click approve refund risk audit
        WebElement approveBtn = driver.findElement(By.xpath("//div[@id='rc-tabs-1-panel-tab-profit-split']//tbody//tr[2]//button[1]"));
        approveBtn.click();

        //submit approval
        WebElement submitBtn = findClickableElementByXpath("//div[@class='pt-audit-actions']/button[2]/span[contains(text(),'Submit')]");
        submitBtn.click();

        //click approve profitSplit  risk audit
        WebElement approveProfitBtn = driver.findElement(By.xpath("//div[@id='rc-tabs-1-panel-tab-profit-split']//tbody//tr[3]//button[1]"));
        approveProfitBtn.click();

        //submit approval
        WebElement submitPBtn = findClickableElementByXpath("//div[@class='pt-audit-actions']/button[2]/span[contains(text(),'Submit')]");
        submitPBtn.click();

        GlobalMethods.printDebugInfo("risk audit for profit split approved");
    }

    public void rejectPayoutandRefund(String sessionNo)
    {
        WebElement searchBtn = driver.findElement(By.xpath("(//div[@class='client-search-action']/button[1]//span[contains(text(),'Search')])[2]"));
        js.executeScript("arguments[0].click()",searchBtn);
        waitLoading();

        WebElement clientSearch = findClickableElementByXpath("//form[@id='pt-search-bar']/div[@class='ant-form-item pt-searchbar-item css-18iikkb'][7]//div[@id='pt-search-bar_clientSearch']//div[@class='ant-select ant-select-in-form-item dropdown-select css-18iikkb ant-select-single ant-select-show-arrow']");
        clientSearch.click();
        waitLoading();

        WebElement sessionNoDiv = driver.findElement(By.xpath("(//div[@title='Session No']/div)[2]"));
        sessionNoDiv.click();

        WebElement searchItem = driver.findElement(By.xpath("(//input[@id='pt-search-bar_userQuery'])[2]"));
        searchItem.sendKeys(sessionNo);
        //js.executeScript("arguments[0].setAttribute('value', '" +sessionNo +"')", searchItem);

        //WebElement searchBtn = driver.findElement(By.xpath("(//div[@class='client-search-action']/button[1]//span[contains(text(),'Search')])[2]"));
        //searchBtn.click();
        js.executeScript("arguments[0].click()",searchBtn);
        waitLoading();

        //click reject refund risk audit
        WebElement approveBtn = driver.findElement(By.xpath("//div[@id='rc-tabs-0-panel-tab-payout-refund']//tbody//tr[2]//button[2]"));
        approveBtn.click();

        //submit approval
        WebElement submitBtn = findClickableElementByXpath("//div[@class='pt-audit-actions']/button[2]/span[contains(text(),'Yes')]");
        submitBtn.click();

        //click approve profitSplit  risk audit
        WebElement approveProfitBtn = driver.findElement(By.xpath("//div[@id='rc-tabs-0-panel-tab-payout-refund']//tbody//tr[3]//button[2]"));
        approveProfitBtn.click();

        //submit approval
        WebElement submitPBtn = findClickableElementByXpath("//div[@class='pt-audit-actions']/button[2]/span[contains(text(),'Yes')]");
        submitPBtn.click();

        GlobalMethods.printDebugInfo("risk audit for profit split rejected");
    }

}
