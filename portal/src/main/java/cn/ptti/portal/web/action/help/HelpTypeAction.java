package cn.ptti.portal.web.action.help;


import cn.ptti.portal.bean.PageForm;
import cn.ptti.portal.bean.PageView;
import cn.ptti.portal.bean.QueryResult;
import cn.ptti.portal.bean.help.HelpType;
import cn.ptti.portal.service.help.HelpTypeService;
import cn.ptti.portal.service.setting.SettingService;
import org.apache.commons.lang3.StringUtils;
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
 * 资讯分类
 *
 */
@Controller
public class HelpTypeAction {
	@Resource HelpTypeService helpTypeService;
	@Resource SettingService settingService;
	
	@RequestMapping("/control/helpType/list") 
	public String execute(PageForm pageForm,ModelMap model,Long parentId,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		StringBuffer jpql = new StringBuffer("");
		//存放参数值
		List<Object> params = new ArrayList<Object>();
		

		//如果所属父类有值
		if(parentId != null && parentId >0L){
				jpql.append(" and o.parentId=?"+ (params.size()+1));//所属父类的ID;(params.size()+1)是为了和下面的条件参数兼容
			params.add(parentId);//设置o.parentId=?2参数
		}else{//如果没有父类
		//	jpql.append(" and o.parent is null");
			jpql.append(" and o.parentId=?"+ (params.size()+1));
			params.add(0L);
		}
		
		PageView<HelpType> pageView = new PageView<HelpType>(settingService.findSystemSetting_cache().getBackstagePageNumber(),pageForm.getPage(),10);
		//当前页
		int firstindex = (pageForm.getPage()-1)*pageView.getMaxresult();;	
		//排序
		LinkedHashMap<String,String> orderby = new LinkedHashMap<String,String>();
		
		orderby.put("sort", "desc");//根据typeid字段降序排序
		
		
		//删除第一个and
		String jpql_str = StringUtils.difference(" and", jpql.toString());
		
		//调用分页算法类
		QueryResult<HelpType> qr = helpTypeService.getScrollData(HelpType.class, firstindex, pageView.getMaxresult(), jpql_str, params.toArray(),orderby);		
		
		pageView.setQueryResult(qr);
		model.addAttribute("pageView", pageView);
		
		
		//分类导航
		if(parentId != null && parentId >0L){
			Map<Long,String> navigation = new LinkedHashMap<Long,String>();
			HelpType helpType = helpTypeService.findById(parentId);
			if(helpType != null){
				List<HelpType> parentHelpTypeList = helpTypeService.findAllParentById(helpType);
				for(HelpType p : parentHelpTypeList){
					navigation.put(p.getId(), p.getName());
				}
				navigation.put(helpType.getId(), helpType.getName());
				model.addAttribute("navigation", navigation);//分类导航
			}
			
		}
		
		return "jsp/help/helpTypeList";
	}
}