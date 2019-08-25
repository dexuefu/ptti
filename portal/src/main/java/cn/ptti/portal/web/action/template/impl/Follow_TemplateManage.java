package cn.ptti.portal.web.action.template.impl;

import cn.ptti.portal.bean.follow.Follow;
import cn.ptti.portal.bean.template.Forum;
import cn.ptti.portal.bean.user.AccessUser;
import cn.ptti.portal.bean.user.User;
import cn.ptti.portal.service.follow.FollowService;
import cn.ptti.portal.web.action.follow.FollowManage;
import cn.ptti.portal.web.action.follow.FollowerManage;
import cn.ptti.portal.web.action.user.UserManage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 关注 -- 模板方法实现
 *
 */
@Component("follow_TemplateManage")
public class Follow_TemplateManage {
	@Resource FollowService followService; 
	@Resource FollowManage followManage;
	@Resource FollowerManage followerManage;
	@Resource UserManage userManage;
	/**
	 * 粉丝总数  -- 实体对象
	 * @param forum 版块对象
	 * @param parameter 参数
	 */
	public Long followerCount_entityBean(Forum forum,Map<String,Object> parameter,Map<String,Object> runtimeParameter){	
		String userName = null;
		//获取参数
		if(parameter != null && parameter.size() >0){		
			for(Map.Entry<String,Object> paramIter : parameter.entrySet()) {
				if("userName".equals(paramIter.getKey())){
					userName = paramIter.getValue().toString();
				}
			}
		}
		if(userName != null && !"".equals(userName.trim())){
			User user = userManage.query_cache_findUserByUserName(userName.trim());
			if(user != null){
				return followerManage.query_cache_followerCount(user.getId(), user.getUserName());
			}
		}
		return 0L;
	}
	
	/**
	 * 是否已经关注该用户  -- 实体对象
	 * @param forum 版块对象
	 * @param parameter 参数
	 */
	public Boolean following_entityBean(Forum forum,Map<String,Object> parameter,Map<String,Object> runtimeParameter){	
		String userName = null;
		//获取参数
		if(parameter != null && parameter.size() >0){		
			for(Map.Entry<String,Object> paramIter : parameter.entrySet()) {
				if("userName".equals(paramIter.getKey())){
					userName = paramIter.getValue().toString();
				}
			}
		}
		
		AccessUser accessUser = null;
		//获取运行时参数
		if(runtimeParameter != null && runtimeParameter.size() >0){		
			for(Map.Entry<String,Object> paramIter : runtimeParameter.entrySet()) {
				if("accessUser".equals(paramIter.getKey())){
					accessUser = (AccessUser)paramIter.getValue();
				}
			}
		}
		if(accessUser != null && userName != null && !"".equals(userName.trim())){
			User user = userManage.query_cache_findUserByUserName(userName.trim());
			if(user != null){
				//生成关注Id
				String followId = followManage.createFollowId(accessUser.getUserId(),user.getId());
				Follow follow = followManage.query_cache_findById(followId);
				if(follow != null){
		  			return true;
			  	}
			}
		}
		return false;
	}
	
	
	/**
	 * 关注用户
	 * @param forum
	 */
	public Map<String,Object> addFollow_collection(Forum forum,Map<String,Object> parameter,Map<String,Object> runtimeParameter){
		Map<String,Object> value = new HashMap<String,Object>();
		
		return value;
	}
}
