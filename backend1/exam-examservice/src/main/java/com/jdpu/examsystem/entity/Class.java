package com.jdpu.examsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: xJh
 * @Date: 2022/4/7
 */
@Data
@TableName("t_class")
public class Class implements Serializable {
    private static final long serialVersionUID = -2143539181805283910L;
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer count;

    private String description;

    private String name;
}
