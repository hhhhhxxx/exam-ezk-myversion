<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="考场批次" prop="roomBatch">
      <el-input v-model="dataForm.roomBatch" placeholder="考场批次"></el-input>
    </el-form-item>
    <el-form-item label="考场场次" prop="roomRound">
      <el-input v-model="dataForm.roomRound" placeholder="考场场次"></el-input>
    </el-form-item>
    <el-form-item label="创建时间" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder="创建时间"></el-input>
    </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  data () {
    return {
      visible: false,
      dataForm: {
        id: 0,
        roomBatch: '',
        roomRound: '',
        createTime: ''
      },
      dataRule: {
        roomBatch: [
          { required: true, message: '考场批次不能为空', trigger: 'blur' }
        ],
        roomRound: [
          { required: true, message: '考场场次不能为空', trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: '创建时间不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    init (id) {
      this.dataForm.id = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.id) {
          this.$axios({
            url: `/api/examsystem/texamroom/info/${this.dataForm.id}`,
            method: 'get'
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.dataForm.roomBatch = data.tExamRoom.roomBatch
              this.dataForm.roomRound = data.tExamRoom.roomRound
              this.dataForm.createTime = data.tExamRoom.createTime
            }
          })

          // this.$http({
          //   url: this.$http.adornUrl(`/examsystem/texamroom/info/${this.dataForm.id}`),
          //   method: 'get',
          //   params: this.$http.adornParams()
          // }).then(({ data }) => {
          //   if (data && data.code === 0) {
          //     this.dataForm.roomBatch = data.tExamRoom.roomBatch
          //     this.dataForm.roomRound = data.tExamRoom.roomRound
          //     this.dataForm.createTime = data.tExamRoom.createTime
          //   }
          // })
        }
      })
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$axios({
            url: `/api/examsystem/texamroom/${!this.dataForm.id ? 'save' : 'update'}`,
            method: 'post',
            data: {
              'id': this.dataForm.id || undefined,
              'roomBatch': this.dataForm.roomBatch,
              'roomRound': this.dataForm.roomRound,
              'createTime': this.dataForm.createTime
            }
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.visible = false
                  this.$emit('refreshDataList')
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })

          // this.$http({
          //   url: this.$http.adornUrl(`/examsystem/texamroom/${!this.dataForm.id ? 'save' : 'update'}`),
          //   method: 'post',
          //   data: this.$http.adornData({
          //     'id': this.dataForm.id || undefined,
          //     'roomBatch': this.dataForm.roomBatch,
          //     'roomRound': this.dataForm.roomRound,
          //     'createTime': this.dataForm.createTime
          //   })
          // }).then(({ data }) => {
          //   if (data && data.code === 0) {
          //     this.$message({
          //       message: '操作成功',
          //       type: 'success',
          //       duration: 1500,
          //       onClose: () => {
          //         this.visible = false
          //         this.$emit('refreshDataList')
          //       }
          //     })
          //   } else {
          //     this.$message.error(data.msg)
          //   }
          // })
        }
      })
    }
  }
}
</script>
