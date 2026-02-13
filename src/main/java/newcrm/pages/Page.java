package newcrm.pages;

import java.net.URI;
import java.net.URLDecoder;
import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;
import java.nio.charset.StandardCharsets;

import lombok.Getter;
import newcrm.global.GlobalMethods;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import newcrm.global.GlobalProperties;
import newcrm.pages.clientpages.elements.AdvertiseElements;
import org.testng.Assert;
import tools.ScreenshotHelper;
import utils.LogUtils;

public abstract class Page {
	@Getter
    protected WebDriver driver;
	protected WebDriverWait waitvisible;
	protected WebDriverWait waitclickable;
	protected WebDriverWait fastwait;

	protected JavascriptExecutor js;
	
	public Page(WebDriver driver) {
		this.driver = driver;
		waitvisible = new WebDriverWait(driver, Duration.ofSeconds(GlobalProperties.WAITTIME), Duration.ofMillis(300L));
		waitclickable = new WebDriverWait(driver, Duration.ofSeconds(GlobalProperties.WAITTIME),Duration.ofMillis(300L));
		fastwait = new WebDriverWait(driver,Duration.ofSeconds(GlobalProperties.WAITTIME));
		
		//设置等待时忽略的异常
		waitvisible.ignoring(ElementNotInteractableException.class, NoSuchElementException.class);
		waitclickable.ignoring(ElementClickInterceptedException.class, NoSuchElementException.class);
		fastwait.ignoring(ElementNotInteractableException.class, NoSuchElementException.class);
		js = (JavascriptExecutor) driver;
		LogUtils.info("Thread ID: " + Thread.currentThread().getId() + " | Driver Hash: " + driver.hashCode());

	}
	public WebElement findVisibleElemntByXpath(String xpath) {
		return waitvisible.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
	}
	
