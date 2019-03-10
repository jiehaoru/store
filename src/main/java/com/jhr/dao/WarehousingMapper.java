package com.jhr.dao;

import com.jhr.entity.Warehousing;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface WarehousingMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Warehousing record);

    Warehousing selectByPrimaryKey(Long id);

    List<Warehousing> selectAll();

    int updateByPrimaryKey(Warehousing record);
}