package newcrm.pages.owspages;

import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class OWSCPAAllocationPage extends Page {

    public OWSCPAAllocationPage(WebDriver driver) {
        super(driver);
    }

    protected WebElement getFirstRowViewButtonEle() {
        return assertElementExists(By.xpath("//*[@data-icon='eye']/parent::span/parent::span/parent::button"), "First Row - View Button");
    }

    protected WebElement getFirstConditionIsIsntEle() {
        return assertElementExists(By.xpath("((//div[contains(@class,'conditionItem')][1]) //span[contains(@class,'ant-typography')])[2]"), "First Condition - Is/Isn't Ele");
    }

    protected List<WebElement> getConditionCountryListEle() {
        return assertElementsExists(By.xpath("((//div[contains(@class,'conditionItem')][1]) //span[contains(@class,'ant-tag')])"), "First Condition - Country List");
    }

    protected WebElement getAssignedSalesNameUIDEle() {
        return assertElementExists(By.xpath("//div[contains(@class,'formItemWithPadding')] //span[contains(@class,'ant-tag')]"), "Assigned Sales - Name & UID");
    }

    public String getCPAAllocationSales(String country){
        triggerElementClickEvent_withoutMoveElement(getFirstRowViewButtonEle());
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("((//div[contains(@class,'conditionItem')][1]) //span[contains(@class,'ant-typography')])[2]")));

        String firstConditionIsIsnt = getFirstConditionIsIsntEle().getText().trim();
        List<WebElement> countryEles = getConditionCountryListEle();
        List<String> countryList = countryEles.stream().map(WebElement::getText).toList();

        if(firstConditionIsIsnt.equalsIgnoreCase("is") && countryList.contains(country)){
            String assignedSalesAndUID = getAssignedSalesNameUIDEle().getText();
            return assignedSalesAndUID.replaceAll("\\s- UID:.*", "").trim();
        } else {
            return "";
        }
    }




}
