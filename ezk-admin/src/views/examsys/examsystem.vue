<template>
  <div class="jiankao-container">
    <el-row :gutter="20">
      <el-col :span="16">
        <!--  人脸识别  -->
        <div class="content-wrapper">
          <h4>
            考生人脸检测
          </h4>

          <div class="imageDiv">
            <p class="loading-zhuapai-class" v-if="!receiveMsg">抓拍中...</p>
            <!--       <canvas id="canvas" height="438" width="780"></canvas>-->
            <img ref="img" height="320" width="480" style="margin: 20px"/>

            <div class="jinggao-info-class">
              <div class="tongji-class">
                <el-descriptions class="margin-top" title="统计信息：" :column="1" size="small" border>
                  <el-descriptions-item>
                    <template slot="label">
                      人数：
                    </template>
                    <p v-if="peopleNum !== 1" style="color: red">{{peopleNum}}</p>
                    <p v-if="peopleNum === 1">{{peopleNum}}</p>
                  </el-descriptions-item>
                  <el-descriptions-item>
                    <template slot="label">
                      性别：
                    </template>
                    <p v-if="checkSex === 0" >男</p>
                    <p v-if="checkSex === 1">女</p>
                  </el-descriptions-item>
                  <el-descriptions-item>
                    <template slot="label">
                      活体：
                    </template>
                    <p v-if="isLive !== true" style="color: red">{{isLive === true ? '是':'否'}}</p>
                    <p v-if="isLive === true" >{{isLive === true ? '是':'否'}}</p>
                  </el-descriptions-item>
                  <el-descriptions-item>
                    <template slot="label">
                      人数变化次数:
                    </template>
                    <p>{{peopleNumChangeCount}}</p>
                  </el-descriptions-item>
                  <el-descriptions-item>
                    <template slot="label">
                      活体变化次数:
                    </template>
                    <p>{{nonLiveChangeCount}}</p>
                  </el-descriptions-item>
                  <el-descriptions-item>
                    <template slot="label">
                      屏幕异常次数:
                    </template>
                    <p>{{exitFullCount}}</p>
                  </el-descriptions-item>
                </el-descriptions>
                <p v-if="peopleNum > 1" style="color: red;font-size: 14px">检测到学生{{chatObj.fromName}}人数大于1人！</p>
                <p v-if="isLive === false" style="color: red;font-size: 14px">检测到学生{{chatObj.fromName}}非活体！</p>
              </div>
            </div>
          </div>
        </div>
      </el-col>

      <el-col :span="8">
        <div class="operate-class">
          <!--<div v-if="currentName === ''">-->
          <!--  老师姓名：-->
          <!--  <el-input v-model="teacherName" style="width: 100px"></el-input>-->
          <!--  <el-button @click="confirmName" style="margin-left: 10px">确认</el-button>-->
          <!--</div>-->

          <p class="current-student-class">监考考生：<b>{{choiceUser}}</b></p>

          <div v-if="chatObj.fromName !== '' && chatObj.code === '2'">
            来自{{chatObj.fromName}}的信息：{{chatObj.msg}}
          </div>

          <div class="send-msg-class">
            <p style="margin-bottom: 10px">发送信息：</p>
            <el-input
              type="textarea"
              :rows="1"
              placeholder="请输入内容"
              v-model="warnMsg">
            </el-input>
            <div>
              <el-button @click="sendWarnMsg" type="primary" class="send-button-class">发送</el-button>
            </div>
          </div>

          <div style="margin-top: 80px">
            <p style="font-size: 14px;color: grey;font-weight: 500">其他操作：</p>
            <el-link type="primary" @click="forceEnd">强制收卷</el-link>
          </div>
        </div>
      </el-col>
    </el-row>

  </div>
</template>

