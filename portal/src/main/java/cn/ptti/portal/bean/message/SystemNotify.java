package cn.ptti.portal.bean.message;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统通知
 *
 */
@Entity
@Table(name="systemnotify",indexes = {@Index(name="systemNotify_1_idx", columnList="sendTime")})
public class SystemNotify implements Serializable{
	private static final long serialVersionUID = 796537226963121494L;
	
	/** ID **/
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/** 员工名称 **/
	@Column(length=30)
	private String staffName;
	/** 通知内容 **/
	@Lob
	private String content;

	/** 发送时间 **/
	@Temporal(TemporalType.TIMESTAMP)
	private Date sendTime = new Date();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}


}
