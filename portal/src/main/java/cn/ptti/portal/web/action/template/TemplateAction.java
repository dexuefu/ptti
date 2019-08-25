package cn.ptti.portal.web.action.template;

import cn.ptti.portal.bean.template.Templates;
import cn.ptti.portal.service.template.TemplateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 模板目录显示
 *
 */
@Controller
public class TemplateAction {
	//注入业务bean
	@Resource(name="templateServiceBean")
	private TemplateService templateService;//通过接口引用代理返回的对象
	
	
	
	@RequestMapping("/control/template/list") 
	public String execute(ModelMap model,HttpServletRequest request)
			throws Exception {

		List<Templates> templatesDir =  templateService.findAllTemplates();

		model.addAttribute("templatesDir", templatesDir);
		return "jsp/template/templateList";
	}
}
