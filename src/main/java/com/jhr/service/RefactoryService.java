package com.jhr.service;

import com.jhr.entity.Refactory;

import java.util.List;

/**
 * <br>
 * 标题：返厂表
 * * 描述：
 *
 * @author jhr
 * @create 2019/03/10 17:31
 */
public interface RefactoryService {
    //插入 返厂条目
    int insertRefactory(Refactory refactory);

    //非空条件查询
    List<Refactory> selectRefactoryListBy(Refactory refactory);

    //根据主键查询
    Refactory selectRefactoryListByPrimaryKey(Refactory req);

    //通过主键修改
    int updateByPrimaryKey(Refactory refactory);

    //通过主键删除
    int deleteByPrimaryKey(Long key);
}
