<template>
  <el-container>
    <el-header height="61" class="student-header">
      <div class="head-user">
        <el-button size="mini" class="head-user-addclass-button" @click="addClassDialogVisable = true">加入班级</el-button>

        <el-dropdown trigger="click" placement="bottom">
          <el-badge :is-dot="messageCount!==0">
            <el-avatar class="el-dropdown-avatar" size="medium"
                       src="https://examsystem-gdpu.oss-cn-guangzhou.aliyuncs.com/base/avatar.png"></el-avatar>
          </el-badge>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="$router.push({path:'/user/index'})">个人中心</el-dropdown-item>
            <el-dropdown-item @click.native="$router.push({path:'/user/message'})">
              <el-badge :value="messageCount" v-if="messageCount!==0">
                <span>消息中心</span>
              </el-badge>
              <span v-if="messageCount===0">消息中心</span>
            </el-dropdown-item>
            <el-dropdown-item @click.native="logout" divided>退出</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
      <div>
        <!--LOGO-->
        <img src="https://examsystem-gdpu.oss-cn-guangzhou.aliyuncs.com/base/logo3.png"
             style="width: 150px;height: 60px"/>
      </div>
    </el-header>

    <el-container>
      <el-aside width="250px" class="side-big-container">
        <!--        <el-avatar  class="avatar-index" :src="userInfo.imagePath === null ? 'https://examsystem-gdpu.oss-cn-guangzhou.aliyuncs.com/base/avatar.png' : userInfo.imagePath"></el-avatar>-->
        <el-avatar class="avatar-index"
                   src="https://examsystem-gdpu.oss-cn-guangzhou.aliyuncs.com/base/avatar.png"></el-avatar>

        <div class="student-name">{{ userName }}</div>
        <el-menu class="side-container" router :default-openeds="['1']" active-text-color="#ffffff">
          <el-menu-item class="side-container" route="/index" index="1-1"><i class="el-icon-message"></i>首页
          </el-menu-item>

          <el-menu-item class="side-container" route="/examsystem/list" index="1-2"><i class="el-icon-edit"></i>考试
          </el-menu-item>
          <el-menu-item class="side-container" route="/examsystem/notice" index="1-3">
            <i class="el-icon-folder-opened"></i>
            通知
            <el-badge :value="unReadCount" class="item" v-if="unReadCount!==0"></el-badge>
          </el-menu-item>
          <el-menu-item class="side-container" route="/mutual/index" index="1-5"><i class="el-icon-coordinate"></i>互评
          </el-menu-item>
          <el-menu-item class="side-container" route="/question/index" index="1-4"><i class="el-icon-coordinate"></i>错题
          </el-menu-item>

          <el-menu-item class="side-container" route="/index?id=1" index="3-2"><i class="el-icon-user-solid"></i>小组
          </el-menu-item>
          <el-menu-item class="side-container" route="/index?id=2" index="3-3"><i class="el-icon-folder-opened"></i>收件箱
          </el-menu-item>

          <el-menu-item class="side-container" route="/index?id=4" index="3-5"><i class="el-icon-trophy"></i>大赛
          </el-menu-item>
          <el-menu-item class="side-container" route="/index?id=5" index="3-6"><i class="el-icon-collection"></i>笔记
          </el-menu-item>
          <el-menu-item class="side-container" route="/index?id=6" index="3-7"><i class="el-icon-s-promotion"></i>云盘
          </el-menu-item>
          <el-menu-item class="side-container" route="/index?id=7" index="3-8"><i class="el-icon-edit"></i>论文检测
          </el-menu-item>

        </el-menu>
      </el-aside>

      <el-main class="student-main">
        <router-view/>
      </el-main>

    </el-container>
    <el-dialog title="班级" :visible.sync="addClassDialogVisable" width="50%">
      <div>
        <el-form label-width="150px">
          <el-form-item label="班级码：">
            <el-input style="width: 300px"></el-input>
          </el-form-item>
          <el-form-item label="扫码进班：">
            <el-upload class="upload-demo" drag :data="{'userId': userId}" name="file"
                       action="/api/thirdparty/class/decode"
                       :auto-upload="true"
                       list-type="picture"
                       :on-success="onSuccessSumbit"
                       :on-error="onError"
                       :limit="1">
              <i class="el-icon-upload"></i>
              <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            </el-upload>
          </el-form-item>
        </el-form>
      </div>

      <span slot="footer" class="dialog-footer">
          <el-button @click="addClassDialogVisable = false">取 消</el-button>
          <el-button type="primary" @click="addClassDialogVisable = false">确 定</el-button>
        </span>
    </el-dialog>
  </el-container>

