package com.kosticnikola.team.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class CreateTeamDTO {

    @NotBlank
    @Size(min = 3, max = 255)
    private String name;

}
