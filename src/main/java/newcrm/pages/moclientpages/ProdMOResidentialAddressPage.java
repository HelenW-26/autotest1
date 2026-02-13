package newcrm.pages.moclientpages;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.ResidentialAddressPage;
import org.openqa.selenium.WebDriver;
import utils.LogUtils;

public class ProdMOResidentialAddressPage extends ResidentialAddressPage {

    public ProdMOResidentialAddressPage(WebDriver driver)
    {
        super(driver);
    }
    @Override
    public void setAddress(String streetnum,String streetname) {
       /* String country = this.getCountry();
        if(country==null || country.trim().length()==0) {
            LogUtils.info("ERROR: ResidentialAddressPage: Find country Failed");
            throw new NoSuchElementException("ResidentialAddressPage: Find country Failed");
        }

        if(country.trim().equalsIgnoreCase("France") ||country.trim().equalsIgnoreCase("United Kingdom")||country.trim().equalsIgnoreCase("Malaysia") ) {*/
            this.setInputValue(this.getStreetNumberInput(), streetnum);
            this.setInputValue(this.getAddressInput(), streetname);
       /* }else {
            this.setInputValue(this.getAddressInput(),streetnum + " "+ streetname);
        }*/

        LogUtils.info("ResidentialAddressPage: Set Address to :" + streetnum + " "+ streetname);
    }

    @Override
    public void setState(String state) {
       /* String country = this.getCountry();

        if(country.trim().equalsIgnoreCase("Australia")) {
            WebElement e = this.findClickableElemntBy(By.id("state"));
            this.moveElementToVisible(e);
            e.click();
            List<WebElement> ops = this.getAllOpendElements();
            state = this.selectRandomValueFromDropDownList(ops);
        }else {*/
            this.setInputValue(this.getStateInput(), state);
       // }
        LogUtils.info("ResidentialAddressPage: Set State to :" + state);
    }
}
