<template>
  <div class="app-container">
    <h3 style="margin-bottom: 10px">试卷名称:</h3>
    <p style="margin-bottom: 10px; font-size: 18px">总人数：</p>
    <el-form :model="queryParam" ref="queryForm" :inline="true">
      <el-form-item label="考生号:">
        <el-input v-model="queryParam.id" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary">查询</el-button>
        <router-link :to="{ path: '/exam/paper/edit' }" class="link-left">
          <el-button type="primary">发送考试通知</el-button>
        </router-link>
        <router-link :to="{ path: '/exam/paper/edit' }" class="link-left">
          <el-button type="primary">发送人脸采集通知</el-button>
        </router-link>
      </el-form-item>
    </el-form>
    <el-table
      :data="tableData"
      border
      fit
      highlight-current-row
      style="width: 100"
    >
      <el-table-column
        prop="name"
        label="学生姓名"
        width="120px"
        align="center"
      />
      <el-table-column
        prop="user_num"
        label="学号"
        width="120px"
        align="center"
      />
      <el-table-column
        prop="phone"
        label="手机号"
        width="120px"
        align="center"
      />
      <el-table-column
        prop="source"
        label="来源"
        width="180px"
        align="center"
      />
      <el-table-column label="照片" width="180px" align="center">
        <img
          src="@/assets/image/1.jpg"
          alt=""
          style="width: 160px; height: 80px"
        />
      </el-table-column>
      <el-table-column label="操作" align="center" width="172px">
        <template slot-scope="{ row }">
          <el-button
            type="primary"
            size="mini"
            @click="
              $router.push({
                path: '/exam/paper/manage',
                query: { id: row.id },
              })
            "
            >发通知</el-button
          >
          <el-button
            type="primary"
            size="mini"
            @click="
              $router.push({
                path: '/manage/invigilate/',
                query: { id: row.id },
              })
            "
            >监考</el-button
          >
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import examPaperApi from "@/api/examPaper";
export default {
  data() {
    return {
      queryParam: {
        id: null,
        level: null,
        subjectId: null,
        pageIndex: 1,
        pageSize: 10,
      },
      subjectFilter: null,
      listLoading: true,
      tableData: [],
      total: 0,
      imgUrl: [],
    };
  },
  created() {
    this.search();
  },
  methods: {
    search() {
      this.listLoading = true;
      examPaperApi.peopleList(this.queryParam).then((data) => {
        const re = data.response;
        this.tableData = re.list;
        this.total = re.total;
        this.queryParam.pageIndex = re.pageNum;
        this.listLoading = false;
      });
      this.imgUrl = this.tableData.map((item) => {
        return item.photo;
      });
    },
  },
};
</script>

<style>
</style>