package com.jhr.serviceImpl;

import com.jhr.entity.Warehousing;
import com.jhr.service.WarehousingService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <br>
 * 标题：入库
 * 描述：
 *
 * @author jhr
 * @create 2019/03/10 16:28
 */
@Service
public class WarehousingServiceImpl implements WarehousingService {
    @Override
    public int insertWarehousing(Warehousing warehousing) {
        return 0;
    }

    @Override
    public List<Warehousing> selectWarehousingListBy(Warehousing warehousing) {
        return null;
    }

    @Override
    public Warehousing selectWarehousingListByPrimaryKey(Warehousing req) {
        return null;
    }

    @Override
    public int updateByPrimaryKey(Warehousing warehousing) {
        return 0;
    }

    @Override
    public int deleteByPrimaryKey(Long key) {
        return 0;
    }
}
