<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible"
    width="80%">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="300px">
    <el-form-item label="试卷表关联id" prop="examPaperId">
      <el-input v-model="dataForm.examPaperId" placeholder="试卷表关联id"></el-input>
    </el-form-item>
    <el-form-item label="开始时间" prop="examStartTime">
      <el-input v-model="dataForm.examStartTime" placeholder="开始时间"></el-input>
    </el-form-item>
    <el-form-item label="截止时间" prop="examEndTime">
      <el-input v-model="dataForm.examEndTime" placeholder="截止时间"></el-input>
    </el-form-item>
    <el-form-item label="限时提交时间(...分钟内不允许提交)" prop="limitSubmitTime">
      <el-input v-model="dataForm.limitSubmitTime" placeholder="限时提交时间(...分钟内不允许提交)"></el-input>
    </el-form-item>
    <el-form-item label="限时进入时间(....分钟后不允许参加考试)" prop="limitEnterTime">
      <el-input v-model="dataForm.limitEnterTime" placeholder="限时进入时间(....分钟后不允许参加考试)"></el-input>
    </el-form-item>
    <el-form-item label="考试批次" prop="examBatch">
      <el-input v-model="dataForm.examBatch" placeholder="考试批次"></el-input>
    </el-form-item>
    <el-form-item label="考试场次" prop="examRound">
      <el-input v-model="dataForm.examRound" placeholder="考试场次"></el-input>
    </el-form-item>
    <el-form-item label="是否开启抓拍监控(0:关闭1:开启)" prop="enableMonitor">
      <el-input v-model="dataForm.enableMonitor" placeholder="是否开启抓拍监控(0:关闭1:开启)"></el-input>
    </el-form-item>
    <el-form-item label="识别到屏幕异常...次,强制收卷" prop="limitScreenCount">
      <el-input v-model="dataForm.limitScreenCount" placeholder="识别到屏幕异常...次,强制收卷"></el-input>
    </el-form-item>
    <el-form-item label="及格标准" prop="passScore">
      <el-input v-model="dataForm.passScore" placeholder="及格标准"></el-input>
    </el-form-item>
    <el-form-item label="考试截止日前运行重做....次" prop="allowRedo">
      <el-input v-model="dataForm.allowRedo" placeholder="考试截止日前运行重做....次"></el-input>
    </el-form-item>
    <el-form-item label="允许学生查看分数(0:不允许1:允许)" prop="allowCheckScore">
      <el-input v-model="dataForm.allowCheckScore" placeholder="允许学生查看分数(0:不允许1:允许)"></el-input>
    </el-form-item>
    <el-form-item label="允许多终端考试(0:网页考试,1:app考试2:允许网页与app考试)" prop="allowMultidevice">
      <el-input v-model="dataForm.allowMultidevice" placeholder="允许多终端考试(0:网页考试,1:app考试2:允许网页与app考试)"></el-input>
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
        examPaperId: '',
        examStartTime: '',
        examEndTime: '',
        limitSubmitTime: '',
        limitEnterTime: '',
        examBatch: '',
        examRound: '',
        enableMonitor: '',
        limitScreenCount: '',
        passScore: '',
        allowRedo: '',
        allowCheckScore: '',
        allowMultidevice: ''
      },
      dataRule: {
        examPaperId: [
          { required: true, message: '考试试卷表关联id不能为空', trigger: 'blur' }
        ],
        examStartTime: [
          { required: true, message: '考试开始时间不能为空', trigger: 'blur' }
        ],
        examEndTime: [
          { required: true, message: '考试截止时间不能为空', trigger: 'blur' }
        ],
        limitSubmitTime: [
          { required: true, message: '限时提交时间(...分钟内不允许提交)不能为空', trigger: 'blur' }
        ],
        limitEnterTime: [
          { required: true, message: '限时进入时间(....分钟后不允许参加考试)不能为空', trigger: 'blur' }
        ],
        examBatch: [
          { required: true, message: '考试批次不能为空', trigger: 'blur' }
        ],
        examRound: [
          { required: true, message: '考试场次不能为空', trigger: 'blur' }
        ],
        enableMonitor: [
          { required: true, message: '是否开启抓拍监控(0:关闭1:开启)不能为空', trigger: 'blur' }
        ],
        limitScreenCount: [
          { required: true, message: '识别到屏幕异常...次,强制收卷不能为空', trigger: 'blur' }
        ],
        passScore: [
          { required: true, message: '及格标准不能为空', trigger: 'blur' }
        ],
        allowRedo: [
          { required: true, message: '考试截止日前运行重做....次不能为空', trigger: 'blur' }
        ],
        allowCheckScore: [
          { required: true, message: '允许学生查看分数(0:不允许1:允许)不能为空', trigger: 'blur' }
        ],
        allowMultidevice: [
          { required: true, message: '允许多终端考试(0:网页考试,1:app考试2:允许网页与app考试)不能为空', trigger: 'blur' }
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
            url: `/api/examsystem/texamarguments/info/${this.dataForm.id}`,
            method: 'get'
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.dataForm.examPaperId = data.tExamArguments.examPaperId
              this.dataForm.examStartTime = data.tExamArguments.examStartTime
              this.dataForm.examEndTime = data.tExamArguments.examEndTime
              this.dataForm.limitSubmitTime = data.tExamArguments.limitSubmitTime
              this.dataForm.limitEnterTime = data.tExamArguments.limitEnterTime
              this.dataForm.examBatch = data.tExamArguments.examBatch
              this.dataForm.examRound = data.tExamArguments.examRound
              this.dataForm.enableMonitor = data.tExamArguments.enableMonitor
              this.dataForm.limitScreenCount = data.tExamArguments.limitScreenCount
              this.dataForm.passScore = data.tExamArguments.passScore
              this.dataForm.allowRedo = data.tExamArguments.allowRedo
              this.dataForm.allowCheckScore = data.tExamArguments.allowCheckScore
              this.dataForm.allowMultidevice = data.tExamArguments.allowMultidevice
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
            url: `/api/examsystem/texamarguments/${!this.dataForm.id ? 'save' : 'update'}`,
            method: 'post',
            data: {
              'id': this.dataForm.id || undefined,
              'examPaperId': this.dataForm.examPaperId,
              'examStartTime': this.dataForm.examStartTime,
              'examEndTime': this.dataForm.examEndTime,
              'limitSubmitTime': this.dataForm.limitSubmitTime,
              'limitEnterTime': this.dataForm.limitEnterTime,
              'examBatch': this.dataForm.examBatch,
              'examRound': this.dataForm.examRound,
              'enableMonitor': this.dataForm.enableMonitor,
              'limitScreenCount': this.dataForm.limitScreenCount,
              'passScore': this.dataForm.passScore,
              'allowRedo': this.dataForm.allowRedo,
              'allowCheckScore': this.dataForm.allowCheckScore,
              'allowMultidevice': this.dataForm.allowMultidevice
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
