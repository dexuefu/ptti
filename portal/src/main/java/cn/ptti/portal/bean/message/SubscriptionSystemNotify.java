package cn.ptti.portal.bean.message;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 订阅系统通知
 *
 */
@Entity
@Table(name="subscriptionsystemnotify_0",indexes = {@Index(name="subscriptionSystemNotify_1_idx", columnList="systemNotifyId"),@Index(name="subscriptionSystemNotify_2_idx", columnList="userId,status,systemNotifyId")})
public class SubscriptionSystemNotify extends SubscriptionSystemNotifyEntity implements Serializable{
	private static final long serialVersionUID = -6898567200600595532L;

}
