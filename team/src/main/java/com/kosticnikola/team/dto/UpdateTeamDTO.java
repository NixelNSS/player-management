package com.kosticnikola.team.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UpdateTeamDTO {

    @NotNull
    private final Long id;

    @NotBlank
    @Size(min = 3, max = 255)
    private final String name;

}
