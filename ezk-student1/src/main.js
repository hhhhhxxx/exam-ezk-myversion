import Vue from 'vue'
import App from './App.vue'
import { router } from './router'
import store from './store'
import 'normalize.css/normalize.css'
import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

import '@/styles/index.scss' // global css
import './icons' // icon
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css'
// import axios from 'axios' // progress bar style

import plugins from './plugins'

Vue.use(plugins)

Vue.use(Element, {
  size: 'medium' // set element-ui default size
})

Vue.config.productionTip = false

// Vue.prototype.$axios = axios

NProgress.configure({ showSpinner: false }) // NProgress Configuration

// router.beforeEach(async (to, from, next) => {
//   // start progress bar
//   NProgress.start()
//   if (to.meta.title !== undefined) {
//     document.title = to.meta.title
//   } else {
//     document.title = '\u200E'
//   }
//
//   if (to.meta.bodyBackground !== undefined) {
//     document.querySelector('body').setAttribute('style', 'background: ' + to.meta.bodyBackground)
//   } else {
//     document.querySelector('body').removeAttribute('style')
//   }
//
//   if (to.path) {
//     // eslint-disable-next-line no-undef
//     _hmt.push(['_trackPageview', '/#' + to.fullPath])
//   }
//   next()
// })
//
// router.afterEach((to, from, next) => {
//   // finish progress bar
//   NProgress.done()
// })

//  js 日期格式化
// eslint-disable-next-line no-extend-native
Date.prototype.format = function (fmt) {
  var o = {
    'M+': this.getMonth() + 1,                 // 月份
    'd+': this.getDate(),                    // 日
    'h+': this.getHours(),                   // 小时
    'm+': this.getMinutes(),                 // 分
    's+': this.getSeconds(),                 // 秒
    'q+': Math.floor((this.getMonth() + 3) / 3), // 季度
    'S': this.getMilliseconds()             // 毫秒
  }
  if (/(y+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length))
  }
  for (var k in o) {
    if (new RegExp('(' + k + ')').test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)))
    }
  }
  return fmt
}

Vue.prototype.$$router = router

new Vue({
  router: router,
  store: store,
  render: h => h(App)
}).$mount('#app')
