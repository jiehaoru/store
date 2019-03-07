package com.jhr.dao;

import com.jhr.entity.Stylesal;
import java.util.List;

public interface StylesalMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Stylesal record);

    Stylesal selectByPrimaryKey(Long id);

    List<Stylesal> selectAll();

    int updateByPrimaryKey(Stylesal record);
}