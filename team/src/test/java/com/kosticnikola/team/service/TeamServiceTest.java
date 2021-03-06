package com.kosticnikola.team.service;

import com.kosticnikola.team.dto.CreateTeamDTO;
import com.kosticnikola.team.dto.UpdateTeamDTO;
import com.kosticnikola.team.entity.Team;
import com.kosticnikola.team.exception.InvalidIDException;
import com.kosticnikola.team.repository.TeamRepository;
import com.kosticnikola.team.restclient.TransferClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class TeamServiceTest {

    List<Team> teams;

    @Mock
    TeamRepository teamRepository;

    @Mock
    TransferClient transferClient;

    @InjectMocks
    TeamService teamService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    void setUpTeams() {
        teams = Arrays.asList(
                new Team(1L, "Liverpool"),
                new Team(2L, "Chelsea"),
                new Team(3L, "Brighton"),
                new Team(4L, "Barcelona")
        );
    }

    @Test
    void getAll_ShouldReturnAListOfTeams() {
        setUpTeams();
        Mockito.when(teamRepository.findAll()).thenReturn(teams);
        Assertions.assertEquals(teams, teamService.getAll());
    }

    @Test
    void getPlayerTeams_ShouldReturnAListOfTeams_IfATransferClientReturnedAListOfIds() {
        setUpTeams();
        Mockito.when(transferClient.getPlayerTeams(Mockito.anyLong())).thenReturn(new ArrayList<>());
        Mockito.when(teamRepository.findAllById(Mockito.any())).thenReturn(teams);

        Assertions.assertEquals(teams, teamService.getPlayerTeams(1L));
    }

    @Test
    void checkIfTeamsExist_ShouldCallTeamRepositoryFindByIdSizeOfTheProvidedListTimes() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L, 4L);
        Team t = new Team(1L, "Chelsea");
        Mockito.when(teamRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(t));
        teamService.checkIfTeamsExist(ids);

        Mockito.verify(teamRepository, Mockito.times(ids.size())).findById(Mockito.anyLong());
    }

    @Test
    void checkIfTeamsExist_ShouldThrowAnInvalidIDException_IfTeamRepositoryReturnedAnEmptyOptional() {
        Mockito.when(teamRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(
                InvalidIDException.class,
                () -> teamService.checkIfTeamsExist(Arrays.asList(1L, 2L))
        );
    }

    @Test
    void create_ShouldReturnATeam_IfTeamRepositoryReturnedATeam() {
        Team t = new Team(1L, "Chelsea");
        Mockito.when(teamRepository.save(Mockito.any(Team.class)))
                .thenReturn(t);
        Assertions.assertEquals(t, teamService.create(new CreateTeamDTO("Chelsea")));
    }

    @Test
    void update_ShouldReturnATeam_IfTeamRepositoryReturnedATeam() {
        Team t = new Team(1L, "Chelsea");
        Mockito.when(teamRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(t));
        Mockito.when(teamRepository.save(Mockito.any(Team.class)))
                .thenReturn(t);
        Assertions.assertEquals(t, teamService.update(new UpdateTeamDTO(1L, "Chelsea")));
    }

    @Test
    void update_ShouldThrowAnInvalidIDException_IfTeamRepositoryReturnedAnEmptyOptional() {
        Mockito.when(teamRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(
                InvalidIDException.class,
                () -> teamService.update(new UpdateTeamDTO(1L, "Chelsea"))
        );
    }

    @Test
    void deleteById_ShouldCallTeamRepositoryDeleteById_IfTeamRepositoryReturnedATeam() {
        Team t = new Team(1L, "Chelsea");
        Mockito.when(teamRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(t));
        teamService.deleteById(Mockito.anyLong());

        Mockito.verify(teamRepository).deleteById(Mockito.anyLong());
    }

    @Test
    void deleteById_ShouldThrowAnInvalidIDException_IfTeamRepositoryReturnedAnEmptyOptional() {
        Mockito.when(teamRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(
                InvalidIDException.class,
                () -> teamService.deleteById(Mockito.anyLong())
        );
    }

}