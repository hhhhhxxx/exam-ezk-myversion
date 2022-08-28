<template>
  <div class="app-container">
    <el-form
      :inline="true"
      :model="dataForm"
      @keyup.enter.native="getDataList()"
    >
      <el-form-item label="考试状态：">
        <el-select
          v-model="examStatusOption"
          clearable
          placeholder="请选择考试状态"
          @change="getDataList()"
        >
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <!--        <el-button v-if="isAuth('examsystem:texamarguments:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>-->
        <!--        <el-button  type="primary" @click="addOrUpdateHandle()">新增</el-button>-->
        <!--        <el-button v-if="isAuth('examsystem:texamarguments:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>-->
        <!--        <el-button type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>-->
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      style="width: 100%"
    >
      <!-- <el-table-column
        type="selection"
        header-align="center"
        align="center"
        width="50"
      >
      </el-table-column> -->
      <el-table-column
        prop="examPaperEditRequestVM.id"
        header-align="center"
        align="center"
        label="id"
        width="60"
      >
      </el-table-column>
      <el-table-column
        prop="examPaperEditRequestVM.name"
        header-align="center"
        align="center"
        label="试卷名称"
        width="80"
      >
      </el-table-column>
      <el-table-column
        prop="examPaperEditRequestVM.name"
        header-align="center"
        align="center"
        label="考生人数"
        width="80"
      >
      </el-table-column>
      <el-table-column
        prop="examStartTime"
        header-align="center"
        align="center"
        label="开始时间"
        width="100"
      >
      </el-table-column>
      <el-table-column
        prop="examEndTime"
        header-align="center"
        align="center"
        label="截止时间"
        width="100"
      >
      </el-table-column>
      <el-table-column
        prop="examBatch"
        header-align="center"
        align="center"
        label="考试批次"
      >
      </el-table-column>
      <el-table-column
        prop="examRound"
        header-align="center"
        align="center"
        label="考试场次"
      >
      </el-table-column>
      <el-table-column header-align="center" align="center" label="及格/总分">
        <template slot-scope="scope">
          <p>
            {{ scope.row.passScore }} /
            {{ scope.row.examPaperEditRequestVM.score }}
          </p>
        </template>
      </el-table-column>
      <el-table-column
        prop="examPaperEditRequestVM.teacherName"
        header-align="center"
        align="center"
        label="监考老师"
      >
      </el-table-column>
      <el-table-column
        prop="allowRedo"
        header-align="center"
        align="center"
        label="重做次数"
      >
      </el-table-column>
      <el-table-column
        prop="allowMultidevice"
        header-align="center"
        align="center"
        label="多终端考试"
      >
        <template slot-scope="scope">
          <p v-if="scope.row.allowMultidevice === '0'">电脑端考试</p>
          <p v-if="scope.row.allowMultidevice === '1'">app考试</p>
          <p v-if="scope.row.allowMultidevice === '2'">允许电脑端与app考试</p>
        </template>
      </el-table-column>
      <el-table-column prop="status" align="center" label="状态">
        <template slot-scope="{ row }">
          <p v-if="row.status === '0'">未开始</p>
          <p v-if="row.status === '1'" style="color: green">进行中</p>
          <p v-if="row.status === '2'" style="color: orange">已结束</p>
        </template>
      </el-table-column>
      <el-table-column
        header-align="center"
        width="100"
        label="操作"
        align="center"
      >
        <template slot-scope="{ row }">
          <!-- <router-link
            :to="{ path: '/manage/detail', params: { id: row.examPaperEditRequestVM.id } }"
            class="link-left"
          >
            <el-button size="mini">考试详情</el-button>
          </router-link> -->
          <el-button
            size="mini"
            @click="toManageDetail(row.examPaperEditRequestVM.id)"
            class="link-left"
            style="margin-top: 10px"
            >考试详情</el-button>
          <el-button
            size="mini"
            @click="delExam(row.examPaperEditRequestVM.id)"
            class="link-left"
            style="margin-top: 10px"
            >删除考试</el-button>
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
        page: this.pageIndex,
        limit: this.pageSize,
        examStatusCode: this.examStatusOption
      }
      console.log(requestData)
      this.$axios({
        url: '/api/examsystem/manage/list',
        method: 'get',
        params: requestData
      }).then((res) => {
        console.log(res.data.response)
        this.dataList = res.data.response
        this.dataListLoading = false
      })
    },

    // editExamPaper(id) {
    //   this.$router.push(`/exam/paper/edit?id=${id}`);
    // },

    // 删除考试
    delExam (id) {
      let requestData = {
        paperId: id
      }
      this.$axios
        .post(`/api/examsystem/manage/delExam`, requestData)
        .then((res) => {
          this.$message.success('删除成功')
          this.getDataList()
        })
    },
    // 复选框改变
    selectionChangeHandle (val) {
      this.dataListSelections = val
    },

    toManageDetail (id) {
      this.$router.push(`/manage/detail/${id}`)
    }
  }
}
</script>

<style scoped>
</style>
