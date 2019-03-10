package com.jhr.service;

import com.jhr.entity.Warehousing;

import java.util.List;

/**
 * <br>
 * 标题：入库
 * 描述：
 *
 * @author jhr
 * @create 2019/03/10 16:28
 */
public interface WarehousingService {
    //入库表插入
    int insertWarehousing(Warehousing warehousing);

    //通过非空条件查询
    List<Warehousing> selectWarehousingListBy(Warehousing warehousing);

    //通过主键查询
    Warehousing selectWarehousingListByPrimaryKey(Warehousing req);

    //通过主键修改
    int updateByPrimaryKey(Warehousing warehousing);

    //通过主键删除
    int deleteByPrimaryKey(Long key);
}
