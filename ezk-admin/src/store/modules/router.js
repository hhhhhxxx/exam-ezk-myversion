import { asyncRouterMap } from '@/router/routes'
import { filterAsyncRouter } from '@/utils/permission'

// import routes from '../../router/routes'

import { sessionstorageUtil } from '@/utils/storage'

const state = {
  routes: [],
  menusMap: {}
}

const routesKey = 'routes'
const menusMapKey = 'menusMap'

const mutations = {
  initRoutes: (state, { menus, menusMap }) => {
    // asyncRouter
    let asyncRouter = filterAsyncRouter(asyncRouterMap, menus, menusMap)

    // cookies 15分钟过期时间
    // var inFifteenMinutes = new Date(new Date().getTime() + 15 * 60 * 1000)

    console.log('cookie存了吗？')

    // 必须跟新state 才能使得getters拿到最新数据
    state.routes = asyncRouter
    state.menusMap = menusMap

    sessionstorageUtil.set(routesKey,asyncRouter)
    sessionstorageUtil.set(menusMapKey,menusMap)

    // sessionStorage.setItem(routesKey, JSON.stringify(asyncRouter))
    // sessionStorage.setItem(menusMapKey, JSON.stringify(menusMap))
  },

  clearRoutesCache: (state) => {
    state.routes = []
    state.menusMap = {}

    // 缓存同一页面的数据，关闭窗口消失
    // sessionStorage.removeItem(routesKey)
    // sessionStorage.removeItem(menusMapKey)
    sessionstorageUtil.remove(routesKey)
    sessionstorageUtil.remove(menusMapKey)
  }
}

const getters = {
  // 菜单和menuMap
  routes: (state) => {
    // state.routes = JSON.parse(sessionStorage.getItem(routesKey) ?? '[]')
    state.routes = sessionstorageUtil.get(routesKey) ?? []
    return state.routes
  },
  menusMap: (state) => {
    // state.menusMap = JSON.parse(sessionStorage.getItem(menusMapKey) ?? '{}')
    state.menusMap = sessionstorageUtil.get(menusMapKey) ?? {}
    return state.menusMap
  }
}

const actions = {
  // 空着
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters
}
