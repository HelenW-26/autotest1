package newcrm.testcases.admintestcases;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import newcrm.adminapi.AdminAPI;
import newcrm.business.adminbusiness.AdminAPIBusiness;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.REGULATOR;
import newcrm.utils.AlphaServerEnv;
import newcrm.utils.encryption.EncryptUtil;
import vantagecrm.RestAPI;
import vantagecrm.Utils;

public class AdminAPITests {

	private AdminAPI admin;
	private AdminAPIBusiness admin_bs;
	
	@BeforeClass(alwaysRun = true)
	@Parameters(value= {"Brand"})
	public void initiEnv(String brand, ITestContext context) throws Exception {
		GlobalMethods.setEnvValues(brand);
		
	}
	
	//To be continued
	@Test
	@Parameters(value= {"AdminURL","AdminName","AdminPass","Regulator","TestEnv","Brand"})
	private void apiAddIBAndAudit(String AdminURL, String AdminName, String AdminPass,
			String regulator, String TestEnv, String brand) throws Exception {

		String testcrmID = "", salesID = "",commCode = "",account = "",accauditID = "";		
		boolean flag1 = true;
		
		if(TestEnv.equalsIgnoreCase("alpha")) {
			//ALPHA test crm info
			testcrmID = "1002365";
			commCode = "249";
		} else {
			//PROD test crm info
			testcrmID = "211943"; 
			commCode = "187";
		}
	
		admin = new AdminAPI(AdminURL, REGULATOR.getREGULATOR(regulator), AdminName, AdminPass,BRAND.getBRAND(brand),ENV.getENV(TestEnv));

		String email = "";
		String mobile = "";
		String countryCode = "";
		String server = "";
		String firstname = "";
		String lastname = "";
		String rebateGroup = "";
		String ibGroup = "";
		String currency = "";
		String sales = "";
		
		//read IB data from excel
		File file=new File(Utils.workingDir + "/src/main/resources/newcrm/data/csvDataSource/IBdata.xlsx");
		try {
			InputStream is = new FileInputStream(file);
			XSSFWorkbook wb = new XSSFWorkbook(is);
			DataFormatter df = new DataFormatter();
			XSSFSheet sheetIBdata = wb.getSheet("IBdata");
			//i=1 coz first line is reserved for header
			for(int i=1; i<=sheetIBdata.getLastRowNum(); i++) {
				
				firstname = df.formatCellValue(sheetIBdata.getRow(i).getCell(0));
				lastname = df.formatCellValue(sheetIBdata.getRow(i).getCell(1));
				email = df.formatCellValue(sheetIBdata.getRow(i).getCell(2));
				mobile = df.formatCellValue(sheetIBdata.getRow(i).getCell(3)); 
				countryCode = df.formatCellValue(sheetIBdata.getRow(i).getCell(4));
				server = df.formatCellValue(sheetIBdata.getRow(i).getCell(5));
				rebateGroup = df.formatCellValue(sheetIBdata.getRow(i).getCell(6));
				ibGroup = df.formatCellValue(sheetIBdata.getRow(i).getCell(7)); 
				currency = df.formatCellValue(sheetIBdata.getRow(i).getCell(8)); 
				sales = df.formatCellValue(sheetIBdata.getRow(i).getCell(9)); 
				salesID = df.formatCellValue(sheetIBdata.getRow(i).getCell(10)); 
				String realname = firstname + " "+ lastname;
				if (mobile.length() <1)
					mobile = "9999999";

				System.out.println("\n**********No."+i+" IB***********");
				/*
				 * System.out.println("===Fistname: "+firstname+"\n"+
				 * "===Lastname: "+lastname+"\n"+ "===Email: "+email+"\n"+
				 * "===Mobile: "+mobile+"\n"+ "===Country Code: "+countryCode+"\n"+
				 * "===Server: "+server+"\n"+ "===Rebate Group: "+rebateGroup+"\n"+
				 * "===IB Group: "+ibGroup+"\n"+ "===Currency: "+currency+"\n"+
				 * "===SalesID: "+salesID+"\n"+ "===Sales: "+sales+"\n"+
				 * "===Realname: "+realname+"\n");
				 */

				String encrypt_email = EncryptUtil.getAdminEmailEncrypt(email); 
				String encrypt_mobile = EncryptUtil.getAdminEmailEncrypt(mobile);


			    
				//Translate data source name to data source ID
				String ds_id = AlphaServerEnv.getDataSourceID(server, brand);
			    //System.out.println("===Data Source ID: "+ds_id);
				/*
				 * API query if email exists in Admin. 
				 * return true if available 
				 * return false if email existed in Admin
				 */
				flag1 = admin.testQueryEmailAvailability(encrypt_email);
				
				//If email available, add external user
				if (flag1) {
					//Admin API adding external user 
					admin.AddExternalUser(firstname, lastname, encrypt_email, encrypt_mobile, salesID, countryCode);
				}	
				
				//API query user_id by email 
				String userID = admin.testQueryUserIdByEmail(email);
			    System.out.println("===User_ID: "+userID);
					
				//Query for account since it's existing user
				//account = admin.testQueryTradingAccountByUserid(userID);
			    account = admin.testQueryTradingAccountByEmail(encrypt_email);
				System.out.println("===account is: "+account);

				if(!account.isEmpty()) {
					/*
					 * API upgrade client to IB in Admin. 
					 * return true if upgrade successfully 
					 * return false if failed to upgrade
					 */
					admin.testClientUpgradeIB(account, userID);
				}
	
				//API query account audit id by name 
				accauditID = admin.testQueryAccAuditIdByEmail(email);
			    System.out.println("===Account Audit ID: "+accauditID);
			    
				admin.auditAccountIB(realname,userID,accauditID,ds_id,rebateGroup,ibGroup,
						currency,encrypt_email, encrypt_mobile, salesID,sales,commCode);
				
				Thread.sleep(2000);
			
			}
			is.close();
			wb.close();
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}			

		
	}
	
