/**
 * Check login state
 * Some middleware to help us ensure the user is authenticated.
 */
import store from '../store'
import NProgress from 'nprogress'
// import router from '../router' 因为这里只是router.js 不是一个export default
import { router } from '../router'

export default (Vue) => {
  // 保证的第一个拦截器 从头部插入
  router.beforeHooks.unshift((to, from, next) => {
    console.log('要去哪？ to: ', to)
    // don't need authorize
    if (!to.meta.requireAuth) return next()

    // 已经初始化 没必要一次请求调一次 发生权限拒绝才重新获取菜单

    // check login state
    store.dispatch('checkToken').then((valid) => {
      // authorized
      if (valid) {
        // 刷新token
        if (to.path === '/' || to.path === '/dashboard') {
          store.dispatch('refreshToken').catch((res) => {
            store.dispatch('deleteToken')
            next({ name: 'Login' })
          })
        }
        return next()
      } else {
        // unauthorized
        console.log('Unauthorized 没有token 回到登录页')
        // redirect记住从哪里跳到登录页的 登录完回去
        next({ name: 'Login' })
      }
    })
  })

  // login page visiable
  router.beforeEach((to, from, next) => {
    NProgress.start()
    //
    if (to.name !== 'Login') {
      return next()
    } else {
      // 如果访问登录页
      store.dispatch('checkToken').then((valid) => {
        if (!valid) {
          // 没token就让他成功访问到Login页面
          return next()
        } else {
          // 有token代表登录过，直接跳去主页
          next({ path: '/' })
        }
      })
    }
  })

  router.afterEach(() => {
    // finish progress bar
    NProgress.done()
  })
}
