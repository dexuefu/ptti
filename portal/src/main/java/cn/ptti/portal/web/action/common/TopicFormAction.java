package cn.ptti.portal.web.action.common;


import cn.ptti.portal.bean.ErrorView;
import cn.ptti.portal.bean.message.Remind;
import cn.ptti.portal.bean.setting.EditorTag;
import cn.ptti.portal.bean.setting.SystemSetting;
import cn.ptti.portal.bean.thumbnail.Thumbnail;
import cn.ptti.portal.bean.topic.*;
import cn.ptti.portal.bean.user.*;
import cn.ptti.portal.service.message.RemindService;
import cn.ptti.portal.service.setting.SettingService;
import cn.ptti.portal.service.template.TemplateService;
import cn.ptti.portal.service.thumbnail.ThumbnailService;
import cn.ptti.portal.service.topic.CommentService;
import cn.ptti.portal.service.topic.TagService;
import cn.ptti.portal.service.topic.TopicIndexService;
import cn.ptti.portal.service.topic.TopicService;
import cn.ptti.portal.service.user.UserGradeService;
import cn.ptti.portal.service.user.UserService;
import cn.ptti.portal.utils.Base64;
import cn.ptti.portal.utils.*;
import cn.ptti.portal.utils.threadLocal.AccessUserThreadLocal;
import cn.ptti.portal.web.action.AccessSourceDeviceManage;
import cn.ptti.portal.web.action.CSRFTokenManage;
import cn.ptti.portal.web.action.FileManage;
import cn.ptti.portal.web.action.TextFilterManage;
import cn.ptti.portal.web.action.filterWord.SensitiveWordFilterManage;
import cn.ptti.portal.web.action.follow.FollowManage;
import cn.ptti.portal.web.action.message.RemindManage;
import cn.ptti.portal.web.action.setting.SettingManage;
import cn.ptti.portal.web.action.thumbnail.ThumbnailManage;
import cn.ptti.portal.web.action.topic.TopicManage;
import cn.ptti.portal.web.action.user.PointManage;
import cn.ptti.portal.web.action.user.UserDynamicManage;
import cn.ptti.portal.web.action.user.UserManage;
import cn.ptti.portal.web.taglib.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;

/**
 * 话题接收表单
 *
 */
@Controller
@RequestMapping("user/control/topic") 
public class TopicFormAction {
	@Resource TemplateService templateService;
	
	@Resource CaptchaManage captchaManage;
	@Resource FileManage fileManage;
	@Resource CommentService commentService;
	@Resource AccessSourceDeviceManage accessSourceDeviceManage;
	
	@Resource TextFilterManage textFilterManage;
	@Resource SettingManage settingManage;
	@Resource SettingService settingService;
	@Resource UserService userService;
	@Resource TopicService topicService;
	@Resource TagService tagService;
	@Resource PointManage pointManage;
	
	@Resource TopicIndexService topicIndexService;

	@Resource CSRFTokenManage csrfTokenManage;
	@Resource TopicManage topicManage;
	@Resource SensitiveWordFilterManage sensitiveWordFilterManage;
	@Resource ThumbnailService thumbnailService;
	@Resource ThumbnailManage thumbnailManage;
	@Resource UserManage userManage;
	
	@Resource RemindService remindService;
	@Resource RemindManage remindManage;
	
	@Resource UserGradeService userGradeService;
	@Resource UserDynamicManage userDynamicManage;
	@Resource FollowManage followManage;
	
