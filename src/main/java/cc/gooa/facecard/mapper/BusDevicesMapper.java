package cc.gooa.facecard.mapper;

import cc.gooa.facecard.model.BusDevices;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BusDevicesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BusDevices record);

    int insertSelective(BusDevices record);

    BusDevices selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BusDevices record);

    int updateByPrimaryKey(BusDevices record);

    int updateStatusByCode(BusDevices record);

    BusDevices selectByCode(String deviceId);
}