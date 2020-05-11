package cc.gooa.facecard.service;

import cc.gooa.facecard.model.BusDevices;

public interface BusDevicesService {
    BusDevices selectByCode(String code);
    int updateStatusByCode(BusDevices record);
}
