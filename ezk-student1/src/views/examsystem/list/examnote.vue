<!--考试前说明-->
<template>
  <div class="myTest">
    <div class="myTest_box">
      <div class="myTest_name">
        <h2>我的考试</h2>
      </div>
      <div class="myTest_line"></div>
      <div class="myTest_text">
        <h2 class="title">考试说明：</h2>
        <p>1、离开或退出考试界面答题计时不停止，请不要中途离开考试界面。</p>
        <p>2、保持座位前的桌面干净，不要有与考试无关的内容。</p>
        <p>3、考试时间截止或答题时间结束，如果处于答题页面，将自动提交试卷。</p>
      </div>
      <div class="myTest_form">
        <div class="myTest_check">
          <el-checkbox v-model="checked">我已阅读并同意</el-checkbox>
        </div>
        <el-button type="primary" round @click="$router.push({path:`/do?id=${doId}`})">开始考试</el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      checked: true,
      doId: ''
    }
  },
  created () {
    this.doId = this.$route.params.doId
  }
}
</script>

<style lang="scss" scoped>
.myTest {
  width: 964px;
  min-height: 200px;
  margin: 40px auto 0;
  background: #ffabab29;
  border: solid #e5e5e5 3px;
  border-radius: 3px;
}

.myTest_box {
  padding: 40px;
}

.myTest_text {
  line-height: 34px;
  font-size: 15px;
  color: #000000;
}

.myTest_form {
  margin-top: 40px;
}

.myTest_check {
  height: 20px;
  line-height: 20px;
  font-size: 14px;
  text-align: center;
}
</style>
