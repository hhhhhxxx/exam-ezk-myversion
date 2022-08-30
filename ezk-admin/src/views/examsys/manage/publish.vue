<template>
  <div class="publish-exam-class">
    <el-form :model="form" ref="form" label-width="180px" v-loading="formLoading" :rules="rules">
      <div class="before-base-option">
        <el-form-item label="试 卷：">
          <i class="el-icon-caret-right"></i>
          <span style="margin-right: 10px" v-if="choiceExamName !== ''">已选择：{{choiceExamName}}</span>
          <el-link type="primary" @click="choiceExamDialog = !choiceExamDialog">请选择试卷</el-link>
          <!--   试卷列表     -->
          <el-dialog title="试卷列表" :visible.sync="choiceExamDialog" width="900px">

            <el-select v-model="choiceQuestionBankId" placeholder="请选择题库">
              <el-option
                v-for="item in questionBankList"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select>
            <el-input style="width: 300px"></el-input>
            <el-button>查找</el-button>

            <el-table :data="examPaperList">
              <el-table-column property="id" label="序号" width="50"></el-table-column>
              <el-table-column property="name" label="试卷名称" width="150"></el-table-column>
              <el-table-column property="questionBankName" label="题库名称"></el-table-column>
              <el-table-column property="questionCount" label="题量"></el-table-column>
              <el-table-column property="createTime" label="创建时间" width="150"></el-table-column>
              <el-table-column property="createUser" label="组卷人"></el-table-column>
              <el-table-column property="operate" label="操作">
                <template slot-scope="scope">
                  <el-button @click="choiceOperate(scope.row)" type="text" size="small">选择</el-button>
                </template>
              </el-table-column>
            </el-table>

            <pagination
              v-show="examListTotal > 0"
              :total="examListTotal"
              :page.sync="examListPaperParam.pageIndex"
              :limit.sync="examListPaperParam.pageSize"
              @pagination="getExamList"/>

            <!--<el-pagination-->
            <!--  @current-change="getExamList"-->
            <!--  :page-sizes="[5, 10, 15, 20]"-->
            <!--  :page-size="examListPageSize"-->
            <!--  layout="sizes, prev, pager, next"-->
            <!--  :total="examListTotal">-->
            <!--</el-pagination>-->
          </el-dialog>
        </el-form-item>
        <el-form-item label="考试发放对象：">
          <el-select v-model="classId" multiple placeholder="请选择">
            <el-option
              v-for="item in classList"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="监考老师：">
          <el-select v-model="optionForm.teacherName" placeholder="请选择">
            <el-option
              v-for="item in userDataList"
              :key="item.id"
              :label="item.name"
              :value="item.name">
            </el-option>
          </el-select>
        </el-form-item>
      </div>
      <div class="base-option-class">
        <i class="el-icon-s-tools"></i>
        <span class="base-option-title-class">基本设置</span>
        <el-form-item label="考试时间：">
          <el-date-picker
            v-model="examTimeArray"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            @change="handleCloseDatePicker"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="限制提交时间(min)：" >
          <el-input v-model="optionForm.limitSubmitTime" style="width: 200px"></el-input>
        </el-form-item>
        <el-form-item label="限制进入时间(min)：">
          <el-input v-model="optionForm.limitEnterTime" style="width: 200px"></el-input>
        </el-form-item>
        <el-form-item label="考试批次：">
          <el-input v-model="optionForm.examBatch" style="width: 200px"></el-input>
        </el-form-item>
        <el-form-item label="考试场次：">
          <el-input v-model="optionForm.examRound" style="width: 200px"></el-input>
        </el-form-item>
        <el-form-item label="延时发布：">
          <el-switch
            v-model="kaowu.isPublish"
            active-color="#13ce66"
            inactive-color="#ff4949"
            :active-value="1"
            :inactive-value="0">
          </el-switch>
        </el-form-item>
        <el-form-item label="延时通知时间：">
          <el-date-picker
            v-model="kaowu.noticeTime"
            type="datetime"
            placeholder="选择日期时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item  label="考试类型：">
          <el-select v-model="kaowu.examType" placeholder="请选择">
            <el-option
              v-for="item in examTypeList"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
      </div>
      <el-form-item>
        <el-button type="primary" @click="submitForm">提交</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>

    <div class="other-button-class">
      <el-button type="primary" @click="gjOption = true;fzbOption = false">高级设置<i class="el-icon-caret-bottom"></i></el-button>
      <el-button type="primary" @click="fzbOption = true;gjOption = false">防作弊设置<i class="el-icon-caret-bottom"></i></el-button>
    </div>

    <el-card v-if="gjOption">
      <el-form>
        <el-form-item label="及格标准：">
          <el-input-number v-model="optionForm.passScore" :min="0" label=""></el-input-number>
        </el-form-item>
        <el-form-item label="考试截止前允许重做次数：">
          <el-input-number v-model="optionForm.allowRedo" :min="0" :max="20" label=""></el-input-number>
        </el-form-item>
        <el-form-item label="允许学生查看分数：">
          <el-switch
            v-model="optionForm.allowCheckScore"
            active-color="#13ce66"
            inactive-color="#ff4949"
            :active-value="1"
            :inactive-value="0">
          </el-switch>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card v-if="fzbOption">
      <el-form>
        <el-form-item label="开启抓拍监控：">
          <el-switch
            v-model="optionForm.enableMonitor"
            active-color="#13ce66"
            inactive-color="#ff4949"
            :active-value="1"
            :inactive-value="0">
          </el-switch>
        </el-form-item>
        <el-form-item label="检测到屏幕异常：">
          <el-input-number v-model="optionForm.limitScreenCount" :min="0" :max="20" label=""></el-input-number>&nbsp;&nbsp; 次，强制收卷
        </el-form-item>
        <el-form-item label="允许：">
          <el-select v-model="optionForm.allowMultidevice" placeholder="请选择">
            <el-option
              v-for="item in multideviceOption"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select> 参与考试
        </el-form-item>
      </el-form>
    </el-card>

    <el-dialog :visible.sync="questionPage.showDialog"  width="70%">
      <el-form :model="questionPage.queryParam" ref="queryForm" :inline="true">
        <el-form-item label="ID：">
          <el-input v-model="questionPage.queryParam.id"  clearable></el-input>
        </el-form-item>
        <el-form-item label="题型：">
          <el-select v-model="questionPage.queryParam.questionType" clearable>
            <el-option v-for="item in questionTypeEnum" :key="item.key" :value="item.key" :label="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="queryForm">查询</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="questionPage.listLoading" :data="questionPage.tableData"
                @selection-change="handleSelectionChange" border fit highlight-current-row style="width: 100%">
        <el-table-column type="selection" width="35"></el-table-column>
        <el-table-column prop="id" label="Id" width="60px"/>
        <el-table-column prop="questionType" label="题型" :formatter="questionTypeFormatter" width="70px"/>
        <el-table-column prop="shortTitle" label="题干" show-overflow-tooltip/>
      </el-table>
      <pagination v-show="questionPage.total>0" :total="questionPage.total"
                  :page.sync="questionPage.queryParam.pageIndex" :limit.sync="questionPage.queryParam.pageSize"
                  @pagination="search"/>
      <span slot="footer" class="dialog-footer">
          <el-button @click="questionPage.showDialog = false">取 消</el-button>
          <el-button type="primary" @click="confirmQuestionSelect">确定</el-button>
     </span>
    </el-dialog>
  </div>
