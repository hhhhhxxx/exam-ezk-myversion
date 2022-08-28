<template>
  <div class="app-container">
    <el-row>
      <el-col :span="13">
        <el-aside style="width: 500px">
          <div
            style="margin-top: 10px"
            v-if="chatObj.fromName !== '' && chatObj.code === '2'"
          >
            来自{{ chatObj.fromName }}的信息：{{ chatObj.msg }}
          </div>
          <!--  人脸识别  -->
          <div class="content-wrapper">
            <h4 style="margin-top: 30px">考生人脸检测</h4>
            <p style="color: red; margin-top: 5px" v-if="peopleNum > 1">
              检测到学生{{ chatObj.fromName }}人数大于1人！
            </p>
            <p v-if="isLive === false" style="color: red; margin-top: 5px">
              检测到学生{{ chatObj.fromName }}非活体！
            </p>

            <div class="imageDiv">
              <!--       <canvas id="canvas" height="438" width="780"></canvas>-->
              <img
                ref="img"
                height="400"
                width="400"
                style="background-color: #7abaff"
              />
            </div>
          </div>
        </el-aside>
      </el-col>
      <el-col :span="11" class="border-common">
        <el-form v-if="currentName === ''" :inline="true">
          <el-form-item label="监考老师姓名：">
            <el-input v-model="teacherName" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-button @click="confirmName" type="primary">确认</el-button>
          </el-form-item>
        </el-form>
        <p style="color: #4e4a58; font-size: 14px; font-weight: bold">
          当前监考考生：<b>{{ choiceUser }}</b>
        </p>
        <el-form style="margin-top: 10px">
          <el-form-item :label="sendMessage">
            <el-input
              type="textarea"
              :rows="2"
              placeholder="请输入内容"
              v-model="warnMsg"
            >
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-button @click="sendWarnMsg" type="primary">发送</el-button>
          </el-form-item>
        </el-form>
      </el-col>

      <!-- <el-col :span="8" class="border-common">
        <div class="warnMsgClass">
          <el-form>
            <el-form-item :label="sendMessage">
              <el-input
                style="width: 500px"
                type="textarea"
                :rows="2"
                placeholder="请输入内容"
                v-model="warnMsg"
              >
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-button @click="sendWarnMsg">发送</el-button>
            </el-form-item>
          </el-form>
        </div>

        <div style="margin-top: 50px">
          <p style="font-size: 14px; color: grey; font-weight: 500">
            其他操作：
          </p>
          <el-link type="primary" @click="forceEnd">强制收卷</el-link>
        </div>
      </el-col> -->
    </el-row>
  </div>
</template>

