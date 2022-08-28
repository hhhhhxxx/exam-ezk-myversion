import Cookies from 'js-cookie'
import userApi from '@/api/user'

// 调用别的
import store from '../../store'

// initial state
const state = {
  userName: Cookies.get('adminUserName'),
  userInfo: Cookies.get('adminUserInfo')
}

// actions
const actions = {
  initUserInfo ({ commit }) {
    userApi.getCurrentUser().then(re => {
      commit('setUserInfo', re.response)
    })
  }
}

// mutations
const mutations = {
  setUserName (state, userName) {
    state.userName = userName
    Cookies.set('adminUserName', userName, { expires: 30 })
  },
  setUserInfo: (state, userInfo) => {
    state.userInfo = userInfo
    Cookies.set('adminUserInfo', userInfo, { expires: 30 })
  },
  clearLogin (state) {
    Cookies.remove('adminUserName')
    Cookies.remove('adminUserInfo')

    localStorage.removeItem('session')

    // 清除侧边栏缓存
    store.commit('router/clearRoutesCache')
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
