package cc.gooa.facecard.service.impl;

import cc.gooa.facecard.mapper.BusDevicesMapper;
import cc.gooa.facecard.model.BusDevices;
import cc.gooa.facecard.service.BusDevicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusDevicesServiceImpl implements BusDevicesService {
    @Autowired
    private BusDevicesMapper busDevicesMapper;

    @Override
    public BusDevices selectByCode(String code) {
        return busDevicesMapper.selectByCode(code);
    }

    @Override
    public int updateStatusByCode(BusDevices record) {
        return busDevicesMapper.updateStatusByCode(record);
    }
}
