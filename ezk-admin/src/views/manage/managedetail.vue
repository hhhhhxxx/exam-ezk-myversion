<template>
  <div class="app-container">
    <el-card>
      <div class="list-container">
        <el-select
          v-model="examPaperId"
          style="width: 200px; margin-top: 5px; margin-left: 5px"
          placeholder="可选择考试"
        >
          <el-option
            v-for="item in examPaperList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          >
          </el-option>
        </el-select>

        <el-button
          type="primary"
          class="confirm-button"
          @click="getExamInfoList"
          >确认</el-button
        >
      </div>

      <el-table :data="tableData" border style="width: 100%; margin-top: 10px">
        <el-table-column prop="studentname" label="学生姓名" align="center">
        </el-table-column>
        <el-table-column
          prop="numOverCount"
          label="人数异常次数"
          align="center"
          width="120"
        >
          <template slot-scope="scope">
            <p v-if="scope.row.numOverCount > 10" style="color: red">
              {{ scope.row.numOverCount }}
            </p>
            <p v-else>{{ scope.row.numOverCount }}</p>
          </template>
        </el-table-column>
        <el-table-column
          prop="exitFullCount"
          label="退出全屏次数"
          align="center"
          width="120"
        >
          <template slot-scope="scope">
            <p v-if="scope.row.exitFullCount > 10" style="color: red">
              {{ scope.row.exitFullCount }}
            </p>
            <p v-else>{{ scope.row.exitFullCount }}</p>
          </template>
        </el-table-column>
        <el-table-column
          prop="nonLiveCount"
          label="非活体次数"
          align="center"
          width="100"
        >
          <template slot-scope="scope">
            <p v-if="scope.row.nonLiveCount > 10" style="color: red">
              {{ scope.row.nonLiveCount }}
            </p>
            <p v-else>{{ scope.row.nonLiveCount }}</p>
          </template>
        </el-table-column>
        <el-table-column prop="checkSex" label="检测性别" align="center">
          <template slot-scope="scope">
            <p v-if="scope.row.checkSex === 0">男</p>
            <p v-if="scope.row.checkSex === 1">女</p>
            <p v-if="scope.row.checkSex !== 1 && scope.row.checkSex !== 0">
              未检测
            </p>
          </template>
        </el-table-column>
        <el-table-column
          prop="beforeExamImg"
          label="考前人脸抓拍"
          width="150"
          align="center"
        >
          <template slot-scope="scope">
            <img
              :src="scope.row.beforeExamImg"
              style="width: 130px; height: 100px"
            />
          </template>
        </el-table-column>
        <el-table-column
          prop="duringExamImg"
          label="考试时抓拍"
          width="150"
          align="center"
        >
          <template slot-scope="scope">
            <img
              :src="scope.row.duringExamImg"
              style="width: 130px; height: 100px"
            />
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="创建时间"
          align="center"
          width="100"
        >
        </el-table-column>
        <el-table-column label="操作" align="center" width="70">
          <template slot-scope="{ row }">
            <router-link
              :to="{ path: '/manage/invigilate/', query: { id: row.id } }"
            >
              <el-button size="mini" type="success">监考</el-button>
            </router-link>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "list",
  data() {
    return {
      examPaperId: null, // exam_paper_Id
      tableData: [], // 监考记录
      examPaperList: [], // 试卷列表
    };
  },
  created() {
    this.examPaperId = this.$route.params.id;
    console.log(this.examPaperId)
    this.getExamPaperList();
    if (this.examPaperId !== null) {
      this.getExamInfoList();
    }
  },
  methods: {
    // 获取考试监考记录列表
    getExamInfoList() {
      let data = {
        examPaperId: this.examPaperId,
      };
      this.$axios
        .post("/api/examsystem/texaminfo/getExamInfoList", data)
        .then((res) => {
          console.log(res.data.entityList);
          this.tableData = res.data.entityList;
        });
    },

    // 获取考试paper列表
    getExamPaperList() {
      let data = {
        id: null,
        level: null,
        subjectId: null,
        pageIndex: 1,
        pageSize: 10,
      };
      this.$axios.post("/api/admin/exam/paper/page", data).then((res) => {
        this.examPaperList = res.data.response.list;
      });
    },
  },
};
</script>

<style scoped>
.list-container {
  display: flex;
}

.confirm-button {
  width: 70px;
  height: 35px;
  margin-top: 5px;
  margin-left: 5px;
}
</style>
