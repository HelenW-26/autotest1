package vantagekcm;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.Duration;
import java.util.Set;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;


public class Utils {
	public static String ChromePath="C:\\Vantage\\automation\\chromedriver_win32\\chromedriver.exe";
	public static String IEPath="";
	public static String FFPath="";
	public static String workingDir=System.getProperty("user.dir");
	//public static String ChromePath=workingDir+"\\chromedriver.exe";
	
	static final String emailSuffix="@test.com";

    protected static String registerUserName;  //User name / email prefix (they are the same for AU)
    protected static String registerUserNameVT;  //VT user name in Chinese
	protected static String addIBName;
	protected static String addClientName;
	protected static String addJointName;
  
	static final String SOURCE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static final String scSOURCE="许多读者都指出这是金庸小说的一个问题比如网上有人纠正说蚕豆花生两种作物都是中国本土所无而后才逐渐由国外传进来的蚕豆又名胡豆寒豆罗汉豆大概在元代才由波斯传入中国";
	static final String numberList="0123456789";
	
	
	static void funcLogInAdmin(WebDriver driver,String userName, String passWord, String Brand) throws Exception
	{

		driver.findElement(By.id("exampleInputEmail1")).clear();;
		driver.findElement(By.id("exampleInputEmail1")).sendKeys(userName);
		driver.findElement(By.id("password_login")).clear();
		driver.findElement(By.id("password_login")).sendKeys(passWord);
		driver.findElement(By.id("btnLogin")).click();
		
		//String text = driver.findElement(By.cssSelector("a.home")).getText();
		//assertEquals("Home",text);
		
		if(Brand.equalsIgnoreCase("vt"))
		{
			WebDriverWait wait03=new WebDriverWait(driver, Duration.ofSeconds(3));
			Actions aMenu=new Actions(driver);
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.header-profile"))).click();
			aMenu.moveToElement(driver.findElement(By.cssSelector("ul.dropdown-menu.dropdown-menu-right li.dropdown-submenu a"))).build().perform();
			Thread.sleep(1000);
			driver.findElement(By.linkText("English")).click();
			Thread.sleep(1000);
		}
		
	}
	
	//Functions
	

	
	static Boolean funcLogOutAdmin(WebDriver driver)
	{
		//Logout Admin
		
		try
		{
			driver.findElement(By.cssSelector("a.header-profer")).click();
			driver.findElement(By.linkText("logout")).click();
			return true;
			
		}catch (NoSuchElementException e)
		{
			return false;
		}
		
	}
	
	static void funcLogInTrader(WebDriver driver, String userName, String passWord, String Brand) throws Exception
	{
		if(Brand.equalsIgnoreCase("vt"))
		{
			Thread.sleep(1000);
			driver.findElement(By.id("language_title")).click();
			Thread.sleep(1000);
			driver.findElement(By.id("language_box")).click();
			Thread.sleep(1000);
		}
		
		driver.findElement(By.id("userName")).clear();
		driver.findElement(By.id("userName")).sendKeys(userName);
		driver.findElement(By.id("password_login")).clear();
		driver.findElement(By.id("password_login")).sendKeys(passWord);
		
		driver.findElement(By.id("btnSubmit")).click();
		
		//Need to remove after transition phase
	
		
	}
	
	static void funcLogOutTrader(WebDriver driver)
	{
		
		WebDriverWait wait01=new WebDriverWait(driver,Duration.ofSeconds(2));
		wait01.until(ExpectedConditions.elementToBeClickable(By.linkText("LOGOUT"))).click();
	}

	static void funcLogInCP(WebDriver driver, String userName, String passWord, String Brand) throws Exception
	{
		Thread.sleep(1000);
		driver.findElement(By.xpath("//input[@placeholder='Email']")).clear();
		driver.findElement(By.xpath("//input[@placeholder='Email']")).sendKeys(userName);
		driver.findElement(By.xpath("//input[@placeholder='Password']")).clear();
		driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys(passWord);
		
		driver.findElement(By.cssSelector("button.el-button")).click();
		Thread.sleep(1000);
	}
	
