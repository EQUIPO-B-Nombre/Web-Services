package com.oncontigo.api.profile.domain.model.commands;

import java.time.LocalDate;

public record UpdateProfileCommand(Long id,
                                   String firstName,
                                   String lastName,
                                   String city,
                                   String country,
                                   LocalDate birthDate,
                                   String description,
                                   String photo,
                                   Integer experience,
                                   String dni,
                                   String phone) {
}
