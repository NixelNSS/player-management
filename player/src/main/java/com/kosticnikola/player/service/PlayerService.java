package com.kosticnikola.player.service;

import com.kosticnikola.player.dto.CreatePlayerDTO;
import com.kosticnikola.player.dto.UpdatePlayerDTO;
import com.kosticnikola.player.entity.Player;
import com.kosticnikola.player.exception.InvalidIDException;
import com.kosticnikola.player.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PlayerService {
    
    private PlayerRepository playerRepository;

    public List<Player> getAll() {
        return playerRepository.findAll();
    }

    public Player getById(Long id) {
        return playerRepository.findById(id).orElseThrow(InvalidIDException::new);
    }
    
    public Player create(CreatePlayerDTO playerDTO) {
        return playerRepository.save(new Player(
                playerDTO.getUpin(),
                playerDTO.getName(),
                playerDTO.getDateOfBirth()
        ));
    }

    public Player update(UpdatePlayerDTO playerDTO) {
        Optional<Player> optionalPlayer = playerRepository.findById(playerDTO.getId());
        if (optionalPlayer.isPresent()) {
            optionalPlayer.get().setUPIN(playerDTO.getUpin());
            optionalPlayer.get().setName(playerDTO.getName());
            optionalPlayer.get().setDateOfBirth(playerDTO.getDateOfBirth());
            return playerRepository.save(optionalPlayer.get());
        }
        throw new InvalidIDException();
    }

    public void deleteById(Long id) {
        Optional<Player> optionalPlayer = playerRepository.findById(id);
        if (optionalPlayer.isPresent())
            playerRepository.deleteById(id);
        throw new InvalidIDException();
    }

}










