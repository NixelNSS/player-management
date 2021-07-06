package com.kosticnikola.team.restclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "transfer")
public interface TransferClient {

    @GetMapping("api/transfer/teams/{playerId}")
    List<Long> getPlayerTeams(@PathVariable Long playerId);

}
