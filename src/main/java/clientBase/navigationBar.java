package clientBase;


import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class navigationBar {
	
	private WebDriver driver;
	private String Brand;
	
	private String homeMenu;
	
	private String accountMainMenu;
	private String liveAccountMenu;
	private String addiAccountMenu;
	private String demoAccountMenu;
	
	private String fundsMenu;
	private String depositMenu;
	private String withdrawMenu;
	private String transferMenu;
	private String payDetailMenu;
	private String transHisMenu;
	private String trasacHisMenu;
	
	private String downMenu;
	
	private String profileMenu;
	private String myProfileMenu;
	private String changePwdMenu;
	
	private String proTraderMenu;
	private String buzzMenu;
	private String calendarMenu;	
	private String ideaMenu;
	private String viewMenu;
	
	private String contactUS;

	
	
	public navigationBar(WebDriver driver, String Brand)
	{
		this.driver = driver;
		this.Brand = Brand;
		
		switch(Brand)
		{
		case "vt":
			homeMenu="Home";
						
			accountMainMenu="Account";
			liveAccountMenu="Live accounts";
			addiAccountMenu="Open additional accounts";
			demoAccountMenu="Demo accounts";
						
			fundsMenu="Funds";
			depositMenu="Deposit funds";
			withdrawMenu="Withdraw funds";
			transferMenu="Transfer between accounts";
			payDetailMenu="Payment details";
			transHisMenu="Transfer history";
			trasacHisMenu="Transaction history";
						
			downMenu="Downloads";
						
			profileMenu="Profile";
			myProfileMenu="My profile";
			changePwdMenu="Change Client Portal password";
						
			proTraderMenu="ProTrader Tools";
			buzzMenu="Market buzz";
			calendarMenu="Economic calendar";	
			ideaMenu="Featured ideas";
			viewMenu="Analyst views";
			
			contactUS="Contact us";

			break;
			
		case "fsa":
		case "svg":
			homeMenu="HOME";
			
			accountMainMenu="ACCOUNTS";
			liveAccountMenu="LIVE ACCOUNTS";
			addiAccountMenu="ADDITIONAL ACCOUNTS";
			demoAccountMenu="DEMO ACCOUNTS";
						
			fundsMenu="FUNDS";
			depositMenu="Deposit funds".toUpperCase();
			withdrawMenu="Withdraw funds".toUpperCase();
			transferMenu="Transfer funds".toUpperCase();
			payDetailMenu="Payment details".toUpperCase();
			trasacHisMenu="Transaction history".toUpperCase();
			transHisMenu=trasacHisMenu;
						
			downMenu="Downloads".toUpperCase();
						
			//PUG ONLY: Profile menu and its submenus have been moved the right top
			profileMenu="Profile".toUpperCase();
			myProfileMenu="My profile".toUpperCase();
			changePwdMenu="Change password".toUpperCase();
			
			proTraderMenu="";
			buzzMenu="";
			calendarMenu="";	
			ideaMenu="";
			viewMenu="";

			contactUS="Contact us".toUpperCase();
			break;
			
			
			default:
				homeMenu="Home".toUpperCase();
				
				accountMainMenu="ACCOUNTS";
				liveAccountMenu="LIVE ACCOUNTS";
				addiAccountMenu="OPEN ADDITIONAL ACCOUNTS";
				demoAccountMenu="DEMO ACCOUNTS";
							
				fundsMenu="FUNDS";
				depositMenu="Deposit funds".toUpperCase();
				withdrawMenu="Withdraw funds".toUpperCase();
				transferMenu="TRANSFER BETWEEN ACCOUNTS";
				payDetailMenu="";
				trasacHisMenu="Transaction history".toUpperCase();
				transHisMenu="Transfer history".toUpperCase();
							
				downMenu="Downloads".toUpperCase();
							
				profileMenu="Profile".toUpperCase();
				myProfileMenu="My profile".toUpperCase();
				changePwdMenu="Change client portal password".toUpperCase();
							
				proTraderMenu="Pro Trader Tools".toUpperCase();
				buzzMenu="Market buzz".toUpperCase();
				calendarMenu="Economic calendar".toUpperCase();	
				ideaMenu="Featured ideas".toUpperCase();
				viewMenu="Analyst views".toUpperCase();
				
				contactUS="Contact us".toUpperCase();
				
		}
	}
	
	private WebElement getMainMenuEle(String menuText)
	{
		String elementLocator = "//span[contains(text(),'Account')]";
		WebElement tempEle = null;
		//div:nth-child(1) ul div:nth-child(1) li > div
		try
		{
			elementLocator = elementLocator.replace("Account", menuText);
			tempEle = driver.findElement(By.xpath(elementLocator));
		}catch(NoSuchElementException e)
		{
			System.out.println("Can't locate element with locator - " + elementLocator);
		}
		
		return tempEle;
		
	}
	
	private WebElement getSubMenuEle(String menuText)
	{
		String elementLocator = "//li[contains(text(),'Live Accounts')]";
		WebElement tempEle = null;
				
		try
		{
			elementLocator = elementLocator.replace("Live Accounts", menuText);
			tempEle = driver.findElement(By.xpath(elementLocator));
		}catch(NoSuchElementException e)
		{
			System.out.println("Can't locate element with locator - " + elementLocator);
		}
		return tempEle;
		
		
	}

	//Home: Main menu
	public WebElement getHomeEle()
	{
		
		return getMainMenuEle(homeMenu);
		
	}
	
	//Accounts: Main menu
	public WebElement getAccountEle()
	{
		
		return getMainMenuEle(accountMainMenu);
		
	}
	
	//Live Account/
	public WebElement getLiveAccountEle()
	{

		
		return getSubMenuEle(liveAccountMenu);
	}
	
	//Open additional accounts
	public WebElement getAddiAccountEle()
	{
		return getSubMenuEle(addiAccountMenu);
	}
	
	// Demo accounts 
	public WebElement getDemoAccountEle()
	{
		return getSubMenuEle(demoAccountMenu);
	}
	
	//Funds main menu
	public WebElement getFundsEle()
	{
		return getMainMenuEle(fundsMenu);
	}
	
	// Deposit funds 
	public WebElement getDepositMenuEle()
	{
		return getSubMenuEle(depositMenu);
	}
	
	// Withdraw funds 
	public WebElement getWithdrawMenuEle()
	{
		return getSubMenuEle(withdrawMenu);
	}
	
	// Transfer funds 
	public WebElement getTransferMenuEle()
	{
		return getSubMenuEle(transferMenu);
	}
	
	//Payment Details: only VT & PUG has this menu
	public WebElement getPayDetailMenuEle()
	{
		return getSubMenuEle(payDetailMenu);
	}
	
	//Transfer History
	public WebElement getTransferHisMenuEle()
	{
		return getSubMenuEle(transHisMenu);
	}	
	//Transaction History
	public WebElement getTrasactionHistoryMenuEle()
	{
		return getSubMenuEle(trasacHisMenu);
	}	
	//Downloads (Main menu)
	public WebElement getDownloadsEle()
	{
		return getMainMenuEle(downMenu);
	}
		
	//Profile (Main Menu)
	public WebElement getProfileEle()
	{
		return getMainMenuEle(profileMenu);
	}
		
	//My Profile
	public WebElement getMyProfileEle()
	{
		return getSubMenuEle(myProfileMenu);
	}	
	
	//Change Client Portal Passowrd
	public WebElement getChangePWDEle()
	{
		return getSubMenuEle(changePwdMenu);
	}	
	
	//ProTrader: Main menu, only AU & VT have this menu
	public WebElement getProTraderEle()
	{
		return getMainMenuEle(proTraderMenu);
	}
		
	//Market Buzz menu
	public WebElement getMarketBuzzEle()
	{
		return getSubMenuEle(buzzMenu);
	}	
	
	//Economic Calendar Menu
	public WebElement getEcoCalendarEle()
	{
		return getSubMenuEle(calendarMenu);
	}	
	
	//Featured Ideas
	public WebElement getFeaturedIdeaEle()
	{
		return getSubMenuEle(ideaMenu);
	}	
	//Analyst Views
	public WebElement getAnalystViewEle()
	{
		return getSubMenuEle(payDetailMenu);
	}	
	//Contact US (Main Menu)
	public WebElement getContactUS()
	{
		return getMainMenuEle(contactUS);
	}
		
	
}
