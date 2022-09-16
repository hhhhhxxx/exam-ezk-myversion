<template>
  <div class="teacher-list-container">
    <el-card>
      <div class="list-container">
        <el-button style="margin-bottom: 10px" class="confirm-button" type="primary" @click="importExcel">导出excel<i class="el-icon-download el-icon--right"></i></el-button>
      </div>

      <el-table
        :data="tableData"
        stripe
        >
        <el-table-column
          fixed
          prop="studentName"
          label="学生姓名"
          width="100">
        </el-table-column>
        <el-table-column
          prop="numOverCount"
          label="人数异常次数"
          width="120">
          <template slot-scope="scope">
            <p v-if="scope.row.numOverCount > 10" style="color: red">{{scope.row.numOverCount}}</p>
            <p v-else>{{scope.row.numOverCount}}</p>
          </template>
        </el-table-column>
        <el-table-column
          prop="exitFullCount"
          label="退出全屏次数"
          width="120">
          <template slot-scope="scope">
            <p v-if="scope.row.exitFullCount > 10" style="color: red">{{scope.row.exitFullCount}}</p>
            <p v-else>{{scope.row.exitFullCount}}</p>
          </template>
        </el-table-column>
        <el-table-column
          prop="nonLiveCount"
          label="非活体次数"
          width="100">
          <template slot-scope="scope">
            <p v-if="scope.row.nonLiveCount > 10" style="color: red">{{scope.row.nonLiveCount}}</p>
            <p v-else>{{scope.row.nonLiveCount}}</p>
          </template>
        </el-table-column>
        <el-table-column
          prop="checkSex"
          label="检测性别"
          width="100">
          <template slot-scope="scope">
            <p v-if="scope.row.checkSex === 0">男</p>
            <p v-if="scope.row.checkSex === 1">女</p>
            <p v-if="scope.row.checkSex !== 1 && scope.row.checkSex !== 0">未检测</p>
          </template>
        </el-table-column>
        <el-table-column
          prop="beforeExamImg"
          label="考前人脸抓拍"
          width="200">
          <template slot-scope="scope">
            <img :src="scope.row.beforeExamImg" style="width: 100px;height: 100px">
          </template>
        </el-table-column>
        <el-table-column
          prop="duringExamImg"
          label="考试时抓拍"
          width="200">
          <template slot-scope="scope">
            <img :src="scope.row.duringExamImg" style="width: 100px;height: 100px">
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="创建时间"
          width="170">
          <template slot-scope="scope">
            <i class="el-icon-time"></i>
            {{scope.row.createTime}}
          </template>
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          >
          <template slot-scope="scope">
            <el-button @click="handleClick(scope.row)" size="mini" type="success">监考</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>

</template>

<script>
export default {
  name: 'list',
  data () {
    return {
      examArgumentsId: null, // exam_paper_Id
      tableData: [], // 监考记录
      examPaperList: [] // 试卷列表
    }
  },
  created () {
    this.examArgumentsId = this.$route.params.id
    if (this.examArgumentsId !== null) {
      this.getExamInfoList()
    }
  },
  methods: {
    // 获取考试监考记录列表
    getExamInfoList () {
      let data = {
        examArgumentsId: this.examArgumentsId
      }
      this.$axios.post('/api/examsystem/texaminfo/getExamInfoList', data).then(res => {
        this.tableData = res.data.entityList
        console.log(res.data.entityList)
      })
    },
    // 监考按钮
    handleClick (row) {
      this.$router.push(`/task/examsys/${row.studentName}/${this.examArgumentsId}`)
    },

    // 导出excel
    importExcel () {
      this.$axios({
        url: `/api/examsystem/texaminfo/downloadExcel/${this.examArgumentsId}`,
        method: 'get',
        responseType: 'blob'
      }).then(res => {
        let blob = new Blob([res.data], { type: res.data.type })
        const fileName = '考试' + this.examArgumentsId + '监考记录.xlsx'
        let downloadElement = document.createElement('a')
        let href = window.URL.createObjectURL(blob) // 创建下载的链接
        downloadElement.href = href
        downloadElement.download = fileName // 下载后文件名
        document.body.appendChild(downloadElement)
        downloadElement.click() // 点击下载
        document.body.removeChild(downloadElement) // 下载完成移除元素
        window.URL.revokeObjectURL(href) // 释放blob
        this.$message.success('[监考信息]已成功导出!')
      })
    }
  }
}
</script>

<style scoped>
.list-container {
  display: flex;
}

.confirm-button {
  width: 120px;
  height: 35px;
  margin-top: 5px;
  margin-left: 5px;
}
</style>
