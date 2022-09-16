<template>
  <div style="margin-top: 10px" class="app-contain">

      <el-table :data="tableData" fit highlight-current-row style="width: 100%">
        <el-table-column prop="id" label="编号" width="90px"/>
        <el-table-column prop="name" label="姓名"/>
        <el-table-column prop="examName" label="互评试卷"/>
        <el-table-column align="right">
          <template slot-scope="{row}">
            <!--<router-link target="_blank" :to="{path:'/do',query:{id:row.id}}">-->
            <!--  <el-button type="text" size="small">开始互评</el-button>-->
            <!--</router-link>-->
            <router-link target="_blank" :to="{path:'/mutual/mutual',query:{id: row.id}}">
              <el-button  type="text" size="small">开始互评</el-button>
            </router-link>
          </template>
        </el-table-column>
      </el-table>

  </div>
</template>

<script>
import Pagination from '@/components/Pagination'

export default {
  components: {Pagination},
  data() {
    return {
      queryParam: {
        pageIndex: 1,
        pageSize: 10
      },
      tabId: '',
      listLoading: true,
      subjectList: [],
      tableData: [{
        id: 67,
        name: '张三',
        examName: '自动组卷-操作系统'
      }, {
        id: 69,
        name: '李四',
        examName: '人工智能期末试卷'
      }, {
        id: 70,
        name: '王五',
        examName: '《思想道德基础与法律修养》期末考试-自动组卷'
      }, {
        id: 72,
        name: '小明',
        examName: '《思想道德基础与法律修养》期末考试'
      }],
      total: 4
    }
  },
  created () {
    // this.initSubject()
  },
  methods: {
    initSubject () {
      // let _this = this
      // subjectApi.list().then(re => {
      //   _this.subjectList = re.response
      //   let subjectId = _this.subjectList[0].id
      //   _this.queryParam.subjectId = subjectId
      //   _this.tabId = subjectId.toString()
      //   _this.search()
      // })
    },
    search () {
    }
  }
}
</script>
