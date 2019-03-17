package com.jhr.serviceImpl;

import com.jhr.dao.StylefunMapper;
import com.jhr.entity.Stylefun;
import com.jhr.service.StylefunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <br>
 * 标题：
 * 描述：
 *
 * @author jhr
 * @create 2019/03/10 21:29
 */
@Service
public class StylefunServiceImpl implements StylefunService {
    @Autowired
    private StylefunMapper stylefunMapper;

    @Override
    public int insert(Stylefun stylefun) {
        return stylefunMapper.insert(stylefun);
    }

    @Override
    public int deleteBy(Stylefun stylefun) {
        //防止 where 没有条件值
        if (null==stylefun.getId()&& null==stylefun.getRefundsid()&&null==stylefun.getStyleid()){
            return -1;
        }
        return stylefunMapper.deleteBy(stylefun);
    }
}
