package com.soshiant.springbootexample.entity;

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
public class Customer {

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

  @Column(name = "BIRTH_DATE", nullable = false)
  private LocalDate birthDate;

  @Column(name = "CREATED_DATE", nullable = false, updatable = false)
  private LocalDateTime createdDate;

  @Column(name = "MODIFIED_DATE",nullable = true, updatable = true)
  private LocalDateTime modifiedDate;

  @OneToMany(targetEntity= CustomerAddress.class, cascade = CascadeType.ALL,
             fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "customerId", referencedColumnName = "CUSTOMER_ID")
  private List<CustomerAddress> addresses = new ArrayList<>();

}
