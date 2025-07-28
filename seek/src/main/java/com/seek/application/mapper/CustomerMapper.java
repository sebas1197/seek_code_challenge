package com.seek.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.seek.application.dto.CustomerRequest;
import com.seek.application.dto.CustomerResponse;
import com.seek.domain.model.Customer;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

    Customer toEntity(CustomerRequest dto);

    @Mapping(target = "age", expression = "java(entity.getAge())")
    CustomerResponse toResponse(Customer entity);
}
