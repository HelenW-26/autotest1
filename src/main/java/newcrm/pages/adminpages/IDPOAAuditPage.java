package newcrm.pages.adminpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class IDPOAAuditPage extends Page {

    public IDPOAAuditPage(WebDriver driver)
    {
        super(driver);
    }

    protected WebElement getPOACityInputEle() {
        return assertElementExists(By.xpath("//input[@id='citySuburb']"), "POA - City");
    }

    protected WebElement getPOAAddressInputEle() {
        return assertElementExists(By.xpath("//input[@id='address']"), "POA - Address");
    }

    public void poipoaAudit(String email)
    {
        waitLoading();
        WebElement searchOption = driver.findElement(By.xpath("//div[contains(text(),'Search Options')]"));
        searchOption.click();
        WebElement emailLink = driver.findElement(By.linkText("Email"));
        emailLink.click();

        WebElement inputQuery = driver.findElement(By.xpath("//input[@id='userQuery']"));
        inputQuery.sendKeys(email);
        WebElement searchBtn = driver.findElement(By.xpath("//button[@id='query']"));
        searchBtn.click();
        waitLoading();

        //audit POI
        try
        {
            WebElement auditPOI = driver.findElement(By.xpath("//table[@id='table']//tbody/tr[1]/td/a[@title='Audit']"));
            auditPOI.click();
            waitLoading();
            WebElement sanctionCheck = driver.findElement(By.xpath("//input[@name='sanctionStatus'][@value='true']"));
            sanctionCheck.click();

            Select passReason =  new Select(driver.findElement(By.id("passed_reason")));
            passReason.selectByValue("1");

            WebElement completedBtnSec = findClickableElementByXpath("//button[@class='btn btn-success']");
            completedBtnSec.click();
            waitLoading();
        } catch (Exception e)
        {
           GlobalMethods.printDebugInfo("Cannot found ID audit button");
        }

        //audit POA
        WebElement auditPOA = driver.findElement(By.xpath("//table[@id='table']//tbody/tr[2]/td/a[@title='Audit']"));
        // auditPOA.click();
        js.executeScript("arguments[0].click()",auditPOA);
        waitLoading();

        WebElement completedBtn = findClickableElementByXpath("//button[@class='btn btn-success']");
        completedBtn.click();

        waitLoading();
        GlobalMethods.printDebugInfo("POA POI audit successfully");
    }

    public void poipoaAuditNew(String email)
    {
        waitLoading();
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(),'Search Options')]")));
        WebElement searchOption = driver.findElement(By.xpath("//div[contains(text(),'Search Options')]"));
        searchOption.click();
        WebElement emailLink = driver.findElement(By.linkText("Email"));
        emailLink.click();

        WebElement inputQuery = driver.findElement(By.xpath("//input[@id='userQuery']"));
        inputQuery.sendKeys(email);
        WebElement searchBtn = driver.findElement(By.xpath("//button[@id='query']"));
        searchBtn.click();
        waitLoading();

        //audit POI
        try
        {
            WebElement auditPOI = driver.findElement(By.xpath("//td[text()='Proof of ID']/following-sibling::td/a[@title='Audit']"));
            auditPOI.click();
            waitLoading();

            if(!driver.findElements(By.xpath("//form[@id='audit_form'] //input[@type='radio' and @value='true' and not(@disabled='disabled')]")).isEmpty()) {
                WebElement sanctionCheck = driver.findElement(By.xpath("//form[@id='audit_form'] //input[@type='radio' and @value='true' and not(@disabled='disabled')]"));
                sanctionCheck.click();

                Select passReason =  new Select(driver.findElement(By.id("aml_passed_reason")));
                passReason.selectByVisibleText("Sumsub Audit Passed");
            }

            WebElement completedBtnSec = findClickableElementByXpath("//button[@class='btn btn-success']");
            completedBtnSec.click();
            By loadingImage = By.id("AjaxLoading");
            fastwait.until(ExpectedConditions.invisibilityOfElementLocated(loadingImage));
            fastwait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'loading table') and not(contains(@style, 'none'))]")));
        } catch (Exception e)
        {
            GlobalMethods.printDebugInfo("Cannot found ID audit button");
        }

        //audit POA
        try
        {
            //If POA status is still 'Submitted' or 'Pending', then continue
            WebElement auditPOA = driver.findElement(By.xpath("//td[text()='Proof of Address']/following-sibling::td[text()='Submitted' or text()='Pending']/following-sibling::td/a[@title='Audit']"));
            auditPOA.click();
//        js.executeScript("arguments[0].click()",auditPOA);
            waitLoading();

            String city = GlobalMethods.getRandomString(3)+ " TestCity";
            String address = GlobalMethods.getRandomNumberString(3)+" Test Address";

            this.setInputValue(getPOACityInputEle(),city);
            this.setInputValue(getPOAAddressInputEle(),address);

            WebElement completedBtn = findClickableElementByXpath("//button[@class='btn btn-success']");
            completedBtn.click();
            By loadingImage = By.id("AjaxLoading");
            fastwait.until(ExpectedConditions.invisibilityOfElementLocated(loadingImage));
            fastwait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'loading table') and not(contains(@style, 'none'))]")));

            GlobalMethods.printDebugInfo("POA POI audit successfully");
        } catch (Exception e)
        {
            GlobalMethods.printDebugInfo("Cannot found address audit button");
        }



    }

    public void poiAudit(String email)
    {
        waitLoading();
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(),'Search Options')]")));
        WebElement searchOption = driver.findElement(By.xpath("//div[contains(text(),'Search Options')]"));
        searchOption.click();
        WebElement emailLink = driver.findElement(By.linkText("Email"));
        emailLink.click();

        WebElement inputQuery = driver.findElement(By.xpath("//input[@id='userQuery']"));
        inputQuery.sendKeys(email);
        WebElement searchBtn = driver.findElement(By.xpath("//button[@id='query']"));
        searchBtn.click();
        waitLoading();

        //audit POI
        WebElement auditPOI = driver.findElement(By.xpath("//td[text()='Proof of ID']/following-sibling::td/a[@title='Audit']"));
        auditPOI.click();
        waitLoading();

        if(!driver.findElements(By.xpath("//form[@id='audit_form'] //input[@type='radio' and @value='true' and not(@disabled='disabled')]")).isEmpty()) {
            WebElement sanctionCheck = driver.findElement(By.xpath("//form[@id='audit_form'] //input[@type='radio' and @value='true' and not(@disabled='disabled')]"));
            sanctionCheck.click();

            Select passReason =  new Select(driver.findElement(By.id("aml_passed_reason")));
            passReason.selectByVisibleText("Sumsub Audit Passed");
        }

        WebElement completedBtnSec = findClickableElementByXpath("//button[@class='btn btn-success']");
        completedBtnSec.click();
        By loadingImage = By.id("AjaxLoading");
        fastwait.until(ExpectedConditions.invisibilityOfElementLocated(loadingImage));
        fastwait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'loading table') and not(contains(@style, 'none'))]")));
    }
}
