<template>
  <div class="main-container">
    <div class="tool-class">
      <div class="clean-all-class">
        <el-button type="primary" class="tool-button" @click="cleanNotice"
        >清空已读<i class="el-icon-delete-solid" style="margin-left: 3px"></i></el-button>
      </div>
      <div class="all-read-class">
        <el-button type="primary" class="tool-button" @click="allRead"
        >全部已读<i class="el-icon-success" style="margin-left: 3px"></i
        ></el-button>
      </div>
    </div>

    <el-card v-for="(item, i) in noticeList" :key="i" style="margin-bottom: 20px" shadow="hover">
      <div>
        <h4 style="color: #000000" v-if="item.isRead === 0">考试通知</h4>
        <span style="color: #808080" v-if="item.isRead === 1">考试通知</span>
        <p style="float: right">
          <el-tag type="danger" v-if="item.isRead === 0">未读</el-tag>
        </p>
      </div>
      <div>
        <p>
          <b v-if="item.isRead === 0">{{ item.name }}</b>
          <span v-if="item.isRead === 1" style="color: #7d7d7f">{{ item.name }}</span>
        </p>
        <p style="font-size: 12px">时间：{{ item.examStartTime }}</p>
        <el-popover
          placement="top-start"
          title=""
          width="200"
          trigger="hover"
        >
          <p>批次：{{ item.examBatch }}</p>
          <p>场次：{{ item.examRound }}</p>
          <p>及格分数：{{ item.passScore }}</p>
          <p>
            考试环境：
            <b v-if="item.allowMultidevice === '0'">电脑端考试</b>
            <b v-if="item.allowMultidevice === '1'">app考试</b>
            <b v-if="item.allowMultidevice === '2'">电脑端与app考试</b>
          </p>
          <el-link class="xiangqing-button-class" slot="reference"
          >详情
          </el-link
          >
        </el-popover>
      </div>

      <p
        class="publish-time-class"
        v-if="getHour(getNowFormatDate(), item.createTime) === 0"
      >
        {{ "刚刚" }}
      </p>
      <p class="publish-time-class" v-else>
        {{ getHour(getNowFormatDate(), item.createTime) + "小时前" }}
      </p>
      <el-button
        class="confirm-button"
        v-if="item.isRead === 0"
        @click="toIsRead(item.argumentsId)"
      >已读
      </el-button>
    </el-card>

  </div>
</template>

<script>
import Cookies from 'js-cookie'

export default {
  name: 'index',
  data () {
    return {
      userId: 0,
      noticeList: []
    }
  },
  created () {
    this.userId = Cookies.get('userId')
    this.getNoticeList()
  },
  methods: {
    getNoticeList () {
      console.log(11)
      this.$axios
        .get(`/api/examsystem/texamnotice/getNotice/${this.userId}`)
        .then((res) => {
          console.log(res)
          this.noticeList = res.data.response
        })
    },

    // 已读通知
    toIsRead (argumentsId) {
      let data = {
        userId: this.userId,
        argumentsId: argumentsId
      }
      this.$axios
        .post(`/api/examsystem/texamnotice/isRead`, data)
        .then((res) => {
          this.$message.info('已读信息')
          // this.getNoticeList()
          this.getNoticeList()
        })
    },

    // 获取现在时间
    getNowFormatDate () {
      var date = new Date()
      var seperator1 = '-'
      var seperator2 = ':'
      var month = date.getMonth() + 1
      var strDate = date.getDate()
      if (month >= 1 && month <= 9) {
        month = '0' + month
      }
      if (strDate >= 0 && strDate <= 9) {
        strDate = '0' + strDate
      }
      var currentdate =
        date.getFullYear() +
        seperator1 +
        month +
        seperator1 +
        strDate +
        ' ' +
        date.getHours() +
        seperator2 +
        date.getMinutes() +
        seperator2 +
        date.getSeconds()
      return currentdate
    },

    // 获取两个时间相差小时
    getHour (s1, s2) {
      s1 = new Date(s1.replace(/-/g, '/'))
      s2 = new Date(s2.replace(/-/g, '/'))
      var ms = Math.abs(s1.getTime() - s2.getTime())
      return parseInt(ms / 1000 / 60 / 60)
    },

    cleanNotice () {
      this.$axios
        .get(`/api/examsystem/texamnotice/deleteUserNotice/${this.userId}`)
        .then((res) => {
          this.$message.info('清空已读信息成功')
          this.getNoticeList()
        })
    },

    allRead () {
      this.$axios
        .get(`/api/examsystem/texamnotice/allReadNotice/${this.userId}`)
        .then((res) => {
          this.$message.info('已读全部信息')
          this.$router.go(0)
        })
    }
  }
}
</script>

<style scoped>

.main-container {
  border-radius: 10px;
  justify-content: center;
  margin-left: 5%;
  width: 90%;
  height: auto;
  border: 0 solid #ededed;
  /*box-shadow: 0 0 40px #ddd;*/
  background-color: white;
}

.notice-box1 {
  float: left;
  width: 300px;
  height: auto;
  margin-left: 30px;
  margin-top: 30px;
  padding: 10px;

  border-radius: 10px;
  box-shadow: 20px 20px 60px #bebebe, -20px -20px 60px #ffffff;
}

.confirm-button {
  float: right;
  background-color: #24292f;
  color: white;
  width: 100px;
  margin-bottom: 10px;
}

.confirm-button:hover {
  float: right;
  background-color: #24292f;
  color: #8c959f;
  width: 100px;
  margin-bottom: 10px;
}

.tool-button {
  background-color: #24292f;
  color: white;
  width: 100px;
}

.tool-button:hover {
  background-color: #24292f;
  color: #8c959f;
  width: 100px;
}

.xiangqing-button-class {
  /*color: #000000;*/
  margin-bottom: 20px;
}

.exam-info-notice {
  font-size: 14px;
  border-top: 1px solid #dddddd;
}

.publish-time-class {
  float: left;
  font-size: 10px;
  color: grey;
}

.is-Read-class {
  float: right;
}

.tool-class {
  display: flex;
  width: 100%;
  height: 60px;
  margin-top: 20px;
  margin-left: 25px;
}

.tool-class .clean-all-class {
  margin: 10px;
}

.tool-class .all-read-class {
  margin: 10px;
}
</style>
