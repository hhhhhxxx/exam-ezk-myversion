/**
 * Custom axios instance
 */
import axios from 'axios'

// /**
//  * Custom axios instance
//  */
import { storage } from '../utils/storage'
import store from '../store'

//  标志当前是否正在刷洗token
let isNotRefreshing = true

axios.interceptors.response.use(
  response => {
    // console.log('新的响应来了', response.config.url)
    if (!response.data.hasOwnProperty('response') || !response.data.hasOwnProperty('code') || response.data.code !== 40005) {
      return Promise.resolve(response)
    }

    // ouath token过期 无感刷新

    //  首先拿到响应的配置参数，这和请求的配置参数是一样的，包括了url、data等信息，待会需要使用这个config来进行重发
    const config = response.config

    if (isNotRefreshing) {
      console.log('刷新token')
      // 改为正在刷新
      isNotRefreshing = false

      return store.dispatch('refreshToken').then((res) => {
        console.log('刷新token成功')
        // 刷新token后重新设置 冲请求中获取，从session中获取有延迟
        const accessToken = res.response.access_token

        // axios.defaults.headers.Authorization = `Bearer ${accessToken}`

        // 更新有延迟 直接重新设置
        config.headers.Authorization = `Bearer ${accessToken}`

        isNotRefreshing = true
        console.log('重发', config.url)
        // 第一个失败的请求最后处理 返回 promise
        return axios(config)
      }).catch(res => {
        console.log('刷新失败')
        store.dispatch('deleteToken')
        return Promise.reject(response)
      })
    } else {
      console.log('同一时间失败的请求')

      // 一秒之后 等token更新重新发送 （当等于分时系统，一人一点时间，js是单线程的）
      return new Promise((resolve, reject) => {
        setTimeout(() => {
          if (isNotRefreshing) {
            const accessToken = store.state.session.accessToken
            config.headers.Authorization = `Bearer ${accessToken}`
            console.log('重发', config.url)
            return resolve(axios(config))
          } else {
            console.log('还没更新token')
            return reject(response)
          }
        }, 1000)
      })
    }

    // return Promise.resolve(response)
  },
  // 服务器状态码不是2开头的的情况
  // 这里可以跟你们的后台开发人员协商好统一的错误状态码
  // 然后根据返回的状态码进行一些操作，例如登录过期提示，错误提示等等
  // 下面列举几个常见的操作，其他需求可自行扩展
  error => {
    if (error.response.code) {
      switch (error.response.code) {
        case 40005: // 没有授权 权限不足
          break
        default:
      }
      console.log('请求错误', error.response)
      return Promise.reject(error.response)
    }
  }
)
axios.defaults.withCredentials = true // carry cookie（携带 cookie）

export default axios
