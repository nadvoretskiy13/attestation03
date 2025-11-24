package com.example.reception.repository;

import com.example.reception.entity.Patient;
import com.example.reception.util.DataUtils;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@DataJpaTest
class PatientRepositoryTests {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Test create patient functionality")
    public void givenPatient_whenSaved_thenCreated() {
        //given
        Patient patient = DataUtils.getJohnDoeTransient();
        //when
        Patient saved = patientRepository.save(patient);
        //then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test get patient by id functionality")
    public void givenPatientCreated_whenGetById_thenPatientIsReturned() {
        //given
        Patient patientToSave = DataUtils.getJohnDoeTransient();
        patientRepository.save(patientToSave);
        //when
        Patient obtainedPatient = patientRepository.findById(patientToSave.getId()).orElseThrow();
        //then
        assertThat(obtainedPatient).isNotNull();
        assertThat(obtainedPatient.getEmail()).isEqualTo("johndoe@example.com");
    }

    @Test
    @DisplayName("Test patient exists by email functionality")
    public void givenPatientCreated_whenExistsByEmail_thenReturnsTrue() {
        //given
        Patient patientToSave = DataUtils.getJohnDoeTransient();
        patientRepository.save(patientToSave);
        //when
        boolean patientExists = patientRepository.existsByEmail(patientToSave.getEmail());
        //then
        assertThat(patientExists).isTrue();
    }

    @Test
    @DisplayName("Test patient not found by id functionality")
    public void givenPatientIsNotCreated_whenGetById_thenOptionalIsEmpty() {
        //given

        //when
        Patient obtainedPatient = patientRepository.findById(1L).orElse(null);
        //then
        assertThat(obtainedPatient).isNull();
    }

    @Test
    @DisplayName("Test patient does not exist by email functionality")
    public void givenPatientCreated_whenExistsByEmail_thenReturnsFalse() {
        //given
        Patient patientToSave = DataUtils.getJohnDoeTransient();
        patientRepository.save(patientToSave);
        //when
        boolean patientExists = patientRepository.existsByEmail("absent@example.com");
        //then
        assertThat(patientExists).isFalse();
    }

    @Test
    @DisplayName("Test get all patients functionality")
    public void givenPatients_whenFindAll_thenListOfAllPatientsIsReturned() {
        // given
        Patient patient1 = DataUtils.getJohnDoeTransient();
        Patient patient2 = DataUtils.getAdamSmithTransient();
        Patient patient3 = DataUtils.getMikeBomTransient();

        patientRepository.saveAll(List.of(patient1, patient2, patient3));
        // when
        List<Patient> patients = patientRepository.findAll();
        //then
        assertThat(patients).hasSize(3);
    }

    @Test
    @DisplayName("Test update patient functionality")
    public void givenPatientToUpdate_whenSetNewEmail_thenEmailIsChanged() {
        //given
        String updatedEmail = "updated@example.com";
        Patient patient = DataUtils.getJohnDoeTransient();
        patientRepository.save(patient);
        //when
        patient.setEmail(updatedEmail);
        patientRepository.flush();
        entityManager.clear();
        //then
        patient = patientRepository.findById(patient.getId()).orElseThrow();
        assertThat(patient).isNotNull();
        assertThat(patient.getEmail()).isEqualTo(updatedEmail);
    }

    @Test
    @DisplayName("Test update patient with existent email functionality")
    public void givenPatientToUpdate_whenSetExistentEmail_thenThrowsAndEmailIsNotChanged() {
        //given
        Patient patient1 = DataUtils.getJohnDoeTransient();
        Patient patient2 = DataUtils.getAdamSmithTransient();
        String oldEmail = patient1.getEmail();

        patientRepository.saveAll(List.of(patient1, patient2));
        //when
        patient1.setEmail(patient2.getEmail());
        Throwable thrown = catchThrowable(() -> patientRepository.flush());
        entityManager.clear();
        //then
        patient1 = patientRepository.findById(patient1.getId()).orElseThrow();
        assertThat(thrown).isInstanceOf(DataIntegrityViolationException.class);
        assertThat(patient1.getEmail()).isEqualTo(oldEmail);
    }

    @Test
    @DisplayName("Test soft delete patient by id functionality")
    public void givenPatientIsSaved_whenDeleteById_thenPatientIsSoftDeleted() {
        //given
        Patient patient = DataUtils.getJohnDoeTransient();
        patientRepository.save(patient);
        //when
        patientRepository.deleteById(patient.getId());
        patientRepository.flush();
        entityManager.clear();
        //then
        patient = patientRepository.findById(patient.getId()).orElseThrow();
        assertThat(patient.isDeleted()).isTrue();
    }
}