package cn.ptti.portal.bean.user;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 注册禁止的用户名称
 *
 */
@Entity
public class DisableUserName implements Serializable{
	private static final long serialVersionUID = -1209741644912978336L;
	
	/** ID **/
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	/** 禁止的用户名称 **/
	@Column(length=30)
	private String name;
	
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
	
	
}
