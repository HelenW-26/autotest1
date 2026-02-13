package newcrm.testcases.cptestcases;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import newcrm.business.businessbase.CPMenu;
import newcrm.global.GlobalProperties.CPMenuName;
import newcrm.testcases.BaseTestCase;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import vantagecrm.Utils;

public class CPTestTranslation extends BaseTestCase {

	private CPMenu menu;
	ArrayList<String> vtKeywords;
	HashMap<String,String> dataPath = new HashMap<>();
	String currentPath="";
	
	@Override
	@BeforeTest(alwaysRun = true)
	@Parameters(value= {"TestEnv","headless","Brand","Regulator","TraderURL", "TraderName", "TraderPass","AdminURL","AdminName","AdminPass","Debug"})
	public void launchBrowser(@Optional("alpha")String TestEnv, @Optional("False") String headless, String Brand, String Regulator, 
			@Optional("")String TraderURL, @Optional("")String TraderName, @Optional("")String TraderPass,
			@Optional("")String AdminURL, @Optional("")String AdminName, @Optional("")String AdminPass, @Optional("True")String Debug,
				              ITestContext context) {
		super.launchBrowser(TestEnv, headless, Brand, Regulator, TraderURL, TraderName, TraderPass, AdminURL, AdminName, AdminPass, Debug, context);
		
		menu = myfactor.newInstance(CPMenu.class);
		utils.Listeners.TestListener.driver=driver;
		
		//check if the folder exist
		currentPath = System.getProperty("user.dir"); 
        File folder = new File(currentPath+"\\ExtentReports\\ScreenShot");
        if(!folder.exists() ) {
        	folder.mkdirs();
        	System.out.println("create folder");
        }else {
        	System.out.println(currentPath+"\\ExtentReports\\ScreenShot" +" exist.");
        }
		
		
		//data path
		//cp
		dataPath.put("vfx", "//src//main//resources//vantagecrm//Data//Translation//index_vfx_cp.js");
		dataPath.put("pug", "//src//main//resources//vantagecrm//Data//Translation//index_pug_cp.js");
		dataPath.put("vt", "//src//main//resources//vantagecrm//Data//Translation//index_vt_cp.js");
		dataPath.put("mo", "//src//main//resources//vantagecrm//Data//Translation//index_mo_cp.js");
		//ib
		dataPath.put("ib_vfx", "//src//main//resources//vantagecrm//Data//Translation//index_vfx_ip.js");
		dataPath.put("ib_pug", "//src//main//resources//vantagecrm//Data//Translation//index_pug_ip.js");
		dataPath.put("ib_vt", "//src//main//resources//vantagecrm//Data//Translation//index_vt_ip.js");
		dataPath.put("ib_mo", "//src//main//resources//vantagecrm//Data//Translation//index_mo_ip.js");
	}
	
	@Test(alwaysRun = true)
	@Parameters(value = {"Language"})
	public void testCPTranslation(String language) throws Exception {
		menu.goToMenu(CPMenuName.CPPORTAL);
		menu.changeLanguage(language);
		funcTestTranslation(TraderURL, dataPath.get(this.Brand.toLowerCase()),true);
	}
	
	@Test(alwaysRun = true)
	@Parameters(value = {"Language","IBURL"})
	public void testIBTranslation(String language,String ibUrl) throws Exception {
		menu.goToMenu(CPMenuName.IBPORTAL);
		menu.changeLanguage(language);
		funcTestTranslation(ibUrl, dataPath.get("ib_"+this.Brand.toLowerCase()),false);
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
			menu.waitLoading();
			
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
						menu.waitLoading();
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
						menu.waitLoading();
						//need get all buttons again otherwise buttons do not attached on the new page.
						buttons = getButtons(cp);
						source = driver.getPageSource();
						
					}else {
						//Only take screenshot when no new tab opened after button clicking.
						if(newTabFlag == false) {
							String newSource = driver.getPageSource();
							
							//get a modal page
							if(!source.equals(newSource)) {
								menu.waitLoading();
								imgPath = takeFullPageScreenShot(driver, absolutePathName);
								utils.Listeners.CustomListener.reportLog("Screenshot after clicking button " + i + " on " + absolutePathName, imgPath);
								
								
								
								List<WebElement> close = driver.findElements(By.xpath("//img[@class='closeImg']"));
								for(WebElement e:close) {
									closeModal(e);
								}
								
							}
						}
					}

					menu.waitLoading();
					
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
		
	}
	
	
	public void funcSearchKeywords(String TraderURL, String dataPath,Boolean cp) throws Exception {
		Scanner inputFile;
		String projectPath = Utils.workingDir;
		String url = "";
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
	    currentPath = currentPath+"\\src\\main\\resources\\vantagecrm\\Data\\Translation\\";
		
	    //need to change
	    switch(this.Brand.toLowerCase()) {
	    case "pug": currentPath += "WordsNotInPUG.txt"; break;
	    case "vt": currentPath += "WordsNotInVT.txt"; break;
	    case "vfx": currentPath += "WordsNotInVFX.txt"; break;
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
			menu.waitLoading();
			
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
		
		menu.waitLoading();
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
