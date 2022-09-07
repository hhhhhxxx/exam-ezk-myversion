<template>
  <div class="main-container">
    <!-- 考试知情框、考前人像采集   -->
    <el-dialog
      :center="true"
      fullscreen
      :show-close="false"
      :close-on-press-escape="false"
      :visible.sync="dialogVisible">
      <div class="camera-container-parent">
        <div class="title-container">
          <!--这里src是一个默认的标靶的图片-->
          <img class="title-img-class"
               src="https://uploadfiles.nowcoder.com/images/20161215/1_1481765923842_E7D55CB6675C14FF518EA9ED12DF12F8"
               width="80px" height="80px">
          <div class="sub-title">
            {{ form.name }} - 第 {{ examArguments.examBatch }} 批,第 {{ examArguments.examRound }} 场
          </div>
          <div style="margin-left: 150px;margin-top: 20px">
            考试时间：{{ format(examArguments.examStartTime) }} - {{ format(examArguments.examEndTime) }}
          </div>
        </div>
        <!--        <p style="padding: 20px;color: grey;font-size: 14px;position: absolute;top:50px">正在采集考前人脸照片,请调整合适姿势,保证上半身入镜.</p>-->

        <div class="camera-container">
          <img :src="headImgSrc"/>
          <canvas ref="canvas" width="320" height="240" hidden></canvas>
          <div class="kaoqiancaiji-class" v-if="kaoqiancaijiTips">人脸采集</div>
          <!--crossorigin="anonymous" 跨域设置-->
          <video class="video-class" ref="video" width="320" height="240" crossorigin="anonymous" autoplay></video>

          <div class="info-box-class">
            <h3 style="margin-left: 20px">考前须知</h3>
            <div class="dialog-top-class">
              <p style="color:red;">*温馨提示：退出全屏<b>{{ examArguments.limitScreenCount }}</b>次以后将自动交卷</p>
              <p>1.在 {{ examArguments.limitSubmitTime }} 分钟内不可提前交卷</p>
              <p>2.在开考 {{ examArguments.limitEnterTime }} 分钟后不可进入考试</p>
              <p v-if="examArguments.allowMultidevice === '0'">3.只允许在<b>电脑端</b>考试</p>
              <p v-if="examArguments.allowMultidevice === '1'">3.只允许在<b>手机app</b>考试</p>
              <p v-if="examArguments.allowMultidevice === '2'">3.允许在<b>电脑端与手机app</b>考试</p>
              <p>4.本场考试允许重做 {{ examArguments.allowRedo }} 次</p>
              <p v-if="examArguments.enableMonitor === 1">考试全程<b>抓拍监控</b></p>
              <p v-if="examArguments.passScore !== 0">{{ examArguments.passScore }} 分及格</p>
            </div>
          </div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button
          class="paishe-button-class"
          type="primary" v-if="chatObj.fromName!==''"
          @click='photograph(1)'>采  集</el-button>
      </span>
    </el-dialog>
    <!--<canvas ref="canvas" width="320" height="240"></canvas>-->
    <!--<el-button @click="testSendMsg">测试websocket</el-button>-->
    <el-card
      id="con_lf_top_div"
      ref="my-el-card"
      :class="{'isScreen':!fullscreen}"
      :style="{background: backgroundColor}">

      <el-button v-if="fullscreen" @click="screen();screen()" class="go-back-fullscreen">全屏</el-button>
      <el-button @click="preventEye" :style="{background: backgroundColor}">{{ eyeTitle }}</el-button>

      <div v-if="this.option === 3">
        <h3>考试区域：</h3>
        <el-row class="do-exam-title" :style="{background: backgroundColor}">
          <el-col :span="24">
            <span :key="item.itemOrder" v-for="item in answer.answerItems">
                 <el-tag :type="questionCompleted(item.completed)" class="do-exam-title-tag"
                         @click="goAnchor('#question-'+item.itemOrder)">{{ item.itemOrder }}</el-tag>
            </span>
            <span class="do-exam-time">
              <label>剩余时间：</label>
              <label>{{ formatSeconds(remainTime) }}</label>
            </span>
          </el-col>
        </el-row>
        <el-row class="do-exam-title-hidden">
          <el-col :span="24">
            <span :key="item.itemOrder" v-for="item in answer.answerItems">
                 <el-tag class="do-exam-title-tag">{{ item.itemOrder }}</el-tag>
            </span>
            <span class="do-exam-time">
              <label>剩余时间：</label>
            </span>
          </el-col>
        </el-row>
        <el-container class="app-item-contain" :style="{background: backgroundColor}">
          <el-header class="align-center">
            <h1>{{ form.name }}</h1>
            <div>
              <span class="question-title-padding">试卷总分：{{ form.score }}</span>
              <span class="question-title-padding">考试时间：{{ form.suggestTime }}分钟</span>
            </div>
          </el-header>
          <el-main>
            <el-form :model="form" ref="form" v-loading="formLoading" label-width="100px">
              <el-row :key="index" v-for="(titleItem,index) in form.titleItems">
                <h3>{{ titleItem.name }}</h3>
                <el-card ref="my-el-card-2" :style="{background: backgroundColor}" class="exampaper-item-box"
                         v-if="titleItem.questionItems.length!==0">
                  <el-form-item :key="questionItem.itemOrder" :label="questionItem.itemOrder+'.'"
                                v-for="questionItem in titleItem.questionItems"
                                class="exam-question-item " label-width="50px"
                                :id="'question-'+ questionItem.itemOrder">
                    <QuestionEdit :qType="questionItem.questionType" :question="questionItem"
                                  :answer="answer.answerItems[questionItem.itemOrder-1]"/>
                  </el-form-item>
                </el-card>
              </el-row>
              <el-row class="do-align-center">
                <el-button type="primary" @click="submitForm">提交</el-button>
                <el-button @click="cancelForm">取消</el-button>
              </el-row>
            </el-form>
          </el-main>
        </el-container>
      </div>
    </el-card>
  </div>
