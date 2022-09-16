package com.jdpu.common.xzsOld.base;

import com.jdpu.common.xzsOld.utils.ModelMapperSingle;
import org.modelmapper.ModelMapper;

/**
 * api父类
 * @author zuck
 */
public class BaseApiController {
    /**
     * 页默认大小
     */
    protected final static String DEFAULT_PAGE_SIZE = "10";
    /**
     * modelMapper
     */
    protected final static ModelMapper modelMapper = ModelMapperSingle.Instance();

}
