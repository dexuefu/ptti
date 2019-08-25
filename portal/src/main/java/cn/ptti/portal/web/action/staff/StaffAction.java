package cn.ptti.portal.web.action.staff;

import cn.ptti.portal.bean.PageForm;
import cn.ptti.portal.bean.PageView;
import cn.ptti.portal.bean.QueryResult;
import cn.ptti.portal.bean.staff.SysUsers;
import cn.ptti.portal.service.setting.SettingService;
import cn.ptti.portal.service.staff.ACLService;
import cn.ptti.portal.service.staff.StaffService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 员工列表
 * @author Administrator
 *
 */
@Controller
public class StaffAction {
	@Resource StaffService staffService;//通过接口引用代理返回的对象
	@Resource ACLService aclService;
	@Resource SettingService settingService;
	
	@RequestMapping("/control/staff/list") 
	public String staff(PageForm pageForm,ModelMap model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		//调用分页算法代码
		PageView<SysUsers> pageView = new PageView<SysUsers>(settingService.findSystemSetting_cache().getBackstagePageNumber(),pageForm.getPage(),10);
		//当前页
		int firstindex = (pageForm.getPage()-1)*pageView.getMaxresult();
		//排序
		LinkedHashMap<String,String> orderby = new LinkedHashMap<String,String>();
		orderby.put("id", "desc");//根据id字段降序排序
		QueryResult<SysUsers> qr = staffService.getScrollData(SysUsers.class,firstindex, pageView.getMaxresult());
		//将查询结果集传给分页List
		pageView.setQueryResult(qr);
		
		List<SysUsers> sysUsersList = qr.getResultlist();
		if(sysUsersList != null && sysUsersList.size() >0){
			List<String> userAccountList = new ArrayList<String>();
			for(SysUsers sysUsers : sysUsersList){
				userAccountList.add(sysUsers.getUserAccount());
			}
			
			Map<String,List<String>> rolesNameMap = aclService.findRolesByUserAccount(userAccountList);
			model.addAttribute("rolesNameMap", rolesNameMap);
		}
		model.addAttribute("pageView", pageView);
		return "jsp/staff/staffList";
	}
	@RequestMapping("/control/staff/roles/list") 
	public String roles(PageForm pageForm,ModelMap model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	
		//调用分页算法代码
		PageView<SysUsers> pageView = new PageView<SysUsers>(settingService.findSystemSetting_cache().getBackstagePageNumber(),pageForm.getPage(),10);
		//当前页
		int firstindex = (pageForm.getPage()-1)*pageView.getMaxresult();

		QueryResult<SysUsers> qr = staffService.getScrollData(SysUsers.class,firstindex, pageView.getMaxresult());
		//将查询结果集传给分页List
		pageView.setQueryResult(qr);
		model.addAttribute("pageView", pageView);
		return "jsp/staff/rolesList";
	}
}
