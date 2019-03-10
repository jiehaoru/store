package com.jhr.service;

import com.jhr.entity.Stylewar;

/**
 * <br>
 * 标题：中间表
 * 款式 - 入库
 * 描述：
 *
 * @author jhr
 * @create 2019/03/10 16:38
 */
public interface StylewarService {
    int insert(Stylewar stylewar);

    //非空条件删除
    int deleteBy(Stylewar stylewar);
}
