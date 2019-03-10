package com.jhr.service;

import com.jhr.entity.Refunds;

import java.util.List;

/**
 * <br>
 * 标题：退货表
 * * 描述：
 *
 * @author jhr
 * @create 2019/03/10 21:26
 */
public interface RefundsService {
    int insertRefunds(Refunds refunds);

    List<Refunds> selectRefundsListBy(Refunds refunds);

    int updateByPrimaryKey(Refunds refunds);

    int deleteByPrimaryKey(Long key);
}