</template>

<script>
import { mapActions, mapMutations, mapState } from 'vuex'
// import loginApi from '@/api/login'
import userApi from '@/api/user'
import Cookies from 'js-cookie'
import store from '../store'

export default {
  name: 'Layout',
  data () {
    return {
      defaultUrl: '/index',
      userInfo: {
        imagePath: null
      },
      unReadCount: 0, // 未读通知数量
      userId: 0,
      userName: '',
      addClassDialogVisable: false,
      timer: null // 刷新计时器
    }
  },
  created () {
    let _this = this
    this.defaultUrl = this.routeSelect(this.$route.path)
    this.getUserMessageInfo()
    userApi.getCurrentUser().then(re => {
      _this.userInfo = re.response
    })
    this.userId = Cookies.get('userId')
    this.userName = Cookies.get('studentUserName')
    this.getUnReadCount()
  },
  watch: {
    $route (to, from) {
      this.defaultUrl = this.routeSelect(to.path)
    }
  },
  methods: {
    onError () {
      this.$message.error('请上传二维码图片！')
    },
    onSuccessSumbit () {
      this.$message.success('成功加入班级')
      this.addClassDialogVisable = false
    },

    getUnReadCount () {
      this.$axios.get(`/api/examsystem/texamnotice/getUnReadCount/${this.userId}`).then(res => {
        this.unReadCount = res.data.response
      })
    },
    routeSelect (path) {
      let topPath = ['/', '/index', '/paper/index', '/record/index', '/question/index']
      if (topPath.indexOf(path)) {
        return path
      }
      return null
    },
    logout () {
      let _this = this
      window.localStorage.removeItem('session')
      _this.clearLogin()
      this.$store.dispatch('deleteToken').then(res => {
        _this.$router.push({ path: '/login' })
      })
    },
    ...mapActions('user', ['getUserMessageInfo']),
    ...mapMutations('user', ['clearLogin'])
  },
  computed: {
    ...mapState('user', {
      messageCount: state => state.messageCount
    })
  },
  mounted () {
    this.timer = setInterval(() => {
      store.dispatch('refreshToken')
    }, 50000)
  },
  destroyed () {
    clearInterval(this.timer)
  }
}
</script>

<style lang="scss" scoped>
.exam-header {
  height: 50px;
  width: 100%;
  background: no-repeat center;
  background-size: cover;
}

.side-big-container {
  background: #ffffff;
  color: #333333;
  width: 350px;
  left: 20px;
  top: 100px;
  height: auto;
  box-shadow: 10px 10px 40px 10px rgba(43, 51, 59, 0.1);
  //height: calc(100vh - 116px);
  margin: 20px;
  border-radius: 10px;
}

.side-container {
  background: #ffffff;
  color: #000000;
}

.side-container:hover {
  background: #ffffff;
  color: #8c959f;
}

.el-menu-item.is-active {
  background-color: darkgray !important;
}

.avatar-index {
  width: 90px;
  height: 90px;
  margin-left: 32%;
  margin-top: 30px;
}

.student-name {
  font-size: 14px;
  text-align: center;
  margin: 20px;
}

.logo-index {
  width: 150px;
  font-family: Helvetica, 'Hiragino Sans GB', 'Microsoft Yahei', '微软雅黑', Arial, sans-serif;
  font-size: 20px;
  margin: 20px;
}

.head-user-addclass-button {
  width: 6.5em;
  height: 2.3em;
  margin: 0.5em;
  background: #242949;
  color: white;
  border: none;
  border-radius: 0.625em;
  font-size: 14px;
  font-weight: bold;
  cursor: pointer;
  position: relative;
  z-index: 1;
  overflow: hidden;
  margin-top: 10px;
}

.head-user-addclass-button:hover {
  color: #24292f;
}

.head-user-addclass-button:after {
  content: "";
  background: white;
  position: absolute;
  z-index: -1;
  left: -20%;
  right: -20%;
  top: 0;
  bottom: 0;
  transform: skewX(-45deg) scale(0, 1);
  transition: all 0.5s;
}

.head-user-addclass-button:hover:after {
  transform: skewX(-45deg) scale(1, 1);
  -webkit-transition: all 0.5s;
  transition: all 0.5s;
}

</style>
