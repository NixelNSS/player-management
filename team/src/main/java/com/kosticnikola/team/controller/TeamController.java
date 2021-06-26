package com.kosticnikola.team.controller;

import com.kosticnikola.team.exception.InvalidIDException;
import com.kosticnikola.team.service.TeamService;
import com.kosticnikola.team.dto.CreateTeamDTO;
import com.kosticnikola.team.dto.UpdateTeamDTO;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/team")
@AllArgsConstructor
@Validated
public class TeamController {

    private TeamService teamService;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok().body(this.teamService.getAll());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful."),
            @ApiResponse(code = 404, message = "Invalid team ID."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @GetMapping("player/{playerId}")
    public ResponseEntity<?> getPlayerTeams(@PathVariable("playerId") Long playerId) {
        try {
            return ResponseEntity.ok().body(this.teamService.getPlayerTeams(playerId));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successful."),
            @ApiResponse(code = 404, message = "Invalid team ID."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @PostMapping("exist")
    public ResponseEntity<?> checkIfTeamsExist(@RequestBody List<Long> ids) {
        try {
            this.teamService.checkIfTeamsExist(ids);
            return ResponseEntity.noContent().build();
        } catch (InvalidIDException e) {
            return ResponseEntity.notFound().build();
        }  catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful."),
            @ApiResponse(code = 400, message = "Invalid team name. (must be between 3 and 255 characters)"),
            @ApiResponse(code = 409, message = "Team with provided name already exists."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody CreateTeamDTO createTeamDTO) {
        try {
            return ResponseEntity.ok().body(this.teamService.create(createTeamDTO));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful."),
            @ApiResponse(code = 400, message = "Invalid team name. (must be between 3 and 255 characters)"),
            @ApiResponse(code = 404, message = "Invalid team ID."),
            @ApiResponse(code = 409, message = "Team with provided name already exists."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @PutMapping("")
    public ResponseEntity<?> update(@Valid @RequestBody UpdateTeamDTO updateTeamDTO) {
        try {
            return ResponseEntity.ok().body(this.teamService.update(updateTeamDTO));
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
            @ApiResponse(code = 404, message = "Invalid team ID."),
            @ApiResponse(code = 500, message = "Internal server error.")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        try {
            this.teamService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (InvalidIDException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}







