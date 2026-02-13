package newcrm.utils.api;

public class HyTechUrl {
    //admin相关接口
    public static String adminLogin = "login/to_login";//admin登录
    public static String userAdd = "individual/add_individual";//admin添加用户
    public static String getPk = "keyPair/pk";//admin的密码加密用的key
    public static String queryUser = "audit/query_user_list";//admin查询用户
    public static String querySameActList = "audit/query_sameAct_list";//admin查询同名账户
    public static String queryIdOrPoa = "user/identityProof/getIdentityProofAuditList";//admin查询id，poa审核列表
    public static String poaAudit = "user/identityProof/addressProofAudit/submit";//admin提交poa审核
    public static String idAudit = "user/identityProof/idProofAudit/submit";//admin提交id审核
    public static String accountAudit = "audit/audit_individual_agree";//admin，账号审核
    public static String auditSameActAgree = "audit/audit_sameAct_agree";//admin，同名账号审核
    public static String tradingAccount = "account/query_accountList";//admin，trading查询列表
    public static String switchRegulator = "admin/switch-regulator";//admin，切换监管
    public static String userIBAdd = "user/add_user";//admin,添加IB
    public static String adminAdd = "admin/admin_add";//admin,添加内部用户
    public static String query_accountList = "account/query_accountList";
    public static String addIBUser = "user/add_user";
    public static String upgradeIB = "individual/upgradeIB";
    public static String information="api/system/information";
    public static String informationURL="/api/system/information";
    public static String telegramlink="cp/api/telegram/binding";
    public static String auditqueryserver="audit/query_server";
    public static String walletchainlist="/wallet/chain/list";
    public static String addCreditAdjustment="/api/account/credit/adjustment/adjustmentAddSubmit";
    public static String searchCreditAdjustment="/api/account/credit/adjustment/query_adjustmentList_V2";
    public static String submitCreditAdjustment="/api/account/credit/adjustment/submitAdjustBulkApprove";
    public final static String LBT_ADDLBTBALANCE_BYCURRENCY ="/payment/lbt/insertAdjustmentDetails";
    public static String queryDepositAudit="/payment/deposit/query_PaymentDepositListSimple";

    //owsadmin相关接口
    public static String owsadminLogin = "/api/login";//admin登录
    public static String owsadminLogout ="/api/logout";//admin退出登录
    public static String owsadmintaskNum = "/api/identityProof/task/number";//task number
    public static String owsadminbrandRegulate = "/api/user/currUser/brandRegulate";//brandRegulate
    public static String identityProofList= "/api/identityProof/task/list";//kyc to-do list
    public static String kycidentityProofRecords="/api/identityProof/clientList";//kyc records
    public static String identityProofView="/api/identityProof/view";//查询POI&POA详细页面
    public static String identityProofcrete="/api/identityProof/create"; //创建poi&poa
    public static String getInfo="/api/getInfo";//用户信息
    public static String getRouters="/api/getRouters";//用户路由
    public static String auditidentityProof="/api/identityProof/audit";//identityProof审核状态
    public static String auditType="/api/identityProof/idProof/idType";
    public static String nationalities="/api/identityProof/idProof/nationalities";
    public static String identityProofcomplete="/api/identityProof/id/complete";
    public static String identityProofSave="/api/identityProof/id/save";
    public static String identityProofPending="/api/identityProof/id/pending";
    public static String pendingReasons="/api/identityProof/pendingReasons";
    public static String POINumlist="/api/identityProof/task/list";
    public static String POANumlist="/api/identityProof/task/list";


