package tools;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;

public class CPSCreateDepositOrderHaUsdtPay {

    public static void main(String[] args) throws InterruptedException {
        // 启动 Chrome 浏览器
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        driver.get("https://test.spnsbm.com/deposit/api-test-tool/index.html");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // 找下拉菜单并选择 HaUsdtPay
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("choseChannel")));
        WebElement dropdown = driver.findElement(By.id("choseChannel"));
        dropdown.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//option[text()='HaUsdtPay']")));
        WebElement dropdownOption = driver.findElement(By.xpath("//option[text()='HaUsdtPay']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dropdownOption);
        dropdownOption.click();

        // 等待新窗口打开并切换
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        List<String> windows = driver.getWindowHandles().stream().toList();
        driver.switchTo().window(windows.get(1));

        // 填写环境字段
        driver.findElement(By.id("environment")).sendKeys("Sandbox");

        // 清空并填写其他字段
        fillInput(driver, "merCd", "520045110001805");
        fillInput(driver, "secret", "abeabe46840fb49f85283c84207b4182a049b7df");
        fillInputByXPath(driver, "//input[@list='crypto_order_currencies_options']", "USDT");
        fillInputByXPath(driver, "//input[@list='crypto_actual_currencies_options']", "USDT-TRC20");
        fillInput(driver, "tag", "SK");
        fillInput(driver, "state", "AutoState");
        fillInputByXPath(driver, "//*[@id='payloadForm']/fieldset/div[1]/div/div[8]/input", "1234567");
        fillInput(driver, "zip", "2000");
        fillInput(driver, "city", "AutoCity");
        fillInput(driver, "address", "AutoRd");
        fillInput(driver, "country", "AutoCountry");
        fillInput(driver, "order_amount", "50");

        System.out.println("Filled merCd: 520045110001805");
        System.out.println("Filled amount: 50");

        // 点击 Generate 按钮生成订单号
        WebElement generateBtn = driver.findElement(By.id("genOrderNo"));
        generateBtn.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("order_id")));
        String orderId = driver.findElement(By.id("order_id")).getAttribute("value");
        System.out.println("Generated Order ID: " + orderId);

        // 点击 Get Encrypt Data 按钮
        WebElement checkBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='check']")));
        checkBtn.click();

        Thread.sleep(2000);

        // 处理弹窗
        try {
            wait.withTimeout(Duration.ofSeconds(5)).until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            System.out.println("Alert text: " + alert.getText());
            alert.accept();
        } catch (TimeoutException e) {
            System.out.println("No alert present.");
        }

        // 点击 Submit 按钮
        WebElement depositBtn = driver.findElement(By.id("submitGateway"));
        wait.until(ExpectedConditions.elementToBeClickable(depositBtn));

        if (depositBtn.isEnabled()) {
            depositBtn.click();
            System.out.println("Clicked the deposit button.");
        } else {
            System.out.println("The deposit button is not clickable.");
        }

        Thread.sleep(3000);
        driver.quit();
    }

    private static void fillInput(WebDriver driver, String id, String value) {
        WebElement element = driver.findElement(By.id(id));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", element);
        element.sendKeys(value);
    }

    private static void fillInputByXPath(WebDriver driver, String xpath, String value) {
        WebElement element = driver.findElement(By.xpath(xpath));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", element);
        element.sendKeys(value);
    }
}