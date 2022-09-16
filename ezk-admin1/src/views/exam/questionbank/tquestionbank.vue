<template>
  <div class="app-container">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column
        type="selection"
        header-align="center"
        align="center"
        width="50">
      </el-table-column>
      <el-table-column
        prop="id"
        header-align="center"
        align="center"
        label="题库id">
      </el-table-column>
      <el-table-column
        prop="name"
        header-align="center"
        align="center"
        label="题库名字">
      </el-table-column>
      <el-table-column
        prop="description"
        header-align="center"
        align="center"
        label="题库简介">
      </el-table-column>
      <el-table-column
        prop="isLock"
        header-align="center"
        align="center"
        label="题库锁定">
        <template slot-scope="scope">
          <p v-if="scope.row.isLock === 1">已锁定</p>
          <p v-if="scope.row.isLock === 0">未锁定</p>
        </template>
      </el-table-column>
      <el-table-column
        prop="isPass"
        header-align="center"
        align="center"
        label="题库审批情况">
        <template slot-scope="scope">
          <p v-if="scope.row.isPass === 1">通过</p>
          <p v-if="scope.row.isPass === 0">未通过</p>
        </template>
      </el-table-column>
      <el-table-column
        prop="img"
        header-align="center"
        align="center"
        label="题库封面图片">
        <template slot-scope="scope">
          <img :src="scope.row.img" style="width: 300px;height: 200px">
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="toBankAddQuestion(scope.row.id)">添加题目</el-button>
          <el-button type="text" size="small" @click="showBankQuestion(scope.row)">查看题目</el-button>
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>

    <el-dialog :visible.sync="bank.dialog" width="70%" :title="currentBankTitle">
      <div v-for="(data,i) of questionData" :key="i">
        <div style="line-height: 1.8">
          <div v-if="data.questionType == 1" >
            <div class="q-title" v-html="data.title" />
            <div class="q-content">
        <span
          :key="item.id"
          v-for="item in data.items"
          class="q-item-contain"
        >
          <span class="q-item-prefix">{{ item.prefix }}</span>
          <span v-html="item.content" class="q-item-content"></span>
        </span>
            </div>
          </div>
          <div v-else-if="data.questionType == 2">
            <div class="q-title" v-html="data.title" />
            <div class="q-content">
        <span
          :key="item.id"
          v-for="item in data.items"
          class="q-item-contain"
        >
          <span class="q-item-prefix">{{ item.prefix }}</span>
          <span v-html="item.content" class="q-item-content"></span>
        </span>
            </div>
          </div>
          <div v-else-if="data.questionType == 3">
            <div
              class="q-title"
              v-html="data.title"
              style="display: inline; margin-right: 10px"
            />
            <span>（</span>
            <span :key="item.id" v-for="item in data.items">
        <span v-html="item.content" class="q-item-content"></span>
      </span>
            <span>）</span>
          </div>
          <div v-else-if="data.questionType == 4">
            <div class="q-title" v-html="data.title" />
          </div>
          <div v-else-if="data.questionType == 5">
            <div class="q-title" v-html="data.title" />
          </div>
          <div v-else></div>
        </div>

        <el-divider></el-divider>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import AddOrUpdate from './tquestionbank-add-or-update'
import QuestionShow from '@/views/exam/question/components/Show'
export default {
  data () {
    return {
      dataForm: {
        key: ''
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
      bank: {
        dialog: false
      },
      questionShow: {
        dialog: false,
        question: null,
        loading: false
      },
      questionData: [],
      currentBankTitle: '',
    }
  },
  components: {
    AddOrUpdate,
    QuestionShow
  },
  created () {
    this.getDataList()
  },
  methods: {
    showBankQuestion (row) {
      this.bank.dialog = true
      this.currentBankTitle = row.name
      this.$axios.post(`/api/examsystem/tquestionbank/getQuestionByBank`, { 'bankId': row.id }).then(res => {
        this.questionData = res.data.data
      })
    },

    toBankAddQuestion (id) {
      this.$router.push(`/exam/questionbank/bankadd?bankId=${id}`)
    },

    // 获取数据列表
    getDataList () {
      this.dataListLoading = true

      this.$axios({
        url: '/api/examsystem/tquestionbank/list',
        method: 'get',
        params: {
          'page': this.pageIndex,
          'limit': this.pageSize,
          'key': this.dataForm.key
        }
      }).then((res) => {
        let data = res.data
        if (data && data.code === 0) {
          this.dataList = data.page.list
          this.totalPage = data.page.totalCount
        } else {
          this.dataList = []
          this.totalPage = 0
        }
        this.dataListLoading = false
      })
    },
    // 每页数
    sizeChangeHandle (val) {
      this.pageSize = val
      this.pageIndex = 1
      this.getDataList()
    },
    // 当前页
    currentChangeHandle (val) {
      this.pageIndex = val
      this.getDataList()
    },
    // 多选
    selectionChangeHandle (val) {
      this.dataListSelections = val
    },
    // 新增 / 修改
    addOrUpdateHandle (id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id)
      })
    },
    // 删除
    deleteHandle (id) {
      var ids = id ? [id] : this.dataListSelections.map(item => {
        return item.id
      })
      this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios({
          url: '/examsystem/tquestionbank/delete',
          methods: 'post',
          data: {
            ids: ids
          }
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.getDataList()
              }
            })
          } else {
            this.$message.error(data.msg)
          }
        })
      })
    }
  }
}
</script>
