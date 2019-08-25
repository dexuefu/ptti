package cn.ptti.portal.web.action;

import freemarker.cache.TemplateLoader;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.List;

/**
 * FreeMarker模板配置
 *
 */
public class MyFreeMarkerConfigurer extends FreeMarkerConfigurer{
	
	@Override  
    protected TemplateLoader getAggregateTemplateLoader(List<TemplateLoader> templateLoaders) {  
  
        return new HtmlTemplateLoader(super.getAggregateTemplateLoader(templateLoaders));  
  
    }  
}
