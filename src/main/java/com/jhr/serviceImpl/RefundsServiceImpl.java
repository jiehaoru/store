package com.jhr.serviceImpl;

import com.jhr.entity.Refunds;
import com.jhr.service.RefundsService;
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
    @Override
    public int insertRefunds(Refunds refunds) {
        return 0;
    }

    @Override
    public List<Refunds> selectRefundsListBy(Refunds refunds) {
        return null;
    }

    @Override
    public int updateByPrimaryKey(Refunds refunds) {
        return 0;
    }

    @Override
    public int deleteByPrimaryKey(Long key) {
        return 0;
    }
}
