package cn.ptti.portal.service.links;

import cn.ptti.portal.bean.links.Links;
import cn.ptti.portal.service.besa.DAO;

import java.util.List;

/**
 * 友情链接
 *
 */
public interface LinksService extends DAO<Links>{
	/**
	 * 根据Id查询友情链接
	 * @param linkId 友情链接Id
	 * @return
	 */
	public Links findById(Integer linkId);
	/**
	 * 查询所有友情链接
	 * @return
	 */
	public List<Links> findAllLinks();
	
	/**
	 * 查询所有友情链接 - 缓存
	 * @return
	 */
	public List<Links> findAllLinks_cache();

	
	/**
	 * 保存友情链接
	 * @param links
	 */
	public void saveLinks(Links links);
	

	/**
	 * 修改友情链接
	 * @param links
	 * @return
	 */
	public Integer updateLinks(Links links);
	/**
	 * 删除友情链接
	 * @param linkId 友情链接Id
	 */
	public Integer deleteLinks(Integer linkId);
}
