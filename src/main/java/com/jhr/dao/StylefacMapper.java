package com.jhr.dao;

import com.jhr.entity.Stylefac;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StylefacMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Stylefac record);

    Stylefac selectByPrimaryKey(Long id);

    List<Stylefac> selectAll();

    int updateByPrimaryKey(Stylefac record);
}