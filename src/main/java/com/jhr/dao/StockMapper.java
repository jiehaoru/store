package com.jhr.dao;

import com.jhr.entity.Stock;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface StockMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Stock record);

    Stock selectByPrimaryKey(Long id);

    List<Stock> selectAll();

    int updateByPrimaryKey(Stock record);

    List<Stock> selectStockListBy(Stock stock);
}