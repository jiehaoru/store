package com.jhr.dao;

import java.util.List;
import com.jhr.entity.Style;

public interface StyleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Style record);

    Style selectByPrimaryKey(Long id);

    List<Style> selectAll();

    int updateByPrimaryKey(Style record);

    List<Style> selectStyleBy(Style style);
}