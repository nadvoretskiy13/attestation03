package com.example.reception.mapper;

import com.example.reception.entity.Patient;
import com.example.reception.dto.PatientDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PatientMapper {
    Patient toEntity(PatientDto patientDto);

    PatientDto toDto(Patient patient);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Patient updateFromDto(PatientDto patientDto, @MappingTarget Patient patient);
}