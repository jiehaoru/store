package com.jhr.serviceImpl;

import com.jhr.dao.StylestoMapper;
import com.jhr.entity.Stylesto;
import com.jhr.service.StylestoService;
import com.jhr.utils.Sequence;
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
//        stylesto.setId(Sequence.getInstance().nextId());
        return stylestoMapper.insert(stylesto);
    }

    @Override
    public int deleteBy(Stylesto stylesto) {
        //防止 where 没有条件值
        if (null==stylesto.getId()&& null==stylesto.getStockid()&&null==stylesto.getStyleid()){
            return -1;
        }
        return stylestoMapper.deleteBy(stylesto);
    }
}
