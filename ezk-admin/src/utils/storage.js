import localStor from 'store/storages/localStorage'
import store from 'store'

// 可以存对象 封装了序列化
export const storage = store.createStore(localStor);
