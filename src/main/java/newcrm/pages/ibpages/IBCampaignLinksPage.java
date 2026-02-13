package newcrm.pages.ibpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.testng.Assert;
import utils.LogUtils;

import java.util.List;

public class IBCampaignLinksPage extends Page {

    public IBCampaignLinksPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getNewCampaignButtonEle() {
        return assertElementExists(By.xpath("//button[contains(@class,'add-btn el-button--primary button el-button')]"), "Add New Campaign Button");
    }

    protected WebElement getCampaignTitleInputEle() {
        return assertElementExists(By.xpath("//div[@class='campaign-dialog-content'] //div[contains(@class, 'suffix-input el-input el-input--suffix')]/input[@class='el-input__inner']"), "Campaign Title Input Box");
    }

    protected WebElement getCampaignLanguageDropdownEle() {
        return assertElementExists(By.xpath("//div[@class='campaign-dialog-content'] //div[@class='el-input el-input--suffix']/input[@class='el-input__inner']"), "Campaign Language Dropdown");
    }

    protected List<WebElement> getCampaignLanguageDropdownListEle() {
        return assertElementsExists(By.xpath("//div[contains(@class, 'select-dropdown') and not(contains(@style, 'display: none'))]/div/div/ul/li/span"), "Campaign Language Dropdown List");
    }

    protected WebElement getSaveButtonEle() {
        return assertElementExists(By.xpath("//div[contains(@class,'campaign-dialog')] //button[contains(@class,'ht-dialog__primary')]"), "Save/OK Button");
    }

    protected WebElement getSearchCampaignTitleInputEle() {
        return assertElementExists(By.xpath("//input[@placeholder='search title' or @placeholder='Search title']"), "Search Campaign Title Input Box");
    }

    protected WebElement getCampaignLinkEle() {
        return assertElementExists(By.xpath("(//div[@class='row-links-text'])[1] //span"), "First Campaign Link");
    }

    protected WebElement getCreatedCampaignTitleEle() {
        return assertElementExists(By.xpath("(//div[contains(@class,'row-campaign-left-main')])[1]"), "First (Created) Campaign Title");
    }

    protected WebElement getShortLinkToggleEle() {
        return assertElementExists(By.xpath("//div[contains(@class,'el-switch')]/span[@class='el-switch__core']"), "Short Link Toggle");
    }


    public String newCampaignLink(){
        triggerElementClickEvent_withoutMoveElement(getNewCampaignButtonEle());
        String campaignTitle = ("campaignTitleTest"+GlobalMethods.getRandomString(8));
        setInputValue(getCampaignTitleInputEle(),campaignTitle);

        return campaignTitle;
    }

    public void selectCampaignLanguage(){
        triggerElementClickEvent_withoutMoveElement(getCampaignLanguageDropdownEle());
        selectRandomDropDownOption_ElementClickEvent(getCampaignLanguageDropdownListEle());
    }

    public void saveCampaign(){
        triggerElementClickEvent_withoutMoveElement(getSaveButtonEle());
        waitLoading();
    }

    public void searchByCampaignTitle(String campaignTitle){
        setInputValue(getSearchCampaignTitleInputEle(),campaignTitle);
        waitLoading();
    }

    public void verifyCampaignTitle(String campaignTitle){
        if(!getCreatedCampaignTitleEle().getText().equals(campaignTitle)){
            Assert.fail("Created campaign title is not correct! Expected: "+campaignTitle+" . Actual: "+getCreatedCampaignTitleEle().getText());
        }
    }

    public void verifyCampaignShortLink(String campaignID){
        String originalHandle = driver.getWindowHandle();

        String longCampaignLink = getCampaignLinkEle().getText().trim();
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get(longCampaignLink);
        String longCampaignLink_RedirectedURL = getCurrentURL();

        //Try again if re-directed to url without the campaignID itself
        if(!longCampaignLink_RedirectedURL.contains(campaignID)){
            LogUtils.info("Redirected URL: "+longCampaignLink_RedirectedURL+" doesn't contain campaign ID: " + campaignID + "re-attempting for the second time:");
            driver.get(longCampaignLink);
            longCampaignLink_RedirectedURL = getCurrentURL();

            //If still the directed URL still doesn't contain the campaignID, fail the test case
            if(!longCampaignLink_RedirectedURL.contains(campaignID)){
                Assert.fail("Second Attempt still does not contain the Campaign ID, Register Campaign Link displayed on IBP: " + longCampaignLink + ";Actual directed Register URL: " + longCampaignLink_RedirectedURL);
            }
        }

        driver.close();
        driver.switchTo().window(originalHandle);

        //TODO - check if longCampaignLink_RedirectedURL is valid

        triggerElementClickEvent_withoutMoveElement(getShortLinkToggleEle());
        String shortCampaignLink = getCampaignLinkEle().getText().trim();
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get(shortCampaignLink);

        if(!getCurrentURL().equals(longCampaignLink_RedirectedURL)){
            driver.get(shortCampaignLink);
            LogUtils.info("Short Campaign Link redirected to wrong site. Attempting second time.");
            if(!getCurrentURL().equals(longCampaignLink_RedirectedURL)){
                Assert.fail("Short Campaign Link redirected to wrong site - Expected: " + longCampaignLink_RedirectedURL + " ; Actual (Short Link): " + shortCampaignLink + " ; Actual (re-directed): " + getCurrentURL());
            }else{
                LogUtils.info("Short Campaign Link: Able to be redirected to correct site on second attempt: " + getCurrentURL());
            }
        }else{
            LogUtils.info("Short Campaign Link: Able to be redirected to correct site: " + getCurrentURL());
        }
        driver.close();
        driver.switchTo().window(originalHandle);

    }

    public String retrieveCampaignSourceID(){

        String campaignLink = getCampaignLinkEle().getText().trim();

        int index = campaignLink.indexOf("?cs=");
        String queryPart = campaignLink.substring(index + 4);
        LogUtils.info("CS ID: " + queryPart);

        return queryPart;
    }



}
