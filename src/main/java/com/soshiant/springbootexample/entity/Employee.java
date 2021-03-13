package com.soshiant.springbootexample.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
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
@Table(name = "EMPLOYEES", schema = "EMP")
public class Employee extends AuditModel {

  @Id
  @SequenceGenerator(name = "empSeqGen", sequenceName = "mySeq", initialValue = 1000, allocationSize = 1)
  @GeneratedValue(generator = "empSeqGen")
  @Column(name = "EMPLOYEE_ID",updatable = false)
  private Long employeeId;

  @Column(name = "FIRST_NAME")
  private String firstName;

  @Column(name = "LAST_NAME")
  private String lastName;

  @Column(name = "PHONE_NUMBER")
  private String phoneNumber;

  @Column(name = "EMAIL_ADDRESS")
  private String emailAddress;

  @Column(name = "BIRTH_DATE")
  private LocalDate birthDate;

}
