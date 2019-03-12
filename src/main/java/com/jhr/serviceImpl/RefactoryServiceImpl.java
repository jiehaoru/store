package com.jhr.serviceImpl;

import com.jhr.dao.RefactoryMapper;
import com.jhr.entity.Refactory;
import com.jhr.service.RefactoryService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private RefactoryMapper refactoryMapper;

    @Override
    public int insertRefactory(Refactory refactory) {
        return refactoryMapper.insert(refactory);
    }

    @Override
    public List<Refactory> selectRefactoryListBy(Refactory refactory) {
        return refactoryMapper.selectRefactoryListBy(refactory);
    }

    @Override
    public Refactory selectRefactoryListByPrimaryKey(Refactory req) {

        return refactoryMapper.selectByPrimaryKey(req.getId());
    }

    @Override
    public int updateByPrimaryKey(Refactory refactory) {
        return refactoryMapper.updateByPrimaryKey(refactory);
    }

    @Override
    public int deleteByPrimaryKey(Long key) {
        return refactoryMapper.deleteByPrimaryKey(key);
    }
}
