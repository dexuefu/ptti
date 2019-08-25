package cn.ptti.portal.web.action.favorite;


import cn.ptti.portal.bean.PageForm;
import cn.ptti.portal.bean.PageView;
import cn.ptti.portal.bean.QueryResult;
import cn.ptti.portal.bean.favorite.Favorites;
import cn.ptti.portal.bean.topic.Topic;
import cn.ptti.portal.service.favorite.FavoriteService;
import cn.ptti.portal.service.setting.SettingService;
import cn.ptti.portal.service.topic.TopicService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 收藏夹
 *
 */
@Controller
public class FavoriteAction {
	@Resource FavoriteService favoriteService;
	@Resource SettingService settingService;
	@Resource TopicService topicService;
	/**
	 * 收藏夹列表
	 * @param model
	 * @param pageForm
	 * @param id 用户Id
	 * @param userName 用户名称
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/control/favorite/list") 
	public String favoriteList(ModelMap model,PageForm pageForm,Long id,String userName,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if(userName != null && !"".equals(userName.trim())){
			
			//调用分页算法代码
			PageView<Favorites> pageView = new PageView<Favorites>(settingService.findSystemSetting_cache().getBackstagePageNumber(),pageForm.getPage(),10,request.getRequestURI(),request.getQueryString());
			//当前页
			int firstIndex = (pageForm.getPage()-1)*pageView.getMaxresult();
			
			QueryResult<Favorites> qr = favoriteService.findFavoriteByUserId(id,userName,firstIndex,pageView.getMaxresult());
			if(qr != null && qr.getResultlist() != null && qr.getResultlist().size() >0){
				List<Long> topicIdList = new ArrayList<Long>();
				for(Favorites favorites : qr.getResultlist()){
					topicIdList.add(favorites.getTopicId());
				}
				List<Topic> topicList = topicService.findByIdList(topicIdList);
				if(topicList != null && topicList.size() >0){
					for(Favorites favorites : qr.getResultlist()){
						for(Topic topic : topicList){
							if(favorites.getTopicId().equals(topic.getId())){
								favorites.setTopicTitle(topic.getTitle());
								break;
							}
						}
					}
				}
			}
			//将查询结果集传给分页List
			pageView.setQueryResult(qr);
			model.addAttribute("pageView", pageView);
		}
		return "jsp/favorite/favoriteList";
	}
	
	
	/**
	 * 话题收藏列表
	 * @param model
	 * @param pageForm
	 * @param topicId 话题Id
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/control/topicFavorite/list") 
	public String dynamicImageFavoriteList(ModelMap model,PageForm pageForm,Long topicId,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if(topicId != null && topicId >0L){
			
			//调用分页算法代码
			PageView<Favorites> pageView = new PageView<Favorites>(settingService.findSystemSetting_cache().getBackstagePageNumber(),pageForm.getPage(),10,request.getRequestURI(),request.getQueryString());
			//当前页
			int firstIndex = (pageForm.getPage()-1)*pageView.getMaxresult();
			
			QueryResult<Favorites> qr = favoriteService.findFavoritePageByTopicId(firstIndex,pageView.getMaxresult(),topicId);
			if(qr != null && qr.getResultlist() != null && qr.getResultlist().size() >0){
				List<Long> topicIdList = new ArrayList<Long>();
				for(Favorites favorites : qr.getResultlist()){
					topicIdList.add(favorites.getTopicId());
				}
				List<Topic> topicList = topicService.findByIdList(topicIdList);
				if(topicList != null && topicList.size() >0){
					for(Favorites favorites : qr.getResultlist()){
						for(Topic topic : topicList){
							if(favorites.getTopicId().equals(topic.getId())){
								favorites.setTopicTitle(topic.getTitle());
								break;
							}
						}
					}
				}
			}
			//将查询结果集传给分页List
			pageView.setQueryResult(qr);
			model.addAttribute("pageView", pageView);
		}
		return "jsp/favorite/topicFavoriteList";
	}
}
