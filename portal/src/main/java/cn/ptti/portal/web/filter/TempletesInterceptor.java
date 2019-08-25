package cn.ptti.portal.web.filter;


import cn.ptti.portal.bean.setting.SystemSetting;
import cn.ptti.portal.bean.user.AccessUser;
import cn.ptti.portal.bean.user.RefreshUser;
import cn.ptti.portal.bean.user.UserState;
import cn.ptti.portal.service.setting.SettingService;
import cn.ptti.portal.service.template.TemplateService;
import cn.ptti.portal.service.user.UserService;
import cn.ptti.portal.utils.IpAddress;
import cn.ptti.portal.utils.SpringConfigTool;
import cn.ptti.portal.utils.UUIDUtil;
import cn.ptti.portal.utils.WebUtil;
import cn.ptti.portal.utils.threadLocal.AccessUserThreadLocal;
import cn.ptti.portal.utils.threadLocal.CSRFTokenThreadLocal;
import cn.ptti.portal.utils.threadLocal.TemplateThreadLocal;
import cn.ptti.portal.web.action.AccessSourceDeviceManage;
import cn.ptti.portal.web.action.CSRFTokenManage;
import cn.ptti.portal.web.action.common.OAuthManage;
import cn.ptti.portal.web.action.statistic.PageViewManage;
import cn.ptti.portal.web.action.template.TemplateMain;
import cn.ptti.portal.web.action.user.UserManage;
import cn.ptti.portal.web.taglib.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.queryString.util.MultiMap;
import org.queryString.util.UrlEncoded;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 模板拦截器(将模板显示功能加入指定的页面中)
 *
 */
public class TempletesInterceptor extends HandlerInterceptorAdapter {
	@Resource OAuthManage oAuthManage;
	@Resource TemplateService templateService;
	
	@Resource SettingService settingService;
	
	@Resource AccessSourceDeviceManage accessSourceDeviceManage;

	@Resource CSRFTokenManage csrfTokenManage;
	@Resource PageViewManage pageViewManage;
	
	@Resource UserService userService;
	@Resource UserManage userManage;
	//?  匹配任何单字符
	//*  匹配0或者任意数量的字符
	//** 匹配0或者更多的目录
	//后台URL
	private AntPathRequestMatcher[] backstage_filterMatchers = {
	    new AntPathRequestMatcher("/control/**"),
	    new AntPathRequestMatcher("/admin/**"),
	    new AntPathRequestMatcher("/captcha/**"),//生成验证码
	    new AntPathRequestMatcher("/checkCaptcha")//校验验证码
	};
	
	
	
