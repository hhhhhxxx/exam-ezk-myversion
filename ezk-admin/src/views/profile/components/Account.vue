<!--<template>-->
<!--  <el-form>-->
<!--    <el-form :model="form" ref="form" label-width="100px" v-loading="formLoading" :rules="rules">-->
<!--      <el-form-item label="用户名：" prop="name" required>-->
<!--        <el-input v-model="form.name"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="真实姓名：" prop="realName" required>-->
<!--        <el-input v-model="form.realName"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="年龄：">-->
<!--        <el-input v-model="form.age"></el-input>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="性别：">-->
<!--        <el-select v-model="form.sex" placeholder="性别" clearable>-->
<!--          <el-option v-for="item in sexEnum" :key="item.key" :value="item.key"-->
<!--                     :label="item.value"></el-option>-->
<!--        </el-select>-->
<!--      </el-form-item>-->
<!--      <el-form-item label="出生日期：">-->
<!--        <el-date-picker v-model="form.birthDay" value-format="yyyy-MM-dd" type="date" placeholder="选择日期"/>-->
<!--      </el-form-item>-->
<!--    <el-form-item>-->
<!--      <el-button type="primary" @click="submit">更新</el-button>-->
<!--    </el-form-item>-->
<!--  </el-form>-->
<!--</template>-->


<!--<script>-->
<!--import { mapGetters, mapState } from 'vuex'-->
<!--import userApi from '@/api/user'-->
<!--export default {-->
<!--  props: {-->
<!--    form: {-->
<!--      type: Object,-->
<!--      default: () => {-->
<!--        return {-->
<!--          name: '',-->
<!--          realName: '',-->
<!--          age: '',-->
<!--          sex: '',-->
<!--          birthDay: null,-->
<!--          phone: null,-->
<!--          userLevel: null,-->
<!--          createTime: null,-->
<!--          portrait: null-->
<!--        }-->
<!--      }-->
<!--    }-->
<!--  },-->

<!--  computed: {-->
<!--    ...mapGetters('enumItem', [-->
<!--      'enumFormat'-->
<!--    ]),-->
<!--    ...mapState('enumItem', {-->
<!--      sexEnum: state => state.user.sexEnum-->
<!--    })-->
<!--  },-->

<!--  methods: {-->
<!--    submit () {-->
<!--      let _this = this-->
<!--      userApi.updateUser(this.form).then(re => {-->
<!--        if (re.code === 1) {-->
<!--          _this.$message.success(re.message)-->
<!--        } else {-->
<!--          _this.$message.error(re.message)-->
<!--        }-->
<!--      })-->
<!--    }-->
<!--  }-->
<!--}-->
<!--</script>-->
