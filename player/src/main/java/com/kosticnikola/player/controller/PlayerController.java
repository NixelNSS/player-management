package com.kosticnikola.player.controller;

import com.kosticnikola.player.dto.CreatePlayerDTO;
import com.kosticnikola.player.dto.UpdatePlayerDTO;
import com.kosticnikola.player.exception.InvalidIDException;
import com.kosticnikola.player.service.PlayerService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
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
        try {
            return ResponseEntity.ok().body(this.playerService.getAll());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful."),
            @ApiResponse(code = 400, message = "Invalid player name. (must be between 3 and 255 characters)"),
            @ApiResponse(code = 409, message = "Player with provided name already exists."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody CreatePlayerDTO createPlayerDTO) {
        try {
            return ResponseEntity.ok().body(this.playerService.create(createPlayerDTO));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
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
        try {
            return ResponseEntity.ok().body(this.playerService.update(updatePlayerDTO));
        } catch (InvalidIDException e) {
            return ResponseEntity.notFound().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successful."),
            @ApiResponse(code = 404, message = "Invalid player ID."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        try {
            this.playerService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (InvalidIDException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}







