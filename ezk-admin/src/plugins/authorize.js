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
    console.log('要去哪？to: ', to)
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
      } else if (Object.keys(menusMap).length > 0) {
        // 走子路径
        return next({ name: menusMap[Object.keys(menusMap)[0]].name })
      } else {
        next({ name: 'PermissionDenied' })
      }
    }

    // check login state
    store.dispatch('checkToken').then((valid) => {
      // authorized
      if (valid) {

        let routes = store.getters['router/routes']
        let menusMap = store.getters['router/menusMap']

        console.log('routes', routes)
        console.log('menusMap', menusMap)
        console.log('routes长度', routes.length)

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
};
