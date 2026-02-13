package newcrm.pages.clientpages.deposit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;

public class ThaiBankTransPage extends InterBankTransPage {

	public ThaiBankTransPage(WebDriver driver) {
		super(driver);

	}

	@Override
	public void setNotes(String note) {
		WebElement notes = this.findClickableElemntByTestId("applicationNotes");
		super.moveElementToVisible(notes);
		notes.clear();
		notes.sendKeys(note);
		GlobalMethods.printDebugInfo("Set Important notes to: " + notes);
	}

	public int getDepositAmountFromThaiBankUI() {
		super.waitLoading();
		WebElement amountInThaiCurrency = driver.findElement(By.cssSelector("p:nth-of-type(2) > span:nth-of-type(2)"));
		super.waitLoading();
		if (amountInThaiCurrency == null) {
			return -1;
		}
		double amount = Double.parseDouble(amountInThaiCurrency.getText());
		int depositAmount = (int) Math.round(amount * 100) / 100;
		System.out.println("Amount in THB is " + "THB: " + depositAmount);
		return depositAmount;

	}

	// deposit/thailandzotapay
	public int getDepositAmountFromZotaPay() {

		WebElement amountInZotapay = driver.findElement(By.cssSelector("h2 > div"));

		if (amountInZotapay == null) {
			return -1;
		}

		String amountWithCurrency = amountInZotapay.getText().trim();
		amountWithCurrency = amountWithCurrency.replaceAll(",", "");

		Pattern compile = Pattern.compile("[1-9]\\d*.[0-9]\\d*.[0-9]\\d*");
		Matcher matcher = compile.matcher(amountWithCurrency);
		matcher.find();
		double amount = Double.parseDouble(matcher.group());
		int zotapayAmount = Double.valueOf(amount).intValue();
		System.out.println("THB amount in ZotaPAY Channel is " + "THB: " + zotapayAmount);
		return zotapayAmount;

	}

	// deposit/thailandPayToDay
	public int getDepositAmountFromPayToDay() {
		WebElement amountInPayToDay = driver
				.findElement(By.cssSelector(".thailand_second ul > li:nth-of-type(4) > span"));
		super.moveElementToVisible(amountInPayToDay);
		super.waitLoading();
		if (amountInPayToDay == null) {
			return -1;
		}

		String amountWithCurrency = amountInPayToDay.getText().trim();
		int payToDayAmount = Double.valueOf(amountWithCurrency).intValue();
		System.out.println("THB amount in PAYTODAY Channel is " + "THB: " + payToDayAmount);
		return payToDayAmount;

	}

	// deposit/thailandEeziePay
	public int getDepositAmountFromEeziePay() {
		WebElement amountInEeziepay = driver.findElement(By.cssSelector("div.amount"));
		if (amountInEeziepay == null) {
			return -1;
		}

		String amountWithCurrency = amountInEeziepay.getText().trim();
		amountWithCurrency = amountWithCurrency.replaceAll(",", "");

		Pattern compile = Pattern.compile("[1-9]\\d*.[0-9]\\d*.[0-9]\\d*");
		Matcher matcher = compile.matcher(amountWithCurrency);
		matcher.find();
		double amount = Double.parseDouble(matcher.group());
		int eezieAmount = Double.valueOf(amount).intValue();
		System.out.println("THB amount in EEZIEPAY Channel is " + "THB: " + eezieAmount);
		return eezieAmount;

	}

	// deposit/mijiPay
	public int getDepositAmountFromThaiPA() {
		WebElement amountInThaiPA = driver
				.findElement(By.cssSelector("div#header-content > .payment-amount.text-info"));
		if (amountInThaiPA == null) {
			return -1;
		}

		String amountWithCurrency = amountInThaiPA.getText().trim();
		amountWithCurrency = amountWithCurrency.replaceAll(",", "");

		Pattern compile = Pattern.compile("[1-9]\\d*.[0-9]\\d*.[0-9]\\d*");
		Matcher matcher = compile.matcher(amountWithCurrency);
		matcher.find();
		double amount = Double.parseDouble(matcher.group());
		int thaiPAAmount = Double.valueOf(amount).intValue();
		System.out.println("THB amount in PA Channel is " + "THB: " + thaiPAAmount);
		return thaiPAAmount;

	}

	// deposit/thailandXPay
	public int getDepositAmountFromXPay() {
		WebElement amountInXPay = driver.findElement(By.cssSelector("tr:nth-of-type(1) > td:nth-of-type(3)"));
		if (amountInXPay == null) {
			return -1;
		}

		String amountWithCurrency = amountInXPay.getText().trim();
		amountWithCurrency = amountWithCurrency.replaceAll(",", "");

		Pattern compile = Pattern.compile("[1-9]\\d*.[0-9]\\d*.[0-9]\\d*");
		Matcher matcher = compile.matcher(amountWithCurrency);
		matcher.find();
		double amount = Double.parseDouble(matcher.group());
		int xPayAmount = Double.valueOf(amount).intValue();
		System.out.println("THB amount in XPAY Channel is " + "THB: " + xPayAmount);
		return xPayAmount;

	}
}
