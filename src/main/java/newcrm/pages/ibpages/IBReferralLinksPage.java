package newcrm.pages.ibpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import utils.LogUtils;

import javax.validation.constraints.AssertTrue;
import java.util.List;

public class IBReferralLinksPage extends Page {

    public IBReferralLinksPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getCampaignLanguageDropdownEle() {
        return assertElementExists(By.xpath("//div[@class='select_option']"), "Campaign Language Dropdown");
    }

    protected List<WebElement> getCampaignLanguageDropdownListEle() {
        return assertElementsExists(By.xpath("//div[not(contains(@style, 'display: none'))]/div/div[@class='el-select-dropdown__wrap el-scrollbar__wrap']/ul/li"), "Campaign Language Dropdown List Options");
    }

    protected String ibRefLinkEnglishDomain(){
        return "https://www.vantagemarkets.com/";
    }

    protected String ibRefLinkTradChineseDomain(){
        return "https://www.vantagemarkets.com/hant/";
    }

    protected String ibRefLinkThaiDomain(){
        return "https://www.vantagemarkets.com/th/";
    }

    protected String ibRefLinkMYMalayDomain(){
        return "https://www.vantagemarkets.io/ms/";
    }

    protected String ibRefLinkMYEnglishDomain(){
        return "https://www.vantagemarkets.io/en/";
    }

    protected String ibRefLinkMYChineseDomain(){
        return "https://www.vantagemarkets.io/hans/";
    }

    protected String ibRefLinkVietDomain(){
        return "https://www.vantagemarkets.com/vi/";
    }

    protected String ibRefLinkIndoDomain(){
        return "https://www.vantagemarketsea.com/id/";
    }

    protected String ibRefLinkIndiaDomain(){
        return "https://www.vantagemarkets.com/en/";
    }

    protected String ibRefLinkKazakhDomain(){
        return "https://www.vantagemarkets.io/kk/";
    }

    protected String ibRefLinkKoreaDomain(){
        return "https://www.vantagemarkets.com/ko/";
    }

    protected String ibRefLinkMongolDomain(){
        return "https://www.vantagemarkets.com/mn/";
    }

    protected String ibRefLinkRussiaDomain(){
        return "https://www.vantage-mkts.com/ru/";
    }

    protected String ibDemoAccountDomain(){
        return "https://www.vantagemarkets.com/open-demo-account-crm/";
    }

    protected String ibRefLiveAccountLinkComponent(){
        return "open-live-account/?affid=";
    }

    protected String ibRefDemoAccountLinkComponent(){
        return "open-demo-account/?affid=";
    }

    protected String ibRefHomeLinkComponent(){
        return "?affid=";
    }

    protected String ibRefDownloadAppLink(){
        return "https://hytech-au1-h5third.app-alpha.com/h5/thirdparty/support/register?agentAccount=";
    }

    protected WebElement getIBRefPageLiveAccountLinkEle(){
        return assertElementExists(By.xpath("//div[@class='referralLinks_content']/div[@class='link_box']/div[1] //p[@class='link_url']"), "Referral Link - Live Account");
    }

    protected WebElement getIBRefPageDemoAccountLinkEle(){
        return assertElementExists(By.xpath("//div[@class='referralLinks_content']/div[@class='link_box']/div[2] //p[@class='link_url']"), "Referral Link - Demo Account");
    }

    protected WebElement getIBRefPageHomePageLinkEle(){
        return assertElementExists(By.xpath("//div[@class='referralLinks_content']/div[@class='link_box']/div[3] //p[@class='link_url']"), "Referral Link - Home Page");
    }

    protected WebElement getIBRefPageDownloadAppLinkEle(){
        return assertElementExists(By.xpath("//div[@class='referralLinks_content']/div[@class='link_box']/div[4] //p[@class='link_url']"), "Referral Link - Home Page");
    }

    protected WebElement getShortLinkToggleEle(){
        return assertElementExists(By.xpath("//span[@class='el-switch__core']"), "Referral Link - Short Link Toggle");
    }

    protected WebElement getShortLinkToggleStatusEle(){
        return assertElementExists(By.xpath("//span[@class='el-switch__core']/parent::div"), "Referral Link - Short Link Toggle Status");
    }

