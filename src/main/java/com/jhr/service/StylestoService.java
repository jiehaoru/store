package com.jhr.service;

import com.jhr.entity.Stylesto;

/**
 * <br>
 * 标题：中间表
 * 款式 - 库存
 * 描述：
 *
 * @author jhr
 * @create 2019/03/10 16:07
 */
public interface StylestoService {
    //插入
    int insert(Stylesto stylesto);
    //通过条件删除
    int deleteBy(Stylesto stylesto);
}
