package com.glady.challenge.controller;

import com.glady.challenge.helpers.DtoObjectHelper;
import com.glady.challenge.model.company.Company;
import com.glady.challenge.service.company.CompanyService;
import com.glady.challenge.web.dto.company.CompanyDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static com.glady.challenge.helpers.Utils.asJsonString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanyController.class)
class CompanyControllerTest {

    @MockBean
    private CompanyService companyService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void testGetAllCompanies() throws Exception {

        boolean includesDeleted = false;
        List<Company> companies = Arrays.asList(new Company(), new Company());
        when(companyService.getAllCompanies(includesDeleted)).thenReturn(companies);

        mockMvc.perform(MockMvcRequestBuilders.get("/company")
                        .param("includes-deleted", String.valueOf(includesDeleted)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(companies.size()));

        verify(companyService, times(1)).getAllCompanies(includesDeleted);
    }

    @Test
    void testGetCompanyById() throws Exception {
        long companyId = 1L;
        boolean includesDeleted = false;
        Company company = new Company();
        when(companyService.getCompanyById(companyId, includesDeleted)).thenReturn(company);

        mockMvc.perform(MockMvcRequestBuilders.get("/company/{id}", companyId)
                        .param("includes-deleted", String.valueOf(includesDeleted)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap());

        verify(companyService, times(1)).getCompanyById(companyId, includesDeleted);
    }

    @Test
    void testCreateCompany() throws Exception {
        CompanyDTO companyDTO = DtoObjectHelper.getCompanyDTO();
        when(companyService.create(companyDTO)).thenReturn(companyDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(companyDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap());

        verify(companyService, times(1)).create(companyDTO);
    }

    @Test
    void testUpdateCompany() throws Exception {
        CompanyDTO companyDTO = DtoObjectHelper.getCompanyDTO();
        when(companyService.update(companyDTO)).thenReturn(companyDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(companyDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap());

        verify(companyService, times(1)).update(companyDTO);
    }

    @Test
    void testSoftDeleteCompany() throws Exception {
        long companyId = 1L;
        doNothing().when(companyService).softDeletionCompany(companyId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/company/{id}", companyId))
                .andExpect(status().isOk());

        verify(companyService, times(1)).softDeletionCompany(companyId);
    }
}
