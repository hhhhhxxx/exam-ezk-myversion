<template>
  <div class="main-container">
    <h3 style="text-align: center">输入考试号码</h3>
    <div style="display: flex">
      <el-input v-model="examid"></el-input>
      <el-button @click="start">进入考试</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'index',
  data () {
    return {
      examid: ''
    }
  },
  created () {

  },
  methods: {
    start () {
      console.log('start')
      this.$router.push({
        path: '/examsystem/startexam/' + this.examid
      })
    }
  }
}
</script>

<style scoped>
.main-container {
  border-radius: 10px;
  justify-content: center;
  margin: 50px auto;
  width: 1400px;
  height: auto;

  border: 5px solid #ededed;
  box-shadow: 0 0 40px #ddd;
  background-color: white;

  padding: 50px;
}


</style>
