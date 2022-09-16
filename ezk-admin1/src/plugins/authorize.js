/**
 * Check login state
 * Some middleware to help us ensure the user is authenticated.
 */
import store from '../store'
import NProgress from 'nprogress'
import router from '../router'

export default (Vue) => {

  // 保证的第一个拦截器 从头部插入
  router.beforeHooks.unshift((to, from, next) => {
    console.log('要去哪: ', to.path)
    // don't need authorize
    if (!to.meta.requireAuth) return next()

    // 已经初始化 没必要一次请求调一次 发生权限拒绝才重新获取菜单

    // getters.session()

    const navigation = (menusMap) => {
      // 权限判断
      if (to.name === 'Dashboard') {
        return next()
      } else if (menusMap[to.name]) {
        return next()
      } else {
        next({ name: 'PermissionDenied' })
      }

    }

    // check login state
    store.dispatch('checkToken').then((valid) => {
      // authorized
      if (valid) {
        // 不在侧边栏的基本功能
        if (to.meta.base === true) {
          next()
        }

        let routes = store.getters['router/routes']
        let menusMap = store.getters['router/menusMap']

        if (routes.length > 0 && menusMap) {
          // 走缓存
          return navigation(menusMap)
        } else {
          // 刷新token
          store.dispatch('refreshToken').then((res) => {
            store.dispatch('getUserPermissions').then((res) => {
              // 发请求获取
              const { menus, menusMap } = res
              store.commit('router/initRoutes', { menus, menusMap })
              return navigation(menusMap)
            })
          }).catch(res => {
            store.dispatch('deleteToken').then(res => {
              next({ name: 'Login' })
            })
          })
        }
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

    // 统计访问量之类的
    if (to.path) {
      // eslint-disable-next-line no-undef
      _hmt.push(['_trackPageview', '/#' + to.fullPath])
    }

    console.log(31)

    if (to.name === 'Login') {
      return next()
    } else {
      // 如果访问登录页
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
    console.log(4)
  })
}
