package vantagecrm;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import clientBase.cpLogIn;
import clientBase.liveAcctHome;
import io.github.bonigarcia.wdm.WebDriverManager; 
 
import ru.yandex.qatools.ashot.AShot;  
import ru.yandex.qatools.ashot.Screenshot;  
import ru.yandex.qatools.ashot.shooting.ShootingStrategies; 

public class CPTestTranslation {
 
	
	WebDriver driver;
	WebDriverWait wait01;	
	WebDriverWait wait50;
	WebDriverWait wait03;
	
	//different wait time among different environments. Test environment will use 1 while beta & production will use 2. 
	//Initialized in LaunchBrowser function.
	
	int waitIndex=1;
	int dateIndex=0;
	  
	liveAcctHome acctHomeCls;
	
	//private String dataPath;
	private String brand;
	ArrayList<String> vtKeywords;
	//Launch driver
	@BeforeClass(alwaysRun=true)
	@Parameters(value= {"TestEnv","headless"})
	public void LaunchBrowser(String TestEnv, @Optional("False") String headless,ITestContext context)
	{
		
    	WebDriverManager.chromedriver().setup();
    	driver = Utils.funcSetupDriver(driver, "chrome", headless);	  
		//driver.manage().window().maximize();		 
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		  
	  	wait50 = new WebDriverWait(driver, Duration.ofSeconds(50));
	  	wait01 = new WebDriverWait(driver, Duration.ofSeconds(10));
	  	wait03 = new WebDriverWait(driver, Duration.ofSeconds(30));
	  	//this.dataPath = datapath;
	  	utils.Listeners.TestListener.driver=driver;
  	    context.setAttribute("driver", driver);  
	}
	
	@AfterClass(alwaysRun=true)
	public void QuitDriver() {
		driver.quit();
	}
	@Test(priority=0)
	@Parameters(value= {"TraderURL","TraderName", "TraderPass", "Brand", "Language"})
	void CPLogIn(String TraderURL, String TraderName, String TraderPass, String Brand, String Language) throws Exception	
	{
		cpLogIn loginCls = new cpLogIn(driver,Brand);
		// Login CP
		driver.get(TraderURL);
		this.brand = Brand;
		Utils.funcLogInCP(driver, TraderName, TraderPass, Brand);
		
		//loginCls.getLanguageIcon().click();
		wait01.until(ExpectedConditions.elementToBeClickable(loginCls.getLanguageIcon())).click();
		wait01.until(ExpectedConditions.elementToBeClickable(loginCls.getLanguageOption(Language))).click();
		//loginCls.getLanguageOption(Language).click();

	}
	
	@Test(alwaysRun = true)
	@Parameters(value = {"TraderURL"})
	public void testAUCPTranslation(String TraderURL) throws Exception {
		
		String dataPath = "//src//main//resources//vantagecrm//Data//Translation//test_cp.js";
		funcTestTranslation(TraderURL, dataPath,true);
		
	}
	
	@Test(alwaysRun = true)
	@Parameters(value = {"IBURL"})
	public void testAUIPTranslation(String TraderURL) throws Exception {
		
		String dataPath = "//src//main//resources//vantagecrm//Data//Translation//index_vfx_ip.js";
		//TraderURL = TraderURL.replace( "secure", "ibportal" );
		
		switchToIBportal(driver);
		funcTestTranslation(TraderURL, dataPath,false);
		
	}
	
	@Test(alwaysRun = true)
	@Parameters(value = {"TraderURL"})
	public void testVTCPTranslation(String TraderURL) throws Exception {
		
		String dataPath = "//src//main//resources//vantagecrm//Data//Translation//index_vt_cp.js";
		funcTestTranslation(TraderURL, dataPath,true);
		
	}
	
	@Test(alwaysRun = true)
	@Parameters(value = {"IBURL"})
	public void testVTIPTranslation(String TraderURL) throws Exception {
		
		String dataPath = "//src//main//resources//vantagecrm//Data//Translation//index_vt_ip.js";
		//TraderURL = TraderURL.replace( "secure", "ibportal" );
		
		switchToIBportal(driver);
		funcTestTranslation(TraderURL, dataPath,false);
		
	}
	
