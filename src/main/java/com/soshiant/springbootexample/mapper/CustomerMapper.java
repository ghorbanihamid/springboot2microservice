package com.soshiant.springbootexample.mapper;

import com.soshiant.springbootexample.dto.CustomerDto;
import com.soshiant.springbootexample.dto.CustomerUpdateDto;
import com.soshiant.springbootexample.entity.Customer;
import com.soshiant.springbootexample.entity.CustomerAddress;
import com.soshiant.springbootexample.util.AppConstants;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = {LocalDateTime.class}
)
public abstract class CustomerMapper {

  @Mapping( target = "createdDate", expression  = "java(getCurrentDateTime())")
  @Mapping( source = "dto", target = "addresses")
  public abstract Customer toCustomer(CustomerDto dto);

  @Mapping( source = "streetNumber", target = "streetNumber")
  @Mapping( source = "streetName",   target = "streetName")
  @Mapping( source = "city",         target = "cityName")
  @Mapping( source = "state",        target = "stateName")
  @Mapping( source = "country",      target = "countryName")
  @Mapping( target = "createdDate",  expression = "java(getCurrentDateTime())")
  public abstract CustomerAddress mapToAddress(CustomerDto dto);

  @Mapping( source = "address.cityName", target = "city")
  @Mapping( source = "address.stateName", target = "state")
  @Mapping( source = "address.countryName", target = "country")
  public abstract CustomerDto toCustomerDto(Customer customer, CustomerAddress address);

  List<CustomerAddress> mapToListOfAddresses(CustomerDto customerDto) {

    List<CustomerAddress> list = new ArrayList<>();
    if (customerDto == null) {
      return list;
    }
    list.add(mapToAddress(customerDto));
    return list;
  }

  LocalDateTime getCurrentDateTime() {

    ZoneId zoneId = ZoneId.of(AppConstants.PACIFIC_TIME_ZONE);
    Instant now = Instant.now();
    ZonedDateTime currentDateTimeInPST = now.atZone( zoneId );
    return currentDateTimeInPST.toLocalDateTime();
  }

  @Mapping( target = "modifiedDate", expression  = "java(getCurrentDateTime())")
  @BeanMapping( nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE )
  public abstract void toCustomerForUpdate(CustomerUpdateDto customerDto, @MappingTarget Customer customer);

}
