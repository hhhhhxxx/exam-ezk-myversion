<template>
  <div class="app-container">
    <el-form
      :model="form"
      ref="form"
      label-width="180px"
      v-loading="formLoading"
      :rules="rules"
    >
      <el-form-item label="年级：" prop="level" required>
        <el-select
          v-model="form.level"
          placeholder="年级"
          @change="levelChange"
        >
          <el-option
            v-for="item in levelEnum"
            :key="item.key"
            :value="item.key"
            :label="item.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="学科：" prop="subjectId" required>
        <el-select v-model="form.subjectId" placeholder="学科">
          <el-option
            v-for="item in subjectFilter"
            :key="item.id"
            :value="item.id"
            :label="item.name + ' ( ' + item.levelName + ' )'"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="试卷类型：" prop="paperType" required>
        <el-select v-model="form.paperType" placeholder="试卷类型">
          <el-option
            v-for="item in paperTypeEnum"
            :key="item.key"
            :value="item.key"
            :label="item.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="时间限制：" required v-show="form.paperType === 4">
        <el-date-picker
          v-model="form.limitDateTime"
          value-format="yyyy-MM-dd HH:mm:ss"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item label="试卷名称：" prop="name" required>
        <el-input v-model="form.name" />
      </el-form-item>
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
      <el-form-item label="限制提交时间(min)：">
        <el-input v-model="optionForm.limitSubmitTime"></el-input>
      </el-form-item>
      <el-form-item label="限制进入时间(min)：">
        <el-input v-model="optionForm.limitEnterTime"></el-input>
      </el-form-item>
      <el-form-item label="考试批次：">
        <el-input v-model="optionForm.examBatch"></el-input>
      </el-form-item>
      <el-form-item label="考试场次：">
        <el-input v-model="optionForm.examRound"></el-input>
      </el-form-item>
      <!-- <el-form-item :key="index" :label="'标题'+(index+1)+'：'" required v-for="(titleItem,index) in form.titleItems">
        <el-input v-model="titleItem.name" style="width: 80%"/>
        <el-button type="text" class="link-left" style="margin-left: 20px" size="mini" @click="addQuestion(titleItem)">
          添加题目
        </el-button>
        <el-button type="text" class="link-left" size="mini" @click="form.titleItems.splice(index,1)">删除</el-button>
        <el-card class="exampaper-item-box" v-if="titleItem.questionItems.length!==0">
          <el-form-item :key="questionIndex" :label="'题目'+(questionIndex+1)+'：'"
                        v-for="(questionItem,questionIndex) in titleItem.questionItems" style="margin-bottom: 15px">
            <el-row>
              <el-col :span="23">
                <QuestionShow :qType="questionItem.questionType" :question="questionItem"/>
              </el-col>
              <el-col :span="1">
                <el-button type="text" size="mini" @click="titleItem.questionItems.splice(questionIndex,1)">删除
                </el-button>
              </el-col>
            </el-row>
          </el-form-item>
        </el-card>
      </el-form-item> -->
      <el-form-item label="建议时长：" prop="suggestTime" required>
        <el-input v-model="form.suggestTime" placeholder="分钟" />
      </el-form-item>
      <el-form-item label="监考老师：" prop="teacherName" required>
        <el-input v-model="form.teacherName" placeholder="姓名" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm">提交</el-button>
        <el-button @click="resetForm">重置</el-button>
        <!-- <el-button type="success" @click="addTitle">添加标题</el-button> -->
      </el-form-item>
    </el-form>
    <div class="other-button-class">
      <el-button
        type="primary"
        @click="
        gjOption = true;
        fzbOption = false;
      "
      >高级设置<i class="el-icon-caret-bottom"></i
      ></el-button>
      <el-button
        type="primary"
        @click="
        fzbOption = true;
        gjOption = false;
      "
      >防作弊设置<i class="el-icon-caret-bottom"></i
      ></el-button>
    </div>

    <el-card v-if="gjOption">
      <el-form>
        <el-form-item label="及格标准：">
          <el-input-number
            v-model="optionForm.passScore"
            :min="0"
            label=""
          ></el-input-number>
        </el-form-item>
        <el-form-item label="考试截止前允许重做次数：">
          <el-input-number
            v-model="optionForm.allowRedo"
            :min="0"
            :max="20"
            label=""
          ></el-input-number>
        </el-form-item>
        <el-form-item label="允许学生查看分数：">
          <el-switch
            v-model="optionForm.allowCheckScore"
            active-color="#13ce66"
            inactive-color="#ff4949"
            :active-value="1"
            :inactive-value="0"
          >
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
            :inactive-value="0"
          >
          </el-switch>
        </el-form-item>
        <el-form-item label="检测到屏幕异常：">
          <el-input-number
            v-model="optionForm.limitScreenCount"
            :min="0"
            :max="20"
            label=""
          ></el-input-number
          >&nbsp;&nbsp; 次，强制收卷
        </el-form-item>
        <el-form-item label="允许：">
          <el-select v-model="optionForm.allowMultidevice" placeholder="请选择">
            <el-option
              v-for="item in multideviceOption"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
          参与考试
        </el-form-item>
      </el-form>
    </el-card>

    <!-- <el-dialog :visible.sync="questionPage.showDialog"  width="70%">
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
    </el-dialog> -->
  </div>