	/**
	 * 话题  添加
	 * @param model
	 * @param tagId 标签Id
	 * @param tagName 标签名称
	 * @param title 标题
	 * @param content 内容
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public String add(ModelMap model,Long tagId,String title,String content,
			String token,String captchaKey,String captchaValue,String jumpUrl,
			RedirectAttributes redirectAttrs,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		//获取登录用户
	  	AccessUser accessUser = AccessUserThreadLocal.get();
		
		boolean isAjax = WebUtil.submitDataMode(request);//是否以Ajax方式提交数据
		
		
		Map<String,String> error = new HashMap<String,String>();
		
			
		
		//判断令牌
		if(token != null && !"".equals(token.trim())){	
			String token_sessionid = csrfTokenManage.getToken(request);//获取令牌
			if(token_sessionid != null && !"".equals(token_sessionid.trim())){
				if(!token_sessionid.equals(token)){
					error.put("token", ErrorView._13.name());//令牌错误
				}
			}else{
				error.put("token", ErrorView._12.name());//令牌过期
			}
		}else{
			error.put("token", ErrorView._11.name());//令牌为空
		}
		
		//验证码
		boolean isCaptcha = captchaManage.topic_isCaptcha(accessUser.getUserName());
		if(isCaptcha){//如果需要验证码
			//验证验证码
			if(captchaKey != null && !"".equals(captchaKey.trim())){
				//增加验证码重试次数
				//统计每分钟原来提交次数
				Integer original = settingManage.getSubmitQuantity("captcha", captchaKey.trim());
	    		if(original != null){
	    			settingManage.addSubmitQuantity("captcha", captchaKey.trim(),original+1);//刷新每分钟原来提交次数
	    		}else{
	    			settingManage.addSubmitQuantity("captcha", captchaKey.trim(),1);//刷新每分钟原来提交次数
	    		}
				
				
				String _captcha = captchaManage.captcha_generate(captchaKey.trim(),"");
				if(captchaValue != null && !"".equals(captchaValue.trim())){
					if(_captcha != null && !"".equals(_captcha.trim())){
						if(!_captcha.equalsIgnoreCase(captchaValue)){
							error.put("captchaValue",ErrorView._15.name());//验证码错误
						}
					}else{
						error.put("captchaValue",ErrorView._17.name());//验证码过期
					}
				}else{
					error.put("captchaValue",ErrorView._16.name());//请输入验证码
				}
				//删除验证码
				captchaManage.captcha_delete(captchaKey.trim());	
			}else{
				error.put("captchaValue", ErrorView._14.name());//验证码参数错误
			}
			
		}
		
		SystemSetting systemSetting = settingService.findSystemSetting_cache();
		
		
		//如果全局不允许提交话题
		if(systemSetting.isAllowTopic() == false){
			error.put("topic", ErrorView._110.name());//不允许提交话题
		}
		
		//如果实名用户才允许提交话题
		if(systemSetting.isRealNameUserAllowTopic() == true){
			User _user = userManage.query_cache_findUserByUserName(accessUser.getUserName());
			if(_user.isRealNameAuthentication() == false){
				error.put("topic", ErrorView._109.name());//实名用户才允许提交话题
			}
			
		}
		
		
		//前3张图片地址
		List<ImageInfo> beforeImageList = new ArrayList<ImageInfo>();
				
		
		Topic topic = new Topic();
		List<String> imageNameList = null;
		boolean isImage = false;//是否含有图片
		List<String> flashNameList = null;
		boolean isFlash = false;//是否含有Flash
		List<String> mediaNameList = null;
		boolean isMedia = false;//是否含有音视频
		List<String> fileNameList = null;
		boolean isFile = false;//是否含有文件
		boolean isMap = false;//是否含有地图
		
		//前台发表话题默认状态
		if(systemSetting.getTopic_defaultState() != null){
			if(systemSetting.getTopic_defaultState().equals(10)){
				topic.setStatus(10);
			}else if(systemSetting.getTopic_defaultState().equals(20)){
				topic.setStatus(20);
			}
		}
		
		if(tagId == null || tagId <=0L){
			error.put("tagId", "标签不能为空");
		}else{
			List<Tag> tagList = tagService.findAllTag_cache();
			if(tagList != null && tagList.size() >0){
				for(Tag tag : tagList){
					if(tag.getId().equals(tagId)){
						topic.setTagId(tag.getId());
						topic.setTagName(tag.getName());
						break;
					}
					
				}
				if(topic.getTagId() == null){
					error.put("tagId", "标签不存在");
				}
			}
		}
		if(title != null && !"".equals(title.trim())){
			if(systemSetting.isAllowFilterWord()){
				String wordReplace = "";
				if(systemSetting.getFilterWordReplace() != null){
					wordReplace = systemSetting.getFilterWordReplace();
				}
				title = sensitiveWordFilterManage.filterSensitiveWord(title, wordReplace);
			}
			
			topic.setTitle(title);
			if(title.length() >150){
				error.put("title", "不能大于150个字符");
			}
		}else{
			error.put("title", "标题不能为空");
		}
		
		
		if(content != null && !"".equals(content.trim())){
			
		//	content = "<div style=\"text-align: center; height: expression(alert('test &ss')); while: expression(alert('test xss'));\">fdfd</div>";
		//	content = "<img src=\"java script:alert(/XSS/)\" width = 100>";
		//	content = "<div style=\"background-image: url(javasc \n\t ript:alert('XSS'))\">";
		//	content = "<img src=\"java\nscript:alert(/XSS/)\" width = 100>";
			
		//	content = "<div style=\"background-color: #123456;color: expr\65ssion(alert('testss'));\"><a href=\"javascript:alert('XSS');\"><a href=\"http://127.0.0.1:8080/cn.ptti.portal/javascript:alert('XSS');\"><a href=\"aaa/ggh.htm\">";
			
		//	content = "<div style=\"background-image: url(javascript:alert('XSS'));background-image: url(javascript&colon;alert('XSSW'));width: expression(alert&#40;'testxss'));\">";
		//	content = "<div style=\"background-image: url(javasc\n\tript:alert('XSS'))\">";
			
		//	content = "<div style=\"color:express/**/ion(eval('\\x69\\x66\\x28\\x77\\x69\\x6e\\x64\\x6f\\x77\\x2e\\x61\\x21\\x3d\\x31\\x29\\x7b\\x61\\x6c\\x65\\x72\\x74\\x28\\x2f\\x73\\x74\\x79\\x6c\\x65\\x5f\\x38\\x2f\\x29\\x3b\\x77\\x69\\x6e\\x64\\x6f\\x77\\x2e\\x61\\x3d\\x31\\x3b\\x7d'))\">";
			
