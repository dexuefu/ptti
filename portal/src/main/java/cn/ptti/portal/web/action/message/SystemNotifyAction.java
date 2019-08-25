package cn.ptti.portal.web.action.message;


import cn.ptti.portal.bean.PageForm;
import cn.ptti.portal.bean.PageView;
import cn.ptti.portal.bean.QueryResult;
import cn.ptti.portal.bean.message.SystemNotify;
import cn.ptti.portal.service.message.SystemNotifyService;
import cn.ptti.portal.service.setting.SettingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;

/**
 * 系统通知
 *
 */
@Controller
public class SystemNotifyAction {
	@Resource SettingService settingService;
	@Resource SystemNotifyService systemNotifyService;
	
	@RequestMapping("/control/systemNotify/list") 
	public String execute(PageForm pageForm,ModelMap model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	
		PageView<SystemNotify> pageView = new PageView<SystemNotify>(settingService.findSystemSetting_cache().getBackstagePageNumber(),pageForm.getPage(),10);
		//当前页
		int firstindex = (pageForm.getPage()-1)*pageView.getMaxresult();;	
		//排序
		LinkedHashMap<String,String> orderby = new LinkedHashMap<String,String>();
		
		orderby.put("sendTime", "desc");//根据sendTime字段降序排序
		
			
		//调用分页算法类
		QueryResult<SystemNotify> qr = systemNotifyService.getScrollData(SystemNotify.class, firstindex, pageView.getMaxresult(), orderby);		
		
		pageView.setQueryResult(qr);
		model.addAttribute("pageView", pageView);
		
		return "jsp/message/systemNotifyList";
	}
}
