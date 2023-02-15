package com.soshiant.springbootexample.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/*
 * Modeling With a Shared Primary Key
 * We don't need @GeneratedValue annotation with this way
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class UserRolePrimaryKey implements Serializable {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "USER_ID")
  private UserInfo userInfo;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "ROLE_ID")
  private Role role;

}
