<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="80px">
      <!--<el-form-item label="定时发放考试通知" prop="noticeTime">-->
      <!--  <el-input v-model="dataForm.noticeTime" placeholder="定时发放考试通知"></el-input>-->
      <!--</el-form-item>-->
      <el-form-item label="定时发放考试通知">
        <el-date-picker v-model="dataForm.noticeTime" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" type="date"
                        placeholder="选择日期"/>
      </el-form-item>

      <el-form-item label="考试类型" prop="examType">
        <el-input v-model="dataForm.examType" placeholder="考试类型"></el-input>
      </el-form-item>
      <el-form-item label="考试是否发放[0未发放,1发放]" prop="isPublish">
        <el-input v-model="dataForm.isPublish" placeholder="考试是否发放[0未发放,1发放]"></el-input>
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
        noticeTime: '',
        examType: '',
        isPublish: ''
      },
      dataRule: {
        noticeTime: [
          { required: true, message: '定时发放考试通知不能为空', trigger: 'blur' }
        ],
        examType: [
          { required: true, message: '考试类型不能为空', trigger: 'blur' }
        ],
        isPublish: [
          { required: true, message: '考试是否发放[0未发放,1发放]不能为空', trigger: 'blur' }
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
            url: `/api/examsystem/texamplan/info/${this.dataForm.id}`,
            method: 'get'
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.dataForm.noticeTime = data.tExamPlan.noticeTime
              this.dataForm.examType = data.tExamPlan.examType
              this.dataForm.isPublish = data.tExamPlan.isPublish
            }
          })

          // this.$http({
          //   url: this.$http.adornUrl(`/examsystem/texamplan/info/${this.dataForm.id}`),
          //   method: 'get',
          //   params: this.$http.adornParams()
          // }).then(({ data }) => {
          //   if (data && data.code === 0) {
          //     this.dataForm.noticeTime = data.tExamPlan.noticeTime
          //     this.dataForm.examType = data.tExamPlan.examType
          //     this.dataForm.isPublish = data.tExamPlan.isPublish
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
            url: `/api/examsystem/texamplan/${!this.dataForm.id ? 'save' : 'update'}`,
            method: 'post',
            data: {
              'id': this.dataForm.id || undefined,
              'noticeTime': this.dataForm.noticeTime,
              'examType': this.dataForm.examType,
              'isPublish': this.dataForm.isPublish
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
          //   url: this.$http.adornUrl(`/examsystem/texamplan/${!this.dataForm.id ? 'save' : 'update'}`),
          //   method: 'post',
          //   data: this.$http.adornData({
          //     'id': this.dataForm.id || undefined,
          //     'noticeTime': this.dataForm.noticeTime,
          //     'examType': this.dataForm.examType,
          //     'isPublish': this.dataForm.isPublish
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
