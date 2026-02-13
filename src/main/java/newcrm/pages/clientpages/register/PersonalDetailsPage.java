package newcrm.pages.clientpages.register;

import java.util.Arrays;
import java.util.List;

import java.util.Random;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import utils.LogUtils;

public class PersonalDetailsPage extends Page {

	public PersonalDetailsPage(WebDriver driver) {
		super(driver);
	}
	
	//elements methods
	protected WebElement getTitleElement() {
		return this.findClickableElementByXpath("//input[@id='title']");
	}
	
	protected WebElement getFirstNameInput() {
		return this.findVisibleElemntByXpath("//input[@data-testid='firstName']");
	}
	
	protected WebElement getMiddleNameInput() {
		return this.findVisibleElemntByXpath("//input[@data-testid='middleName']");
	}
	
	protected WebElement getLastNameInput() {
		return this.findVisibleElemntByXpath("//input[@data-testid='lastName']");
	}
	
	protected WebElement getNationalityInput(){
		return this.findClickableElemntBy(By.id("nationalityId"));
	}
	
	protected WebElement getEmailInput() {
		return this.findVisibleElemntByXpath("//input[@data-testid='email']");
	}
	
	protected WebElement getMobileInput() {
		return this.findVisibleElemntByXpath("//input[@data-testid='mobile']");
	}
	
	protected WebElement getDayInput() {
		return this.findClickableElementByXpath("//div[@data-testid='dob']/div/input");
	}
	
	protected WebElement getMonthInput() {
		return this.findClickableElementByXpath("//div[@data-testid='month']/div/input");
	}
	
	protected WebElement getYearInput() {
		return this.findClickableElementByXpath("//div[@data-testid='year']/div/input");
	}
	
	protected WebElement getBirthPlaceInput() {
		return this.findClickableElemntBy(By.xpath("//input[@id='placeOfBirth']"));
	}
	
	protected WebElement getIDTypeInput() {
		return this.findClickableElemntBy(By.xpath("//input[@id='idType']"));
	}
	
	protected WebElement getIDNumInput() {
		return this.findClickableElemntBy(By.xpath("//input[@data-testid='idNumber']"));
	}

	protected WebElement getGenderInput() {
		return this.findClickableElementByXpath("//div[@data-testid='gender']/div/input");
	}

	protected WebElement getOpenAccountDialogCloseBtnEle() {
		return assertElementExists(By.xpath("//div[contains(@class,'account_opening_drawer') and not(contains(@style,'display'))]//button[@class='el-drawer__close-btn']"), "Dialog Close button");
	}

//	protected WebElement getReferCheckBox() {
//		return this.findClickableElementByXpath("//label[@data-testid='checkbox']/span[1]");
//	}
//	
//	
//	protected WebElement getReferInput() {
//		return this.findClickableElementByXpath("//input[@data-testid='referredBy']");
//	}
	
	protected WebElement getNextButton() {
		return this.findClickableElementByXpath("//button[@data-testid='next']");
	}

	//get necessary data from page
	
	public String getPageTitle() {
		return this.findVisibleElemntByXpath("//*[self::h2 or self::h3]").getText();
	}
	
	public String getFirstName() {
		return this.getFirstNameInput().getAttribute("value");
	}
	
	public String getLastName() {
		return this.getLastNameInput().getAttribute("value");
	}
	
	public String getEmail() {
		return this.getEmailInput().getAttribute("value");
	}
	
	public String getPhone() {
		return this.getMobileInput().getAttribute("value");
	}
	
	public String getCountry() {
		return this.findVisibleElemntByXpath("//li[@class='el-select-dropdown__item selected']/span").getAttribute("innerText").trim();
	}
	
	//put data to the page
	/**
	 * set a default name
	 */
	
	public String setMiddleName(String name) {
		this.setInputValue(this.getMiddleNameInput(), name);
		LogUtils.info("PersonalDetailsPage: set Middle name to: " + name);

		return name;
	}
	
//	public void setRefer(String name) {
//		WebElement e = this.getReferCheckBox();
//		if(e==null) {
//			return;
//		}
//		String info = e.getAttribute("class");
//		if(info!=null) {
//			if(!info.contains("is-checked")) {
//				e.click();
//			}
//		}
//		this.setInputValue(this.getReferInput(), name);
//		LogUtils.info("PersonalDetailsPage: set referredBy to: " + name);
//	}
	
	public String setNationality() {
		WebElement e = this.getNationalityInput();
		this.moveElementToVisible(e);
		e.click();
		List<WebElement> els = this.getAllOpendElements();
		String result = selectNationalityRandomValueFromDropDownList(els);

		if(result!=null) {
			LogUtils.info("PersonalDetailsPage: set Nationality to :" + result );
			return result;
		}else
		{
			LogUtils.info("ERROR: PersonalDetailsPage: set Nationality failed!");
			e.click();
		}
		return null;
	}
	
