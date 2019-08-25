package cn.ptti.portal.web.action.help;

import cn.ptti.portal.bean.PageForm;
import cn.ptti.portal.bean.PageView;
import cn.ptti.portal.bean.QueryResult;
import cn.ptti.portal.bean.help.Help;
import cn.ptti.portal.bean.help.HelpType;
import cn.ptti.portal.service.help.HelpService;
import cn.ptti.portal.service.help.HelpTypeService;
import cn.ptti.portal.service.setting.SettingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 帮助管理
 *
 */
@Controller
public class HelpAction {
	@Resource HelpService helpService;
	@Resource HelpTypeService helpTypeService;
	@Resource SettingService settingService;
	
	@RequestMapping("/control/help/list") 
	public String execute(PageForm pageForm,ModelMap model,Boolean visible,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		StringBuffer jpql = new StringBuffer("");
		//存放参数值
		List<Object> params = new ArrayList<Object>();

		if(visible != null && visible == false){
			jpql.append(" and o.visible=?"+ (params.size()+1));
			params.add(false);
		}else{
			jpql.append(" and o.visible=?"+ (params.size()+1));
			params.add(true);
		}
		//删除第一个and
		String _jpql = org.apache.commons.lang3.StringUtils.difference(" and", jpql.toString());
		
		PageView<Help> pageView = new PageView<Help>(settingService.findSystemSetting_cache().getBackstagePageNumber(),pageForm.getPage(),10);
		//当前页
		int firstindex = (pageForm.getPage()-1)*pageView.getMaxresult();;	
		//排序
		LinkedHashMap<String,String> orderby = new LinkedHashMap<String,String>();
		
		orderby.put("id", "desc");//根据typeid字段降序排序
		
		
		//调用分页算法类
		QueryResult<Help> qr = helpService.getScrollData(Help.class, firstindex, pageView.getMaxresult(), _jpql, params.toArray(),orderby);
	

		pageView.setQueryResult(qr);
		
		List<Long> helpTypeIdList = new ArrayList<Long>();
		
		if(qr.getResultlist() != null && qr.getResultlist().size() >0){
			for(Help help :pageView.getRecords()){
				helpTypeIdList.add(help.getHelpTypeId());
			}
			//查询分类名称
			List<HelpType> helpTypeList = helpTypeService.findByIdList(helpTypeIdList);
			if(helpTypeList != null && helpTypeList.size() >0){
				for(Help help :pageView.getRecords()){
					for(HelpType helpType : helpTypeList){
						if(help.getHelpTypeId().equals(helpType.getId())){
							help.setHelpTypeName(helpType.getName());
							break;
						}
					}
				}
			}
			
		}
		
		
		
		model.addAttribute("pageView", pageView);

		return "jsp/help/helpList";
	}
	
	
	
}