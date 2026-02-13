package clientBase;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import vantagecrm.Utils;

public class wholesale {
	private WebDriver driver;

	public wholesale(WebDriver driver)
	{
		this.driver = driver;
	}
	
	public WebElement getUpgrade2ProLnk() {
		WebElement tempEle = driver.findElement(By.linkText("UPGRADE TO PRO"));
		return tempEle;
	}
	
	//page 1 T&C checkbox
	public WebElement getUpgradeCheckbox() {
		WebElement tempEle = driver.findElement(By.xpath("//label[@data-testid='upgradeCheckbox']/span"));
		return tempEle;
	}
	
	public WebElement getUpgradeButton() {
		WebElement tempEle = driver.findElement(By.xpath("//button[@data-testid='upgradeButton']"));
		return tempEle;
	}
	
	//Page 2 wealth test
	public List<WebElement> getInfoGroup() {
		List<WebElement> tempList = null;
		tempList = driver.findElements(By.cssSelector("section.info_group > p"));
		return tempList;
	}
	
	public WebElement getProName() {
		WebElement tempEle = getInfoGroup().get(0);
		return tempEle;
	}
	
	public WebElement getProEmail() {
		WebElement tempEle = getInfoGroup().get(1);
		return tempEle;
	}
	
	public List<WebElement> get1stQuestionOptions() {
		List<WebElement> tempList = null;
		tempList = driver.findElements(By.xpath("//div[@data-testid='question_wholesale.wealth.annualincome.gross']/div/label"));
		return tempList;
	}
	
	//0-$50k
	public WebElement get1stQuestionOption50k() {
		WebElement tempEle = driver.findElement(By.xpath("//label[@data-testid='answer_wholesale.wealth.annualincome.gross.amount1']/span"));
		return tempEle;
	}
	//$50- $150k
	public WebElement get1stQuestionOption150k() {
		WebElement tempEle = driver.findElement(By.xpath("//label[@data-testid='answer_wholesale.wealth.annualincome.gross.amount2']/span"));
		return tempEle;
	}
	//$150- $249k
	public WebElement get1stQuestionOption249k() {
		WebElement tempEle = driver.findElement(By.xpath("//label[@data-testid='answer_wholesale.wealth.annualincome.gross.amount3']/span"));
		return tempEle;
	}
	//Over $250k
	public WebElement get1stQuestionOption250k() {
		WebElement tempEle = driver.findElement(By.xpath("//label[@data-testid='answer_wholesale.wealth.annualincome.gross.amount4']/span"));
		return tempEle;
	}
	//The second question
	public List<WebElement> get2ndQuestionOptions() {
		List<WebElement> tempList = null;
		tempList = driver.findElements(By.xpath("//div[@data-testid='question_wholesale.wealth.annualincome.lasttwoyears']/div/label"));
		return tempList;
	}
	
	//The second question option yes
	public WebElement get2ndQuestionOptionYes() {
		WebElement tempEle = driver.findElement(By.xpath("//label[@data-testid='answer_wholesale.wealth.annualincome.lasttwoyears.yes']/span"));
		return tempEle;
	}
	//The second question option no
	public WebElement get2ndQuestionOptionNo() {
		WebElement tempEle = driver.findElement(By.xpath("//label[@data-testid='answer_wholesale.wealth.annualincome.lasttwoyears.no']/span"));
		return tempEle;
	}
	//The third question
	public List<WebElement> get3rdQuestionOptions() {
		List<WebElement> tempList = null;
		tempList = driver.findElements(By.xpath("//div[@data-testid='question_wholesale.wealth.netassets']/div/label"));
		return tempList;
	}
	
	//The third question option yes
	public WebElement get3rdQuestionOptionYes() {
		WebElement tempEle = driver.findElement(By.xpath("//label[@data-testid='answer_wholesale.wealth.netassets.yes']/span"));
		return tempEle;
	}
	//The third question option no
	public WebElement get3rdQuestionOptionNo() {
		WebElement tempEle = driver.findElement(By.xpath("//label[@data-testid='answer_wholesale.wealth.netassets.no']/span"));
		return tempEle;
	}
	
	//Next Button
	public WebElement getNextButton() {
		WebElement tempEle = driver.findElement(By.xpath("//button[@data-testid='nextButton']"));
		return tempEle;
	}
	
