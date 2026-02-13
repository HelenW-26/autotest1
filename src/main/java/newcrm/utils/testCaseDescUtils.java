package newcrm.utils;

import newcrm.global.GlobalProperties;

import org.apache.commons.lang3.StringEscapeUtils;

public class testCaseDescUtils {
    public static final String CPAPIDEPOSIT_AUDIT_TAG ="审核入金- = 失败/ Pending" ;

    // region [ Global Declaration ]

    public static String sMethodDescNotAvailable = "Not available";
    public static final String depositTag = "入金 - ";
    public static final String walletDepositTag = "钱包充值 - ";
    public static final String withdrawalTag = "出金 - ";
    public static final String walletWithdrawalTag = "钱包提现 - ";
    public static final String withdrawRebateTag = "Withdraw Rebate - ";
    public static final String accountTag = "账户 - ";
    public static final String adminTag = "Admin - ";

    public static final String detailsStyle = "style='margin-left: 15px;'";
    public static final String detailsStart = "<details " + detailsStyle + " >";
    public static final String detailsEnd = "</details>";

    public static final String summaryStart = "<summary>";
    public static final String summaryEnd = "</summary>";
    public static final String summaryTitle = "A. 查看流程";
    public static final String summaryDesc = summaryStart + summaryTitle + summaryEnd;
    public static final String summaryAPITitle = "A. API覆盖范围";
    public static final String summaryAPIDesc = summaryStart + summaryAPITitle + summaryEnd;

    public static final String detailsDivStyle = "style='margin-left: 15px;'";
    public static final String detailsDivStart = "<div " + detailsDivStyle + " >";
    public static final String detailsDivEnd = "</div>";

    public static final String breakLine = "<br/>";

    public static final String CPAPIDEPOSIT_AUDIT_PENDING_OR_FAILED_DESC =
            detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 使用各种渠道进行出金" + breakLine +
                    "3. 后台Pending或者调整出金状态为Failed Payment" + breakLine +
                    "4. 检查账户金额，净值金额不会增加" + breakLine +
                    "5. 检查CP交易记录，资金页面入金状态显示正确" + breakLine +
                    "6. web可正常收到邮件，邮件内容正确" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd;
    // endregion

    // region [ CP Dashboard ]

    // region [ Account Assets ]

