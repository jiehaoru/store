package com.jhr.serviceImpl;

import com.jhr.entity.Refactory;
import com.jhr.service.RefactoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <br>
 * 标题：返厂表
 * 描述：
 *
 * @author jhr
 * @create 2019/03/10 17:32
 */
@Service
public class RefactoryServiceImpl implements RefactoryService {
    @Override
    public int insertRefactory(Refactory refactory) {
        return 0;
    }

    @Override
    public List<Refactory> selectRefactoryListBy(Refactory refactory) {
        return null;
    }

    @Override
    public Refactory selectRefactoryListByPrimaryKey(Refactory req) {
        return null;
    }

    @Override
    public int updateByPrimaryKey(Refactory refactory) {
        return 0;
    }

    @Override
    public int deleteByPrimaryKey(Long key) {
        return 0;
    }
}
