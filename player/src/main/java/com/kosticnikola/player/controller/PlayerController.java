package com.kosticnikola.player.controller;

import com.kosticnikola.player.dto.CreatePlayerDTO;
import com.kosticnikola.player.dto.UpdatePlayerDTO;
import com.kosticnikola.player.service.PlayerService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/player")
@AllArgsConstructor
@Validated
public class PlayerController {

    private PlayerService playerService;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().body(this.playerService.getAll());
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful."),
            @ApiResponse(code = 404, message = "Invalid player ID."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(this.playerService.getById(id));
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful."),
            @ApiResponse(code = 400, message = "Invalid player name. (must be between 3 and 255 characters)"),
            @ApiResponse(code = 409, message = "Player with provided name already exists."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody CreatePlayerDTO createPlayerDTO) {
        return ResponseEntity.ok().body(this.playerService.create(createPlayerDTO));
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful."),
            @ApiResponse(code = 400, message = "Invalid player name. (must be between 3 and 255 characters)"),
            @ApiResponse(code = 404, message = "Invalid player ID."),
            @ApiResponse(code = 409, message = "Player with provided name already exists."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @PutMapping("")
    public ResponseEntity<?> update(@Valid @RequestBody UpdatePlayerDTO updatePlayerDTO) {
        return ResponseEntity.ok().body(this.playerService.update(updatePlayerDTO));
    }

    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successful."),
            @ApiResponse(code = 404, message = "Invalid player ID."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        this.playerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}