    public static final String CPDASHBOARD_ACC_ASSETS =
            "{" + "主页账户资产检查" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 跳转至主页面" + breakLine +
                    "3. 对比总资产展示结果（币种图片、数额、币种）与 CP API 返回结果（POST /web-api/cp/api/home/query-client-total-equity）" + breakLine +
                    "4. 对比真实账户展示结果（交易平台、账号、数额、币种、账户类型）与 CP API 返回结果 （POST /hgw/account-api/bsn/account/getWebHomeAccounts）" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ Trade View ]

    public static final String CPDASHBOARD_TRADE_VIEW =
            "{" + "主页交易走势图检查" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 跳转至主页面" + breakLine +
                    "3. 检查页面每一个交易类型的走势图展示" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion


    // region withdrawal security limit

    public static final String WITHDRAW_SECURITY_LIMIT_UNBIND_2FA =
            "{" + "解绑 2fa 验证出金限制" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 进入 security management 页面" + breakLine +
                    "2. 解绑 2fa" + breakLine +
                    "3. 获取 security limit list" + breakLine +
                    "4. 重新登录 CP" + breakLine +
                    "5. 进入出金页面，进行一笔出金" + breakLine +
                    "6. 选中指定渠道，判断 withdrawal limit message" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";
    public static final String WITHDRAW_SECURITY_LIMIT_MODIFY_2FA =
            "{" + "修改 2fa 验证出金限制" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 进入 security management 页面" + breakLine +
                    "2. 修改 2fa" + breakLine +
                    "3. 获取 security limit list" + breakLine +
                    "4. 重新登录 CP" + breakLine +
                    "5. 进入出金页面，进行一笔出金" + breakLine +
                    "6. 选中指定渠道，判断 withdrawal limit message" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";
    public static final String WITHDRAW_SECURITY_LIMIT_MODIFY_EMAIL =
            "{" + "修改邮箱验证出金限制" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 进入 security management 页面" + breakLine +
                    "2. 修改邮箱" + breakLine +
                    "3. 获取 security limit list" + breakLine +
                    "4. 重新登录 CP" + breakLine +
                    "5. 进入出金页面，进行一笔出金" + breakLine +
                    "6. 选中指定渠道，判断 withdrawal limit message" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";
    public static final String WITHDRAW_SECURITY_LIMIT_MODIFY_PWD =
            "{" + "修改 password 验证出金限制" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 进入 security management 页面" + breakLine +
                    "2. 修改 password" + breakLine +
                    "3. 获取 security limit list" + breakLine +
                    "4. 重新登录 CP" + breakLine +
                    "5. 进入出金页面，进行一笔出金" + breakLine +
                    "6. 选中指定渠道，判断 withdrawal limit message" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";
    public static final String WITHDRAW_SECURITY_LIMIT_RESET_PWD =
            "{" + "reset password 验证出金限制" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 使用 api 重置 password" + breakLine +
                    "2. 获取 security limit list" + breakLine +
                    "3. 重新登录 CP" + breakLine +
                    "4. 进入出金页面，进行一笔出金" + breakLine +
                    "5. 选中指定渠道，判断 withdrawal limit message" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";
    public static final String WITHDRAW_SECURITY_LIMIT_MODIFY_PHONE =
            "{" + "修改手机号验证出金限制" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 进入 security management 页面" + breakLine +
                    "2. 修改手机号" + breakLine +
                    "3. 获取 security limit list" + breakLine +
                    "4. 重新登录 CP" + breakLine +
                    "5. 进入出金页面，进行一笔出金" + breakLine +
                    "6. 选中指定渠道，判断 withdrawal limit message" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";
    public static final String WITHDRAW_CREDIT_DIALOG =
            "{" + "验证出金赠金弹窗" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 进入 security management 页面" + breakLine +
                    "2. 修改手机号" + breakLine +
                    "3. 获取 security limit list" + breakLine +
                    "4. 重新登录 CP" + breakLine +
                    "5. 进入出金页面，进行一笔出金" + breakLine +
                    "6. 选中指定渠道，判断 withdrawal limit message" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";


    // endregion

    // region [ CP Account ]

    // region [ Account Registration ]

    // region [ Golden Flow Registration ]

    public static final String CPACC_REGISTER_GOLDEN_FLOW_MT5 =
            "{" + "MT5 Live 黄金开户（人工身份验证）" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 进入模拟注册网址进行注册" + breakLine +
                    "1.1. AU -> VFSC2 -> Latvia -> " + GlobalProperties.ALPHAURL + breakLine +
                    "2. 提交后跳转至 CP 个人信息验证 - 验证电子邮箱页面" + breakLine +
                    "2.1. 验证电子邮箱页面 - 提交OTP验证码" + breakLine +
                    "2.2. 绑定手机号码页面 - 提交手机号码" + breakLine +
                    "2.3. 验证手机号码页面 - 提交OTP验证码" + breakLine +
                    "2.4. 个人信息页面 - 提交个人信息" + breakLine +
                    "3. 完成个人信息验证后，点击补全账号按钮，进行设置交易账户操作" + breakLine +
                    "4. 提交 MT5 真实交易账户配置（Standard STP 账户类型、USD 货币类型）" + breakLine +
                    "5. 在主页身份验证专区点击立即验证按钮，进行人工身份验证操作" + breakLine +
                    "6. 提交身份验证后，登入 OWS 进行账户审核" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACC_REGISTER_GOLDEN_FLOW_MT5_ALPHA =
            "{" + "MT5 Live 黄金开户（人工身份验证）" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 进入模拟注册网址进行注册" + breakLine +
                    "1.1. AU -> VFSC2 -> Latvia -> " + GlobalProperties.ALPHAURL + breakLine +
                    "2. 提交后跳转至 CP 个人信息验证 - 验证电子邮箱页面" + breakLine +
                    "2.1. 验证电子邮箱页面 - 提交OTP验证码" + breakLine +
                    "2.2. 绑定手机号码页面 - 提交手机号码" + breakLine +
                    "2.3. 验证手机号码页面 - 提交OTP验证码" + breakLine +
                    "2.4. 个人信息页面 - 提交个人信息" + breakLine +
                    "3. 完成个人信息验证后，点击补全账号按钮，进行设置交易账户操作" + breakLine +
                    "4. 提交 MT5 真实交易账户配置（Standard STP 账户类型、USD 货币类型）" + breakLine +
                    "5. 在主页身份验证专区点击立即验证按钮，进行人工身份验证操作" + breakLine +
                    "6. 提交身份验证后，通过 Admin API 进行账户审核 POST /audit/audit_individual_agree" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACC_REGISTER_GOLDEN_FLOW_MT5_WITH_SUMSUB =
            "{" + "MT5 Live 黄金开户（Sumsub 身份验证）" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 进入模拟注册网址进行注册" + breakLine +
                    "1.1. AU -> VFSC2 -> Estonia -> " + GlobalProperties.ALPHAURL + breakLine +
                    "2. 提交后跳转至 CP 个人信息验证 - 验证电子邮箱页面" + breakLine +
                    "2.1. 验证电子邮箱页面 - 提交OTP验证码" + breakLine +
                    "2.2. 绑定手机号码页面 - 提交手机号码" + breakLine +
                    "2.3. 验证手机号码页面 - 提交OTP验证码" + breakLine +
                    "2.4. 个人信息页面 - 提交个人信息" + breakLine +
                    "3. 完成个人信息验证后，点击补全账号按钮，进行设置交易账户操作" + breakLine +
                    "4. 提交 MT5 真实交易账户配置（Standard STP 账户类型、USD 货币类型）" + breakLine +
                    "5. 在主页身份验证专区点击立即验证按钮，进行 Sumsub 身份验证操作" + breakLine +
                    "6. 提交身份验证后，登入 OWS 进行账户审核" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACC_REGISTER_GOLDEN_FLOW_MT5_WITH_SUMSUB_ALPHA =
            "{" + "MT5 Live 黄金开户（Sumsub 身份验证）" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 进入模拟注册网址进行注册" + breakLine +
                    "1.1. AU -> VFSC2 -> Estonia -> " + GlobalProperties.ALPHAURL + breakLine +
                    "2. 提交后跳转至 CP 个人信息验证 - 验证电子邮箱页面" + breakLine +
                    "2.1. 验证电子邮箱页面 - 提交OTP验证码" + breakLine +
                    "2.2. 绑定手机号码页面 - 提交手机号码" + breakLine +
                    "2.3. 验证手机号码页面 - 提交OTP验证码" + breakLine +
                    "2.4. 个人信息页面 - 提交个人信息" + breakLine +
                    "3. 完成个人信息验证后，点击补全账号按钮，进行设置交易账户操作" + breakLine +
                    "4. 提交 MT5 真实交易账户配置（Standard STP 账户类型、USD 货币类型）" + breakLine +
                    "5. 在主页身份验证专区点击立即验证按钮，进行 Sumsub 身份验证操作" + breakLine +
                    "6. 提交身份验证后，通过 Admin API 进行账户审核 POST /audit/audit_individual_agree" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACC_REGISTER_GOLDEN_FLOW_MT5_WITHOUT_CHECK_WITH_SUMSUB =
            "{" + "MT5 Live 黄金开户（Sumsub 身份验证）" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 进入模拟注册网址进行注册" + breakLine +
                    "1.1. AU -> VFSC2 -> Estonia -> " + GlobalProperties.ALPHAURL + breakLine +
                    "2. 提交后跳转至 CP 个人信息验证 - 验证电子邮箱页面" + breakLine +
                    "2.1. 验证电子邮箱页面 - 提交OTP验证码" + breakLine +
                    "2.2. 绑定手机号码页面 - 提交手机号码" + breakLine +
                    "2.3. 验证手机号码页面 - 提交OTP验证码" + breakLine +
                    "2.4. 个人信息页面 - 提交个人信息" + breakLine +
                    "3. 完成个人信息验证后，点击补全账号按钮，进行设置交易账户操作" + breakLine +
                    "4. 提交 MT5 真实交易账户配置（Standard STP 账户类型、USD 货币类型）" + breakLine +
                    "5. 在主页身份验证专区点击立即验证按钮，进行 Sumsub 身份验证操作" + breakLine +
                    "6. 跳过账户审核流程" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACC_REGISTER_GOLDEN_FLOW_MT5_WITHOUT_CHECK_WITH_SUMSUB_PROD =
            "{" + "MT5 Live 黄金开户（Sumsub 身份验证）" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 进入注册网址进行注册" + breakLine +
                    "1.1. AU -> VFSC2 -> 法国 -> " + GlobalProperties.PRODVFXURL + breakLine +
                    "2. 提交后跳转至 CP 个人信息验证 - 验证电子邮箱页面" + breakLine +
                    "2.1. 验证电子邮箱页面 - 提交OTP验证码" + breakLine +
                    "2.2. 绑定手机号码页面 - 提交手机号码" + breakLine +
                    "2.3. 验证手机号码页面 - 提交OTP验证码" + breakLine +
                    "2.4. 个人信息页面 - 提交个人信息" + breakLine +
                    "3. 完成个人信息验证后，点击补全账号按钮，进行设置交易账户操作" + breakLine +
                    "4. 提交 MT5 真实交易账户配置（Standard STP 账户类型、USD 货币类型）" + breakLine +
                    "5. 在主页身份验证专区点击立即验证按钮，进行 Sumsub 身份验证操作" + breakLine +
                    "6. 跳过账户审核流程" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ Golden Flow Auto Registration ]

    public static final String CPACC_AUTO_REGISTER_GOLDEN_FLOW_MT5_WITH_SUMSUB =
            "{" + "MT5 Live 黄金自动开户（Sumsub 身份验证）" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 进入模拟注册网址进行注册" + breakLine +
                    "1.1. AU -> VFSC2 -> Malaysia -> " + GlobalProperties.ALPHAURL + breakLine +
                    "2. 提交后跳转至 CP 个人信息验证 - 验证电子邮箱页面" + breakLine +
                    "2.1. 验证电子邮箱页面 - 提交OTP验证码" + breakLine +
                    "2.2. 绑定手机号码页面 - 提交手机号码" + breakLine +
                    "2.3. 验证手机号码页面 - 提交OTP验证码" + breakLine +
                    "2.4. 个人信息页面 - 提交个人信息" + breakLine +
                    "3. 完成个人信息验证后，点击补全账号按钮，进行设置交易账户操作" + breakLine +
                    "4. 提交 MT5 真实交易账户配置（Standard STP 账户类型、USD 货币类型）" + breakLine +
                    "5. 在主页身份验证专区点击立即验证按钮，进行 Sumsub 身份验证操作" + breakLine +
                    "6. 提交身份验证后，检查账户是否自动审核成功" + breakLine +
                    "7. 通过 Admin API 检查账户信息 POST /account/query_accountList" + breakLine +
                    "7.1. 杠杆默认值（500:1）、交易平台、账户类型、货币类型" + breakLine +
                    "8. 将账号调整至测试组" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACC_AUTO_REGISTER_GOLDEN_FLOW_MT5_WITH_SUMSUB_ALPHA =
            "{" + "MT5 Live 黄金自动开户（Sumsub 身份验证）" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 进入模拟注册网址进行注册" + breakLine +
                    "1.1. AU -> VFSC2 -> Laos -> " + GlobalProperties.ALPHAURL + breakLine +
                    "2. 提交后跳转至 CP 个人信息验证 - 验证电子邮箱页面" + breakLine +
                    "2.1. 验证电子邮箱页面 - 提交OTP验证码" + breakLine +
                    "2.2. 绑定手机号码页面 - 提交手机号码" + breakLine +
                    "2.3. 验证手机号码页面 - 提交OTP验证码" + breakLine +
                    "2.4. 个人信息页面 - 提交个人信息" + breakLine +
                    "3. 完成个人信息验证后，点击补全账号按钮，进行设置交易账户操作" + breakLine +
                    "4. 提交 MT5 真实交易账户配置（Standard STP 账户类型、USD 货币类型）" + breakLine +
                    "5. 在主页身份验证专区点击立即验证按钮，进行 Sumsub 身份验证操作" + breakLine +
                    "6. 提交身份验证后，检查账户是否自动审核成功" + breakLine +
                    "7. 通过 Admin API 检查账户信息 POST /account/query_accountList" + breakLine +
                    "7.1. 杠杆默认值（500:1）、交易平台、账户类型、货币类型" + breakLine +
                    "8. 将账号调整至测试组" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ Golden Flow Demo Registration ]

    public static final String CPACC_REGISTER_DEMO_GOLDEN_FLOW_MT5_WITH_SUMSUB =
            "{" + "MT5 Demo 黄金开户（Sumsub 身份验证）" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 进入官网Demo注册模拟页面" + breakLine +
                    "1.1. AU -> VFSC2 -> Malaysia -> https://www.vantagemarkets.com/open-demo-account-crm" + breakLine +
                    "2. 提交后跳转至 CP Demo 账户启动页面" + breakLine +
                    "3. 检验账户信息（账号、服务器、到期日）" + breakLine +
                    "4. 核对信息后，点击开设真实账户按钮" + breakLine +
                    "5. 在主页个人信息验证专区点击立即验证按钮，进入个人信息验证 - 验证电子邮箱页面" + breakLine +
                    "5.1. 验证电子邮箱页面 - 提交OTP验证码" + breakLine +
                    "5.3. 验证手机号码页面 - 提交OTP验证码" + breakLine +
                    "5.4. 个人信息页面 - 提交个人信息" + breakLine +
                    "6. 完成个人信息验证后，点击补全账号按钮，进行设置交易账户操作" + breakLine +
                    "7. 提交 MT5 真实交易账户配置（Standard STP 账户类型、USD 货币类型）" + breakLine +
                    "8. 在主页身份验证专区点击立即验证按钮，进行 Sumsub 身份验证操作" + breakLine +
                    "9. 提交身份验证后，检查账户是否自动审核成功" + breakLine +
                    "10. 通过 Admin API 检查账户信息 POST /account/query_accountList" + breakLine +
                    "10.1. 杠杆默认值（500:1）、交易平台、账户类型、货币类型" + breakLine +
                    "11. 将账号调整至测试组" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACC_REGISTER_DEMO_GOLDEN_FLOW_MT5_WITH_SUMSUB_ALPHA =
            "{" + "MT5 Demo 黄金开户（Sumsub 身份验证）" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 进入官网Demo注册模拟页面" + breakLine +
                    "1.1. AU -> VFSC2 -> Laos -> https://www.vantagemarkets.com/open-demo-account-crm" + breakLine +
                    "2. 提交后跳转至 CP Demo 账户启动页面" + breakLine +
                    "3. 检验账户信息（账号、服务器、到期日）" + breakLine +
                    "4. 核对信息后，点击开设真实账户按钮" + breakLine +
                    "5. 在主页个人信息验证专区点击立即验证按钮，进入个人信息验证 - 验证电子邮箱页面" + breakLine +
                    "5.1. 验证电子邮箱页面 - 提交OTP验证码" + breakLine +
                    "5.3. 验证手机号码页面 - 提交OTP验证码" + breakLine +
                    "5.4. 个人信息页面 - 提交个人信息" + breakLine +
                    "6. 完成个人信息验证后，点击补全账号按钮，进行设置交易账户操作" + breakLine +
                    "7. 提交 MT5 真实交易账户配置（Standard STP 账户类型、USD 货币类型）" + breakLine +
                    "8. 在主页身份验证专区点击立即验证按钮，进行 Sumsub 身份验证操作" + breakLine +
                    "9. 提交身份验证后，检查账户是否自动审核成功" + breakLine +
                    "10. 通过 Admin API 检查账户信息 POST /account/query_accountList" + breakLine +
                    "10.1. 杠杆默认值（500:1）、交易平台、账户类型、货币类型" + breakLine +
                    "11. 将账号调整至测试组" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ ASIC Registration ]

    public static final String CPACC_REGISTER_ASIC_MT5 =
            "{" + "MT5 Live ASIC 开户（人工身份验证）" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 进入模拟注册网址进行注册" + breakLine +
                    "1.1. AU、STAR、PU -> ASIC -> Australia -> " + GlobalProperties.ALPHAURL + breakLine +
                    "2. 提交后跳转至 CP 个人信息页面进行开户流程" + breakLine +
                    "3. 提交个人信息" + breakLine +
                    "4. 提交居住地址信息" + breakLine +
                    "5. 提交开户问卷" + breakLine +
                    "6. 开户问卷通过后，跳转设置交易账户页面" + breakLine +
                    "7. 提交真实交易账户配置（选择 MT5 交易平台、Standard STP 账户类型、USD 货币类型）" + breakLine +
                    "8. 人工上传身份与地址证明文件" + breakLine +
                    "9. 点击开始答题按钮进行交易问卷" + breakLine +
                    "10. 提交交易问卷" + breakLine +
                    "11. 完成开户流程后，登入 OWS 进行账户审核" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACC_REGISTER_ASIC_MT5_ALPHA =
            "{" + "MT5 Live ASIC 开户（人工身份验证）" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 进入模拟注册网址进行注册" + breakLine +
                    "1.1. AU、STAR、PU -> ASIC -> Australia -> " + GlobalProperties.ALPHAURL + breakLine +
                    "2. 提交后跳转至 CP 个人信息页面进行开户流程" + breakLine +
                    "3. 提交个人信息" + breakLine +
                    "4. 提交居住地址信息" + breakLine +
                    "5. 提交开户问卷" + breakLine +
                    "6. 开户问卷通过后，跳转设置交易账户页面" + breakLine +
                    "7. 提交真实交易账户配置（选择 MT5 交易平台、Standard STP 账户类型、USD 货币类型）" + breakLine +
                    "8. 人工上传身份与地址证明文件" + breakLine +
                    "9. 点击开始答题按钮进行交易问卷" + breakLine +
                    "10. 提交交易问卷" + breakLine +
                    "11. 完成开户流程后，通过 Admin API 进行账户审核 POST /audit/audit_individual_agree" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACC_REGISTER_ASIC_MT5_WITHOUT_CHECK =
            "{" + "MT5 Live ASIC 开户（人工身份验证）" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 进入模拟注册网址进行注册" + breakLine +
                    "1.1. AU、STAR、PU -> ASIC -> 澳大利亚 -> " + GlobalProperties.ALPHAURL + breakLine +
                    "2. 提交后跳转至 CP 个人信息页面进行开户流程" + breakLine +
                    "3. 提交个人信息" + breakLine +
                    "4. 提交居住地址信息" + breakLine +
                    "5. 提交开户问卷" + breakLine +
                    "6. 开户问卷通过后，跳转设置交易账户页面" + breakLine +
                    "7. 提交真实交易账户配置（选择 MT5 交易平台、Standard STP 账户类型、USD 货币类型）" + breakLine +
                    "8. 人工上传身份与地址证明文件" + breakLine +
                    "9. 点击开始答题按钮进行交易问卷" + breakLine +
                    "10. 提交交易问卷" + breakLine +
                    "11. 跳过账户审核流程" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ Standard Registration ]

    public static final String CPACC_REGISTER_MT4 =
            "{" + "MT4 Live 开户" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 进入账户注册页面" + breakLine +
                    "1.1 模拟注册网址 " + GlobalProperties.ALPHAURL + breakLine +
                    "2. 提交注册资料 POST /hgw/user-api/bsn/registration/register" + breakLine +
                    "2.1 PU、VT、STAR - SVG - 意大利" + breakLine +
                    "2.2 VJP - SVG - 日本" + breakLine +
                    "2.3 MO - VFSC - 马来西亚" + breakLine +
                    "2.4 UM - SVG - 法国" + breakLine +
                    "3. 跳转至CP，进行开户认证操作" + breakLine +
                    "4. 提交个人信息" + breakLine +
                    "5. 提交真实账户配置（MT4交易平台、Standard STP账户类型、USD货币类型）" + breakLine +
                    "6. 身份验证（上传身份证明文件）" + breakLine +
                    "6.1 根据身份验证页面（人工或Sumsub）进行验证" + breakLine +
                    "7 完成开户流程后，通过Admin API进行账户审核 POST /audit/audit_individual_agree" + breakLine +
                    "7.1 账户审核默认值（杠杆 100:1、随机挑选test server & account group）" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACC_REGISTER_MT4_WITHOUT_CHECK =
            "{" + "MT4 Live 开户" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 进入账户注册页面" + breakLine +
                    "1.1 模拟注册网址 " + GlobalProperties.ALPHAURL + breakLine +
                    "2. 提交注册资料 POST /hgw/user-api/bsn/registration/register" + breakLine +
                    "2.1 PU、VT、STAR - SVG - 意大利" + breakLine +
                    "2.2 VJP - SVG - 日本" + breakLine +
                    "2.3 MO - VFSC - 马来西亚" + breakLine +
                    "2.4 UM - SVG - 法国" + breakLine +
                    "3. 跳转至CP，进行开户认证操作" + breakLine +
                    "4. 提交个人信息" + breakLine +
                    "5. 提交真实账户配置（MT4交易平台、Standard STP账户类型、USD货币类型）" + breakLine +
                    "6. 身份验证（上传身份证明文件）" + breakLine +
                    "6.1 根据身份验证页面（人工或Sumsub）进行验证" + breakLine +
                    "7. 跳过账户审核流程" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACC_REGISTER_MT5 =
            "{" + "MT5 Live 开户" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 进入账户注册页面" + breakLine +
                    "1.1 模拟注册网址 " + GlobalProperties.ALPHAURL + breakLine +
                    "2. 提交注册资料 POST /hgw/user-api/bsn/registration/register" + breakLine +
                    "2.1 PU、VT、STAR - SVG - 意大利" + breakLine +
                    "2.2 VJP - SVG - 日本" + breakLine +
                    "2.3 MO - VFSC - 马来西亚" + breakLine +
                    "2.4 UM - SVG - 法国" + breakLine +
                    "3. 跳转至CP，进行开户认证操作" + breakLine +
                    "4. 提交个人信息" + breakLine +
                    "5. 提交真实账户配置（MT5交易平台、Standard STP账户类型、USD货币类型）" + breakLine +
                    "6. 身份验证（上传身份证明文件）" + breakLine +
                    "6.1 根据身份验证页面（人工或Sumsub）进行验证" + breakLine +
                    "7 完成开户流程后，通过Admin API进行账户审核 POST /audit/audit_individual_agree" + breakLine +
                    "7.1 账户审核默认值（杠杆 100:1、随机挑选test server & account group）" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACC_REGISTER_MT5_WITHOUT_CHECK =
            "{" + "MT5 Live 开户" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 进入账户注册页面" + breakLine +
                    "1.1 模拟注册网址 " + GlobalProperties.ALPHAURL + breakLine +
                    "2. 提交注册资料 POST /hgw/user-api/bsn/registration/register" + breakLine +
                    "2.1 PU、VT、STAR - SVG - 意大利" + breakLine +
                    "2.2 VJP - SVG - 日本" + breakLine +
                    "2.3 MO - VFSC - 马来西亚" + breakLine +
                    "2.4 UM - SVG - 法国" + breakLine +
                    "3. 跳转至CP，进行开户认证操作" + breakLine +
                    "4. 提交个人信息" + breakLine +
                    "5. 提交真实账户配置（MT5交易平台、Standard STP账户类型、USD货币类型）" + breakLine +
                    "6. 身份验证（上传身份证明文件）" + breakLine +
                    "6.1 根据身份验证页面（人工或Sumsub）进行验证" + breakLine +
                    "7. 跳过账户审核流程" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ Auto Registration ]

    public static final String CPACC_AUTO_REGISTER_MT5 =
            "{" + "MT5 Live 自动开户" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 进入账户注册页面" + breakLine +
                    "1.1 模拟注册网址 " + GlobalProperties.ALPHAURL + breakLine +
                    "2. 提交注册资料 POST /hgw/user-api/bsn/registration/register" + breakLine +
                    "2.1 PU、VT、STAR - SVG - 意大利" + breakLine +
                    "2.2 VJP - SVG - 日本" + breakLine +
                    "2.3 MO - VFSC - 马来西亚" + breakLine +
                    "2.4 UM - SVG - 法国" + breakLine +
                    "3. 跳转至CP，进行开户认证操作" + breakLine +
                    "4. 提交个人信息" + breakLine +
                    "5. 提交真实账户配置（MT5交易平台、Standard STP账户类型、USD货币类型）" + breakLine +
                    "6. 身份验证（上传身份证明文件）" + breakLine +
                    "6.1 根据身份验证页面（人工或Sumsub）进行验证" + breakLine +
                    "7. 完成开户流程后，检查账户审核杠杆默认值，将该账号调整至测试组" + breakLine +
                    "7.1 PU、VT、STAR、MO、UM - 500:1" + breakLine +
                    "7.2 VJP - 1000:1" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ Demo Registration ]

    public static final String CPACC_REGISTER_DEMO_MT5 =
            "{" + "MT5 Demo 开户" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 进入官网Demo账户注册模拟页面" + breakLine +
                    "1.1 VT https://www.vtmarkets.com/bit-demo-test" + breakLine +
                    "1.2 PU https://www.puprime.com/demo-account-test" + breakLine +
                    "1.3 STAR https://www.startrader.com/demo-account-crm" + breakLine +
                    "1.4 VJP https://www.vantagetradings.com/en/open-demo-account-crm" + breakLine +
                    "1.5 MO https://www.monetamarkets.com/open-demo-account-crm" + breakLine +
                    "1.6 UM https://www.ultimamarkets.com/free-demo-account-crm" + breakLine +
                    "2. 提交注册资料" + breakLine +
                    "2.1 PU、VT、STAR - SVG - 意大利" + breakLine +
                    "2.2 VJP - SVG - 日本" + breakLine +
                    "2.3 MO - VFSC - 马来西亚" + breakLine +
                    "2.4 UM - SVG - 法国" + breakLine +
                    "3. 跳转至CP 测试账户启动页面，核对测试账户信息，，点击开设真实账户按钮" + breakLine +
                    "4. 提交个人信息" + breakLine +
                    "5. 提交真实账户配置（MT5交易平台、Standard STP账户类型、USD货币类型）" + breakLine +
                    "6. 身份验证（上传身份证明文件）" + breakLine +
                    "6.1 根据身份验证页面（人工或Sumsub）进行验证" + breakLine +
                    "7 完成开户流程后，通过Admin API进行账户审核 POST /audit/audit_individual_agree" + breakLine +
                    "7.1 账户审核默认值（杠杆 100:1、随机挑选test server & account group）" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ Profile Verification ]

    public static final String CPACC_PROFILE_VERIFICATION =
            "{" + "个人信息验证操作" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 进入账户注册页面" + breakLine +
                    "1.1 模拟注册网址 " + GlobalProperties.ALPHAURL + breakLine +
                    "2. 提交注册资料 POST /hgw/user-api/bsn/registration/register" + breakLine +
                    "2.1 PU、STAR - SVG - 意大利" + breakLine +
                    "2.2 VJP - SVG - 日本" + breakLine +
                    "2.3 MO - VFSC - 马来西亚" + breakLine +
                    "2.4 UM - SVG - 法国" + breakLine +
                    "3. 跳转至CP，关闭个人信息验证弹窗" + breakLine +
                    "4. 点击个人信息菜单，跳转至个人信息页面" + breakLine +
                    "5. 检查验证区块标准内容按钮的状态和显示情况" + breakLine +
                    "5.1 lv 1 个人信息验证 - 按钮可点击" + breakLine +
                    "5.2 lv 2 身份验证、lv 3 居住地址验证 - 按钮置灰" + breakLine +
                    "6. 点击 lv 1 个人信息验证按钮进行验证操作" + breakLine +
                    "7. 检查验证标准内容按钮的状态和显示情况" + breakLine +
                    "7.1 lv 1 个人信息验证 - 按钮隐藏" + breakLine +
                    "7.1 lv 2 身份验证、lv 3 居住地址验证 - 按钮可点击" + breakLine +
                    "8. 点击 lv 2 身份验证按钮进行验证操作" + breakLine +
                    "9. 点击 lv 3 居住地址验证按钮进行验证操作" + breakLine +
                    "10. 跳过账户审核流程" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // endregion

    // region [ Update Account ]

    // region [ Demo Account Info ]

    public static final String CPACC_UPDATE_MT4_DEMO_ACC_INFO =
            "{" + "修改 MT4 Demo 账号信息（余额、杠杆）" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击账户管理菜单，跳转至账户管理页面" + breakLine +
                    "3. 随机点击账号里的编辑余额图标" + breakLine +
                    "4. 随机设置数值" + breakLine +
                    "5. 提交更改" + breakLine +
                    "6. 点击同个账号里的设置图标" + breakLine +
                    "7. 点击变更杠杆" + breakLine +
                    "8. 随机挑选新的杠杆倍值" + breakLine +
                    "9. 提交更改" + breakLine +
                    "10. 检查账号新杠杆倍值与余额展示正确" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACC_UPDATE_MT5_DEMO_ACC_INFO =
            "{" + "修改 MT5 Demo 账号信息（余额、杠杆）" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击账户管理菜单，跳转至账户管理页面" + breakLine +
                    "3. 随机点击账号里的编辑余额图标" + breakLine +
                    "4. 随机设置数值" + breakLine +
                    "5. 提交更改" + breakLine +
                    "6. 点击同个账号里的设置图标" + breakLine +
                    "7. 点击变更杠杆" + breakLine +
                    "8. 随机挑选新的杠杆倍值" + breakLine +
                    "9. 提交更改" + breakLine +
                    "10. 检查账号新杠杆倍值与余额展示正确" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ Account Leverage ]

    public static final String CPACC_UPDATE_LEVERAGE_MT4 =
            "{" + "修改 MT4 Live 账号杠杆" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击账户管理菜单，跳转至账户管理页面" + breakLine +
                    "3. 随机点击账号里的设置图标" + breakLine +
                    "4. 点击变更杠杆" + breakLine +
                    "5. 随机挑选新的杠杆倍值与点击同意条款和条件" + breakLine +
                    "6. 提交更改" + breakLine +
                    "7. 检查账号新杠杆倍值展示正确" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACC_UPDATE_LEVERAGE_MT5 =
            "{" + "修改 MT5 Live 账号杠杆" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击账户管理菜单，跳转至账户管理页面" + breakLine +
                    "3. 随机点击账号里的设置图标" + breakLine +
                    "4. 点击变更杠杆" + breakLine +
                    "5. 选择挑选新的杠杆倍值与点击同意条款和条件" + breakLine +
                    "6. 提交更改" + breakLine +
                    "7. 检查账号新杠杆倍值展示正确" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACC_UPDATE_LEVERAGE_DEMO_ACC_MT4 =
            "{" + "修改 MT4 Demo 账号杠杆" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击账户管理菜单，跳转至账户管理页面" + breakLine +
                    "3. 随机点击账号里的设置图标" + breakLine +
                    "4. 点击变更杠杆" + breakLine +
                    "5. 随机挑选新的杠杆倍值" + breakLine +
                    "6. 提交更改" + breakLine +
                    "7. 检查账号新杠杆倍值展示正确" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACC_UPDATE_LEVERAGE_DEMO_ACC_MT5 =
            "{" + "修改 MT5 Demo 账号杠杆" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击账户管理菜单，跳转至账户管理页面" + breakLine +
                    "3. 随机点击账号里的设置图标" + breakLine +
                    "4. 点击变更杠杆" + breakLine +
                    "5. 随机挑选新的杠杆倍值" + breakLine +
                    "6. 提交更改" + breakLine +
                    "7. 检查账号新杠杆倍值展示正确" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ Account Balance ]

    public static final String CPACC_UPDATE_BALANCE_DEMO_ACC_MT4 =
            "{" + "修改 MT4 Demo 账号余额" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击账户管理菜单，跳转至账户管理页面" + breakLine +
                    "3. 随机点击账号里的编辑余额图标" + breakLine +
                    "4. 随机设置数值" + breakLine +
                    "5. 提交更改" + breakLine +
                    "6. 在账户管理页面检查新余额值" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACC_UPDATE_BALANCE_DEMO_ACC_MT5 =
            "{" + "修改 MT5 Demo 账号余额" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击账户管理菜单，跳转至账户管理页面" + breakLine +
                    "3. 随机点击账号里的编辑余额图标" + breakLine +
                    "4. 随机设置数值" + breakLine +
                    "5. 提交更改" + breakLine +
                    "6. 在账户管理页面检查新余额值" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ Remove Credit ]

    public static final String CPACC_MT4_LIVE_ACC_REMOVE_CREDIT =
            "{" + "移除 MT4 Live 账号赠金" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击账户管理菜单，跳转至账户管理页面" + breakLine +
                    "3. 随机点击账号里的设置图标" + breakLine +
                    "4. 点击删除积分" + breakLine +
                    "5. 提交删除请求" + breakLine +
                    "6. 检查账号赠金归零" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACC_MT5_LIVE_ACC_REMOVE_CREDIT =
            "{" + "移除 MT5 Live 账号赠金" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击账户管理菜单，跳转至账户管理页面" + breakLine +
                    "3. 随机点击账号里的设置图标" + breakLine +
                    "4. 点击删除积分" + breakLine +
                    "5. 提交删除请求" + breakLine +
                    "6. 检查账号赠金归零" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";
    // endregion

    // region [ Account Nickname ]

    public static final String CPACC_UPDATE_MT4_LIVE_ACC_NICKNAME =
            "{" + "修改 MT4 Live 账号昵称" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击账户管理菜单，跳转至账户管理页面" + breakLine +
                    "3. 随机点击账号里的设置图标" + breakLine +
                    "4. 点击设置账户昵称" + breakLine +
                    "5. 提交新昵称" + breakLine +
                    "6. 检查账号新昵称展示正确" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACC_UPDATE_MT5_LIVE_ACC_NICKNAME =
            "{" + "修改 MT5 Live 账号昵称" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击账户管理菜单，跳转至账户管理页面" + breakLine +
                    "3. 随机点击账号里的设置图标" + breakLine +
                    "4. 点击设置账户昵称" + breakLine +
                    "5. 提交新昵称" + breakLine +
                    "6. 检查账号新昵称展示正确" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ Change Password ]

    public static final String CPACC_MT4_LIVE_ACC_CHG_PWD =
            "{" + "修改 MT4 Live 账号密码" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击账户管理菜单，跳转至账户管理页面" + breakLine +
                    "3. 随机点击账号里的设置图标" + breakLine +
                    "4. 点击修改密码" + breakLine +
                    "5. 填写目前密码（123Qwe@@），新密码（123Qwe!!）" + breakLine +
                    "6. 提交更改" + breakLine +
                    "7. 再次修改密码，把密码修改回默认密码（123Qwe@@）" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACC_MT5_LIVE_ACC_CHG_PWD =
            "{" + "修改 MT5 Live 账号密码" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击账户管理菜单，跳转至账户管理页面" + breakLine +
                    "3. 随机点击账号里的设置图标" + breakLine +
                    "4. 点击修改密码" + breakLine +
                    "5. 填写目前密码（123Qwe@@）与新密码（123Qwe!!）" + breakLine +
                    "6. 提交更改" + breakLine +
                    "7. 再次修改密码，把密码修改回默认密码（123Qwe@@）" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // endregion

    // region [ Additional Account ]

    public static final String CPACC_ADDITIONAL_ACC_MT4 =
            "{" + "开通 MT4 Live 同名账号" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击账户管理菜单，跳转至账户管理页面" + breakLine +
                    "3. 点击开户按钮" + breakLine +
                    "4. 选择 MT4 交易平台、Standard STP 账户类型、USD 货币类型与点击同意条款和条件" + breakLine +
                    "5. 提交真实账户配置后，通过 Admin API 拒绝同名账号申请 POST /audit/audit_sameAct_refuse" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACC_ADDITIONAL_ACC_MT5 =
            "{" + "开通 MT5 Live 同名账号" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击账户管理菜单，跳转至账户管理页面" + breakLine +
                    "3. 点击开户按钮" + breakLine +
                    "4. 选择 MT5 交易平台、Standard STP 账户类型、USD 货币类型与点击同意条款和条件" + breakLine +
                    "5. 提交真实账户配置后，通过 Admin API 拒绝同名账号申请 POST /audit/audit_sameAct_refuse" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACC_ADDITIONAL_ACC_MTS =
            "{" + "开通 MTS 同名账号" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击账户管理菜单，跳转至账户管理页面" + breakLine +
                    "3. 点击开户按钮" + breakLine +
                    "4. 选择 MTS 交易平台、Standard STP 账户类型、USD 货币类型与点击同意条款和条件" + breakLine +
                    "5. 提交真实账户配置后，通过 Admin API 拒绝同名账号申请 POST /audit/audit_sameAct_refuse" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ Additional Account Config Check ]

    public static final String CPACC_ADDITIONAL_ACC_MT4_CONFIG_CHECK =
            "{" + "MT4 Live 同名账号账户类型展示检查" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击账户管理菜单，跳转至账户管理页面" + breakLine +
                    "3. 点击开户按钮" + breakLine +
                    "4. 检查 MT4 交易平台选项展示正常" + breakLine +
                    "5. 选择 MT4 交易平台" + breakLine +
                    "6. 检查账户类型是否展示齐全" + breakLine +
                    "7. 点击每个账户类型，检查币种列表是否展示齐全" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACC_ADDITIONAL_ACC_MT5_CONFIG_CHECK =
            "{" + "MT5 Live 同名账号账户类型展示检查" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击账户管理菜单，跳转至账户管理页面" + breakLine +
                    "3. 点击开户按钮" + breakLine +
                    "4. 检查 MT5 交易平台选项展示正常" + breakLine +
                    "5. 选择 MT5 交易平台" + breakLine +
                    "6. 检查账户类型是否展示齐全" + breakLine +
                    "7. 点击每个账户类型，检查币种列表是否展示齐全" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACC_ADDITIONAL_ACC_MTS_CONFIG_CHECK =
            "{" + "MTS Copy Trading 同名账号账户类型展示检查" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击账户管理菜单，跳转至账户管理页面" + breakLine +
                    "3. 点击开户按钮" + breakLine +
                    "4. 检查 Copy Trading 交易平台选项展示正常" + breakLine +
                    "4.1 泰国、法国 - Copy Trading 交易平台选项展示" + breakLine +
                    "4.1 英国 - Copy Trading 交易平台选项隐藏" + breakLine +
                    "5. 选择 Copy Trading 交易平台" + breakLine +
                    "6. 检查账户类型是否展示齐全" + breakLine +
                    "7. 点击每个账户类型，检查币种列表是否展示齐全" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ Live Account Info Check ]

    public static final String CPACC_MT4_LIVE_ACC_INFO_CHECK =
            "{" + "MT4 Live 账号信息检查" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击账户管理菜单，跳转至账户管理页面" + breakLine +
                    "3. 筛选 MT4 交易平台" + breakLine +
                    "4. 对比账号信息展示结果（交易平台、账号、账户类型、server名称、币种、余额、净值、信用额、杠杆）与 CP API 返回结果（POST /hgw/account-api/bsn/account/getWebHomeAccounts）" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";


    public static final String CPACC_MT5_LIVE_ACC_INFO_CHECK =
            "{" + "MT5 Live 账号信息检查" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击账户管理菜单，跳转至账户管理页面" + breakLine +
                    "3. 筛选 MT5 交易平台" + breakLine +
                    "4. 对比账号信息展示结果（交易平台、账号、账户类型、server名称、币种、余额、净值、信用额、杠杆）与 CP API 返回结果（POST /hgw/account-api/bsn/account/getWebHomeAccounts）" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACC_MT4_LIVE_ACC_SWITCH_DISPLAY_MODE_CHECK =
            "{" + "切换 MT4 Live 账号展示模式" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击账户管理菜单，跳转至账户管理页面" + breakLine +
                    "3. 点击网格展示模式" + breakLine +
                    "4. 点击列表展示模式" + breakLine +
                    "5. 检查网格与列表展示模式下账户数量是否一致" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACC_MT5_LIVE_ACC_SWITCH_DISPLAY_MODE_CHECK =
            "{" + "切换 MT5 Live 账号展示模式" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击账户管理菜单，跳转至账户管理页面" + breakLine +
                    "3. 点击网格展示模式" + breakLine +
                    "4. 点击列表展示模式" + breakLine +
                    "5. 检查网格与列表展示模式下账户数量是否一致" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACC_MT5_LIVE_ACC_LEVERAGE_500_CHECK =
            "{" + "MT5 Live 账号 500 杠杆倍数检查" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击账户管理菜单，跳转至账户管理页面" + breakLine +
                    "3. 随机点击账号里的设置图标" + breakLine +
                    "4. 点击变更杠杆" + breakLine +
                    "5. 检查最高杠杆倍数" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACC_MT5_LIVE_ACC_LEVERAGE_1000_CHECK =
            "{" + "MT5 Live 账号 1000 杠杆倍数检查" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击账户管理菜单，跳转至账户管理页面" + breakLine +
                    "3. 随机点击账号里的设置图标" + breakLine +
                    "4. 点击变更杠杆" + breakLine +
                    "5. 检查最高杠杆倍数" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACC_MT5_LIVE_ACC_LEVERAGE_2000_CHECK =
            "{" + "MT5 Live 账号 2000 杠杆倍数检查" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击账户管理菜单，跳转至账户管理页面" + breakLine +
                    "3. 随机点击账号里的设置图标" + breakLine +
                    "4. 点击变更杠杆" + breakLine +
                    "5. 检查最高杠杆倍数" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ Demo Account Info Check ]

    public static final String CPACC_MT4_DEMO_ACC_INFO_CHECK =
            "{" + "MT4 Demo 账号信息检查" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击账户管理菜单，跳转至账户管理页面" + breakLine +
                    "3. 筛选模拟账号，MT4 交易平台" + breakLine +
                    "4. 对比账号信息展示结果（交易平台、账号、账户类型、server名称、币种、净值、杠杆）与 CP API 返回结果（POST /hgw/account-api/bsn/account/getWebHomeAccountDemos）" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACC_MT5_DEMO_ACC_INFO_CHECK =
            "{" + "MT5 Demo 账号信息检查" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击账户管理菜单，跳转至账户管理页面" + breakLine +
                    "3. 筛选模拟账号，MT5 交易平台" + breakLine +
                    "4. 对比账号信息展示结果（交易平台、账号、账户类型、server名称、币种、净值、杠杆）与 CP API 返回结果（POST /hgw/account-api/bsn/account/getWebHomeAccountDemos）" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ User Profile Info Check ]

    public static final String CPACC_USER_PROFILE_CONTACT_INFO_MASKING_CHECK =
            "{" + "账户个人信息脱敏展示检查" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击个人信息菜单，跳转至个人信息页面" + breakLine +
                    "3. 检查邮箱与手机号脱敏展示" + breakLine +
                    "4. 检查安全管理邮箱与手机号脱敏展示" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // endregion

    // region [ CP Fund Transfer ]

    public static final String CPFUNDTRANS =
            "{" + "Transfer Between Accounts" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击Funds menu." + breakLine +
                    "3. 点击Transfer Between Accounts menu." + breakLine +
                    "4. 选择账号，输入金额" + breakLine +
                    "5. 提交转账" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPMT2WALLETTRANS =
            "{" + "Transfer Between MT 2 Wallet" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击Funds menu." + breakLine +
                    "3. 点击Transfer Between Accounts menu." + breakLine +
                    "4. 选择 MT 和 wallet 账号，输入金额" + breakLine +
                    "5. 提交转账" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";
    public static final String CPWALLE2MTTTRANS =
            "{" + "Transfer Between Wallet 2 MT" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击Funds menu." + breakLine +
                    "3. 点击Transfer Between Accounts menu." + breakLine +
                    "4. 选择 MT 和 wallet 账号，输入金额" + breakLine +
                    "5. 提交转账" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPMT2MTWITHCREDIT =
            "{" + "Transfer Between Wallet 2 MT" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击Funds menu." + breakLine +
                    "3. 点击Transfer Between Accounts menu." + breakLine +
                    "4. 选择 MT 和 wallet 账号，输入金额" + breakLine +
                    "5. 提交转账" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ CP Deposit ]

    // Process Flow Global Description
    private static final String CPDEPOSIT_PROCESSFLOW_GLOBAL_DESC =
            detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击Deposit Funds menu" + breakLine +
                    "3. 选择账号，输入入金金额" + breakLine +
                    "4. 点击Continue进入下一步" + breakLine +
                    "5. 选择入金Type为该渠道后点Continue" + breakLine +
                    "6. 检查deposit details中的账号和金额" + breakLine +
                    "7. 提交入金" + breakLine +
                    "8. 进入Transaction History检查入金记录（账号、金额、入金类型）" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd;
    //入金callback
    private static final String CPDEPOSIT_CALLBACK_DESC =
            detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击Deposit Funds menu" + breakLine +
                    "3. 选择账号，输入入金金额" + breakLine +
                    "4. 点击Continue进入下一步" + breakLine +
                    "5. 选择入金Type为该渠道后点Continue" + breakLine +
                    "6. 检查deposit details中的账号和金额" + breakLine +
                    "7. 提交入金，回调callback" + breakLine +
                    "8. 进入Transaction History检查入金记录（账号、金额、入金类型、状态【successful】）" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd;

    private static final String CPDEPOSIT_BridgePayWithCALLBACK_DESC =
            detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击Deposit Funds menu" + breakLine +
                    "3. 选择账号，输入入金金额" + breakLine +
                    "4. 点击Continue进入下一步" + breakLine +
                    "5. 选择入金Type为该渠道后点Continue" + breakLine +
                    "6. 检查deposit details中的账号和金额" + breakLine +
                    "7. 提交入金，回调callback" + breakLine +
                    "8. 进入Transaction History检查入金记录（账号、金额、入金类型、状态【successful】）" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd;
    private static final String CPDEPOSIT_WITHDRAW_INPUT_INFO_DESC =
            detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击Accounts menu" + breakLine +
                    "3. 查看账号、余额、币种" + breakLine +
                    "4. 点击Deposit Funds 菜单" + breakLine +
                    "5. 查看出入金输入界面展示的账号、余额、币种" + breakLine +
                    "6. 检查Deposit\\WithDraw 输入界面账号、余额、币种是否与账号页面一致" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd;
    public static final String CPAPIDEPOSIT_AUDIT_PENDING_OR_FAILED_CHECK ="{" + CPAPIDEPOSIT_AUDIT_TAG  + "},{" + CPAPIDEPOSIT_AUDIT_PENDING_OR_FAILED_DESC + "}";;

    public static final String CPDEPOSIT_ST = "{" + "入金" + "}," + "{" + "Execute deposit" + "}";

    // E-Wallet
    public static final String CPDEPOSIT_EWALLET_BITWALLET = "{" + depositTag + "Bitwallet" + "},{" + CPDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPDEPOSIT_EWALLET_VOLET = "{" + depositTag + "Volet" + "},{" + CPDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPDEPOSIT_EWALLET_STICPAY = "{" + depositTag + "SticPay" + "},{" + CPDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPDEPOSIT_EWALLET_SKRILL = "{" + depositTag + "Skrill" + "},{" + CPDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPDEPOSIT_EWALLET_BINANCEPAY = "{" + depositTag + "Binance Pay" + "},{" + CPDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";

    // Cryptocurrency
    public static final String CPDEPOSIT_CRYPTO_BITCOIN = "{" + depositTag + "Bitcoin" + "},{" + CPDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPDEPOSIT_CRYPTO_ETH = "{" + depositTag + "ETH" + "},{" + CPDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPDEPOSIT_CRYPTO_USDC_ERC20 = "{" + depositTag + "USDC-ERC20" + "},{" + CPDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPDEPOSIT_CRYPTO_USDT_ERC20 = "{" + depositTag + "USDT-ERC20" + "},{" + CPDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPDEPOSIT_CRYPTO_USDT_BEP20 = "{" + depositTag + "USDT-BEP20" + "},{" + CPDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPDEPOSIT_CRYPTO_USDT_TRC20 = "{" + depositTag + "USDT-TRC20" + "},{" + CPDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";

    // Credit Card
    public static final String CPDEPOSIT_CC_PAYMENT_OPTION = "{" + depositTag + "Credit Card" + "},{" + CPDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPDEPOSIT_CC_BRIDGERPAY = "{" + depositTag + "Credit/Debit Card" + "},{" + CPDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPDEPOSIT_CC_APPLEGOOGLEPAY = "{" + depositTag + "ApplePay/GooglePay" + "},{" + CPDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";

    // Local Bank Transfer (LBT)
    public static final String CPDEPOSIT_JAPAN_BT = "{" + depositTag + "Japan Bank Transfer" + "},{" + CPDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";

    // Offline Transfer
    public static final String CPDEPOSIT_INTER_BANK_TRANS = "{" + depositTag + "International Bank Transfer" + "},{" + CPDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";

    // 入金-输入金额页面信息检查
    public static final String CPDEPOSIT_INPUT_INFO_CHECK = "{" + "出入金-" + "出入金页面信息检查" + "},{" + CPDEPOSIT_WITHDRAW_INPUT_INFO_DESC + "}";
    private static final String CPDEPOSIT_WITHDRAW_PAYDETAIL_INFO_DESC =  detailsStart + summaryDesc + detailsDivStart +
            "1. 登入CP" + breakLine +
            "2. 点击Deposit Funds 菜单" + breakLine +
            "3. 点击Deposit，输入入金金额，点击'继续'" + breakLine +
            "4. 选择入金方式，点击'继续'" + breakLine +
            "5. 检查信息确认页：\n" +
            "- 显示已选择的渠道\n" +
            "- 渠道提示显示正确\n" +
            "- 付款详情显示正确\n" +
            "- 汇率转换显示正确（如有）" + breakLine +
            detailsDivEnd + breakLine + detailsEnd; ;
    // 入金-输入金额支付详情页面信息检查
    public static final String CPDEPOSIT_WITHDRAW_PAGE_CHECK = "{" + depositTag + "出入金支付详情页面信息检查" + "},{" + CPDEPOSIT_WITHDRAW_PAYDETAIL_INFO_DESC + "}";;

    // endregion

    // region [ CP Withdrawal ]

    public static final String CPAPIWITHDRAW_UNIONPAY_WITHDRAW =     "{" + "出金- UnionPay出金" + "}," +
            "{" + detailsStart + summaryDesc + detailsDivStart +
            "1. 进入CP页面" + breakLine +
            "2. 选择账号和出金金额" + breakLine +
            "3. 出金渠道选择UnionPay" + breakLine +
            "4. 点击下一步，提交出金" + breakLine +
            "5. 检查出金记录" + breakLine +
            detailsDivEnd + breakLine + detailsEnd + "}"; ;

    public static final String CP2FA_POPUP_CHECK =     "{" + "出金- IBT进阶审核弹窗" + "}," +
            "{" + detailsStart + summaryDesc + detailsDivStart +
            "1.- 如果客户没有通过ID审核，或者进阶审核，会弹框引导客户进行审核" + breakLine +
            "2.- 如果客户的进阶审核已提交未审核，会提示客户等待审核通过再出金" + breakLine +
            "3.- 如果客户进阶审核已通过，则可以进行出金" + breakLine +
            detailsDivEnd + breakLine + detailsEnd + "}"; ;

    public static final String CP2FA_DEPOIST_POPUP_CHECK =     "{" + "入金- IBT进阶审核弹窗" + "}," +
            "{" + detailsStart + summaryDesc + detailsDivStart +
            "1.- 如果客户没有通过ID审核，或者进阶审核，会弹框引导客户进行审核" + breakLine +
            "2.- 如果客户的进阶审核已提交未审核，会提示客户等待审核通过再出金" + breakLine +
            "3.- 如果客户进阶审核已通过，则可以进行入金" + breakLine +
            detailsDivEnd + breakLine + detailsEnd + "}"; ;
    // Process Flow Global Description
    private static final String CPWITHDRAW_PROCESSFLOW_GLOBAL_DESC =
            detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击Funds menu" + breakLine +
                    "3. 点击Withdraw Funds menu" + breakLine +
                    "4. 选择账号，输入出金金额" + breakLine +
                    "5. 点击Continue进入下一步" + breakLine +
                    "6. 选择出金Type为该渠道后填写withdrawal details" + breakLine +
                    "7. 提交出金" + breakLine +
                    "8. 进入Transaction History检查出金记录（账号、金额、出金类型）" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd;
    //入金黄金流程
    public static final String CPDEPOSIT_PROCESSFLOW_GLOBAL_MAX_AMOUNT =
            detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入AU CP" + breakLine +
                    "2. 点击Funds menu" + breakLine +
                    "3. 点击Deposit Funds menu" + breakLine +
                    "4. 选择账号，输入入金金额" + breakLine +
                    "5. 点击Continue进入下一步" + breakLine +
                    "6. 查看界面出金限额" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd;

    // 出金黄金流程-出金额度校验
    public static final String CPWITHDRAW_PROCESSFLOW_GLOBAL_MAX_AMOUNT =
            detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入AU CP" + breakLine +
                    "2. 点击Funds menu" + breakLine +
                    "3. 点击Withdraw Funds menu" + breakLine +
                    "4. 选择账号，输入出金金额" + breakLine +
                    "5. 点击Continue进入下一步" + breakLine +
                    "6. 查看界面出金限额" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd;
    public static final String CPDEPOSIT_GOLD_FLOW = "{" + depositTag + "入金黄金流程" + "},{" + CPDEPOSIT_PROCESSFLOW_GLOBAL_MAX_AMOUNT + "}";

    public static final String CPWITHDRAW_PROCESSFLOW_GLOBAL_MAX_AMOUNT_DESC = "{" + withdrawalTag + "出金黄金流程限额" + "},{" + CPWITHDRAW_PROCESSFLOW_GLOBAL_MAX_AMOUNT + "}";

    public static final String CPWITHDRAW_ST = "{" + "出金" + "}," + "{" + "Execute withdrawal" + "}";

    // E-Wallet
    public static final String CPWITHDRAW_EWALLET_BITWALLET = "{" + withdrawalTag + "Bitwallet" + "},{" + CPWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPWITHDRAW_EWALLET_STICPAY = "{" + withdrawalTag + "SticPay" + "},{" + CPWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPWITHDRAW_EWALLET_TYGAPAY = "{" + withdrawalTag + "TygaPay" + "},{" + CPWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPWITHDRAW_EWALLET_BINANCEPAY = "{" + withdrawalTag + "BinancePay" + "},{" + CPWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";

    // Cryptocurrency
    public static final String CPWITHDRAW_CRYPTO_BITCOIN = "{" + withdrawalTag + "Bitcoin" + "},{" + CPWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPWITHDRAW_CRYPTO_ETH = "{" + withdrawalTag + "ETH" + "},{" + CPWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPWITHDRAW_CRYPTO_USDC_ERC20 = "{" + withdrawalTag + "USDC-ERC20" + "},{" + CPWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPWITHDRAW_CRYPTO_USDT_ERC20 = "{" + withdrawalTag + "USDT-ERC20" + "},{" + CPWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPWITHDRAW_CRYPTO_USDT_BEP20 = "{" + withdrawalTag + "USDT-BEP20" + "},{" + CPWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPWITHDRAW_CRYPTO_USDT_TRC20 = "{" + withdrawalTag + "USDT-TRC20" + "},{" + CPWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";

    // Credit Card

    // Local Bank Transfer (LBT)
    public static final String CPWITHDRAW_JAPAN_BT = "{" + withdrawalTag + "Japan Bank Transfer" + "},{" + CPWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPWITHDRAW_MALAYSIA_BT = "{" + withdrawalTag + "Malaysia Bank Transfer" + "},{" + CPWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPWITHDRAW_VIETNAM_BT = "{" + withdrawalTag + "Vietnam Bank Transfer" + "},{" + CPWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";

    // Offline Transfer
    public static final String CPWITHDRAW_INTER_BANK_TRANS = "{" + withdrawalTag + "International Bank Transfer" + "},{" + CPWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";

    // endregion

    // region [ CP Wallet ]

    public static final String CPWALLET_OPENACCOUNT =
            "{" + "MT5 Live VFSC 钱包开户（Sumsub 身份验证）" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 进入模拟注册网址进行注册" + breakLine +
                    "1.1. AU -> VFSC2 -> Estonia -> " + GlobalProperties.ALPHAURL + breakLine +
                    "2. 提交后跳转至 CP 个人信息验证 - 验证电子邮箱页面" + breakLine +
                    "3. 关闭个人信息页面后，点击钱包菜单，跳转至钱包首页" + breakLine +
                    "4. 点击钱包激活按钮，进行个人信息验证操作" + breakLine +
                    "4.1. 验证电子邮箱页面 - 提交OTP验证码" + breakLine +
                    "4.2. 绑定手机号码页面 - 提交手机号码" + breakLine +
                    "4.3. 验证手机号码页面 - 提交OTP验证码" + breakLine +
                    "4.4. 个人信息页面 - 提交个人信息" + breakLine +
                    "5. 完成个人信息验证后，点击补全账号按钮，进行设置交易账户操作" + breakLine +
                    "6. 提交 MT5 真实交易账户配置（Standard STP 账户类型、USD 货币类型）" + breakLine +
                    "7. 在主页身份验证专区点击立即验证按钮，进行 Sumsub 身份验证操作" + breakLine +
                    "8. 提交身份验证后，登入 OWS 进行账户审核" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    private static final String CPWALLET_DEPOSIT_PROCESSFLOW_GLOBAL_DESC =
            detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击钱包菜单，跳转至钱包首页" + breakLine +
                    "3. 点击币种列表充值按钮，跳转至钱包充值页面" + breakLine +
                    "4. 点击币种网络" + breakLine +
                    "5. 点击地址复制按钮" + breakLine +
                    "6. 点击充值历史列表详情按钮，显示充值详情页面弹窗" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd;


    public static final String CPWALLET_DEPOSIT_CRYPTO_BITCOIN = "{" + walletDepositTag + "BTC币种" + "},{" + CPWALLET_DEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPWALLET_DEPOSIT_CRYPTO_ETH = "{" + walletDepositTag + "ETH 币种" + "},{" + CPWALLET_DEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPWALLET_DEPOSIT_CRYPTO_USDC = "{" + walletDepositTag + "USDC 币种" + "},{" + CPWALLET_DEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPWALLET_DEPOSIT_CRYPTO_USDT = "{" + walletDepositTag + "USDT 币种" + "},{" + CPWALLET_DEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";

    private static final String CPWALLET_WITHDRAW_PROCESSFLOW_GLOBAL_DESC =
            detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击钱包菜单，跳转至钱包首页" + breakLine +
                    "3. 点击币种列表提现按钮，跳转至钱包提现页面" + breakLine +
                    "4. 输入提现币种网络地址" + breakLine +
                    "5. 点击币种网络" + breakLine +
                    "6. 输入提现金额" + breakLine +
                    "7. 提交提现" + breakLine +