	static void funcLogOutCP(WebDriver driver) throws Exception
	{
		Thread.sleep(1000);
		WebDriverWait wait01=new WebDriverWait(driver,Duration.ofSeconds(2));
		wait01.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='header']//img[2]"))).click();
	}

	
	static BigDecimal splitAccount(String option) throws Exception
	{
		BigDecimal moneyAmount=new BigDecimal("0.00");
		String subStr;
		String[] strSplit;

		NumberFormat nf=NumberFormat.getInstance();
		Number n;
		
		
		if(option.contains("Commission Account"))
		{


			strSplit=new String[2];			
			strSplit=option.split(",");
			subStr=strSplit[1].trim();
			
			
			strSplit=subStr.split(" ");
			subStr=strSplit[0].substring(1);
			
			
		} else
		{
			strSplit=new String[3];
			strSplit=option.split(" ");
			subStr=strSplit[1].substring(2);		

			
		}	
		
		n=nf.parse(subStr); //remove thousand separator
		moneyAmount=new BigDecimal(n.toString());
		
		return moneyAmount;
	}


	//Login to Admin url and get cookie
	static String getAdminCookie(WebDriver driver,String userName, String passWord, String Brand) throws Exception
	{

		driver.findElement(By.id("exampleInputEmail1")).clear();;
		driver.findElement(By.id("exampleInputEmail1")).sendKeys(userName);
		driver.findElement(By.id("password_login")).clear();
		driver.findElement(By.id("password_login")).sendKeys(passWord);
		driver.findElement(By.id("btnLogin")).click();
		
		//String text = driver.findElement(By.cssSelector("a.home")).getText();
		//assertEquals("Home",text);
		Set <Cookie> coo = driver.manage().getCookies();
		System.out.println(coo);
		return coo.toString();
		
	}
	
	static String removeASIC(String nameOld)
	{
		String nameNew=nameOld;
		int i=nameOld.indexOf("(");
		if(i >0)
		{
			nameNew=nameOld.substring(0, i);
		}
		return nameNew;
	}
	
	
	//handlePopup should be removed after June 23.
	static void handlePopup(WebDriver driver, boolean flag) throws Exception
	{
		//flag==true: I Agree
		//flag==false: Remind me later->Close
		
		WebDriverWait wait10=new WebDriverWait(driver,Duration.ofSeconds(10));
		
		try
		{
			if(flag==false)
			{
			
				//Click Remind me later button in the popup dialog
				wait10.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Remind me later']"))).click();
				Thread.sleep(500);
				
				//Click Close button in the popup dialog
				wait10.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Close']"))).click();
				
			} else
			{
				//Click agree button in the popup dialog
				wait10.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='I Agree']"))).click();;
			}
			
		}catch(TimeoutException | NoSuchElementException e)
		{
			System.out.println("The logged in user has no popup dialog.");
			//e.printStackTrace();
		}
				
		
	}
	
	 
		public static void funcLogInKcmTrader(WebDriver driver, String userName, String passWord, String Brand) throws Exception
		{
			if(Brand.equalsIgnoreCase("vt"))
			{
				Thread.sleep(1000);
				driver.findElement(By.id("language_title")).click();
				Thread.sleep(1000);
				driver.findElement(By.id("language_box")).click();
				Thread.sleep(1000);
			}
			
			driver.findElement(By.id("email")).clear();
			driver.findElement(By.id("email")).sendKeys(userName);
			driver.findElement(By.id("password")).clear();
			driver.findElement(By.id("password")).sendKeys(passWord);
			
			driver.findElement(By.id("btnSubmit")).click();
			
			//Need to remove after transition phase
		
			
		}
		
		public static void funcLogOutKcmTrader(WebDriver driver)
		{
			
			WebDriverWait wait01=new WebDriverWait(driver,Duration.ofSeconds(2));
			wait01.until(ExpectedConditions.elementToBeClickable(By.linkText("LOGOUT"))).click();
		}
		
		static void checkLogBySSH(String logServer) {
		    //String host="192.168.66.180";
		    String user="root";
		    String password="4Rrft65";
		    String command1="cat /project/logs/kcm_myaccount/catalina.out |grep \"NABDirectPostController 2\"";
		    try{
		    	
		    	java.util.Properties config = new java.util.Properties(); 
		    	config.put("StrictHostKeyChecking", "no");
		    	JSch jsch = new JSch();
		    	Session session=jsch.getSession(user, logServer, 22);
		    	session.setPassword(password);
		    	session.setConfig(config);
		    	session.connect();
		    	System.out.println("SSH Connected..");
		    	
		    	Channel channel=session.openChannel("exec");
		        ((ChannelExec)channel).setCommand(command1);
		        channel.setInputStream(null);
		        ((ChannelExec)channel).setErrStream(System.err);
		        
		        InputStream in=channel.getInputStream();
		        channel.connect();
		        byte[] tmp=new byte[1024];
		        while(true){
		          while(in.available()>0){
		            int i=in.read(tmp, 0, 1024);
		            if(i<0)break;
		            System.out.print(new String(tmp, 0, i));
		          }
		          if(channel.isClosed()){
		            System.out.println("exit-status: "+channel.getExitStatus());
		            break;
		          }
		          try{Thread.sleep(1000);}catch(Exception ee){}
		        }
		        channel.disconnect();
		        session.disconnect();
		        System.out.println("DONE");
		    }catch(Exception e){
		    	e.printStackTrace();
		    }

	    }

	
}



