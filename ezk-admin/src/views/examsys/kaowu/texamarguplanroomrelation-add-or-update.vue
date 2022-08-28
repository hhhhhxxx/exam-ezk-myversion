<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="关联考试id" prop="examArgumentsId">
      <el-input v-model="dataForm.examArgumentsId" placeholder="关联考试id"></el-input>
    </el-form-item>
    <el-form-item label="考试计划表id" prop="examPlanId">
      <el-input v-model="dataForm.examPlanId" placeholder="考试计划表id"></el-input>
    </el-form-item>
    <el-form-item label="考场编排表id" prop="examRoomId">
      <el-input v-model="dataForm.examRoomId" placeholder="考场编排表id"></el-input>
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
        examArgumentsId: '',
        examPlanId: '',
        examRoomId: ''
      },
      dataRule: {
        examArgumentsId: [
          { required: true, message: '关联考试id不能为空', trigger: 'blur' }
        ],
        examPlanId: [
          { required: true, message: '考试计划表id不能为空', trigger: 'blur' }
        ],
        examRoomId: [
          { required: true, message: '考场编排表id不能为空', trigger: 'blur' }
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
            url: `/api/examsystem/texamarguplanroomrelation/info/${this.dataForm.id}`,
            method: 'get'
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.dataForm.examArgumentsId = data.tExamArguPlanRoomRelation.examArgumentsId
              this.dataForm.examPlanId = data.tExamArguPlanRoomRelation.examPlanId
              this.dataForm.examRoomId = data.tExamArguPlanRoomRelation.examRoomId
            }
          })
          // this.$http({
          //   url: this.$http.adornUrl(`/examsystem/texamarguplanroomrelation/info/${this.dataForm.id}`),
          //   method: 'get',
          //   params: this.$http.adornParams()
          // }).then(({ data }) => {
          //   if (data && data.code === 0) {
          //     this.dataForm.examArgumentsId = data.tExamArguPlanRoomRelation.examArgumentsId
          //     this.dataForm.examPlanId = data.tExamArguPlanRoomRelation.examPlanId
          //     this.dataForm.examRoomId = data.tExamArguPlanRoomRelation.examRoomId
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
            url: `/api/examsystem/texamarguplanroomrelation/${!this.dataForm.id ? 'save' : 'update'}`,
            method: 'post',
            data:{
              'id': this.dataForm.id || undefined,
              'examArgumentsId': this.dataForm.examArgumentsId,
              'examPlanId': this.dataForm.examPlanId,
              'examRoomId': this.dataForm.examRoomId
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
          //   url: this.$http.adornUrl(`/examsystem/texamarguplanroomrelation/${!this.dataForm.id ? 'save' : 'update'}`),
          //   method: 'post',
          //   data: this.$http.adornData({
          //     'id': this.dataForm.id || undefined,
          //     'examArgumentsId': this.dataForm.examArgumentsId,
          //     'examPlanId': this.dataForm.examPlanId,
          //     'examRoomId': this.dataForm.examRoomId
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