    public void selectRandomCampaignLanguage(){
        triggerElementClickEvent_withoutMoveElement(getCampaignLanguageDropdownEle());
        selectRandomValueFromDropDownList(getCampaignLanguageDropdownListEle());
    }

    public void verifyReferralLinks(String affId, String testEnv){
        String originalHandle = driver.getWindowHandle();

        triggerElementClickEvent_withoutMoveElement(getCampaignLanguageDropdownEle());
        fastwait.until(ExpectedConditions.visibilityOfAllElements(getCampaignLanguageDropdownListEle()));
        int totalLanguages = getCampaignLanguageDropdownListEle().size() + 1;
        triggerElementClickEvent_withoutMoveElement(getCampaignLanguageDropdownEle());

        String expectedDownloadAppLink = "";
        if(testEnv.equalsIgnoreCase("alpha")){
            //Verify Long Links - Download App
            expectedDownloadAppLink = ibRefDownloadAppLink() + affId;
            if(!getIBRefPageDownloadAppLinkEle().getText().equals(expectedDownloadAppLink)){
                Assert.fail("Incorrect Download App link - Expected: " + expectedDownloadAppLink + " ; Actual: " + getIBRefPageDownloadAppLinkEle().getText());
            }
            GlobalMethods.printDebugInfo("Verified Download App link");
        }

        for (int i = 1; i < totalLanguages; i++) {
            //Verify Long Links - Live, Demo, Home are correct
            if(getShortLinkToggleStatusEle().getAttribute("class").contains("is-checked")){
                triggerElementClickEvent_withoutMoveElement(getShortLinkToggleEle());
                waitLoading();
            }
            triggerElementClickEvent_withoutMoveElement(getCampaignLanguageDropdownEle());
            String currentLanguage = getCampaignLanguageDropdownListEle().get(i-1).getAttribute("data-testid");
            triggerElementClickEvent_withoutMoveElement(getCampaignLanguageDropdownListEle().get(i-1));

            //Verify Long Link - Live Account
            String expectedLiveAccLink = retrieveCurrentLanguageDomain(currentLanguage) + ibRefLiveAccountLinkComponent() + affId;
            if(!getIBRefPageLiveAccountLinkEle().getText().equals(expectedLiveAccLink)){
                Assert.fail("Incorrect Live Account Link - Expected: " + expectedLiveAccLink + " ; Actual: " + getIBRefPageLiveAccountLinkEle().getText());
            }

            //Verify Long Link - Demo Account
            String expectedDemoAccLink = retrieveCurrentLanguageDomain(currentLanguage) + ibRefDemoAccountLinkComponent() + affId;
            if(!getIBRefPageDemoAccountLinkEle().getText().equals(expectedDemoAccLink)){
                Assert.fail("Incorrect Demo Account Link - Expected: " + expectedDemoAccLink + " ; Actual: " + getIBRefPageDemoAccountLinkEle().getText());
            }

            //Verify Long Link - Home Page
            String expectedHomePageLink = retrieveCurrentLanguageDomain(currentLanguage) + ibRefHomeLinkComponent() + affId;
            if(!getIBRefPageHomePageLinkEle().getText().equals(expectedHomePageLink)){
                Assert.fail("Incorrect Demo Account Link for " + currentLanguage + "- Expected: " + expectedHomePageLink + " ; Actual: " + getIBRefPageHomePageLinkEle().getText());
            }

            //Verify if Short Link radio button is turned ON
            if(!getShortLinkToggleStatusEle().getAttribute("class").contains("is-checked")){
                triggerElementClickEvent_withoutMoveElement(getShortLinkToggleEle());
                waitLoading();
            }

            String liveAccShortLink = getIBRefPageLiveAccountLinkEle().getText();
            String demoAccShortLink = getIBRefPageDemoAccountLinkEle().getText();
            String homePageShortLink = getIBRefPageHomePageLinkEle().getText();

            //Verify Short Link - Live Account
            driver.switchTo().newWindow(WindowType.TAB);
            driver.get(liveAccShortLink);
            if(!getCurrentURL().equals(expectedLiveAccLink)){
                driver.get(liveAccShortLink);
                LogUtils.info("Short Live Account Link for "+currentLanguage+" redirected to wrong site. Attempting second time.");
                if(!getCurrentURL().equals(expectedLiveAccLink)){
                    Assert.fail("Short Live Account Link for "+currentLanguage+" redirected to wrong site - Expected: " + expectedLiveAccLink + " ; Actual (Short Link): " + liveAccShortLink + " ; Actual (re-directed): " + getCurrentURL());
                }else{
                    LogUtils.info("Able to be redirected to correct site on second attempt");
                }
            }
            driver.close();
            driver.switchTo().window(originalHandle);

            //Verify Short Link - Demo Account
            driver.switchTo().newWindow(WindowType.TAB);
            driver.get(demoAccShortLink);
            if(!getCurrentURL().equals(expectedDemoAccLink)){
                Assert.fail("Short Demo Account Link for "+currentLanguage+" redirected to wrong site - Expected: " + expectedDemoAccLink + " ; Actual (Short Link): " + demoAccShortLink + " ; Actual (re-directed): " + getCurrentURL());
            }
            driver.close();
            driver.switchTo().window(originalHandle);

            //Verify Short Link - Home Page
            driver.switchTo().newWindow(WindowType.TAB);
            driver.get(homePageShortLink);
            expectedHomePageLink = retrieveCurrentLanguageDomain(currentLanguage) + ibRefHomeLinkComponent() + affId.replace("=","%3D");
            if(!getCurrentURL().equals(expectedHomePageLink)){
                Assert.fail("Short Home Page Link for "+currentLanguage+" redirected to wrong site - Expected: " + expectedHomePageLink + " ; Actual (Short Link): " + homePageShortLink + " ; Actual (re-directed): " + getCurrentURL());
            }
            driver.close();
            driver.switchTo().window(originalHandle);

            LogUtils.info(currentLanguage + " - verified Live Account, Demo Account, Home Page links");
        }

        if(testEnv.equalsIgnoreCase("alpha")){
            //Verify Short Link - Download App
        String downloadAppShortLink = getIBRefPageDownloadAppLinkEle().getText();
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get(downloadAppShortLink);
        expectedDownloadAppLink = ibRefDownloadAppLink() + affId.replace("=","%3D");
        if(!getCurrentURL().contains(expectedDownloadAppLink)){
            Assert.fail("Short Download App Link redirected to wrong site - Expected: " + expectedDownloadAppLink + " ; Actual: " + getCurrentURL());
        }
        driver.close();
        driver.switchTo().window(originalHandle);
        }



    }

