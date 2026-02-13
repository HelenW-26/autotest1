package newcrm.pages.clientpages;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.*;

import cn.hutool.extra.qrcode.BufferedImageLuminanceSource;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import newcrm.global.GlobalMethods;
import newcrm.global.GlobalProperties;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import newcrm.global.GlobalProperties.DEPOSITMETHOD;
import org.testng.Assert;
import tools.ScreenshotHelper;
import utils.LogUtils;
import vantagecrm.Utils;

import javax.imageio.ImageIO;

public class WithdrawPage extends WithdrawBasePage {

	WebDriverWait wait03;
	
	public WithdrawPage(WebDriver driver) {
		super(driver);
		this.wait03 = new WebDriverWait(driver, Duration.ofSeconds(20));
	}


    //Get web element of withdraw type
	protected WebElement getMethodDiv() {
		LogUtils.info("getMethodDiv");
		WebElement method = null;
		try {
			String xpath = "//button/span[contains(text( ),'Cancel')]";
			LogUtils.info("xpath: " + xpath);
			WebElement cancelbtn = driver.findElement(By.xpath(xpath));
			if(cancelbtn.isDisplayed())
			{	LogUtils.info("Click cancel button");
				js.executeScript("arguments[0].click()",cancelbtn);
			}
		}catch(Exception e) {
			LogUtils.info("No cancel Btn display ");
		}
		try {
			String divIdName = "withdrawalType";
			LogUtils.info("divIdName: " + divIdName);
			method = this.findClickableElemntByTestId(divIdName);
		}catch(Exception e) {
			LogUtils.info("Could not find withdraw methods or you should withdraw through credit card. ");
			return null;
		}
		return method;
	}
	
	//Get web element of withdraw method
	protected WebElement getWithdrawMethodElement(DEPOSITMETHOD method) {
		WebElement w_method = null;
//		String xpath = "//li//*[contains(text(), '"+method.getWithdrawName()+"')] | (//span[contains(normalize-space(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')), '" + method.getWithdrawName().toLowerCase() + "')])[1]";
		String xpath = "//li//*[contains(text(), '"+method.getWithdrawName()+"')]| //span[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + method.getWithdrawName().toLowerCase() + "')]"
				;

		LogUtils.info("getWithdrawMethodElement xpath: " + xpath);
		//xpath = xpath.replace("withdrawmethod", method.getWithdrawName());
		try {
			wait03.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
			w_method =  driver.findElement(By.xpath(xpath));
		}catch(Exception e) {
			System.out.println("NonCCWithdrawPage: Could not find withdraw methods or you should withdraw through credit card. ");
			return null;
		}
		return w_method;
	}

	protected WebElement getWithdrawMethodElementNew(DEPOSITMETHOD method) {
		LogUtils.info("in getWithdrawMethodElementNew" );
		waitLoading();
		WebElement w_method = null;
		//String xpath= "//div[@class='categories-menu']/following-sibling::li//*[contains(text(), '"+method.getWithdrawName()+"')]";
		String xpath = "(//span[contains(normalize-space(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')), '" + method.getWithdrawName().toLowerCase() + "')])[1]";
		LogUtils.info("getWithdrawMethodElement xpath: " + xpath);
		try {
			wait03.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
			moveElementToVisible(driver.findElement(By.xpath(xpath)));
			w_method =  driver.findElement(By.xpath(xpath));
		} catch(Exception e) {
			GlobalMethods.printDebugInfo("Could not find the withdraw method: " + method.getWithdrawName());
			Assert.fail("Could not find the withdraw method: " + method.getWithdrawName());
			return null;
		}

		return w_method;
	}
	
	//Click on withdraw type and specific withdraw method  
	public String setWithdrawMethod(DEPOSITMETHOD method) {
		WebElement m_element = this.getMethodDiv();
		if(m_element==null) {
			System.out.println("NonCCWithdrawPage: Find Withdraw Type Element Failed: Could not find the element.");
			return null;
		}
		//m_element.click();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click()",m_element);

		WebElement element = getWithdrawMethodElement(method);
		try {
			this.moveElementToVisible(element);
			//element.click();
			js.executeScript("arguments[0].click()",element);
			System.out.println("NonCCWithdrawPage: Select withdraw method: " + method.getWithdrawName());
			return method.getWithdrawName();
		}catch(Exception e) {	
			System.out.println("NonCCWithdrawPage: Could not find the withdraw method: " + method.getWithdrawName());
		}
		return null; 
	}

