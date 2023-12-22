package com.glady.challenge.service.gladyuser;

import com.glady.challenge.exception.EntityAlreadyExistException;
import com.glady.challenge.exception.EntityNotFoundException;
import com.glady.challenge.model.company.Company;
import com.glady.challenge.model.enums.VoucherTypeEnum;
import com.glady.challenge.model.user.GladyUser;
import com.glady.challenge.model.wallet.Voucher;
import com.glady.challenge.repository.GladyUserRepository;
import com.glady.challenge.service.common.ErrorMessage;
import com.glady.challenge.service.company.CompanyService;
import com.glady.challenge.service.wallet.VoucherService;
import com.glady.challenge.web.dto.user.GladyUserDTO;
import com.glady.challenge.web.dto.user.GladyUserInfoDTO;
import com.glady.challenge.web.mapper.GladyUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class GladyUserService {

    private static final String ENTITY_NAME = "GladyUser";
    private static final GladyUserMapper gladyUserMapper = GladyUserMapper.INSTANCE;

    private final GladyUserRepository gladyUserRepository;
    private final CompanyService companyService;
    private final VoucherService voucherService;

    /**
     * Get page of glady users
     *
     * @param page page number
     * @param size number of users by page
     * @param sortOrder Sort order (asc / desc)
     * @return Page of GladyUsers
     */
    public Page<GladyUserDTO> getAll(int page, int size, String sortOrder) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }
        Sort sort = Sort.by(direction, "username");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<GladyUser> gladyUserPage = gladyUserRepository.findAll(pageable);

        return gladyUserPage.map(GladyUserMapper.INSTANCE::toGladyUserDTO);
    }

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

    /**
     * Get Glady User gloabal information
     * @param id Glady User ID
     */
    public GladyUserInfoDTO getGladyUserInfo(Long id) {
        GladyUser gladyUser = getById(id);
        Map<VoucherTypeEnum, List<Voucher>> vouchersMap = new EnumMap<>(VoucherTypeEnum.class);
        gladyUser.getWallets().forEach(wallet -> {
            vouchersMap.put(wallet.getWalletType(), this.voucherService.getVouchersByWallet(wallet.getId()));
        });

        List<Voucher> valideGiftVouchers = vouchersMap.get(VoucherTypeEnum.GIFT) == null ?
                Collections.emptyList() :
                vouchersMap.get(VoucherTypeEnum.GIFT)
                        .stream()
                        .filter(voucher -> voucher.getExpiresOn().isAfter(ZonedDateTime.now()))
                        .collect(Collectors.toList());

        List<Voucher> valideMealVouchers = vouchersMap.get(VoucherTypeEnum.MEAL) == null ?
                Collections.emptyList() :
                vouchersMap.get(VoucherTypeEnum.MEAL)
                        .stream()
                        .filter(voucher -> voucher.getExpiresOn().isAfter(ZonedDateTime.now()))
                        .collect(Collectors.toList());

        List<Voucher> expiredVouchers = vouchersMap.values().stream()
                .flatMap(Collection::stream)
                .filter(voucher -> voucher.getExpiresOn().isBefore(ZonedDateTime.now()))
                .collect(Collectors.toList());

//        int expiredVouchersCount = vouchersMap.get(VoucherTypeEnum.GIFT).size() - valideGiftVouchers.size()
//                                   + vouchersMap.get(VoucherTypeEnum.MEAL).size() - valideMealVouchers.size();

        double giftBalance = valideGiftVouchers.isEmpty() ? 0 : valideGiftVouchers.stream()
                .mapToDouble(Voucher::getAmount)
                .sum();

        double mealBalance = valideMealVouchers.isEmpty() ? 0 : valideMealVouchers.stream()
                .mapToDouble(Voucher::getAmount)
                .sum();


        return GladyUserInfoDTO.builder()
                .id(gladyUser.getId())
                .username(gladyUser.getUsername())
                .companyName(gladyUser.getCompany().getCompanyName())
                .totalBalance(giftBalance + mealBalance)
                .valideGiftVouchersCount(valideGiftVouchers.size())
                .giftBalance(giftBalance)
                .valideMealVouchersCount(valideMealVouchers.size())
                .mealBalance(mealBalance)
                .totalExpiredVouchersCount(expiredVouchers.size())
                .build();
    }
}
