package com.jdpu.common.xzsOld.base;

import com.jdpu.common.xzsOld.utils.ModelMapperSingle;
import org.modelmapper.ModelMapper;

/**
 * VM父类
 * @author zuck
 */
public class BaseVM {
    protected static ModelMapper modelMapper = ModelMapperSingle.Instance();
    public static ModelMapper getModelMapper() {
        return modelMapper;
    }
    public static void setModelMapper(ModelMapper modelMapper) {
        BaseVM.modelMapper = modelMapper;
    }
}
