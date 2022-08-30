<template>
  <div class="container">
    <el-header class="my-header">
      <el-row>
        <el-col :span="4">
          <div class="my-title">e智考在线考试系统</div>
        </el-col>
        <el-col :span="16" class="my-nav">
          <el-menu
            :default-active="activeIndex"
            class="el-menu-demo"
            mode="horizontal"
            @select="handleSelect"
          >
            <el-menu-item index="1">首页</el-menu-item>
            <el-menu-item index="2">AI面试</el-menu-item>
            <el-menu-item index="3">视频面试</el-menu-item>
            <el-menu-item index="4">在线笔试</el-menu-item>
            <el-menu-item index="6">行业资讯</el-menu-item>
            <el-menu-item index="7">关于我们</el-menu-item>
            <el-menu-item index="8">加入我们</el-menu-item>
          </el-menu>
          <div class="line"></div>
        </el-col>
        <el-col :span="4" style="line-height: 60px">
          <!-- <el-button>登录</el-button> -->
          <router-link :to="{ path: '/login' }">
            <el-button style="margin-right: 10px">登录</el-button>
          </router-link>
          <router-link :to="{ path: '/login' }">
            <el-button type="primary">免费试用</el-button>
          </router-link>
        </el-col>
      </el-row>
    </el-header>
    <div class="my-first">
      <div class="my-bg">
        <h2>多年行业经验，专注在线考试系统解决方案</h2>
        <el-button style="border-radius: 20px; margin-top: 20px"
          >免费注册</el-button
        >
      </div>
    </div>
    <div class="my-content">
      <div class="title">
        <span>功能特色</span>
      </div>
      <div class="my-list">
        <div class="first-li">
          <div class="list-content">
            <h3>考试管理</h3>
            <ul class="item-list">
              <li>
                <span class="listCourse">题库管理</span>
                | 支持多种试题类型和录题方式，题库无限层级支持标签
              </li>
              <li>
                <span class="listCourse">组卷发布</span>
                | 支持选题、抽题、随机等组卷模式，微信扫码即可答题
              </li>
              <li>
                <span class="listCourse">答题模式</span>
                | 正式考试及练习、闯关模式，支持手写板、拍照作答
              </li>
              <li>
                <span class="listCourse">考试设置</span>
                | 支持账号密码登录、免登录考试，PC、手机双端考试
              </li>
            </ul>
          </div>
        </div>
        <div class="first-li" style="padding: 380px 20px">
          <div class="list-content">
            <h3>智能监考</h3>
            <ul class="item-list">
              <li>
                <span class="listCourse">人脸识别</span>
                | 答题前人脸身份核验，与权威数据库比对验证
              </li>
              <li>
                <span class="listCourse">防作弊</span>
                | 答题设备设置，无操作、切屏强制交卷搭建防作弊体系
              </li>
              <li>
                <span class="listCourse">监考大屏</span>
                | 答题时随机拍照，AI人脸对比监测自动标记辅助监考
              </li>
              <li>
                <span class="listCourse">视频监考 </span>
                | 「PC主摄像头、手机副摄像头、桌面录屏」实时监考
              </li>
            </ul>
          </div>
        </div>
        <div class="first-li" style="padding: 80px 20px">
          <div class="list-content">
            <h3>阅卷分析</h3>
            <ul class="item-list">
              <li>
                <span class="listCourse">考后结果</span>
                | 考后成绩、名次等信息自由控制，查看试卷、解析等
              </li>
              <li>
                <span class="listCourse">考试成绩</span>
                | 支持考后自动出分、人工批改判分，线下成绩导入
              </li>
              <li>
                <span class="listCourse">流水判卷</span>
                | 支持分题分学员判卷及匿名判卷，添加批改评语
              </li>
              <li>
                <span class="listCourse">统计分析</span>
                | 试题、考生多维度数据统计分析，自动生成能力模型
              </li>
            </ul>
          </div>
        </div>
        <div class="first-li" style="padding: 450px 20px 0px">
          <div class="list-content">
            <h3>培考闭环</h3>
            <ul class="item-list">
              <li>
                <span class="listCourse">培训功能</span>
                | 支持线上课程、直播、线下培训、每日学练等培训模块
              </li>
              <li>
                <span class="listCourse">流程功能</span>
                | 问卷调查、报名、支付红包、证书，完善培考流程闭环
              </li>
              <li>
                <span class="listCourse">任务流程</span>
                | 任意组合培训、考试功能模块，灵活搭建学习项目
              </li>
              <li>
                <span class="listCourse">培训顾问</span>
                | 定位「场景+工具+内容+轻运营」，搭建培训考试体系
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
    <!-- <el-backtop
      target=".page-component__scroll .el-scrollbar__wrap"
    ></el-backtop> -->
  </div>
