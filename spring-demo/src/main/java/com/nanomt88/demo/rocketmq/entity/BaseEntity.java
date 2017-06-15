package com.nanomt88.demo.rocketmq.entity;

import com.swwx.charm.commons.lang.base.BasicTO;

import javax.persistence.*;
import java.util.Date;

/**
 * @author xu.han
 * @ClassName: BaseEntity
 * @date 16/3/25 下午4:29
 */
@MappedSuperclass
public class BaseEntity extends BasicTO {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;


	@Column(insertable = false, updatable = false)
	private Date createTime;

	@Column(insertable = false, updatable = false)
	private Date lastUpdateTime;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
}
