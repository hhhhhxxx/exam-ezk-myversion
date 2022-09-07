import { post,get } from '@/utils/request'

export default {
  // 班级
  classList: query => post('/api/examsystem/class/list', query),
  saveOrUpdateClass: query => post('/api/examsystem/class/SaveOrUpdate', query),
  deleteClass: id => post('/api/examsystem/class/delete/' + id),
  getClassById: id => get('/api/examsystem/class/get/' + id),

  // 学生
  studentList: query => post('/auth/auth/getStuPages', query),
  addStudent: query => post('/api/examsystem/class/bind/' ,query),
  removeStudent: query => post('/api/examsystem/class/delBind/',query)
}
