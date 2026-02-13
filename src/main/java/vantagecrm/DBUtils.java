package vantagecrm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

//import org.json.simple.JSONObject;

import newcrm.global.GlobalProperties.BRAND;
import newcrm.global.GlobalProperties.ENV;
import newcrm.global.GlobalProperties.REGULATOR;
import newcrm.utils.db.DbUtils;
import utils.LogUtils;

public class DBUtils {
	
	public static String dbLocation = "cloud";
	
	public enum DBConn
	{
		
		TPDBConn("61.220.65.154:3306", "root", "4Rrft65"),
		//AUDBConn("192.168.66.171:3306", "root", "4Rrft65"),
		AUDBConn("db01.crm-alpha.com", "alpha_business_rw","LsiDHEAzA4W1jtJG8GWL9eDA0iHu58RZ"),
		ALPHAConn("db01.crm-alpha.com", "alpha_business_rw","LsiDHEAzA4W1jtJG8GWL9eDA0iHu58RZ");
		
		private String DBAddr;
		private String DBUser;
		private String DBPwd;
		
		private DBConn(String DBAddr, String DBUser, String DBPwd)
		{
			this.DBAddr = DBAddr;
			this.DBUser=DBUser;
			this.DBPwd=DBPwd;
		}
		
		public String getDBAddr()
		{
			return DBAddr;
		}
		
		public String getDBUser()
		{
		
			return DBUser;
		}	
		
		public String getDBPwd()
		{
		
			return DBPwd;
		}	
	
	}

	
	//Check DB status after clicking Next button
	public static String checkDBStatus(String fName, String TestEnv, String Brand) throws Exception
	{
		String userID = funcReadUserInBusinessDB(fName, TestEnv, Brand);
		funcReadUserInGlobalDB(userID, TestEnv, Brand);
		
		Thread.sleep(1000);
		return userID;
	}
	
	//Read Business Db -> tb_user for user inforamtion including useID. Return userID
	public static String funcReadUserInBusinessDB(String realName, String TestEnv, String Brand) throws Exception
	{
		
		String selectSql="select id,first_name,last_name,real_name,create_time,is_del from tb_user where real_name like '%" + realName +"%' and is_del!=1;";
	    
	    //Get Brand Global DB and Business DB
	    String brandDB[] = Utils.getDBName(Brand);
	    String userID = funcreadDB(brandDB[1], selectSql, TestEnv);
	    
	    return userID;
	}
	
	//Read Global DB->tb_user_login for status
	public static void funcReadUserInGlobalDB(String userID, String TestEnv, String Brand) throws Exception
	{

		String brandDB[] = Utils.getDBName(Brand);
		String selectSql="select user_id, create_time,  regulator, (client_status >> 7)%2 as DemoStatus, (client_status >> 6)%2 as POAStatus, " + 
				"(client_status >> 5)%2 as ID3CheckStatus, (client_status >> 4)%2 as WorldCheckStatus,  "  
				 + "(client_status & 15) as RegisterSteps from tb_user_login where user_id = " + userID +";";
		
		if(userID!=null && userID.length()>0)
		{
		
			funcreadDB(brandDB[0], selectSql, TestEnv); 
		}else
		{
			System.out.println("userID is null/empty. Can't ready database " + brandDB[0]);
		}
	}
	
	//Read Global DB->tb_user_login for Pro status
	public static String funcReadUserProStatusInGlobalDB(String userID, String TestEnv, String Brand) throws Exception
		{
			String proStatus = "";
			String brandDB[] = Utils.getDBName(Brand);
			String selectSql="SELECT pro_client_status,user_id,Client_status," +
			"case " +
			"when (pro_client_status >> 6)%2 = 1 then 'WealthTest' " +
			"when (pro_client_status >> 6)%2 = 0 then 'Start/SophisticatedTest' " +
			"end as ProTestType, (pro_client_status >> 5)%2 as WealthPassStatus, (pro_client_status >> 4)%2 as SophisticatedPassStatus,  (pro_client_status & 15) as ProCheckSteps " +
			"FROM tb_user_login where user_id = " + userID +";";

			
			if(userID!=null && userID.length()>0)
			{
			
				proStatus = funcreadDB(brandDB[0], selectSql, TestEnv); 
			}else
			{
				System.out.println("userID is null/empty. Can't ready database " + brandDB[0]);
			}
			
			return proStatus;
		}
	