	@Test
	@Parameters(value= {"AdminURL","AdminName","AdminPass","Regulator","Brand","TestEnv"})	
	public void apiUpdateAccGroupNew(String AdminURL, String AdminName, String AdminPass,
			String regulator, String brand, String TestEnv) throws Exception {		

		regulator = regulator.toUpperCase();
		TestEnv = TestEnv.toUpperCase();
		admin_bs = new AdminAPIBusiness(AdminURL,REGULATOR.valueOf(regulator),AdminName,AdminPass,ENV.valueOf(TestEnv),BRAND.valueOf(brand));
		
		admin_bs.APIUpdateTradingAccountToTestGroup();
		
	}
	
	@Test
	@Parameters(value= {"AdminURL","AdminName","AdminPass","Regulator","Brand","TestEnv"})	
	public void apiUpdateRebateAccGroup(String AdminURL, String AdminName, String AdminPass,
			String regulator, String brand, String TestEnv) throws Exception {		

		regulator = regulator.toUpperCase();
		TestEnv = TestEnv.toUpperCase();
		
		if(admin_bs == null)
			admin_bs = new AdminAPIBusiness(AdminURL,REGULATOR.valueOf(regulator),AdminName,AdminPass,ENV.valueOf(TestEnv),BRAND.valueOf(brand));
		
		admin_bs.APIUpdateRebateAccountToTestGroup();
		
	}
	
	//Obsolete tests
	@Parameters(value= {"AdminURL","AdminName","AdminPass","Regulator","Brand","TestEnv"})
	private void apiUpdateAccGroup(String AdminURL, String AdminName, String AdminPass,
			String regulator, String brand, String TestEnv) throws Exception {
		
		regulator = regulator.toUpperCase();
		String v_Brand = GlobalMethods.getPreVBrand(regulator, brand);		
		
		//Admin login
		AdminURL = Utils.ParseInputURL(AdminURL);
		String cookie = RestAPI.testPostForAdminCookie(AdminURL, AdminName, AdminPass, brand, regulator, TestEnv);
		
		//Change regulator
		RestAPI.testPostSwitchRegulator(v_Brand, AdminURL, cookie);
		
		RestAPI.funcAPIUpdateTradingAccountGroup( TestEnv,  v_Brand,  AdminURL, cookie);
	}
	
	@Test
	@Parameters(value= {"AdminURL","AdminName","AdminPass","Regulator","Brand","TestEnv"})
	private void apiLoginAdmin(String AdminURL, String AdminName, String AdminPass,
			String regulator, String brand, String TestEnv) throws Exception {
		
		regulator = regulator.toUpperCase();
		String v_Brand = GlobalMethods.getPreVBrand(regulator, brand);		
		System.out.println(AdminName);
		//Admin login
		AdminURL = Utils.ParseInputURL(AdminURL);
		//RestAPI.testPostForAdminCookie(AdminURL, AdminName, AdminPass, brand, regulator, TestEnv);
		TestEnv = TestEnv.toUpperCase();

		if(admin_bs == null)
			admin_bs = new AdminAPIBusiness(AdminURL,REGULATOR.valueOf(regulator),AdminName,AdminPass,ENV.valueOf(TestEnv),BRAND.valueOf(brand));

	}

	
	@Test
	@Parameters(value= {"AdminURL","AdminName","AdminPass","Regulator","Brand","TestEnv",
			"Account","Adjustment", "Type", "Currency"})	
	public void apiCashAdjustment(String AdminURL, String AdminName, String AdminPass,
			String regulator, String brand, String TestEnv, String account, String adjustment,
			String type, String currency) throws Exception {		

		regulator = regulator.toUpperCase();
		TestEnv = TestEnv.toUpperCase();
		AdminAPI admin = new AdminAPI(AdminURL, REGULATOR.valueOf(regulator), AdminName, AdminPass, BRAND.valueOf(brand), ENV.valueOf(TestEnv));
		admin.cashAdjustment(account, adjustment, type, currency);
		
	}
}