	public String setWithdrawMethodNew(DEPOSITMETHOD method) {
		LogUtils.info("setWithdrawMethodNew: " + method.getWithdrawName());
		WebElement m_element = this.getMethodDiv();
		ScreenshotHelper.highlightElement(driver,m_element);

		if(m_element==null) {
			LogUtils.info("Find Withdraw Type Element Failed: Could not find the element.");
			return null;
		}
		//m_element.click();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click()",m_element);
		waitLoading();

		WebElement element = getWithdrawMethodElementNew(method);

		try {
			this.moveElementToVisible(element);
			//element.click();
			js.executeScript("arguments[0].click()",element);
			waitLoading();
			System.out.println("Select withdraw method: " + method.getWithdrawName());
			this.moveContainerToTop();
			return method.getWithdrawName();
		}catch(Exception e) {
			Assert.fail("Could not find the withdraw method: " + method.getWithdrawName());
		}

		return null;
	}

	public boolean checkSavedAcctDropdown() {
		String acctDropdownXpath = "//div[@data-testid='selectedCardID']";
		boolean ddlAvailable = true;
		try {
			waitLoading();
			driver.findElement(By.xpath(acctDropdownXpath));
		} catch (Exception e) {
			ddlAvailable = false;
			GlobalMethods.printDebugInfo("WithdrawPage: No saved account dropdown");
		}
		return ddlAvailable;
	}

	public List<WebElement> getSavedAccountList() {
		List<WebElement> savedAcctList = null;
		String acctDropdownXpath = "//div[@data-testid='selectedCardID']";
		String savedAcctListXpath = "//div[contains(@class,'el-select-dropdown el-popper') and not(contains(@style,'display'))]//li";

		try {
			WebElement element = driver.findElement(By.xpath(acctDropdownXpath));
			js.executeScript("arguments[0].click()", element);
			savedAcctList = driver.findElements(By.xpath(savedAcctListXpath));
		}catch(Exception e) {
			GlobalMethods.printDebugInfo("WithdrawPage: Dropdown close jor");
			WebElement element = driver.findElement(By.xpath(acctDropdownXpath));
			js.executeScript("arguments[0].click()",element);
			savedAcctList = driver.findElements(By.xpath(savedAcctListXpath));
		}
		return savedAcctList;
	}

	public List<String> getSavedAccount() {
		List<String> savedAcctResult = new ArrayList<>();
		List<WebElement> savedAcctList = getSavedAccountList();

		for(WebElement acct : savedAcctList) {
			String info = acct.getAttribute("innerText");
			String valueClass = acct.getAttribute("class");
			if(!valueClass.trim().toLowerCase().contains("disable".toLowerCase())) {
				if (info != null) {
					if (!info.toLowerCase().contains("add")) {
						GlobalMethods.printDebugInfo("Find card: " + info);
						savedAcctResult.add(info.trim());
					}
				}
			}
		}
		return savedAcctResult;
	}

	public boolean chooseSavedAccount(String account) {
		List<WebElement> cards = this.getSavedAccountList();
		for(WebElement card : cards) {
			String info = card.getAttribute("innerText");
			if(info!=null) {
				if(info.toLowerCase().contains(account.trim().toLowerCase())) {
					GlobalMethods.printDebugInfo(info);
					this.moveElementToVisible(card);
					this.clickElement(card);
					GlobalMethods.printDebugInfo("Choose Saved Account: " + info);
					return true;
				}
			}
		}
		System.out.println("ERROR WithdrawPage: Choose saved account " + account + " failed");
		return false;
	}

	public boolean chooseAddNewAccount() {
		List<WebElement> savedAcctList = getSavedAccountList();
		for(WebElement acct : savedAcctList) {
			String info = acct.getAttribute("innerText");
			if(info!=null) {
				GlobalMethods.printDebugInfo("Find saved accounts info: " + info);
				if(info.toLowerCase().contains("add")) {
					this.moveElementToVisible(acct);
					this.clickElement(acct);
					return true;
				}
			}
		}
		System.out.println("ERROR WithdrawPage: Choose add new account failed");
		return false;
	}
    public void inputOtp(String code){
        return;
    }

    public void typePhone(String phone){

        WebElement webElement = findVisibleElemntByXpath("//input[@data-testid='phone']");
        webElement.clear();

        webElement.sendKeys(phone);
    }