	 public static String funcreadDB(String dbName, String selectSql, String testEnv) throws Exception
	 {
		 
		 Connection connection = null;
		 
		 DBConn dbInfo = getDBInfo(dbLocation); 
		 
		 String serverIP=dbInfo.getDBAddr();
		 String dbUser=dbInfo.getDBUser();
		 String dbPwd=dbInfo.getDBPwd();
		 
		 String userID="";
		
		 String connInfo = "jdbc:mysql://"+ serverIP + "/"+dbName;		 
		 		 
/*		 if(dbLocation.equalsIgnoreCase("cloud"))
		 {*/
			 connInfo = connInfo + "?useSSL=false";
	/* } */
		 
		 // System.out.println("connInfo:" + connInfo);
		
		 //System.out.println(connInfo + "--" + dbUser + "--" + dbPwd);

			if(testEnv.equalsIgnoreCase("test")||(testEnv.equalsIgnoreCase("alpha")))
			{
				try
			    {       
			        //Class.forName("com.mysql.jdbc.Driver");
					Class.forName("com.mysql.cj.jdbc.Driver");
			        connection = DriverManager.getConnection(connInfo, dbUser, dbPwd);
			  			        
			        // Create and execute a SELECT SQL statement.		         		
		            Statement statement = connection.createStatement();
		            System.out.println(selectSql);
		            ResultSet resultSet = statement.executeQuery(selectSql);
		            ResultSetMetaData resultMD=resultSet.getMetaData();
		            	            
		            if(!resultSet.next())
		            {
		            	System.out.println("\n"+dbName + " doesn't return any results");
		        
		            }else
		            {
		            	System.out.println("\n"+dbName + " returns the following results:");	            	
		            	resultSet.beforeFirst();
		        
		            	// Print results from select statement
		                while (resultSet.next())
		                {
		                	
		    	          for(int i=1;i<=resultMD.getColumnCount();i++)
		    	          {
		    	        	  if(i>1) System.out.print(", ");
		    	        	  
		    	        	  System.out.print(resultMD.getColumnName(i) + ": " + resultSet.getString(i));
		    	          }
		    	          
		    	          System.out.println();
		    	          userID=resultSet.getString(1);
		    	          
		    	         	    	        		    	    		                	
		                }
	          
		            }
		           
		            statement.close();
		            connection.close();
			    }
			    catch(Exception e)
			    {
			        e.printStackTrace();
			    }		
			}else if ((testEnv.equalsIgnoreCase("beta"))||(testEnv.equalsIgnoreCase("prod"))) {
				Thread.sleep(2000);
				userID=RestAPI.APIReadDB(dbName,selectSql,testEnv);
			}
		 
		
		 return userID;
	 }
	

	 public static String[] readDB(String selectSQL, String testEnv,String Brand) throws Exception
	 {
		 String dbArray[]=Utils.getDBName(Brand);
		 
		 String userID[]=new String[dbArray.length];
		 
		 for(int i=0; i<dbArray.length; i++)
		 {
			 userID[i]=funcreadDB(dbArray[i], selectSQL, testEnv); 
		 }
		 
		 return userID;
		 
	 }

