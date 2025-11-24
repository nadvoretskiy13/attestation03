package com.example.reception.repository.filter;

import com.example.reception.repository.PatientRepository;
import com.example.reception.util.DataUtils;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SoftDeletedFilterTests {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private EntityManager entityManager;

    SoftDeletedFilter filter;

    @BeforeEach
    void setUp() {
        filter = new SoftDeletedFilter(entityManager);
        filter.filterName("deletedPatientFilter").filterParameter("isDeleted");
    }

    @Test
    void givenTwoActiveOneDeleted_whenGetAllActive_returnsListOfTwo() {
        //given
        var allPatients = List.of(
                DataUtils.getJohnDoeTransient(),
                DataUtils.getAdamSmithTransient(),
                DataUtils.getMikeBomTransient());
        patientRepository.saveAllAndFlush(allPatients);
        entityManager.clear();
        // when
        var activePatients = filter.apply(patientRepository::findAll, false);
        // then
        assertThat(activePatients).hasSize(2);
    }

    @Test
    void givenTwoActiveOneDeleted_whenGetAllDeleted_returnsListOfOne() {
        //given
        var allPatients = List.of(
                DataUtils.getJohnDoeTransient(),
                DataUtils.getAdamSmithTransient(),
                DataUtils.getMikeBomTransient());
        patientRepository.saveAllAndFlush(allPatients);
        entityManager.clear();
        // when
        var deletedPatients = filter.apply(patientRepository::findAll, true);
        // then
        assertThat(deletedPatients).hasSize(1);
    }

    @Test
    void givenActive_whenGetByIdActive_returnsPatient() {
        //given
        var saved = patientRepository.saveAndFlush(DataUtils.getJohnDoeTransient());
        entityManager.clear();
        // when
        var patient = filter.apply(() -> patientRepository.findById(saved.getId()), false);
        // then
        assertThat(patient).isNotEmpty();
    }

    @Test
    void givenActive_whenGetByIdDeleted_returnsEmpty() {
        //given
        var saved = patientRepository.saveAndFlush(DataUtils.getJohnDoeTransient());
        entityManager.clear();
        // when
        var patient = filter.apply(() -> patientRepository.findById(saved.getId()), true);
        // then
        assertThat(patient).isEmpty();
    }

    @Test
    void givenDeleted_whenGetByIdDeleted_returnsPatient() {
        //given
        var saved = patientRepository.saveAndFlush(DataUtils.getMikeBomTransient());
        entityManager.clear();
        // when
        var patient = filter.apply(() -> patientRepository.findById(saved.getId()), true);
        // then
        assertThat(patient).isNotEmpty();
    }

    @Test
    void givenDeleted_whenGetByIdActive_returnsEmpty() {
        //given
        var saved = patientRepository.saveAndFlush(DataUtils.getMikeBomTransient());
        entityManager.clear();
        // when
        var patient = filter.apply(() -> patientRepository.findById(saved.getId()), false);
        // then
        assertThat(patient).isEmpty();
    }
}