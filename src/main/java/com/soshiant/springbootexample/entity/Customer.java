package com.soshiant.springbootexample.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.soshiant.springbootexample.util.LocalDateDeserializer;
import com.soshiant.springbootexample.util.LocalDateSerializer;
import com.soshiant.springbootexample.util.LocalDateTimeDeserializer;
import com.soshiant.springbootexample.util.LocalDateTimeSerializer;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CUSTOMERS", schema = "CST")
public class Customer implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "CUSTOMER_ID", nullable = false, updatable = false)
  private Long customerId;

  @Column(name = "FIRST_NAME", nullable = false)
  private String firstName;

  @Column(name = "LAST_NAME", nullable = false)
  private String lastName;

  @Column(name = "PHONE_NUMBER", nullable = false)
  private String phoneNumber;

  @Column(name = "EMAIL_ADDRESS", nullable = false)
  private String emailAddress;

  @JsonDeserialize(using = LocalDateDeserializer.class)
  @JsonSerialize(using = LocalDateSerializer.class)
  @Column(name = "BIRTH_DATE", nullable = false)
  private LocalDate birthDate;

  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @Column(name = "CREATED_DATE", nullable = false, updatable = false)
  private LocalDateTime createdDate;

  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @Column(name = "MODIFIED_DATE",nullable = true, updatable = true)
  private LocalDateTime modifiedDate;

  @OneToOne(targetEntity= UserInfo.class,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
  private UserInfo userInfo;

  @OneToMany(targetEntity= CustomerAddress.class, cascade = CascadeType.ALL,
             fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "customerId", referencedColumnName = "CUSTOMER_ID")
  private List<CustomerAddress> addresses = new ArrayList<>();

}
