package com.jhr.serviceImpl;

import com.jhr.dao.SaleMapper;
import com.jhr.entity.Sale;
import com.jhr.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <br>
 * 标题：出售
 * 描述：
 *
 * @author jhr
 * @create 2019/03/10 20:47
 */
@Service
public class SaleServiceImpl implements SaleService {
    @Autowired
    private SaleMapper saleMapper;
    @Override
    public List<Sale> selectSaleListBy(Sale sale) {
        return saleMapper.selectSaleListBy(sale);
    }

    @Override
    public int updateByPrimaryKey(Sale sale) {
        return saleMapper.updateByPrimaryKey(sale);
    }

    @Override
    public int deleteByPrimaryKey(Long key) {
        return saleMapper.deleteByPrimaryKey(key);
    }

    @Override
    public int insertSale(Sale sale) {
        return saleMapper.insert(sale);
    }
}
