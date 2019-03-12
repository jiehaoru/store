package com.jhr.serviceImpl;

import com.jhr.dao.RefundsMapper;
import com.jhr.entity.Refunds;
import com.jhr.service.RefundsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <br>
 * 标题：退货表
 * 描述：
 *
 * @author jhr
 * @create 2019/03/10 21:27
 */
@Service
public class RefundsServiceImpl implements RefundsService {
    @Autowired
    private RefundsMapper refundsMapper;

    @Override
    public int insertRefunds(Refunds refunds) {
        return refundsMapper.insert(refunds);
    }

    @Override
    public List<Refunds> selectRefundsListBy(Refunds refunds) {
        return refundsMapper.selectRefundsListBy(refunds);
    }

    @Override
    public int updateByPrimaryKey(Refunds refunds) {
        return refundsMapper.updateByPrimaryKey(refunds);
    }

    @Override
    public int deleteByPrimaryKey(Long key) {
        return refundsMapper.deleteByPrimaryKey(key);
    }
}
