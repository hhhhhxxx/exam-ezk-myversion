import localStor from 'store/storages/localStorage'
import sessionStor from 'store/storages/sessionStorage'
import store from 'store'

// 可以存对象 封装了序列化
export const localstorageUtil = store.createStore(localStor)

export const sessionstorageUtil = store.createStore(sessionStor)
