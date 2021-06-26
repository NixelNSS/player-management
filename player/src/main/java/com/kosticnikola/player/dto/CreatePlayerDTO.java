package com.kosticnikola.player.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class CreatePlayerDTO {

    @NotNull
    private final Long upin;

    @NotBlank
    @Size(min = 3, max = 255)
    private final String name;

    @NotNull
    private final LocalDate dateOfBirth;

}
