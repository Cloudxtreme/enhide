package com.enhide.models.persistent;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.Version;

/**
 *
 * @author sales@enhide.com
 */
@MappedSuperclass
public class Base {

	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	public Date createdAt;
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	@Column(name = "updated_at")
	public Date updatedAt;

	@Version
	public int version;

	@PrePersist
	void createdAt() {
		this.createdAt = this.updatedAt = new Date();
	}

	@PreUpdate
	void updatedAt() {
		this.updatedAt = new Date();
	}
}
