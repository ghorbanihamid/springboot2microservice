package com.soshiant.springbootexample.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
 * Modeling With a Shared Primary Key
 * We don't need @GeneratedValue annotation with this way
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USERS", schema = "CST")
@DiscriminatorValue("USERS")
public class UserInfo implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "USER_ID", nullable = false, updatable = false)
  private Long userId;

  @Column(name = "USER_NAME", nullable = false)
  private String username;

  @Column(name = "PASSWORD", nullable = false)
  private String password;

  @Column(name = "LOCKED", nullable = false)
  private boolean locked;

  @Column(name = "ENABLED", nullable = false)
  private boolean enabled;

  @Column(name = "FAILED_ATTEMPT")
  private int failedAttempt;

  @Column(name = "LOCK_TIME")
  private LocalDateTime lockTime;

  @Column(name = "CREATED_DATE", nullable = false, updatable = false)
  private LocalDateTime createdDate;

  @Column(name = "MODIFIED_DATE",nullable = true, updatable = true)
  private LocalDateTime modifiedDate;

  @Transient
  private String jwtToken;

}
