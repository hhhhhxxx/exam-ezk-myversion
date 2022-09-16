package com.jdpu.api.controller.student;

import com.jdpu.api.context.UserBaseApiController;
import com.jdpu.api.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ？？
 */
@RestController
@RequestMapping(value = "/api/student/question")
public class StudentQuestionController extends UserBaseApiController {

    private final QuestionService questionService;

    @Autowired
    public StudentQuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }
}
