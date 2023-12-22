package com.glady.challenge.repository;

import com.glady.challenge.model.company.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {

    Optional<Company> findByCompanyNameIgnoreCase(String companyName);

    default Optional<Company> findById(@Param("id") Long id){
        return findById(id, false);
    }

    default Optional<Company> findById(Long id, boolean includesDeleted){
        Specification<Company> spec = isDeleted(includesDeleted)
                .and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id));
        return findOne(spec);
    }

    default List<Company> findAll(boolean includesDeleted){
        return findAll(isDeleted(includesDeleted));
    }

    default Page<Company> findAll(Pageable pageable, boolean includesDeleted){
        return findAll(isDeleted(includesDeleted), pageable);
    }

    default Specification<Company> isDeleted(boolean includesDeleted) {
        return includesDeleted ?  Specification.where(null) : (root, query, builder) -> builder.isNull(root.get("deletedOn"));
    }
}
