package com.kosticnikola.transfer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransferDTO {

    @NotNull
    private Long playerId;

    @NotNull
    private Long newTeamId;

    @NotNull
    @Min(1)
    @Max(10)
    private Integer commission;

}
