import Vue from 'vue'
import Router from 'vue-router'
import identity from './identity'

Vue.use(Router)
const router = new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      component: () => import('@/views/shared/Layout'),
      children: [
        {
          path: '/',
          name: '/',
          meta: {
            noShowLoading: true,
          },
        },
        //only au
        {
          path: '/loginByMobile',
          name: 'loginByMobile',
          component: () => import('@/views/LoginClientMobile'),
          props: true,
          meta: {
            allowAnonymous: true,
            noShowLoading: true,
            restrictBanner: true,
          },
        },
        //only au
        {
          path: '/resetPasswordMobile',
          name: 'resetPasswordMobile',
          component: () => import('@/views/ResetPasswordMobile'),
          meta: {
            allowAnonymous: true,
            restrictBanner: true,
          },
        },
        {
          path: '/forgetPasswordMobile',
          name: 'forgetPasswordMobile',
          component: () => import('@/views/ForgetPasswordReqMobile'),
          meta: {
            allowAnonymous: true,
            restrictBanner: true,
          },
        },
        {
          path: '/logout',
          name: 'logout',
          meta: {
            allowAnonymous: true,
            noShowLoading: true,
            restrictBanner: true,
          },
        },
        {
          path: '/register',
          name: 'register',
          component: () => import('@/views/register/Register'),
          meta: {
            restrictBanner: true,
          },
        },
        {
          path: '/demo',
          name: 'demo',
          component: () => import('@/views/register/Demo'),
          meta: {
            restrictBanner: true,
          },
        },
        {
          path: '/authority',
          name: 'authority',
          component: () => import('@/views/Authority'),
        },
        {
          path: '/registerFinish',
          name: 'registerFinish',
          component: () => import('@/views/register/RegisterFinish'),
        },
        {
          path: '/error',
          name: 'error',
          component: () => import('@/views/register/Error'),
          meta: {
            allowAnonymous: true,
            noShowLoading: true,
            restrictBanner: true,
          },
        },
        {
          path: '/tradingTool/MARKET_BUZZ',
          name: 'MARKET_BUZZ',
          component: () => import('@/views/TradingTools'),
          meta: {
            noShowLoading: true,
            restrictBanner: true,
            authority: true,
          },
        },
        {
          path: '/tradingTool/ECON_CALENDAR',
          name: 'ECON_CALENDAR',
          component: () => import('@/views/TradingTools'),
          meta: {
            noShowLoading: true,
            restrictBanner: true,
            authority: true,
          },
        },
        {
          path: '/tradingTool/FEAT_FX',
          name: 'FEAT_FX',
          component: () => import('@/views/TradingTools'),
          meta: {
            noShowLoading: true,
            restrictBanner: true,
            authority: true,
          },
        },
        {
          path: '/tradingTool/FX_IDEAS',
          name: 'FX_IDEAS',
          component: () => import('@/views/TradingTools'),
          meta: {
            noShowLoading: true,
            restrictBanner: true,
            authority: true,
          },
        },
        {
          path: '/marketWidgets/technicalAnalysis',
          name: 'technicalAnalysis',
          component: () => import('@/views/marketWidgets/TechnicalAnalysis'),
          meta: {
            authority: true,
          },
        },
        {
          path: '/TradingVideoTutorials',
          name: 'TradingVideoTutorials',
          component: () => import('@/views/TradingVideoTutorials'),
          meta: {
            noShowLoading: true,
            restrictBanner: true,
            authority: true,
          },
        },
        {
          path: '/TradingView',
          name: 'TradingView',
          component: () => import('@/views/TradingView'),
          meta: {
            noShowLoading: true,
            restrictBanner: true,
            authority: true,
          },
        },
        {
          path: '/home',
          name: 'home',
          component: () => import('@/views/Home'),
          meta: {
            authority: true,
          },
        },
        {
          path: '/homeDemo',
          name: 'homeDemo',
          component: () => import('@/views/HomeDemo'),
        },
        {
          path: '/liveAccount',
          name: 'liveAccount',
          component: () => import('@/views/Home'),
          meta: {
            authority: true,
          },
        },
        {
          path: '/openLiveAccount',
          name: 'openLiveAccount',
          component: () => import('@/views/OpenLiveAccount'),
          meta: {
            authority: true,
          },
        },
        {
          path: '/openDemoAccount',
          name: 'openDemoAccount',
          component: () => import('@/views/OpenDemoAccount'),
        },
        {
          path: '/resetPassword',
          name: 'resetPassword',
          component: () => import('@/views/ResetPassword'),
          meta: {
            allowAnonymous: true,
          },
        },
        {
          path: '/depositFunds',
          name: 'depositFunds',
          component: () => import(`@/views/deposit/DepositFunds`),
          meta: {
            authority: true,
          },
        },
        {
          path: '/restrictDeposit',
          name: 'restrictDeposit',
          component: () => import(`@/views/deposit/restrictDeposit`),
          meta: {
            noShowLoading: true,
            authority: true,
          },
        },
        {
          path: '/withdrawFunds',
          name: 'withdrawFunds',
          component: () => import('@/views/withdraw/WithdrawFunds'),
          meta: {
            authority: true,
          },
        },
        {
          path: '/transferFunds',
          name: 'transferFunds',
          component: () => import('@/views/TransferFunds'),
          meta: {
            authority: true,
          },
        },
        {
          path: '/transactionHistory',
          name: 'transactionHistory',
          component: () => import('@/views/TransactionHistory'),
          meta: {
            authority: true,
          },
        },
        {
          path: '/downloads',
          name: 'downloads',
          component: () => import('@/views/Downloads'),
        },
        {
          path: '/myProfile',
          name: 'myProfile',
          component: () => import('@/views/MyProfile'),
        },
        {
          path: '/depositBonus',
          name: 'depositBonus',
          component: () => import('@/views/promotion/DepositBonus'),
        },
        {
          path: '/changePassword',
          name: 'changePassword',
          component: () => import('@/views/ChangePassword'),
        },
        {
          path: '/twoFactorAuthentication',
          name: 'twoFactorAuthentication',
          component: () => import('@/views/TwoFactorAuthentication'),
        },
        {
          path: '/upgradeToPro',
          name: 'upgradeToPro',
          component: () => import('@/views/wholesale/UpgradeToPro'),
        },
        {
          path: '/contactUs',
          name: 'contactUs',
          component: () => import(`@/views/platform/${process.env.PLATFORM}/ContactUs`),
        },
        {
          path: '/paymentDetails',
          name: 'paymentDetails',
          component: () => import('@/views/PaymentDetails'),
          meta: {
            authority: true,
          },
        },
        {
          path: '/questionnaire',
          name: 'questionnaire',
          component: () => import('@/views/Questionnaire'),
        },
        // pages of thirdParty
        {
          path: '/fasapay/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/neteller/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/neteller/thirdPartyDepositFailed',
          name: 'thirdPartyDepositFailed',
          component: () => import('@/views/callback/ThirdPartyDepositFailed'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/paysafe_neteller/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/paytrust/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/paywize/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/bitwallet/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/kr_df/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/paytrust/thirdPartyDepositFailed',
          name: 'thirdPartyDepositFailed',
          component: () => import('@/views/callback/ThirdPartyDepositFailed'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/zotapay/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/paytoday/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/unionpay/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/tinkbit/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/creditcard/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/transact365/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/solid/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/apg/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/sd/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/bridgepay/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/safeCharge/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/safeCharge/thirdPartyDepositFailed',
          name: 'thirdPartyDepositFailed',
          component: () => import('@/views/callback/ThirdPartyDepositFailed'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/skrill/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/skrill/thirdPartyDepositFailed',
          name: 'thirdPartyDepositFailed',
          component: () => import('@/views/callback/ThirdPartyDepositFailed'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/paypal/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/paypal/thirdPartyDepositFailed',
          name: 'thirdPartyDepositFailed',
          component: () => import('@/views/callback/ThirdPartyDepositFailed'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/cardpay/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/cardpay/thirdPartyDepositFailed',
          name: 'thirdPartyDepositFailed',
          component: () => import('@/views/callback/ThirdPartyDepositFailed'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/crypto/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/crypto/thirdPartyDepositFailed',
          name: 'thirdPartyDepositFailed',
          component: () => import('@/views/callback/ThirdPartyDepositFailed'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/cryptousdt/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/cryptousdt/thirdPartyDepositFailed',
          name: 'thirdPartyDepositFailed',
          component: () => import('@/views/callback/ThirdPartyDepositFailed'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/mijipay/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/stic/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/stic/thirdPartyDepositFailed',
          name: 'thirdPartyDepositFailed',
          component: () => import('@/views/callback/ThirdPartyDepositFailed'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/xpay/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          //india
          path: '/paygate/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/trustly/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
            restrictApi: true,
            noShowLoading: true,
          },
        },
        {
          path: '/trustly/thirdPartyDepositFailed',
          name: 'thirdPartyDepositFailed',
          component: () => import('@/views/callback/ThirdPartyDepositFailed'),
          meta: {
            restrictReload: true,
            restrictApi: true,
            noShowLoading: true,
          },
        },
        {
          path: '/poli/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/poli/thirdPartyDepositFailed',
          name: 'thirdPartyDepositFailed',
          component: () => import('@/views/callback/ThirdPartyDepositFailed'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/assembly/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/assembly/thirdPartyDepositFailed',
          name: 'thirdPartyDepositFailed',
          component: () => import('@/views/callback/ThirdPartyDepositFailed'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/pagsmile/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/astropay/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/astropay/thirdPartyDepositFailed',
          name: 'thirdPartyDepositFailed',
          component: () => import('@/views/callback/ThirdPartyDepositFailed'),
          meta: {
            restrictReload: true,
          },
        },
        // UK
        {
          path: '/everpay/thirdPartyDepositResult',
          name: 'thirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/everpay/thirdPartyDepositFailed',
          name: 'thirdPartyDepositFailed',
          component: () => import('@/views/callback/ThirdPartyDepositFailed'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/thirdPartyDepositResult/:id',
          name: 'thirdPartyResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/thirdPartyDepositFailed/:id',
          name: 'thirdPartyFailed',
          component: () => import('@/views/callback/ThirdPartyDepositFailed'),
          meta: {
            restrictReload: true,
          },
        },
        // pages of deposit
        {
          path: '/deposit/creditOrDebit',
          name: 'creditOrDebit',
          component: () => import('@/views/deposit/CreditOrDebit'),
        },
        {
          path: '/deposit/bitwalletDeposit',
          name: 'bitwalletDeposit',
          component: () => import('@/views/deposit/BitwalletDeposit'),
        },
        {
          path: '/deposit/indiaPayment',
          name: 'indiaPayment',
          component: () => import('@/views/deposit/IndiaPayment'),
        },
        {
          path: '/deposit/dragonPhoenixDeposit',
          name: 'dragonPhoenixDeposit',
          component: () => import('@/views/deposit/DragonPhoenixDeposit'),
        },
        {
          path: '/deposit/bridgePaymentDeposit',
          name: 'bridgePaymentDeposit',
          component: () => import('@/views/deposit/BridgepayDeposit'),
        },
        {
          path: '/deposit/brokerToBrokerTransfer',
          name: 'brokerToBrokerTransfer',
          component: () => import('@/views/deposit/BrokerToBrokerTransfer'),
        },
        {
          path: '/deposit/bPayDeposit',
          name: 'bPayDeposit',
          component: () => import('@/views/deposit/BPayDeposit'),
        },
        {
          path: '/deposit/bankWireAustralia',
          name: 'bankWireAustralia',
          component: () => import('@/views/deposit/BankWireAustralia'),
        },
        {
          path: '/deposit/fasaPay',
          name: 'fasaPay',
          component: () => import('@/views/deposit/FasaPay'),
        },
        {
          path: '/deposit/internationalSwift',
          name: 'internationalSwift',
          component: () => import('@/views/deposit/InternationalSwift'),
        },
        {
          path: '/deposit/neteller',
          name: 'neteller',
          component: () => import('@/views/deposit/NetellerDeposit'),
        },
        {
          path: '/deposit/canadaLocalBankDeposit',
          name: 'canadaLocalBank',
          component: () => import('@/views/deposit/CanadaLocalBankDeposit'),
        },
        {
          path: '/deposit/paypal',
          name: 'paypal',
          component: () => import('@/views/deposit/PaypalDeposit'),
        },
        {
          path: '/deposit/trustly',
          name: 'trustly',
          component: () => import('@/views/deposit/TrustlyDeposit'),
        },
        {
          path: '/deposit/poliPay',
          name: 'poliPay',
          component: () => import('@/views/deposit/PoliPay'),
        },
        {
          path: '/deposit/mobilePay',
          name: 'mobilePay',
          component: () => import('@/views/deposit/MobilePay'),
        },
        {
          path: '/deposit/skrillPay',
          name: 'skrillPay',
          component: () => import('@/views/deposit/SkrillPay'),
        },
        {
          path: '/deposit/sticPay',
          name: 'sticPay',
          component: () => import('@/views/deposit/SticPay'),
        },
        {
          path: '/deposit/webMoney',
          name: 'webMoney',
          component: () => import('@/views/deposit/WebMoney'),
        },
        {
          path: '/deposit/unionPay',
          name: 'unionPay',
          component: () => import('@/views/deposit/UnionPay'),
        },
        {
          path: '/deposit/unionPayChina',
          name: 'unionPayChina',
          component: () => import('@/views/deposit/UnionPayChina'),
        },
        {
          path: '/deposit/thailandzotaPay',
          name: 'thailandzotaPay',
          component: () => import('@/views/deposit/ThailandZotapay'),
        },
        {
          path: '/deposit/thailandPaytoday',
          name: 'thailandPaytoday',
          component: () => import('@/views/deposit/ThailandPaytoday'),
        },
        {
          path: '/deposit/nigeriaZotapay',
          name: 'nigeriaZotapay',
          component: () => import('@/views/deposit/NigeriaZotapay'),
        },
        {
          path: '/deposit/vietnamZotapay',
          name: 'vietnamZotapay',
          component: () => import(`@/views/deposit/VietnamZotapay`),
        },
        {
          path: '/deposit/malaysiaPayment',
          name: 'malaysiaPayment',
          component: () => import(`@/views/deposit/MalaysiaPayment`),
        },
        {
          path: '/deposit/philipinessPayment',
          name: 'philipinessPayment',
          component: () => import(`@/views/deposit/PhilipinessPayment`),
        },
        {
          path: '/deposit/indonesiaPayment',
          name: 'indonesiaPayment',
          component: () => import(`@/views/deposit/IndonesiaPayment`),
        },
        {
          path: '/deposit/crypto',
          name: 'crypto',
          component: () => import('@/views/deposit/Crypto'),
        },
        {
          path: '/deposit/mijiPay',
          name: 'mijiPay',
          component: () => import('@/views/deposit/MijiPay'),
        },
        {
          path: '/deposit/pagsmile',
          name: 'pagsmile',
          component: () => import('@/views/deposit/Pagsmile'),
        },
        {
          path: '/deposit/indiaP2P',
          name: 'indiaP2P',
          component: () => import('@/views/deposit/IndiaP2P'),
          meta: { limitLang: 'en_US' },
        },
        {
          path: '/deposit/astropayDeposit',
          name: 'astropayDeposit',
          component: () => import('@/views/deposit/AstropayDeposit'),
        },
        {
          path: '/deposit/uk',
          name: 'uk',
          component: () => import('@/views/deposit/UK'),
        },
        {
          path: '/deposit/southAfricaPayment',
          name: 'southAfricaPayment',
          component: () => import('@/views/deposit/SouthAfricaPayment'),
        },
        {
          path: '/deposit/Vload',
          name: 'vload',
          component: () => import('@/views/deposit/Vload'),
        },
        {
          path: '/deposit/laos',
          name: 'laos',
          component: () => import('@/views/deposit/Laos'),
        },
      ],
    },
    {
      path: '/',
      component: () => import('@/views/shared/LoginLayout'),
      children: [
        {
          path: '/login',
          name: 'login',
          component: () => import(`@/views/platform/${process.env.PLATFORM}/LoginClient`),
          meta: {
            allowAnonymous: true,
            noShowLoading: true,
          },
        },
        {
          path: '/login/to_login',
          name: 'to_login',
          component: () => import(`@/views/platform/${process.env.PLATFORM}/LoginClient`),
          meta: {
            allowAnonymous: true,
            noShowLoading: true,
          },
        },
        {
          path: '/forgetPassword',
          name: 'forgetPassword',
          component: () => import(`@/views/platform/${process.env.PLATFORM}/ForgetPasswordReq`),
          meta: {
            allowAnonymous: true,
          },
        },
        {
          path: '/resetProfilePassword',
          name: 'resetProfilePassword',
          component: () => import(`@/views/platform/${process.env.PLATFORM}/ResetProfilePassword`),
          meta: {
            allowAnonymous: true,
          },
        },
      ],
    },
    {
      path: '',
      component: () => import('@/views/shared/Layout'),
      children: [
        {
          name: '404',
          path: '*',
          component: () => import('@/views/404'),
        },
      ],
    },
  ],
})
export default router
identity.authorize()