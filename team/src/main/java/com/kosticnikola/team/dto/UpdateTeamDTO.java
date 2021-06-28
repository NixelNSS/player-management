package com.kosticnikola.team.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTeamDTO {

    @NotNull
    private Long id;

    @NotBlank
    @Size(min = 3, max = 255)
    private String name;

}
