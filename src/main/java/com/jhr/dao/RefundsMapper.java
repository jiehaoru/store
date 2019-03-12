package com.jhr.dao;

import com.jhr.entity.Refunds;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface RefundsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Refunds record);

    Refunds selectByPrimaryKey(Long id);

    List<Refunds> selectAll();

    int updateByPrimaryKey(Refunds record);

    List<Refunds> selectRefundsListBy(Refunds refunds);
}