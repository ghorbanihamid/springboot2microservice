package com.soshiant.springbootexample.service;


import com.soshiant.springbootexample.dto.CustomerDto;
import com.soshiant.springbootexample.dto.CustomerUpdateDto;
import com.soshiant.springbootexample.entity.Customer;
import com.soshiant.springbootexample.exception.CustomerServiceException;
import java.util.List;

/**
 *
 * @author Hamid.Ghorbani
 *
 */
public interface CustomerService {

  /**
   * @param customerDto customer object
   * @return CustomerId CustomerId
   */
  Customer registerCustomer(CustomerDto customerDto) throws CustomerServiceException;

  /**
   * @param customerId customer Id
   * @param emailAddress email address
   * @return boolean
   */
  boolean updateCustomerEmailAddress(long customerId, String emailAddress) throws CustomerServiceException;

  /**
   * @param customerDto customer object
   * @return CustomerId CustomerId
   */
  Customer updateCustomerInfo(CustomerUpdateDto customerDto) throws CustomerServiceException;

  /**
   * @param customerIds customerIds
   * @return List of Customer Object
   */
  List<Customer> getCustomers(List<Long> customerIds) throws CustomerServiceException;

  /**
   * @param firstName first name
   * @param lastName last name
   * @return Customer Customer Object
   */
  Customer getCustomer(String firstName, String lastName) throws CustomerServiceException;

  /**
   * @param phoneNumber phone number
   * @return Customer Customer Object
   */
  Customer getCustomer(String phoneNumber) throws CustomerServiceException;

}
