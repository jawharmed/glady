package com.glady.challenge.repository;

import com.glady.challenge.model.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query(value = "from Company as C where C.id = :id and C.deletedOn is null")
    Optional<Company> findById(@Param("id") Long id);

    Optional<Company> findByCompanyNameIgnoreCase(String companyName);
}
