/*
使用mockjs提供mock数据接口
 */
import Mock from 'mockjs'
import data from './data.json'
// Mock.mock('/api/admin/dashboard/index', { code: 0, data: data.dashboard })

// Mock.mock('/api/admin/user/page/list', { code: 0, response: data.userList })
// 返回管理员的接口
// 返回info的接口
// Mock.mock('/user/student/edit', {code:0, data: data.info})
// Mock.mock('/api/admin/exam/paper/page', { code: 0, response: data.pageList })
// Mock.mock('/api/admin/education/subject/list', { code: 0, response: data.subjectList })

// Mock.mock('/api/admin/examPaperAnswer/page', { code: 0, response: data.paperList })
// 考试人员
Mock.mock('/api/admin/exam/paper/people', { code: 0, response: data.people })

Mock.mock('/api/admin/examPaperAnswer/answerList', { code: 0, response: data.correctPaper })

Mock.mock('/api/admin/analysis/score/', { code: 0, data: data.score })

Mock.mock('/api/admin/exam/paper/totalTitle', { code: 0, response: data.totalTitle })

// Mock.mock('/api/examsystem/texaminfo/getExamInfoList')
//
// Mock.mock(RegExp('/api/examsystem/texamarguments/list' + '.*'), 'get', function (options) {
//   return {
//     'code': 0,
//     'page': {
//       'list': [
//         {
//           'id': '1003',
//           'examPaperId': '123456',
//           'examStartTime': '2022-03-13 08:00',
//           'examEndTime': '2022-03-13 10:00',
//           'limitSubmitTime': '30min',
//           'limitEnterTime': '30min',
//           'examBatch': 1,
//           'examRound': 2,
//           'enableMonitor': 1,
//           'limitScreenCount': 2,
//           'passScore': 60,
//           'allowRedo': 2,
//           'allowCheckScore': 1,
//           'allowMultidevice': 0
//         },
//         {
//           'id': '1004',
//           'examPaperId': '123456',
//           'examStartTime': '2022-03-13 08:00',
//           'examEndTime': '2022-03-13 10:00',
//           'limitSubmitTime': '30min',
//           'limitEnterTime': '30min',
//           'examBatch': 1,
//           'examRound': 2,
//           'enableMonitor': 0,
//           'limitScreenCount': 2,
//           'passScore': 60,
//           'allowRedo': 2,
//           'allowCheckScore': 1,
//           'allowMultidevice': 2
//         },
//         {
//           'id': '1005',
//           'examPaperId': '123456',
//           'examStartTime': '2022-03-13 08:00',
//           'examEndTime': '2022-03-13 10:00',
//           'limitSubmitTime': '30min',
//           'limitEnterTime': '30min',
//           'examBatch': 1,
//           'examRound': 2,
//           'enableMonitor': 1,
//           'limitScreenCount': 2,
//           'passScore': 60,
//           'allowRedo': 2,
//           'allowCheckScore': 0,
//           'allowMultidevice': 0
//         },
//         {
//           'id': '1006',
//           'examPaperId': '123456',
//           'examStartTime': '2022-03-13 08:00',
//           'examEndTime': '2022-03-13 10:00',
//           'limitSubmitTime': '30min',
//           'limitEnterTime': '30min',
//           'examBatch': 1,
//           'examRound': 2,
//           'enableMonitor': 1,
//           'limitScreenCount': 2,
//           'passScore': 60,
//           'allowRedo': 2,
//           'allowCheckScore': 1,
//           'allowMultidevice': 1
//         }
//       ],
//       'totalCount': 4
//     }
//   }
// })

// Mock.mock(RegExp('/api/examsystem/manage/list' + '.*'), 'get', function (options) {
//   return {
//     'response': [
//       {
//         'examPaperEditRequestVM': {
//           'id': '1001',
//           'name': 'hhhhh',
//           'score': 30,
//           'teacherName': '张三'
//         },
//         'examStartTime': '2022-03-28 08:00',
//         'examEndTime': '2022-03-28 10:00',
//         'examBatch': 2,
//         'examRound': 3,
//         'passScore': 60,
//         'allowRedo': 5,
//         'status': '0'
//
//       },
//       {
//         'examPaperEditRequestVM': {
//           'id': '1002',
//           'name': 'hhhhh',
//           'score': 30,
//           'teacherName': '张三'
//         },
//         'examStartTime': '2022-03-28 08:00',
//         'examEndTime': '2022-03-28 10:00',
//         'examBatch': 2,
//         'examRound': 3,
//         'passScore': 60,
//         'allowRedo': 5,
//         'status': '1'
//       },
//       {
//         'examPaperEditRequestVM': {
//           'id': '1002',
//           'name': 'hhhhh',
//           'score': 30,
//           'teacherName': '张三'
//         },
//         'examStartTime': '2022-03-28 08:00',
//         'examEndTime': '2022-03-28 10:00',
//         'examBatch': 2,
//         'examRound': 3,
//         'passScore': 60,
//         'allowRedo': 5,
//         'status': '2'
//       }
//     ]
//   }
// })
//
// Mock.mock('/api/examsystem/texaminfo/getExamInfoList', function () {
//   return {
//     entityList: [
//       {
//         'studentname': '张三',
//         'numOverCount': 3,
//         'exitFullCount': 5,
//         'nonLiveCount': 2,
//         'checkSex': 0,
//         'beforeExamImg': '@/assets/image/1.jpg',
//         'duringExamImg': '@/assets/image/2.jpg',
//         'createTime': '2022-03-28 14:00'
//       },
//       {
//         'studentname': '李四',
//         'numOverCount': 11,
//         'exitFullCount': 12,
//         'nonLiveCount': 12,
//         'checkSex': 1,
//         'beforeExamImg': '@/assets/image/1.jpg',
//         'duringExamImg': '@/assets/image/2.jpg',
//         'createTime': '2022-03-28 14:00'
//       },
//       {
//         'studentname': '赵武',
//         'numOverCount': 7,
//         'exitFullCount': 8,
//         'nonLiveCount': 11,
//         'checkSex': 3,
//         'beforeExamImg': '../../../assets/image/1.jpg',
//         'duringExamImg': '../../../assets/image/2.jpg',
//         'createTime': '2022-03-28 14:00'
//       }
//     ]
//   }
// })

