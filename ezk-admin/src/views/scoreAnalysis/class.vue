<template>
  <div class="dashboard-container">
    <el-row class="echarts-line">
      <el-col :xs="12" :sm="12" :lg="22">
        <div
          id="echarts-sex"
          v-loading="loading"
          style="width: 500px; height: 500px; margin: 10px auto"
        />
        <!-- style="width: 700px; height: 500px; margin: 10px auto" -->
      </el-col>
    </el-row>
    <el-row class="echarts-line">
      <el-col :xs="12" :sm="12" :lg="24">
        <Score className="class1" />
      </el-col>
    </el-row>
    <el-row class="echarts-line">
      <el-col :xs="12" :sm="12" :lg="24">
        <Student className="class1" />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import resize from "./components/mixins/resize";
import analysisApi from "@/api/analysis";
import Score from "./components/score.vue";
import Student from "./components/student.vue";
export default {
  mixins: [resize],
  components: {
    Score,
    Student,
  },
  data() {
    return {
      echartsUserAction: null,
      echartsQuestion: null,
      loading: false,
    };
  },
  mounted() {
    // eslint-disable-next-line no-undef
    this.echartsQuestion = echarts.init(
      document.getElementById("echarts-sex"),
      "westeros"
    );
    let _this = this;
    this.loading = true;

    analysisApi.selectByClass().then((re) => {
      let girlsAndBoys = re.girlsAndBoys;
      const option1 = {
        title: {
          text: "男女生比例",
          x: "center",
        },
        tooltip: {
          trigger: "item",
        },
        legend: {
          top: "10%",
          left: "left",
        },
        series: [
          {
            type: "pie",
            radius: ["40%", "70%"],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: "#fff",
              borderWidth: 2,
            },
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: "rgba(0, 0, 0, 0.5)",
              },
            },
            data: [
              {
                value: girlsAndBoys.girls,
                name: "女",
              },
              {
                value: girlsAndBoys.boys,
                name: "男",
              },
            ],
          },
        ],
      };
      _this.echartsQuestion.setOption(option1);
      this.loading = false;
    });
  },
};
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;
}
.echarts-line {
  background: #fff;
  padding: 16px 16px 0;
  margin-bottom: 32px;
}
canvas {
  border-radius: 20px;
}
</style>