	 public static String funcReadDBReturnAll(String dbName, String selectSql, String testEnv) throws Exception
	 {
		 
		 Connection connection = null;  
		 ArrayList<String> listNames = new ArrayList<String>();
		 String result = "";
		 
		 DBConn dbInfo = getDBInfo(dbLocation); 
		 
		 String serverIP=dbInfo.getDBAddr();
		 String dbUser=dbInfo.getDBUser();
		 String dbPwd=dbInfo.getDBPwd();
		 
		 String connInfo = "jdbc:mysql://"+ serverIP + "/"+dbName;
		 connInfo = connInfo + "?useSSL=false";

			if(testEnv.equalsIgnoreCase("test")||(testEnv.equalsIgnoreCase("alpha")))
			{
				try
			    {       
			       // Class.forName("com.mysql.jdbc.Driver");
					Class.forName("com.mysql.cj.jdbc.Driver");
			        connection = DriverManager.getConnection(connInfo, dbUser, dbPwd);
			  			        
			        // Create and execute a SELECT SQL statement.		         		
		            Statement statement = connection.createStatement();
		            ResultSet resultSet = statement.executeQuery(selectSql);
		            ResultSetMetaData resultMD=resultSet.getMetaData();
		            	            
		            if(!resultSet.next())
		            {
		            	System.out.println("\n"+dbName + " doesn't return any results");
		        
		            }else
		            {
		            	//System.out.println("\n"+dbName + " returns the following results:");	            	
		            	resultSet.beforeFirst();
		        
		            	// Print results from select statement
		                while (resultSet.next())
		                {
		                	
		    	          for(int i=1;i<=resultMD.getColumnCount();i++)
		    	          {
		    	        	  listNames.add(resultMD.getColumnName(i) + ": " + resultSet.getString(i)); 
		    	          }
		    	          
		    	              	        		    	    		                	
		                }
	          
		            }
		           
		            statement.close();
		            connection.close();
			    }
			    catch(Exception e)
			    {
			        e.printStackTrace();
			    }	
				result = listNames.toString();
				
			}else if ((testEnv.equalsIgnoreCase("beta"))||(testEnv.equalsIgnoreCase("prod"))) {
				Thread.sleep(2000);
				result=RestAPI.APIReadDB(dbName,selectSql,testEnv);
			}
		 
		
		 return result;
	 }
	    
	
		//Update database to change deposit status for online deposit method 
		public static void updateSQLDepositStatus(String mt4Account, String testEnv, String Brand) throws SQLException, ClassNotFoundException {
			Connection connection = null;
			String payment_id="";
			
			if(testEnv.equalsIgnoreCase("test")||testEnv.equalsIgnoreCase("alpha"))
			{
				
				 DBConn dbInfo = getDBInfo(dbLocation); 
				 
				 String serverIP=dbInfo.getDBAddr();
				 String dbUser=dbInfo.getDBUser();
				 String dbPwd=dbInfo.getDBPwd();
				 String dbName = Utils.getDBName(Brand)[1];
				 
				 String connInfo = "jdbc:mysql://"+ serverIP + "/"+dbName;
				 //System.out.println("connInfo:" + connInfo);
				 
				
				try
			    {       
			        //Class.forName("com.mysql.jdbc.Driver");
					Class.forName("com.mysql.cj.jdbc.Driver");
			        System.out.println("Driver Loaded");
			        
			        connection = DriverManager.getConnection(connInfo,dbUser,dbPwd);
			        
			        //Database Name - testDB, Username - "root", Password - ""
			        System.out.println("Connected...");        
			        
			        // Create and execute a SELECT SQL statement.
		            //String selectSql = "SELECT * FROM db_au_vgp.tb_payment_deposit where user_id=69327 ORDER by create_time desc";
			        //1: submitted ?? 2: Pending ??  3 Submit to 3rd party
			        String selectSql = "SELECT * FROM tb_payment_deposit where mt4_account="+mt4Account+" and status in (1,2,3) ORDER by create_time desc";
		
		            try (Statement statement = connection.createStatement();
		            ResultSet resultSet = statement.executeQuery(selectSql)) {
		                // Print results from select statement
		                while (resultSet.next())
		                {
		                	/*if (resultSet.getString("status").equals("1") || resultSet.getString("status").equals("2")) {*/
		    	                payment_id = resultSet.getString("id");
		                        System.out.println("Payment id: "+payment_id + " User id: "
		                        + resultSet.getString(2));
		                        break;
								/* } */
		                }
		                if (payment_id.equals("")) {
		            		System.out.println("No pending audit status!");
		            	}else
		            	{
			               //execute a UPDATE SQL statement.
				            String updateSql = "UPDATE tb_payment_deposit SET status=6 WHERE id="+payment_id;
			                int result = statement.executeUpdate(updateSql);
			                System.out.println("Update succeffully: "+result);
		            	}
		            }
		            connection.close();
			    }
			    catch(Exception e)
			    {
			        e.printStackTrace();
			    }		
			}
		}
		
		public static String funcupdateDB(String dbName, String updateSql, String testEnv) throws Exception
		 {
			 
			 Connection connection = null;
			 DBConn dbInfo = getDBInfo(dbLocation); 
			 
			 String serverIP=dbInfo.getDBAddr();
			 String dbUser=dbInfo.getDBUser();
			 String dbPwd=dbInfo.getDBPwd();
			 
			 String sqlReturn="";
			 
			 String connInfo = "jdbc:mysql://"+ serverIP + "/"+dbName;
			 
		
				if(testEnv.equalsIgnoreCase("test")||testEnv.equalsIgnoreCase("alpha"))
				{
					try
				    {       
				        //Class.forName("com.mysql.jdbc.Driver");
						Class.forName("com.mysql.cj.jdbc.Driver");
				        connection = DriverManager.getConnection(connInfo, dbUser, dbPwd);
				  			        
				        // Create and execute a SELECT SQL statement.		         		
			            Statement statement = connection.createStatement();
			           statement.executeUpdate(updateSql);
			         
			            statement.close();
			            connection.close();
				    }
				    catch(Exception e)
				    {
				        e.printStackTrace();
				    }		
				}else 
				{
					System.out.println("Can't Update Online Data.");
				}
			 
			
			 return sqlReturn;
		 }
		
