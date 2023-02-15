package com.soshiant.springbootexample.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.soshiant.springbootexample.util.LocalDateDeserializer;
import com.soshiant.springbootexample.util.LocalDateSerializer;
import com.soshiant.springbootexample.util.LocalDateTimeDeserializer;
import com.soshiant.springbootexample.util.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CUSTOMERS", schema = "CST")
public class Customer implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
 // this prevents OneToOne or OneToMany mapping
//  @Column(name = "CUSTOMER_ID", nullable = false, updatable = false)
  private Long id;

  @Column(name = "FIRST_NAME", nullable = false)
  private String firstName;

  @Column(name = "LAST_NAME", nullable = false)
  private String lastName;

  @Column(name = "PHONE_NUMBER", nullable = true)
  private String phoneNumber;

  @Column(name = "EMAIL_ADDRESS", nullable = true)
  private String emailAddress;

  @JsonDeserialize(using = LocalDateDeserializer.class)
  @JsonSerialize(using = LocalDateSerializer.class)
  @Column(name = "BIRTH_DATE", nullable = true)
  private LocalDate birthDate;

  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @Column(name = "CREATED_DATE", nullable = false, updatable = false)
  private LocalDateTime createdDate;

  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @Column(name = "MODIFIED_DATE",nullable = true, updatable = true)
  private LocalDateTime modifiedDate;

//  @OneToOne(mappedBy = "customerInfo",cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
//  @JsonManagedReference
//  private UserInfo userInfo;

  @OneToMany(targetEntity= CustomerAddress.class, cascade = CascadeType.ALL,
             fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "customer_id", referencedColumnName = "id")
  private List<CustomerAddress> addresses = new ArrayList<>();

}
