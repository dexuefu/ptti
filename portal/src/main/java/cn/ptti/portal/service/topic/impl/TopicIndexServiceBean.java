package cn.ptti.portal.service.topic.impl;


import cn.ptti.portal.bean.topic.TopicIndex;
import cn.ptti.portal.service.besa.DaoSupport;
import cn.ptti.portal.service.topic.TopicIndexService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.List;


/**
 * 话题Lucene索引
 *
 */
@Service
@Transactional
public class TopicIndexServiceBean extends DaoSupport<TopicIndex> implements TopicIndexService {
	
	/**
	 * 查询话题索引变化标记
	 */
	@Transactional(readOnly=true,propagation=Propagation.NOT_SUPPORTED)
	public List<TopicIndex> findTopicIndex(int firstIndex, int maxResult){
		
		Query query = em.createQuery("select o from TopicIndex o ORDER BY o.id asc");
		//索引开始,即从哪条记录开始
		query.setFirstResult(firstIndex);
		//获取多少条数据
		query.setMaxResults(maxResult);
		return query.getResultList();
	
	}
	
	/**
	 * 添加话题索引变化标记
	 * @param ProductInfoIndex 索引变化标记
	 */
	public void addTopicIndex(TopicIndex topicIndex){
		this.save(topicIndex);
	}
	/**
	 * 删除话题索引变化标记
	 * @param indexIdList 索引变化标记Id集合
	 */
	public void deleteTopicIndex(List<Long> indexIdList){
		Query query = em.createQuery("delete from TopicIndex o where o.id in(:id)")
		.setParameter("id",indexIdList);
		query.executeUpdate();
	}
	/**
	 * 删除所有话题索引变化标记
	 */
	public Integer deleteAllIndex(){
		Query query = em.createNativeQuery("truncate table topicindex");
		
		return query.executeUpdate();
		
	}
}
