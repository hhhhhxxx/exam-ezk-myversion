<!--考试列表页面-->
<template>
  <div class="main-container">
    <div class="ks_tips">退出或离开答题页面，答题计时不暂停，进入考试后请不要中途离开，以防超时系统自动收卷。</div>
    <div class="dr_box">
      <div class="dr_main">

        <div class="dr_thead">
          <h3 class="dr_title f1">考试列表</h3>
          <p style="float:right;position:relative;margin:14px;">
            <el-button type="primary" @click="$router.push({path:'/exam/paper/examcode'})">考试码</el-button>
            <span
              style="background: #e6e6e6;width:1px;height:30px;display:inline-block;margin-left: 10px;vertical-align: middle;"></span>
            <el-button type="primary" plain @click="getDataList">刷新</el-button>
          </p>
        </div>

        <el-form :inline="true" :model="formInline" class="form-inline">
          <el-form-item>
            <el-select v-model="formInline.state" placeholder="全部">
              <el-option :value="0" label="全部"></el-option>
              <el-option :value="50001" label="未开始"></el-option>
              <el-option :value="50002" label="进行中"></el-option>
              <el-option :value="50004" label="已结束"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="formInline.mean" placeholder="选择考试方式">
              <el-option value="网页考试"></el-option>
              <el-option value="APP考试"></el-option>
              <el-option value="网页或APP考试"></el-option>
              <el-option value="未设置终端"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-input v-model="formInline.name" placeholder="请输入考试名称查找"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="onSubmit">查找</el-button>
          </el-form-item>
        </el-form>

        <div class="dr_table">
          <el-table :data="tableData" :stripe=true
                    style="width: 100%">
            <el-table-column width="50px" prop="id" label="编号"></el-table-column>
            <el-table-column prop="name" label="试卷名称"></el-table-column>
            <el-table-column prop="examStartTime" label="考试时间"></el-table-column>
            <el-table-column prop="examTime" label="考试时长(分钟)"></el-table-column>
            <el-table-column prop="examStatusCode" label="状态">
              <template slot-scope="scope">
                <p v-if="scope.row.examStatusCode === 50001">未开始</p>
                <p v-if="scope.row.examStatusCode === 50002 || scope.row.examStatusCode === 50003">进行中</p>
                <p v-if="scope.row.examStatusCode === 50004">已结束</p>
              </template>
            </el-table-column>
            <el-table-column prop="score" label="分数"></el-table-column>
            <el-table-column prop="allowMultidevice" label="考试方式">
              <template slot-scope="scope">
                <p v-if="scope.row.allowMultidevice === '0'">电脑端考试</p>
                <p v-if="scope.row.allowMultidevice === '1'">app端考试</p>
                <p v-if="scope.row.allowMultidevice === '2'">电脑端和app端考试</p>
              </template>
            </el-table-column>
            <el-table-column prop="operate" label="操作" align="center">
              <template slot-scope="scope">
                <el-button v-if="scope.row.examStatusCode !== 50004"
                           @click="$router.push({path:`/examsystem/examnote/${scope.row.id}`})">进入考试
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

// import Pagination from '@/components/Pagination'

export default {
  // eslint-disable-next-line vue/no-unused-components
  // components: { Pagination },

  data () {
    return {
      formInline: {
        state: 0,
        mean: '',
        name: ''
      },
      tableData: [{
        id: '',
        name: '',
        time: '',
        duration: '',
        state: '',
        score: '',
        mean: '',
        operate: ''
      }]
    }
  },
  created () {
    this.getDataList()
  },
  methods: {
    onSubmit () {
      this.getDataList()
    },

    getDataList () {
      this.$axios.get(`/api/examsystem/manage/getExamPaperList/${this.formInline.state}`).then(res => {
        this.tableData = res.data.response
      })
    }
  }

}
</script>

<style lang="scss" scoped>

.dr_box {
  padding: 20px;
}

.ks_tips {
  padding: 10px 15px;
  line-height: 1.5;
  background: #88c3f4;
  font-size: 16px;
  color: #FFFFFF;
  text-align: center;
}

.dr_main {
  background: #FFFFFF;
  min-height: 300px;
  padding-bottom: 30px;
  box-shadow: #aca6a6;
}

.dr_thead {
  height: 63px;
  padding: 0 50px 0 30px;
  margin-bottom: 20px;
  border-bottom: solid #f7f7f7 1px;
}

.dr_thead .dr_title {
  line-height: 63px;
  font-size: 16px;
  color: #333;
  font-weight: bold;
}

.l_cta2 {
  width: 72px;
  height: 34px;
  line-height: 34px;
  color: #0099ff;
  border-radius: 4px;
  background-color: #ffffff;
  font-size: 14px;
  text-align: center;
  display: inline-block;
  border: 1px solid #0099ff;
}

.dr_table {
  margin: 20px 30px 0;
  border-top: solid #ebebeb 1px;
}

.main-container {
  border-radius: 10px;
  margin: 20px;
  width: 100%;
  height: auto;

  border: 0 solid #ededed;
  //box-shadow: 0 0 40px #ddd;
  background-color: white;

}
</style>
