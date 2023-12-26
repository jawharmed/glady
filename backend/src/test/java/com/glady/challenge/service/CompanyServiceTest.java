package com.glady.challenge.service;

import com.glady.challenge.exception.EntityAlreadyExistException;
import com.glady.challenge.exception.EntityNotFoundException;
import com.glady.challenge.helpers.DtoObjectHelper;
import com.glady.challenge.helpers.ObjectHelper;
import com.glady.challenge.model.company.Company;
import com.glady.challenge.repository.CompanyRepository;
import com.glady.challenge.service.common.ErrorMessage;
import com.glady.challenge.service.company.CompanyService;
import com.glady.challenge.web.dto.company.CompanyDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @Spy
    @InjectMocks
    private CompanyService companyService;

    @Test
    void testGetById_WhenCompanyExists_ShouldReturnCompany() {
        Company expectedCompany = ObjectHelper.getCompany();
        when(companyRepository.findById(1L, false)).thenReturn(Optional.of(expectedCompany));
        Company result = companyService.getCompanyById(1L);
        assertEquals(expectedCompany, result);
        assertEquals(expectedCompany.getId(), result.getId());
        assertEquals(expectedCompany.getCompanyName(), result.getCompanyName());
    }

    @Test
    void testGetById_WhenCompanyNotExists_ShouldThrowEntityNotFoundException() {
        when(companyRepository.findById(1L, false)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> companyService.getCompanyById(1L));
    }

    @Test
    void testGetCompanyDtoById_GivenIncludesDeleteIsFalse_WhenCompanyExists_ShouldReturnCompanyDTO() {
        Company existingCompany = ObjectHelper.getCompany();
        when(companyRepository.findById(1L, false)).thenReturn(Optional.of(existingCompany));
        CompanyDTO result = companyService.getCompanyDtoById(1L, false);
        assertEquals(existingCompany.getId(), result.getId());
        assertEquals(existingCompany.getCompanyName(), result.getCompanyName());
    }

    @Test
    void testGetCompanyDtoById_WithoutGivenIncludedDeleted_WhenCompanyExists_ShouldReturnCompanyDTO() {
        Company existingCompany = ObjectHelper.getCompany();
        when(companyRepository.findById(1L, false)).thenReturn(Optional.of(existingCompany));
        CompanyDTO result = companyService.getCompanyDtoById(1L);
        assertEquals(existingCompany.getId(), result.getId());
        assertEquals(existingCompany.getCompanyName(), result.getCompanyName());
    }

    @Test
    void testGetDtoById_WhenCompanyDoesNotExist_ShouldThrowEntityNotFoundException() {
        when(companyRepository.findById(1L, false)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> companyService.getCompanyDtoById(1L, false));
    }

    @Test
    void testCreate_WhenCompanyNotExist_ShouldReturnCreatedCompanyDTO() throws Exception {
        CompanyDTO companyDTO = new CompanyDTO(null, "Apple", 10.0, 20.0);
        Company created = new Company(1L, "Apple", 10.0, 20.0, null);

        doNothing().when(companyService).checkIfCompanyExist(any(), any());
        when(companyRepository.save(any())).thenReturn(created);
        CompanyDTO result = companyService.create(companyDTO);

        assertEquals(1L, result.getId());
        assertEquals("Apple", result.getCompanyName());
        assertEquals(10.0, result.getMealBalance());
        assertEquals(20.0, result.getGiftBalance());
    }

    @Test
    void testUpdate_GivenNewCompanyName_WhenCompanyNameNotExistsBefore_ShouldReturnUpdatedCompanyDTO() throws EntityNotFoundException, EntityAlreadyExistException {

        Company existingCompany = new Company(1L, "Existing Company", 10.0, 20.0, null);
        CompanyDTO updatedCompanyDTO = new CompanyDTO(1L, "Updated Company", 15.0, 25.0);

        when(companyRepository.findById(updatedCompanyDTO.getId(), false)).thenReturn(Optional.of(existingCompany));
        doNothing().when(companyService).checkIfCompanyExist(any(), any());
        when(companyRepository.save(any(Company.class))).thenAnswer(response -> response.getArgument(0) );

        CompanyDTO result = companyService.update(updatedCompanyDTO);

        assertEquals(updatedCompanyDTO.getId(), result.getId());
        assertEquals(updatedCompanyDTO.getCompanyName(), result.getCompanyName());
        assertEquals(updatedCompanyDTO.getMealBalance(), result.getMealBalance());
        assertEquals(updatedCompanyDTO.getGiftBalance(), result.getGiftBalance());
    }

    @Test
    void testCreate_WhenCompanyAlreadyExists_ShouldThrowEntityAlreadyExistException() {
        CompanyDTO existingCompanyDTO = new CompanyDTO(null, "Apple", 0.0, 0.0);
        doThrow(new EntityAlreadyExistException(String.format(ErrorMessage.ENTITY_ID_NAME_ALREADY_EXIST, "Company", 2L, existingCompanyDTO.getCompanyName())))
                .when(companyService).checkIfCompanyExist(any(), any());

        assertThrows(EntityAlreadyExistException.class, () -> companyService.create(existingCompanyDTO));
    }

    @Test
    void testUpdate_WhenCompanyNotExist_ShouldThrowEntityNotFoundException() {
        CompanyDTO existingCompanyDTO = DtoObjectHelper.getCompanyDTO();
        Long companyId = existingCompanyDTO.getId();
        when(companyRepository.findById(companyId, false)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> companyService.update(existingCompanyDTO));
    }

    @Test
    void testUpdate_GivenNewExistingCompanyName_ShouldThrowEntityAlreadyExistException() {
        Company existingCompany = new Company(1L, "Existing Company", 10.0, 20.0, null);
        CompanyDTO updatedCompanyDTO = new CompanyDTO(1L, "Updated Company Name", 15.0, 25.0);

        when(companyRepository.findById(updatedCompanyDTO.getId(), false)).thenReturn(Optional.of(existingCompany));
        doThrow(new EntityAlreadyExistException(String.format(ErrorMessage.ENTITY_ID_NAME_ALREADY_EXIST, "Company", existingCompany.getId(), existingCompany.getCompanyName())))
                .when(companyService).checkIfCompanyExist(any(), any());

        assertThrows(EntityAlreadyExistException.class, () -> companyService.update(updatedCompanyDTO));
    }


    @Test
    void testGetAllCompanies(){
        List<Company> expectedCompanies = Collections.singletonList(new Company());
        when(companyRepository.findAll(companyRepository.isDeleted(anyBoolean()))).thenReturn(expectedCompanies);
        List<Company> actualCompanies = companyService.getAllCompanies(anyBoolean());
        assertEquals(expectedCompanies, actualCompanies);

        verify(companyRepository, times(2)).findAll(companyRepository.isDeleted(anyBoolean()));
        verify(companyRepository, times(2)).isDeleted(anyBoolean());
    }

    @Test
    void testSoftDeletion(){
        Company companyToDelete = new Company(1L, "Apple", 10.0, 20.0, null);
        when(companyRepository.findById(companyToDelete.getId(), false)).thenReturn(Optional.of(companyToDelete));
        when(companyRepository.save(any())).thenAnswer(response -> response.getArgument(0) );
        assertDoesNotThrow(() -> companyService.softDeletionCompany(companyToDelete.getId()));

        verify(companyRepository, times(1)).findById(companyToDelete.getId(), false);
        verify(companyRepository, times(1)).save(companyToDelete);

        assertNotNull(companyToDelete.getDeletedOn());
    }

    @Test
    void testSoftDeletion_WhenCompanyNotExist() {
        Long companyId = 1L;
        when(companyRepository.findById(companyId, false)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> companyService.softDeletionCompany(companyId));

        verify(companyRepository, times(1)).findById(companyId, false);
        verify(companyRepository, never()).save(any());
    }


    @Test
    void testCheckIfCompanyExist_NewCompanyWithExistingName_ThrowsEntityAlreadyExistException() {
        CompanyDTO newCompanyDTO = DtoObjectHelper.getCompanyDTO();
        Company existingCompany = ObjectHelper.getCompany();

        when(companyRepository.isDeleted(anyBoolean())).thenReturn((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());
        when(companyRepository.findOne(any(Specification.class))).thenReturn(Optional.of(existingCompany));

        assertThrows(EntityAlreadyExistException.class, () -> companyService.checkIfCompanyExist(newCompanyDTO, existingCompany));

        verify(companyRepository, times(1)).findOne(any(Specification.class));
    }

    @Test
    void testCheckIfCompanyExist_NewCompanyWithDeletedName_ThrowsEntityAlreadyExistExceptionWithDeletedMessage() {
        CompanyDTO newCompanyDTO = DtoObjectHelper.getCompanyDTO();
        Company existingCompany = ObjectHelper.getCompany();
        existingCompany.setDeletedOn(ZonedDateTime.now());

        when(companyRepository.isDeleted(true)).thenReturn((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());
        when(companyRepository.findOne(any(Specification.class))).thenReturn(Optional.of(existingCompany));

        assertThrows(EntityAlreadyExistException.class, () -> companyService.checkIfCompanyExist(newCompanyDTO, existingCompany));

        verify(companyRepository, times(1)).findOne(any(Specification.class));
    }

    @Test
    void testCheckIfCompanyExist_NewCompanyWithDifferentName_NoExceptionThrown() {
        CompanyDTO newCompanyDTO = DtoObjectHelper.getCompanyDTO();
        newCompanyDTO.setCompanyName("NewCompany");
        Company existingCompany = ObjectHelper.getCompany();

        when(companyRepository.isDeleted(true)).thenReturn((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());
        when(companyRepository.findOne(any(Specification.class))).thenReturn(Optional.empty());

        companyService.checkIfCompanyExist(newCompanyDTO, existingCompany);

        verify(companyRepository, times(1)).findOne(any(Specification.class));
    }

}
