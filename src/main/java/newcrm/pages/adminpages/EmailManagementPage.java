package newcrm.pages.adminpages;

import newcrm.business.adminbusiness.AdminMenu;
import newcrm.factor.Factor;
import newcrm.global.GlobalProperties.AdminMenuName;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.LogUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EmailManagementPage extends AdminPage {

    WebDriverWait wait;
    WebDriverWait wait03;
    WebDriverWait longwait;
    public Factor myfactor;

    public EmailManagementPage(WebDriver driver)
    {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(50));
        this.wait03 = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.longwait = new WebDriverWait(driver, Duration.ofSeconds(50));
    }

    public void checkEmail(String AdminURL, String TraderEmail) throws InterruptedException {

        // Navigate to admin portal
        navigateToAdmin(AdminURL);

        // Go to Email Management
        goToEmailManagement();
        
        //Set Recipients
        setRecipients(TraderEmail);

        //Set from date
        setFromDate();

        //Check Status
        checkStatus();
    }

    public void navigateToAdmin(String url)
    {
        driver.navigate().to(url);
        driver.navigate().refresh();
        waitLoading();
    }

    public void goToEmailManagement()
    {
        AdminMenu adminMenu = myfactor.newInstance(AdminMenu.class);
        adminMenu.goToMenu(AdminMenuName.EMAIL_MGMT);

        // Wait for page content to load before further proceed
        waitLoadingEmailContent();

        // Sometimes when redirect to email management too fast, previous page content will be loaded in email page. Hence, need to refresh page to reload email content.
        WebElement emailBtn = checkElementExists(By.xpath("//button[normalize-space() = 'Send Email']"));

        if (emailBtn == null) {
            LogUtils.info("Page content not fully loaded. Going to refresh page...");
            adminMenu.refresh();
            adminMenu.goToMenu(AdminMenuName.EMAIL_MGMT);
            waitLoadingEmailContent();
        }

        LogUtils.info("Start to find the email");
        waitLoading();
    }

    public void waitLoadingEmailContent() {
        assertVisibleElementExists(By.xpath("//div[@id='mailList']"), "Page Content");
    }

    public void setRecipients(String TraderEmail)
    {
        WebElement recip = driver.findElement(By.xpath("//input[@class='form-control bootstrap-table-filter-control-toMail search-input']"));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@class='form-control bootstrap-table-filter-control-toMail search-input']")));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click()",recip);
        js.executeScript("arguments[0].value = '" + TraderEmail + "';", recip);
        LogUtils.info("Set Recipients as: " + TraderEmail);
    }

    public void setFromDate()
    {
        LocalDate dayBeforeYesterday = LocalDate.now().minusDays(2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = dayBeforeYesterday.format(formatter);

        WebElement fromDate = driver.findElement(By.xpath("//div[./span[contains(text(),'From')]]/input"));
        fromDate.clear();
        fromDate.sendKeys(formattedDate);

        WebElement FromSpan = driver.findElement(By.xpath("//span[contains(text(),'From')]"));
        FromSpan.click();
    }
    public void checkStatus()
    {
        // Click search button
        clickSearchBtn();

        //Check Status
        //boolean status = driver.findElement(By.xpath("//table[@id='table']/tbody/tr[1]/td[contains(text(),'Request Success')]")).isDisplayed();
        String statusText = driver.findElement(By.xpath("//table[@id='table']/tbody/tr[1]/td[7]")).getText();
        boolean bIsSendSuccess = statusText.equals("Request Success");

        LogUtils.info("The first email status is: " + statusText);

        if (!bIsSendSuccess) {
            assert false : "The email failed to send, Status: " + statusText;
        }

        //Assert.assertTrue(status,"The email send failed");
    }

}