	/**
	 * preHandle()方法在业务处理器处理请求之前被调用 
	 */
	  
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, 
			Object handler) throws Exception { 
	//System.out.println(request.getRequestURI()+" -- "+request.getQueryString());
		
		
		//设置自定义标签的URL
		if(request != null){
			if(Configuration.getPath() == null || "".equals(Configuration.getPath())){
				Configuration.setPath(request.getContextPath());
			}
			//添加sessionId
	    	TemplateThreadLocal.addRuntimeParameter("sessionId", request.getSession().getId());
	    	
	    	//Cookies
	    	TemplateThreadLocal.addRuntimeParameter("cookies", request.getCookies());
	    
	    	//URI
	    	TemplateThreadLocal.addRuntimeParameter("requestURI", request.getRequestURI());
	    	
	    	//URL参数
	    	TemplateThreadLocal.addRuntimeParameter("queryString", request.getQueryString());
	    	
	    	//IP
	    	TemplateThreadLocal.addRuntimeParameter("ip", IpAddress.getClientIpAddress(request));
	    	
	    	//获取登录用户(user/开头的URL才有值)
		  	AccessUser accessUser = AccessUserThreadLocal.get();
	    	if(accessUser != null){
	    		
	    		TemplateThreadLocal.addRuntimeParameter("accessUser", accessUser);
	    	}else{
	    		//获取登录用户
	    		AccessUser _accessUser = oAuthManage.getUserName(request);
	    		if(_accessUser != null){
	    			UserState userState = userManage.query_userState(_accessUser.getUserName().trim());//用户状态
	    			if(userState != null && userState.getSecurityDigest().equals(_accessUser.getSecurityDigest())){//验证安全摘要
	    				TemplateThreadLocal.addRuntimeParameter("accessUser", _accessUser );
		    			AccessUserThreadLocal.set(_accessUser);
	    			}
    			}	
	    	}
		}
		//设置令牌
		csrfTokenManage.setToken(request,response);

		SystemSetting systemSetting = settingService.findSystemSetting_cache();
		
		if(systemSetting.getCloseSite().equals(3)){//3.全站关闭
			boolean backstage_flag = false;
			//后台URL
			for (AntPathRequestMatcher rm : backstage_filterMatchers) {
				if (rm.matches(request)) { 
					backstage_flag = true;
				}
			}
			if(backstage_flag == false){
				String baseURI = Configuration.baseURI(request.getRequestURI(), request.getContextPath());
				//删除后缀
				baseURI = StringUtils.substringBeforeLast(baseURI, ".");
				if(!baseURI.equalsIgnoreCase("message")){
					response.sendRedirect(Configuration.getUrl(request)+"message");
					return false;
				}
			}
		}
		return true;   
	} 
	 /**
	  * postHandle()方法在业务处理器处理请求之后被调用
	  */
	public void postHandle(HttpServletRequest request, HttpServletResponse response, 
			 Object handler, ModelAndView modelAndView)throws Exception {  

		//获取登录用户
	  	AccessUser accessUser = AccessUserThreadLocal.get();
		if(accessUser == null){
			//为非user开头URI的登录用户令牌续期,因为user开头的URI由LoginFilter拦截
			String baseURI = Configuration.baseURI(request.getRequestURI(), request.getContextPath());
			if(!StringUtils.startsWithIgnoreCase(baseURI, "user/")){//如果不是user/开头
				String accessToken = WebUtil.getCookieByName(request, "cn.ptti.portal_accessToken");
				String refreshToken = WebUtil.getCookieByName(request, "cn.ptti.portal_refreshToken");
				if(accessToken != null && !"".equals(accessToken.trim()) && refreshToken != null && !"".equals(refreshToken.trim())){
					RefreshUser refreshUser = oAuthManage.getRefreshUserByRefreshToken(refreshToken.trim());
					if(refreshUser != null && accessToken.equals(refreshUser.getAccessToken())){
						
						//令牌续期
						oAuthManage.tokenRenewal(refreshToken,refreshUser,request,response);
						accessUser = AccessUserThreadLocal.get();
						
					}
				}
			}
		}
		
		String mav = "";
		if(modelAndView != null){ 
			mav = modelAndView.getViewName();
		}
		
	    //如果视图方法返回值为templates开头,就将模板显示功能加入页面中	
		if(mav != null && !"".equals(mav)){
		//	if(mav.matches("^templates/.*$")){
			if(mav != null && !"".equals(mav) && mav.length() >10){
				String begin = mav.substring(0, 9);//取得templates
				if("templates".equals(begin)){//如果以templates开头

					//统计访问量
					pageViewManage.addPV(request);
	
	    			//获取URL参数
			       	String queryString = request.getQueryString();
			       	if(queryString != null && !"".equals(queryString)){
			       		MultiMap<String> values = new MultiMap<String>();  
				       	UrlEncoded.decodeTo(queryString, values, "UTF-8");
				       	Iterator iter = values.entrySet().iterator();  
				       	while(iter.hasNext()){  
				       		Map.Entry e = (Map.Entry)iter.next();  
				       		if(e.getValue() != null){
				       			if(e.getValue() instanceof List){
				       				List<String> valueList = (List)e.getValue();
					       			if(valueList.size() >0){
					       				for(String value :valueList){
					       					if(value != null && !"".equals(value.trim())){
					       						request.setAttribute("url_"+e.getKey(),value.trim());//分页
					       						break;
					       					}
					       				}
						       		}
					       		}
				       		}
				       	} 
			       	}
	    			TemplateMain templateMain = (TemplateMain)SpringConfigTool.getContext().getBean("templateMain");
    		    	
	    			//取得请求页面名称
    		    	String requestName = StringUtils.substringAfterLast(mav,"/");//返回最后一个指定字符串之后的所有字符 

    		    	//添加到模板参数线程变量
    		    	TemplateThreadLocal.setLayoutFile(requestName+".html");
    		    	
    		    	
    		    	
    		    	
    		    	String dirName = templateService.findTemplateDir_cache();	

    		    	SystemSetting systemSetting = settingService.findSystemSetting_cache();
    		    	String csrf_token = csrfTokenManage.getToken(request);
    		    	if(csrf_token == null || "".equals(csrf_token.trim())){//如果第一次打开时还没设置Cookie
    		    		csrf_token = CSRFTokenThreadLocal.get();
    		    	}
    		    	
    		    	//执行自定义标签		
					request.setAttribute("baseURL", Configuration.getUrl(request));//系统路径
					request.setAttribute("commonPath", "common/"+dirName+"/"+accessSourceDeviceManage.accessDevices(request)+"/");//资源路径
	    			request.setAttribute("contextPath",request.getContextPath());//系统虚拟目录
	    			request.setAttribute("suffix",Configuration.getSuffix());//取得系统后缀名
	    			request.setAttribute("templateDir",dirName);//模板目录名称
	    			request.setAttribute("title",systemSetting.getTitle());//站点名称
	    			request.setAttribute("keywords",systemSetting.getKeywords());//站点关键词
	    			request.setAttribute("description",systemSetting.getDescription());//站点描述
	    			request.setAttribute("systemUser",accessUser);//登录用户
	    			request.setAttribute("baseURI",Configuration.baseURI(request.getRequestURI(), request.getContextPath()));//系统资源标识符
	    			request.setAttribute("token",csrf_token);
	    			request.setAttribute("identificationNumber",UUIDUtil.getUUID32());//识别号：用来区别每次请求
	    			
    		    	if(requestName != null && !"".equals(requestName)){
    		    		
    		    		
    		    		
    		    		
    		    		String requestName_begin_blank = "";
    		    		String requestName_begin_column = "";
    		    		if(requestName.length() >6 && "blank_".equals(requestName.substring(0, 6))){//取得Blank_
    		    			requestName_begin_blank = "blank_";
    		    		}
    		    		if(requestName.length() >7 && "column_".equals(requestName.substring(0, 7))){//取得Blank_
    		    			requestName_begin_column = "column_";
    		    		}
    		    		if("blank_".equals(requestName_begin_blank)){//空白页
    		    			//调用公共模板处理类
    	        		    Map<String,Object> roots = templateMain.list(4,requestName+".html",request);//4.空白页	
    			        	modelAndView.addAllObjects(roots);
    		    		}if("column_".equals(requestName_begin_column)){//站点栏目详细页
    		    			//调用公共模板处理类
    	        		    Map<String,Object> roots = templateMain.list(7,requestName+".html",request);//7.站点栏目详细页
    			        		
    			        	modelAndView.addAllObjects(roots);
    		    		}else{//默认页
    		    			
    		    			Map<String,Object> roots = templateMain.list(1,requestName+".html",request);//1.默认页

    		    			modelAndView.addAllObjects(roots);
    		    		}
    		    		
    		    	}
  
	    			//公共页(引用版块值)辅助类
        			Map<String,Object> publicAuxiliaryMap = templateMain.publicQuoteCall(mav+".html",request);
        			modelAndView.addAllObjects(publicAuxiliaryMap);
        			//公共页(生成新引用页)辅助类
        			Map<String,Object> newPublicAuxiliaryMap = templateMain.newPublic(mav+".html",request);
        			modelAndView.addAllObjects(newPublicAuxiliaryMap);
				}
		    }
		}
	}   
	/**
	 *  afterCompletion()方法在DispatcherServlet完全处理完请求后被调用
	 */
	public void afterCompletion(HttpServletRequest request, HttpServletResponse 
			 response, Object handler, Exception ex)throws Exception {  	
    	//删除当前线程副本
    	TemplateThreadLocal.removeThreadLocal();
    	AccessUserThreadLocal.removeThreadLocal();
    	CSRFTokenThreadLocal.removeThreadLocal();
	}   
}
