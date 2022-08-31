import { storage } from '../utils'
// import { storage } from '@/utils/index'
export default {
  // 客户端会话信息
  session: storage.get('session') ?? {}
}
