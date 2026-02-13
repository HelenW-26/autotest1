package newcrm.testcases.cps;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.Set;

public class stageCPSDepositCallbackTest {
    @Test
    @Parameters({"CallbackUrl", "merCd", "orderNo", "headless"})
    public void CPSDepositCallbackCoboUSDTTRC20(String CallbackUrl, String merCd, String orderNo, String headless) throws InterruptedException {
        // ===== 设置 Chrome 启动参数 =====
        ChromeOptions options = new ChromeOptions();
        if ("true".equalsIgnoreCase(headless)) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        }
        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        // ===== 打开页面 =====
        driver.get(CallbackUrl);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String oldWindow = driver.getWindowHandle();

        // ===== 选择下拉项 =====
        // 第一个下拉菜单：选择 Mode -> Deposit
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("choseMode")));
        WebElement dropdownMode = driver.findElement(By.id("choseMode"));
        dropdownMode.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//option[text()='Deposit']")));
        WebElement modeOption = driver.findElement(By.xpath("//option[text()='Deposit']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", modeOption);
        modeOption.click();

        // 第二个下拉菜单：选择 Channel -> CoboPay
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("choseChannel")));
        WebElement dropdownChannel = driver.findElement(By.id("choseChannel"));
        dropdownChannel.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//option[text()='CoboPay']")));
        WebElement channelOption = driver.findElement(By.xpath("//option[text()='CoboPay']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", channelOption);
        channelOption.click();

        // ===== 等待新窗口打开并切换到新窗口，同时关闭旧窗口 =====
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        Set<String> handles = driver.getWindowHandles();
        for (String handle : handles) {
            if (!handle.equals(oldWindow)) {
                driver.switchTo().window(handle);
                break;
            }
        }
        driver.switchTo().window(oldWindow).close(); // 关闭旧窗口
        driver.switchTo().window(driver.getWindowHandles().iterator().next()); // 切换回新窗口

        // ===== 表单填写 =====
        // new Select(driver.findElement(By.id("environment"))).selectByValue("stage");

        fillInput(driver, "orderNo", orderNo);


        fillInput(driver, "txnAmt", "11");
        fillInput(driver, "sourceAddress", "TQETUR38cL8nY1gQcr43sWgFFwTKSwQjLw");

        // ===== 点击按钮 =====
        WebElement generateBtn = driver.findElement(By.id("genTxHash"));
        generateBtn.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("txid")));
        String orderId = driver.findElement(By.id("txid")).getAttribute("value");

        Thread.sleep(2000);

        // ===== 提交订单 =====
        WebElement depositBtn = driver.findElement(By.id("submitGateway"));
        wait.until(ExpectedConditions.elementToBeClickable(depositBtn));

        if (depositBtn.isEnabled()) {
            depositBtn.click();
            System.out.println("Clicked the deposit button and submitted.");
        } else {
            Assert.fail("❌ 提交按钮未激活，无法提交回调表单");
        }

        // 提交后处理 alert弹窗并识别是否成功
        try {
            WebDriverWait waitAlert = new WebDriverWait(driver, Duration.ofSeconds(5));
            waitAlert.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();

            if (alertText.contains("ok")) {
                System.out.println("DepositCallback succeeded");
                //确认成功后才开始打平台流水号、商户号等信息
                System.out.println("Cobo.USDTTRC20 Deposit with merCd: " + merCd + ", orderNo: " + orderNo + ", Amount: 11");
            } else {
                alert.accept();
                Assert.fail("❌ 弹窗内容不是 'ok'，实际内容为: " + alertText);
            }
            alert.accept();
        } catch (TimeoutException e) {
            Assert.fail("❌ 提交后未出现任何 alert 弹窗");
        }

        // ===== 关闭所有窗口并退出 =====
        try {
            Thread.sleep(7000); // 给页面缓冲期
            for (String handle : driver.getWindowHandles()) {
                driver.switchTo().window(handle);
                driver.close();
            }
            driver.quit();
        } catch (Exception e) {
            System.out.println("driver quit() failed (ignored): " + e.getMessage());
        }
    }
    private void fillInput(WebDriver driver, String id, String value) {
        WebElement element = driver.findElement(By.id(id));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", element);
        element.sendKeys(value);
    }
    private void fillInputByXPath(WebDriver driver, String xpath, String value) {
        WebElement element = driver.findElement(By.xpath(xpath));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", element);
        element.sendKeys(value);
    }
}