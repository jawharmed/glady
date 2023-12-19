package com.glady.challenge.service.company;

import com.glady.challenge.exception.EntityAlreadyExistException;

import com.glady.challenge.exception.EntityNotFoundException;
import com.glady.challenge.model.company.Company;
import com.glady.challenge.repository.CompanyRepository;
import com.glady.challenge.service.common.ErrorMessage;
import com.glady.challenge.web.dto.company.CompanyDTO;
import com.glady.challenge.web.mapper.CompanyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private static final String ENTITY_NAME = "Company";

    private static final CompanyMapper companyMapper = CompanyMapper.INSTANCE;

    private final CompanyRepository companyRepository;

    /**
     * Get Company Information
     * @param id Company ID
     * @return Company Information
     * @throws EntityNotFoundException If Company not found
     */
    public Company getById(Long id) throws EntityNotFoundException {
        Optional<Company> company = companyRepository.findById(id);
        if(!company.isPresent())
            throw new EntityNotFoundException(String.format(ErrorMessage.ENTITY_ID_NOT_FOUND, ENTITY_NAME, id));
        return company.get();
    }

    /**
     * Get Company Information
     * @param id Company ID
     * @return Company DTO Information
     * @throws EntityNotFoundException If Company not found
     */
    public CompanyDTO getDtoById(Long id) throws EntityNotFoundException {
        return companyMapper.toCompanyDTO(this.getById(id));
    }

    /**
     * Create a new Company
     *
     * @param companyDTO Company information
     * @return The new created company information
     * @throws EntityAlreadyExistException If a company with the new name already exists
     */
    public CompanyDTO create(CompanyDTO companyDTO) throws EntityAlreadyExistException {
        checkIfCompanyExist(companyDTO);
        Company company = companyRepository.save(companyMapper.toCompany(companyDTO));
        return companyMapper.toCompanyDTO(company);
    }


    /**
     * Updates company information.
     *
     * @param companyDTO New company information to be updated.
     * @return Up-to-date company information.
     * @throws EntityNotFoundException   If the company with the specified ID is not found.
     * @throws EntityAlreadyExistException If a company with the new name already exists.
     */
    public CompanyDTO update(CompanyDTO companyDTO) throws EntityNotFoundException, EntityAlreadyExistException {
        Company existingCompany = companyRepository.findById(companyDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ErrorMessage.ENTITY_ID_NAME_NOT_FOUND, ENTITY_NAME, companyDTO.getId(), companyDTO.getCompanyName())));

        if( existingCompany!=null && existingCompany.getId()!=null && !existingCompany.getCompanyName().equals(companyDTO.getCompanyName())){
            checkIfCompanyExist(companyDTO);
        }

        Company company = companyRepository.save(companyMapper.toCompany(companyDTO));
        return companyMapper.toCompanyDTO(company);
    }

    /**
     * Soft deletion of Company : The company is tagged deleted in the database by adding the current date to the deletedOn field.
     *
     * @param idCompany  ID Company
     * @throws EntityNotFoundException  If the company with the specified ID is not found.
     */
    public void softDeletionCompany(Long idCompany) throws EntityNotFoundException {
        Company existingCompany = companyRepository.findById(idCompany)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ErrorMessage.ENTITY_ID_NOT_FOUND, ENTITY_NAME, idCompany)));

        existingCompany.setDeletedOn(ZonedDateTime.now());
        companyRepository.save(existingCompany);
    }

    /**
     * Raises an exception if the company exists in DB, an explicit message is returned if the company is already deleted but still exists in the DB.
     *
     * @param companyDTO Company information
     * @throws EntityAlreadyExistException If a company with the new name already exists.
     */
    private void checkIfCompanyExist(CompanyDTO companyDTO) throws EntityAlreadyExistException {
        Optional<Company> existingCompany = companyRepository.findByCompanyNameIgnoreCase(companyDTO.getCompanyName());
        if(existingCompany.isPresent()){
            if(existingCompany.get().getDeletedOn() != null)
                throw new EntityAlreadyExistException(String.format(ErrorMessage.ENTITY_NAME_ALREADY_EXIST_BUT_DELETED, ENTITY_NAME, companyDTO.getCompanyName(), existingCompany.get().getDeletedOn()));
            else
                throw new EntityAlreadyExistException(String.format(ErrorMessage.ENTITY_ID_NAME_ALREADY_EXIST, ENTITY_NAME, companyDTO.getId(), companyDTO.getCompanyName()));
        }
    }
}