    public Map<String,Object> getCreditMsg(){

        List<WebElement> spans = driver.findElements(By.xpath("//div[@role='dialog']//div[contains(@class,'dialog_info')]//span[@class='count']"));

        Map<String,Object> map = new HashMap<>();
        map.put("account",spans.get(0).getText());
        map.put("amount",spans.get(1).getText());
        return map;

//        WebElement titleElement = driver.findElement(By.xpath("//div[@role='dialog']//div[contains(@class,'dialog_info')]"));
//        return titleElement.getText();
    }
	// 点掉ICBC弹框
	public void checkICBCPopup() {
		WebElement popup = driver.findElement(By.xpath("//*[@id=\"unionPayForm\"]//div[contains(@class, 'unionpay-tip') and contains(text(), 'ICBC')]"));
		if(popup.isDisplayed()) {
			LogUtils.info("ICBC popup found");
//			WebElement confirm = driver.findElement(By.xpath("//button[contains(@data-testid,'confirm')]//span[contains(normalize-space(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')), 'confirm')]") );
//			confirm.click();
			Actions actions = new Actions(driver);
			actions.sendKeys(Keys.ESCAPE).build().perform();
			waitLoading();
		}
	}
	//校验出金是否展示2FA提示
	public String checkWithdrawal2FANotice() {
		String xpath = "//*[@id=\"withdraw\"]//div[contains(@class,'pending_content')]|" +
				"//div[contains(@class,'kyc_permission_dialog')]";
		LogUtils.info(xpath);
//		WebElement pendingContent = findVisibleElemntByXpath(xpath);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
		WebElement pendingContent =wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
//		WebElement pendingContent = findVisibleElemntByXpath(xpath);

		ScreenshotHelper.highlightElement(driver, pendingContent);
		return pendingContent.getText();
	}

    public WebElement getQRpath(){
        return findVisibleElemntByXpath("//div[@id='changeAuthenticator']//div[@class='left']//img");
    }

    public String getQRcodefromCanvas(){
        waitLoading();
        String filename = "qrcode" + new Date().getTime() + ".png";

        File file = new File("screenshots/"+filename);
        try {
            WebElement webElement = getQRpath();

            File qrscreenshot = webElement.getScreenshotAs(OutputType.FILE);

            Files.copy(qrscreenshot.toPath(),file.toPath(), StandardCopyOption.REPLACE_EXISTING);

            System.out.println("save screenshot...");

            BufferedImage bufferedImage = ImageIO.read(file);
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = new MultiFormatReader().decode(bitmap);



            return result.getText();

        } catch (Exception e) {
            GlobalMethods.printDebugInfo("get qr code failed");
        }finally {
            file.delete();
        }
        return null;
    }

    public String readFile(){
        try{
            String content = new String(Files.readAllBytes(Paths.get(Utils.workingDir + "/src/main/resources/newcrm/data/authUrl.json")), "UTF-8");
            JSONObject jsonObject = JSONObject.parseObject(content);

            JSONObject js = jsonObject.getJSONObject(GlobalProperties.env.toUpperCase());

            String brand = GlobalProperties.brand;

            if(brand.equalsIgnoreCase("AU") || brand.equalsIgnoreCase("VFX" )){
                brand = "AU";
            }

            return js.getString(brand.toUpperCase());
        }catch (Exception e){
            GlobalMethods.printDebugInfo("read file failed。");
        }

        return null;
    }

    public void writeFile(String url){
        try{

            String filePath = Utils.workingDir + "/src/main/resources/newcrm/data/authUrl.json";
            String content = new String(Files.readAllBytes(Paths.get(filePath)), "UTF-8");
            JSONObject jsonObject = JSONObject.parseObject(content);

            JSONObject js = jsonObject.getJSONObject(GlobalProperties.env.toUpperCase());
            String brand = GlobalProperties.brand;

            if(brand.equalsIgnoreCase("AU") || brand.equalsIgnoreCase("VFX" )){
                brand = "AU";
            }

            js.put(brand.toUpperCase(),url);

            String newJson = jsonObject.toJSONString();
            Files.write(Paths.get(filePath), newJson.getBytes("UTF-8"), StandardOpenOption.TRUNCATE_EXISTING);

        }catch (Exception e){
            GlobalMethods.printDebugInfo("write file failed。");
        }

    }

    private JSONObject readFileTo() throws Exception{
        String filePath = Utils.workingDir + "/src/main/resources/newcrm/data/authUrl.json";
        String content = new String(Files.readAllBytes(Paths.get(filePath)), "UTF-8");
        JSONObject jsonObject = JSONObject.parseObject(content);

        // 3. 获取 ALPHA 对象
        return jsonObject.getJSONObject(GlobalProperties.env.toUpperCase());
    }


}

