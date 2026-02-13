package newcrm.pages.clientpages.wallet;

import newcrm.global.GlobalMethods;
import newcrm.pages.Page;
import newcrm.utils.totp.EmailTOTP;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import utils.LogUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class WalletCryptoHomePage extends Page {

    public WalletCryptoHomePage(WebDriver driver) {
        super(driver);
    }
    protected List<String> cryptoList = Arrays.asList("USDT", "BTC", "ETH", "USDC");
    // 钱包首页-开卡弹窗

    // 钱包首页-开卡弹窗关闭按钮
    protected WebElement getBtnCloseOpenCard() {
        return checkElementExists(By.xpath("//img[@class='close-icon']"), "Wallet Close");
    }
    // 总资产 Total Balance

    // 总余额-行
    protected WebElement getTotalBalance() {
        return checkElementExists(By.xpath("//p[text()=' Total Balance ']/../div[@class='totalBalance']"), "Wallet Total Balance");
    }
    protected WebElement getTotalBalanceUAT() {
        return assertElementExists(By.xpath("//div[@class='balance__total-balance']"), "Wallet Total Balance");
    }

    // 首页币种下拉框
    protected WebElement getSelectCrypto() {
        return assertElementExists(By.xpath("//span[contains(@class,'currency-dropdown__trigger')]"), "Wallet Crypto-dropdown");
    }
    // 首页下拉框选项
    protected WebElement getSelectCryptoOption(String coinName) {
        return assertElementExists(By.xpath("//ul[@class='el-dropdown-menu el-popper currency_style_balance']//span[text()='" + coinName + "']"), "Wallet Crypto-dropdown-" + coinName);
    }
    // 首页币种选择More
    protected WebElement getMoreCrypto() {
        return assertElementExists(By.xpath("//ul[@x-placement='bottom-end']//span[text()='More']"), "Wallet Crypto-dropdown-More select");
    }
    protected WebElement getMoreCryptoUAT(){
        return assertElementExists(By.xpath("//li[@class='el-dropdown-menu__item']//span[text()='More']"), "Wallet Crypto-dropdown-More select");
    }
    // More更多币种选项
    protected WebElement selectMoreCoinType(String coinName) {
        return assertElementExists(By.xpath("//span[@class='currency_item' and text()=' " + coinName + " ']"), "Wallet More-btn " + coinName);
    }
    protected WebElement selectMoreCoinTypeUAT(String coinName) {
        return assertElementExists(By.xpath("//span[@class='currency_item' and normalize-space()='" + coinName + "']"), "Wallet More-btn " + coinName);
    }

    // 总余额-转换-行
    protected WebElement getTotalSwitchBalance() {
        return checkElementExists(By.xpath("//p[text()=' Total Balance ']/../div[@class='switch_balance']"), "Wallet Total switch_balance(≈)");
    }
    protected WebElement getTotalSwitchBalanceUAT() {
        return assertElementExists(By.xpath("//div[@class='balance__total']//div[@class='balance__total-switch-balance']"), "Wallet Total switch_balance(≈)");
    }
    // 总余额-入金按钮
    protected WebElement getBalanceBtnDeposit() {
        return checkElementExists(By.xpath("//div[@class='balance_btn']//span[text()=' Deposit ']"), "Wallet TotalBalance-Deposit");
    }
    protected WebElement getBalanceBtnDepositUAT() {
        return checkElementExists(By.xpath("//div[@class='balance__btn']//span[normalize-space(text())='Deposit']"), "Wallet TotalBalance-Deposit");
    }
    // 总余额-出金按钮
    protected WebElement getBalanceBtnWithdraw() {
        return checkElementExists(By.xpath("//div[@class='balance_btn']//span[text()=' Withdraw ']"), "Wallet TotalBalance-Withdraw");
    }
    protected WebElement getBalanceBtnWithdrawUAT() {
        return checkElementExists(By.xpath("//div[@class='balance__btn']//span[normalize-space(text())='Withdraw']"), "Wallet TotalBalance-Withdraw");
    }
    // 总余额-转账按钮
    protected WebElement getBalanceBtnTransfer() {
        return checkElementExists(By.xpath("//div[@class='balance_btn']//span[text()=' Transfer ']"), "Wallet TotalBalance-Transfer");
    }
    protected WebElement getBalanceBtnTransferUAT() {
        return checkElementExists(By.xpath("//div[@class='balance__btn']//span[normalize-space(text())='Transfer']"), "Wallet TotalBalance-Transfer");
    }
    // 总余额-兑换按钮
    protected WebElement getBalanceBtnConvert() {
        return checkElementExists(By.xpath("//div[@class='balance_btn']//span[text()=' Convert ']"), "Wallet TotalBalance-Convert");
    }
    protected WebElement getBalanceBtnConvertUAT() {
        return checkElementExists(By.xpath("//div[@class='balance__btn']//span[normalize-space(text())='Convert']"), "Wallet TotalBalance-Convert");
    }


    // 可用余额 Available Balance

    // 当前总余额货币图标
    protected WebElement getCurrencyTotalCryptoImg() {
        return assertElementExists(By.xpath("//p[text()=' Total Balance ']/..//div[@class='totalBalance']/img"), "Wallet Crypto-dropdown");
    }
    protected WebElement getCurrencyTotalCryptoImgUAT() {
        return assertElementExists(By.xpath("//div[@class='balance__total-balance']//img[@class='balance__total-currency-img']"), "Wallet Crypto-dropdown");
    }
    // 可用余额-行
    protected WebElement getAvailableBalance() {
        return checkElementExists(By.xpath("//p[contains(text(),'Available Balance')]/../div[@class='totalBalance']"), "Wallet Available Balance");
    }
    protected WebElement getAvailableBalanceUAT() {
        return checkElementExists(By.xpath("//div[contains(text(),' Available Balance ')]/.."), "Wallet Available Balance");
    }
    // 首页可用余额单位
    protected WebElement getAvailableBalanceUnit() {
        return assertElementExists(By.xpath("//span[@class='available_Balance_currency']"), "Wallet Available Balance Unit");
    }
    protected WebElement getAvailableBalanceUnitUAT() {
        return assertElementExists(By.xpath("//span[@class='balance__available-currency']"), "Wallet Available Balance Unit");
    }
    // 可用余额-转换-行
    protected WebElement getAvailableSwitchBalance() {
        return checkElementExists(By.xpath("//p[contains(text(),'Available Balance')]/../div[@class='switch_balance']"), "Wallet Available switch_balance(≈)");
    }
    protected WebElement getAvailableSwitchBalanceUAT() {
        return assertElementExists(By.xpath("//div[@class='balance__available']//div[@class='balance__available-switch-balance']"), "Wallet Available switch_balance(≈)");
    }
    // 首页可以用余额图标
    protected WebElement getCurrencyAvailableCryptoImg() {
        return assertElementExists(By.xpath("//p[contains(text(),'Available Balance')]/..//div[@class='totalBalance']/img"), "Wallet Available Balance Unit");
    }


    // 币种列表-Crypto List

    // 获取钱包表格元素
    protected WebElement getWalletTable() {
        return assertElementExists(By.xpath("//div[@class='wallet_table']"), "Wallet Table Container");
    }
    //钱包overview页面ByCoin已被选择
    protected WebElement getWalletByCoin(){
        return assertElementExists(By.xpath("//div[@id='tab-By Coin' and @aria-selected='true']"), "wallet by coin is not selected");
    }
    //funding页面的table
    protected WebElement getWalletTableUAT(){
        return assertElementExists(By.xpath("//div[@class='el-tabs__content']/div[@id='pane-funding']"), "Wallet Table Container");
    }


    // 入金页面
    // 入金选择币种按钮
    protected WebElement getBtnChooseCoin() {
        return checkElementExists(By.xpath("//div[text()='Choose coin']/../div[@class='deposit-selector currency-input']//input"), "Wallet Deposit-Choose coin");
    }

    // 入金选择对应币种
    protected WebElement getChooseCoinOption(String coinName) {
        return assertElementExists(By.xpath("//li//span[text()='" + coinName + "']"), "Wallet Deposit-Choose coin-" + coinName);
    }

    // 入金选择对应Network按钮
    protected WebElement getChooseNetwork() {
        return assertElementExists(By.xpath("//div[text()='Choose Network']/..//i"), "Wallet Deposit-Choose Network");
    }
    // 入金选择对应Network选项
    protected WebElement getChooseNetworkOption(String networkName) {
        return assertElementExists(By.xpath("//div[@class='el-scrollbar']//span[text()='" + networkName + "']"), "Wallet Deposit-Choose Network-" + networkName);
    }
    // 入金二维码
    protected WebElement getQRCode() {
        return assertElementExists(By.xpath("//canvas"), "Wallet Deposit-QRCode");
    }
    // 入金地址
    protected WebElement getDepositAddress() {
        return assertElementExists(By.xpath("//div[@class='copy-text']"), "Wallet Deposit-Address");
    }
    // 入金信息-入金币种
    protected WebElement getDepositInfoCoinType() {
        return assertElementExists(By.xpath("//span[text()='Deposit To']/../span[2]"), "Wallet Deposit-Info-Deposit To");
    }
    // 入金信息-入金提示
    protected WebElement getDepositInfoNote() {
        return assertElementExists(By.xpath("//div[@class='address-alert']"), "Wallet Deposit-Info-Note");
    }

    // 出金页面
    // 出金选择币种按钮
    protected WebElement getBtnChooseCoinWithdraw() {
        return assertElementExists(By.xpath("//div[text()=' Choose Coin ']/../..//input"), "Wallet Withdraw-Choose coin");
    }
    // 出金选择对应币种
    protected WebElement getChooseCoinOptionWithdraw(String coinName) {
        return assertElementExists(By.xpath("//div[@class='el-scrollbar']//div[text()=' "+ coinName +" ']"), "Wallet Withdraw-Choose coin-" + coinName);
    }

    // 出金页面-输入出金地址
    protected WebElement getWithdrawAddressInput() {
        return assertElementExists(By.xpath("//input[@placeholder='Please enter Address']"), "Wallet Withdraw-Address Input");
    }

    // 出金页面-选择链路Chain按钮
    protected WebElement getWithdrawChooseChainBtn() {
        return assertElementExists(By.xpath("//input[@placeholder='Please Select Chain']"), "Wallet Withdraw-Choose Chain");
    }

    // 出金页面-选择链路Chain选项
    protected WebElement getWithdrawChooseChainOption(String chainName) {
        return assertElementExists(By.xpath("//div[@class='chain-name' and text()='" + chainName + "']"), "Wallet Withdraw-Choose Chain-" + chainName);
    }

    // 出金页面-输入出金金额
    protected WebElement getWithdrawInputAmount() {
        return assertElementExists(By.xpath("//input[@placeholder='Please Enter']"), "Wallet Withdraw-Input Amount");
    }

    // 出金页面-出金信息-From
    protected WebElement getWithdrawInfoFrom() {
        return assertElementExists(By.xpath("//div[@class='withdraw-from-value']"), "Wallet Withdraw-Info-From");
    }

    // 出金页面-出金信息-收到金额ReceiveAmount
    protected WebElement getWithdrawInfoReceiveAmount() {
        return assertElementExists(By.xpath("//div[@class='receive-amount']"), "Wallet Withdraw-Info-ReceiveAmount");
    }

    // 出金页面-选择出金按钮-提交
    protected WebElement getWithdrawBtnSubmit() {
        return assertElementExists(By.xpath("//button//span[text()=' Withdraw ']"), "Wallet Withdraw-Btn-Submit");
    }

    // 出金页面-出金表单ReviewFrom
    protected WebElement getWithdrawReviewFrom() {
        return checkElementExists(By.xpath("//div[text()=' Withdraw From ']/following-sibling::div[@class='data-item-value']"), "Wallet Withdraw-ReviewFrom");
    }

    // 出金页面-出金表单ReviewNetwork
    protected WebElement getWithdrawReviewNetwork() {
        return checkElementExists(By.xpath("//div[text()=' Network ']/following-sibling::div[@class='data-item-value']"), "Wallet Withdraw-Review");
    }

    // 出金页面-出金表单ReviewAddress
    protected WebElement getWithdrawReviewAddress() {
        return checkElementExists(By.xpath("//div[text()=' Withdraw Address ']/following-sibling::div[@class='data-item-value']"), "Wallet Withdraw-Review");
    }

    // 出金页面-出金表单Review-Amount
    protected WebElement getWithdrawReviewAmount() {
        return checkElementExists(By.xpath("//div[text()=' Withdraw Amount ']/following-sibling::div[@class='data-item-value']"), "Wallet Withdraw-Review-Amount");
    }


    // 出金页面-出金表单Review-提交确认Confirm
    protected WebElement getWithdrawReviewBtnConfirm() {
        return assertElementExists(By.xpath("//div[@class='withdraw-confirm-footer']//span[text()=' Confirm ']"), "Wallet Withdraw-Review-Btn-Confirm");
    }

    // 出金页面-输入2FA验证码
    protected WebElement getWithdrawInput2FA() {
        return assertElementExists(By.xpath("//input[@class='input-box']"), "Wallet Withdraw-Input 2FA");
    }

    // 出金页面-输入2FA验证码-OK
    protected WebElement getWithdrawInput2FAOK() {
        return assertElementExists(By.xpath("//div[@class='el-dialog__body']/following-sibling::div//span[text()=' OK ']"), "Wallet Withdraw-Input 2FA-OK");
    }

    // 出金页面-输入邮箱验证码
    protected WebElement getWithdrawInputEmail() {
        return assertElementExists(By.xpath("//input[@class='input-box']"), "Wallet Withdraw-Input Email");
    }

    // 出金页面-输入邮箱验证码-OK
    protected WebElement getWithdrawInputEmailOK() {
        return assertElementExists(By.xpath("//div[@class='el-dialog__body']/following-sibling::div//span[text()=' OK ']"), "Wallet Withdraw-Input Email-OK");
    }

    protected WebElement getFundingPage(){
        return assertElementExists(By.xpath("//span[text()='Funding']"), "Wallet Funding");
    }

    /**
     * 获取VWallet页面总览值，包括账户和理财
     * @return
     */
    protected WebElement getVWalletOverviewTotalValue(){
        return assertElementExists(By.xpath("//div[@class='balance_content']//div[@class='totalBalance']/div/span/span"), "Wallet Overview-Total Value");
    }

    protected WebElement getVWalletFundingTotalValue(){
        return assertElementExists(By.xpath("//div[@class='balance__total-balance']/div/span/span"), "Wallet Funding-Total Value");
    }
    protected WebElement getVWalletEarnTotalValue(){
        return assertElementExists(By.xpath("//div[@class='totalBalance']/div/span/span"), "Wallet Earn-Total Value");
    }
    /**
     * 获取VWallet页面总览币种
     * @return
     */
    protected WebElement getVWalletOverviewTotalCurrency(){
        return assertElementExists(By.xpath("//div[@class='balance_content']//div[@class='totalBalance']/div[@class='currency-selector']//span[@class='currency-dropdown__trigger']"), "Wallet Overview-Total Currency");
    }

    /**
     * 获取VWallet页面按钮，包括入金、出金、转账、闪兑
     * @return
     */
    protected List<WebElement> listVWalletOverviewButton(){
        return assertElementsExists(By.xpath("//div[@class='balance_content']//div[@class='balance_btn']//span"),"VWallet Overview Button");
    }
    protected List<WebElement> listFundingButton(){
        return assertElementsExists(By.xpath("//div[@class='balance__btn']/button/span"),"Wallet Funding Button");
    }

    protected List<WebElement> listEarnButton(){
        return assertElementsExists(By.xpath("//div[@class='balance_btn']/button/span"),"Wallet Earn Button");
    }
    /**
     * assets的类别按钮
     * @param buttonName  By Coin/By Account
     * @return
     */
    protected WebElement getAssetsButton(String buttonName){
        return assertElementExists(By.xpath("//div[@class='wallet_table']//div[@class='el-tabs__header is-top']//div[text()='"+buttonName+"']"),buttonName);
    }
    // 钱包首页-关闭开卡弹框
    public void closeOpenCardDialog() {
        // 自适配关闭弹框
        if (getBtnCloseOpenCard()!=  null){
            getBtnCloseOpenCard().click();
        }
    }

    // 获取所有行
    public List<WebElement> getAllRows() {
        return assertElementsExists(
            By.xpath("//div[@class='wallet_table']//div[contains(@class,'el-table__body-wrapper')]//table//tr"),
            "Wallet Table Rows");
    }
    public List<WebElement> getAllRowsFunding(){
        return assertElementsExists(
            By.xpath("//div[@id='pane-funding']//div[contains(@class,'el-table__body-wrapper')]//table//tr"),
            "Wallet By Coin Table Rows");
    }

    /**
     * 获取加密货币的可用余额
     * @return 加密货币的余额列表
     */
    public Map<String,String> getCryptoAvailableBalance(){
        List<WebElement> rows = getAllRowsFunding();
        Map<String,String> balances = new HashMap<>();
        for (WebElement row : rows){
            WebElement availableBalance = row.findElement(By.xpath(".//td[3]//div"));
            String balance = availableBalance.getText().trim().replaceAll("[^0-9.]","");
            Assert.assertNotNull(balance, "Wallet Crypto Available Balance is null: " + balance);
            WebElement coin = row.findElement(By.xpath(".//td[1]//div"));
            String coinName = coin.getText().trim();
            balances.put(coinName, balance);
        }
        Assert.assertNotNull( balances, "Wallet Crypto Available Balance is null: "+balances);
        Assert.assertTrue(balances.size()>=4, "Wallet Crypto Available Balance size is 4: " + balances);
        LogUtils.info("Wallet Crypto Available Balance: " + balances);
        return balances;
    }

    // 根据加密货币名称获取行索引
    public int getCryptoRowIndex(String cryptoName) {
        List<WebElement> rows = getAllRows();
        for (int i = 0; i < rows.size(); i++) {
            try {
                WebElement cryptoCell = rows.get(i).findElement(By.xpath(".//td[1]//div"));
                if (cryptoCell.getText().trim().equalsIgnoreCase(cryptoName)) {
                    return i + 1;
                }
            } catch (Exception e) {
                // 继续查找下一行
            }
        }
        return -1;
    }
    public int getCryptoRowIndexUAT(String cryptoName) {
        List<WebElement> rows = getAllRowsFunding();
        for (int i = 0; i < rows.size(); i++) {
            try {
                WebElement cryptoCell = rows.get(i).findElement(By.xpath(".//td[1]//div"));
                if (cryptoCell.getText().trim().equalsIgnoreCase(cryptoName)) {
                    return i + 1;
                }
            } catch (Exception e) {
                // 继续查找下一行
            }
        }
        return -1;
    }

    // 验证表格结构
    public void validateWalletTableStructure() {
        // 确认表格存在
        getWalletTable();

        // 获取所有行
        List<WebElement> rows = getAllRows();

        // 验证至少有4行数据（USDT, BTC, ETH, USDC）
        Assert.assertTrue(rows.size() >= 4, "Expected at least 4 rows in wallet table, but found " + rows.size());

        // 验证每行有正确的列数
        for (int i = 0; i < rows.size(); i++) {
            List<WebElement> cells = rows.get(i).findElements(By.xpath(".//td"));
            Assert.assertEquals(cells.size(), 6, "Row " + (i+1) + " should have 5 columns, but found " + cells.size());
        }
        System.out.println("Wallet table structure validated.");
    }
    public void validateWalletTableStructureUAT() {
        // 确认表格存在
        getWalletTableUAT();

        // 获取所有行
        List<WebElement> rows = getAllRowsFunding();

        // 验证至少有4行数据（USDT, BTC, ETH, USDC）
        Assert.assertTrue(rows.size() >= 4, "Expected at least 4 rows in wallet table, but found " + rows.size());

        // 验证每行有正确的列数
        for (int i = 0; i < rows.size(); i++) {
            List<WebElement> cells = rows.get(i).findElements(By.xpath(".//td"));
            Assert.assertEquals(cells.size(), 5, "Row " + (i+1) + " should have 5 columns, but found " + cells.size());
        }
        LogUtils.info("Wallet table structure validated.");
    }

    // 验证特定加密货币数据
    public void validateCryptoData(String cryptoName) {
        validateCryptoData(cryptoName, null, null, null, null);
    }

    public void validateCryptoData(String cryptoName, String totalBalance, String equivalent, String availableBalance, String frozen) {
        int cryptoRowIndex = getCryptoRowIndex(cryptoName);
        Assert.assertNotEquals(cryptoRowIndex, -1, cryptoName + " row should exist in the table");

        // 获取指定加密货币行的所有单元格
        List<WebElement> cryptoCells = getRowCells(cryptoRowIndex);

        // 验证加密货币列（第1列）
        Assert.assertEquals(cryptoCells.get(0).getText().trim(), cryptoName, "Crypto currency should be " + cryptoName);

        // 验证总余额列（第2列）
        if (totalBalance != null) {
            Assert.assertEquals(cryptoCells.get(1).getText().trim(), totalBalance, "Total balance should be " + totalBalance);
        } else {
            // 默认验证大于零
            String totalBalanceText = cryptoCells.get(1).getText().trim();
            double balance = Double.parseDouble(totalBalanceText);
            Assert.assertTrue(balance >= 0, "Total balance should be ≥ 0, but found: " + totalBalanceText);
        }

        // 验证等值列（第3列）
        if (equivalent != null) {
            Assert.assertEquals(cryptoCells.get(2).getText().trim(), equivalent, "Equivalent should be " + equivalent);
        } else {
            // 默认验证大于等于零
            String equivalentText = cryptoCells.get(2).getText().trim().split(" ")[0]; // 只取数字部分
            double equivalentAmount = Double.parseDouble(equivalentText);
            Assert.assertTrue(equivalentAmount >= 0, "Equivalent should be ≥ 0, but found: " + equivalentText);
        }

        // 验证可用余额列（第4列）
        if (availableBalance != null) {
            Assert.assertEquals(cryptoCells.get(3).getText().trim(), availableBalance, "Available balance should be " + availableBalance);
        } else {
            // 默认验证大于等于零
            String availableBalanceText = cryptoCells.get(3).getText().trim();
            double availableBalanceValue = Double.parseDouble(availableBalanceText);
            Assert.assertTrue(availableBalanceValue >= 0, "Available balance should be ≥ 0, but found: " + availableBalanceText);
        }

        // 验证冻结金额列（第5列）
        if (frozen != null) {
            Assert.assertEquals(cryptoCells.get(4).getText().trim(), frozen, "Frozen should be " + frozen);
        } else {
            // 默认验证大于等于零
            String frozenText = cryptoCells.get(4).getText().trim();
            double frozenValue = Double.parseDouble(frozenText);
            Assert.assertTrue(frozenValue >= 0, "Frozen should be ≥ 0, but found: " + frozenText);
        }
        System.out.println("Crypto data for " + cryptoName + " validated.");
    }

    public void validateCryptoDataUAT(String cryptoName) {
        validateCryptoDataUAT(cryptoName, null, null, null, null);
    }
    public void validateCryptoDataUAT(String cryptoName, String totalBalance, String equivalent, String availableBalance, String frozen){
        int cryptoRowIndex = getCryptoRowIndexUAT(cryptoName);
        Assert.assertNotEquals(cryptoRowIndex, -1, cryptoName + " row should exist in the table");
        // 获取指定加密货币行的所有单元格
        List<WebElement> cryptoCells = getRowCells(cryptoRowIndex);

        // 验证加密货币列（第1列）
        Assert.assertEquals(cryptoCells.get(0).getText().trim(), cryptoName, "Crypto currency should be " + cryptoName);

        // 验证总余额列（第2列）
        if (totalBalance != null) {
            Assert.assertEquals(cryptoCells.get(1).findElement(By.xpath("//span[@class='original-value']")).getText().trim(), totalBalance, "Total balance should be " + totalBalance);
        } else {
            // 默认验证大于零
            String totalBalanceText = cryptoCells.get(1).findElement(By.xpath("//span[@class='original-value']")).getText().trim();
            double balance = Double.parseDouble(totalBalanceText);
            Assert.assertTrue(balance >= 0, "Total balance should be ≥ 0, but found: " + totalBalanceText);
        }

        // 验证等值列（第2列）
        if (equivalent != null) {
            Assert.assertEquals(cryptoCells.get(1).findElement(By.xpath("//span[@class='fait-value']")).getText().trim(), equivalent, "Equivalent should be " + equivalent);
        } else {
            // 默认验证大于等于零
            String equivalentText = cryptoCells.get(1).findElement(By.xpath("//span[@class='fait-value']")).getText().trim().replaceAll("[^0-9.]", "").split(" ")[0]; // 只取数字部分
            double equivalentAmount = Double.parseDouble(equivalentText);
            Assert.assertTrue(equivalentAmount >= 0, "Equivalent should be ≥ 0, but found: " + equivalentText);
        }

        // 验证可用余额列（第3列）
        if (availableBalance != null) {
            Assert.assertEquals(cryptoCells.get(2).getText().trim(), availableBalance, "Available balance should be " + availableBalance);
        } else {
            // 默认验证大于等于零
            String availableBalanceText = cryptoCells.get(2).getText().trim();
            double availableBalanceValue = Double.parseDouble(availableBalanceText);
            Assert.assertTrue(availableBalanceValue >= 0, "Available balance should be ≥ 0, but found: " + availableBalanceText);
        }

        // 验证冻结金额列（第4列）
        if (frozen != null) {
            Assert.assertEquals(cryptoCells.get(3).getText().trim(), frozen, "Frozen should be " + frozen);
        } else {
            // 默认验证大于等于零
            String frozenText = cryptoCells.get(3).getText().trim();
            double frozenValue = Double.parseDouble(frozenText);
            Assert.assertTrue(frozenValue >= 0, "Frozen should be ≥ 0, but found: " + frozenText);
        }
        LogUtils.info("Crypto data for " + cryptoName + " validated.");
    }
    // 获取指定行的所有单元格
    private List<WebElement> getRowCells(int rowIndex) {
        String rowXPath = String.format(
            "//div[@class='wallet_table']//div[contains(@class,'el-table__body-wrapper')]//table//tr[%d]//td",
            rowIndex);
        return assertElementsExists(By.xpath(rowXPath), "Row " + rowIndex + " cells");
    }

    // 获取指定加密货币的指定单元格文本值
    public String getCryptoCellValue(String cryptoName, int columnIndex) {
        // 获取指定加密货币的行号
        int rowIndex = getCryptoRowIndex(cryptoName);
        Assert.assertNotEquals(rowIndex, -1, cryptoName + " row should exist in the table");
        // 获取指定行指定列的XPath
        String cellXPath = String.format(
            "//div[@class='wallet_table']//div[contains(@class,'el-table__body-wrapper')]//table//tr[%d]//td[%d]//div | " +
            "//div[@class='wallet_table']//div[contains(@class,'el-table__body-wrapper')]//table//tr[%d]//td[%d]",
            rowIndex, columnIndex, rowIndex, columnIndex);
        //  判断指定行指定列的元素存在
        WebElement cell = assertElementExists(By.xpath(cellXPath), cryptoName + " column: " + columnIndex + " row: " + rowIndex );
        return cell.getText().trim();
    }
    public String getCryptoCellValueUAT(String cryptoName, int columnIndex) {
        // 获取指定加密货币的行号
        int rowIndex = getCryptoRowIndexUAT(cryptoName);
        Assert.assertNotEquals(rowIndex, -1, cryptoName + " row should exist in the table");
        // 获取指定行指定列的XPath
        String cellXPath = String.format(
                "//div[@class='wallet_table']//div[contains(@class,'el-table__body-wrapper')]//table//tr[%d]//td[%d]//div//span[@class='fait-value']  | " +
                        "//div[@class='wallet_table']//div[contains(@class,'el-table__body-wrapper')]//table//tr[%d]//td[%d]//div//span[@class='fait-value'] ",
                rowIndex, columnIndex, rowIndex, columnIndex);
        //  判断指定行指定列的元素存在
        WebElement cell = assertElementExists(By.xpath(cellXPath), cryptoName + " column: " + columnIndex + " row: " + rowIndex );
        return cell.getText().trim();
    }
    // 验证操作按钮
    public void validateActionButtons() {
        // 首页默认显示四种货币
        for (String crypto : cryptoList) {
            int rowIndex = getCryptoRowIndex(crypto);
            Assert.assertNotEquals(rowIndex, -1, crypto + " row should exist");

            // 验证操作按钮存在
            assertElementExists(By.xpath("//*[text()='" + crypto + " ']/../..//span[text()=' Deposit ']"), "Deposit button for " + crypto);
            assertElementExists(By.xpath("//*[text()='" + crypto + " ']/../..//span[text()=' Withdraw ']"), "Withdraw button for " + crypto);
            assertElementExists(By.xpath("//*[text()='" + crypto + " ']/../..//span[text()=' Transfer ']"), "Transfer button for " + crypto);
            assertElementExists(By.xpath("//*[text()='" + crypto + " ']/../..//span[text()=' Convert ']"), "Convert button for " + crypto);
            System.out.println("Validated action buttons for " + crypto);
        }

    }
    public void validateActionButtonsFunding() {
        // 首页默认显示四种货币
        for (String crypto : cryptoList) {
            int rowIndex = getCryptoRowIndexUAT(crypto);
            Assert.assertNotEquals(rowIndex, -1, crypto + " row should exist");

            // 验证操作按钮存在
            assertElementExists(By.xpath("//*[text()='" + crypto + " ']/../..//span[text()=' Deposit ']"), "Deposit button for " + crypto);
            assertElementExists(By.xpath("//*[text()='" + crypto + " ']/../..//span[text()=' Withdraw ']"), "Withdraw button for " + crypto);
            assertElementExists(By.xpath("//*[text()='" + crypto + " ']/../..//span[text()=' Transfer ']"), "Transfer button for " + crypto);
            assertElementExists(By.xpath("//*[text()='" + crypto + " ']/../..//span[text()=' Convert ']"), "Convert button for " + crypto);
            System.out.println("Validated action buttons for " + crypto);
        }

    }


    // 验证数值格式
    public void validateNumericFormats() {
        List<WebElement> rows = getAllRows();

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.xpath(".//td"));
            if (cells.size() >= 5) {
                String cryptoName = cells.get(0).getText().trim();
                assertVisibleElementExists(By.xpath("//*[text()='" + cryptoName + " ']/..//img[@class='currencyImg']"), "Currency image for " + cryptoName);

                // 验证总余额格式（第2列）
                String balance = cells.get(1).getText().trim();
                Assert.assertTrue(isValidNumberFormat(balance) || isValidZeroBalanceFormat(balance),
                    cryptoName + " balance format is invalid: " + balance);

                // 验证价值格式（第3列，包含货币单位）
                String value = cells.get(2).getText().trim();
                Assert.assertTrue(isValidCurrencyFormat(value) || isValidZeroCurrencyFormat(value),
                    cryptoName + " value format is invalid: " + value);

                // 验证可用存款格式（第4列）
                String pendingDeposit = cells.get(3).getText().trim();
                Assert.assertTrue(isValidNumberFormat(pendingDeposit) || isValidZeroBalanceFormat(pendingDeposit),
                    cryptoName + " pending deposit format is invalid: " + pendingDeposit);

                // 验证冻结款格式（第5列）
                String pendingWithdrawal = cells.get(4).getText().trim();
                Assert.assertTrue(isValidNumberFormat(pendingWithdrawal) || isValidZeroBalanceFormat(pendingWithdrawal),
                    cryptoName + " pending withdrawal format is invalid: " + pendingWithdrawal);
                System.out.println("Validated numeric formats for " + cryptoName);
            }
        }
    }

    private boolean isValidNumberFormat(String number) {
        // 验证纯数字格式（可包含小数点）
        return number.matches("\\d+(\\.\\d+)?");
    }

    private boolean isValidZeroBalanceFormat(String number) {
        // 验证零余额格式
        return number.matches("0+\\.0+");
    }

    private boolean isValidCurrencyFormat(String currency) {
        // 验证货币格式（数字 + 货币单位）
        return currency.matches("\\d+(\\.\\d+)? [A-Z]{3}");
    }

    private boolean isValidZeroCurrencyFormat(String currency) {
        // 验证零货币格式
        return currency.matches("0+\\.0+ [A-Z]{3}");
    }

    // 验证表格完整性
    public void validateWalletTable() {
        // 验证表格结构
        validateWalletTableStructure();
        // 验证操作按钮
        validateActionButtons();
        // 验证数值格式
        validateNumericFormats();
    }
    public void validateWalletTableFunding(){
        //验证Funding页面表格结构
        validateWalletTableStructureUAT();
        validateActionButtonsFunding();

    }
    // 模块显示总余额、可用余额、总余额切换、可用余额切换
    public void checkBalanceView() {
        WebElement totalBalance = getTotalBalance();
        WebElement availableBalance = getAvailableBalance();
        WebElement totalSwitchBalance = getTotalSwitchBalance();
        WebElement availableSwitchBalance = getAvailableSwitchBalance();

        boolean checkBalanceView = totalBalance != null && availableBalance != null && totalSwitchBalance != null && availableSwitchBalance != null;
        Assert.assertTrue(checkBalanceView, "check TotalBalanceView fail");
        System.out.println("check TotalBalanceView success");
    }
    public void checkBalanceViewUAT(){
        WebElement totalBalance = getTotalBalanceUAT();
        WebElement availableBalance = getAvailableBalanceUAT();
        WebElement totalSwitchBalance = getTotalSwitchBalanceUAT();
        WebElement availableSwitchBalance = getAvailableSwitchBalanceUAT();

        boolean checkBalanceView = totalBalance != null && availableBalance != null && totalSwitchBalance != null && availableSwitchBalance != null;
        Assert.assertTrue(checkBalanceView, "check TotalBalanceView fail");
        LogUtils.info("check TotalBalanceView success");
    }
    // 模块校验入金、出金、转账、闪兑按钮
    public void checkBalanceBtn() {
        boolean checkBalanceBtn = getBalanceBtnDeposit() != null && getBalanceBtnWithdraw() != null && getBalanceBtnTransfer() != null && getBalanceBtnConvert() != null;
        Assert.assertTrue(checkBalanceBtn, "check BalanceButton fail");
        System.out.println("check BalanceButton success");
    }
    public void checkBalanceBtnUAT() {
        boolean checkBalanceBtn = getBalanceBtnDepositUAT() != null && getBalanceBtnWithdrawUAT() != null && getBalanceBtnTransferUAT() != null && getBalanceBtnConvertUAT() != null;
        Assert.assertTrue(checkBalanceBtn, "check BalanceButton fail");
        LogUtils.info("check BalanceButton success");
    }
    // 模块直接点击入金按钮，校验默认币种
    public void checkBalanceBtnDeposit(){
        String firstCryptoName = "USDT";
        clickElement(getBalanceBtnDeposit());
        waitLoading();
        assertElementsExists(By.xpath("//span[@class='text-a' and text()='" + firstCryptoName + "']/ancestor::li[contains(@class, 'selected')]"), firstCryptoName + "not the default choice");
    }
    public void checkBalanceBtnDepositUAT(){
        String firstCryptoName = "USDT";
        clickElement(getBalanceBtnDepositUAT());
        waitLoading();
        assertElementsExists(By.xpath("//span[@class='text-a' and text()='" + firstCryptoName + "']/ancestor::li[contains(@class, 'selected')]"), firstCryptoName + "not the default choice");
    }

    // 下拉列表选择货币并校验模块单位
    public void selectCrypto(String cryptoName) {
        GlobalMethods.printDebugInfo("======Current crypto is: " + cryptoName + "======");
        clickElement(getSelectCrypto());
        clickElement(getSelectCryptoOption(cryptoName));
        waitLoading();
        // 获取当前货币单位
        String currentTotalCryptoName = getSelectCrypto().getText().trim();
        String currentAvailableCryptoName = getAvailableBalanceUnit().getText().trim();
        // 图标路径名称包含 cryptoName
        boolean currentTotalIMG = getCurrencyTotalCryptoImg().getAttribute("data-src").contains(cryptoName);
        //上线新UI后available balance没有图标了
//        boolean currentAvailableIMG = getCurrencyAvailableCryptoImg().getAttribute("data-src").contains(cryptoName);
        // 验证当前币种单位
        Assert.assertEquals(currentTotalCryptoName, cryptoName, "Select crypto fail, TotalBanlance Unit is not " + cryptoName);
        Assert.assertEquals(currentAvailableCryptoName, cryptoName, "Select crypto fail, AvailableBanlance Unit is not " + cryptoName);
        // 验证当前币种图标
        Assert.assertTrue(currentTotalIMG, "Select crypto fail, TotalBanlance IMG is not " + cryptoName);
//        Assert.assertTrue(currentAvailableIMG, "Select crypto fail, AvailableBanlance IMG is not " + cryptoName);
        GlobalMethods.printDebugInfo("Select crypto success, TotalBanlance Unit is " + cryptoName);
        GlobalMethods.printDebugInfo("Select crypto success, AvailableBanlance Unit is " + cryptoName);
    }
    public void selectCryptoUAT(String cryptoName) {
        LogUtils.info("======Current crypto is: " + cryptoName + "======");
        clickElement(getSelectCrypto());
        clickElement(getSelectCryptoOption(cryptoName));
        waitLoading();
        // 获取当前货币单位
        String currentTotalCryptoName = getSelectCrypto().getText().trim();
        String currentAvailableCryptoName = getAvailableBalanceUnitUAT().getText().trim();
        // 图标路径名称包含 cryptoName
        boolean currentTotalIMG = getCurrencyTotalCryptoImgUAT().getAttribute("src").contains(cryptoName);
        //可用余额没有图标
//        boolean currentAvailableIMG = getCurrencyAvailableCryptoImg().getAttribute("data-src").contains(cryptoName);
        // 验证当前币种单位
        Assert.assertEquals(currentTotalCryptoName, cryptoName, "Select crypto fail, TotalBanlance Unit is not " + cryptoName);
        Assert.assertEquals(currentAvailableCryptoName, cryptoName, "Select crypto fail, AvailableBanlance Unit is not " + cryptoName);
        // 验证当前币种图标
        Assert.assertTrue(currentTotalIMG, "Select crypto fail, TotalBanlance IMG is not " + cryptoName);
//        Assert.assertTrue(currentAvailableIMG, "Select crypto fail, AvailableBanlance IMG is not " + cryptoName);
        LogUtils.info("Select crypto success, TotalBanlance Unit is " + cryptoName);
        LogUtils.info("Select crypto success, AvailableBanlance Unit is " + cryptoName);
    }
    // More选择等值单位并校验模块与列表单位
    public void selectCryptoEqualValue(String coinName) {
        GlobalMethods.printDebugInfo("=======Current coinName is: " + coinName + "=======");
        clickElement(getSelectCrypto());
        clickElement(getMoreCrypto());
        clickElement(selectMoreCoinType(coinName));
        waitLoading();
        boolean currentTotalSwicthUnit = getTotalSwitchBalance().getText().trim().contains(coinName);
        boolean currentAvailableSwicthUnit = getAvailableSwitchBalance().getText().trim().contains(coinName);
        GlobalMethods.printDebugInfo("TotalSwitchBalance is: " + getTotalSwitchBalance().getText().trim());
        GlobalMethods.printDebugInfo("AvailableSwitchBalance is: " + getAvailableSwitchBalance().getText().trim());
        Assert.assertTrue(currentTotalSwicthUnit, "Select crypto Equivalent fail, TotalSwicthBanlance Unit is not " + coinName);
        Assert.assertTrue(currentAvailableSwicthUnit, "Select crypto Equivalent fail, AvailableSwicthBanlance Unit is not " + coinName);
        for (String crypto : cryptoList) {
            boolean unit = getCryptoCellValue(crypto, 3).contains(coinName);
            Assert.assertTrue(unit, crypto + "row Equivalent unit is not " + coinName);
            GlobalMethods.printDebugInfo(crypto + " row Equivalent unit is " + coinName);
        }
    }
    public void selectCryptoEqualValueUAT(String coinName) {
        LogUtils.info("=======Current coinName is: " + coinName + "=======");
        clickElement(getSelectCrypto());
        clickElement(getMoreCryptoUAT());
        clickElement(selectMoreCoinTypeUAT(coinName));
        waitLoading();
        boolean currentTotalSwicthUnit = getTotalSwitchBalanceUAT().getText().trim().contains(coinName);
        boolean currentAvailableSwicthUnit = getAvailableSwitchBalanceUAT().getText().trim().contains(coinName);
        LogUtils.info("TotalSwitchBalance is: " + getTotalSwitchBalanceUAT().getText().trim());
        LogUtils.info("AvailableSwitchBalance is: " + getAvailableSwitchBalanceUAT().getText().trim());
        Assert.assertTrue(currentTotalSwicthUnit, "Select crypto Equivalent fail, TotalSwicthBanlance Unit is not " + coinName);
        Assert.assertTrue(currentAvailableSwicthUnit, "Select crypto Equivalent fail, AvailableSwicthBanlance Unit is not " + coinName);
        for (String crypto : cryptoList) {
            boolean unit = getCryptoCellValueUAT(crypto, 2).contains(coinName);
            Assert.assertTrue(unit, crypto + "row Equivalent unit is not " + coinName);
            LogUtils.info(crypto + " row Equivalent unit is " + coinName);
        }
    }
    // 进入入金界面
    public void wallectIntoDeposit() {
        clickElement(getBalanceBtnDeposit());
        waitLoading();
    }
    // 入金界面选择币种与network
    public void wallectDepositOptions(String coinName, String networkName){
        clickElement(getBtnChooseCoin());
        clickElement(getChooseCoinOption(coinName));
        waitLoading();;
        clickElement(getChooseNetwork());
        clickElement(getChooseNetworkOption(networkName));
        waitLoading();
        LogUtils.info("CoinType is: " + getDepositInfoCoinType().getText());
        LogUtils.info("NetworkName is: " + getDepositInfoNote().getText());
        Assert.assertTrue(getQRCode().isDisplayed(), "QRCode not exist");
        Assert.assertTrue(getDepositAddress().isDisplayed(), "DepositAddress not exist");
        Assert.assertTrue(getDepositInfoCoinType().getText().contains(coinName));
        Assert.assertTrue(getDepositInfoNote().getText().contains(networkName));
    }
    // 进入出金页面
    public void wallectIntoWithdraw() {
        clickElement(getBalanceBtnWithdraw());
        waitLoading();
    }

    // 出金页面-创建出金表单
    public void wallectWithdrawCreatOrder(String coinName, String Address, String chainName, String amount) {
        // 选择币种
        clickElement(getBtnChooseCoinWithdraw());
        clickElement(getChooseCoinOptionWithdraw(coinName));
        // 输入 Address
        setInputValue(getWithdrawAddressInput(), Address);
        // 选择 chainName
        clickElement(getWithdrawChooseChainBtn());
        clickElement(getWithdrawChooseChainOption(chainName));
        // 输入 amount
        setInputValue(getWithdrawInputAmount(), amount);
        LogUtils.info("Withdraw From is: " + getWithdrawInfoFrom().getText());
        LogUtils.info("Withdraw ReceiveAmount is: " + getWithdrawInfoReceiveAmount().getText());
        Assert.assertTrue(getWithdrawInfoFrom().getText().contains(coinName), "Withdraw From is not correct");
        Assert.assertTrue(getWithdrawInfoReceiveAmount().getText().contains(amount), "Receive Amount is not correct");
        clickElement(getWithdrawBtnSubmit());
        waitLoading();
    }

    // 出金页面-确认出金表单
    public void checkWithdrawReviewFrom(String coinName, String Address, String chainName, String amount) {
        StringBuilder resultInfo = new StringBuilder();

        // 检查Withdraw From字段
        String reviewFromText = getElementTextSafely(this::getWithdrawReviewFrom);
        validateElementAndValue(resultInfo, "Withdraw From", reviewFromText, coinName);

        // 检查Withdraw Address字段
        String reviewAddressText = getElementTextSafely(this::getWithdrawReviewAddress);
        validateElementAndValue(resultInfo, "Withdraw Address", reviewAddressText, Address);

        // 检查Network字段
        String reviewNetworkText = getElementTextSafely(this::getWithdrawReviewNetwork);
        validateElementAndValue(resultInfo, "Network", reviewNetworkText, chainName);

        // 检查Withdraw Amount字段
        String reviewAmountText = getElementTextSafely(this::getWithdrawReviewAmount);
        // 如果是类似"30 USDT"这种格式，转换为"30.0000 USDT"
        if (!reviewAmountText.equals("[Element Not Found]") && !reviewAmountText.contains(".")) {
            reviewAmountText = reviewAmountText.replaceFirst("(\\d+)(\\s+.+)", "$1.0000$2");
        }
        validateElementAndValue(resultInfo, "Withdraw Amount", reviewAmountText, amount);

        // 最终断言
        LogUtils.info("Withdraw review form: " + resultInfo);
        if (resultInfo.length() > 0) {
            Assert.fail("Withdraw review form validation failed: " + resultInfo);
        }
        clickElement(getWithdrawReviewBtnConfirm());
    }
    //钱包资金页面
    public void walletFundingPage(){
        clickElement(getFundingPage());
        waitLoading();
    }

    /**
     * 子方法，验证元素存在性和值是否匹配
     *
     * @param resultInfo    结果信息收集器
     * @param fieldName     显示的字段名称
     * @param actualValue   实际值
     * @param expectedValue 期望值
     */
    private void validateElementAndValue(StringBuilder resultInfo, String fieldName, String actualValue, String expectedValue) {
        if (actualValue.equals("[Element Not Found]")) {
            resultInfo.append(fieldName).append(" element not found; ");
        } else {
            if (!actualValue.equals(expectedValue)) {
                resultInfo.append(fieldName)
                        .append(" is incorrect. Expected: ")
                        .append(expectedValue)
                        .append(", but found: ")
                        .append(actualValue)
                        .append("; ");
            }
        }
    }

    /**
     * 子方法，安全地获取元素文本内容，如果元素不存在则返回"[Element Not Found]"
     *
     * @param elementSupplier 元素提供者函数
     * @return 元素文本或"[Element Not Found]"
     */
    private String getElementTextSafely(java.util.function.Supplier<WebElement> elementSupplier) {
        try {
            WebElement element = elementSupplier.get();
            if (element != null) {
                return element.getText().trim();
            }
        } catch (Exception e) {
            // 忽略异常，返回默认值
        }
        return "[Element Not Found]";
    }

    public void emailTOPTCheck() throws Exception {
        EmailTOTP emailTOTP = new EmailTOTP("otpauth://totp/Vantage%3Aauto0509122803271%40nqmo.com%3ArhECVW?secret=RBFGNVSMDXQESKQYMJDI7W25EGLRYQDV&issuer=Vantage");
        // 生成TOTP验证码
        String totpCode = emailTOTP.generateTOTP();
        LogUtils.info("code is: " + totpCode);
    }

    public void checkVWalletOverview(){
        WebElement overviewTitle = assertElementExists(By.xpath("//span[@class='tab-title-wrapper' and text()='Overview']"),"VWallet Overview Title");
        clickElement(overviewTitle);
        waitLoading();
        String walletTotalValue = getVWalletOverviewTotalValue().getText().trim();
//        String walletTotalCurrency = getVWalletOverviewTotalCurrency().getText().trim();
        Assert.assertFalse(walletTotalValue.contains("0.00"), "Wallet Total Value is not correct");
        Set<String> walletButtonSet = listVWalletOverviewButton().stream()
                .map(WebElement::getText)
                .collect(Collectors.toSet());
        //默认存在该四个按钮
        Set<String> expectedButtons = Set.of("Deposit", "Withdraw", "Transfer", "Convert");
        LogUtils.info("Wallet Buttons: " + walletButtonSet);
        Assert.assertTrue(walletButtonSet.containsAll(expectedButtons),
                "Missing wallet buttons. Expected: " + expectedButtons + ", Actual: " + walletButtonSet);
        //校验币种
        clickElement(getAssetsButton("By Coin"));
        waitLoading();
        List<WebElement> rows = getAllRows();
        List<String> coinList = new ArrayList<>();
        //rows下前四个为币种
        for (int i = 0; i < 4; i++){
            String coin = rows.get(i).findElement(By.xpath(".//div[@class='cell']")).getText().trim();
            coinList.add(coin);
        }
        LogUtils.info("Coin List: " + coinList);
        Assert.assertTrue(new HashSet<>(cryptoList).containsAll(coinList), "Crypto List is not correct");
        //校验账户
        clickElement(getAssetsButton("By Account"));
        waitLoading();
        List<String> accountDefaultList = List.of("Funding","Earn");
        Map<String, String> accountInfo = new HashMap<>();
        for (int i = 4; i<accountDefaultList.size()+4; i++){
            String account = rows.get(i).findElement(By.xpath(".//div[@class='account-item']")).getText().trim();
            String equivalent = rows.get(i).findElement(By.xpath(".//div[@class='table-currency-item']/span[1]")).getText().trim();
            accountInfo.put(account, equivalent);
            Assert.assertEquals(account,accountDefaultList.get(i-4), "Account List is not correct"+account);
        }
        String totalValue = assertElementExists(By.xpath("//div[@class='switch_balance']/span"),"switch balance").getText().trim();
        verifyAmountSum(accountInfo.get("Funding"), accountInfo.get("Earn"), totalValue);
    }
    public void checkVWalletFunding(){
        WebElement fundingTitle = assertElementExists(By.xpath("//span[@class='tab-title-wrapper' and text()='Funding']"),"wallet funding title");
        clickElement(fundingTitle);
        waitLoading();
        String walletTotalValue = getVWalletFundingTotalValue().getText().trim();
        Assert.assertFalse(walletTotalValue.contains("0.00"), "Wallet Total Value is not correct");
        Set<String> walletButtonSet = listFundingButton().stream()
                .map(WebElement::getText)
                .collect(Collectors.toSet());
        //默认存在该四个按钮
        Set<String> expectedButtons = Set.of("Deposit", "Withdraw", "Transfer", "Convert");
        LogUtils.info("Wallet Buttons: " + walletButtonSet);
        Assert.assertTrue(walletButtonSet.containsAll(expectedButtons),
                "Missing wallet buttons. Expected: " + expectedButtons + ", Actual: " + walletButtonSet);
        List<WebElement> rows = getAllRows();
        List<String> coinList = new ArrayList<>();
        //rows下前四个为币种
        for (WebElement row : rows) {
            String coin = row.findElement(By.xpath(".//div[@class='cell']")).getText().trim();
            coinList.add(coin);
           Set<String> actionButtons = row.findElements(By.xpath("./td[5]//span")).stream()
                   .map(element -> element.getAttribute("textContent").trim())
                   .collect(Collectors.toSet());
           Assert.assertTrue(actionButtons.containsAll(expectedButtons),
                   coin+" Missing wallet buttons. Expected: " + expectedButtons + ", Actual: " + actionButtons);
        }
        LogUtils.info("Coin List: " + coinList);
        Assert.assertTrue(new HashSet<>(cryptoList).containsAll(coinList), "Crypto List is not correct");


    }
    public void checkVWalletEarn(){
        WebElement earnTitle = assertElementExists(By.xpath("//span[@class='tab-title-wrapper' and text()='Earn']"),"wallet earn title");
        clickElement(earnTitle);
        waitLoading();
        String walletTotalValue = getVWalletEarnTotalValue().getText().trim();
        Assert.assertFalse(walletTotalValue.contains("0.00"), "Wallet Total Value is not correct");
        Set<String> walletButtonSet = listEarnButton().stream()
                .map(WebElement::getText)
                .collect(Collectors.toSet());

        Set<String> expectedButtons = Set.of("Earn", "Analysis");
        LogUtils.info("Wallet Buttons: " + walletButtonSet);
        Assert.assertTrue(walletButtonSet.containsAll(expectedButtons),
                "Missing wallet buttons. Expected: " + expectedButtons + ", Actual: " + walletButtonSet);
        List<WebElement> rows = getAllRows();
        List<String> coinList = new ArrayList<>();
        Set<String> earnActionButtons = Set.of("Subscribe","Redeem","Details");
        //rows下前四个为币种
        for (WebElement row : rows) {
            String coin = row.findElement(By.xpath(".//div[@class='cell']")).getText().trim();
            coinList.add(coin);
            Set<String> actionButtons = row.findElements(By.xpath("./td[5]//span")).stream()
                    .map(element -> element.getAttribute("textContent").trim())
                    .collect(Collectors.toSet());
            LogUtils.info("Earn Action Buttons: " + actionButtons);
            Assert.assertTrue(actionButtons.containsAll(earnActionButtons),
                    coin+" Missing wallet buttons. Expected: " + earnActionButtons + ", Actual: " + actionButtons);
        }
        LogUtils.info("Coin List: " + coinList);
        Assert.assertTrue(new HashSet<>(cryptoList).containsAll(coinList), "Crypto List is not correct");
    }

    /**
     * 校验钱包总金额是否与账户和理财金额相同
     * @param amount1Text
     * @param amount2Text
     * @param totalText
     */
    public void verifyAmountSum(String amount1Text, String amount2Text, String totalText) {
        double amount1 = parseAmount(amount1Text);
        double amount2 = parseAmount(amount2Text);
        double expectedTotal = parseAmount(totalText);
        double actualTotal = amount1 + amount2;

        Assert.assertEquals(actualTotal, expectedTotal, 0.01,
                String.format("Amounts do not sum correctly. %s (%.2f) + %s (%.2f) = %.2f, but expected %s (%.2f)",
                        amount1Text, amount1, amount2Text, amount2, actualTotal, totalText, expectedTotal));
    }

    private double parseAmount(String amountText) {
        return Double.parseDouble(amountText.replaceAll("[^\\d.,]", "").replace(",", ""));
    }


}
