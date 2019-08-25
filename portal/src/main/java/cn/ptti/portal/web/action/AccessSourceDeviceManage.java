package cn.ptti.portal.web.action;

import cn.ptti.portal.bean.setting.SystemSetting;
import cn.ptti.portal.service.setting.SettingService;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 访问来源设备
 *
 */
@Component("accessSourceDeviceManage")
public class AccessSourceDeviceManage {
	
	@Resource SettingService settingService;
	
	private final DeviceResolver deviceResolver = new LiteDeviceResolver();  
	

	/**
	 * 访问设备
	 * @param request
	 * @return pc:电脑 wap:移动设备
	 */
	public String accessDevices(HttpServletRequest request) {
		SystemSetting systemSetting = settingService.findSystemSetting_cache();
		if(systemSetting.getSupportAccessDevice() != null){
			if(systemSetting.getSupportAccessDevice().equals(2)){
				return "pc";//电脑端
			}else if(systemSetting.getSupportAccessDevice().equals(3)){
				return "wap";//移动设备
			}
		}

		Device device = deviceResolver.resolveDevice(request);
		if(device.isNormal()){
			return "pc";//电脑
		}else{
			return "wap";//移动设备
		}
	}
	

}
