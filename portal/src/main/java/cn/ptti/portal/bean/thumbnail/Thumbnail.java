package cn.ptti.portal.bean.thumbnail;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 缩略图
 *
 */
@Entity
@Table(name="thumbnail",uniqueConstraints = {
		@UniqueConstraint(columnNames={"specificationGroup"}
   )}
)
public class Thumbnail implements Serializable{
	private static final long serialVersionUID = 2981137798741140950L;
	
	/** Id **/
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	/** 缩略图名称 **/
	@Column(length=200)
	private String name;
	/** 宽 **/
	private Integer width;
	/** 高 **/
	private Integer high;
	/** 规格组 **/
	@Column(length=25)
	private String specificationGroup;
	
	
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
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public Integer getHigh() {
		return high;
	}
	public void setHigh(Integer high) {
		this.high = high;
	}
	public String getSpecificationGroup() {
		return specificationGroup;
	}
	public void setSpecificationGroup(String specificationGroup) {
		this.specificationGroup = specificationGroup;
	}
	
}
