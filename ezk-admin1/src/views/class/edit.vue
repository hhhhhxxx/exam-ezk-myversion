<template>
  <div class="app-container">

    <el-form :model="form" ref="form" label-width="100px" v-loading="formLoading" :rules="rules">
      <el-form-item label="班级名："  prop="name" required>
        <el-input v-model="form.name"></el-input>
      </el-form-item>
      <el-form-item label="人数：" prop="count" required>
        <el-input v-model="form.count" type="number"></el-input>
      </el-form-item>
      <el-form-item label="描述：" prop="realName">
        <el-input v-model="form.description"></el-input>
      </el-form-item>

      <el-form-item label="加入此班级：" v-show="addVisiable">
        <el-switch
          v-model="form.add"
          active-text="是"
          inactive-text="否">
        </el-switch>
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
        id: null,
        name: '',
        count: null,
        description: '',
        add: true,
      },
      addVisiable: true,
      formLoading: false,
      rules: {
        name: [
          { required: true, message: '请输入班级名', trigger: 'blur' }
        ],
        count: [
          { required: true, message: '请输入班级人数', trigger: 'blur' }
        ]
      }
    }
  },
  created () {
    let id = this.$route.query.id
    let _this = this
    if (id && parseInt(id) !== 0) {
      _this.addVisiable = false;
      _this.formLoading = true

      classApi.getClassById(id).then((res) => {
        _this.form = res.response
        _this.formLoading = false
      })

    }
  },
  methods: {
    submitForm () {
      let _this = this
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.formLoading = true
          classApi.saveOrUpdateClass(this.form).then((res) => {
            this.$message.success(res.message)
            this.$router.back()
          }).catch((res => {
            this.$message.error(res.message)
          }))
        } else {
          return false
        }
      })
    },
    resetForm () {
      let lastId = this.form.id
      this.$refs['form'].resetFields()
      this.form = {
        id: null,
        name: '',
        count: null,
        description: '',
      }
      this.form.id = lastId
    },
  }
}
</script>

<style lang="scss" scoped>
.el-input {
  width: 75%;
}
</style>
