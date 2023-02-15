package com.soshiant.springbootexample.mapper;

import com.soshiant.springbootexample.dto.SignupDto;
import com.soshiant.springbootexample.entity.UserInfo;
import com.soshiant.springbootexample.util.AppConstants;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = {LocalDateTime.class}
)
public abstract class UserMapper {

  @Mapping( target = "createdDate", expression  = "java(getCurrentDateTime())")
  @Mapping( target = "customer.createdDate", expression  = "java(getCurrentDateTime())")
  @Mapping( target = "userRole.createdDate", expression  = "java(getCurrentDateTime())")
  @Mapping( source = "firstName",           target = "customer.firstName")
  @Mapping( source = "lastName",            target = "customer.lastName")
  @Mapping( source = "phoneNumber",         target = "customer.phoneNumber")
  @Mapping( source = "emailAddress",        target = "customer.emailAddress")
//  @Mapping( source = "userRole",            target  = "userRole.role")
  @Mapping( source = "username",            target = "username")
  @Mapping( source = "password",            target = "password")
  public abstract UserInfo toUser(SignupDto dto);

  LocalDateTime getCurrentDateTime() {

    ZoneId zoneId = ZoneId.of(AppConstants.PACIFIC_TIME_ZONE);
    Instant now = Instant.now();
    ZonedDateTime currentDateTimeInPST = now.atZone( zoneId );
    return currentDateTimeInPST.toLocalDateTime();
  }

}
