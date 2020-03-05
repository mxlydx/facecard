package cc.gooa.facecard.service.impl;

import cc.gooa.facecard.mapper.BusCreditLogMapper;
import cc.gooa.facecard.model.BusCreditLog;
import cc.gooa.facecard.service.BusCreditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusCreditLogServiceImpl implements BusCreditLogService {

    @Autowired
    private BusCreditLogMapper busCreditLogMapper;

    @Override
    public int insert(BusCreditLog bean) {
       return busCreditLogMapper.insertSelective(bean);
    }
}
