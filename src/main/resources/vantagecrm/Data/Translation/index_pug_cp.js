import Vue from 'vue'
import VueRouter from 'vue-router'
import identity from './identity'

Vue.use(VueRouter)

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
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
          component: () => import('@/views/register/Authority'),
        },
        {
          path: '/registerFinish',
          name: 'registerFinish',
          component: () => import('@/views/register/RegisterFinish'),
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
          path: '/depositFunds',
          name: 'depositFunds',
          component: () => import(`@/views/deposit/DepositFunds`),
          meta: {
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
          path: '/transferHistory',
          name: 'transferHistory',
          component: () => import('@/views/TransferHistory'),
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
          path: '/changePassword',
          name: 'changePassword',
          component: () => import('@/views/ChangePassword'),
        },
        {
          path: '/contactUs',
          name: 'contactUs',
          component: () => import(`@/views/ContactUs`),
        },
        {
          path: '/paymentDetails',
          name: 'paymentDetails',
          component: () => import('@/views/PaymentDetails'),
          meta: {
            authority: true,
          },
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
        // pages of deposit
        {
          path: '/deposit/apg',
          name: 'apgDeposit',
          component: () => import('@/views/deposit/CreditOrDebit/ApgPay'),
        },
        {
          path: '/deposit/sdPay/:id',
          name: 'sdPayDeposit',
          component: () => import('@/views/deposit/CreditOrDebit/Sdpayment'),
        },
        {
          path: '/deposit/virtualPay',
          name: 'virtualPayDeposit',
          component: () => import('@/views/deposit/CreditOrDebit/VirtualPay'),
        },
        {
          path: '/deposit/bitwalletDeposit',
          name: 'bitwalletDeposit',
          component: () => import('@/views/deposit/BitwalletDeposit'),
        },
        {
          path: '/deposit/dragonPhoenixDeposit',
          name: 'dragonPhoenixDeposit',
          component: () => import('@/views/deposit/DragonPhoenixDeposit'),
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
          path: '/deposit/canadaLocalBankDeposit',
          name: 'canadaLocalBank',
          component: () => import('@/views/deposit/CanadaLocalBankDeposit'),
        },
        {
          path: '/deposit/mobilePay',
          name: 'mobilePay',
          component: () => import('@/views/deposit/MobilePay'),
        },
        {
          path: '/deposit/sticPay',
          name: 'sticPay',
          component: () => import('@/views/deposit/SticPay'),
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
          path: '/deposit/VietnamZotapay',
          name: 'VietnamZotapay',
          component: () => import(`@/views/deposit/VietnamZotapay`),
        },
        {
          path: '/deposit/MalaysiaPayment',
          name: 'MalaysiaPayment',
          component: () => import(`@/views/deposit/MalaysiaPayment`),
        },
        {
          path: '/deposit/PhilipinessPayment',
          name: 'PhilipinessPayment',
          component: () => import(`@/views/deposit/PhilipinessPayment`),
        },
        {
          path: '/deposit/indonesiaBankwire',
          name: 'indonesiaBankwire',
          component: () => import(`@/views/deposit/indonesia/Bankwire`),
        },
        {
          path: '/deposit/indonesiaXpay',
          name: 'indonesiaXpay',
          component: () => import(`@/views/deposit/indonesia/Xpay`),
        },
        {
          path: '/deposit/bitcoin',
          name: 'bitcoin',
          component: () => import('@/views/deposit/crypto/Bitcoin'),
        },
        {
          path: '/deposit/usdt/:id',
          name: 'usdt',
          component: () => import('@/views/deposit/crypto/USDT'),
        },
        {
          path: '/deposit/usdt_cps',
          name: 'usdt_cps',
          component: () => import('@/views/deposit/crypto/USDT_CPS'),
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
          component: () => import(`@/views/LoginClient`),
          meta: {
            allowAnonymous: true,
            noShowLoading: true,
          },
        },
        {
          path: '/login/to_login',
          name: 'to_login',
          component: () => import(`@/views/LoginClient`),
          meta: {
            allowAnonymous: true,
            noShowLoading: true,
          },
        },
        {
          path: '/forgetPassword',
          name: 'forgetPassword',
          component: () => import(`@/views/ForgetPasswordReq`),
          meta: {
            allowAnonymous: true,
          },
        },
        {
          path: '/resetProfilePassword',
          name: 'resetProfilePassword',
          component: () => import(`@/views/ResetProfilePassword`),
          meta: {
            allowAnonymous: true,
          },
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
          path: '/error',
          name: 'error',
          component: () => import('@/views/register/Error'),
          meta: {
            allowAnonymous: true,
            noShowLoading: true,
            restrictBanner: true,
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