    //中台支付后端相关接口
    public static String findChannel="/api/v1/channel_merchant/findChannel";
    public static String data_source_type="/api/admin/v1/options/find/data_source_type";
    public static String payment_field="/api/admin/v1/payment_field/3";
    public static String channels = "/api/admin/v1/channels/find";
    public static String findBrandAndRegulator="/api/v1/channel_merchant/findBrandAndRegulator";
    public static String channel_statu="/api/admin/v1/options/find/channel_status";
    public static String channel_categor="/api/admin/v1/options/find/channel_category";
    public static String transaction_type="/api/admin/v1/options/find/transaction_type";
    public static String is_attach_variabl="/api/admin/v1/options/find/is_attach_variable";
    public static String merchant="/api/admin/v1/options/db/merchant";
    public static String channelConfigStatu="/api/admin/v1/options/find/channelConfigStatus";
    public static String display_field_type="/api/admin/v1/options/find/display_field_type";
    public static String item_source="/api/admin/v1/options/find/item_source";
    public static String value_sourc="/api/admin/v1/options/find/value_source";
    public static String personal_config="/api/admin/v1/options/find/personal_config";
    public static String cashier_channel_merchant_rule_type="/api/admin/v1/options/find/cashier_channel_merchant_rule_type";
    public static String general_setting="/api/admin/reg_channel_config/general_setting";
    public static String country_setting="/api/admin/reg_channel_config/country_setting";
    public static String other_setting="/api/admin/reg_channel_config/other_setting";
    public static String config="/api/internal/config";
    public static String langs="/api/admin/v1/translation/langs";
    public static String translation_key="/api/admin/v1/translation/keys";
    public static String contents="/api/admin/v1/translation/contents";
    public static String attrs="/api/admin/v1/translation/contents/attrs";
    public static String querychannels="/api/admin/v1/channels/1";
    public static String updateChannelSource="/api/admin/v1/channel_source/updateChannelSource";
    public static String channel_merchant="/api/v1/channel_merchant/find";
    public static String reg_channel_config="/api/admin/reg_channel_config";
    public static String quota_config="/api/admin/v1/quota-config/merchant/find";
    public static String quota_config_Detail="/api/admin/v1/quota-config/merchant/detail";
    public static String quota_config_update="/api/admin/v1/quota-config/merchant/categorySort/update";
    public static String quota_config_sortLogic_update="/api/admin/v1/quota-config/merchant/sortLogic/update";
    public static String paymentLogin="/api/user/login";
    public static String getcountry="/api/admin/v1/options/db/country";
    public static String merchant_country="/api/admin/v1/options/db/country";
    public static String getChannelSetting="/api/admin/v1/BrandRegulatorCountry/getChannelSetting";
    public static String find_restriction="/api/admin/v1/merchant_country_currency/find_restriction";
    public static String paymentlogout="/api/user/logout";
    public static String groupfind="/api/admin/v1/group/find";
    public static String groupRestriction="/api/admin/v1/groupRestriction/details";
    public static String groupChannelCategorySort="/api/admin/v1/groupChannelCategorySort/find";
    public static String cashierGroupChannel="/api/admin/v1/cashierGroupChannel/details";
    public static String spicial_rule="/api/admin/v1/special_rule/find";
    public static String channel_merchantadd="/api/v1/channel_merchant/add";
    public static String merchant_sortLogic="/api/admin/v1/quota-config/merchant/sortLogic/0/1";
    public static String findBrandList="/api/admin/v1/merchant/findBrandList";
    public static String getExistingUserIds="/api/admin/v1/group/getExistingUserIds";
    public static String special_ruleSort="/api/admin/v1/special_rule/categorySort/0/4";
    public static String special_rule_sortLogic="/api/admin/v1/special_rule/sortLogic/0/4";

    //payroll
    public static String pcsPaymentUrl="https://pcs-au.eks.crm-beta.com";
    //https://pcs.nextpaymenthub.com/api/cashier/checkout
    public static String nextpaymenthub="/api/cashier/checkout";
    //briager提交 /api/cashier/v1/deposit/channel-forwarder
    public static String channel_forwarder="/api/cashier/v1/deposit/channel-forwarder";
    //cp/api/deposit/init_deposit 初始化入金
    public static final String INIT_DEPOSIT = "hgw/rw/payment-api/cp/api/deposit/init_deposit";
    //选择渠道 /api/cashier/checkout
    public static String cashier_checkout="/api/cashier/checkout";
    //选择渠道cyrpto  /api/cashier/checkout/crypto
    public static String cashier_crypto="/api/cashier/checkout/crypto";
    //提交入金 /api/cashier/v1/deposit/crypto
    public static String deposit_crypto="/api/cashier/v1/deposit/crypto";
    //cps提交 /api/cashier/v1/deposit/cps
    public static String deposit_cps="/api/cashier/v1/deposit/cps";

