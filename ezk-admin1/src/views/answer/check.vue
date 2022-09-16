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
            <div class="exam-result">
              <p>
                结果：
                <el-tag size="small" type="danger" v-if="item.true === 0"
                  >错误</el-tag
                >
                <el-tag size="small" type="success" v-else>正确</el-tag>
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
              <p>分数：{{ item.totalScore }}</p>
              <p>得分：{{ item.myScore }}</p>
            </div>
          </div>
        </div>
      </el-form>
    </el-main>
  </div>
</template>

<script>
import examPaperAnswerApi from "@/api/examPaperAnwser";
export default {
  data() {
    return {
      title: "",
      student: "",
      time: "",
      perScore: "",
      totalNum: "",
      difficult: 3,
      paperList: [],
      score: 0.0,
      formData: {},
    };
  },
  created() {
    this.search();
  },
  methods: {
    search() {
      examPaperAnswerApi.checkPaper(this.queryParam).then((data) => {
        const re = data.response;
        this.paperList = re.list;
        this.title = re.title;
        this.student = re.student;
        this.time = re.time;
        this.perScore = re.perScore;
        this.totalNum = re.totalNum;
      });
    },
    submitForm() {
      this.$message({
        message: "已完成批改",
        type: "success",
      });
      this.$router.push("/answer/list/");
    },
    answerCorrect(correct) {
      if (correct === 1) return "success";
      else return "danger";
    },
    goAnchor(selector) {
      this.$el.querySelector(selector).scrollIntoView({
        behavior: "instant",
        block: "center",
        inline: "nearest",
      });
    },
  },
};
</script>

<style>
</style>