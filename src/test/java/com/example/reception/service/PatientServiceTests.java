package com.example.reception.service;

import com.example.reception.dto.PatientDto;
import com.example.reception.entity.Patient;
import com.example.reception.exception.PatientAlreadyExistsException;
import com.example.reception.exception.PatientNotFoundException;
import com.example.reception.mapper.PatientMapper;
import com.example.reception.mapper.PatientMapperImpl;
import com.example.reception.repository.PatientRepository;
import com.example.reception.repository.filter.SoftDeletedFilter;
import com.example.reception.util.DataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class PatientServiceTests {

    @Mock
    PatientRepository patientRepository;

    @Spy
    PatientMapper patientMapper = new PatientMapperImpl();

    @Mock
    SoftDeletedFilter filter;

    @InjectMocks
    PatientServiceImpl patientService;

    @Test
    @DisplayName("Test get patient by id functionality")
    void givenId_whenGetById_thenPatientIsReturned() {
        // given
        Patient patient = DataUtils.getJohnDoePersisted();
        given(filter.filterName(anyString())).willReturn(filter);
        given(filter.filterParameter(anyString())).willReturn(filter);
        given(filter.apply(ArgumentMatchers.<Supplier<Optional<Patient>>>any(), anyBoolean()))
                .willReturn(Optional.of(patient));
        // when
        Patient foundPatient = patientService.findById(patient.getId());
        // then
        assertThat(foundPatient).isNotNull();
        assertThat(foundPatient.getId()).isEqualTo(patient.getId());
    }

    @Test
    @DisplayName("Test get patient by invalid id functionality")
    void givenInvalidId_whenGetById_thenExceptionIsThrown() {
        // given
        given(filter.filterName(anyString())).willReturn(filter);
        given(filter.filterParameter(anyString())).willReturn(filter);
        given(filter.apply(ArgumentMatchers.<Supplier<Optional<Patient>>>any(), anyBoolean()))
                .willThrow(PatientNotFoundException.class);
        // when
        Throwable thrown = catchThrowable(() -> patientService.findById(anyLong()));
        // then
        assertThat(thrown).isInstanceOf(PatientNotFoundException.class);
    }

    @Test
    @DisplayName("Test get all patients functionality")
    void givenPatients_whenGetAll_thenActivePatientsAreReturned() {
        // given
        Patient patient1 = DataUtils.getJohnDoePersisted();
        Patient patient2 = DataUtils.getAdamSmithPersisted();
        given(filter.filterName(anyString())).willReturn(filter);
        given(filter.filterParameter(anyString())).willReturn(filter);
        given(filter.apply(ArgumentMatchers.<Supplier<List<Patient>>>any(), anyBoolean()))
                .willReturn(List.of(patient1, patient2));
        // when
        List<Patient> patients = patientService.findAll();
        // then
        assertThat(patients).hasSize(2);
    }

    @Test
    @DisplayName("Test save patient functionality")
    void givenPatientToSave_whenSavePatient_thenRepositoryIsCalled() {
        // given
        PatientDto patientDto = DataUtils.getJohnDoeDto();
        given(patientRepository.existsByEmail(anyString())).willReturn(false);
        given(patientRepository.save(any(Patient.class))).willReturn(DataUtils.getJohnDoePersisted());
        // when
        Patient saved = patientService.create(patientDto);
        // then
        then(patientRepository).should().save(any(Patient.class));
        assertThat(saved).isNotNull();
    }

    @Test
    @DisplayName("Test save patient with duplicate email functionality")
    void givenPatientToSaveWithDuplicateEmail_whenSavePatient_thenExceptionIsThrown() {
        // given
        PatientDto patient = DataUtils.getJohnDoeDto();
        given(patientRepository.existsByEmail(anyString())).willReturn(true);
        // when
        Throwable thrown = catchThrowable(() -> patientService.create(patient));
        // then
        then(patientRepository).should(never()).save(any(Patient.class));
        assertThat(thrown).isInstanceOf(PatientAlreadyExistsException.class);
    }

    @Test
    @DisplayName("Test update patient functionality")
    void givenPatientToUpdate_whenUpdatePatient_thenRepositoryIsCalled() {
        // given
        Patient patient = DataUtils.getJohnDoePersisted();
        PatientDto patientDto = DataUtils.getJohnDoeDto();
        given(filter.filterName(anyString())).willReturn(filter);
        given(filter.filterParameter(anyString())).willReturn(filter);
        given(filter.apply(ArgumentMatchers.<Supplier<Optional<Patient>>>any(), anyBoolean()))
                .willReturn(Optional.of(patient));
        // when
        Patient updatedPatient = patientService.update(patient.getId(), patientDto);
        // then
        then(patientMapper).should().updateFromDto(any(PatientDto.class), any(Patient.class));
        assertThat(updatedPatient).isNotNull();
        assertThat(updatedPatient.getId()).isEqualTo(patient.getId());
    }

    @Test
    @DisplayName("Test update patient with incorrect id functionality")
    void givenPatientToUpdateWithIncorrectId_whenUpdatePatient_thenExceptionIsThrown() {
        // given
        PatientDto patientDto = DataUtils.getJohnDoeDto();
        given(filter.filterName(anyString())).willReturn(filter);
        given(filter.filterParameter(anyString())).willReturn(filter);
        given(filter.apply(ArgumentMatchers.<Supplier<Optional<Patient>>>any(), anyBoolean()))
                .willThrow(PatientNotFoundException.class);
        // when
        Throwable thrown = catchThrowable(() -> patientService.update(anyLong(), patientDto));
        // then
        then(patientMapper).should(never()).updateFromDto(any(PatientDto.class), any(Patient.class));
        assertThat(thrown).isInstanceOf(PatientNotFoundException.class);
    }

    @Test
    @DisplayName("Test update patient with duplicate email functionality")
    void givenPatientToUpdateWithDuplicateEmail_whenUpdatePatient_thenExceptionIsThrown() {
        // given
        PatientDto patientDto = DataUtils.getJohnDoeDto();
        given(filter.filterName(anyString())).willReturn(filter);
        given(filter.filterParameter(anyString())).willReturn(filter);
        given(filter.apply(ArgumentMatchers.<Supplier<Optional<Patient>>>any(), anyBoolean()))
                .willReturn(Optional.of(DataUtils.getAdamSmithPersisted()));
        given(patientRepository.existsByEmail(anyString())).willReturn(true);
        // when
        Throwable thrown = catchThrowable(() -> patientService.update(anyLong(), patientDto));
        // then
        then(patientMapper).should(never()).updateFromDto(any(PatientDto.class), any(Patient.class));
        assertThat(thrown).isInstanceOf(PatientAlreadyExistsException.class);
    }

    @Test
    @DisplayName("Test delete by id patient functionality")
    void delete() {
        // given

        // when
        patientService.delete(anyLong());
        // then
        then(patientRepository).should().deleteById(anyLong());
    }
}
