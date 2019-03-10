package com.jhr.service;

import com.jhr.entity.Stock;

import java.util.List;

/**
 * <br>
 * 标题： 库存
 * 描述：
 *
 * @author jhr
 * @create 2019/03/10 12:22
 */
public interface StockService {
    //插入库存数据
    int insertStock(Stock stock);

    //动态查询, 判断属性是否为空,不为空就加入where条件中
    List<Stock> selectStockListBy(Stock stock);

    // 通过主键修改
    int updateByPrimaryKey(Stock stock);

    //通过主键物理删除
    int deleteByPrimaryKey(Long key);
}
