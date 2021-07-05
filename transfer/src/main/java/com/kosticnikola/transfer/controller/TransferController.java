package com.kosticnikola.transfer.controller;

import com.kosticnikola.transfer.dto.CreateTransferDTO;
import com.kosticnikola.transfer.service.TransferService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/transfer")
@AllArgsConstructor
@Validated
public class TransferController {

    private TransferService transferService;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful."),
            @ApiResponse(code = 400, message = "Invalid id."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @GetMapping("teams/{playerId}")
    public ResponseEntity<?> getAll(@PathVariable("playerId") Long playerId) {
        return ResponseEntity.ok().body(this.transferService.getAllTeamIdsByPlayerId(playerId));
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful."),
            @ApiResponse(code = 400, message = "Invalid id(s) or commission (must be between 1 and 10)."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody CreateTransferDTO createTransferDTO) {
        return ResponseEntity.ok().body(this.transferService.create(createTransferDTO));
    }

}







