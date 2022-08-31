import { storage } from '../utils/storage'
import axios from 'axios'
import { CHANGE_SESSION } from './mutations'


// 更新session
const storagePlugin = store => {
  // called when the store is initialized
  store.subscribe((mutation, state) => {
    // called after every mutation.
    // The mutation comes in the format of `{ type, payload }`.
    switch (mutation.type) {
      case CHANGE_SESSION:
        // save session
        storage.set('session', state.session)
        // console.log('更新session1',state.session)
        // localStorage.setItem('session', JSON.stringify(state.session))
        break
    }
  })
}

/**
 * @type {import('vuex/types').Plugin<typeof import('./state').default>}
 */
// 在请求头加token
const axiosPlugin = store => {
  // change axios authorization header
  if (store.state.session && store.state.session.accessToken) {
    // init axios headers
    // console.log('更新token1', store.state.session.accessToken)
    axios.defaults.headers.Authorization = `Bearer ${store.state.session.accessToken}`
  }
  // called when the store is initialized
  store.subscribe((mutation, state) => {


    // called after every mutation.
    // The mutation comes in the format of `{ type, payload }`.
    if (mutation.type !== CHANGE_SESSION) return

    // change axios default request auth token
    if (state.session && state.session.accessToken) {
      // change axios authorization header
      // console.log('更新token2', store.state.session.accessToken)
      // console.log('更新token2')
      axios.defaults.headers.Authorization = `Bearer ${state.session.accessToken}`
    }
  })
}

export default [
  storagePlugin,
  axiosPlugin
]
