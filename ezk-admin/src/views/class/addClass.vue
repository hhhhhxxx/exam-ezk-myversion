<template>
  <div class="app-container">
    <el-form :model="form" ref="form" label-width="100px" v-loading="formLoading" :rules="rules">
      <!--<el-form-item label="id：" required>-->
      <!--  <el-col :span="8">-->
      <!--    <el-input v-model="form.id"></el-input>-->
      <!--  </el-col>-->
      <!--</el-form-item>-->
      <el-form-item label="班级名：" prop="name" required>
        <el-col :span="8">
          <el-input v-model="form.name"></el-input>
        </el-col>
      </el-form-item>
      <el-form-item label="人数：" prop="count" required>
        <el-col :span="8">
          <el-input v-model.number="form.count"></el-input>
        </el-col>
      </el-form-item>
      <el-form-item label="描述：" prop="description" required>
        <el-col :span="8">
          <el-input v-model="form.description"></el-input>
        </el-col>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm">提交</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import classApi from '@/api/class'

export default {
  data () {
    return {
      form: {
        createUserId: 1,
        name: '',
        count: '',
        description: ''
      },
      formLoading: false,
      rules: {
        name: [
          { required: true, message: '请输入考试标题', trigger: 'blur' }
        ],
        count: [
          { required: true, message: '请输入人数', trigger: 'blur' },
          { type: 'number', message: '输入数字' }
        ],
        description: [
          { required: true, message: '请输入描述', trigger: 'blur' }
        ]
      }
    }
  },
  created () {
  },
  methods: {
    submitForm () {
      this.$refs.form.validate((valid) => {
        if (valid) {
          classApi.addClass(this.form).then((res) => {
            this.$router.push('/class/list')
          })
        } else {
          return false
        }
      })
    },
    resetForm () {
      this.$refs['form'].resetFields()
      this.form = {
        name: '',
        count: '',
        description: ''
      }
    }
  },
  computed: {
  }
}
</script>
