package com.jhr.dao;

import com.jhr.entity.Sale;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface SaleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Sale record);

    Sale selectByPrimaryKey(Long id);

    List<Sale> selectAll();

    int updateByPrimaryKey(Sale record);
}