package com.kosticnikola.player.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class CreatePlayerDTO {

    @NotBlank
    private final Long UPIN;

    @NotBlank
    @Size(min = 3, max = 255)
    private final String name;

    @NotBlank
    @Size(min = 3, max = 255)
    private final LocalDate dateOfBirth;

}
