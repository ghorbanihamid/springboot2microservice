package com.soshiant.springbootexample.mapper;

import com.soshiant.springbootexample.dto.EmployeeRequestDto;
import com.soshiant.springbootexample.entity.Employee;
import com.soshiant.springbootexample.entity.EmployeeAddress;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = {LocalDateTime.class}
)
public abstract class EmployeeMapper {


  public abstract Employee toEmployee(EmployeeRequestDto dto);

  List<EmployeeAddress> mapToListOfAddresses(EmployeeRequestDto customerDto) {

    List<EmployeeAddress> list = new ArrayList<>();
    if (customerDto == null) {
      return list;
    }
    list.add(mapToAddress(customerDto));
    return list;
  }

  @Mapping( source = "streetNumber", target = "streetNumber")
  @Mapping( source = "streetName",   target = "streetName")
  @Mapping( source = "city",         target = "cityName")
  @Mapping( source = "state",        target = "stateName")
  @Mapping( source = "country",      target = "countryName")
  public abstract EmployeeAddress mapToAddress(EmployeeRequestDto dto);


  @Mapping( source = "city", target = "cityName")
  @Mapping( source = "state", target = "stateName")
  @Mapping( source = "country", target = "countryName")
  @Mapping( source = "firstName", target = "employee.firstName")
  @Mapping( source = "lastName", target = "employee.lastName")
  @Mapping( source = "phoneNumber", target = "employee.phoneNumber")
  @Mapping( source = "emailAddress", target = "employee.emailAddress")
  @Mapping( source = "birthDate", target = "employee.birthDate")
  public abstract EmployeeAddress toEmployeeWithAddress(EmployeeRequestDto dto);

  @Mapping( source = "address.cityName", target = "city")
  @Mapping( source = "address.stateName", target = "state")
  @Mapping( source = "address.countryName", target = "country")
  public abstract EmployeeRequestDto toEmployeeDto(Employee employee, EmployeeAddress address);

}
