package com.example.reception.controller;

import com.example.reception.dto.PatientDto;
import com.example.reception.entity.Patient;
import com.example.reception.service.PatientService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = PatientRestController.BASE_URI, produces = MediaType.APPLICATION_JSON_VALUE)
@OpenAPIDefinition(
        info = @Info(title = "API для управления пациентами", version = "1.0"),
        security = @SecurityRequirement(name = "basicAuth")
)
@Tag(name = "patients", description = "Операции с пациентами")
@RequiredArgsConstructor
public class PatientRestController {

    static final String BASE_URI = "/api/v1/patients";

    private final PatientService patientService;

    @Operation(
            summary = "Получить список всех пациентов",
            responses = @ApiResponse(responseCode = "200")
    )
    @GetMapping
    public List<Patient> getPatients() {
        return patientService.findAll();
    }

    @Operation(
            summary = "Добавить нового пациента",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Успешное добавление"),
                    @ApiResponse(responseCode = "400", description = "Данные пациента имеют неверный формат или отсутствуют"),
                    @ApiResponse(responseCode = "409", description = "Пациент с указанным email уже существует")
            }
    )
    @PostMapping
    public ResponseEntity<Patient> addPatient(@Valid @RequestBody PatientDto patientDto) {
        var patient = patientService.create(patientDto);
        return ResponseEntity.created(URI.create(BASE_URI + "/" + patient.getId()))
                .body(patient);
    }

    @Operation(
            summary = "Получить пациента по id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешное получение пациента с указанным id"),
                    @ApiResponse(responseCode = "404", description = "Пациент с указанным id не существует")
            }
    )
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<Patient> getPatient(
            @Parameter(description = "id пациента", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(patientService.findById(id));
    }

    @Operation(
            summary = "Изменить пациента по id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешное изменение пациента с указанным id"),
                    @ApiResponse(responseCode = "400", description = "Данные пациента имеют неверный формат или отсутствуют"),
                    @ApiResponse(responseCode = "404", description = "Пациент с указанным id не существует"),
                    @ApiResponse(responseCode = "409", description = "Пациент с указанным email уже существует")
            }
    )
    @PutMapping("/{id:\\d+}")
    public ResponseEntity<Patient> updatePatient(
            @Parameter(description = "id пациента", required = true)
            @PathVariable Long id,
            @Parameter(description = "данные пациента", required = true)
            @Valid @RequestBody PatientDto patientDto) {
        var patient = patientService.update(id, patientDto);
        return ResponseEntity.ok(patient);
    }

    @Operation(
            summary = "Удалить пациента по id",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Успешное удаление пациента с указанным id")
            }
    )
    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> deletePatient(
            @Parameter(description = "id пациента", required = true)
            @PathVariable Long id) {
        patientService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

