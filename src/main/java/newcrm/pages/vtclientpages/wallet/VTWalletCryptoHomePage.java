package newcrm.pages.vtclientpages.wallet;

import newcrm.pages.clientpages.wallet.WalletCryptoHomePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;


public class VTWalletCryptoHomePage extends WalletCryptoHomePage {
    public VTWalletCryptoHomePage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void validateActionButtons() {
        // 首页默认显示四种货币
        for (String crypto : cryptoList) {
            int rowIndex = getCryptoRowIndex(crypto);
            Assert.assertNotEquals(rowIndex, -1, crypto + " row should exist");

            // 验证操作按钮存在
            assertElementExists(By.xpath("(//span[text()='" + crypto + "'])[2]/../../../../..//span[normalize-space()='Deposit']"), "Deposit button for " + crypto);
            assertElementExists(By.xpath("(//span[text()='" + crypto + "'])[2]/../../../../..//span[normalize-space()='Withdraw']"), "Withdraw button for " + crypto);
            assertElementExists(By.xpath("(//span[text()='" + crypto + "'])[2]/../../../../..//span[normalize-space()='Transfer']"), "Transfer button for " + crypto);
            assertElementExists(By.xpath("(//span[text()='" + crypto + "'])[2]/../../../../..//span[normalize-space()='Convert']"), "Convert button for " + crypto);
            System.out.println("Validated action buttons for " + crypto);
        }
    }

    @Override
    public int getCryptoRowIndex(String cryptoName) {
        List<WebElement> rows = getAllRows();
        for (int i = 0; i < rows.size(); i++) {
            try {
                WebElement cryptoCell = rows.get(i).findElement(By.xpath(".//td[1]//div"));
                if (cryptoCell.getText().trim().contains(cryptoName)) {
                    return i + 1;
                }
            } catch (Exception e) {
                // 继续查找下一行
            }
        }
        return -1;
    }

}