//                    "8. 点击提现历史列表详情按钮，显示提现详情页面弹窗" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd;

    public static final String CPWALLET_WITHDRAW_CRYPTO_BITCOIN = "{" + walletWithdrawalTag + "BTC币种" + "},{" + CPWALLET_WITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPWALLET_WITHDRAW_CRYPTO_ETH = "{" + walletWithdrawalTag + "ETH币种" + "},{" + CPWALLET_WITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPWALLET_WITHDRAW_CRYPTO_USDC = "{" + walletWithdrawalTag + "USDC币种" + "},{" + CPWALLET_WITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPWALLET_WITHDRAW_CRYPTO_USDT = "{" + walletWithdrawalTag + "USDT币种" + "},{" + CPWALLET_WITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";

    private static final String CPWALLET_DEPOSIT_HISTORY_PROCESSFLOW_GLOBAL_DESC =
            "{" + "钱包入金历史操作" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击钱包菜单，跳转至钱包首页" + breakLine +
                    "3. 点击币种列表入金按钮，跳转至钱包入金页面" + breakLine +
                    "3. 点击入金历史列表详情按钮，显示入金详情弹窗" + breakLine +
//                    "4. 点击查看更多，跳转至订单历史页面" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPWALLET_DEPOSIT_HISTORY_CRYPTO_BITCOIN = "{" + depositTag + "BTC" + "},{" + CPWALLET_DEPOSIT_HISTORY_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPWALLET_DEPOSIT_HISTORY_CRYPTO_ETH = "{" + depositTag + "ETH" + "},{" + CPWALLET_DEPOSIT_HISTORY_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPWALLET_DEPOSIT_HISTORY_CRYPTO_USDC = "{" + depositTag + "USDC" + "},{" + CPWALLET_DEPOSIT_HISTORY_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPWALLET_DEPOSIT_HISTORY_CRYPTO_USDT = "{" + depositTag + "USDT" + "},{" + CPWALLET_DEPOSIT_HISTORY_PROCESSFLOW_GLOBAL_DESC + "}";

    public static final String CPWALLET_HOME_Balance_View =
            "{" + "CP钱包首页模块校验" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. 登录CP" + breakLine +
                    "2. 点击钱包菜单，跳转至钱包首页" + breakLine +
                    "3. 查看Balance模块，模块包括：总余额、可用余额以及转换金额" + breakLine +
                    "4. 查看Balance模块按钮，包括入金按钮、出金按钮、划转按钮、闪兑按钮" + breakLine +
                    "5. 点击Balance模块出金按钮，跳转至出金页面，默认显示第一个币种" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPWALLET_HOME_CryptoList =
            "{" + "CP钱包首页列表校验" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. 登录CP" + breakLine +
                    "2. 点击钱包菜单，跳转至钱包首页" + breakLine +
                    "3. 查看币种列表，币种图标及缩写、余额、预估金额、可用余额、冻结额和操作（入金按钮、出金按钮、划转按钮、闪兑按钮）" + breakLine +
                    "4. 列表格式正确、图标显示正常，每个币种独立包含操作按钮" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";
    public static final String CPWALLET_HOME_DropdownMenu =
            "{" + "CP钱包首页列表校验" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. 登录CP" + breakLine +
                    "2. 点击钱包菜单，跳转至钱包首页" + breakLine +
                    "3. 展开下拉框，选择USDT、BTC、ETH、USDC币种 +" + breakLine +
                    "4. 总余额、可用余额改成相应币种" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";
    public static final String CPWALLET_HOME_DropdownMore =
            "{" + "CP钱包首页列表校验" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. 登录CP" + breakLine +
                    "2. 点击钱包菜单，跳转至钱包首页" + breakLine +
                    "3. 列表点击更多按钮，弹出币种列表页面" + breakLine +
                    "4. 选择相应币种后，首页等值币种均改变成该币种" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPWALLET_HOME_RealDeposit =
            "{" + "CP钱包首页列表校验" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. 登录CP" + breakLine +
                    "2. 点击钱包菜单，跳转至钱包首页" + breakLine +
                    "3. 点击入金按钮，选择USDT-TRON链" + breakLine +
                    "4. 生成二维码、地址" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";
    public static final String CPWALLET_HOME_RealWithdraw =
            "{" + "CP钱包首页列表校验" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. 登录CP" + breakLine +
                    "2. 点击钱包菜单，跳转至钱包首页" + breakLine +
                    "3. 点击出金按钮，选择USDT-BSC链" + breakLine +
                    "4. 校验生产的表格信息" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";
    // endregion

    // region [ CP Login ]

    public static final String CPACCOUNT_PHONE_LOGIN_SUCCESS =
            "{" + "手机登录" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 在CP登录页面点击手机标签" + breakLine +
                    "2. 提交正确的手机与密码进行登录" + breakLine +
                    "3. 成功登入CP" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACCOUNT_PHONE_LOGIN_FAIL =
            "{" + "手机登录失败" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 在CP登录页面点击手机标签" + breakLine +
                    "2. 提交正确的手机号和错误的密码进行登录" + breakLine +
                    "3. 系统提示登录失败，并停留在登录页面" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACCOUNT_EMAIL_LOGIN_FAIL =
            "{" + "邮箱登录失败" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 在CP登录页面邮箱标签，提交正确的邮箱和错误的密码进行登录" + breakLine +
                    "2. 系统提示登录失败，并停留在登录页面" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACCOUNT_EMAIL_TOTP_LOGIN =
            "{" + "邮箱TOTP登录" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 在CP登录页面邮箱标签，提交邮箱和密码进行登录" + breakLine +
                    "2. 跳转至TOTP验证页面" + breakLine +
                    "3. 提交TOTP验证码" + breakLine +
                    "4. 成功登入CP" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ CP Forgot Password ]

    public static final String CPACCOUNT_EMAIL_FORGOT_PWD =
            "{" + "邮箱忘记密码" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 在CP登录页面点击邮箱忘记密码" + breakLine +
                    "2. 跳转至CP邮箱忘记密码页面" + breakLine +
                    "3. 提交邮箱信息" + breakLine +
                    "4. 跳转至提交成功页面" + breakLine +
                    "5. 从数据库获取邮箱密码重置链接" + breakLine +
                    "6. 浏览器打开邮箱密码重置链接，跳转至CP密码设置页面" + breakLine +
                    "7. 提交新密码" + breakLine +
                    "8. 成功重置密码" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPACCOUNT_PHONE_FORGOT_PWD =
            "{" + "手机忘记密码" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 在CP登录页面点击手机忘记密码" + breakLine +
                    "2. 跳转至CP手机忘记密码页面" + breakLine +
                    "3. 填写手机号后点击发送短信验证码按钮" + breakLine +
                    "4. 从数据库获取验证码" + breakLine +
                    "4.1 AU 根据Apollo设定判断，从数据库或三方信息中心中获取验证码" + breakLine +
                    "5. 提交验证码" + breakLine +
                    "6. 跳转至CP密码设置页面" + breakLine +
                    "7. 提交新密码" + breakLine +
                    "8. 成功重置密码" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ CP Download ]

    public static final String CPDOWNLOAD_INFO_CHECK =
            "{" + "检查下载页面的下载信息" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击下载菜单，跳转至下载页面" + breakLine +
                    "3. 检查 MetaTrader 4 下载平台信息（图标、显示文本、下载链接、下载的文件名）" + breakLine +
                    "3.1. AU - 不支持检查下载文件名" + breakLine +
                    "4. 检查 MetaTrader 5 下载平台信息（图标、显示文本、下载链接、下载的文件名）" + breakLine +
                    "4.1. AU - 不支持检查下载文件名" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ CP Support  ]

    public static final String CPSUPPORT_CREATE_TICKET =
            "{" + "创建支持请求" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击帮助中心菜单，跳转至帮助中心页面" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ CP API Deposit ]

    // Process Flow Global Description
    private static final String CPAPIDEPOSIT_PROCESSFLOW_GLOBAL_DESC = "暂无描述";

    // E-Wallet
    public static final String CPAPIDEPOSIT_EWALLET_BITWALLET = "{" + depositTag + "Bitwallet" + "},{" + CPAPIDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPAPIDEPOSIT_EWALLET_VOLET = "{" + depositTag + "Volet" + "},{" + CPAPIDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPAPIDEPOSIT_EWALLET_STICPAY = "{" + depositTag + "SticPay" + "},{" + CPAPIDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPAPIDEPOSIT_EWALLET_BinancePay="{" + depositTag + "BinancePay" + "},{" + CPAPIDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPAPIDEPOSIT_EWALLET_SKRILL = "{" + depositTag + "Skrill" + "},{" + CPAPIDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPAPIDEPOSIT_EWALLET_TYGAPAY = "{" + depositTag + "Tygapay" + "},{" + CPAPIDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPAPIDEPOSIT_EWALLET_NETELLER = "{" + depositTag + "Neteller" + "},{" + CPAPIDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPAPIDEPOSIT_EWALLET_MALAYSIA = "{" + depositTag + "Malaysia EWallet" + "},{" + CPAPIDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";

    // Cryptocurrency
    public static final String CPAPIDEPOSIT_CRYPTO_TRC = "{" + depositTag + "Crypto TRC" + "},{" + CPAPIDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";

    // Credit Card
    public static final String CPAPIDEPOSIT_CC_BANXA = "{" + depositTag + "Banxa" + "},{" + CPAPIDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";

    // Local Bank Transfer (LBT)
    public static final String CPAPIDEPOSIT_JAPAN_BT = "{" + depositTag + "Japan Bank Transfer" + "},{" + CPAPIDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPAPIDEPOSIT_JAPAN_EMONEY = "{" + depositTag + "Japan e-Money" + "},{" + CPAPIDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPAPIDEPOSIT_MALAYSIA_BT = "{" + depositTag + "Malaysia Bank Transfer" + "},{" + CPAPIDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPAPIDEPOSIT_MALAYSIA_FPX = "{" + depositTag + "Malaysia FPX" + "},{" + CPAPIDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPAPIDEPOSIT_THAI_BT = "{" + depositTag + "Thailand Bank Transfer" + "},{" + CPAPIDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPAPIDEPOSIT_PHP_BT = "{" + depositTag + "Philippines Bank Transfer" + "},{" + CPAPIDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPAPIDEPOSIT_VIETNAM_BT = "{" + depositTag + "Vietnam Bank Transfer" + "},{" + CPAPIDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";

    // Unionpay
    public static final String CPAPIDEPOSIT_UNIONPAY = "{" + depositTag + "Unionpay" + "},{" + CPAPIDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";

    public static final String CPAPIDEPOSIT_HISTORY =
            "{" + "CP Deposit API - Deposit History" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. API 登录CP" + breakLine +
                    "2. 查询入金记录 POST /web-api/cp/api/transactionHistory/deposit" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPAPIDEPOSIT_CASH_ADJUSTMENT =
            "{" + "CP Deposit API - Deposit Cash Adjustment " + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. 在CRM 點擊 Add " + breakLine +
                    "2. 輸入 Account, Type = Deposit, 金額" + breakLine +
                    "3.  點擊Confirm" + breakLine +
                    "4. 使用另一個賬戶審核成功" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPAPIDEPOSIT_CREDIT_ADJUSTMENT =
            "{" + "CP Deposit API - Deposit Credit Adjustment " + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. 在CRM task-credit adjustment audit 點擊Single Item Upload  " + breakLine +
                    "2. 輸入 Account, Type = Credit In, 金額" + breakLine +
                    "3.  點擊Confirm" + breakLine +
                    "4. 使用另一個賬戶審核成功" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPAPIDEPOSIT_APPROVE_DEPOSIT_SUCCESS =
            "{" + "admin Deposit API - Deposit Approve success" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. admin验证审核入金成功 " + breakLine +
                    "2. 入金账号净值检查" + breakLine +
                    "3. CP portal交易历史检查" + breakLine +
                    "4. 邮件推送检查" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";
    public static final String CPUI_CREDIT_CARD_WITHDRAWCHECK =
            "{" + "入金 - 信用卡出金信息检查" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. 信用卡入金，审核入金成功 " + breakLine +
                    "2. 信用卡出金" + breakLine +
                    "3. CP portal检查出金是否包含入金信用卡信息" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPAPIDEPOSIT_APPROVE_DEPOSIT_FAIL_PENDING =
            "{" + "admin Deposit API - Deposit Approve Pending/Failed" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. admin验证审核入金 = 失败/ Pending " + breakLine +
                    "2. 入金账号净值检查" + breakLine +
                    "3. CP portal交易历史检查" + breakLine +
                    "4. 邮件推送检查" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPDEPOSIT_MIN_AMOUNT_POPUP_WINDOW_GLOBAL_DESC =
            detailsStart + summaryAPIDesc + detailsDivStart +
                    "1.- 如果客户输入的金额未达到渠道的最低入金，提示弹窗"  + breakLine+
                    "2.- 点击Change Amount后，金额成功增加，符合渠道最低需求"  + breakLine+
                    "3.- 点击Change Payment Method后，成功收起弹窗，金额保持不变" + breakLine +
                    "4.- 点击支付后，查看页面是否跳转成功" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd ;

    public static final String CPDEPOSIT_MIN_AMOUNT_POPUP_WINDOW = "{" + depositTag + "Unionpay入金弹框校验&支付跳转校验" + "},{" + CPDEPOSIT_MIN_AMOUNT_POPUP_WINDOW_GLOBAL_DESC + "}";


    // endregion

    // region [ CP API Withdrawal ]

    // Process Flow Global Description
    private static final String CPAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC = "暂无描述";

    // E-Wallet
    public static final String CPAPIWITHDRAW_EWALLET_BITWALLET = "{" + withdrawalTag + "Bitwallet" + "},{" + CPAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPAPIWITHDRAW_EWALLET_VOLET = "{" + withdrawalTag + "Volet" + "},{" + CPAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPAPIWITHDRAW_EWALLET_STICPAY = "{" + withdrawalTag + "SticPay" + "},{" + CPAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPAPIWITHDRAW_EWALLET_SKRILL = "{" + withdrawalTag + "Skrill" + "},{" + CPAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPAPIWITHDRAW_EWALLET_TYGAPAY = "{" + withdrawalTag + "Tygapay" + "},{" + CPAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPAPIWITHDRAW_EWALLET_NETELLER = "{" + withdrawalTag + "Neteller" + "},{" + CPAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPAPIWITHDRAW_EWALLET_AIRTM = "{" + withdrawalTag + "AirTM" + "},{" + CPAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";

    // Cryptocurrency
    public static final String CPAPIWITHDRAW_CRYPTO_BITCOIN = "{" + withdrawalTag + "Bitcoin" + "},{" + CPAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPAPIWITHDRAW_CRYPTO_ETH = "{" + withdrawalTag + "ETH" + "},{" + CPAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPAPIWITHDRAW_CRYPTO_USDC_ERC20 = "{" + withdrawalTag + "USDC-ERC20" + "},{" + CPAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPAPIWITHDRAW_CRYPTO_USDT_ERC20 = "{" + withdrawalTag + "USDT-ERC20" + "},{" + CPAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPAPIWITHDRAW_CRYPTO_USDT_BEP20 = "{" + withdrawalTag + "USDT-BEP20" + "},{" + CPAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPAPIWITHDRAW_CRYPTO_USDT_TRC20 = "{" + withdrawalTag + "USDT-TRC20" + "},{" + CPAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";

    // Credit Card
    public static final String CPAPIWITHDRAW_CC = "{" + withdrawalTag + "Credit Card" + "},{" + CPAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";

    // Local Bank Transfer (LBT)
    public static final String CPAPIWITHDRAW_UNIONPAY = "{" + withdrawalTag + "Unionpay" + "},{" + CPAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPAPIWITHDRAW_JAPAN_BT = "{" + withdrawalTag + "Japan Bank Transfer" + "},{" + CPAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPAPIWITHDRAW_MALAYSIA_BT = "{" + withdrawalTag + "Malaysia Bank Transfer" + "},{" + CPAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPAPIWITHDRAW_THAI_BT = "{" + withdrawalTag + "Thailand Bank Transfer" + "},{" + CPAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPAPIWITHDRAW_PHP_BT = "{" + withdrawalTag + "Philippines Bank Transfer" + "},{" + CPAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPAPIWITHDRAW_VIETNAM_BT = "{" + withdrawalTag + "Vietnam Bank Transfer" + "},{" + CPAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPAPIWITHDRAW_KOREA_BT = "{" + withdrawalTag + "Korea Bank Transfer" + "},{" + CPAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String CPAPIWITHDRAW_BRAZIL_BT = "{" + withdrawalTag + "Brazil Bank Transfer" + "},{" + CPAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";

    //出金渠道页面信息校验（含汇率信息）
    public static final String CPAPIWITHDRAW_WITHDRAW_DETAIL_INFO_CHECK =
            "{" + "CP出金渠道頁面-提取页面信息校验" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. 登录CP" + breakLine +
                    "2. 选择LBT渠道出金" + breakLine +
                    "3. 检查渠道页面汇率信息" + breakLine +
                    "4. 检查提现详情显示正确账户和提现金额" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";
    // Offline Transfer

    // Big Amount Withdrawal
    public static final String CPAPIWITHDRAW_BIGAMOUNT_RISKAUDIT =
            "{" + "CP大额出金" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. 登录CP" + breakLine +
                    "2. 提交大额出金(Crypto TRC20出金 $50000.55)" + breakLine +
                    "3. 检查账号余额已扣款" + breakLine +
                    "4. 检查出金在risk audit状态 - status: 21" + breakLine +
                    "5. email中risk audit返回成功" + breakLine +
                    "6. 检查出金变成accepted状态 - status: 5" + breakLine +
                    "7. 取消出金，恢复余额" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    //Others Withdrawal API
    public static final String CPAPIWITHDRAW_OTHERSAPI =
            "{" + "CP Withdraw API" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. 登录CP" + breakLine +
                    "2. 查询出金账号数据 POST /web-api/cp/api/withdrawal/getWithdrawalData_cp" + breakLine +
                    "3. 获取出金汇率 GET /hgw/rw/payment-api/cp/api/withdrawal/exchangeRate" + breakLine +
                    "4. 查询LBT余额 GET /hgw/rw/payment-api/cp/api/withdrawal/getLBTWithdrawalData_cp" + breakLine +
                    "5. 查询出金黑名单 GET /hgw/r/payment-api/cp/api/withdrawal/queryBlacklist" + breakLine +
                    "6. 查询出金权限 GET /web-api/cp/api/withdrawal/getLimitInfo" + breakLine +
                    "7. 查询非CC出金渠道 POST /hgw/rw/payment-api/cp/api/withdrawal/getNonCreditCardWithdrawTypeCP" + breakLine +
                    "8. 校验MAM费用结算 POST /hgw/r/payment-api/cp/api/withdrawal/validateMamFeeSettlement" + breakLine +
                    "9. 查询出金PCS启用状态 GET /hgw/r/payment-api/cp/api/withdrawal/pcs/enabled" + breakLine +
                    "10. 查询出金PCS币值上限 POST /hgw/r/payment-api/cp/api/withdrawal/pcs/currency-limit" + breakLine +
                    "11. 查询出金PCS排序信息 POST /hgw/rw/payment-api/cp/api/withdrawal/pcs/v2/sort-info" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ CP API Transfer]

    // Process Flow Global Description

    public static final String CPAPITRANSFER_TRADINGACCOUNT =
            "{" + "CP Transfer API" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. API login CP" + breakLine +
                    "2. 提交转账 POST /hgw/rw/payment-api/cp/api/transfer/applyTransfer_cp" + breakLine +
                    "3. 转账汇率列表 GET /hgw/r/payment-api/cp/api/transfer/currencyList" + breakLine +
                    "4. 查询转账汇率 GET /hgw/rw/payment-api/cp/api/transfer/currencyRate" + breakLine +
                    "5. 转账Credit扣除数额 POST /hgw/rw/payment-api/cp/api/withdrawal/getDeductCredit" + breakLine +
                    "6. 查询转账黑名单 GET /hgw/r/payment-api/cp/api/transfer/queryBlacklist" + breakLine +
                    "7. 查询转账权限 POST /hgw/r/payment-api/cp/api/transfer/transferPermission" + breakLine +
                    "8. 查询转账数据 POST /hgw/r/payment-api/cp/api/transfer/getTransferData_cp" + breakLine +
                    "9. 转账历史记录 GET /web-api/cp/api/funds/transfer-history/get" + breakLine +
                    "10. 下载报告 POST /web-api/cp/api/transactionHistory/downloadAccountTransactionHistoryPDF" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ CP API Wallet ]

    // Process Flow Global Description
    public static final String CPAPIWALLET_OPENACCOUNT =
            "{" + "钱包帐户页面操作" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. API login CP" + breakLine +
                    "2. 钱包帐户展示 GET /web-api/cp/api/wallet/open-account-validate" + breakLine +
                    "3. 钱包主页 POST /web-api/cp/api/wallet/home" + breakLine +
                    "4. 钱包帐户基本信息 GET /web-api/cp/api/wallet/base-info" + breakLine +
                    "5. 钱包级别信息 GET /web-api/cp/api/wallet/level-info" + breakLine +
                    "6. 钱包帐户选择列表 GET /web-api/cp/api/wallet/select-list" + breakLine +
                    "7. 钱包入金币种H5 GET /web-api/cp/api/wallet/deposit/mobile/getCurrencyChains" + breakLine +
                    "8. 资金流水列表页 POST /web-api/cp/api/wallet/flow-list" + breakLine +
                    "9. 钱包帐户列表下载 POST /api/admin/wallet/account/account-list-download" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPAPIWALLET_CONVERSION =
            "{" + "钱包闪兑页面操作" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. API login CP" + breakLine +
                    "2. 钱包权限 GET /web-api/cp/api/wallet/user-permission" + breakLine +
                    "3. 钱包闪兑列表 POST /web-api/cp/api/wallet/convert/get-conver-list" + breakLine +
                    "4. 钱包闪兑数量 POST /web-api/cp/api/wallet/convert/get-quantity-and-exchange" + breakLine +
                    "5. 钱包闪兑提交 POST /web-api/cp/api/wallet/convert/conver-submit" + breakLine +
                    "6. 钱包闪兑刷新订单 POST /web-api/cp/api/wallet/convert/refresh-order" + breakLine +
                    "7. 钱包闪兑订单确认 POST /web-api/cp/api/wallet/convert/confirm-order" + breakLine +
                    "8. 钱包闪兑记录列表 POST /web-api/cp/api/wallet/convert/record-list" + breakLine +

                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPAPIWALLET_DEPOSIT =
            "{" + "钱包入金页面操作" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. API login CP" + breakLine +
                    "2. 钱包入金币种 GET /web-api/cp/api/wallet/deposit/getCurrencyChains" + breakLine +
                    "3. 钱包入金权限 GET /web-api/cp/api/wallet/deposit/checkDepositPermissions" + breakLine +
                    "4. 钱包入金权限H5 GET /web-api/cp/api/wallet/deposit/mobile/checkDepositPermissions" + breakLine +
                    "5. 钱包入金地址 GET /web-api/cp/api/wallet/deposit/getDepositAddress?currency=USDT&chain=TRX" + breakLine +
                    "6. 钱包入金记录详细 GET /web-api/cp/api/wallet/deposit/record-detail?id=" + breakLine +
                    "7. 钱包入金记录列表 POST /web-api/cp/api/wallet/deposit/record-list" + breakLine +
                    "8. 钱包入金记录下载 POST /web-api/cp/api/wallet/deposit/record-list" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPAPIWALLET_WITHDRAW =
            "{" + "钱包出金页面操作" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. API login CP" + breakLine +
                    "2. 钱包出金黑名单 GET /hgw/r/payment-api/cp/api/withdrawal/queryBlacklist" + breakLine +
                    "3. 钱包出金币种列表 POST /web-api/cp/api/wallet/withdraw/currency-list" + breakLine +
                    "4. 钱包出金列表 POST /web-api/cp/api/wallet/withdraw/order-list" + breakLine +
                    "5. 钱包crypto转换 POST /web-api/cp/api/wallet/fetch-crypto-fiat-exchange" + breakLine +
                    "6. 钱包出金汇率计算 POST /web-api/cp/api/wallet/withdraw/calc-fee" + breakLine +
                    "7. 钱包出金验证 POST /web-api/cp/api/wallet/withdraw/order-verification" + breakLine +
                    "8. 钱包出金验证信息 GET /web-api/cp/api/multiFactorAuth/wallet-withdraw/getMultiAuthMethod" + breakLine +
                    "9. 钱包出金确认 POST /web-api/cp/api/wallet/withdraw/order-confirm" + breakLine +
                    "10. 钱包出金详细 GET /web-api/cp/api/wallet/withdraw/order-detail" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPAPIWALLET_TRANSFER =
            "{" + "钱包转帐页面操作" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. API login CP" + breakLine +
                    "2. 钱包防重验证token GET /web-api/api/token/anti-reuse" + breakLine +
                    "3. 钱包转帐 POST /hgw/r/payment-api/cp/api/transfer/getTransferData_cp" + breakLine +
                    "4. 钱包转帐列表 POST /web-api/cp/api/wallet/trans-history/transfer-list" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ Admin API Wallet ]

    // Process Flow Global Description
    public static final String AdminAPIWALLET_ACCOUNTMANAGEMENT =
            "{" + "Admin钱包帐户管理，" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. API login Admin" + breakLine +
                    "2. 查询冻结明细 POST /api/admin/wallet/account/frozen-detail/list" + breakLine +
                    "3. Bybit账户管理-币种参数 GET /admin/wallet/platformAccount/queryAccountCurrencyList" + breakLine +
                    "4. 帐户管理币种列表 POST /admin/wallet/platformAccount/queryAccountCurrencyList" + breakLine +
                    "5. 钱包账户列表 POST /api/admin/wallet/account/list" + breakLine +
                    "6. 币种账户 POST /admin/wallet/account/currency-account-info" + breakLine +
                    "7. 钱包权限列表 GET /admin/wallet/account/permissions " + breakLine +
                    "8. 设置钱包帐户权限 POST /admin/wallet/account/set-permissions" + breakLine +
                    "9. 查询当前 钱包账户的资金流水 POST /api/admin/wallet/account/fund-flow-list" + breakLine +
                    "10. 冻结钱包账户 POST /api/admin/wallet/account/freeze" + breakLine +
                    "11. 解冻钱包账户 POST /api/admin/wallet/account/unfreeze" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";


    public static final String AdminAPIWALLET_DEPOSIT =
            "{" + "Admin钱包入金管理，" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. API login Admin" + breakLine +
                    "2. 钱包入金列表 POST /api/admin/wallet/deposit/currency-list" + breakLine +
                    "3. 钱包入金交易订单列表 POST /api/admin/wallet/deposit/wallet-order-list" + breakLine +
                    "4. 钱包入金交易详情页 POST /api/admin/wallet/deposit/wallet-order-detail" + breakLine +

                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String AdminAPIWALLET_WITHDRAWAL =
            "{" + "Admin钱包出金管理，" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. API login Admin" + breakLine +
                    "2. 钱包出金列表 POST /api/admin/wallet/withdraw/order-list" + breakLine +
                    "3. 钱包出金交易详细页 POST /api/admin/wallet/withdraw/order-detail" + breakLine +
                    "4. 钱包网关出金详细页 POST api/admin/wallet/ingress/withdraw/detail" + breakLine +
                    "5. 钱包网关出金页列表 POST api/admin/wallet/ingress/withdraw/list" + breakLine +
                    "6. 钱包网关出金导出 POST api/admin/wallet/ingress/withdraw/export" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String AdminAPIWALLET_TRANSFER =
            "{" + "Admin钱包转帐管理，" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. API login Admin" + breakLine +
                    "2. 钱包转帐列表 POST /api/admin/wallet/transfer/transfer-order-list" + breakLine +
                    "3. 钱包转帐交易详细页 POST /api/admin/wallet/transfer/transfer-order-detail" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String AdminAPIWALLET_CONVERT =
            "{" + "Admin钱包闪兑管理，" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. API login Admin" + breakLine +
                    "2. 钱包闪兑列表 POST /api/admin/wallet/convert/convert-order-list" + breakLine +
                    "3. 钱包闪兑订单列表 POST /api/admin/wallet/hedge/convert-order-list" + breakLine +
                    "4. 钱包闪兑下载 POST /admin/wallet/hedge/convert-order-list-download" + breakLine +
                    "5. 钱包闪兑订单下载 POST /admin/wallet/convert/convert-order-list-download" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";


    public static final String AdminAPIWALLET_BYBITPLATFROM =
            "{" + "Admin钱包Bybit帐户管理，" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. API login Admin" + breakLine +
                    "2. 钱包Bybit账户管理-列表数据 POST /api/admin/wallet/platformAccount/queryAccountList" + breakLine +
                    "3. Bybit账户管理 POST /api/admin/wallet/platformAccount/queryAccountCurrencyList" + breakLine +
                    "4. 奖励订单管理 POST /admin/wallet/platformAccount/queryAccountCurrencyList" + breakLine +
                    "5. 奖励订单详情 GET /api/admin/wallet/deposit/wallet-reward-order-detail" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";


    public static final String AdminAPIWALLET_BYBITPLATFROMTRANSACTION =
            "{" + "Admin钱包bybit动帐，" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. API login Admin" + breakLine +
                    "2. 钱包Bybit动账记录-列表数据 POST /api/admin/wallet/platformFund/getFundList" + breakLine +
                    "3. Bybit账户管理-币种参数 POST /admin/wallet/platformAccount/queryAccountCurrencyList" + breakLine +
                    "4. 钱包Bybit账户详情详情页 POST /api/admin/wallet/platformAccount/queryAccountDetailList" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";


    public static final String AdminAPIWALLET_CHANNEL =
            "{" + "Admin钱包入金链，" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. API login Admin" + breakLine +
                    "2. 钱包admin入金Bybit订单列表 POST /admin/wallet/deposit/channel-order-list" + breakLine +
                    "3. admin入金链 POST /admin/wallet/deposit/chain-list" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";


    public static final String AdminAPIWALLET_DEPOSITASSETS =
            "{" + "Admin钱包Bybit归集管理，" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. API login Admin" + breakLine +
                    "2. 钱包admin BYbit归集划转订单查询列表 POST /admin/wallet/deposit/channel-transfer-record-list" + breakLine +
                    "3. adminBYbit归集划转订单查询详情 POST /api/admin/wallet/deposit/channel-transfer-record-detail" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";


    public static final String AdminAPIWALLET_BYBITASSETSDASHBOARD =
            "{" + "Admin钱包Bybit Board，" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. API login Admin" + breakLine +
                    "2. 钱包admin Today's Deposit POST /admin/wallet/platformAsset/queryTodayAsset" + breakLine +
                    "3. 钱包admin Yesterday's Deposit POST /admin/wallet/platformAsset/queryYesterdayAsset" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion


    // region [ IB Withdraw ]


    // Process Flow Global Description
    private static final String IBWITHDRAW_PROCESSFLOW_GLOBAL_DESC =
            detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入IB" + breakLine +
                    "2. 点击Withdraw Rebate menu" + breakLine +
                    "3. 选择账号，输入出金金额" + breakLine +
                    "4. 点击Submit进入下一步" + breakLine +
                    "5. 选择出金Type为该渠道后填写withdrawal details" + breakLine +
                    "6. 提交出金" + breakLine +
                    "7. 进入Transaction History，点击Withdrawal menu检查出金记录（账号、金额、出金类型）" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd;

    public static final String IBWITHDRAW_ST = "{" + "IB出金" + "}," + "{" + "Execute IB withdrawal" + "}";

    // E-Wallet
    public static final String IBWITHDRAW_EWALLET_STICPAY = "{" + withdrawRebateTag + "SticPay" + "},{" + IBWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";

    public static final String IBWITHDRAW_EWALLET_Neteller = "{" + withdrawRebateTag + "Neteller" + "},{" + IBWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";

    // Cryptocurrency
    public static final String IBWITHDRAW_CRYPTO_ETH = "{" + withdrawRebateTag + "ETH" + "},{" + IBWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String IBWITHDRAW_CRYPTO_USDT_ERC20 = "{" + withdrawRebateTag + "USDT-ERC20" + "},{" + IBWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";

    // Offline Transfer
    public static final String IBWITHDRAW_INTER_BANK_TRANS = "{" + withdrawRebateTag + "International Bank Transfer" + "},{" + IBWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";

    // endregion

    // region [ IB Transfer ]

    public static final String IBFUNDTRANS =
            "{" + "Transfer Rebate" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入IB" + breakLine +
                    "2. 点击Transfer Rebate menu" + breakLine +
                    "3. 选择账号，输入金额" + breakLine +
                    "4. 提交转账" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ IB API Withdraw ]

    private static final String IBAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC = "暂无描述";

    public static final String IBAPIWITHDRAW_THAI_BT = "{" + withdrawalTag + "Thailand Bank Transfer" + "},{" + IBAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String IBAPIWITHDRAW_EWALLET_BITWALLET = "{" + withdrawalTag + "Bitwallet" + "},{" + IBAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String IBAPIWITHDRAW_CRYPTO_BITCOIN = "{" + withdrawalTag + "Bitcoin" + "},{" + IBAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String IBAPIWITHDRAW_EWALLET_NETELLER = "{" + withdrawalTag + "Neteller" + "},{" + IBAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";
    public static final String IBAPIWITHDRAW_INTER_BANK_TRANS = "{" + withdrawalTag + "International Bank Transfer" + "},{" + IBAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";


    //IB Others Withdrawal API
    public static final String IBAPIWITHDRAW_OTHERSAPI =
            "{" + "IB Withdraw API" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. 登录CP" + breakLine +
                    "2. IB出金黑名单状态 GET /web-api/api/withdrawal/queryBlacklist" + breakLine +
                    "3. 支付数据 POST /hgw/rw/payment-api/api/payment/info/getInfo" + breakLine +
                    "4. 支付列表 POST /hgw/rw/payment-api/api/payment/info/getPaymentInfoList" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ IB API Transfer ]

    public static final String IBAPITRANSFER_OWNANDSUBIB_OTHERSAPI =
            "{" + "IB Transfer API" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. API 登入IB" + breakLine +
                    "2. IB提交转账 POST /web-api/api/transfer/applyTransfer" + breakLine +
                    "3. IB提交SubIB/Client转账 POST /web-api/api/transfer/apply/sub-ib-transfer" + breakLine +
                    "4. 查询Rebate账户余额 POST /web-api/api/home/queryAvailableBalance" + breakLine +
                    "5. 查询IB转账权限 GET /web-api/api/transfer/eligible-transfer" + breakLine +
                    "6. IB Rebate&Trading账户数据 POST /web-api/api/transfer/toApplyTransferView" + breakLine +
                    "7. 下载转账表单 GET /web-api/api/transfer/download-transfer-template" + breakLine +
                    "8. 查询SubIB数据 POST /web-api/api/transfer/sub-ib/info" + breakLine +
                    "9. IB Rebate账户数据 POST /web-api/api/transfer/toApplyTransferOthersView" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ IB Account ]

    // region [ Account Registration ]

    public static final String IBACCOUNT_HOMEPAGE_REFERRAL_LINK_REGISTER_CP_UPGRADE_IB =
            "{" + "返佣账号推荐链接进行注册" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 在IBP获取推荐链接" + breakLine +
                    "2. 使用相关返佣账号的推荐链接进行注册账号" + breakLine +
                    "3. 注册交易账号" + breakLine +
                    "3a. AU - 升级新注册账号为IB" + breakLine +
                    "4. 注册用户可以归属到对应IB下" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String IBACCOUNT_REFERRAL_LINK_VERIFICATION =
            "{" + "返佣账号推广链接核实" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 在IBP跳转推广链接页面" + breakLine +
                    "2. 核实推广链接域名" + breakLine +
                    "3. 核实推广短链能跳转正确域名" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String IBACCOUNT_REFERRAL_LINK_REGISTRATION =
            "{" + "返佣账号推广链接注册流程" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 在IBP跳转推广链接页面" + breakLine +
                    "2. 通过Live Account推广链接注册账号及归属到对应返佣账号" + breakLine +
                    "3. 通过Demo Account推广链接注册账号及归属到对应返佣账号" + breakLine +
                    "4. 通过h5推广链接注册账号及归属到对应返佣账号" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

        public static final String IBACCOUNT_CAMPAIGN_LINK =
            "{" + "返佣账号活动链接核实" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 在IBP跳转活动链接页面" + breakLine +
                    "2. 创建活动链接" + breakLine +
                    "3. 通过活动链接注册交易账号" + breakLine +
                    "4. 注册账号及Campaign Source归属到对应返佣账号" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String IBACCOUNT_AGREEMENT =
            "{" + "返佣协议上传及同意" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. Admin端上传返佣协议" + breakLine +
                    "2. 登入IBP并同意协议" + breakLine +
                    "3. 核实协议状态已更新" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String IBPROGRAM_ACCOUNTREGISTRATION =
            "{" + "返佣Program注册新返佣账号" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 通过IB Program注册新IB用户" + breakLine +
                    "2. 审核新IB账号" + breakLine +
                    "3. 确认新IB账号能在IBP显示" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String API_IB_REBATE_AGREEMENT =
            "{" + "使用接口核实协议状态：待签约" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 调用api/rebate/agreement/account" + breakLine +
                    "2. 核实状态为 1 (待签约)" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // endregion

    // region [ IB Report ]

    public static final String IB_REBATE_REPORT =
            "{" + "佣金报告查询" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 在IBP首页点击进入IB报告" + breakLine +
                    "2. 点击佣金报告" + breakLine +
                    "3. 查看总返佣金及各个产品返佣金值" + breakLine +
                    "4. 查看返佣记录日期是否存在" + breakLine +
                    "5. 查看返佣记录账号是否存在" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String IB_ACCOUNT_REPORT =
            "{" + "客户管理页面查询" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 在IBP首页点击进入客户管理" + breakLine +
                    "2. 点击客户管理及查看记录是否存在" + breakLine +
                    "3. 点击未入金客户及查看记录是否存在" + breakLine +
                    "4. 点击已开账户数及查看记录是否存在" + breakLine +
                    "5. 点击待审核客户及查看记录是否存在" + breakLine +
                    "6. 点击潜在客户及查看记录是否存在" + breakLine +
                    "7. 点击归档客户及查看记录是否存在" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ CPA UI ]

    public static final String DAP_CPA_Registration =
            "{" + "CPA注册流程" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. CPA注册流程" + breakLine +
                    "2. 同步注册一个client账号" + breakLine +
                    "3. CPA和Client的sales走自动分配逻辑" + breakLine +
                    "4. client账户监管分配正确" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String DAP_CPA_Login_Logout =
            "{" + "CPA登入+登出" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. CPA登入" + breakLine +
                    "2. CPA登出" + breakLine +
                    "3. 检查登出后的登入页面语言一致" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";


    // endregion

    // region [ APP API Account]

    private static final String APPAPIACC_PROCESSFLOW_GLOBAL_DESC = "暂无描述";

    // Account Registration
    public static final String APPAPIACC_REGISTER_MT4 = "{" + accountTag + "MT4 Live Account Registration" + "},{" + "Create live account set with Standard Account Type and USD currency." + "}";
    public static final String APPAPIACC_REGISTER_DEMO_MT4 = "{" + accountTag + "MT4 Demo Account Registration" + "},{" + "Create demo account set with Standard Account Type and USD currency." + "}";

    // Additional Account Registration
    public static final String APPAPIACC_ADDITIONAL_ACC = "{" + accountTag + "MT4 Additional Live Account" + "},{" + "Create an additional account set with Standard Account Type and USD currency." + "}";

    // endregion

    // region [ APP API Deposit ]

    // Process Flow Global Description
    private static final String APPAPIDEPOSIT_PROCESSFLOW_GLOBAL_DESC = "暂无描述";

    // E-Wallet

    // Cryptocurrency
    public static final String APPAPIDEPOSIT_CRYPTO_USDT_TRC20 = "{" + depositTag + "Japan Bank Transfer / USDT-TRC20" + "},{" + APPAPIDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";

    // Credit Card
    public static final String APPAPIDEPOSIT_CC = "{" + depositTag + "Credit Card" + "},{" + APPAPIDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";

    // Local Bank Transfer (LBT)
    public static final String APPAPIDEPOSIT_JAPAN_BT = "{" + depositTag + "Japan Bank Transfer" + "},{" + APPAPIDEPOSIT_PROCESSFLOW_GLOBAL_DESC + "}";

    // Offline Transfer

    // endregion

    // region [ APP API Withdraw ]

    // Process Flow Global Description
    private static final String APPAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC = "暂无描述";

    // E-Wallet

    // Cryptocurrency
    public static final String APPAPIWITHDRAW_CRYPTO_USDT_TRC20 = "{" + withdrawalTag + "USDT-TRC20" + "},{" + APPAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";

    // Credit Card
    public static final String APPAPIWITHDRAW_CC = "{" + withdrawalTag + "Credit Card" + "},{" + APPAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";

    // Local Bank Transfer (LBT)
    public static final String APPAPIWITHDRAW_JAPAN_BT = "{" + withdrawalTag + "Japan Bank Transfer / Malaysia Bank Transfer" + "},{" + APPAPIWITHDRAW_PROCESSFLOW_GLOBAL_DESC + "}";

    // Offline Transfer

    // endregion

    // region [ Admin External User ]

    // region [ IB Account Registration ]

    public static final String ADMIN_CREATE_IB_EXT_USER =
            "{" + "外部用户创建IB及账号审核" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 在admin外部用户管理列表创建IB" + breakLine +
                    "2. 审核相关的IB账号" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String ADMIN_UPGRADE_CLIENT_TO_IB =
            "{" + "Admin端升级客户到IB" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 在admin的Rebate Account查找相关的IB账号" + breakLine +
                    "2. 查找使用IB链接创建的client" + breakLine +
                    "3. 确保相关client已经过了POI, POA及账号审核" + breakLine +
                    "4. 升级相关的Client为IB" + breakLine +
                    "5. 审核相关的IB账号" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String ADMIN_CONFIGURE_IB_COMMISSION_RULES =
            "{" + "IB佣金规则配置" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 在admin的Rebate Account查找相关的账号" + breakLine +
                    "2. 配置佣金规则" + breakLine +
                    "3. 保存佣金规则" + breakLine +
                    "4. 返回配置佣金规则页面，确保佣金规则成功保存" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";
    // endregion

    // endregion

    // region [ Admin API Payment ]
    public static final String CPDEPOSIT_Callback = "{" + depositTag + "Deposit Callback" + "},{" + CPDEPOSIT_CALLBACK_DESC + "}"; ;

    public static final String CPDEPOSIT_BridgePayWithCallback = "{" + depositTag + "Credit/Debit Card With CallBack" + "},{" + CPDEPOSIT_BridgePayWithCALLBACK_DESC + "}"; ;

    public static final String APAPIPAYMENT_DEPOSITAUDIT    =
            "{" + "Deposit Audit Page" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. Deposit Audit Search" + breakLine +
                    "2. Deposit Audit Approve" + breakLine +
                    "3. Deposit Audit Update Status" + breakLine +
                    "4. Deposit Audit Pending" + breakLine +
                    "5. Deposit Audit Reject" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String APAPIPAYMENT_WITHDRAWAUDIT =
            "{" + "Withdrawal Audit Page" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. Withdrawal Audit - Search" + breakLine +
                    "2. Withdrawal Audit - Get Daily Cap Amount" + breakLine +
                    "3. Withdrawal Audit - Get Record's Claim Status" + breakLine +
                    "4. Withdrawal Audit - Claim Record" + breakLine +
                    "5. Withdrawal Audit - Pending Record" + breakLine +
                    "6. Withdrawal Audit - Reverse/Reject Record" + breakLine +
                    "7. Withdrawal Audit - Approve Record" + breakLine +

                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String APAPIPAYMENT_DPWDREPORT  =
            "{" + "Deposit/Withdrawal Report Page" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. Deposit/Withdrawal Report Search" + breakLine +
                    "2. Deposit/Withdrawal Report Statistic" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String APAPIPAYMENT_FINANCIALINFOAUDIT  =
            "{" + "Financial Information Audit Page" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. Financial Information Audit - Search Unionpay" + breakLine +
                    "2. Financial Information Audit - Reject Unionpay" + breakLine +
                    "3. Financial Information Audit - Approve Unionpay" + breakLine +
                    "4. Financial Information Audit - Search Credit Card" + breakLine +
                    "5. Financial Information Audit - Disable Credit Card" + breakLine +
                    "6. Financial Information Audit - Enable Unionpay" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String APAPIPAYMENT_ACCTRANSFERAUDIT  =
            "{" + "Account Transfer Audit Page" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. Account Transfer Audit - Search" + breakLine +
                    "2. Account Transfer Audit - Get From Account Info" + breakLine +
                    "3. Account Transfer Audit - Add IB Transfer Record" + breakLine +
                    "4. Account Transfer Audit - Approve/Reject Record" + breakLine +
                    "5. Account Transfer Audit - Bulk Reject Record" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String APAPIPAYMENT_CCTRANSACTIONAUDIT  =
            "{" + "Credit Card Transaction Audit Page" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. CC Transaction Audit - Search" + breakLine +
                    "2. CC Transaction Audit - Insert Adjustment" + breakLine +
                    "3. CC Transaction Audit - Bulk Adjustment" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String APAPIPAYMENT_CCARCHIVEAUDIT  =
            "{" + "Credit Card Archive Audit Page" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. CC Archive Audit - Search" + breakLine +
                    "2. CC Archive Audit - Pending Record" + breakLine +
                    "3. CC Archive Audit - Reject Record" + breakLine +
                    "4. CC Archive Audit - Approve Record" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String APAPIPAYMENT_CASHADJUSTMENTAUDIT  =
            "{" + "Cash Adjustment Audit Page" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. Cash Adjustment Audit - Single - Check Account" + breakLine +
                    "2. Cash Adjustment Audit - Single - Check Amount" + breakLine +
                    "3. Cash Adjustment Audit - Single - Check Record" + breakLine +
                    "4. Cash Adjustment Audit - Single - Add Record" + breakLine +
                    "5. Cash Adjustment Audit - Approve Record" + breakLine +
                    "6. Cash Adjustment Audit - Bulk - Upload File" + breakLine +
                    "7. Cash Adjustment Audit - Bulk - Check Record" + breakLine +
                    "8. Cash Adjustment Audit - Bulk - Add Record" + breakLine +
                    "9. Cash Adjustment Audit - Reject Record" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String APAPIPAYMENT_LBTTRANSACTIONAUDIT  =
            "{" + "LBT Transaction Audit Page" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. LBT Transaction Audit - Insert Adjustment" + breakLine +
                    "2. LBT Transaction Audit - Check Account Balance" + breakLine +
                    "3. LBT Transaction Audit - Search" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String APAPIPAYMENT_UNIONPAYWITHDRAWAL  =
            "{" + "Unionpay Withdrawal Page" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. LBT Transaction Audit - Insert Adjustment" + breakLine +
                    "2. LBT Transaction Audit - Check Account Balance" + breakLine +
                    "3. LBT Transaction Audit - Search" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String APAPIPAYMENT_AUTOAUDITSWITCH  =
            "{" + "Auto Audit Switch (Transfer/Deposit) Page" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. Account Transfer Setting Page - Disable Auto Transfer" + breakLine +
                    "2. Account Transfer Setting Page - Enable Auto Transfer" + breakLine +
                    "3. All Deposit Switch Page - Disable All Deposit" + breakLine +
                    "4. All Deposit Switch Page - Enable All Deposit" + breakLine +
                    "5. Unionpay Auto Deposit Page - Disable Unionpay Auto Deposit" + breakLine +
                    "6. Unionpay Auto Deposit Page - Enable Unionpay Auto Deposit" + breakLine +
                    "7. Unionpay Auto Deposit Page - Disable Quick Payment Auto Deposit" + breakLine +
                    "8. Unionpay Auto Deposit Page - Enable Quick Payment Auto Deposit" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String APAPIPAYMENT_EXCHANGERATE  =
            "{" + "Exchange Rate Relevant Page" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. Exchange Rate Limit Page - Get Currency Pair" + breakLine +
                    "2. Exchange Rate Limit Page - Get Exchange Rate Limit" + breakLine +
                    "3. Exchange Rate Setting Page - Update Deposit Exchange Rate" + breakLine +
                    "4. Exchange Rate Setting Page - Update Withdrawal Exchange Rate" + breakLine +
                    "5. Exchange Rate Setting Page - Get Current Exchange Rate" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String APAPIPAYMENT_CACHEMANAGEMENT  =
            "{" + "Payment Cache Management Page" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. Withdraw Type - Refresh" + breakLine +
                    "2. Withdraw Type - Search Result" + breakLine +
                    "3. Portal Withdraw Type - Refresh" + breakLine +
                    "4. Portal Withdraw Type - Search Result" + breakLine +
                    "5. Withdraw Channel - Refresh" + breakLine +
                    "6. Withdraw Channel - Search Result" + breakLine +
                    "7. Deposit Type - Refresh" + breakLine +
                    "8. Deposit Type - Search Result" + breakLine +
                    "9. Deposit Channel - Refresh" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String APAPIPAYMENT_DAVINCI =
            "{" + "Davinci Report Page" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. Davinci Deposit/Withdrawal Report - Withdrawal Method API" + breakLine +
                    "2. Davinci Deposit/Withdrawal Report - Withdrawal Status API" + breakLine +
                    "3. Davinci Deposit/Withdrawal Report - Deposit Method API" + breakLine +
                    "4. Davinci Deposit/Withdrawal Report - Deposit Status API" + breakLine +
                    "5. Davinci Deposit/Withdrawal Report - Account Type API" + breakLine +
                    "6. Davinci Deposit/Withdrawal Report - Account Currency API" + breakLine +
                    "7. Davinci Deposit/Withdrawal Report - Country API" + breakLine +
                    "8. Davinci Deposit/Withdrawal Report - Search API" + breakLine +
                    "9. Davinci Deposit/Withdrawal Report - Summary API" + breakLine +
                    "10. Davinci First Time Deposit Report - Search API" + breakLine +
                    "11. Davinci Dep/Withdraw Daily Report - Search API" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ CP Copy Trading ]

    public static final String CPCOPYTRADING =
            "{" + "Copy Trading" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击 Copy Trading - Discover." + breakLine +
                    "3. 搜索指定策略账号，提交copy申请" + breakLine +
                    "4. 点击 Copy Trading - Copier" + breakLine +
                    "5. 检查信息卡片的名称以及position列表信息" + breakLine +
                    "6. 点击 Copy Trading - Signal Provider" + breakLine +
                    "7. 选择提交跟随的账号，检查账号资产信息" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPCOPYTRADING_EUR_EquivalentMargin =
            "{" + "Copy Trading EUR-USD" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击 Copy Trading - Discover." + breakLine +
                    "3. 搜索指定策略账号(EUR)，copy mode选择 Equivalent Used Margin, 提交copy申请" + breakLine +
                    "4. 点击 Copy Trading - Copier" + breakLine +
                    "5. 检查信息卡片的名称以及position列表信息" + breakLine +
                    "6. 点击 Copy Trading - Signal Provider" + breakLine +
                    "7. 选择提交跟随的账号(USD)，检查账号资产信息" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPCOPYTRADING_EUR_FixLots =
            "{" + "Copy Trading EUR-USD" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击 Copy Trading - Discover." + breakLine +
                    "3. 搜索指定策略账号(EUR)，copy mode选择 Fixed Lots, 提交copy申请" + breakLine +
                    "4. 点击 Copy Trading - Copier" + breakLine +
                    "5. 检查信息卡片的名称以及position列表信息" + breakLine +
                    "6. 点击 Copy Trading - Signal Provider" + breakLine +
                    "7. 选择提交跟随的账号(USD)，检查账号资产信息" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPCOPYTRADING_EUR_FixedMultiples =
            "{" + "Copy Trading EUR-USD" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击 Copy Trading - Discover." + breakLine +
                    "3. 搜索指定策略账号(EUR)，copy mode选择 FixedMultiples, 提交copy申请" + breakLine +
                    "4. 点击 Copy Trading - Copier" + breakLine +
                    "5. 检查信息卡片的名称以及position列表信息" + breakLine +
                    "6. 点击 Copy Trading - Signal Provider" + breakLine +
                    "7. 选择提交跟随的账号(USD)，检查账号资产信息" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPCOPYTRADING_OPENACCOUNT =
            "{" + "CopyTrading Register" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 进入账户注册页面" + breakLine +
                    "1.1 模拟注册网址 " + GlobalProperties.ALPHAURL + breakLine +
                    "2. 提交注册资料 POST /hgw/user-api/bsn/registration/register" + breakLine +
                    "3. 跳转至CP，进行个人信息验证操作" + breakLine +
                    "4. 邮箱OTP验证" + breakLine +
                    "6. 绑定手机号码" + breakLine +
                    "6. 手机号OTP验证" + breakLine +
                    "7. 提交个人信息" + breakLine +
                    "8. 个人信息验证验证成功后，点击补全账号按钮，进行账户配置操作" + breakLine +
                    "9. 提交真实账户配置（默认MTS交易平台、Standard STP账户类型、USD货币类型）" + breakLine +
                    "10. 在首页点击身份验证立即验证按钮，进行身份验证操作" + breakLine +
                    "11. 身份验证（上传身份证明文件）" + breakLine +
                    "12. 根据身份验证页面（人工或Sumsub）进行验证" + breakLine +
                    "13. 完成开户流程" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPCOPYTRADING_DISCOVER =
            "{" + "CopyTrading Discover" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击 Copy Trading - Discover" + breakLine +
                    "3. Recommendation - 检查Highest Annual Return/Low Risk And Stable Return/High Win Rate" + breakLine +
                    "4. Highest Annual Return选中第一个卡片，点击卡片标签，检查跳转信号源详情页正常" + breakLine +
                    "5. Highest Annual Return选中第一个卡片，点击卡片copy标签，弹窗展示自主交易账号信息正常" + breakLine +
                    "6. 点击Rank List，检查信息展示正常" + breakLine +
                    "7. 点击第四个策略信息中的view，检查跳转信号源详情正常" + breakLine +
                    "8. 点击view more，检查展示正常" + breakLine +
                    "9. 点击搜索框，检查搜索框弹出" + breakLine +
                    "10. search strategies - 策略展示正常" + breakLine +
                    "11. 模糊搜索search strategies" + breakLine +
                    "12. 点击策略信息，检查跳转信号源详情页正常" + breakLine +
                    "13. search signal provider - 信号源展示正常" + breakLine +
                    "14. 模糊搜索signal provider" + breakLine +
                    "15. 点击策略信息，检查跳转信号源详情页正常" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPCOPYTRADING_DISCOVER_DETAIL =
            "{" + "CopyTrading Discover Detail" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击 Copy Trading - Discover" + breakLine +
                    "3. 点击Rank List" + breakLine +
                    "4. 点击第四个策略信息中的view，检查跳转信号源详情正常,如果没有第四个策略信息就点击第一个策略信息" + breakLine +
                    "5. 切换到Overview选项卡" + breakLine +
                    "6. 选择不同天数可以正常切换且数据能正常加载" + breakLine +
                    "7. Strategy Rating/Max drawdown选项卡可正常切换且数据能正常加载" + breakLine +
                    "8. Trading Analysis下 Return / Strategy Equity选项卡可正常切换且数据能正常加载" + breakLine +
                    "9. Daily/Weekly/Monthly 选项卡可正常切换且数据能正常加载" + breakLine +
                    "10. Daily PnL/Trades Stats 选项卡可正常切换且数据能正常加载" + breakLine +
                    "11. 切换Order选项卡,验证Position/History 选项卡可正常切换且数据能正常加载" + breakLine +
                    "12. 切换Copiers选项卡, 验证Copiers列表可正常显示" + breakLine +
                    "13. 切换Watchers选项卡, 验证Copiers列表可正常显示" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPCOPYTRADING_POSITION =
            "{" + "CopyTrading Position" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击 Copy Trading - Copier" + breakLine +
                    "3. 从账号列表中选择账号" + breakLine +
                    "4. 检查是否有position，如果已有position停止跟单" + breakLine +
                    "5. 点击 Copy Trading - Discover" + breakLine +
                    "6. 选择信号源跟单" + breakLine +
                    "7. 检查position列表" + breakLine +
                    "8. 检查position detail的balance和equity是否是数值" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPCOPYTRADING_SIGNALPROVIDER =
            "{" + "CopyTrading Signal Provider" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击 Copy Trading - SignalProvider" + breakLine +
                    "3. 从账号列表中选择账号" + breakLine +
                    "4. 选择Strategies - Public" + breakLine +
                    "5. 选择Delist,检查public的信号源变成delisted" + breakLine +
                    "6. 选择Strategies - Delisted" + breakLine +
                    "7. 选择public,检查delisted的信号源变成public" + breakLine +
                    "8. 点击profit sharing statement,验证跳转到summary正常" + breakLine +
                    "9. 点击strategy homepage,验证跳转并且数据展示正常" + breakLine +
                    "10. SignalProvider - Position,检查position显示正确" + breakLine +
                    "11. SignalProvider - History,选择某一时间范围,检查history显示正确" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPCOPYTRADING_SUBSIGNALPROVIDER =
            "{" + "CopyTrading Sub Signal Provider" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击 Copy Trading - SignalProvider" + breakLine +
                    "3. 从账号列表中选择账号" + breakLine +
                    "4. 增加副策略,验证发布成功" + breakLine +
                    "5. 检查策略中显示被激活的offer数量" + breakLine +
                    "6. 修改副策略,验证发布成功之后策略信息展示正确" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPCOPYTRADING_SIGNALPROVIDER_COPIER_REVIEW =
            "{" + "CopyTrading Signal Provider" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击 Copy Trading - SignalProvider" + breakLine +
                    "3. 点击Copier Review,检查跳转到Copier Review tab" + breakLine +
                    "4. 下拉框选择Approved,检查approved页面" + breakLine +
                    "5. 下拉框选择Pending,检查pending页面" + breakLine +
                    "6. 下拉框选择Rejected,检查rejected页面" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPCOPYTRADING_SIGNALPROVIDER_AGENT_LINK =
            "{" + "CopyTrading Signal Provider" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击 Copy Trading - SignalProvider" + breakLine +
                    "3. 点击Copier Review,检查跳转到Agent Link tab" + breakLine +
                    "4. 选择pending,检查pending页面" + breakLine +
                    "5. 选择approve,检查approve页面" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPCOPYTRADING_DETAIL=
            "{" + "CopyTrading Position" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击 Copy Trading - Copier" + breakLine +
                    "3. 检查是否有position，如果已有position就开始检查，没有就去discover page跟单" + breakLine +
                    "4. 点击position的detail" + breakLine +
                    "5. 订单详情页点击manage" + breakLine +
                    "6. 检查下拉菜单：add funds, remove funds, pause copying, stop copy, pause/resume copy, more settings" + breakLine +
                    "7. 点击add funds，检查追加金额页面信息展示正常" + breakLine +
                    "8. 点击remove funds，检查减少金额页面信息展示正常" + breakLine +
                    "9. 点击pause copy，检查暂停跟单唤起二次确认框，功能正常" + breakLine +
                    "10. 点击resume copy，检查暂停跟单唤起二次确认框，功能正常" + breakLine +
                    "11. 点击more setting，页面信息展示正常" + breakLine +
                    "12. 点击stop copy，停止跟单" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPCOPYTRADING_MANAGEMENU=
            "{" + "CopyTrading Manage Menu" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击 Copy Trading - Copier" + breakLine +
                    "3. 检查是否有position，如果已有position就开始检查，没有就去discover page跟单" + breakLine +
                    "4. 点击position的manage" + breakLine +
                    "5. 检查下拉菜单：add funds, remove funds, pause copying, stop copy, pause/resume copy, more settings" + breakLine +
                    "6. 点击add funds，检查追加金额页面信息展示正常" + breakLine +
                    "7. 点击remove funds，检查减少金额页面信息展示正常" + breakLine +
                    "8. 点击pause copy，检查暂停跟单唤起二次确认框，功能正常" + breakLine +
                    "9. 点击resume copy，检查暂停跟单唤起二次确认框，功能正常" + breakLine +
                    "10. 点击more setting，页面信息展示正常" + breakLine +
                    "11. Update跟随模式: 等比例占用保证金,修改成功" + breakLine +
                    "10. Update跟随模式: 固定手数模式,修改成功" + breakLine +
                    "10. Update跟随模式: 固定手数倍数模式,修改成功" + breakLine +
                    "11. Copier页面history，订单信息卡片列表" + breakLine +
                    "12. History订单详情，点击订单卡片detail，跳转到订单详情页" + breakLine +
                    "13. 点击stop copy，停止跟单" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPCOPYTRADING_HISTORY=
            "{" + "CopyTrading Copier Detail" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击 Copy Trading - Copier" + breakLine +
                    "3. 检查是否有position，如果已有position就开始检查，没有就去discover page跟单" + breakLine +
                    "4. 取消跟单" + breakLine +
                    "5. Copier页面history，订单信息卡片列表" + breakLine +
                    "6. History订单详情，点击订单卡片detail，跳转到订单详情页" + breakLine +
                    ". 点击stop copy，停止跟单" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";
    // endregion

    // region [ CP Web Trading ]

    public static final String CPWEBTRADING_PLACEORDER =
            "{" + "Web Trading - 持仓单 buy/sell" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击 Trading " + breakLine +
                    "3. 选择产品 AUDUSD" + breakLine +
                    "4. 下Buy/Sell单" + breakLine +
                    "5. 检查下单前端和ws提示信息" + breakLine +
                    "5. 检查position信息，确认下单成功" + breakLine +
                    "6. 修改持仓TP/SL信息（异常值）" + breakLine +
                    "7. 检查异常值导致修改失败，有报错信息" + breakLine +
                    "8. 修改持仓TP/SL信息（正常值）" + breakLine +
                    "9. 检查修改持仓成功，TP/SL已经修改" + breakLine +
                    "10. 平仓" + breakLine +
                    "11. 检查平仓成功前端和ws提示信息" + breakLine +
                    "12. 检查仓位关闭" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPWEBTRADING_PENDINGORDER_STOPLIMIT =
            "{" + "Web Trading - 挂单 - Buy/Sell stop limit" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击 Trading " + breakLine +
                    "3. 选择产品 AUDUSD" + breakLine +
                    "4. 下Buy/Sell stop limit单（异常值）" + breakLine +
                    "5. 检查有错误信息以及下挂单没有成功" + breakLine +
                    "6. 下Buy/Sell stop limit 单（正常值）" + breakLine +
                    "7. 检查Buy/Sell stop limit挂单成功" + breakLine +
                    "8. 取消挂单，验证取消成功" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPWEBTRADING_PENDINGORDER_STOP =
            "{" + "Web Trading - 挂单 - Buy/Sell stop " + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击 Trading " + breakLine +
                    "3. 选择产品 AUDUSD" + breakLine +
                    "4. 下Buy/Sell stop 单（异常值）" + breakLine +
                    "5. 检查有错误信息以及下挂单没有成功" + breakLine +
                    "6. 下Buy/Sell stop单（正常值）" + breakLine +
                    "7. 检查Buy/Sell stop 挂单成功" + breakLine +
                    "8. 取消挂单，验证取消成功" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String CPWEBTRADING_PENDINGORDER_LIMIT =
            "{" + "Web Trading - 挂单 - Buy/Sell limit" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击 Trading " + breakLine +
                    "3. 选择产品 AUDUSD" + breakLine +
                    "4. 下Buy/Sell limit单（异常值）" + breakLine +
                    "5. 检查有错误信息以及下挂单没有成功" + breakLine +
                    "6. 下Buy/Sell limit单（正常值）" + breakLine +
                    "7. 检查Buy/Sell limit 挂单成功" + breakLine +
                    "8. 取消挂单，验证取消成功" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";
    public static final String CPWEBTRADING_POSITION_CLOSE_ALL =
            "{" + "Web Trading - 全部平仓" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击 Trading " + breakLine +
                    "3. 选择产品 AUDUSD" + breakLine +
                    "4. 下Buy/Sell单" + breakLine +
                    "5. 检查下单前端和ws提示信息" + breakLine +
                    "6. 检查position信息，确认下单成功" + breakLine +
                    "7. 检查仓位关闭" + breakLine +
                    "8. 检查 Position History" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";
    public static final String CPWEBTRADING_POSITION_CLOSE_BY =
            "{" + "Web Trading - 互抵平仓" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击 Trading " + breakLine +
                    "3. 选择产品 AUDUSD" + breakLine +
                    "4. 下Buy/Sell单" + breakLine +
                    "5. 检查下单前端和ws提示信息" + breakLine +
                    "6. 检查position信息，确认下单成功" + breakLine +
                    "7. 检查仓位关闭" + breakLine +
                    "8. 检查 Position History" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";
    public static final String CPWEBTRADING_POSITION_REVERSE =
            "{" + "Web Trading - 反向开仓" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击 Trading " + breakLine +
                    "3. 选择产品 AUDUSD" + breakLine +
                    "4. 下Buy/Sell单" + breakLine +
                    "5. 检查下单前端和ws提示信息" + breakLine +
                    "6. 检查position信息，确认下单成功" + breakLine +
                    "7. 检查仓位关闭" + breakLine +
                    "8. 检查 Position History" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";
    public static final String CPWEBTRADING_POSITION_WSS =
            "{" + "Web Trading - 查看行情" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. 登入CP" + breakLine +
                    "2. 点击 Trading " + breakLine +
                    "3. 选择产品 AUDUSD" + breakLine +
                    "4. 下Buy/Sell单" + breakLine +
                    "5. 检查下单前端和ws提示信息" + breakLine +
                    "6. 检查position信息，确认下单成功" + breakLine +
                    "7. 检查仓位关闭" + breakLine +
                    "8. 检查 Position History" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";
    // endregion

    // endregion

    // region [ Admin API User / Account ]

    public static final String APAPIACCOUNT_TRADINGACCT =
            "{" + "Trading Account Page" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. Trading Account Search" + breakLine +
                    "2. Trading Account Update" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String APAPIACCOUNT_REBATEACCT  =
            "{" + "Rebate Account Page" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. Rebate Account Search" + breakLine +
                    "2. Rebate Account Update" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String APAPIACCOUNT_USERS  =
            "{" + "Users Relevant Page" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. Client Search" + breakLine +
                    "2. External User Search" + breakLine +
                    "3. Lead User Search" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ Admin API Campaign / Loyalty Program / Campaign Setting ]

    // Process Flow Global Description

    public static final String APAPICAMPAIGN_SEARCHALLPAGE =
            "{" + "Admin - Campaign Management API" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1.  API login Admin" + breakLine +
                    "2.  Database搜索此用户参与活动名称和参加状态 GET /campaign/admin/databaseSearch/user/participationInfo" + breakLine +
                    "3.  Database搜索此用户入金记录与获取的赠金额 GET /campaign/admin/databaseSearch/user/operationRecord" + breakLine +
                    "4.  Database搜索此用户被加入黑名单的记录 GET /campaign/admin/databaseSearch/depositBonus/transaction" + breakLine +
                    "5.  搜索用户的参加状态和获得赠金的记录 GET /campaign/admin/depositBonus/v2/users" + breakLine +
                    "6.  搜索用户的入金记录和获得赠金的记录 GET /campaign/admin/depositBonus/transaction" + breakLine +
                    "7.  搜索被加入黑名单的用户记录 GET /campaign/admin/blackList" + breakLine +
                    "8. 搜索被加入白名单的用户记录 GET /campaign/admin/whiteList" + breakLine +
                    "9. 搜索活动创建记录和状态 GET /campaign/admin/settings/list" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String APAPICAMPAIGNSETTING_ACTIONS =
            "{" + "Admin - Campaign Setting API" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. API login Admin" + breakLine +
                    "2. 创建活动 POST /campaign/admin/settings/create" + breakLine +
                    "3. 批准活动 POST /campaign/admin/settings/auditCampaignSetting_approve" + breakLine +
                    "4. 修改活动 POST /campaign/admin/settings/edit" + breakLine +
                    "5. 结束活动 POST campaign/admin/settings/end" + breakLine +
                    "6. 查看活动 POST /campaign/admin/settings/view" + breakLine +
                    "7. 归档活动 POST /campaign/admin/settings/archive" + breakLine +
                    "8. 恢复归档的活动 POST /campaign/admin/settings/unarchive" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    public static final String APAPILOYALTYPROGRAM_SEARCH =
            "{" + "Admin - Loyalty Program API" + "}," +
                    "{" + detailsStart + summaryAPIDesc + detailsDivStart +
                    "1. API login Admin" + breakLine +
                    "2. 搜索Loyalty Program用户信息&积分 GET /loyalty/admin/users" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ Admin Audit ]

    public static final String APAUDIT_WITHDAWAL =
            "{" + adminTag + "Withdrawal Audit" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. Logon to Admin Portal." + breakLine +
                    "2. Select Task menu." + breakLine +
                    "3. Select Withdrawal Audit menu." + breakLine +
                    "4. Search for specific automation user record." + breakLine +
                    "5. Check first row record's payment status." + breakLine +
                    "5.1 Reverse record if payment has completed." + breakLine +
                    "5.2 Approve record audit if payment in Accepted Status." + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ Admin System Setting ]

    public static final String APSETTING_EMAIL =
            "{" + adminTag + "Check Email Send Status" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "1. Logon to Admin Portal." + breakLine +
                    "2. Select System Setting menu." + breakLine +
                    "3. Select Email Management menu." + breakLine +
                    "4. Search for specific automation user record." + breakLine +
                    "5. Check if the email send status of the first row record is success." + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ GEP / Message Center ]

    public static final String THIRDPARTY_MSGCENTER_PHONEOTP =
            "{" + "三方信息中心 API" + "}," +
                    "{" + detailsStart + summaryDesc + detailsDivStart +
                    "Url: https://share-msg-frontend.app-alpha.com"  + breakLine +
                    "1. 用户登录 /api/auth/oauth2/token" + breakLine +
                    "2. 发送OTP /api/msg/sms/send-otp/v1" + breakLine +
                    "3. 获取国家ID /api/msgAdmin/country/page" + breakLine +
                    "4. 根据手机号获取OTP /api/msgAdmin/statistics/otpRecord/page" + breakLine +
                    detailsDivEnd + breakLine + detailsEnd + "}";

    // endregion

    // region [ Global Functions ]

    public static String generateProcessFlowNotAvaiDesc() {
        return "<div style='margin-left: 28px;'>暂无描述</div>";
    }

    public static String generateMessageLogDesc(Throwable errorCause, boolean bIsLastDesc) {
        if(errorCause == null){
            return "<div style='margin-left: 28px;'>暂无描述</div>";
        }
        return "<details style='margin-left:15px;'>" +
                "<summary>B. View Error details</summary>" +
                "<div style='margin-left: 15px;'><pre>" + StringEscapeUtils.escapeHtml4(errorCause.toString()) + "</pre></div>" +
                (bIsLastDesc ? "" : breakLine)  +
                "</details>";
    }
    public static String generateScreenshotDesc(String base64Screenshot) {
        return "<details style='margin-left:15px;'>" +
                "<summary>C. View screenshot</summary>" +
                "<div style='margin-left: 15px;'>" +
                "<a href='" + base64Screenshot + "' data-featherlight='image'><span class='badge badge-gradient-primary'>base64 img</span></a>" +
                "</div>" +
                "</details>";
    }
    public static final String WALLET_CARD_HOME = "1.卡首页 卡片信息，数据展示正确\n" +
            "2.点击Spending Power右侧的小眼睛，正确显示可用余额\n" +
            "3.Per MAX Payment下方正确展示单笔最高限额\n" +
            "4.点击Deposit，跳转入金页面默认选中USDT\n" +
            "5.点击Paying with，跳转查看卡支付顺序及支付限额\n" +
            "6.点击Manage，跳转卡片管理页面\n" +
            "7.点击Rebates ，跳转返现页面\n" +
            "8.点击Limits，弹出查看限额弹窗\n" +
            "9.点击右上角按钮，跳转历史交易记录页面(仅H5&APP有此按钮)\n" +
            "10.卡首页下方默认展示10条历史交易记录，点击View More跳转历史交易记录页面";

    public static final String WALLET_CARD_DETAILS = "1.查看卡详情成功，展示内容正确：\n" +
            "Card Name:完整的客户姓名\n" +
            "Card Number:完整的卡号\n" +
            "Expire Date :MM/YY\n" +
            "CVV :完整的cvv\n" +
            "2.复制卡号，复制成功，粘贴卡号正确\n" +
            "3.倒计时60S，弹窗关闭";

    public static final String WALLET_CARD_CHANGE_AUTO_CASHBACK = "1、从卡首页点击rebate页面\n" +
            "2、打开：点击自动提取按钮，点击‘I acknowledge’按钮\n" +
            "3、关闭：再次重复点击自动提取按钮，点击‘I acknowledge’按钮";

    public static final String WALLET_CARD_CHANGE_STATUS = "1.进入卡首页-卡管理页面\n" +
            "2.点击Freeze或Unfreeze\n" +
            "3.OTP验证成功";
    public static final String WALLET_CARD_ALL_TRANSACTIONS = "1.卡片筛选框，选择不同卡片\n" +
            "2.状态筛选框，选择不同状态\n" +
            "3.时间控件，选择不同时间\n" +
            "4.筛选器，选择不同收支类型、交易类型、金额、币种、消费类别\n" +
            "5.检查记录列表展示";
    public static final String WALLET_CARD_AUTHORIZATIONS = "1.卡片筛选框，选择不同卡片\n" +
            "2.时间控件，选择不同时间\n" +
            "3.筛选器，选择不同金额、币种、消费类别\n" +
            "4.检查记录列表展示";

    public static final String WALLET_CARD_TRANSACTION_DETAILS = "历史记录-all transaction tab-点击卡消费/退款订单的details";
    public static final String WALLET_HOME_OVERVIEW = "1、模块包括：预估总资产 = 资金账户总资产+理财账户总持仓资产；入金、出金、转账、闪兑；\n" +
            "2.模块包括：\n" +
            "1）按币种：币种简写、币种全称、币种金额、折合为法币的金额和操作（入金按钮、出金按钮、划转按钮、闪兑按钮）\n" +
            "2）按账户：展示资金账户、理财账户各个币种的资产，展示字段有：Funding、 Earn";
    public static final String WALLET_HOME_FUNDING = "1.模块包括：总余额、可用余额、入金按钮、出金按钮、划转按钮、闪兑按钮\n" +
            "2.模块包括：币种图标及缩写、余额、预估金额、可用余额、冻结额和操作（入金按钮、出金按钮、划转按钮、闪兑按钮）";
    public static final String WALLET_HOME_EARN = "1、模块包括：预估总资产、昨日收益、总收益、理财按钮、分析按钮；\n" +
            "2.模块包括：理财持仓的币种简写、持仓金额、预估年利率百分比、总收益；";
    public static final String WALLET_HOME_EARN_HOME = "1.检查总资产模块\n" +
            "2.检查活期理财模块\n" +
            "3.FAQ";
    // endregion

}