	public String setBirthDay() {
		WebElement e = this.getYearInput();
		this.moveElementToVisible(e);
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
		this.moveElementToVisible(e);
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
		this.moveElementToVisible(e);
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
	
	public String setBirthPlace() {
		WebElement e = this.getBirthPlaceInput();
		e.click();
		String result = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
		if(result!=null) {
			LogUtils.info("PersonalDetailsPage: set Birth Place to: " + result);
		}else
		{
			LogUtils.info("ERROR: PersonalDetailsPage: set Birth Place failed!" );
			e.click();
			return null;
		}
		return result;
	}
	
	public String setIdentificationType() {
		WebElement e = this.getIDTypeInput();
		e.click();
		String result = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
		if(result!=null) {
			LogUtils.info("PersonalDetailsPage: set Identification Type to: " + result);
		}else
		{
			LogUtils.info("ERROR: PersonalDetailsPage: set Identification Type failed!" );
			e.click();
			return null;
		}
		return result;
	}
	
	public void setIDNumber(String idnum) {
		this.setInputValue(this.getIDNumInput(), idnum);
		LogUtils.info("PersonalDetailsPage: set Identification Number to: " + idnum);
	}
	
	public String setPersonTitle() {
		WebElement e = this.getTitleElement();
		e.click();
		String result = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
		if(result!=null) {
			LogUtils.info("PersonalDetailsPage: set person title to: " + result);
		}else
		{
			LogUtils.info("ERROR: PersonalDetailsPage: set person title failed!" );
			e.click();
			return null;
		}
		return result;
	}
	public String setGender(String gender) {
		WebElement e = this.getGenderInput();
		this.moveElementToVisible(e);
		e.click();
		String result = this.selectRandomValueFromDropDownList(this.getAllOpendElements());
		LogUtils.info("PersonalDetailsPage: set Gender to: " + result);

		return result;
	}

	public String setfirstName(String firstName) {

		WebElement e = this.getFirstNameInput();
		e.sendKeys(firstName);
		LogUtils.info("PersonalDetailsPage: set firstName to: " + firstName);

		return firstName;
	}


	public String setLastName(String lastName) {

		WebElement e = this.getLastNameInput();
		e.sendKeys(lastName);
		LogUtils.info("PersonalDetailsPage: set lastName to: " + lastName);

		return lastName;
	}

	public String setPhone(String phone) {

		WebElement e = this.getMobileInput();
		e.sendKeys(phone);
		LogUtils.info("PersonalDetailsPage: set mobile to: " + phone);
		return phone;
	}

	public void submit() {
		WebElement e = this.getNextButton();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click()",e);
		this.waitLoading();
	}

	public void createButton()
	{
		this.waitLoading();
		try {
			driver.findElement(By.xpath("//button[@class='el-dialog__headerbtn'])[2]")).click();
		}
		catch(Exception e)
		{
			LogUtils.info("no popup ad");
		}
		try{
			driver.findElement(By.xpath("//li[@data-testid='menu.authentication']")).click();
		}
		catch(Exception e)
		{
			LogUtils.info("no need to go to authentication page");
		}
		waitLoading();
		WebElement createBtn = driver.findElement(By.xpath("//button[@data-testid='button']"));
		js.executeScript("arguments[0].click();",createBtn);
		waitLoading();
	}

	public String setCountry()
	{
		driver.findElement(By.xpath("//div[@data-testid='selection']")).sendKeys("France");
		return "France";
	}

	public void closeImg()
	{
		driver.findElement(By.xpath("//img[@data-testid='closeImg']")).click();
	}

	public void closeToolSkipButton()
	{
		try {
			driver.findElement(By.xpath("//a[@class='introjs-skipbutton']")).click();
			waitLoading();
		}
		catch (Exception e)
		{
			LogUtils.info("no tools skip popup");
		}
	}
	protected String selectNationalityRandomValueFromDropDownList(List<WebElement> els) {
		if(els==null || els.size()< 1) {
			return null;
		}
		//ACM-22361,PD-1754,PD-1849 remove the following four countries because of some restriction
		WebElement e;
		List<String> excludeCountries = Arrays.asList("VIETNAM", "KAZAKHSTAN", "RUSSIA","LATVIA");
		String result = "";
		do {
			Random random = new Random();
			 e = els.get(random.nextInt(els.size()));
			result = e.getAttribute("innerText");
		}
		while(excludeCountries.contains(result.toUpperCase()));
		this.moveElementToVisible(e);
		this.clickElement(e);
		return result;
	}

	public void proceedToIDVerfication() {}

	public void closeOpenAccountDialog() {
		WebElement e = getOpenAccountDialogCloseBtnEle();
		triggerClickEvent(e);
		LogUtils.info("Close Account Opening Verification Dialog");
	}

    public void goToPersonalDetailsPage() {
    }

}
