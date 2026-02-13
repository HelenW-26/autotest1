package newcrm.pages.moclientpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.PersonalDetailsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.LogUtils;

public class MOPersonalDetailsPage extends PersonalDetailsPage {

    public  MOPersonalDetailsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected WebElement getMobileInput() {
        return this.findClickableElementByXpath("//div[@data-testid='phoneCode']/input");
    }

    @Override
    public String setGender(String gender) {
        WebElement e = this.getGenderInput();
        e.click();
        String result = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
        LogUtils.info("PersonalDetailsPage: set Gender to: " + result);

        return result;
    }

    @Override
    public String setPhone(String phone) {
        WebElement e = this.getMobileInput();
        e.click();
        e.sendKeys(phone);
        LogUtils.info("PersonalDetailsPage: set Mobile to: " + phone);
        return phone;
    }

    @Override
    public void proceedToIDVerfication()
    {
        WebElement e = driver.findElement(By.xpath("//div[@class='register_guide_btn_box']/button/span[contains(text(),'ID Verification')]"));
        triggerElementClickEvent_withoutMoveElement(e);
        LogUtils.info("Go to Personal Details page.");
    }

    @Override
    public String setBirthDay() {
        WebElement e = this.getYearInput();
        e.click();
        String year = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
        if(year!=null) {
            LogUtils.info("PersonalDetailsPage: set Year to: " + year);
        }else
        {
            LogUtils.info("ERROR: PersonalDetailsPage: set Year failed!" );
            e.click();
            return null;
        }
        waitLoading();
        e = this.getMonthInput();
        e.click();
        String month = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
        if(month!=null) {
            LogUtils.info("PersonalDetailsPage: set Month to: " + month);
        }else
        {
            LogUtils.info("ERROR: PersonalDetailsPage: set Month failed!" );
            e.click();
            return null;
        }
        waitLoading();
        e = this.getDayInput();
        e.click();
        String day = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
        if(day!=null) {
            LogUtils.info("PersonalDetailsPage: set Day to: " + day);
        }else
        {
            LogUtils.info("ERROR: PersonalDetailsPage: set Day failed!" );
            e.click();
            return null;
        }

        return day+"-"+month+"-"+year;
    }

}
