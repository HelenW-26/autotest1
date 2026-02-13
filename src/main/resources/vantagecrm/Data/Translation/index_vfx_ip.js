import Vue from 'vue'
import Router from 'vue-router'


Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [

    {
      path: '/home',
      name: 'home',
      component: resolve => require(['../views/home/index.vue'], resolve)
    },
    {
      path: '/marketingstools',
      name: 'marketingstools',
      component: resolve => require(['../views/MarketingTools.vue'], resolve)
    },
    {
      path: '/ibreport',
      name: 'ibReport',
      component: resolve => require(['../views/IbReport.vue'], resolve)
    },
    {
      path: '/rebatereport',
      name: 'rebateReport',
      component: resolve => require(['../views/RebateReport.vue'], resolve)
    },
    {
      path: '/ibaccounts',
      name: 'ibAccounts',
      component: resolve => require(['../views/IbAccounts.vue'], resolve)
    },
    {
      path: '/pendingaccounts',
      name: 'pendingAccounts',
      component: resolve => require(['../views/PendingAccounts.vue'], resolve)
    },
    {
      path: '/leads',
      name: 'leads',
      component: resolve => require(['../views/Leads.vue'], resolve)
    },
    {
      path: '/unfundedaccounts',
      name: 'unfundedAccounts',
      component: resolve => require(['../views/UnfundedAccounts.vue'], resolve)
    },
    {
      path: '/payment',
      name: 'payment',
      component: resolve => require(['../views/Payment.vue'], resolve)
    },
    {
      path: '/multilevel',
      name: 'multilevel',
      component: resolve => require(['../views/Multilevel.vue'], resolve)
    },
    {
      path: '/rebateTransfer',
      name: 'rebateTransfer',
      component: resolve => require(['../views/RebateTransfer.vue'], resolve)
    },
    {
      path: '/RebateWithdraw',
      name: 'RebateWithdraw',
      component: resolve => require(['../views/RebateWithdraw.vue'], resolve)
    },
    {
      path: '/settings',
      name: 'settings',
      component: resolve => require(['../views/Settings.vue'], resolve)
    },
    {
      path: '/contactUs',
      name: 'contactUs',
      component: resolve => require(['../views/ContactUs.vue'], resolve)
    },
    {
      path: '/rebatePaymentHistory',
      name: 'rebatePaymentHistory',
      component: resolve => require(['../views/RebatePaymentHistory.vue'], resolve)
    }
    
  ]
})