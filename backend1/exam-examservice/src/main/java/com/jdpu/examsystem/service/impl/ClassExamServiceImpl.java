package com.jdpu.examsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jdpu.examsystem.service.ClassExamService;
import com.jdpu.examsystem.dao.ClassExamMapper;
import com.jdpu.examsystem.entity.ClassExam;
import org.springframework.stereotype.Service;

/**
 * @Author: xJh
 * @Date: 2022/4/8
 */
@Service
public class ClassExamServiceImpl extends ServiceImpl<ClassExamMapper, ClassExam> implements ClassExamService {
}
