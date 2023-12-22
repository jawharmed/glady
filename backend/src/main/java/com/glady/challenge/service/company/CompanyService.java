package com.glady.challenge.service.company;

import com.glady.challenge.exception.EntityAlreadyExistException;
import com.glady.challenge.exception.EntityNotFoundException;
import com.glady.challenge.model.company.Company;
import com.glady.challenge.repository.CompanyRepository;
import com.glady.challenge.service.common.ErrorMessage;
import com.glady.challenge.web.dto.company.CompanyDTO;
import com.glady.challenge.web.mapper.CompanyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CompanyService {

    private static final String ENTITY_NAME = "Company";

    private static final CompanyMapper companyMapper = CompanyMapper.INSTANCE;

    private final CompanyRepository companyRepository;

    /**
     * Get Company Information
     *
     * @param id Company ID
     * @return Company Information
     */
    public Company getCompanyById(Long id){
        return getCompanyById(id, false);
    }

    /**
     * Get Company Information
     * @param id Company ID
     * @return Company Information
     * @throws EntityNotFoundException If Company not found
     */
    public Company getCompanyById(Long id, boolean includesDeleted) throws EntityNotFoundException {

        Optional<Company> company = companyRepository.findById(id, includesDeleted);
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
    public CompanyDTO getCompanyDtoById(Long id) throws EntityNotFoundException {
        return companyMapper.toCompanyDTO(this.getCompanyById(id, false));
    }

    /**
     * Get Company Information
     * @param id Company ID
     * @param deletedIncluded True if includes also deleted companies
     * @return Company DTO Information
     * @throws EntityNotFoundException If Company not found
     */
    public CompanyDTO getCompanyDtoById(Long id, boolean deletedIncluded) throws EntityNotFoundException {
        return companyMapper.toCompanyDTO(this.getCompanyById(id, deletedIncluded));
    }


    /**
     * Get all companies
     * @param deletedIncluded True if includes also deleted companies
     * @return List of Companies
     */
    public List<Company> getAllCompanies(boolean deletedIncluded){
        return companyRepository.findAll(companyRepository.isDeleted(deletedIncluded));
    }

    /**
     * Create a new Company
     *
     * @param companyDTO Company information
     * @return The new created company information
     * @throws EntityAlreadyExistException If a company with the new name already exists
     */
    public CompanyDTO create(CompanyDTO companyDTO) throws EntityAlreadyExistException {
        checkIfCompanyExist(companyDTO, null);
        Company company = companyRepository.save(companyMapper.toCompany(companyDTO));
        return companyMapper.toCompanyDTO(company);
    }


    /**
     * Updates company information.
     *
     * @param companyToUpdate New company information to be updated.
     * @return Up-to-date company information.
     * @throws EntityNotFoundException   If the company with the specified ID is not found.
     * @throws EntityAlreadyExistException If a company with the new name already exists.
     */
    public CompanyDTO update(CompanyDTO companyToUpdate) throws EntityNotFoundException, EntityAlreadyExistException {
        Company existingCompany = this.getCompanyById(companyToUpdate.getId(), false);

        checkIfCompanyExist(companyToUpdate, existingCompany);

        Company company = companyRepository.save(companyMapper.toCompany(companyToUpdate));
        return companyMapper.toCompanyDTO(company);
    }

    /**
     * Soft deletion of Company : The company is tagged deleted in the database by adding the current date to the deletedOn field.
     *
     * @param idCompany  ID Company
     * @throws EntityNotFoundException  If the company with the specified ID is not found.
     */
    public void softDeletionCompany(Long idCompany) throws EntityNotFoundException {
       Company existingCompany = this.getCompanyById(idCompany, false);

        existingCompany.setDeletedOn(ZonedDateTime.now());
        companyRepository.save(existingCompany);
    }

    /**
     * If "existingCompany" is not NULL, in this case we check if a "Company" with the same name exists except "existingCompany".
     * This methode raises an exception if the company exists in Database, an explicit message is returned if the company is already deleted but still exists in the DB.
     *
     * @param newCompanyDTO  New Company information
     * @param existingCompany  Existing Company information
     */
    public void checkIfCompanyExist(@NonNull CompanyDTO newCompanyDTO, @Nullable Company existingCompany) {
        Specification<Company> spec = companyRepository.isDeleted(true)
                .and((root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.lower(root.get("companyName")), newCompanyDTO.getCompanyName().toLowerCase()));

        if(existingCompany !=null){
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.notEqual(criteriaBuilder.lower(root.get("companyName")), existingCompany.getCompanyName().toLowerCase()));
        }

        Optional<Company> company = companyRepository.findOne(spec);

        if(company.isPresent()){
            if(company.get().getDeletedOn()!=null) {
                throw new EntityAlreadyExistException(String.format(ErrorMessage.ENTITY_NAME_ALREADY_EXIST_BUT_DELETED, ENTITY_NAME, newCompanyDTO.getCompanyName(), company.get().getDeletedOn()));
            }
            else {
                throw new EntityAlreadyExistException(String.format(ErrorMessage.ENTITY_ID_NAME_ALREADY_EXIST, ENTITY_NAME, company.get().getId(), newCompanyDTO.getCompanyName()));
            }
        }
    }

}
