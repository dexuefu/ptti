package cn.ptti.portal.web.action.template.impl;

import cn.ptti.portal.bean.template.Forum;
import cn.ptti.portal.bean.topic.Tag;
import cn.ptti.portal.service.topic.TagService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 标签 -- 模板方法实现
 *
 */
@Component("tag_TemplateManage")
public class Tag_TemplateManage {
	@Resource TagService tagService; 
	/**
	 * 标签列表 -- 集合
	 * @param forum
	 */
	public List<Tag> tag_collection(Forum forum,Map<String,Object> parameter,Map<String,Object> runtimeParameter){
		List<Tag> tagList = tagService.findAllTag_cache();
		return tagList;
	}
}
