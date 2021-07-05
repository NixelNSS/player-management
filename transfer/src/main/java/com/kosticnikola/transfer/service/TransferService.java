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
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransferService {
    
    private TransferRepository transferRepository;
    private RestTemplate restTemplate;

    public Set<Long> getAllTeamIdsByPlayerId(Long playerId) {
        getPlayer(playerId);
        List<Transfer> transfers = transferRepository.findAllByPlayerId(playerId);
        if (transfers.isEmpty()) return new HashSet<>();

        return transfers.stream()
                .map(Transfer::getNewTeamId)
                .collect(Collectors.toSet());
    }
    
    public Transfer create(CreateTransferDTO transferDTO) {
        PlayerDTO playerDTO = getPlayer(transferDTO.getPlayerId());
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
                "http://team/api/team/exist",
                Arrays.asList(transferDTO.getNewTeamId(), oldTeamId),
                Void.class)
                    .getStatusCode()
                    .equals(HttpStatus.NO_CONTENT))
            throw new InvalidIDException();

        long months = 0;
        optionalTransfer = transferRepository.findFirstByPlayerIdOrderByCreatedAtAsc(playerDTO.getId());
        if (optionalTransfer.isPresent())
            months = ChronoUnit.MONTHS.between(optionalTransfer.get().getCreatedAt().toLocalDateTime(), LocalDateTime.now());

        int period = Period.between(playerDTO.getDateOfBirth(), LocalDate.now()).getYears();
        if (period == 0) period = 1;
        double transferFee = months * 100000 / period;
        double contractFee = transferFee + transferFee * transferDTO.getCommission() / 100;

        return transferRepository.save(new Transfer(
                Timestamp.valueOf(LocalDateTime.now()),
                playerDTO.getId(),
                oldTeamId,
                transferDTO.getNewTeamId(),
                contractFee
        ));
    }

    private PlayerDTO getPlayer(Long playerId) {
        ResponseEntity<PlayerDTO> result = restTemplate.getForEntity(
                "http://player/api/player/" + playerId,
                PlayerDTO.class
        );
        return result.getBody();
    }

}










