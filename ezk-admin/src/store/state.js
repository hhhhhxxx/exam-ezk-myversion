import { storage } from '../utils'

export default {
  /**
   * 客户端会话信息
   */
  // session: storage.get('session') ?? {}

  session:  JSON.parse(localStorage.getItem('session') ?? '{}'),

  tokenTimeOut: null
}