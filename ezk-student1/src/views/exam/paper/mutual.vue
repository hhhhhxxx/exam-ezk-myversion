<template>
  <div style="background-color: #c8edcc">
    <el-row class="do-exam-title">
      <el-col :span="1">
        <span :key="item.itemOrder" v-for="item in answer.answerItems">
             <el-tag :type="questionDoRightTag(item.doRight)" class="do-exam-title-tag"
                     @click="goAnchor('#question-'+item.itemOrder)">{{ item.itemOrder }}</el-tag>
        </span>
      </el-col>
    </el-row>
    <el-row class="do-exam-title-hidden">
      <el-col :span="24">
        <span :key="item.itemOrder" v-for="item in answer.answerItems">
             <el-tag class="do-exam-title-tag">{{ item.itemOrder }}</el-tag>
        </span>
      </el-col>
    </el-row>
    <el-container class="app-item-contain">
      <el-header class="align-center">
        <h1>互评同学的“ {{ form.name }}”</h1>
        <div>
          <span class="question-title-padding">试卷得分：{{ answer.score }}</span>
          <span class="question-title-padding">试卷耗时：{{ formatSeconds(answer.doTime) }}</span>
        </div>
      </el-header>
      <el-main>
        <el-form :model="form" ref="form" v-loading="formLoading" label-width="100px">
          <el-row :key="index" v-for="(titleItem,index) in form.titleItems">
            <h3>{{ titleItem.name }}</h3>
            <el-card class="exampaper-item-box" v-if="titleItem.questionItems.length!==0">
              <el-form-item :key="questionItem.itemOrder" :label="questionItem.itemOrder+'.'"
                            v-for="questionItem in titleItem.questionItems"
                            class="exam-question-item" label-width="50px" :id="'question-'+ questionItem.itemOrder">
                <el-row>
                  <QuestionAnswerShow2 :qType="questionItem.questionType" :question="questionItem"
                                       :answer="answer.answerItems[questionItem.itemOrder-1]"/>
                </el-row>
                <el-row v-if="answer.answerItems[questionItem.itemOrder-1].doRight === null">
                  <label style="color: #e6a23c">评分：</label>
                  <el-radio-group v-model="answer.answerItems[questionItem.itemOrder-1].score">
                    <el-radio v-for="item in scoreSelect(questionItem.score)" :key="item" :label="item">
                      {{ item }}
                    </el-radio>
                  </el-radio-group>
                </el-row>
              </el-form-item>
            </el-card>
          </el-row>

          <el-row>
            <h3>总得分： {{ allScore }}</h3>
          </el-row>

          <el-row class="do-align-center">
            <el-button type="primary" @click="submitForm">提交</el-button>
            <router-link target="_blank" :to="{ path:'/mutualList/index' }">
              <el-button>取消</el-button>
            </router-link>
          </el-row>
        </el-form>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import {mapState, mapGetters} from 'vuex'
import {formatSeconds} from '@/utils'
import QuestionAnswerShow2 from '../components/QuestionAnswerShow2'
import examPaperAnswerApi from '@/api/examPaperAnswer'

export default {
  components: {QuestionAnswerShow2},
  data() {
    return {
      form: {},
      formLoading: false,
      answer: {
        id: null,
        score: 0,
        doTime: 0,
        answerItems: [],
        doRight: false
      }
    }
  },
  created() {
    let id = this.$route.query.id
    let _this = this
    if (id && parseInt(id) !== 0) {
      _this.formLoading = true
      examPaperAnswerApi.read(id).then(re => {
        _this.form = re.response.paper
        _this.answer = re.response.answer
        _this.formLoading = false
      })
    }
  },
  methods: {
    submitForm() {
      let _this = this
      _this.$router.push('/mutual/index')
    },
    scoreSelect(score) {
      let array = []
      for (let i = 0; i <= parseInt(score); i++) {
        array.push(i.toString())
      }
      if (score.indexOf('.') !== -1) {
        array.push(score)
      }
      return array
    },
    formatSeconds(theTime) {
      return formatSeconds(theTime)
    },
    questionDoRightTag(status) {
      return this.enumFormat(this.doRightTag, status)
    },
    goAnchor(selector) {
      this.$el.querySelector(selector).scrollIntoView({behavior: 'instant', block: 'center', inline: 'nearest'})
    }
  },
  computed: {
    ...mapGetters('enumItem', ['enumFormat']),
    ...mapState('enumItem', {
      doRightTag: state => state.exam.question.answer.doRightTag
    }),
    allScore() {
      let score = 0
      this.answer.answerItems.forEach(item => {
        score += Number(item.score)
      })
      return score
    }
  }
}
</script>

<style lang="scss" scoped>
.align-center {
  text-align: center
}

.exam-question-item {
  padding: 10px;

  .el-form-item__label {
    font-size: 15px !important;
  }
}

.do-exam-title el-col {
  display: flex;
  flex-direction: column;
}

.question-title-padding {
  padding-left: 25px;
  padding-right: 25px;
}
</style>
