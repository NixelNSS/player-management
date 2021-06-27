package com.kosticnikola.team.service;

import com.kosticnikola.team.entity.Team;
import com.kosticnikola.team.repository.TeamRepository;
import com.kosticnikola.team.dto.CreateTeamDTO;
import com.kosticnikola.team.dto.UpdateTeamDTO;
import com.kosticnikola.team.exception.InvalidIDException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TeamService {
    
    private TeamRepository teamRepository;
    private RestTemplate restTemplate;

    public List<Team> getAll() {
        return teamRepository.findAll();
    }

    public List<Team> getPlayerTeams(Long playerId) {
        try {
            ResponseEntity<Long[]> result = restTemplate.getForEntity(
                    "http://transfer/api/transfer/teams/" + playerId,
                    Long[].class
            );
            return teamRepository.findAllById(Arrays.asList(result.getBody()));
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND))
                throw new InvalidIDException();
            else
                throw new RuntimeException();
        }
    }

    public void checkIfTeamsExist(List<Long> ids) {
        ids.forEach(
                id -> teamRepository.findById(id).orElseThrow(InvalidIDException::new)
        );
    }
    
    public Team create(CreateTeamDTO teamDTO) {
        return teamRepository.save(new Team(teamDTO.getName()));
    }

    public Team update(UpdateTeamDTO teamDTO) {
        Optional<Team> optionalTeam = teamRepository.findById(teamDTO.getId());
        if (optionalTeam.isPresent()) {
            optionalTeam.get().setName(teamDTO.getName());
            return teamRepository.save(optionalTeam.get());
        }
        throw new InvalidIDException();
    }

    public void deleteById(Long id) {
        Optional<Team> optionalTeam = teamRepository.findById(id);
        if (!optionalTeam.isPresent())
            throw new InvalidIDException();
        teamRepository.deleteById(id);
    }

}










