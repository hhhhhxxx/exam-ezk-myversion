<template>
  <div class="com-container">
    <div
      id="echarts-paragraph"
      v-loading="loading"
      style="width: 800px; height: 500px; margin: 10px auto"
    />
  </div>
</template>

<script>
import analysisApi from "@/api/analysis";
import resize from "./mixins/resize";
export default {
  name: "Score",
  mixins: [resize],
  data() {
    return {
      echartsParagraph: null,
      //   allData: null, // 从服务器中获取的所有数据
      //   showChoice: false, // 是否显示可选项
      //   choiceType: "map", // 显示的数据类型
      titleFontSize: 0, // 指明标题的字体大小
      allData: null,
      loading: false,
    };
  },
  props: ["className"],
  mounted() {
    this.initChart();
    this.getData();
  },
  methods: {
    initChart() {
      this.echartsParagraph = echarts.init(
        document.getElementById("echarts-paragraph"),
        "westeros"
      );
      const initOption = {
        title: {
          text: "各科分数段人数",
        },
        grid: {
          left: "3%",
          top: "8%",
          right: "10%",
          bottom: "4%",
          containLabel: true,
        },
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "cross",
            label: {
              backgroundColor: "#6a7985",
            },
          },
        },
        xAxis: {
          type: "category",
          boundaryGap: false,
        },
        yAxis: {
          type: "value",
        },
        toolbox: {
          feature: {
            dataZoom: {
              yAxisIndex: "none",
            },
            restore: {},
            saveAsImage: {},
          },
        },
        dataZoom: [
          {
            type: "inside",
            start: 0,
            end: 100,
          },
          {
            start: 0,
            end: 100,
          },
        ],
      };
      this.echartsParagraph.setOption(initOption);
    },
    getData() {
      // this.className
      analysisApi.eachSubjectPara().then((re) => {
        this.allData = re;
        this.updateChart();
        this.loading = false;
      });
    },
    updateChart() {
      // 半透明的颜色值
      const colorArr1 = [
        "rgba(11, 168, 44, 0.5)",
        "rgba(44, 110, 255, 0.5)",
        "rgba(22, 242, 217, 0.5)",
        "rgba(254, 33, 30, 0.5)",
        "rgba(250, 105, 0, 0.5)",
      ];
      // 全透明的颜色值
      const colorArr2 = [
        "rgba(11, 168, 44, 0)",
        "rgba(44, 110, 255, 0)",
        "rgba(22, 242, 217, 0)",
        "rgba(254, 33, 30, 0)",
        "rgba(250, 105, 0, 0)",
      ];
      // 处理数据
      // 类目轴的数据
      const timeArr = this.allData.para;
      // y轴的数据 series下的数据
      const valueArr = this.allData.list;
      const seriesArr = valueArr.map((item, index) => {
        return {
          name: item.name,
          type: "line",
          data: item.data,
          stack: "choice",
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
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
        };
      });
      // 图例的数据
      const legendArr = valueArr.map((item) => {
        return item.name;
      });
      const dataOption = {
        xAxis: {
          data: timeArr,
        },
        legend: {
          data: legendArr,
        },
        series: seriesArr,
      };
      this.echartsParagraph.setOption(dataOption);
    },
  },
};
</script>

<style lang="scss" scoped>
.title {
  position: absolute;
  left: 20px;
  top: 20px;
  z-index: 10;
  color: white;
  .title-icon {
    margin-left: 10px;
    cursor: pointer;
  }
  .select-con {
    background-color: #222733;
  }
}
</style>
