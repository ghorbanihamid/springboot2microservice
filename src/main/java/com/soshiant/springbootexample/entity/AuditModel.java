package com.soshiant.springbootexample.entity;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditModel implements Serializable {

  private static final long serialVersionUID = 1213546532135L;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "CREATED_DATE", nullable = false, updatable = false)
  @CreatedDate
  private Calendar createdDate;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "MODIFIED_DATE")
  @LastModifiedDate
  private Calendar lastModifiedDate;

}
