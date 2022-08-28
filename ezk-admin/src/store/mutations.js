// 改变session token重点
export const CHANGE_SESSION = 'CHANGE_SESSION'



/**
 * @type {import('vuex/types').MutationTree<typeof import('./state').default>}
 */
const mutations = {
  /**
   * 改变客户端会话信息
   */
  [CHANGE_SESSION]: (state, session) => {
    // TODO: new session mixin
    Object.assign(state.session, session)
    // console.log("调用更新session")
  }

}

export default mutations
