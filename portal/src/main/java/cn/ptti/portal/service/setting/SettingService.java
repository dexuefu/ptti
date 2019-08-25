package cn.ptti.portal.service.setting;

import cn.ptti.portal.bean.setting.SystemSetting;
import cn.ptti.portal.service.besa.DAO;

/**
 * 系统设置
 * @author Administrator
 *
 */
public interface SettingService extends DAO<SystemSetting>{
	/**
	 * 查询系统设置
	 * @return
	 **/
	public SystemSetting findSystemSetting();
	/**
	 * 查询系统设置 - 缓存
	 * @return
	 */
	public SystemSetting findSystemSetting_cache();
	
	/**
	 * 修改系统设置
	 * @param systemSetting
	 * @return
	 */
	public void updateSystemSetting(SystemSetting systemSetting);
	
	
}
