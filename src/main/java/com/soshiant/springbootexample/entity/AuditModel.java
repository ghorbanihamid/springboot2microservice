package com.soshiant.springbootexample.entity;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditModel implements Serializable {

  private static final long serialVersionUID = 1213546532135L;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "CREATED_DATE", nullable = false, updatable = false)
  @CreatedDate
  private Calendar createDate;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "MODIFIED_DATE")
  @LastModifiedDate
  private Calendar lastModifiedDate;

}
