package com.glady.challenge.service.gladyuser;

import com.glady.challenge.exception.EntityAlreadyExistException;
import com.glady.challenge.exception.EntityNotFoundException;
import com.glady.challenge.model.company.Company;
import com.glady.challenge.model.user.GladyUser;
import com.glady.challenge.repository.GladyUserRepository;
import com.glady.challenge.service.common.ErrorMessage;
import com.glady.challenge.service.company.CompanyService;
import com.glady.challenge.web.dto.user.GladyUserDTO;
import com.glady.challenge.web.mapper.GladyUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GladyUserService {

    private static final String ENTITY_NAME = "GladyUser";
    private static final GladyUserMapper gladyUserMapper = GladyUserMapper.INSTANCE;

    private final GladyUserRepository gladyUserRepository;
    private final CompanyService companyService;

    /**
     * Get Glady User information
     * @param id Glady User ID
     * @return GladyUser information
     */
    public GladyUser getById(Long id){
        return gladyUserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ErrorMessage.ENTITY_ID_NOT_FOUND, ENTITY_NAME, id)));
    }

    /**
     * Get Glady User information
     * @param id Glady User ID
     * @return GladyUser DTO information
     */
    public GladyUserDTO getDtoById(Long id) {
        return gladyUserMapper.toGladyUserDTO(this.getById(id));
    }

    /**
     *
     * @param gladyUserDTO The new Glady User information
     * @return Created Glady User
     * @throws EntityAlreadyExistException  If the Glady User with the specified ID already exist
     */
    public GladyUserDTO create(GladyUserDTO gladyUserDTO) throws EntityAlreadyExistException {
        checkIfGladyUserExist(gladyUserDTO);
        Company company = companyService.getCompanyById(gladyUserDTO.getCompanyId(), false);
        GladyUser gladyUser = gladyUserRepository.save(gladyUserMapper.toGladyUser(gladyUserDTO, company));
        return gladyUserMapper.toGladyUserDTO(gladyUser);
    }

    /**
     *
     * @param gladyUserDTO Glady User information to be updated.
     * @return Up-to-date Glady User information.
     * @throws EntityAlreadyExistException If the Glady User with the specified ID already exist
     * @throws EntityNotFoundException If the Glady User with the specified ID is not found.
     */
    public GladyUserDTO update(GladyUserDTO gladyUserDTO) throws EntityAlreadyExistException, EntityNotFoundException {
        GladyUser existingGladyUser = gladyUserRepository.findById(gladyUserDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ErrorMessage.ENTITY_ID_NAME_NOT_FOUND, ENTITY_NAME, gladyUserDTO.getUsername(), gladyUserDTO.getId())));

        if(!existingGladyUser.getUsername().equals(gladyUserDTO.getUsername())){
            checkIfGladyUserExist(gladyUserDTO);
        }

        Company company = companyService.getCompanyById(gladyUserDTO.getId(), false);
        GladyUser gladyUser = gladyUserRepository.save(gladyUserMapper.toGladyUser(gladyUserDTO, company));
        return gladyUserMapper.toGladyUserDTO(gladyUser);
    }

    /**
     *
     * @param gladyUserId Glady User Id
     * @throws EntityNotFoundException If the Glady User with the specified ID is not found.
     */
    public void delete(Long gladyUserId) throws EntityNotFoundException {
        Optional<GladyUser> gladyUser = gladyUserRepository.findById(gladyUserId);
        if(!gladyUser.isPresent()){
            throw new EntityNotFoundException(String.format(ErrorMessage.ENTITY_ID_NOT_FOUND, ENTITY_NAME, gladyUserId));
        }

        gladyUserRepository.deleteById(gladyUserId);
    }

    /**
     * Raises an exception if a Glady User exists in DB
     *
     * @param gladyUserDTO Glady User information
     * @throws EntityAlreadyExistException If a Glady User already exists.
     */
    private void checkIfGladyUserExist(GladyUserDTO gladyUserDTO) throws EntityAlreadyExistException {
        Optional<GladyUser> existingUser = gladyUserRepository.findByUsernameIgnoreCase(gladyUserDTO.getUsername());
        if(existingUser.isPresent()){
            throw new EntityAlreadyExistException(String.format(ErrorMessage.ENTITY_ID_NAME_ALREADY_EXIST, ENTITY_NAME, gladyUserDTO.getId(), gladyUserDTO.getUsername()));
        }
    }
}
