package com.jhr.dao;

import com.jhr.entity.Style;
import java.util.List;

public interface StyleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Style record);

    Style selectByPrimaryKey(Long id);

    List<Style> selectAll();

    int updateByPrimaryKey(Style record);
}