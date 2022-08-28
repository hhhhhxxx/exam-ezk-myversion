<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item label="考试状态：">
        <el-select v-model="examStatusOption" clearable placeholder="请选择考试状态" @change="getDataList()">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      style="width: 100%;">
      <el-table-column
        prop="argumentId"
        header-align="center"
        align="center"
        label="id"
        width="60">
      </el-table-column>
      <el-table-column
        prop="examPaperEditRequestVM.name"
        header-align="center"
        align="center"
        label="试卷名称"
        width="180">
      </el-table-column>
      <el-table-column
        prop="examStatus"
        header-align="center"
        align="center"
        label="考试状态"
        width="120">
        <template slot-scope="scope">
          <p v-if="scope.row.examStatus === 50001">未开始</p>
          <p v-if="scope.row.examStatus === 50002" style="color: green">进行中</p>
          <p v-if="scope.row.examStatus === 50003" style="color: green">进行中,限制进入</p>
          <p v-if="scope.row.examStatus === 50004" style="color: orange">已结束</p>
        </template>
      </el-table-column>
      <el-table-column
        header-align="center"
        align="center"
        label="考试时间"
        width="190">
        <template slot-scope="scope">
          开始:{{scope.row.examStartTime}}
          <br>
          结束:{{scope.row.examEndTime}}
        </template>
      </el-table-column>
      <el-table-column
        prop="limitEnterTime"
        header-align="center"
        align="center"
        label="限制进入时间"
        width="120">
        <template slot-scope="scope">
          <p>{{scope.row.limitEnterTime}}&nbsp;分钟</p>
        </template>
      </el-table-column>
      <el-table-column
        prop="examBatch"
        header-align="center"
        align="center"
        label="考试批次"
      width="90">
      </el-table-column>
      <el-table-column
        prop="examRound"
        header-align="center"
        align="center"
        label="考试场次"
        width="90">
      </el-table-column>
      <el-table-column
        prop="teacherName"
        header-align="center"
        align="center"
        label="监考老师" width="90">
      </el-table-column>
      <el-table-column
        prop="allowRedo"
        header-align="center"
        align="center"
        label="重做次数"
      width="90">
      </el-table-column>
      <el-table-column
        prop="allowCheckScore"
        header-align="center"
        align="center"
        label="允许查看分数"
        width="130">
        <template slot-scope="scope" >
          <el-tag v-if="scope.row.allowCheckScore === 0" type="success">允许</el-tag>
          <el-tag v-if="scope.row.allowCheckScore === 1" type="danger">不允许</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="enableMonitor"
        header-align="center"
        align="center"
        label="监控抓拍" width="120">
        <template slot-scope="scope" >
          <el-tag v-if="scope.row.enableMonitor === 1" type="success">开启</el-tag>
          <el-tag v-if="scope.row.enableMonitor === 0" type="danger">关闭</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="allowMultidevice"
        header-align="center"
        align="center"
        label="多终端考试" width="120">
        <template slot-scope="scope" >
          <p v-if="scope.row.allowMultidevice === '0'">电脑端考试</p>
          <p v-if="scope.row.allowMultidevice === '1'">app考试</p>
          <p v-if="scope.row.allowMultidevice === '2'">允许电脑端与app考试</p>
        </template>
      </el-table-column>

      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="90"
        label="操作">
        <template slot-scope="scope">
          <el-button class="link-left" type="button" size="small" @click="toExamInfo(scope.row.argumentId)">考试详情</el-button>
          <br>
          <el-button style="margin-top: 10px" class="link-left" type="button" size="small" @click="toEditArgument(scope.row.argumentId)">修改考试</el-button>
          <br>
          <el-button style="margin-top: 10px" class="link-left" type="danger" size="small" @click="delExam(scope.row.argumentId)">删除考试</el-button>
        </template>
      </el-table-column>
    </el-table>
<!--    <el-pagination-->
<!--      @size-change="sizeChangeHandle"-->
<!--      @current-change="currentChangeHandle"-->
<!--      :current-page="pageIndex"-->
<!--      :page-sizes="[10, 20, 50, 100]"-->
<!--      :page-size="pageSize"-->
<!--      :total="totalPage"-->
<!--      layout="total, sizes, prev, pager, next, jumper">-->
<!--    </el-pagination>-->
    <!-- 弹窗, 新增 / 修改 -->
<!--    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>-->
  </div>
</template>

<script>
export default {
  name: 'list',
  data () {
    return {
      examStatusOption: null,
      options: [
        { value: 50001, label: '未开始' },
        { value: 50002, label: '进行中' },
        { value: 50004, label: '已结束' }
      ],
      dataForm: {
        key: ''
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false
    }
  },
  created () {
    this.getDataList()
  },
  methods: {
    getDataList () {
      this.dataListLoading = true
      let requestData = {
        'page': this.pageIndex,
        'limit': this.pageSize,
        'examStatusCode': this.examStatusOption === null ? null : this.examStatusOption
      }
      this.$axios({
        url: '/api/examsystem/manage/list',
        method: 'get',
        params: requestData
      }).then(res => {
        console.log(res.data.response)
        this.dataList = res.data.response
        this.dataListLoading = false
      })
    },

    // 跳转到编辑考试参数页面
    toEditArgument (argumentId) {
      this.$router.push(`/task/argument/edit/${argumentId}`)
    },

    // 考试详情
    toExamInfo (id) {
      this.$router.push(`/task/examsyslist/${id}`)
    },

    // 删除考试
    delExam (id) {
      let requestData = {
        argumentsId: id
      }
      this.$axios.post(`/api/examsystem/manage/delExam`, requestData).then(res => {
        this.$message.success('删除成功')
        this.getDataList()
      })
    }

  }
}
</script>

<style scoped>
.mod-config{
  margin: 20px;
}
</style>
