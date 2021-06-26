package com.kosticnikola.transfer.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CreateTransferDTO {

    @NotNull
    private final Long playerId;

    @NotNull
    private final Long newTeamId;

    @NotNull
    @Min(1)
    @Max(10)
    private final Integer commission;

}
