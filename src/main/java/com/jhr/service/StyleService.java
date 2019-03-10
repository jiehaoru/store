package com.jhr.service;

import com.jhr.entity.Style;
import com.jhr.utils.base.BaseRsp;

import java.util.List;

/**
 * <br>
 * 标题： 款式表
 * 描述：
 *
 * @author jhr
 * @create 2019/03/07 10:11
 */
public interface StyleService {

    //插入
    public int insertStyle(Style style) throws Exception;

    //查询全部
    List<Style> selectStyleList();

    //通过ID查询
    Style selectStyleByPrimaryKey(Long id);

//    //通过 款式名查询
////    List<Style> selectStyleBy(String styleName);
////
////    //通过 自定义编号查询
////    Style selectStyleBynumstr(String numstr);

    //通过条件动态查询
    List<Style> selectStyleBy(Style style);

    //通过主键修改
    int updateByPrimaryKey(Style style);

    //通过主键删除
    int deleteByPrimaryKey(Long id);

    //查询有效状态的(首页表格只显示有效的)
    List<Style> selectStyleListByFlag(int flag);
}