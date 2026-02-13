package tools;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.support.ui.Select;

public class CPSCreateWithdrawOrderUSDT {
    public static void main(String[] args) throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("https://test.spnsbm.com/deposit/api-test-tool/index.html");

        // 选择下拉项 "in common use withdrawal (出金)"
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("choseChannel")));
        WebElement dropdown = driver.findElement(By.id("choseChannel"));
        dropdown.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//option[text()='in common use withdrawal (出金)']")));
        WebElement option = driver.findElement(By.xpath("//option[text()='in common use withdrawal (出金)']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", option);
        option.click();

        // 等待新窗口并切换
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        List<String> windows = driver.getWindowHandles().stream().toList();
        driver.switchTo().window(windows.get(1));

        // 填表字段
        Select envSelect = new Select(driver.findElement(By.id("environment")));
        envSelect.selectByValue("sandbox");
        fill(driver, "merCd", "520045110001805");
        fill(driver, "secret", "abeabe46840fb49f85283c84207b4182a049b7df");
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
        fill(driver, "card_number", "TQETUR38cL8nY1gQcr43sWgFFwTKSwQjLw");
        fill(driver, "amount", "50");

        System.out.println("Filled merCd: 520045110001805");
        System.out.println("Filled payment_method: F00000");
        System.out.println("Filled amount: 50");

        // 点击 "Generate" 按钮
        WebElement genBtn = driver.findElement(By.id("genOrderNo"));
        genBtn.click();

        wait.withTimeout(Duration.ofSeconds(5)).until(ExpectedConditions.presenceOfElementLocated(By.id("order_id")));
        String orderId = driver.findElement(By.id("order_id")).getAttribute("value");
        System.out.println("Generated Order ID: " + orderId);

        // 点击 "Get encrypt data"
        WebElement checkBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='check']")));
        checkBtn.click();
        Thread.sleep(2000);

        // 弹窗处理
        try {
            wait.withTimeout(Duration.ofSeconds(5)).until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            System.out.println("Alert text: " + alert.getText());
            alert.accept();
        } catch (TimeoutException e) {
            System.out.println("No alert present.");
        }

        // 提交 withdrawal
        WebElement withdrawalBtn = driver.findElement(By.id("submitGateway"));
        wait.until(ExpectedConditions.elementToBeClickable(withdrawalBtn));

        if (withdrawalBtn.isEnabled()) {
            withdrawalBtn.click();
            System.out.println("Clicked the withdrawal button.");
        } else {
            System.out.println("The withdrawal button is not clickable.");
        }

        Thread.sleep(3000);
        driver.quit();
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