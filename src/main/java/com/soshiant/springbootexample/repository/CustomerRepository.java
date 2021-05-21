package com.soshiant.springbootexample.repository;

import com.soshiant.springbootexample.entity.Customer;
import com.soshiant.springbootexample.entity.UserInfo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>  {

  /**
   * retrieves a customer info from database based on customerIds
   *
   * @param customerIds customer Ids
   */
  List<Customer> findByCustomerIdIn(List<Long> customerIds);

  /**
   * retrieves a customer info from database based on his first name and last name
   *
   * @param firstName customer's first name
   * @param lastName  customer's last name
   */
  Optional<Customer> findByFirstNameAndLastName(@Param("firstName") String firstName,
      @Param("lastName")  String lastName);

  /**
   * retrieves a customer info from database based on its phone number
   *
   * @param phoneNumber customer's phone number
   */
  Optional<Customer> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);


  /**
   * retrieves a customer info from database based on its phone number
   *
   * @param emailAddress customer's email address
   */
  Optional<Customer> findByEmailAddress(String emailAddress);


  /**
   * retrieves a customer info from database based on the username
   *
   * @param username customer's username
   */
  Optional<Customer> findByUserInfo_Username(@Param("username") String username);

  /**
   * retrieves a Employee info from database based on its email address
   *
   * @param emailAddress customer's email address
   */
  @Modifying(clearAutomatically = true)
  @Query("UPDATE Customer SET EMAIL_ADDRESS = :emailAddress WHERE CUSTOMER_ID = :customerId")
  int updateEmailAddress(@Param("customerId") long customerId,
                         @Param("emailAddress") String emailAddress);


}
