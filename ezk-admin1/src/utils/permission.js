// 过滤路由
export function filterAsyncRouter (asyncRouterMap, menus, memusMap) {

  const accessedRouters = asyncRouterMap.filter((route, index, arr) => {
    if (memusMap[route.name]) {

      // 子菜单过滤
      let children = route.children.filter(subRoute => {
        if (memusMap[subRoute.name] && memusMap[subRoute.name].shown === true) {
          return true
        } else {
          return false
        }
      })

      arr[index].children = children

      return true
    } else {
      return false
    }
  })



  // 主页置顶
  accessedRouters.unshift(asyncRouterMap[0])

  return accessedRouters
}
