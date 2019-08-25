package cn.ptti.portal.bean.user;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 会员等级
 *
 */
@Entity
public class UserGrade implements Serializable{
	private static final long serialVersionUID = 526330864606455854L;
	
	/** ID **/
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	/** 等级名称 **/
	@Column(length=50)
	private String name;
	/** 需要积分 **/
	private Long needPoint;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getNeedPoint() {
		return needPoint;
	}
	public void setNeedPoint(Long needPoint) {
		this.needPoint = needPoint;
	}

}
