package newcrm.pages.auclientpages.Register;

import newcrm.global.GlobalMethods;
import newcrm.pages.clientpages.register.ResidentialAddressPage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import utils.LogUtils;

public class PRODVFSCResidentAdressPage extends ResidentialAddressPage{
    public PRODVFSCResidentAdressPage(WebDriver driver) {
        super(driver);

    }

    public void setAddress(String streetnum,String streetname) {
        String country = this.getCountry();
        if(country==null || country.trim().length()==0) {
            LogUtils.info("ERROR: ResidentialAddressPage: Find country Failed");
            throw new NoSuchElementException("ResidentialAddressPage: Find country Failed");
        }

        this.setInputValue(this.getStreetNumberInput(), streetnum);
        this.setInputValue(this.getAddressInput(), streetname);

        LogUtils.info("ResidentialAddressPage: Set Address to :" + streetnum + " "+ streetname);
    }
}
