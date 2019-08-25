package cn.ptti.portal.web.action.template.impl;

import cn.ptti.portal.bean.favorite.TopicFavorite;
import cn.ptti.portal.bean.template.Forum;
import cn.ptti.portal.bean.user.AccessUser;
import cn.ptti.portal.service.favorite.FavoriteService;
import cn.ptti.portal.utils.Verification;
import cn.ptti.portal.web.action.favorite.FavoriteManage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 收藏夹 -- 模板方法实现
 *
 */
@Component("favorite_TemplateManage")
public class Favorite_TemplateManage {
	@Resource FavoriteService favoriteService; 
	@Resource FavoriteManage favoriteManage;
	
	/**
	 * 话题用户收藏总数  -- 实体对象
	 * @param forum 版块对象
	 * @param parameter 参数
	 */
	public Long favoriteCount_entityBean(Forum forum,Map<String,Object> parameter,Map<String,Object> runtimeParameter){	
		Long topicId = null;
		//获取参数
		if(parameter != null && parameter.size() >0){		
			for(Map.Entry<String,Object> paramIter : parameter.entrySet()) {
				if("topicId".equals(paramIter.getKey())){
					if(Verification.isNumeric(paramIter.getValue().toString())){
						if(paramIter.getValue().toString().length() <=18){
							topicId = Long.parseLong(paramIter.getValue().toString());	
						}
					}
				}
			}
		}
		if(topicId != null){
			
			//根据话题Id查询被收藏数量
			return favoriteManage.query_cache_findFavoriteCountByTopicId(topicId);
		}else{
			return 0L;
		}
	}
	
	/**
	 * 用户是否已经收藏话题  -- 实体对象
	 * @param forum 版块对象
	 * @param parameter 参数
	 */
	public Boolean alreadyCollected_entityBean(Forum forum,Map<String,Object> parameter,Map<String,Object> runtimeParameter){	
		Long topicId = null;
		//获取参数
		if(parameter != null && parameter.size() >0){		
			for(Map.Entry<String,Object> paramIter : parameter.entrySet()) {
				if("topicId".equals(paramIter.getKey())){
					if(Verification.isNumeric(paramIter.getValue().toString())){
						if(paramIter.getValue().toString().length() <=18){
							topicId = Long.parseLong(paramIter.getValue().toString());	
						}
					}
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
		if(accessUser != null && topicId != null){
			//话题收藏Id
		  	String topicFavoriteId = favoriteManage.createTopicFavoriteId(topicId, accessUser.getUserId());
		  
		  	TopicFavorite topicFavorite = favoriteManage.query_cache_findTopicFavoriteById(topicFavoriteId);
	  		
	  		if(topicFavorite != null){
	  			return true;
		  	}
			
			
		}
		return false;
	}
	
	
	/**
	 * 加入收藏夹
	 * @param forum
	 */
	public Map<String,Object> addFavorite_collection(Forum forum,Map<String,Object> parameter,Map<String,Object> runtimeParameter){
		Map<String,Object> value = new HashMap<String,Object>();
		
		return value;
	}
}
