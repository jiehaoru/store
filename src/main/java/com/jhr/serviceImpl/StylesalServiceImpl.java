package com.jhr.serviceImpl;

import com.jhr.dao.StylesalMapper;
import com.jhr.entity.Stylesal;
import com.jhr.service.StylesalService;
import com.jhr.service.StylewarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <br>
 * 标题：中间表
 * 款式 - 出售
 * 描述：
 *
 * @author jhr
 * @create 2019/03/10 20:59
 */
@Service
public class StylesalServiceImpl implements StylesalService {
    @Autowired
    private StylesalMapper stylesalMapper;
    @Override
    public int insert(Stylesal stylesal) {
        return stylesalMapper.insert(stylesal);
    }

    @Override
    public int deleteBy(Stylesal stylesal) {
        //防止 where 没有条件值
        if (null==stylesal.getId()&& null==stylesal.getSaleid()&&null==stylesal.getStyleid()){
            return -1;
        }
        return stylesalMapper.deleteBy(stylesal);
    }
}