</template>

<script>
export default {
  data() {
    return {
      activeIndex: "1",
      activeIndex2: "1",
    };
  },
  methods: {
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    },
  },
};
</script>

<style scoped lang="scss">
.container {
  // text-align: center;
  .my-header {
    position: fixed;
    height: 50px;
    width: 100%;
    background-color: #fff;
    z-index: 100;
    .my-title {
      font: 24px/65px "微软雅黑";
    }
    .el-menu-demo {
      padding-left: 50px;
      height: 60px;
      .el-menu-item {
        font-size: 16px;
      }
      .el-menu-item.is-active {
        border-bottom: 2px solid #3877fe;
        color: #3877fe;
        margin-bottom: 5px;
        font-weight: bold;
      }
    }
    .el-menu--horizontal {
      border-bottom: 0px;
    }
  }
  .my-first {
    padding-top: 65px;

    .my-bg {
      text-align: center;
      background: url(./image/homeback.jpg) no-repeat center;
      height: 500px;
      width: 100%;
      overflow: hidden;
      h2 {
        color: #fff;
        margin-top: 220px;
        font-size: 24px;
      }
    }
  }
  .my-content {
    .title {
      padding-top: 20px;
      height: 30px;
      text-align: center;
      span {
        font: 30px/30px "微软雅黑";
        border-bottom: 2px solid #3877fe;
        color: #3b426b;
      }
    }
  }
}
.my-content .my-list {
  height: 2300px;
  width: 1200px;
  position: relative;
  background: url(./image/feature-bg.png) center no-repeat;
  margin: 0 auto;
  counter-reset: function-counter;
}
.my-content .my-list .first-li {
  padding: 150px 20px;
  position: relative;
  box-sizing: border-box;
}
.first-li .list-content {
  position: relative;
  box-sizing: border-box;
}
.first-li:nth-child(2n + 1) .list-content {
  float: right;
}
.first-li:nth-child(2) .list-content {
  top: -40px;
}
.my-content .my-list .first-li .list-content::before {
  counter-increment: function-counter;
  content: "0" counter(function-counter);
  display: inline-block;
  font-family: "Arial Black";
  font-size: 160px;
  font-weight: 700;
  color: #e4eaf4;
  position: absolute;
  top: -100px;
  left: 240px;
  z-index: -1;
}
.first-li:nth-child(2n + 1) .list-content::before {
  right: auto;
  left: -70px;
}

.first-li .list-content h3 {
  font-size: 34px;
  line-height: 50px;
  color: #3877fe;
  margin-bottom: 20px;
}
.first-li .list-content .item-list li {
  font-size: 14px;
  line-height: 24px;
  color: #6d717c;
  margin-bottom: 10px;
  padding-left: 25px;
  position: relative;
  list-style: none;
}
.first-li .list-content .item-list li .listCourse {
  font-size: 14px;
  font-family: SourceHanSansSC-Medium, SourceHanSansSC;
  font-weight: 500px;
  color: #3776ff;
  line-height: 20px;
}
.first-li .list-content .item-list li::before {
  content: "";
  display: block;
  width: 6px;
  height: 6px;
  border: 1px solid #3877fe;
  border-radius: 50%;
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
}
</style>
