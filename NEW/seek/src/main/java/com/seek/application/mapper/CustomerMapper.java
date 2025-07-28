package com.seek.application.mapper;

import com.seek.application.dto.CustomerRequest;
import com.seek.application.dto.CustomerResponse;
import com.seek.domain.model.Customer;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper MapStruct para transformar entre entidades del dominio y DTOs.
 * Se genera automáticamente en tiempo de compilación.
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CustomerMapper {

    /* ---------- Solicitud → Entidad ---------- */
    Customer toEntity(CustomerRequest dto);

    /* ---------- Entidad → Respuesta ---------- */
    @Mapping(target = "age", expression = "java(entity.getAge())")
    CustomerResponse toResponse(Customer entity);
}
