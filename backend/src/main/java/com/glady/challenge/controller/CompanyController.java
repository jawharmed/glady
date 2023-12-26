package com.glady.challenge.controller;

import com.glady.challenge.model.company.Company;
import com.glady.challenge.service.company.CompanyService;
import com.glady.challenge.web.dto.company.CompanyDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all companies", notes = "This method retrieves the list of all companies.", responseContainer = "List<CompanyDTO>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of companies"),
    })
    public List<Company> getAllCompanies(@RequestParam(name = "includes-deleted", required = false, defaultValue = "false") boolean includesDeleted){
        return companyService.getAllCompanies(includesDeleted);
    }


    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get company by id", notes = "This method retrieves one company by id", response = Company.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The company exists in the database"),
            @ApiResponse(code = 404, message = "The company doesn't exists in database"),
    })
    public Company getCompanyById(@PathVariable Long id,
                                  @RequestParam(name = "includes-deleted", required = false, defaultValue = "false") boolean includesDeleted){
        return companyService.getCompanyById(id, includesDeleted);
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create company", notes = "This method is used to create a new company.", response = CompanyDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The company is created in the database"),
            @ApiResponse(code = 409, message = "The company already exists in database"),
            @ApiResponse(code = 400, message = "Error occurred on creation of company"),
    })
    public ResponseEntity<CompanyDTO> create(@Valid @RequestBody CompanyDTO companyDTO){
        CompanyDTO dto = companyService.create(companyDTO);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update company", notes = "This method is used to update an existing company.", response = CompanyDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The company is updated in the database"),
            @ApiResponse(code = 404, message = "The company doesn't exists in database"),
            @ApiResponse(code = 409, message = "The new company name already exists in database"),
            @ApiResponse(code = 400, message = "Error occurred on update of company"),
    })
    public ResponseEntity<CompanyDTO> update(@Valid @RequestBody CompanyDTO companyDTO){
        CompanyDTO dto =  companyService.update(companyDTO);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "Soft delete company", notes = "This method is used to soft delete an existing company.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The company is deleted"),
            @ApiResponse(code = 404, message = "The company doesn't exists in database"),
    })
    public ResponseEntity<Void> softDelete(@Valid @PathVariable Long id){
        companyService.softDeletionCompany(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
