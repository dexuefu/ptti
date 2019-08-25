package cn.ptti.portal.web.action.sms;

import cn.ptti.portal.bean.PageForm;
import cn.ptti.portal.bean.PageView;
import cn.ptti.portal.bean.QueryResult;
import cn.ptti.portal.bean.sms.SmsInterface;
import cn.ptti.portal.service.setting.SettingService;
import cn.ptti.portal.service.sms.SmsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;

@Controller
public class SmsAction {
	//注入业务bean
	@Resource SmsService smsService;//通过接口引用代理返回的对象
	@Resource SettingService settingService;
		
	/**
	 * 短信接口列表
	 * @param model
	 * @param pageForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/control/smsInterface/list")  
	public String execute(ModelMap model, PageForm pageForm,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {	
		
		//调用分页算法代码
		PageView<SmsInterface> pageView = new PageView<SmsInterface>(settingService.findSystemSetting_cache().getBackstagePageNumber(),pageForm.getPage(),10);
		//当前页
		int firstindex = (pageForm.getPage()-1)*pageView.getMaxresult();;	
		//排序
		LinkedHashMap<String,String> orderby = new LinkedHashMap<String,String>();
		
		orderby.put("sort", "desc");//根据sort字段降序排序
		QueryResult<SmsInterface> qr = smsService.getScrollData(SmsInterface.class,firstindex, pageView.getMaxresult(),orderby);
		//将查询结果集传给分页List
		pageView.setQueryResult(qr);
		request.setAttribute("pageView", pageView);
		
		return "jsp/sms/smsInterfaceList";
	}
}
