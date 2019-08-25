package cn.ptti.portal.web.action.filePackage;

import cn.ptti.portal.bean.filePackage.FilePackage;
import cn.ptti.portal.utils.FileSize;
import cn.ptti.portal.utils.PathUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.util.*;


/**
 * 文件打包
 *
 */
@Controller
public class FilePackageAction {
	
	/**
	 * 文件打包管理列表
	 */
	@RequestMapping("/control/filePackage/list")  
	public String execute(ModelMap model)
			throws Exception {	

		List<FilePackage> filePackageList = new ArrayList<FilePackage>();
		
		//模板目录
		String pathDir = "WEB-INF"+File.separator+"data"+ File.separator+"filePackage"+ File.separator;
		
		String[] extensions = null;//后缀名{"doc", "pdf"}
		boolean recursive = false;//是否递归
		Collection<File> files = FileUtils.listFiles(new File(PathUtil.path()+File.separator+pathDir), extensions, recursive);
		
		
		// 迭代输出
		for (Iterator<File> iterator = files.iterator(); iterator.hasNext();) {
		    File file = iterator.next();
		   
		    FilePackage filePackage = new FilePackage();
		    
		    filePackage.setFileName(file.getName());
		    filePackage.setCreateTime(new Date(file.lastModified()));
		    filePackage.setSize(FileSize.conversion(file.length()));
		   
		    filePackageList.add(filePackage);
		    
			
		}
		
		//排序
        Collections.sort(filePackageList, new ComparatorDate());  
        
		model.addAttribute("filePackageList",filePackageList);
		return "jsp/filePackage/filePackageList";
	}
	
	/**
	 * 按日期排序(新到旧)
	 *
	 */
	private class ComparatorDate implements Comparator<Object> {  
		  
	    public int compare(Object obj1, Object obj2) {  
	        Date begin = ((FilePackage)obj1).getCreateTime();  
	        Date end = ((FilePackage)obj2).getCreateTime();  
	        if (begin.before(end)) {  
	            return 1;  
	        } else {  
	            return -1;  
	        }  
	    }  
	}  
	
}
