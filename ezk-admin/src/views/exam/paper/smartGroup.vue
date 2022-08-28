<template>
  <div class="app-container">
    <el-form :model="form" ref="form" label-width="100px" v-loading="formLoading" :rules="rules">
      <el-form-item label="考试标题：" prop="name" required>
        <el-col :span="8">
          <el-input v-model="form.name"></el-input>
        </el-col>

      </el-form-item>
      <el-form-item label="题目数量：" prop="titleCount" required>
        <el-col :span="8">
          <el-input v-model.number="form.titleCount"></el-input>
        </el-col>
      </el-form-item>
      <el-form-item label="建议时长：" prop="suggestTime" required>
        <el-col :span="8">
          <el-input v-model.number="form.suggestTime" placeholder="分钟"/>
        </el-col>
      </el-form-item>

      <el-form-item label="难度:">
        <el-select clearable v-model="form.difficult">
          <el-option v-for="(item, index) in options" :key="index" :value="item"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="课程：">
        <el-select v-model="form.subjectId" placeholder="未选择" clearable>
          <el-option v-for="item in subjects" :key="item.id" :value="item.id" :label="subjectEnumFormat(item.id)">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm">提交</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>

import examPaperApi from '@/api/examPaper'
import { mapActions, mapGetters, mapState } from 'vuex'

export default {
  data () {
    return {
      options: [1, 2, 3,4,5],
      form: {
        id: '',
        level: '',
        subjectId: '',
        paperType: 1,
        name: '',
        suggestTime: '',
        limitDateTime: null,
        difficult: 0,
        score: '',
        titleCount: ''
      },
      formLoading: false,
      rules: {
        name: [
          { required: true, message: '请输入考试标题', trigger: 'blur' }
        ],
        titleCount: [
          { required: true, message: '请输入题目数量', trigger: 'blur' },
          { type: 'number', message: '输入数字' }
        ],
        suggestTime: [
          { required: true, message: '请输入考试难度', trigger: 'blur' },
          { type: 'number', message: '输入数字' }
        ]
      }
    }
  },
  created () {
    // 获取课程
    // 获取学科
    this.initSubject()
  },
  methods: {
    submitForm () {
      this.$refs.form.validate((valid) => {
        if (valid) {
          examPaperApi.smartGroup(this.form).then((res) => {
            this.$router.push('/exam/paper/list')
          })
        } else {
          return false
        }
      })
    },
    resetForm () {
      // let lastId = this.form.id
      this.$refs['form'].resetFields()
      this.form = {
        id: '',
        level: '',
        subjectId: '',
        paperType: 1,
        name: '',
        suggestTime: '',
        limitDateTime: '',
        difficult: '',
        score: '',
        titleCount: ''
      }
      // this.form.id = lastId
    },
    ...mapActions('exam', { initSubject: 'initSubject' })
  },
  computed: {
    ...mapGetters('exam', ['subjectEnumFormat']),
    ...mapGetters('enumItem', ['enumFormat']),
    ...mapState('exam', { subjects: (state) => state.subjects }),
    ...mapState('enumItem', {
      levelEnum: (state) => state.user.levelEnum
    })
  }
}
</script>
