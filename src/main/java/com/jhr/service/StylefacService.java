package com.jhr.service;

import com.jhr.entity.Stylefac;

/**
 * <br>
 * 标题：中间表
 * 款式 - 返厂
 * 描述：
 *
 * @author jhr
 * @create 2019/03/10 17:41
 */
public interface StylefacService {

    int insert(Stylefac stylefac);
    //通过主键删除
    int deleteBy(Stylefac stylefac);
}
