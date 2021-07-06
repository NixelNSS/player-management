package com.kosticnikola.team.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosticnikola.team.dto.CreateTeamDTO;
import com.kosticnikola.team.dto.UpdateTeamDTO;
import com.kosticnikola.team.entity.Team;
import com.kosticnikola.team.exception.APIExceptionHandler;
import com.kosticnikola.team.exception.InvalidIDException;
import com.kosticnikola.team.service.TeamService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TeamControllerTest {

    List<Team> teams;

    MockMvc mockMvc;

    @Mock
    TeamService teamService;

    @InjectMocks
    TeamController teamController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(teamController)
                .setControllerAdvice(new APIExceptionHandler())
                .build();
    }

    void setUpTeams() {
         teams = Arrays.asList(
                new Team(1L, "Liverpool"),
                new Team(2L, "Chelsea"),
                new Team(3L, "Brighton"),
                new Team(4L, "Barcelona")
        );
    }

    static String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAll_ShouldReturnAListOfTeamsInResponseBodyAndAStatusCode200_IfTeamServiceReturnedAListOfTeams() throws Exception {
        setUpTeams();
        Mockito.when(teamService.getAll()).thenReturn(teams);
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/team"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Assertions.assertEquals(asJsonString(teams), result.getResponse().getContentAsString());
    }

    @Test
    void getAll_ShouldReturnAStatusCode500_IfARuntimeExceptionIsThrown() throws Exception {
        Mockito.when(teamService.getAll()).thenThrow(RuntimeException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/team"))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    void getPlayerTeams_ShouldReturnAListOfTeamsInResponseBodyAndAStatusCode200_IfTeamServiceReturnedAListOfTeams() throws Exception {
        setUpTeams();
        Mockito.when(teamService.getPlayerTeams(Mockito.anyLong())).thenReturn(teams);
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/team/player/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Assertions.assertEquals(asJsonString(teams), result.getResponse().getContentAsString());
    }

    @Test
    void getPlayerTeams_ShouldReturnAStatusCode400_IfAnInvalidIDExceptionIsThrown() throws Exception {
        Mockito.when(teamService.getPlayerTeams(Mockito.anyLong())).thenThrow(InvalidIDException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/team/player/1"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void getPlayerTeams_ShouldReturnAStatusCode500_IfARuntimeExceptionIsThrown() throws Exception {
        Mockito.when(teamService.getPlayerTeams(Mockito.anyLong())).thenThrow(RuntimeException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/team/player/1"))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    void checkIfTeamsExist_ShouldReturnAStatusCode204() throws Exception {
        Mockito.doNothing().when(teamService).checkIfTeamsExist(Mockito.anyList());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/team/exist")
                .content(asJsonString(Arrays.asList(1L, 2L, 3L)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void checkIfTeamsExist_ShouldReturnAStatusCode400_IfProvidedListIsNull() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/team/exist")
                .content(asJsonString(null))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void checkIfTeamsExist_ShouldReturnAStatusCode400_IfListIsNotProvided() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/team/exist"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void checkIfTeamsExist_ShouldReturnAStatusCode400_IfAnInvalidIDExceptionIsThrown() throws Exception {
        Mockito.doThrow(InvalidIDException.class).when(teamService).checkIfTeamsExist(Mockito.anyList());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/team/exist")
                .content(asJsonString(Arrays.asList(1L, 2L, 3L)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void checkIfTeamsExist_ShouldReturnAStatusCode500_IfARuntimeExceptionIsThrown() throws Exception {
        Mockito.doThrow(RuntimeException.class).when(teamService).checkIfTeamsExist(Mockito.anyList());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/team/exist")
                .content(asJsonString(Arrays.asList(1L, 2L, 3L)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    void create_ShouldReturnATeamInResponseBodyAndAStatusCode200_IfTeamServiceReturnedATeam() throws Exception {
        Mockito.when(teamService.create(Mockito.any(CreateTeamDTO.class)))
                .thenReturn(new Team(1L, "Real Madrid"));
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/team")
                        .content(asJsonString(new CreateTeamDTO("Real Madrid")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertEquals(
                asJsonString(new Team(1L, "Real Madrid")),
                result.getResponse().getContentAsString()
        );
    }

    @Test
    void create_ShouldReturnAStatusCode409_IfADataIntegrityViolationExceptionIsThrown() throws Exception {
        Mockito.when(teamService.create(Mockito.any(CreateTeamDTO.class)))
                .thenThrow(DataIntegrityViolationException.class);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/team")
                        .content(asJsonString(new CreateTeamDTO("Real Madrid")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn();
    }

    @Test
    void create_ShouldReturnAStatusCode500_IfARuntimeExceptionIsThrown() throws Exception {
        Mockito.when(teamService.create(Mockito.any(CreateTeamDTO.class)))
                .thenThrow(RuntimeException.class);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/team")
                        .content(asJsonString(new CreateTeamDTO("Real Madrid")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    void create_ShouldReturnAStatusCode400_IfProvidedNameIsLessThan3CharactersLong() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/team")
                        .content(asJsonString(new CreateTeamDTO("EE")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void create_ShouldReturnAStatusCode400_IfProvidedNameIsMoreThan255CharactersLong() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/team")
                        .content(asJsonString(new CreateTeamDTO("OKwSlulPrhxweja5EhCXqWlOfgUYUM5gJ1Y6dMDBCCY5YUyFoRJ3OWeQcK0mQa5yDBwL3hnt21Jqo41OcCs4cEO5ZRvCWqBGidcc9F1qYirAMHoAxvy2SfKUXeUMNw0bc3VpR8lngh1PnYY2ORgRdqWmVd8WEmEWEOR2ui6CHqwdGGamgBmcWitwAuFbkTIIbnPpCoQ1M1to4dhGFbOLIEXUg47CWdm6XgM5sD7KVD67rV5JZirkZeuwB4INAl3e")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void create_ShouldReturnAStatusCode400_IfProvidedNameIsNull() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/team")
                        .content(asJsonString(new CreateTeamDTO(null)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void create_ShouldReturnAStatusCode400_IfNameIsNotProvided() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/team")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void update_ShouldReturnATeamInResponseBodyAndAStatusCode200_IfTeamServiceReturnedATeam() throws Exception {
        Mockito.when(teamService.update(Mockito.any(UpdateTeamDTO.class)))
                .thenReturn(new Team(1L, "Liverpool"));
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/team")
                        .content(asJsonString(new UpdateTeamDTO(1L, "Real Madrid")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertEquals(
                asJsonString(new Team(1L, "Liverpool")),
                result.getResponse().getContentAsString()
        );
    }

    @Test
    void update_ShouldReturnAStatusCode400_IfProvidedNameIsLessThan3CharactersLong() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/team")
                        .content(asJsonString(new UpdateTeamDTO(1L, "EE")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void update_ShouldReturnAStatusCode400_IfProvidedNameIsMoreThan255CharactersLong() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/team")
                        .content(asJsonString(new UpdateTeamDTO(1L, "OKwSlulPrhxweja5EhCXqWlOfgUYUM5gJ1Y6dMDBCCY5YUyFoRJ3OWeQcK0mQa5yDBwL3hnt21Jqo41OcCs4cEO5ZRvCWqBGidcc9F1qYirAMHoAxvy2SfKUXeUMNw0bc3VpR8lngh1PnYY2ORgRdqWmVd8WEmEWEOR2ui6CHqwdGGamgBmcWitwAuFbkTIIbnPpCoQ1M1to4dhGFbOLIEXUg47CWdm6XgM5sD7KVD67rV5JZirkZeuwB4INAl3e")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void update_ShouldReturnAStatusCode400_IfProvidedIDIsNull() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/team")
                        .content(asJsonString(new UpdateTeamDTO(null, "Real Madrid")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void update_ShouldReturnAStatusCode400_IfProvidedNameIsNull() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/team")
                        .content(asJsonString(new UpdateTeamDTO(1L, null)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void update_ShouldReturnAStatusCode400_IfIDIsNotProvided() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/team")
                        .content(asJsonString(new CreateTeamDTO("Real Madrid")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void update_ShouldReturnAStatusCode400_IfNameIsNotProvided() throws Exception {
        UpdateTeamDTO dto = new UpdateTeamDTO();
        dto.setId(1L);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/team")
                        .content(asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void update_ShouldReturnAStatusCode400_IfAnInvalidIDExceptionIsThrown() throws Exception {
        Mockito.when(teamService.update(Mockito.any(UpdateTeamDTO.class)))
                .thenThrow(InvalidIDException.class);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/team")
                        .content(asJsonString(new UpdateTeamDTO(1L, "Real Madrid")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void update_ShouldReturnAStatusCode409_IfADataIntegrityViolationExceptionIsThrown() throws Exception {
        Mockito.when(teamService.update(Mockito.any(UpdateTeamDTO.class)))
                .thenThrow(DataIntegrityViolationException.class);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/team")
                        .content(asJsonString(new UpdateTeamDTO(1L, "Real Madrid")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn();
    }

    @Test
    void update_ShouldReturnAStatusCode500_IfARuntimeExceptionIsThrown() throws Exception {
        Mockito.when(teamService.update(Mockito.any(UpdateTeamDTO.class)))
                .thenThrow(RuntimeException.class);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/team")
                        .content(asJsonString(new UpdateTeamDTO(1L, "Real Madrid")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    void deleteById_ShouldReturnAStatusCode204() throws Exception{
        Mockito.doNothing().when(teamService).deleteById(Mockito.anyLong());
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/team/1"))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void deleteById_ShouldReturnAStatusCode400_IfAnInvalidIDExceptionIsThrown() throws Exception{
        Mockito.doThrow(InvalidIDException.class).when(teamService).deleteById(Mockito.anyLong());
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/team/1"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void deleteById_ShouldReturnAStatusCode500_IfARuntimeExceptionIsThrown() throws Exception{
        Mockito.doThrow(RuntimeException.class).when(teamService).deleteById(Mockito.anyLong());
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/team/1"))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

}