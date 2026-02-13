package newcrm.pages.clientpages.wallet;

import com.google.common.collect.Sets;
import newcrm.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import utils.LogUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author wesley
 * @Description
 **/
public class WalletCardHomePage extends Page {
    public WalletCardHomePage(WebDriver driver) {
        super(driver);
    }

    /**
     * vcard开户
     *
     * @return
     */
    protected WebElement walletCardApplication() {
        return assertElementExists(By.xpath("//button[@class='el-button ht-button card-apply-button el-button--default']/span"), "Wallet card application");
    }

    /**
     * vcard开户勾选框
     *
     * @return
     */
    protected WebElement walletCardApplicationCheckbox() {
        return findClickableElemntBy(By.xpath("//span[@class='el-checkbox__inner']"));
    }

    protected WebElement walletCardVerifyNow() {
        return findClickableElemntBy(By.xpath("//div[@class='btn-box']//span[normalize-space()='Verify Now']"));
    }

    /**
     * 查看vcard余额
     *
     * @return WebElement
     */
    protected WebElement walletSpendingPowerEyes() {
        return assertElementExists(By.xpath("//div[@class='available-balance__spending-power']//*[name()='svg']"), "Spending Power eye");

    }

    /**
     * Vcard最大限额
     *
     * @return WebElement
     */
    protected WebElement walletPerMaxPayment() {
        return assertElementExists(By.xpath("//div[@class='per-max-payment__amount']"), "Per MAX Payment");
    }

    /**
     * Available balance的入金按钮
     *
     * @return
     */
    protected WebElement walletCardDeposit() {
        return assertElementExists(By.xpath("//button[@class='el-button ht-button available-balance__deposit el-button--primary el-button--medium']//span[normalize-space()='Deposit']"), "Wallet Card Deposit");
    }

    /**
     * 支付方式
     *
     * @return
     */
    protected WebElement walletCardPayingWith() {
        return assertElementExists(By.xpath("//span[normalize-space()='Paying With']"), "Paying With");
    }

    /**
     * 进入卡管理
     *
     * @return
     */
    protected WebElement walletCardManageCard() {
        return assertElementExists(By.xpath("//span[@class='wallet-card-home__manage-card']"), "Manage Card");
    }

    /**
     * 进入返现页面
     *
     * @return
     */
    protected WebElement walletCardRebates() {
        return assertElementExists(By.xpath("//div[@class='el-card__body']/div[@class='rewards__header']/span[normalize-space()='View']"), "Rebates view");
    }

    /**
     * 限额弹窗
     *
     * @return
     */
    protected WebElement walletCardCheckLimit() {
        return assertElementExists(By.xpath("//span[normalize-space()='Check Limits']"), "Check Limit");
    }

    /**
     * 更多历史记录
     *
     * @return
     */
    protected WebElement walletCardViewMore() {
        return assertElementExists(By.xpath("//span[normalize-space()='View More']"), "View more");
    }

    /**
     * 显示卡详情
     * @return
     */
    protected WebElement walletCardViewDetails(){
        return assertElementExists(By.xpath("//div[normalize-space()='View Details']/../div[@class='mask-switch']"), "View Details");
    }

    /**
     * 验证码输入框
     * @return
     */
    protected List<WebElement> walletCardDetailsPhoneOTP(){
        return assertElementsExists(By.xpath("//div[@aria-describedby='otp-instructions']//input[@class='otp_input']"),"Wallet Card Details Phone Verification");
    }

    protected WebElement walletCardInformationIframe(){
        // 等待iframe元素出现，可能需要等待加载完成
        try {
            // 先尝试查找包含 'card-info__iframe' 的iframe元素
            return assertVisibleElementExists(By.xpath("//iframe[contains(@class, 'card-info__iframe')]"), "Wallet Card Information");
        } catch (Exception e) {
            // 如果没找到，等待一段时间再试
            waitLoading();
            return assertElementExists(By.xpath("//iframe[contains(@class, 'card-info__iframe')]"), "Wallet Card Information");
        }
    }