	public WebElement findVisibleElemntByCss(String css) {
		return waitvisible.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(css)));
	}
	
	protected WebElement findClickableElementByXpath(String xpath) {
		return waitclickable.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
	}
	
	protected WebElement findClickableElementByCss(String css) {
		return waitclickable.until(ExpectedConditions.elementToBeClickable(By.cssSelector(css)));
	}
	
	protected WebElement findVisibleElemntByTestId(String testid) {
		return waitvisible.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@data-testid='"+testid+"']")));
	}

	protected WebElement findVisibleElementByContainTestId(String testid) {
		return waitvisible.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format("//div[contains(@data-testid,\"%s\")]", testid))));
	}
	protected WebElement findElementLocatedByContainTestId(String testid) {
		return waitvisible.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format("//div[contains(@data-testid,\"%s\")]", testid))));
	}
	protected WebElement findClickableElemntByTestId(String testid) {
		return waitclickable.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@data-testid='"+testid+"']")));
	}
	
	protected WebElement findClickableElemntBy(By by) {
		return waitclickable.until(ExpectedConditions.elementToBeClickable(by));
	}

	protected WebElement findVisibleElemntBy(By by) {
		return waitvisible.until(ExpectedConditions.visibilityOfElementLocated(by));
	}

	protected List<WebElement> findVisibleElemntsBy(By by) {
		return waitvisible.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
	}

	protected boolean findInvisibleElemntBy(By by) {
		return waitvisible.until(ExpectedConditions.invisibilityOfElementLocated(by));
	}

	public void waitLoading() {

		try {
			fastwait.until((ExpectedCondition<Boolean>) d -> {
				List<WebElement> loaders = d.findElements(By.xpath("//div[@id='web_loading' and not(contains(@style, 'display: none'))]//i[@class='web_loading_gif']"));
				return loaders.isEmpty();
			});
			fastwait.until((ExpectedCondition<Boolean>) d -> {
				List<WebElement> loaders = d.findElements(By.xpath("//i[contains(@class,'client-portal-loading') or contains(@class, 'el-loading-mask') or @class='el-icon-loading']"));
				return loaders.isEmpty();
			});
		} catch (Exception e) {
		}

		//处理页面的广告等
		
//		try {
//			AdvertiseElements els = PageFactory.initElements(driver, AdvertiseElements.class);
//			els.adv.click();
//		}catch(Exception e) {
//			//没广告会报错，在这里先处理掉
//		}

	}

	//only for wait loading time more than 60s, will remove it after loading time back to normal
	public void waitLoadingForCustomise(int sec) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(sec));

		try {
			wait.until((ExpectedCondition<Boolean>) d -> {
				try{
					List<WebElement> loaders = d.findElements(By.xpath("//div[@id='web_loading' and not(contains(@style, 'display: none'))]//i[@class='web_loading_gif']"));
					return loaders.isEmpty();
				}catch(Exception e) {
					return true;
				}
			});

			wait.until((ExpectedCondition<Boolean>) d -> {
				try{
					List<WebElement> loaders = d.findElements(By.xpath("//i[contains(@class,'client-portal-loading') or contains(@class, 'el-loading-mask') or @class='el-icon-loading']"));
					return loaders.isEmpty();
				}catch(Exception e) {
					return true;
				}
			});

		} catch (Exception e) {
		}
	}

	public void waitLoader() {
		fastwait.until((ExpectedCondition<Boolean>) d -> {
			try{
				d.findElement(By.xpath("//div[@class='loader']"));
			}catch(Exception e) {
				return true;
			}
			return false;
		});
	}
	public void waitLoaderForTrader() {
		fastwait.until((ExpectedCondition<Boolean>) d -> {
			try{
				d.findElement(By.xpath("//div[@class='loading-spinner']"));
			}catch(Exception e) {
				return true;
			}
			return false;
		});
	}

	public void waitButtonLoader() {
		fastwait.until((ExpectedCondition<Boolean>) d -> {
			try{
				d.findElement(By.xpath("//i[contains(@class,'el-icon-loading')]"));
			}catch(Exception e) {
				return true;
			}
			return false;
		});
	}

	public void waitSumSubLanguageIconLoader() {
		fastwait.until((ExpectedCondition<Boolean>) d -> {
			try{
				d.findElement(By.cssSelector("span.sdk-select.lang-selector button svg[role='progressbar']"));
			}catch(Exception e) {
				return true;
			}
			return false;
		});
	}

	public void waitSumSubFileUploadLoader() {
		fastwait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("svg[role='progressbar']")));
	}

	public void waitSumSubBtnLoader() {
		fastwait.until((ExpectedCondition<Boolean>) d -> {
			try{
				d.findElement(By.xpath("//div[@id='sdk-base-container']//button[1]//div[contains(@class,'loader')]"));
			}catch(Exception e) {
				return true;
			}
			return false;
		});
	}

	public void waitPaymentGreyLoader() {
		// Wait for deposit method content to load
		LogUtils.info("Start loading selected deposit method content...");
		fastwait.until(driver -> driver.findElement(By.xpath("//div[@class='channel-area'] | //div[@class='channel-area channel-list'] | //div[@class='channel-area channel-detail']")).isDisplayed());
		fastwait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='ant-skeleton-content']")));
		fastwait.until(driver -> driver.findElement(By.xpath("(//div[@class='channel-area'] | //div[@class='channel-area channel-list'] | //div[@class='channel-area channel-detail'])//section")).isDisplayed());
		waitLoading();
		LogUtils.info("Finish loading selected deposit method content...");
	}

	public void waitPaymentLoader() {
		LogUtils.info("Start loading payment page...");

		//fastwait.until(driver -> driver.findElement(By.xpath("//div[@class='channel-area'] | //div[@class='channel-area channel-list']")).isDisplayed());

		By channel = By.cssSelector(".channel-area, .channel-area.channel-list");

		//add waitloading if channel list not find
		try {
			waitvisible.until(d -> d.findElements(channel).stream().anyMatch(WebElement::isDisplayed));
			LogUtils.info("Find channel list");
		} catch (Exception e) {
			waitLoading();
			System.out.println("Channel list not found or not visible in time");
		}
		fastwait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@id='__LOADING_PARENT__']")));

		// Check for empty channels content
		WebElement eleEmpty = checkElementExists(By.xpath("//*[contains(normalize-space(text()), 'No available channels')]"));

		if (eleEmpty != null) {
			String emptyMsg = eleEmpty.getText();
			Assert.fail(emptyMsg);
		}

		fastwait.until(driver -> driver.findElement(By.xpath("//div[@class='channel-item']")).isDisplayed());
		waitLoading();
		LogUtils.info("Finish loading payment page...");
	}

	public void waitCCLoader() {
		driver.switchTo().defaultContent();
		LogUtils.info("Start loading third party payment content...");
		fastwait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@class='bp-splash-screen']")));
		LogUtils.info("Finish loading third party payment content...");
	}

	public void waitPageNavigatation() {
		// Wait until page is fully loaded
		fastwait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
	}

    protected void waitElementInvisible(String xpath) {
		
		fastwait.until((ExpectedCondition<Boolean>) d -> {
			
			WebElement el = null;
			try{
				el = d.findElement(By.xpath(xpath));
			}catch(Exception e) {
				return true;
			}
			String style = el.getAttribute("style");
			if(style==null) {
				return true;
			}
			if(!style.contains("display")) {
				return true;
			}
			return false;
		});
	}

	public void waitLoadingInCopyTrading(){
		try {
			fastwait.until((ExpectedCondition<Boolean>) d -> {
				List<WebElement> loaders = d.findElements(By.xpath("//div[@class='el-skeleton__item el-skeleton__text']"));
				return loaders.isEmpty();
			});

			fastwait.until((ExpectedCondition<Boolean>) d -> {
				try{
					List<WebElement> loaders = d.findElements(By.xpath("//div[@class='el-skeleton__item el-skeleton__circle']"));
					return loaders.isEmpty();
				}catch(Exception e) {
					return true;
				}
			});

			fastwait.until((ExpectedCondition<Boolean>) d -> {
				List<WebElement> loaders = d.findElements(By.xpath("//div[contains(@class,'loader')]"));
				return loaders.isEmpty();
			});
		} catch (Exception e) {
		}}
	
	public void goBack() {
		driver.navigate().back();
		this.waitLoading();
		LogUtils.info("Pressing the browser's back button");
	}

	public void goHomePage(String homeURL) {
		driver.get(homeURL);
		this.waitLoading();
	}

	public boolean checkUrlContains(String keyword) {
		//this.waitLoadingForCustomise(120);
		try {
			String currentURL = driver.getCurrentUrl();
			if(!currentURL.contains(keyword))
			{
				//jump to third party page sometimes very slow
				Thread.sleep(10000);
			}
			currentURL = driver.getCurrentUrl();

			System.out.println("currentURL: " + currentURL + " currentURL.contains(keyword):" + currentURL.contains(keyword)+ " keyword: " + keyword);
			String finalCurrentURL = currentURL;
			waitvisible.until((ExpectedCondition<Boolean>) driver -> finalCurrentURL.contains(keyword));
		}catch(Exception e) {
			Assert.fail("Did not navigate to the correct url which include : " + keyword);
			return false;
		}
		
		return true;
	}

	public boolean checkUrlNotContains(String keyword) {
		this.waitLoadingForCustomise(120);
		try {
			waitvisible.until((ExpectedCondition<Boolean>) driver ->!driver.getCurrentUrl().contains(keyword));
			//waitvisible.until((ExpectedCondition<Boolean>) driver ->!driver.findElement(By.tagName("body")).getText().contains("error"));
			//waitvisible.until((ExpectedCondition<Boolean>) driver ->!driver.findElement(By.tagName("body")).getText().contains("invalid"));
			//waitvisible.until((ExpectedCondition<Boolean>) driver ->!driver.findElement(By.tagName("body")).getText().contains("fail"));
		}catch(Exception e) {
			System.out.println("**********Did not navigate to the third party, kindly check**********");
			/* try {
				Utils.funcTakeScreenShot(driver, "failSS");
			} catch (Exception e1) {
				e1.printStackTrace();
			} */
			return false;
		}
		
		return true;
	}

	public boolean checkUrlContains_customWait(String keyword) {
		this.waitLoadingForCustomise(120);
		try {
			String currentURL = driver.getCurrentUrl();
			if(!currentURL.contains(keyword))
			{
				//jump to third party page sometimes very slow
				Thread.sleep(10000);
			}
			currentURL = driver.getCurrentUrl();

			System.out.println("currentURL: " + currentURL + " currentURL.contains(keyword):" + currentURL.contains(keyword)+ " keyword: " + keyword);
			String finalCurrentURL = currentURL;

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			wait.ignoring(ElementNotInteractableException.class, NoSuchElementException.class);
			wait.until((ExpectedCondition<Boolean>) driver -> finalCurrentURL.contains(keyword));

		} catch(Exception e) {
			Assert.fail("Did not navigate to the correct url which include : " + keyword);
			return false;
		}

		return true;
	}

	public boolean validateThirdPartyUrls(List<String> urlList, WebDriverWait wait) {
	    boolean result = false;

	    for (String thirdpartyurl : urlList) {
	        try {
	            wait.until(ExpectedConditions.urlContains(thirdpartyurl));
	            result = true;
	            break;
	        } catch (Exception e) {
	            result = false;
	            continue;
	        }
	    }

	    return result;
	}
	
	public String getURL() {
		return driver.getCurrentUrl();
	}

	public void moveContainerToTop () {
		WebElement container = driver.findElement(By.cssSelector(".el-container.wrapper"));
		js.executeScript("arguments[0].scrollTop = 0;", container);
	}

	/**
	 * Move Element to the screen
	 * @param element
	 */
	protected void moveElementToVisible(WebElement element) {
		JavascriptExecutor js_executor = (JavascriptExecutor) driver; 
		String js_str = "arguments[0].scrollIntoView(true);window.scrollBy(0, -window.innerHeight / 2);";
		js_executor.executeScript(js_str,element);
	}
	
	protected void clickElement(WebElement element) {
		JavascriptExecutor js_executor = (JavascriptExecutor) driver; 
		String js_str = "arguments[0].click();";
		js_executor.executeScript(js_str,element);
	}
	protected void clickSvgElement(WebElement element){
        JavascriptExecutor js_executor = (JavascriptExecutor) driver;
        String useClickScript = """
        const eventTypes = ['mousedown', 'mouseup', 'click'];
        eventTypes.forEach(type => {
            const evt = new MouseEvent(type, {
                bubbles: true,
                cancelable: true,
                composed: true
            });
            arguments[0].dispatchEvent(evt);
        });
    """;
        js_executor.executeScript(useClickScript, element);
    }
	protected void setInputValue(WebElement element, String value) {
		this.moveElementToVisible(element);
		element.clear();
		element.sendKeys(value);
	}

	protected void setInputValue_withoutMoveElement(WebElement element, String value) {
		element.clear();
		element.sendKeys(value);
	}

	protected void triggerClickEvent(WebElement element) {
		this.moveElementToVisible(element);
		this.clickElement(element);
		this.waitLoading();
	}

	protected void triggerClickEvent_withoutMoveElement(WebElement element) {
		this.clickElement(element);
		this.waitLoading();
	}

	protected void triggerElementClickEvent(WebElement element) {
		this.moveElementToVisible(element);
		element.click();
		this.waitLoading();
	}

	protected void triggerElementClickEvent_withoutMoveElement(WebElement element) {
		element.click();
		this.waitLoading();
	}

	protected String selectRandomValueFromDropDownList(List<WebElement> els) {
		if(els==null || els.size()< 1) {
			return null;
		}
		Random random = new Random();
		
		WebElement  e = els.get(random.nextInt(els.size()));
		String result = e.getAttribute("innerText");
		this.moveElementToVisible(e);
		this.clickElement(e);
		return result;
		
	}

    protected String selectRandomDropDownOption_ElementClickEvent(List<WebElement> els) {
        if(els==null || els.size()< 1) {
            return null;
        }
        Random random = new Random();

        WebElement  e = els.get(random.nextInt(els.size()));
        String result = e.getAttribute("innerText");
        this.moveElementToVisible(e);
        triggerElementClickEvent(e);
		LogUtils.info("Value selected: " + result);
        return result;
    }

	public void refresh() {
		driver.navigate().refresh();
		LogUtils.info("Refresh page");
		this.waitLoading();
	}
	
	/**
	 * 
	 * @return return the dropdown elements under div[class='el-select-dropdown el-popper']
	 */
	protected List<WebElement> getAllOpendElements(){
		waitLoading();

		String xpath = "//div[contains(@class,'el-select-dropdown el-popper') and not(contains(@style, 'display: none'))]//li";

		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			wait.until(driver -> {
				try {
					return driver.findElements(By.xpath("//div[contains(@class,'el-select-dropdown el-popper') and not(contains(@style, 'display: none'))]//p[@class='el-select-dropdown__empty']")).isEmpty();

				} catch (Exception ex) {
					return false;
				}
			});

			this.findVisibleElemntByXpath(xpath);

		} catch (Exception ex) {
			Assert.fail("No data available or timeout waiting for data listing");
		}

		return driver.findElements(By.xpath(xpath));
	}

	public WebElement assertElementExists(By by, String message, Predicate<WebElement> filter, WebElement ele) {

		List<WebElement> elementList = ele != null ? ele.findElements(by) : driver.findElements(by);

		return elementList
				.stream()
				.filter(filter != null ? filter : e -> true)
				.findFirst()
				.orElseThrow(() -> new AssertionError(message + " not found"));
    }

	public List<WebElement> assertElementsExists(By by, String message, Predicate<WebElement> filter, WebElement ele) {

		List<WebElement> elementList = ele != null ? ele.findElements(by) : driver.findElements(by);

		List<WebElement> filteredElementList = elementList
				.stream()
				.filter(filter != null ? filter : e -> true)
				.toList();

		if (filteredElementList.isEmpty()) {
			throw new AssertionError(message + " not found");
		}

		return filteredElementList;
	}

	public WebElement checkElementExists(By by, String msg, Predicate<WebElement> filter, WebElement ele) {

		List<WebElement> elementList = ele != null ? ele.findElements(by) : driver.findElements(by);

		WebElement element = elementList
				.stream()
				.filter(filter != null ? filter : e -> true)
				.findFirst()
				.orElse(null);

		if (element == null && msg != null) {
			System.out.println(msg + " not found");
		}

		return element;
	}

	public WebElement checkElementExists(By by) {
		return checkElementExists(by, null, null, null);
	}

	public WebElement checkElementExists(By by, String msg) {
		return checkElementExists(by, msg, null, null);
	}

	public WebElement checkElementExists(By by, String msg, Predicate<WebElement> filter) {
		return checkElementExists(by, msg, filter, null);
	}

	public WebElement checkElementExists(By by, WebElement ele) {
		return checkElementExists(by, null, null, ele);
	}

	public WebElement checkElementExists(By by, String msg, WebElement ele) {
		return checkElementExists(by, msg, null, ele);
	}

	public WebElement assertElementExists(By by, String message) {
		return assertElementExists(by, message, null, null);
	}

	public WebElement assertElementExists(By by, String message, Predicate<WebElement> filter) {
		return assertElementExists(by, message, filter, null);
	}

	public WebElement assertElementExists(By by, String message, WebElement ele) {
		return assertElementExists(by, message, null, ele);
	}

	public List<WebElement> assertElementsExists(By by, String message) {
		return assertElementsExists(by, message, null, null);
	}

	public List<WebElement> assertElementsExists(By by, String message, Predicate<WebElement> filter) {
		return assertElementsExists(by, message, filter, null);
	}

	public List<WebElement> assertElementsExists(By by, String message, WebElement ele) {
		return assertElementsExists(by, message, null, ele);
	}

	public WebElement assertVisibleElementExists(By by, String message) {
		try {
			return findVisibleElemntBy(by);
		} catch (Exception e) {
			Assert.fail(message + " not found");
		}

		return null;
	}

	public List<WebElement> assertVisibleElementsExists(By by, String message) {
		try {
			return findVisibleElemntsBy(by);
		} catch (Exception e) {
			Assert.fail(message + " not found");
		}

		return null;
	}

	public boolean assertInvisibleElementExists(By by, String message) {
		try {
			return findInvisibleElemntBy(by);
		} catch (Exception e) {
			Assert.fail(message + " not found");
		}

		return false;
	}

	public WebElement assertClickableElementExists(By by, String message) {
		try {
			return findClickableElemntBy(by);
		} catch (Exception e) {
			Assert.fail(message + " not found");
		}

		return null;
	}

    public WebElement assertClickableElementByTestIdExists(String testid, String message) {
        try {
            return findClickableElemntByTestId(testid);
        } catch (Exception e) {
            Assert.fail(message + " not found");
        }

        return null;
    }

	//按ESC键退出通知窗口
	public void pressEsc2ExitNotification() {
		findVisibleElemntBy(By.xpath("//body")).sendKeys(Keys.ESCAPE);

	}

	public String getCurrentTab() {
		return driver.getWindowHandle();
	}
	public Set<String> getAllTab() {
		return driver.getWindowHandles();
	}
	public String getCurrentURL() {
		return driver.getCurrentUrl();
	}
	public void hoverToElement(WebElement element) {
		Actions actions = new Actions(driver);
		actions.moveToElement(element).perform();
	}
	public void switchTab(String originalTab) {
		// Wait for the new tab to open
		fastwait.until(driver -> driver.getWindowHandles().size() > 1);

		Set<String> allTabs = driver.getWindowHandles();
		//switch to another tab
		for(String tab : allTabs)
		{
			if(!tab.equals(originalTab)) {
				driver.switchTo().window(tab);
				break;
			}
		}
	}

	public void switchToOriginalTab(String originalTab) {
		// Close the new tab and go back
		driver.close();
		driver.switchTo().window(originalTab);
	}

	public String checkExistsAlertMsg(Supplier<By> elementSupplier, String label) {
		// Capture ss
		ScreenshotHelper.takeFullPageScreenshot(getDriver(), "screenshots", label.replace(" ", ""));

		// Element path
		By locator  = elementSupplier.get();
		if (locator == null) return null;

		// Get element by locator
		WebElement ele = checkElementExists(locator);
		if (ele == null) return null;

		// Get element text
		String alertMsg = ele.getText();
		LogUtils.info(label + " Failed. Resp Msg: " + alertMsg);
		return alertMsg;
	}

	public boolean checkNavigateUrl(String url) throws Exception {

		// Check navigated url after move away from page.
		// If navigated url subdomain is not match with previous page, means redirect to different site.
		String currUrl = getCurrentURL();
		URI uri = new URI(url);
		String host = uri.getHost();
		String[] parts = host.split("\\.");

		if(!currUrl.contains(parts[1])) {
			LogUtils.info("Navigate to different url. Previous Url: " + url + ", Current Url: " + currUrl);
			return false;
		}

		return true;
	}

	/**
	 * 使用JavaScriptExecutor执行XPath查找元素
	 * @param xpath XPath表达式
	 * @return WebElement 找到的元素
	 */
	public WebElement findElementByXpathWithJS(String xpath) {
		String script = String.format(
				"return document.evaluate('%s', document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;",
				xpath.replace("'", "\\'")
		);
		return (WebElement) js.executeScript(script);
	}

	/**
	 * 通用方法：通过XPath查找并点击元素
	 */
    protected void clickElementByXpathWithJS(String xpath, String elementName) {
		try {
			// 等待元素出现
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(driver -> findElementByXpathWithJS(xpath) != null);

			// 使用JS执行点击
			WebElement element = findElementByXpathWithJS(xpath);
			if (element != null) {
				js.executeScript("arguments[0].click();", element);
				System.out.println("成功点击: " + elementName);
			} else {
				throw new RuntimeException("未找到元素: " + elementName);
			}
		} catch (Exception e) {
			throw new RuntimeException("点击" + elementName + "失败: " + e.getMessage());
		}
	}
	//退出frame
	public void exitIframe() {
		driver.switchTo().defaultContent();
	}

	public void goToIframe(String frameId) {
		driver.switchTo().frame(frameId);
	}
	// 获取页面文本
	public String getPageTextContent(){
		JavascriptExecutor js = (JavascriptExecutor) driver;
        return (String) js.executeScript("return document.body.textContent;");
	}

	public boolean isEleHidden(WebElement element) {
		String style = element.getAttribute("style");
		return style != null && style.contains("visibility: hidden");
	}

	public boolean isEleHasPopup(WebElement element) {
		String val = element.getAttribute("aria-haspopup");
		return val != null && "true".equalsIgnoreCase(val.trim());
	}

	public boolean isElePopupExpanded(WebElement element) {
		String val = element.getAttribute("aria-expanded");
		return val != null && "true".equalsIgnoreCase(val.trim());
	}

}
