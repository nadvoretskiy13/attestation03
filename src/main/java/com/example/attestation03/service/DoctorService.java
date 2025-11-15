package com.example.attestation03.service;

import com.example.attestation03.model.Doctor;
import com.example.attestation03.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    private final DoctorRepository repo;
    public DoctorService(DoctorRepository repo) { this.repo = repo; }

    public List<Doctor> listAll() { return repo.findAll(); }
    public Optional<Doctor> get(Long id) { return repo.findById(id); }
    public Doctor create(Doctor d) { d.setId(null); return repo.save(d); }
    public Optional<Doctor> update(Long id, Doctor updated) {
        return repo.findById(id).map(ex -> {
            ex.setFirstName(updated.getFirstName());
            ex.setLastName(updated.getLastName());
            ex.setSpecialization(updated.getSpecialization());
            ex.setPhone(updated.getPhone());
            ex.setEmail(updated.getEmail());
            return repo.save(ex);
        });
    }
    public void delete(Long id) { repo.deleteById(id); }
}
