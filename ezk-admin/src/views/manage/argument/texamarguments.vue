<template>
  <div class="mod-config">
    <el-form
      :inline="true"
      :model="dataForm"
      @keyup.enter.native="getDataList()"
    >
      <el-form-item>
        <el-input
          v-model="dataForm.key"
          placeholder="参数名"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <!--        <el-button v-if="isAuth('examsystem:texamarguments:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>-->
        <el-button type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <!--        <el-button v-if="isAuth('examsystem:texamarguments:delete')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>-->
        <el-button
          type="danger"
          @click="deleteHandle()"
          :disabled="dataListSelections.length <= 0"
          >批量删除</el-button
        >
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      style="width: 100%"
    >
      <el-table-column
        type="selection"
        header-align="center"
        align="center"
        width="50"
      >
      </el-table-column>
      <el-table-column
        prop="id"
        header-align="center"
        align="center"
        label="id"
      >
      </el-table-column>
      <el-table-column
        prop="examPaperId"
        header-align="center"
        align="center"
        label="试卷表关联id"
      >
      </el-table-column>
      <el-table-column
        prop="examStartTime"
        header-align="center"
        align="center"
        label="开始时间"
      >
      </el-table-column>
      <el-table-column
        prop="examEndTime"
        header-align="center"
        align="center"
        label="截止时间"
      >
      </el-table-column>
      <el-table-column
        prop="limitSubmitTime"
        header-align="center"
        align="center"
        label="限时提交/min"
      >
      </el-table-column>
      <el-table-column
        prop="limitEnterTime"
        header-align="center"
        align="center"
        label="限时进入/min"
      >
      </el-table-column>
      <el-table-column
        prop="examBatch"
        header-align="center"
        align="center"
        label="考试批次"
      >
      </el-table-column>
      <el-table-column
        prop="examRound"
        header-align="center"
        align="center"
        label="考试场次"
      >
      </el-table-column>
      <el-table-column
        prop="enableMonitor"
        header-align="center"
        align="center"
        label="是否开启监控"
      >
        <template slot-scope="scope">
          <p v-if="scope.row.enableMonitor === 0">关闭</p>
          <p v-if="scope.row.enableMonitor !== 0">允许</p>
        </template>
      </el-table-column>
      <el-table-column
        prop="limitScreenCount"
        header-align="center"
        align="center"
        label="屏幕异常/次,收卷"
      >
      </el-table-column>
      <el-table-column
        prop="passScore"
        header-align="center"
        align="center"
        label="及格标准"
      >
      </el-table-column>
      <el-table-column
        prop="allowRedo"
        header-align="center"
        align="center"
        label="重做次数"
      >
      </el-table-column>
      <el-table-column
        prop="allowCheckScore"
        header-align="center"
        align="center"
        label="学生查看分数"
      >
        <template slot-scope="scope">
          <p v-if="scope.row.allowCheckScore === 0">不允许</p>
          <p v-if="scope.row.allowCheckScore !== 0">允许</p>
        </template>
      </el-table-column>
      <el-table-column
        prop="allowMultidevice"
        header-align="center"
        align="center"
        label="多终端考试"
      >
        <template slot-scope="scope">
          <p v-if="scope.row.allowMultidevice === 0">网页考试</p>
          <p v-if="scope.row.allowMultidevice === 1">app考试</p>
          <p v-if="scope.row.allowMultidevice === 2">允许网页与app考试</p>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作"
      >
        <template slot-scope="scope">
          <el-button
            type="text"
            size="small"
            @click="addOrUpdateHandle(scope.row.id)"
            >修改</el-button
          >
          <el-button
            type="text"
            size="small"
            @click="deleteHandle(scope.row.id)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper"
    >
    </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update
      v-if="addOrUpdateVisible"
      ref="addOrUpdate"
      @refreshDataList="getDataList"
    ></add-or-update>
  </div>
</template>

<script>
import AddOrUpdate from "./texamarguments-add-or-update";
export default {
  data() {
    return {
      dataForm: {
        key: "",
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
    };
  },
  components: {
    AddOrUpdate,
  },
  activated() {
    this.getDataList();
  },
  created() {
    this.getDataList();
  },
  methods: {
    // 获取数据列表
    getDataList() {
      this.dataListLoading = true;
      // this.$axios({
      //   url: this.$axios.adornUrl('/api/examsystem/texamarguments/list'),
      //   method: 'get',
      //   params: this.$axios.adornParams({
      //     'page': this.pageIndex,
      //     'limit': this.pageSize,
      //     'key': this.dataForm.key
      //   })
      // }).then(({ data }) => {
      //   if (data && data.code === 0) {
      //     this.dataList = data.page.list
      //     this.totalPage = data.page.totalCount
      //   } else {
      //     this.dataList = []
      //     this.totalPage = 0
      //   }
      //   this.dataListLoading = false
      // })
      let listData = {
        page: this.pageIndex,
        limit: this.pageSize,
        key: this.dataForm.key,
      };
      this.$axios({
        url: "/api/examsystem/texamarguments/list",
        method: "get",
        params: listData,
        // data: listData,
      }).then((res) => {
        console.log(res);
        if (res.data && res.data.code === 0) {
          this.dataList = res.data.page.list;
          this.totalPage = res.data.page.totalCount;
        } else {
          this.dataList = [];
          this.totalPage = 0;
        }
        this.dataListLoading = false;
      });
    },
    // 每页数
    sizeChangeHandle(val) {
      this.pageSize = val;
      this.pageIndex = 1;
      this.getDataList();
    },
    // 当前页
    currentChangeHandle(val) {
      this.pageIndex = val;
      this.getDataList();
    },
    // 多选
    selectionChangeHandle(val) {
      this.dataListSelections = val;
    },
    // 新增 / 修改
    addOrUpdateHandle(id) {
      this.addOrUpdateVisible = true;
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id);
      });
    },
    // 删除
    deleteHandle(id) {
      var ids = id
        ? [id]
        : this.dataListSelections.map((item) => {
            return item.id;
          });
      this.$confirm(
        `确定对[id=${ids.join(",")}]进行[${id ? "删除" : "批量删除"}]操作?`,
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      ).then(() => {
        this.$http({
          url: this.$http.adornUrl("/examsystem/texamarguments/delete"),
          method: "post",
          data: this.$http.adornData(ids, false),
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message({
              message: "操作成功",
              type: "success",
              duration: 1500,
              onClose: () => {
                this.getDataList();
              },
            });
          } else {
            this.$message.error(data.msg);
          }
        });
      });
    },
  },
};
</script>
