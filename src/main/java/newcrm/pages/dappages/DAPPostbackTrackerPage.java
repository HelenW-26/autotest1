package newcrm.pages.dappages;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import utils.LogUtils;

import java.util.List;

public class DAPPostbackTrackerPage extends Page {

    public DAPPostbackTrackerPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getCurrentlySelectedEventTypeEle() {
        return assertElementExists(By.xpath("//div[contains(@class,'event-list__item--active')]/div[@class='event-list__item-text']"), "Currently Selected Event Type");
    }

    protected WebElement getRegistrationPostbackEle() {
        return assertElementExists(By.xpath("(//div[@class='event-list__item-text'])[1]"), "Event - Registration Postback");
    }

    protected WebElement getFTDPostbackEle() {
        return assertElementExists(By.xpath("(//div[@class='event-list__item-text'])[2]"), "Event - FTD Postback");
    }

    protected WebElement getQFTDPostbackEle() {
        return assertElementExists(By.xpath("(//div[@class='event-list__item-text'])[3]"), "Event - QFTD Postback");
    }

    protected WebElement getCommissionPostbackEle() {
        return assertElementExists(By.xpath("(//div[@class='event-list__item-text'])[4]"), "Event - Commission Postback");
    }

    protected WebElement getPlacePostbackURLInputEle() {
        return assertElementExists(By.xpath("//div[@class='ht-textarea content-value']/textarea"), "Place postback URL Input Box");
    }

    protected WebElement getEditButtonEle() {
        return assertElementExists(By.xpath("//div[@class='operate']/button"), "Event - Edit Button");
    }

    protected WebElement getConfirmButtonEle() {
        return assertElementExists(By.xpath("//div[@class='operate-submit']/button[contains(@class,'primary')]"), "Event - Confirm Button");
    }

    public void verifyPostbackURLEventType(String selectedPostbackEventType){

        //If no event type has been enabled yet
        if(selectedPostbackEventType.equals("")){
            String currentlySelectedEventType = getCurrentlySelectedEventTypeEle().getText();
            Assert.assertTrue(driver.findElements(By.xpath("//div[@class='event-list__item--active-flag']")).isEmpty());
            Assert.assertTrue("Registration Postback".equals(currentlySelectedEventType), "Postback Event Type is not as expected. Expected (Default for not yet setup): Registration Postback, Actual: " + currentlySelectedEventType);
        }else{
            String currentlySelectedEventType = getCurrentlySelectedEventTypeEle().getText();
            Assert.assertTrue(selectedPostbackEventType.equals(currentlySelectedEventType), "Postback Event Type is not as expected. Expected: " + selectedPostbackEventType + ", Actual: " + currentlySelectedEventType);
            LogUtils.info("Verified Postback Event Type is as expected: " + currentlySelectedEventType);
        }
    }





}
