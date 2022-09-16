import { post } from '@/utils/request'

export default {
  saveOrUpdate: query => post('/user/edit', query),
  getCurrentUser: () => post('/user/current'),

  getUserEvent: () => post('/api/student/user/log'),
  messagePageList: query => post('/api/student/user/message/page', query),
  read: id => post('/api/student/user/message/read/' + id),
  getMessageCount: () => post('/api/student/user/message/unreadCount')
}
