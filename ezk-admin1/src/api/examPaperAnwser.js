import { post } from '@/utils/request'

export default {
  page: query => post('/api/admin/examPaperAnswer/page', query),
  answerList: query => post('/api/admin/examPaperAnswer/answerList', query),
  titleList: query => post('/api/exam/paper/total/title', query),
  getFinishPaperList: query => post('/api/exam/paper/finish', query),
  checkPaper: query => post('/api/exam/paper/finish/check', query)
}
