package com.jhr.serviceImpl;

import com.jhr.dao.StylefacMapper;
import com.jhr.entity.Stylefac;
import com.jhr.service.StylefacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <br>
 * 标题：中间表
 * 款式 - 返厂
 * 描述：
 *
 * @author jhr
 * @create 2019/03/10 17:42
 */
@Service
public class StylefacServiceImpl implements StylefacService {
    @Autowired
    private StylefacMapper stylefacMapper;
    @Override
    public int insert(Stylefac stylefac) {
        return stylefacMapper.insert(stylefac);
    }

    @Override
    public int deleteBy(Stylefac stylefac) {

        //防止 where 没有条件值
        if (null==stylefac.getId()&& null==stylefac.getRefactoryid()&&null==stylefac.getStyleid()){
            return -1;
        }
        return stylefacMapper.deleteBy(stylefac);
    }
}
