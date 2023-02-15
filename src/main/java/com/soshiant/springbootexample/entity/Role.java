package com.soshiant.springbootexample.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/*
 * Modeling With a Shared Primary Key
 * We don't need @GeneratedValue annotation with this way
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ROLES", schema = "CST")
@DiscriminatorValue("ROLES")
public class Role implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
 // this prevents OneToOne or OneToMany mapping
//  @Column(name = "ROLE_ID", nullable = false, updatable = false)
  private Long id;

  @Column(name = "ROLE_NAME", nullable = false)
  private String roleName;

  @Column(name = "ACTIVE", nullable = false)
  private boolean active;

  @Column(name = "CREATED_DATE", nullable = false, updatable = false)
  private LocalDateTime createdDate;

  @Column(name = "MODIFIED_DATE",nullable = true, updatable = true)
  private LocalDateTime modifiedDate;

  @ManyToMany(mappedBy = "roles")
  private Set<UserInfo> users = new HashSet<>();
}
