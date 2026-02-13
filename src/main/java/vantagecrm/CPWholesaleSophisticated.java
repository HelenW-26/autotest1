package vantagecrm;

import static org.testng.Assert.assertTrue;


import java.time.Duration;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import clientBase.cpLogIn;
import clientBase.wholesale;

public class CPWholesaleSophisticated {
	
	WebDriver driver;
	WebDriverWait wait03;
	WebDriverWait wait15;
	
	wholesale ws;
	JavascriptExecutor executor;
	String userId;
	String testEnv;
	String brand;
	String[] quizAnswerKeys;
	
	HashMap<String,String> questions;
	
	HashMap<String,String> answers;
	
	// Launch driver
	@BeforeClass(alwaysRun = true)
	@Parameters(value= {"headless"})
	public void LaunchBrowser( @Optional("False") String headless, ITestContext context)
	{
		driver = Utils.funcSetupDriver(driver, "chrome", headless);
		
		context.setAttribute("driver", driver);
		wait03 = new WebDriverWait(driver, Duration.ofSeconds(3));
		wait15 = new WebDriverWait(driver, Duration.ofSeconds(15));
		ws = new wholesale (driver);
		executor = (JavascriptExecutor)driver;
		
		questions = new HashMap <String,String>();
		
		answers = new HashMap <String,String>();
	}
	
	@Test(priority = 0)
	@Parameters(value = { "TraderURL", "TraderName", "TraderPass", "Brand" })
	void CPLogIn(String TraderURL, String TraderName, String TraderPass, String Brand) throws Exception

