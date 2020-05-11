package cc.gooa.facecard.service.impl;

import cc.gooa.facecard.base.RedisKey;
import cc.gooa.facecard.mapper.BusDevicesMapper;
import cc.gooa.facecard.model.BusDevices;
import cc.gooa.facecard.service.BusDevicesService;
import cc.gooa.facecard.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tone.cache.liver.impl.SampleLiverCache;


/**
 * 上下线缓存对象
 * @author zlf
 */
@Component
public class OnOffCache extends SampleLiverCache<String, Object> {
	Logger logger = LoggerFactory.getLogger(this.getClass());;
	@Autowired
	BusDevicesService busDevicesService;

	public OnOffCache() {
		// 90s心跳超时
		super( 30 * 1000);
	}

	@Override
	protected void afterRemove(String key, Property<Object> property) {
		// update
		String deviceId = property.getExtra().toString();
		BusDevices busDevices = new BusDevices();
		busDevices.setCode(deviceId);
		busDevices.setStatus(1);
		try {
			busDevicesService.updateStatusByCode(busDevices);
			RedisUtil.setValue(RedisKey.CACHE_DEVICE_ONLINE + deviceId, null);
		} catch (Exception e) {
			logger.warn("machineOnOff machine off error", e);
		}
	}

}