package com.jhr.serviceImpl;

import com.jhr.entity.Sale;
import com.jhr.service.SaleService;
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
    @Override
    public List<Sale> selectSaleListBy(Sale sale) {
        return null;
    }

    @Override
    public int updateByPrimaryKey(Sale sale) {
        return 0;
    }

    @Override
    public int deleteByPrimaryKey(Long key) {
        return 0;
    }

    @Override
    public int insertSale(Sale sale) {
        return 0;
    }
}
