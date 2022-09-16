<!-- 商家销量统计的横向柱状图 -->
<template>
  <div class="com-container">
    <div
      ref="score"
      v-loading="loading"
      style="width: 800px; height: 500px; margin: 10px auto"
    />
  </div>
</template>

<script>
import analysisApi from "@/api/analysis";
export default {
  data() {
    return {
      chartInstance: null,
      allData: null, // 服务器返回的数据
      currentPage: 1, // 当前显示的页数
      totalLength: 0, // 一共有多少页
      timerId: null, // 定时器的标识
      loading: true,
    };
  },
  mounted() {
    this.initChart();
    this.getData();
  },
  destroyed() {
    clearInterval(this.timerId);
  },
  methods: {
    // 初始化echartInstance对象
    initChart() {
      this.chartInstance = echarts.init(this.$refs.score, "westeros");
      // 对图表初始化配置的控制
      const initOption = {
        title: {
          text: "各科学生成绩",
          left: 20,
          top: 20,
        },
        grid: {
          top: "20%",
          left: "3%",
          right: "6%",
          bottom: "3%",
          containLabel: true, // 距离是包含坐标轴上的文字
        },
        xAxis: {
          type: "category",
        },
        yAxis: {
          type: "value",
        },
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "line",
            z: 0,
            lineStyle: {
              color: "#2D3443",
            },
          },
        },
      };
      this.chartInstance.setOption(initOption);
      // 对图表对象进行鼠标事件的监听
      this.chartInstance.on("mouseover", () => {
        clearInterval(this.timerId);
      });
      this.chartInstance.on("mouseout", () => {
        this.startInterval();
      });
    },
    // 获取服务器的数据
    async getData() {
      this.allData = await analysisApi.eachSubjectScore();
      // 每5个元素显示一页
      this.totalLength =
        this.allData.totalLength % 5 === 0
          ? this.allData.totalLength / 5
          : this.allData.totalLength / 5 + 1;
      this.loading = false;
      this.updateChart();
      // 启动定时器
      this.startInterval();
    },
    // 更新图表
    updateChart() {
      // 半透明的颜色值
      const colorArr1 = ["#1c8ef0", "#5052EE", "#0066ff", "#99ff00", "#cc3366"];
      // 全透明的颜色值
      const colorArr2 = ["#83bff6", "#AB6EE5", "#00ffcc", "#99ff33", "#cc9999"];
      const start = (this.currentPage - 1) * 5;
      const end = this.currentPage * 5;
      const studentNames = this.allData.studentNames.slice(start, end);
      const seriesArr = this.allData.studentScore.map((item, index) => {
        return {
          name: item.name,
          type: "bar",
          data: item.data.slice(start, end),
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [
              {
                offset: 0,
                color: colorArr1[index],
              }, // %0的颜色值
              {
                offset: 1,
                color: colorArr2[index],
              }, // 100%的颜色值
            ]),
          },
          label: {
            show: true,
            position: "right",
            textStyle: {
              color: "white",
            },
          },
        };
      });
      const dataOption = {
        xAxis: {
          data: studentNames,
        },
        series: seriesArr,
      };
      this.chartInstance.setOption(dataOption);
    },
    startInterval() {
      if (this.timerId) {
        clearInterval(this.timerId);
      }
      this.timerId = setInterval(() => {
        this.currentPage++;
        if (this.currentPage > this.totalLength) {
          this.currentPage = 1;
        }
        this.updateChart();
      }, 2000);
    },
  },
};
</script>

<style lang="less" scoped>
</style>
