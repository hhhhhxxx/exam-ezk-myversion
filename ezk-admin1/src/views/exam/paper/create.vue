<template>
  <div class="app-container">
    <el-header class="header">
      <el-form :inline="true">
        <el-form-item label="标题:" style="padding-right: 30px">
          <el-input placeholder="新建试卷" v-model="title"></el-input>
        </el-form-item>
        <el-form-item label="难度:">
          <el-select clearable v-model="select">
            <el-option
              v-for="(item, index) in options"
              :key="index"
              :value="item"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
    </el-header>
    <el-aside class="my-side">
      <el-header style="height: 38px">
        <span style="padding-right: 30px"
          >题量:
          <i style="color: red">{{ totalExam }}</i>
        </span>
        <span
          >总分:
          <i style="color: red">{{ totalNum }}</i>
        </span>
      </el-header>
      <el-row>
        <el-col :span="24">
          <div>
            <p>
              1 单选题(共{{ single.singleNum }}题，{{ single.singleScore }}分)
            </p>
            <span
              style="padding-left: 8px"
              v-for="(item, index) in single.singleNum"
              :key="index"
            >
              <span>({{ ++index }})</span>
            </span>
          </div>
          <div>
            <p>
              2 多选题(共{{ multiple.multipleNum }}题，{{
                multiple.multipleScore
              }}分)
            </p>
            <span
              style="padding-left: 8px"
              v-for="(item, index) in multiple.multipleNum"
              :key="index"
            >
              <span>({{ ++index }})</span>
            </span>
          </div>
          <div>
            <p>
              3 填空题(共{{ gapFilling.gapFillingNum }}题，{{
                gapFilling.gapFillingScore
              }}分)
            </p>
            <span
              style="padding-left: 8px"
              v-for="(item, index) in gapFilling.gapFillingNum"
              :key="index"
            >
              <span>({{ ++index }})</span>
            </span>
          </div>
          <div>
            <p>
              4 判断题(共{{ trueFalse.trueFalseNum }}题，{{
                trueFalse.trueFalseScore
              }}分)
            </p>
            <span
              style="padding-left: 8px"
              v-for="(item, index) in trueFalse.trueFalseNum"
              :key="index"
            >
              <span>({{ ++index }})</span>
            </span>
          </div>
          <div>
            <p>
              5 简答题(共{{ shortAnswer.shortAnswerNum }}题，{{
                shortAnswer.shortAnswerScore
              }}分)
            </p>
            <span
              style="padding-left: 8px"
              v-for="(item, index) in shortAnswer.shortAnswerNum"
              :key="index"
            >
              <span>({{ ++index }})</span>
            </span>
          </div>
        </el-col>
      </el-row>
    </el-aside>
    <el-main class="my-content">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="判断题" name="first">
          <true-false />
        </el-tab-pane>
        <el-tab-pane label="单选题" name="second">
          <single-choice />
        </el-tab-pane>
        <el-tab-pane label="多选题" name="third">
          <multiple-choice />
        </el-tab-pane>
        <el-tab-pane label="填空题" name="fourth">
          <gap-filling />
        </el-tab-pane>
        <el-tab-pane label="简答题" name="fifth">
          <short-answer />
        </el-tab-pane>
      </el-tabs>
    </el-main>
  </div>
</template>

<script>
import SingleChoice from "./component/singlechoice.vue";
import GapFilling from "./component/gap-filling.vue";
import MultipleChoice from "./component/multiple-choice.vue";
import ShortAnswer from "./component/short-answer.vue";
import TrueFalse from "./component/true-false.vue";
import examPaperAnswerApi from "@/api/examPaperAnwser";
export default {
  components: {
    SingleChoice,
    GapFilling,
    MultipleChoice,
    ShortAnswer,
    TrueFalse,
  },
  data() {
    return {
      options: ["难", "易", "适中"],
      select: "易",
      title: "",
      totalNum: 0,
      totalExam: 0,
      activeName: "first",
      single: {},
      multiple: {},
      trueFalse: {},
      gapFilling: {},
      shortAnswer: {},
    };
  },
  mounted() {
    this.getTotalTitle();
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    },
    getTotalTitle() {
      let _this = this;
      examPaperAnswerApi.titleList().then((data) => {
        console.log(data);
        _this.single = data.single;
        _this.multiple = data.multiple;
        _this.trueFalse = data.trueFalse;
        _this.shortAnswer = data.shortAnswer;
        _this.gapFilling = data.gapFilling;
        _this.totalExam =
          _this.single.singleNum +
          _this.multiple.multipleNum +
          _this.shortAnswer.shortAnswerNum;
        _this.totalNum =
          _this.single.singleScore + _this.multiple.multipleScore;
      });
    },
  },
};
</script>
<style scoped lang="scss">
.el-row {
  margin-bottom: 10px;
  &:last-child {
    margin-bottom: 0;
  }
}
.el-col {
  border-radius: 4px;
}
.bg-purple-dark {
  background: #99a9bf;
}
.bg-purple {
  background: #d3dce6;
}
.bg-purple-light {
  background: #e5e9f2;
}
.grid-content {
  border-radius: 4px;
  min-height: 36px;
}
.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}
</style>
