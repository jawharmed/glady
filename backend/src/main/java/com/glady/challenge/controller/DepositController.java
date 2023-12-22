package com.glady.challenge.controller;

import com.glady.challenge.exception.GladyException;
import com.glady.challenge.service.deposit.DepositService;
import com.glady.challenge.web.dto.deposit.DepositDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("deposit")
@RequiredArgsConstructor
public class DepositController {

    private final DepositService depositService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Make deposit", notes = "This method is used distribute vouchers to users.", response = DepositDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The deposit is created and vouchers are distributed"),
            @ApiResponse(code = 400, message = "The company balance is insufficient."),
            @ApiResponse(code = 400, message = "Error occurred while making deposit"),
    })
    public DepositDTO makeDeposit(@RequestBody DepositDTO depositDTO) throws GladyException {
        return depositService.makeDeposit(depositDTO);
    }
}
