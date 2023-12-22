package com.glady.challenge.controller;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.glady.challenge.service.gladyuser.GladyUserService;
import com.glady.challenge.web.dto.user.GladyUserDTO;
import com.glady.challenge.web.dto.user.GladyUserInfoDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

@RestController
@RequestMapping("glady-user")
@RequiredArgsConstructor
public class GladyUserController {

    private final GladyUserService gladyUserService;

    @GetMapping
    @JsonDeserialize(as = PageImpl.class)
    public Page<GladyUserDTO> getAll(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(defaultValue = "asc") String sortOrder){
        return gladyUserService.getAll(page, size, sortOrder);
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Get User", notes = "This method retrieve user's information", response = GladyUserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The information is retrieved."),
            @ApiResponse(code = 404, message = "The user doesn't exists in database."),
    })
    public GladyUserDTO getById(@PathVariable Long id){
        return gladyUserService.getDtoById(id);
    }

    @GetMapping(value = "{id}/info", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get User Info", notes = "This method retrieve information about user's balances.", response = GladyUserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The information is retrieved."),
            @ApiResponse(code = 404, message = "The user doesn't exists in database."),
    })
    public GladyUserInfoDTO getGladyUserInfo(@PathVariable Long id){
        return gladyUserService.getGladyUserInfo(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create User", notes = "This method is used to create new glady user.", response = GladyUserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The user is created."),
            @ApiResponse(code = 409, message = "The user already exists in database."),
    })
    public ResponseEntity<GladyUserDTO> create(@RequestBody GladyUserDTO gladyUserDTO){
        return new ResponseEntity<>(gladyUserService.create(gladyUserDTO), HttpStatus.CREATED);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update User", notes = "This method is used to update existing glady user.", response = GladyUserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The user is created."),
            @ApiResponse(code = 409, message = "The username already exists in database."),
            @ApiResponse(code = 404, message = "The user doesn't exists in database."),
    })
    public GladyUserDTO update(@RequestBody GladyUserDTO gladyUserDTO){
        return gladyUserService.update(gladyUserDTO);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id){
        gladyUserService.delete(id);
    }

}
