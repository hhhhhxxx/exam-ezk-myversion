<template>
  <div class="app-container">
    <el-aside
      class="my-side"
      style="margin: 10px; width: 290px; position: fixed"
    >
      <div style="border-bottom: 2px solid #ccc">
        <p>试卷名称：{{ title }}</p>
        <p>提交人：{{ student }}</p>
        <p>耗时：{{ time }}</p>
        <p>得分：{{ perScore }}/{{ totalNum }}</p>
      </div>
      <el-row
        style="
          margin-top: 10px;
          border-bottom: 2px solid #ccc;
          padding-bottom: 10px;
        "
      >
        <span :key="item.itemOrder" v-for="item in paperList">
          <el-tag
            :type="answerCorrect(item.true)"
            class="do-exam-title-tag"
            @click="goAnchor('#question-' + item.itemOrder)"
            >{{ item.itemOrder }}</el-tag
          >
        </span>
      </el-row>
      <div class="my-button" align="center" style="margin-top: 10px">
        <el-button @click="submitForm"> 提交批改 </el-button>
      </div>
    </el-aside>
    <el-main class="my-main">
      <el-form>
        <div>
          <div
            class="content"
            v-for="item in paperList"
            :key="item.id"
            :id="'question-' + item.itemOrder"
          >
            <div class="my-title">{{ item.itemOrder }}、{{ item.title }}</div>
            <div class="my-select">
              <el-radio-group
                v-model="item.selectOption"
                v-if="item.type === 1"
              >
                <el-radio label="A">A</el-radio>
                <el-radio label="B">B</el-radio>
                <el-radio label="C">C</el-radio>
                <el-radio label="D">D</el-radio>
              </el-radio-group>
              <el-radio-group
                v-model="item.selectOption"
                v-if="item.type === 3"
              >
                <el-radio label="是">是</el-radio>
                <el-radio label="否">否</el-radio>
              </el-radio-group>
              <el-checkbox-group
                v-model="item.selectOption"
                v-if="item.type === 2"
              >
                <el-checkbox label="A"></el-checkbox>
                <el-checkbox label="B"></el-checkbox>
                <el-checkbox label="C"></el-checkbox>
                <el-checkbox label="D"></el-checkbox>
                <el-checkbox label="E"></el-checkbox>
              </el-checkbox-group>
              <p v-if="item.type === 4 || item.type === 5">
                {{ item.selectOption }}
              </p>
            </div>
            <div class="exam-result">
              <p>
                结果：
                <el-tag size="small" type="danger" v-if="item.true === 0"
                  >错误</el-tag
                >
                <el-tag size="small" type="success" v-else-if="item.true === 1"
                  >正确</el-tag
                >
                <el-tag size="small" type="warning" v-else>待批改</el-tag>
              </p>
              <p>
                <span class="question-show-item">难度：</span>
                <el-rate
                  v-model="item.difficult"
                  class="question-item-rate"
                  style="display: inline"
                  disabled
                ></el-rate>
              </p>
              <p>解析：{{ item.explain }}</p>
              <p>正确答案：{{ item.answer }}</p>
              <p>分数：{{ item.totalScore }}</p>
              <p>
                得分：
                <span v-if="item.type < 4">{{ item.myScore }}</span>
                <el-input-number
                  v-else
                  v-model="item.myScore"
                  :min="0.0"
                  :max="item.totalScore"
                  :precision="1"
                ></el-input-number>
              </p>
            </div>
          </div>
        </div>
      </el-form>
    </el-main>
  </div>
</template>

<script>
import examPaperAnswerApi from '@/api/examPaperAnwser'
export default {
  data () {
    return {
      title: '',
      student: '',
      time: '',
      perScore: '',
      totalNum: '',
      difficult: 3,
      paperList: [],
      score: 0.0,
      formData: {}
    }
  },
  created () {
    this.search()
  },
  methods: {
    search () {
      examPaperAnswerApi.answerList(this.queryParam).then((data) => {
        const re = data.response
        this.paperList = re.list
        this.title = re.title
        this.student = re.student
        this.time = re.time
        this.perScore = re.perScore
        this.totalNum = re.totalNum
      })
    },
    submitForm () {
      this.$message({
        message: '已完成批改',
        type: 'success'
      })
      this.$router.push('/answer/list/')
    },
    answerCorrect (correct) {
      if (correct === 1) return 'success'
      else if (correct === 0) return 'danger'
      else return 'warning'
    },
    goAnchor (selector) {
      this.$el.querySelector(selector).scrollIntoView({
        behavior: 'instant',
        block: 'center',
        inline: 'nearest'
      })
    }
  }
}
</script>

<style>
</style>
