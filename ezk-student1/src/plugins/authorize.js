/**
 * Check login state
 * Some middleware to help us ensure the user is authenticated.
 */
import store from '../store'
import NProgress from 'nprogress'
// import router from '../router' 因为这里只是router.js 不是一个export default
import { router } from '../router'

export default (Vue) => {
  // login page visiable
  router.beforeEach((to, from, next) => {
    NProgress.start()

    // 统计访问量之类的
    if (to.path) {
      // eslint-disable-next-line no-undef
      _hmt.push(['_trackPageview', '/#' + to.fullPath])
    }

    if (to.name === 'Login') {
      return next()
    } else {
      // 如果访问其他页
      store.dispatch('checkToken').then((valid) => {
        if (valid) {
          // 有token代表登录过，直接跳去主页
          next()
        } else {
          // 没token就让他成功访问到Login页面
          next({ name: 'Login' })
        }
      })
    }
  })

  router.afterEach(() => {
    // finish progress bar
    NProgress.done()
  })
}
