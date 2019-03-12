package com.jhr.serviceImpl;

import com.jhr.dao.WarehousingMapper;
import com.jhr.entity.Warehousing;
import com.jhr.service.WarehousingService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private WarehousingMapper warehousingMapper;
    @Override
    public int insertWarehousing(Warehousing warehousing) {
        return warehousingMapper.insert(warehousing);
    }

    @Override
    public List<Warehousing> selectWarehousingListBy(Warehousing warehousing) {
        return warehousingMapper.selectWarehousingListBy(warehousing);
    }

    @Override
    public Warehousing selectWarehousingListByPrimaryKey(Warehousing req) {
        List<Warehousing> warehousings = warehousingMapper.selectWarehousingListBy(req);
        Warehousing warehousing=null;
        if (warehousings != null) {
            warehousing=warehousings.get(0);
        }
        return warehousing;
    }

    @Override
    public int updateByPrimaryKey(Warehousing warehousing) {
        return warehousingMapper.updateByPrimaryKey(warehousing);
    }

    @Override
    public int deleteByPrimaryKey(Long key) {
        return warehousingMapper.deleteByPrimaryKey(key);
    }
}