	//Page 3: Wealth Test: upload file and tick declaration
	/***************************************/
	public void chooseUploadedFile() {
		driver.findElement(By.xpath("//input[@type='file']")).sendKeys(Utils.workingDir + "\\proof.png");
	}
	
	//declaration checkbox
	public WebElement getDeclarationCheckbox() {
		WebElement tempEle = driver.findElement(By.xpath("//label[@data-testid='declarationCheckbox']/span"));
		return tempEle;
	}
	
	public WebElement getUploadButton() {
		WebElement tempEle = driver.findElement(By.xpath("//button[@data-testid='uploadButton']"));
		return tempEle;
	}
	
	public WebElement getBackButton() {
		WebElement tempEle = driver.findElement(By.xpath("//a[@data-testid='backButton']"));
		return tempEle;
	}
	
	//Page 3: 2 Sophisticated questions
	/***************************************/
	
	//The first question
	public List<WebElement> get3rdPg1stQuestionOptions() {
		List<WebElement> tempList = null;
		tempList = driver.findElements(By.xpath("//div[@data-testid='question_wholesale.wealthadditional.quarterlytraderecord']/div/label"));
		return tempList;
	}
	
	//The Page 3 first question option yes
	public WebElement get3rdPg1stQuestionOptionYes() {
		WebElement tempEle = driver.findElement(By.xpath("//label[@data-testid='answer_wholesale.wealthadditional.quarterlytraderecord.yes']/span"));
		return tempEle;
	}
	
	//The Page 3 first question option no
	public WebElement get3rdPg1stQuestionOptionNo() {
		WebElement tempEle = driver.findElement(By.xpath("//label[@data-testid='answer_wholesale.wealthadditional.quarterlytraderecord.no']/span"));
		return tempEle;
	}
	
	//The Page 3 second question
	public List<WebElement> get3rdPg2ndQuestionOptions() {
		List<WebElement> tempList = null;
		tempList = driver.findElements(By.xpath("//div[@data-testid='question_wholesale.wealthadditional.yearlytraderecord']/div/label"));
		return tempList;
	}
	
	//The Page 3 second question option yes
	public WebElement get3rdPg2ndQuestionOptionYes() {
		WebElement tempEle = driver.findElement(By.xpath("//label[@data-testid='answer_wholesale.wealthadditional.yearlytraderecord.yes']/span"));
		return tempEle;
	}
	//The Page 3 second question option no
	public WebElement get3rdPg2ndQuestionOptionNo() {
		WebElement tempEle = driver.findElement(By.xpath("//label[@data-testid='answer_wholesale.wealthadditional.yearlytraderecord.no']/span"));
		return tempEle;
	}
	
	//page 5 Access Sophisticated Investor Quiz
	public WebElement getQuizButton() {
		WebElement tempEle = driver.findElement(By.xpath("//button[@data-testid='showQuizButton']"));
		return tempEle;
	}
	
	public WebElement getResitQuizButton() {
		WebElement tempEle = driver.findElement(By.xpath("//button[@data-testid='retryButton']"));
		return tempEle;
	}
	
	// The Quiz 50 Questions
	/***************************************/
	public WebElement getQuizAnswer(int questionId,String answer) {
		//String xpath="//label[@data-testid='answer_" + testId +"']/span";
		//String xpath = "//span[@class='el-radio-button__inner' and contains(text(),'"+ answer +"')]";
		String xpath = "//ul[@class='radio_wrapper']/li["+questionId+"]/div/div/div/div/div/label/span[contains(text(),'"+ answer +"')]";
		WebElement tempEle = driver.findElement(By.xpath(xpath));
		return tempEle;
	}
	
	public WebElement getQuizQuestionOrder(int questionId) {
		WebElement tempEle = driver.findElement(By.xpath("//ul[@class='radio_wrapper']/li["+questionId+"]"));
		return tempEle;
	}
	
	public WebElement getQuizQuestion(String testId) {
		String xpath="//div[@data-testid='question_" + testId +"']/div/label";
		WebElement tempEle = driver.findElement(By.xpath(xpath));
		return tempEle;
	}
	
	public List<WebElement> getQuizQuestionOptions() {
		String xpath="//div[@class='el-radio-group']/label/span";
		List<WebElement> tempList = null;
		tempList = driver.findElements(By.xpath(xpath));
		return tempList;
	}
	
	public List<WebElement> getQuizQuestions() {
		List<WebElement> tempList = null;
		tempList = driver.findElements(By.xpath("//div[@class='label']"));
		return tempList;
	}
	
}
