<template>
  <div class="app-container">

    <el-form :model="queryParam" ref="queryForm" :inline="true">

      <el-form-item>
        <el-tag size="medium" effect="plain">班级： {{ className }}</el-tag>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="addStudentShowDialog">老师入班</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="createQrCode">班级邀请二维码</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="listLoading" :data="tableData"
              border fit highlight-current-row
              style="width: 100%">
      <el-table-column prop="id" label="Id" align="center"/>
      <el-table-column prop="name" label="用户名"/>
      <el-table-column prop="realName" label="真实姓名"/>
      <el-table-column prop="phone" label="手机号"/>
      <el-table-column label="操作" align="center" width="200" fixed="right">
        <template slot-scope="{ row }" class="operatorBox">
          <el-button class="link-left" size="mini" @click="deleteStudent(row)">移出班级</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParam.pageIndex"
      :limit.sync="queryParam.pageSize"
      @pagination="search"/>

    <el-dialog :visible.sync="addStudent.showDialog" width="70%">
      <el-form :model="addStudent.queryParam" ref="queryForm" :inline="true">
        <el-form-item label="ID：">
          <el-input v-model="addStudent.queryParam.id" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="queryForm">查询</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="addStudent.listLoading" :data="addStudent.tableData"
                @selection-change="handleSelectionChange" border fit highlight-current-row style="width: 100%">
        <el-table-column type="selection" width="35"></el-table-column>
        <el-table-column prop="id" label="Id" width="120px"/>
        <el-table-column prop="name" label="用户名"/>
        <el-table-column prop="realName" label="真实姓名"/>
        <el-table-column prop="phone" label="手机号"/>
      </el-table>
      <pagination v-show="addStudent.total>0"
                  :total="addStudent.total"
                  :page.sync="addStudent.queryParam.pageIndex"
                  :limit.sync="addStudent.queryParam.pageSize"
                  @pagination="searchNotIn"/>
      <span slot="footer" class="dialog-footer">
          <el-button @click="addStudent.showDialog = false">取 消</el-button>
          <el-button type="primary" @click="confirmStudentSelect">确定</el-button>
     </span>
    </el-dialog>

    <el-dialog :visible.sync="qrcodeVisable" width="25%" center>
      <div style="margin-left: 25%">
        <el-image :src="qrcodeWebUrl" style="width: 200px;height: 200px"></el-image>
      </div>
      <span slot="footer" class="dialog-footer">
          <el-button @click="qrcodeVisable = false">取 消</el-button>
          <el-button type="primary" @click="saveQrCode">保存二维码</el-button>
     </span>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import classApi from '@/api/class'

export default {
  components: { Pagination },
  data () {
    return {
      qrcodeVisable: false,
      qrcodeWebUrl: '',
      queryParam: {
        classId: null,
        pageIndex: 1,
        pageSize: 10
      },
      listLoading: true,
      tableData: [],
      total: 0,
      // 弹窗
      addStudent: {
        showDialog: false,
        queryParam: {
          pageIndex: 1,
          pageSize: 5
        },
        listLoading: true,
        tableData: [],
        total: 0
      }
    }
  },
  created () {
    this.className = this.$route.query.className
    this.queryParam.classId = this.$route.query.id
    this.search()
  },
  methods: {
    saveQrCode () {
      this.downloadIamge(this.qrcodeWebUrl, '班级邀请二维码')
    },

    downloadIamge (imgsrc, name) {//下载图片地址和图片名
      var image = new Image()
      // 解决跨域 Canvas 污染问题
      image.setAttribute('crossOrigin', 'anonymous')
      image.onload = function () {
        var canvas = document.createElement('canvas')
        canvas.width = image.width
        canvas.height = image.height
        var context = canvas.getContext('2d')
        context.drawImage(image, 0, 0, image.width, image.height)
        var url = canvas.toDataURL('image/png') //得到图片的base64编码数据

        var a = document.createElement('a') // 生成一个a元素
        var event = new MouseEvent('click') // 创建一个单击事件
        a.download = name || 'photo' // 设置图片名称
        a.href = url // 将生成的URL设置为a.href属性
        a.dispatchEvent(event) // 触发a的单击事件
      }
      image.src = imgsrc
    },

    createQrCode () {
      this.qrcodeVisable = true
      this.$axios.post(`/api/thirdparty/class/qrcode`, { 'classId': this.queryParam.classId }).then(res => {
        console.log(res)
        this.qrcodeWebUrl = res.data.response.webUrl
      })
    },

    submitForm () {
      this.queryParam.pageIndex = 1
      this.search()
    },
    search () {
      this.listLoading = true
      // 请求表格数据
      classApi.teacherList(this.queryParam).then((res) => {
        const result = res.response
        this.tableData = result.records
        this.total = result.total
        this.queryParam.pageIndex = result.current
        this.listLoading = false
      })
    },
    deleteStudent (row) {
      let _this = this
      classApi.removeOutClass({ classId: _this.queryParam.classId, userId: row.id }).then((res) => {
        if (res.code === 1) {
          _this.search()
          _this.$message.success(res.message)
        } else {
          _this.$message.error(res.message)
        }
      })
    },
    // 已下添加学生
    addStudentShowDialog () {
      // this.currentTitleItem = titleItem
      this.addStudent.showDialog = true
      this.searchNotIn()
    },
    searchNotIn () {
      this.addStudent.listLoading = true
      classApi.teacherList(this.addStudent.queryParam).then(res => {
        const result = res.response
        this.addStudent.tableData = result.records
        this.addStudent.total = result.total
        this.addStudent.queryParam.pageIndex = result.current
        this.addStudent.listLoading = false
      })
    },
    queryForm () {
      this.addStudent.queryParam.pageIndex = 1
      this.searchNotIn()
    },
    handleSelectionChange (val) {
      this.addStudent.multipleSelection = val
    },
    confirmStudentSelect () {
      let _this = this
      this.addStudent.multipleSelection.forEach((q,index) => {
        classApi.addToClass({ classId: _this.queryParam.classId, userId: q.id }).then(res => {

          // 最后一个
          if(index == this.addStudent.multipleSelection.length - 1) {
            this.search()
          }

        })
      })
      this.addStudent.showDialog = false
    }
  },
  computed: {}
}
</script>

<style lang="scss" scoped>
.operatorBox {
}
</style>