    //非admin接口
    public static String get_demo_data="https://new.vantagefx.com/get_demo_data_0805.php?v=2024073052486";
    public static String get_demo_form="https://new.vantagefx.com/get_demo_form_0805.php?v=2024073052486";
    public static String registerPhp = "https://testregmarkets:147258@testreg.marketsdata1.com/data/get_live_form_v2.php?v=2024071219178";//获取入参
    public static String registerV2 = "web-api/api/registrationV2/register";//获取入参
    public static String register = "web-api/api/registration/register";//获取入参
    public static String login_register = "/web-api/api/login/to_login_register";
    public static String registerProcess = "web-api/api/verificationV2/process";//用户信息补充
    public static String registerProcessV1 = "web-api/api/verification/process";//用户信息补充
    public static String logout = "web-api/api/login/to_logout?userId=10065173";
    public static String Prelogout = "web-api/api/login/to_logout?userId=2321078";
    public static String to_login_by_token = "web-api/api/login/to_login_by_token";
    public static String userFileUploads = "web-api/api/file/uploads";//上传文件
    public static String updateUserIdAddress = "web-api/cp/api/identityProof/updateUserIdAddress";//身份证信息修改
    public static String cpLogin = "/hgw/auth-api/login/customer/pc/to_login";//cp端用户登录
    public static String verificationV2 = "web-api/cp/api/identityProof/verificationV2";//poa信息修改
    public static String applyAdditionalAccountURL = "web-api/cp/api/account/applyAdditionalAccount";//创建同名账户
    public static String createDemoAccount = "web-api/cp/api/account/createDemoAccount";//创建demo账户
    public static String setlanguage = "web-api/api/userProfile/setlanguage";
    public static String updateLeverageDemo = "web-api/cp/api/home/update-leverage-demo";
    public static String getDemoAccs = "hgw/account-api/bsn/account/getWebHomeAccountDemos";
    public static String verifyMethod = "web-api/api/personalDetail/verifyMethod?forceLegacy=true";
    public static String query_mt_accounts = "web-api/cp/api/deposit/query_mt_accounts";//查询账户号
    public static String bridgepay = "web-api/cp/api/deposit/bridgepay";//信用卡入金
    public final static String ANTI_REUSE = "hgw/rw/payment-api/api/token/anti-reuse";//出入金转账,获取token
//    public static final String FT_WALLET_EXCHGRATE = "hgw/rw/payment-api/cp/api/transfer/getWalletExchangeRate";
    public static final String FT_CURRENCYLIST = "hgw/r/payment-api/cp/api/transfer/currencyList";
    public static final String FT_CURRENCYRATE = "hgw/rw/payment-api/cp/api/transfer/currencyRate";
    public static final String FT_DEDUCTCRED = "hgw/rw/payment-api/cp/api/withdrawal/getDeductCredit";
    public final static String FT_APPLY = "hgw/rw/payment-api/cp/api/transfer/applyTransfer_cp"; //提交转账
    public final static String FT_GET_DATA = "/hgw/r/payment-api/cp/api/transfer/getTransferData_cp"; // 转账操作，获取账号
    public final static String FT_PERMISSION = "hgw/r/payment-api/cp/api/transfer/transferPermission"; // 检查转账许可
    public final static String FT_BLACKLIST = "hgw/r/payment-api/cp/api/transfer/queryBlacklist"; //
    public final static String FT_HISTORY = "hgw/rw/payment-api/cp/api/funds/transfer-history/get"; // 转账记录
    public final static String EXP_TRANS_HISTORY = "hgw/rw/payment-api/cp/api/transactionHistory/downloadAccountTransactionHistoryPDF"; // 下载出入金报告
    public final static String DP_GET_RESTRICT_DATA = "hgw/r/payment-api/cp/api/deposit/getRestrictedDepositData";
    public final static String DP_TRANS_HISTORY = "hgw/rw/payment-api/cp/api/transactionHistory/deposit"; // 入金记录
    public final static String WD_TRANS_HISTORY = "hgw/rw/payment-api/cp/api/transactionHistory/withdraw"; // 出金记录
    public final static String WD_CANCEL_URL = "hgw/rw/payment-api/cp/api/withdrawal/cancelWithdrawalOrder?withdrawalId=";
    public final static String WD_IB_CANCEL_URL = "hgw/rw/payment-api/api/withdrawal/cancelWithdrawalOrder?withdrawalId=";
    public final static String WD_GET_UNIONPAY_CARD = "/hgw/rw/payment-api/cp/api/withdrawal/queryUnionpayCard";
    public final static String WD_BLACKLIST_STATUS = "/hgw/r/payment-api/cp/api/withdrawal/queryBlacklist"; // 检查Client是否被列入出金黑名单
    public final static String WD_VALIDATE_MAMFEESETTLEMENT = "/hgw/r/payment-api/cp/api/withdrawal/validateMamFeeSettlement";
    public final static String WD_APPLY = "hgw/rw/payment-api/cp/api/withdrawal/applyWithdrawal_cp_batch";//出金
    public final static String WD_DATA_CP = "hgw/rw/payment-api/cp/api/withdrawal/getWithdrawalData_cp";
    public static final String WD_CC_BALANCES = "hgw/rw/payment-api/cp/api/withdrawal/creditCard/balances/";
    public final static String WD_LIMITINFO = "hgw/rw/payment-api/cp/api/withdrawal/getLimitInfo";
    public final static String WD_NONCCTYPE = "hgw/rw/payment-api/cp/api/withdrawal/getNonCreditCardWithdrawTypeCP";
    public final static String WD_LBT_DATA = "hgw/rw/payment-api/cp/api/withdrawal/getLBTWithdrawalData_cp";
    public final static String WD_EXCHG_RATE = "/hgw/rw/payment-api/cp/api/withdrawal/exchangeRate";
    public final static String WD_PCS_SORTINFO = "hgw/rw/payment-api/cp/api/withdrawal/pcs/v2/sort-info";
    public final static String WD_PCS_CHANINFO = "/hgw/rw/payment-api/cp/api/withdrawal/pcs/channel-info";
    public final static String WD_PCS_CHANINFO_V2 = "/hgw/rw/payment-api/cp/api/withdrawal/apply_channel_cp";

