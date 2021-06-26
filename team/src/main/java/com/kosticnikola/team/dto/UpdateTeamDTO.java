package com.kosticnikola.team.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UpdateTeamDTO {

    private final Long id;

    @NotBlank
    @Size(min = 3, max = 255)
    private final String name;

}
