package com.example.registry;

import com.example.registry.model.Patient;
import com.example.registry.repository.PatientRepository;
import com.example.registry.service.PatientServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class PatientServiceUnitTest {

    @Test
    void softDeleteShouldMarkDeleted() {
        PatientRepository repo = Mockito.mock(PatientRepository.class);
        Patient p = new Patient();
        p.setId(1L);

        Mockito.when(repo.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(p));
        Mockito.when(repo.save(Mockito.any())).thenAnswer(i -> i.getArgument(0));

        PatientServiceImpl service = new PatientServiceImpl(repo);
        boolean ok = service.softDelete(1L);

        assertThat(ok).isTrue();
        assertThat(p.isDeleted()).isTrue();
        Mockito.verify(repo).save(p);
    }
}