    public void navigateToDownloadAppURL(){
        String downloadAppShortLink = getIBRefPageDownloadAppLinkEle().getText();
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get(downloadAppShortLink);
    }

    public void navigateToDemoAccountURL(String affID){
        String demoAccountURL = ibDemoAccountDomain() +"?affid="+ affID;
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get(demoAccountURL);
    }

    public String retrieveCurrentLanguageDomain(String language){
        String domain = null;

        switch (language){
            case "English":
                domain = ibRefLinkEnglishDomain();
                break;
            case "Traditional Chinese":
                domain = ibRefLinkTradChineseDomain();
                break;
            case "Thai":
                domain = ibRefLinkThaiDomain();
                break;
            case "Malay":
                domain = ibRefLinkMYMalayDomain();
                break;
            case "MY - English":
                domain = ibRefLinkMYEnglishDomain();
                break;
            case "MY - Chinese":
                domain = ibRefLinkMYChineseDomain();
                break;
            case "Vietnamese":
                domain = ibRefLinkVietDomain();
                break;
            case "Indonesian":
                domain = ibRefLinkIndoDomain();
                break;
            case "India":
                domain = ibRefLinkIndiaDomain();
                break;
            case "Kazakh":
                domain = ibRefLinkKazakhDomain();
                break;
            case "Korean":
                domain = ibRefLinkKoreaDomain();
                break;
            case "Mongolian":
                domain = ibRefLinkMongolDomain();
                break;
            case "Russian":
                domain = ibRefLinkRussiaDomain();
                break;
        }
        return domain;
    }

}
