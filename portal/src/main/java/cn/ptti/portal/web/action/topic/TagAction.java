package cn.ptti.portal.web.action.topic;


import cn.ptti.portal.bean.topic.Tag;
import cn.ptti.portal.service.setting.SettingService;
import cn.ptti.portal.service.topic.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 标签
 *
 */
@Controller
public class TagAction {
	@Resource TagService tagService;
	@Resource SettingService settingService;
	
	@RequestMapping("/control/tag/list") 
	public String execute(ModelMap model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List<Tag> tagList = tagService.findAllTag();
		model.addAttribute("tagList", tagList);
		return "jsp/topic/tagList";
	}
}
