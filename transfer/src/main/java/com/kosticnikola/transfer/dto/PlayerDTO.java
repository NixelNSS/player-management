package com.kosticnikola.transfer.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PlayerDTO {
    private Long id, UPIN;
    private String name;
    private LocalDate dateOfBirth;
}
