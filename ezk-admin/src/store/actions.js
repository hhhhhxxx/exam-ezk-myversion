import {
  CHANGE_SESSION,
} from './mutations'
import { axios } from '../utils'

import bossApi from '@/api/boss'

const actions = {
  /**
   * 创建新的客户端令牌
   */
  // createToken: async ({ commit }, { userName, password }) => {
  //
  //   const res = await TokenService.userLogin({
  //     username: userName.trim(),
  //     password: password.trim()
  //   })
  //
  //   if (res.state !== 1) {
  //     return Promise.resolve(res)
  //   }
  //   const result = JSON.parse(res.content)
  //   commit(CHANGE_SESSION, { accessToken: result.access_token, refreshToken: result.refresh_token })
  //   return res
  // },

  /**
   * 检查客户端令牌是否可用
   */
  checkToken: async ({ commit, getters }) => {
    // validate local store
    if (!getters.session.accessToken) {
      return Promise.resolve(false)
    }
    return Promise.resolve(true)
  },

  /**
   * 删除客户端令牌
   */
  deleteToken: async ({ commit, getters }) => {
    commit(CHANGE_SESSION, { accessToken: null, refreshToken: null })
    await Promise.resolve()
  },

  /**
   * 获取当前登录用户信息
   */
  // getCurrentUser: async ({ commit }) => {
  //   const res = await UserService.getUserInfo()
  //   commit(CHANGE_SESSION, { user: res.content })
  //   return res.data
  // },

  /**
   * 获取当前登录用户权限
   */
  getUserPermissions: async ({ commit }) => {

    const res = await bossApi.getUserPermissions()

    const { menuList, resourceList } = res.content

    let menus = []
    const menusMap = {}

    const splapMenu = (treeData) => {
      // 是list走这里
      if (treeData.length > 0) {
        return treeData.map(item => splapMenu(item))
      }
      // 单体走这里
      const result = {}
      result.id = treeData.id
      result.text = treeData.name
      result.label = treeData.name
      result.name = treeData.href
      result.icon = treeData.icon
      result.shown = treeData.shown
      // 如果有名字 就加入map
      result.name && (menusMap[result.name] = result)

      // 判断有没有子菜单
      if (treeData.subMenuList) {
        result.children = []
        treeData.subMenuList.forEach(item => {
          result.children.push(splapMenu(item))
        })
      }
      return result
    }

    const formatMenu = (treeData) => {
      if (treeData.length > 0) {
        return treeData.map(item => formatMenu(item))
      }
      const result = {}
      if (treeData.shown) {
        result.id = treeData.id
        result.text = treeData.name
        result.label = treeData.name
        result.name = treeData.href // 这里重点 router是通过name来路由的，原来的name反而变成text label，而href变成真正的name
        result.icon = treeData.icon
        result.shown = treeData.shown
      } else {
        return ''
      }
      // 有孩子就递归
      if (treeData.subMenuList) {
        result.children = []
        treeData.subMenuList.forEach(item => {
          // 这里相当于一个条件判断 a 返回true 才执行b
          formatMenu(item) && result.children.push(formatMenu(item))
        })
        if (result.children.length === 0) {
          delete result.children
        }
      }
      return result
    }

    // 相当于拷贝对象，字段不一样了, ，
    // 这一句是为了构造map
    splapMenu(menuList)  // map方法返回新数组 不会改变原数组 所以这一句无效

    // 这个不保留shown = true 的对象
    menus = formatMenu(menuList)
    // 打印一下两个list
    console.log('menus:', menus)
    console.log('menusMap:', menusMap)

    // commit(CHANGE_SIDERBAR_MENU, menus)

    // menusMap记录所有页面路由 不分级
    return { menus, resourceList, menuList, menusMap }
  },

  /**
   * 本地定时更新token
   */
  refreshToken: async ({ commit, state }) => {
    const { refreshToken } = state.session

    const res = await bossApi.refreshToken(refreshToken)
    // request.js 那一层 直接返回res.data 不像拦截器 是真正的response

    if (res.code !== '000000') {
      console.log('刷新返回结果', res)
      return Promise.reject(res)
    }

    const result = res.data
    commit(CHANGE_SESSION, { accessToken: result.access_token, refreshToken: result.refresh_token })
    return Promise.resolve(res)
  }
}

export default actions