    protected WebElement walletCardName(){
        return assertElementExists(By.xpath("//span[text()='Card Name']/../span[@class='card-info__value']"),"Card Name");
    }
    protected WebElement walletCardNumber(){
        return assertElementExists(By.xpath("//span[text()='Card Number']/../span[@class='card-info__value card-number']"),"Card Number");
    }
    protected WebElement walletCardExpiryDate(){
        return assertElementExists(By.xpath("//span[text()='Expiry Date']/../span[@class='card-info__value']"),"Expiry Date");
    }
    protected WebElement walletCardCVV(){
        return assertElementExists(By.xpath("//span[text()='CVV']/../span[@class='card-info__value']"),"CVV");
    }

    protected WebElement walletCardManage(){
        return assertElementExists(By.xpath("//span[@class='wallet-card-home__manage-card']"),"Manage Card");
    }
    protected WebElement walletCardFreeze(String to){
        return assertElementExists(By.xpath("//span[normalize-space()='"+to+"']"),"Freeze");
    }
    protected List<WebElement> walletCardFreezeApplyOptions(){
        return assertElementsExists(By.xpath("//section[@class='card-freeze__apply-options']/div"),"Wallet Card Freeze Apply Options");
    }
    protected WebElement cardFreezeConfirm(){
        return assertElementExists(By.xpath("//button[@class='el-button ht-button card-freeze__apply__button el-button--primary']/span[normalize-space()='Confirm']"),"Confirm");
    }


    protected WebElement cardRebates(){
        return assertElementExists(By.xpath("//span[@class='rewards__entry']"),"Rebates Views");
    }
    protected WebElement cardRebatesAutoCashback(){
        return assertElementExists(By.xpath("//div[@class='card-rewards__auto-cashback']//span[@class='el-switch__core']"),"Auto Cashback");
    }
    protected WebElement cardRebatesCheck(){
        return assertElementExists(By.xpath("//span[normalize-space()='I Acknowledge']"),"I Acknowledge");
    }
    protected WebElement cardRebatesClaim(){
        return assertElementExists(By.xpath("//span[normalize-space()='Claim']"),"Claim");
    }
    protected WebElement cardAmountAvailable(){
        return assertElementExists(By.xpath("//span[@class='redeem__section-value-label']/span"),"card Claim Amount Available");
    }

    /**
     * 卡消费历史记录View More
     * @return webElement
     */
    protected WebElement cardRecentTransactions(){
        return assertElementExists(By.xpath("//span[normalize-space()='View More']"),"Recent Transactions View More");
    }
    protected WebElement cardAuthorizationsPage(){
        return assertElementExists(By.xpath("//div[text()='Authorizations']"),"Authorizations");
    }
    protected WebElement cardSwitchCard(String index){
        return assertElementExists(By.xpath("(//input[@placeholder='All Card']/../span)["+index+"]"), "switch ALL card");

    }
    protected WebElement cardSwitchStatus(){
        return assertElementExists(By.xpath("//div[@id='pane-1']//input[@placeholder='All Status']/../span"), "switch ALL status");
    }
    protected WebElement cardFilters(String index){
        return assertElementExists(By.xpath("//div[@id='pane-"+index+"']//div[@class='filters_wrap'][normalize-space()='Filters']//*[name()='svg']"),"card Filters");
    }
    protected WebElement cardFiltersExpenditureType(){
        return assertElementExists(By.xpath("//span[normalize-space()='Expenditure']"),"card Expenditure");
    }
    protected WebElement cardFiltersTransactionTypes(String transactionType){
        return assertElementExists(By.xpath("//span[normalize-space()='"+transactionType+"']"),"card Transaction Types "+transactionType);
    }
    protected WebElement cardFiltersCoinsChoice(){
        return assertElementExists(By.xpath("//div[@class='filter_item']//div[@class='el-input el-input--suffix']//span[@class='el-input__suffix']"),"card Coins");
    }

