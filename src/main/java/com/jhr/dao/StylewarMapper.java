package com.jhr.dao;

import com.jhr.entity.Stylewar;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StylewarMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Stylewar record);

    Stylewar selectByPrimaryKey(Long id);

    List<Stylewar> selectAll();

    int updateByPrimaryKey(Stylewar record);
}