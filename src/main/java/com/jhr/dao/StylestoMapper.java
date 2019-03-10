package com.jhr.dao;

import com.jhr.entity.Stylesto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface StylestoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Stylesto record);

    Stylesto selectByPrimaryKey(Long id);

    List<Stylesto> selectAll();

    int updateByPrimaryKey(Stylesto record);
}