    protected WebElement cardFiltersCoinsUSDT(){
        return assertElementExists(By.xpath("//div[@class='dropdown_currency_wrap']//span[contains(text(),'USDT')]"),"card Coins USDT");
    }
    protected WebElement cardFiltersConfirm(){
        return assertElementExists(By.xpath("//button[@class='el-button ht-button el-button--button el-button--large']//span[contains(text(),'Confirm')]"),"card Filters Confirm");
    }
    protected WebElement backToCard(){
        return assertElementExists(By.xpath("//div[@class='to-back-page-bar']//*[name()='svg']"),"Back To Card Page");
    }
    protected WebElement closeTransactionDetails(){
        return assertElementExists(By.xpath("//div[@class='ht-dialog__close']//*[name()='svg']"),"Close Transaction Details");
    }
    protected List<WebElement> cardTransactionDetailsInfoRow(){
        return assertElementsExists(By.xpath("//div[@class='ht-dialog__content-overflow-container']/div[@class='content']//div[@class='info_row']"),"card Transaction Details Info Row");
    }


    /**
     * 卡消费订单详情列表
     * @return
     */
    protected List<WebElement> transactionTables(){
        return assertElementsExists(By.xpath("//div[@class='el-tab-pane' and not(contains(@style, 'display: none'))]//div[@class='el-table__body-wrapper is-scrolling-none']/table[@class='el-table__body']//tr"), "card recent transactions table");
    }
    protected WebElement cardManagementStatus(String to){
        if (to.equals("Freeze")) {
            return assertElementExists(By.xpath("//span[@class='wallet-card-manage__card-status frozen']"), "virtual card status frozen");
        }
        else {
            return assertElementExists(By.xpath("//span[@class='wallet-card-manage__card-status active']"), "virtual card status active");
        }
        }
    protected WebElement cardManagementTips(){
        return assertElementExists(By.xpath("//span[normalize-space()='Your card unfreezing application is success.']"),"card management tips");
    }
    /**
     * 点击spending power eyes按钮显示可用余额
     */
    public void checkWalletCardHomeAvailable() {
        // 点击spending power eyes，显示可以用余额
        clickSvgElement(walletSpendingPowerEyes());
        WebElement availableBalance = assertElementExists(By.xpath("//span[@class='available-balance__amount']"), "Available Balance");
        String availableBalanceText = availableBalance.getText();
        Assert.assertNotNull(availableBalanceText, "Available balance text should not be null");
        Assert.assertFalse(availableBalanceText.trim().isEmpty(), "Available balance text should not be empty");
        String cleanBalanceText = availableBalanceText.trim().replaceAll("[^0-9.]", "");
        BigDecimal availableBalanceNum = new BigDecimal(cleanBalanceText);
        Assert.assertTrue(availableBalanceNum.compareTo(BigDecimal.ZERO) >= 0,
                "Available balance should be greater than or equal to zero: " + availableBalanceNum);
        LogUtils.info("check Wallet Card Home Available success ");
    }

    /**
     * 单笔最高限额,不能超过钱包可用余额
     */
    public void checkPreMAXTransactions() {
        String preMAXTransactions = walletPerMaxPayment().getText();
        int preMAXTransactionsNum = Integer.parseInt(preMAXTransactions.replaceAll("[^0-9]", ""));
        Assert.assertTrue(preMAXTransactionsNum > 0, "Pre MAX Transactions should be greater than zero: " + preMAXTransactionsNum);
        LogUtils.info("Pre MAX Transactions check success,pre max transactions: " + preMAXTransactionsNum);
    }

    /**
     * 校验入金默认选择币种
     */
    public void checkWalletCardDeposit() {
        clickElement(walletCardDeposit());
        waitLoading();
        WebElement e = assertElementExists(By.xpath("//div[@class='el-input el-input--prefix el-input--suffix']/input"), "Deposit Choose coin");
        String chooseCoin = e.getAttribute("value");
        //默认勾选的币种是USDT
        Assert.assertEquals(chooseCoin, "USDT", "Choose coin should be USDT: " + chooseCoin);
        WebElement back = assertElementExists(By.xpath("//div[@class='to-back-page-bar']/div"), "Deposit Back");
        clickElement( back);
        LogUtils.info("Deposit back success");
    }

