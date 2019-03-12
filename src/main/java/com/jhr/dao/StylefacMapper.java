package com.jhr.dao;

import com.jhr.entity.Stylefac;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface StylefacMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Stylefac record);

    Stylefac selectByPrimaryKey(Long id);

    List<Stylefac> selectAll();

    int updateByPrimaryKey(Stylefac record);

    int deleteBy(Stylefac stylefac);
}