<template>
  <div class="dashboard-container">
    <el-row :gutter="40" class="panel-group">
      <el-col
        :xs="12"
        :sm="12"
        :lg="8"
        class="card-panel-col"
        v-for="item in classes"
        :key="item"
      >
        <div class="card-panel" @click="skipToClass(item)">
          <div class="card-panel-icon-wrapper icon-people">
            <svg-icon icon-class="class" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">{{ item }}</div>
          </div>
        </div>
      </el-col>
      <!-- <el-col :xs="12" :sm="12" :lg="8" class="card-panel-col">
        <div class="card-panel" @click="skipToClass">
          <div class="card-panel-icon-wrapper icon-message">
            <svg-icon icon-class="class" class-name="card-panel-icon" />
          </div>
          <div
            class="card-panel-description"
            @mouseover="classLoading2 = true"
            @mouseleave="classLoading2 = false"
            v-bind:class="
              classLoading2 ? 'animate__animated animate__rubberBand' : ''
            "
          >
            <div class="card-panel-text">题目总数</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="8" class="card-panel-col">
        <div class="card-panel" @click="skipToClass">
          <div class="card-panel-icon-wrapper icon-shopping">
            <svg-icon icon-class="class" class-name="card-panel-icon" />
          </div>
          <div
            class="card-panel-description"
            @mouseover="classLoading3 = true"
            @mouseleave="classLoading3 = false"
            v-bind:class="
              classLoading3 ? 'animate__animated animate__rubberBand' : ''
            "
          >
            <div class="card-panel-text">答卷总数</div>
          </div>
        </div>
      </el-col> -->
    </el-row>
    <el-row class="echarts-line">
      <div
        id="echarts-moth-user"
        style="width: 700px; height: 500px; margin: 10px auto"
        v-loading="loading"
      />
    </el-row>
    <el-row class="echarts-line">
      <div
        id="echarts-moth-question"
        style="width: 700px; height: 500px; margin: 10px auto"
        v-loading="loading"
      />
    </el-row>
  </div>
</template>

<script>
import resize from './components/mixins/resize'
import analysisApi from '@/api/analysis'
import 'animate.css'
export default {
  mixins: [resize],
  data () {
    return {
      echartsUserAction: null,
      echartsQuestion: null,
      loading: false,
      classLoading1: false,
      classLoading2: false,
      classLoading3: false,
      classLoading4: false,
      teacherId: '',
      classes: []
    }
  },
  mounted () {
    this.echartsUserAction = echarts.init(
      document.getElementById('echarts-moth-user'),
      'westeros'
    )
    // eslint-disable-next-line no-undef
    this.echartsQuestion = echarts.init(
      document.getElementById('echarts-moth-question'),
      'westeros'
    )
    let _this = this
    this.loading = true
    analysisApi.scoreAnalysis().then((re) => {
      let response = re.data
      var myOption = {
        title: {
          text: '占比人数',
          x: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          top: '5%',
          left: 'center'
        },
        series: [
          {
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '40',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: response.valueData
          }
        ]
      }
      _this.echartsUserAction.setOption(
        this.option(
          '分数段人数',
          '{b}分数段{c}人',
          response.categoryData,
          response.valueData,
          'bar'
        )
      )
      _this.echartsQuestion.setOption(myOption)
      this.loading = false
    })
    analysisApi.selectTeacherClass(this.teacherId).then((re) => {
      this.classes = re.list
    })
  },
  methods: {
    option (title, formatter, label, value, type) {
      return {
        title: {
          text: title,
          x: 'center'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'line',
            z: 0,
            lineStyle: {
              color: '#2D3443'
            }
          },
          formatter: formatter
        },
        xAxis: {
          type: 'category',
          data: label
        },
        grid: {
          left: 10,
          right: 10,
          bottom: 20,
          top: 40,
          containLabel: true
        },
        legend: {
          show: true
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: value,
            type: type,
            barWidth: '30px',
            markPoint: {
              data: [
                { type: 'max', name: 'Max' },
                { type: 'min', name: 'Min' }
              ]
            },
            itemStyle: {
              // 指明颜色渐变的方向
              // 指明不同百分比之下颜色的值
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                // 百分之0状态之下的颜色值
                {
                  offset: 0,
                  color: '#5052EE'
                },
                // 百分之100状态之下的颜色值
                {
                  offset: 1,
                  color: '#AB6EE5'
                }
              ]),
              barBorderRadius: [50, 50, 0, 0]
            }
          }
        ]
      }
    },
    skipToClass (item) {
      this.$router.push('/analysis/class')
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width: 1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}

.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .github-corner {
    position: absolute;
    top: 0px;
    border: 0;
    right: 0;
  }

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width: 1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}

.panel-group {
  margin-top: 18px;

  .card-panel-col {
    margin-bottom: 32px;
  }

  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, 0.05);
    border-color: rgba(0, 0, 0, 0.05);

    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-people {
        background: #40c9c6;
      }

      .icon-message {
        background: #36a3f7;
      }

      .icon-money {
        background: #f4516c;
      }

      .icon-shopping {
        background: #34bfa3;
      }
    }

    .icon-people {
      color: #40c9c6;
    }

    .icon-message {
      color: #36a3f7;
    }

    .icon-money {
      color: #f4516c;
    }

    .icon-shopping {
      color: #34bfa3;
    }

    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
      float: right;
      font-weight: bold;
      margin-right: 26px;
      margin-left: 0px;

      .card-panel-text {
        line-height: 108px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
      }

      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}

@media (max-width: 550px) {
  .card-panel-description {
    display: none;
  }

  .card-panel-icon-wrapper {
    float: none !important;
    width: 100%;
    height: 100%;
    margin: 0 !important;

    .svg-icon {
      display: block;
      margin: 14px auto !important;
      float: none !important;
    }
  }
}

.echarts-line {
  background: #fff;
  padding: 16px 16px 0;
  margin-bottom: 32px;
}
</style>
