package com.jhr.dao;

import com.jhr.entity.Stylesal;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StylesalMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Stylesal record);

    Stylesal selectByPrimaryKey(Long id);

    List<Stylesal> selectAll();

    int updateByPrimaryKey(Stylesal record);
}