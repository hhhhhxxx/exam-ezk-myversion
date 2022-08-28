<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="120px">
    <el-form-item label="题库名字" prop="name">
      <el-input v-model="dataForm.name" placeholder="题库名字"></el-input>
    </el-form-item>
    <el-form-item label="题库简介" prop="description">
      <el-input v-model="dataForm.description" placeholder="题库简介"></el-input>
    </el-form-item>
    <el-form-item label="题库锁定" prop="isLock">
      <el-input v-model="dataForm.isLock" placeholder="题库锁定,不允许查看[1允许,0不允许]"></el-input>
    </el-form-item>
    <el-form-item label="题库审批情况" prop="isPass">
      <el-input v-model="dataForm.isPass" placeholder="题库审批情况[0不通过,1通过]"></el-input>
    </el-form-item>
    <el-form-item label="题库封面图片" prop="img">
      <el-input v-model="dataForm.img" placeholder="题库封面图片"></el-input>
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
        name: '',
        description: '',
        isLock: '',
        isPass: '',
        img: ''
      },
      dataRule: {
        name: [
          { required: true, message: '题库名字不能为空', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '题库简介不能为空', trigger: 'blur' }
        ],
        isLock: [
          { required: true, message: '题库锁定,不允许查看[1允许,0不允许]不能为空', trigger: 'blur' }
        ],
        isPass: [
          { required: true, message: '题库审批情况[0不通过,1通过]不能为空', trigger: 'blur' }
        ],
        img: [
          { required: true, message: '题库封面图片不能为空', trigger: 'blur' }
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
            url: `/api/examsystem/tquestionbank/info/${this.dataForm.id}`,
            method: 'get'
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.dataForm.name = data.tQuestionBank.name
              this.dataForm.description = data.tQuestionBank.description
              this.dataForm.isLock = data.tQuestionBank.isLock
              this.dataForm.isPass = data.tQuestionBank.isPass
              this.dataForm.img = data.tQuestionBank.img
            }
          })
        }
      })
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$axios({
            url: `/api/examsystem/tquestionbank/${!this.dataForm.id ? 'save' : 'update'}`,
            method: 'post',
            data: {
              'id': this.dataForm.id || undefined,
              'name': this.dataForm.name,
              'description': this.dataForm.description,
              'isLock': this.dataForm.isLock,
              'isPass': this.dataForm.isPass,
              'img': this.dataForm.img
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
        }
      })
    }
  }
}
</script>
