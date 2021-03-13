package com.soshiant.springbootexample.entity;

import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "EMPLOYEE_ADDRESSES", schema = "EMP")
public class EmployeeAddress extends AuditModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ADDRESS_ID", nullable = false, updatable = false)
  private Long addressId;

  @Column(name = "STREET_NUMBER", nullable = false)
  private String streetNumber;

  @Column(name = "STREET_NAME", nullable = false)
  private String streetName;

  @Column(name = "CITY_NAME", nullable = false)
  private String cityName;

  @Column(name = "STATE_NAME", nullable = false)
  private String stateName;

  @Column(name = "COUNTRY_NAME", nullable = false)
  private String countryName;

  @Column(name = "ZIP_CODE", nullable = false)
  private String zipCode;

  @ManyToOne(targetEntity=Employee.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name="EMPLOYEE_ID", referencedColumnName = "EMPLOYEE_ID")
  private Employee employee;

}