		//Added by Alex Liu for making query like 'select ...'
		public static String funcQueryDB(String testEnv, String selectSql, String Brand) throws Exception
		 {
			 
			 Connection connection = null;
			 DBConn dbInfo = getDBInfo(dbLocation); 
			 
			 String serverIP=dbInfo.getDBAddr();
			 String dbUser=dbInfo.getDBUser();
			 String dbPwd=dbInfo.getDBPwd();
			 String dbName = Utils.getDBName(Brand)[1];
			 
			 String sqlReturn="";
			 
			 String connInfo = "jdbc:mysql://"+ serverIP + "/"+dbName;
			 
		
				if(testEnv.equalsIgnoreCase("test")||testEnv.equalsIgnoreCase("alpha"))
				{
					System.out.println("\nGoing to make jdbc connection...");  
					
					try
				    {       
				       // Class.forName("com.mysql.jdbc.Driver");
						Class.forName("com.mysql.cj.jdbc.Driver");
				        connection = DriverManager.getConnection(connInfo, dbUser, dbPwd);
				  			        
				        // Create and execute a SELECT SQL statement.		         		
			            Statement statement = connection.createStatement();
			            ResultSet rs = statement.executeQuery(selectSql);
			            while (rs.next()) {
			            	sqlReturn = rs.getString(1);
			            }
			            statement.close();
			            connection.close();
				    }
				    catch(Exception e)
				    {
				        e.printStackTrace();
				    }		
				}else 
				{
					System.out.println("Not able to access online data");
				}
			 
			
			 return sqlReturn;
		 }
		
		public static DBConn getDBInfo(String dbLocation)
		{
			DBConn dbInfo;
			
			switch(dbLocation.toLowerCase())
			{
			
			  case "taipei":
			  dbInfo = DBConn.TPDBConn;
			  break;
			  case "cloud":
			  dbInfo = DBConn.ALPHAConn;
			  break;
			 
			default:
				dbInfo = DBConn.AUDBConn;
			}
			
			return dbInfo;
		}
		
	    public static void readEmailvUserName(String userName, String Brand, String testEnv, int numberOfEmail) throws Exception
	    {
	 
	    	String selectSql = "select subject, template_invoke_name, create_time from tb_mail_send_log where vars like  '%MAILMAIL%' order by create_time desc limit XNO";
	    	String processedSql;
	    	String dbName="";
	    		    		    	

	    	processedSql = selectSql.replace("MAILMAIL", userName);
	    	
	    	processedSql=processedSql.replace("XNO", Integer.toString(numberOfEmail));
	    	
	    	System.out.println(processedSql);
	    	
	    	dbName = Utils.getDBName(Brand)[1];     	
  	
	    	funcreadDB(dbName, processedSql, testEnv);
	    	
	    	
	    }

	    //Extracted from UpdateTradAcc.java. Return the number of MT4/5 Synchronization DBs. Like in AU, we have au, uk, au2, uk2, au3, uk3, mxt, mt5;
	    public static int getNumOfServers(String Brand)
	    {
	    	int numofServers =0;
	    	
			switch(Brand)
			{
				case "au":
				case "ky":
				case "vfsc":
				case "vfsc2":
				case "regulator2":
					
					numofServers=10;
					break;
					
				case "vt":
					numofServers=3;
					break;
				case "fca":
				case "fsa":
				case "svg":
					numofServers=2;
					break;
					
				default:
					numofServers=0;
					System.out.println("DBUtils.getNumOfServers doesn't support brand: " + Brand);
					break;
			}
	    	
	    	return numofServers;
	    	
	    	
	    }
	    
	    
	    
	   //Read Business Db -> tb_mail_send_log for fetching admin login verification code. Return the code.
		public static String funcReadVerifyCodeInBusinessDB(String TestEnv, String Brand) throws Exception
		{
			
			String selectSql="select vars from tb_mail_send_log where template_invoke_name like \'Verify_Code_For_CRM_login\' order by create_time desc limit 1;";
		    
		    //Get Brand Global DB and Business DB
		    String brandDB[] = Utils.getDBName(Brand);
		    String vars = funcreadDB(brandDB[1], selectSql, TestEnv);
		    
		    return vars;
		}
		