</template>

<script>

import { mapGetters, mapState, mapActions } from 'vuex'
import Pagination from '@/components/Pagination'
import QuestionShow from '@/views/exam/question/components/Show'
// import examPaperApi from '@/api/examPaper'
import questionApi from '@/api/question'
import classApi from '@/api/class'


export default {
  components: { Pagination, QuestionShow },
  data () {
    return {
      form: {
        id: null,
        level: null,
        subjectId: null,
        paperType: 1,
        limitDateTime: [],
        name: '',
        suggestTime: null,
        teacherName: '', // 新增监考老师
        titleItems: []
      },
      subjectFilter: null,
      formLoading: false,
      rules: {
        level: [
          { required: true, message: '请选择年级', trigger: 'change' }
        ],
        subjectId: [
          { required: true, message: '请选择学科', trigger: 'change' }
        ],
        paperType: [
          { required: true, message: '请选择试卷类型', trigger: 'change' }
        ],
        name: [
          { required: true, message: '请输入试卷名称', trigger: 'blur' }
        ],
        suggestTime: [
          { required: true, message: '请输入建议时长', trigger: 'blur' }
        ]
      },
      questionPage: {
        multipleSelection: [],
        showDialog: false,
        queryParam: {
          id: null,
          questionType: null,
          subjectId: 1,
          pageIndex: 1,
          pageSize: 5
        },
        listLoading: true,
        tableData: [],
        total: 0
      },
      currentTitleItem: null,

      gjOption: false,
      fzbOption: false,
      optionForm: {
        teacherName: '', // 监考老师
        examStartTime: null,
        examEndTime: null,
        limitSubmitTime: null,
        limitEnterTime: null,
        examBatch: null,
        examRound: null,
        enableMonitor: null,
        limitScreenCount: 0,
        passScore: null,
        allowRedo: 0,
        allowCheckScore: 0,
        allowMultidevice: '0'
      },
      multideviceOption: [
        { value: '0', label: '电脑端' },
        { value: '1', label: 'app' },
        { value: '2', label: '电脑端与app' }
      ],
      examTimeArray: [],
      userDataList: [], // 所有用户list
      userIdList: [], // 发送通知id list

      choiceExamDialog: false, // 选择试卷对话框可见

      examPaperList: [], // 试卷列表
      examListTotal: 0,
      examListPaperParam: {
        pageIndex: 1,
        pageSize: 5
      },

      choiceExamId: null, // 选中试卷Id
      choiceExamName: '', // 选择的试卷名字


      kaowu: {
        noticeTime: '',
        examType: '',
        isPublish: 0
      },

      examTypeList: [
        { value: '0', label: '0' },
        { value: '1', label: '1' },
        { value: '2', label: '2' }
      ],

      classList: [], // 班级列表
      classQueryParam: {
        // 顶死id
        id: 1,
        pageIndex: 1,
        pageSize: 10
      },
      classId: 0, // 选择的班级id
      questionBankList: [],
      choiceQuestionBankId: null,
    }
  },
  created () {
    this.getExamList()
    this.getUserList()
    this.getClassList()
    this.getQuestionBankList()
    // let id = this.$route.query.id
    this.choiceExamId = this.$route.query.paperid
    this.choiceExamName = this.$route.query.name
    let _this = this
    this.initSubject(function () {
      _this.subjectFilter = _this.subjects
    })
  },
  methods: {
    getQuestionBankList(){
      this.$axios.get(`/api/examsystem/tquestionbank/list`).then(res=>{
        console.log(res)
        this.questionBankList = res.data.page.list
      })
    },

    getClassList(){
      classApi.classList(this.classQueryParam).then((res) => {
        this.classList = res.response.records
      })
    },

    // 选择试卷
    choiceOperate (row) {
      this.choiceExamId = row.id
      this.choiceExamDialog = false
      this.choiceExamName = row.name
    },

    // 发送请求获取试卷列表
    getExamList () {
      // let data = {
      //   pageIndex: val,
      //   pageSize: this.examListPageSize
      // }

      let param = {
        pageIndex: this.examListPaperParam.pageIndex,
        pageSize: this.examListPaperParam.pageSize
      }

      this.$axios.post('/api/admin/exam/paper/page', param).then(res => {
        this.examListTotal = res.data.response.total
        this.examPaperList = res.data.response.list
      })
    },
    // 获取用户列表
    getUserList () {
      let query = {
        currentPage: 1,
        pageSize: 20
      }
      this.$axios.post(`/user/getUserPages`,query).then(res=>{
        this.userDataList = res.data.records
      })
    },

    // 日期改变
    handleCloseDatePicker () {
      this.optionForm.examStartTime = this.examTimeArray[0]
      this.optionForm.examEndTime = this.examTimeArray[1]
    },

    // 发布考试
    submitForm () {
      let _this = this
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.formLoading = true
          let requestData = {
            paperId: this.choiceExamId,
            argumentsEntity: _this.optionForm,
            // noticeUserIdList: _this.userIdList,
            kaowuEntity: {
              noticeTime: _this.kaowu.noticeTime,
              examType: _this.kaowu.examType,
              isPublish: _this.kaowu.isPublish,
              roomBatch: _this.optionForm.examBatch,
              roomRound: _this.optionForm.examRound
            },
            classId: this.classId
          }
          // 发布考试arguments表
          this.$axios.post('/api/examsystem/manage/publishExam', requestData).then(res => {
            if (res.data.code === 500) {
              _this.$message.error(res.data.message)
              this.formLoading = false
              return
            }
            _this.$message.success('发布成功')
            _this.delCurrentView(_this).then(() => {
              _this.$router.push('/task/manage/list')
            })
          })
        } else {
          return false
        }
      })
    },
    queryForm () {
      this.questionPage.queryParam.pageIndex = 1
      this.search()
    },
    confirmQuestionSelect () {
      let _this = this
      this.questionPage.multipleSelection.forEach(q => {
        questionApi.select(q.id).then(re => {
          _this.currentTitleItem.questionItems.push(re.response)
        })
      })
      this.questionPage.showDialog = false
    },
    levelChange () {
      this.form.subjectId = null
      this.subjectFilter = this.subjects.filter(data => data.level === this.form.level)
    },
    search () {
      this.questionPage.queryParam.subjectId = this.form.subjectId
      this.questionPage.listLoading = true
      questionApi.pageList(this.questionPage.queryParam).then(data => {
        const re = data.response
        this.questionPage.tableData = re.list
        this.questionPage.total = re.total
        this.questionPage.queryParam.pageIndex = re.pageNum
        this.questionPage.listLoading = false
      })
    },
    handleSelectionChange (val) {
      this.questionPage.multipleSelection = val
    },
    questionTypeFormatter (row, column, cellValue, index) {
      return this.enumFormat(this.questionTypeEnum, cellValue)
    },
    subjectFormatter (row, column, cellValue, index) {
      return this.subjectEnumFormat(cellValue)
    },
    resetForm () {
      let lastId = this.form.id
      this.$refs['form'].resetFields()
      this.form = {
        id: null,
        level: null,
        subjectId: null,
        paperType: 1,
        limitDateTime: [],
        name: '',
        suggestTime: null,
        titleItems: []
      }
      this.form.id = lastId
    },
    ...mapActions('exam', { initSubject: 'initSubject' }),
    ...mapActions('tagsView', { delCurrentView: 'delCurrentView' })
  },
  computed: {
    ...mapGetters('enumItem', ['enumFormat']),
    ...mapState('enumItem', {
      questionTypeEnum: state => state.exam.question.typeEnum,
      paperTypeEnum: state => state.exam.examPaper.paperTypeEnum,
      levelEnum: state => state.user.levelEnum
    }),
    ...mapState('exam', { subjects: state => state.subjects })
  }
}
</script>

<style lang="scss">
.exampaper-item-box {
  .q-title {
    margin: 0px 5px 0px 5px;
  }
  .q-item-content {
  }
}
.before-base-option{
  height: auto;
  margin: 10px;
  border: 2px solid #ededed;
  padding: 10px;
}

.base-option-class{
  height: auto;
  margin: 10px;
  border: 2px solid #ededed;
  padding: 10px;
}

.base-option-title-class{
  font-size: 14px;
  margin-left: 10px;
  margin-bottom: 20px;
  color: grey;
}

.other-button-class{
  margin-left: 50px;
  margin-top: 50px;
}

.publish-exam-class{
  width: 100%;
}
</style>
