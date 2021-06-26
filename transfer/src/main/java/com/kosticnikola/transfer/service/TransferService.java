package com.kosticnikola.transfer.service;

import com.kosticnikola.transfer.dto.CreateTransferDTO;
import com.kosticnikola.transfer.dto.PlayerDTO;
import com.kosticnikola.transfer.entity.Transfer;
import com.kosticnikola.transfer.exception.InvalidIDException;
import com.kosticnikola.transfer.repository.TransferRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransferService {
    
    private TransferRepository transferRepository;
    private RestTemplate restTemplate;

    public Set<Long> getAllTeamIdsByPlayerId(Long playerId) {
        List<Transfer> transfers = transferRepository.findAllByPlayerId(playerId);
        Set<Long> teamIds = transfers
                .stream()
                .map(Transfer::getOldTeamId)
                .collect(Collectors.toSet());
        teamIds.add(transfers.get(transfers.size() - 1).getNewTeamId());
        return teamIds;
    }
    
    public Transfer create(CreateTransferDTO transferDTO) {
        PlayerDTO playerDTO;
        try {
            ResponseEntity<PlayerDTO> result = restTemplate.getForEntity(
                    "http://localhost:8083/api/player/" + transferDTO.getPlayerId(),
                    PlayerDTO.class
            );
            playerDTO = result.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND))
                throw new InvalidIDException();
            else
                throw new RuntimeException();
        }
        
        long oldTeamId;
        Optional<Transfer> optionalTransfer = transferRepository.findFirstByPlayerIdOrderByCreatedAtDesc(playerDTO.getId());
        if (optionalTransfer.isPresent()) {
            if (optionalTransfer.get().getNewTeamId().equals(transferDTO.getNewTeamId()))
                throw new InvalidIDException();
            oldTeamId = optionalTransfer.get().getNewTeamId();
        }
        else
            oldTeamId = transferDTO.getNewTeamId();

        if (!restTemplate.postForEntity(
                "http://localhost:8082/api/team/exist",
                Arrays.asList(transferDTO.getNewTeamId(), oldTeamId),
                Void.class)
                    .getStatusCode()
                    .equals(HttpStatus.NO_CONTENT))
            throw new InvalidIDException();

        long months = 0;
        optionalTransfer = transferRepository.findFirstByPlayerIdOrderByCreatedAtAsc(playerDTO.getId());
        if (optionalTransfer.isPresent())
            months = ChronoUnit.MONTHS.between(optionalTransfer.get().getCreatedAt().toLocalDateTime(), LocalDateTime.now());

        double transferFee = months * 100000 / Period.between(playerDTO.getDateOfBirth(), LocalDate.now()).getYears();
        double contractFee = transferFee + transferFee * transferDTO.getCommission() / 100;

        return transferRepository.save(new Transfer(
                Timestamp.valueOf(LocalDateTime.now()),
                playerDTO.getId(),
                oldTeamId,
                transferDTO.getNewTeamId(),
                contractFee
        ));
    }

}










