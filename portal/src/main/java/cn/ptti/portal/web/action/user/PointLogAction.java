package cn.ptti.portal.web.action.user;

import cn.ptti.portal.bean.PageForm;
import cn.ptti.portal.bean.PageView;
import cn.ptti.portal.bean.QueryResult;
import cn.ptti.portal.bean.user.PointLog;
import cn.ptti.portal.bean.user.User;
import cn.ptti.portal.service.setting.SettingService;
import cn.ptti.portal.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 积分日志管理
 *
 */
@Controller
public class PointLogAction{

	@Resource UserService userService;
	@Resource SettingService settingService;
	
	
	@RequestMapping("/control/pointLog/list")  
	public String execute(ModelMap model, PageForm pageForm,String userName,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {	
		//调用分页算法代码
		PageView<PointLog> pageView = new PageView<PointLog>(settingService.findSystemSetting_cache().getBackstagePageNumber(),pageForm.getPage(),10);
		//当前页
		int firstIndex = (pageForm.getPage()-1)*pageView.getMaxresult();
		
		if(userName != null && !"".equals(userName.trim())){
			User user = userService.findUserByUserName(userName);
			if(user != null){
				QueryResult<PointLog> qr =  userService.findPointLogPage(user.getId(),user.getUserName(),firstIndex, pageView.getMaxresult());
				
				//将查询结果集传给分页List
				pageView.setQueryResult(qr);
				request.setAttribute("pageView", pageView);
			}
			
		}
		
		

		return "jsp/user/pointLogList";
	}
	
}