</template>

<script>
import { mapState, mapGetters } from 'vuex'
import { formatSeconds } from '@/utils'
import QuestionEdit from '../components/QuestionEdit'
import examPaperApi from '@/api/examPaper'
import examPaperAnswerApi from '@/api/examPaperAnswer'

export default {
  components: { QuestionEdit },
  data () {
    return {
      dialogVisible: true, // 考前知情框的显示状态
      form: {},
      formLoading: false,
      answer: {
        questionId: null,
        doTime: 0,
        answerItems: []
      },
      timer: null,
      remainTime: 0,
      teacherName: '', // 监考老师名字
      ws: null, // websocket
      headImgSrc: null, // 显示人物抓拍
      chatObj: { // ws传输对象
        code: '',
        fromName: '', // 当前用户
        msg: ''
      },
      currentName: '', // 与fromName一致，可以删除
      option: 0, // 0：输入姓名阶段， 1： 确认摄像头可用阶段， 2：考试阶段

      examClocking: 5000, // 5秒钟抓拍一次
      exitFullNum: 5, // 限制离开全屏次数
      fullscreen: false, // 是否全屏
      goCount: 0, // 退出第几次
      examPaperId: 0, // exam_paper_id
      examInfoId: null, // exam_info_id

      peopleNum: 0, // 人脸识别到的人数
      isLive: true, // 识别到是否为活体
      peopleNumChangeCount: 0, // 统计一共出现1人以上情况的次数
      nonLiveChangeCount: 0, // 统计出现非活体现象情况的次数
      gender: null, // 性别
      examArguments: { // 考试发布相关参数

      },
      examStatus: null, // 考试状态
      argumentId: null,
      paperId: null,
      kaoqiancaijiTips: true, // 考前采集
      backgroundColor: '#ffffff',
      eyeTitle: '开启护眼模式',
      photoTimer: null // 抓拍定时器
    }
  },
  created () {
    // let id = this.$route.query.id
    this.argumentId = this.$route.query.id
    // this.chatObj.fromName
    this.confirmName()
    this.getExamArguments()

    // 直接开始 不要考前截图了 方便测试
    // this.finalConfirmExam()
  },
  mounted () {

  },
  beforeDestroy () {
    this.closeCamera()
    window.clearInterval(this.timer)
    // 之前没有关 多个定时器同时抓拍
    window.clearInterval(this.photoTimer)

    this.ws = null
  },
  watch: {
    peopleNum: 'peopleNumChange',
    isLive: 'isLiveChange',
    gender: 'genderChange'
  },
  methods: {
    testSendMsg () {
      var data = {
        'code': '4',
        'msg': 'hello worde',
        'toName': '18631142256',
        'examInfoId': this.examInfoId
      }
      var s = JSON.stringify(data)
      console.log(s)
      this.ws.send(s)
    },
    // 护眼模式
    preventEye () {
      if (this.backgroundColor !== '#ffffff') {
        this.backgroundColor = '#ffffff'
        this.eyeTitle = '开启护眼模式'
        return
      }
      this.eyeTitle = '关闭护眼模式'
      this.backgroundColor = '#C7EDCC'
    },

    // 日期格式化
    format (time) {
      if (!time) time = new Date()
      let date = new Date(time)
      let y = date.getFullYear()
      let m = (date.getMonth() + 1 + '').padStart(2, '0')
      let d = (date.getDate() + '').padStart(2, '0')
      let hh = (date.getHours() + '').padStart(2, '0')
      let mm = (date.getMinutes() + '').padStart(2, '0')
      let ss = (date.getSeconds() + '').padStart(2, '0')

      return `${y}年${m}月${d}日 ${hh}时${mm}分`
    },

    // 获取考试状态
    getExamStatus () {
      this.$axios.get(`/api/examsystem/texamarguments/getExamStatusById/${this.argumentId}`).then(res => {
        this.examStatus = res.data.code
        if (this.examStatus === 50004) {
          // 考试结束
          this.$alert('考试已在' + this.examArguments.examEndTime + '结束', '考试已结束', {
            confirmButtonText: '确定',
            callback: action => {
              this.$router.push('/paper/index')
            }
          })
        } else if (this.examStatus === 50001) {
          // 考试未开始
          this.$alert('考试在' + this.examArguments.examStartTime + '开始', '考试未开始', {
            confirmButtonText: '确定',
            callback: action => {
              this.$router.push('/paper/index')
            }
          })
        } else if (this.examStatus === 50003) {
          // 考试限制时间进入
          this.$axios.get(`/api/examsystem/texaminfo/getIsEnterExam/${this.argumentId}/${this.chatObj.fromName}`).then(res => {
            if (res.data === false) {
              // 考生之前没进入过考试,限制进入
              this.$alert('考试在开考' + this.examArguments.limitEnterTime + '分钟后不允许进入考试', '限制进入', {
                confirmButtonText: '确定',
                callback: action => {
                  this.$router.push('/paper/index')
                }
              })
            }
          })
        } else if (this.examStatus === 50005) {
          // 考试异常
          this.$alert('考试异常请联系管理员', '考试异常', {
            confirmButtonText: '确定',
            callback: action => {
              this.$router.push('/paper/index')
            }
          })
        }
      })
    },

    // 获取考试参数
    getExamArguments () {
      this.$axios.get(`/api/examsystem/texamarguments/getArgumentById/${this.argumentId}`).then(res => {
        this.examArguments = res.data.response
        this.teacherName = res.data.response.teacherName
        this.getExamStatus()
        // 获取试卷信息
        let _this = this
        this.examPaperId = res.data.response.examPaperId
        let id = res.data.response.examPaperId
        this.formLoading = true
        examPaperApi.select(id).then(re => {
          // 设置监考老师
          // _this.teacherName = re.response.teacherName

          _this.form = re.response
          _this.remainTime = re.response.suggestTime * 60
          _this.initAnswer()
          _this.timeReduce()
          _this.formLoading = false
          // if (id && parseInt(id) !== 0) {
          //   _
          //   })
        })
      })
    },

    // 监听到性别发生改变
    genderChange () {
      let genderChangeData = {
        examInfoId: this.examInfoId,
        gender: this.gender
      }
      this.$axios.post('/api/examsystem/texaminfo/updateCheckSex', genderChangeData)
    },

    // 监听到非活体情况变化
    isLiveChange () {
      this.nonLiveChangeCount++
      let updateNonLiveData = {
        examInfoId: this.examInfoId,
        nonLiveCount: this.nonLiveChangeCount
      }
      this.$axios.post('/api/examsystem/texaminfo/updateNonLiveCount', updateNonLiveData)
    },

    // 监听到人数的变化
    peopleNumChange (curVal, oldVal) {
      this.peopleNumChangeCount++
      let updateOverNumData = {
        examInfoId: this.examInfoId,
        peopleNumCount: this.peopleNumChangeCount
      }
      this.$axios.post('/api/examsystem/texaminfo/updateOverNum', updateOverNumData)
    },

    // 强制收卷
    forceEnd (json) {
      this.submitForm(1)
    },
    // 有警告信息
    showWarnMsg (json) {
      this.$notify({
        title: '监考老师警告',
        message: json.msg,
        type: 'warning',
        duration: 0
      })
    },

    // 确认按钮
    finalConfirmExam () {
      this.startExamClocking()

      // 考前拍照完 才全屏
      if (this.examArguments.limitScreenCount > 0) {
        this.initScreen() // 全屏
        this.screen()
        this.screen()
      }

      this.dialogVisible = false
      this.option = 3
    },

    //= ===================全屏设置=========================
    // 初始化全屏方法
    initScreen () {
      this.goCount = 0
      this.screen() // 打开全屏
      window.addEventListener('keydown', function (event) {
        // 禁掉F11的全屏的默认事件,不会禁止F11的退出全屏
        const e = event || window.event
        if (e && e.keyCode === 122) {
          e.preventDefault()
        }
      })
      document.addEventListener('fullscreenchange', v => {
        if (this.fullscreen === true) {
          this.fullscreen = false
        } else {
          this.goCount++
          // 注意这里的事件都会触发两次
          console.log('当前是退出第' + this.goCount + '次')
          this.$message.info('当前是退出第' + this.goCount + '次')

          // 向exam_info表更新exit_full_count记录
          let updateExitFullData = {
            examInfoId: this.examInfoId,
            examArgumentsId: this.argumentId,
            studentName: this.chatObj.fromName,
            exitCount: this.goCount
          }
          this.$axios.post('/api/examsystem/texaminfo/updateExitFull', updateExitFullData)

          this.fullscreen = true
          if (this.goCount >= this.examArguments.limitScreenCount) {
            this.goBack()
          }
        }
      })
    },
    // 全屏方法
    screen () {
      // 设置后就是id==con_lf_top_div 的容器全屏
      let element = document.getElementById('con_lf_top_div')
      if (this.fullscreen) {
        if (document.exitFullscreen) {
          document.exitFullscreen()
        } else if (document.webkitCancelFullScreen) {
          document.webkitCancelFullScreen()
        } else if (document.mozCancelFullScreen) {
          document.mozCancelFullScreen()
        } else if (document.msExitFullscreen) {
          document.msExitFullscreen()
        }
      } else {
        if (element.requestFullscreen) {
          element.requestFullscreen()
        } else if (element.webkitRequestFullScreen) {
          element.webkitRequestFullScreen()
        } else if (element.mozRequestFullScreen) {
          element.mozRequestFullScreen()
        } else if (element.msRequestFullscreen) {
          // IE11
          element.msRequestFullscreen()
        }
      }
      this.fullscreen = !this.fullscreen
    },
    // 退出全屏方法
    goBack () {
      this.$message.error('您已退出全屏', this.examArguments.limitScreenCount, '次，当前考试已经结束,自动提交试卷')
      this.closeCamera()
      this.submitForm()
    },

    //= ===========================================================

    // 定时抓拍设置
    startExamClocking () {
      this.photoTimer = setInterval(this.photograph, this.examClocking) //先注释太频繁了

      // this.$nextTick(() => {
      //   setInterval(this.photograph, this.examClocking)
      // })
    },

    // 确认考试姓名
    confirmName () {
      this.kaoqiancaijiTips = false
      this.callCamera()
      // 摄像头调用成功
      this.option = 2

      // todo 登录完成后，从session获取用户名
      this.$axios({
        method: 'post',
        url: `/api/examsystem/frontInfo`
      }).then(res => {
        // console.log(res)
        this.chatObj.fromName = res.data.response.userName
        console.log(this.chatObj.fromName)
        this.$axios.get('/api/examsystem/currentUser').then(res => {
          this.currentName = res.data
          this.initWebSocket()
        })

        // 向考试记录表插入数据,返回exam_info_id
        let examInfoData = {
          examArgumentsId: this.argumentId,
          studentName: this.chatObj.fromName
        }
        this.$axios.post('/api/examsystem/texaminfo/insertByStudent', examInfoData).then(res => {
          this.examInfoId = res.data.examInfoId
        })
      })
    },

    // 初始化websocket服务器
    initWebSocket () {
      this.ws = new WebSocket('ws://localhost:8200/exam')

      // onOpen
      this.ws.onopen = () => {
        console.log('连接已建立')
      }

      // onMessage
      // 获取服务器发来的信息
      this.ws.onmessage = (ev) => {
        var json = JSON.parse(ev.data)
        console.log('ev.data = ', ev.data)
        if (json.code === '1') {
          this.onlineUser = json.msg
          console.log('this.onlineUser = ', this.onlineUser)
        } else if (json.code === '2') {
          this.chatObj = json
        } else if (json.code === '3') {
          this.chatObj = json
        } else if (json.code === '4') {
          this.showWarnMsg(json)
        } else if (json.code === '5') {
          this.forceEnd(json)
        }
      }

      // onClose
      this.ws.onclose = () => {
        console.log('连接已关闭')
      }
    },

    //= =======================================================
    // 调用摄像头
    callCamera () {
      // H5调用电脑摄像头API
      navigator.mediaDevices.getUserMedia({
        video: true
      }).then((success) => {
        // 摄像头开启成功
        this.$refs['video'].srcObject = success
        // 实时拍照效果
        this.$refs['video'].play()
      }).catch((error) => {
        console.error('摄像头开启失败，请检查摄像头是否可用！', error)
      })
    },

    // 拍照
    photograph (option) {
      console.log('这里', this.$refs.canvas)
      let ctx = this.$refs.canvas.getContext('2d')
      // 把当前视频帧内容渲染到canvas上
      // eslint-disable-next-line no-unused-expressions
      // this.$refs['video'].crossOrigin = 'anonymous'
      ctx.drawImage(this.$refs['video'], 0, 0, 320, 240)
      // 转base64格式、图片格式转换、图片质量压缩
      let imgBase64 = this.$refs['canvas'].toDataURL('image/jpeg', 0.7)
      // 由字节转换为KB 判断大小
      let str = imgBase64.replace('data:image/jpeg;base64,', '')
      let strLength = str.length
      let fileLength = parseInt(strLength - (strLength / 8) * 2)
      // 图片尺寸  用于判断
      let size = (fileLength / 1024).toFixed(2)
      console.log('传输大小:', size)
      // 一开始进入考试时
      if (option === 1) {
        this.$message.success('采集完成，可开始作答')
        let upData = {
          imgBase64: imgBase64
        }
        // 上传图片到oss 先不上传
        // this.$axios.post('/api/thirdparty/oss/uploadBase64', upData).then(res => {
        //   let responseData = res.data
        //   let updateBeforeImg = {
        //     examInfoId: this.examInfoId,
        //     webUrl: responseData.webUrl
        //   }
        //   // 更新考前抓拍
        //   this.$axios.post('/api/examsystem/texaminfo/updateBeforeImg', updateBeforeImg).then(res => {
        //     console.log(res)
        //   })
        // })
        // 采集完成，开始考试
        this.finalConfirmExam()
      } else {
        console.log('发抓拍')
        // 向教师端传输抓拍数据
        var data = {
          'code': '3',
          'msg': imgBase64,
          'toName': this.teacherName,
          'examInfoId': this.examInfoId
        }
        var s = JSON.stringify(data)
        // console.log(s)
        this.ws.send(s)

        // 调用人脸识别，上传识别数据
        this.detectFace(imgBase64)
      }
    },

    // 人脸检测
    detectFace (image) {
      // 人脸识别数据
      let data = {
        image: image,
        studentName: this.chatObj.fromName
      }
      // 必须是同步的，否则canvas无法画识别框
      this.$axios.post('/api/examsystem/detectFaces', data).then(res => {
        // console.log(res)
        this.peopleNum = res.data.data.length
        if (res.data.code === 0 && res.data.data.length > 0) {
          res.data.data.forEach(r => {
            this.gender = r.gender
            if (r.liveness === 1) {
              this.isLive = true
            } else if (r.liveness === 0) {
              this.isLive = false
            }
          })
        }
      })
    },

    // 关闭摄像头
    closeCamera () {
      if (!this.$refs['video'].srcObject) {
        this.dialogCamera = false
        return
      }
      let stream = this.$refs['video'].srcObject
      let tracks = stream.getTracks()
      tracks.forEach((track) => {
        track.stop()
      })
      this.$refs['video'].srcObject = null
    },

    //= =======================================================
    // 退出答题
    cancelForm () {
      this.$router.push('/index')
    },
    formatSeconds (theTime) {
      return formatSeconds(theTime)
    },
    timeReduce () {
      let _this = this
      this.timer = setInterval(function () {
        if (_this.remainTime <= 0) {
          _this.submitForm()
        } else {
          ++_this.answer.doTime
          --_this.remainTime
        }
      }, 1000)
    },
    questionCompleted (completed) {
      return this.enumFormat(this.doCompletedTag, completed)
    },
    goAnchor (selector) {
      this.$el.querySelector(selector).scrollIntoView({ behavior: 'instant', block: 'center', inline: 'nearest' })
    },
    initAnswer () {
      this.answer.id = this.form.id
      let titleItemArray = this.form.titleItems
      for (let tIndex in titleItemArray) {
        let questionArray = titleItemArray[tIndex].questionItems
        for (let qIndex in questionArray) {
          let question = questionArray[qIndex]
          this.answer.answerItems.push({
            questionId: question.id,
            content: null,
            contentArray: [],
            completed: false,
            itemOrder: question.itemOrder
          })
        }
      }
    },
    submitForm (option) {
      let _this = this
      window.clearInterval(_this.timer)
      _this.formLoading = true
      examPaperAnswerApi.answerSubmit(this.answer).then(re => {
        if (re.code === 1) {
          if (option === 1) {
            _this.$alert('您被教师强制收卷,试卷得分：' + re.response + '分', '考试结果', {
              confirmButtonText: '返回考试记录',
              callback: action => {
                _this.$router.push('/examsystem/submitnote')
              }
            })
          } else {
            _this.$alert('试卷得分：' + re.response + '分', '考试结果', {
              confirmButtonText: '返回考试记录',
              callback: action => {
                _this.$router.push('/examsystem/submitnote')
              }
            })
          }
        } else {
          _this.$message.error(re.message)
        }
        _this.formLoading = false
      }).catch(e => {
        _this.formLoading = false
      })
    }
  },
  computed: {
    ...mapGetters('enumItem', ['enumFormat']),
    ...mapState('enumItem', {
      doCompletedTag: state => state.exam.question.answer.doCompletedTag
    })
  }
}
</script>

