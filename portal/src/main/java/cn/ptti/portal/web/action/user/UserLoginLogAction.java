package cn.ptti.portal.web.action.user;


import cn.ptti.portal.bean.PageForm;
import cn.ptti.portal.bean.PageView;
import cn.ptti.portal.bean.QueryResult;
import cn.ptti.portal.bean.user.UserLoginLog;
import cn.ptti.portal.service.setting.SettingService;
import cn.ptti.portal.service.user.UserService;
import cn.ptti.portal.utils.IpAddress;
import cn.ptti.portal.web.action.SystemException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户登录日志
 *
 */
@Controller
public class UserLoginLogAction {
	
	@Resource UserService userService;
	@Resource SettingService settingService;
	/**
	 * 用户登录日志列表
	 * @param userId 用户Id
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/control/userLoginLog/list") 
	public String execute(ModelMap model,Long id,PageForm pageForm,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {	
		//调用分页算法代码
		PageView<UserLoginLog> pageView = new PageView<UserLoginLog>(settingService.findSystemSetting_cache().getBackstagePageNumber(),pageForm.getPage(),10);
		//当前页
		int firstIndex = (pageForm.getPage()-1)*pageView.getMaxresult();;	
		
		if(id != null && id >0L){

			QueryResult<UserLoginLog> qr = userService.findUserLoginLogPage(id, firstIndex, pageView.getMaxresult());
			
			if(qr != null && qr.getResultlist() != null && qr.getResultlist().size() >0){
				for(UserLoginLog userLoginLog : qr.getResultlist()){
					if(userLoginLog.getIp() != null && !"".equals(userLoginLog.getIp().trim())){
						userLoginLog.setIpAddress(IpAddress.queryAddress(userLoginLog.getIp()));
					}
				}
			}
			
			//将查询结果集传给分页List
			pageView.setQueryResult(qr);	
		}else{//如果接收到所属用户为空
			throw new SystemException("参数错误！");
		}
		model.addAttribute("pageView", pageView);

		return "jsp/user/loginLogList";
	}
}
