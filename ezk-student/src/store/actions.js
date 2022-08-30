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
    if (!res) {
      console.error('刷新token错误')
      return Promise.resolve()
    }
    const result = res.data
    commit(CHANGE_SESSION, { accessToken: result.access_token, refreshToken: result.refresh_token })
    return Promise.resolve()
  }
}

export default actions
