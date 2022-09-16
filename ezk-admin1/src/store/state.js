import { localstorageUtil } from '../utils/storage'

export default {
  // 客户端会话信息
  session: localstorageUtil.get('session') ?? {}
}
