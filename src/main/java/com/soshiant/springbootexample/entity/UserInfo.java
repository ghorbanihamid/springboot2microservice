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
@Table(name = "USERS", schema = "CST")
@DiscriminatorValue("USERS")
public class UserInfo implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
// this prevents OneToOne or OneToMany mapping
//  @Column(name = "USER_ID", nullable = false, updatable = false)
  private Long id;

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

  @Column(name = "MODIFIED_DATE")
  private LocalDateTime modifiedDate;

  @Transient
  private String jwtToken;

  @OneToOne(targetEntity= Customer.class,optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "id")
  private Customer customer;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinTable(name = "USER_ROLE",schema = "CST",
             joinColumns = @JoinColumn(name = "USER_ID"),
             inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
  private Set<Role> roles = new HashSet<>();

}
