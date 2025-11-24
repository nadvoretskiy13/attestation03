package com.example.reception.controller;

import com.example.reception.config.SecurityConfiguration;
import com.example.reception.dto.PatientDto;
import com.example.reception.entity.Patient;
import com.example.reception.exception.PatientAlreadyExistsException;
import com.example.reception.exception.PatientNotFoundException;
import com.example.reception.service.PatientService;
import com.example.reception.util.DataUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientRestController.class)
@Import(SecurityConfiguration.class)
class PatientRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PatientService patientService;

    @Test
    @WithMockUser
    @DisplayName("Test create patient functionality")
    void givenPatientDto_whenCreatePatient_thenSuccessResponse() throws Exception {
        // given
        PatientDto dto = DataUtils.getJohnDoeDto();
        Patient entity = DataUtils.getJohnDoePersisted();
        given(patientService.create(any(PatientDto.class))).willReturn(entity);
        // when
        ResultActions result = mockMvc.perform(post("/api/v1/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));
        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(entity.getId()));
    }

    @Test
    @WithMockUser
    @DisplayName("Test create patient with duplicate email functionality")
    void givenPatientDtoWithDuplicateEmail_whenCreatePatient_thenErrorResponse() throws Exception {
        // given
        PatientDto dto = DataUtils.getJohnDoeDto();
        given(patientService.create(any(PatientDto.class))).willThrow(new PatientAlreadyExistsException());
        // when
        ResultActions result = mockMvc.perform(post("/api/v1/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));
        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errors..field").isArray())
                .andExpect(jsonPath("$.errors..field", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field").value("email"));
    }

    @Test
    @WithMockUser
    @DisplayName("Test create patient with invalid fields functionality")
    void givenPatientDtoWithInvalidFields_whenCreatePatient_thenErrorResponse() throws Exception {
        // given
        PatientDto patientDto = DataUtils.getInvalidDto();
        BindingResult bindingResult = new BeanPropertyBindingResult(patientDto, "patientDto");
        bindingResult.reject("email", "reception.form.field.email");
        bindingResult.reject("passport", "reception.form.field.empty");
        Method method = PatientRestController.class.getDeclaredMethod("addPatient", PatientDto.class);

        given(patientService.create(any(PatientDto.class)))
                .willAnswer(invocation -> {
                    throw new MethodArgumentNotValidException(new MethodParameter(method, 0), bindingResult);
                });
        // when
        ResultActions result = mockMvc.perform(post("/api/v1/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientDto)));
        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors..field").isArray())
                .andExpect(jsonPath("$.errors..field", hasSize(2)))
                .andExpect(jsonPath("$.errors..field", hasItem("email")))
                .andExpect(jsonPath("$.errors..field", hasItem("passport")));
    }

    @Test
    @WithMockUser
    @DisplayName("Test update patient functionality")
    void givenPatientDto_whenUpdatePatient_thenSuccessResponse() throws Exception {
        // given
        PatientDto dto = DataUtils.getJohnDoeDto();
        Patient entity = DataUtils.getJohnDoePersisted();
        given(patientService.update(anyLong(), any(PatientDto.class))).willReturn(entity);
        // when
        ResultActions result = mockMvc.perform(put("/api/v1/patients/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));
        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entity.getId()));
    }

    @Test
    @WithMockUser
    @DisplayName("Test update patient with incorrect id functionality")
    void givenPatientDtoWithIncorrectId_whenUpdatePatient_thenErrorResponse() throws Exception {
        // given
        PatientDto dto = DataUtils.getJohnDoeDto();
        given(patientService.update(anyLong(), any(PatientDto.class))).willThrow(new PatientNotFoundException());
        // when
        ResultActions result = mockMvc.perform(put("/api/v1/patients/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));
        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors..field").value("id"));
    }

    @Test
    @WithMockUser
    @DisplayName("Test update patient with incorrect fields functionality")
    void givenPatientDtoWithIncorrectFields_whenUpdatePatient_thenErrorResponse() throws Exception {
        // given
        PatientDto patientDto = DataUtils.getInvalidDto();
        BindingResult bindingResult = new BeanPropertyBindingResult(patientDto, "patientDto");
        bindingResult.reject("email", "reception.form.field.email");
        bindingResult.reject("passport", "reception.form.field.empty");
        Method method = PatientRestController.class.getDeclaredMethod("addPatient", PatientDto.class);

        given(patientService.update(anyLong(), any(PatientDto.class)))
                .willAnswer(invocation -> {
                    throw new MethodArgumentNotValidException(new MethodParameter(method, 0), bindingResult);
                });
        // when
        ResultActions result = mockMvc.perform(put("/api/v1/patients/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientDto)));
        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors..field").isArray())
                .andExpect(jsonPath("$.errors..field", hasSize(2)))
                .andExpect(jsonPath("$.errors..field", hasItem("email")))
                .andExpect(jsonPath("$.errors..field", hasItem("passport")));
    }

    @Test
    @WithMockUser
    @DisplayName("Test update patient with duplicate email functionality")
    void givenPatientDtoWithDuplicateEmail_whenUpdatePatient_thenErrorResponse() throws Exception {
        // given
        PatientDto dto = DataUtils.getJohnDoeDto();
        given(patientService.update(anyLong(), any(PatientDto.class))).willThrow(new PatientAlreadyExistsException());
        // when
        ResultActions result = mockMvc.perform(put("/api/v1/patients/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));
        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errors..field").isArray())
                .andExpect(jsonPath("$.errors..field", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field").value("email"));
    }

    @Test
    @WithMockUser
    @DisplayName("Test find patient by id functionality")
    void givenPatientId_whenFindPatientById_thenSuccessResponse() throws Exception {
        // given
        Patient entity = DataUtils.getJohnDoePersisted();
        given(patientService.findById(anyLong())).willReturn(entity);
        // when
        ResultActions result = mockMvc.perform(get("/api/v1/patients/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON));
        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entity.getId()));
    }

    @Test
    @WithMockUser
    @DisplayName("Test find patient by invalid id functionality")
    void givenInvalidPatientId_whenFindPatientById_thenErrorResponse() throws Exception {
        // given
        given(patientService.findById(anyLong())).willThrow(new PatientNotFoundException());
        // when
        ResultActions result = mockMvc.perform(get("/api/v1/patients/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON));
        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("Test find all patients functionality")
    void givenPatients_whenFindAllPatients_thenSuccessResponse() throws Exception {
        // given
        Patient entity1 = DataUtils.getJohnDoePersisted();
        Patient entity2 = DataUtils.getAdamSmithPersisted();
        given(patientService.findAll()).willReturn(List.of(entity1, entity2));
        // when
        ResultActions result = mockMvc.perform(get("/api/v1/patients")
                .contentType(MediaType.APPLICATION_JSON));
        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..id").isArray())
                .andExpect(jsonPath("$..id", hasSize(2)));
    }

    @Test
    @WithMockUser
    @DisplayName("Test delete patient by id functionality")
    void givenPatientId_whenDeletePatient_thenSuccessResponse() throws Exception {
        // given
        doNothing().when(patientService).delete(anyLong());
        // when
        ResultActions result = mockMvc.perform(delete("/api/v1/patients/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON));
        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    @DisplayName("Test delete patient by invalid id functionality")
    void givenInvalidPatientId_whenDeletePatient_thenErrorResponse() throws Exception {
        // given
        doNothing().when(patientService).delete(anyLong());
        // when
        ResultActions result = mockMvc.perform(delete("/api/v1/patients/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON));
        // then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent());
    }
}