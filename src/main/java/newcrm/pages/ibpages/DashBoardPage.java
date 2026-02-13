package newcrm.pages.ibpages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.*;

import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties.DEPOSITMETHOD;
import newcrm.pages.Page;
import newcrm.pages.clientpages.LoginPage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import vantagecrm.Utils;

public class DashBoardPage extends Page {

	public DashBoardPage(WebDriver driver) {
		super(driver);
	}
	
	protected WebElement getAccountDiv() {
		return this.findClickableElemntByTestId("accountNumber");
	}

    protected WebElement getCurrentlySelectedAccountEle() {
        return assertElementExists(By.xpath("//div[@class='rebate_account']/*"), "Currently Selected IB Account");
    }

    public List<WebElement> getRecentlyOpenedTradingAccountsDiv() {
        return assertElementsExists(By.xpath("//tbody/tr/td[1]/div"), "Recently Opened Accounts Div");
    }
	
	public List<String> getAllAccounts(){
		ArrayList<String> accs = new ArrayList<>();
		WebElement div = this.getAccountDiv();
		div.click();
		List<WebElement> els = super.getAllOpendElements();
		for(WebElement e: els) {
			String acc = e.getAttribute("innerText");
			if(acc!=null && acc.trim().length()>0) {
				accs.add(acc);
			}
		}
		div.click();
		return accs;
	}

    public String getCurrentlySelectedAccount(){
        fastwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='rebate_account']/*")));
        return getCurrentlySelectedAccountEle().getText();
    }
	
	public boolean setAccount(String account) {
		WebElement div = this.getAccountDiv();
		div.click();
		List<WebElement> els = super.getAllOpendElements();
		for(WebElement e: els) {
			String acc = e.getAttribute("innerText");
			if(acc!=null && acc.trim().length()>0) {
				if(acc.trim().contains(account.trim())) {
					this.moveElementToVisible(e);
					this.clickElement(e);
					this.waitLoading();
					GlobalMethods.printDebugInfo("DashBoardPage: Set IB rebate account to : " + account);
					return true;
				}
			}
		}
		div.click();
		GlobalMethods.printDebugInfo("DashBoardPage: Set IB rebate account to : " + account + " failed!");
		return false;
	}
	
	public double getRebateBalance() {
		WebElement element = this.findVisibleElemntByXpath("(//h1)[2]");
		String info = element.getAttribute("innerText");
		System.out.println(info);
		return GlobalMethods.getBalanceFromString(info);
		
	}
	
	public void clickWithDraw() {
		this.waitLoading();
		js.executeScript("window.scrollTo(0, 0);");
		WebElement e = assertElementExists(By.xpath("//button[@data-testid='WITHDRAW_REBATE']"), "Dashboard Withdraw button");
		triggerElementClickEvent_withoutMoveElement(e);
		GlobalMethods.printDebugInfo("Click Dashboard Withdraw button");
	}

	public void clickTransfer() {
		this.waitLoading();
		js.executeScript("window.scrollTo(0, 0);");
		WebElement e = assertElementExists(By.xpath("//button[@data-testid='TRANSFER_REBATE_INTO_YOUR_TRADING_ACCOUNT']"), "Dashboard Transfer button");
		triggerElementClickEvent_withoutMoveElement(e);
		GlobalMethods.printDebugInfo("Click Dashboard Transfer button");
	}

    //获取IB返佣账号的Referral Link
    public String getIBReferralLink() {
        WebElement e = assertElementExists(By.xpath("//div[contains(text(), 'Register Online')]"), "IB Referral Link");
        String info = e.getAttribute("innerText");

        // Check if 'info' exists/contains "https"
        if (info == null) {
            Assert.fail("IB Referral Link not found");
        } else if (!info.contains("https")) {
            Assert.fail("IB Referral Link does not contain 'https'");
        }

        String ibUrl = info.replaceAll(".*(https?://\\S+).*", "$1");
        GlobalMethods.printDebugInfo("IB Referral Link: " + ibUrl);
        driver.navigate().to(ibUrl);
        String ibLiveRefLink = getCurrentURL();

        int index = ibLiveRefLink.indexOf("?affid=");
            // Extract everything from "?affid=" to the end
            String queryPart = ibLiveRefLink.substring(index + 7);
            GlobalMethods.printDebugInfo("Affliate ID: " + queryPart);

            return queryPart;
    }

    //获取IB返佣账号的Referral Link - with a new tab
    public String getIBReferralLink_NewTab() {
        WebElement e = assertElementExists(By.xpath("//div[contains(text(), 'Register Online')]"), "IB Referral Link");
        String info = e.getAttribute("innerText");

        // Check if 'info' exists/contains "https"
        if (info == null) {
            Assert.fail("IB Referral Link not found");
        } else if (!info.contains("https")) {
            Assert.fail("IB Referral Link does not contain 'https'");
        }
        String originalHandle = driver.getWindowHandle();

        String ibUrl = info.replaceAll(".*(https?://\\S+).*", "$1");
        GlobalMethods.printDebugInfo("IB Referral Link: " + ibUrl);

        driver.switchTo().newWindow(WindowType.TAB);
        driver.navigate().to(ibUrl);
        String ibLiveRefLink = getCurrentURL();

        int index = ibLiveRefLink.indexOf("?affid=");
        // Extract everything from "?affid=" to the end
        String queryPart = ibLiveRefLink.substring(index + 7);
        GlobalMethods.printDebugInfo("Affliate ID: " + queryPart);

        driver.close();
        driver.switchTo().window(originalHandle);

        return queryPart;
    }

    //查询返佣账号下所有的交易账号
    public List<String> getAllRecentlyOpenedTradingAccounts(){
        ArrayList<String> accs = new ArrayList<>();
        moveElementToVisible(this.getRecentlyOpenedTradingAccountsDiv().get(0));
        List<WebElement> div = this.getRecentlyOpenedTradingAccountsDiv();
        for(WebElement e: div) {
            String acc = e.getAttribute("innerText");
            if(acc!=null && acc.trim().length()>0) {
                accs.add(acc);
            }
        }
        return accs;
    }

	public static void main(String[] args) {
		
		WebDriver driver = null;
		driver = Utils.funcSetupDriver(driver, "chrome", "false");
		LoginPage login = new LoginPage(driver,"https://secure-staging-au-ex.crm-alpha.com");
		login.setUserName("automationcreditcardexclusive@mailinator.com");
		login.setPassWord("123Qwe");
		login.submitOld();
		
		DashBoardPage dashboard = new DashBoardPage(driver);
		//dashboard.setAccount("805300294");
		
		//System.out.println("get balance is: " + dashboard.getRebateBalance());
		dashboard.clickWithDraw();
		RebateWithdrawBasePage wbpage = new RebateWithdrawBasePage(driver);
		List<DEPOSITMETHOD> methods = wbpage.getAllMethods();
		for(DEPOSITMETHOD m : methods) {
			System.out.println(m.toString());
		}
		
		wbpage.setMethod(DEPOSITMETHOD.AUBANKTRANSFER);
		System.out.println("Finish");
	}
}
