package cn.ptti.portal.web.action.setting;

import cn.ptti.portal.bean.setting.EditorTag;
import cn.ptti.portal.bean.setting.SystemSetting;
import cn.ptti.portal.service.setting.SettingService;
import cn.ptti.portal.service.statistic.PageViewService;
import cn.ptti.portal.service.user.UserService;
import cn.ptti.portal.utils.JsonUtils;
import cn.ptti.portal.web.action.lucene.TopicIndexManage;
import cn.ptti.portal.web.action.user.UserLoginLogManage;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 设置管理
 *
 */
@Component("settingManage")
public class SettingManage {

	@Resource TopicIndexManage topicIndexManage;
	@Resource UserLoginLogManage userLoginLogManage;
	@Resource SettingService settingService;
	
	@Resource UserService userService;
	@Resource PageViewService pageViewService;
	
	/**
	 * 增加 用户每分钟提交次数
	 * @param module 模块
	 * @param userName 用户名称
	 * @param count 次数
	 * @return
	 */
	@CachePut(value="settingManage_cache_submitQuantity",key="#module + '_' + #userName")
	public Integer addSubmitQuantity(String module,String userName,Integer count){
		return count;
	}
	/**
	 * 查询 用户每分钟提交次数
	 * @param module 模块
	 * @param userName 用户名称
	 * @return
	 */
	@Cacheable(value="settingManage_cache_submitQuantity",key="#module + '_' + #userName")
	public Integer getSubmitQuantity(String module,String userName){
		return null;
	}
	/**
	 * 删除 用户每分钟提交次数
	 * @param module 模块
	 * @param userName 用户名称
	 * @return
	*/
	@CacheEvict(value="settingManage_cache_submitQuantity",key="#module + '_' + #userName")
	public void deleteSubmitQuantity(String module,String userName){
	}
	
	
	/**
	 * 添加全部话题索引(异步)
	 */
	@Async
	public void addAllTopicIndex(){
		topicIndexManage.addAllTopicIndex();
	}
	/**
	 * 删除浏览量数据 (异步)
	 * @param endTime 结束时间
	 */
	@Async
	public void executeDeletePageViewData(Date endTime){
		pageViewService.deletePageView(endTime);
	}
	
	/**
	 * 删除用户登录日志数据 (异步)
	 * @param endTime 结束时间
	 */
	@Async
	public void executeDeleteUserLoginLogData(Date endTime){
		userService.deleteUserLoginLog(endTime);
	}
	
	/**
	 * 读取话题编辑器允许使用标签
	 * @return
	 */
	public EditorTag readTopicEditorTag(){
		SystemSetting systemSetting = settingService.findSystemSetting_cache();
		if(systemSetting.getTopicEditorTag() != null && !"".equals(systemSetting.getTopicEditorTag().trim())){
			return JsonUtils.toObject(systemSetting.getTopicEditorTag(), EditorTag.class);
		}
		return null;
	}
	
	/**
	 * 读取评论编辑器允许使用标签
	 * @return
	 */
	public EditorTag readEditorTag(){
		SystemSetting systemSetting = settingService.findSystemSetting_cache();
		if(systemSetting.getEditorTag() != null && !"".equals(systemSetting.getEditorTag().trim())){
			return JsonUtils.toObject(systemSetting.getEditorTag(), EditorTag.class);
		}
		return null;
	}
	
	

	
	/**  
     * 总内存  
     *   
     * @return  
     */  
    public long totalMemory() {   
        long l = Runtime.getRuntime().totalMemory();   
        return (l / 1024 / 1024);   
    }   
  
    /**  
     * 分配最大内存  
     *   
     * @return  
     */  
    public long maxMemory() {   
        long l = Runtime.getRuntime().maxMemory();   
        return (l / 1024 / 1024);   
    }   
  
    /**  
     * 空闲内存 
     * @return  
     */  
    public long freeMemory() {   
        long l = Runtime.getRuntime().freeMemory();   
        return (l / 1024 / 1024);   
    }   
	
}