    /**
     * 查看卡支付顺序以及支付限额
     *
     * @param availableBalanceList 从钱包首页获取的可用余额
     */
    public void checkWalletCardPayingWith(Map<String,String> availableBalanceList) {
        clickElement(walletCardPayingWith());
        waitLoading();
        List<WebElement> eList = assertElementsExists(By.xpath("//section[@class='payment-list__list']//span[@class='paying-method__label']"), "Paying With");
        //默认的币种排序
        List<String> payingWithDefaultList = List.of("USDT", "USDC", "ETH", "BTC");
        //查看卡支付顺序
        for (WebElement e : eList) {
            String payingWith = e.getText();
            String payingWithDefault = payingWithDefaultList.get(eList.indexOf(e));
            Assert.assertEquals(payingWith, payingWithDefault,
                    String.format("Paying With should be %s, but %s: ",
                            payingWithDefault,
                            payingWith));
        }
        //查看卡支付限额，不能超过wallet的余额
        List<WebElement> payingWithAvailableBalanceList = assertElementsExists(By.xpath("//section[@class='payment-list__list']//span[@class='paying-method__amount']"), "Paying with balance");
        for (WebElement e : payingWithAvailableBalanceList) {
            String payingWithAvailableBalance = e.getText().trim().replaceAll("[^0-9.]","");
            String coinName = e.findElement(By.xpath("./../../span[@class='paying-method__label']")).getText().trim();
            String availableBalance = availableBalanceList.get(coinName);
            Assert.assertEquals(payingWithAvailableBalance, availableBalance,
                    String.format("%s Paying With Available Balance should be %s, but %s: ",
                            coinName,availableBalance, payingWithAvailableBalance));
        }
        LogUtils.info("Verify the payment order and payment limit successfully");

        clickSvgElement(assertElementExists(By.xpath("//div[@class='el-dialog__wrapper ht-dialog paying-with-modal no-footer overflow-limit']//div[@class='ht-dialog__close']//*[name()='svg']"),"spending limits close"));
    }

    /**
     * 点击card manage进入卡管理页面
     */
    public void checkWalletCardManageCard() {
        clickElement(walletCardManageCard());
        waitLoading();
        WebElement e = assertElementExists(By.xpath("//div[@class='menu-title']/span"), "Card Management");
        String manageCard = e.getText();
        Assert.assertEquals(manageCard, "Card Management", "Manage Card should be Card Management,but: " + manageCard);
        LogUtils.info("Card Management check success");
    }

    /**
     * 从卡管理页面返回卡首页
     */
    public void cardManagementBackToCard() {
        WebElement backToCard = assertElementExists(By.xpath("//div[text()='Back to the previous page']"), "Back to Card");
        clickElement(backToCard);
        waitLoading();
        LogUtils.info("Back to Card success");
    }

    /**
     * 校验进入rebates页面
     */
    public void checkWalletRebates() {
        clickElement(walletCardRebates());
        waitLoading();
        WebElement rebates = assertElementExists(By.xpath("//span[normalize-space()='Rebates']"), "Rebates");
        WebElement recentRebates = assertElementExists(By.xpath("//span[normalize-space()='Recent Rebates']"), "Recent Rebates");
        clickElement(assertElementExists(By.xpath("//div[@class='go-back-text']"),"close rebates"));

        LogUtils.info("Rebates check success");
    }

    /**
     * 校验历史交易记录页面
     */
    public void checkWalletCardRecentTransactions() {
        clickElement(walletCardViewMore());
        waitLoading();

    }

    /**
     * 限额校验
     */
    public void checkWalletCardLimits() {
        List<String> limitTitles = List.of("Today", "This Month", "This Year");
        //默认限额 不同账户默认限额不同，此规则取消
        List<String> limitBalances = List.of("200 USDT", "20000 USDT", "200000 USDT");
        clickElement(walletCardCheckLimit());
        waitLoading();
        List<WebElement> limits = assertElementsExists(By.xpath("//div[@class='limit-item']"), "limits");
        for (WebElement e : limits) {
            String title = e.findElement(By.xpath("./div[@class='limit-item__title']/span")).getText().trim();
            //限额
            List<WebElement> values = e.findElements(By.xpath("./div[@class='limit-item__value']/span"));

            String used = values.get(0).getText().trim();
            String limit = values.get(1).getText().trim();
            String defaultTitle = limitTitles.get(limits.indexOf(e));
            String defaultLimit = limitBalances.get(limits.indexOf(e));
            Assert.assertEquals(title, defaultTitle,
                    String.format("Limit Title should be %s, but %s: ",
                            defaultTitle,
                            title));
            Assert.assertFalse(limit.isBlank());
        }
        LogUtils.info("Verify the limits successfully");
        clickSvgElement(assertElementExists(By.xpath("//div[@class='el-dialog__wrapper ht-dialog wallet-card-limits no-footer overflow-limit']//div[@class='ht-dialog__close']//*[name()='svg']"),"close card limits"));
    }

