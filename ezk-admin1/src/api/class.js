import { post,get } from '@/utils/request'

export default {
  // 班级
  classList: query => post('/api/examsystem/class/page/list', query),

  saveOrUpdateClass: query => post('/api/examsystem/class/SaveOrUpdate', query),
  deleteClass: id => post('/api/examsystem/class/delete/' + id),
  getClassById: id => get('/api/examsystem/class/get/' + id),

  // 学生
  studentList: query => post('/user/page/student', query),
  teacherList: query => post('/user/page/teacher', query),


  addToClass: query => post('/api/examsystem/class/bind/' ,query),
  removeOutClass: query => post('/api/examsystem/class/delBind/',query)
}
