package com.kosticnikola.transfer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosticnikola.transfer.dto.CreateTransferDTO;
import com.kosticnikola.transfer.entity.Transfer;
import com.kosticnikola.transfer.exception.APIExceptionHandler;
import com.kosticnikola.transfer.exception.InvalidIDException;
import com.kosticnikola.transfer.service.TransferService;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TransferControllerTest {

    MockMvc mockMvc;

    @Mock
    TransferService transferService;

    @InjectMocks
    TransferController transferController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(transferController)
                .setControllerAdvice(new APIExceptionHandler())
                .build();
    }

    static String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAll_ShouldReturnALongSetAndAStatusCode200_IfTransferServiceReturnedALongSet() throws Exception{
        Set<Long> ids = new HashSet<>(Arrays.asList(1L, 2L, 3L));
        Mockito.when(transferService.getAllTeamIdsByPlayerId(Mockito.anyLong()))
                .thenReturn(ids);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/transfer/teams/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Assertions.assertEquals(asJsonString(ids), result.getResponse().getContentAsString());
    }

    @Test
    void getAll_ShouldReturnAStatusCode400_IfAnInvalidIDExceptionIsThrown() throws Exception{
        Mockito.when(transferService.getAllTeamIdsByPlayerId(Mockito.anyLong()))
                .thenThrow(InvalidIDException.class);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/transfer/teams/1"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void getAll_ShouldReturnAStatusCode500_IfARuntimeExceptionIsThrown() throws Exception{
        Mockito.when(transferService.getAllTeamIdsByPlayerId(Mockito.anyLong()))
                .thenThrow(RuntimeException.class);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/transfer/teams/1"))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    void create_ShouldReturnATransferObjectAndAStatusCode200_IfTransferServiceReturnedATransferObject() throws Exception {
        Transfer t = new Transfer(
                Timestamp.valueOf(LocalDateTime.now()),
                1L,
                1L,
                1L,
                0D
        );
        Mockito.when(transferService.create(Mockito.any(CreateTransferDTO.class)))
                .thenReturn(t);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/transfer")
                .content(asJsonString(new CreateTransferDTO(1L, 1L, 5)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Assertions.assertEquals(asJsonString(t), result.getResponse().getContentAsString());
    }

    @Test
    void create_ShouldReturnAStatusCode400_IfAPlayerIdIsNotProvided() throws Exception {
        JSONObject object = new JSONObject();
        object.put("newTeamId", 1L);
        object.put("commission", 5);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/transfer")
                .content(object.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void create_ShouldReturnAStatusCode400_IfANewTeamIdIsNotProvided() throws Exception {
        JSONObject object = new JSONObject();
        object.put("playerId", 1L);
        object.put("commission", 5);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/transfer")
                        .content(object.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void create_ShouldReturnAStatusCode400_IfACommissionIsNotProvided() throws Exception {
        JSONObject object = new JSONObject();
        object.put("playerId", 1L);
        object.put("newTeamId", 1L);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/transfer")
                        .content(object.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void create_ShouldReturnAStatusCode400_IfAPlayerIdIsNull() throws Exception {
        CreateTransferDTO dto = CreateTransferDTO.builder()
                .newTeamId(1L).commission(5).build();
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/transfer")
                        .content(asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void create_ShouldReturnAStatusCode400_IfANewTeamIdIsNull() throws Exception {
        CreateTransferDTO dto = CreateTransferDTO.builder()
                .playerId(1L).commission(5).build();
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/transfer")
                        .content(asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void create_ShouldReturnAStatusCode400_IfACommissionIsNull() throws Exception {
        CreateTransferDTO dto = CreateTransferDTO.builder()
                .playerId(1L).newTeamId(1L).build();
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/transfer")
                        .content(asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void create_ShouldReturnAStatusCode400_IfACommissionIsLessThan1() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/transfer")
                .content(asJsonString(new CreateTransferDTO(1L, 1L, 0)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void create_ShouldReturnAStatusCode400_IfACommissionIsGreaterThan10() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/transfer")
                        .content(asJsonString(new CreateTransferDTO(1L, 1L, 11)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void create_ShouldReturnAStatusCode400_IfAnInvalidIDExceptionIsThrown() throws Exception {
        Mockito.when(transferService.create(Mockito.any(CreateTransferDTO.class)))
                .thenThrow(InvalidIDException.class);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/transfer")
                        .content(asJsonString(new CreateTransferDTO(1L, 1L, 5)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void create_ShouldReturnAStatusCode500_IfARuntimeExceptionIsThrown() throws Exception {
        Mockito.when(transferService.create(Mockito.any(CreateTransferDTO.class)))
                .thenThrow(RuntimeException.class);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/transfer")
                        .content(asJsonString(new CreateTransferDTO(1L, 1L, 5)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

}