package com.example.servicehub.domain.common;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.ToString;

@Getter
@MappedSuperclass
@ToString
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

	@Column(name = "created_by", updatable = false, nullable = false)
	@CreatedBy
	protected String createdBy;

	@Column(name = "created_at", nullable = false, updatable = false)
	@CreatedDate
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	protected LocalDate createdAt;

	@Column(name = "updated_at", nullable = false)
	@LastModifiedDate
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	protected LocalDate updatedAt;

	@Column(name = "modified_by")
	@LastModifiedBy
	protected String modifiedBy;
}
