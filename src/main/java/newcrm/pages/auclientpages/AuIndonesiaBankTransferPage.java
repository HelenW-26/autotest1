package newcrm.pages.auclientpages;

import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.pages.clientpages.deposit.IndonesiaBankTransferPage;

public class AuIndonesiaBankTransferPage extends IndonesiaBankTransferPage{

	public AuIndonesiaBankTransferPage(WebDriver driver) {
		super(driver);
	}
	
	@Override
    public void setBank()
    {
        driver.findElement(By.xpath("(//input[@placeholder='Select'])[2]")).click();

        List<WebElement> allbank = driver.findElements(By.xpath("(//ul[@class='el-scrollbar__view el-select-dropdown__list'])[3]/li"));

        Random rd = new Random();
        int i = rd.nextInt(4) + 1;
        allbank.get(i).click();
    }
	
}
