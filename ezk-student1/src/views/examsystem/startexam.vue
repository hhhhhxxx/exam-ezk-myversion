<template>
  <div class="main-container">
    <div class="info-container">
      <h3>当前考试号：{{ examid }}</h3>
      <h3>当前监考老师：{{ username }}</h3>
      <h3>考试备注：{{ note }}</h3>
      <h3>考试批次：{{ examBatch }}</h3>
      <h3>考试场次：{{ examRound }}</h3>
      <h3>考试开始时间：{{ examTime }}</h3>
      <h3>考试限时：{{ limitTime }}分钟</h3>
      <h3>考试状态：{{ status === 1 ? '考试进行中' : '考试结束' }}</h3>
      <h3>试卷总分：{{ totalScore }}</h3>
    </div>
    <el-row>
      <el-col :span="4" class="info-container">
        <h2>答题情况：</h2>
        共{{ problemVos.length }}道题，已完成
      </el-col>
      <el-col :span="16" class="info-container">

        <el-button @click="getProblem()">下一题</el-button>
        {{ currentProblemTitle }}
        <el-radio-group v-if="currentProblemTitle !== ''" v-model="radio" @change="changeAns()">
          <el-radio label="A">A:{{ resultJson.A }}</el-radio>
          <el-radio label="B">B:{{ resultJson.B }}</el-radio>
          <el-radio label="C">C:{{ resultJson.C }}</el-radio>
          <el-radio label="D">D:{{ resultJson.D }}</el-radio>
        </el-radio-group>

        <!--        <h2>考试题目：</h2>-->
        <!--        <div style="font-size: 18px">-->
        <!--          <div v-for="(item,index) of problemVos" >-->
        <!--            问题{{index + 1}}:{{item.title}}-->
        <!--            <span style="margin-left: 50%">分值：{{item.score}}</span><br>-->
        <!--            &lt;!&ndash;选项&ndash;&gt;-->
        <!--            <el-radio-group v-model="radio" @change="changeAns(radio,index)">-->
        <!--              <el-radio label="A">A:{{JSON.parse(item.resultJson).A}}</el-radio>-->
        <!--              <el-radio label="B">B:{{JSON.parse(item.resultJson).B}}</el-radio>-->
        <!--              <el-radio label="C">C:{{JSON.parse(item.resultJson).C}}</el-radio>-->
        <!--              <el-radio label="D">D:{{JSON.parse(item.resultJson).D}}</el-radio>-->
        <!--            </el-radio-group>-->
        <!--          </div>-->
        <!--        </div>-->
      </el-col>
      <el-col :span="4" class="info-container">
        <h2>信息：</h2>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'startexam',
  data () {
    return {
      examid: '',
      status: 0,
      examTime: '',
      limitTime: 0,
      examBatch: 1,
      examRound: 1,
      note: '',
      totalScore: 0,
      username: '',
      problemVos: [],
      radio: '',

      currentProblemId: 0,
      currentProblemTitle: '',
      resultJson: {},
      currentAns: {}
    }
  },
  mounted () {

  },
  created () {
    this.examid = this.$route.params.examid
    this.init()
  },
  methods: {
    // 初始化考试环境
    init () {
      this.$axios.post('/api/examsystem/tableexam/initExam/' + this.examid)
        .then(res => {
          console.log(res.data.data)
          this.status = res.data.data.status
          this.examTime = res.data.data.examTime
          this.limitTime = res.data.data.limitTime
          this.examBatch = res.data.data.examBatch
          this.examRound = res.data.data.examRound
          this.note = res.data.data.note
          this.totalScore = res.data.data.totalScore
          this.username = res.data.data.username
          this.problemVos = res.data.data.problemVos
        })
    },

    //  答题情况
    changeAns () {
      console.log('第', this.currentProblemId, '题：', this.radio)
    },

    getProblem () {
      this.radio = ''
      if (this.currentProblemId >= this.problemVos.length) {
        return
      }
      var curProblem = this.problemVos[this.currentProblemId]
      this.currentProblemTitle = curProblem.title
      this.resultJson = JSON.parse(curProblem.resultJson)
      console.log(this.currentProblemId)
      this.currentProblemId++
    }
  }
}
</script>

<style scoped>
.main-container {
  border-radius: 10px;
  justify-content: center;
  margin: 50px auto;
  width: 90%;
  height: auto;

  border: 5px solid #ededed;
  box-shadow: 0 0 40px #ddd;
  background-color: white;

  padding: 50px;
}

.info-container {
  border: 5px solid #ededed;
  padding: 20px;
}
</style>
