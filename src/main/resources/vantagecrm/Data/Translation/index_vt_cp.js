import Vue from 'vue'
import Router from 'vue-router'
import identity from './identity'

Vue.use(Router)
const router = new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      component: () => import('@/views/Layout'),
      children: [
        {
          path: '/',
          name: '/',
          meta: {
            noShowLoading: true,
          },
        },
        {
          path: '/login',
          name: 'login',
          component: () => import('@/views/LoginClient.vue'),
          meta: {
            allowAnonymous: true,
            noShowLoading: true,
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
          component: () => import('@/views/register/Register.vue'),
          meta: {
            restrictBanner: true,
          },
        },
        {
          path: '/demo',
          name: 'demo',
          component: () => import('@/views/register/Demo.vue'),
          meta: {
            restrictBanner: true,
          },
        },
        {
          path: '/authority',
          name: 'authority',
          component: () => import('@/views/Authority.vue'),
        },
        {
          path: '/registerFinish',
          name: 'registerFinish',
          component: () => import('@/views/register/RegisterFinish.vue'),
        },
        {
          path: '/error',
          name: 'error',
          component: () => import('@/views/register/Error.vue'),
          meta: {
            allowAnonymous: true,
            noShowLoading: true,
            restrictBanner: true,
          },
        },
        {
          path: '/tradingTool/MARKET_BUZZ',
          name: 'MARKET_BUZZ',
          component: () => import('@/views/TradingTools.vue'),
          meta: {
            noShowLoading: true,
            restrictBanner: true,
            authority: true,
          },
        },
        {
          path: '/tradingTool/ECON_CALENDAR',
          name: 'ECON_CALENDAR',
          component: () => import('@/views/TradingTools.vue'),
          meta: {
            noShowLoading: true,
            restrictBanner: true,
            authority: true,
          },
        },
        {
          path: '/tradingTool/FEAT_FX',
          name: 'FEAT_FX',
          component: () => import('@/views/TradingTools.vue'),
          meta: {
            noShowLoading: true,
            restrictBanner: true,
            authority: true,
          },
        },
        {
          path: '/tradingTool/FX_IDEAS',
          name: 'FX_IDEAS',
          component: () => import('@/views/TradingTools.vue'),
          meta: {
            noShowLoading: true,
            restrictBanner: true,
            authority: true,
          },
        },
        {
          path: '/home',
          name: 'home',
          component: () => import('@/views/Home.vue'),
          meta: {
            authority: true,
          },
        },
        {
          path: '/homeDemo',
          name: 'homeDemo',
          component: () => import('@/views/HomeDemo.vue'),
        },
        {
          path: '/liveAccount',
          component: () => import('@/views/Home.vue'),
          meta: {
            authority: true,
          },
        },
        {
          path: '/openLiveAccount',
          name: 'openLiveAccount',
          component: () => import('@/views/OpenLiveAccount.vue'),
          meta: {
            authority: true,
          },
        },
        {
          path: '/openDemoAccount',
          name: 'openDemoAccount',
          component: () => import('@/views/OpenDemoAccount.vue'),
        },
        {
          path: '/resetPassword',
          name: 'resetPassword',
          component: () => import('@/views/ResetPassword.vue'),
          meta: {
            allowAnonymous: true,
            restrictReload: true,
            restrictLang: true,
          },
        },
        {
          path: '/resetProfilePassword',
          name: 'resetProfilePassword',
          component: () => import('@/views/ResetProfilePassword.vue'),
          meta: {
            allowAnonymous: true,
            restrictReload: true,
            restrictLang: true,
          },
        },
        {
          path: '/depositFunds',
          name: 'depositFunds',
          component: () => import(`@/views/deposit/DepositFunds.vue`),
          meta: {
            authority: true,
          },
        },
        {
          path: '/withdrawFunds',
          name: 'withdrawFunds',
          component: () => import('@/views/withdraw/WithdrawFunds.vue'),
          meta: {
            authority: true,
          },
        },
        {
          path: '/transferFunds',
          name: 'transferFunds',
          component: () => import('@/views/TransferFunds.vue'),
          meta: {
            authority: true,
          },
        },
        {
          path: '/transactionHistory',
          name: 'transactionHistory',
          component: () => import('@/views/TransactionHistory.vue'),
          meta: {
            authority: true,
          },
        },
        {
          path: '/transferHistory',
          name: 'transferHistory',
          component: () => import('@/views/TransferHistory.vue'),
          meta: {
            authority: true,
          },
        },
        {
          path: '/downloads',
          name: 'downloads',
          component: () => import('@/views/Downloads.vue'),
        },
        {
          path: '/myProfile',
          name: 'myProfile',
          component: () => import('@/views/MyProfile.vue'),
        },
        {
          path: '/changePassword',
          name: 'changePassword',
          component: () => import('@/views/ChangePassword.vue'),
        },
        {
          path: '/contactUs',
          name: 'contactUs',
          component: () => import('@/views/ContactUs.vue'),
        },
        {
          path: '/forgetPassword',
          name: 'forgetPassword',
          component: () => import('@/views/ForgetPasswordReq.vue'),
          meta: {
            allowAnonymous: true,
          },
        },
        {
          path: '/paymentDetails',
          name: 'paymentDetails',
          component: () => import('@/views/PaymentDetails.vue'),
          meta: {
            authority: true,
          },
        },
        {
          path: '/unionpay/thirdPartyDepositResult',
          name: 'unionpayThirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult.vue'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/creditcard/thirdPartyDepositResult',
          name: 'creditcardThirdPartyDepositResult',
          component: () => import('@/views/callback/ThirdPartyDepositResult.vue'),
          meta: {
            restrictReload: true,
          },
        },
        {
          path: '/deposit/creditOrDebit',
          name: 'creditOrDebit',
          component: () => import('@/views/deposit/CreditOrDebitDeposit.vue'),
        },
        {
          path: '/deposit/internationalSwift',
          name: 'internationalSwift',
          component: () => import('@/views/deposit/InternationalSwift.vue'),
        },
        {
          path: '/deposit/mobilePay',
          name: 'mobilePay',
          component: () => import('@/views/deposit/MobilePay.vue'),
        },
        {
          path: '/deposit/unionPay',
          name: 'unionPay',
          component: () => import('@/views/deposit/UnionPay.vue'),
        },
        {
          path: '/deposit/unionPayChina',
          name: 'unionPayChina',
          component: () => import('@/views/deposit/UnionPayChina.vue'),
        },
      ],
    },
    {
      path: '',
      component: () => import('@/views/Layout'),
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