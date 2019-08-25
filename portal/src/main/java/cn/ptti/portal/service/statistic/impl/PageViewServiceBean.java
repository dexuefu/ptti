package cn.ptti.portal.service.statistic.impl;

import cn.ptti.portal.bean.statistic.PV;
import cn.ptti.portal.service.besa.DaoSupport;
import cn.ptti.portal.service.statistic.PageViewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * 页面访问量
 *
 */
@Service
@Transactional
public class PageViewServiceBean extends DaoSupport<PV> implements PageViewService{

	/**
	 * 保存访问量
	 * @param pvList 访问量集合
	 */
	public void savePageView(List<PV> pvList){
		if(pvList != null && pvList.size() >0){
			for(PV pv : pvList){
				this.save(pv);
			}
			
		}
		
	}
	
	/**
	 * 删除访问量
	 * @param endTime 结束时间
	 */
	public void deletePageView(Date endTime){
		Query query = em.createQuery("delete from PV o where o.times<:date")
				.setParameter("date", endTime);
			query.executeUpdate();

	}
	
}
