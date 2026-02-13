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
        { path: '/', name: '/', component: () => import('@/views/home/index.vue') },
        {
          path: '/home',
          name: 'home',
          component: resolve => require(['../views/home/index.vue'], resolve),
        },
        {
          path: '/ibreport',
          name: 'ibReport',
          component: resolve => require(['../views/IbReport.vue'], resolve),
        },
        {
          path: '/rebatereport',
          name: 'rebateReport',
          component: resolve => require(['../views/RebateReport.vue'], resolve),
        },
        {
          path: '/ibaccounts',
          name: 'ibAccounts',
          component: resolve => require(['../views/IbAccounts.vue'], resolve),
        },
        {
          path: '/pendingaccounts',
          name: 'pendingAccounts',
          component: resolve => require(['../views/PendingAccounts.vue'], resolve),
        },
        {
          path: '/leads',
          name: 'leads',
          component: resolve => require(['../views/Leads.vue'], resolve),
        },
        {
          path: '/unfundedaccounts',
          name: 'unfundedAccounts',
          component: resolve => require(['../views/UnfundedAccounts.vue'], resolve),
        },
        {
          path: '/rebateTransfer',
          name: 'rebateTransfer',
          component: resolve => require(['../views/RebateTransfer.vue'], resolve),
        },
        {
          path: '/rebateWithdraw',
          name: 'rebateWithdraw',
          component: resolve => require(['../views/RebateWithdraw.vue'], resolve),
        },
        {
          path: '/settings',
          name: 'settings',
          component: resolve => require(['../views/Settings.vue'], resolve),
        },
        {
          path: '/contactUs',
          name: 'contactUs',
          component: resolve => require(['../views/ContactUs.vue'], resolve),
        },
        {
          path: '/rebatePaymentHistory',
          name: 'rebatePaymentHistory',
          component: resolve => require(['../views/RebatePaymentHistory.vue'], resolve),
        },
        {
          path: '/referralLinks',
          name: 'referralLinks',
          component: resolve => require(['../views/ReferralLinks.vue'], resolve),
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