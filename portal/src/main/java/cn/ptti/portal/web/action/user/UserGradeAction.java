package cn.ptti.portal.web.action.user;

import cn.ptti.portal.bean.PageForm;
import cn.ptti.portal.bean.user.UserGrade;
import cn.ptti.portal.service.user.UserGradeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *  等级
 *
 */
@Controller
public class UserGradeAction {
	//注入业务bean
	@Resource UserGradeService userGradeService;
	
	/**
	 * 用户等级
	 * @param formbean
	 * @param pageForm
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/control/userGrade/list") 
	public String execute(PageForm pageForm,ModelMap model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {	
		List<UserGrade> userGradeList = userGradeService.findAllGrade();
		model.addAttribute("userGradeList", userGradeList);
		return "jsp/user/userGradeList";
	}
}