    public final static String WD_PCS_ENABLED = "/hgw/r/payment-api/cp/api/withdrawal/pcs/enabled";
    public final static String WD_PCS_CURRENCYLIMIT = "hgw/r/payment-api/cp/api/withdrawal/pcs/currency-limit";
    public final static String WD_LBT_SWITCH = "hgw/rw/payment-api/cp/api/withdrawal/lbt_switch";

    //IB接口
    public final static String IB_REBATELIST = "/web-api/api/home/queryRebatesList";
    public final static String IB_AVAIBALANCE = "web-api/api/home/queryAvailableBalance";
    public final static String IB_PAYMENT_INFO = "hgw/rw/payment-api/api/payment/info/getInfo";
    public final static String IB_PAYMENT_LIST = "hgw/rw/payment-api/api/payment/info/getPaymentInfoList/";
    public final static String IBWD_DATA = "hgw/rw/payment-api/api/withdrawal/getWithdrawalData";
    public final static String IBWD_APPLY = "hgw/rw/payment-api/api/withdrawal/applyWithdrawal";
    public final static String IBWD_REBATE_HISTORY = "hgw/rw/payment-api/api/rebate/withdrawHistory";
    public final static String IBWD_BLACKLIST_STATUS = "hgw/r/payment-api/api/withdrawal/queryBlacklist"; // 检查 IB是否被列入出金黑名单
    public final static String IBFT_ELIGTRANSFER = "hgw/rw/payment-api/api/transfer/eligible-transfer";
    public final static String IBFT_SUBIBINFO = "web-api/api/transfer/sub-ib/info";
    public final static String IBFT_REBATETRAD_ACC = "hgw/rw/payment-api/api/transfer/toApplyTransferView";
    public final static String IBFT_REBATE_ACC = "hgw/rw/payment-api/api/transfer/toApplyTransferOthersView";
    public final static String IBFT_TRANSFER_IB = "hgw/rw/payment-api/api/transfer/applyTransfer";
    public final static String IBFT_TRANSFER_SUBIB = "hgw/rw/payment-api/api/transfer/apply/sub-ib-transfer";
    public final static String IBFT_DOWNLOAD_TEMPLATE = "web-api/api/transfer/download-transfer-template";
    public final static String VERIFICATION_CODE = "web-api/api/mfa/sendEmailVerificationCode";


