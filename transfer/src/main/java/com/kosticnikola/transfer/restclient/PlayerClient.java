package com.kosticnikola.transfer.restclient;

import com.kosticnikola.transfer.dto.PlayerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "player")
public interface PlayerClient {

    @GetMapping("api/player/{playerId}")
    PlayerDTO getPlayerTeams(@PathVariable Long playerId);

}
