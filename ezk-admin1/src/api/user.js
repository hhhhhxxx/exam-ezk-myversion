import { post } from '@/utils/request'

export default {
  getUserPageList: query => post('/user/page/list', query),
  saveOrUpdateUser: query => post('/user/edit', query),
  selectUser: id => post('/user/select/' + id),
  getCurrentUser: () => post('/user/current'),
  updateUser: query => post('/user/update', query),
  changeStatus: id => post('/user/changeStatus/' + id),
  deleteUser: id => post('/user/delete/' + id),
  selectByUserName: query => post('/user/selectByUserName', query),

  getUserEventPageList: query => post('/api/admin/user/event/page/list', query)
}
