import Vue from 'vue'
import Vuex from 'vuex'

import getters from './getters'

// 权限
import state from './state'
import mutations from './mutations'
import actions from './actions'
import plugins from './plugins'

Vue.use(Vuex)

// store除了getters和modules里面的部分，其他都不原来的

// https://webpack.js.org/guides/dependency-management/#requirecontext
const modulesFiles = require.context('./modules', true, /\.js$/)

// you do not need `import app from './modules/app'`
// it will auto require all vuex module from modules file
const modules = modulesFiles.keys().reduce((modules, modulePath) => {
  // set './app.js' => 'app'
  const moduleName = modulePath.replace(/^\.\/(.*)\.\w+$/, '$1')
  const value = modulesFiles(modulePath)
  modules[moduleName] = value.default
  return modules
}, {})

// vuex是否启动严格模式
// const strict = process.env.NODE_ENV !== 'production'

const store = new Vuex.Store({
  modules,
  getters,
  mutations,
  actions,
  state,
  plugins
})

export default store
