package cn.ptti.portal.web.action.user;


import cn.ptti.portal.bean.PageForm;
import cn.ptti.portal.bean.PageView;
import cn.ptti.portal.bean.QueryResult;
import cn.ptti.portal.bean.user.DisableUserName;
import cn.ptti.portal.service.setting.SettingService;
import cn.ptti.portal.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;


/**
 * 禁止的用户名称 分页显示
 *
 */
@Controller
public class DisableUserNameAction {
	//注入业务bean
	@Resource(name="userServiceBean")
	private UserService userService;
	@Resource SettingService settingService;
	/**
	 * 禁止的用户名称
	 * @param formbean
	 * @param pageForm
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/control/disableUserName/list") 
	public String execute(PageForm pageForm,ModelMap model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {	
	
		//调用分页算法代码
		PageView<DisableUserName> pageView = new PageView<DisableUserName>(settingService.findSystemSetting_cache().getBackstagePageNumber(),pageForm.getPage(),10);
		//当前页
		int firstindex = (pageForm.getPage()-1)*pageView.getMaxresult();;	
		//排序
		LinkedHashMap<String,String> orderby = new LinkedHashMap<String,String>();
		
		orderby.put("id", "desc");//根据sort字段降序排序
		QueryResult<DisableUserName> qr = userService.getScrollData(DisableUserName.class,firstindex, pageView.getMaxresult(),orderby);
		//将查询结果集传给分页List
		pageView.setQueryResult(qr);
		request.setAttribute("pageView", pageView);
		
		return "jsp/user/disableUserNameList";
	}
	
	
}
