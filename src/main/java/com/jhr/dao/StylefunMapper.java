package com.jhr.dao;

import com.jhr.entity.Stylefun;
import java.util.List;

public interface StylefunMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Stylefun record);

    Stylefun selectByPrimaryKey(Long id);

    List<Stylefun> selectAll();

    int updateByPrimaryKey(Stylefun record);
}