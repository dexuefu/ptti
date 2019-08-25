package cn.ptti.portal.web.action.staff;

import cn.ptti.portal.bean.PageForm;
import cn.ptti.portal.bean.PageView;
import cn.ptti.portal.bean.QueryResult;
import cn.ptti.portal.bean.staff.SysRoles;
import cn.ptti.portal.service.setting.SettingService;
import cn.ptti.portal.service.staff.ACLService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ACL列表
 * @author Administrator
 *
 */
@Controller
public class ACLAction {
	@Resource ACLService aclService;//通过接口引用代理返回的对象
	
	@Resource SettingService settingService;

	/**
	 * 角色列表
	 * @param pageForm
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/control/roles/list") 
	public String roles(PageForm pageForm,ModelMap model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		//调用分页算法代码
		PageView<SysRoles> pageView = new PageView<SysRoles>(settingService.findSystemSetting_cache().getBackstagePageNumber(),pageForm.getPage(),10);
		//当前页
		int firstindex = (pageForm.getPage()-1)*pageView.getMaxresult();
		//排序
		QueryResult<SysRoles> qr = aclService.getScrollData(SysRoles.class,firstindex, pageView.getMaxresult());
		//将查询结果集传给分页List
		pageView.setQueryResult(qr);
		model.addAttribute("pageView", pageView);
		return "jsp/staff/rolesList";
	}
}
