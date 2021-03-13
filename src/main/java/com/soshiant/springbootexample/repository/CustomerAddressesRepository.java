package com.soshiant.springbootexample.repository;

import com.soshiant.springbootexample.entity.CustomerAddress;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface CustomerAddressesRepository extends JpaRepository<CustomerAddress, Long>  {

  /**
   * retrieves a customer's address info from database based on its address Id
   *
   * @param addressId address Id
   */
  Optional<CustomerAddress> findByAddressId(@Param("addressId") String addressId);


}
