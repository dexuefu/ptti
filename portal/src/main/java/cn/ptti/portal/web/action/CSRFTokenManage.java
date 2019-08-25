package cn.ptti.portal.web.action;

import cn.ptti.portal.utils.UUIDUtil;
import cn.ptti.portal.utils.WebUtil;
import cn.ptti.portal.utils.threadLocal.CSRFTokenThreadLocal;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * CSRF令牌管理
 *
 */
@Component("csrfTokenManage")
public class CSRFTokenManage {
	
	/**
	 * 设置令牌
	 * @param request
	 * @param response
	 */
	public void setToken(HttpServletRequest request,HttpServletResponse response){
		String token = this.getToken(request);
		if(token == null || "".equals(token.trim())){
			String new_token = UUIDUtil.getUUID32();
			//将令牌添加到Cookie
			WebUtil.addCookie(response, "cn.ptti.portal_token",new_token , 0);
			CSRFTokenThreadLocal.set(new_token);
		}
	}
	
	/**
	 * 获取令牌
	 * @param request
	 * @return
	 */
	public String getToken(HttpServletRequest request){
		//获取token
		return WebUtil.getCookieByName(request, "cn.ptti.portal_token");
	}
	/**
	 * 删除令牌
	 * @param response
	 * @return
	 */
	public void deleteToken(HttpServletResponse response){
		WebUtil.deleteCookie(response, "cn.ptti.portal_token");
	}
}