<style lang="scss" scoped>
.align-center {
  text-align: center
}

.exam-question-item {
  padding: 10px;

  .el-form-item__label {
    font-size: 15px !important;
  }
}

.question-title-padding {
  padding-left: 25px;
  padding-right: 25px;
}

.main-container {
  border-radius: 10px;
  margin: 1px auto;
  height: 100%;
  //width: 90%;

  border: 5px solid #ededed;
  box-shadow: 0 0 40px #ddd;
  //background-color: white;

}

.dialog-top-class {
  padding: 20px;

}

.confirm-student-name-class {
  padding: 10px;
  margin-left: 20px;
}

.confirm-student-name-class .confirm-button-class {
  margin-left: 20px;
  background-color: #88c3f4;
  color: white;
  width: 100px;
}

.go-back-fullscreen {
  position: absolute;
  top: 0;
  right: 0;
  background-color: #88c3f4;
  color: white;
  width: 100px;
}

.photo-button-class {
  margin: 20px;
  background-color: #88c3f4;
  color: white;
  width: 100px;
}

.paishe-button-class {
  background-color: #88c3f4;
  color: white;
  width: 100px;
}

.camera-container-parent {
  box-shadow: 0 0 20px #ddd;
  width: 50%;
  margin: 0 auto;
}

.camera-container-parent .title-container {
  margin-bottom: 10px;
}

.camera-container-parent .title-img-class {
  position: relative;
  top: 30px;
  left: 20px;
}

.camera-container-parent .title-container .sub-title {
  display: inline-block;
  font-weight: 600;
  font-size: 18px;
  position: relative;
  left: 40px;
}

.camera-container {
  display: flex;
  width: 100%;
  margin-top: 30px;
}

.video-class {
  margin: 10px;
  padding: 20px;
  height: auto;
  background-color: #ddd;
}

.info-box-class {
  border: 1px solid #ddd;
  box-shadow: 0 0 2px #ddd;
  width: 100%;
  float: right;
  margin: 10px;

}

.kaoqiancaiji-class {
  position: absolute;
  margin-left: 8%;
  margin-top: 8%;
  color: #99a9bf;

}

.app-item-contain {
  background-color: #C7EDCC;
}

.app-item-question-container {
  background-color: #C7EDCC;
}

</style>
