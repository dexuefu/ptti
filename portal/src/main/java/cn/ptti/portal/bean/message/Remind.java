package cn.ptti.portal.bean.message;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;


/**
 * 提醒
 *
 */
@Entity
@Table(name="remind_0",indexes = {@Index(name="remind_1_idx", columnList="receiverUserId,status,sendTimeFormat"),@Index(name="remind_2_idx", columnList="topicId"),@Index(name="remind_3_idx", columnList="receiverUserId,typeCode,sendTimeFormat")})
public class Remind extends RemindEntity implements Serializable{
	private static final long serialVersionUID = 3141074310515107936L;
}
