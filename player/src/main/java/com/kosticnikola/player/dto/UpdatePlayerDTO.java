package com.kosticnikola.player.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class UpdatePlayerDTO {

    private final Long id;

    @NotBlank
    private final Long UPIN;

    @NotBlank
    @Size(min = 3, max = 255)
    private final String name;

    @NotBlank
    @Size(min = 3, max = 255)
    private final LocalDate dateOfBirth;

}
