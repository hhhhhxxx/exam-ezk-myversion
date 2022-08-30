import { storage } from '../utils'
import axios from 'axios'
import { CHANGE_SESSION } from './mutation-types'

/**
 * @type {import('vuex/types').Plugin<typeof import('./state').default>}
 */
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
    axios.defaults.headers.Authorization = `${store.state.session.accessToken}`
  }
  // called when the store is initialized
  store.subscribe((mutation, state) => {
    // called after every mutation.
    // The mutation comes in the format of `{ type, payload }`.
    if (mutation.type !== CHANGE_SESSION) return

    // change axios default request auth token
    if (state.session && state.session.accessToken) {
      // change axios authorization header
      axios.defaults.headers.Authorization = `${state.session.accessToken}`
    }
  })
}

export default [
  storagePlugin,
  axiosPlugin
]