	@Test(alwaysRun = true)
	@Parameters(value = {"TraderURL"})
	public void testPUGCPTranslation(String TraderURL) throws Exception {
		
		String dataPath = "//src//main//resources//vantagecrm//Data//Translation//index_pug_cp.js";
		funcTestTranslation(TraderURL, dataPath,true);
		
	}
	
	@Test(alwaysRun = true)
	@Parameters(value = {"IBURL"})
	public void testPUGIPTranslation(String TraderURL) throws Exception {
		
		String dataPath = "//src//main//resources//vantagecrm//Data//Translation//index_pug_ip.js";
		//TraderURL = TraderURL.replace( "secure", "ibportal" );
		
		switchToIBportal(driver);
		Thread.sleep(1000);
		funcTestTranslation(TraderURL, dataPath,false);
		
	}
	
	@Test(alwaysRun = true)
	@Parameters(value = {"TraderURL"})
	public void testPUGCPSensitiveWords(String TraderURL) throws Exception {
		
		String dataPath = "//src//main//resources//vantagecrm//Data//Translation//index_pug_cp.js";
		funcSearchKeywords(TraderURL, dataPath,true);
		
	}
	
	@Test(alwaysRun = true)
	@Parameters(value = {"IBURL"})
	public void testPUGIPSensitiveWords(String TraderURL) throws Exception {
		String dataPath = "//src//main//resources//vantagecrm//Data//Translation//index_pug_ip.js";
		switchToIBportal(driver);
		Thread.sleep(1000);
		funcSearchKeywords(TraderURL, dataPath,false);
		
	}
	
	@Test(alwaysRun = true)
	@Parameters(value = {"TraderURL"})
	public void testVFXCPSensitiveWords(String TraderURL) throws Exception {
		
		String dataPath = "//src//main//resources//vantagecrm//Data//Translation//index_vfx_cp.js";
		funcSearchKeywords(TraderURL, dataPath,true);
		
	}
	
	@Test(alwaysRun = true)
	@Parameters(value = {"IBURL"})
	public void testVFXIPSensitiveWords(String TraderURL) throws Exception {
		String dataPath = "//src//main//resources//vantagecrm//Data//Translation//index_vfx_ip.js";
		switchToIBportal(driver);
		Thread.sleep(1000);
		funcSearchKeywords(TraderURL, dataPath,false);
		
	}
	
	public void funcTestTranslation(String TraderURL, String dataPath,Boolean cp) throws Exception {
		Scanner inputFile;
		String projectPath = Utils.workingDir;
		String url = "", imgPath = "",absolutePathName="";
		List<String> result = new ArrayList<>();
		//int i = 1;

		//Going to read CP url from index.js file.
		inputFile = new Scanner(new File(projectPath + dataPath));
		//inputFile = new Scanner(new File(dataPath));
		inputFile.useDelimiter("[\\r]");
		
		
		while (inputFile.hasNext()) {
			String line = inputFile.next();
			if (line.contains("path:")) {
				
				int start = line.indexOf("'");
				line = line.substring(start+1, line.indexOf("'", start+1));
				
				/* Only keep those absolute path longer than 2. 
				 */
				if (line.length() > 2) {
					if(line.contains("resetProfilePassword") || line.contains("resetPassword") || line.contains("login/to_login") || line.contains("login")) {
						continue;
					}
					line = line.replace(":id", "1");
					result.add(line);
				}
			}
		}
		
		for (String a: result) {
			/* Need to use a.substring(1, a.length()) to
			 * remove the / at the beginning of each absolute path
			 */
			if (a.contains("logout")) {
				continue; //For real test
				//break; // For debugging
			}
			
			url = Utils.ParseInputURL(TraderURL) + a.substring(1, a.length());
			System.out.println("Going to " + url);
			driver.get(url);
			Thread.sleep(1000);
			
			//下边两条代码是调整高度，使ashot拿到的正确的页面高度
			JavascriptExecutor je = (JavascriptExecutor) driver; 
			String jsstr = "document.getElementsByClassName('wrapper')[0].style.height = 'auto';";
			je.executeScript(jsstr);
			
			//Get absolute path name for naming the screenshot
			absolutePathName = a.replaceAll("/","");
			
			//Name the screenshot with absolute path
			//imgPath = takeScreenShot(driver, absolutePathName);
			imgPath = takeFullPageScreenShot(driver, absolutePathName);
			
			//Log Screenshot and url to the ExtentReports
			utils.Listeners.CustomListener.reportLog("Screenshot for " + url, imgPath);  
			

		   /* Going to click all the buttons on the page
		 	* and take screenshot one by one
			*/
			clickButtonAndTakeScreenShot(driver, absolutePathName,cp);
						
		}

	}

