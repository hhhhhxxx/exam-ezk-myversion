package com.jdpu.examsystem.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: xJh
 * @Date: 2022/4/7
 * 试卷班级关联表
 */
@Data
@TableName("t_class_exam")
public class ClassExam implements Serializable {
    private static final long serialVersionUID = -2143539181805283910L;

    private Integer id;

    private Integer classId;

    private Integer examagrId;


}
