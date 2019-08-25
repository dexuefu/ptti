package cn.ptti.portal.service.feedback;

import cn.ptti.portal.bean.feedback.Feedback;
import cn.ptti.portal.service.besa.DAO;

/**
 * 在线留言
 *
 */
public interface FeedbackService extends DAO<Feedback>{
	/**
	 * 根据Id查询留言
	 * @param feedbackId 留言Id
	 * @return
	 */
	public Feedback findById(Long feedbackId);
	/**
	 * 保存留言
	 * @param feedback
	 */
	public void saveFeedback(Feedback feedback);
	

	/**
	 * 修改留言
	 * @param feedback
	 * @return
	 */
	public Integer updateFeedback(Feedback feedback);
	
	/**
	 * 删除留言
	 * @param feedbackId 留言Id
	 */
	public Integer deleteFeedback(Long feedbackId);
}
