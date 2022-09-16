import { post } from '@/utils/request'

export default {
  scoreAnalysis: query => post('/api/admin/analysis/score/', query),
  selectTeacherClass: query => post('/api/admin/analysis/select/class', query),
  selectByClass: query => post('/api/admin/analysis/select/class/analysis', query),
  eachSubjectPara: query => post('/api/admin/analysis/select/class/para',query),
  eachSubjectScore: query => post('/api/admin/analysis/select/class/score',query)
}
