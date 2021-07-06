package com.kosticnikola.transfer.restclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "team")
public interface TeamClient {

    @PostMapping("api/team/exist")
    Object checkIfTeamsExist(@RequestBody List<Long> list);

}