	//Legacy method
	//Take screenshot and store to ../autotest/ScreenShot directory
	public  String takeScreenShot(WebDriver driver, String pathName) throws IOException{
        SimpleDateFormat smf = new SimpleDateFormat("MMddHHmmss") ;
        String curTime = smf.format(new java.util.Date());
        String fileName = pathName+"_"+curTime+".png";
        
        String currentPath = System.getProperty("user.dir");
        
        
        
        String imgfile = currentPath+"\\ScreenShot\\"+fileName;
        
        driver.manage().window().fullscreen();
        
        File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        //Copy screenshot to ScreenShot folder       
        FileUtils.copyFile(srcFile, new File(imgfile));
        
        return imgfile;
    }

	
	public String takeFullPageScreenShot(WebDriver driver, String pathName) throws IOException {
        SimpleDateFormat smf = new SimpleDateFormat("MMddHHmmss") ;
        String curTime = smf.format(new java.util.Date());
        String fileName = pathName+"_"+curTime+".png";
        
        String currentPath = System.getProperty("user.dir");
      //check if the folder exist
        File folder = new File(currentPath+"\\ExtentReports\\ScreenShot");
        if(!folder.exists() ) {
        	folder.mkdirs();
        	System.out.println("create folder");
        }else {
        	//System.out.println(currentPath+"\\ExtentReports\\ScreenShot" +" exist.");
        }
        
        
        String htmlImgPath = "\\ScreenShot\\"+fileName;
        String imgfile = currentPath+"\\ExtentReports"+htmlImgPath;
        System.out.println(imgfile);
        //driver.manage().window().fullscreen();
		
        Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);
        //Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(ShootingStrategies.scaling(2),100)).takeScreenshot(driver);
        //Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(driver);
        
		try {
		  ImageIO.write(screenshot.getImage(), "PNG" , new File(imgfile));
		} catch (IOException e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
		}
		 