		//Read Business Db -> tb_mail_send_log for fetching admin login OTP and return the db vars content.
		public static String funcReadOTPInBusinessDB(String testEnv, String brand, String regulator, String adminName ) throws Exception
		{
			ENV enumEnv = ENV.valueOf(testEnv.toUpperCase());
			BRAND enumBrand = BRAND.valueOf(brand.toUpperCase());
			REGULATOR enumRegulator = REGULATOR.valueOf(regulator.toUpperCase());
			DbUtils obj = new DbUtils(enumEnv,enumBrand,enumRegulator);	
			
			String selectSql="select vars from tb_mail_send_log where template_invoke_name like \'verification_code\' and vars like '%" + adminName + "%' order by create_time desc limit 1;";
			    
			JSONObject value = obj.queryRegulatorDB(selectSql).getJSONObject(0);   
			return value.toString();
		}

        public static String funcReadResetPwdInBusinessDB(String testEnv, String brand, String regulator, String adminName ) throws Exception
		{
			ENV enumEnv = ENV.valueOf(testEnv.toUpperCase());
			BRAND enumBrand = BRAND.valueOf(brand.toUpperCase());
			REGULATOR enumRegulator = REGULATOR.valueOf(regulator.toUpperCase());
			DbUtils obj = new DbUtils(enumEnv,enumBrand,enumRegulator);

			String selectSql="select vars from tb_mail_send_log where template_invoke_name like \'Reset_Profile_Password\' and vars like '%" + adminName + "%' order by create_time desc limit 1;";

			JSONObject value = obj.queryRegulatorDB(selectSql).getJSONObject(0);
			return value.toString();
		}
		
		//Read db to get default regulator for admin login
	    public static String getDefaultRegulator(String AdminName, String Brand, String TestEnv, String Regulator) throws Exception
	    {
	    	String defaultRegulator="";
	    	ENV enumEnv = ENV.valueOf(TestEnv.toUpperCase());
			BRAND enumBrand = BRAND.valueOf(Brand.toUpperCase());
			REGULATOR enumRegulator = REGULATOR.valueOf(Regulator.toUpperCase());
			DbUtils obj = new DbUtils(enumEnv,enumBrand,enumRegulator);	
			
	    	String sql = "SELECT default_regulator FROM tb_user_inner where user_name ='" + AdminName + "' order by create_time desc limit 1;";
	    	
	    	JSONObject value = obj.queryGlobalDB(sql).getJSONObject(0);
			//System.out.println(value.toJSONString());
			defaultRegulator = (String) value.get("default_regulator");   
			
	    	return defaultRegulator;
	    }
	    
	   //Read Global Db -> tb_sms_history for fetching admin login verification code. Return the code.
		public static String funcReadSMSCodeInGlobalDB(String TestEnv, String Brand, String innerUserName) throws Exception
		{
			
			String selectSql="SELECT body FROM tb_sms_history where user_id in (select user_id from tb_user_inner where user_name ='DUMPUSERNAME') order by id desc limit 1;";
			
			//PREPARE selectSql
			selectSql=selectSql.replace("DUMPUSERNAME", innerUserName); 
		    
		    //Get Brand Global DB and Business DB
		    String brandDB[] = Utils.getDBName(Brand);		    
		 
		    String vars = funcreadDB(brandDB[0], selectSql, TestEnv);
		    
		    return vars;
		}

        public static void updateSafeValidate(String testEnv, String brand, String regulator,String userId)  throws Exception{
            String selectValidate = "select id,user_id,original_ip,original_city,new_ip,new_city,safe_validate,create_time,validate_time" +
                    " from tb_user_ip_city_change_info  where user_id = "+userId+" and safe_validate = 0 order by create_time desc;";

            ENV enumEnv = ENV.valueOf(testEnv.toUpperCase());
            BRAND enumBrand = BRAND.valueOf(brand.toUpperCase());
            REGULATOR enumRegulator = REGULATOR.valueOf(regulator.toUpperCase());
            DbUtils obj = new DbUtils(enumEnv,enumBrand,enumRegulator);
            JSONArray array = obj.queryGlobalDB(selectValidate);
            LogUtils.info("query safe validate sql is : " + selectValidate);
            if(!array.isEmpty()){
                String  updateSafeValidate = "update tb_user_ip_city_change_info set safe_validate = 1  where user_id = "+userId+" and safe_validate = 0 order by create_time desc;";
                obj.updateGlobalDB(updateSafeValidate);
            }

        }
}