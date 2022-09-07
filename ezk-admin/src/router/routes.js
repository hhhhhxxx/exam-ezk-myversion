import Layout from '@/layout'

/**
 * @type {import('vue-router/types/router').RouteConfig[]}
 */

// 业务
export const asyncRouterMap = [
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    name: 'Index',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/dashboard/index'),
        name: 'Dashboard',
        meta: { title: '主页', icon: 'home', affix: true, requireAuth: true }
      }
    ]
  },

  // 班级管理
  {
    path: '/class',
    component: Layout,
    name: 'Class',
    meta: {
      title: '班级管理',
      icon: 'exam'
    },
    children: [
      {
        path: 'list',
        component: () => import('@/views/class/list'),
        name: 'ClassList',
        meta: { title: '班级列表', noCache: true, requireAuth: true }
      },
      {
        path: 'edit',
        component: () => import('@/views/class/edit'),
        name: 'EditClass',
        meta: { title: '编辑班级', noCache: true, requireAuth: true },
        hidden: true
      },
      {
        path: 'student/list',
        component: () => import('@/views/class/studentList'),
        name: 'StudentInClass',
        meta: { title: '班内学生', noCache: true, requireAuth: true },
        hidden: true
      }
    ]
  },

  // 考务管理
  {
    path: '/kaowu',
    component: Layout,
    name: 'KaoWu',
    meta: {
      title: '考务管理',
      icon: 'exam'
    },
    children: [
      {
        path: 'room',
        component: () => import('@/views/examsys/kaowu/texamroom'),
        name: 'RoomList',
        meta: { title: '考场列表', noCache: true, requireAuth: true }
      },
      {
        path: 'plan',
        component: () => import('@/views/examsys/kaowu/texamplan'),
        name: 'PlanList',
        meta: { title: '考试计划', noCache: true, requireAuth: true }
      },
      {
        path: 'exam',
        component: () => import('@/views/examsys/kaowu/texamarguplanroomrelation'),
        name: 'KaoWuExamList',
        meta: { title: '考试编排', noCache: true, requireAuth: true }
      },
      {
        path: 'roomUser',
        component: () => import('@/views/examsys/kaowu/texamroomuserrelation'),
        name: 'KaoWuRoomUser',
        meta: { title: '考生分配考场', noCache: true, requireAuth: true }
      }
    ]
  },

  // 学科管理
  {
    path: '/subject',
    component: Layout,
    name: 'LogPage',
    meta: {
      title: '课程管理',
      icon: 'log'
    },
    alwaysShow: true,
    children: [
      {
        path: 'list/',
        component: () => import('@/views/subject/list'),
        name: 'SubjectList',
        meta: { title: '课程列表', noCache: true, requireAuth: true }
      },
      {
        path: 'edit/',
        component: () => import('@/views/subject/edit'),
        name: 'SubjectEdit',
        meta: { title: '课程创编', noCache: true, activeMenu: '/subject/list', requireAuth: true },
        hidden: true
      }
    ]
  },
  // 卷库管理
  {
    path: '/exam',
    component: Layout,
    name: 'ExamPage',
    meta: {
      title: '卷库管理',
      icon: 'exam'
    },
    children: [
      {
        path: 'paper/list',
        component: () => import('@/views/exam/paper/list'),
        name: 'ExamPaperPageList',
        meta: { title: '试卷列表', noCache: true, requireAuth: true }
      },
      {
        path: 'paper/edit',
        component: () => import('@/views/exam/paper/edit'),
        name: 'ExamPaperEdit',
        meta: { title: '试卷创建', noCache: true, activeMenu: '/exam/paper/list', requireAuth: true },
        hidden: true
      },
      {
        path: 'paper/autocreate',
        component: () => import('@/views/exam/paper/smartGroup'),
        name: 'ExamPaperAutoCreate',
        meta: { title: '智能组卷', noCache: true, activeMenu: '/exam/paper/list', requireAuth: true },
        hidden: true
      },
      { // 不使用
        path: 'paper/publish',
        component: () => import('@/views/exam/paper/publish'),
        name: 'ManageList',
        meta: { title: '发放设置', noCache: true, activeMenu: '/exam/paper/list', requireAuth: true },
        hidden: true
      },
      {
        path: 'paper/preview',
        component: () => import('@/views/exam/paper/preview'),
        name: 'PreviewPaper',
        meta: { title: '试卷预览', noCache: true, activeMenu: '/exam/paper/list', requireAuth: true },
        hidden: true
      },

      {
        path: 'question/list',
        component: () => import('@/views/exam/question/list'),
        name: 'ExamQuestionPageList',
        meta: { title: '题目列表', noCache: true, requireAuth: true }
      },
      {
        path: 'question/edit/singleChoice',
        component: () => import('@/views/exam/question/edit/single-choice'),
        name: 'singleChoicePage',
        meta: { title: '单选题编辑', noCache: true, activeMenu: '/exam/question/list', requireAuth: true },
        hidden: true
      },
      {
        path: 'question/edit/multipleChoice',
        component: () => import('@/views/exam/question/edit/multiple-choice'),
        name: 'multipleChoicePage',
        meta: { title: '多选题编辑', noCache: true, activeMenu: '/exam/question/list', requireAuth: true },
        hidden: true
      },
      {
        path: 'question/edit/trueFalse',
        component: () => import('@/views/exam/question/edit/true-false'),
        name: 'trueFalsePage',
        meta: { title: '判断题编辑', noCache: true, activeMenu: '/exam/question/list', requireAuth: true },
        hidden: true
      },
      {
        path: 'question/edit/gapFilling',
        component: () => import('@/views/exam/question/edit/gap-filling'),
        name: 'gapFillingPage',
        meta: { title: '填空题编辑', noCache: true, activeMenu: '/exam/question/list', requireAuth: true },
        hidden: true
      },
      {
        path: 'question/edit/shortAnswer',
        component: () => import('@/views/exam/question/edit/short-answer'),
        name: 'shortAnswerPage',
        meta: { title: '简答题编辑', noCache: true, activeMenu: '/exam/question/list', requireAuth: true },
        hidden: true
      },

      {
        path: 'questionbank/list',
        component: () => import('@/views/exam/questionbank/tquestionbank'),
        name: 'QuestionBankList',
        meta: { title: '题库列表', noCache: true, requireAuth: true }
      },
      {
        path: 'questionbank/edit',
        component: () => import('@/views/exam/questionbank/tquestionbank-add-or-update'),
        name: 'QuestionBankEdit',
        hidden: true,
        meta: { title: '题库编辑', noCache: true, requireAuth: true }
      },
      {
        path: 'questionbank/bankadd',
        component: () => import('@/views/exam/questionbank/bankaddquestion'),
        name: 'QuestionBankAdd',
        meta: { title: '题库添加题目', noCache: true, requireAuth: true }
      }
    ]
  },
  // 任务管理
  {
    path: '/task',
    component: Layout,
    name: 'TaskPage',
    meta: {
      title: '考试管理',
      icon: 'task'
    },
    alwaysShow: true,
    children: [
      // {
      //   path: 'list',
      //   component: () => import('@/views/task/list'),
      //   name: 'TaskListPage',
      //   meta: { title: '任务列表', noCache: true , requireAuth: true},
      //   hidden: true
      // },

      // {
      //   path: 'edit',
      //   component: () => import('@/views/task/edit'),
      //   name: 'TaskEditPage',
      //   meta: { title: '任务创建', noCache: true , requireAuth: true},
      //   hidden: true
      // },
      {
        path: 'examsys/:studentName/:argumentsId',
        component: () => import('@/views/examsys/examsystem'),
        name: 'ExamSysStudent',
        meta: { title: '监考学生', noCache: true, requireAuth: true },
        hidden: true
      },
      {
        path: 'examsyslist/:id',
        component: () => import('@/views/examsys/teacher/list'),
        name: 'ExamSysList',
        meta: { title: '考试监考', noCache: true, requireAuth: true },
        hidden: true
      },
      {
        path: 'argument/list',
        component: () => import('@/views/examsys/argument/texamarguments'),
        name: 'ExamArgumentList',
        meta: { title: '考试参数列表', noCache: true, requireAuth: true },
        hidden: true
      },
      {
        path: 'argument/edit/:id',
        component: () => import('@/views/examsys/argument/texamarguments-add-or-update'),
        name: 'ExamArgumentEdit',
        meta: { title: '参数编辑', noCache: true, requireAuth: true },
        hidden: true
      },
      {
        path: 'manage/list',
        component: () => import('@/views/examsys/manage/list'),
        name: 'ExamManageList',
        meta: { title: '考试列表', noCache: true, requireAuth: true }
      },
      {
        path: 'manage/publish',
        component: () => import('@/views/examsys/manage/publish'),
        name: 'ExamManagePublish',
        meta: { title: '发布考试', noCache: true, requireAuth: true }
      }
    ]
  },

  // {
  //   path: '/answer',
  //   component: Layout,
  //   name: 'AnswerPage',
  //   meta: {
  //     title: '成绩管理',
  //     icon: 'answer'
  //   },
  //   alwaysShow: true,
  //   children: [
  //     {
  //       path: 'list',
  //       component: () => import('@/views/answer/list'),
  //       name: 'AnswerPageList',
  //       meta: { title: '答卷列表', noCache: true }
  //     }
  //   ]
  // },
  // 答卷管理
  {
    path: '/answer',
    component: Layout,
    name: 'AnswerPage',
    meta: {
      title: '答卷管理',
      icon: 'answer'
    },
    alwaysShow: true,
    children: [
      {
        path: 'list/',
        component: () => import('@/views/answer/list'),
        name: 'AnswerPageList',
        meta: { title: '批改列表', noCache: true, requireAuth: true }
      },
      {
        path: 'finish/',
        component: () => import('@/views/answer/finish'),
        name: 'AnswerPageFinish',
        meta: { title: '试卷完成', noCache: true, requireAuth: true }
      },
      {
        path: 'correct',
        component: () => import('@/views/answer/correctPaper'),
        name: 'CorrectPaper',
        meta: { title: '试卷批改', noCache: true, activeMenu: '@/views/answer/list', requireAuth: true },
        hidden: false
      },
      {
        path: 'check',
        component: () => import('@/views/answer/check'),
        name: 'CorrectCheck',
        meta: { title: '试卷检查', noCache: true, requireAuth: true },
        hidden: false
      }
    ]
  },
  // 成绩分析
  {
    path: '/analysis',
    component: Layout,
    name: 'AnalysisPage',
    meta: {
      title: '成绩分析',
      icon: 'exam'
    },
    alwaysShow: true,
    children: [
      {
        path: 'list/',
        component: () => import('@/views/scoreAnalysis/list'),
        name: 'AnalysisPageList',
        meta: { title: '试卷列表', noCache: true, requireAuth: true }
      },
      {
        path: 'scoreAnalysis',
        component: () => import('@/views/scoreAnalysis/analysis'),
        name: 'ScoreAnalysis',
        meta: { title: '成绩分析', noCache: true, activeMenu: '/analysis/list', requireAuth: true }
      },
      {
        path: 'class',
        component: () => import('@/views/scoreAnalysis/class'),
        name: 'ClassAnalysis',
        meta: { title: '班级分析', noCache: true, activeMenu: '/analysis/list', requireAuth: true }
      }
    ]
  },
  // 消息中心
  {
    path: '/message',
    component: Layout,
    name: 'MessagePage',
    meta: {
      title: '消息中心',
      icon: 'message'
    },
    alwaysShow: true,
    children: [
      {
        path: 'list',
        component: () => import('@/views/message/list'),
        name: 'MessageListPage',
        meta: { title: '消息列表', noCache: true, requireAuth: true }
      },
      {
        path: 'send',
        component: () => import('@/views/message/send'),
        name: 'MessageSendPage',
        meta: { title: '消息发送', noCache: true, requireAuth: true }
      }
    ]
  },
  // 日志中心
  {
    path: '/log',
    component: Layout,
    name: 'LogPage',
    meta: {
      title: '日志中心',
      icon: 'log'
    },
    alwaysShow: false,
    children: [
      {
        path: 'user/list',
        component: () => import('@/views/log/list'),
        name: 'LogUserPage',
        meta: { title: '用户日志', noCache: true, requireAuth: true }
      }
    ]
  },
  // 个人简介
  {
    path: '/profile',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'index',
        component: () => import('@/views/profile/index'),
        name: 'Profile',
        meta: { title: '个人简介', icon: 'user', noCache: true } // 不用验证
      }
    ]
  },

  // 菜单列表
  {
    path: '/permission',
    name: 'Permission',
    component: Layout,
    meta: {
      icon: 'users',
      title: '权限管理',
      noCache: true,
      requireAuth: true
    },
    children: [
      {
        path: 'user/list',
        component: () => import('@/views/user/list'),
        name: 'UserPage',
        meta: { title: '用户列表', noCache: true, requireAuth: true }
      },
      {
        path: 'user/edit',
        component: () => import('@/views/user/edit'),
        name: 'UserEdit',
        meta: {
          title: '用户编辑',
          noCache: true,
          activeMenu: '/user/admin/list',
          requireAuth: true
        }
      },
      {
        path: 'role',
        name: 'Role',
        component: () =>
          import('@/views/PermissionManage/Roles'),
        meta: { requireAuth: true, title: '角色管理' }
      },
      {
        path: 'allocMenu',
        name: 'AllocMenu',
        component: () =>
          import(
            /* webpackChunkName: 'allocMenu' */ '@/views/PermissionManage/AllocMenu'
            ),
        meta: { requireAuth: true, title: '角色菜单管理' }
      },
      {
        path: 'allocResource',
        name: 'AllocResource',
        component: () =>
          import(
            /* webpackChunkName: 'allocResource' */ '@/views/PermissionManage/AllocResource'
            ),
        meta: { requireAuth: true, title: '角色资源管理' }
      },
      {
        path: 'menu',
        name: 'Menu',
        component: () =>
          import(
            /* webpackChunkName: 'menu' */ '@/views/PermissionManage/Menus'
            ),
        meta: { requireAuth: true, title: '菜单管理' }
      },
      {
        path: 'addMenu',
        name: 'AddMenu',
        component: () =>
          import(
            /* webpackChunkName: 'menuAdd' */ '@/views/PermissionManage/AddMenu'
            ),
        meta: { requireAuth: true, title: '添加菜单' }
      },
      {
        path: 'updateMenu',
        name: 'UpdateMenu',
        component: () =>
          import(
            /* webpackChunkName: 'menuUpdate' */ '@/views/PermissionManage/UpdateMenu'
            ),
        meta: { requireAuth: true, title: '编辑菜单' }
      },
      {
        path: 'resource',
        name: 'Resource',
        component: () =>
          import(
            /* webpackChunkName: 'resource' */ '../views/PermissionManage/Resources'
            ),
        meta: { requireAuth: true, title: '资源管理' }
      },
      {
        path: 'resourceCategory',
        name: 'ResourceCategory',
        component: () =>
          import(
            /* webpackChunkName: 'categoryList' */ '../views/PermissionManage/CategoryList'
            ),
        meta: { requireAuth: true, title: '资源分类' }
      }
    ]
  },

  {
    path: '*',
    hidden: true,
    component: () => import('@/views/error-page/404'),
    meta: { title: '404', noCache: true, requireAuth: true }
  }
]

const routes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path*',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    hidden: true,
    component: () => import('@/views/login/index'),
    meta: { title: '登录' }
  },
  ...asyncRouterMap
]

export default routes