    public String sendPhoneCode(String code) {
        waitLoading();
        List<WebElement> elements = this.walletCardDetailsPhoneOTP();
        char[] chars = code.toCharArray();

        if (!elements.isEmpty()) {
            for (int i = 0; i < elements.size(); i++) {
                WebElement element = elements.get(i);
                String otp = String.valueOf(chars[i]);

                element.click();
                element.sendKeys(otp);
            }
        }

        LogUtils.info("Wallet View Details Page: set Phone OTP to: " + code);

        return code;
    }

    private String getUserName(){
        WebElement avatar = assertElementExists(By.xpath("//span[@class='el-popover__reference-wrapper']"), "avatar");
        clickElement(avatar);
        WebElement basicUserName = assertElementExists(By.xpath("//span[@class='basic-info__username']"), "basic user name");
        return basicUserName.getText().trim();
    }
    private void switchVerification(){
        waitLoading();
        WebElement switchVerification = assertElementExists(By.xpath("//div[@class='switch-box']/div"), "switch verification");
        triggerClickEvent(switchVerification);
        WebElement email = assertElementExists(By.xpath("//div[@visible='visible']//div[@aria-label='Switch verification']//div[@class='el-dialog__body']//div[@class='content']//div//div[@class='title-text'][normalize-space()='Email']"),"Switch verification to Email");
        triggerClickEvent( email);
        waitLoading();
    }
    /**
     * 验证卡详情
     */
    public void checkWalletCardDetails(){
//        String userName = getUserName();
        triggerClickEvent(walletCardViewDetails());
        switchVerification();
        this.sendPhoneCode("999999");
        waitLoading();
        this.driver.switchTo().frame(walletCardInformationIframe());
        Map<String,String> cardUserInfo = new HashMap<>();
        String cardName = walletCardName().getText().trim();
        String CardNum = walletCardNumber().getText().trim();
        String cardCVV = walletCardCVV().getText().trim();
        String cardExpiry = walletCardExpiryDate().getText().trim();
        cardUserInfo.put("cardName",cardName);
        cardUserInfo.put("CardNum",CardNum);
        cardUserInfo.put("cardCVV",cardCVV);
        cardUserInfo.put("cardExpiry",cardExpiry);
        Assert.assertFalse(cardName.contains("*"));
        Assert.assertFalse(CardNum.contains("*"));
        Assert.assertNotNull(cardCVV);
        Assert.assertNotNull(cardExpiry);
        Assert.assertTrue(cardExpiry.matches("^(0[1-9]|1[0-2])/\\d{2}$"), "Card expiry format should be MM/yy, but was: " + cardExpiry);
        LogUtils.info("Verify the card details successfully,card user info:"+cardUserInfo);
    }

    /**
     * 冻结卡
     */
    public void freezeCard(){
        String result = freezeProcess("Freeze");
        Assert.assertEquals(result, "Card frozen successfully", "Toast should be Card frozen successful, but was: " + result);
        LogUtils.info("Verify the card freeze successfully");
        WebElement confirm = findClickableElementByXpath("//div[@class='el-dialog__wrapper ht-dialog card-freeze-result overflow-limit']//button[@type='button']/span");
        triggerClickEvent(confirm);
        String status  = cardManagementStatus("Freeze").getText().trim();
        Assert.assertEquals(status, "Frozen", "Status should be Frozen, but was: " + status);

    }

