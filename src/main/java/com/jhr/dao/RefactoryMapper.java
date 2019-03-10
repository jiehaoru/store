package com.jhr.dao;

import com.jhr.entity.Refactory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RefactoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Refactory record);

    Refactory selectByPrimaryKey(Long id);

    List<Refactory> selectAll();

    int updateByPrimaryKey(Refactory record);
}