<script>
export default {
  name: "examsystem",
  data() {
    return {
      warnMsg: "", // 来自老师的警告信息

      currentName: "",
      teacherName: "", // 教师姓名
      ws: null, // websocket
      chatObj: {
        // ws传输对象
        code: "",
        fromName: "", // 当前用户
        msg: "",
        examInfoId: "", // exam_info_id
      },
      onlineUser: [], // 在线用户列表
      choiceUser: null, // 选择监控某个学生的姓名

      ctx: null,
      peopleNum: 0, // 人脸识别到的人数
      isLive: true, // 识别到是否为活体
      peopleNumChangeCount: 0, // 统计一共出现1人以上情况的次数
      nonLiveChangeCount: 0, // 统计出现非活体现象情况的次数
      sendMessage: "",
    };
  },
  created() {
    this.choiceUser = this.$route.params.studentName;
    this.sendMessage = "发送警告信息给" + this.choiceUser + ":";
  },
  watch: {
    // 监听 检测到人数的变化
    peopleNum: "peopleNumChange",
    isLive: "isLiveChange",
  },
  methods: {
    // 强制收卷
    forceEnd() {
      this.$message.success("已强制收卷");
      let data = {
        code: "5",
        msg: "强制收卷",
        toName: this.choiceUser,
        examInfoId: this.examInfoId,
      };
      let s = JSON.stringify(data);
      this.ws.send(s);
    },

    // 教师发送警告信息
    sendWarnMsg() {
      if (this.warnMsg === "") {
        this.$message.info("请输入警告内容");
        return;
      }
      this.$message.success("已发送给学生");
      let data = {
        code: "4",
        msg: this.warnMsg,
        toName: this.choiceUser,
        examInfoId: this.examInfoId,
      };
      let s = JSON.stringify(data);
      this.ws.send(s);
    },

    // 监听到非活体情况变化
    isLiveChange() {
      this.nonLiveChangeCount++;
      let updateNonLiveData = {
        examInfoId: this.chatObj.examInfoId,
        nonLiveCount: this.nonLiveChangeCount,
      };
      this.$axios.post(
        "/api/examsystem/texaminfo/updateNonLiveCount",
        updateNonLiveData
      );
    },

    // 监听到人数的变化
    peopleNumChange(curVal, oldVal) {
      this.peopleNumChangeCount++;
      let updateOverNumData = {
        examInfoId: this.chatObj.examInfoId,
        peopleNumCount: this.peopleNumChangeCount,
      };
      this.$axios.post(
        "/api/examsystem/texaminfo/updateOverNum",
        updateOverNumData
      );
    },

    //= ===========================人脸检测===============================
    async detectFace(image) {
      let canvas = document.createElement("canvas");
      canvas.width = image.width;
      canvas.height = image.height;
      this.ctx = canvas.getContext("2d");
      this.ctx.drawImage(image, 0, 0, image.width, image.height);

      // 人脸识别数据
      let data = {
        image: canvas.toDataURL("image/png"),
        studentName: this.chatObj.fromName,
      };

      // console.log('data,', data)
      let res = null;

      // 必须是同步的，否则canvas无法画识别框
      await this.$axios
        .post("/api/examsystem/detectFaces", data)
        .then((result) => {
          console.log(result);
          res = result;
        });
      this.peopleNum = res.data.data.length;
      if (res.data.code === 0 && res.data.data.length > 0) {
        res.data.data.forEach((r) => {
          let rect = r.rect;
          let x = rect.left;
          let y = rect.top;
          let w = rect.right - rect.left;
          let h = rect.bottom - rect.top;
          this.ctx.strokeStyle = "#FF0000";
          this.ctx.lineWidth = 5;
          this.ctx.strokeRect(x, y, w, h);
          let gender = "未知";
          if (r.gender === 0) {
            gender = "男";
          } else if (r.gender === 1) {
            gender = "女";
          }

          let liveness = "-";
          if (r.liveness === 1) {
            liveness = "活体";
            this.isLive = true;
          } else if (r.liveness === 0) {
            liveness = "非活体";
            this.isLive = false;
          }

          // let txt = '性别:' + gender + ',年龄:' + r.age + ',' + liveness
          let txt = gender + "," + r.age + "," + liveness;
          this.ctx.fillStyle = "#FF0000";
          this.ctx.font = "12px Georgia";
          this.ctx.fillText(txt, x, y - 10);
          console.info("detectSuccess");
        });
      }

      this.$refs.img.src = canvas.toDataURL("image/png");
    },
    // 将传来的图片，调用detectFace函数
    fileInput() {
      let _this = this;
      let image = new Image();
      image.src = this.chatObj.msg;
      image.onload = function () {
        _this.detectFace(image);
      };
      console.info("file input!");
    },

    //= ==================================================================

    // 确认考试姓名
    confirmName() {
      this.$message.success("已开始监考");
      let user = {
        username: this.teacherName,
      };
      // todo 登录完成后，从session获取用户名
      this.$axios.post("/api/examsystem/loginExamSystem", user).then((res) => {
        this.$axios.get("/api/examsystem/currentUser").then((res) => {
          this.currentName = res.data;
          this.initWebSocket();
        });
      });
    },
    // 初始化websocket服务器
    initWebSocket() {
      this.ws = new WebSocket("ws://localhost:8200/exam");

      // onOpen
      this.ws.onopen = () => {
        console.log("连接已建立");
      };

      // onMessage
      // 获取服务器发来的信息
      this.ws.onmessage = (ev) => {
        var json = JSON.parse(ev.data);
        console.log("ev.data = ", ev.data);
        if (json.code === "1") {
          // code=1 在线用户广播
          this.onlineUser = json.msg;
          console.log("this.onlineUser = ", this.onlineUser);
        } else if (json.code === "2") {
          // code=2 文本信息广播
          this.chatObj = json;
        } else if (json.code === "3") {
          // code=3 抓拍图片
          // if (json.fromName !== this.chatObj.fromName){
          //   var element = document.createElement('img');
          //   element.src = json.msg
          // }
          // 选项框里选中的考生，才展示监控画面
          if (this.choiceUser === json.fromName) {
            this.chatObj = json;
            this.fileInput();
          }
        }
      };

      // onClose
      this.ws.onclose = () => {
        console.log("连接已关闭");
      };
    },
  },
};
</script>

<style scoped>
.warnMsgClass {
  font-size: 13px;
}

.border-common {
  margin-top: 20px;
  /*border-radius: 10px;*/
  /*margin: 50px auto;*/
  /*height: auto;*/
  /*border: 5px solid #ededed;*/
  /*box-shadow: 0 0 40px #ddd;*/
  /*background-color: white;*/
}
.sys-container {
  width: 80%;
  margin-left: 50px;
}
.app-container {
  color: #4e4a58;
}
</style>
