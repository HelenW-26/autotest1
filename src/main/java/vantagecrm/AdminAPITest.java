package vantagecrm;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.ITestContext;

import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import newcrm.global.GlobalMethods;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class AdminAPITest { 
	
	int waitIndex=1; 
	String userName; //Client Name
	String IBName;  //IB Name
	String internalUserName; //Internal user name;
	Random tRandom=new Random();
	WebDriverWait wait03;	
	String cookie="";
	static String urlbase="";
	WebDriver driver;
	WebDriverWait wait01;
	WebDriverWait wait02;
	
	//Launch driver
	//@BeforeClass(alwaysRun=true)
	@Parameters(value= {"TestEnv","headless"})
	public void LaunchBrowser(String TestEnv, @Optional("False") String headless, ITestContext context)
	{
		
		  System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
		  driver = Utils.funcSetupDriver(driver, "chrome", headless);
    	  context.setAttribute("driver", driver);
    	  
    	  utils.Listeners.TestListener.driver=driver;
    	  
		  driver.manage().window().maximize();		 
		  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	
		  if(TestEnv.equalsIgnoreCase("beta") || TestEnv.equalsIgnoreCase("prod"))
		  {
			  waitIndex=2;
		  }
		  
		  wait01=new WebDriverWait(driver, Duration.ofSeconds(10));
		  wait02=new WebDriverWait(driver,Duration.ofSeconds(20));
	}
	
	@Parameters({"AdminURL","AdminName","AdminPass","Brand","Regulator","TestEnv"})
	@Test(priority=0)
	public void AdminLogIn(String AdminURL, String AdminName, String AdminPass, String Brand, String Regulator, String TestEnv,Method method) throws Exception
	{
		urlbase = Utils.ParseInputURL(AdminURL);
		if (TestEnv.equals("testABC")) {
			cookie  = RestAPI.testPostForAdminCookie(urlbase,AdminName,AdminPass,Brand,Regulator,TestEnv);
		}else {
			driver.get(AdminURL);
			cookie  = Utils.getAdminCookie(driver, AdminName,AdminPass,Brand,TestEnv);
			
		}
		
		/*
		 * AdminURL = Utils.ParseInputURL(AdminURL);
		 * String cookie = RestAPI.testPostForAdminCookie(AdminURL, AdminName, AdminPass, Brand, TestEnv);
		 * 
		 * //Change regulator
		 * RestAPI.testPostSwitchRegulator(Brand, AdminURL, cookie);
		 */
	}
	
	@Parameters({"AdminURL","AdminName","AdminPass", "Brand", "TestEnv"})
	@Test(dependsOnMethods="AdminLogIn",priority=0)
	public void APIClientSearch(String AdminURL, String AdminName, String AdminPass, String Brand, String TestEnv, Method method) throws Exception
	{
		String url="",result="", real_name="",uid="",is_del="",country="",account="",leadSource="",cpa="" ;
		String[] entry=null;
		System.out.println("\n-------------------------------------------------------------");
		System.out.println("TEST STARTS: Start to Search Client using API ....");
		System.out.println("-------------------------------------------------------------");
		
		//String selectSql="select id,real_name,is_del FROM tb_user where real_name like \"%Webt%\" AND status=\"1\" order by create_time desc limit 1;";
		String selectSql="select id,real_name,is_del FROM tb_user where real_name like %" + Utils.webUserPrefix +"% AND status=\"1\" order by create_time desc limit 1;";
		String dbName = Utils.getDBName(Brand)[1];
		
		result = DBUtils.funcReadDBReturnAll(dbName,selectSql, TestEnv);
		   
 		//parse the result and get the column we need
        result = result.substring(1, result.length()-1); 
        String[] b=result.split(",");
		System.out.println("\n"+ "Result converted is: " + Arrays.toString(b));
		
		for (String each : b) {

			if(!TestEnv.equalsIgnoreCase("test")) {
				entry = each.split("=");     
			}else {
			    entry = each.split(":");        
			}
			//Get the real_name
		    if (entry[0].trim().equals("real_name")) {
		    	real_name = entry[1].trim();
		
		    }
		    
		    //Get the is_del
		    if (entry[0].trim().equals("is_del")) {
		    	is_del = entry[1].trim();
		    }
		    
		    //Get the id
		    if (entry[0].trim().equals("id")) {
		    	uid = entry[1].trim();
		    }

		}
        
		url = urlbase.concat("individual/query_individualList");
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json; charset=UTF-8");
		header.put("Accept", "application/json");
		header.put("Cookie", cookie);
		
		//Send API (search by Client Name)
		System.out.println("\n===========>>>API Test Searching Client By Name");
		if (!is_del.equals("1")) {
			String body = "{\"limit\":10,\"pageNo\":1,\"offset\":0,\"order\":\"asc\",\"search\":{\"returnTime\":\"0\",\"subordinate\":\"\",\"agentuserQuery\":\"\",\"search_type\":\"real_name\",\"userQuery\":\""+real_name+"\",\"directLevel\":\"5\",\"user_id\":\"\",\"org_id\":\"\"}}"; 
			
			result = RestAPI.commonPostAPI(url,header,body);
			Utils.funcIsStringContains(result, real_name, Brand);
		}

		account = funcParseResult(result, TestEnv, "mt4Account");
		country = funcParseResult(result, TestEnv, "country_name");
		leadSource = funcParseResult(result, TestEnv, "leadSource");
		cpa = funcParseResult(result, TestEnv, "cpa");
		//System.out.println("webSource: "+webSource);
		
		//Send API (search by account)
		System.out.println("\n===========>>>API Test Searching Client By Account");
		if (!is_del.equals("1")) {
			String body = "{\"limit\":10,\"pageNo\":1,\"offset\":0,\"order\":\"asc\",\"search\":{\"returnTime\":\"0\",\"subordinate\":\"\",\"agentuserQuery\":\"\",\"search_type\":\"mt4Account\",\"userQuery\":\""+account+"\",\"directLevel\":\"5\",\"user_id\":\"\",\"org_id\":\"\"}}";
			result = RestAPI.commonPostAPI(url,header,body);

			Utils.funcIsStringContains(result, real_name, Brand);
		}

		//Send API (search by country in ky)
		if((!is_del.equals("1")) && (Brand.equals("ky") || Brand.contains("vfsc") ||Brand.equals("fca")) ) {
			System.out.println("\n===========>>>API Test Searching Client By Country");
			if (!is_del.equals("1")) {
				String body = "{\"limit\":10,\"pageNo\":1,\"offset\":0,\"order\":\"asc\",\"search\":{\"returnTime\":\"0\",\"subordinate\":\"\",\"agentuserQuery\":\"\",\"search_type\":\"country\",\"userQuery\":\""+country+"\",\"directLevel\":\"5\",\"user_id\":\"\",\"org_id\":\"\"}}";
				result = RestAPI.commonPostAPI(url,header,body);
	
				Utils.funcIsStringContains(result, real_name, Brand);
			}
		}

		//Send API (search by leadSource)
		if(!leadSource.equals("0")) {
			System.out.println("\n===========>>>API Test Searching Client By leadSource");
			if (!is_del.equals("1")) {
				String body = "{\"limit\":10,\"pageNo\":1,\"offset\":0,\"order\":\"asc\",\"search\":{\"returnTime\":\"0\",\"subordinate\":\"\",\"agentuserQuery\":\"\",\"search_type\":\"leadSource\",\"userQuery\":\""+leadSource+"\", \"directLevel\":\"5\",\"user_id\":\"\",\"org_id\":\"\"}}";
				result = RestAPI.commonPostAPI(url,header,body);
	
				Utils.funcIsStringContains(result, real_name, Brand);
			}
		}
		
		//Send API (search by cpa)
		if(!cpa.equals("")) {
			System.out.println("\n===========>>>API Test Searching Client By cpa");
			if (!is_del.equals("1")) {
				String body = "{\"limit\":10,\"pageNo\":1,\"offset\":0,\"order\":\"asc\",\"search\":{\"returnTime\":\"0\",\"subordinate\":\"\",\"agentuserQuery\":\"\",\"search_type\":\"cpa\",\"userQuery\":\""+cpa+"\",\"directLevel\":\"5\",\"user_id\":\"\",\"org_id\":\"\"}}";
				result = RestAPI.commonPostAPI(url,header,body);
	
				Utils.funcIsStringContains(result, real_name, Brand);
			}
		}
	}
	
	
	@Parameters({"AdminURL","AdminName","AdminPass", "Brand", "TestEnv"})
	@Test(priority=0,dependsOnMethods="AdminLogIn")
	public void APIClientUpdate(String AdminURL, String AdminName, String AdminPass, String Brand, String TestEnv, Method method) throws Exception
	{
		String url="",result="", first_name="", last_name="", uid="",is_del="",real_name="",
				cpa="",db="",title="",parentId="",cxd="",body="" ;
		String[] entry=null;
		System.out.println("\n-------------------------------------------------------------");
		System.out.println("TEST STARTS: Start to Update Client using API ....");
		System.out.println("-------------------------------------------------------------");
		
		//For getting real_name,is_del,user_id(uid)
		String selectSql1="select * FROM tb_user where real_name like %" + Utils.webUserPrefix +"% AND status=\"1\" order by create_time desc limit 1;";
		
		db = Utils.getDBName(Brand)[1];
		result = DBUtils.funcReadDBReturnAll(db,selectSql1, TestEnv);
	   
 		//parse the result and get the column we need
        result = result.substring(1, result.length()-1); 
        String[] b=result.split(",");
		System.out.println("\n"+ "Result converted is: " + Arrays.toString(b));
		
		for (String each : b) {

			if(!TestEnv.equalsIgnoreCase("test")) {
				entry = each.split("=");     
			}else {
			    entry = each.split(":");        
			}
			
			//Get the real_name
		    if (entry[0].trim().equals("real_name")) {
		    	real_name = entry[1].trim();
		
		    }
		    //Get the first_name
		    if (entry[0].trim().equals("first_name")) {
		    	first_name = entry[1].trim();
		    } 
		    
		    //Get the last_name
		    if (entry[0].trim().equals("last_name")) {
		    	last_name = entry[1].trim();
		    } 
		    //Get the is_del
		    if (entry[0].trim().equals("is_del")) {
		    	is_del = entry[1].trim();
		    }
		    
		    //Get the id
		    if (entry[0].trim().equals("id")) {
		    	uid = entry[1].trim();
		    }

		}
        
		//For getting title,cpa,cxd
		String selectSql2="select * FROM tb_user_extends where user_id= \""+uid+"\";";
		result = DBUtils.funcReadDBReturnAll(db,selectSql2, TestEnv);
		
		//parse the result and get the column we need
        result = result.substring(1, result.length()-1); 
        b=result.split(",");
		System.out.println("\n"+ "Result converted is: " + Arrays.toString(b));
		
		for (String each : b) {

			if(!TestEnv.equalsIgnoreCase("test")) {
				entry = each.split("=");     
			}else {
			    entry = each.split(":");        
			}
			//Get the title
		    if (entry[0].trim().equals("title")) {
		    	title = entry[1].trim();
		    }
		    
		    //Get the cxd
		    if (entry[0].trim().equals("cxd")) {
		    	cxd = entry[1].trim();
		    }

		}
		
		//For getting parentId,
		String selectSql3="select * FROM tb_user_outer where user_id= \""+uid+"\";";
		result = DBUtils.funcReadDBReturnAll(db,selectSql3, TestEnv);
		
		//parse the result and get the column we need
        result = result.substring(1, result.length()-1); 
        b=result.split(",");
		System.out.println("\n"+ "Result converted is: " + Arrays.toString(b));
		
		for (String each : b) {

			if(!TestEnv.equalsIgnoreCase("test")) {
				entry = each.split("=");     
			}else {
			    entry = each.split(":");        
			}
			//Get the parentId
		    if (entry[0].trim().equals("parentId")) {
		    	parentId = entry[1].trim();
		    	if ( parentId.equals("null") ) {
		    		parentId = "";
		    	}
		    }
		    

		}

		url = urlbase.concat("individual/update_individual");
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		header.put("Cookie", cookie);
		
		switch(Brand)
		{
		  case "au":
		    body = "id="+uid+"&accountName="+real_name+"&p_Id="+parentId+"&password=&cpa=1aomer&mobile_code=61&mobile=&add_cxd="+cxd+"&lead_source=API_automation&homePhone=&promotion=14&workPhone=&occupation=Telecommunications&op_other=&country=3512&ib_Mployment=Self+Employed&ib_Nationality=3&income=100+K%2B&street_name=10+Mock+street&ib_Sinvest=%24250%2C000+and+above&suburb=MockSub&ib_funds=Employment&funds_other=&state=New+South+Wales&state_input=New+South+Wales&indi_postcode=12212&credit_card=&firstBirthday=2001-04-28&whoreferredyoutous=Automation_scripts&processed_notes=&passport=&bankStatement=&worldCheck=&lpoa=";
		    break;
		    
		  default:

			body = "id="+uid+"&acc_title="+title+"&acc_first_name="+first_name+"&p_Id="+parentId+"&acc_middle_name=&acc_last_name="+last_name+"&cpa=1aomer&add_cxd="+cxd+"&lead_source=API_automation&password=&promotion=14&mobile_code=960&mobile=4f5152e99fbd3561cec7fe981bc5c3719ca141d6f277c528faab10b3d8fad145d1f08fe0bbd08efc8cef2e9927047a952708a1a13f442a1a8f4bf8a659d0e9ea1aa980ecb84330b2c3a30cf58da87833cc7ed07b834140b1e29739b0d74b8fcee6969335a22fc9e2ebad154c5f05fb71107a70619c8248ecb604faf633bf7c71&ib_Mployment=Self+Employed&country=5015&income=%2450%2C001+-+%24100%2C000&ib_Nationality=8&acc_id_type=Driver+license&street_name=94+JOUL+NMJZ+Street&acc_id_num=112418156788746867&suburb=Washington&ib_Sinvest=%2450%2C001+-+%24100%2C000&state=&state_input=Automation_proc&ib_funds=Company+sale+or+sale+of+interest+in+company&indi_postcode=7749&firstBirthday=1997-07-25&credit_card=&whoreferredyoutous=Automation_scripts&acc_tax_us=0&acc_invest_deposit=%2450%2C001+-+%24100%2C000&processed_notes=&acc_investmentexp_tradew=1+-+15+Trades&acc_investmentexp_amount_tradew=1%2C001+-+2500+USD&acc_investmentexp_security=Quarterly&acc_investmentexp_levfx=Semi-Annually&acc_investmentexp_derivative=Weekly&acc_investmentexp_vol=%240+-+%2450%2C000&passport=&worldCheck=&lpoa=&suspected2=1"; 

			//Yanni on 1/07/2020: VT& PUG now have the same fields with ky. So comment out the following lines
			/*
			 * break;
			 * case "vt":
			 * case "pug":
			 
		  default:
		    body = "id="+uid+"&accountName="+real_name+"&p_Id="+parentId+"&password=&cpa=1aomer&mobile_code=960&mobile=&add_cxd="+cxd+"&lead_source=API_automation&homePhone=&promotion=14&workPhone=&occupation=Energy+and+Utilities&op_other=&country=5015&ib_Mployment=Self+Employed&ib_Nationality=8&income=%2450%2C001+-+%24100%2C000&street_name=94+JOUL+NMJZ+Street&ib_Sinvest=0~%2449%2C999&suburb=Washington&ib_funds=other&funds_other=Company+sale+or+sale+of+interest+in+company&state=&state_input=Automation_proc&indi_postcode=7749&credit_card=&firstBirthday=1997-07-25&whoreferredyoutous=Automation_scripts&processed_notes=&passport=&bankStatement=&worldCheck=&lpoa=&suspected2=1"; 
		    break;	
		    */
		}
		
		//Send API update client
		System.out.println("\n===========>>>API Test updating Client body: "+body);
		if (!is_del.equals("1")) {
			result = RestAPI.commonPostAPI(url,header,body);
			Utils.funcIsStringContains(result, "true", Brand);
		}else {
			System.out.println("!!!User is deleted, not able to proceed!");
		}
		
		//Verify if client has been updated(check if cpa, referer, leadSource contains updated value)
		result = DBUtils.funcReadDBReturnAll(db,selectSql2, TestEnv);
		Utils.funcIsStringContains(result, "1aomer", Brand);
		Utils.funcIsStringContains(result, "Automation_scripts", Brand);
		Utils.funcIsStringContains(result, "API_automation", Brand);	
		

	}
	
	@Parameters({"AdminURL","AdminName","AdminPass", "Brand", "TestEnv"})
	@Test(dependsOnMethods="AdminLogIn",priority=0)
	public void APIClientDBSearch(String AdminURL, String AdminName, String AdminPass, String Brand, String TestEnv, Method method) throws Exception
	{
		String db="",url1="",url2="",result="", real_name="",uid="",is_del="",country="",account="",leadSource="",websiteId="",cpa="" ;
		String[] entry=null;
		ArrayList<String> mt4_acc_list = new ArrayList<String> ();

		int j=0;
		System.out.println("\n-------------------------------------------------------------");
		System.out.println("TEST STARTS: Start to Search Client Database using API ....");
		System.out.println("-------------------------------------------------------------");
		
		String selectSql1="select id,real_name,is_del FROM tb_user where real_name like %"+ Utils.webUserPrefix + "% AND status=\"1\" order by create_time desc limit 1;";
		
		db = Utils.getDBName(Brand)[1];
		result = DBUtils.funcReadDBReturnAll(db,selectSql1, TestEnv);
	   
 		//parse the result and get the column we need
        result = result.substring(1, result.length()-1); 
        String[] b=result.split(",");
		System.out.println("\n"+ "Result converted is: " + Arrays.toString(b));
		
		for (String each : b) {

			if(!TestEnv.equalsIgnoreCase("test")) {
				entry = each.split("=");     
			}else {
			    entry = each.split(":");        
			}
			//Get the real_name
		    if (entry[0].trim().equals("real_name")) {
		    	real_name = entry[1].trim();
		
		    }
		    
		    //Get the is_del
		    if (entry[0].trim().equals("is_del")) {
		    	is_del = entry[1].trim();
		    }
		    
		    //Get the id
		    if (entry[0].trim().equals("id")) {
		    	uid = entry[1].trim();
		    }

		}
        
		//For getting all the mt4 accounts
		String selectSql2="select mt4_account FROM tb_account_mt4 where user_id= \""+uid+"\";";
		result = DBUtils.funcReadDBReturnAll(db,selectSql2, TestEnv);
		
		//parse the result and get the column we need
        result = result.substring(1, result.length()-1); 
        b=result.split(",");
		System.out.println("\n"+ "Result converted is: " + Arrays.toString(b));
		
		for (int i=0; i<b.length; i++) {

			if(!TestEnv.equalsIgnoreCase("test")) {
				entry = b[i].split("=");     
			}else {
			    entry = b[i].split(":");        
			}
			//System.out.println("\n"+ "entry converted is: " + entry[1]);
			//Get the parentId
		    if (!entry[1].trim().equals("null")) {
		    	mt4_acc_list.add(entry[1].trim());
		    }else {
		    	//"null" account counter
		    	j++;
		    }

		}

		
		url1 = urlbase.concat("sale/query_clientDataSearch");
		url2 = urlbase.concat("sale/query_leadsDataSearch");
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json; charset=UTF-8");
		header.put("Accept", "application/json");
		header.put("Cookie", cookie);
		
		/**Send API for searching Client DB by Client Name
		 * */
		System.out.println("\n===========>>>API Test Searching Client DB By Name");
		String body = "{\"limit\":10,\"pageNo\":1,\"offset\":0,\"order\":\"asc\",\"search\":{\"search_type\":\"real_name\",\"keyword\":\""+real_name+"\"}}"; 	
		result = RestAPI.commonPostAPI(url1,header,body);
		
		//Verify if all the mt4 accounts are included
		for (String each:mt4_acc_list) {
		    Utils.funcIsStringContains(result, each, Brand);
		}

		//Send API for searching Lead DB by Client Name
		System.out.println("\n===========>>>API Test Searching Lead DB By Name");
		result = RestAPI.commonPostAPI(url2,header,body);
		//Verify if all the mt4 accounts are included
		Utils.funcIsStringContains(result, "\"total\":0", Brand);
		
		
		
		/**Send API for searching Client DB by Account
		 * */
		System.out.println("\n===========>>>API Test Searching Client DB By Account");
		for (String each:mt4_acc_list) {
			body = "{\"limit\":10,\"pageNo\":1,\"offset\":0,\"order\":\"asc\",\"search\":{\"search_type\":\"mt4Account\",\"keyword\":\""+each+"\"}}"; 	
			result = RestAPI.commonPostAPI(url1,header,body);
			//Verify if real_name are included
		    Utils.funcIsStringContains(result, real_name, Brand);
		}

		//Send API for searching Lead DB by Account
		System.out.println("\n===========>>>API Test Searching Lead DB By Account");
		result = RestAPI.commonPostAPI(url2,header,body);
		//Verify if all the mt4 accounts are included
		Utils.funcIsStringContains(result, "\"total\":0", Brand);	
		
		
		/**Send API for searching Client DB by Country
		 * */
		country = funcQueryCountryFromName(real_name, cookie, TestEnv);
		System.out.println("\n===========>>>API Test Searching Client DB By Country: "+country);
		
		body = "{\"limit\":10,\"pageNo\":1,\"offset\":0,\"order\":\"asc\",\"search\":{\"search_type\":\"country\",\"keyword\":\""+country+"\"}}"; 	
		result = RestAPI.commonPostAPI(url1,header,body);
		
		//Verify if real_name are included
		Utils.funcIsStringContains(result, real_name, Brand);
		
		//Verify if all the mt4 accounts are included
		for (String each:mt4_acc_list) {
			Utils.funcIsStringContains(result, each, Brand);
		}

		//Send API for searching Lead DB by Country
		System.out.println("\n===========>>>API Test Searching Lead DB By Country: "+country);
		result = RestAPI.commonPostAPI(url2,header,body);
		//Verify if all the mt4 accounts are included
		Utils.funcIsStringContains(result, "true", Brand);	

		
		/**Send API for searching Client DB by CPA
		 * */
		cpa = funcQueryCPAById(uid, db, cookie, TestEnv);
		if ((!cpa.equals(""))&&(!cpa.equals("null"))) {

			System.out.println("\n===========>>>API Test Searching Client DB By CPA: "+cpa);
			
			body = "{\"limit\":10,\"pageNo\":1,\"offset\":0,\"order\":\"asc\",\"search\":{\"search_type\":\"cpa\",\"keyword\":\""+cpa+"\"}}"; 	
			result = RestAPI.commonPostAPI(url1,header,body);
			
			//Verify if real_name are included
			Utils.funcIsStringContains(result, real_name, Brand);
			
			//Verify if all the mt4 accounts are included
			for (String each:mt4_acc_list) {
				Utils.funcIsStringContains(result, each, Brand);
			}
	
			//Send API for searching Lead DB by CPA
			System.out.println("\n===========>>>API Test Searching Lead DB By CPA: "+cpa);
			result = RestAPI.commonPostAPI(url2,header,body);
			//Verify if all the mt4 accounts are included
			Utils.funcIsStringContains(result, "true", Brand);	
		}else {
			System.out.println("!!!cpa is null, skip to next check!");
		}
		
		
		/**Send API for searching Client DB by leadSource
		 * */
		leadSource = funcQueryLeadSourceById(uid, db, cookie, TestEnv);
		if (!leadSource.equals("")) {
			System.out.println("\n===========>>>API Test Searching Client DB By leadSource: "+leadSource);
			
			body = "{\"limit\":10,\"pageNo\":1,\"offset\":0,\"order\":\"asc\",\"search\":{\"search_type\":\"leadSource\",\"keyword\":\""+leadSource+"\"}}"; 	
			result = RestAPI.commonPostAPI(url1,header,body);
			
			//Verify if real_name are included
			Utils.funcIsStringContains(result, real_name, Brand);
			
			//Verify if all the mt4 accounts are included
			for (String each:mt4_acc_list) {
				Utils.funcIsStringContains(result, each, Brand);
			}
	
			//Send API for searching Lead DB by leadSource
			System.out.println("\n===========>>>API Test Searching Lead DB By leadSource: "+leadSource);
			result = RestAPI.commonPostAPI(url2,header,body);
			//Verify if all the mt4 accounts are included
			Utils.funcIsStringContains(result, "true", Brand);	
		}else {
			System.out.println("!!!leadSource is null, skip to next check!");
		}
		
		
		/**Send API for searching Client DB by webSource
		 * */
		websiteId = funcQueryWebSourceById(uid, db, cookie, TestEnv);
		if (!websiteId.equals("")) {
			System.out.println("\n===========>>>API Test Searching Client DB By webSource: "+websiteId);
			
			body = "{\"limit\":10,\"pageNo\":1,\"offset\":0,\"order\":\"asc\",\"search\":{\"search_type\":\"websiteId\",\"keyword\":\""+websiteId+"\"}}"; 	
			result = RestAPI.commonPostAPI(url1,header,body);
			
			//Verify if real_name are included
			Utils.funcIsStringContains(result, real_name, Brand);
			
			//Verify if all the mt4 accounts are included
			for (String each:mt4_acc_list) {
				Utils.funcIsStringContains(result, each, Brand);
			}
	
			//Send API for searching Lead DB by webSource
			System.out.println("\n===========>>>API Test Searching Lead DB By webSource: "+websiteId);
			result = RestAPI.commonPostAPI(url2,header,body);
			//Verify if all the mt4 accounts are included
			Utils.funcIsStringContains(result, "true", Brand);	
		}else {
			System.out.println("!!!leadSource is null, skip to next check!");
		}
	}

	
	//@Parameters({"AdminURL","AdminName","AdminPass", "Brand", "TestEnv"})
	//@Test(dependsOnMethods="AdminLogIn",priority=0)
	public void APIDownloadReport(String AdminURL, String AdminName, String AdminPass, String Brand, String TestEnv, Method method) throws Exception
	{
		String db="",url2="",result="", uid="",is_del="",country="",leadSource="",websiteId="",cpa="" ;
		String[] entry=null;
		ArrayList<String> mt4_acc_list = new ArrayList<String> ();
		
		System.out.println("\n-------------------------------------------------------------");
		System.out.println("TEST STARTS: Start to download deposit and withdraw report using API ....");
		System.out.println("-------------------------------------------------------------");
		
		String selectSql1="select id,real_name,is_del FROM tb_user where real_name like %" + Utils.webUserPrefix + "% AND status=\"1\" order by create_time desc limit 1;";
		db = Utils.getDBName(Brand)[1];
	}
	
	
	@Test(dependsOnMethods="AdminLogIn",invocationCount=1, alwaysRun=true)
	@Parameters({"TraderURL", "TraderName", "TraderPass","Brand", "TestEnv"})
	public void APITestMinDeposit(String TraderURL, String TraderName, String TraderPass, String Brand, String TestEnv, Method method) throws Exception
	{
		String userId, db="", result,account,cpurl,url_cc365,cp_cookie,body,token,account_au;


		System.out.println("\n-------------------------------------------------------------");
		System.out.println("TEST STARTS: Start to test minimum deposit value using API ....");
		System.out.println("-------------------------------------------------------------");
		
		//Get the CP cookies
		ChromeOptions options=new ChromeOptions();
		options.addArguments("--incognito");			
		System.setProperty("webdriver.chrome.driver", Utils.ChromePath);
		driver = new ChromeDriver(options);
		driver.get(TraderURL);		
		cp_cookie = Utils.getTraderCookie(driver,TraderName, TraderPass, Brand);
		System.out.println("cp cookie: "+cp_cookie);
		
		cpurl = Utils.ParseInputURL(TraderURL);
		userId = RestAPI.testQueryUserIdByName(urlbase, cookie, TraderName.substring(0, TraderName.indexOf("@")));
		System.out.println("user id is: "+userId);
		
		account = funcQueryAccountById(userId, "USD", Brand, TestEnv);
		
		/********** Minimum Deposit Test ************
		 ** Credit Card 365 Deposit - Minimum is 50**/
		Thread.sleep(1000);
		url_cc365 = cpurl.concat("cp/api/deposit/cc_payment_365");
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json; charset=UTF-8");
		header.put("Accept", "application/json, text/plain, */*");
		header.put("Cookie", cp_cookie);
		
		System.out.println("\n===========>>>API Test CC365 deposit 50");
		body = "{\"mt4Account\":"+account+",\"paymentMethod\":\"creditcard\",\"depositAmount\":50,\"cardNumber\":\"4336875756203495\",\"cardHolderName\":\"test cc\",\"expireYear\":\"2022\",\"expireMonth\":\"10\",\"cvv\":\"393\",\"is3DS\":true,\"cardExist\":false}"; 	
		result = RestAPI.commonPostAPI(url_cc365,header,body);
		Utils.funcIsStringContains(result, "gateway.transact365.com", Brand);
		
		System.out.println("\n===========>>>API Test CC365 deposit 1");
		body = "{\"mt4Account\":"+account+",\"paymentMethod\":\"creditcard\",\"depositAmount\":1,\"cardNumber\":\"4336875756203495\",\"cardHolderName\":\"test cc\",\"expireYear\":\"2022\",\"expireMonth\":\"10\",\"cvv\":\"393\",\"is3DS\":true,\"cardExist\":false}"; 	
		result = RestAPI.commonPostAPI(url_cc365,header,body);
		Utils.funcIsStringContains(result, "500", Brand);
		
		
		/********** Minimum Deposit Test ************
		 ** Credit Card NAB Deposit - Minimum is 50**/
		Thread.sleep(1000);
		String url_ccnab = cpurl.concat("cp/api/deposit/cc_payment");
		header = new HashMap<String, String>();
		header.put("Content-Type", "application/json; charset=UTF-8");
		header.put("Accept", "application/json, text/plain, */*");
		header.put("Cookie", cp_cookie);

		System.out.println("\n===========>>>API Test CC NAB non-3D deposit 50");
		body = "{\"mt4Account\":"+account+",\"paymentMethod\":\"creditcard\",\"depositAmount\":50,\"cardNumber\":\"4336875756203495\",\"cardHolderName\":\"test cc\",\"expireYear\":\"2022\",\"expireMonth\":\"10\",\"cvv\":\"393\",\"is3DS\":false,\"cardExist\":false}"; 	
		result = RestAPI.commonPostAPI(url_ccnab,header,body);
		if(TestEnv.equalsIgnoreCase("test")) {
			Utils.funcIsStringContains(result, "\"code\":0", Brand);
		}else {
			Utils.funcIsStringContains(result, "success", Brand);
		}
		
		System.out.println("\n===========>>>API Test CC NAB non-3D deposit 1");
		body = "{\"mt4Account\":"+account+",\"paymentMethod\":\"creditcard\",\"depositAmount\":1,\"cardNumber\":\"4336875756203495\",\"cardHolderName\":\"test cc\",\"expireYear\":\"2022\",\"expireMonth\":\"10\",\"cvv\":\"393\",\"is3DS\":false,\"cardExist\":false}"; 	
		result = RestAPI.commonPostAPI(url_ccnab,header,body);
		Utils.funcIsStringContains(result, "500", Brand);
		
		Thread.sleep(1000);
		
		System.out.println("\n===========>>>API Test CC NAB 3D deposit 50");
		body = "{\"mt4Account\":"+account+",\"paymentMethod\":\"creditcard\",\"depositAmount\":50,\"cardNumber\":\"4336875756203495\",\"cardHolderName\":\"test cc\",\"expireYear\":\"2022\",\"expireMonth\":\"10\",\"cvv\":\"393\",\"is3DS\":true,\"cardExist\":false}"; 	
		result = RestAPI.commonPostAPI(url_ccnab,header,body);
		if(TestEnv.equalsIgnoreCase("test")) {
			Utils.funcIsStringContains(result, "\"code\":0", Brand);
		}else {
			Utils.funcIsStringContains(result, "transact.nab.com.au/live/directpostv2/authorise3d", Brand);
		}
		
		System.out.println("\n===========>>>API Test CC NAB 3D deposit 1");
		body = "{\"mt4Account\":"+account+",\"paymentMethod\":\"creditcard\",\"depositAmount\":1,\"cardNumber\":\"4336875756203495\",\"cardHolderName\":\"test cc\",\"expireYear\":\"2022\",\"expireMonth\":\"10\",\"cvv\":\"393\",\"is3DS\":true,\"cardExist\":false}"; 	
		result = RestAPI.commonPostAPI(url_ccnab,header,body);
		Utils.funcIsStringContains(result, "500", Brand);
		
		
		if (Brand.equalsIgnoreCase("ky") || Brand.toLowerCase().contains("vfsc") || Brand.equalsIgnoreCase("fca")) {
			/********** Minimum Deposit Test ************
			 ** International Bank Deposit - Minimum is 1**/
			Thread.sleep(1000);
			String url_int = cpurl.concat("cp/api/deposit/international_bank_payment");
			result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
			token = result.substring(18, result.length()-2); 
			System.out.println("token is: "+token);
			header = new HashMap<String, String>();
			header.put("Content-Type", "application/json; charset=UTF-8");
			header.put("Accept", "application/json, text/plain, */*");
			header.put("Cookie", cp_cookie);
			header.put("token", token);
	
			System.out.println("\n===========>>>API Test International Bank Deposit 1");
			body = "{\"mt4Account\":\""+account+"\",\"operateAmount\":1,\"applicationNotes\":\"\",\"currency\":\"EUR\",\"fileList\":[\"/group1/M00/00/3E/wKhCaF3u3OuACmO2AAECs1vC-QY939.png\"]}"; 	
			result = RestAPI.commonPostAPI(url_int,header,body);
			Utils.funcIsStringContains(result, "true", Brand);
			
			System.out.println("\n===========>>>API Test International Bank Deposit 0.1");		
			result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
			token = result.substring(18, result.length()-2); 
			header.put("token", token);
			body = "{\"mt4Account\":\""+account+"\",\"operateAmount\":0.1,\"applicationNotes\":\"\",\"currency\":\"EUR\",\"fileList\":[\"/group1/M00/00/3E/wKhCaF3u3OuACmO2AAECs1vC-QY939.png\"]}"; 		
			result = RestAPI.commonPostAPI(url_int,header,body);
			Utils.funcIsStringContains(result, "500", Brand);
			
			
			/********** Minimum Deposit Test ************
			 ** Skrill Deposit - Minimum is 1**/
			Thread.sleep(1000);
			String url_sk = cpurl.concat("cp/api/deposit/skrill_payment");
			result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
			token = result.substring(18, result.length()-2); 
			System.out.println("token is: "+token);
			header = new HashMap<String, String>();
			header.put("Content-Type", "application/json; charset=UTF-8");
			header.put("Accept", "application/json, text/plain, */*");
			header.put("Cookie", cp_cookie);
			header.put("token", token);

			System.out.println("\n===========>>>API Test Skrill Deposit 1");
			body = "{\"mt4Account\":"+account+",\"operateAmount\":1,\"applicationNotes\":\"\",\"skrillEmail\":\"skrill@test.com\"}"; 	
			result = RestAPI.commonPostAPI(url_sk,header,body);
			Utils.funcIsStringContains(result, "true", Brand);
			
			System.out.println("\n===========>>>API Test Skrill Deposit 0.1");		
			result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
			token = result.substring(18, result.length()-2); 
			header.put("token", token);
			body = "{\"mt4Account\":"+account+",\"operateAmount\":0.1,\"applicationNotes\":\"\",\"skrillEmail\":\"skrill@test.com\"}"; 		
			result = RestAPI.commonPostAPI(url_sk,header,body);
			Utils.funcIsStringContains(result, "500", Brand);
			
			
			/********** Minimum Deposit Test ************
			 ** Neteller Deposit - Minimum is 50**/
			Thread.sleep(1000);
			String url_net = cpurl.concat("cp/api/deposit/neteller_payment");
			result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
			token = result.substring(18, result.length()-2); 
			System.out.println("token is: "+token);
			header = new HashMap<String, String>();
			header.put("Content-Type", "application/json; charset=UTF-8");
			header.put("Accept", "application/json, text/plain, */*");
			header.put("Cookie", cp_cookie);
			header.put("token", token);
			
			System.out.println("\n===========>>>API Test Neteller Deposit 50");
			body = "{\"mt4Account\":"+account+",\"operateAmount\":50,\"applicationNotes\":\"123\",\"paymentMethod\":\"neteller\"}"; 	
			result = RestAPI.commonPostAPI(url_net,header,body);
			Utils.funcIsStringContains(result, "true", Brand);
			
			System.out.println("\n===========>>>API Test Neteller Deposit 1");	
			result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
			token = result.substring(18, result.length()-2); 
			header.put("token", token);
			body = "{\"mt4Account\":"+account+",\"operateAmount\":1,\"applicationNotes\":\"123\",\"paymentMethod\":\"neteller\"}"; 		
			result = RestAPI.commonPostAPI(url_net,header,body);
			Utils.funcIsStringContains(result, "500", Brand);
			
			
			/********** Minimum Deposit Test ************
			 ** Paypal Deposit - Minimum is 50**/
			Thread.sleep(1000);
			String url_pp = cpurl.concat("cp/api/deposit/paypal_payment");
			result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
			token = result.substring(18, result.length()-2); 
			System.out.println("token is: "+token);
			header = new HashMap<String, String>();
			header.put("Content-Type", "application/json; charset=UTF-8");
			header.put("Accept", "application/json, text/plain, */*");
			header.put("Cookie", cp_cookie);
			header.put("token", token);
			
			System.out.println("\n===========>>>API Test Paypal Deposit 50");
			body = "{\"mt4Account\":"+account+",\"operateAmount\":50,\"applicationNotes\":\"01Qm\",\"paymentMethod\":\"paypal\"}"; 	
			result = RestAPI.commonPostAPI(url_pp,header,body);
			Utils.funcIsStringContains(result, "paypal.com", Brand);
			
			System.out.println("\n===========>>>API Test Paypal Deposit 1");	
			result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
			token = result.substring(18, result.length()-2); 
			header.put("token", token);
			body = "{\"mt4Account\":"+account+",\"operateAmount\":1,\"applicationNotes\":\"01Qm\",\"paymentMethod\":\"paypal\"}"; 		
			result = RestAPI.commonPostAPI(url_pp,header,body);
			Utils.funcIsStringContains(result, "500", Brand);
			
		}else if(Brand.equalsIgnoreCase("au")) {
			//get an AUD account for the following tests
			account_au = funcQueryAccountById(userId, "AUD", Brand, TestEnv);
			
			/********** Minimum Deposit Test ************
			 ** Australia Bank Deposit - Minimum is 1**/
			Thread.sleep(1000);
			String url_au = cpurl.concat("cp/api/deposit/australian_bank_payment");
			result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
			token = result.substring(18, result.length()-2); 
			System.out.println("token is: "+token);
			header = new HashMap<String, String>();
			header.put("Content-Type", "application/json; charset=UTF-8");
			header.put("Accept", "application/json, text/plain, */*");
			header.put("Cookie", cp_cookie);
			header.put("token", token);
	
			System.out.println("\n===========>>>API Test Australia Bank Deposit 1");
			body = "{\"mt4Account\":\""+account_au+"\",\"operateAmount\":1,\"applicationNotes\":\"\",\"fileList\":[\"/group1/M00/00/3E/wKhCaF3u3OuACmO2AAECs1vC-QY939.png\"]}"; 	
			result = RestAPI.commonPostAPI(url_au,header,body);
			Utils.funcIsStringContains(result, "true", Brand);
			
			System.out.println("\n===========>>>API Test Australia Bank Deposit 0.1");		
			result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
			token = result.substring(18, result.length()-2); 
			header.put("token", token);
			body = "{\"mt4Account\":\""+account_au+"\",\"operateAmount\":0.1,\"applicationNotes\":\"\",\"fileList\":[\"/group1/M00/00/3E/wKhCaF3u3OuACmO2AAECs1vC-QY939.png\"]}"; 		
			result = RestAPI.commonPostAPI(url_au,header,body);
			Utils.funcIsStringContains(result, "500", Brand);
			
			
			/********** Minimum Deposit Test ************
			 ** Bpay Deposit - Minimum is 50**/
			//Update account with Bpay number
			RestAPI.testPostUpdateAccBpayNo(urlbase, cookie, account_au);
			
			Thread.sleep(1000);
			String url_bpay = cpurl.concat("cp/api/deposit/bpay_payment");
			result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
			token = result.substring(18, result.length()-2); 
			System.out.println("token is: "+token);
			header = new HashMap<String, String>();
			header.put("Content-Type", "application/json; charset=UTF-8");
			header.put("Accept", "application/json, text/plain, */*");
			header.put("Cookie", cp_cookie);
			header.put("token", token);
	
			System.out.println("\n===========>>>API Test Bpay Deposit 50");
			body = "{\"mt4Account\":\""+account_au+"\",\"operateAmount\":50,\"applicationNotes\":\"\",\"fileList\":[\"/group1/M00/00/3E/wKhCaF3u3OuACmO2AAECs1vC-QY939.png\"]}"; 	
			result = RestAPI.commonPostAPI(url_bpay,header,body);
			Utils.funcIsStringContains(result, "true", Brand);
			
			System.out.println("\n===========>>>API Test Bpay Deposit 1");		
			result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
			token = result.substring(18, result.length()-2); 
			header.put("token", token);
			body = "{\"mt4Account\":\""+account_au+"\",\"operateAmount\":1,\"applicationNotes\":\"\",\"fileList\":[\"/group1/M00/00/3E/wKhCaF3u3OuACmO2AAECs1vC-QY939.png\"]}"; 		
			result = RestAPI.commonPostAPI(url_bpay,header,body);
			Utils.funcIsStringContains(result, "500", Brand);		
			
			
			
			/********** Minimum Deposit Test ************
			 ** Poli Deposit - Minimum is 50**/
			Thread.sleep(1000);
			String url_poli = cpurl.concat("cp/api/deposit/poli_payment");
			result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
			token = result.substring(18, result.length()-2); 
			System.out.println("token is: "+token);
			header = new HashMap<String, String>();
			header.put("Content-Type", "application/json; charset=UTF-8");
			header.put("Accept", "application/json, text/plain, */*");
			header.put("Cookie", cp_cookie);
			header.put("token", token);
	
			System.out.println("\n===========>>>API Test Poli Deposit 50");
			body = "{\"mt4Account\":\""+account_au+"\",\"operateAmount\":50,\"applicationNotes\":\"\",\"fileList\":[\"/group1/M00/00/3E/wKhCaF3u3OuACmO2AAECs1vC-QY939.png\"]}"; 	
			result = RestAPI.commonPostAPI(url_poli,header,body);
			Utils.funcIsStringContains(result, "true", Brand);
			
			System.out.println("\n===========>>>API Test Poli Deposit 1");		
			result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
			token = result.substring(18, result.length()-2); 
			header.put("token", token);
			body = "{\"mt4Account\":\""+account_au+"\",\"operateAmount\":1,\"applicationNotes\":\"\",\"fileList\":[\"/group1/M00/00/3E/wKhCaF3u3OuACmO2AAECs1vC-QY939.png\"]}"; 		
			result = RestAPI.commonPostAPI(url_poli,header,body);
			Utils.funcIsStringContains(result, "500", Brand);	
		}
		

		/********** Minimum Deposit Test ************
		 ** Broker-to-Broker Deposit - Minimum is 1**/
		Thread.sleep(1000);
		String url_btb = cpurl.concat("cp/api/deposit/broker_to_broker_payment");
		result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
		token = result.substring(18, result.length()-2); 
		System.out.println("token is: "+token);
		header = new HashMap<String, String>();
		header.put("Content-Type", "application/json; charset=UTF-8");
		header.put("Accept", "application/json, text/plain, */*");
		header.put("Cookie", cp_cookie);
		header.put("token", token);
		
		System.out.println("\n===========>>>API Test Broker-to-Broker Deposit 1");
		body = "{\"mt4Account\":"+account+",\"operateAmount\":1,\"currency\":\"\",\"fileUrl\":[\"/group1/M00/00/3F/wKhCaF3u_8uAN1rUAAECs1vC-QY857.png\"]}"; 	
		result = RestAPI.commonPostAPI(url_btb,header,body);
		Utils.funcIsStringContains(result, "true", Brand);
		
		System.out.println("\n===========>>>API Test Broker-to-Broker Deposit 0.1");	
		result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
		token = result.substring(18, result.length()-2); 
		header.put("token", token);
		body = "{\"mt4Account\":"+account+",\"operateAmount\":0.1,\"currency\":\"\",\"fileUrl\":[\"/group1/M00/00/3F/wKhCaF3u_8uAN1rUAAECs1vC-QY857.png\"]}"; 		
		result = RestAPI.commonPostAPI(url_btb,header,body);
		Utils.funcIsStringContains(result, "500", Brand);
		
		
		/********** Minimum Deposit Test ************
		 ** Unionpay Deposit - Minimum is 1**/
		Thread.sleep(1000);
		String url_up = cpurl.concat("cp/api/deposit/unionpay");
		result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
		token = result.substring(18, result.length()-2); 
		System.out.println("token is: "+token);
		header = new HashMap<String, String>();
		header.put("Content-Type", "application/json; charset=UTF-8");
		header.put("Accept", "application/json, text/plain, */*");
		header.put("Cookie", cp_cookie);
		header.put("token", token);
		
		System.out.println("\n===========>>>API Test Unionpay Deposit 50");
		body = "{\"mt4Account\":"+account+",\"operateAmount\":50,\"applicationNotes\":\"01Qm\"}"; 	
		result = RestAPI.commonPostAPI(url_up,header,body);
		Utils.funcIsStringContains(result, "www.settlebm.com", Brand);
		
		System.out.println("\n===========>>>API Test Unionpay Deposit 1");	
		result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
		token = result.substring(18, result.length()-2); 
		header.put("token", token);
		body = "{\"mt4Account\":"+account+",\"operateAmount\":1,\"applicationNotes\":\"01Qm\"}"; 		
		result = RestAPI.commonPostAPI(url_up,header,body);
		Utils.funcIsStringContains(result, "500", Brand);
		
		
		/********** Minimum Deposit Test ************
		 ** Unionpay Transfer - Minimum is 1**/
		Thread.sleep(1000);
		String url_p2p = cpurl.concat("cp/api/deposit/unionpay_transfer");
		result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
		token = result.substring(18, result.length()-2); 
		System.out.println("token is: "+token);
		header = new HashMap<String, String>();
		header.put("Content-Type", "application/json; charset=UTF-8");
		header.put("Accept", "application/json, text/plain, */*");
		header.put("Cookie", cp_cookie);
		header.put("token", token);
		
		System.out.println("\n===========>>>API Test Unionpay Transfer 50");
		body = "{\"mt4Account\":"+account+",\"operateAmount\":50,\"accountName\":\"automation test user\",\"applicationNotes\":\"01Qm\"}"; 	
		result = RestAPI.commonPostAPI(url_p2p,header,body);
		Utils.funcIsStringContains(result, "www.settlebm.com", Brand);
		
		System.out.println("\n===========>>>API Test Unionpay Transfer 1");	
		result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
		token = result.substring(18, result.length()-2); 
		header.put("token", token);
		body = "{\"mt4Account\":"+account+",\"operateAmount\":1,\"accountName\":\"automation test user\",\"applicationNotes\":\"01Qm\"}"; 		
		result = RestAPI.commonPostAPI(url_p2p,header,body);
		Utils.funcIsStringContains(result, "500", Brand);
		
		
		/********** Minimum Deposit Test ************
		 ** FasaPay Deposit - Minimum is 1**/
		Thread.sleep(1000);
		String url_fasa = cpurl.concat("cp/api/deposit/fasapay_payment");
		result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
		token = result.substring(18, result.length()-2); 
		System.out.println("token is: "+token);
		header = new HashMap<String, String>();
		header.put("Content-Type", "application/json; charset=UTF-8");
		header.put("Accept", "application/json, text/plain, */*");
		header.put("Cookie", cp_cookie);
		header.put("token", token);
		
		System.out.println("\n===========>>>API Test FasaPay Deposit 50");
		body = "{\"mt4Account\":"+account+",\"operateAmount\":50,\"applicationNotes\":\"01Qm\"}"; 	
		result = RestAPI.commonPostAPI(url_fasa,header,body);
		Utils.funcIsStringContains(result, "sci.fasapay.com", Brand);
		
		System.out.println("\n===========>>>API Test FasaPay Deposit 1");	
		result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
		token = result.substring(18, result.length()-2); 
		header.put("token", token);
		body = "{\"mt4Account\":"+account+",\"operateAmount\":1,\"applicationNotes\":\"01Qm\"}"; 		
		result = RestAPI.commonPostAPI(url_fasa,header,body);
		Utils.funcIsStringContains(result, "500", Brand);
		
		
		/********** Minimum Deposit Test ************
		 ** MobilePay Deposit - Minimum is 1**/
		Thread.sleep(1000);
		String url_mob = cpurl.concat("cp/api/deposit/mobile_payment");
		header = new HashMap<String, String>();
		header.put("Content-Type", "application/json; charset=UTF-8");
		header.put("Accept", "application/json, text/plain, */*");
		header.put("Cookie", cp_cookie);
		
		System.out.println("\n===========>>>API Test MobilePay Deposit 50");
		body = "{\"mt4Account\":"+account+",\"operateAmount\":50,\"applicationNotes\":\"10Qm\",\"paymentMethod\":\"mobile\",\"depositAmount\":\"100\",\"rate\":2}"; 	
		result = RestAPI.commonPostAPI(url_mob,header,body);
		Utils.funcIsStringContains(result, "transferpay.paylomo.net", Brand);
		
		System.out.println("\n===========>>>API Test MobilePay Deposit 1");	
		body = "{\"mt4Account\":"+account+",\"operateAmount\":1,\"applicationNotes\":\"10Qm\",\"paymentMethod\":\"mobile\",\"depositAmount\":\"2\",\"rate\":2}"; 		
		result = RestAPI.commonPostAPI(url_mob,header,body);
		Utils.funcIsStringContains(result, "500", Brand);
			
		
		//Update the user's country to Vietnum(6991)
		RestAPI.testPostUpdateCountry(urlbase, cookie, userId, "6991");
		
		/********** Minimum Deposit Test ************
		 ** Vietnum Deposit - Minimum is 50**/
		Thread.sleep(1000);
		String url_vn = cpurl.concat("cp/api/deposit/vietnam_bankwire_payment");
		result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
		token = result.substring(18, result.length()-2); 
		System.out.println("token is: "+token);
		header = new HashMap<String, String>();
		header.put("Content-Type", "application/json; charset=UTF-8");
		header.put("Accept", "application/json, text/plain, */*");
		header.put("Cookie", cp_cookie);
		header.put("token", token);
		
		System.out.println("\n===========>>>API Test Vietnum Deposit 50");
		body = "{\"mt4Account\":"+account+",\"operateAmount\":50,\"applicationNotes\":\"10Qm!\",\"paymentChannel\":\"\",\"depositAmount\":\"500.00\",\"rate\":10}"; 	
		result = RestAPI.commonPostAPI(url_vn,header,body);
		Utils.funcIsStringContains(result, "true", Brand);
		
		System.out.println("\n===========>>>API Test Vietnum Deposit 1");	
		result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
		token = result.substring(18, result.length()-2); 
		header.put("token", token);
		body = "{\"mt4Account\":"+account+",\"operateAmount\":1,\"applicationNotes\":\"10Qm!\",\"paymentChannel\":\"\",\"depositAmount\":\"10.00\",\"rate\":10}"; 		
		result = RestAPI.commonPostAPI(url_vn,header,body);
		Utils.funcIsStringContains(result, "500", Brand);

		//Update the user's country to Thailand (6163)
		RestAPI.testPostUpdateCountry(urlbase, cookie, userId, "6163");
		
		/********** Minimum Deposit Test ************
		 ** Thai Deposit - Minimum is 50**/
		Thread.sleep(1000);
		String url_zota = cpurl.concat("cp/api/deposit/zotapay_payment");
		String url_ptoday = cpurl.concat("cp/api/deposit/paytoday_payment");
		result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
		token = result.substring(18, result.length()-2); 
		System.out.println("token is: "+token);
		header = new HashMap<String, String>();
		header.put("Content-Type", "application/json; charset=UTF-8");
		header.put("Accept", "application/json, text/plain, */*");
		header.put("Cookie", cp_cookie);
		header.put("token", token);
		
		System.out.println("\n===========>>>API Test Thai Zotapay Deposit 50");
		body = "{\"mt4Account\":"+account+",\"operateAmount\":50,\"applicationNotes\":\"10Qm!\",\"paymentChannel\":\"1\",\"depositAmount\":\"500.00\",\"rate\":10}"; 	
		result = RestAPI.commonPostAPI(url_zota,header,body);
		Utils.funcIsStringContains(result, "true", Brand);
		
		System.out.println("\n===========>>>API Test Thai Zotapay Deposit 1");	
		result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
		token = result.substring(18, result.length()-2); 
		header.put("token", token);
		body = "{\"mt4Account\":"+account+",\"operateAmount\":1,\"applicationNotes\":\"10Qm!\",\"paymentChannel\":\"1\",\"depositAmount\":\"10.00\",\"rate\":10}"; 		
		result = RestAPI.commonPostAPI(url_zota,header,body);
		Utils.funcIsStringContains(result, "500", Brand);
		
		
		System.out.println("\n===========>>>API Test Thai Paytoday Deposit 50");	
		result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
		token = result.substring(18, result.length()-2); 
		header.put("token", token);
		body = "{\"mt4Account\":"+account+",\"operateAmount\":50,\"applicationNotes\":\"10Qm!\",\"paymentChannel\":\"2\",\"depositAmount\":\"500.00\",\"rate\":10,\"bankCode\":\"BBL\"}"; 		
		result = RestAPI.commonPostAPI(url_ptoday,header,body);
		Utils.funcIsStringContains(result, "www.paytoday.co.th", Brand);
		
		System.out.println("\n===========>>>API Test Thai Paytoday Deposit 1");	
		result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
		token = result.substring(18, result.length()-2); 
		header.put("token", token);
		body = "{\"mt4Account\":"+account+",\"operateAmount\":1,\"applicationNotes\":\"10Qm!\",\"paymentChannel\":\"2\",\"depositAmount\":\"10.00\",\"rate\":10,\"bankCode\":\"BBL\"}"; 		
		result = RestAPI.commonPostAPI(url_ptoday,header,body);
		Utils.funcIsStringContains(result, "500", Brand);
		
		//Update the user's country to Malaysia(5015)
		RestAPI.testPostUpdateCountry(urlbase, cookie, userId, "5015");
		
		/********** Minimum Deposit Test ************
		 ** Malaysia Deposit - Minimum is 50**/
		Thread.sleep(1000);
		String url_malay = cpurl.concat("cp/api/deposit/malaysia_bankwire_payment");
		result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
		token = result.substring(18, result.length()-2); 
		System.out.println("token is: "+token);
		header = new HashMap<String, String>();
		header.put("Content-Type", "application/json; charset=UTF-8");
		header.put("Accept", "application/json, text/plain, */*");
		header.put("Cookie", cp_cookie);
		header.put("token", token);
		
		System.out.println("\n===========>>>API Test Malaysia Zotapay Deposit 50");
		body = "{\"mt4Account\":"+account+",\"operateAmount\":50,\"applicationNotes\":\"10Qm!\",\"paymentChannel\":\"1\",\"depositAmount\":\"500.00\",\"rate\":10}"; 	
		result = RestAPI.commonPostAPI(url_malay,header,body);
		Utils.funcIsStringContains(result, "true", Brand);
		
		System.out.println("\n===========>>>API Test Malaysia Zotapay Deposit 1");	
		result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
		token = result.substring(18, result.length()-2); 
		header.put("token", token);
		body = "{\"mt4Account\":"+account+",\"operateAmount\":1,\"applicationNotes\":\"10Qm!\",\"paymentChannel\":\"1\",\"depositAmount\":\"10.00\",\"rate\":10}"; 		
		result = RestAPI.commonPostAPI(url_malay,header,body);
		Utils.funcIsStringContains(result, "500", Brand);
		
		
		System.out.println("\n===========>>>API Test Malaysia paytrust Deposit 50");	
		result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
		token = result.substring(18, result.length()-2); 
		header.put("token", token);
		body = "{\"mt4Account\":"+account+",\"operateAmount\":50,\"applicationNotes\":\"10Qm!\",\"paymentChannel\":\"2\",\"depositAmount\":\"500.00\",\"rate\":10,\"bankCode\":\"BBL\"}"; 		
		result = RestAPI.commonPostAPI(url_malay,header,body);
		//Utils.funcIsStringContains(result, "www.paytoday.co.th", Brand);
		Utils.funcIsStringContains(result, "widget.paytrust88.com", Brand);
		
		System.out.println("\n===========>>>API Test Malaysia paytrust Deposit 1");	
		result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
		token = result.substring(18, result.length()-2); 
		header.put("token", token);
		body = "{\"mt4Account\":"+account+",\"operateAmount\":1,\"applicationNotes\":\"10Qm!\",\"paymentChannel\":\"2\",\"depositAmount\":\"10.00\",\"rate\":10,\"bankCode\":\"BBL\"}"; 		
		result = RestAPI.commonPostAPI(url_malay,header,body);
		Utils.funcIsStringContains(result, "500", Brand);
		
		
		//Update the user's country to Nigeria(5796)
		RestAPI.testPostUpdateCountry(urlbase, cookie, userId, "5796");
		
		/********** Minimum Deposit Test ************
		 ** Nigeria Deposit - Minimum is 50**/
		Thread.sleep(1000);
		String url_nige = cpurl.concat("cp/api/deposit/nigeria_bankwire_payment");
		result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
		token = result.substring(18, result.length()-2); 
		System.out.println("token is: "+token);
		header = new HashMap<String, String>();
		header.put("Content-Type", "application/json; charset=UTF-8");
		header.put("Accept", "application/json, text/plain, */*");
		header.put("Cookie", cp_cookie);
		header.put("token", token);
		
		System.out.println("\n===========>>>API Test Nigeria Deposit 50");
		body = "{\"mt4Account\":"+account+",\"operateAmount\":50,\"applicationNotes\":\"10Qm!\",\"depositAmount\":\"500.00\",\"rate\":10}"; 	
		result = RestAPI.commonPostAPI(url_nige,header,body);
		Utils.funcIsStringContains(result, "true", Brand);
		
		System.out.println("\n===========>>>API Test Nigeria Deposit 1");	
		result = RestAPI.testPostRequireToken(cpurl, cp_cookie);
		token = result.substring(18, result.length()-2); 
		header.put("token", token);
		body = "{\"mt4Account\":"+account+",\"operateAmount\":1,\"applicationNotes\":\"10Qm!\",\"depositAmount\":\"10.00\",\"rate\":10}"; 		
		result = RestAPI.commonPostAPI(url_nige,header,body);
		Utils.funcIsStringContains(result, "500", Brand);
		
	}
	
	/* Added by Alex L on 04/06/2020
	 * This test is to search all web users that have formal account groups on all servers,
	 * and replace the groups with TEST group.
	 */
	@Parameters({"AdminURL","AdminName","AdminPass", "TestEnv", "Brand","Regulator"})
	@Test(priority=0)
	public void APIUpdateTestAccGroup(String AdminURL, String AdminName, String AdminPass, String TestEnv, String brand, String regulator, Method method) throws Exception
	{

		regulator = regulator.toUpperCase();
		String v_Brand = GlobalMethods.getPreVBrand(regulator, brand);		
		
		//Admin login
		AdminURL = Utils.ParseInputURL(AdminURL);
		String cookie = RestAPI.testPostForAdminCookie(AdminURL, AdminName, AdminPass, brand, regulator, TestEnv);
		
		//Change regulator
		RestAPI.testPostSwitchRegulator(v_Brand, AdminURL, cookie);
		
		RestAPI.funcAPIUpdateTradingAccountGroup( TestEnv,  v_Brand,  AdminURL, cookie);
	}
	
	@Parameters({"AdminURL","AdminName","AdminPass", "TestEnv", "Brand"})
	@Test(priority=0)
	public void APIUpdateAccPassword(String AdminURL, String AdminName, String AdminPass, String TestEnv, String Brand, String Regulator, Method method) throws Exception
	{

		AdminURL = Utils.ParseInputURL(AdminURL);
		String cookie = RestAPI.testPostForAdminCookie(AdminURL, AdminName, AdminPass, Brand, Regulator, TestEnv);

		//Change regulator
		RestAPI.testPostSwitchRegulator(Brand, AdminURL, cookie);

		//Get dataSourceId
		//String[] dataSourceId =Utils.getDataSourceId(Brand);
		String dataSourceId = "903";
		
		String projectPath = System.getProperty("user.dir");
		//System.out.println(projectPath);
		//read email Id from excel
		String dataPath;
		dataPath = "/src/main/resources/vantagecrm/Data/Accounts.xlsx";

		File file=new File(projectPath + dataPath);
		try {
			InputStream is = new FileInputStream(file);
			XSSFWorkbook wb = new XSSFWorkbook(is);
			DataFormatter df = new DataFormatter();

			XSSFSheet sheetEmailId = wb.getSheet("Sheet1");

			for(int i=1; i<=sheetEmailId.getLastRowNum(); i++) {

				String accountNo = df.formatCellValue(sheetEmailId.getRow(i).getCell(0));
				if (accountNo.length()!=0)
				{
					System.out.println("---->>>Going to reset pw for: "+accountNo);
					RestAPI.funcAPIUpdateTradingAccountPassword( TestEnv,  Brand,  AdminURL, cookie, accountNo, dataSourceId);
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
	
	
	@Parameters({"AdminURL","AdminName","AdminPass", "TestEnv", "Brand", "Regulator"})
	@Test(priority=0)
	public void APICheckTestAccGroup(String AdminURL, String AdminName, String AdminPass, String TestEnv, String Brand, String Regulator, Method method) throws Exception
	{

		AdminURL = Utils.ParseInputURL(AdminURL);
		String cookie = RestAPI.testPostForAdminCookie(AdminURL, AdminName, AdminPass, Brand, Regulator, TestEnv);
		
		//Change regulator
		RestAPI.testPostSwitchRegulator(Brand, AdminURL, cookie);
		
		RestAPI.funcAPICheckNonTestAccountGroup( TestEnv,  Brand,  AdminURL, cookie);
	}
	
	
	public static String funcParseResult(String result, String TestEnv, String item) throws Exception
	{
		String value="";
		String[] entry=null;
		//parse the result and get the column we need
		if(result.contains(item))
        result = result.substring(result.indexOf(item),result.length()); 
        String[] b=result.split(",");
        
        if(!TestEnv.equalsIgnoreCase("test")) {
        	value = b[0].substring(b[0].indexOf("=")+1,b[0].length()).trim();    
		}else {
			value = b[0].substring(b[0].indexOf(":")+1,b[0].length()).trim();       
		}
        
        if(value.contains("\"")) {
        	value = value.replace("\"", "");
        }
        //System.out.println("\n"+ "Result converted is: " + value);

		
		return value;
	}	
		
	public static String funcQueryCountryFromName(String real_name, String cookie, String TestEnv) throws Exception
	{
		System.out.println("\n===========>>>Entering funcQueryCountryFromName");
		String body="", country="";
		String url = urlbase.concat("individual/query_individualList");
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json; charset=UTF-8");
		header.put("Accept", "application/json");
		header.put("Cookie", cookie);
        body = "{\"limit\":10,\"pageNo\":1,\"offset\":0,\"order\":\"asc\",\"search\":{\"returnTime\":\"0\",\"subordinate\":\"\",\"agentuserQuery\":\"\",\"search_type\":\"real_name\",\"userQuery\":\""+real_name+"\",\"directLevel\":\"5\",\"user_id\":\"\",\"org_id\":\"\"}}"; 
			
		String result = RestAPI.commonPostAPI(url,header,body);

		country = funcParseResult(result, TestEnv, "country_name");
		return country;
	}	
	
	public static String funcQueryWebSourceById(String uid, String db, String cookie, String TestEnv) throws Exception
	{
		String entry[]=null;
		String webSource="";
		System.out.println("\n===========>>>Entering funcQueryWebSourceById");
		//For getting title,cpa,cxd
		String selectSql2="select webSource FROM tb_user_extends where user_id= \""+uid+"\";";
		String result = DBUtils.funcReadDBReturnAll(db,selectSql2, TestEnv);
		
		//parse the result and get the column we need
        result = result.substring(1, result.length()-1); 

		System.out.println("\n"+ "Result converted is: " + result);
		
		if(!TestEnv.equalsIgnoreCase("test")) {
			entry = result.split("=");     
		}else {
		    entry = result.split(":");        
		}

		webSource = entry[1].trim();

		return webSource;
	}
	
	
	public static String funcQueryCPAById(String uid, String db, String cookie, String TestEnv) throws Exception
	{
		String entry[]=null;
		String cpa="";
		System.out.println("\n===========>>>Entering funcQueryCPAById");
		//For getting title,cpa,cxd
		String selectSql="select cpa FROM tb_user_extends where user_id= \""+uid+"\";";
		String result = DBUtils.funcReadDBReturnAll(db,selectSql, TestEnv);
		
		//parse the result and get the column we need
        result = result.substring(1, result.length()-1); 

		System.out.println("\n"+ "Result converted is: " + result);
		
		if(!TestEnv.equalsIgnoreCase("test")) {
			entry = result.split("=");     
		}else {
		    entry = result.split(":");        
		}

		cpa = entry[1].trim();

		return cpa;
	}	
	
	
	public static String funcQueryLeadSourceById(String uid, String db, String cookie, String TestEnv) throws Exception
	{
		String entry[]=null;
		String leadSource="";
		System.out.println("\n===========>>>Entering funcQueryLeadSourceById");
		//For getting title,cpa,cxd
		String selectSql="select leadSource FROM tb_user_extends where user_id= \""+uid+"\";";
		String result = DBUtils.funcReadDBReturnAll(db,selectSql, TestEnv);
		
		//parse the result and get the column we need
        result = result.substring(1, result.length()-1); 

		System.out.println("\n"+ "Result converted is: " + result);
		
		if(!TestEnv.equalsIgnoreCase("test")) {
			entry = result.split("=");     
		}else {
		    entry = result.split(":");        
		}

		leadSource = entry[1].trim();

		return leadSource;
	}
	
	public static String funcQueryAccountById(String userId,String currency,String Brand,String TestEnv) throws Exception
	{
		String selectSql,db="",result,account;
		if(Brand.equals("test")) {
		    selectSql="select mt4_account FROM tb_account_mt4 where user_id = \""+userId+"\" and apply_currency=\""+currency+"\" order by mt4_account desc limit 1;";
		}else {
			selectSql="select mt4_account FROM tb_account_mt4 where user_id = \'"+userId+"\' and apply_currency=\'"+currency+"\' order by mt4_account desc limit 1;";
		}

		db=Utils.getDBName(Brand)[1];
		
		result = DBUtils.funcReadDBReturnAll(db,selectSql, TestEnv);
		System.out.println("\n"+ "result is: " + result);
 		//parse the result and get the column we need
		if(Brand.equals("test")) {
			account = result.substring(result.indexOf(":")+1, result.length()-1).trim();
		}else {
			account = result.substring(result.indexOf("=")+1, result.length()-1).trim();
		}
		System.out.println("\n"+ currency +" mt4 account is: " + account);
		return account;
	}
	
	//@AfterClass(alwaysRun=true)
	void ExitBrowser()
	{
		
		Utils.funcLogOutAdmin(driver);
		//Close all browsers
		driver.quit();
	}

}
