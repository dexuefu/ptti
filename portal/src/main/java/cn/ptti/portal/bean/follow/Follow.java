package cn.ptti.portal.bean.follow;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;


/**
 * 关注
 *
 */
@Entity
@Table(name="follow_0",indexes = {@Index(name="follow_1_idx", columnList="userName,addtime")})
public class Follow extends FollowEntity implements Serializable{
	private static final long serialVersionUID = -6073764011318403096L;
	

}