    //wallet cp和admin端地址
    public static String withdrawcurrency_list="/web-api/cp/api/wallet/withdraw/currency-list";
    public static String order_verification="/web-api/cp/api/wallet/withdraw/order-verification";
    public static String getMultiAuthMethod="/web-api/cp/api/multiFactorAuth/wallet-withdraw/getMultiAuthMethod";
    public static String transfer_list="/hgw/wallet-api/cp/api/wallet/trans-history/transfer-list";
    public static String anti_reuse="/web-api/api/token/anti-reuse";
    public static String getTransferData_cp ="/hgw/r/payment-api/cp/api/transfer/getTransferData_cp";
    public static String getWalletTransferLimit_cp = "/web-api/cp/api/transfer/getWalletTransferLimit_cp";//
    public static String getWalletExchangeRate_cp = "hgw/rw/payment-api/cp/api/transfer/getWalletExchangeRate";//
    public static String withdraworder_confirm = "/web-api/cp/api/wallet/withdraw/order-confirm";
    public static String withdrawcalc_fee = "/web-api/cp/api/wallet/withdraw/calc-fee";
    public static String withdrawfetch_crypto = "/web-api/cp/api/wallet/fetch-crypto-fiat-exchange";
    public static String withdraworder_list = "/web-api/cp/api/wallet/withdraw/order-list";
    public static String queryBlacklist = "/hgw/r/payment-api/cp/api/withdrawal/queryBlacklist";
    public static String export_pdf = "/web-api/cp/api/wallet/export-pdf";
    public static String convertrecordlist = "/hgw/wallet-api/cp/api/wallet/convert/record-list";
    public static String convertconfirmorder = "/hgw/wallet-api/cp/api/wallet/convert/confirm-order";
    public static String convertrefreshorder = "/hgw/wallet-api/cp/api/wallet/convert/refresh-order";
    public static String convertsubmit = "/hgw/wallet-api/cp/api/wallet/convert/conver-submit";
    public static String convertquantityexchange = "/hgw/wallet-api/cp/api/wallet/convert/get-quantity-and-exchange";
    public static String convertgetconverlist = "/hgw/wallet-api/cp/api/wallet/convert/get-conver-list";
    public static String wallethome = "/hgw/wallet-api/cp/api/wallet/home";
    public static String checkDepositPermissions = "/hgw/wallet-api/cp/api/wallet/deposit/mobile/checkDepositPermissions";
    public static String checkDepositwebPermissions = "/hgw/wallet-api/cp/api/wallet/deposit/checkDepositPermissions";
    public static String Depositrecorddetail = "/hgw/wallet-api/cp/api/wallet/deposit/record-detail?id=";
    public static String DepositRecordList = "/hgw/wallet-api/cp/api/wallet/deposit/record-list";
    public static String getDepositAddress = "/web-api/cp/api/wallet/deposit/mobile/getDepositAddress";
    public static String getDepositpcAddress = "/web-api/cp/api/wallet/deposit/getDepositAddress?currency=USDT&chain=TRX";
    public static String openaccountvalidate = "hgw/wallet-api/cp/api/wallet/open-account-validate";
    public static String walletbaseinfo = "/hgw/wallet-api/cp/api/wallet/base-info";
    public static String walletlevelinfo = "/hgw/wallet-api/cp/api/wallet/level-info";
    public static String walletflowdetail = "/web-api/cp/api/wallet/flow-detail";
    public static String walletselectlist = "/hgw/wallet-api/cp/api/wallet/select-list";
    public static String walletWithdrawOrderDetail = "/web-api/cp/api/wallet/withdraw/order-detail";
    public static String walletuserpermission = "/hgw/wallet-api/cp/api/wallet/user-permission";
    public static String getCurrencyChains = "/hgw/wallet-api/cp/api/wallet/deposit/getCurrencyChains";
    public static String mobilegetCurrencyChains = "/hgw/wallet-api/cp/api/wallet/deposit/mobile/getCurrencyChains";
    public static String walletFlowList = "/hgw/wallet-api/cp/api/wallet/flow-list";
    public static String admincurrencyaccountinfo = "/api/admin/wallet/account/currency-account-info";
    public static String adminusercurrencyaccountinfo = "/api/admin/wallet/account/user-currency-account-info";
    public static String adminaccountpermissions = "/api/admin/wallet/account/permissions?accountNo=1028&lang=en";
    public static String adminaccountpermissionsUAT = "/api/admin/wallet/account/permissions?userId=1028&lang=en";
    public static String adminqueryPermission = "/api/admin/permission/queryPermission";
    public static String adminsetPermission = "/api/admin/wallet/account/set-permissions";
    public static String adminaccountfundflowlist = "/api/admin/wallet/account/fund-flow-list";
    public static String adminaccountfreeze = "/api/admin/wallet/account/freeze";
    public static String adminaccountunfreeze = "/api/admin/wallet/account/un-freeze";

