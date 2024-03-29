package com.soshiant.springbootexample.service.impl;

import static com.soshiant.springbootexample.util.AppTestConstants.CUSTOMER_ID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.soshiant.springbootexample.dto.CustomerRequestDto;
import com.soshiant.springbootexample.dto.CustomerUpdateDto;
import com.soshiant.springbootexample.entity.Customer;
import com.soshiant.springbootexample.exception.CustomerServiceException;
import com.soshiant.springbootexample.repository.CustomerRepository;
import com.soshiant.springbootexample.util.DataUtils;
import com.soshiant.springbootexample.util.LocalDateSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ActiveProfiles("test")
@ExtendWith({SpringExtension.class, MockitoExtension.class})
class CustomerServiceImplTest {

  @Mock
  private CustomerRepository customerRepository;

  @InjectMocks
  private CustomerServiceImpl customerService;

  @MockBean
  private PasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(customerService,"passwordEncoder",passwordEncoder);
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  @DisplayName("Test Register new customer")
  void testRegisterNewCustomer() throws Exception {

    Mockito.when(customerRepository.save(any(Customer.class)))
        .thenReturn(DataUtils.buildCustomerObject());

    Customer result = customerService.registerCustomer(DataUtils.buildCustomerDto());
    assertThat(result, is(notNullValue()));
    assertThat(result.getId(), CoreMatchers.equalTo(CUSTOMER_ID));

  }

  @Test
  @DisplayName("Test Register new customer")
  void testRegisterNewCustomerThrowsException() {

    Mockito.when(customerRepository.save(any(Customer.class)))
        .thenThrow(new QueryTimeoutException("QueryTimeout Exception"));

    Throwable thrown = assertThrows(
        CustomerServiceException.class,
        () -> customerService.registerCustomer(new CustomerRequestDto()));

    assertEquals("QueryTimeout Exception",thrown.getMessage());

  }

  @Test
  void testUpdateCustomerSuccess() throws Exception {

    Optional<Customer> customer = Optional.of(DataUtils.buildCustomerObject());
    Mockito.when(customerRepository.findById(anyLong())).thenReturn(customer);
    Mockito.when(customerRepository.save(any(Customer.class)))
        .thenReturn(DataUtils.buildCustomerObject());
    CustomerUpdateDto customerUpdateDto = new CustomerUpdateDto();
    customerUpdateDto.setCustomerId(1L);
    Customer savedCustomer = customerService.updateCustomerInfo(customerUpdateDto);
    assertThat(savedCustomer, CoreMatchers.is(notNullValue()));
    verify(customerRepository, times(1)).findById(anyLong());
    verify(customerRepository, times(1)).save(any(Customer.class));

  }

  @Test
  void testUpdateCustomerThrowsException() {

    Optional<Customer> customer = Optional.of(DataUtils.buildCustomerObject());
    Mockito.when(customerRepository.findById(anyLong())).thenReturn(customer);
    Mockito.when(customerRepository.save(any(Customer.class)))
        .thenThrow(new QueryTimeoutException("QueryTimeout Exception"));

    CustomerUpdateDto customerUpdateDto = new CustomerUpdateDto();
    customerUpdateDto.setCustomerId(1L);
    Throwable thrown = assertThrows(
        CustomerServiceException.class,
        () -> customerService.updateCustomerInfo(customerUpdateDto));

    assertEquals("QueryTimeout Exception",thrown.getMessage());
  }

  @Test
  void testGetCustomerByValidId() throws Exception {

    Mockito.when(customerRepository.findById(CUSTOMER_ID))
        .thenReturn(Optional.of(DataUtils.buildCustomerObject()));
    ArrayList<Long> customerIds = new ArrayList<>();
    customerIds.add(CUSTOMER_ID);
    List<Customer> customerList = customerService.getCustomers(customerIds);

    assertThat(customerList.size(), greaterThanOrEqualTo(1));
    assertThat(customerList.get(0).getId(), CoreMatchers.equalTo(CUSTOMER_ID));

  }

  @Test
  void testGetCustomerByOneInvalidId() throws Exception {

    Mockito.when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

    ArrayList<Long> customerIds = new ArrayList<>();
    customerIds.add(CUSTOMER_ID);
    List<Customer> customerList = customerService.getCustomers(customerIds);
    assertThat(customerList.isEmpty(),CoreMatchers.is(true));

  }

  @Test
  void testGetCustomerByMultipleInvalidId() throws Exception {

    Mockito.when(customerRepository.findByIdIn(anyList())).thenReturn(new ArrayList<>());

    ArrayList<Long> customerIds = new ArrayList<>();
    customerIds.add(CUSTOMER_ID);
    customerIds.add(CUSTOMER_ID);
    List<Customer> customerList = customerService.getCustomers(customerIds);
    assertThat(customerList.isEmpty(),CoreMatchers.is(true));

  }


}