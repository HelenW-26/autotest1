package newcrm.pages.ptclientpages.register;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.RegisterEntryPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PTRegisterEntryPage extends RegisterEntryPage {
    private String tradeUrl = "";
    public PTRegisterEntryPage(WebDriver driver) {
        super(driver);
    }

    public void submit() {
        WebElement button = this.getSubmitButton();
        js.executeScript("arguments[0].click()",button);
        this.waitLoadingForCustomise(180);
        this.checkUrlContains(this.tradeUrl);
        this.waitLoading();
    }

    @Override
    public void setActionUrl(String url) {
        try{
            driver.findElement(By.xpath("//img[contains(@class,'closure')]")).click();
        }
        catch(Exception e)
        {
            GlobalMethods.printDebugInfo("no img popup");
        }

        WebElement e = this.getActionInput();
        this.tradeUrl = url.trim();
        if(e == null) {
            GlobalMethods.printDebugInfo("WARNING ** RegisterEntryPage: Do not find action input element");
            return;
        }
        this.setInputValue(this.getActionInput(), url);
        WebElement b = this.getActionButton();
        this.moveElementToVisible(b);
        b.click();
        GlobalMethods.printDebugInfo("RegisterEntryPage: set action url to: " + url);
    }


    @Override
    public boolean setCountry(String country) {
        this.getCountryInput().click();
        List<WebElement> countries = this.getCountryElements();
        for(WebElement e:countries) {
            String value = e.getAttribute("data-address");
            if(value != null && value.trim().length()>0) {
                if(country.equalsIgnoreCase(value.trim())) {
                    this.moveElementToVisible(e);
                    e.click();
                    GlobalMethods.printDebugInfo("RegisterEntryPage: set country to: " + value.trim());
                    return true;
                }
            }
        }
        GlobalMethods.printDebugInfo("RegisterEntryPage: Do not find country : " + country +".");
        this.getCountryInput().click();
        return false;
    }
    @Override
    protected WebElement getActionInput() {
        return this.findVisibleElemntByXpath("//input[@id='changeurl']");
    }

    @Override
    protected WebElement getActionButton() {
        return this.findClickableElemntBy(By.xpath("//button[@id='changebtn']"));
    }

    @Override
    protected List<WebElement> getCountryElements(){
        String xpath = "//li[@class='country']//ul[@class='account_opiton']/li";
        this.findVisibleElemntByXpath(xpath);
        return driver.findElements(By.xpath(xpath));
    }

    @Override
    protected WebElement getSubmitButton() {
        return this.findClickableElemntBy(By.xpath("//button[@id='button']"));
    }
}
