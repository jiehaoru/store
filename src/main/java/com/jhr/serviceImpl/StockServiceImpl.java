package com.jhr.serviceImpl;

import com.jhr.entity.Stock;
import com.jhr.service.StockService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <br>
 * 标题：
 * 描述：
 *
 * @author jhr
 * @create 2019/03/10 12:25
 */
@Service
public class StockServiceImpl implements StockService {
    @Override
    public int insertStock(Stock stock) {
        return 0;
    }

    @Override
    public List<Stock> selectStockListBy(Stock stock) {
        return null;
    }

    @Override
    public int updateByPrimaryKey(Stock stock) {
        return 0;
    }

    @Override
    public int deleteByPrimaryKey(Long key) {
        return 0;
    }
}
