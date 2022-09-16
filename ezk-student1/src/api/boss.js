import { get, postNoAuthHeader } from '@/utils/request'

export default {
  // 没用到 也没这个接口
  getUserInfo: () => get('/boss/user/getInfo'),

  // 登录调用
  refreshToken: (refreshToken) => postNoAuthHeader(`/user/refresh_token?refreshtoken=${refreshToken}`)
}
