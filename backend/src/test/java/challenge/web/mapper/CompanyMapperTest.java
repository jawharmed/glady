package challenge.web.mapper;

import challenge.helpers.DtoObjectHelper;
import challenge.helpers.ObjectHelper;
import com.glady.challenge.model.company.Company;
import com.glady.challenge.web.dto.company.CompanyDTO;
import com.glady.challenge.web.mapper.CompanyMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class CompanyMapperTest {

    private final CompanyMapper companyMapper = CompanyMapper.INSTANCE;

    @Test
    void testCompanyToCompanyDTO() {
        Company company = ObjectHelper.getCompany();
        assertNotNull(company);

        CompanyDTO companyDTO = companyMapper.toCompanyDTO(company);

        assertNotNull(companyDTO);
        assertEquals(company.getId(), companyDTO.getId());
        assertEquals(company.getCompanyName(), companyDTO.getCompanyName());
        assertEquals(company.getMealBalance(), companyDTO.getMealBalance());
        assertEquals(company.getGiftBalance(), companyDTO.getGiftBalance());
    }

    @Test
    void testCompanyDTOToCompany() {
        CompanyDTO companyDTO = DtoObjectHelper.getCompanyDTO();
        assertNotNull(companyDTO);

        Company company = companyMapper.toCompany(companyDTO);

        assertNotNull(company);
        assertEquals(companyDTO.getId(), company.getId());
        assertEquals(companyDTO.getCompanyName(), company.getCompanyName());
        assertEquals(companyDTO.getMealBalance(), company.getMealBalance());
        assertEquals(companyDTO.getGiftBalance(), company.getGiftBalance());
    }

    @Test
    void testCompanyDTOToCompany_when_companyDTO_Is_Null() {
        Company company = companyMapper.toCompany(null);
        assertNull(company);
    }

    @Test
    void testCompanyToCompanyDTO_When_CompanyDTO_Is_Null() {
        CompanyDTO companyDTO = companyMapper.toCompanyDTO(null);
        assertNull(companyDTO);
    }
}