        return "."+ htmlImgPath;
	    
	}
	
	private List<WebElement> getButtons(Boolean cp){
		List<WebElement> buttons;
		List<WebElement> imgs;
		List<WebElement> lis;
		if(cp) {
			
			buttons = driver.findElements(By.xpath("//main[@id='elMain']/div[2]//button"));
			imgs = driver.findElements(By.xpath("//main[@id='elMain']/div[2]//img"));
			lis = driver.findElements(By.xpath("//main/div[2]//li[@data-testid!='']"));
			System.out.println(imgs.size());
			Set<String> srcs = new HashSet<>();
			buttons.removeIf(button->{
				if(button.getAttribute("class").equals("el-dialog__headerbtn")) {
					return true;
				}else {
					return false;
				}
			});
			
			
			
			if(imgs!=null && imgs.size() > 0) {
				
				imgs.removeIf(element->{
				
					if(element.getAttribute("class").equals("closeImg")) {
						return true;
					}
				
				if(srcs.contains(element.getAttribute("src"))) {
					return true;
				}else {
					srcs.add(element.getAttribute("src"));
					return false;
				}	
				});
				System.out.println(imgs.size());
				buttons.addAll(imgs);
				
			}
			buttons.addAll(lis);
			System.out.println("lis: "+ lis.size());
			
		}else {
			buttons=driver.findElements(By.xpath("//button"));
		}
		
		return buttons;
	}
	
	//Find all buttons on the page and click
	/***
	 * 
	 * @param driver
	 * @param absolutePathName
	 * @param cp  pages are different between cp and ib portal
	 * @throws IOException
	 */
	public void clickButtonAndTakeScreenShot(WebDriver driver, String absolutePathName, Boolean cp) throws IOException{
		String imgPath="", handle="";
		String currentURL = driver.getCurrentUrl();
		Set<String> handleS;
		List<WebElement> buttons;
		
		buttons = getButtons(cp);
		
		
		if(buttons.size() != 0){			
			System.out.println("Button appears! number = "+buttons.size());
			String source="";
			for (int i = 0; i <buttons.size(); i++) {

				Boolean newTabFlag = false;
				
				try {
					//Get current 
					handle = driver.getWindowHandle();
					System.out.println("Button "+i+" class: "+ buttons.get(i).getAttribute("class"));
					source = driver.getPageSource();
					//Not click arrow buttons in banner and header buttons
					if (!buttons.get(i).getAttribute("class").contains("el-carousel__arrow") && 
							!buttons.get(i).getAttribute("class").contains("el-dialog__headerbtn")) {
						buttons.get(i).click();
						Thread.sleep(1000);
					}
					
						
					/*
					 * imgPath = takeFullPageScreenShot(driver,
					 * absolutePathName);
					 * utils.Listeners.CustomListener.
					 * reportLog("Screenshot after clicking button " + i +
					 * " on " + absolutePathName, imgPath);
					 */
					
					handleS = driver.getWindowHandles();

					for (String s : handleS) {

						//Close new tab if new tab has been opened
						//Won't screenshot
						if (!s.equals(handle)) {

							driver.switchTo().window(s).close();
							driver.switchTo().window(handle);
							newTabFlag = true;
						}
					}
					if (!driver.getCurrentUrl().equals(currentURL)) {
						//Navigate back if button clicking makes url change.
					    //Won't screenshot
						System.out.println("Go to url: " + driver.getCurrentUrl());
						System.out.println("@@@@@@@@@\n Going to navigate back! " + currentURL +"\n@@@@@@@@@");
						driver.get(currentURL);
						Thread.sleep(1000);
						//need get all buttons again otherwise buttons do not attached on the new page.
						buttons = getButtons(cp);
						source = driver.getPageSource();
						
					}else {
						//Only take screenshot when no new tab opened after button clicking.
						if(newTabFlag == false) {
							String newSource = driver.getPageSource();
							
							//get a modal page
							if(!source.equals(newSource)) {
								Thread.sleep(1000);
								imgPath = takeFullPageScreenShot(driver, absolutePathName);
								utils.Listeners.CustomListener.reportLog("Screenshot after clicking button " + i + " on " + absolutePathName, imgPath);
								
								
								
								List<WebElement> close = driver.findElements(By.xpath("//img[@class='closeImg']"));
								for(WebElement e:close) {
									closeModal(e);
								}
								
							}
						}
					}

					Thread.sleep(1000);
					
				}catch(Exception e) {
					System.out.println("Button "+ i +" got exception: \n" + e.getMessage());
				}
			}
		}
		
    }
	
	public void click(WebElement e) {
		try {
			e.findElement(By.className("closeImg")).click();
		}catch(Exception err) {
			System.out.println("On click method: " + err.getMessage());
		}
	}
	
	public void closeModal(WebElement e) {
		JavascriptExecutor je = (JavascriptExecutor) driver;
		String js = "arguments[0].click()";
		try {
			je.executeScript(js, e);
		}catch(Exception err){
			System.out.println("onCloseModal "+err.getMessage());
		}
	}
	
	public void switchToIBportal(WebDriver driver) throws Exception {
		if (driver.getTitle().contains("Client Portal"))
		{
			
			Utils.waitUntilLoaded(driver);
			wait50.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.login_inner.el-dropdown-selfdefine"))).click();
			Thread.sleep(500);
			//Switch to Client Portal
			driver.findElement(By.cssSelector(".login_back > span")).click();
			wait50.until(ExpectedConditions.titleContains("IB"));
		}
	}
	
	
	public void funcSearchKeywords(String TraderURL, String dataPath,Boolean cp) throws Exception {
		Scanner inputFile;
		String projectPath = Utils.workingDir;
		String url = "", imgPath = "",absolutePathName="";
		List<String> result = new ArrayList<>();
		//int i = 1;

		//Going to read CP url from index.js file.
		inputFile = new Scanner(new File(projectPath + dataPath));
		
		inputFile.useDelimiter("[\\r]");
		
		
		while (inputFile.hasNext()) {
			String line = inputFile.next();
			if (line.contains("path:")) {
				
				int start = line.indexOf("'");
				line = line.substring(start+1, line.indexOf("'", start+1));
				
				/* Only keep those absolute path longer than 2. 
				 */
				if (line.length() > 2) {
					if(line.contains("resetProfilePassword") || line.contains("resetPassword") || line.contains("login/to_login") || line.contains("login")) {
						continue;
					}
					line = line.replace(":id", "1");
					result.add(line);
				}
			}
		}
		vtKeywords = new ArrayList<>();
		String currentPath = System.getProperty("user.dir");
	    currentPath = currentPath+"\\src\\main\\resources\\vantagecrm\\Data\\Translation\\";
		
	    switch(this.brand.toLowerCase()) {
	    case "fsa":
	    case "svg": currentPath += "WordsNotInPUG.txt"; break;
	    case "vt": currentPath += "WordsNotInVT.txt"; break;
	    case "vfsc":
	    case "vfsc2":
	    case "fca":
	    case "au": currentPath += "WordsNotInVFX.txt"; break;
	    }
	    Scanner input = new Scanner(new File(currentPath));
	    while(input.hasNext()) {
	    	vtKeywords.add(input.nextLine().trim().toLowerCase());
	    }
		input.close();
		for (String a: result) {
			/* Need to use a.substring(1, a.length()) to
			 * remove the / at the beginning of each absolute path
			 */
			if (a.contains("logout")) {
				continue; //For real test
				//break; // For debugging
			}
			
			url = Utils.ParseInputURL(TraderURL) + a.substring(1, a.length());
			System.out.println("Going to " + url);
			driver.get(url);
			Thread.sleep(1000);
			
			//Search the page of Change Portal Password
			SearchKeyWord(driver.getCurrentUrl());
						
		}

	}
	
	void SearchKeyWord(String strURL) throws Exception	
	{
 		
	
		/*String[] vtKeywords = new String[] {"万致", "VFX", "Vantage", "VantageFX", "Vantage FX", "1300945517", "31 Market街 悉尼,新南威尔士 2000, 澳大利亚",
				"+61 2 8999 2044", "VT Markets", "VT", "400-880-7900", "31 Market Street Sydney NSW 2000 Australia", "Vantage Global Prime", "AFSL 428901",
				"info@vtmarkets.com.cn", "4th Floor The Harbour Centre, 42 N Church St", "1157 2701", "1383491", "ASIC", "CIMA", 
				"info@vtmarkets.com", "info@vtmarkets.com.au","FRN: 590299", "+61 2 8999 2044", "support@vantagefx.com.au", "Vanuatu"};*/
		
		
		ArrayList<String> webWords = new ArrayList<>();
		List<WebElement> els = driver.findElements(By.xpath("//*[text()!='']"));
		
		els.addAll(driver.findElements(By.xpath("//*[@value!='']")));
		
		//strs.addAll(driver.findElements(By.xpath("//@value")));
		int pos = 0;
		
		if(els.size() > 0) {
			
			for(WebElement e: els) {
				try {
					String tmpstr = e.getAttribute("textContent").trim().toLowerCase();
					//System.out.println("Find Element: "+e.getTagName());
					//System.out.println("text is: " + tmpstr);
					//System.out.println("****************************");
				if(tmpstr.length()> 0) {
					webWords.add(tmpstr);
					
				}else if(e.getAttribute("value")!=null && e.getAttribute("value").trim().length()>0) {
					webWords.add(e.getAttribute("value").toLowerCase().trim());
				}
				
				pos ++;
				} catch(org.openqa.selenium.StaleElementReferenceException ex) {
					System.out.println("ERROR: element is not attached to the page document, index is: "+ pos);
				}
			}
		}
		
		Thread.sleep(2000);
		if(driver.getCurrentUrl().toLowerCase().contains("rebatetransfer")) {
			System.out.println("elements size: " + els.size());
			System.out.println("text size: " + webWords.size());
		}
    	System.out.println("URL: " + driver.getCurrentUrl());
		
       for(String strKey: vtKeywords)
       {
	       
           int count = 0;
           
        	   for(String webStr: webWords) {
            	   if(webStr.contains(strKey)) {
            		   count++;
            	   }
               }
        	   if(count>0) {
        	   System.out.println("Total occurrences of " + strKey +": " + count);
        	   }
       }
	}
}