<script>
export default {
  name: 'examsystem',
  data () {
    return {
      warnMsg: '', // 来自老师的警告信息

      currentName: '',
      teacherName: 'zs', // 教师姓名
      ws: null, // websocket
      chatObj: { // ws传输对象
        code: '',
        fromName: '', // 当前用户
        msg: '',
        examInfoId: '' // exam_info_id
      },
      onlineUser: [], // 在线用户列表
      choiceUser: null, // 选择监控某个学生的姓名

      ctx: null,
      peopleNum: 0, // 人脸识别到的人数
      isLive: true, // 识别到是否为活体
      peopleNumChangeCount: 0, // 统计一共出现1人以上情况的次数
      nonLiveChangeCount: 0, // 统计出现非活体现象情况的次数
      exitFullCount: 0,
      beforeExamImg: '',
      checkSex: 0,
      receiveMsg: false,
      argumentsId: null,
    }
  },
  created () {
    this.choiceUser = this.$route.params.studentName
    this.argumentsId = this.$route.params.argumentsId
    this.confirmName()
    this.$nextTick(() => {
      setInterval(this.getExamInfoData, 5000)
    })
  },
  methods: {
    // 定时获取考生监控数据
    getExamInfoData () {
      let data = {
        studentName: this.choiceUser,
        argumentsId: this.argumentsId
      }
      this.$axios.post(`/api/examsystem/texaminfo/getExamInfoData`,data).then(res => {
        let data = res.data.response
        // todo
        console.log(data)
        this.peopleNumChangeCount = data.numOverCount
        this.nonLiveChangeCount = data.nonLiveCount
        this.exitFullCount = data.exitFullCount
        this.beforeExamImg = data.beforeExamImg
        this.checkSex = data.checkSex
      })
    },
    // 强制收卷
    forceEnd () {
      this.$message.success('已强制收卷')
      let data = {
        'code': '5',
        'msg': '强制收卷',
        'toName': this.choiceUser,
        'examInfoId': this.examInfoId
      }
      let s = JSON.stringify(data)
      this.ws.send(s)
    },

    // 教师发送警告信息
    sendWarnMsg () {
      if (this.warnMsg === '') {
        this.$message.info('请输入警告内容')
        return
      }
      this.$message.success('已发送给学生')
      let data = {
        'code': '4',
        'msg': this.warnMsg,
        'toName': this.choiceUser,
        'examInfoId': this.examInfoId
      }
      let s = JSON.stringify(data)
      this.ws.send(s)
    },

    // ============================人脸检测===============================
    async detectFace (image) {
      let canvas = document.createElement('canvas')
      canvas.width = image.width
      canvas.height = image.height
      this.ctx = canvas.getContext('2d')
      this.ctx.drawImage(image, 0, 0, image.width, image.height)

      // 人脸识别数据
      let data = {
        image: canvas.toDataURL('image/png'),
        studentName: this.chatObj.fromName
      }

      // console.log('data,', data)
      let res = null

      // 必须是同步的，否则canvas无法画识别框
      await this.$axios.post('/api/examsystem/detectFaces', data).then(result => {
        console.log(result)
        res = result
      })
      this.peopleNum = res.data.data.length
      if (res.data.code === 0 && res.data.data.length > 0) {
        res.data.data.forEach(r => {
          let rect = r.rect
          let x = rect.left
          let y = rect.top
          let w = rect.right - rect.left
          let h = rect.bottom - rect.top
          this.ctx.strokeStyle = '#FF0000'
          this.ctx.lineWidth = 5
          this.ctx.strokeRect(x, y, w, h)
          let gender = '未知'
          if (r.gender === 0) {
            gender = '男'
          } else if (r.gender === 1) {
            gender = '女'
          }

          let liveness = '-'
          if (r.liveness === 1) {
            liveness = '活体'
            this.isLive = true
          } else if (r.liveness === 0) {
            liveness = '非活体'
            this.isLive = false
          }

          // let txt = '性别:' + gender + ',年龄:' + r.age + ',' + liveness
          let txt = gender + ',' + r.age + ',' + liveness
          this.ctx.fillStyle = '#FF0000'
          this.ctx.font = '12px Georgia'
          this.ctx.fillText(txt, x, y - 10)
          console.info('detectSuccess')
        })
      }

      this.$refs.img.src = canvas.toDataURL('image/png')

      // this.$refs.img.src = canvas.toDataURL('image/jpeg')

    },
    // 将传来的图片，调用detectFace函数
    fileInput () {
      let _this = this
      let image = new Image()
      image.src = this.chatObj.msg
      image.onload = function () {
        _this.detectFace(image)
      }
      console.info('file input!')
    },

    // ===================================================================

    // 确认考试姓名
    confirmName () {
      this.$message.success('已开始监考')
      let user = {
        username: this.teacherName
      }
      // todo 登录完成后，从session获取用户名
      this.$axios({
        method: 'post',
        url: `/api/examsystem/frontInfo`
      }).then(res => {
        console.log(res)
        this.chatObj.fromName = res.data.response.userName
        this.$axios.get('/api/examsystem/currentUser').then(res => {
          this.currentName = res.data
          // this.option = 1
          this.initWebSocket()
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
          // code=1 在线用户广播
          this.onlineUser = json.msg
          // console.log('this.onlineUser = ', this.onlineUser)
        } else if (json.code === '2') {
          // code=2 文本信息广播
          this.chatObj = json
        } else if (json.code === '3') {
          // code=3 抓拍图片
          // if (json.fromName !== this.chatObj.fromName){
          //   var element = document.createElement('img');
          //   element.src = json.msg
          // }
          // 选项框里选中的考生，才展示监控画面
          if (this.choiceUser === json.fromName) {
            this.receiveMsg = true
            this.chatObj = json
          // console.log(json)
            this.fileInput()
           }
        }
      }
      // onClose
      this.ws.onclose = () => {
        console.log('连接已关闭')
      }
    }

  }
}
</script>

<style scoped>
.jiankao-container{
  justify-content: center;
  border: 0 solid #ededed;
  /*box-shadow: 0 0 40px #ddd;*/
  width: 100%;
}

.content-wrapper{
  width: auto;
  height: 500px;
  border: 1px solid #ededed;
  box-shadow: 0 0 40px #ddd;
  padding: 10px;
  margin-bottom: 5px;
  margin-left: 5px;
  margin-top: 5px;
}

.imageDiv{
  display: flex;
  /*background-color: #dddddd;*/
}

.operate-class{
  width: auto;
  height: 500px;
  border: 0 solid #ededed;
  box-shadow: 0 0 40px #ddd;
  padding: 20px;
  margin-top: 10px;
}

.current-student-class{
  margin-top: 20px;
}

.send-msg-class{
  width: auto;
  height: auto;
  border: 0 solid #ededed;
  background-color: white;
  margin-bottom: 30px;
  margin-top: 60px;
}

.send-button-class{
  width: 200px;
  float: left;
  margin-top: 10px;
}

.loading-zhuapai-class{
  font-size: 14px;
  color: #646464;
  position: absolute;
  left: 15%;
  top :15%
}

.jinggao-info-class{
  width: 300px;
  margin-left: 5px;
  margin-top: 20px;

}

.tongji-class{
  font-size: 14px;
  color: #646464;
  margin-top: 20px;
  margin-right: 20px;

}

</style>
