package newcrm.pages.clientpages.wallet;

import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import utils.LogUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author wesley
 * @Description
 **/
public class WalletEarnPage extends Page {
    public WalletEarnPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement VWalletEarnTitle(){
        return assertElementExists(By.xpath("//span[@class='tab-title-wrapper' and text()='Earn']"),"VWallet Earn Title");
    }

    protected WebElement VWalletEarnSpan(){
        return assertElementExists(By.xpath("//div[@class='balance_btn']/button/span[normalize-space()='Earn']"),"VWallet Earn Page Button");
    }
    protected WebElement earnPageHome(){
        return assertElementExists(By.xpath("//div[@class='earn_home']"),"earn Page Home");
    }

    protected WebElement earnMyHoldings(){
        return assertElementExists(By.xpath("//span[text()='My Holdings']"),"earn My Holdings");
    }
    protected WebElement earnTotalValue(){
        return assertElementExists(By.xpath("//div[@class='earn_info_item']//div[@class='amount_info']//span[@class='amount']"),"earn total value");
    }
    protected WebElement earnYesterdayYield(){
        return assertElementExists(By.xpath("//div[@class='earn_info_item']//div[@class='earn_item_right']//span[text()='Yesterday’s Yield']/../div"),"earn Yesterday’s Yield");
    }
    protected WebElement earnTotalYield(){
        return assertElementExists(By.xpath("//div[@class='earn_info_item']//div[@class='earn_item_right']//span[text()='Total Yield']/../div"),"earn Total Yield");
    }
    protected List<WebElement> earnFlexibleSavingsTables(){
        return assertElementsExists(By.xpath("//thead[@class='has-gutter']/tr/th[@colspan=1]/div"),"Flexible Saving table");
    }
    protected List<WebElement> listEarnFAQ(){
        return assertElementsExists(By.xpath("//div[@class='el-collapse ht-collapse ht-collapse--clean-with-arrow']//div[@role='button']"),"earn FAQ");
    }
    /**
     * 验证从VWallet页面进入Earn页面
     */
    public void checkVWalletToEarnPage(){
        clickElement(VWalletEarnTitle());
        waitLoading();
        clickElement(VWalletEarnSpan());
        waitLoading();
        earnPageHome();
    }

    /**
     * 验证直接从首页tab 进入Earn页面
     */
    public void checkGoToEarnPage(){
        WebElement earnTab = assertElementExists(By.xpath("//li[@data-testid='Earn' or @data-testid='wallet.earn.home.earn']"),"Earn Tab");
        clickElement(earnTab);
        waitLoading();
        earnPageHome();
    }

    public void checkEarnMyHoldings(){
        clickElement(earnMyHoldings());
        waitLoading();
        assertElementExists(By.xpath("//div[@class='wallet_home']"),"wallet home");
    }

    public void checkEarnHomePage(){
        String totalValue = earnTotalValue().getText().trim();
        String yesterdayYield = earnYesterdayYield().getText().trim();
        String totalYield = earnTotalYield().getText().trim();
        LogUtils.info("earn total value: " + totalValue);
        LogUtils.info("earn yesterday yield: " + yesterdayYield);
        LogUtils.info("earn total yield: " + totalYield);
        Assert.assertFalse(totalValue.isBlank());
        Assert.assertFalse(yesterdayYield.isBlank());
        Assert.assertFalse(totalYield.isBlank());
        Assert.assertTrue(getValue(totalValue) > 0);
        Assert.assertTrue(getValue(yesterdayYield) > 0);
        Assert.assertTrue(getValue(totalYield) > 0);

        Set<String> tableHeaders = earnFlexibleSavingsTables().stream().map(WebElement::getText).map(String::trim).collect(Collectors.toSet());
        Set<String> defaultHeaders = Set.of("Coin","Duration","Equivalent","Action");
        Assert.assertTrue(tableHeaders.containsAll(defaultHeaders),"Earn Flexible Saving table headers");

        List<WebElement> earnFAQ = listEarnFAQ();
        for (WebElement element : earnFAQ) {
            WebElement expendEarnFAQ = element.findElement(By.xpath("./i"));
            triggerClickEvent(expendEarnFAQ);
            List<WebElement> activeEle = assertElementsExists(By.xpath("//div[@class='el-collapse-item main-faq__item is-active']"), "FAQ element is active");
            Assert.assertEquals(activeEle.size(), 1);
        }
    }

    /**
     * 获取金额，去除货币单位
     * @param totalValue 带货币单位的金额，超过1000 的可能也会有,
     * @return 具体金额
     */
    private Double getValue(String totalValue){
        if (totalValue == null || totalValue.trim().isEmpty()) {
            return 0.00;
        }
        String numericValue = totalValue.trim().replaceAll("[^\\d.,]", "").replace(",", "");
        try {
            return Double.parseDouble(numericValue);
        } catch (NumberFormatException e) {
            LogUtils.error("Failed to parse numeric value: " + numericValue, e);
            return 0.00;
        }
    }


}
