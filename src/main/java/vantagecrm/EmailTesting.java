package vantagecrm;


import java.io.File;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ExtentReports.ExtentTestManager;
import vantagecrm.RestAPI.MyHttpRequestRetryHandler;
 
import org.testng.Assert;
import org.apache.http.client.HttpClient;  
import org.apache.http.client.config.RequestConfig;   
import org.apache.http.config.SocketConfig;  
import org.apache.http.impl.client.HttpClients;  
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;  

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


	public class EmailTesting{
		WebDriver driver;
		WebDriverWait wait01;
		WebDriverWait wait02;
		int waitIndex=1;
		String emailSuffix=Utils.emailSuffix;
		Select t;
		
	/*	String AdminURL = "http://devops_new.tianyitechs.com/auth/login";
		String AdminName = "shan.liu";
		String AdminPass = "F6P4HMCMSzqaVnTz";*/
		
		private static HttpClient httpClient;  
		 
	    private static final int MAX_CONNECTION = 100;  

	    private static final int MAX_CONCURRENT_CONNECTIONS = 100;  
	    //microsecond
	    private static final int CONNECTION_TIME_OUT = 80000;  
	    //microsecond
	    private static final int REQUEST_TIME_OUT = 80000;  
	 
	    private static final int MAX_FAIL_RETRY_COUNT = 3;
		private static final String UTF_8 = null;  

	    private static RequestConfig requestConfig;  
	  
	    static {  
	        SocketConfig socketConfig = SocketConfig.custom()  
	                .setSoTimeout(REQUEST_TIME_OUT).setSoKeepAlive(true)  
	                .setTcpNoDelay(true).build();  
	  
	        requestConfig = RequestConfig.custom()  
	                .setSocketTimeout(REQUEST_TIME_OUT)  
	                .setConnectTimeout(CONNECTION_TIME_OUT).build();  
	 
	        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();  
	        connManager.setMaxTotal(MAX_CONNECTION);  
	        connManager.setDefaultMaxPerRoute(MAX_CONCURRENT_CONNECTIONS);  
	        connManager.setDefaultSocketConfig(socketConfig);  
	  
	        httpClient = HttpClients.custom().setConnectionManager(connManager)  
	  
	                .setRetryHandler(new MyHttpRequestRetryHandler()).build();  
	    }  
		
	    //@Test
		public void getTianyitechsToken() throws Exception {
	    	 System.out.println("test starts");
	    	HttpEntity entity = null;
	    	String result="";
	        String url = "http://devops_new.tianyitechs.com/api/cmdb/user/login/";  
	        System.out.println("usrl is: "+url);
	        
	        URIBuilder uriBuilder = new URIBuilder(url);  
	        HttpPost httpPost = new HttpPost(uriBuilder.build());  
	        
	        //Set request header
	        httpPost.addHeader("Content-Type", "application/json;charset=UTF-8"); 
	        httpPost.addHeader("Accept", "application/json"); 
	        
	        //Config request body
	        String content = "{\"username\":\"shan.liu\",\"password\":\"F6P4HMCMSzqaVnTz\",\"remember\":true}";
	        String charset = "UTF-8";
	        
	        StringEntity se = new StringEntity(content);
	        se.setContentEncoding(charset);
	        se.setContentType("application/json");
	        httpPost.setEntity(se);
	        
	        //httpPost.setConfig(requestConfig);
	        
	        System.out.println("content is: "+content);
	        HttpResponse response = httpClient.execute(httpPost);   
	        System.out.println("Executing request " + httpPost.getRequestLine());
	        int status = response.getStatusLine().getStatusCode();
	        System.out.println("status code is: "+status);
	        if (status >= 200 && status < 300) {
	            entity = response.getEntity();
	            result = EntityUtils.toString(entity);
	            System.out.println(result);
	        }
	        JSONParser parser = new JSONParser();
	        JSONObject json = (JSONObject) parser.parse(result);
	        String token = (String) json.get("token");
            System.out.println(token);

	    }
		
	   



		//@Test(priority=0)
		@Parameters(value= {"TraderURL", "TraderName", "TraderPass","Brand"})
		void TraderLogIn(String TraderURL, String TraderName, String TraderPass, String Brand) throws Exception
		
		{
	 		
	  	  //Login AU Trader
			driver.get(TraderURL);

			Utils.funcLogInTrader(driver, TraderName, TraderPass, Brand);	
			Thread.sleep(waitIndex*2000);
			
			Assert.assertTrue(driver.getTitle().equals("My Account")||driver.getTitle().equals("Live Accounts"));
			
			
		}

		

		
		//Launch driver
		@BeforeClass(alwaysRun=true)
		@Parameters(value="TestEnv")
		public void LaunchBrowser(String TestEnv, ITestContext context)  //Added one parameter ITestContext context to pass variables by Yanni on 5/15/2019
		{
			  
			  String browserName= "chrome";
			  System.out.println("Browser is: " + browserName);
			  System.out.println("Thread Id is: " + Thread.currentThread().getId());
			  
			  switch (browserName)
			  {
			  case "chrome":
			  
				  WebDriverManager.chromedriver().setup();
				  ChromeOptions options = new ChromeOptions();
				  options.addArguments("--no-sandbox");
				  options.setAcceptInsecureCerts(true);
				  options.setExperimentalOption("useAutomationExtension", false);
				  driver = new ChromeDriver(options);
				  break;
			  
				/*
				 * case "firefox":
				 * WebDriverManager.firefoxdriver().setup();
				 * driver = new FirefoxDriver();
				 * break;
				 * case "edge":
				 * WebDriverManager.edgedriver().setup();
				 * driver = new EdgeDriver();
				 * break;
				 * case "safari":
				 * driver = new SafariDriver();
				 * break;
				 */
			  }
			 
				/*
				 * ChromeOptions options=new ChromeOptions();
				 * options.setAcceptInsecureCerts(true);
				 * 
				 * System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
				 * driver = new ChromeDriver(options);
				 */
	    	  
	    	  utils.Listeners.TestListener.driver=driver;
	    	  context.setAttribute("driver", driver);      //Added by Yanni: to pass driver to Listener
	    	  
			  driver.manage().window().maximize();		 
			  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		
			  if(TestEnv.equalsIgnoreCase("beta") || TestEnv.equalsIgnoreCase("prod"))
			  {
				  waitIndex=2;
			  }
			  wait02=new WebDriverWait(driver, Duration.ofSeconds(20));
		}
		
		
		String funcTestStamp(String Brand) {
			  Date date = new Date();
		        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		        int month = localDate.getMonthValue();
		        int day   = localDate.getDayOfMonth();

		        String dayStr = String.valueOf(day);
		        String monthStr=String.valueOf(month);
			  
			  String testSubject = Brand+" Test"+ dayStr + monthStr;
			  return testSubject;
		}
		
		/* Developed by Alex Liu 
		 * For resending specified emails in DB. 
		 * 
		 */
		@Parameters({"AdminURL","AdminName","AdminPass", "Brand","TestEnv"})
		@Test(priority=0)
		public void APIResendEmail(String AdminURL, String AdminName, String AdminPass, String Brand, String TestEnv, Method method) throws Exception
		{
			
			  ExtentTestManager.startTest(method.getName(),"Description: Resend Email");
			  driver.get(AdminURL);
			  String cookie = Utils.getAdminCookie(driver, AdminName, AdminPass, Brand, TestEnv);
			  String testStamp=funcTestStamp(Brand);

			  //remove the admin/main from AdminURL
			  String url= Utils.ParseInputURL(AdminURL);
			  
			  //Email ID provided as array
			  /*
				 * String[] array =
				 * {"841627\",\"841628\",\"841629\",\"841630\",\"841631\",\"841632\",\"841633\",\"841634\",\"841635\",\"841636\",\"841637\",\"841638\",\"841639\",\"841640\",\"841641\",\"841642\",\"841643\",\"841644\",\"841645\",\"841646\",\"841647\",\"841649\",\"841650\",\"841651\",\"841652\",\"841653\",\"841654\",\"841655\",\"841656\",\"841657\",\"841658\",\"841659\",\"841660\",\"841661\",\"841662\",\"841663\",\"841664\",\"841665\",\"841666\",\"841667\",\"841668\",\"841669\",\"841677\",\"841678\",\"841679\",\"841680\",\"841684\",\"841693\",\"841694\",\"841695\",\"841696\",\"841697\",\"841698\",\"841710\",\"841711\",\"841712\",\"841713\",\"841714\",\"841715\",\"841716\",\"841717\",\"841728\",\"841729\",\"841730\",\"841733\",\"841734\",\"841735\",\"841761\",\"841762\",\"841763\",\"841774\",\"841775\",\"841776\",\"841779\",\"841780\",\"841781\",\"841782\",\"841783\",\"841784\",\"841785\",\"841786\",\"841787\",\"841788\",\"841789\",\"841790\",\"841791\",\"841792\",\"841793\",\"841794\",\"841795\",\"841796\",\"841797\",\"841798\",\"841799\",\"841811\",\"841851\",\"841852\",\"841853\",\"841854\",\"841855"
				 * };
				 * for (String eid : array) {
				 * 
				 * 
				 * System.out.println("---->>>Going to sendi emails matches id: "+eid);
				 * RestAPI.testPostResendMailSubmit(url, cookie, eid, "");
				 * 
				 * }
				 */			 

			// Email ID provided in Excel
			String projectPath = System.getProperty("user.dir");
			//System.out.println(projectPath);
			//read email Id from excel
			String dataPath;
			dataPath = "/src/main/resources/vantagecrm/Data/EmailID.xlsx";

			File file=new File(projectPath + dataPath);
			try {
				InputStream is = new FileInputStream(file);
				XSSFWorkbook wb = new XSSFWorkbook(is);
				DataFormatter df = new DataFormatter();

				XSSFSheet sheetEmailId = wb.getSheet("Sheet1");

				for(int i=1; i<=sheetEmailId.getLastRowNum(); i++) {

					String eid = df.formatCellValue(sheetEmailId.getRow(i).getCell(0));
					String etmp = df.formatCellValue(sheetEmailId.getRow(i).getCell(1));
					if (eid.length()!=0)
					{
						System.out.println("---->>>Going to sendi emails matches id: "+eid);
						RestAPI.testPostResendMailSubmit(url, cookie, eid,testStamp,etmp);
					}
					else
						break;
				}

				is.close();
				wb.close();

			}catch(FileNotFoundException e) {
				e.printStackTrace();
			}
			
		}


		/* Developed by Alex Liu 
		 * For getting email templates from excel file and resending the latest email in DB matched the template. 
		 * 
		 */
		@Parameters({"AdminURL","AdminName","AdminPass", "Brand","TestEnv"})
		@Test(priority=0)
		public void SQLGetMailIdByTemplate(String AdminURL, String AdminName, String AdminPass, String Brand, String TestEnv, Method method) throws Exception
		{
			String dbName,result,selectSql,eid;
			ExtentTestManager.startTest(method.getName(),"Description: Get email IDs by email templates");
			driver.get(AdminURL);
			String cookie = Utils.getAdminCookie(driver, AdminName, AdminPass, Brand, TestEnv);
			//remove the admin/main from AdminURL
			String url= Utils.ParseInputURL(AdminURL);
			
			
			 dbName= Utils.getDBName(Brand)[1];			
			
			//read email templates from excel
			File file=new File(Utils.workingDir + "/src/main/resources/vantagecrm/Data/Book1.xlsx");
			try {
				InputStream is = new FileInputStream(file);
				XSSFWorkbook wb = new XSSFWorkbook(is);
				DataFormatter df = new DataFormatter();
				XSSFSheet sheetEmailId = wb.getSheet("Sheet1");
				for(int i=0; i<=sheetEmailId.getLastRowNum(); i++) {
					
					//第几列，0为第一列
					String etmp = df.formatCellValue(sheetEmailId.getRow(i).getCell(0));
					
					if(!etmp.equals("")) {
						//Search tb_mail_send_log to get the latest email that matched the template
						//selectSql="select id from tb_mail_send_log where template_invoke_name like '%" + etmp +"%' and is_resend=0 order by create_time desc limit 1;";
						
						selectSql="select id from tb_mail_send_log A where template_invoke_name like '%" + etmp +"%' "
								+ "and CAST(A.create_time AS Datetime) >= '2023-05-16 04:00:00.000' and is_resend=0 order by create_time desc limit 1;";
		
						result = DBUtils.funcReadDBReturnAll(dbName, selectSql, TestEnv);
						//result = DBUtils.funcReadDBReturnAll(dbName, selectSql, TestEnv);
						//System.out.println(result);
						if( result.length()>2) {
							//System.out.println(result);
							if (TestEnv.equalsIgnoreCase("test")||TestEnv.equalsIgnoreCase("alpha")) {
								eid = result.substring(5, result.length()-1);
							}else {
								eid = result;
							}
							System.out.println("Email id is: "+eid);
							System.out.println("---->>>Going to send emails for template: "+etmp);
							RestAPI.testPostResendMailSubmit(url, cookie, eid, funcTestStamp(Brand), etmp);
						}else {
							System.out.println("=======================>>>No email found for " + etmp);	
						}
					}
				}
				is.close();
				wb.close();
			}catch(FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		
		/* Developed by Alex Liu 
		 * Resend email by date
		 * 
		 */
		@Parameters({"AdminURL","AdminName","AdminPass", "Brand","TestEnv"})
		@Test(priority=0)
		public void SQLGetMailIdByDate(String AdminURL, String AdminName, String AdminPass, String Brand, String TestEnv, Method method) throws Exception
		{
			String dbName,selectSql;
			ExtentTestManager.startTest(method.getName(),"Description: Get email IDs by email templates");
			driver.get(AdminURL);
			String cookie = Utils.getAdminCookie(driver, AdminName, AdminPass, Brand, TestEnv);
			//remove the admin/main from AdminURL
			String url= Utils.ParseInputURL(AdminURL);
			
			
			 dbName= Utils.getDBName(Brand)[1];			
			

			selectSql="select id from tb_mail_send_log where create_time >= '2022-05-13 04:00:00.000' and is_resend=0 group by template_invoke_name";
		
			String result = DBUtils.funcReadDBReturnAll(dbName, selectSql, TestEnv);
			
			String[] ary = result.substring(1, result.length()-1).split(",");
			
			for (String eid : ary) {

				eid = eid.trim();
				eid = eid.substring(4, eid.length());
				System.out.println(eid);
				RestAPI.testPostResendMailSubmit(url, cookie, eid, "", "alex test");
			}

		}
		
	}

