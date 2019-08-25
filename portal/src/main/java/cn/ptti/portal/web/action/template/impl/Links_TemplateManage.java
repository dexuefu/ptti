package cn.ptti.portal.web.action.template.impl;

import cn.ptti.portal.bean.links.Links;
import cn.ptti.portal.bean.template.Forum;
import cn.ptti.portal.service.links.LinksService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 友情链接 -- 模板方法实现
 *
 */
@Component("links_TemplateManage")
public class Links_TemplateManage {
	@Resource LinksService linksService; 
	/**
	 * 友情链接列表 -- 集合
	 * @param forum
	 */
	public List<Links> links_collection(Forum forum,Map<String,Object> parameter,Map<String,Object> runtimeParameter){
		List<Links> linksList = linksService.findAllLinks_cache();
		return linksList;
	}
}
