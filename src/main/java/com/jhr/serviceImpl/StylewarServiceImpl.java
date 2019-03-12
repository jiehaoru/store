package com.jhr.serviceImpl;

import com.jhr.dao.StylewarMapper;
import com.jhr.entity.Stylewar;
import com.jhr.service.StylewarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <br>
 * 标题：中间表
 * 款式 - 入库
 * * 描述：
 *
 * @author jhr
 * @create 2019/03/10 16:43
 */
@Service
public class StylewarServiceImpl implements StylewarService {
    @Autowired
    private StylewarMapper stylewarMapper;
    @Override
    public int insert(Stylewar stylewar) {
        return stylewarMapper.insert(stylewar);
    }

    @Override
    public int deleteBy(Stylewar stylewar) {
        return stylewarMapper.deleteBy(stylewar);
    }
}
