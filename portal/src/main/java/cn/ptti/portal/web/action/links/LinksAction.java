package cn.ptti.portal.web.action.links;


import cn.ptti.portal.bean.links.Links;
import cn.ptti.portal.service.links.LinksService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 友情链接
 *
 */
@Controller
public class LinksAction {
	@Resource LinksService linksService;
	
	@RequestMapping("/control/links/list") 
	public String execute(ModelMap model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List<Links> linksList = linksService.findAllLinks();
		model.addAttribute("linksList", linksList);
		return "jsp/links/linksList";
	}
}
