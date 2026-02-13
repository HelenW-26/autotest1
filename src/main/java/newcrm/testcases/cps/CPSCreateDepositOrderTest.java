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

public class CPSCreateDepositOrderTest{
    @Test(groups = {"HaUsdtPay-USDTTRC20"})
    @Parameters({"ToolUrl", "merCd", "secret", "headless"})
    public void CPSCreateDepositOrderHaUsdtPayUSDTTRC20(String ToolUrl, String merCd, String secret, String headless) throws InterruptedException {
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
        driver.get(ToolUrl);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String oldWindow = driver.getWindowHandle();

        // ===== 选择 HaUsdtPay 下拉项 =====
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("choseChannel")));
        WebElement dropdown = driver.findElement(By.id("choseChannel"));
        dropdown.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//option[text()='HaUsdtPay']")));
        WebElement dropdownOption = driver.findElement(By.xpath("//option[text()='HaUsdtPay']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dropdownOption);
        dropdownOption.click();

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
        new Select(driver.findElement(By.id("environment"))).selectByValue("sandbox");

        fillInput(driver, "merCd", merCd);
        fillInput(driver, "secret", secret);
        fillInputByXPath(driver, "//input[@list='crypto_order_currencies_options']", "USDT");
        fillInputByXPath(driver, "//input[@list='crypto_actual_currencies_options']", "USDT-TRC20");
        fillInput(driver, "tag", "SK");
        fillInput(driver, "state", "AutoState");
        fillInputByXPath(driver, "//*[@id='payloadForm']/fieldset/div[1]/div/div[8]/input", "1234567");
        fillInput(driver, "zip", "2000");
        fillInput(driver, "city", "AutoCity");
        fillInput(driver, "address", "AutoRd");
        fillInput(driver, "country", "AutoCountry");
        fillInput(driver, "order_amount", "2");

        System.out.println("HaUsdtPay-USDTTRC20 Deposit with merCd: " + merCd + ", Amount: 2");

        // ===== 点击按钮 =====
        WebElement generateBtn = driver.findElement(By.id("genOrderNo"));
        generateBtn.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("order_id")));
        String orderId = getNonEmptyValueOrFail(driver, "order_id", "Order ID");
        if (orderId.trim().isEmpty()) {
            System.out.println("❌ 未生成 Order ID");
            Assert.fail("❌ 未生成 Order ID，入金建单任务失败");
        }

        WebElement checkBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='check']")));
        checkBtn.click();

        Thread.sleep(2000);

        // ===== 处理弹窗 =====
        try {
            wait.withTimeout(Duration.ofSeconds(5)).until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            System.out.println("Alert text: " + alert.getText());
            alert.accept();
        } catch (TimeoutException e) {
            System.out.println("No alert present.");
        }

        // ===== 提交订单 =====
        WebElement depositBtn = driver.findElement(By.id("submitGateway"));
        wait.until(ExpectedConditions.elementToBeClickable(depositBtn));

        if (depositBtn.isEnabled()) {
            depositBtn.click();
            System.out.println("Clicked the deposit button and submitted.");
        } else {
            Assert.fail("❌ 提交按钮不可点击，入金建单任务失败");
        }

        // 等待“TrustWorthPAY” logo 出现，表明页面已稳定加载
        try {
            WebDriverWait waitLogo = new WebDriverWait(driver, Duration.ofSeconds(10));
            waitLogo.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("img[alt='TrustWorthPAY']")
            ));
            System.out.println("Detected final confirmation element: TrustWorthPAY logo.");
            //在成功跳转后才开始打印单号
            if (orderId.trim().isEmpty()) {
                System.out.println("❌ 未生成 Order ID");
                Assert.fail("❌ 未生成 Order ID，入金建单任务失败");
            }
            System.out.println("Generated Order ID: " + orderId);
        } catch (TimeoutException e) {
            System.out.println("TrustWorthPAY logo not found, page may not be fully loaded.");
            Assert.fail("❌ 未成功跳转至第三方，入金建单任务失败");
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
    @Test(groups = {"Skrill-USD"})
    @Parameters({"ToolUrl", "merCd", "secret", "headless"})
    public void CPSCreateDepositOrderSkrill(String ToolUrl, String merCd, String secret, String headless) throws InterruptedException {
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
        driver.get(ToolUrl);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String oldWindow = driver.getWindowHandle();

        // ===== 选择 Skrill 下拉项 =====
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("choseChannel")));
        WebElement dropdown = driver.findElement(By.id("choseChannel"));
        dropdown.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//option[text()='Skrill']")));
        WebElement dropdownOption = driver.findElement(By.xpath("//option[text()='Skrill']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dropdownOption);
        dropdownOption.click();

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
        new Select(driver.findElement(By.id("environment"))).selectByValue("sandbox");

        fillInput(driver, "merCd", merCd);
        fillInput(driver, "secret", secret);
        WebElement input = driver.findElement(By.cssSelector("input[list='order_currencies_options']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", input); // 清空
        input.sendKeys("USD");
        fillInput(driver, "tag", "AB");
        fillInput(driver, "state", "AutoState");
        fillInput(driver, "zip", "2000");
        fillInput(driver, "city", "AutoCity");
        fillInput(driver, "address", "AutoRd");
        fillInput(driver, "order_amount", "50");

        System.out.println("Skrill Deposit with merCd: " + merCd + ", Amount: 50");

        // ===== 点击按钮 =====
        WebElement generateBtn = driver.findElement(By.id("genOrderNo"));
        generateBtn.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("order_id")));
        String orderId = getNonEmptyValueOrFail(driver, "order_id", "Order ID");
        if (orderId.trim().isEmpty()) {
            System.out.println("❌ 未生成 Order ID");
            Assert.fail("❌ 未生成 Order ID，入金建单任务失败");
        }

        WebElement checkBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='check']")));
        checkBtn.click();

        Thread.sleep(2000);

        // ===== 处理弹窗 =====
        try {
            wait.withTimeout(Duration.ofSeconds(5)).until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            System.out.println("Alert text: " + alert.getText());
            alert.accept();
        } catch (TimeoutException e) {
            System.out.println("No alert present.");
        }

        // ===== 提交订单 =====
        WebElement depositBtn = driver.findElement(By.id("submitGateway"));
        wait.until(ExpectedConditions.elementToBeClickable(depositBtn));

        if (depositBtn.isEnabled()) {
            depositBtn.click();
            System.out.println("Clicked the deposit button and submitted.");
        } else {
            Assert.fail("❌ 提交按钮不可点击，入金建单任务失败");
        }

        List<WebElement> cookieButtons = driver.findElements(By.id("onetrust-accept-btn-handler"));
        if (!cookieButtons.isEmpty()) {
            cookieButtons.get(0).click();
            System.out.println("Accepted cookies.");
        } else {
            System.out.println("Accept Cookies button not present.");
        }

        // 等待“Skrill”logo 出现，表明页面已稳定加载
        try {
            WebDriverWait waitLogo = new WebDriverWait(driver, Duration.ofSeconds(10));
            waitLogo.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("img[src='assets/paysafe-mer-checkout/skrill/images/logo/default.svg']")
            ));
            System.out.println("Detected final confirmation element: Skrill logo.");
            //在成功跳转后才开始打印单号
            if (orderId.trim().isEmpty()) {
                System.out.println("❌ 未生成 Order ID");
                Assert.fail("❌ 未生成 Order ID，入金建单任务失败");
            }
            System.out.println("Generated Order ID: " + orderId);
        } catch (TimeoutException e) {
            System.out.println("Skrill logo not found, page may not be fully loaded.");
            Assert.fail("❌ 未成功跳转至第三方，入金建单任务失败");
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
    @Test(groups = {"Paypal-USD"})
    @Parameters({"ToolUrl", "merCd", "secret", "headless"})
    public void CPSCreateDepositOrderPaypal(String ToolUrl, String merCd, String secret, String headless) throws InterruptedException {
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
        driver.get(ToolUrl);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String oldWindow = driver.getWindowHandle();

        // ===== 选择 PayPal 下拉项 =====
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("choseChannel")));
        WebElement dropdown = driver.findElement(By.id("choseChannel"));
        dropdown.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//option[text()='PayPal']")));
        WebElement dropdownOption = driver.findElement(By.xpath("//option[text()='PayPal']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dropdownOption);
        dropdownOption.click();

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
        new Select(driver.findElement(By.id("environment"))).selectByValue("sandbox");

        fillInput(driver, "merCd", merCd);
        fillInput(driver, "secret", secret);

        WebElement input = driver.findElement(By.cssSelector("input[list='order_currencies_options']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", input); // 清空
        input.sendKeys("USD");

        fillInput(driver, "tag", "AA");
        fillInput(driver, "first_name", "Automation");
        fillInput(driver, "last_name", "Test");
        fillInput(driver, "card_name", "CardName");
        fillInput(driver, "state", "AutoState");
        fillInput(driver, "zip", "2000");
        fillInput(driver, "city", "AutoCity");
        fillInput(driver, "address", "AutoRd");
        fillInput(driver, "order_amount", "1");

        System.out.println("Paypal Deposit with merCd: " + merCd + ", Amount: 1");

        // ===== 点击按钮 =====
        WebElement generateBtn = driver.findElement(By.id("genOrderNo"));
        generateBtn.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("order_id")));
        String orderId = getNonEmptyValueOrFail(driver, "order_id", "Order ID");
        if (orderId.trim().isEmpty()) {
            System.out.println("❌ 未生成 Order ID");
            Assert.fail("❌ 未生成 Order ID，入金建单任务失败");
        }

        WebElement checkBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='check']")));
        checkBtn.click();

        Thread.sleep(2000);

        // ===== 处理弹窗 =====
        try {
            wait.withTimeout(Duration.ofSeconds(5)).until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            System.out.println("Alert text: " + alert.getText());
            alert.accept();
        } catch (TimeoutException e) {
            System.out.println("No alert present.");
        }

        // ===== 提交订单 =====
        WebElement depositBtn = driver.findElement(By.id("submitGateway"));
        wait.until(ExpectedConditions.elementToBeClickable(depositBtn));

        if (depositBtn.isEnabled()) {
            depositBtn.click();
            System.out.println("Clicked the deposit button and submitted.");
        } else {
            Assert.fail("❌ 提交按钮不可点击，入金建单任务失败");
        }

        // 等待“PayPal”logo 出现，表明页面已稳定加载
        try {
            WebDriverWait waitLogo = new WebDriverWait(driver, Duration.ofSeconds(15));
            waitLogo.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("p[aria-label='PayPal Logo']")
            ));
            System.out.println("Detected final confirmation element: PayPal logo.");
            //在成功跳转后才开始打印单号
            if (orderId.trim().isEmpty()) {
                System.out.println("❌ 未生成 Order ID");
                Assert.fail("❌ 未生成 Order ID，入金建单任务失败");
            }
            System.out.println("Generated Order ID: " + orderId);
        } catch (TimeoutException e) {
            System.out.println("PayPal logo not found, page may not be fully loaded.");
            Assert.fail("❌ 未成功跳转至第三方，入金建单任务失败");
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
    @Test(groups = {"TcPay-USD"})
    @Parameters({"ToolUrl", "merCd", "secret", "headless"})
    public void CPSCreateDepositOrderTcPay(String ToolUrl, String merCd, String secret, String headless) throws InterruptedException {
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
        driver.get(ToolUrl);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String oldWindow = driver.getWindowHandle();

        // ===== 选择 TcPay 下拉项 =====
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("choseChannel")));
        WebElement dropdown = driver.findElement(By.id("choseChannel"));
        dropdown.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//option[text()='TcPay']")));
        WebElement dropdownOption = driver.findElement(By.xpath("//option[text()='TcPay']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dropdownOption);
        dropdownOption.click();

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
        new Select(driver.findElement(By.id("environment"))).selectByValue("sandbox");

        fillInput(driver, "merCd", merCd);
        fillInput(driver, "secret", secret);

        WebElement input = driver.findElement(By.cssSelector("input[list='order_currencies_options']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", input); // 清空
        input.sendKeys("USD");

        fillInput(driver, "email", "api_test@ebuycompany.com");
        fillInput(driver, "tag", "AC");
        fillInput(driver, "state", "AutoState");
        fillInput(driver, "zip", "2000");
        fillInput(driver, "city", "AutoCity");
        fillInput(driver, "address", "AutoRd");
        fillInput(driver, "order_amount", "3");

        System.out.println("TcPay Deposit with merCd: " + merCd + ", Amount: 3");

        // ===== 点击按钮 =====
        WebElement generateBtn = driver.findElement(By.id("genOrderNo"));
        generateBtn.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("order_id")));
        String orderId = getNonEmptyValueOrFail(driver, "order_id", "Order ID");
        if (orderId.trim().isEmpty()) {
            System.out.println("❌ 未生成 Order ID");
            Assert.fail("❌ 未生成 Order ID，入金建单任务失败");
        }

        WebElement checkBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='check']")));
        checkBtn.click();

        Thread.sleep(2000);

        // ===== 处理弹窗 =====
        try {
            wait.withTimeout(Duration.ofSeconds(5)).until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            System.out.println("Alert text: " + alert.getText());
            alert.accept();
        } catch (TimeoutException e) {
            System.out.println("No alert present.");
        }

        // ===== 提交订单 =====
        WebElement depositBtn = driver.findElement(By.id("submitGateway"));
        wait.until(ExpectedConditions.elementToBeClickable(depositBtn));

        if (depositBtn.isEnabled()) {
            depositBtn.click();
            System.out.println("Clicked the deposit button and submitted.");
        } else {
            Assert.fail("❌ 提交按钮不可点击，入金建单任务失败");
        }

        // 等待“TCPay” logo 出现，表明页面已稳定加载
        try {
            WebDriverWait waitLogo = new WebDriverWait(driver, Duration.ofSeconds(10));
            waitLogo.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("div.tc-logo")
            ));
            System.out.println("Detected final confirmation element: TCPay logo.");
            //在成功跳转后才开始打印单号
            if (orderId.trim().isEmpty()) {
                System.out.println("❌ 未生成 Order ID");
                Assert.fail("❌ 未生成 Order ID，入金建单任务失败");
            }
            System.out.println("Generated Order ID: " + orderId);
        } catch (TimeoutException e) {
            System.out.println("TCPay logo not found, page may not be fully loaded.");
            Assert.fail("❌ 未成功跳转至第三方，入金建单任务失败");
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
    @Test(groups = {"Cobo-USDTTRC20"})
    @Parameters({"ToolUrl", "merCd", "secret", "headless"})
    public void CPSCreateDepositOrderCoboUSDTTRC20(String ToolUrl, String merCd, String secret, String headless) throws InterruptedException {
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
        driver.get(ToolUrl);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String oldWindow = driver.getWindowHandle();

        // ===== 选择 CoboPay 下拉项 =====
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("choseChannel")));
        WebElement dropdown = driver.findElement(By.id("choseChannel"));
        dropdown.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//option[text()='CoboPay']")));
        WebElement dropdownOption = driver.findElement(By.xpath("//option[text()='CoboPay']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dropdownOption);
        dropdownOption.click();

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
        new Select(driver.findElement(By.id("environment"))).selectByValue("sandbox");

        fillInput(driver, "merCd", merCd);
        fillInput(driver, "secret", secret);
        fillInputByXPath(driver, "//input[@list='crypto_order_currencies_options']", "USDT");
        fillInputByXPath(driver, "//input[@list='crypto_actual_currencies_options']", "USDT-TRC20");
        fillInput(driver, "tag", "CO");
        fillInput(driver, "user_id", "10068233");
        fillInput(driver, "country", "5145");
        fillInput(driver, "order_amount", "11");

        System.out.println("Cobo.USDTTRC20 Deposit with merCd: " + merCd + ", Amount: 11");

        // ===== 点击按钮 =====
        WebElement generateBtn = driver.findElement(By.id("genOrderNo"));
        generateBtn.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("order_id")));
        String orderId = getNonEmptyValueOrFail(driver, "order_id", "Order ID");
        if (orderId.trim().isEmpty()) {
            System.out.println("❌ 未生成 Order ID");
            Assert.fail("❌ 未生成 Order ID，入金建单任务失败");
        }

        WebElement checkBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='check']")));
        checkBtn.click();

        Thread.sleep(2000);

        // ===== 处理弹窗 =====
        try {
            wait.withTimeout(Duration.ofSeconds(5)).until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            System.out.println("Alert text: " + alert.getText());
            alert.accept();
        } catch (TimeoutException e) {
            System.out.println("No alert present.");
        }

        // ===== 提交订单 =====
        WebElement depositBtn = driver.findElement(By.id("submitGateway"));
        wait.until(ExpectedConditions.elementToBeClickable(depositBtn));

        if (depositBtn.isEnabled()) {
            depositBtn.click();
            System.out.println("Clicked the deposit button and submitted.");
        } else {
            Assert.fail("❌ 提交按钮不可点击，入金建单任务失败");
        }

        // 等待“TrustWorthPAY” logo 出现，表明页面已稳定加载
        try {
            WebDriverWait waitLogo = new WebDriverWait(driver, Duration.ofSeconds(10));
            waitLogo.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("img[alt='TrustWorthPAY']")
            ));
            System.out.println("Detected final confirmation element: TrustWorthPAY logo.");
            //在成功跳转后才开始打印单号
            if (orderId.trim().isEmpty()) {
                System.out.println("❌ 未生成 Order ID");
                Assert.fail("❌ 未生成 Order ID，入金建单任务失败");
            }
            System.out.println("Generated Order ID: " + orderId);
        } catch (TimeoutException e) {
            System.out.println("TrustWorthPAY logo not found, page may not be fully loaded.");
            Assert.fail("❌ 未成功跳转至第三方，入金建单任务失败");
        }

        // ===== 关闭所有窗口并退出 ======
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

    // 通用方法：根据 ID 获取非空值，否则抛出断言异常
    private static String getNonEmptyValueOrFail(WebDriver driver, String id, String fieldName) {
        WebElement el = driver.findElement(By.id(id));
        String value = el.getAttribute("value");
        if (value == null || value.trim().isEmpty()) {
            System.out.println("❌ " + fieldName + " 为空");
            Assert.fail("❌ " + fieldName + " 为空，任务失败");
        }
        return value.trim();
    }
}


