package tools;

import newcrm.pages.Page;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

/**
 * Selenium 截图工具类（优化版）
 */
public class ScreenshotHelper {
//    public ScreenshotHelper(WebDriver  driver) {
//        super(driver);
//    }

    /**
     * 截图并保存到指定文件夹（元素未找到时截取当前页面）
     * @param driver    WebDriver实例
     * @param element   目标元素（可为null）
     * @param folder    保存目录（如 "target/screenshots"）
     * @param fileName  自定义文件名（不含后缀，为null时用时间戳）
     * @return          截图文件的完整路径，若失败返回null
     */

    public static String takeScreenshot(WebDriver driver, WebElement element, String folder, String fileName) {
        try {
            // 1. 创建文件夹（如果不存在）
            Path dir = Paths.get(folder);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }

            // 2. 高亮元素（如果存在且可见）
            if (element != null && isElementVisible(driver, element)) {
                highlightElement(driver, element);
            } else {
                System.out.println("[WARN] 元素未找到或不可见，将截取当前页面");
            }

            // 3. 生成文件名
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String file = (fileName == null ? "screenshot_" + timestamp : fileName + "_" + timestamp) + ".png";
            Path screenshotPath = dir.resolve(file);

            // 4. 截图并保存
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(screenshot.toPath(), screenshotPath);

            // 返回绝对路径
            String absolutePath = screenshotPath.toAbsolutePath().toString();
            System.out.println("[INFO] 截图保存成功: " + absolutePath);
            return absolutePath;
        } catch (Exception e) {
            System.err.println("[ERROR] 截图失败: " + e.getMessage());
            return null;
        }
    }


    /**
     * 检查元素是否可见
     */
    private static boolean isElementVisible(WebDriver driver, WebElement element) {
        try {
            return element.isDisplayed();
        } catch (StaleElementReferenceException | NoSuchElementException e) {
            return false;
        }
    }

    /**
     * 用红色边框高亮元素
     */
    public static void highlightElement(WebDriver driver, WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            // 红色虚线边框 + 黄色背景
            js.executeScript(
                    "arguments[0].style.border='2px dashed red'; arguments[0].style.backgroundColor='yellow';",
                    element
            );
        } catch (Exception e) {
            System.err.println("[WARN] 元素高亮失败: " + e.getMessage());
        }
    }

    /**
     * 用红色边框高亮元素
     */
    public static void highlightElementBorder(WebDriver driver, WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            // 红色虚线边框
            js.executeScript(
                    "arguments[0].style.border='2px dashed red'; ",
                    element
            );
        } catch (Exception e) {
            System.err.println("[WARN] 元素高亮失败: " + e.getMessage());
        }
    }
    /**
     * 等待元素可见后截图（元素未找到时截取当前页面）
     * @param by      元素定位器
     * @param timeout 等待超时（秒）
     */
    public static String takeScreenshotOfElement(WebDriver driver, By by, String folder, int timeout) {
        try {
            WebElement element = new WebDriverWait(driver, Duration.ofSeconds(timeout))
                    .until(ExpectedConditions.visibilityOfElementLocated(by));
            return takeScreenshot(driver, element, folder, "element_" + by.toString().replace(" ", "_"));
        } catch (TimeoutException e) {
            System.out.println("[WARN] 元素未在 " + timeout + " 秒内找到，将截取当前页面");
            return takeScreenshot(driver, null, folder, "timeout_" + by.toString().replace(" ", "_"));
        }
    }
    // 添加文字水印
    public static void addTextWatermark(WebDriver driver, String text) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script = """
        var watermark = document.createElement('div');
        watermark.innerHTML = arguments[0];
        watermark.style.position = 'fixed';
        watermark.style.top = '50%';
        watermark.style.left = '50%';
        watermark.style.transform = 'translate(-50%, -50%) rotate(-45deg)';
        watermark.style.fontSize = '50px';
        watermark.style.color = 'rgba(200, 200, 200, 0.2)';
        watermark.style.zIndex = '9999';
        watermark.style.pointerEvents = 'none';
        watermark.style.fontWeight = 'bold';
        watermark.style.whiteSpace = 'nowrap';
        document.body.appendChild(watermark);
        """;
        js.executeScript(script, text);
    }

    //设置页面缩放级别
    public static void setZoomLevel(WebDriver driver, double level) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script = "document.body.style.transform = 'scale(" + level + ")';" +
                "document.body.style.transformOrigin = '0 0';";
        js.executeScript(script);
    }

    // 放大页面
    public static void zoomIn(WebDriver driver, int times) {
        WebElement body = driver.findElement(By.tagName("body"));
        for (int i = 0; i < times; i++) {
            body.sendKeys(Keys.chord(Keys.CONTROL, Keys.ADD)); // Ctrl + +
            // 或者使用：body.sendKeys(Keys.chord(Keys.CONTROL, Keys.equals)); // 某些浏览器
        }
    }

    // 缩小页面
    public static void zoomOut(WebDriver driver, int times) {
        WebElement body = driver.findElement(By.tagName("body"));
        for (int i = 0; i < times; i++) {
            body.sendKeys(Keys.chord(Keys.CONTROL, Keys.SUBTRACT)); // Ctrl + -
        }
    }

    // 重置缩放
    public static void resetZoom(WebDriver driver) {
        WebElement body = driver.findElement(By.tagName("body"));
        body.sendKeys(Keys.chord(Keys.CONTROL, "0")); // Ctrl + 0
    }

    //截长图
    public static void catchFullPageScreenshot(WebDriver driver, String fileName) throws InterruptedException, IOException {
        // 获取页面总高度
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Long pageHeight = (Long) js.executeScript("return document.body.scrollHeight");

        // 设置窗口高度
        driver.manage().window().setSize(new Dimension(1200, 800));
        int windowHeight = driver.manage().window().getSize().height;

        // 计算需要截图的次数
        int scrollTimes = (int) (pageHeight / windowHeight) + 1;

        // 创建最终的长图
        BufferedImage finalImage = new BufferedImage(
                1200, pageHeight.intValue(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = finalImage.createGraphics();

        // 滚动并截图
        for (int i = 0; i < scrollTimes; i++) {
            // 滚动到当前位置
            js.executeScript("window.scrollTo(0, " + (windowHeight * i) + ")");
            Thread.sleep(200); // 等待滚动完成

            // 截取当前视口
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            BufferedImage currentImage = ImageIO.read(screenshot);

            // 将当前视口图片拼接到长图中
            graphics.drawImage(currentImage, 0, windowHeight * i, null);
        }

        // 保存最终图片
        ImageIO.write(finalImage, "PNG", new File(fileName));
        graphics.dispose();
    }

    /**
     * 截取整个页面的长截图（包括未显示的部分）
     * @param driver WebDriver实例
     * @param folder 保存截图的目录路径
     * @param fileName 文件名（不含扩展名）
     * @return 截图文件的完整路径，失败时返回null
     */
    public static String takeFullPageScreenshot(WebDriver driver, String folder, String fileName) {
        try {
            // 1. 创建文件夹（如果不存在）
            Path dir = Paths.get(folder);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }

            // 2. 使用aShot截取全页长图
            Screenshot screenshot = new AShot()
                    .shootingStrategy(ShootingStrategies.viewportPasting(500)) // 设置滚动延迟
                    .takeScreenshot(driver);

            // 3. 生成文件名
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String file = (fileName == null ? "fullpage_screenshot_" + timestamp : fileName + "_" + timestamp) + ".png";
            Path screenshotPath = dir.resolve(file);

            // 4. 保存图片
            ImageIO.write(screenshot.getImage(), "PNG", screenshotPath.toFile());

            System.out.println("[INFO] 全页截图保存成功: " + screenshotPath);
            return screenshotPath.toString();
        } catch (Exception e) {
            System.err.println("[ERROR] 全页截图失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}

