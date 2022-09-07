<template>
  <div class="app-container">
    <div>
      <el-row :gutter="20">

        <el-col :span="6" :xs="24">
          <user-card  :userInfo="userInfo" />
        </el-col>

        <el-col :span="18" :xs="24">
          <el-card>
            <el-tabs active-name="timeline">
              <el-tab-pane label="时间线" name="timeline">
                <timeline :userInfo="userInfo" />
              </el-tab-pane>
              <!--<el-tab-pane label="账号" name="account">-->
              <!--  <account :userInfo="userInfo"  />-->
              <!--</el-tab-pane>-->
              <el-tab-pane label="个人资料修改" name="update">
                <el-form :model="form" ref="form" label-width="100px" v-loading="formLoading" :rules="rules">
                  <el-form-item label="用户名：" prop="name" required>
                    <el-input v-model="form.name"></el-input>
                  </el-form-item>
                  <el-form-item label="真实姓名：" prop="realName" required>
                    <el-input v-model="form.realName"></el-input>
                  </el-form-item>
                  <el-form-item label="年龄：">
                    <el-input v-model="form.age"></el-input>
                  </el-form-item>
                  <el-form-item label="性别：">
                    <el-select v-model="form.sex" placeholder="性别" clearable>
                      <el-option v-for="item in sexEnum" :key="item.key" :value="item.key"
                                 :label="item.value"></el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item label="出生日期：">
                    <el-date-picker v-model="form.birthDay" value-format="yyyy-MM-dd" type="date" placeholder="选择日期"/>
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" @click="submitForm">更新</el-button>
                  </el-form-item>
                </el-form>
              </el-tab-pane>
            </el-tabs>
          </el-card>
        </el-col>

      </el-row>
    </div>
  </div>
</template>

<script>
import UserCard from './components/UserCard'
import Timeline from './components/Timeline'
// import Account from './components/Account'
import userApi from '@/api/user'
import { mapGetters, mapState } from 'vuex'
export default {
  name: 'Profile',
  data () {
    return {
      userInfo: {
        realName: '',
        phone: '',
        lastActiveTime: '',
        createTime: '',
        role: '1',
        imagePath: null
      },
      formLoading: false,
      form: {
        name: '',
        realName: '',
        age: '',
        sex: '',
        birthDay: null,
        phone: null,
        userLevel: null,
        createTime: null,
        portrait: null
      },
      rules: {
        name: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        realName: [
          { required: true, message: '请输入真实姓名', trigger: 'blur' }
        ]
      }
    }
  },
  components: { UserCard, Timeline },

  created () {
    let _this = this
    userApi.getCurrentUser().then(res => {
      _this.form = res.data
    })
  },
  computed: {
    ...mapState('enumItem', {
      sexEnum: state => state.user.sexEnum,
    })
  },

  methods: {
    submitForm () {
      let _this = this
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.formLoading = true
          userApi.updateUser(this.form).then(res => {
            if (res.code === '000000') {
              _this.$message.success(res.mesg)
            } else {
              _this.$message.error(res.mesg)
            }
            _this.formLoading = false
          }).catch(e => {
            _this.formLoading = false
          })
        } else {
          return false
        }
      })
    }
  }
}
</script>
