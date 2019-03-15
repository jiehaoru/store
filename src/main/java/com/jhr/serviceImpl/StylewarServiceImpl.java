package com.jhr.serviceImpl;

import com.jhr.dao.StylewarMapper;
import com.jhr.entity.Stylewar;
import com.jhr.service.StylewarService;
import com.jhr.utils.Sequence;
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
public class StylewarServiceImpl implements StylewarService  {
    @Autowired
    private StylewarMapper stylewarMapper;
    @Override
    public int insert(Stylewar stylewar) {
        stylewar.setId(Sequence.getInstance().nextId());
        return stylewarMapper.insert(stylewar);
    }

    @Override
    public int deleteBy(Stylewar stylewar) {

        //防止 where 没有条件值
        if (null==stylewar.getId()&& null==stylewar.getWarehousingid()&&null==stylewar.getStyleid()){
               return -1;
        }
        return stylewarMapper.deleteBy(stylewar);
    }
}