</template>

<script>
import { mapGetters, mapState, mapActions } from 'vuex'
import Pagination from '@/components/Pagination'
import QuestionShow from '@/views/exam/question/components/Show'
import examPaperApi from '@/api/examPaper'
import questionApi from '@/api/question'

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
        level: [{ required: true, message: '请选择年级', trigger: 'change' }],
        subjectId: [
          { required: true, message: '请选择学科', trigger: 'change' }
        ],
        paperType: [
          { required: true, message: '请选择试卷类型', trigger: 'change' }
        ],
        name: [{ required: true, message: '请输入试卷名称', trigger: 'blur' }],
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
      examTimeArray: []
    }
  },
  created () {
    let id = this.$route.query.id
    let _this = this
    this.initSubject(function () {
      _this.subjectFilter = _this.subjects
    })
    if (id && parseInt(id) !== 0) {
      _this.formLoading = true
      examPaperApi.select(id).then((re) => {
        _this.form = re.response
        _this.formLoading = false
      })
    }
  },
  methods: {
    // 日期改变
    handleCloseDatePicker () {
      this.optionForm.examStartTime = this.examTimeArray[0]
      this.optionForm.examEndTime = this.examTimeArray[1]
    },

    submitForm () {
      let _this = this
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.formLoading = true
          // examPaperApi.edit(this.form).then(re => {
          //   if (re.code === 1) {
          //     _this.$message.success(re.message)
          //     _this.delCurrentView(_this).then(() => {
          //       _this.$router.push('/exam/paper/list')
          //     })
          //   } else {
          //     _this.$message.error(re.message)
          //     this.formLoading = false
          //   }
          // }).catch(e => {
          //   this.formLoading = false
          // })
          let requestData = {
            paperEditRequestVM: _this.form,
            argumentsEntity: _this.optionForm
          }
          this.$axios
            .post('/api/examsystem/texamarguments/publish', requestData)
            .then((res) => {
              _this.$message.success(res.message)
              _this.delCurrentView(_this).then(() => {
                _this.$router.push('/exam/paper/list')
              })
            })
        } else {
          return false
        }
      })
    },
    // addTitle () {
    //   this.form.titleItems.push({
    //     name: '',
    //     questionItems: []
    //   })
    // },
    addQuestion (titleItem) {
      this.currentTitleItem = titleItem
      this.questionPage.showDialog = true
      this.search()
    },
    removeTitleItem (titleItem) {
      this.form.titleItems.remove(titleItem)
    },
    removeQuestion (titleItem, questionItem) {
      titleItem.questionItems.remove(questionItem)
    },
    queryForm () {
      this.questionPage.queryParam.pageIndex = 1
      this.search()
    },
    confirmQuestionSelect () {
      let _this = this
      this.questionPage.multipleSelection.forEach((q) => {
        questionApi.select(q.id).then((re) => {
          _this.currentTitleItem.questionItems.push(re.response)
        })
      })
      this.questionPage.showDialog = false
    },
    levelChange () {
      this.form.subjectId = null
      this.subjectFilter = this.subjects.filter(
        (data) => data.level === this.form.level
      )
    },
    search () {
      this.questionPage.queryParam.subjectId = this.form.subjectId
      this.questionPage.listLoading = true
      questionApi.pageList(this.questionPage.queryParam).then((data) => {
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
      questionTypeEnum: (state) => state.exam.question.typeEnum,
      paperTypeEnum: (state) => state.exam.examPaper.paperTypeEnum,
      levelEnum: (state) => state.user.levelEnum
    }),
    ...mapState('exam', { subjects: (state) => state.subjects })
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


</style>