Mock.mock('/api/exam/paper/total/title', function () {
  return {
    single: {
      singleNum: 5,
      singleScore: 10
    },
    multiple: {
      multipleNum: 5,
      multipleScore: 10
    },
    trueFalse: {
      trueFalseNum: 5,
      trueFalseScore: 10
    },
    shortAnswer: {
      shortAnswerNum: 5,
      shortAnswerScore: 10
    },
    gapFilling: {
      gapFillingNum: 5,
      gapFillingScore: 10
    }
  }
})

Mock.mock('/api/exam/paper/finish', function () {
  return {
    code: 0,
    response: {
      list: [
        {
          id: '1001',
          paperName: '傻逼',
          studentName: '张三',
          studentScore: 8,
          totalScore: 22,
          studentReal: 3,
          totalTitle: 8,
          consumeTime: '90min',
          submitTime: '2022-03-28 12:00'
        },
        {
          id: '1002',
          paperName: '傻逼',
          studentName: '张三',
          studentScore: 8,
          totalScore: 22,
          studentReal: 3,
          totalTitle: 8,
          consumeTime: '90min',
          submitTime: '2022-03-28 12:00'
        },
        {
          id: '1001',
          paperName: '傻逼',
          studentName: '张三',
          studentScore: 8,
          totalScore: 22,
          studentReal: 3,
          totalTitle: 8,
          consumeTime: '90min',
          submitTime: '2022-03-28 12:00'
        },
        {
          id: '1001',
          paperName: '傻逼',
          studentName: '张三',
          studentScore: 8,
          totalScore: 22,
          studentReal: 3,
          totalTitle: 8,
          consumeTime: '90min',
          submitTime: '2022-03-28 12:00'
        },
        {
          id: '1001',
          paperName: '傻逼',
          studentName: '张三',
          studentScore: 8,
          totalScore: 22,
          studentReal: 3,
          totalTitle: 8,
          consumeTime: '90min',
          submitTime: '2022-03-28 12:00'
        },
        {
          id: '1001',
          paperName: '傻逼',
          studentName: '张三',
          studentScore: 8,
          totalScore: 22,
          studentReal: 3,
          totalTitle: 8,
          consumeTime: '90min',
          submitTime: '2022-03-28 12:00'
        }
      ],
      total: 6
    },
    pageNum: 1

  }
})

Mock.mock('/api/admin/analysis/select/class', function () {
  return {
    code: 0,
    list: ['19电商管理2班', '19物流管理', '19人力资源管理']
  }
})

Mock.mock('/api/admin/analysis/select/class/analysis', function () {
  return {
    code: 0,
    girlsAndBoys: {
      girls: 30,
      boys: 40
    },
    paragraph: {
      xData: ['0-30', '30-60', '60-70', '70-80', '80-90', '90-100'],
      yData: [3, 5, 30, 20, 10, 8]
    }
  }
})

Mock.mock('/api/exam/paper/finish/check', { code: 0, response: data.checkPaper })

Mock.mock('/api/admin/analysis/select/class/para', function () {
  return {
    para: ['0-30', '30-60', '60-70', '70-80', '80-90', '90-100'],
    list: [
      {
        name: 'java',
        data: [5, 8, 20, 10, 10, 5]
      },
      {
        name: 'C语言',
        data: [8, 5, 10, 20, 5, 10]
      },
      {
        name: 'Python',
        data: [5, 8, 5, 25, 10, 5]
      },
      {
        name: 'C++',
        data: [5, 8, 10, 10, 20, 5]
      },
      {
        name: 'JavaScript',
        data: [5, 8, 10, 20, 10, 5]
      }
    ]
  }
})

Mock.mock('/api/admin/analysis/select/class/score', function () {
  return {
    names: ['Java', 'C++', 'Python', 'JavaScript', 'Vue'],
    totalLength: 13,
    studentNames: ['小明', '小红', '小张', '小赵', '小四', '小邵', '小祝', '小武', '小曲', '小舞', '小超', '小吴·', '小布' ],
    studentScore: [
      {
        name: 'Java',
        data: [23, 33, 25, 45, 80, 60, 50, 76, 68, 78, 86, 88, 80]
      },
      {
        name: 'C++',
        data: [33, 23, 45, 25, 60, 80, 76, 56, 66, 77, 80, 84, 882]
      },
      {
        name: 'Python',
        data: [35, 36, 55, 75, 90, 30, 60, 86, 64, 70, 88, 82, 83]
      },
      {
        name: 'JavaScript',
        data: [38, 40, 55, 65, 87, 35, 57, 76, 78, 45, 37, 89, 85]
      },
      {
        name: 'Vue',
        data: [64, 32, 68, 56, 80, 36, 79, 37, 69, 85, 76, 80, 88]
      }
    ]
  }
})
