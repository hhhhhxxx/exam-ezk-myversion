import {
  CHANGE_SESSION
} from './mutation-types'

import bossApi from '@/api/boss'

const actions = {
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
  getCurrentUser: async ({ commit }) => {
    const res = await bossApi.getUserInfo()
    commit(CHANGE_SESSION, { user: res.content })
    return res.data
  },
  /**
   * 本地定时更新token
   */
  refreshToken: async ({ commit, state }) => {
    const { refreshToken } = state.session

    const res = await bossApi.refreshToken(refreshToken)
    // request.js 那一层 直接返回res.data 不像拦截器 是真正的response

    if (res.code !== 1) {
      console.log('刷新返回结果', res)
      return Promise.reject(res)
    }

    const result = res.response
    commit(CHANGE_SESSION, { accessToken: result.access_token, refreshToken: result.refresh_token })
    return Promise.resolve(res)

  }
}

export default actions
