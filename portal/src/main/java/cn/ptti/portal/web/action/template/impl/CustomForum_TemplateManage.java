package cn.ptti.portal.web.action.template.impl;

import cn.ptti.portal.bean.template.CustomHTML;
import cn.ptti.portal.bean.template.Forum;
import cn.ptti.portal.bean.template.Forum_CustomForumRelated_CustomHTML;
import cn.ptti.portal.utils.JsonUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 自定义版块 -- 模板方法实现
 *
 */
@Component("customForum_TemplateManage")
public class CustomForum_TemplateManage {
	
	/**
	 * 自定义HTML -- 实体对象
	 * @param forum
	 */
	public CustomHTML customHTML_entityBean(Forum forum,Map<String,Object> parameter,Map<String,Object> runtimeParameter){
		String formValueJSON = forum.getFormValue();//表单值
		if(formValueJSON != null && !"".equals(formValueJSON)){
			
			Forum_CustomForumRelated_CustomHTML forum_CustomForumRelated_CustomHTML = JsonUtils.toObject(formValueJSON, Forum_CustomForumRelated_CustomHTML.class);
			
			
			if(forum_CustomForumRelated_CustomHTML != null){
				CustomHTML customHTML = new CustomHTML();
				customHTML.setForumTitle(forum.getName());
				customHTML.setContent(forum_CustomForumRelated_CustomHTML.getHtml_content());
				return customHTML;
			}
		}
		return null;
	}
}
