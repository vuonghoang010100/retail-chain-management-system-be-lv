package com.example.sales_system.mapper;

import com.example.sales_system.dto.request.EmployeeCreateRequest;
import com.example.sales_system.dto.request.EmployeeUpdateRequest;
import com.example.sales_system.dto.response.EmployeeResponse;
import com.example.sales_system.entity.tenant.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = ConfigMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper {
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "stores", ignore = true)
    Employee toEmployee(EmployeeCreateRequest employeeCreateRequest);

    //    @Mapping(target = "gender",
//            expression = "java(employee.getGender() != null ? employee.getGender().toString() : null)"
//    )
    EmployeeResponse toEmployeeResponse(Employee employee);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "stores", ignore = true)
    void updateEmployee(@MappingTarget Employee employee, EmployeeUpdateRequest employeeUpdateRequest);
}
