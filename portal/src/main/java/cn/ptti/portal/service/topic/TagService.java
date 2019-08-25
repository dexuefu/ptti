package cn.ptti.portal.service.topic;

import cn.ptti.portal.bean.topic.Tag;
import cn.ptti.portal.service.besa.DAO;

import java.util.List;

/**
 * 标签
 *
 */
public interface TagService extends DAO<Tag>{
	/**
	 * 根据Id查询标签
	 * @param tagId 标签Id
	 * @return
	 */
	public Tag findById(Long tagId);
	/**
	 * 查询所有标签
	 * @return
	 */
	public List<Tag> findAllTag();
	/**
	 * 查询所有标签 - 缓存
	 * @return
	 */
	public List<Tag> findAllTag_cache();
	/**
	 * 保存标签
	 * @param tag
	 */
	public void saveTag(Tag tag);
	/**
	 * 修改标签
	 * @param tag
	 * @return
	 */
	public Integer updateTag(Tag tag);
	/**
	 * 删除标签
	 * @param tagId 标签Id
	 */
	public Integer deleteTag(Long tagId);
}