			EditorTag editorTag = settingManage.readTopicEditorTag();
			//过滤标签
			content = textFilterManage.filterTag(request,content,editorTag);
			Object[] object = textFilterManage.filterHtml(request,content,"topic",editorTag);
			String value = (String)object[0];
			imageNameList = (List<String>)object[1];
			isImage = (Boolean)object[2];//是否含有图片
			flashNameList = (List<String>)object[3];
			isFlash = (Boolean)object[4];//是否含有Flash
			mediaNameList = (List<String>)object[5];
			isMedia = (Boolean)object[6];//是否含有音视频
			fileNameList = (List<String>)object[7];
			isFile = (Boolean)object[8];//是否含有文件
			isMap = (Boolean)object[9];//是否含有地图
			
			List<UserGrade> userGradeList = userGradeService.findAllGrade_cache();
			//校正隐藏标签
			String validValue =  textFilterManage.correctionHiddenTag(value,userGradeList);
			
			//允许使用的隐藏标签
			List<Integer> allowHiddenTagList = new ArrayList<Integer>();
			if(editorTag.isHidePassword()){
				//输入密码可见
				allowHiddenTagList.add(HideTagType.PASSWORD.getName());
			}
			if(editorTag.isHideComment()){
				//评论话题可见
				allowHiddenTagList.add(HideTagType.COMMENT.getName());
			}
			if(editorTag.isHideGrade()){
				//达到等级可见
				allowHiddenTagList.add(HideTagType.GRADE.getName());	
			}
			if(editorTag.isHidePoint()){
				//积分购买可见
				allowHiddenTagList.add(HideTagType.POINT.getName());	
			}
			if(editorTag.isHideAmount()){
				//余额购买可见
				allowHiddenTagList.add(HideTagType.AMOUNT.getName());	
			}

			//解析隐藏标签
			Map<Integer,Object> analysisHiddenTagMap = textFilterManage.analysisHiddenTag(validValue);
			for (Map.Entry<Integer,Object> entry : analysisHiddenTagMap.entrySet()) {
				if(!allowHiddenTagList.contains(entry.getKey())){
					error.put("content", "发表话题不允许使用 '"+HideTagName.getKey(entry.getKey())+"' 隐藏标签");//隐藏标签
					break;
				}
			}
			
			//删除隐藏标签
			String new_content = textFilterManage.deleteHiddenTag(value);

			//不含标签内容
			String text = textFilterManage.filterText(new_content);
			//清除空格&nbsp;
			String trimSpace = cn.ptti.portal.utils.StringUtil.replaceSpace(text).trim();
			//摘要
			if(trimSpace != null && !"".equals(trimSpace)){
				if(systemSetting.isAllowFilterWord()){
					String wordReplace = "";
					if(systemSetting.getFilterWordReplace() != null){
						wordReplace = systemSetting.getFilterWordReplace();
					}
					trimSpace = sensitiveWordFilterManage.filterSensitiveWord(trimSpace, wordReplace);
				}
				if(trimSpace.length() >180){
					topic.setSummary(trimSpace.substring(0, 180)+"..");
				}else{
					topic.setSummary(trimSpace+"..");
				}
			}
			
			//不含标签内容
			String source_text = textFilterManage.filterText(content);
			//清除空格&nbsp;
			String source_trimSpace = cn.ptti.portal.utils.StringUtil.replaceSpace(source_text).trim();
			
			if(isImage == true || isFlash == true || isMedia == true || isFile==true ||isMap== true || (!"".equals(source_text.trim()) && !"".equals(source_trimSpace))){
				if(systemSetting.isAllowFilterWord()){
					String wordReplace = "";
					if(systemSetting.getFilterWordReplace() != null){
						wordReplace = systemSetting.getFilterWordReplace();
					}
					validValue = sensitiveWordFilterManage.filterSensitiveWord(validValue, wordReplace);
				}
				
				
				
				
				topic.setIp(IpAddress.getClientIpAddress(request));
				topic.setUserName(accessUser.getUserName());
				topic.setIsStaff(false);
				topic.setContent(validValue);
			}else{
				error.put("content", "话题内容不能为空");
			}	
			
			
			//非隐藏标签内图片
			List<String> other_imageNameList = textFilterManage.readImageName(new_content,"topic");
			
