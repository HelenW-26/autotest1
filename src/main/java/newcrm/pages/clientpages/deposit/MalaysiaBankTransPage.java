package newcrm.pages.clientpages.deposit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;

public class MalaysiaBankTransPage extends InterBankTransPage {

	public MalaysiaBankTransPage(WebDriver driver) {
		super(driver);
	}

	public WebElement secondaryButton() {

		WebElement secondaryButton = super.findClickableElementByXpath("//div[@class='info_box_choose']/button[2]");
		return secondaryButton;

	}


	public double getDepositAmountFromMalayBank() {
		super.waitLoading();
		WebElement amountInMalayCurrency = this.findVisibleElemntBy(By.xpath("//form/div/ul[2]/li[2]/p[2]/span[2]"));
		super.waitLoading();
		if (amountInMalayCurrency == null) {
			return -1.0;
		}
		double amount = Double.parseDouble(amountInMalayCurrency.getText());
		System.out.println("amount in Malaysian Ringgit is" + " MYR: " + amount);
		return amount;

	}

	public double getDepositAmountFromThirdParty() {

		WebElement amountInThirdParty = null;
		try {
			amountInThirdParty = super.findClickableElementByXpath("//*[@id=\"main\"]/div[1]/h2/div");
		} catch (Exception e) {
			GlobalMethods.printDebugInfo("MalaysiaBankTransPage: ERROR!! Did not find amount from third party page");
			driver.navigate().back();
			throw new NoSuchElementException("Try to find amount from the third party page");
		}

		if (amountInThirdParty == null) {
			return -1.0;
		}
		String amountwithCurrency = amountInThirdParty.getText().trim();
		amountwithCurrency = amountwithCurrency.replaceAll(",", "");

		Pattern compile = Pattern.compile("[1-9]\\d*.[0-9]\\d*.[0-9]\\d*");
		Matcher matcher = compile.matcher(amountwithCurrency);
		matcher.find();
		double amount = Double.parseDouble(matcher.group());
		System.out.println("Amount in Malaysian Ringgit in Third URL is" + " MYR: " + amount);
		return amount;

	}

	public void moveToNoteBox() {

		WebElement notes = this.findClickableElementByXpath("//input[@data-testid='applicationNotes']");
		super.moveElementToVisible(notes);
	}

	@Override
	public void setNotes(String notes) {

		WebElement notesInput = this.findClickableElementByXpath("//input[@data-testid='applicationNotes']");
		notesInput.clear();
		notesInput.sendKeys(notes);
		GlobalMethods.printDebugInfo("Set Important notes to: " + notes);
	}
}