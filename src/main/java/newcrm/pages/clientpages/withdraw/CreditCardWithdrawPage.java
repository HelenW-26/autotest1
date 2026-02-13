package newcrm.pages.clientpages.withdraw;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CreditCardWithdrawPage extends SkrillWithdrawPage {
	public CreditCardWithdrawPage(WebDriver driver) {
		super(driver);
	}

	public int getAmount1() {
		WebElement ele = driver
				.findElement(By.xpath("//div[@data-testid='creditCard']//div[@class='amount']"));
		super.moveElementToVisible(ele);
		if (ele == null) {
			return -1;
		}
		String txt = ele.getText();
		int amount1 = Integer.parseInt(txt.substring(1,txt.length()));
		return amount1;

	}

	public int getAmount2() {
		WebElement ele = driver
				.findElement(By.xpath("//*[@id=\"withdrawalMethod\"]/div[3]/div[2]/div/div[1]/div/div[2]/div[2]"));
		super.moveElementToVisible(ele);
		if (ele == null) {
			return -1;
		}
		int amount2 = Integer.parseInt(ele.getText());
		return amount2;

	}

	public int getCCTotalAmount() {
		WebElement total = driver
				.findElement(By.cssSelector("div.totalAmount.el-col.el-col-24.el-col-xs-8.el-col-sm-12"));
		super.moveElementToVisible(total);
		if (total == null) {
			return -1;
		}
		int totalAmount = Integer.parseInt(total.getText().substring(8));
		int amount1 = this.getAmount1();
		int amount2 = this.getAmount2();
		if (amount1 + amount2 == totalAmount) {
			System.out.println("CreditCard Withdraw Amount 1 is " + amount1);
			System.out.println("CreditCard Withdraw Amount 2 is " + amount2);
			System.out.println("CreditCard Withdraw Total is " + totalAmount);
			System.out.println("CreditCard Withdraw 1 + 2= " + totalAmount);

		}
		return totalAmount;

	}

	public String getCCWithdrawTabText() {
		// 点击 Credit Card tab
		WebElement ccTab=findClickableElemntByTestId("form_0");
		ccTab.click();

		WebElement ccTabList=findVisibleElemntByCss("body > div.el-select-dropdown.el-popper.ht-select-dropdown > div.el-scrollbar > " +
				"div.el-select-dropdown__wrap.el-scrollbar__wrap > ul");
		return ccTab.getText() ;

	}
	/**
	 * 处理Credit popup
	 */
	public void handleCreditPopUp() {
        try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"withdraw\"]//div[contains(@class, 'dialog_info') and contains(., 'credit amounting to')]")));
            WebElement popUp=wait.until(driver ->
					driver.findElement(By.
							xpath("//*[@id=\"withdraw\"]//div[contains(@class, 'dialog_info') and contains(., 'credit amounting to')]")));
            if (popUp != null){
                WebElement close=findClickableElementByXpath("//*[@id=\"withdraw\"]//div//button[contains(., 'OK')]");
                close.click();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}