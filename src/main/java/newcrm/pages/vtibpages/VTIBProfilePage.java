package newcrm.pages.vtibpages;

import newcrm.pages.Page;
import newcrm.pages.ibpages.IBProfilePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import utils.LogUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class VTIBProfilePage extends IBProfilePage {

    public VTIBProfilePage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected WebElement getIbAgreementTabEle() {
        return assertElementExists(By.xpath("//div[@role='radiogroup']/label[2]"), "IB Agreement Tab");
    }

    @Override
    protected WebElement getIBAgreementAccountEle() {
        return assertElementExists(By.xpath("//div[@class='ib-agreement-content']/div/div[2] //tr/td[2]/div"), "IB Agreement Account Number");
    }
}