    /**
     * 解冻卡
     */
    public void unFreezeCard(){
        String result = freezeProcess("UnFreeze");
        Assert.assertEquals(result, "Card unfreeze successfully", "Toast should be Card unfreeze successful, but was: " + result);
        LogUtils.info("Verify the card unfreeze successfully");
        WebElement confirm = findClickableElementByXpath("//div[@class='el-dialog__wrapper ht-dialog card-freeze-result overflow-limit']//button[@type='button']/span");
        triggerClickEvent(confirm);
        String status  = cardManagementStatus("UnFreeze").getText().trim();
        Assert.assertEquals(status, "Active", "Status should be Active, but was: " + status);
        cardManagementTips();
    }
    private String freezeProcess(String to){
        triggerClickEvent(walletCardManageCard());
        LogUtils.info("Wallet Card Manage Card: " + to + " card start");
        waitLoading();
        triggerClickEvent(walletCardFreeze(to));
        triggerClickEvent(cardFreezeConfirm());
        waitLoading();
        switchVerification();
        this.sendPhoneCode("999999");
        waitLoading();
        WebElement resultToast = findVisibleElemntByXpath("//*[@class='freeze-result__title']");
        return resultToast.getText().trim();
    }
    /**
     * 切换自动提取佣金
     * @param to 要切换的值 on/off
     */
    public void changeAutoCashback(String to){
        triggerClickEvent(cardRebates());
        waitLoading();
        triggerClickEvent(cardRebatesAutoCashback());
        waitLoading();
        triggerClickEvent(cardRebatesCheck());
        WebElement toast = findVisibleElemntByXpath("//div[@class='el-message ht-message ht-message--success']/p");
        String toastTest = toast.getText().trim();
        Assert.assertEquals("Turn " + to + " successful", toastTest, "Toast should be Turn "+to+" successful, but was: " + toastTest);
        LogUtils.info("change Auto" + to + " Cashback success");
    }

    /**
     * 从Rebates页面返回V-card首页
     */
    public void rebatesBackToCardPage(){
        WebElement rebates = driver.findElement(By.xpath("//div[@class='menu-title']/span[normalize-space()='Rebates']"));
        if (rebates.isDisplayed()){
            clickSvgElement(backToCard());
            assertElementExists(By.xpath("//div[@class='menu-title']/span"), "go back to V-card fail");
            LogUtils.info("Rebates back V-card success");
        }
    }

    /**
     * 卡消费订单-列表
     */
    public void cardSpendingOrdersList(){
        clickElement(cardRecentTransactions());
        List<String> defaultTableColumns = Arrays.asList("Merchant Name", "Card", "Type", "Status", "Total Pay Amount", "Currency", "Transaction Date & Time", "Action");
        List<WebElement> realTableColumns = assertElementsExists(By.xpath("//div[@id='pane-1']//div[contains(@class,'el-table__header-wrapper')]//th[@colspan='1']/div"),"table columns");
        List<String> realTableColumnsText = realTableColumns.stream()
                .map(element -> element.getAttribute("textContent").trim())
                .toList();
        LogUtils.info("realTableColumnsText is:"+realTableColumnsText);
        Assert.assertEquals(realTableColumnsText, defaultTableColumns, "Table columns should be " + defaultTableColumns + ", but was: " + realTableColumnsText);
        LogUtils.info("check table columns success");
        checkAllCard("1");
        checkAllStatus(List.of("Failed"));
        checkTime();
        checkFilter("1", "Purchase");
    }