	{
		driver.get(TraderURL);
		cpLogIn login = new cpLogIn(driver,Brand);

		wait15.until(ExpectedConditions.visibilityOf(login.getInputPwd()));
		Utils.funcLogInCP(driver, TraderName, TraderPass, Brand);
		wait15.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'HOME')]")));
		
		
	}
	
	@Test(invocationCount=1)
	@Parameters(value= {"TraderName","TestEnv","Brand"})
	void SophisticatedTest(String TraderName,String TestEnv,String Brand) throws Exception {
		testEnv=TestEnv;
		brand=Brand;
		String fName = TraderName.split("@")[0].toString();
		String proStatus = "";
		
		try {
		ws.getUpgrade2ProLnk().click();
		} catch (Exception ex) {
			System.out.println("NO 'Upgrade To Pro' link found in the left menu. Please check Status = Approved and Classification Statue = Retail on Client page in Admin");
			
			assertTrue(false);
		}

		proStatus = funcGetProStatus(fName);
		if (proStatus==null || proStatus.isEmpty()) {
			proStatus = "1";
		}
		
		switch (proStatus) {
		case "1":
			funcPg1();
			funcPg2();
			funcPg3();
			funcPg4();
			funcPg5Quiz();
			funcPg6UploadFile();
			funcBack2Home();
			break;
			
		case "2":
			funcPg2();
			funcPg3();
			funcPg4();
			funcPg5Quiz();
			funcPg6UploadFile();
			funcBack2Home();
			break;
			
		case "3":
			funcPg3();
			funcPg4();
			funcPg5Quiz();
			funcPg6UploadFile();
			funcBack2Home();
			break;
			
		case "4":
			funcPg4();
			funcPg5Quiz();	
			funcPg6UploadFile();
			funcBack2Home();
			break;
			
		case "5":
			funcPg5Quiz();
			funcPg6UploadFile();
			funcBack2Home();

			break;
			
		case "6":
			funcPg6UploadFile();
			funcBack2Home();

			break;
			
		case "7":
			System.out.println("The upgrade to pro request has been submitted");
			assertTrue(false);
			break;
			
		default:
			System.out.println("Not sophisticated request, please run wealth test case instead");
			break;
		}
		
		
	}
	
	String funcGetProStatus(String realName) throws Exception {
		String proStatus = "";
		userId = DBUtils.funcReadUserInBusinessDB(realName, testEnv, brand);
		proStatus = DBUtils.funcReadUserProStatusInGlobalDB(userId, testEnv, brand);
		Thread.sleep(1000);
		
		return proStatus;
	}
	
	void funcCheckProStatus() throws Exception {
		DBUtils.funcReadUserProStatusInGlobalDB(userId, testEnv, brand);
		Thread.sleep(1000);
	}
	
	void funcPg1() throws Exception
	{
		
		executor.executeScript("arguments[0].click();", ws.getUpgradeCheckbox());

		ws.getUpgradeButton().click();
		
		Thread.sleep(2000);
		funcCheckProStatus();
	}
	
	void funcPg2()  throws Exception
	{
		executor.executeScript("arguments[0].click();", ws.get1stQuestionOption50k());
		
		executor.executeScript("arguments[0].click();", ws.get3rdQuestionOptionNo());
		
		ws.getNextButton().click();
		
		Thread.sleep(2000);
		funcCheckProStatus();
	}
	
	void funcPg3()  throws Exception
	{
		executor.executeScript("arguments[0].click();", ws.get3rdPg1stQuestionOptionYes());
		
		executor.executeScript("arguments[0].click();", ws.get3rdPg2ndQuestionOptionYes());
		
		ws.getNextButton().click();
		
		Thread.sleep(2000);
		funcCheckProStatus();
	}
	
	void funcPg4()  throws Exception
	{
		executor.executeScript("arguments[0].click();", ws.getDeclarationCheckbox());
		
		ws.getNextButton().click();
		
		Thread.sleep(2000);
		funcCheckProStatus();
	}
	

	void funcPg5Quiz()  throws Exception
	{
		executor.executeScript("arguments[0].click();", ws.getQuizButton());
		Thread.sleep(1000);
		
		funcFailedQuiz();
		ws.getNextButton().click();
		Thread.sleep(1000);
		System.out.println("Failed quiz and try again.");
		ws.getResitQuizButton().click();
		Thread.sleep(2000);
		/*
		funcFailedQuiz();
		ws.getNextButton().click();
		Thread.sleep(1000);
		System.out.println("Failed quiz and try again.");
		ws.getResitQuizButton().click();
		Thread.sleep(2000);
		*/

		funcPassQuiz();
		ws.getNextButton().click();
		Thread.sleep(2000);
		
		funcCheckProStatus();
	}
	
	
	void funcPg6UploadFile()  throws Exception
	{
		ws.chooseUploadedFile();

		wait15.until(ExpectedConditions.elementToBeClickable(ws.getUploadButton()));
		executor.executeScript("arguments[0].click();", ws.getUploadButton());
		
		Thread.sleep(2000);
		funcCheckProStatus();
	}
	
	void funcBack2Home() throws Exception
	{
		wait15.until(ExpectedConditions.elementToBeClickable(ws.getBackButton()));
		executor.executeScript("arguments[0].click();", ws.getBackButton());
		
		wait15.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'HOME')]")));
	}
	
	void funcGetQuizAnwsers() throws Exception {
		String selectSql1="select p_key from tb_question_solution s,tb_question_option o where s.question_id=o.question_id and s.solution_option_id = o.id and p_key like '%wholesale.sophisticated.v2.q%' and o.is_del=0;";
		
		String db = Utils.getDBName(brand)[1];
		String resultAnswerKey = DBUtils.funcReadDBReturnAll(db,selectSql1, testEnv);
		String originalAnswerKeys=resultAnswerKey;
		resultAnswerKey = resultAnswerKey.replace("p_key: ", "'");
		resultAnswerKey = resultAnswerKey.replace(",", "',");
		resultAnswerKey = resultAnswerKey.substring(1,resultAnswerKey.length()-1);
		
		originalAnswerKeys = originalAnswerKeys.replace("p_key: ", "");
		originalAnswerKeys = originalAnswerKeys.substring(1,originalAnswerKeys.length()-1);
		String[] quizAnswerKeys=originalAnswerKeys.split(",");
		System.out.println(resultAnswerKey);
		
		String selectSql2="select p_value from tb_config_prop where p_key in (" + resultAnswerKey +"');";
		System.out.println(selectSql2);
		db = Utils.getDBName(brand)[0];
		String resulAnswer = DBUtils.funcReadDBReturnAll(db,selectSql2, testEnv);
		resulAnswer = resulAnswer.substring(10,resulAnswer.length()-1);
		String [] quizAnswers = resulAnswer.split(", p_value: ");
		System.out.println(resulAnswer);
		
		for (int i=0; i<quizAnswerKeys.length;i++)
		{
			answers.put(quizAnswerKeys[i].trim(), quizAnswers[i].trim());
		}
		
	}
	
	void funcGetQuizQuestions() throws Exception {
		
		String selectSql1="select p_key from tb_question where category = 'SOPHISTICATED_INVESTOR_QUIZ' and is_del=0;";
		
		String db = Utils.getDBName(brand)[1];
		String resultQuestionKeys = DBUtils.funcReadDBReturnAll(db,selectSql1, testEnv);
		String originalQuestionKeys=resultQuestionKeys;
		resultQuestionKeys = resultQuestionKeys.replace("p_key: ", "'");
		resultQuestionKeys = resultQuestionKeys.replace(",", "',");
		resultQuestionKeys = resultQuestionKeys.substring(1,resultQuestionKeys.length()-1);
		
		originalQuestionKeys = originalQuestionKeys.replace("p_key: ", "");
		originalQuestionKeys = originalQuestionKeys.substring(1,originalQuestionKeys.length()-1);
		String[] quizQuestionKeys=originalQuestionKeys.split(",");
		
		String selectSql2="select p_value from tb_config_prop where p_key in (" + resultQuestionKeys +"');";
		db = Utils.getDBName(brand)[0];
		String resultQuestion = DBUtils.funcReadDBReturnAll(db,selectSql2, testEnv);
		resultQuestion = resultQuestion.substring(10,resultQuestion.length()-1);
		String [] quizQuestions = resultQuestion.split(", p_value: ");
		System.out.println(resultQuestion);
		
		for (int i=0; i<quizQuestionKeys.length;i++)
		{
			questions.put(quizQuestions[i].trim(),quizQuestionKeys[i].trim());
		}
			
	}
	
	void funcPassQuiz() throws Exception {
		String txtAnswer="";
		int questionOrder;
		funcGetQuizQuestions();
		funcGetQuizAnwsers();
				
		List <WebElement> lstQuestion = ws.getQuizQuestions();
		System.out.println("------------------Questions & Answers-------------------");
		for (int i=0;i<lstQuestion.size();i++) {
			questionOrder=i+1;
			System.out.println("Question " + questionOrder + ": " +lstQuestion.get(i).getText().trim());

			for (String question : questions.keySet()) {
				if (lstQuestion.get(i).getText().trim().contains(question))
				{
					for(String answer:answers.keySet())
					{
						if (answer.contains(questions.get(question))) {
							if (answers.get(answer).contains("'")) {
								txtAnswer=answers.get(answer).split("'")[0];
							} else if (question.contains("What is Forex leverage?")) {
								txtAnswer="The use of borrowed capital, allowing a Forex trader to gain access to larger exposure";
							}
							else {
								txtAnswer=answers.get(answer);
							}
							wait15.until(ExpectedConditions.elementToBeClickable(ws.getQuizAnswer(questionOrder, txtAnswer)));
							executor.executeScript("arguments[0].click();", ws.getQuizAnswer(questionOrder, txtAnswer));
							System.out.println("Select answer: " + txtAnswer);
							//Thread.sleep(6000);
							break;
						}
					}
				}
			}
		}

	}

	void funcFailedQuiz() throws Exception {
		List<WebElement> quizOptions = ws.getQuizQuestionOptions();
		for (int i=0; i<quizOptions.size(); i++){
			executor.executeScript("arguments[0].click();", quizOptions.get(i));
		}
		
	}

	//@AfterClass(alwaysRun=true)
	@Parameters(value="Brand")
	void ExitBrowser(String Brand) throws Exception
	{
		Utils.funcLogOutCP(driver, Brand);
		//Close all browsers
		driver.quit();
	}

}
