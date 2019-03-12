package com.jhr.dao;

import com.jhr.entity.Stylefun;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface StylefunMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Stylefun record);

    Stylefun selectByPrimaryKey(Long id);

    List<Stylefun> selectAll();

    int updateByPrimaryKey(Stylefun record);

    int deleteBy(Stylefun stylefun);
}