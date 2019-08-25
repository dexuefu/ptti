package cn.ptti.portal.web.action.quartz;

import cn.ptti.portal.web.action.FileManage;
import cn.ptti.portal.web.action.lucene.TopicIndexManage;
import cn.ptti.portal.web.action.thumbnail.ThumbnailManage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 定时任务类
 *
 */
@Component("task")
public class Task {
	
	@Resource FileManage fileManage;
	
	@Resource ThumbnailManage thumbnailManage;
	@Resource TopicIndexManage topicIndexManage;
	/**
	 * 话题全文索引
	 */
	public void topicIndex() {
		topicIndexManage.updateTopicIndex();
	}

	
	/**
	 * 处理缩略图
	 */
	public void treatmentThumbnail() {
		thumbnailManage.treatmentThumbnail();
	}
	
	/**
	 * 删除无效的上传临时文件
	 */
	public void deleteInvalidFile() {
		fileManage.deleteInvalidFile();
	}
	
	
}
