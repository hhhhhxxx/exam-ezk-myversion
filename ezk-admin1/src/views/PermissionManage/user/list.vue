<template>
  <div class="app-container">

    <el-form :model="queryParam" ref="queryForm" :inline="true">
      <el-form-item label="用户名：">
        <el-input v-model="queryParam.userName"></el-input>
      </el-form-item>

      <el-form-item label="角色：">
        <el-select v-model="queryParam.roleId"  @change="changeSelect" clearable placeholder="请选择">
          <el-option
            v-for="item in allRoleList"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submitForm">查询</el-button>
        <router-link :to="{path:'/permission/user/edit'}" class="link-left">
          <el-button type="primary">添加</el-button>
        </router-link>
      </el-form-item>
    </el-form>


    <el-table v-loading="listLoading" :data="tableData" border fit highlight-current-row style="width: 100%">
      <el-table-column label="头像" min-width="50">
        <template slot-scope="scope">
          <img class="avatar"
               :src="scope.row.portrait ||
            '//www.lgstatic.com/thumbnail_100x100/i/image2/M01/5E/65/CgotOVszSAOANi0LAAAse2IVWAE693.jpg'
              "
               :alt="scope.row.name"/>
        </template>
      </el-table-column>
      <el-table-column prop="id" label="Id"/>
      <el-table-column prop="name" label="用户名"/>
      <el-table-column prop="realName" label="真实姓名"/>
      <el-table-column prop="sex" label="性别" width="60px;" :formatter="sexFormatter"/>
      <el-table-column prop="phone" label="手机号"/>
      <el-table-column prop="createTime" label="创建时间" width="160px" :formatter="dateFormatter"/>
      <el-table-column label="状态" prop="status" width="70px">
        <template slot-scope="{row}">
          <el-tag :type="statusTagFormatter(row.status)">
            {{ statusFormatter(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column width="220px" label="操作" align="center">
        <template slot-scope="{row}">
          <el-button size="mini" @click="changeStatus(row)" class="link-left">
            {{ statusBtnFormatter(row.status) }}
          </el-button>
          <router-link :to="{path:'/permission/user/edit', query:{id:row.id}}" class="link-left">
            <el-button size="mini">编辑</el-button>
          </router-link>
          <el-button size="mini" type="danger" @click="deleteUser(row)" class="link-left">删除</el-button>
        </template>
      </el-table-column>

      <el-table-column label="权限" align="center" min-width="120">
        <template slot-scope="scope">
          <el-button size="mini" type="success" @click="handleSelectRole(scope.$index, scope.row)">分配角色
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0"
                :total="total"
                :page.sync="queryParam.pageIndex"
                :limit.sync="queryParam.pageSize"
                @pagination="search"/>

    <el-dialog
      title="分配角色"
      :visible.sync="allocDialogVisible"
      width="30%">
      <el-select
        v-model="allocRoleIds"
        multiple
        placeholder="请选择"
        size="small"
        style="width: 80%">
        <el-option
          v-for="item in allRoleList"
          :key="item.id"
          :label="item.name"
          :value="item.id">
        </el-option>
      </el-select>
      <span slot="footer" class="dialog-footer">
          <el-button @click="allocDialogVisible = false" size="small">取 消</el-button>
          <el-button
            type="primary"
            @click="handleAllocDialogConfirm()"
            size="small">确 定</el-button>
        </span>
    </el-dialog>
  </div>
</template>

<script>
// import { getRoleByAdmin, getRolesById, allocRole } from '@/services/login'
import moment from 'moment'
import { sessionstorageUtil } from '@/utils/storage'
import bossApi from '@/api/boss'

import { mapGetters, mapState } from 'vuex'
import Pagination from '@/components/Pagination'
import userApi from '@/api/user'
import { formatDate } from '@/utils/date'

export default {
  name: 'Users',
  title: '用户管理',
  components: {
    Pagination
  },
  data () {

    const filter = {
      id: '',
      username: '',
      created: '',
      last_login: '',
    }

    return {
      queryParam: {
        userName: '',
        roleId: null,
        pageIndex: 1,
        pageSize: 10
      },
      listLoading: true,
      tableData: [],
      total: 0,

      allocAdminId: '',
      allocDialogVisible: false,
      allocRoleIds: [],
      allRoleList: []
    }
  },
  created () {
    // initial data
    this.queryParam.roleId = sessionstorageUtil.get('roleId') ?? null
    // 角色下拉选择
    this.setSelector()

    this.search()


  },
  methods: {
    changeSelect() {
      if(this.queryParam.roleId != null) {
        sessionstorageUtil.set('roleId',this.queryParam.roleId)
      }
      this.search()
    },

    search () {
      this.listLoading = true
      userApi.getUserPageList(this.queryParam).then(res => {
        const result = res.response
        this.tableData = result.records
        this.total = result.total
        this.queryParam.pageIndex = result.current
        this.listLoading = false
      })
    },

    setSelector() {
      bossApi.getRoleAll().then((res) => {
        this.allRoleList = res.response.map((item) => {
          return { id: item.id, name: item.name }
        })

        this.allRoleList.push({
          id: 0,
          name: '无角色用户'
        })

      }).catch((res) => {
        this.$message.error(res.message)
      })
    },

    changeStatus (row) {
      let _this = this
      userApi.changeStatus(row.id).then(re => {
        if (re.code === 1) {
          row.status = re.response
          _this.$message.success(re.mesg)
        } else {
          _this.$message.error(re.mesg)
        }
      })
    },
    deleteUser (row) {
      let _this = this
      userApi.deleteUser(row.id).then(re => {
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
    },
    sexFormatter (row, column, cellValue, index) {
      return this.enumFormat(this.sexEnum, cellValue)
    },
    statusFormatter (status) {
      return this.enumFormat(this.statusEnum, status)
    },
    statusTagFormatter (status) {
      return this.enumFormat(this.statusTag, status)
    },
    statusBtnFormatter (status) {
      return this.enumFormat(this.statusBtn, status)
    },
    // by hhx
    dateFormatter (status) {
      // console.log(status.createTime)
      return formatDate(new Date(status.createTime), 'yyyy-MM-dd')
    },


    // dateFormatter (row, column, value, index) {
    //   return moment(value).format('YYYY-MM-DD HH:mm:ss')
    // },

    handleSelectRole (index, row) {
      this.allocAdminId = row.id
      this.allocDialogVisible = true
      this.getRoleListByAdmin(row.id)
    },
    getRoleListByAdmin (id) {
      bossApi.getRoleAll().then((res) => {
        this.allRoleList = res.response.map((item) => {
          return { id: item.id, name: item.name }
        })
      }).catch((res) => {
        console.log('出错:', res)
      })

      bossApi.getRolesById(id).then((res) => {
        const allocRoleList = res.response
        this.allocRoleIds = []
        if (allocRoleList != null && allocRoleList.length > 0) {
          for (let i = 0; i < allocRoleList.length; i++) {
            this.allocRoleIds.push(allocRoleList[i].id)
          }
        }
      }).catch((res) => {
        console.log('出错:', res)
      })
    },
    handleAllocDialogConfirm () {
      bossApi.allocRole({
        userId: this.allocAdminId,
        roleIdList: this.allocRoleIds,
      }).then((response) => {
        this.$message({
          message: '分配成功！',
          type: 'success',
        })
        this.allocDialogVisible = false
      }).catch()
    },
  },
  computed: {
    ...mapGetters('enumItem', [
      'enumFormat'
    ]),
    ...mapState('enumItem', {
      sexEnum: state => state.user.sexEnum,
      statusEnum: state => state.user.statusEnum,
      statusTag: state => state.user.statusTag,
      statusBtn: state => state.user.statusBtn
    })
  }
}
</script>

<style lang="scss">
// 头像
.avatar {
  margin-right: 10px;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  box-sizing: border-box;
  vertical-align: middle;
}
</style>
