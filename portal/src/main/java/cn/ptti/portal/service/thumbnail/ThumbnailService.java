package cn.ptti.portal.service.thumbnail;

import cn.ptti.portal.bean.thumbnail.Thumbnail;
import cn.ptti.portal.service.besa.DAO;

import java.util.List;

/**
 * 缩略图DAO接口
 *
 */
public interface ThumbnailService extends DAO<Thumbnail> {
	/**
	 * 查询所有缩略图
	 * @return
	 */
	public List<Thumbnail> findAllThumbnail();
	/**
	 * 查询所有缩略图 缓存
	 * @return
	 */
	public List<Thumbnail> findAllThumbnail_cache();
	/**
	 * 根据缩略图Id查询缩略图
	 * @param thumbnailId
	 * @return
	 */
	public Thumbnail findByThumbnailId(Integer thumbnailId);
	/**
	 * 根据规格组查询缩略图
	 * @param specificationGroup 规格组
	 * @return
	 */
	public Thumbnail findThumbnailBySpecificationGroup(String specificationGroup);
	/**
	 * 保存缩略图
	 * @param thumbnail
	 */
	public void saveThumbnail(Thumbnail thumbnail);
	/**
	 * 删除缩略图
	 * @param thumbnailId 缩略图Id
	 */
	public int deleteThumbnail(Integer thumbnailId);
}