    /**
     * 卡授权消费订单-列表
     */
    public void cardAuthorizationsOrdersList(){
        clickElement(cardRecentTransactions());
        //switch authorizations tab
        clickElement(cardAuthorizationsPage());
        List<String> defaultTableColumns = Arrays.asList("Merchant Name", "Card", "Type", "Frozen Amount", "Currency", "Transaction Date & Time", "Action");
        List<WebElement> realTableColumns = assertElementsExists(By.xpath("//div[@id='pane-2']//div[contains(@class,'el-table__header-wrapper')]//th[@colspan='1']/div"),"table columns");
        List<String> realTableColumnsText = realTableColumns.stream()
                .map(element -> element.getAttribute("textContent").trim())
                .toList();
        LogUtils.info("realTableColumnsText is:"+realTableColumnsText);
        Assert.assertEquals(realTableColumnsText, defaultTableColumns, "Table columns should be " + defaultTableColumns + ", but was: " + realTableColumnsText);
        LogUtils.info("check table columns success");
        checkAllCard("2");
        checkTime();
        checkFilter("2","Purchase");
    }
    private void checkAllCard(String index){
        waitLoading();
        clickElement(cardSwitchCard(index));
        //目前仅只有一张虚拟卡，后续有 applePay、googlePay 实体卡。所以目前仅切换All card和虚拟卡
        List<WebElement> cardList = assertElementsExists(By.xpath("//div[@x-placement='bottom-start']//ul/li"), "Card list");
        WebElement allCard = cardList.get(0).findElement(By.xpath(".//span[text()='All Card']"));
        if (allCard.isDisplayed()){
            clickElement(allCard);
            LogUtils.info("check All Card success");
        }

    }
    private void checkAllStatus(List<String> defaultStausList){
        waitLoading();
        clickElement(cardSwitchStatus());
        List<WebElement> statusList = assertElementsExists(By.xpath("//div[@x-placement='bottom-start']//ul/li"), "Status list");
        //List<String> defaultStausList = Arrays.asList("All Status","Processing","Pending","Successful","Failed","Reversed");

        for (int i = 0; i < defaultStausList.size(); i++) {
            WebElement s = statusList.get(i);
            WebElement status = s.findElement(By.xpath("./..//span[text()='" + defaultStausList.get(i) + "']"));
            status.click();
            List<WebElement> transactionData = assertElementsExists(By.xpath("//div[@class='el-table__body-wrapper is-scrolling-none']//tbody/tr"),"Transaction data");
            if (!transactionData.isEmpty()){
                String currentExpectedStatus = defaultStausList.get(i);
                Set<String> transactionDataStatus = transactionData.stream()
                .map(e -> e.findElement(By.xpath("./td[4]//div[@class='status_column']/div")).getText())
                        .collect(Collectors.toSet());

                LogUtils.info("transactionDataStatus is :"+transactionDataStatus);
                //校验当前历史记录列表所有状态与筛选状态保持一致
                Assert.assertEquals(transactionDataStatus.size(), 1, "All transaction statuses should be the same");
                Assert.assertTrue(transactionDataStatus.contains(currentExpectedStatus),
                        "Transaction data status should be " + currentExpectedStatus + ", but was: " + transactionDataStatus);
            }
        }
        LogUtils.info("check All Status success");
    }

    private void checkTime(){

    }

    private void checkFilter(String index,String transactionType){
        waitLoading();
        clickSvgElement(cardFilters(index));
        waitLoading();
        clickElement(cardFiltersExpenditureType());
        clickElement(cardFiltersTransactionTypes(transactionType));
        clickElement(cardFiltersCoinsChoice());
        clickElement(cardFiltersCoinsUSDT());
        clickElement(cardFiltersConfirm());
        LogUtils.info("check Filter success");
    }

    /**
     * 卡订单-详情
     * @param transactionType Purchase/Refund
     */
    public void cardSpendingOrdersDetails(String transactionType){
        clickElement(cardRecentTransactions());
        checkFilter("1", transactionType);
        WebElement detail = assertElementExists(By.xpath("(//div[@id='pane-1']//div[@class='el-table__body-wrapper is-scrolling-none']//tbody/tr/td[8]//span)[1]"),"details");
        clickElement(detail);
        waitLoading();
        //verify transaction details
        List<WebElement> infoRow = cardTransactionDetailsInfoRow();
        Map<String, WebElement> info = new HashMap<>();
        for (int i = 0; i < infoRow.size(); i++){
            String label = infoRow.get(i).findElement(By.xpath(".//div[@class='label_wrap']")).getText().trim();
            WebElement value = infoRow.get(i).findElement(By.xpath(".//div[@class='value_wrap']"));
            info.put(label, value);
        }
        Set<String> requiredKeys = Set.of("Payment Type", "Status", "Merchant", "Payment Time", "Card Ending", "Total Pay Amount", "Local Currency Amount", "Payment ID", "Order Number", "Consumption category", "Location");
        Set<String> actualKeys = info.keySet();
        Assert.assertTrue(actualKeys.containsAll(requiredKeys),
                "Missing required keys: " + Sets.difference(requiredKeys, actualKeys));

        clickSvgElement(closeTransactionDetails());
    }
}
