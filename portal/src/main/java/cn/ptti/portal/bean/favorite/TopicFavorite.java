package cn.ptti.portal.bean.favorite;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;


/**
 * 话题收藏
 *
 */
@Entity
@Table(name="topicfavorite_0",indexes = {@Index(name="topicFavorite_1_idx", columnList="topicId,addtime")})
public class TopicFavorite extends TopicFavoriteEntity implements Serializable{

	private static final long serialVersionUID = 6369571392400904412L;
	
	

}
