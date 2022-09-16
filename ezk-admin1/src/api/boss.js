import { post,get,postNoAuthHeader } from "@/utils/request";


export default {
  // 没用到 也没这个接口
  getUserInfo: () => get('/boss/user/getInfo'),

  // 登录调用
  getUserPermissions: () => get('/boss/permission/getUserPermissions'),
  refreshToken: (refreshToken) => postNoAuthHeader(`/user/refresh_token?refreshtoken=${refreshToken}`),

  // 用户管理------------
  getUserPages : (params) => post('/boss/user/getUserPages',params),
  forbidUser: (params) => post('/boss/user/forbidUser',params),

  getRoleAll: () => get('/boss/role/all'),
  getRolesById : (userId) => get('/boss/role/user/'+userId),
  allocRole : (params) => post('/boss/role/allocateUserRoles',params),

  // 角色管理------------
  getRolePages: (params) => post('/boss/role/getRolePages',params),
  // 穿不穿id 决定是更新还是添加
  saveOrUpdateRole:(params) => post('/boss/role/SaveOrUpdate',params),
  deleteRole: (id) => post('/boss/role/'+id),


  // 角色分配菜单
  listMenuByRole: (roleId) => {
    return get('/boss/menu/getRoleMenus/'+roleId).then(res => {
      return Promise.resolve(SelectTreeData(res.response))
    })
  },
  allocMenu: (params) => post('/boss/menu/allocateRoleMenus',params),
  getMenuNodeList: (params) =>  get('/boss/menu/getMenuNodeList',params),

  // 角色分配资源
  fetchAllResourceList: (params) =>  get('/boss/resource/getAll',params),
  listAllCate: (params) =>  get('/boss/resource/category/getAll',params),

  listResourceByRole:(roleId)=> get('/boss/resource/getRoleResources/'+roleId),
  allocResource: (params) => post('/boss/resource/allocateRoleResources',params),


  // 菜单管理
  fetchAllMenuList: (parentId,params) =>  get('/boss/menu/getAll',params),

  deleteMenu: (id) =>  post('/boss/menu/' + id),

  saveOrUpdateMenu: (params) =>  post('/boss/menu/saveOrUpdate',params),

  getMenu: (id) =>  get('/boss/menu/getEditMenuInfo/'+id),

  // 资源管理
  fetchResourceList: (params) =>  post('/boss/resource/getResourcePages',params),
  saveOrUpdateResource: (params) =>  post('/boss/resource/saveOrUpdate',params),
  deleteResource: (id) =>  post('/boss/resource/delete/'+id),


  // 资源分类
  saveOrUpdateResourceCategory : (params) =>  post('/boss/resource/category/saveOrderUpdate',params),
  deleteResourceCategory: (id) =>  post('/boss/resource/category/delete/'+id),



}

function SelectTreeData (treeData) {
  let result = []

  if (treeData.length > 0) {
    treeData.forEach(item => {
      result = result.concat(SelectTreeData(item))
      return item
    })
    return result
  }
  if (treeData.selected) {
    result.push(treeData.id)
  }
  // text: item.name,
  //   icon: 's-marketing',
  //   name: item.href
  if (treeData.subMenuList) {
    let childrens = []
    childrens = childrens.concat(SelectTreeData(treeData.subMenuList))
    if (childrens.length > 0) {
      result.pop()
      result = result.concat(childrens)
    }
  }
  return result
}


