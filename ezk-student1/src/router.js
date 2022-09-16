import Vue from 'vue'
import Router from 'vue-router'
import Layout from '@/layout'

Vue.use(Router)
const router = new Router({
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/login/index'),
      meta: { title: '登录', bodyBackground: '#fbfbfb' }
    },
    {
      path: '/register',
      name: 'Register',
      component: () => import('@/views/register/index'),
      meta: { title: '注册', bodyBackground: '#fbfbfb' }
    },
    {
      path: '/home',
      name: 'HomeIndex',
      component: () => import('@/views/home/index'),
      meta: { title: '首页', bodyBackground: '#fbfbfb', requireAuth: true }
    },
    {
      path: '/',
      component: Layout,
      redirect: '/index',
      children: [
        {
          path: 'index',
          component: () => import('@/views/dashboard/index'),
          name: 'Dashboard',
          meta: { title: '首页', requireAuth: true }
        }
      ]
    },
    {
      path: '/paper',
      component: Layout,
      children: [
        {
          path: 'index',
          component: () => import('@/views/paper/index'),
          name: 'PaperIndex',
          meta: { title: '试卷中心', requireAuth: true }
        }
      ]
    },
    {
      path: '/record',
      component: Layout,
      children: [
        {
          path: 'index',
          component: () => import('@/views/record/index'),
          name: 'RecordIndex',
          meta: { title: '考试记录', requireAuth: true }
        }
      ]
    },
    {
      path: '/question',
      component: Layout,
      children: [
        {
          path: 'index',
          component: () => import('@/views/question-error/index'),
          name: 'QuestionErrorIndex',
          meta: { title: '错题本', requireAuth: true }
        }
      ]
    },
    {
      path: '/user',
      component: Layout,
      children: [
        {
          path: 'index',
          component: () => import('@/views/user-info/index'),
          name: 'UserInfo',
          meta: { title: '个人中心', requireAuth: true }
        }
      ]
    },
    {
      path: '/user',
      component: Layout,
      children: [
        {
          path: 'message',
          component: () => import('@/views/user-info/message'),
          name: 'UserMessage',
          meta: { title: '消息中心', requireAuth: true }
        }
      ]
    },
    {
      path: '/examsystem',
      component: Layout,
      children: [
        {
          path: 'index',
          component: () => import('@/views/examsystem/index'),
          name: 'ExamSystemIndex',
          meta: { title: '考前页面', requireAuth: true }
        },
        {
          path: 'startexam/:examid',
          name: 'ExamSystemStart',
          component: () => import('@/views/examsystem/startexam'),
          meta: { title: '开始考试', requireAuth: true }
        },
        {
          path: 'list',
          name: 'ExamSystemList',
          component: () => import('@/views/examsystem/list/index'),
          meta: { title: '我的考试', requireAuth: true }
        },
        {
          path: 'examnote/:doId',
          name: 'ExamSystemExamNote',
          component: () => import('@/views/examsystem/list/examnote'),
          meta: { title: '考前须知', requireAuth: true }
        },
        {
          path: 'submitnote',
          name: 'ExamSystemSubmitNote',
          component: () => import('@/views/examsystem/list/submitnote'),
          meta: { title: '提交试卷须知', requireAuth: true }
        },
        {
          path: 'notice',
          name: 'ExamSystemSubmitNote',
          component: () => import('@/views/examsystem/notice/index'),
          meta: { title: '通知', requireAuth: true }
        },
      ]
    },
    {
      path: '/mutual',
      component: Layout,
      children: [
        {
          path: 'index',
          component: () => import('@/views/exam/paper/mutualList'),
          name: 'UserInfo',
          meta: { title: '互评试卷' }
        },
        {
          path: 'mutual',
          component: () => import('@/views/exam/paper/mutual'),
          name: 'UserInfo',
          meta: { title: '互评试卷' }
        }
      ]
    },

    { path: '/do', name: 'ExamPaperDo', component: () => import('@/views/exam/paper/do'), meta: { title: '试卷答题' } },
    {
      path: '/edit',
      name: 'ExamPaperEdit',
      component: () => import('@/views/exam/paper/edit'),
      meta: { title: '试卷批改' }
    },
    {
      path: '/read',
      name: 'ExamPaperRead',
      component: () => import('@/views/exam/paper/read'),
      meta: { title: '试卷查看' }
    },
    { path: '*', component: () => import('@/views/error-page/404'), meta: { title: '404' } },

  ]
})

export { router }
