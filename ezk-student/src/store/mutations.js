import { CHANGE_SESSION } from './mutation-types'
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
  }
}
export default mutations
