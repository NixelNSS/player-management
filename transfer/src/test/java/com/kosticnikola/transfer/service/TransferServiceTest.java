package com.kosticnikola.transfer.service;

import com.kosticnikola.transfer.dto.CreateTransferDTO;
import com.kosticnikola.transfer.dto.PlayerDTO;
import com.kosticnikola.transfer.entity.Transfer;
import com.kosticnikola.transfer.exception.InvalidIDException;
import com.kosticnikola.transfer.repository.TransferRepository;
import com.kosticnikola.transfer.restclient.PlayerClient;
import com.kosticnikola.transfer.restclient.TeamClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

class TransferServiceTest {

    @Mock
    TransferRepository transferRepository;

    @Mock
    PlayerClient playerClient;

    @Mock
    TeamClient teamClient;

    @InjectMocks
    TransferService transferService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTeamIdsByPlayerId_ShouldReturnAnEmptySet_IfTransferRepositoryReturnedAnEmptyList() {
        PlayerDTO dto = new PlayerDTO(1L, 123L, "Peter", LocalDate.now());
        Mockito.when(playerClient.getPlayerTeams(Mockito.anyLong())).thenReturn(dto);
        Mockito.when(transferRepository.findAllByPlayerId(Mockito.anyLong()))
                .thenReturn(new ArrayList<>());

        Assertions.assertEquals(
                0,
                transferService.getAllTeamIdsByPlayerId(1L).size()
        );
    }

    @Test
    void getAllTeamIdsByPlayerId_ShouldReturnASet_IfTransferRepositoryReturnedAList() {
        List<Transfer> list = Arrays.asList(
                new Transfer(1L, 1L),
                new Transfer(1L, 2L),
                new Transfer(2L, 3L),
                new Transfer(3L, 1L)
        );
        PlayerDTO dto = new PlayerDTO(1L, 123L, "Peter", LocalDate.now());
        Mockito.when(playerClient.getPlayerTeams(Mockito.anyLong())).thenReturn(dto);
        Mockito.when(transferRepository.findAllByPlayerId(Mockito.anyLong()))
                .thenReturn(list);

        Assertions.assertEquals(
                (int) list.stream().map(Transfer::getNewTeamId).distinct().count(),
                transferService.getAllTeamIdsByPlayerId(1L).size()
        );
    }

    @Test
    void create_ShouldThrowAnInvalidIDException_IfTheLastTransferNewTeamIdEqualsProvidedNewTeamId() {
        PlayerDTO dto = new PlayerDTO(1L, 123L, "Peter", LocalDate.now());
        Mockito.when(playerClient.getPlayerTeams(Mockito.anyLong())).thenReturn(dto);
        Mockito.when(transferRepository.findFirstByPlayerIdOrderByCreatedAtDesc(Mockito.anyLong()))
                .thenReturn(Optional.of(new Transfer(1L, 1L)));

        Assertions.assertThrows(
                InvalidIDException.class,
                () -> transferService.create(new CreateTransferDTO(1L, 1L, 5))
        );
    }

