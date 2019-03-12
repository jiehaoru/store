package com.jhr.serviceImpl;

import com.jhr.dao.StockMapper;
import com.jhr.entity.Stock;
import com.jhr.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private StockMapper stockMapper;
    @Override
    public int insertStock(Stock stock) {
        return stockMapper.insert(stock);
    }

    @Override
    public List<Stock> selectStockListBy(Stock stock) {
        return stockMapper.selectStockListBy(stock);
    }

    @Override
    public int updateByPrimaryKey(Stock stock) {
        return stockMapper.updateByPrimaryKey(stock);
    }

    @Override
    public int deleteByPrimaryKey(Long key) {
        return stockMapper.deleteByPrimaryKey(key);
    }
}
