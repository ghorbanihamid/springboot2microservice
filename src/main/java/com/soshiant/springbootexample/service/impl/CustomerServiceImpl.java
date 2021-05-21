package com.soshiant.springbootexample.service.impl;

import com.soshiant.springbootexample.dto.CustomerRequestDto;
import com.soshiant.springbootexample.dto.CustomerUpdateDto;
import com.soshiant.springbootexample.entity.Customer;
import com.soshiant.springbootexample.entity.UserInfo;
import com.soshiant.springbootexample.exception.CustomerServiceException;
import com.soshiant.springbootexample.mapper.CustomerMapper;
import com.soshiant.springbootexample.repository.CustomerRepository;
import com.soshiant.springbootexample.service.CustomerService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Hamid.Ghorbani
 *
 */
@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

  private final  CustomerMapper customerMapper = Mappers.getMapper(CustomerMapper.class);

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public Customer registerCustomer(CustomerRequestDto customerRequestDto) throws CustomerServiceException {
    log.info("register new customer {}", customerRequestDto);

    Customer customerInfo;
    try {
      customerRequestDto.setPassword(passwordEncoder.encode(customerRequestDto.getPassword()));
      customerInfo = customerMapper.toCustomer(customerRequestDto);
      return customerRepository.save(customerInfo);

    } catch (Exception e) {
      log.error(
          "Exception occurred during registerCustomer method for customerDto : {} with message :{} ",
          customerRequestDto, e.getCause());
      throw new CustomerServiceException(e.getMessage());
    }
  }

  @Override
  public boolean updateCustomerEmailAddress(long customerId, String emailAddress) throws CustomerServiceException {

    log.info("updating customer {} 's email address {}", customerId, emailAddress);
    try {
      int affectedRecord = customerRepository.updateEmailAddress(customerId, emailAddress);
      return affectedRecord == 1;

    } catch (Exception e) {
      log.error(
          "Exception occurred during updateCustomerInfo method for customer:{} with message :{} ",
          customerId, e.getCause());
      throw new CustomerServiceException(e.getMessage());
    }

  }

  @Override
  public Customer updateCustomerInfo(CustomerUpdateDto customerDto) throws CustomerServiceException {

    log.info("updating customer info {}", customerDto);
    Customer customerInfo;
    try {
      Optional<Customer> customerOptional = customerRepository.findById(customerDto.getCustomerId());
      if(customerOptional.isPresent()) {
        customerInfo = customerOptional.get();
        customerMapper.toCustomerForUpdate(customerDto, customerInfo);
        return customerRepository.save(customerInfo);
      } else {
        return null;
      }

    } catch (Exception e) {
      log.error(
          "Exception occurred during updateCustomerInfo method for customer:{} with message :{} ",
          customerDto.toString(), e.getCause());
      throw new CustomerServiceException(e.getMessage());
    }
  }

  @Override
  public List<Customer> getCustomers(List<Long> customerIds) throws CustomerServiceException {

    List<Customer> customerList;
    try {
      if(customerIds == null || customerIds.isEmpty()){
        customerList = customerRepository.findAll();
      }
      else if (customerIds.size() == 1) {
        Optional<Customer> customerOptional = customerRepository.findById(customerIds.get(0));
        customerList = new ArrayList<>();
        customerOptional.ifPresent(customerList::add);
      }
      else {
        customerList = customerRepository.findByCustomerIdIn(customerIds);
      }

      return customerList;

    } catch (Exception e){
      log.error("Exception in getCustomer, customerIds: {}, message: {}. ",
          customerIds, e.getMessage());

      throw new CustomerServiceException(e.getMessage());
    }

  }

  @Override
  public Customer getCustomer(String firstName, String lastName) throws CustomerServiceException {

    try {
      Optional<Customer> customerOptional =
        customerRepository.findByFirstNameAndLastName(firstName, lastName);

    return (customerOptional.orElse(null));
    } catch (Exception e){
      log.error("Exception in getCustomer, firstName: {}, lastName: {}, message: {}. ",
                 firstName, lastName, e.getMessage());
      throw new CustomerServiceException(e.getMessage());
    }

  }

  @Override
  public Customer getCustomer(String phoneNumber) throws CustomerServiceException {

    try {
      Optional<Customer> customerOptional = customerRepository.findByPhoneNumber(phoneNumber);
      return (customerOptional.orElse(null));

    } catch (Exception e){
      log.error(
        "Exception in getCustomer, phoneNumber: {}, message: {}. ", phoneNumber, e.getMessage());
      throw new CustomerServiceException(e.getMessage());
    }

  }

  @Override
  public Customer getCustomerByUsername(String username) throws CustomerServiceException {

    try {
      Optional<Customer> customerOptional = customerRepository.findByUserInfo_Username(username);
      return (customerOptional.orElse(null));

    } catch (Exception e){
      log.error(
        "Exception in getCustomer, phoneNumber: {}, message: {}. ", username, e.getMessage());
      throw new CustomerServiceException(e.getMessage());
    }

  }

}