    @Test
    void create_ShouldSaveATransferObjectWithContractFeeEquals0_IfTransferRepositoryFindFirstAscMethodReturnedAnEmptyOptional() {
        PlayerDTO dto = new PlayerDTO(1L, 123L, "Peter", LocalDate.now());
        Mockito.when(playerClient.getPlayerTeams(Mockito.anyLong())).thenReturn(dto);
        Mockito.when(transferRepository.findFirstByPlayerIdOrderByCreatedAtDesc(Mockito.anyLong()))
                .thenReturn(Optional.of(new Transfer(Long.MAX_VALUE, Long.MAX_VALUE)));
        Mockito.when(teamClient.checkIfTeamsExist(Mockito.anyList())).thenReturn(new Object());
        Mockito.when(transferRepository.findFirstByPlayerIdOrderByCreatedAtAsc(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        transferService.create(new CreateTransferDTO(1L, 1L, 5));

        ArgumentCaptor<Transfer> argument = ArgumentCaptor.forClass(Transfer.class);
        Mockito.verify(transferRepository).save(argument.capture());
        Assertions.assertEquals(0, argument.getValue().getContractFee());
    }

    @Test
    void create_ShouldSaveATransferObjectWithOldTeamIdEqualsNewTeamId_IfTransferRepositoryFindFirstByPlayerIdOrderByCreatedAtDescReturnedAnEmptyOptional() {
        PlayerDTO dto = new PlayerDTO(1L, 123L, "Peter", LocalDate.now());
        Mockito.when(playerClient.getPlayerTeams(Mockito.anyLong())).thenReturn(dto);
        Mockito.when(transferRepository.findFirstByPlayerIdOrderByCreatedAtDesc(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        Mockito.when(teamClient.checkIfTeamsExist(Mockito.anyList())).thenReturn(new Object());
        Mockito.when(transferRepository.findFirstByPlayerIdOrderByCreatedAtAsc(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        transferService.create(new CreateTransferDTO(1L, 1L, 5));

        ArgumentCaptor<Transfer> argument = ArgumentCaptor.forClass(Transfer.class);
        Mockito.verify(transferRepository).save(argument.capture());
        Assertions.assertEquals(1L, argument.getValue().getOldTeamId());
    }

    @Test
    void create_ShouldSaveATransferObject_IfTransferRepositoryFindFirstAscMethodDidNotReturnAnEmptyOptional() {
        PlayerDTO dto = new PlayerDTO(1L, 123L, "Peter", LocalDate.now());
        Transfer t = new Transfer(
                Timestamp.valueOf(LocalDateTime.now().minus(5, ChronoUnit.YEARS)),
                1L,
                1L,
                1L,
                10000D
        );
        Mockito.when(playerClient.getPlayerTeams(Mockito.anyLong())).thenReturn(dto);
        Mockito.when(transferRepository.findFirstByPlayerIdOrderByCreatedAtDesc(Mockito.anyLong()))
                .thenReturn(Optional.of(new Transfer(Long.MAX_VALUE, Long.MAX_VALUE)));
        Mockito.when(teamClient.checkIfTeamsExist(Mockito.anyList())).thenReturn(new Object());
        Mockito.when(transferRepository.findFirstByPlayerIdOrderByCreatedAtAsc(Mockito.anyLong()))
                .thenReturn(Optional.of(t));

        transferService.create(new CreateTransferDTO(1L, 1L, 5));

        double transferFee = 12 * 5 * 100000 / 1;
        double contractFee = transferFee + transferFee * 5 / 100;
        ArgumentCaptor<Transfer> argument = ArgumentCaptor.forClass(Transfer.class);
        Mockito.verify(transferRepository).save(argument.capture());
        Assertions.assertEquals(contractFee, argument.getValue().getContractFee());
    }

    @Test
    void create_ShouldReturnATransferObject_IfTransferRepositoryReturnedATransferObject() {
        PlayerDTO dto = new PlayerDTO(1L, 123L, "Peter", LocalDate.now());
        Transfer t = new Transfer(
                Timestamp.valueOf(LocalDateTime.now()),
                1L,
                1L,
                1L,
                0D
        );
        Mockito.when(playerClient.getPlayerTeams(Mockito.anyLong())).thenReturn(dto);
        Mockito.when(transferRepository.findFirstByPlayerIdOrderByCreatedAtDesc(Mockito.anyLong()))
                .thenReturn(Optional.of(new Transfer(Long.MAX_VALUE, Long.MAX_VALUE)));
        Mockito.when(teamClient.checkIfTeamsExist(Mockito.anyList())).thenReturn(new Object());
        Mockito.when(transferRepository.findFirstByPlayerIdOrderByCreatedAtAsc(Mockito.anyLong()))
                .thenReturn(Optional.of(t));
        Mockito.when(transferRepository.save(Mockito.any(Transfer.class)))
                .thenReturn(t);

        Assertions.assertEquals(
                t,
                transferService.create(new CreateTransferDTO(1L, 1L, 5))
        );
    }

}










