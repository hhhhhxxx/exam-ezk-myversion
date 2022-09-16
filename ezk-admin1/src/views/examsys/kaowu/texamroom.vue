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
    <el-table :data="dataList"
              border v-loading="dataListLoading"
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
        label="考场编排表id">
      </el-table-column>
      <el-table-column
        prop="roomBatch"
        header-align="center"
        align="center"
        label="考场批次">
      </el-table-column>
      <el-table-column
        prop="roomRound"
        header-align="center"
        align="center"
        label="考场场次">
      </el-table-column>
      <el-table-column
        prop="createTime"
        header-align="center"
        align="center"
        label="创建时间">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="addUser(scope.row.id)">添加考生</el-button>
          <el-button type="text" size="small" @click="listUser(scope.row.id)">查看考生</el-button>

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

    <el-dialog :visible.sync="dialog" style="width: 100%;height: 100%">

      <el-table :data="users" v-loading="dataListLoading" element-loading-text="数据加载中...">
        <el-table-column prop="id" label="用户ID" width="100"></el-table-column>
        <el-table-column label="头像" min-width="50">
          <template slot-scope="scope">
            <img class="avatar" :src="
                scope.row.portrait ||
                '//www.lgstatic.com/thumbnail_100x100/i/image2/M01/5E/65/CgotOVszSAOANi0LAAAse2IVWAE693.jpg'"
                 :alt="scope.row.name"
                 style="width: 50px;height: 50px"/>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="用户名" min-width="150"></el-table-column>
        <el-table-column prop="phone" label="手机号" min-width="150"></el-table-column>
        <el-table-column prop="createTime" label="注册时间" min-width="180" :formatter="dateFormatter"></el-table-column>
        <el-table-column
          prop="status"
          label="状态"
          align="center"
          min-width="120">
          <template slot-scope="scope">
            <i class="status status-success"
               title="正常"
               v-if="scope.row.status === 'ENABLE'"
               @click="handleToggleStatus(scope.row)"></i>
            <i class="status status-danger"
               title="禁用"
               v-else-if="scope.row.status === 'DISABLE'"></i>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" min-width="120">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              @click="addUserToRoom(scope.row)"
            >添加
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-button
        @click="saveAdd"
      >保存更改
      </el-button>
    </el-dialog>

    <el-dialog :visible.sync="dialog2" style="width: 100%;height: 100%">
      <el-table
        :data="users2"
        v-loading="dataListLoading"
        element-loading-text="数据加载中...">
        <el-table-column
          prop="id"
          header-align="center"
          align="center"
          label="考场与用户关联表id">
        </el-table-column>
        <el-table-column
          prop="examRoomId"
          header-align="center"
          align="center"
          label="考场编排表id">
        </el-table-column>
        <el-table-column
          prop="userId"
          header-align="center"
          align="center"
          label="用户表id">
        </el-table-column>

      </el-table>
      <el-button @click="dialog2 = false">关闭</el-button>
    </el-dialog>

  </div>
</template>

<script>

import AddOrUpdate from './texamroom-add-or-update'
import { formatDate } from '@/utils/date'

export default {
  data () {
    return {
      dataForm: {
        key: ''
      },
      dialog: false,
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
      users: [],
      userIds: [],
      roomId: 0,

      users2: [],
      dialog2: false
    }
  },
  components: {
    AddOrUpdate
  },
  created () {
    this.getDataList()
  },
  methods: {
    listUser (roomId) {
      this.$axios.get(`/api/examsystem/texamroom/getRoomUserList/${roomId}`).then(res => {
        console.log(res.data.data)
        this.users2 = res.data.data
        this.dialog2 = true
      })
    },

    saveAdd () {
      console.log('yes')
      this.dialog = false
      let data = {
        roomId: this.roomId,
        userIds: this.userIds
      }
      this.$axios.post(`/api/examsystem/texamroom/addUserToRoom`, data).then(res => {
        console.log(res)
        this.$message.success('保存成功')
      })
    },

    addUserToRoom (row) {
      this.userIds.push(row.id)
      this.$message.success('添加' + row.name + '成功')
    },

    addUser (roomId) {
      this.roomId = roomId
      this.dialog = true
      let data = {
        currentPage: 1,
        pageSize: 20
      }
      this.$axios.post('/boss/user/getUserPages', data).then(res => {
        console.log(res.data.data.records)
        this.users = res.data.data.records
      })
    },

    // 获取数据列表
    getDataList () {
      this.dataListLoading = true
      this.$axios({
        url: '/api/examsystem/texamroom/list',
        method: 'get',
        params: {
          'page': this.pageIndex,
          'limit': this.pageSize,
          'key': this.dataForm.key
        }
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataList = data.page.list
          this.totalPage = data.page.totalCount
        } else {
          this.dataList = []
          this.totalPage = 0
        }
        this.dataListLoading = false
      })

      // this.$http({
      //   url: this.$http.adornUrl('/examsystem/texamroom/list'),
      //   method: 'get',
      //   params: this.$http.adornParams({
      //     'page': this.pageIndex,
      //     'limit': this.pageSize,
      //     'key': this.dataForm.key
      //   })
      // }).then(({ data }) => {
      //   if (data && data.code === 0) {
      //     this.dataList = data.page.list
      //     this.totalPage = data.page.totalCount
      //   } else {
      //     this.dataList = []
      //     this.totalPage = 0
      //   }
      //   this.dataListLoading = false
      // })
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
          url: '/api/examsystem/texamroom/delete',
          method: 'post',
          data: { ids }
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

        // this.$http({
        //   url: this.$http.adornUrl('/examsystem/texamroom/delete'),
        //   method: 'post',
        //   data: this.$http.adornData(ids, false)
        // }).then(({ data }) => {
        //   if (data && data.code === 0) {
        //     this.$message({
        //       message: '操作成功',
        //       type: 'success',
        //       duration: 1500,
        //       onClose: () => {
        //         this.getDataList()
        //       }
        //     })
        //   } else {
        //     this.$message.error(data.msg)
        //   }
        // })
      })
    },
    // by hhx
    dateFormatter (status) {
      // console.log(status.createTime)
      return formatDate(new Date(status.createTime), 'yyyy-MM-dd')
    }
  }
}
</script>
