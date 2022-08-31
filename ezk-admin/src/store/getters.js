const getters = {
  // 原先侧边栏
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews,
  userName: state => state.user.userName,


  // 权限相关vuex getters
  session: state => state.session
}
export default getters
