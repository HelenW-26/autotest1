package newcrm.testcases.cps;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.Assert;
import java.time.Duration;
import java.util.List;
import java.util.Set;

public class stageCPSCreateWithdrawOrderTest {
    @Test(groups = {"USDTTRC20"})
    @Parameters({"ToolUrl", "merCd", "secret", "headless"})
    public void CPSCreateWithdrawOrderUSDTTRC20(String ToolUrl, String merCd, String secret, String headless) throws InterruptedException {
        // ===== Chrome Headless 参数设置 =====
        ChromeOptions options = new ChromeOptions();
        if ("true".equalsIgnoreCase(headless)) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            // ---加入這三行迴避CORS block---
            options.addArguments("--disable-web-security"); // 禁用網頁安全性檢查
            options.addArguments("--user-data-dir=/tmp/chrome_dev_test"); // 必須指定一個暫存目錄，CORS 禁用才會生效
            options.addArguments("--allow-running-insecure-content"); // 允許載入不安全的內容
            //---------
        }

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();

        driver.get(ToolUrl);
        String oldWindow = driver.getWindowHandle();

        // ===== 选择 “in common use withdrawal (出金)” =====
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("choseChannel")));
        WebElement dropdown = driver.findElement(By.id("choseChannel"));
        dropdown.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//option[text()='in common use withdrawal (出金)']")));
        WebElement option = driver.findElement(By.xpath("//option[text()='in common use withdrawal (出金)']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", option);
        option.click();

        // ===== 等待新窗口打开并切换，同时关闭旧窗口 =====
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        Set<String> handles = driver.getWindowHandles();
        for (String handle : handles) {
            if (!handle.equals(oldWindow)) {
                driver.switchTo().window(handle);
            }
        }
        driver.switchTo().window(oldWindow).close();
        driver.switchTo().window(driver.getWindowHandles().iterator().next());

        // ===== 填写表单 =====
        // new Select(driver.findElement(By.id("environment"))).selectByValue("stage");
        fill(driver, "merCd", merCd);
        fill(driver, "secret", secret);
        fill(driver, "transaction_tag", "T");
        fillByXpath(driver, "//input[@list='all_order_currencies_options']", "USDT");
        fillByXpath(driver, "//input[@list='all_actual_currencies_options']", "USDT-TRC20");
        fill(driver, "payment_method", "F00000");
        fill(driver, "first_name", "Auto");
        fill(driver, "last_name", "Mation");
        fill(driver, "card_name", "AutomationTest");
        fill(driver, "birthday", "20011001");
        fill(driver, "email", "Automation@gmail.com");
        fill(driver, "bank_no", "Automation123");
        fill(driver, "bank_name", "AutomationBank");
        fill(driver, "bank_branch", "AutomationBranch");
        fill(driver, "swift", "0123456");
        fill(driver, "personal_id", "AutomationPersonalId123");
        fill(driver, "invoice_id", "EW93714003W");
        fill(driver, "country", "TW");
        fill(driver, "bank_country", "ZA");
        fill(driver, "state", "Arkansas");
        fill(driver, "phone", "083712768");
        fill(driver, "tag", "SK");
        fill(driver, "user_id", "111");
        fill(driver, "brand", "VAU");
        fill(driver, "regulator", "VFSC");
        fill(driver, "crm_user_id", "10077179");
        fill(driver, "card_number", "TMbbkVdCs5NeuePKiSVd16BgfiRRdPegSw");
        fill(driver, "amount", "1");

        System.out.println("USDT Withdraw with merCd: " + merCd + ", Amount: 1");

        // ===== 生成订单号 =====
        driver.findElement(By.id("genOrderNo")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("order_id")));
        String orderId = driver.findElement(By.id("order_id")).getAttribute("value");
        if (orderId == null || orderId.trim().isEmpty()) {
            Assert.fail("❌ 未成功生成订单号，出金建单任务失败");
        }

        // ===== 获取加密数据 =====
        driver.findElement(By.xpath("//*[@id='check']")).click();
        Thread.sleep(3000);

        // ===== 点击提交按钮 =====
        WebElement withdrawBtn = driver.findElement(By.id("submitGateway"));
        wait.until(ExpectedConditions.elementToBeClickable(withdrawBtn));
        if (withdrawBtn.isEnabled()) {
            withdrawBtn.click();
            System.out.println("Clicked the withdrawal button and submitted.");
            // ✅ 提交后先等一会儿再捕捉 alert，防止 alert 太快出现
            Thread.sleep(2000);
        } else {
            Assert.fail("❌ 提交按钮不可点击，出金建单任务失败");
        }

        // 提交后处理 alert弹窗并识别是否成功
        try {
            WebDriverWait waitAlert = new WebDriverWait(driver, Duration.ofSeconds(5));
            waitAlert.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();

            if (alertText.contains("出金訂單建立成功")) {
                System.out.println("Withdrawal succeeded: 出金訂單建立成功");
                // 确认成功提交后再打印订单号
                System.out.println("Generated Order ID: " + orderId);
            } else {
                Assert.fail("❌ 弹窗存在，但内容不为 '出金訂單建立成功'，出金建单任务失败");
            }

            alert.accept();
        } catch (TimeoutException e) {
            Assert.fail("❌ 提交后未弹出任何弹窗，出金建单任务失败");
        }

        // ===== 清理并退出 =====
        try {
            Thread.sleep(5000);
            for (String handle : driver.getWindowHandles()) {
                driver.switchTo().window(handle);
                driver.close();
            }
            driver.quit();
        } catch (Exception e) {
            System.out.println("driver quit() failed (ignored): " + e.getMessage());
        }
    }

    // 通用清空 + 填值方法（by ID）
    private static void fill(WebDriver driver, String id, String value) {
        WebElement el = driver.findElement(By.id(id));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", el);
        el.sendKeys(value);
    }

    // 通用清空 + 填值方法（by XPath）
    private static void fillByXpath(WebDriver driver, String xpath, String value) {
        WebElement el = driver.findElement(By.xpath(xpath));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", el);
        el.sendKeys(value);
    }
    }