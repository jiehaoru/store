package com.jhr.dao;

import com.jhr.entity.Style;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StyleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Style record);

    Style selectByPrimaryKey(Long id);

    List<Style> selectAll();

    int updateByPrimaryKey(Style record);
}