			if(other_imageNameList != null && other_imageNameList.size() >0){
				for(int i=0; i<other_imageNameList.size(); i++){
					ImageInfo imageInfo = new ImageInfo();
					imageInfo.setName(fileManage.getName(other_imageNameList.get(i)));
					imageInfo.setPath(fileManage.getFullPath(other_imageNameList.get(i)));
					
					beforeImageList.add(imageInfo);
					
					if(i ==2){//只添加3张图片
						break;
					}
				}
				topic.setImage(JsonUtils.toJSONString(beforeImageList));
				
			}
		}else{
			error.put("content", "话题内容不能为空");
		}
			
		if(error.size() == 0){
			//保存评论
			topicService.saveTopic(topic);
			//更新索引
			topicIndexService.addTopicIndex(new TopicIndex(String.valueOf(topic.getId()),1));
			
			PointLog pointLog = new PointLog();
			pointLog.setId(pointManage.createPointLogId(accessUser.getUserId()));
			pointLog.setModule(100);//100.话题
			pointLog.setParameterId(topic.getId());//参数Id 
			pointLog.setOperationUserType(2);//操作用户类型  0:系统  1: 员工  2:会员
			pointLog.setOperationUserName(accessUser.getUserName());//操作用户名称
			
			pointLog.setPoint(systemSetting.getTopic_rewardPoint());//积分
			pointLog.setUserName(accessUser.getUserName());//用户名称
			pointLog.setRemark("");
			//增加用户积分
			userService.addUserPoint(accessUser.getUserName(),systemSetting.getTopic_rewardPoint(), pointManage.createPointLogObject(pointLog));
			
			
			//用户动态
			UserDynamic userDynamic = new UserDynamic();
			userDynamic.setId(userDynamicManage.createUserDynamicId(accessUser.getUserId()));
			userDynamic.setUserName(accessUser.getUserName());
			userDynamic.setModule(100);//模块 100.话题
			userDynamic.setTopicId(topic.getId());
			userDynamic.setPostTime(topic.getPostTime());
			userDynamic.setStatus(topic.getStatus());
			
			Object new_userDynamic = userDynamicManage.createUserDynamicObject(userDynamic);
			userService.saveUserDynamic(new_userDynamic);

			
			//删除缓存
			userManage.delete_cache_findUserById(accessUser.getUserId());
			userManage.delete_cache_findUserByUserName(accessUser.getUserName());
			followManage.delete_cache_userUpdateFlag(accessUser.getUserName());
			
			String fileNumber = "b"+ accessUser.getUserId();
			
			//删除图片锁
			if(imageNameList != null && imageNameList.size() >0){
				for(String imageName :imageNameList){
			
					 if(imageName != null && !"".equals(imageName.trim())){
						//如果验证不是当前用户上传的文件，则不删除锁
						 if(!topicManage.getFileNumber(fileManage.getBaseName(imageName.trim())).equals(fileNumber)){
							 continue;
						 }
						 
						 
						 fileManage.deleteLock("file"+File.separator+"topic"+File.separator+"lock"+File.separator,imageName.replaceAll("/","_"));
					
					 }
				}
			}
			//falsh
			if(flashNameList != null && flashNameList.size() >0){
				for(String flashName :flashNameList){
					 if(flashName != null && !"".equals(flashName.trim())){
						//如果验证不是当前用户上传的文件，则不删除锁
						if(!topicManage.getFileNumber(fileManage.getBaseName(flashName.trim())).equals(fileNumber)){
							continue;
						}
						fileManage.deleteLock("file"+File.separator+"topic"+File.separator+"lock"+File.separator,flashName.replaceAll("/","_"));
					
					 }
				}
			}
			//音视频
			if(mediaNameList != null && mediaNameList.size() >0){
				for(String mediaName :mediaNameList){
					if(mediaName != null && !"".equals(mediaName.trim())){
						//如果验证不是当前用户上传的文件，则不删除锁
						if(!topicManage.getFileNumber(fileManage.getBaseName(mediaName.trim())).equals(fileNumber)){
							continue;
						}
						fileManage.deleteLock("file"+File.separator+"topic"+File.separator+"lock"+File.separator,mediaName.replaceAll("/","_"));
					
					}
				}
			}
			//文件
			if(fileNameList != null && fileNameList.size() >0){
				for(String fileName :fileNameList){
					if(fileName != null && !"".equals(fileName.trim())){
						//如果验证不是当前用户上传的文件，则不删除锁
						if(!topicManage.getFileNumber(fileManage.getBaseName(fileName.trim())).equals(fileNumber)){
							continue;
						}
						fileManage.deleteLock("file"+File.separator+"topic"+File.separator+"lock"+File.separator,fileName.replaceAll("/","_"));
					
					}
				}
			}
			
			List<Thumbnail> thumbnailList = thumbnailService.findAllThumbnail_cache();
			if(thumbnailList != null && thumbnailList.size() >0){
				//异步增加缩略图
				if(beforeImageList != null && beforeImageList.size() >0){
					thumbnailManage.addThumbnail(thumbnailList,beforeImageList);
				}
			}
			
			//统计每分钟原来提交次数
			Integer original = settingManage.getSubmitQuantity("topic", accessUser.getUserName());
    		if(original != null){
    			settingManage.addSubmitQuantity("topic", accessUser.getUserName(),original+1);//刷新每分钟原来提交次数
    		}else{
    			settingManage.addSubmitQuantity("topic", accessUser.getUserName(),1);//刷新每分钟原来提交次数
    		}

		}
		
		
		Map<String,String> returnError = new HashMap<String,String>();//错误
		if(error.size() >0){
			//将枚举数据转为错误提示字符
    		for (Map.Entry<String,String> entry : error.entrySet()) {		 
    			if(ErrorView.get(entry.getValue()) != null){
    				returnError.put(entry.getKey(),  ErrorView.get(entry.getValue()));
    			}else{
    				returnError.put(entry.getKey(),  entry.getValue());
    			}
			}
		}
		if(isAjax == true){
			
    		Map<String,Object> returnValue = new HashMap<String,Object>();//返回值
    		
    		if(error != null && error.size() >0){
    			returnValue.put("success", "false");
    			returnValue.put("error", returnError);
    			if(isCaptcha){
    				returnValue.put("captchaKey", UUIDUtil.getUUID32());
    			}
    			
    		}else{
    			returnValue.put("success", "true");
    		}
    		WebUtil.writeToWeb(JsonUtils.toJSONString(returnValue), "json", response);
			return null;
		}else{
			
			
			if(error != null && error.size() >0){//如果有错误
				redirectAttrs.addFlashAttribute("error", returnError);//重定向传参
				redirectAttrs.addFlashAttribute("topic", topic);
				

				String referer = request.getHeader("referer");	
				
				
				referer = StringUtils.removeStartIgnoreCase(referer,Configuration.getUrl(request));//移除开始部分的相同的字符,不区分大小写
				referer = StringUtils.substringBefore(referer, ".");//截取到等于第二个参数的字符串为止
				referer = StringUtils.substringBefore(referer, "?");//截取到等于第二个参数的字符串为止
				
				String queryString = request.getQueryString() != null && !"".equals(request.getQueryString().trim()) ? "?"+request.getQueryString() :"";

				return "redirect:/"+referer+queryString;
	
			}
			
			
			if(jumpUrl != null && !"".equals(jumpUrl.trim())){
				String url = Base64.decodeBase64URL(jumpUrl.trim());
				
				return "redirect:"+url;
			}else{//默认跳转
				model.addAttribute("message", "保存话题成功");
				String referer = request.getHeader("referer");
				if(RefererCompare.compare(request, "login")){//如果是登录页面则跳转到首页
					referer = Configuration.getUrl(request);
				}
				model.addAttribute("urlAddress", referer);
				
				String dirName = templateService.findTemplateDir_cache();
				
				
				return "templates/"+dirName+"/"+accessSourceDeviceManage.accessDevices(request)+"/jump";	
			}
		}
		
		
		

	}

	/**
	 * 文件上传
	 * dir: 上传类型，分别为image、flash、media、file 
	 * 
	 * 员工发话题 上传文件名为UUID + a + 员工Id
	 * 用户发话题 上传文件名为UUID + b + 用户Id
	 */
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	@ResponseBody//方式来做ajax,直接返回字符串
	public String upload(ModelMap model,
			MultipartFile imgFile,HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String,Object> returnJson = new HashMap<String,Object>();
		SystemSetting systemSetting = settingService.findSystemSetting_cache();
		String errorMessage  = "";
		
		//获取登录用户
	  	AccessUser accessUser = AccessUserThreadLocal.get();
		boolean flag = true;
		
		//如果全局不允许提交话题
		if(systemSetting.isAllowTopic() == false){
			flag = false;
		}
		
		//如果实名用户才允许提交话题
		if(systemSetting.isRealNameUserAllowTopic() == true){
			User _user = userManage.query_cache_findUserByUserName(accessUser.getUserName());
			if(_user.isRealNameAuthentication() == false){
				flag = false;
			}
		}
		if(flag){
			DateTime dateTime = new DateTime();     
			String date = dateTime.toString("yyyy-MM-dd");
			
			if(imgFile != null && !imgFile.isEmpty()){
				EditorTag editorSiteObject = settingManage.readTopicEditorTag();
				if(editorSiteObject != null){
					if(editorSiteObject.isImage()){//允许上传图片
						//上传文件编号
						String fileNumber = "b"+accessUser.getUserId();
						
						//当前文件名称
						String fileName = imgFile.getOriginalFilename();
						
						//文件大小
						Long size = imgFile.getSize();
						//取得文件后缀
						String suffix = fileManage.getExtension(fileName).toLowerCase();
						
						//允许上传图片格式
						List<String> imageFormat = editorSiteObject.getImageFormat();
						//允许上传图片大小
						long imageSize = editorSiteObject.getImageSize();
						
						//验证文件类型
						boolean authentication = fileManage.validateFileSuffix(imgFile.getOriginalFilename(),imageFormat);
						
						if(authentication ){
							if(size/1024 <= imageSize){
								//文件保存目录;分多目录主要是为了分散图片目录,提高检索速度
								String pathDir = "file"+File.separator+"topic"+File.separator + date +File.separator +"image"+ File.separator;
								//文件锁目录
								String lockPathDir = "file"+File.separator+"topic"+File.separator+"lock"+File.separator;
								//构建文件名称
								String newFileName = UUIDUtil.getUUID32()+ fileNumber+"." + suffix;
								
								//生成文件保存目录
								fileManage.createFolder(pathDir);
								//生成锁文件保存目录
								fileManage.createFolder(lockPathDir);
								//生成锁文件
								fileManage.newFile(lockPathDir+date +"_image_"+newFileName);
								//保存文件
								fileManage.writeFile(pathDir, newFileName,imgFile.getBytes());
								//上传成功
								returnJson.put("error", 0);//0成功  1错误
								returnJson.put("url", "file/topic/"+date+"/image/"+newFileName);
								return JsonUtils.toJSONString(returnJson);
							}else{
								errorMessage = "文件超出允许上传大小";
							}
						}else{
							errorMessage = "当前文件类型不允许上传";
						}
					}else{
						errorMessage = "不允许上传文件";
					}
				}else{
					errorMessage = "读取话题编辑器允许使用标签失败";
				}	
			}else{
				errorMessage = "文件内容不能为空";
			}
		}else{
			errorMessage = "不允许发表话题";
		}
		
		//上传失败
		returnJson.put("error", 1);
		returnJson.put("message", errorMessage);
		return JsonUtils.toJSONString(returnJson);
	}
	
	
	/**
	 * 话题  取消隐藏
	 * @param model
	 * @param topicId 话题Id
	 * @param hideType 隐藏类型
	 * @param password 密码
	 * @param token
	 * @param jumpUrl
	 * @param redirectAttrs
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/unhide", method=RequestMethod.POST)
	public String topicUnhide(ModelMap model,Long topicId,Integer hideType, String password,
			String token,String jumpUrl,
			RedirectAttributes redirectAttrs,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取登录用户
	  	AccessUser accessUser = AccessUserThreadLocal.get();
		
		boolean isAjax = WebUtil.submitDataMode(request);//是否以Ajax方式提交数据
		
		
		Map<String,String> error = new HashMap<String,String>();
		
			
		
		//判断令牌
		if(token != null && !"".equals(token.trim())){	
			String token_sessionid = csrfTokenManage.getToken(request);//获取令牌
			if(token_sessionid != null && !"".equals(token_sessionid.trim())){
				if(!token_sessionid.equals(token)){
					error.put("token", ErrorView._13.name());//令牌错误
				}
			}else{
				error.put("token", ErrorView._12.name());//令牌过期
			}
		}else{
			error.put("token", ErrorView._11.name());//令牌为空
		}
		
		//统计每分钟原来提交次数
		Integer quantity = settingManage.getSubmitQuantity("topicUnhide", accessUser.getUserName());
    	if(quantity != null && quantity >30){//如果每分钟提交超过30次，则一分钟内不允许'取消隐藏'
    		error.put("topicUnhide", ErrorView._1640.name());//提交过于频繁，请稍后再提交
    	}
    	
    	Topic topic = null;
    	if(error.size() == 0){
    		if(topicId != null && topicId >0L){
    	  		
    	  		topic = topicManage.queryTopicCache(topicId);//查询缓存

    	  		
    	  	}else{
    	  		error.put("topicUnhide", ErrorView._103.name());//话题Id不能为空
    	  	}
    		
    		if(topic.getUserName().equals(accessUser.getUserName())){
    			error.put("topicUnhide", ErrorView._1690.name());//不允许解锁自已发表的话题
    		}
    		
    	  	if(topic != null){
    	  		//话题取消隐藏Id
    		  	String topicUnhideId = topicManage.createTopicUnhideId(accessUser.getUserId(), hideType, topicId);
    		  
    		  	TopicUnhide topicUnhide = topicManage.query_cache_findTopicUnhideById(topicUnhideId);
    	  		
    	  		if(topicUnhide != null){
    		  		error.put("topicUnhide", ErrorView._1610.name());//当前话题已经取消隐藏
    		  	}
    	  		
    	  		
    		}
    	}
    	
    	//消费积分
		Long point = null;
	  	
		List<Integer> hideTypeList = new ArrayList<Integer>();
		hideTypeList.add(HideTagType.PASSWORD.getName());
		hideTypeList.add(HideTagType.POINT.getName());
		hideTypeList.add(HideTagType.AMOUNT.getName());
		
		if(!hideTypeList.contains(hideType)){
			error.put("topicUnhide", ErrorView._1620.name());//隐藏标签不存在
		}
		
			
		if(error.size() == 0){
			//解析隐藏标签
			Map<Integer,Object> analysisHiddenTagMap = textFilterManage.analysisHiddenTag(topic.getContent());
			if(!analysisHiddenTagMap.containsKey(hideType)){
				error.put("topicUnhide", ErrorView._1660.name());//话题内容不含当前标签
			}
			
			for (Map.Entry<Integer,Object> entry : analysisHiddenTagMap.entrySet()) {
				if(entry.getKey().equals(HideTagType.PASSWORD.getName()) && HideTagType.PASSWORD.getName().equals(hideType)){//输入密码可见
					if(password == null || "".equals(password.trim())){
						error.put("password", ErrorView._1650.name());//密码不能为空
						break;
					}
					
					
					if(!entry.getValue().equals(password)){
						error.put("topicUnhide", ErrorView._1630.name());//密码错误
					}
					break;
				}
				
				if(entry.getKey().equals(HideTagType.POINT.getName()) && HideTagType.POINT.getName().equals(hideType)){//积分购买可见
					//获取登录用户
			  		User _user = userService.findUserByUserName(accessUser.getUserName());
			  		if(_user != null){
			  			if(_user.getPoint() < (Long)entry.getValue()){
			  				error.put("topicUnhide", ErrorView._1680.name());//用户积分不足
			  			}else{
			  				point = (Long)entry.getValue();
			  			}
			  		}else{
			  			error.put("topicUnhide", ErrorView._1670.name());//用户不存在
			  		}
			  		
				}
				
			}
		}

		//统计每分钟原来提交次数
		Integer original = settingManage.getSubmitQuantity("topicUnhide", accessUser.getUserName());
		if(original != null){
			settingManage.addSubmitQuantity("topicUnhide", accessUser.getUserName(),original+1);//刷新每分钟原来提交次数
		}else{
			settingManage.addSubmitQuantity("topicUnhide", accessUser.getUserName(),1);//刷新每分钟原来提交次数
		}
		
		
		

		if(error.size() == 0){
			
			TopicUnhide topicUnhide = new TopicUnhide();
			String topicUnhideId = topicManage.createTopicUnhideId(accessUser.getUserId(), hideType, topicId);
			topicUnhide.setId(topicUnhideId);
			topicUnhide.setUserName(accessUser.getUserName());
			topicUnhide.setCancelTime(new Date());
			topicUnhide.setHideTagType(hideType);
			topicUnhide.setPostUserName(topic.getUserName());//发布话题的用户名称
			topicUnhide.setTopicId(topicId);
			
			//用户消费积分日志
			Object consumption_pointLogObject = null;
			//用户收入积分日志
			Object income_pointLogObject = null;
			if(point != null){
				topicUnhide.setPoint(point);
				
				PointLog pointLog = new PointLog();
				pointLog.setId(pointManage.createPointLogId(accessUser.getUserId()));
				pointLog.setModule(400);//400.积分购买隐藏话题
				pointLog.setParameterId(topic.getId());//参数Id 
				pointLog.setOperationUserType(2);//操作用户类型  0:系统  1: 员工  2:会员
				pointLog.setOperationUserName(accessUser.getUserName());//操作用户名称
				pointLog.setPointState(2);//2:账户支出
				pointLog.setPoint(point);//积分
				pointLog.setUserName(accessUser.getUserName());//用户名称
				pointLog.setRemark("");
				consumption_pointLogObject = pointManage.createPointLogObject(pointLog);
				
				if(!topic.getIsStaff()){//如果是用户
					User _user = userManage.query_cache_findUserByUserName(topic.getUserName());
					PointLog income_pointLog = new PointLog();
					income_pointLog.setId(pointManage.createPointLogId(_user.getId()));
					income_pointLog.setModule(400);//400.积分购买隐藏话题
					income_pointLog.setParameterId(topic.getId());//参数Id 
					income_pointLog.setOperationUserType(2);//操作用户类型  0:系统  1: 员工  2:会员
					income_pointLog.setOperationUserName(accessUser.getUserName());//操作用户名称
					income_pointLog.setPointState(1);//1:账户存入
					income_pointLog.setPoint(point);//积分
					income_pointLog.setUserName(topic.getUserName());//用户名称
					income_pointLog.setRemark("");
					income_pointLogObject = pointManage.createPointLogObject(income_pointLog);
					
					//删除用户缓存
					userManage.delete_cache_findUserById(_user.getId());
					userManage.delete_cache_findUserByUserName(topic.getUserName());
					
				}
				
			}
			
			try {
				//保存'话题取消隐藏'
				topicService.saveTopicUnhide(topicManage.createTopicUnhideObject(topicUnhide),hideType,point,accessUser.getUserName(),consumption_pointLogObject,topic.getUserName(),income_pointLogObject);
				
				//删除'话题取消隐藏'缓存;
				topicManage.delete_cache_findTopicUnhideById(topicUnhideId);
				
				//删除用户缓存
				userManage.delete_cache_findUserById(accessUser.getUserId());
				userManage.delete_cache_findUserByUserName(accessUser.getUserName());
				
				
				
				User _user = userManage.query_cache_findUserByUserName(topic.getUserName());
				
				//别人解锁了我的话题  只对(隐藏标签类型 10:输入密码可见  40:积分购买可见  50:余额购买可见)发提醒
				if(!topic.getIsStaff() && _user != null && !topic.getUserName().equals(accessUser.getUserName())){//不发提醒给自己
					
					//提醒楼主
					Remind remind = new Remind();
					remind.setId(remindManage.createRemindId(_user.getId()));
					remind.setReceiverUserId(_user.getId());//接收提醒用户Id
					remind.setSenderUserId(accessUser.getUserId());//发送用户Id
					remind.setTypeCode(60);//60:别人解锁了我的话题
					remind.setSendTimeFormat(new Date().getTime());//发送时间格式化
					remind.setTopicId(topic.getId());//话题Id
					
					Object remind_object = remindManage.createRemindObject(remind);
					remindService.saveRemind(remind_object);
					
					//删除提醒缓存
					remindManage.delete_cache_findUnreadRemindByUserId(_user.getId());
						
					
				}
				
				
				
				
				
				
				
			} catch (org.springframework.orm.jpa.JpaSystemException e) {
				error.put("topicUnhide", ErrorView._1600.name());//话题重复取消隐藏
				
			}
	
		}
		
		
		
		
		
		Map<String,String> returnError = new HashMap<String,String>();//错误
		if(error.size() >0){
			//将枚举数据转为错误提示字符
    		for (Map.Entry<String,String> entry : error.entrySet()) {		 
    			if(ErrorView.get(entry.getValue()) != null){
    				returnError.put(entry.getKey(),  ErrorView.get(entry.getValue()));
    			}else{
    				returnError.put(entry.getKey(),  entry.getValue());
    			}
			}
		}
		if(isAjax == true){
			
    		Map<String,Object> returnValue = new HashMap<String,Object>();//返回值
    		
    		if(error != null && error.size() >0){
    			returnValue.put("success", "false");
    			returnValue.put("error", returnError);
    			
    		}else{
    			returnValue.put("success", "true");
    		}
    		WebUtil.writeToWeb(JsonUtils.toJSONString(returnValue), "json", response);
			return null;
		}else{
			
			
			if(error != null && error.size() >0){//如果有错误
				redirectAttrs.addFlashAttribute("error", returnError);//重定向传参
				redirectAttrs.addFlashAttribute("hideType", hideType);
				

				String referer = request.getHeader("referer");	
				
				
				referer = StringUtils.removeStartIgnoreCase(referer,Configuration.getUrl(request));//移除开始部分的相同的字符,不区分大小写
				referer = StringUtils.substringBefore(referer, ".");//截取到等于第二个参数的字符串为止
				referer = StringUtils.substringBefore(referer, "?");//截取到等于第二个参数的字符串为止
				
				String queryString = request.getQueryString() != null && !"".equals(request.getQueryString().trim()) ? "?"+request.getQueryString() :"";

				return "redirect:/"+referer+queryString;
	
			}
			
			
			if(jumpUrl != null && !"".equals(jumpUrl.trim())){
				String url = Base64.decodeBase64URL(jumpUrl.trim());
				
				return "redirect:"+url;
			}else{//默认跳转
				model.addAttribute("message", "话题取消隐藏成功");
				String referer = request.getHeader("referer");
				if(RefererCompare.compare(request, "login")){//如果是登录页面则跳转到首页
					referer = Configuration.getUrl(request);
				}
				model.addAttribute("urlAddress", referer);
				
				String dirName = templateService.findTemplateDir_cache();
				
				
				return "templates/"+dirName+"/"+accessSourceDeviceManage.accessDevices(request)+"/jump";	
			}
		}
	}
	
	
}
