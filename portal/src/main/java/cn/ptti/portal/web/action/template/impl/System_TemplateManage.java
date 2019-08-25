package cn.ptti.portal.web.action.template.impl;

import cn.ptti.portal.bean.template.Forum;
import cn.ptti.portal.bean.template.Forum_SystemRelated_SearchWord;
import cn.ptti.portal.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 系统部分 -- 模板方法实现
 *
 */
@Component("system_TemplateManage")
public class System_TemplateManage {
	
	/**
	 * 热门搜索词
	 * @param forum
	 */
	public List<String> searchWord_collection(Forum forum,Map<String,Object> parameter,Map<String,Object> runtimeParameter){
		List<String> searchWordList = new ArrayList<String>();

		String formValueJSON = forum.getFormValue();//表单值
		if(formValueJSON != null && !"".equals(formValueJSON)){
			Forum_SystemRelated_SearchWord forum_SystemRelated_SearchWord = JsonUtils.toGenericObject(formValueJSON, new TypeReference<Forum_SystemRelated_SearchWord>(){});
			if(forum_SystemRelated_SearchWord != null){
				if(forum_SystemRelated_SearchWord.getSearchWordList() != null && forum_SystemRelated_SearchWord.getSearchWordList().size() >0){
					searchWordList.addAll(forum_SystemRelated_SearchWord.getSearchWordList());
				}
			}
		}
		return searchWordList;
	}
	
}