    public static String adminIngressWithdrawDetail = "/api/admin/wallet/ingress/withdraw/detail";
    public static String adminIngressWithdrawList = "/api/admin/wallet/ingress/withdraw/list";
    public static String adminIngressWithdrawExport = "/api/admin/wallet/ingress/withdraw/export";
    public static String adminWithdrawOrderDetail = "/api/admin/wallet/deposit/wallet-order-detail";
    public static String admindepositcurencylist = "/api/admin/wallet/deposit/currency-list";
    public static String admindepositwalletorderlist = "/api/admin/wallet/deposit/wallet-order-list";
    public static String adminwalletorderlist = "/api/admin/wallet/withdraw/order-list";
    public static String adminwalletorderlistwalletWithdrawOrderNo = "/api/admin/wallet/withdraw/order-detail?walletWithdrawOrderNo=";
    public static String admintransferorderlist = "/api/admin/wallet/transfer/transfer-order-list";
    public static String adminwalletqueryAccountCurrencyList = "/api/admin/wallet/platformFund/queryAccountCurrencyList";
    public static String adminHedgeConvertOrderList = "/api/admin/wallet/hedge/convert-order-list";
    public static String platformFund = "/api/admin/wallet/platformFund/getFundList";
    public static String queryAccountCurrencyList = "/api/admin/wallet/platformAccount/queryAccountCurrencyList";
    public static String admintransferorderdetailid = "/api/admin/wallet/transfer/transfer-order-detail";
    public static String adminconvertorderlist = "/api/admin/wallet/convert/convert-order-list";
    public static String platformAccountqueryAccountList = "/api/admin/wallet/platformAccount/queryAccountList";
    public static String genSyncDepositRecords = "/web-api/wallet/deposit-test/genSyncDepositRecords";
    public static String walletConvertDownload="/admin/wallet/convert/convert-order-list-download";
    public static String walletHedgeDownload="/admin/wallet/hedge/convert-order-list-download";
    public static String walletChannelList="/admin/wallet/deposit/channel-order-list";
    public static String walletChannelDetail="/admin/wallet/deposit/channel-order-detail";
    public static String walletChaninList="/admin/wallet/deposit/chain-list";
    public static String walletChannelTransferRecordlist="/admin/wallet/deposit/channel-transfer-record-list";
    public static String walletChannelTransferRecordDetail="/api/admin/wallet/deposit/channel-transfer-record-detail";
    public static String walletQueryTodayAsset="/admin/wallet/platformAsset/queryTodayAsset";
    public static String walletQueryYesterAsset="/admin/wallet/platformAsset/queryYesterdayAsset";
    public static String frozenDetailList="/api/admin/wallet/account/frozen-detail/list";
    public static String adminqueryAccountCurrencyList="/admin/wallet/platformAccount/queryAccountCurrencyList";
    public static String walletaccountlist="/api/admin/wallet/account/list";
    public static String walletWithdraworderListDownload="/api/admin/wallet/withdraw/order-list-download";
    public static String walletWithdrawCurrencyList="/api/admin/wallet/withdraw/currency-list";
    public static String walletDepositRewardOrderDetail="/api/admin/wallet/deposit/wallet-reward-order-detail";
    public static String walletIngressChainList="/api/admin/wallet/ingress/withdraw/chain-list";
    public static String walletAccountFundFlowDownloadUrl="/api/admin/wallet/account/fund-flow-list-download";
    public static String walletAccountDownload="/api/admin/wallet/account/account-list-download";
    public static String walletDepositWalletRewardOrderListUrl="/api/admin/wallet/deposit/wallet-reward-order-detail";
    public static String walletDepositOrderListDownloadUrl="/api/admin/wallet/deposit/deposit-order-list-download";
    public static String TransferOrderListDownloadURL="/api/admin/wallet/transfer/transfer-order-list-download";
    public static String walletPlatFormAccountQueryAccountDetailURL="/api/admin/wallet/platformAccount/queryAccountDetailList";
    public static String walletTransferRateURL = "/hgw/rw/payment-api/cp/api/transfer/getWalletExchangeRate";
    public static String getPaymentTransferData_cp ="/hgw/r/payment-api/cp/api/transfer/getTransferData_cp";

    //活动接口
    public final static String CAMPAIGNDB_PARTICIPATIONINFO="/api/campaign/admin/databaseSearch/user/participationInfo";
    public final static String CAMPAIGNDB_OPERATIONRECORD="/api/campaign/admin/databaseSearch/user/operationRecord";
    public final static String CAMPAIGNDB_DEPOBONUS_TRANSACTION="/api/campaign/admin/databaseSearch/depositBonus/transaction";
    public final static String CAMPAIGN_USERDATA="/api/campaign/admin/depositBonus/v2/users";
    public final static String CAMPAIGN_TRANSACTIONDATA="/api/campaign/admin/depositBonus/transaction";
    public final static String CAMPAIGN_BLACKLIST="/api/campaign/admin/blackList";
    public final static String CAMPAIGN_WHITELIST="/api/campaign/admin/whiteList";
    public final static String CAMPAIGNSETTING_SEARCH_CAMPAIGN ="/api/campaign/admin/settings/list";
    public final static String CAMPAIGNSETTING_CREATE_CAMPAIGN ="/api/campaign/admin/settings/create";
    public final static String CAMPAIGNSETTING_APPROVE_CAMPAIGN ="/api/campaign/admin/settings/auditCampaignSetting_approve";
    public final static String CAMPAIGNSETTING_VIEW_CAMPAIGN ="/api/campaign/admin/settings/view";
    public final static String CAMPAIGNSETTING_EDIT_CAMPAIGN ="/api/campaign/admin/settings/edit";
    public final static String CAMPAIGNSETTING_END_CAMPAIGN ="/api/campaign/admin/settings/end";
    public final static String CAMPAIGNSETTING_ARCHIVE_CAMPAIGN ="/api/campaign/admin/settings/archive";
    public final static String CAMPAIGNSETTING_UNARCHIVE_CAMPAIGN ="/api/campaign/admin/settings/unarchive";
    public final static String LOYALTYPROGRAM_USERDATA="/loyalty/admin/users";

