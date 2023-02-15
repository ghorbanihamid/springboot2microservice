package com.soshiant.springbootexample.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/*
 * Modeling With a Shared Primary Key
 * We don't need @GeneratedValue annotation with this way
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USERS_ROLES", schema = "CST")
public class UserRole implements Serializable {

  @EmbeddedId
  private UserRolePrimaryKey id;

  @Column(name = "ENABLED", nullable = false)
  private boolean enabled;

  @Column(name = "CREATED_DATE")
  @CreationTimestamp
  private LocalDateTime createdDate;

  @Column(name = "MODIFIED_DATE")
  private LocalDateTime modifiedDate;

}
