package com.jhr.serviceImpl;

import com.jhr.dao.StylestoMapper;
import com.jhr.entity.Stylesto;
import com.jhr.service.StylestoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <br>
 * 标题：
 * 描述：
 *
 * @author jhr
 * @create 2019/03/10 16:18
 */
@Service
public class StylestoServiceImpl implements StylestoService {
    @Autowired
    private StylestoMapper stylestoMapper;
    @Override
    public int insert(Stylesto stylesto) {
        return stylestoMapper.insert(stylesto);
    }

    @Override
    public int deleteBy(Stylesto stylesto) {
        return stylestoMapper.deleteBy(stylesto);
    }
}