    //cp account
    public static String antiReuseUrl="/web-api/api/token/anti-reuse";
    public static String getBotIdUrl="/web-api/api/third/tg-bot/getBotId";
    public static String id3PassedUrl="/web-api/api/personalDetail/id3Passed";
    public static String demoAccsUrl="/web-api/cp/api/home/get-demo-accs";
    public static String queryMetaTraderAccountOverviewURL="/web-api/cp/api/home/query-metaTrader-account-overview";
    public static String loyaltyOverviewURL="/web-api/cp/api/loyalty/overview";
    public static String hintInfoURL="/web-api/cp/api/multiFactorAuth/hintInfo";
    public static String queryClientTotalEquityURL="web-api/cp/api/home/query-client-total-equity";
    public static String getSystemInfo="/web-api/r/api/system/information";
    public static String getDirectClientURL="/web-api/cp/api/potential/ib/getDirectClient";
    public static String cashRebateAvailableAccountsURL="/web-api/cp/api/loyalty/cash-rebate-available-accounts";
    public static String bannerdisplayPlatformURL="/web-api/api/banner/enabled?displayPlatform=1&country=3636&displayPage=1";
    public static String bannerEnabled="/web-api/api/banner/enabled";
    public static String tokenantireuseURL="/web-api/api/token/anti-reuse";
    public static String getAllLastProofURL="/web-api/cp/api/identityProof/getAllLastProof";
    public static String multiFactorAuthURL="/web-api/cp/api/multiFactorAuth/validateEnv";
    public static String queryMetaTraderAccountDetailURL="hgw/account-api/bsn/account/getWebHomeAccounts";
    public static String queryLeverageStatusesURL="/web-api/cp/api/home/query-leverage-statuses";
    public static String queryOpenAccountStatusURL="/web-api/cp/api/home/query-open-account-status";
    public static String getOpenAccountConditionURL="/web-api/cp/api/account/getOpenAccountCondition";
    public static String getPerpetualSwitchURL="/web-api/cp/api/account/getPerpetualSwitch";
    public static String getSupportCryptoCurreniesURL="/web-api/cp/api/account/getSupportCryptoCurrenies?accountType=standardSTP&mtPlatform=MT5";
    public static String openAccountValidateURL="/web-api/cp/api/wallet/open-account-validate";
    public static String queryAvailableLeveragesURL="/web-api/cp/api/home/query-available-leverages";
    public static String updateLeverageURL="/web-api/cp/api/home/update-leverage";
    public static String saveNicknameURL="/web-api/cp/api/home/save-nickname";
    public static String multiFactorAuthInfoURL="/web-api/cp/api/multiFactorAuth/info";
    public static String getAccountTypeURL="/web-api/cp/api/account/getAccountType";
    public static String verificationGetDataURL="/web-api/api/verificationV2/getData?step=5";
    public static String currentcurrentURL="/web-api/api/security/current/changeAuthenticator/rule";
    public static String getMultiAuthMethodURL="/web-api/cp/api/multiFactorAuth/enable-auth-2fa/getMultiAuthMethod";
    public static String twoFactorStatusURL="/web-api/cp/api/two-factor/status";
    public static String twoFactorEnableURL="/web-api/cp/api/two-factor/enable";
    public static String multiFactorAuthverifyStatusURL="/web-api/cp/api/multiFactorAuth/verifyStatus";
    public static String updatePasswordURL="/web-api/api/security/current/updatePassword/rule";
    public static String modifyPasswordURL="/web-api/cp/api/multiFactorAuth/modify-password/getMultiAuthMethod";
    public static String updatePwdURL="/web-api/cp/api/profile/updatePassword";
    public static String updateEmailURL="/web-api/cp/api/multiFactorAuth/changeEmail";
    public static String sendEmailVerifyCode="/web-api/api/mfa/sendEmailVerificationCode";
    public static String sendRWEmailVerifyCode="/web-api/rw/api/mfa/sendEmailVerificationCode";
    public static String totpVerify="/web-api/cp/api/multiFactorAuth/totp/preValidate";
    public static String checkPasswordURL="/web-api/cp/api/profile/checkPassword";
    public static String getClientSwitchURL="/web-api/api/ibaccount/getClientSwitch";
    public static String queryRebatesBlackListURL="/web-api/api/home/queryRebatesBlackList";
    public static String sessionidURL="/web-api/api/tool/session-id";
    public static String jumpURL="/web-api/api/verification/sumsub/jump";
    public static String getUsername="/web-api/cp/api/user/getUsername";
    public static String get_invite_code_by_ib_count = "/web-api/api/invitecode/get_invite_code_by_ib_count?ibAccount=2399";
    public static String getInstruments = "/web-api/api/report/get-instruments";
    public static String HomeActivityTickerURL = "/web-api/api/home/activity/ticker";
    public static String get_cp_url = "/web-api/api/login/get_cp_url";
    public static String employmentFinance = "/web-api/api/question/employmentFinance";
    public static String questionTrading = "/web-api/api/question/trading";
    public static String multiAuthVerifyNum= "/web-api/cp/api/multiFactorAuth/verifyNum";
    public static String birthPlace = "/web-api/api/personalDetail/birthPlace";
    public static String provinces = "/web-api/api/personalDetail/provinces";
    public static String getMetaTraderAccounts = "/web-api/cp/api/account/getMetaTraderAccounts";
    public static String userAnswerCheck = "/web-api/api/question/userAnswerCheck";
    public static String getCurrentStep = "/web-api/api/proclient/getCurrentStep";
    public static String categorisationTest = "/web-api/api/question/categorisationTest";
    public static String questionnaireVerify = "/web-api/cp/api/questionnaire/verify";
    public static String exchangeRate= "/web-api/api/exchange-rate";
    public static String getAddressProofAndBankInforURL="/web-api/cp/api/identityProof/getAddressProofAndBankInfor";
    public static String checkTransferIBURL="/web-api/cp/api/profile/checkTransferIB-Affiliate";
    public static String historyIbTransferURL="/web-api/cp/api/profile/historyIbTransfer?pageNo=1&pageSize=10";
    public static String transferIBAffiliatenewURL="/web-api/cp/api/profile/transferIB-Affiliate_new";
    public static String getSecuritysURL="/web-api/api/security/getSecuritys";
    public static String setlanguageURL="/web-api/api/userProfile/setlanguage";
    public static String i18nURL="/web-api/api/file/i18n-url/zh_CN";
    public static String sliderCheckURL="/web-api/api/sliderCheck/passed";
    public static String updateBalanceDemoURL="/web-api/cp/api/home/update-balance-demo";
    public static String updateLeverageDemoURL="/web-api/cp/api/home/update-leverage-demo";
    public static String encrtTextForOfficeWebsitURL="/web-api/api/registrationV2/encrtTextForOfficeWebsit?encrtText=1747619768";
    public static String toLoginbyTokenURL="/web-api/api/login/to_login_by_token";
    public static String ibQueryAvailableBalanceURL="/web-api/api/home/queryAvailableBalance";
    public static String getNearestOpenAccountURL="/web-api/api/tradeaccount/getNearestOpenAccount";
    public static String queryRebateVolumeListURL="/web-api/api/TradeRebateVolume/queryRebateVolumeList";
    public static String queryTotalCommissiontURL="/web-api/api/home/queryTotalCommission";
    public static String getNewOpenUserTotalURL="/web-api/api/home/getNewOpenUserTotal";
    public static String queryFoldLineTotalCommissionURL="/web-api/api/foldline/queryFoldLineTotalCommission";
    public static String queryPieTotalVolumeByGoodsURL="/web-api/api/pie/queryPieTotalVolumeByGoods";
    public static String ibNotificationURL="/web-api/api/notification/enabled?country=5015";
    public static String subIbStreeURL="/web-api/api/ibaccount/sub_ibs_tree";
    public static String retailClientsV2URL="/web-api/api/ibaccount/retail_clientsV2";
    public static String zeroBalanceURL="/web-api/api/unfundedaccount/zerobalance";
    public static String neverFundedURL="/web-api/api/unfundedaccount/neverfunded";
    public static String queryIbReportDataURL="/web-api/api/ibreport/queryIbReportData";
    public static String getPendingAccountURL="/web-api/api/tradeaccount/getPendingAccount";
    public static String profileInfoURL="hgw/user-api/bsn/user/clientProfile";
    public static String rebateAgreementURL="/web-api/api/rebate/agreement/account/2399";
    public static String getFirstAccountAuditStatusURL="/web-api/cp/api/account/getFirstAccountAuditStatus";
    public static String resetChangeIPWarnURL="/web-api/cp/api/profile/reset_change_ip_warn";
    public static String rebateHistoryURL="/web-api/api/rebate/rebateHistory";
    public static String transferHistoryURL="/web-api/api/rebate/transferHistory";
    public static String applyCommissionURL="/web-api/api/commission/applyCommission";
    public static String reportQueryByDateURL="/web-api/api/report/queryByDate";
    public static String rebateReportURL="/web-api/api/report/rebate-report";
    public static String leadsURL="/web-api/api/menu/leads";
    public static String sendEmailVerificationCodeURL="/web-api/api/mfa/sendEmailVerificationCode";
    public static String keyPairPKURL="/web-api/cp/api/keyPair/pk?token=null";
    public static String subIbsURL="/web-api/api/ibaccount/sub_ibs";
    public static String getAgreementListURL="/web-api/api/rebate/agreement/getAgreementList";
    public static String subIbClientsURL="/web-api/api/ibaccount/sub_ib_clients";
    public static String identityProofURL="/web-api/cp/api/identityProof/get_last_identifiy_proof";
    public static String emailPreValidateURL="/web-api/cp/api/multiFactorAuth/email/preValidate";
    public static String accessTokenURL="/web-api/cp/api/sumsub/access-token";

    public static String reqResetPassword = "/web-api/cp/api/profile/req_reset_profile_password";
    public static String resetPassword = "/web-api/cp/api/profile/submit_reset_profile_password";

}
