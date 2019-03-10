package com.jhr.service;

import com.jhr.entity.Stylesal;

/**
 * <br>
 * 标题： 中间表
 * 款式 - 出售
 * 描述：
 *
 * @author jhr
 * @create 2019/03/10 20:59
 */

public interface StylesalService {
    int insert(Stylesal stylesal);

    int deleteBy(Stylesal stylesal);
}
