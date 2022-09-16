/**
 * My plugins
 */

import axios from './axios'
import authorize from './authorize'

export default {
  install (Vue) {
    axios(Vue)
    authorize(Vue)
  }
}
