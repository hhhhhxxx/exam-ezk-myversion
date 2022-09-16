<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true">
      <el-form-item label="试卷名称：">
        <el-input v-model="queryParam.subjectId" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm">查询</el-button>
      </el-form-item>
    </el-form>

    <el-table
      v-loading="listLoading"
      :data="tableData"
      border
      fit
      highlight-current-row
      style="width: 100%"
    >
      <el-table-column prop="id" label="Id" width="120" align="center" />
      <el-table-column
        prop="paperName"
        label="试卷名称"
        width="150"
        align="center"
      />
      <el-table-column
        prop="paperName"
        label="班级名称"
        width="100"
        align="center"
      />
      <el-table-column
        prop="paperName"
        label="监考老师"
        width="200"
        align="center"
      />
      <el-table-column
        prop="createTime"
        label="提交时间"
        width="300px"
        align="center"
      />
      <el-table-column width="142px" label="操作" align="center">
        <template slot-scope="{ row }">
          <router-link
            :to="{ path: '/analysis/scoreAnalysis', query: { id: row.id } }"
          >
            <el-button size="mini">成绩分析</el-button>
          </router-link>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParam.pageIndex"
      :limit.sync="queryParam.pageSize"
      @pagination="search"
    />
  </div>
</template>

<script>
import { mapGetters, mapState, mapActions } from "vuex";
import Pagination from "@/components/Pagination";
import examPaperAnswerApi from "@/api/examPaperAnwser";

export default {
  components: { Pagination },
  data() {
    return {
      queryParam: {
        subjectId: null,
        pageIndex: 1,
        pageSize: 10,
      },
      listLoading: false,
      tableData: [],
      total: 0,
    };
  },
  created() {
    this.initSubject();
    this.search();
  },
  methods: {
    search() {
      this.listLoading = true;
      examPaperAnswerApi.page(this.queryParam).then((data) => {
        const re = data.response;
        this.tableData = re.list;
        this.total = re.total;
        this.queryParam.pageIndex = re.pageNum;
        this.listLoading = false;
      });
    },
    submitForm() {
      this.queryParam.pageIndex = 1;
      this.search();
    },
    ...mapActions("exam", { initSubject: "initSubject" }),
  },
  computed: {
    ...mapGetters("enumItem", ["enumFormat"]),
    ...mapGetters("exam", ["subjectEnumFormat"]),
    ...mapState("exam", { subjects: (state) => state.subjects }),
  },
};
</script>
