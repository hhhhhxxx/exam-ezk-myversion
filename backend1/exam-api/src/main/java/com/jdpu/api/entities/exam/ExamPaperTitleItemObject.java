package com.jdpu.api.entities.exam;


import java.util.List;

/**
 * 试卷下的题目数组
 * titleItems:[]
 * 比如：单选题、 多选题 、 简答题
 */
public class ExamPaperTitleItemObject {

    private String name;    // 单选题、多选题....

    private List<ExamPaperQuestionItemObject> questionItems;    // 具体到每一个问题

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ExamPaperQuestionItemObject> getQuestionItems() {
        return questionItems;
    }

    public void setQuestionItems(List<ExamPaperQuestionItemObject> questionItems) {
        this.questionItems = questionItems;
    }

    @Override
    public String toString() {
        return "ExamPaperTitleItemObject{" +
                "name='" + name + '\'' +
                ", questionItems=" + questionItems +
                '}';
    }
}
