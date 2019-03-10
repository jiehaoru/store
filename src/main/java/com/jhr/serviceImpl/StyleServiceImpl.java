package com.jhr.serviceImpl;

import com.jhr.dao.StyleMapper;
import com.jhr.entity.Style;
import com.jhr.service.StyleService;
import com.jhr.utils.base.BaseRsp;

import com.jhr.utils.base.BaseRspConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <br>
 * 标题：
 * 描述：
 *
 * @author jhr
 * @create 2019/03/07 14:32
 */
@Service
public class StyleServiceImpl implements StyleService {

    private static final Logger logger=LoggerFactory.getLogger(StyleServiceImpl.class);

    @Autowired
    private StyleMapper styleMapper;

    @Override
    public int insertStyle(Style style) throws Exception {

       return styleMapper.insert(style);
    }

    @Override
    public List<Style> selectStyleList() {
        return null;
    }

    @Override
    public Style selectStyleByPrimaryKey(Long id) {
        return null;
    }

    @Override
    public List<Style> selectStyleBy(Style style) {
        return null;
    }


    @Override
    public int updateByPrimaryKey(Style style) {
        return 0;
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return 0;
    }

    @Override
    public List<Style> selectStyleListByFlag(int flag) {
        return null;
    }
}