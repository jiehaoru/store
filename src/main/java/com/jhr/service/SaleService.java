package com.jhr.service;

import com.jhr.entity.Sale;

import java.util.List;

/**
 * <br>
 * 标题： 出售表
 * 描述：
 *
 * @author jhr
 * @create 2019/03/10 20:46
 */
public interface SaleService {
    // 非空的条件查询
    List<Sale> selectSaleListBy(Sale sale);

    int updateByPrimaryKey(Sale sale);

    int deleteByPrimaryKey(Long key);

    int insertSale(Sale sale);
}
