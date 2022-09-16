package com.jdpu.api.entities;


import java.util.List;

/**
 *
 */
public class ExamPaperAnswerInfo {
    public ExamPaper examPaper; // 考试试卷
    public ExamPaperAnswer examPaperAnswer; // 学生考试记录
    public List<ExamPaperQuestionCustomerAnswer> examPaperQuestionCustomerAnswers;

    public ExamPaper getExamPaper() {
        return examPaper;
    }

    public void setExamPaper(ExamPaper examPaper) {
        this.examPaper = examPaper;
    }

    public ExamPaperAnswer getExamPaperAnswer() {
        return examPaperAnswer;
    }

    public void setExamPaperAnswer(ExamPaperAnswer examPaperAnswer) {
        this.examPaperAnswer = examPaperAnswer;
    }

    public List<ExamPaperQuestionCustomerAnswer> getExamPaperQuestionCustomerAnswers() {
        return examPaperQuestionCustomerAnswers;
    }

    public void setExamPaperQuestionCustomerAnswers(List<ExamPaperQuestionCustomerAnswer> examPaperQuestionCustomerAnswers) {
        this.examPaperQuestionCustomerAnswers = examPaperQuestionCustomerAnswers;
    }


    @Override
    public String toString() {
        return "ExamPaperAnswerInfo{" +
                "examPaper=" + examPaper +
                ", examPaperAnswer=" + examPaperAnswer +
                ", examPaperQuestionCustomerAnswers=" + examPaperQuestionCustomerAnswers +
                '}';
    }
}
