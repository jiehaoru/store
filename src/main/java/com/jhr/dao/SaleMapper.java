package com.jhr.dao;

import com.jhr.entity.Sale;
import java.util.List;

public interface SaleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Sale record);

    Sale selectByPrimaryKey(Long id);

    List<Sale> selectAll();

    int updateByPrimaryKey(Sale record);
}