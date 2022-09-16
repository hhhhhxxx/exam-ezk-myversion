<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true">
      <el-form-item label="试卷名称：">
        <el-input v-model="queryParam.paperId"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm">查询</el-button>
        <router-link :to="{ path: '/user/student/edit' }" class="link-left">
          <el-button type="primary">导出</el-button>
        </router-link>
      </el-form-item>
    </el-form>

    <el-table
      v-loading="listLoading"
      :data="tableData"
      border
      fit
      highlight-current-row
      style="width: 100%"
    >
      <el-table-column prop="id" label="序号" align="center" />
      <el-table-column prop="paperName" label="试卷名称" align="center" />
      <el-table-column prop="studentName" label="学生姓名" align="center" />
      <el-table-column label="得分" align="center">
        <template slot-scope="{ row }">
          {{ row.studentScore }}/{{ row.totalScore }}
        </template>
      </el-table-column>
      <el-table-column label="题目对错" width="100px;" align="center">
        <template slot-scope="{ row }">
          {{ row.studentReal }}/{{ row.totalTitle }}
        </template>
      </el-table-column>
      <el-table-column prop="consumeTime" label="耗时/min" align="center" />
      <el-table-column
        prop="submitTime"
        label="提交时间"
        width="160px"
        align="center"
      />
      <el-table-column width="150px" label="操作" align="center">
        <template slot-scope="{ row }">
          <router-link
            :to="{
              path: '/answer/correct/finish/check',
              query: { id: row.id },
            }"
            class="link-left"
          >
            <el-button size="mini">查看</el-button>
          </router-link>
          <el-button
            size="mini"
            type="danger"
            @click="deleteUserPaper(row)"
            class="link-left"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParam.pageIndex"
      :limit.sync="queryParam.pageSize"
      @pagination="search"
    />
  </div>
</template>

<script>
import { mapGetters, mapState } from 'vuex'
import Pagination from '@/components/Pagination'
import examPaperAnswerApi from '@/api/examPaperAnwser'

export default {
  components: { Pagination },
  data () {
    return {
      queryParam: {
        paperId: ''
      },
      listLoading: true,
      tableData: [],
      total: 0
    }
  },
  created () {
    this.search()
  },
  methods: {
    search () {
      this.listLoading = true
      examPaperAnswerApi.getFinishPaperList(this.queryParam).then((data) => {
        const re = data.response
        this.tableData = re.list
        this.total = re.total
        this.queryParam.pageIndex = data.pageNum
        this.listLoading = false
      })
    },
    deleteUserPaper (row) {
      let _this = this
      userApi.deleteUser(row.id).then((re) => {
        if (re.code === 1) {
          _this.search()
          _this.$message.success(re.message)
        } else {
          _this.$message.error(re.message)
        }
      })
    },
    submitForm () {
      this.queryParam.pageIndex = 1
      this.search()
    }
  }
}
</script>
