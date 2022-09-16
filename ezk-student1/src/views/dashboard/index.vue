<template>
  <div class="main-container">
    <div style="margin-left: 50px;margin-right: 90px">
      <h3 style="color: #24292f">e智考 在线考试系统
        <p style="color: #7d7d7f;font-size: 12px">web端考试解决方案，人脸识别监考，自动化组卷</p>
      </h3>
    </div>
    <div style="margin-left: 50px;margin-right: 90px">
      <el-carousel indicator-position="outside" autoplay height="350px" :interval="5000">
        <el-carousel-item v-for="(item,index) in srcList" :key="index">
          <el-image :src="item" style="width: 100%;height: 350px"></el-image>
        </el-carousel-item>
      </el-carousel>
    </div>

    <div style="margin-left: 50px;margin-right: 90px">
      <h3 style="color: #24292f">· 我的课程</h3>
    </div>
    <div style="display: flex">
      <div class="class-block-style" v-for="(item,index) in subjectList" :key="index">
        <img class="class-block-style-img" :src="item.img" >
        <span class="class-block-style-title">{{item.name}}({{item.levelName}})</span>
        <div class="class-block-style-button">
          <el-button size="mini">退课</el-button>
          <el-button size="mini">详情</el-button>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import { mapState, mapGetters } from 'vuex'
// import indexApi from '@/api/dashboard'
import Cookies from 'js-cookie'

export default {
  data () {
    return {
      addClassDialogVisable: false,
      userId: 0,
      userName: '',
      srcList: ['https://examsystem-gdpu.oss-cn-guangzhou.aliyuncs.com/base/autoplay1.jpg',
        'https://examsystem-gdpu.oss-cn-guangzhou.aliyuncs.com/base/autoplay2.jpg',
        'https://examsystem-gdpu.oss-cn-guangzhou.aliyuncs.com/base/autoplay3.jpg',
        'https://examsystem-gdpu.oss-cn-guangzhou.aliyuncs.com/base/autoplay4.jpg'
      ],
      subjectList: [
        { name: '高等数学', levelName: '大学', img: 'https://examsystem-gdpu.oss-cn-guangzhou.aliyuncs.com/base/%E9%AB%98%E7%AD%89%E6%95%B0%E5%AD%A6.png' },
        { name: '人工智能', levelName: '大学', img: 'https://examsystem-gdpu.oss-cn-guangzhou.aliyuncs.com/base/%E4%BA%BA%E5%B7%A5%E6%99%BA%E8%83%BD.png' },
        { name: '操作系统', levelName: '大学', img: 'https://examsystem-gdpu.oss-cn-guangzhou.aliyuncs.com/base/%E6%93%8D%E4%BD%9C%E7%B3%BB%E7%BB%9F.jpg' },
        { name: '思想道德基础与法律修养', levelName: '大学', img: 'https://examsystem-gdpu.oss-cn-guangzhou.aliyuncs.com/base/%E5%85%B6%E4%BB%96.jpg' }
      ]
    }
  },
  created () {
    this.userName = Cookies.get('studentUserName')
    this.userId = Cookies.get('userId')
  },
  methods: {
    onError () {
      this.$message.error('请上传二维码图片！')
    },
    onSuccessSumbit () {
      this.$message.success('成功加入班级')
      this.addClassDialogVisable = false
    },
    statusTagFormatter (status) {
      return this.enumFormat(this.statusTag, status)
    },
    statusTextFormatter (status) {
      return this.enumFormat(this.statusEnum, status)
    }
  },
  computed: {
    ...mapGetters('enumItem', [
      'enumFormat'
    ]),
    ...mapState('enumItem', {
      statusEnum: state => state.exam.examPaperAnswer.statusEnum,
      statusTag: state => state.exam.examPaperAnswer.statusTag
    })
  }
}
</script>

<style lang="scss" scoped>

.main-container {
  border-radius: 10px;
  justify-content: center;
  margin: 50px auto;
  width: 100%;
  height: auto;

  border: 0 solid #ededed;
  //box-shadow: 0 0 40px #ddd;
  background-color: white;

}

.el-carousel__item h3 {
  color: #475669;
  font-size: 18px;
  opacity: 0.75;
  line-height: 300px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}

.class-block-style{
  align-items: center;
  border-radius: 10px;
  justify-content: center;
  margin: 10px;
  width: 330px;
  height: 250px;

  border: 5px solid #ededed;
  box-shadow: 0 0 40px #ddd;
  background-color: white;
  padding: 10px;
  transition: all .3s ease-in 0s;

  .class-block-style-img{
    width: 300px;
    height: 160px;
  }

  .class-block-style-title{
    font-size: 14px;
    color: #24292f;
    margin: 5px 10px;
  }

  .class-block-style-button{
    margin-top: 10px;
    margin-left: 20px;
    float: right;
  }
}

.class-block-style:hover{
  transform: scale(1.05);
  transition: all 0.3s ease-in;
}

</style>
