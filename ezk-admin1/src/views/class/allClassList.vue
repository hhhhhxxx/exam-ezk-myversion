<template>
  <div class="app-container">

    <el-form :model="queryParam" ref="queryForm" :inline="true">
      <el-form-item >
        <router-link :to="{ path: '/class/edit' }" class="link-left">
          <el-button type="primary">添加班级</el-button>
        </router-link>
      </el-form-item>
    </el-form>

    <el-table v-loading="listLoading" :data="tableData"
              border fit highlight-current-row
              style="width: 100%">
      <el-table-column prop="id" label="Id" align="center"/>
      <el-table-column prop="name" label="班级" align="center"/>
      <el-table-column prop="count" label="总人数" align="center"/>
      <el-table-column prop="description" label="描述" align="center"/>
      <el-table-column label="操作" align="center" width="400" fixed="right">
        <template slot-scope="{ row }" class="operatorBox">
          <el-button class="link-left" size="mini"
                     @click="$router.push({ path: '/class/edit', query: { id: row.id } })">班级信息
          </el-button>
          <el-button class="link-left" size="mini"
                     @click="$router.push({ path: '/class/student/list', query: { id: row.id, className: row.name } })">班级学生
          </el-button>
          <el-button class="link-left" size="mini"
                     @click="$router.push({ path: '/class/teacher/list', query: { id: row.id, className: row.name } })">班级老师
          </el-button>
          <el-button class="link-left" size="mini" @click="deleteClass(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0"
                :total="total"
                :page.sync="queryParam.pageIndex"
                :limit.sync="queryParam.pageSize"
                @pagination="search"/>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import classApi from '@/api/class'

export default {
  components: { Pagination },
  data () {
    return {
      queryParam: {
        // 顶死id
        classNmae: '',
        pageIndex: 1,
        pageSize: 10
      },
      listLoading: true,
      tableData: [],
      total: 0,
      className: ''
    }
  },
  created () {
    this.search()
  },
  methods: {
    submitForm () {
      this.queryParam.pageIndex = 1
      this.search()
    },
    search () {
      this.listLoading = true
      // 请求表格数据
      classApi.classList(this.queryParam).then((res) => {
        const result = res.response
        this.tableData = result.records
        this.total = result.total
        this.queryParam.pageIndex = result.current
        this.listLoading = false
      })
    },

    deleteClass (row) {
      let _this = this
      classApi.deleteClass(row.id).then((res) => {
        if (res.code === 1) {
          _this.search()
          _this.$message.success(res.message)
        } else {
          _this.$message.error(res.message)
        }
      })
    }
  },
  computed: {
  }
}
</script>

<style lang="scss" scoped>
.operatorBox {
}
</style>
