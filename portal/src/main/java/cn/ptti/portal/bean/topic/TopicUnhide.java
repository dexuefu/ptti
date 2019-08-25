package cn.ptti.portal.bean.topic;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;


/**
 * 话题取消隐藏
 *
 */
@Entity
@Table(name="topicunhide_0",indexes = {@Index(name="topicUnhide_1_idx", columnList="topicId,cancelTime")})
public class TopicUnhide extends UnhideEntity implements Serializable{

	private static final long serialVersionUID = 6455873305843959259L;

	
}
